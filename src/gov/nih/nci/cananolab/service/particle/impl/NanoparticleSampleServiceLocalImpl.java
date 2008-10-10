package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleSampleServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
 * Service methods involving nanoparticle samples
 * 
 * @author pansu
 * 
 */
public class NanoparticleSampleServiceLocalImpl implements
		NanoparticleSampleService {
	private static Logger logger = Logger
			.getLogger(NanoparticleSampleServiceLocalImpl.class);

	private NanoparticleSampleServiceHelper helper = new NanoparticleSampleServiceHelper();

	/**
	 * 
	 * @return all particle sources
	 */
	public SortedSet<Source> findAllParticleSources() throws ParticleException {
		SortedSet<Source> sampleSources = new TreeSet<Source>(
				new CaNanoLabComparators.ParticleSourceComparator());
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(Source.class);
			for (Object obj : results) {
				sampleSources.add((Source) obj);
			}
			return sampleSources;
		} catch (Exception e) {
			String err = "Error in retrieving all nanoparticle sample sources";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * 
	 * @return all particle sources visible to user
	 */
	public SortedSet<Source> findAllParticleSources(UserBean user)
			throws ParticleException {
		SortedSet<Source> sampleSources = new TreeSet<Source>(
				new CaNanoLabComparators.ParticleSourceComparator());
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Source.class);
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
			List results = appService.query(crit);
			for (Object obj : results) {
				Source source = (Source) obj;
				// if user can access at least one particle from the source, set
				// access to true
				List<String> particleNames = new ArrayList<String>();
				for (NanoparticleSample sample : source
						.getNanoparticleSampleCollection()) {
					particleNames.add(sample.getName());
				}
				if (auth.isAllowedAtLeastOne(auth, particleNames, user)) {
					sampleSources.add((Source) obj);
				}
			}
			return sampleSources;
		} catch (Exception e) {
			String err = "Error in retrieving all nanoparticle sample sources for a user";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * Persist a new nanoparticle sample or update an existing nanoparticle
	 * sample
	 * 
	 * @param particleSample
	 * @throws ParticleException,
	 *             DuplicateEntriesException
	 */
	public void saveNanoparticleSample(NanoparticleSample particleSample)
			throws ParticleException, DuplicateEntriesException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			NanoparticleSample dbParticle = (NanoparticleSample) appService
					.getObject(NanoparticleSample.class, "name", particleSample
							.getName());
			if (dbParticle != null
					&& !dbParticle.getId().equals(particleSample.getId())) {
				throw new DuplicateEntriesException();
			}
			Source dbSource = (Source) appService.getObject(Source.class,
					"organizationName", particleSample.getSource()
							.getOrganizationName());
			if (dbSource != null) {
				particleSample.getSource().setId(dbSource.getId());
			}
			appService.saveOrUpdate(particleSample.getSource());
			for (Keyword keyword : particleSample.getKeywordCollection()) {
				// turned off cascade save-update in order to share the same
				// keyword instance with LabFile keywords.
				Keyword dbKeyword = (Keyword) appService.getObject(
						Keyword.class, "name", keyword.getName());
				if (dbKeyword != null) {
					keyword.setId(dbKeyword.getId());
				}
				appService.saveOrUpdate(keyword);
			}
			appService.saveOrUpdate(particleSample);
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in saving the nanoparticle sample.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * 
	 * @param particleSources
	 * @param nanoparticleEntityClassNames
	 * @param otherNanoparticleTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws ParticleException
	 */
	public List<ParticleBean> findNanoparticleSamplesBy(String particleSource,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws ParticleException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		try {
			List<NanoparticleSample> particleSamples = helper
					.findNanoparticleSamplesBy(particleSource,
							nanoparticleEntityClassNames,
							otherNanoparticleTypes,
							functionalizingEntityClassNames,
							otherFunctionalizingEntityTypes,
							functionClassNames, otherFunctionTypes,
							characterizationClassNames, wordList);
			Collections.sort(particleSamples,
					new CaNanoLabComparators.NanoparticleSampleComparator());
			for (NanoparticleSample particleSample : particleSamples) {
				ParticleBean particleBean = new ParticleBean(particleSample);
				particles.add(particleBean);
				// load summary information
				particleBean.setCharacterizationClassNames(helper
						.getStoredCharacterizationClassNames(particleSample)
						.toArray(new String[0]));
				particleBean.setFunctionalizingEntityClassNames(helper
						.getStoredFunctionalizingEntityClassNames(
								particleSample).toArray(new String[0]));
				particleBean.setNanoparticleEntityClassNames(helper
						.getStoredNanoparticleEntityClassNames(particleSample)
						.toArray(new String[0]));
				particleBean.setFunctionClassNames(helper
						.getStoredFunctionClassNames(particleSample).toArray(
								new String[0]));
			}
			return particles;
		} catch (Exception e) {
			String err = "Problem finding particles with the given search parameters.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public ParticleBean findNanoparticleSampleById(String particleId)
			throws ParticleException {
		try {
			NanoparticleSample particleSample = helper
					.findNanoparticleSampleById(particleId);
			ParticleBean particleBean = new ParticleBean(particleSample);
			return particleBean;
		} catch (Exception e) {
			String err = "Problem finding the particle by id: " + particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public ParticleBean findFullNanoparticleSampleById(String particleId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("id").eq(new Long(particleId)));
		// characterization
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.derivedBioAssayDataCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"characterizationCollection.derivedBioAssayDataCollection.derivedDatumCollection",
						FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection.instrumentConfiguration",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"characterizationCollection.instrumentConfiguration.instrument",
						FetchMode.JOIN);

		// sampleComposition
		crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanoparticleEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection."
				+ "composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode("sampleComposition.labFileCollection",
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
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		NanoparticleSample particleSample = null;
		ParticleBean particleBean = null;
		if (!result.isEmpty()) {
			particleSample = (NanoparticleSample) result.get(0);
			particleBean = new ParticleBean(particleSample);
		}
		return particleBean;
	}

	public NanoparticleSample findNanoparticleSampleByName(String particleName)
			throws ParticleException {
		try {
			return helper.findNanoparticleSampleByName(particleName);
		} catch (Exception e) {
			String err = "Problem finding the particle by name: "
					+ particleName;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * Get other particles from the given particle source
	 * 
	 * @param particleSource
	 * @param particleName
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public SortedSet<SortableName> findOtherParticles(String particleSource,
			String particleName, UserBean user) throws ParticleException {
		SortedSet<SortableName> otherParticles = new TreeSet<SortableName>();
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(NanoparticleSample.class);
			crit.add(Restrictions.ne("name", particleName));
			crit.createAlias("source", "source").add(
					Restrictions.eq("source.organizationName", particleSource));

			List results = appService.query(crit);
			for (Object obj : results) {
				NanoparticleSample particle = (NanoparticleSample) obj;
				if (auth.isUserAllowed(particle.getName(), user)) {
					otherParticles.add(new SortableName(particle.getName()));
				}
			}
			return otherParticles;
		} catch (Exception e) {
			String err = "Error in retrieving other particles from source "
					+ particleSource;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public void retrieveVisibility(ParticleBean particleBean, UserBean user)
			throws ParticleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (auth.isUserAllowed(particleBean.getDomainParticleSample()
					.getName(), user)) {
				particleBean.setHidden(false);
				// get assigned visible groups
				List<String> accessibleGroups = auth.getAccessibleGroups(
						particleBean.getDomainParticleSample().getName(),
						CaNanoLabConstants.CSM_READ_ROLE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				particleBean.setVisibilityGroups(visibilityGroups);
			} else {
				particleBean.setHidden(true);
			}
		} catch (Exception e) {
			String err = "Error in setting visibility groups for particle sample "
					+ particleBean.getDomainParticleSample().getName();
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public void deleteAnnotationById(String className, Long dataId)
			throws ParticleException {
		try {
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (className == null) {
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.particle.characterization")) {
				NanoparticleCharacterizationService service = new NanoparticleCharacterizationServiceLocalImpl();
				service.removeCharacterizationPublicVisibility(authService,
						findFullCharacterizationById(dataId.toString()));
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation")) {
				NanoparticleCompositionService service = new NanoparticleCompositionServiceLocalImpl();
				ChemicalAssociation chemicalAssociation = service
						.findChemicalAssociationById(dataId.toString())
						.getDomainAssociation();
				service.removeChemicalAssociationPublicVisibility(authService,
						chemicalAssociation);
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization")) {
				NanoparticleCompositionService service = new NanoparticleCompositionServiceLocalImpl();
				service.removeFunctionalizingEntityPublicVisibility(authService, this
						.findFullFunctionalizingEntityById(dataId.toString()));
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity")) {
				NanoparticleCompositionService service = new NanoparticleCompositionServiceLocalImpl();
				service.removeNanoparticleEntityPublicVisibility(authService, this
						.findFullNanoparticleEntityById(dataId.toString()));
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.deleteById(Class.forName(className), dataId);
		} catch (Exception e) {
			String err = "Error deleting annotation of class " + className
					+ " by ID " + dataId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public SortedSet<String> findAllNanoparticleSampleNames(UserBean user)
			throws ParticleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);

			SortedSet<String> names = new TreeSet<String>(new CaNanoLabComparators.SortableNameComparator());
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select sample.name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample sample");
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
			throw new ParticleException(err, e);
		}
	}

	public int getNumberOfPublicNanoparticleSamples() throws ParticleException {
		try {
			int count = helper.getNumberOfPublicNanoparticleSamples();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public nanoparticle samples.";
			logger.error(err, e);
			throw new ParticleException(err, e);

		}
	}

	public void assignAssociatedPublicVisibility(AuthorizationService authService,
			ParticleBean particleSampleBean, String[] visibleGroups)
			throws Exception {
		// remove public group in all associated records
		// NanoparticleCharacterizationServiceHelper charHelper = new
		// NanoparticleCharacterizationServiceHelper();
		// NanoparticleCompositionServiceHelper compositionHelper = new
		// NanoparticleCompositionServiceHelper();

		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		NanoparticleCompositionService compositionService = new NanoparticleCompositionServiceLocalImpl();
		removeAssociatedPublicVisibility(authService, particleSampleBean,
				charService, compositionService);
		NanoparticleSample nanoparticleSample = particleSampleBean
			.getDomainParticleSample();
		if (Arrays.asList(visibleGroups).contains(
				CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
			// source, need special handle for sharing			
			if (nanoparticleSample.getSource() != null) {
				authService.removePublicGroup(nanoparticleSample.getSource()
						.getId().toString());
				authService.assignPublicVisibility(nanoparticleSample.getSource()
						.getId().toString());
			}
			// keyword, need special handle for sharing			
			Collection<Keyword> keywordCollection = nanoparticleSample
					.getKeywordCollection();
			if (keywordCollection != null) {
				for (Keyword keyword : keywordCollection) {
					if (keyword != null) {
						authService.removePublicGroup(keyword.getId().toString());
						authService.assignPublicVisibility(
								keyword.getId().toString());
					}
				}
			}
			// characterization
			Collection<Characterization> characterizationCollection = nanoparticleSample
					.getCharacterizationCollection();
			if (characterizationCollection != null) {
				for (Characterization aChar : characterizationCollection) {
					charService.assignCharacterizationPublicVisibility(authService,
							aChar);
				}
			}
			// sampleComposition
			if (nanoparticleSample.getSampleComposition() != null) {
				authService.assignPublicVisibility(nanoparticleSample
						.getSampleComposition().getId().toString());
				// sampleComposition.nanoparticleEntityCollection,
				Collection<NanoparticleEntity> nanoparticleEntityCollection = nanoparticleSample
						.getSampleComposition()
						.getNanoparticleEntityCollection();
				if (nanoparticleEntityCollection != null) {
					for (NanoparticleEntity nanoparticleEntity : nanoparticleEntityCollection) {
						compositionService.assignNanoparicleEntityPublicVisibility(
								authService, nanoparticleEntity);
					}
				}
				// sampleComposition.functionalizingEntityCollection,
				Collection<FunctionalizingEntity> functionalizingEntityCollection = nanoparticleSample
						.getSampleComposition()
						.getFunctionalizingEntityCollection();
				if (functionalizingEntityCollection != null) {
					for (FunctionalizingEntity functionalizingEntity : functionalizingEntityCollection) {
						compositionService
								.assignFunctionalizingEntityPublicVisibility(
										authService, functionalizingEntity);

					}
				}
				// sampleComposition.chemicalAssociationCollection
				Collection<ChemicalAssociation> chemicalAssociationCollection = nanoparticleSample
						.getSampleComposition()
						.getChemicalAssociationCollection();
				if (functionalizingEntityCollection != null) {
					for (ChemicalAssociation chemicalAssociation : chemicalAssociationCollection) {
						compositionService
								.assignChemicalAssociationPublicVisibility(
										authService, chemicalAssociation);
					}
				}			
			}
			/*
			 * //do not need now // sample management SampleManagement
			 * sampleManagement = nanoparticleSample .getSampleManagement(); if
			 * (sampleManagement != null) {
			 * authService.assignVisibility(sampleManagement.getId()
			 * .toString(), visibleGroups); // sampleManagement.sampleContainer
			 * Collection<SampleContainer> sampleContainerCollection =
			 * sampleManagement .getSampleContainerCollection(); if
			 * (sampleContainerCollection != null) { for (SampleContainer
			 * sampleContainer : sampleContainerCollection) { if
			 * (sampleContainer != null) {
			 * authService.assignVisibility(sampleContainer .getId().toString(),
			 * visibleGroups); } // sampleContainer.storageElement Collection<StorageElement>
			 * storageElementCollection = sampleContainer
			 * .getStorageElementCollection(); if (storageElementCollection !=
			 * null) { for (StorageElement storageElement :
			 * storageElementCollection) { if (storageElement != null) {
			 * authService.assignVisibility(storageElement .getId().toString(),
			 * visibleGroups); } } } // sampleContainer.aliquot Collection<Aliquot>
			 * sliquotCollection = sampleContainer .getAliquotCollection(); if
			 * (sliquotCollection != null) { for (Aliquot aliquot :
			 * sliquotCollection) { if (aliquot != null) {
			 * authService.assignVisibility(aliquot .getId().toString(),
			 * visibleGroups); } } } } } }
			 */
		}else{
			// source, if private need special handle for sharing			
			if (nanoparticleSample.getSource() != null) {	
				if (!isExistPublicNanoparticleSampleForSource(
						nanoparticleSample.getSource().getId().toString())){
					authService.removePublicGroup(nanoparticleSample.getSource()
							.getId().toString());
				}
			}
			// keyword, if private need special handle for sharing			
			Collection<Keyword> keywordCollection = nanoparticleSample
					.getKeywordCollection();
			if (keywordCollection != null) {
				for (Keyword keyword : keywordCollection) {
					if (keyword != null) {
						if (!isExistPublicNanoparticleSampleForKeyword(
								keyword.getId().toString())){
							authService.removePublicGroup(keyword.getId().toString());
						}
					}
				}
			}
		}
		
	}

	public void removeAssociatedPublicVisibility(AuthorizationService authService,
			ParticleBean particleSampleBean,
			NanoparticleCharacterizationService charService,
			NanoparticleCompositionService compositionService) throws Exception {
		// remove public group in all associated records
		NanoparticleSample nanoparticleSample = particleSampleBean
				.getDomainParticleSample();

		// source, source need special handle
//		if (nanoparticleSample.getSource() != null) {
//			authService.removePublicGroup(nanoparticleSample.getSource()
//					.getId().toString());
//		}
		// keyword, keyword need special handle
//		Collection<Keyword> keywordCollection = nanoparticleSample
//				.getKeywordCollection();
//		if (keywordCollection != null) {
//			for (Keyword keyword : keywordCollection) {
//				if (keyword != null) {
//					authService.removePublicGroup(keyword.getId().toString());
//				}
//			}
//		}
		// characterization
		Collection<Characterization> characterizationCollection = nanoparticleSample
				.getCharacterizationCollection();
		if (characterizationCollection != null) {
			for (Characterization aChar : characterizationCollection) {
				charService
						.removeCharacterizationPublicVisibility(authService, aChar);
			}
		}
		// sampleComposition
		if (nanoparticleSample.getSampleComposition() != null) {
			authService.removePublicGroup(nanoparticleSample
					.getSampleComposition().getId().toString());
			// sampleComposition.nanoparticleEntityCollection,
			Collection<NanoparticleEntity> nanoparticleEntityCollection = nanoparticleSample
					.getSampleComposition().getNanoparticleEntityCollection();
			if (nanoparticleEntityCollection != null) {
				for (NanoparticleEntity nanoparticleEntity : nanoparticleEntityCollection) {
					compositionService.removeNanoparticleEntityPublicVisibility(
							authService, nanoparticleEntity);
				}
			}
			// sampleComposition.functionalizingEntityCollection,
			Collection<FunctionalizingEntity> functionalizingEntityCollection = nanoparticleSample
					.getSampleComposition()
					.getFunctionalizingEntityCollection();
			if (functionalizingEntityCollection != null) {
				for (FunctionalizingEntity functionalizingEntity : functionalizingEntityCollection) {
					compositionService.removeFunctionalizingEntityPublicVisibility(
							authService, functionalizingEntity);
				}
			}
			// sampleComposition.chemicalAssociationCollection
			Collection<ChemicalAssociation> chemicalAssociationCollection = nanoparticleSample
					.getSampleComposition().getChemicalAssociationCollection();
			if (functionalizingEntityCollection != null) {
				for (ChemicalAssociation chemicalAssociation : chemicalAssociationCollection) {
					compositionService.removeChemicalAssociationPublicVisibility(
							authService, chemicalAssociation);
				}
			}
		}
		/*
		 * //sampleManagement do not need now // sample management
		 * SampleManagement sampleManagement = nanoparticleSample
		 * .getSampleManagement(); if (sampleManagement != null) {
		 * authService.removePublicGroup(sampleManagement.getId().toString()); //
		 * sampleManagement.sampleContainer Collection<SampleContainer>
		 * sampleContainerCollection = sampleManagement
		 * .getSampleContainerCollection(); if (sampleContainerCollection !=
		 * null) { for (SampleContainer sampleContainer :
		 * sampleContainerCollection) { if (sampleContainer != null) {
		 * authService.removePublicGroup(sampleContainer.getId() .toString()); } //
		 * sampleContainer.storageElement Collection<StorageElement>
		 * storageElementCollection = sampleContainer
		 * .getStorageElementCollection(); if (storageElementCollection != null) {
		 * for (StorageElement storageElement : storageElementCollection) { if
		 * (storageElement != null) {
		 * authService.removePublicGroup(storageElement .getId().toString()); } } } //
		 * sampleContainer.aliquot Collection<Aliquot> aliquotCollection =
		 * sampleContainer .getAliquotCollection(); if (aliquotCollection !=
		 * null) { for (Aliquot aliquot : aliquotCollection) { if (aliquot !=
		 * null) { authService.removePublicGroup(aliquot.getId() .toString()); } } } } } }
		 */
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
		crit.setFetchMode("instrumentConfiguration", FetchMode.JOIN);
		crit.setFetchMode("instrumentConfiguration.instrument", FetchMode.JOIN);

		List result = appService.query(crit);
		Characterization achar = null;
		if (!result.isEmpty()) {
			achar = (Characterization) result.get(0);
		}
		return achar;
	}

	public NanoparticleEntity findFullNanoparticleEntityById(String id)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleEntity.class).add(
				Property.forName("id").eq(new Long(id)));
		// sampleComposition.NanoparticleEntity
		crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);

		List result = appService.query(crit);
		NanoparticleEntity entity = null;
		if (!result.isEmpty()) {
			entity = (NanoparticleEntity) result.get(0);
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
	 * Check if there exists public nanoparticle sample for given sourceId
	 * 
	 * @param sourcId
	 * @return true / false
	 */
	public boolean isExistPublicNanoparticleSampleForSource(String sourceId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select aParticle.name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample aParticle "+
				" where aParticle.source="+sourceId);
		List results = appService.query(crit);
		int count = 0;
		for (Object obj : results) {
			String name = (String) obj.toString();
			if (publicData.contains(name)) {
				count=1;
				break;
			}
		}
		if (count>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Check if there exists public nanoparticle sample for given keywordId
	 * 
	 * @param keywordId
	 * @return true / false
	 */
	public boolean isExistPublicNanoparticleSampleForKeyword (String keywordId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();	
		HQLCriteria crit = new HQLCriteria(
				"select aParticle.name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample aParticle "+
				"join aParticle.keywordCollection keyword where keyword.id= '"+keywordId +"'");	

		List results = appService.query(crit);
		int count = 0;
		for (Object obj : results) {
			String name = (String) obj.toString();
			if (publicData.contains(name)) {
				count=1;
				break;
			}
		}
		if (count>0){
			return true;
		}else{
			return false;
		}
	}
	
	
	public List<PublicationBean> findPublicationsByParticleId(String particleId)
			throws ParticleException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
			.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleSample.class).add(
					Property.forName("id").eq(new Long(particleId)));		
			crit.setFetchMode("publicationCollection", FetchMode.JOIN);
			crit.setFetchMode("publicationCollection.authorCollection", FetchMode.JOIN);
			crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			
			List result = appService.query(crit);
			NanoparticleSample particleSample = null;
			List<PublicationBean> publicationCollection = new ArrayList<PublicationBean>();
			if (!result.isEmpty()) {
				particleSample = (NanoparticleSample) result.get(0);			
			}
			if (particleSample!=null) {
				for (Publication publication: particleSample.getPublicationCollection()) {
					//do not load particle sample in PublicationBean
					publicationCollection.add(new PublicationBean(publication, false));
				}
			}		
			return publicationCollection;	
		} catch (Exception e) {
			String err = "Problem finding publication collections with the given particle ID.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}


}
