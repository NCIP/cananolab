package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

/**
 * Local implementation of ProtocolService
 *
 * @author pansu
 *
 */
public class ProtocolServiceLocalImpl implements ProtocolService {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceLocalImpl.class);
	private ProtocolServiceHelper helper = new ProtocolServiceHelper();

	public ProtocolBean findProtocolById(String protocolId, UserBean user)
			throws ProtocolException, NoAccessException {
		ProtocolBean protocolBean = null;
		try {
			Protocol protocol = helper.findProtocolById(protocolId, user);
			if (protocol != null) {
				protocolBean = new ProtocolBean(protocol);
				if (user != null)
					protocolBean.setVisibilityGroups(helper
							.retrieveVisibility(protocol));
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the protocol by id: " + protocolId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
		return protocolBean;
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 *
	 * @param protocolBean
	 * @throws Exception
	 */
	public void saveProtocol(ProtocolBean protocolBean, UserBean user)
			throws ProtocolException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			FileService fileService = new FileServiceLocalImpl();

			if (protocolBean.getFileBean() != null) {
				fileService.prepareSaveFile(protocolBean.getFileBean()
						.getDomainFile(), user);
			}
			Protocol dbProtocol = helper.findProtocolBy(protocolBean
					.getDomain().getType(), protocolBean.getDomain().getName(),
					protocolBean.getDomain().getVersion(), user);
			if (dbProtocol != null) {
				if (dbProtocol.getId() != protocolBean.getDomain().getId()) {
					protocolBean.getDomain().setId(dbProtocol.getId());
				}
				protocolBean.getDomain()
						.setCreatedBy(dbProtocol.getCreatedBy());
				protocolBean.getDomain().setCreatedDate(
						dbProtocol.getCreatedDate());
			}
			// protocol type, name or version has been updated but protocol ID
			// was kept
			else if (protocolBean.getDomain().getId() != null) {
				protocolBean.getDomain().setId(null);
				protocolBean.getDomain().setCreatedBy(user.getLoginName());
				protocolBean.getDomain().setCreatedDate(new Date());
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			appService.saveOrUpdate(protocolBean.getDomain());

			// save to the file system fileData is not empty
			if (protocolBean.getFileBean() != null) {
				fileService.writeFile(protocolBean.getFileBean(), user);
			}

			// set visibility
			assignVisibility(protocolBean.getDomain(), protocolBean
					.getVisibilityGroups());
		} catch (Exception e) {
			String err = "Error in saving the protocol file.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public ProtocolBean findProtocolBy(String protocolType,
			String protocolName, String protocolVersion, UserBean user)
			throws ProtocolException, NoAccessException {
		try {
			Protocol protocol = helper.findProtocolBy(protocolType,
					protocolName, protocolVersion, user);
			if (protocol != null) {
				ProtocolBean protocolBean = new ProtocolBean(protocol);
				if (user != null)
					protocolBean.setVisibilityGroups(helper
							.retrieveVisibility(protocol));
				return protocolBean;
			} else {
				return null;
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding protocol by name and type.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle,
			UserBean user) throws ProtocolException {
		List<ProtocolBean> protocolBeans = new ArrayList<ProtocolBean>();
		try {
			List<Protocol> protocols = helper.findProtocolsBy(protocolType,
					protocolName, protocolAbbreviation, fileTitle, user);

			for (Protocol protocol : protocols) {
				ProtocolBean protocolBean = new ProtocolBean(protocol);
				if (user != null)
					protocolBean.setVisibilityGroups(helper
							.retrieveVisibility(protocol));
				protocolBeans.add(protocolBean);
			}
			return protocolBeans;
		} catch (Exception e) {
			String err = "Problem finding protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public int getNumberOfPublicProtocols() throws ProtocolException {
		try {
			int count = helper.getNumberOfPublicProtocols();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public List<String> deleteProtocol(Protocol protocol, UserBean user,
			Boolean removeVisibility) throws ProtocolException,
			NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// assume protocol is loaded with protocol file
			// find associated characterizations
			List<Long> charIds = findCharacterizationIdsByProtocolId(protocol
					.getId().toString(), user);
			CharacterizationServiceHelper charServiceHelper = new CharacterizationServiceHelper();
			for (Long id : charIds) {
				Characterization achar = charServiceHelper
						.findCharacterizationById(id.toString(), user);
				achar.setProtocol(null);
				appService.saveOrUpdate(achar);
			}
			appService.delete(protocol);
			entries.addAll(removeVisibility(protocol, removeVisibility));
		} catch (Exception e) {
			String err = "Error in deleting the protocol.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
		return entries;
	}

	private List<Long> findCharacterizationIdsByProtocolId(String protocolId,
			UserBean user) throws Exception {
		if (helper.getAuthService().checkReadPermission(user, protocolId)) {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					Characterization.class).setProjection(
					Projections.distinct(Property.forName("id")));
			crit.createAlias("protocol", "protocol");
			crit.add(Property.forName("protocol.id").eq(new Long(protocolId)));
			List results = appService.query(crit);
			List<Long> ids = new ArrayList<Long>();
			for (Object obj : results) {
				Long charId = (Long) obj;
				if (helper.getAuthService().checkReadPermission(user,
						charId.toString())) {
					ids.add(charId);
				} else {
					logger
							.debug("User doesn't have access to characterization "
									+ charId);
				}
			}
			return ids;
		} else {
			throw new NoAccessException(
					"User doesn't have acess to the protocol of id: "
							+ protocolId);
		}
	}

	private void assignVisibility(Protocol protocol, String[] visibilityGroups)
			throws Exception {
		helper.getAuthService().assignVisibility(protocol.getId().toString(),
				visibilityGroups, null);
		// set file visibility as well
		if (protocol.getFile() != null) {
			helper.getAuthService().assignVisibility(
					protocol.getFile().getId().toString(), visibilityGroups,
					null);
		}
	}

	private List<String> removeVisibility(Protocol protocol, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (protocol != null) {
			if (remove == null || remove)
				helper.getAuthService().removeCSMEntry(
						protocol.getId().toString());
			entries.add(protocol.getId().toString());
			if (protocol.getFile() != null) {
				if (remove == null || remove) {
					helper.getAuthService().removeCSMEntry(
							protocol.getFile().getId().toString());
				}
				entries.add(protocol.getFile().getId().toString());
			}
		}
		return entries;
	}

}
