/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.SecuredDataBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.dao.AclDao;
import gov.nih.nci.cananolab.security.enums.AccessTypeEnum;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Local implementation of ProtocolService
 * 
 * @author pansu
 * 
 */
@Component("protocolService")
public class ProtocolServiceLocalImpl extends BaseServiceLocalImpl implements ProtocolService
{
	private static Logger logger = Logger.getLogger(ProtocolServiceLocalImpl.class);
	
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private ProtocolServiceHelper protocolServiceHelper;
	
	@Autowired
	private AclDao aclDao;

	public ProtocolBean findProtocolById(String protocolId) throws ProtocolException, NoAccessException
	{
		ProtocolBean protocolBean = null;
		try {
			Protocol protocol = protocolServiceHelper.findProtocolById(protocolId);
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
	
	public ProtocolBean findWorkspaceProtocolById(String protocolId) throws ProtocolException, NoAccessException
	{
		ProtocolBean protocolBean = null;
		try {
			Protocol protocol = protocolServiceHelper.findProtocolById(protocolId);
			if (protocol != null) {
				protocolBean = loadProtocolBean(protocol, false);
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
		if (SpringSecurityUtil.getPrincipal() != null) {
			springSecurityAclService.loadAccessControlInfoForObject(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz(), protocolBean);
		}
		return protocolBean;
	}
	
	private ProtocolBean loadProtocolBean(Protocol protocol, boolean checkReadPermission) throws Exception
	{
		ProtocolBean protocolBean = new ProtocolBean(protocol);
		if (SpringSecurityUtil.getPrincipal() != null) {
			if (checkReadPermission)
			{
				if (springSecurityAclService.currentUserHasReadPermission(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz()))
					springSecurityAclService.loadAccessControlInfoForObject(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz(), protocolBean);
				else
					throw new NoAccessException();
			}
			
		}
		return protocolBean;
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 * 
	 * @param protocolBean
	 * @throws Exception
	 */
	public void saveProtocol(ProtocolBean protocolBean) throws ProtocolException, NoAccessException 
	{
		if (SpringSecurityUtil.getPrincipal() == null) {
			throw new NoAccessException();
		}
		try {
			Boolean newProtocol = true;

			// save to the file system fileData is not empty
			if (protocolBean.getFileBean() != null) {
				fileUtils.writeFile(protocolBean.getFileBean());
			}
			if (protocolBean.getDomain().getId() != null) {
				newProtocol = false;
				if (!springSecurityAclService.currentUserHasWritePermission(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz())) {
					throw new NoAccessException();
				}

				Protocol dbProtocol = null;
				// confirm if the record in the database exists
				dbProtocol = protocolServiceHelper.findProtocolById(protocolBean.getDomain().getId().toString());
				if (dbProtocol != null) {
					// reuse existing createdBy and createdDate
					protocolBean.getDomain().setCreatedBy(dbProtocol.getCreatedBy());
					protocolBean.getDomain().setCreatedDate(dbProtocol.getCreatedDate());
				}
				// the given ID is invalid, create a new one
				else {
					newProtocol = true;
				}
			}
			if (newProtocol) {
				protocolBean.getDomain().setId(null);
				protocolBean.getDomain().setCreatedBy(SpringSecurityUtil.getLoggedInUserName());
				protocolBean.getDomain().setCreatedDate(new Date());
			}
			if (protocolBean.getFileBean() != null) {
				fileUtils.prepareSaveFile(protocolBean.getFileBean().getDomainFile());
			}
			
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
			appService.saveOrUpdate(protocolBean.getDomain());

			// save default accesses
			if (newProtocol)
			{
				springSecurityAclService.saveDefaultAccessForNewObject(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz());
				/*if (protocolBean.getFileBean() != null)
				{
					springSecurityAclService.saveAccessForChildObject(protocolBean.getDomain().getId(), 
																	SecureClassesEnum.PROTOCOL.getClazz(), 
																	protocolBean.getFileBean().getDomainFile().getId(), 
																	SecureClassesEnum.FILE.getClazz());
				}*/
			}
		} catch (Exception e) {
			String err = "Error in saving the protocol file.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public ProtocolBean findProtocolBy(String protocolType, String protocolName, String protocolVersion) throws ProtocolException, NoAccessException
	{
		try {
			Protocol protocol = protocolServiceHelper.findProtocolBy(protocolType, protocolName, protocolVersion);
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
			List<Protocol> protocols = protocolServiceHelper.findProtocolsBy(protocolType, protocolName, protocolAbbreviation, fileTitle);
			Collections.sort(protocols, new Comparators.ProtocolDateComparator());
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
			int count = protocolServiceHelper.getNumberOfPublicProtocols();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public int getNumberOfPublicProtocolsForJob() throws ProtocolException {
		try {
			int count = protocolServiceHelper.getNumberOfPublicProtocolsForJob();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public void deleteProtocol(Protocol protocol) throws ProtocolException, NoAccessException
	{
		if (SpringSecurityUtil.getPrincipal() == null) {
			throw new NoAccessException();
		}

		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
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
			List<Characterization> chars = this.findCharacterizationsByProtocolId(protocol.getId().toString());
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

	private List<Long> findCharacterizationIdsByProtocolId(String protocolId) throws Exception
	{
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Characterization.class).setProjection(Projections.distinct(Property.forName("id")));
		crit.createAlias("protocol", "protocol");
		crit.add(Property.forName("protocol.id").eq(new Long(protocolId)));
		List results = appService.query(crit);
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i< results.size(); i++) {
			Long charId = (Long) results.get(i);
			ids.add(charId);
		}
		return ids;
	}

	private List<Characterization> findCharacterizationsByProtocolId(String protocolId) throws Exception
	{
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Characterization.class);
		crit.createAlias("protocol", "protocol");
		crit.add(Property.forName("protocol.id").eq(new Long(protocolId)));
		List results = appService.query(crit);
		List<Characterization> chars = new ArrayList<Characterization>();
		for (int i = 0; i< results.size(); i++) {
			Characterization achar = (Characterization) results.get(i);
			chars.add(achar);
		}
		return chars;
	}

	public void assignAccessibility(AccessControlInfo access, Protocol protocol) throws ProtocolException, NoAccessException
	{
		if (!springSecurityAclService.isOwnerOfObject(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz())) {
			throw new NoAccessException();
		}
		try
		{
			// if access is Public, remove all other access except Public
			// Curator and owner
			// if access is Public, remove all other access except Public, Curator and owner
			if (CaNanoRoleEnum.ROLE_ANONYMOUS.getRoleName().equalsIgnoreCase(access.getRecipient()))
			{
				springSecurityAclService.deleteAllAccessExceptPublicAndDefault(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz());
			}
			// if sample is already public, retract from public
			else
			{
				springSecurityAclService.retractObjectFromPublic(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz());
			}

			springSecurityAclService.saveAccessForObject(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz(), access.getRecipient(), 
														 access.isPrincipal(), access.getRoleName());

			/*if (protocol.getFile() != null) {
				springSecurityAclService.saveAccessForChildObject(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz(), 
																  protocol.getFile().getId(), SecureClassesEnum.FILE.getClazz());
			}*/
		} catch (Exception e) {
			String error = "Error in assigning access to protocol";
			throw new ProtocolException(error, e);
		}
	}

	public void removeAccessibility(AccessControlInfo access, Protocol protocol) throws ProtocolException, NoAccessException
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		if (userDetails != null && (userDetails.isCurator() ||
			springSecurityAclService.isOwnerOfObject(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz())))
		{
			throw new NoAccessException();
		}
		try {
			if (protocol != null)
			{
				String sid = access.getRecipient();
				
				springSecurityAclService.retractAccessToObjectForSid(protocol.getId(), SecureClassesEnum.PROTOCOL.getClazz(), sid);
				
				//File object access automatically updated due to inheritance set up
			}
		} catch (Exception e) {
			String error = "Error in assigning access to protocol";
			throw new ProtocolException(error, e);
		}
	}

	public List<String> findProtocolIdsByOwner(String currentOwner) throws ProtocolException
	{
		List<String> protocolIds = new ArrayList<String>();
		try {
			protocolIds = protocolServiceHelper.findProtocolIdsByOwner(currentOwner);
		} catch (Exception e) {
			String error = "Error in retrieving protocolIds by owner";
			throw new ProtocolException(error, e);
		}
		return protocolIds;
	}
	
	@Override
	public List<String> findProtocolIdsSharedWithUser(CananoUserDetails userDetails) throws ProtocolException
	{
		List<String> protocolIds = new ArrayList<String>();
		try
		{
			List<String> sharedWithSids = new ArrayList<String>(userDetails.getGroups());
			sharedWithSids.add(userDetails.getUsername());
			protocolIds = aclDao.getIdsOfClassSharedWithSid(SecureClassesEnum.PROTOCOL, userDetails.getUsername(), sharedWithSids);
		}catch (Exception e) {
			String error = "Error in retrieving protocolIds shared with logged in user. " + e.getMessage();
			throw new ProtocolException(error, e);
		}
		return protocolIds;
	}
	
	public List<ProtocolBean> getProtocolsByChar(HttpServletRequest request, String characterizationType) throws Exception {
		String protocolType = null;
		if (characterizationType
				.equals(Constants.PHYSICOCHEMICAL_CHARACTERIZATION)) {
			protocolType = Constants.PHYSICOCHEMICAL_ASSAY_PROTOCOL;
		} else if (characterizationType.equals(Constants.INVITRO_CHARACTERIZATION)) {
			protocolType = Constants.INVITRO_ASSAY_PROTOCOL;
		} else {
			protocolType = null; // update if in vivo is implemented
		}
		List<ProtocolBean> protocols = findProtocolsBy(protocolType, null, null, null);
		request.getSession().setAttribute("characterizationProtocols", protocols);
		return protocols;
	}

	@Override
	public ProtocolServiceHelper getHelper() {
		return protocolServiceHelper;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

}
