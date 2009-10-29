package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.AdvancedSampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.service.security.LoginService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
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
 * Service methods involving samples
 *
 * @author pansu
 *
 */
public class SampleServiceLocalImpl implements SampleService {
	private static Logger logger = Logger
			.getLogger(SampleServiceLocalImpl.class);

	private SampleServiceHelper helper = new SampleServiceHelper();
	private AdvancedSampleServiceHelper advancedHelper = new AdvancedSampleServiceHelper();

	/**
	 * Persist a new sample or update an existing canano sample
	 *
	 * @param sample
	 * @throws SampleException ,
	 *             DuplicateEntriesException
	 */
	public void saveSample(SampleBean sampleBean, UserBean user)
			throws SampleException, DuplicateEntriesException,
			NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		Boolean newSample = true;
		if (sampleBean.getDomain().getId() != null) {
			newSample = false;
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample sample = sampleBean.getDomain();
			Sample dbSample = (Sample) appService.getObject(Sample.class,
					"name", sample.getName());
			if (dbSample != null && !dbSample.getId().equals(sample.getId())) {
				throw new DuplicateEntriesException();
			}
			if (sample.getKeywordCollection() != null) {
				Collection<Keyword> keywords = new HashSet<Keyword>(sample
						.getKeywordCollection());
				sample.getKeywordCollection().clear();
				for (Keyword keyword : keywords) {
					Keyword dbKeyword = (Keyword) appService.getObject(
							Keyword.class, "name", keyword.getName());
					if (dbKeyword != null) {
						keyword.setId(dbKeyword.getId());
					}
					else {
						keyword.setId(null);
					}
					// turned off cascade save-update in order to share the same
					// keyword instance with File keywords.
					appService.saveOrUpdate(keyword);
					sample.getKeywordCollection().add(keyword);
				}
			}
			appService.saveOrUpdate(sample);
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in saving the sample";
			logger.error(err, e);
			throw new SampleException(err, e);
		}

		// assign CSM visibility and associated visibility
		// requires fully loaded sample if it's an existing sample)
		try {
			if (!newSample) {
				assignFullVisibility(sampleBean.getVisibilityGroups(),
						sampleBean.getDomain().getName().toString(), user);
			} else {
				assignVisibility(sampleBean);
			}
		} catch (Exception e) {
			throw new SampleException(
					"Error assigning visibility for the sample", e);
		}
	}

	private void assignFullVisibility(String[] visibilityGroups,
			String sampleName, UserBean user) throws Exception {
		SampleBean fullyLoadedSampleBean = findFullSampleByName(sampleName,
				user);
		fullyLoadedSampleBean.setVisibilityGroups(visibilityGroups);
		assignVisibility(fullyLoadedSampleBean);
	}

	public void savePointOfContact(PointOfContactBean pocBean, UserBean user)
			throws PointOfContactException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			PointOfContact domainPOC = pocBean.getDomain();
			Organization domainOrg = domainPOC.getOrganization();
			// get created by and created date from database
			PointOfContact dbPointOfContact = helper
					.findPointOfContactByNameAndOrg(domainPOC.getFirstName(),
							domainPOC.getLastName(), domainPOC
									.getOrganization().getName(), user);
			if (dbPointOfContact != null) {
				domainPOC.setId(dbPointOfContact.getId());
				domainPOC.setCreatedBy(dbPointOfContact.getCreatedBy());
				domainPOC.setCreatedDate(dbPointOfContact.getCreatedDate());
			}
			//create a new POC if not an existing one
			else {
				domainPOC.setId(null);
			}

			// get created by and created date from database
			Organization dbOrganization = helper.findOrganizationByName(
					domainOrg.getName(), user);
			if (dbOrganization != null) {
				domainOrg.setId(dbOrganization.getId());
				domainOrg.setCreatedBy(dbOrganization.getCreatedBy());
				domainOrg.setCreatedDate(dbOrganization.getCreatedDate());
			}
			//create a new org if not an existing one
			else {
				domainOrg.setId(null);
			}
			appService.saveOrUpdate(domainPOC);

			// assign visibility
			assignVisibility(pocBean);
		} catch (Exception e) {
			String err = "Error in saving the PointOfContact.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	private void assignVisibility(SampleBean sampleBean) throws Exception {
		String[] visibleGroups = sampleBean.getVisibilityGroups();
		String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
				.getOrganization().getName();
		// assign visibility for sample
		// visibility for POC is handled by POC separately
		helper.getAuthService().assignVisibility(
				sampleBean.getDomain().getName(), visibleGroups, owningGroup);
		// assign associated visibilities
		Sample sample = sampleBean.getDomain();
		CharacterizationServiceHelper charHelper = new CharacterizationServiceHelper();
		CompositionServiceHelper compHelper = new CompositionServiceHelper();
		Collection<Characterization> characterizationCollection = sample
				.getCharacterizationCollection();
		// characterizations
		if (characterizationCollection != null) {
			for (Characterization aChar : characterizationCollection) {
				charHelper.assignVisibility(aChar, visibleGroups, owningGroup);
			}
		}
		// sampleComposition
		if (sample.getSampleComposition() != null) {
			compHelper.assignVisibility(sample.getSampleComposition(),
					visibleGroups, owningGroup);
		}

		// keywords to public
		if (sample.getKeywordCollection() != null) {
			for (Keyword keyword : sample.getKeywordCollection()) {
				helper.getAuthService().assignVisibility(
						keyword.getId().toString(),
						new String[] { Constants.CSM_PUBLIC_GROUP }, null);

			}
		}
	}

	private void assignVisibility(PointOfContactBean pocBean) throws Exception {
		String[] visibleGroups = pocBean.getVisibilityGroups();
		String owningGroup = pocBean.getDomain().getOrganization().getName();
		// poc
		helper.getAuthService().assignVisibility(
				pocBean.getDomain().getId().toString(), visibleGroups,
				owningGroup);
		// org
		helper.getAuthService().assignVisibility(
				pocBean.getDomain().getOrganization().getId().toString(),
				visibleGroups, owningGroup);
	}

	/**
	 *
	 * @param samplePointOfContacts
	 * @param nanomaterialEntityClassNames
	 * @param otherNanomaterialEntityTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws SampleException
	 */
	public List<String> findSampleNamesBy(String samplePointOfContact,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList,
			UserBean user) throws SampleException {
		try {
			return helper.findSampleNamesBy(samplePointOfContact,
					nanomaterialEntityClassNames, otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, characterizationClassNames,
					otherCharacterizationTypes, wordList, user);
		} catch (Exception e) {
			String err = "Problem finding samples with the given search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findSampleById(String sampleId, UserBean user)
			throws SampleException, NoAccessException {
		try {
			DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
					.add(Property.forName("id").eq(new Long(sampleId)));

			crit.setFetchMode("keywordCollection", FetchMode.JOIN);
			crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
			crit.setFetchMode("primaryPointOfContact.organization",
					FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection.organization",
					FetchMode.JOIN);
			crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			// used for delete check
			crit.setFetchMode(
					"sampleComposition.chemicalAssociationCollection",
					FetchMode.JOIN);
			crit.setFetchMode("publicationCollection", FetchMode.JOIN);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List result = appService.query(crit);
			Sample sample = null;
			SampleBean sampleBean = null;
			if (!result.isEmpty()) {
				sample = (Sample) result.get(0);
				if (helper.getAuthService().checkReadPermission(user,
						sample.getName())) {
					sampleBean = new SampleBean(sample);
					// check visibility of POC
					if (sampleBean.getPrimaryPOCBean() != null) {
						Organization org = sampleBean.getPrimaryPOCBean()
								.getDomain().getOrganization();
						if (org != null) {
							if (!helper.getAuthService().checkReadPermission(
									user, org.getId().toString())) {
								sampleBean.setPrimaryPOCBean(null);
								logger
										.debug("User can't access primary point of contact:"
												+ org.getId());
							}
						}
					}
					if (sampleBean.getOtherPOCBeans() != null) {
						List<PointOfContactBean> pocBeans = new ArrayList<PointOfContactBean>();
						for (PointOfContactBean poc : sampleBean
								.getOtherPOCBeans()) {
							Organization org = poc.getDomain()
									.getOrganization();
							if (org != null) {
								if (helper.getAuthService()
										.checkReadPermission(user,
												org.getId().toString())) {
									pocBeans.add(poc);
								} else {
									logger
											.debug("User can't access point of contact:"
													+ poc.getDomain().getId());
								}
							}
						}
						sampleBean.setOtherPOCBeans(pocBeans);
					}
					if (user != null) {
						retrieveVisibility(sampleBean, user);
					}
				} else {
					throw new NoAccessException();
				}
			}
			return sampleBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the sample by id: " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	private SampleBean findFullSampleByName(String sampleName, UserBean user)
			throws SampleException, NoAccessException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
					.add(Property.forName("name").eq(sampleName));
			// characterization
			crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
			crit.setFetchMode("characterizationCollection.findingCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"characterizationCollection.findingCollection.datumCollection",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"characterizationCollection.findingCollection.datumCollection.conditionCollection",
							FetchMode.JOIN);
			crit.setFetchMode(
					"characterizationCollection.experimentConfigCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"characterizationCollection.experimentConfigCollection.instrumentCollection",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"characterizationCollection.experimentConfigCollection.technique",
							FetchMode.JOIN);
			// sampleComposition
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.nanomaterialEntityCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection.targetCollection",
							FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.chemicalAssociationCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.chemicalAssociationCollection.associatedElementA",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.chemicalAssociationCollection.associatedElementB",
							FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.functionalizingEntityCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.functionalizingEntityCollection.activationMethod",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.functionalizingEntityCollection.functionCollection",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.functionalizingEntityCollection.functionCollection.targetCollection",
							FetchMode.JOIN);
			crit.setFetchMode("keywordCollection", FetchMode.JOIN);
			crit.setFetchMode("publicationCollection", FetchMode.JOIN);
			crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
			crit.setFetchMode("primaryPointOfContact.organization",
					FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection.organization",
					FetchMode.JOIN);
			List result = appService.query(crit);
			Sample sample = null;
			SampleBean sampleBean = null;
			if (!result.isEmpty()) {
				sample = (Sample) result.get(0);
				// if (helper.getAuthService().checkReadPermission(user,
				// sample.getName())) {
				sampleBean = new SampleBean(sample);
				if (user != null) {
					retrieveVisibility(sampleBean, user);
				}
				// } else {
				// throw new NoAccessException();
				// }
			}
			return sampleBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the full sample by name: "
					+ sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findSampleByName(String sampleName, UserBean user)
			throws SampleException, NoAccessException {
		try {
			Sample sample = helper.findSampleByName(sampleName, user);
			SampleBean sampleBean = null;
			if (sample != null) {
				sampleBean = new SampleBean(sample);
				// load summary information
				sampleBean.setCharacterizationClassNames(helper
						.getStoredCharacterizationClassNames(sample).toArray(
								new String[0]));
				sampleBean.setFunctionalizingEntityClassNames(helper
						.getStoredFunctionalizingEntityClassNames(sample)
						.toArray(new String[0]));
				sampleBean.setNanomaterialEntityClassNames(helper
						.getStoredNanomaterialEntityClassNames(sample).toArray(
								new String[0]));
				sampleBean.setFunctionClassNames(helper
						.getStoredFunctionClassNames(sample).toArray(
								new String[0]));
				if (user != null)
					retrieveVisibility(sampleBean, user);
			}
			return sampleBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the sample by name: " + sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	private void retrieveVisibility(SampleBean sampleBean, UserBean user)
			throws Exception {
		// get assigned visible groups
		List<String> accessibleGroups = helper.getAuthService()
				.getAccessibleGroups(sampleBean.getDomain().getName(),
						Constants.CSM_READ_PRIVILEGE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		sampleBean.setVisibilityGroups(visibilityGroups);

		// retrieve visibility for point of contact information
		// don't need to because POC visibility info is not shown in the summary
		// view
		// PointOfContactBean primaryPocBean = sampleBean.getPrimaryPOCBean();
		// if (primaryPocBean.getDomain() != null
		// && primaryPocBean.getDomain().getId() != null) {
		// // get assigned visible groups
		// List<String> pocAccessibleGroups = helper.getAuthService()
		// .getAccessibleGroups(
		// primaryPocBean.getDomain().getId().toString(),
		// Constants.CSM_READ_PRIVILEGE);
		// String[] pocVisibilityGroups = pocAccessibleGroups
		// .toArray(new String[0]);
		// primaryPocBean.setVisibilityGroups(pocVisibilityGroups);
		// }
		// for (PointOfContactBean pocBean : sampleBean.getOtherPOCBeans()) {
		// if (pocBean.getDomain().getId() != null) {
		// // get assigned visible groups
		// List<String> pocAccessibleGroups = helper.getAuthService()
		// .getAccessibleGroups(
		// pocBean.getDomain().getId().toString(),
		// Constants.CSM_READ_PRIVILEGE);
		// String[] pocVisibilityGroups = pocAccessibleGroups
		// .toArray(new String[0]);
		// pocBean.setVisibilityGroups(pocVisibilityGroups);
		// }
		// }
	}

	public SortedSet<String> findAllSampleNames(UserBean user)
			throws SampleException {
		try {
			SortedSet<String> names = new TreeSet<String>(
					new Comparators.SortableNameComparator());
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select sample.name from gov.nih.nci.cananolab.domain.particle.Sample sample");
			List results = appService.query(crit);
			List filteredResults = new ArrayList(results);
			if (user == null) {
				filteredResults = helper.getAuthService().filterNonPublic(
						results);
			}
			for (Object obj : filteredResults) {
				String name = ((String) obj).trim();
				if (user == null
						|| helper.getAuthService().checkReadPermission(user,
								name)) {
					names.add(name);
				} else {
					logger.debug("User doesn't have access to sample of name: "
							+ name);
				}
			}
			return names;
		} catch (Exception e) {
			String err = "Error finding samples for " + user.getLoginName();
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public int getNumberOfPublicSamples() throws SampleException {
		try {
			int count = helper.getNumberOfPublicSamples();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public samples.";
			logger.error(err, e);
			throw new SampleException(err, e);

		}
	}

	public SortedSet<SortableName> findOtherSamplesFromSamePointOfContact(
			String sampleId, UserBean user) throws SampleException {
		SortedSet<SortableName> otherSamples = new TreeSet<SortableName>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select other.name from gov.nih.nci.cananolab.domain.particle.Sample as other "
							+ "where exists ("
							+ "select sample.name from gov.nih.nci.cananolab.domain.particle.Sample as sample "
							+ "where sample.primaryPointOfContact=other.primaryPointOfContact and sample.id="
							+ sampleId + " and other.name!=sample.name)");
			List results = appService.query(crit);
			List filteredResults = new ArrayList(results);
			if (user == null) {
				filteredResults = helper.getAuthService().filterNonPublic(
						results);
			}
			for (Object obj : filteredResults) {
				String name = (String) obj.toString();
				if (user == null
						|| helper.getAuthService().checkReadPermission(user,
								name)) {
					otherSamples.add(new SortableName(name));
				} else {
					logger.debug("User doesn't have access to sample of name: "
							+ name);
				}
			}
			return otherSamples;
		} catch (Exception e) {
			String err = "Error in retrieving other samples from the same point of contact "
					+ sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public PointOfContactBean findPointOfContactById(String POCId, UserBean user)
			throws PointOfContactException, NoAccessException {
		PointOfContactBean pocBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(PointOfContact.class);
			crit.setFetchMode("organization", FetchMode.JOIN);
			crit.add(Restrictions.eq("id", new Long(POCId)));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			for (Object obj : results) {
				PointOfContact poc = (PointOfContact) obj;
				if (helper.getAuthService().checkReadPermission(user,
						poc.getId().toString())) {
					pocBean = new PointOfContactBean(poc);
					if (user != null)
						retrieveVisibility(pocBean);
				} else {
					throw new NoAccessException();
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding point of contact for the given id.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
		return pocBean;
	}

	// retrieve point of contact accessibility
	private void retrieveVisibility(PointOfContactBean pocBean)
			throws PointOfContactException {
		try {
			if (pocBean != null) {
				AuthorizationService auth = new AuthorizationService(
						Constants.CSM_APP_NAME);
				// get assigned visible groups
				List<String> accessibleGroups = auth.getAccessibleGroups(
						pocBean.getDomain().getId().toString(),
						Constants.CSM_READ_PRIVILEGE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				pocBean.setVisibilityGroups(visibilityGroups);
			}
		} catch (Exception e) {
			String err = "Error in retrieving point of contact accessibility for "
					+ pocBean.getDisplayName();
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

	public SortedSet<String> getAllOrganizationNames(UserBean user)
			throws PointOfContactException {
		try {
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

	public void updateAssociatedVisibility(UserBean user) throws Exception {
		// load name only instead of whole sample to save memory
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select sample.name from gov.nih.nci.cananolab.domain.particle.Sample sample");
		List results = appService.query(crit);
		for (Object obj : results) {
			String sampleName = ((String) obj).trim();
			List<String> visibleGroups = helper.getAuthService()
					.getAccessibleGroups(sampleName,
							Constants.CSM_READ_PRIVILEGE);
			System.out.println(sampleName);
			assignFullVisibility(visibleGroups.toArray(new String[0]),
					sampleName, user);
		}
	}

	public void updateInitialPublicData(UserBean user) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		// assign Public visibility for keyword
		HQLCriteria crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.Keyword");
		List results = appService.query(crit);
		for (Object obj : results) {
			String id = obj.toString();
			helper.getAuthService().assignVisibility(id,
					new String[] { Constants.CSM_PUBLIC_GROUP }, null);
		}

		// assign Public visibility for instrument
		crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.Instrument");
		results = appService.query(crit);
		for (Object obj : results) {
			String id = obj.toString();
			helper.getAuthService().assignVisibility(id,
					new String[] { Constants.CSM_PUBLIC_GROUP }, null);
		}

		// assign Public visibility for technique
		crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.Technique");
		results = appService.query(crit);
		for (Object obj : results) {
			String id = obj.toString();
			helper.getAuthService().assignVisibility(id,
					new String[] { Constants.CSM_PUBLIC_GROUP }, null);
		}

		// assign Public visibility for organization
		crit = new HQLCriteria(
				"select org.id from gov.nih.nci.cananolab.domain.common.Organization org");
		results = appService.query(crit);
		for (Object obj : results) {
			String id = obj.toString();
			helper.getAuthService().assignVisibility(id,
					new String[] { Constants.CSM_PUBLIC_GROUP }, null);
		}

	}

	public List<String> findSampleNamesByAdvancedSearch(
			AdvancedSampleSearchBean searchBean, UserBean user)
			throws SampleException {
		try {
			return advancedHelper.findSampleNamesByAdvancedSearch(searchBean,
					user);
		} catch (Exception e) {
			String err = "Problem finding samples with the given advanced search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(
			String sampleName, AdvancedSampleSearchBean searchBean,
			UserBean user) throws SampleException {
		try {
			return advancedHelper.findAdvancedSampleByAdvancedSearch(
					sampleName, searchBean, user);
		} catch (Exception e) {
			String err = "Problem finding advanced sample details with the given advanced search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public static void main(String[] args) {
		String userName = args[0];
		String password = args[1];
		try {
			LoginService loginService = new LoginService(Constants.CSM_APP_NAME);
			UserBean user = loginService.login(userName, password);
			if (user.isCurator()) {
				SampleServiceLocalImpl service = new SampleServiceLocalImpl();
				service.updateAssociatedVisibility(user);
				service.updateInitialPublicData(user);
				System.exit(0);
			} else {
				System.out
						.println("You need to be the curator to be able to execute this function");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
