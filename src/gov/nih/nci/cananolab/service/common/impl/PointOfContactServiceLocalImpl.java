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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

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
			PointOfContact dbPointOfContact = findPointOfContact(primaryPointOfContact);
			if (dbPointOfContact != null
					&& !dbPointOfContact.getId().equals(primaryPointOfContact.getId())) {
				throw new DuplicateEntriesException();
			}
			savePointOfContact(primaryPointOfContact, authService, appService);
			
			if (otherPointOfContactCollection != null) {
				for (PointOfContact poc: otherPointOfContactCollection) {	
					dbPointOfContact = findPointOfContact(primaryPointOfContact);
					if (dbPointOfContact != null
							&& !dbPointOfContact.getId().equals(primaryPointOfContact.getId())) {
						throw new DuplicateEntriesException();
					}
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
	
	public PointOfContact findPointOfContact(PointOfContact primaryPointOfContact)
		throws PointOfContactException{
		PointOfContact dbPointOfContact = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(PointOfContact.class);
			//crit.setFetchMode("organization", FetchMode.JOIN);
			crit.add(Restrictions.eq("lastName",primaryPointOfContact.getLastName()));
			crit.add(Restrictions.eq("firsName",primaryPointOfContact.getFirstName()));
			crit.add(Restrictions.eq("organization.name",primaryPointOfContact.getOrganization().getName()));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			for (Object obj : results) {
				dbPointOfContact = (PointOfContact) obj;
			}
			return dbPointOfContact;
		} catch (Exception e) {
			String err = "Problem finding findPointOfContact with the given lastName, firstName and organization name.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	
	private void savePointOfContact(PointOfContact pointOfContact,
			AuthorizationService authService, CustomizedApplicationService appService)
		throws Exception{
		String user = pointOfContact.getCreatedBy();
		//TODO:: association of pointOfContact and organization
		Organization organization = pointOfContact.getOrganization();
		if (organization!=null) {
			//TODO:: re-test (now, org is not from dropdown list, then org.pocCollection==null,
			//if pri-org and sec-org are the same, pri-org is null after overwritten
			
			//if orgID!=null load its POCCollection??
			if (organization.getPointOfContactCollection()==null) {
				organization.setPointOfContactCollection(new HashSet<PointOfContact>());
			}else {
				organization.getPointOfContactCollection().add(pointOfContact);
			}
			organization.setCreatedBy(user);
			organization.setCreatedDate(new Date());
			pointOfContact.setOrganization(organization);
			//TODO:: test update (get orgID from dropdown list)
			organization.setId(new Long(10780673));
			appService.saveOrUpdate(organization);	
			//pointOfContact.setOrganization(organization);
		}
		if (pointOfContact.getCreatedDate()==null) {
			//TODO:: myCal.add(Calendar.SECOND, 1);???
			pointOfContact.setCreatedDate(new Date());
		}
		appService.saveOrUpdate(pointOfContact);		
	}
	
	public PointOfContactBean findPointOfContactById(String POCId) 
		throws PointOfContactException{
		return helper.findPointOfContactById(POCId);		
	}

}
