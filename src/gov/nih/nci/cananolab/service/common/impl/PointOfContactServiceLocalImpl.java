package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.helper.PointOfContactServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
			crit.createAlias("organization", "organization");
			crit.add(Restrictions.eq("lastName",primaryPointOfContact.getLastName()));
			crit.add(Restrictions.eq("firstName",primaryPointOfContact.getFirstName()));
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
			Organization dbOrganization = (Organization) appService.getObject(
					Organization.class, "name", organization.getName());
			if (dbOrganization != null) {
				organization.setId(dbOrganization.getId());		
			}
			if (organization.getCreatedBy()==null){			
				organization.setCreatedBy(user);
			}
			if (organization.getCreatedDate()==null){
				organization.setCreatedDate(new Date());
			}
			pointOfContact.setOrganization(organization);
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
	
	public SortedSet<Organization> findAllOrganizations(UserBean user)
		throws PointOfContactException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);		
			SortedSet<Organization> organizations = new TreeSet<Organization>(
					new CaNanoLabComparators.OrganizationComparator());
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();		
			DetachedCriteria crit = DetachedCriteria.forClass(Organization.class);
			List results = appService.query(crit);
			//TODO:: to test
			for (Object obj : results) {
				Organization org = ((Organization) obj);
				if (auth.isUserAllowed(org.getId().toString(), user)) {
					organizations.add(org);
				}
			}
			return organizations;
		} catch (Exception e) {
			String err = "Error finding all organization for " + user.getLoginName();
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}
	
	public SortedSet<String> getAllOrganizationNames(UserBean user)
		throws PointOfContactException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			SortedSet<String> names = new TreeSet<String>(
					new CaNanoLabComparators.SortableNameComparator());
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select org.name from gov.nih.nci.cananolab.domain.common.Organization org");
			List results = appService.query(crit);
			for (Object obj : results) {
				String name = ((String) obj).trim();
				if (auth.isUserAllowed(name, user)) {
					names.add(name);
				}
			}
			return names;
		} catch (Exception e) {
			String err = "Error finding organization for " + user.getLoginName();
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

}
