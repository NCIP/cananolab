package gov.nih.nci.cananolab.service.organization.impl;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.OrganizationException;
import gov.nih.nci.cananolab.service.organization.OrganizationService;
import gov.nih.nci.cananolab.service.organization.helper.OrganizationServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Local implementation of SourceService
 * 
 * @author tanq
 * 
 */
public class OrganizationServiceLocalImpl implements OrganizationService {
	private static Logger logger = Logger
			.getLogger(OrganizationServiceLocalImpl.class);
	private OrganizationServiceHelper helper = new OrganizationServiceHelper();

	/**
	 * Persist a new organization or update an existing organizations
	 * 
	 * @param primaryOrganization
	 * @param otherOrganizationCollection
	 * 
	 * @throws OrganizationException
	 */
	public void saveOrganization(
			Organization primaryOrganization, 
			Collection<Organization> otherOrganizationCollection)
		throws OrganizationException{
		
		//TODO: to verify if organization.primaryNanoparticleSampleCollection is empty		
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);			
			Organization dbOrganization = (Organization) appService
					.getObject(Organization.class, "name", primaryOrganization.getName());
			if (dbOrganization != null
					&& !dbOrganization.getId().equals(primaryOrganization.getId())) {
				throw new DuplicateEntriesException();
			}
			saveOrganization(primaryOrganization, authService, appService);
			
			if (otherOrganizationCollection != null) {
				for (Organization organization: otherOrganizationCollection) {					
					saveOrganization(organization, authService, appService);
				}
			}
		} catch (Exception e) {
			String err = "Error in saving the organization.";
			logger.error(err, e);
			throw new OrganizationException(err, e);
		}
	}
	

	
	public List<OrganizationBean> findOtherOrganizationCollection(String particleId)
		throws OrganizationException{
		return helper.findOtherOrganizationCollection(particleId);		
	}
	
	public OrganizationBean findPrimaryOrganization(String particleId)
		throws OrganizationException{
		return helper.findPrimaryOrganization(particleId);	
	}
	
	private void saveOrganization(Organization organization,
			AuthorizationService authService, CustomizedApplicationService appService)
		throws Exception{
		String user = organization.getCreatedBy();
		if (organization.getPointOfContactCollection() == null) {
			organization.setPointOfContactCollection(new HashSet<PointOfContact>());
		} else {
			for (PointOfContact poc : organization.getPointOfContactCollection()) {
				if (poc.getId() != null)
					authService
							.removePublicGroup(poc.getId().toString());
			}
			organization.getPointOfContactCollection().clear();
		}
		if (organization.getPointOfContactCollection() != null) {
			Calendar myCal = Calendar.getInstance();
			for (PointOfContact poc : organization.getPointOfContactCollection()) {
				if (!StringUtils.isBlank(poc.getFirstName())
						|| !StringUtils.isBlank(poc.getLastName())
						|| !StringUtils.isBlank(poc.getMiddleInitial())) {
					if (poc.getCreatedDate() == null) {
						myCal.add(Calendar.SECOND, 1);
						poc.setCreatedDate(myCal.getTime());
						poc.setCreatedBy(user);
					}
					poc.setOrganization(organization);
					organization.getPointOfContactCollection().add(poc);
				}
			}
		}
		if (organization.getCreatedDate()==null) {
			//TODO:: myCal.add(Calendar.SECOND, 1);???
			organization.setCreatedDate(new Date());
		}
		appService.saveOrUpdate(organization);		
	}

}
