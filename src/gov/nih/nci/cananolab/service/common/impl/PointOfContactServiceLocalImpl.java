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
	public void savePointOfContact(PointOfContact primaryPointOfContact,
			Collection<PointOfContact> otherPointOfContactCollection)
			throws PointOfContactException, DuplicateEntriesException {

		// TODO: to verify if organization.primaryNanoparticleSampleCollection
		// is empty
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			PointOfContact dbPointOfContact = findPointOfContact(primaryPointOfContact);
			if (dbPointOfContact != null
					&& !dbPointOfContact.getId().equals(
							primaryPointOfContact.getId())) {
				throw new DuplicateEntriesException();
			}
			savePointOfContact(primaryPointOfContact, authService, appService);

			if (otherPointOfContactCollection != null) {
				for (PointOfContact poc : otherPointOfContactCollection) {
					PointOfContact dbOtherPOC = findPointOfContact(poc);
					if (dbOtherPOC != null) {
						poc.setId(dbOtherPOC.getId());
					}
					savePointOfContact(poc, authService, appService);
				}
			}
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in saving the PointOfContact.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public List<PointOfContactBean> findOtherPointOfContactCollection(
			String particleId) throws PointOfContactException {
		return helper.findOtherPointOfContactCollection(particleId);
	}

	public PointOfContact findPointOfContact(
			PointOfContact primaryPointOfContact)
			throws PointOfContactException {
		PointOfContact dbPointOfContact = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(PointOfContact.class);
			crit.createAlias("organization", "organization");
			crit.add(Restrictions.eq("lastName", primaryPointOfContact
					.getLastName()));
			crit.add(Restrictions.eq("firstName", primaryPointOfContact
					.getFirstName()));
			crit.add(Restrictions.eq("organization.name", primaryPointOfContact
					.getOrganization().getName()));
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
			AuthorizationService authService,
			CustomizedApplicationService appService) throws Exception {
		String user = pointOfContact.getCreatedBy();
		//TODO::: if pointOfContact is new, pointOfContact.getOrganization got empty organization
		Organization organization = pointOfContact.getOrganization();
		if (pointOfContact.getCreatedDate() == null) {
			// TODO:: myCal.add(Calendar.SECOND, 1);???
			pointOfContact.setCreatedDate(new Date());
		}
		if (organization != null) {
			Organization dbOrganization = getDBOrganizationByName(organization.getName());
			if (dbOrganization != null) {
				organization.setId(dbOrganization.getId());
				organization.setPointOfContactCollection(dbOrganization
						.getPointOfContactCollection());
				organization.getPointOfContactCollection().add(pointOfContact);
			}else {
				organization
					.setPointOfContactCollection(new HashSet<PointOfContact>());
			}			
			if (organization.getCreatedBy() == null) {
				organization.setCreatedBy(user);
			}
			if (organization.getCreatedDate() == null) {
				organization.setCreatedDate(new Date());
			}
			pointOfContact.setOrganization(organization);
			appService.saveOrUpdate(organization);
		}		
		appService.saveOrUpdate(pointOfContact);
	}

	public PointOfContactBean findPointOfContactById(String POCId)
			throws PointOfContactException {
		return helper.findPointOfContactById(POCId);
	}

	public Organization findOrganizationByName(String orgName)
			throws PointOfContactException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Organization.class);
			crit.add(Restrictions.eq("name", orgName));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			Organization org = null;
			for (Object obj : results) {
				org = (Organization) obj;
			}
			return org;
		} catch (Exception e) {
			String err = "Problem finding organization with the given name "
					+ orgName;
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	private Organization getDBOrganizationByName(String orgName)
			throws PointOfContactException {
		Organization dbOrganization = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Organization.class);
			crit.add(Restrictions.eq("name", orgName));
			crit.createAlias("pointOfContactCollection", "poc",
					CriteriaSpecification.LEFT_JOIN);
			;
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			for (Object obj : results) {
				dbOrganization = ((Organization) obj);
			}
			return dbOrganization;
		} catch (Exception e) {
			String err = "Problem finding organization with the given organization ID.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public PointOfContact loadPOCNanoparticleSample(PointOfContact poc,
			String nanoparticleSampleCollection) throws PointOfContactException {
		PointOfContact dbPoc = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(PointOfContact.class);
			crit.add(Restrictions.eq("id", poc.getId()));
			crit.createAlias(nanoparticleSampleCollection, "otherSamples",
					CriteriaSpecification.LEFT_JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			for (Object obj : results) {
				dbPoc = ((PointOfContact) obj);
			}
			return dbPoc;
		} catch (Exception e) {
			String err = "Problem finding PointOfContact with the given PointOfContact ID.";
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
				names.add(name);
				// TODO:: to verify
				// if (auth.isUserAllowed(name, user)) {
				// names.add(name);
				// }
			}
			return names;
		} catch (Exception e) {
			String err = "Error finding organization for "
					+ user.getLoginName();
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	// retrieve point of contact accessibility
	public void retrieveAccessibility(PointOfContactBean pocBean, UserBean user)
			throws PointOfContactException {
		try {
			if (pocBean != null) {
				AuthorizationService auth = new AuthorizationService(
						CaNanoLabConstants.CSM_APP_NAME);
				if (pocBean.getDomain().getId() != null
						&& auth.isUserAllowed(pocBean.getDomain().getId()
								.toString(), user)) {
					pocBean.setHidden(false);
					// get assigned visible groups
					List<String> accessibleGroups = auth.getAccessibleGroups(
							pocBean.getDomain().getId().toString(),
							CaNanoLabConstants.CSM_READ_PRIVILEGE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					pocBean.setVisibilityGroups(visibilityGroups);
				} else {
					pocBean.setHidden(true);
				}
			}
		} catch (Exception e) {
			String err = "Error in setting point of contact accessibility for "
					+ pocBean.getDisplayName();
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

}
