package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.sample.PointOfContactService;
import gov.nih.nci.cananolab.service.sample.helper.PointOfContactServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
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

		// TODO: to verify if organization.primarySampleCollection
		// is empty
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
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
			String sampleId) throws PointOfContactException {
		return helper.findOtherPointOfContactCollection(sampleId);
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
		Organization organization = pointOfContact.getOrganization();
		if (pointOfContact.getCreatedDate() == null) {
			// TODO:: myCal.add(Calendar.SECOND, 1);???
			pointOfContact.setCreatedDate(new Date());
		}
		if (organization != null) {
			Organization dbOrganization = getDBOrganizationByName(organization
					.getName());
			if (dbOrganization != null) {
				organization.setId(dbOrganization.getId());
				organization.setPointOfContactCollection(dbOrganization
						.getPointOfContactCollection());
				organization.getPointOfContactCollection().add(pointOfContact);
			} else {
				organization.setId(null);
				organization.setCreatedBy(user);
				organization.setCreatedDate(new Date());
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
			/**
			 * Removed as cascade="save-update" was added in POC Hibernate
			 * mapping file.
			 */
			// appService.saveOrUpdate(organization);
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
			crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
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

	public PointOfContact loadPOCSample(PointOfContact poc,
			String sampleCollection) throws PointOfContactException {
		PointOfContact dbPoc = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(PointOfContact.class);
			crit.add(Restrictions.eq("id", poc.getId()));
			crit.setFetchMode(sampleCollection, FetchMode.JOIN);
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
					Constants.CSM_APP_NAME);
			SortedSet<String> names = new TreeSet<String>(
					new Comparators.SortableNameComparator());
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select org.name from gov.nih.nci.cananolab.domain.common.Organization org");
			List results = appService.query(crit);
			for (Object obj : results) {
				String name = ((String) obj).trim();
				names.add(name);
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
	public void retrieveVisibility(PointOfContactBean pocBean, UserBean user)
			throws PointOfContactException {
		try {
			if (pocBean != null) {
				AuthorizationService auth = new AuthorizationService(
						Constants.CSM_APP_NAME);
				if (pocBean.getDomain().getId() != null
						&& auth.checkReadPermission(user, pocBean.getDomain()
								.getId().toString())) {
					pocBean.setHidden(false);
					// get assigned visible groups
					List<String> accessibleGroups = auth.getAccessibleGroups(
							pocBean.getDomain().getId().toString(),
							Constants.CSM_READ_PRIVILEGE);
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

	public void saveOrganization(Organization organization, String user)
			throws Exception {
		if (organization != null && organization.getName() != null) {
			Organization dbOrganization = getDBOrganizationByName(organization
					.getName());
			if (dbOrganization == null) {
				organization.setId(null);
				organization
						.setPointOfContactCollection(new HashSet<PointOfContact>());
				organization.setCreatedBy(user);
				organization.setCreatedDate(new Date());
				CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
						.getApplicationService();
				appService.saveOrUpdate(organization);
			}
		}
	}

	/**
	 *
	 * @return all PointOfContacts
	 */
	// TODO: verify if fetching sampleCollection is necessary on all
	// calls
	public SortedSet<PointOfContact> findAllPointOfContacts()
			throws PointOfContactException {
		SortedSet<PointOfContact> pointOfContacts = new TreeSet<PointOfContact>(
				new Comparators.SamplePointOfContactComparator());
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(PointOfContact.class);
			crit.setFetchMode("organization", FetchMode.JOIN);
			List results = appService.query(crit);
			for (Object obj : results) {
				pointOfContacts.add((PointOfContact) obj);
			}
			return pointOfContacts;
		} catch (Exception e) {
			String err = "Error in retrieving all point of contacts";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public List<PointOfContactBean> findPointOfContactsBySampleId(
			String sampleId) throws PointOfContactException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
					.add(Property.forName("id").eq(new Long(sampleId)));
			crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
			crit.setFetchMode("primaryPointOfContact.organization",
					FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection.organization",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			List<PointOfContactBean> pointOfContactCollection = new ArrayList<PointOfContactBean>();
			for (Object obj : results) {
				Sample particle = (Sample) obj;
				PointOfContact primaryPOC = particle.getPrimaryPointOfContact();
				Collection<PointOfContact> otherPOCs = particle
						.getOtherPointOfContactCollection();
				pointOfContactCollection
						.add(new PointOfContactBean(primaryPOC));
				for (PointOfContact poc : otherPOCs) {
					pointOfContactCollection.add(new PointOfContactBean(poc));
				}
			}
			return pointOfContactCollection;
		} catch (Exception e) {
			String err = "Problem finding all PointOfContact collections with the given sample ID.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	/**
	 * Persist a new sample or update an existing canano sample
	 *
	 * @param sample
	 * @throws SampleException,
	 *             DuplicateEntriesException
	 */
	public void saveOtherPOCs(Sample sample) throws PointOfContactException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample dbSample = (Sample) appService.getObject(Sample.class,
					"name", sample.getName());
			if (dbSample != null && !dbSample.getId().equals(sample.getId())) {
				throw new DuplicateEntriesException();
			}
			dbSample.setOtherPointOfContactCollection(sample
					.getOtherPointOfContactCollection());
			appService.saveOrUpdate(dbSample);
		} catch (Exception e) {
			String err = "Error in saving OtherPOCs.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

}
