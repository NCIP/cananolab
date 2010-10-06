package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Service methods for site preference and visitor counter.
 * 
 * @author houyh
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
			//List<AccessibilityBean> userAccesses = super.findUserAccessibilities(sampleId);
			//List<AccessibilityBean> groupAccesses = super.findGroupAccessibilities(sampleId);
			appService.saveOrUpdate(domain);
			
			handleAccessibility(currentOwner, newOwner, domain, userAccesses, groupAccesses, sampleService);
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
	private void handleAccessibility(String currentOwner, String newOwner, Sample sample,
			List<AccessibilityBean> userAccesses,List<AccessibilityBean> groupAccesses, SampleService sampleService) throws Exception{
			
		
		//load existing privilege 
		// loadSample to load current user access
		// copy first then modify the user to the newuser
		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(userAccesses);
		// save the new user access
		//need to retrieve new user info
		//In another words, if user accesses return empty on the previous owner, we’d search for group accesses and 
		//copy the roles from the group accesses and generate new user accesses with the same roles for the new owner.
		List<UserBean> newUserBean = sampleService.findUserLoginNames(newOwner);
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
				sampleService.assignAccessibility(newOwnerBean, sample);
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
						sampleService.assignAccessibility(newOwnerUser, sample);
					}
				}
				
			}
		}
		//need to remove access for the previous owner if not a curator
		List<UserBean> previousUserBean = sampleService.findUserLoginNames(currentOwner);
		UserBean previousUser = getUserBean(currentOwner, previousUserBean);
		if(previousUser == null){
			logger.error("The current owner entered doesn't exist: " + currentOwner );
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
				if(previousOwnerBean.getUserBean() != null){
					sampleService.removeAccessibility(previousOwnerBean, sample);
				}
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
				if(groupAccesses != null){
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
				}if(previousOwnerBean.getUserBean() != null){
					sampleService.removeAccessibility(previousOwnerBean, sample);
				}
			}
		}
	}






	public void transferOwner(PublicationService publicationService, SecurityService securityService,
			Set<String> publicationIds, String currentOwner, String newOwner)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void transferOwner(ProtocolService protocolService, SecurityService securityService,
			Set<String> protocolIds, String currentOwner, String newOwner)
			throws Exception {
		// TODO Auto-generated method stub
		
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
