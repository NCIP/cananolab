package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.exception.AdministrationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Service methods for transfer ownership.
 *
 * @author lethai, pansu
 */
public class OwnershipTransferServiceImpl implements OwnershipTransferService {

	private static Logger logger = Logger
			.getLogger(OwnershipTransferServiceImpl.class);

	private void transferOwner(SampleService sampleService,
			List<String> sampleIds, String currentOwner, String newOwner)
			throws AdministrationException, NoAccessException {
		SecurityService securityService = ((SampleServiceLocalImpl) sampleService)
				.getSecurityService();
		// user needs to be both curator and admin
		if (securityService.getUserBean().isCurator()
				&& securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			for (String sampleId : sampleIds) {
				Sample domain = sampleService.findSampleById(sampleId, false)
						.getDomain();
				domain.setCreatedBy(newOwner);
				SampleComposition sampleComposition = domain
						.getSampleComposition();
				Collection<ChemicalAssociation> chemicalAssociation = new ArrayList<ChemicalAssociation>();
				Collection<FunctionalizingEntity> functionalizingEntity = new ArrayList<FunctionalizingEntity>();
				Collection<NanomaterialEntity> nanomaterialEntity = new ArrayList<NanomaterialEntity>();
				Collection<Characterization> characterization = new ArrayList<Characterization>();
				List<AccessibilityBean> userAccesses = sampleService
						.findUserAccessibilities(sampleId);
				List<AccessibilityBean> groupAccesses = sampleService
						.findGroupAccessibilities(sampleId);
				appService.saveOrUpdate(domain);

				assignAndRemoveAccessForSample(currentOwner, newOwner, domain,
						userAccesses, groupAccesses, sampleService, securityService);
				if (sampleComposition != null) {
					chemicalAssociation = sampleComposition
							.getChemicalAssociationCollection();
					functionalizingEntity = sampleComposition
							.getFunctionalizingEntityCollection();
					nanomaterialEntity = sampleComposition
							.getNanomaterialEntityCollection();
					characterization = domain.getCharacterizationCollection();

					for (ChemicalAssociation ca : chemicalAssociation) {
						ca.setCreatedBy(newOwner);
						appService.saveOrUpdate(ca);
					}
					for (FunctionalizingEntity fe : functionalizingEntity) {
						fe.setCreatedBy(newOwner);
						appService.saveOrUpdate(fe);
					}
					for (NanomaterialEntity ne : nanomaterialEntity) {
						ne.setCreatedBy(newOwner);
						appService.saveOrUpdate(ne);
					}

					for (Characterization c : characterization) {
						c.setCreatedBy(newOwner);
						appService.saveOrUpdate(c);
					}
				}
			}
		} catch (Exception e) {
			String error = "Error transferring ownership for samples";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
	}

	// take care of accessibility when ownership has been transfered for Sample.
	private void assignAndRemoveAccessForSample(String currentOwner,
			String newOwner, Sample sample,
			List<AccessibilityBean> userAccesses,
			List<AccessibilityBean> groupAccesses, SampleService sampleService,
			SecurityService securityService)
			throws Exception {

		// load existing privilege for current owner
		// make a copy first
		// then modify the newly copy for the new owner
		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(
				userAccesses);

		// get UserBean for newOwner
		// if newOwner is a curator, don't need to do anything since he/she has
		// access to everything as a curator
		// if not a curator, copy role from currentOwner to newOwner if the
		// currentOwner userAcesses is empty.
		// this means the current owner belongs to collaborator group
		// if not a curator, currentOwner userAccesses is not empty, go through
		// each one, replace the
		// currentOwner userBean with the newOwner userBean
		List<UserBean> newUserBean = sampleService.findUserBeans(newOwner);
		UserBean newUser = getUserBean(newOwner, newUserBean);
		Boolean isNewOwnerCurator = securityService.isCurator(newOwner);
		// assigning accessibility to newOwner
		if (newUser == null) {
			logger.error("The new owner entered doesn't exist: " + newOwner);
			throw new Exception("The new owner entered doesn't exist. "
					+ newOwner);

		} else if (!isNewOwnerCurator) {
			if(newUserAccesses.isEmpty()){
				for(AccessibilityBean groupAccess : groupAccesses){
					AccessibilityBean newOwnerAccessibilityBean = 
						getUserAccessiblityBeanForGroupAccesses(groupAccess, newUser, currentOwner);					
					if(newOwnerAccessibilityBean != null){
						sampleService.assignAccessibility(newOwnerAccessibilityBean, sample);
					}
				}
			}else{
				for( AccessibilityBean newOwnerUser : newUserAccesses){
					AccessibilityBean newUserAccessibilityBean = getUserAccessiblityBeanForUserAccesses(newOwnerUser, newUser, currentOwner);
					if(newUserAccessibilityBean != null){ 
						sampleService.assignAccessibility(newUserAccessibilityBean, sample);
					}
				}				
			}
		}
		// need to remove access for the current owner
		// get currentOwner UserBean
		// if currentOwner is a curator, do not do any thing
		// if currentOwner is not curator
		// if currentOwner is a researcher, he/she will loose privilege to edit
		// the sample
		// this is done by removeAccessibility for the userAccess

		// removing accessibility for currentOwner
		List<UserBean> previousUserBean = sampleService
				.findUserBeans(currentOwner);
		UserBean previousUser = getUserBean(currentOwner, previousUserBean);
		Boolean isCurrentOwnerCurator = securityService.isCurator(currentOwner);
		if (previousUser == null) {
			logger.error("The current owner entered doesn't exist: "
					+ currentOwner);
			throw new Exception("The current owner entered doesn't exist. "
					+ currentOwner);
		} else if (!isCurrentOwnerCurator) {
			if(!userAccesses.isEmpty()){
				for( AccessibilityBean userAccess : userAccesses){
					AccessibilityBean userAccessibilityBean = getUserAccessiblityBeanForUserAccesses(userAccess, previousUser, currentOwner);
					if(userAccessibilityBean != null ){
						sampleService.removeAccessibility(userAccessibilityBean, sample);
					}
				}				
			}else{				
				for(AccessibilityBean groupAccess : groupAccesses){
					AccessibilityBean userAccessibilityBean = 
						getUserAccessiblityBeanForGroupAccesses(groupAccess, newUser, currentOwner);					
					if(userAccessibilityBean != null){					
						sampleService.removeAccessibility(userAccessibilityBean, sample);
					}
				}
			}			
		}
	}

	private void transferOwner(PublicationService publicationService,
			List<String> publicationIds, String currentOwner, String newOwner)
			throws AdministrationException, NoAccessException {
		SecurityService securityService = ((PublicationServiceLocalImpl) publicationService)
				.getSecurityService();
		// user needs to be both curator and admin
		if (securityService.getUserBean().isCurator()
				&& securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			PublicationServiceHelper helper = new PublicationServiceHelper(
					securityService);
			for (String publicationId : publicationIds) {
				Publication publication = helper
						.findPublicationById(publicationId);
				publication.setCreatedBy(newOwner);
				appService.saveOrUpdate(publication);

				assignAndRemoveAccessForPublication(publicationService,
						currentOwner, newOwner, publication, securityService);
			}
		} catch (Exception e) {
			String error = "Error transferring ownership for publications";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
	}

	private void assignAndRemoveAccessForPublication(
			PublicationService publicationService, String currentOwner,
			String newOwner, Publication publication, SecurityService securityService) throws Exception {
		String publicationId = publication.getId().toString();
		List<AccessibilityBean> userAccesses = publicationService
				.findUserAccessibilities(publicationId);
		List<AccessibilityBean> groupAccesses = publicationService
				.findGroupAccessibilities(publicationId);

		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(
				userAccesses);
	
		List<UserBean> newUserBean = publicationService.findUserBeans(newOwner);
		UserBean newUser = getUserBean(newOwner, newUserBean);
		Boolean isNewOwnerCurator = securityService.isCurator(newOwner);
		if (newUser == null) {
			logger.error("The new owner entered doesn't exist: " + newOwner);
			throw new Exception("The new owner entered doesn't exist. "
					+ newOwner);
		} else if (!isNewOwnerCurator) {
			if(newUserAccesses.isEmpty()){
				for(AccessibilityBean groupAccess : groupAccesses){
					AccessibilityBean newOwnerAccessibilityBean = 
						getUserAccessiblityBeanForGroupAccesses(groupAccess, newUser, currentOwner);					
					if(newOwnerAccessibilityBean != null){
						publicationService.assignAccessibility(newOwnerAccessibilityBean, publication);
					}
				}
			}else{
				for( AccessibilityBean newOwnerUser : newUserAccesses){
					AccessibilityBean newUserAccessibilityBean = getUserAccessiblityBeanForUserAccesses(newOwnerUser, newUser, currentOwner);
					if(newUserAccessibilityBean != null){ 
						publicationService.assignAccessibility(newUserAccessibilityBean, publication);
					}
				}				
			}
		}
		// need to remove access for the previous owner if not a curator
		List<UserBean> previousUserBean = publicationService
				.findUserBeans(currentOwner);
		UserBean previousUser = getUserBean(currentOwner, previousUserBean);
		Boolean isCurrentOwnerCurator = securityService.isCurator(currentOwner);
		if (previousUser == null) {
			logger.error("The current owner entered doesn't exist: "
					+ currentOwner);
			throw new Exception("The current owner entered doesn't exist. "
					+ currentOwner);
		} else if (!isCurrentOwnerCurator) {
			if(!userAccesses.isEmpty()){
				for( AccessibilityBean userAccess : userAccesses){
					AccessibilityBean userAccessibilityBean = getUserAccessiblityBeanForUserAccesses(userAccess, previousUser, currentOwner);
					if(userAccessibilityBean != null ){
						publicationService.removeAccessibility(userAccessibilityBean, publication);
					}
				}				
			}else{
				//AccessibilityBean previousOwnerBean = new AccessibilityBean();
				//previousOwnerBean.setAccessBy("");
				for(AccessibilityBean groupAccess : groupAccesses){
					AccessibilityBean userAccessibilityBean = getUserAccessiblityBeanForGroupAccesses(groupAccess, previousUser, currentOwner);
					if(userAccessibilityBean != null){
						publicationService.removeAccessibility(userAccessibilityBean, publication);
					}
				}
			}
		}
	}

	private void transferOwner(ProtocolService protocolService,
			List<String> protocolIds, String currentOwner, String newOwner)
			throws AdministrationException, NoAccessException {
		SecurityService securityService = ((ProtocolServiceLocalImpl) protocolService)
				.getSecurityService();
		// user needs to be both curator and admin
		// user needs to be both curator and admin
		if (securityService.getUserBean().isCurator()
				&& securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			ProtocolServiceHelper helper = new ProtocolServiceHelper(
					securityService);
			for (String protocolId : protocolIds) {
				Protocol protocol = helper.findProtocolById(protocolId);
				protocol.setCreatedBy(newOwner);
				appService.saveOrUpdate(protocol);
				assignAndRemoveAccessForProtocol(protocolService, currentOwner,
						newOwner, protocol, securityService);
			}
		} catch (Exception e) {
			String error = "Error transferring ownership for protocols";
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
	}

	private void assignAndRemoveAccessForProtocol(ProtocolService service,
			String currentOwner, String newOwner, Protocol protocol, SecurityService securityService)
			throws Exception {
		String protocolId = protocol.getId().toString();
		List<AccessibilityBean> userAccesses = service
				.findUserAccessibilities(protocolId);
		List<AccessibilityBean> groupAccesses = service
				.findGroupAccessibilities(protocolId);

		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(
				userAccesses);		
		List<UserBean> newUserBean = service.findUserBeans(newOwner);
		UserBean newUser = getUserBean(newOwner, newUserBean);
		Boolean isNewOwnerCurator = securityService.isCurator(newOwner);
		if (newUser == null) {
			logger.error("The new owner entered doesn't exist: " + newOwner);
			throw new Exception("The new owner entered doesn't exist. "
					+ newOwner);
		} else if (!isNewOwnerCurator) {
			if(newUserAccesses.isEmpty()){
				for(AccessibilityBean groupAccess : groupAccesses){
					AccessibilityBean newOwnerAccessibilityBean = 
						getUserAccessiblityBeanForGroupAccesses(groupAccess, newUser, currentOwner);					
					if(newOwnerAccessibilityBean != null){
						service.assignAccessibility(newOwnerAccessibilityBean, protocol);
					}
				}
			}else{
				for( AccessibilityBean newOwnerUser : newUserAccesses){
					AccessibilityBean newUserAccessibilityBean = getUserAccessiblityBeanForUserAccesses(newOwnerUser, newUser, currentOwner);
					if(newUserAccessibilityBean != null){ 
						service.assignAccessibility(newUserAccessibilityBean, protocol);
					}
				}				
			}
		}
		// need to remove access for the previous owner if not a curator
		List<UserBean> previousUserBean = service.findUserBeans(currentOwner);
		UserBean previousUser = getUserBean(currentOwner, previousUserBean);
		Boolean isCurrentOwnerCurator = securityService.isCurator(currentOwner);
		if (previousUser == null) {
			logger.error("The current owner entered doesn't exist: "
					+ currentOwner);
			throw new Exception("The current owner entered doesn't exist. "
					+ currentOwner);
		} else if (!isCurrentOwnerCurator) {
			if(!userAccesses.isEmpty()){
				for( AccessibilityBean userAccess : userAccesses){
					AccessibilityBean userAccessibilityBean = getUserAccessiblityBeanForUserAccesses(userAccess, previousUser, currentOwner);
					if(userAccessibilityBean != null ){
						service.removeAccessibility(userAccessibilityBean, protocol);
					}
				}				
			}else{
				//AccessibilityBean previousOwnerBean = new AccessibilityBean();
				//previousOwnerBean.setAccessBy("");
				for(AccessibilityBean groupAccess : groupAccesses){
					AccessibilityBean userAccessibilityBean = getUserAccessiblityBeanForGroupAccesses(groupAccess, previousUser, currentOwner);
					if(userAccessibilityBean != null){
						service.removeAccessibility(userAccessibilityBean, protocol);
					}
				}
				
			}
		}
	}

	private void transferOwner(CommunityService communityService,
			List<String> collaborationGroupIds, String currentOwner,
			String newOwner) throws AdministrationException, NoAccessException {
		SecurityService securityService = ((CommunityServiceLocalImpl) communityService)
				.getSecurityService();
		// user needs to be both curator and admin
		if (securityService.getUserBean().isCurator()
				&& securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}
		try {
			for (String id : collaborationGroupIds) {
				communityService.assignOwner(id, newOwner);
			}
		} catch (Exception e) {
			String error = "Error changing the collaboration group owner";
			throw new AdministrationException(error, e);
		}
	}

	public void transferOwner(SecurityService securityService,
			List<String> dataIds, String dataType, String currentOwner,
			String newOwner) throws AdministrationException, NoAccessException {
		try {
			BaseService service = null;
			if (dataType
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_SAMPLE)) {
				service = new SampleServiceLocalImpl(securityService);
			} else if (dataType
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PROTOCOL)) {
				service = new ProtocolServiceLocalImpl(securityService);
			} else if (dataType
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_PUBLICATION)) {
				service = new PublicationServiceLocalImpl(securityService);
			} else if (dataType
					.equalsIgnoreCase(OwnershipTransferService.DATA_TYPE_GROUP)) {
				service = new CommunityServiceLocalImpl(securityService);
			} else {
				throw new AdministrationException(
						"No such transfer data type is supported.");
			}
			this.transferOwner(service, dataIds, currentOwner, newOwner);
		} catch (Exception e) {
			String error = "Error in transfering ownership for type "
					+ dataType;
			logger.error(error, e);
			throw new AdministrationException(error, e);
		}
	}

	private AccessibilityBean getUserAccessiblityBeanForGroupAccesses(
			AccessibilityBean groupAccess, UserBean userBean,
			String ownerLoginName) {
		AccessibilityBean userAccessibilityBean = new AccessibilityBean();
		if(!groupAccess.getGroupName().equals(
				AccessibilityBean.CSM_PUBLIC_GROUP)){
			userAccessibilityBean.setAccessBy("");
			String role = groupAccess.getRoleName();
			userAccessibilityBean.setRoleName(role);
			userAccessibilityBean.setUserBean(userBean);	
			return userAccessibilityBean;
		}
		return null;
	}

	// replace the current owner userAccess userBean with new owner userBean
	// when the loginName match
	private AccessibilityBean getUserAccessiblityBeanForUserAccesses(
			AccessibilityBean userAccess, UserBean userBean,
			String ownerLoginName) {
		UserBean user = userAccess.getUserBean();		
		String loginName = user.getLoginName();
		if(ownerLoginName.endsWith(loginName)){				
			userAccess.setUserBean(userBean);
			return userAccess;
		}
		return null;
	}

	// find the userBean that match with the ownerLoginName
	private UserBean getUserBean(String ownerLoginName,
			List<UserBean> userBeanList) {
		UserBean userBean = null;
		if (userBeanList != null && !userBeanList.isEmpty()) {
			for (UserBean bean : userBeanList) {
				if (ownerLoginName.equals(bean.getLoginName())) {
					userBean = bean;
					break;
				}
			}
		}
		return userBean;
	}

	public void transferOwner(BaseService baseService, List<String> dataIds,
			String currentOwner, String newOwner)
			throws AdministrationException, NoAccessException {
		if (baseService instanceof SampleService) {
			SampleService sampleService = (SampleService) baseService;
			this.transferOwner(sampleService, dataIds, currentOwner, newOwner);
		} else if (baseService instanceof ProtocolService) {
			ProtocolService protocolService = (ProtocolService) baseService;
			this
					.transferOwner(protocolService, dataIds, currentOwner,
							newOwner);
		} else if (baseService instanceof PublicationService) {
			PublicationService publicationService = (PublicationService) baseService;
			this.transferOwner(publicationService, dataIds, currentOwner,
					newOwner);
		} else if (baseService instanceof CommunityService) {
			CommunityService communityService = (CommunityService) baseService;
			this.transferOwner(communityService, dataIds, currentOwner,
					newOwner);
		} else {
			throw new AdministrationException(
					"Not a supported service for transferring ownership");
		}
	}

}
