package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate helper class for local implementation of
 * NanoparticleSampleSearchInterface and grid service implementation.
 * 
 * @author pansu
 * 
 */
public class NanoparticleSampleSearchHibernateHelper {
	public List<NanoparticleSample> findNanoparticleSamplesBy(
			String[] particleSources, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws Exception {
		List<NanoparticleSample> particles = new ArrayList<NanoparticleSample>();

		DetachedCriteria crit = DetachedCriteria
				.forClass(NanoparticleSample.class);
		if (particleSources != null && particleSources.length > 0) {
			crit.createAlias("source", "source",
					CriteriaSpecification.LEFT_JOIN)
					.add(
							Restrictions.in("source.organizationName",
									particleSources));
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
				for (String other : otherFunctionTypes) {
					if (storedFunctions.contains(other)) {
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
				for (String other : otherNanoparticleEntityTypes) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(other)) {
						filteredSet1.add(particle);
						break;
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
				for (String other : otherFunctionalizingEntityTypes) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(other)) {
						filteredSet2.add(particle);
						break;
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
}