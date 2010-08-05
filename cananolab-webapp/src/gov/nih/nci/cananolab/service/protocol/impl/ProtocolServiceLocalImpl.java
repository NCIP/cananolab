package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
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
public class ProtocolServiceLocalImpl extends BaseServiceLocalImpl implements
		ProtocolService {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceLocalImpl.class);
	private ProtocolServiceHelper helper;
	private FileServiceLocalImpl fileService;

	public ProtocolServiceLocalImpl() {
		super();
		helper = new ProtocolServiceHelper(this.securityService);
		fileService = new FileServiceLocalImpl(this.securityService);
	}

	public ProtocolServiceLocalImpl(UserBean user) {
		super(user);
		helper = new ProtocolServiceHelper(this.securityService);
		fileService = new FileServiceLocalImpl(this.securityService);
	}

	public ProtocolServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new ProtocolServiceHelper(this.securityService);
		fileService = new FileServiceLocalImpl(this.securityService);
	}

	public ProtocolBean findProtocolById(String protocolId)
			throws ProtocolException, NoAccessException {
		ProtocolBean protocolBean = null;
		try {
			Protocol protocol = helper.findProtocolById(protocolId);
			if (protocol != null) {
				protocolBean = loadProtocolBean(protocol);
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

	private ProtocolBean loadProtocolBean(Protocol protocol) throws Exception {
		ProtocolBean protocolBean = new ProtocolBean(protocol);
		if (user != null) {
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(protocol.getId().toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(protocol.getId().toString());

			protocolBean.setUserAccesses(userAccesses);
			protocolBean.setGroupAccesses(groupAccesses);
		}
		return protocolBean;
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 *
	 * @param protocolBean
	 * @throws Exception
	 */
	public void saveProtocol(ProtocolBean protocolBean)
			throws ProtocolException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			Boolean newProtocol = true;
			if (protocolBean.getDomain().getId() != null) {
				newProtocol = false;
				if (!securityService.checkCreatePermission(protocolBean
						.getDomain().getId().toString())) {
					throw new NoAccessException();
				}
			}
			if (protocolBean.getFileBean() != null) {
				fileService.prepareSaveFile(protocolBean.getFileBean()
						.getDomainFile());
			}
			Protocol dbProtocol = helper.findProtocolBy(protocolBean
					.getDomain().getType(), protocolBean.getDomain().getName(),
					protocolBean.getDomain().getVersion());
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
				protocolBean.getDomain().setCreatedBy(
						helper.getUser().getLoginName());
				protocolBean.getDomain().setCreatedDate(new Date());
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			appService.saveOrUpdate(protocolBean.getDomain());

			// save to the file system fileData is not empty
			if (protocolBean.getFileBean() != null) {
				fileService.writeFile(protocolBean.getFileBean());
			}

			// save default accesses
			if (newProtocol) {
				super.saveDefaultAccessibility(protocolBean.getDomain().getId()
						.toString());
			}
		} catch (Exception e) {
			String err = "Error in saving the protocol file.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public ProtocolBean findProtocolBy(String protocolType,
			String protocolName, String protocolVersion)
			throws ProtocolException, NoAccessException {
		try {
			Protocol protocol = helper.findProtocolBy(protocolType,
					protocolName, protocolVersion);
			if (protocol != null) {
				ProtocolBean protocolBean = loadProtocolBean(protocol);
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
			String protocolName, String protocolAbbreviation, String fileTitle)
			throws ProtocolException {
		List<ProtocolBean> protocolBeans = new ArrayList<ProtocolBean>();
		try {
			List<Protocol> protocols = helper.findProtocolsBy(protocolType,
					protocolName, protocolAbbreviation, fileTitle);
			Collections.sort(protocols,
					new Comparators.ProtocolNameVersionComparator());
			for (Protocol protocol : protocols) {
				// don't need to load accessibility
				ProtocolBean protocolBean = new ProtocolBean(protocol);
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

	public void deleteProtocol(Protocol protocol) throws ProtocolException,
			NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}

		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// assume protocol is loaded with protocol file
			// find associated characterizations
			// List<Long> charIds = findCharacterizationIdsByProtocolId(protocol
			// .getId().toString());
			// CharacterizationServiceHelper charServiceHelper = new
			// CharacterizationServiceHelper();
			// for (Long id : charIds) {
			// Characterization achar = charServiceHelper
			// .findCharacterizationById(id.toString());
			// achar.setProtocol(null);
			// appService.saveOrUpdate(achar);
			// }
			List<Characterization> chars = this
					.findCharacterizationsByProtocolId(protocol.getId()
							.toString());
			for (Characterization achar : chars) {
				achar.setProtocol(null);
				appService.saveOrUpdate(achar);
			}
			appService.delete(protocol);
		} catch (Exception e) {
			String err = "Error in deleting the protocol.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	private List<Long> findCharacterizationIdsByProtocolId(String protocolId)
			throws Exception {
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
			ids.add(charId);
		}
		return ids;
	}

	private List<Characterization> findCharacterizationsByProtocolId(
			String protocolId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(Characterization.class);
		crit.createAlias("protocol", "protocol");
		crit.add(Property.forName("protocol.id").eq(new Long(protocolId)));
		List results = appService.query(crit);
		List<Characterization> chars = new ArrayList<Characterization>();
		for (Object obj : results) {
			Characterization achar = (Characterization) obj;
			chars.add(achar);
		}
		return chars;
	}

	public void assignAccessibility(AccessibilityBean access, Protocol protocol)
			throws ProtocolException, NoAccessException {
		if (!isUserOwner(protocol.getCreatedBy())) {
			throw new NoAccessException();
		}
		try {
			// get existing accessibilities
			List<AccessibilityBean> groupAccesses = this
					.findGroupAccessibilities(protocol.getId().toString());
			List<AccessibilityBean> userAccesses = this
					.findUserAccessibilities(protocol.getId().toString());
			// do nothing is access already exist
			if (groupAccesses.contains(access)) {
				return;
			} else if (userAccesses.contains(access)) {
				return;
			}

			// if access is Public, remove all other access except Public
			// Curator and owner
			if (access.getGroupName().equals(Constants.CSM_PUBLIC_GROUP)) {
				for (AccessibilityBean acc : groupAccesses) {
					// remove group accesses that are not public or curator
					if (!acc.getGroupName().equals(Constants.CSM_PUBLIC_GROUP)
							&& !acc.getGroupName().equals(
									(Constants.CSM_DATA_CURATOR))) {
						this.removeAccessibility(acc, protocol);
					}
				}
				for (AccessibilityBean acc : userAccesses) {
					// remove accesses that are not owner
					if (!acc.getUserBean().getLoginName().equals(
							protocol.getCreatedBy())) {
						this.removeAccessibility(acc, protocol);
					}
				}
			}
			// if protocol is already public, retract from public
			else {
				if (groupAccesses.contains(Constants.CSM_PUBLIC_ACCESS)) {
					this.removeAccessibility(Constants.CSM_PUBLIC_ACCESS,
							protocol);
				}
			}

			super.saveAccessibility(access, protocol.getId().toString());
			if (protocol.getFile() != null) {
				super.saveAccessibility(access, protocol.getFile().getId()
						.toString());
			}
		} catch (Exception e) {
			String error = "Error in assigning access to protocol";
			throw new ProtocolException(error, e);
		}
	}

	public void removeAccessibility(AccessibilityBean access, Protocol protocol)
			throws ProtocolException, NoAccessException {
		if (!isUserOwner(protocol.getCreatedBy())) {
			throw new NoAccessException();
		}
		try {
			if (protocol != null) {
				super.deleteAccessibility(access, protocol.getId().toString());
				if (protocol.getFile() != null) {
					super.deleteAccessibility(access, protocol.getFile()
							.getId().toString());
				}
			}
		} catch (Exception e) {
			String error = "Error in assigning access to protocol";
			throw new ProtocolException(error, e);
		}
	}

	public ProtocolServiceHelper getHelper() {
		return helper;
	}
}
