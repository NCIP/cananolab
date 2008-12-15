package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
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
	public SortedSet<PointOfContact> findPointOfContacts() throws ParticleException {
		SortedSet<PointOfContact> pointOfContacts = new TreeSet<PointOfContact>(
				new CaNanoLabComparators.ParticlePointOfContactComparator());
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(PointOfContact.class);
			crit.setFetchMode("organization", FetchMode.JOIN);
			List results = appService.query(crit);			
			for (Object obj : results) {
				pointOfContacts.add((PointOfContact) obj);
			}
			return pointOfContacts;
		} catch (Exception e) {
			String err = "Error in retrieving all point of contacts";
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
			if (particleSample.getPrimaryPointOfContact().getId() != null) {
				PointOfContact dbPointOfContact = (PointOfContact) appService
						.getObject(PointOfContact.class, "id", particleSample
								.getPrimaryPointOfContact().getId());
				if (dbPointOfContact != null) {
					dbPointOfContact = (PointOfContact) appService.load(
							PointOfContact.class, dbPointOfContact.getId());
					particleSample.setPrimaryPointOfContact(dbPointOfContact);
				}
			}
			if (particleSample.getKeywordCollection() != null) {
				Collection<Keyword> keywords = new HashSet<Keyword>(
						particleSample.getKeywordCollection());
				particleSample.getKeywordCollection().clear();
				for (Keyword keyword : keywords) {
					Keyword dbKeyword = (Keyword) appService.getObject(
							Keyword.class, "name", keyword.getName());
					if (dbKeyword != null) {
						keyword.setId(dbKeyword.getId());
					}
					// turned off cascade save-update in order to share the same
					// keyword instance with File keywords.
					appService.saveOrUpdate(keyword);
					particleSample.getKeywordCollection().add(keyword);
				}
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
	 * @param particlePointOfContacts
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
	public List<ParticleBean> findNanoparticleSamplesBy(String particlePointOfContact,
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
					.findNanoparticleSamplesBy(particlePointOfContact,
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
				.setFetchMode("sampleComposition.fileCollection",
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
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
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
	 * Get other particles from the given particle point of contacts
	 * 
	 * @param particlePointOfContact
	 * @param particleName
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public SortedSet<SortableName> findOtherParticles(String particleOrganization,
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
			crit.createAlias("primaryPointOfContact", "pointOfContact");
			crit.createAlias("pointOfContact.organization","organization").add(
					Restrictions.eq("organization.name", particleOrganization));

			List results = appService.query(crit);
			for (Object obj : results) {
				NanoparticleSample particle = (NanoparticleSample) obj;
				if (auth.isUserAllowed(particle.getName(), user)) {
					otherParticles.add(new SortableName(particle.getName()));
				}
			}
			return otherParticles;
		} catch (Exception e) {
			String err = "Error in retrieving other particles from point of contact "
					+ particleOrganization;
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
						CaNanoLabConstants.CSM_READ_PRIVILEGE);
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
				service.removeFunctionalizingEntityPublicVisibility(
						authService, this
								.findFullFunctionalizingEntityById(dataId
										.toString()));
			} else if (className
					.startsWith("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity")) {
				NanoparticleCompositionService service = new NanoparticleCompositionServiceLocalImpl();
				service.removeNanoparticleEntityPublicVisibility(authService,
						this.findFullNanoparticleEntityById(dataId.toString()));
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

			SortedSet<String> names = new TreeSet<String>(
					new CaNanoLabComparators.SortableNameComparator());
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

	public void assignAssociatedPublicVisibility(
			AuthorizationService authService, ParticleBean particleSampleBean,
			String[] visibleGroups) throws Exception {
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
			// POCs, need special handle for sharing
			if (nanoparticleSample.getPrimaryPointOfContact() != null) {
				authService.removePublicGroup(nanoparticleSample.getPrimaryPointOfContact()
						.getId().toString());
				authService.assignPublicVisibility(nanoparticleSample
						.getPrimaryPointOfContact().getId().toString());
			}
			// keyword, need special handle for sharing
			Collection<Keyword> keywordCollection = nanoparticleSample
					.getKeywordCollection();
			if (keywordCollection != null) {
				for (Keyword keyword : keywordCollection) {
					if (keyword != null) {
						authService.removePublicGroup(keyword.getId()
								.toString());
						authService.assignPublicVisibility(keyword.getId()
								.toString());
					}
				}
			}
			// characterization
			Collection<Characterization> characterizationCollection = nanoparticleSample
					.getCharacterizationCollection();
			if (characterizationCollection != null) {
				for (Characterization aChar : characterizationCollection) {
					charService.assignCharacterizationPublicVisibility(
							authService, aChar);
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
						compositionService
								.assignNanoparicleEntityPublicVisibility(
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
		} else {
			//TODO: need? to be verify by Sue
			// POCs, if private need special handle for sharing
			if (nanoparticleSample.getPrimaryPointOfContact() != null) {
				if (!isExistPublicNanoparticleSampleForPOC(nanoparticleSample
						.getPrimaryPointOfContact().getId().toString())) {
					authService.removePublicGroup(nanoparticleSample
							.getPrimaryPointOfContact().getId().toString());
				}
			}
			// keyword, if private need special handle for sharing
			Collection<Keyword> keywordCollection = nanoparticleSample
					.getKeywordCollection();
			if (keywordCollection != null) {
				for (Keyword keyword : keywordCollection) {
					if (keyword != null) {
						if (!isExistPublicNanoparticleSampleForKeyword(keyword
								.getId().toString())) {
							authService.removePublicGroup(keyword.getId()
									.toString());
						}
					}
				}
			}
		}

	}

	public void removeAssociatedPublicVisibility(
			AuthorizationService authService, ParticleBean particleSampleBean,
			NanoparticleCharacterizationService charService,
			NanoparticleCompositionService compositionService) throws Exception {
		// remove public group in all associated records
		NanoparticleSample nanoparticleSample = particleSampleBean
				.getDomainParticleSample();

		// source, source need special handle
		// if (nanoparticleSample.getOrganization() != null) {
		// authService.removePublicGroup(nanoparticleSample.getOrganization()
		// .getId().toString());
		// }
		// keyword, keyword need special handle
		// Collection<Keyword> keywordCollection = nanoparticleSample
		// .getKeywordCollection();
		// if (keywordCollection != null) {
		// for (Keyword keyword : keywordCollection) {
		// if (keyword != null) {
		// authService.removePublicGroup(keyword.getId().toString());
		// }
		// }
		// }
		// characterization
		Collection<Characterization> characterizationCollection = nanoparticleSample
				.getCharacterizationCollection();
		if (characterizationCollection != null) {
			for (Characterization aChar : characterizationCollection) {
				charService.removeCharacterizationPublicVisibility(authService,
						aChar);
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
					compositionService
							.removeNanoparticleEntityPublicVisibility(
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
							.removeFunctionalizingEntityPublicVisibility(
									authService, functionalizingEntity);
				}
			}
			// sampleComposition.chemicalAssociationCollection
			Collection<ChemicalAssociation> chemicalAssociationCollection = nanoparticleSample
					.getSampleComposition().getChemicalAssociationCollection();
			if (functionalizingEntityCollection != null) {
				for (ChemicalAssociation chemicalAssociation : chemicalAssociationCollection) {
					compositionService
							.removeChemicalAssociationPublicVisibility(
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
	 * Check if there exists public nanoparticle sample for given pocId
	 * 
	 * @param sourcId
	 * @return true / false
	 */
	public boolean isExistPublicNanoparticleSampleForPOC(String pocId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select aParticle.name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample aParticle "
						+ " where aParticle.primaryPointOfContact=" + pocId);
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
	 * Check if there exists public nanoparticle sample for given keywordId
	 * 
	 * @param keywordId
	 * @return true / false
	 */
	public boolean isExistPublicNanoparticleSampleForKeyword(String keywordId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select aParticle.name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample aParticle "
						+ "join aParticle.keywordCollection keyword where keyword.id= '"
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

	public List<ParticleBean> getUserAccessibleParticles(
			List<ParticleBean> particles, UserBean user)
			throws ParticleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			List<ParticleBean> filtered = new ArrayList<ParticleBean>();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List<String> publicData = appService.getPublicData();
			for (ParticleBean particle : particles) {
				String particleName = particle.getDomainParticleSample()
						.getName();
				if (StringUtils.containsIgnoreCase(publicData, particleName)) {
					filtered.add(particle);
				} else if (user != null
						&& auth.checkReadPermission(user, particleName)) {
					filtered.add(particle);
				}
			}
			return filtered;
		} catch (Exception e) {
			String err = "Error in retrieving accessible particles for user.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

}
