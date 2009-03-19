package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.CharacterizationService;
import gov.nih.nci.cananolab.service.particle.CompositionService;
import gov.nih.nci.cananolab.service.particle.SampleService;
import gov.nih.nci.cananolab.service.particle.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
public class SampleServiceLocalImpl implements
		SampleService {
	private static Logger logger = Logger
			.getLogger(SampleServiceLocalImpl.class);

	private SampleServiceHelper helper = new SampleServiceHelper();

	/**
	 * Persist a new sample or update an existing canano
	 * sample
	 *
	 * @param sample
	 * @throws SampleException,
	 *             DuplicateEntriesException
	 */
	public void saveSample(Sample sample)
			throws SampleException, DuplicateEntriesException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			Sample dbSample = (Sample) appService
					.getObject(Sample.class, "name", sample
							.getName());
			if (dbSample != null
					&& !dbSample.getId().equals(sample.getId())) {
				throw new DuplicateEntriesException();
			}
			if (sample.getPrimaryPointOfContact().getId() != null) {
				PointOfContact dbPointOfContact = (PointOfContact) appService
						.getObject(PointOfContact.class, "id", sample
								.getPrimaryPointOfContact().getId());
				if (dbPointOfContact != null) {
					dbPointOfContact = (PointOfContact) appService.load(
							PointOfContact.class, dbPointOfContact.getId());
					sample.setPrimaryPointOfContact(dbPointOfContact);
				}
			}
			if (sample.getKeywordCollection() != null) {
				Collection<Keyword> keywords = new HashSet<Keyword>(
						sample.getKeywordCollection());
				sample.getKeywordCollection().clear();
				for (Keyword keyword : keywords) {
					Keyword dbKeyword = (Keyword) appService.getObject(
							Keyword.class, "name", keyword.getName());
					if (dbKeyword != null) {
						keyword.setId(dbKeyword.getId());
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
			String err = "Error in saving the sample.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	/**
	 * Persist a new sample or update an existing canano
	 * sample
	 *
	 * @param sample
	 * @throws SampleException,
	 *             DuplicateEntriesException
	 */
	public void saveOtherPOCs(Sample sample)
			throws SampleException, DuplicateEntriesException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample dbSample = (Sample) appService
					.getObject(Sample.class, "name", sample
							.getName());
			if (dbSample != null
					&& !dbSample.getId().equals(sample.getId())) {
				throw new DuplicateEntriesException();
			}
			dbSample.setOtherPointOfContactCollection(sample
					.getOtherPointOfContactCollection());
			appService.saveOrUpdate(dbSample);
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in saving OtherPOCs.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	/**
	 *
	 * @param samplePointOfContacts
	 * @param nanomaterialEntityClassNames
	 * @param otherNanoparticleTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws SampleException
	 */
	public List<SampleBean> findSamplesBy(
			String samplePointOfContact,
			String[] nanomaterialEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws SampleException {
		List<SampleBean> particles = new ArrayList<SampleBean>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
			.getApplicationService();
//			HQLCriteria crit=new HQLCriteria("select sample, sample.primaryPointOfContact, sample.keywordCollection, sample.sampleComposition comp, comp.from gov.nih.nci.cananolab.domain.particle.Sample sample");
//			List results=appService.query(crit);
//			List<Sample>samples=new ArrayList<Sample>();
//			for (Object obj : results) {
//				samples.add((Sample)obj);
//			}
			List<Sample> samples = helper
					.findSamplesBy(samplePointOfContact,
							nanomaterialEntityClassNames,
							otherNanoparticleTypes,
							functionalizingEntityClassNames,
							otherFunctionalizingEntityTypes,
							functionClassNames, otherFunctionTypes,
							characterizationClassNames, wordList);
			Collections.sort(samples,
					new Comparators.SampleComparator());
			for (Sample sample : samples) {
				SampleBean sampleBean = new SampleBean(sample);
				particles.add(sampleBean);
				// load summary information
				sampleBean.setCharacterizationClassNames(helper
						.getStoredCharacterizationClassNames(sample)
						.toArray(new String[0]));
				sampleBean.setFunctionalizingEntityClassNames(helper
						.getStoredFunctionalizingEntityClassNames(
								sample).toArray(new String[0]));
				sampleBean.setNanomaterialEntityClassNames(helper
						.getStoredNanomaterialEntityClassNames(sample)
						.toArray(new String[0]));
				sampleBean.setFunctionClassNames(helper
						.getStoredFunctionClassNames(sample).toArray(
								new String[0]));
			}
			return particles;
		} catch (Exception e) {
			String err = "Problem finding particles with the given search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findSampleById(String sampleId)
			throws SampleException {
		try {
			Sample sample = helper
					.findSampleById(sampleId);
			SampleBean sampleBean = new SampleBean(sample);
			return sampleBean;
		} catch (Exception e) {
			String err = "Problem finding the particle by id: " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findFullSampleById(String sampleId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		// characterization
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.derivedBioAssayDataCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"characterizationCollection.derivedBioAssayDataCollection.derivedDatumCollection",
						FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.experimentConfigCollection",
				FetchMode.JOIN);
		// sampleComposition
		crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection."
				+ "composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.fileCollection", FetchMode.JOIN);
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
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.createAlias("otherPointOfContactCollection", "otherPoc",
				CriteriaSpecification.LEFT_JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		Sample sample = null;
		SampleBean sampleBean = null;
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
			sampleBean = new SampleBean(sample);
		}
		return sampleBean;
	}

	public Sample findSampleByName(String sampleName)
			throws SampleException {
		try {
			return helper.findSampleByName(sampleName);
		} catch (Exception e) {
			String err = "Problem finding the particle by name: "
					+ sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void retrieveVisibility(SampleBean sampleBean, UserBean user)
			throws SampleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);
			if (auth.isUserAllowed(sampleBean.getDomain()
					.getName(), user)) {
				sampleBean.setHidden(false);
				// get assigned visible groups
				List<String> accessibleGroups = auth.getAccessibleGroups(
						sampleBean.getDomain().getName(),
						Constants.CSM_READ_PRIVILEGE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				sampleBean.setVisibilityGroups(visibilityGroups);
			} else {
				sampleBean.setHidden(true);
			}
		} catch (Exception e) {
			String err = "Error in setting visibility groups for particle sample "
					+ sampleBean.getDomain().getName();
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void deleteAnnotationById(String className, Long dataId)
			throws SampleException {
		try {
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			if (className == null) {
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.characterization")) {
				CharacterizationService service = new CharacterizationServiceLocalImpl();
				service.removePublicVisibility(authService,
						findFullCharacterizationById(dataId.toString()));
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.linkage")) {
				CompositionService service = new CompositionServiceLocalImpl();
				ChemicalAssociation chemicalAssociation = service
						.findChemicalAssociationById(dataId.toString())
						.getDomainAssociation();
				service.removeChemicalAssociationPublicVisibility(authService,
						chemicalAssociation);
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.agentmaterial")) {
				CompositionService service = new CompositionServiceLocalImpl();
				service.removeFunctionalizingEntityPublicVisibility(
						authService, this
								.findFullFunctionalizingEntityById(dataId
										.toString()));
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.nanomaterial")) {
				CompositionService service = new CompositionServiceLocalImpl();
				service.removeNanomaterialEntityPublicVisibility(authService,
						this.findFullNanomaterialEntityById(dataId.toString()));
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.deleteById(Class.forName(className), dataId);
		} catch (Exception e) {
			String err = "Error deleting annotation of class " + className
					+ " by ID " + dataId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SortedSet<String> findAllSampleNames(UserBean user)
			throws SampleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);

			SortedSet<String> names = new TreeSet<String>(
					new Comparators.SortableNameComparator());
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select sample.name from gov.nih.nci.cananolab.domain.particle.Sample sample");
			List results = appService.query(crit);
			for (Object obj : results) {
				String name = ((String) obj).trim();
				if (auth.isUserAllowed(name, user)) {
					names.add(name);
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

	public void assignVisibility(SampleBean sampleBean)
			throws Exception {
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		// assign visibility for particle
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		String orgName = pocService.findPointOfContactById(
				sampleBean.getPocBean().getDomain().getId().toString())
				.getOrganization().getName();
		authService.assignVisibility(sampleBean
				.getDomain().getName(), sampleBean
				.getVisibilityGroups(), orgName);

		// assign or remove associated public visibility
		Sample sample = sampleBean
				.getDomain();
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		// characterization
		Collection<Characterization> characterizationCollection = sample
				.getCharacterizationCollection();
		CompositionService compService = new CompositionServiceLocalImpl();
		String[] visibleGroups = sampleBean.getVisibilityGroups();

		// if containing public group, assign associated public visibility
		// otherwise remove associated public visibility
		if (Arrays.asList(visibleGroups).contains(
				Constants.CSM_PUBLIC_GROUP)) {
			// keywords
			Collection<Keyword> keywordCollection = sample
					.getKeywordCollection();
			if (keywordCollection != null) {
				for (Keyword keyword : keywordCollection) {
					if (keyword != null) {
						authService.assignPublicVisibility(keyword.getId()
								.toString());
					}
				}
			}
			// characterizations
			if (characterizationCollection != null) {
				for (Characterization aChar : characterizationCollection) {
					charService.assignPublicVisibility(authService, aChar);
				}
			}
			// sampleComposition
			if (sample.getSampleComposition() != null) {
				compService.assignPublicVisibility(authService,
						sample.getSampleComposition());
			}
		} else {
			// remove associated public visibility
			if (characterizationCollection != null) {
				for (Characterization aChar : characterizationCollection) {
					charService.removePublicVisibility(authService, aChar);
				}
			}
			if (sample.getSampleComposition() != null) {
				compService.assignPublicVisibility(authService,
						sample.getSampleComposition());
			}
		}
	}

	public void removeAssociatedPublicVisibility(
			AuthorizationService authService, SampleBean sampleBean,
			CharacterizationService charService,
			CompositionService compositionService) throws Exception {
		// remove public group in all associated records
		Sample sample = sampleBean
				.getDomain();

		// characterization
		Collection<Characterization> characterizationCollection = sample
				.getCharacterizationCollection();
		if (characterizationCollection != null) {
			for (Characterization aChar : characterizationCollection) {
				charService.removePublicVisibility(authService, aChar);
			}
		}
		// sampleComposition
		if (sample.getSampleComposition() != null) {
			authService.removePublicGroup(sample
					.getSampleComposition().getId().toString());
			// sampleComposition.nanomaterialEntityCollection,
			Collection<NanomaterialEntity> nanomaterialEntityCollection = sample
					.getSampleComposition().getNanomaterialEntityCollection();
			if (nanomaterialEntityCollection != null) {
				for (NanomaterialEntity nanomaterialEntity : nanomaterialEntityCollection) {
					compositionService
							.removeNanomaterialEntityPublicVisibility(
									authService, nanomaterialEntity);
				}
			}
			// sampleComposition.functionalizingEntityCollection,
			Collection<FunctionalizingEntity> functionalizingEntityCollection = sample
					.getSampleComposition()
					.getFunctionalizingEntityCollection();
			if (functionalizingEntityCollection != null) {
				for (FunctionalizingEntity functionalizingEntity : functionalizingEntityCollection) {
					compositionService
							.removeFunctionalizingEntityPublicVisibility(
									authService, functionalizingEntity);
				}
			}
			// sampleComposition.chemicalAssociationCollection
			Collection<ChemicalAssociation> chemicalAssociationCollection = sample
					.getSampleComposition().getChemicalAssociationCollection();
			if (functionalizingEntityCollection != null) {
				for (ChemicalAssociation chemicalAssociation : chemicalAssociationCollection) {
					compositionService
							.removeChemicalAssociationPublicVisibility(
									authService, chemicalAssociation);
				}
			}
		}
	}

	public Characterization findFullCharacterizationById(String charId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				Characterization.class).add(
				Property.forName("id").eq(new Long(charId)));
		// characterization
		crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"derivedBioAssayDataCollection.derivedDatumCollection",
				FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);

		List result = appService.query(crit);
		Characterization achar = null;
		if (!result.isEmpty()) {
			achar = (Characterization) result.get(0);
		}
		return achar;
	}

	public NanomaterialEntity findFullNanomaterialEntityById(String id)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				NanomaterialEntity.class).add(
				Property.forName("id").eq(new Long(id)));
		// sampleComposition.NanomaterialEntity
		crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);

		List result = appService.query(crit);
		NanomaterialEntity entity = null;
		if (!result.isEmpty()) {
			entity = (NanomaterialEntity) result.get(0);
		}
		return entity;
	}

	public FunctionalizingEntity findFullFunctionalizingEntityById(String id)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				FunctionalizingEntity.class).add(
				Property.forName("id").eq(new Long(id)));
		// sampleComposition.FunctionalizingEntity
		crit.setFetchMode("functionCollection", FetchMode.JOIN);

		List result = appService.query(crit);
		FunctionalizingEntity entity = null;
		if (!result.isEmpty()) {
			entity = (FunctionalizingEntity) result.get(0);
		}
		return entity;
	}

	/**
	 * Check if there exists public sample for given pocId
	 *
	 * @param sourcId
	 * @return true / false
	 */
	public boolean isExistPublicSampleForPOC(String pocId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select aSample.name from gov.nih.nci.cananolab.domain.particle.Sample aSample "
						+ " where aSample.primaryPointOfContact=" + pocId);
		List results = appService.query(crit);
		for (Object obj : results) {
			String name = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if there exists public sample for given keywordId
	 *
	 * @param keywordId
	 * @return true / false
	 */
	public boolean isExistPublicSampleForKeyword(String keywordId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select aSample.name from gov.nih.nci.cananolab.domain.particle.Sample aSample "
						+ "join aSample.keywordCollection keyword where keyword.id= '"
						+ keywordId + "'");

		List results = appService.query(crit);
		for (Object obj : results) {
			String name = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, name)) {
				return true;
			}
		}
		return false;
	}

	public List<SampleBean> getUserAccessibleSamples(
			List<SampleBean> particles, UserBean user)
			throws SampleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);
			List<SampleBean> filtered = new ArrayList<SampleBean>();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List<String> publicData = appService.getPublicData();
			for (SampleBean particle : particles) {
				String sampleName = particle.getDomain()
						.getName();
				if (StringUtils.containsIgnoreCase(publicData, sampleName)) {
					filtered.add(particle);
				} else if (user != null
						&& auth.checkReadPermission(user, sampleName)) {
					filtered.add(particle);
				}
				// set POC accessibility
				PointOfContactService pocService = new PointOfContactServiceLocalImpl();
				pocService.retrieveAccessibility(particle.getPocBean(), user);
			}
			return filtered;
		} catch (Exception e) {
			String err = "Error in retrieving accessible particles for user.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SortedSet<String> findSampleNamesByPublicationId(
			String publicationId) throws SampleException {
		try {
			return helper.findSampleNamesByPublicationId(publicationId);
		} catch (Exception e) {
			String err = "Error in retrieving particle names for publication: "
					+ publicationId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SortedSet<String> findAllSampleNames() throws SampleException {
		try {
			DetachedCriteria crit = DetachedCriteria
					.forClass(Sample.class);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List results = appService.query(crit);
			SortedSet<String> names = new TreeSet<String>();
			for (Object obj : results) {
				Sample sample = (Sample) obj;
				names.add(sample.getName());
			}
			return names;
		} catch (Exception e) {
			String err = "Error in retrieving all particle names.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SortedSet<SortableName> findOtherSamplesFromSamePointOfContact(
			String sampleId) throws SampleException {
		SortedSet<SortableName> otherSamples = new TreeSet<SortableName>();
		try {
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select other.name from gov.nih.nci.cananolab.domain.particle.Sample as other "
							+ "where exists ("
							+ "select sample.name from gov.nih.nci.cananolab.domain.particle.Sample as sample "
							+ "where sample.primaryPointOfContact=other.primaryPointOfContact and sample.id="
							+ sampleId + " and other.name!=sample.name)");
			List results = appService.query(crit);
			for (Object obj : results) {
				String name = (String) obj.toString();
				otherSamples.add(new SortableName(name));
			}
			return otherSamples;
		} catch (Exception e) {
			String err = "Error in retrieving other particles from the same point of contact "
					+ sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}
}
