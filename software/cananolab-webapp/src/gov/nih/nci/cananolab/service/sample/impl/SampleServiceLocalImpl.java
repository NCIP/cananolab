package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.AdvancedSampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.service.security.LoginService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving samples
 *
 * @author pansu
 *
 */
public class SampleServiceLocalImpl implements SampleService {
	private static Logger logger = Logger
			.getLogger(SampleServiceLocalImpl.class);

	private SampleServiceHelper helper;
	private AdvancedSampleServiceHelper advancedHelper;
	private CharacterizationServiceLocalImpl charService;
	private CompositionServiceLocalImpl compService;
	private PublicationServiceLocalImpl publicationService;
	private FileServiceLocalImpl fileService;

	public SampleServiceLocalImpl() {
		try {
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			helper = new SampleServiceHelper(authService);
			charService = new CharacterizationServiceLocalImpl(authService);
			compService = new CompositionServiceLocalImpl(authService);
			publicationService = new PublicationServiceLocalImpl(authService);
			fileService = new FileServiceLocalImpl(authService);
			advancedHelper = new AdvancedSampleServiceHelper();
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}

	}

	public SampleServiceLocalImpl(UserBean user) {
		helper = new SampleServiceHelper(user);
		charService = new CharacterizationServiceLocalImpl(user);
		compService = new CompositionServiceLocalImpl(user);
		publicationService = new PublicationServiceLocalImpl(user);
		fileService = new FileServiceLocalImpl(user);
		advancedHelper = new AdvancedSampleServiceHelper(user);
	}

	public SampleServiceLocalImpl(AuthorizationService authService) {
		helper = new SampleServiceHelper(authService);
		charService = new CharacterizationServiceLocalImpl(authService);
		compService = new CompositionServiceLocalImpl(authService);
		publicationService = new PublicationServiceLocalImpl(authService);
		fileService = new FileServiceLocalImpl(authService);
		advancedHelper = new AdvancedSampleServiceHelper(authService);
	}

	public SampleServiceLocalImpl(AuthorizationService authService,
			UserBean user) {
		helper = new SampleServiceHelper(authService, user);
		charService = new CharacterizationServiceLocalImpl(authService, user);
		compService = new CompositionServiceLocalImpl(authService, user);
		publicationService = new PublicationServiceLocalImpl(authService, user);
		fileService = new FileServiceLocalImpl(authService, user);
		advancedHelper = new AdvancedSampleServiceHelper(authService, user);
	}

	/**
	 * Persist a new sample or update an existing canano sample
	 *
	 * @param sample
	 *
	 * @throws SampleException
	 *             , DuplicateEntriesException
	 */
	public void saveSample(SampleBean sampleBean) throws SampleException,
			DuplicateEntriesException, NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		Boolean newSample = true;
		if (sampleBean.getDomain().getId() != null) {
			newSample = false;
		}
		Sample sample = sampleBean.getDomain();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
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
					} else {
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
		// assign CSM visibility and associated visibility
		// requires fully loaded sample if it's an existing sample)
		try {
			if (!newSample) {
				Sample fullyLoadedSample = this
						.findLoadedSampleForAssociatedVisibility(sample
								.getName());
				assignVisibility(fullyLoadedSample, sampleBean
						.getVisibilityGroups());
			} else {
				assignVisibilityToSampleOnly(sample, sampleBean
						.getVisibilityGroups());
			}
		} catch (Exception e) {
			throw new SampleException(
					"Error assigning visibility for the sample", e);
		}
	}

	private void assignVisibility(Sample sample, String[] visibleGroups)
			throws Exception {
		assignVisibilityToSampleOnly(sample, visibleGroups);
		assignAssociatedVisibility(sample, visibleGroups);
	}

	private void assignVisibilityToSampleOnly(Sample sample,
			String[] visibleGroups) throws Exception {
		String owningGroup = null;
		if (sample.getPrimaryPointOfContact() != null
				&& sample.getPrimaryPointOfContact().getOrganization() != null) {
			owningGroup = sample.getPrimaryPointOfContact().getOrganization()
					.getName();
		}
		// visibility for sample, visibility for POC is handled by POC
		// separately
		helper.getAuthService().assignVisibility(sample.getName(),
				visibleGroups, owningGroup);
	}

	private void assignAssociatedVisibility(Sample sample,
			String[] visibleGroups) throws Exception {
		String owningGroup = null;
		if (sample.getPrimaryPointOfContact() != null
				&& sample.getPrimaryPointOfContact().getOrganization() != null) {
			owningGroup = sample.getPrimaryPointOfContact().getOrganization()
					.getName();
		}
		// assign associated visibilities
		Collection<Characterization> characterizationCollection = sample
				.getCharacterizationCollection();
		// characterizations
		if (characterizationCollection != null) {
			for (Characterization aChar : characterizationCollection) {
				charService.assignVisibility(aChar, visibleGroups, owningGroup);
			}
		}
		// sampleComposition
		if (sample.getSampleComposition() != null) {
			compService.assignVisibility(sample.getSampleComposition(),
					visibleGroups, owningGroup);
		}
		// don't need to reset keywords
	}

	private Sample findLoadedSampleForAssociatedVisibility(String sampleName)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		// don't load fields that have separate visibility
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("name").eq(sampleName).ignoreCase());
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.experimentConfigCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"characterizationCollection.experimentConfigCollection.technique",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"characterizationCollection.experimentConfigCollection.instrumentCollection",
						FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection.findingCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.findingCollection.datumCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"characterizationCollection.findingCollection.datumCollection.conditionCollection",
						FetchMode.JOIN);
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
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection.targetCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.activationMethod",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementA",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementB",
						FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.authorCollection",
				FetchMode.JOIN);
		List result = appService.query(crit);
		Sample sample = null;
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		if (sample == null) {
			throw new NotExistException("Sample doesn't exist in the database");
		}
		return sample;
	}

	public void savePointOfContact(PointOfContactBean pocBean)
			throws PointOfContactException, NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		try {
			PointOfContact dbPointOfContact = null;
			Long oldPOCId = null;
			String oldOrgName = null;

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			PointOfContact domainPOC = pocBean.getDomain();
			Organization domainOrg = domainPOC.getOrganization();
			// get existing organization from database and reuse ID,
			// created by and created date
			// address information will be updated
			Organization dbOrganization = helper
					.findOrganizationByName(domainOrg.getName());
			if (dbOrganization != null) {
				domainOrg.setId(dbOrganization.getId());
				domainOrg.setCreatedBy(dbOrganization.getCreatedBy());
				domainOrg.setCreatedDate(dbOrganization.getCreatedDate());
			}
			// create a new org if not an existing one
			else {
				domainOrg.setId(null);
			}
			// if point of contact has no ID
			if (domainPOC.getId() == null) {
				// check if org name, first name and last name matches existing
				// one
				dbPointOfContact = helper.findPointOfContactByNameAndOrg(
						domainPOC.getFirstName(), domainPOC.getLastName(),
						domainPOC.getOrganization().getName());
				// if found, reuse ID, created_date and created_by
				if (dbPointOfContact != null) {
					domainPOC.setId(dbPointOfContact.getId());
					domainPOC.setCreatedDate(dbPointOfContact.getCreatedDate());
					domainPOC.setCreatedBy(dbPointOfContact.getCreatedBy());
				}
			} else {
				// check if organization is changed
				dbPointOfContact = helper.findPointOfContactById(domainPOC
						.getId().toString());
				Organization dbOrg = dbPointOfContact.getOrganization();
				// if organization information is changed, create a new POC
				if (!dbOrg.getName().equals(domainOrg.getName())) {
					oldPOCId = domainPOC.getId();
					oldOrgName = dbOrg.getName();
					domainPOC.setId(null);
				}
				// if name information is changed, create a new POC
				else if (domainPOC.getFirstName().equalsIgnoreCase(
						dbPointOfContact.getFirstName())
						|| domainPOC.getLastName().equalsIgnoreCase(
								dbPointOfContact.getLastName())) {
				} else {
					domainPOC.setId(dbPointOfContact.getId());
					domainPOC.setCreatedBy(dbPointOfContact.getCreatedBy());
					domainPOC.setCreatedDate(dbPointOfContact.getCreatedDate());
				}
			}
			appService.saveOrUpdate(domainPOC);
			// update visibility group
			if (oldPOCId != null && !oldPOCId.equals(domainPOC.getId())) {
				// remove oldOrg from POC visibility
				String[] pocVisGroups = pocBean.getVisibilityGroups();
				pocBean.setVisibilityGroups(StringUtils.removeFromArray(
						pocVisGroups, oldOrgName));
			}
			// assign visibility
			assignVisibility(domainPOC, pocBean.getVisibilityGroups());
		} catch (Exception e) {
			String err = "Error in saving the PointOfContact.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	private void assignVisibility(PointOfContact poc, String[] visibleGroups)
			throws Exception {
		String owningGroup = poc.getOrganization().getName();
		// poc
		helper.getAuthService().assignVisibility(poc.getId().toString(),
				visibleGroups, owningGroup);
		// assign organization to public for it's shared by multiple poc
		helper.getAuthService().assignVisibility(
				poc.getOrganization().getId().toString(),
				new String[] { Constants.CSM_PUBLIC_GROUP }, null);
	}

	/**
	 *
	 * @param nanomaterialEntityClassNames
	 * @param otherNanomaterialEntityTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @param samplePointOfContacts
	 * @return
	 * @throws SampleException
	 */
	public List<String> findSampleNamesBy(String sampleName,
			String samplePointOfContact, String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList)
			throws SampleException {
		try {
			List<String> sampleNames = helper.findSampleNamesBy(sampleName,
					samplePointOfContact, nanomaterialEntityClassNames,
					otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, characterizationClassNames,
					otherCharacterizationTypes, wordList);
			Collections.sort(sampleNames,
					new Comparators.SortableNameComparator());
			return sampleNames;
		} catch (Exception e) {
			String err = "Problem finding samples with the given search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findSampleById(String sampleId) throws SampleException,
			NoAccessException {
		SampleBean sampleBean = null;
		try {
			Sample sample = helper.findSampleById(sampleId);
			sampleBean = loadSampleBean(sample);
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the sample by id: " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return sampleBean;
	}

	private Sample findFullyLoadedSampleByName(String sampleName)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		// load composition and characterization separate because of Hibernate
		// join limitation
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("name").eq(sampleName).ignoreCase());
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.authorCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.keywordCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Sample sample = null;
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		if (sample == null) {
			throw new NotExistException("Sample doesn't exist in the database");
		}

		// fully load composition
		SampleComposition comp = compService.getHelper()
				.findCompositionBySampleId(sample.getId().toString());
		sample.setSampleComposition(comp);

		// fully load characterizations
		List<Characterization> chars = charService.getHelper()
				.findCharacterizationsBySampleId(sample.getId().toString());
		if (chars != null && !chars.isEmpty()) {
			sample.setCharacterizationCollection(new HashSet<Characterization>(
					chars));
		} else {
			sample.setCharacterizationCollection(null);
		}
		return sample;
	}

	private SampleBean loadSampleBean(Sample sample) throws Exception {
		SampleBean sampleBean = new SampleBean(sample);
		// set visibility of POC
		if (helper.getUser() != null
				&& sample.getPrimaryPointOfContact() != null) {
			PointOfContactBean pocBean = sampleBean.getPrimaryPOCBean();
			pocBean.setVisibilityGroups(helper.getAuthService()
					.getAccessibleGroups(
							pocBean.getDomain().getId().toString(),
							Constants.CSM_READ_PRIVILEGE));
		}
		if (sample.getOtherPointOfContactCollection() != null
				&& helper.getUser() != null) {
			for (PointOfContactBean pocBean : sampleBean.getOtherPOCBeans()) {
				pocBean.setVisibilityGroups(helper.getAuthService()
						.getAccessibleGroups(
								pocBean.getDomain().getId().toString(),
								Constants.CSM_READ_PRIVILEGE));
			}
		}
		if (helper.getUser() != null) {
			String[] visibleGroups = helper.getAuthService()
					.getAccessibleGroups(sample.getName().toString(),
							Constants.CSM_READ_PRIVILEGE);
			sampleBean.setVisibilityGroups(visibleGroups);
		}
		return sampleBean;
	}

	public SampleBean findSampleByName(String sampleName)
			throws SampleException, NoAccessException {
		try {
			Sample sample = helper.findSampleByName(sampleName);
			SampleBean sampleBean = null;
			if (sample != null) {
				// sampleBean = loadSampleBean(sample);
				sampleBean = new SampleBean(sample);
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

	public int getNumberOfPublicSampleSources() throws SampleException {
		try {
			int count = helper.getNumberOfPublicSampleSources();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public sample sources.";
			logger.error(err, e);
			throw new SampleException(err, e);

		}
	}

	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException, NoAccessException {
		PointOfContactBean pocBean = null;
		try {
			PointOfContact poc = helper.findPointOfContactById(pocId);
			if (poc != null) {
				pocBean = new PointOfContactBean(poc);
				if (helper.getUser() != null) {
					pocBean.setVisibilityGroups(helper.getAuthService()
							.getAccessibleGroups(poc.getId().toString(),
									Constants.CSM_READ_PRIVILEGE));
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

	public SortedSet<String> getAllOrganizationNames()
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
					+ helper.getUser().getLoginName();
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
			try {
				String[] visibleGroups = helper.getAuthService()
						.getAccessibleGroups(sampleName,
								Constants.CSM_READ_PRIVILEGE);
				System.out.println(sampleName);
				Sample fullyLoadedSample = this
						.findLoadedSampleForAssociatedVisibility(sampleName);
				this.assignVisibility(fullyLoadedSample, visibleGroups);
			} catch (Exception e) {
				System.out
						.println("problem setting associated visibility for: "
								+ sampleName);
				e.printStackTrace();
			}
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
			AdvancedSampleSearchBean searchBean) throws SampleException {
		try {
			return advancedHelper.findSampleNamesByAdvancedSearch(searchBean);
		} catch (Exception e) {
			String err = "Problem finding samples with the given advanced search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(
			String sampleName, AdvancedSampleSearchBean searchBean)
			throws SampleException {
		try {
			return advancedHelper.findAdvancedSampleByAdvancedSearch(
					sampleName, searchBean);
		} catch (Exception e) {
			String err = "Problem finding advanced sample details with the given advanced search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean cloneSample(String originalSampleName,
			String newSampleName) throws SampleException, NoAccessException,
			DuplicateEntriesException, NotExistException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		SampleBean newSampleBean = null;
		Sample origSample = null;
		SampleBean origSampleBean = null;
		Sample newSample0 = new Sample();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample dbNewSample = (Sample) appService.getObject(Sample.class,
					"name", newSampleName);
			if (dbNewSample != null) {
				throw new DuplicateEntriesException();
			}
			// fully load original sample
			origSample = findFullyLoadedSampleByName(originalSampleName);
			origSampleBean = new SampleBean(origSample);
			// retrieve visibilities of the original sample,
			// then copy the visibilities to new sample
			origSampleBean.setVisibilityGroups(helper.getAuthService()
					.getAccessibleGroups(origSample.getName().toString(),
							Constants.CSM_READ_PRIVILEGE));
			newSample0.setName(newSampleName);
			newSample0.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
			newSample0.setCreatedDate(new Date());
			// save the sample so later up just update the cloned the
			// associations.
			SampleBean newSampleBean0 = new SampleBean(newSample0);
			// then copy the visibilities to new sample
			newSampleBean0.setVisibilityGroups(origSampleBean
					.getVisibilityGroups());
			// save the sample to get an ID before saving associations
			saveSample(newSampleBean0);
		} catch (NotExistException e) {
			throw e;
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in loading the original sample "
					+ originalSampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		try {
			// clone the sample
			Sample newSample = origSampleBean.getDomainCopy();
			newSample.setName(newSampleName);
			// keep the id
			newSample.setId(newSample0.getId());
			newSampleBean = new SampleBean(newSample);

			// need to save associations one by one (except keywords)
			// Hibernate mapping settings for most use cases
			saveClonedPOCs(newSampleBean);
			saveClonedCharacterizations(origSample.getName(), newSampleBean);
			saveClonedComposition(origSampleBean, newSampleBean);
			saveClonedPublications(origSample.getName(), newSampleBean);
			saveSample(newSampleBean);
		} catch (Exception e) {
			// delete the already persisted new sample in case of error
			deleteSample(newSampleName, true);
			String err = "Error in cloning the sample " + originalSampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return newSampleBean;
	}

	private void saveClonedPOCs(SampleBean sampleBean) throws Exception {
		// retrieve visibility
		PointOfContactBean primaryPOCBean = sampleBean.getPrimaryPOCBean();
		if (primaryPOCBean.getDomain().getId() != null) {
			primaryPOCBean.setVisibilityGroups(helper.getAuthService()
					.getAccessibleGroups(
							primaryPOCBean.getDomain().getId().toString(),
							Constants.CSM_READ_PRIVILEGE));
		}
		savePointOfContact(sampleBean.getPrimaryPOCBean());
		if (sampleBean.getOtherPOCBeans() != null
				&& !sampleBean.getOtherPOCBeans().isEmpty()) {
			for (PointOfContactBean pocBean : sampleBean.getOtherPOCBeans()) {
				pocBean.setVisibilityGroups(helper.getAuthService()
						.getAccessibleGroups(
								pocBean.getDomain().getId().toString(),
								Constants.CSM_READ_PRIVILEGE));
				savePointOfContact(pocBean);
			}
		}
	}

	private void saveClonedCharacterizations(String origSampleName,
			SampleBean sampleBean) throws Exception {
		if (sampleBean.getDomain().getCharacterizationCollection() != null) {
			String newSampleName = sampleBean.getDomain().getName();
			for (Characterization achar : sampleBean.getDomain()
					.getCharacterizationCollection()) {
				CharacterizationBean charBean = new CharacterizationBean(achar);
				if (charBean.getExperimentConfigs() != null) {
					for (ExperimentConfigBean configBean : charBean
							.getExperimentConfigs()) {
						charService.saveExperimentConfig(configBean);
					}
				}
				if (charBean.getFindings() != null) {
					for (FindingBean findingBean : charBean.getFindings()) {
						for (FileBean fileBean : findingBean.getFiles()) {
							fileService.updateClonedFileInfo(fileBean,
									origSampleName, newSampleName);
						}
						charService.saveFinding(findingBean);
					}
				}
				charService.saveCharacterization(sampleBean, charBean);
			}
		}
	}

	private void saveClonedComposition(SampleBean origSampleBean,
			SampleBean sampleBean) throws Exception {
		String origSampleName = origSampleBean.getDomain().getName();

		if (sampleBean.getDomain().getSampleComposition() != null) {
			String newSampleName = sampleBean.getDomain().getName();
			// save nanomaterial entities
			if (sampleBean.getDomain().getSampleComposition()
					.getNanomaterialEntityCollection() != null) {
				for (NanomaterialEntity entity : sampleBean.getDomain()
						.getSampleComposition()
						.getNanomaterialEntityCollection()) {
					NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
							entity);
					for (FileBean fileBean : entityBean.getFiles()) {
						fileService.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName);
					}
					compService.saveNanomaterialEntity(sampleBean, entityBean);
				}
			}
			// save functionalizing entities
			if (sampleBean.getDomain().getSampleComposition()
					.getFunctionalizingEntityCollection() != null) {
				for (FunctionalizingEntity entity : sampleBean.getDomain()
						.getSampleComposition()
						.getFunctionalizingEntityCollection()) {
					FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
							entity);
					for (FileBean fileBean : entityBean.getFiles()) {
						fileService.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName);
					}
					compService.saveFunctionalizingEntity(sampleBean,
							entityBean);
				}
			}
			// save files
			if (sampleBean.getDomain().getSampleComposition()
					.getFileCollection() != null) {
				for (File file : sampleBean.getDomain().getSampleComposition()
						.getFileCollection()) {
					FileBean fileBean = new FileBean(file);
					fileService.updateClonedFileInfo(fileBean, origSampleName,
							newSampleName);
					compService.saveCompositionFile(sampleBean, fileBean);
				}
			}
			// save chemical association
			if (sampleBean.getDomain().getSampleComposition()
					.getChemicalAssociationCollection() != null) {
				for (ChemicalAssociation assoc : sampleBean.getDomain()
						.getSampleComposition()
						.getChemicalAssociationCollection()) {
					ChemicalAssociationBean assocBean = new ChemicalAssociationBean(
							assoc);
					// set the correct IDs for associated elements
					updateAssociatedElementId(sampleBean.getDomain()
							.getSampleComposition(), assoc
							.getAssociatedElementA());
					updateAssociatedElementId(sampleBean.getDomain()
							.getSampleComposition(), assoc
							.getAssociatedElementB());
					for (FileBean fileBean : assocBean.getFiles()) {
						fileService.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName);
					}
					compService.saveChemicalAssociation(sampleBean, assocBean);
				}
			}
		}
	}

	private void updateAssociatedElementId(SampleComposition comp,
			AssociatedElement associatedElement) {
		if (associatedElement != null) {
			String origId = associatedElement.getCreatedBy().substring(5);
			// finding the matching functionalizing entity
			if (associatedElement instanceof FunctionalizingEntity) {
				for (FunctionalizingEntity entity : comp
						.getFunctionalizingEntityCollection()) {
					String entityOrigId = entity.getCreatedBy().substring(5);
					if (entityOrigId.equals(origId)) {
						associatedElement.setId(entity.getId());
						break;
					}
				}
			}
			if (associatedElement instanceof ComposingElement) {
				for (NanomaterialEntity entity : comp
						.getNanomaterialEntityCollection()) {
					if (entity.getComposingElementCollection() != null) {
						for (ComposingElement ce : entity
								.getComposingElementCollection()) {
							String ceOrigId = ce.getCreatedBy().substring(5);
							if (ceOrigId.equals(origId)) {
								associatedElement.setId(ce.getId());
								break;
							}
						}
					}
				}
			}
		}
	}

	private void saveClonedPublications(String origSampleName,
			SampleBean sampleBean) throws Exception {
		if (sampleBean.getDomain().getPublicationCollection() != null) {
			for (Publication pub : sampleBean.getDomain()
					.getPublicationCollection()) {
				PublicationBean pubBean = new PublicationBean(pub);
				String[] accessibleGroups = helper.getAuthService()
						.getAccessibleGroups(pub.getId().toString(),
								Constants.CSM_READ_PRIVILEGE);
				pubBean.setVisibilityGroups(accessibleGroups);
				// to prevent overwriting sample associations
				pubBean.setFromSamplePage(true);
				// / / don't need to reset sample names because
				// savePublication
				// / / takes care of empty sample names.
				publicationService.savePublication(pubBean);
			}
		}
	}

	public List<String> deleteSample(String sampleName, Boolean removeVisibility)
			throws SampleException, NoAccessException, NotExistException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		Sample sample = null;
		// / / to save time, clean up CSM entries in the background
		try {
			// / / fully load original sample
			sample = findFullyLoadedSampleByName(sampleName);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// / / delete characterizations
			if (sample.getCharacterizationCollection() != null) {
				for (Characterization achar : sample
						.getCharacterizationCollection()) {
					entries.addAll(charService.deleteCharacterization(achar,
							removeVisibility));
				}
			}

			// / / delete composition
			if (sample.getSampleComposition() != null) {
				entries.addAll(compService.deleteComposition(sample
						.getSampleComposition(), removeVisibility));
			}
			sample.setSampleComposition(null);

			// / / remove publication associations
			if (sample.getPublicationCollection() != null) {
				sample.setPublicationCollection(null);
			}
			// / / remove keyword associations
			if (sample.getKeywordCollection() != null) {
				sample.setKeywordCollection(null);
			}
			appService.saveOrUpdate(sample);
			appService.delete(sample);
			removeVisibility(sample, removeVisibility);
			entries.add(sample.getName());
		} catch (NotExistException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in deleting the sample " + sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return entries;
	}

	private List<String> removeVisibility(PointOfContact poc, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (remove == null || remove)
			helper.getAuthService().removeCSMEntry(poc.getId().toString());
		entries.add(poc.getId().toString());
		return entries;
	}

	private List<String> removeVisibility(Sample sample, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (remove == null || remove)
			helper.getAuthService().removeCSMEntry(sample.getName());
		entries.add(sample.getName());
		Collection<Characterization> characterizationCollection = sample
				.getCharacterizationCollection();
		// characterizations
		if (characterizationCollection != null) {
			for (Characterization aChar : characterizationCollection) {
				entries.addAll(charService.removeVisibility(aChar, remove));
			}
		}
		// sampleComposition
		if (sample.getSampleComposition() != null) {
			entries.addAll(compService.removeVisibility(sample
					.getSampleComposition(), remove));
		}
		return entries;
	}

	public void updatePOCAssociatedWithCharacterizations(String sampleName,
			Long oldPOCId, Long newPOCId) throws SampleException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Characterization.class);
			crit.createAlias("sample", "sample");
			crit.createAlias("pointOfContact", "poc");
			crit.add(Property.forName("poc.id").eq(oldPOCId));
			crit.add(Property.forName("sample.name").eq(sampleName));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				Characterization achar = (Characterization) obj;
				// update POC to the new ID
				achar.getPointOfContact().setId(newPOCId);
				appService.saveOrUpdate(achar);
			}
		} catch (Exception e) {
			String err = "Error in updating POC associated sample characterizations "
					+ sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void updateSampleVisibilityWithPOCChange(SampleBean sampleBean,
			String oldPOCId) throws SampleException {
		try {
			// remove oldOrg from sample visibility
			PointOfContact oldPOC = getHelper()
					.findPointOfContactById(oldPOCId);
			String oldOrgName = oldPOC.getOrganization().getName();
			String[] sampleVisGroups = sampleBean.getVisibilityGroups();
			String[] updatedGroups = StringUtils.removeFromArray(
					sampleVisGroups, oldOrgName);
			sampleBean.setVisibilityGroups(updatedGroups);
		} catch (Exception e) {
			String err = "Error in updating sample visibility with POC change for "
					+ sampleBean.getDomain().getName();
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

	public List<String> findOtherSampleNamesFromSamePrimaryOrganization(
			String sampleId) throws SampleException {
		List<String> sortedNames = null;
		try {
			Set<String> names = helper
					.findOtherSamplesFromSamePrimaryOrganization(sampleId);
			sortedNames = new ArrayList<String>(names);
			Collections.sort(sortedNames,
					new Comparators.SortableNameComparator());

		} catch (Exception e) {
			String err = "Error in deleting the sample " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return sortedNames;
	}

	public SampleServiceHelper getHelper() {
		return helper;
	}

}
