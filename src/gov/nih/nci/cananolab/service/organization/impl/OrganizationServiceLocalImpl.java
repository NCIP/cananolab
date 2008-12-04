package gov.nih.nci.cananolab.service.organization.impl;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.exception.OrganizationException;
import gov.nih.nci.cananolab.service.organization.OrganizationService;
import gov.nih.nci.cananolab.service.organization.helper.OrganizationServiceHelper;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
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
	public void saveOrganization(String particleId,
			OrganizationBean primaryOrganization, 
			List<OrganizationBean> otherOrganizationCollection)
		throws OrganizationException{
		
		//TODO: to verify if organization.primaryNanoparticleSampleCollection is empty		
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			NanoparticleSampleService NanoparticleSampleService = new NanoparticleSampleServiceLocalImpl();
			NanoparticleSample nanoparticleSample = NanoparticleSampleService.findNanoparticleSampleById(particleId).getDomainParticleSample();
			Collection<NanoparticleSample> primaryNanoparticleSampleCollection = 
				primaryOrganization.getDomain().getPrimaryNanoparticleSampleCollection();
			if (primaryNanoparticleSampleCollection==null) {
				primaryNanoparticleSampleCollection = new HashSet<NanoparticleSample>();
			}
			if (!primaryNanoparticleSampleCollection.contains(nanoparticleSample)){
				primaryNanoparticleSampleCollection.add(nanoparticleSample);
			}
			primaryOrganization.getDomain()
					.setPrimaryNanoparticleSampleCollection(
							primaryNanoparticleSampleCollection);
			
			saveOrganization(primaryOrganization, authService, appService);
			
			if (otherOrganizationCollection != null) {
				for (OrganizationBean organizationBean: otherOrganizationCollection) {
					Collection<NanoparticleSample> otherNanoparticleSampleCollection = 
						organizationBean.getDomain().getNanoparticleSampleCollection();
					if (otherNanoparticleSampleCollection!=null &&
							!otherNanoparticleSampleCollection.contains(nanoparticleSample)){
						otherNanoparticleSampleCollection.add(nanoparticleSample);
					}
					saveOrganization(organizationBean, authService, appService);
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
	
	private void saveOrganization(OrganizationBean organizationBean,
			AuthorizationService authService, CustomizedApplicationService appService)
		throws Exception{
		if (organizationBean.getDomain().getPointOfContactCollection() == null) {
			organizationBean.getDomain().setPointOfContactCollection(new HashSet<PointOfContact>());
		} else {
			for (PointOfContact poc : organizationBean.getDomain().getPointOfContactCollection()) {
				if (poc.getId() != null)
					authService
							.removePublicGroup(poc.getId().toString());
			}
			organizationBean.getDomain().getPointOfContactCollection().clear();
		}
		if (organizationBean.getPocs() != null) {
			Calendar myCal = Calendar.getInstance();
			for (PointOfContact poc : organizationBean.getPocs()) {
				if (!StringUtils.isBlank(poc.getFirstName())
						|| !StringUtils.isBlank(poc.getLastName())
						|| !StringUtils.isBlank(poc.getMiddleInitial())) {
					if (poc.getCreatedDate() == null) {
						myCal.add(Calendar.SECOND, 1);
						poc.setCreatedDate(myCal.getTime());
					}
					organizationBean.getDomain().getPointOfContactCollection().add(poc);
				}
			}
		}
		if (organizationBean.getDomain().getCreatedDate()==null) {
			organizationBean.getDomain().setCreatedDate(new Date());
		}
		appService.saveOrUpdate(organizationBean.getDomain());		
	}

}
