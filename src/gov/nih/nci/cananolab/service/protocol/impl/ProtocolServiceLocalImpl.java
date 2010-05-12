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
					helper.retrieveVisibility(protocolBean);
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
			helper.getAuthService().assignVisibility(
					protocolBean.getDomain().getId().toString(),
					protocolBean.getVisibilityGroups(), null);
			// set file visibility as well
			if (protocolBean.getFileBean() != null) {
				helper.getAuthService().assignVisibility(
						protocolBean.getFileBean().getDomainFile().getId()
								.toString(),
						protocolBean.getVisibilityGroups(), null);
			}
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
					helper.retrieveVisibility(protocolBean);
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
					helper.retrieveVisibility(protocolBean);
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
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List<String> publicData = appService.getAllPublicData();
			HQLCriteria crit = new HQLCriteria(
					"select id from gov.nih.nci.cananolab.domain.common.Protocol");
			List results = appService.query(crit);
			List<String> publicIds = new ArrayList<String>();
			for (Object obj : results) {
				String id = (String) obj.toString();
				if (StringUtils.containsIgnoreCase(publicData, id)) {
					publicIds.add(id);
				}
			}
			return publicIds.size();
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
			List<Long> charIds = helper.findCharacterizationIdsByProtocolId(
					protocol.getId().toString(), user);
			CharacterizationServiceHelper charServiceHelper = new CharacterizationServiceHelper();
			for (Long id : charIds) {
				Characterization achar = charServiceHelper
						.findCharacterizationById(id.toString(), user);
				achar.setProtocol(null);
				appService.saveOrUpdate(achar);
			}
			appService.delete(protocol);
			entries.addAll(helper.removeVisibility(protocol, removeVisibility));
		} catch (Exception e) {
			String err = "Error in deleting the protocol.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
		return entries;
	}
}
