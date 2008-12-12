package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.helper.PointOfContactServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Local implementation of SourceService
 * 
 * @author tanq
 * 
 */
public class PointOfContactServiceLocalImpl implements PointOfContactService {
	private static Logger logger = Logger
			.getLogger(PointOfContactServiceLocalImpl.class);
	private PointOfContactServiceHelper helper = new PointOfContactServiceHelper();

	/**
	 * Persist a new organization or update an existing organizations
	 * 
	 * @param primaryPointOfContact
	 * @param otherPointOfContactCollection
	 * 
	 * @throws PointOfContactException
	 */
	public void savePointOfContact(
			PointOfContact primaryPointOfContact, 
			Collection<PointOfContact> otherPointOfContactCollection)
		throws PointOfContactException{
		
		//TODO: to verify if organization.primaryNanoparticleSampleCollection is empty		
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);			
			//TODO:::: lastName is not unique, lastName+firstName+email
			PointOfContact dbPointOfContact = (PointOfContact) appService
					.getObject(PointOfContact.class, "lastName", primaryPointOfContact.getLastName());
			if (dbPointOfContact != null
					&& !dbPointOfContact.getId().equals(primaryPointOfContact.getId())) {
				throw new DuplicateEntriesException();
			}
			savePointOfContact(primaryPointOfContact, authService, appService);
			
			if (otherPointOfContactCollection != null) {
				for (PointOfContact poc: otherPointOfContactCollection) {					
					savePointOfContact(poc, authService, appService);
				}
			}
		} catch (Exception e) {
			String err = "Error in saving the PointOfContact.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}
	

	
	public List<PointOfContactBean> findOtherPointOfContactCollection(String particleId)
		throws PointOfContactException{
		return helper.findOtherPointOfContactCollection(particleId);		
	}
	
	public PointOfContactBean findPrimaryPointOfContact(String particleId)
		throws PointOfContactException{
		return helper.findPrimaryPointOfContact(particleId);	
	}
	
	private void savePointOfContact(PointOfContact pointOfContact,
			AuthorizationService authService, CustomizedApplicationService appService)
		throws Exception{
		String user = pointOfContact.getCreatedBy();
		//TODO:: association of pointOfContact and organization
		Organization organization = pointOfContact.getOrganization();
		if (organization!=null) {
			if (organization.getPointOfContactCollection()==null) {
				organization.setPointOfContactCollection(new HashSet<PointOfContact>());
			}else {
				organization.getPointOfContactCollection().add(pointOfContact);
			}
			organization.setCreatedBy(user);
			organization.setCreatedDate(new Date());
			pointOfContact.setOrganization(organization);
		}
		
//		if (pointOfContact.getPointOfContactCollection() == null) {
//			pointOfContact.setPointOfContactCollection(new HashSet<PointOfContact>());
//		} else {
//			for (PointOfContact poc : organization.getPointOfContactCollection()) {
//				if (poc.getId() != null)
//					authService
//							.removePublicGroup(poc.getId().toString());
//			}
//			organization.getPointOfContactCollection().clear();
//		}
//		if (organization.getPointOfContactCollection() != null) {
//			Calendar myCal = Calendar.getInstance();
//			for (PointOfContact poc : organization.getPointOfContactCollection()) {
//				if (!StringUtils.isBlank(poc.getFirstName())
//						|| !StringUtils.isBlank(poc.getLastName())
//						|| !StringUtils.isBlank(poc.getMiddleInitial())) {
//					if (poc.getCreatedDate() == null) {
//						myCal.add(Calendar.SECOND, 1);
//						poc.setCreatedDate(myCal.getTime());
//						poc.setCreatedBy(user);
//					}
//					poc.setOrganization(organization);
//					organization.getPointOfContactCollection().add(poc);
//				}
//			}
//		}
		if (pointOfContact.getCreatedDate()==null) {
			//TODO:: myCal.add(Calendar.SECOND, 1);???
			pointOfContact.setCreatedDate(new Date());
		}
		appService.saveOrUpdate(pointOfContact);		
	}

}
