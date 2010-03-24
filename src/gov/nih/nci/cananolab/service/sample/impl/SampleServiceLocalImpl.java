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
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.AdvancedSampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.LoginService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.axis.utils.StringUtils;
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

	private SampleServiceHelper helper = new SampleServiceHelper();
	private AdvancedSampleServiceHelper advancedHelper = new AdvancedSampleServiceHelper();
	private FileServiceHelper fileHelper = new FileServiceHelper();

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
		Sample fullyLoadedSample = findFullyLoadedSampleByName(sampleName, user);
		SampleBean fullyLoadedSampleBean = new SampleBean(fullyLoadedSample);
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
			PointOfContact dbPointOfContact = null;
			// if update an existing POC
			if (domainPOC.getId() != null) {
				dbPointOfContact = helper.findPointOfContactById(domainPOC
						.getId().toString(), user);
			} else {
				dbPointOfContact = helper.findPointOfContactByNameAndOrg(
						domainPOC.getFirstName(), domainPOC.getLastName(),
						domainPOC.getOrganization().getName(), user);
			}
			// get created by and created date from database
			if (dbPointOfContact != null) {
				domainPOC.setId(dbPointOfContact.getId());
				domainPOC.setCreatedBy(dbPointOfContact.getCreatedBy());
				domainPOC.setCreatedDate(dbPointOfContact.getCreatedDate());
			}
			// create a new POC if not an existing one
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
			// create a new org if not an existing one
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
		String owningGroup = null;
		if (!StringUtils.isEmpty(sampleBean.getPrimaryPOCBean()
				.getDisplayName())) {
			owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
		}
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
		SampleBean sampleBean = null;
		try {
			Sample sample = helper.findSampleById(sampleId, user);
			sampleBean = new SampleBean(sample);
			// set visibility of POC
			if (user != null) {
				helper.retrieveVisibility(sampleBean.getPrimaryPOCBean());
			}
			if (sampleBean.getOtherPOCBeans() != null && user != null) {
				for (PointOfContactBean poc : sampleBean.getOtherPOCBeans()) {
					helper.retrieveVisibility(poc);
				}
			}
			if (user != null) {
				helper.retrieveVisibility(sampleBean);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the sample by id: " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return sampleBean;
	}

	private Sample findFullyLoadedSampleByName(String sampleName, UserBean user)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		// lazily load sample
		Sample sample = (Sample) appService.getObject(Sample.class, "name",
				sampleName);
		if (sample == null) {
			throw new NotExistException("Sample doesn't exist in the database");
		}
		String sampleId = sample.getId().toString();
		// fully load keywords
		SampleServiceHelper sampleServiceHelper = new SampleServiceHelper();
		List<Keyword> keywords = sampleServiceHelper.findKeywordsBySampleId(
				sampleId, user);
		if (keywords != null && !keywords.isEmpty()) {
			sample.setKeywordCollection(new HashSet<Keyword>(keywords));
		} else {
			sample.setKeywordCollection(null);
		}

		// fully load POCs
		PointOfContact primaryPOC = sampleServiceHelper
				.findPrimaryPointOfContactBySampleId(sampleId, user);
		sample.setPrimaryPointOfContact(primaryPOC);

		List<PointOfContact> otherPOCs = sampleServiceHelper
				.findOtherPointOfContactsBySampleId(sampleId, user);
		if (otherPOCs != null) {
			sample
					.setOtherPointOfContactCollection(new HashSet<PointOfContact>(
							otherPOCs));
		} else {
			sample.setOtherPointOfContactCollection(null);
		}

		// fully load composition
		CompositionServiceHelper compServiceHelper = new CompositionServiceHelper();
		SampleComposition comp = compServiceHelper.findCompositionBySampleId(
				sample.getId().toString(), user);
		sample.setSampleComposition(comp);

		// fully load characterizations
		CharacterizationServiceHelper charServiceHelper = new CharacterizationServiceHelper();
		List<Characterization> chars = charServiceHelper
				.findCharacterizationsBySampleId(sample.getId().toString(),
						user);
		if (chars != null && !chars.isEmpty()) {
			sample.setCharacterizationCollection(new HashSet<Characterization>(
					chars));
		} else {
			sample.setCharacterizationCollection(null);
		}

		// fully load publications
		PublicationService pubService = new PublicationServiceLocalImpl();
		List<PublicationBean> pubBeans = pubService.findPublicationsBySampleId(
				sample.getId().toString(), user);
		if (pubBeans != null) {
			Collection<Publication> publications = new HashSet<Publication>();
			for (PublicationBean pubBean : pubBeans) {
				publications.add((Publication) pubBean.getDomainFile());
			}
			sample.setPublicationCollection(publications);
		} else {
			sample.setPublicationCollection(null);
		}
		return sample;
	}

	public SampleBean findSampleByName(String sampleName, UserBean user)
			throws SampleException, NoAccessException {
		try {
			Sample sample = helper.findSampleByName(sampleName, user);
			SampleBean sampleBean = null;
			if (sample != null) {
				sampleBean = new SampleBean(sample);
				// set visibility of POC
				if (user != null) {
					helper.retrieveVisibility(sampleBean.getPrimaryPOCBean());
				}
				if (sampleBean.getOtherPOCBeans() != null && user != null) {
					for (PointOfContactBean poc : sampleBean.getOtherPOCBeans()) {
						helper.retrieveVisibility(poc);
					}
				}
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
					helper.retrieveVisibility(sampleBean);
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
							+ "where sample.primaryPointOfContact.organization.name=other.primaryPointOfContact.organization.name and sample.id="
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

	public PointOfContactBean findPointOfContactById(String pocId, UserBean user)
			throws PointOfContactException, NoAccessException {
		PointOfContactBean pocBean = null;
		try {
			PointOfContact poc = helper.findPointOfContactById(pocId, user);
			if (poc != null) {
				pocBean = new PointOfContactBean(poc);
				if (user != null) {
					helper.retrieveVisibility(pocBean);
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

	public SampleBean cloneSample(String originalSampleName,
			String newSampleName, UserBean user) throws SampleException,
			NoAccessException, DuplicateEntriesException, NotExistException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		SampleBean newSampleBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample dbNewSample = (Sample) appService.getObject(Sample.class,
					"name", newSampleName);
			if (dbNewSample != null) {
				throw new DuplicateEntriesException();
			}
			// fully load original sample
			Sample origSample = findFullyLoadedSampleByName(originalSampleName,
					user);
			SampleBean origSampleBean = new SampleBean(origSample);
			// retrieve visibilities of the original sample,
			// then copy the visibilities to new sample
			helper.retrieveVisibility(origSampleBean);

			Sample newSample0 = new Sample();
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
			saveSample(newSampleBean0, user);

			// clone the sample
			Sample newSample = origSampleBean.getDomainCopy();
			newSample.setName(newSampleName);
			// keep the id
			newSample.setId(newSample0.getId());
			newSampleBean = new SampleBean(newSample);

			// need to save associations one by one (except keywords)
			// Hibernate mapping settings for most use cases
			saveClonedPOCs(newSampleBean, user);
			saveClonedCharacterizations(origSample.getName(), newSampleBean,
					user);
			saveClonedComposition(origSampleBean, newSampleBean, user);
			saveClonedPublications(origSample.getName(), newSampleBean, user);
			saveSample(newSampleBean, user);
		} catch (NotExistException e) {
			throw e;
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in cloning the sample " + originalSampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return newSampleBean;
	}

	private void saveClonedPOCs(SampleBean sampleBean, UserBean user)
			throws Exception {
		// retrieve visibility
		helper.retrieveVisibility(sampleBean.getPrimaryPOCBean());
		savePointOfContact(sampleBean.getPrimaryPOCBean(), user);
		if (sampleBean.getOtherPOCBeans() != null
				&& !sampleBean.getOtherPOCBeans().isEmpty()) {
			for (PointOfContactBean pocBean : sampleBean.getOtherPOCBeans()) {
				helper.retrieveVisibility(pocBean);
				savePointOfContact(pocBean, user);
			}
		}
	}

	private void saveClonedCharacterizations(String origSampleName,
			SampleBean sampleBean, UserBean user) throws Exception {
		if (sampleBean.getDomain().getCharacterizationCollection() != null) {
			String newSampleName = sampleBean.getDomain().getName();
			CharacterizationService charService = new CharacterizationServiceLocalImpl();
			for (Characterization achar : sampleBean.getDomain()
					.getCharacterizationCollection()) {
				CharacterizationBean charBean = new CharacterizationBean(achar);
				if (charBean.getExperimentConfigs() != null) {
					for (ExperimentConfigBean configBean : charBean
							.getExperimentConfigs()) {
						charService.saveExperimentConfig(configBean, user);
					}
				}
				if (charBean.getFindings() != null) {
					for (FindingBean findingBean : charBean.getFindings()) {
						for (FileBean fileBean : findingBean.getFiles()) {
							fileHelper.updateClonedFileInfo(fileBean,
									origSampleName, newSampleName, user);
						}
						charService.saveFinding(findingBean, user);
					}
				}
				charService.saveCharacterization(sampleBean, charBean, user);
			}
		}
	}

	private void saveClonedComposition(SampleBean origSampleBean,
			SampleBean sampleBean, UserBean user) throws Exception {
		String origSampleName = origSampleBean.getDomain().getName();

		if (sampleBean.getDomain().getSampleComposition() != null) {
			String newSampleName = sampleBean.getDomain().getName();
			CompositionService compService = new CompositionServiceLocalImpl();
			// save nanomaterial entities
			if (sampleBean.getDomain().getSampleComposition()
					.getNanomaterialEntityCollection() != null) {
				for (NanomaterialEntity entity : sampleBean.getDomain()
						.getSampleComposition()
						.getNanomaterialEntityCollection()) {
					NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
							entity);
					for (FileBean fileBean : entityBean.getFiles()) {
						fileHelper.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName, user);
					}
					compService.saveNanomaterialEntity(sampleBean, entityBean,
							user);
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
						fileHelper.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName, user);
					}
					compService.saveFunctionalizingEntity(sampleBean,
							entityBean, user);
				}
			}
			// save files
			if (sampleBean.getDomain().getSampleComposition()
					.getFileCollection() != null) {
				for (File file : sampleBean.getDomain().getSampleComposition()
						.getFileCollection()) {
					FileBean fileBean = new FileBean(file);
					fileHelper.updateClonedFileInfo(fileBean, origSampleName,
							newSampleName, user);
					compService.saveCompositionFile(sampleBean, fileBean, user);
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
						fileHelper.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName, user);
					}
					compService.saveChemicalAssociation(sampleBean, assocBean,
							user);
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
			SampleBean sampleBean, UserBean user) throws Exception {
		if (sampleBean.getDomain().getPublicationCollection() != null) {
			PublicationService pubService = new PublicationServiceLocalImpl();
			for (Publication pub : sampleBean.getDomain()
					.getPublicationCollection()) {
				PublicationBean pubBean = new PublicationBean(pub);
				List<String> accessibleGroups = helper.getAuthService()
						.getAccessibleGroups(pub.getId().toString(),
								Constants.CSM_READ_PRIVILEGE);
				pubBean.setVisibilityGroups(accessibleGroups
						.toArray(new String[0]));
				// to prevent overwriting sample associations
				pubBean.setFromSamplePage(true);
				// / / don't need to reset sample names because
				// savePublication
				// / / takes care of empty sample names.
				pubService.savePublication(pubBean, user);
			}
		}
	}

	public List<String> deleteSample(String sampleName, UserBean user,
			Boolean removeVisibility) throws SampleException,
			NoAccessException, NotExistException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		Sample sample = null;
		// / / to save time, clean up CSM entries in the background
		try {
			// / / fully load original sample
			sample = findFullyLoadedSampleByName(sampleName, user);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// / / delete characterizations
			if (sample.getCharacterizationCollection() != null) {
				CharacterizationService charService = new CharacterizationServiceLocalImpl();
				for (Characterization achar : sample
						.getCharacterizationCollection()) {
					entries.addAll(charService.deleteCharacterization(achar,
							user, removeVisibility));
				}
			}

			// / / delete composition
			if (sample.getSampleComposition() != null) {
				CompositionService compService = new CompositionServiceLocalImpl();
				entries.addAll(compService.deleteComposition(sample
						.getSampleComposition(), user, removeVisibility));
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
			helper.removeVisibility(sample, removeVisibility);
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
