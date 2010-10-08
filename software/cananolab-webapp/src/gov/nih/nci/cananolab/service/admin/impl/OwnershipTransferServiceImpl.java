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
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Service methods for transfer ownership.
 * 
 * @author lethai
 */
public class OwnershipTransferServiceImpl implements OwnershipTransferService {

	private static Logger logger = Logger
	.getLogger(OwnershipTransferServiceImpl.class);
	
	
	public void transferOwner(SampleService sampleService, SecurityService securityService,
			Set<String> sampleIds, String currentOwner, String newOwner)
			throws Exception {
		if (!securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}

		
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		for (String sampleId : sampleIds) {
			Sample domain = sampleService.findSampleById(sampleId, false).getDomain(); //helper.findSampleById(sampleId);
			domain.setCreatedBy(newOwner);
			SampleComposition sampleComposition = domain.getSampleComposition();
			Collection<ChemicalAssociation> chemicalAssociation = new ArrayList<ChemicalAssociation>();
			Collection<FunctionalizingEntity> functionalizingEntity = new ArrayList<FunctionalizingEntity>();
			Collection<NanomaterialEntity> nanomaterialEntity = new ArrayList<NanomaterialEntity>();
			;
			Collection<Characterization> characterization = new ArrayList<Characterization>();
			List<AccessibilityBean> userAccesses = sampleService.findUserAccessibilities(sampleId);
			List<AccessibilityBean> groupAccesses = sampleService.findGroupAccessibilities(sampleId);
			appService.saveOrUpdate(domain);
			
			assignAndRemoveAccessForSample(currentOwner, newOwner, domain, userAccesses, groupAccesses, 
					sampleService);
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
	}

	//take care of accessibility when ownership has been transfered for Sample.
	private void assignAndRemoveAccessForSample(String currentOwner, String newOwner, Sample sample,
			List<AccessibilityBean> userAccesses,List<AccessibilityBean> groupAccesses, 
			SampleService sampleService) throws Exception{
			
		
		// load existing privilege for current owner
		// make a copy first 
		// then modify the newly copy for the new owner
		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(userAccesses);
		
		// get UserBean for newOwner
		// if newOwner is a curator, don't need to do anything since he/she has access to everything as a curator
		// if not a curator, copy role from currentOwner to newOwner if the currentOwner userAcesses is empty. 
		// this means the current owner belongs to collaborator group
		// if not a curator, currentOwner userAccesses is not empty, go through each one, replace the 
		// currentOwner userBean with the newOwner userBean
		List<UserBean> newUserBean = sampleService.findUserBeans(newOwner);
		UserBean newUser = getUserBean(newOwner, newUserBean);
		//assigning accessibility to newOwner
		if(newUser == null){
			logger.error("The new owner entered doesn't exist: " + newOwner );
			throw new Exception("The new owner entered doesn't exist. " + newOwner);
			
		}else if(!newUser.isCurator()){
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
		// if currentOwner is a researcher, he/she will loose privilege to edit the sample
		// this is done by removeAccessibility for the userAccess
		
		// removing accessibility for currentOwner	
		List<UserBean> previousUserBean = sampleService.findUserBeans(currentOwner);
		UserBean previousUser = getUserBean(currentOwner, previousUserBean);
		if(previousUser == null){
			logger.error("The current owner entered doesn't exist: " + currentOwner );
			throw new Exception("The current owner entered doesn't exist. " + currentOwner);
		}else if(!previousUser.isCurator()){
			if(!userAccesses.isEmpty()){
				for( AccessibilityBean userAccess : userAccesses){
					AccessibilityBean userAccessibilityBean = getUserAccessiblityBeanForUserAccesses(userAccess, previousUser, currentOwner);
					if(userAccessibilityBean != null ){
						sampleService.removeAccessibility(userAccessibilityBean, sample);
					}
				}				
			}else{
				AccessibilityBean previousOwnerBean = new AccessibilityBean();
				for(AccessibilityBean groupAccess : groupAccesses){
					//System.out.println("group: " + groupAccess.getGroupName() + "\trole: " + groupAccess.getRoleName());
					UserBean user = groupAccess.getUserBean();
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){					
						String role = groupAccess.getRoleName();
						previousOwnerBean.setRoleName(role);
						previousOwnerBean.setUserBean(user);						
					}
				}
				if(previousOwnerBean.getUserBean() != null){
					sampleService.removeAccessibility(previousOwnerBean, sample);
				}
			}
		}
	}

	


	public void transferOwner(PublicationService publicationService, SecurityService securityService,
			Set<String> publicationIds, String currentOwner, String newOwner)
			throws Exception {
		if (!securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		PublicationServiceHelper helper = new PublicationServiceHelper(securityService);
		for (String publicationId : publicationIds) {
			Publication publication = helper.findPublicationById(publicationId);
			publication.setCreatedBy(newOwner);
			appService.saveOrUpdate(publication);
			
			assignAndRemoveAccessForPublication(publicationService,currentOwner, newOwner, publication);
		}
		
	}
	
	private void assignAndRemoveAccessForPublication( PublicationService publicationService, String currentOwner, String newOwner, 
			Publication publication)throws Exception{
		String publicationId = publication.getId().toString();
		List<AccessibilityBean> userAccesses = publicationService.findUserAccessibilities(publicationId);
		List<AccessibilityBean> groupAccesses = publicationService.findGroupAccessibilities(publicationId);
		
		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(userAccesses);
		// save the new user access
		//need to retrieve new user info
		//In another words, if user accesses return empty on the previous owner, we’d search for group accesses and 
		//copy the roles from the group accesses and generate new user accesses with the same roles for the new owner.
		List<UserBean> newUserBean = publicationService.findUserBeans(newOwner);
		UserBean newUser=getUserBean(newOwner, newUserBean);
		
		if(newUser == null){
			logger.error("The new owner entered doesn't exist: " + newOwner );
			throw new Exception("The new owner entered doesn't exist. " + newOwner);
		}else if(!newUser.isCurator()){
			if(newUserAccesses.isEmpty()){
				AccessibilityBean newOwnerBean = new AccessibilityBean();
				for(AccessibilityBean groupAccess : groupAccesses){
					//System.out.println("group: " + groupAccess.getGroupName() + "\trole: " + groupAccess.getRoleName());
					String role = groupAccess.getRoleName();
					newOwnerBean.setRoleName(role);
					newOwnerBean.setUserBean(newUser);					
				}
				publicationService.assignAccessibility(newOwnerBean, publication);
			}else{
				for( AccessibilityBean newOwnerUser : newUserAccesses){
					UserBean user = newOwnerUser.getUserBean();
					//System.out.println("currentUser loginName: " + user.getLoginName());
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){
						newOwnerUser.setUserBean(newUser);
						System.out.println("currentowner match with user login name ");
						//super.saveAccessibility(newOwnerUser, sampleId);
						publicationService.assignAccessibility(newOwnerUser, publication);
					}
				}
				
			}
		}
		//need to remove access for the previous owner if not a curator
		List<UserBean> previousUserBean = publicationService.findUserBeans(currentOwner);
		UserBean previousUser=null;
		if(!previousUserBean.isEmpty()){
			for(UserBean bean : previousUserBean){
				if(currentOwner.equals(bean.getLoginName())){
					previousUser = bean;
					break;
				}
			}
		}
		if(previousUser == null){
			logger.error("The current owner entered doesn't exist: " + newOwner );
			throw new Exception("The current owner entered doesn't exist. " + currentOwner);
		}else if(!previousUser.isCurator()){
			if(userAccesses.isEmpty()){
				AccessibilityBean previousOwnerBean = new AccessibilityBean();
				for(AccessibilityBean groupAccess : groupAccesses){
					//System.out.println("group: " + groupAccess.getGroupName() + "\trole: " + groupAccess.getRoleName());
					UserBean user = groupAccess.getUserBean();
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){					
						String role = groupAccess.getRoleName();
						previousOwnerBean.setRoleName(role);
						previousOwnerBean.setUserBean(user);	
						
					}
				}
				publicationService.removeAccessibility(previousOwnerBean, publication);
			}else{
				AccessibilityBean previousOwnerBean = new AccessibilityBean();
				for( AccessibilityBean previousOwnerUser : userAccesses){
					UserBean user = previousOwnerUser.getUserBean();
					//System.out.println("currentUser loginName: " + user.getLoginName());
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){
						previousOwnerBean.setUserBean(user);
						System.out.println("currentowner match with user login name ");
					}
				}
				publicationService.removeAccessibility(previousOwnerBean, publication);
			}
		}
		
	}

	public void transferOwner(ProtocolService protocolService, SecurityService securityService,
			Set<String> protocolIds, String currentOwner, String newOwner)
			throws Exception {
		if (securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		ProtocolServiceHelper helper = new ProtocolServiceHelper(securityService);
		for (String protocolId : protocolIds) {
			Protocol protocol = helper.findProtocolById(protocolId);
			protocol.setCreatedBy(newOwner);
			appService.saveOrUpdate(protocol);
			assignAndRemoveAccessForProtocol(protocolService, currentOwner, newOwner, protocol);
		}
		
	}
	
	private void assignAndRemoveAccessForProtocol(ProtocolService service, String currentOwner, String newOwner, 
			Protocol protocol) throws Exception{
		String protocolId = protocol.getId().toString();
		List<AccessibilityBean> userAccesses = service.findUserAccessibilities(protocolId);
		List<AccessibilityBean> groupAccesses = service.findGroupAccessibilities(protocolId);
		
		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(userAccesses);
		// save the new user access
		//need to retrieve new user info
		//In another words, if user accesses return empty on the previous owner, we’d search for group accesses and 
		//copy the roles from the group accesses and generate new user accesses with the same roles for the new owner.
		List<UserBean> newUserBean = service.findUserBeans(newOwner);
		UserBean newUser = getUserBean(newOwner, newUserBean);
		if(newUser == null){
			logger.error("The new owner entered doesn't exist: " + newOwner );
			throw new Exception("The new owner entered doesn't exist. " + newOwner);
		}else if(!newUser.isCurator()){
			if(newUserAccesses.isEmpty()){
				AccessibilityBean newOwnerBean = new AccessibilityBean();
				for(AccessibilityBean groupAccess : groupAccesses){
					//System.out.println("group: " + groupAccess.getGroupName() + "\trole: " + groupAccess.getRoleName());
					String role = groupAccess.getRoleName();
					newOwnerBean.setRoleName(role);
					newOwnerBean.setUserBean(newUser);					
				}
				service.assignAccessibility(newOwnerBean, protocol);
				//need to remove access for the previous owner if not a curator
			}else{
				for( AccessibilityBean newOwnerUser : newUserAccesses){
					UserBean user = newOwnerUser.getUserBean();
					//System.out.println("currentUser loginName: " + user.getLoginName());
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){
						newOwnerUser.setUserBean(newUser);
						System.out.println("currentowner match with user login name ");
						//super.saveAccessibility(newOwnerUser, sampleId);
						service.assignAccessibility(newOwnerUser, protocol);
					}
				}
				
			}
		}
		//need to remove access for the previous owner if not a curator
		List<UserBean> previousUserBean = service.findUserBeans(currentOwner);
		UserBean previousUser=getUserBean(currentOwner, previousUserBean);
		
		if(previousUser == null){
			logger.error("The current owner entered doesn't exist: " + newOwner );
			throw new Exception("The current owner entered doesn't exist. " + currentOwner);
		}else if(!previousUser.isCurator()){
			if(userAccesses.isEmpty()){
				AccessibilityBean previousOwnerBean = new AccessibilityBean();
				for(AccessibilityBean groupAccess : groupAccesses){
					//System.out.println("group: " + groupAccess.getGroupName() + "\trole: " + groupAccess.getRoleName());
					UserBean user = groupAccess.getUserBean();
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){					
						String role = groupAccess.getRoleName();
						previousOwnerBean.setRoleName(role);
						previousOwnerBean.setUserBean(user);	
						
					}
				}
				service.removeAccessibility(previousOwnerBean, protocol);
			}else{
				AccessibilityBean previousOwnerBean = new AccessibilityBean();
				for( AccessibilityBean previousOwnerUser : userAccesses){
					UserBean user = previousOwnerUser.getUserBean();
					//System.out.println("currentUser loginName: " + user.getLoginName());
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){
						previousOwnerBean.setUserBean(user);
						System.out.println("currentowner match with user login name ");
					}
				}
				service.removeAccessibility(previousOwnerBean, protocol);
			}
		}
		
	}
	
	private AccessibilityBean getUserAccessiblityBeanForGroupAccesses(AccessibilityBean groupAccess, UserBean userBean, String ownerLoginName){
		AccessibilityBean userAccessibilityBean = new AccessibilityBean();
		UserBean user = groupAccess.getUserBean();
		String loginName = user.getLoginName();
		if(ownerLoginName.endsWith(loginName)){	
			String role = groupAccess.getRoleName();
			userAccessibilityBean.setRoleName(role);
			userAccessibilityBean.setUserBean(userBean);	
			return userAccessibilityBean;	
		}
		return null;		
	}

	//replace the current owner userAccess userBean with new owner userBean when the loginName match
	private AccessibilityBean getUserAccessiblityBeanForUserAccesses(AccessibilityBean userAccess, UserBean userBean,String ownerLoginName){
		UserBean user = userAccess.getUserBean();		
		String loginName = user.getLoginName();
		if(ownerLoginName.endsWith(loginName)){				
			userAccess.setUserBean(userBean);
			return userAccess;
		}
		return null;
	}

	//find the userBean that match with the ownerLoginName
	private UserBean getUserBean(String ownerLoginName, List<UserBean> userBeanList) {
		UserBean userBean=null;
		if(userBeanList != null && !userBeanList.isEmpty()){
			for(UserBean bean : userBeanList){
				if(ownerLoginName.equals(bean.getLoginName())){
					userBean = bean;
					break;
				}
			}
		}
		return userBean;
	}
	
}
