package gov.nih.nci.cananolab.service.particle.helper;

import gov.nih.nci.cananolab.domain.common.Aliquot;
import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.InstrumentConfiguration;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.SampleContainer;
import gov.nih.nci.cananolab.domain.common.SampleManagement;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.common.StorageElement;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of NanoparticleSampleService and grid service *
 * 
 * @author pansu, tanq
 * 
 */
public class NanoparticleSampleServiceHelper {
	private static Logger logger = Logger
			.getLogger(NanoparticleSampleServiceHelper.class);

	public List<NanoparticleSample> findNanoparticleSamplesBy(
			String particleSource, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws Exception {
		List<NanoparticleSample> particles = new ArrayList<NanoparticleSample>();

		DetachedCriteria crit = DetachedCriteria
				.forClass(NanoparticleSample.class);
		if (particleSource != null && particleSource.length() > 0) {
			TextMatchMode sourceMatchMode = new TextMatchMode(particleSource);
			crit.createAlias("source", "source",
					CriteriaSpecification.LEFT_JOIN).add(
					Restrictions.ilike("source.organizationName",
							sourceMatchMode.getUpdatedText(), sourceMatchMode
									.getMatchMode()));
		}

		if (wordList != null && wordList.length > 0) {
			// turn words into upper case before searching keywords
			String[] upperKeywords = new String[wordList.length];
			for (int i = 0; i < wordList.length; i++) {
				upperKeywords[i] = wordList[i].toUpperCase();
			}
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("keywordCollection", "keyword1",
					CriteriaSpecification.LEFT_JOIN);
			for (String keyword : upperKeywords) {
				Criterion keywordCrit1 = Restrictions.like("keyword1.name",
						keyword, MatchMode.ANYWHERE);
				disjunction.add(keywordCrit1);
			}
			crit.createAlias("characterizationCollection", "chara",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"chara.derivedBioAssayDataCollection", "derived",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"derived.labFile", "charFile",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"charFile.keywordCollection", "keyword2",
					CriteriaSpecification.LEFT_JOIN);
			;
			for (String keyword : upperKeywords) {
				Criterion keywordCrit2 = Restrictions.like("keyword2.name",
						keyword, MatchMode.ANYWHERE);
				disjunction.add(keywordCrit2);
			}
			for (String word : wordList) {
				Criterion summaryCrit1 = Restrictions.ilike(
						"chara.description", word, MatchMode.ANYWHERE);
				Criterion summaryCrit2 = Restrictions.ilike(
						"charFile.description", word, MatchMode.ANYWHERE);
				Criterion summaryCrit = Restrictions.or(summaryCrit1,
						summaryCrit2);
				disjunction.add(summaryCrit);
			}
			crit.add(disjunction);
		}
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanoparticleEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanoparticleEntityCollection.composingElementCollection.inherentFunctionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			NanoparticleSample particle = (NanoparticleSample) obj;
			particles.add(particle);
		}

		List<NanoparticleSample> functionFilteredParticles = filterByFunctions(
				functionClassNames, otherFunctionTypes, particles);
		List<NanoparticleSample> characterizationFilteredParticles = filterByCharacterizations(
				characterizationClassNames, functionFilteredParticles);
		List<NanoparticleSample> theParticles = filterByCompositions(
				nanoparticleEntityClassNames, otherNanoparticleTypes,
				functionalizingEntityClassNames,
				otherFunctionalizingEntityTypes,
				characterizationFilteredParticles);
		Collections.sort(theParticles,
				new CaNanoLabComparators.NanoparticleSampleComparator());
		return theParticles;
	}

	/**
	 * Return all stored functionalizing entity class names. In case of
	 * OtherFunctionalizingEntity, store the OtherFunctionalizingEntity type
	 * 
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredFunctionalizingEntityClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null
				&& particleSample.getSampleComposition()
						.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : particleSample
					.getSampleComposition()
					.getFunctionalizingEntityCollection()) {
				if (entity instanceof OtherFunctionalizingEntity) {
					storedEntities.add(((OtherFunctionalizingEntity) entity)
							.getType());
				} else {
					storedEntities.add(ClassUtils.getShortClassName(entity
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedEntities;
	}

	/**
	 * Return all stored function class names. In case of OtherFunction, store
	 * the otherFunction type
	 * 
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredFunctionClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedFunctions = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null) {
			if (particleSample.getSampleComposition()
					.getNanoparticleEntityCollection() != null) {
				for (NanoparticleEntity entity : particleSample
						.getSampleComposition()
						.getNanoparticleEntityCollection()) {
					for (ComposingElement element : entity
							.getComposingElementCollection()) {
						for (Function function : element
								.getInherentFunctionCollection()) {
							if (function instanceof OtherFunction) {
								storedFunctions.add(((OtherFunction) function)
										.getType());
							} else {
								storedFunctions.add(ClassUtils
										.getShortClassName(function.getClass()
												.getCanonicalName()));
							}
						}
					}
				}
			}
			if (particleSample.getSampleComposition()
					.getFunctionalizingEntityCollection() != null) {
				for (FunctionalizingEntity entity : particleSample
						.getSampleComposition()
						.getFunctionalizingEntityCollection()) {
					for (Function function : entity.getFunctionCollection()) {
						if (function instanceof OtherFunction) {
							storedFunctions.add(((OtherFunction) function)
									.getType());
						} else {
							storedFunctions.add(ClassUtils
									.getShortClassName(function.getClass()
											.getCanonicalName()));
						}
					}
				}
			}
		}
		return storedFunctions;
	}

	/**
	 * Return all stored nanoparticle entity class names. In case of
	 * OtherNanoparticleEntity, store the otherNanoparticleEntity type
	 * 
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredNanoparticleEntityClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null
				&& particleSample.getSampleComposition()
						.getNanoparticleEntityCollection() != null) {
			for (NanoparticleEntity entity : particleSample
					.getSampleComposition().getNanoparticleEntityCollection()) {
				if (entity instanceof OtherNanoparticleEntity) {
					storedEntities.add(((OtherNanoparticleEntity) entity)
							.getType());
				} else {
					storedEntities.add(ClassUtils.getShortClassName(entity
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedEntities;
	}

	public SortedSet<String> getStoredCharacterizationClassNames(
			NanoparticleSample particle) {
		SortedSet<String> storedChars = new TreeSet<String>();
		if (particle.getCharacterizationCollection() != null) {
			for (Characterization achar : particle
					.getCharacterizationCollection()) {
				storedChars.add(ClassUtils.getShortClassName(achar.getClass()
						.getCanonicalName()));
			}
		}
		return storedChars;
	}

	private List<NanoparticleSample> filterByFunctions(
			String[] functionClassNames, String[] otherFunctionTypes,
			List<NanoparticleSample> particles) {
		if (functionClassNames != null && functionClassNames.length > 0) {
			List<NanoparticleSample> filteredSet = new ArrayList<NanoparticleSample>();
			for (NanoparticleSample particle : particles) {
				SortedSet<String> storedFunctions = getStoredFunctionClassNames(particle);
				for (String func : functionClassNames) {
					// if at least one function type matches, keep the particle
					if (storedFunctions.contains(func)) {
						filteredSet.add(particle);
						break;
					}
				}
				if (otherFunctionTypes != null) {
					for (String other : otherFunctionTypes) {
						if (storedFunctions.contains(other)) {
							filteredSet.add(particle);
							break;
						}
					}
				}
			}
			return filteredSet;
		} else {
			return particles;
		}
	}

	private List<NanoparticleSample> filterByCharacterizations(
			String[] characterizationClassNames,
			List<NanoparticleSample> particles) {
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0) {
			List<NanoparticleSample> filteredSet = new ArrayList<NanoparticleSample>();
			for (NanoparticleSample particle : particles) {
				SortedSet<String> storedChars = getStoredCharacterizationClassNames(particle);
				for (String func : characterizationClassNames) {
					// if at least one characterization type matches, keep the
					// particle
					if (storedChars.contains(func)) {
						filteredSet.add(particle);
						break;
					}
				}
			}
			return filteredSet;
		} else {
			return particles;
		}
	}

	private List<NanoparticleSample> filterByCompositions(
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			List<NanoparticleSample> particles) {

		List<NanoparticleSample> filteredSet1 = new ArrayList<NanoparticleSample>();
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0) {
			for (NanoparticleSample particle : particles) {
				SortedSet<String> storedEntities = getStoredNanoparticleEntityClassNames(particle);
				for (String entity : nanoparticleEntityClassNames) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(entity)) {
						filteredSet1.add(particle);
						break;
					}
				}
				if (otherNanoparticleEntityTypes != null) {
					for (String other : otherNanoparticleEntityTypes) {
						// if at least one function type matches, keep the
						// particle
						if (storedEntities.contains(other)) {
							filteredSet1.add(particle);
							break;
						}
					}
				}
			}
		} else {
			filteredSet1 = particles;
		}
		List<NanoparticleSample> filteredSet2 = new ArrayList<NanoparticleSample>();
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0) {
			for (NanoparticleSample particle : particles) {
				SortedSet<String> storedEntities = getStoredFunctionalizingEntityClassNames(particle);
				for (String entity : functionalizingEntityClassNames) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(entity)) {
						filteredSet2.add(particle);
						break;
					}
				}
				if (otherFunctionalizingEntityTypes != null) {
					for (String other : otherFunctionalizingEntityTypes) {
						// if at least one function type matches, keep the
						// particle
						if (storedEntities.contains(other)) {
							filteredSet2.add(particle);
							break;
						}
					}
				}
			}
		} else {
			filteredSet2 = particles;
		}
		if (filteredSet1.size() > filteredSet2.size()) {
			filteredSet1.retainAll(filteredSet2);
			return filteredSet1;
		} else {
			filteredSet2.retainAll(filteredSet1);
			return filteredSet2;
		}
	}

	public NanoparticleSample findNanoparticleSampleById(String particleId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("id").eq(new Long(particleId)));
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
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
		crit.setFetchMode("reportCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		NanoparticleSample particleSample = null;
		if (!result.isEmpty()) {
			particleSample = (NanoparticleSample) result.get(0);
		}
		return particleSample;
	}

	public NanoparticleSample findNanoparticleSampleByName(String particleName)
			throws Exception {
		NanoparticleSample particleSample = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("name").eq(particleName));
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
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
		crit.setFetchMode("reportCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			particleSample = (NanoparticleSample) result.get(0);
		}
		return particleSample;
	}

	public SortedSet<Source> findAllParticleSources() throws Exception {
		SortedSet<Source> sampleSources = new TreeSet<Source>(
				new CaNanoLabComparators.ParticleSourceComparator());
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		List results = appService.getAll(Source.class);
		for (Object obj : results) {
			sampleSources.add((Source) obj);
		}
		return sampleSources;
	}

	public Collection<DerivedBioAssayData> findDerivedBioAssayDataByCharId(
			String charId) throws ParticleCharacterizationException {
		Collection<DerivedBioAssayData> derivedBioAssayDataCollection = new ArrayList<DerivedBioAssayData>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select achar.derivedBioAssayDataCollection from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar where achar.id = "
							+ charId);
			List results = appService.query(crit);
			FileServiceHelper fileHelper = new FileServiceHelper();
			for (Object obj : results) {
				DerivedBioAssayData derivedBioAssayData = (DerivedBioAssayData) obj;
				// derivedBioAssayData's labfile
				LabFile labFile = findDerivedBioAssayDataLabFile(derivedBioAssayData
						.getId().toString());

				// labFile's keyword
				Collection<Keyword> keywords = fileHelper
						.findKeywordsByFileId(labFile.getId().toString());
				labFile.setKeywordCollection(new HashSet<Keyword>(keywords));

				derivedBioAssayData.setLabFile(labFile);
				derivedBioAssayDataCollection.add(derivedBioAssayData);
			}
		} catch (Exception e) {
			logger
					.error(
							"Problem to retrieve Characterization derivedBioAssayDataCollection.",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve retrieve Characterization derivedBioAssayDataCollection ");
		}
		return derivedBioAssayDataCollection;
	}

	public LabFile findDerivedBioAssayDataLabFile(String derivedId)
			throws ParticleCharacterizationException {
		LabFile labFile = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select bioassay.labFile from gov.nih.nci.cananolab.domain.common.DerivedBioAssayData bioassay where bioassay.id = "
							+ derivedId);
			List results = appService.query(crit);
			for (Object obj : results) {
				labFile = (LabFile) obj;
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve DerivedBioAssayData labFile.", e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve retrieve DerivedBioAssayData labFile ");
		}
		return labFile;
	}

	public void assignAssociatedVisibility(AuthorizationService authService,
			ParticleBean particleSampleBean, String[] visibleGroups)
			throws CaNanoLabSecurityException {
		// remove public group in all associated records
		removeAssociatedVisibility(authService, particleSampleBean);
		if (Arrays.asList(visibleGroups).contains(
				CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
			// set public group in all associated records
			visibleGroups = new String[] { CaNanoLabConstants.CSM_PUBLIC_GROUP };

			NanoparticleSample nanoparticleSample = particleSampleBean
					.getDomainParticleSample();
			// source
			if (nanoparticleSample.getSource() != null) {
				authService.assignVisibility(nanoparticleSample.getSource()
						.getId().toString(), visibleGroups);
			}
			// keyword
			Collection<Keyword> keywordCollection = nanoparticleSample
					.getKeywordCollection();
			if (keywordCollection != null) {
				for (Keyword keyword : keywordCollection) {
					if (keyword != null) {
						authService.assignVisibility(
								keyword.getId().toString(), visibleGroups);
					}
				}
			}
			// characterization
			Collection<Characterization> characterizationCollection = nanoparticleSample
					.getCharacterizationCollection();
			if (characterizationCollection != null) {
				for (Characterization aChar : characterizationCollection) {
					if (aChar != null) {
						authService.assignVisibility(aChar.getId().toString(),
								visibleGroups);
					}
					// char.derivedBioAssayDataCollection
					Collection<DerivedBioAssayData> derivedBioAssayDataCollection = aChar
							.getDerivedBioAssayDataCollection();
					if (derivedBioAssayDataCollection != null) {
						for (DerivedBioAssayData aDerived : derivedBioAssayDataCollection) {
							if (aDerived != null) {
								authService.assignVisibility(aDerived.getId()
										.toString(), visibleGroups);
							}
							// derived.derivedDatum
							Collection<DerivedDatum> derivedDatumCollection = aDerived
									.getDerivedDatumCollection();
							if (derivedDatumCollection != null) {
								for (DerivedDatum aDerivedDatum : derivedDatumCollection) {
									if (aDerivedDatum != null) {
										authService.assignVisibility(
												aDerivedDatum.getId()
														.toString(),
												visibleGroups);
									}
								}
							}
						}
					}
					// InstrumentConfiguration
					if (aChar.getInstrumentConfiguration() != null) {
						authService.assignVisibility(aChar
								.getInstrumentConfiguration().getId()
								.toString(), visibleGroups);
						// InstrumentConfiguration.Instrument
						if (aChar.getInstrumentConfiguration().getInstrument() != null) {
							authService.assignVisibility(aChar
									.getInstrumentConfiguration()
									.getInstrument().getId().toString(),
									visibleGroups);
						}
					}
				}
			}
			// sampleComposition
			if (nanoparticleSample.getSampleComposition() != null) {
				authService.assignVisibility(nanoparticleSample
						.getSampleComposition().getId().toString(),
						visibleGroups);

				// sampleComposition.nanoparticleEntityCollection,
				Collection<NanoparticleEntity> nanoparticleEntityCollection = nanoparticleSample
						.getSampleComposition()
						.getNanoparticleEntityCollection();
				if (nanoparticleEntityCollection != null) {
					for (NanoparticleEntity nanoparticleEntity : nanoparticleEntityCollection) {
						if (nanoparticleEntity != null) {
							authService.assignVisibility(nanoparticleEntity
									.getId().toString(), visibleGroups);
						}
						// nanoparticleEntityCollection.composingElementCollection,
						Collection<ComposingElement> composingElementCollection = nanoparticleEntity
								.getComposingElementCollection();
						if (composingElementCollection != null) {
							for (ComposingElement composingElement : composingElementCollection) {
								if (composingElement != null) {
									authService
											.assignVisibility(composingElement
													.getId().toString(),
													visibleGroups);
								}
								// composingElementCollection.inherentFucntionCollection
								Collection<Function> inherentFunctionCollection = composingElement
										.getInherentFunctionCollection();
								if (inherentFunctionCollection != null) {
									for (Function function : inherentFunctionCollection) {
										if (function != null) {
											authService
													.assignVisibility(
															function.getId()
																	.toString(),
															visibleGroups);
										}
									}
								}
							}
						}
					}
				}
				// sampleComposition.functionalizingEntityCollection,
				Collection<FunctionalizingEntity> functionalizingEntityCollection = nanoparticleSample
						.getSampleComposition()
						.getFunctionalizingEntityCollection();
				if (functionalizingEntityCollection != null) {
					for (FunctionalizingEntity functionalizingEntity : functionalizingEntityCollection) {
						if (functionalizingEntity != null) {
							authService.assignVisibility(functionalizingEntity
									.getId().toString(), visibleGroups);
						}
						// functionalizingEntityCollection.functionCollection
						Collection<Function> functionCollection = functionalizingEntity
								.getFunctionCollection();
						if (functionCollection != null) {
							for (Function function : functionCollection) {
								if (function != null) {
									authService.assignVisibility(function
											.getId().toString(), visibleGroups);
								}
							}
						}
					}
				}
				// sampleComposition.chemicalAssociationCollection
				Collection<ChemicalAssociation> chemicalAssociationCollection = nanoparticleSample
						.getSampleComposition()
						.getChemicalAssociationCollection();
				if (functionalizingEntityCollection != null) {
					for (ChemicalAssociation chemicalAssociation : chemicalAssociationCollection) {
						if (chemicalAssociation != null) {
							authService.assignVisibility(chemicalAssociation
									.getId().toString(), visibleGroups);
							// chemicalAssociation.associatedElementA
							if (chemicalAssociation.getAssociatedElementA() != null) {
								authService.assignVisibility(
										chemicalAssociation
												.getAssociatedElementA()
												.getId().toString(),
										visibleGroups);
							}
							// chemicalAssociation.associatedElementB
							if (chemicalAssociation.getAssociatedElementB() != null) {
								authService.assignVisibility(
										chemicalAssociation
												.getAssociatedElementB()
												.getId().toString(),
										visibleGroups);
							}
						}
					}
				}
			}

			// sample management
			SampleManagement sampleManagement = nanoparticleSample
					.getSampleManagement();
			if (sampleManagement != null) {
				authService.assignVisibility(sampleManagement.getId()
						.toString(), visibleGroups);

				// sampleManagement.sampleContainer
				Collection<SampleContainer> sampleContainerCollection = sampleManagement
						.getSampleContainerCollection();
				if (sampleContainerCollection != null) {
					for (SampleContainer sampleContainer : sampleContainerCollection) {
						if (sampleContainer != null) {
							authService.assignVisibility(sampleContainer
									.getId().toString(), visibleGroups);
						}
						// sampleContainer.storageElement
						Collection<StorageElement> storageElementCollection = sampleContainer
								.getStorageElementCollection();
						if (storageElementCollection != null) {
							for (StorageElement storageElement : storageElementCollection) {
								if (storageElement != null) {
									authService.assignVisibility(storageElement
											.getId().toString(), visibleGroups);
								}
							}
						}
						// sampleContainer.aliquot
						Collection<Aliquot> sliquotCollection = sampleContainer
								.getAliquotCollection();
						if (sliquotCollection != null) {
							for (Aliquot aliquot : sliquotCollection) {
								if (aliquot != null) {
									authService.assignVisibility(aliquot
											.getId().toString(), visibleGroups);
								}
							}
						}
					}
				}
			}
		}
	}

	public void removeAssociatedVisibility(AuthorizationService authService,
			ParticleBean particleSampleBean) throws CaNanoLabSecurityException {
		// remove public group in all associated records
		NanoparticleSample nanoparticleSample = particleSampleBean
				.getDomainParticleSample();

		// source
		if (nanoparticleSample.getSource() != null) {
			authService.removePublicGroup(nanoparticleSample.getSource()
					.getId().toString());
		}
		// keyword
		Collection<Keyword> keywordCollection = nanoparticleSample
				.getKeywordCollection();
		if (keywordCollection != null) {
			for (Keyword keyword : keywordCollection) {
				if (keyword != null) {
					authService.removePublicGroup(keyword.getId().toString());
				}
			}
		}
		// characterization
		Collection<Characterization> characterizationCollection = nanoparticleSample
				.getCharacterizationCollection();
		if (characterizationCollection != null) {
			for (Characterization aChar : characterizationCollection) {
				if (aChar != null) {
					authService.removePublicGroup(aChar.getId().toString());
				}
				// char.derivedBioAssayDataCollection
				Collection<DerivedBioAssayData> derivedBioAssayDataCollection = aChar
						.getDerivedBioAssayDataCollection();
				if (derivedBioAssayDataCollection != null) {
					for (DerivedBioAssayData aDerived : derivedBioAssayDataCollection) {
						if (aDerived != null) {
							authService.removePublicGroup(aDerived.getId()
									.toString());
						}
						// derived.derivedDatum
						Collection<DerivedDatum> derivedDatumCollection = aDerived
								.getDerivedDatumCollection();
						if (derivedDatumCollection != null) {
							for (DerivedDatum aDerivedDatum : derivedDatumCollection) {
								if (aDerivedDatum != null) {
									authService.removePublicGroup(aDerivedDatum
											.getId().toString());
								}
							}
						}
					}
				}
				// InstrumentConfiguration
				InstrumentConfiguration instrumentConfiguration = aChar
						.getInstrumentConfiguration();
				if (instrumentConfiguration != null) {
					authService.removePublicGroup(instrumentConfiguration
							.getId().toString());
				}
				// InstrumentConfiguration.Instrument
				if (instrumentConfiguration.getInstrument() != null) {
					authService.removePublicGroup(aChar
							.getInstrumentConfiguration().getInstrument()
							.getId().toString());
				}
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
					if (nanoparticleEntity != null) {
						authService.removePublicGroup(nanoparticleEntity
								.getId().toString());
					}
					// nanoparticleEntityCollection.composingElementCollection,
					Collection<ComposingElement> composingElementCollection = nanoparticleEntity
							.getComposingElementCollection();
					if (composingElementCollection != null) {
						for (ComposingElement composingElement : composingElementCollection) {
							if (composingElement != null) {
								authService.removePublicGroup(composingElement
										.getId().toString());
							}
							// composingElementCollection.inherentFucntionCollection
							Collection<Function> inherentFunctionCollection = composingElement
									.getInherentFunctionCollection();
							if (inherentFunctionCollection != null) {
								for (Function function : inherentFunctionCollection) {
									if (function != null) {
										authService.removePublicGroup(function
												.getId().toString());
									}
								}
							}
						}
					}
				}
			}
			// sampleComposition.functionalizingEntityCollection,
			Collection<FunctionalizingEntity> functionalizingEntityCollection = nanoparticleSample
					.getSampleComposition()
					.getFunctionalizingEntityCollection();
			if (functionalizingEntityCollection != null) {
				for (FunctionalizingEntity functionalizingEntity : functionalizingEntityCollection) {
					if (functionalizingEntity != null) {
						authService.removePublicGroup(functionalizingEntity
								.getId().toString());
					}
					// functionalizingEntityCollection.functionCollection
					Collection<Function> functionCollection = functionalizingEntity
							.getFunctionCollection();
					if (functionCollection != null) {
						for (Function function : functionCollection) {
							if (function != null) {
								authService.removePublicGroup(function.getId()
										.toString());
							}
						}
					}
				}
			}
			// sampleComposition.chemicalAssociationCollection
			Collection<ChemicalAssociation> chemicalAssociationCollection = nanoparticleSample
					.getSampleComposition().getChemicalAssociationCollection();
			if (functionalizingEntityCollection != null) {
				for (ChemicalAssociation chemicalAssociation : chemicalAssociationCollection) {
					if (chemicalAssociation != null) {
						authService.removePublicGroup(chemicalAssociation
								.getId().toString());
						// chemicalAssociation.associatedElementA
						if (chemicalAssociation.getAssociatedElementA() != null) {
							authService
									.removePublicGroup(chemicalAssociation
											.getAssociatedElementA().getId()
											.toString());
						}
						// chemicalAssociation.associatedElementB
						if (chemicalAssociation.getAssociatedElementB() != null) {
							authService
									.removePublicGroup(chemicalAssociation
											.getAssociatedElementB().getId()
											.toString());
						}
					}
				}
			}
		}
		// sample management
		SampleManagement sampleManagement = nanoparticleSample
				.getSampleManagement();
		if (sampleManagement != null) {
			authService.removePublicGroup(sampleManagement.getId().toString());

			// sampleManagement.sampleContainer
			Collection<SampleContainer> sampleContainerCollection = sampleManagement
					.getSampleContainerCollection();
			if (sampleContainerCollection != null) {
				for (SampleContainer sampleContainer : sampleContainerCollection) {
					if (sampleContainer != null) {
						authService.removePublicGroup(sampleContainer.getId()
								.toString());
					}
					// sampleContainer.storageElement
					Collection<StorageElement> storageElementCollection = sampleContainer
							.getStorageElementCollection();
					if (storageElementCollection != null) {
						for (StorageElement storageElement : storageElementCollection) {
							if (storageElement != null) {
								authService.removePublicGroup(storageElement
										.getId().toString());
							}
						}
					}
					// sampleContainer.aliquot
					Collection<Aliquot> aliquotCollection = sampleContainer
							.getAliquotCollection();
					if (aliquotCollection != null) {
						for (Aliquot aliquot : aliquotCollection) {
							if (aliquot != null) {
								authService.removePublicGroup(aliquot.getId()
										.toString());
							}
						}
					}
				}
			}
		}
	}

	public Collection<Keyword> findKeywordsForNanoparticleSampleId(
			String particleSampleId) throws Exception {
		Collection<Keyword> keywords = new ArrayList<Keyword>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();		
			HQLCriteria crit = new HQLCriteria(
					"select aParticle.keywordCollection from gov.nih.nci.cananolab.domain.particle.NanoparticleSample aParticle where aParticle.id = "+
					particleSampleId);
			List results = appService.query(crit);
			for (Object obj : results) {
				Keyword keyword = (Keyword) obj;
				keywords.add(keyword);
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve NanoparticleSample keywordCollection.",
							e);
			throw new ParticleException(
					"Problem to retrieve retrieve NanoparticleSample keywordCollection ");
		}
		return keywords;
	}
}