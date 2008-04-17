package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
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
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
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
 * This class includes service calls involved around a nanoparticle sample
 * 
 * @author pansu
 * 
 */
public class NanoparticleSampleService {
	private static Logger logger = Logger
			.getLogger(NanoparticleSampleService.class);

	/**
	 * 
	 * @return all particle sources
	 */
	public SortedSet<Source> getAllParticleSources() throws ParticleException {
		SortedSet<Source> sampleSources = new TreeSet<Source>(
				new CaNanoLabComparators.ParticleSourceComparator());
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(Source.class);
			for (Object obj : results) {
				sampleSources.add((Source) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all nanoparticle sample sources",
					e);
			throw new ParticleException();
		}
		return sampleSources;
	}

	/**
	 * 
	 * @return all particle sources visible to user
	 */
	public SortedSet<Source> getAllParticleSources(UserBean user)
			throws ParticleException, CaNanoLabSecurityException {
		SortedSet<Source> sampleSources = new TreeSet<Source>(
				new CaNanoLabComparators.ParticleSourceComparator());
		AuthorizationService auth = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Source.class);
			crit.createAlias("nanoparticleSampleCollection", "particle");
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
			List results = appService.query(crit);
			for (Object obj : results) {
				Source source = (Source) obj;
				boolean status = false;
				// if user can access at least one particle from the source, set
				// access to true
				for (NanoparticleSample particle : source
						.getNanoparticleSampleCollection()) {
					if (auth.checkReadPermission(user, particle.getName())) {
						status = true;
						break;
					}
				}
				if (status) {
					sampleSources.add((Source) obj);
				}
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in retrieving all nanoparticle sample sources for a user",
							e);
			throw new ParticleException();
		}

		return sampleSources;
	}

	/**
	 * Persist a new nanoparticle sample or update an existing nanoparticle
	 * sample
	 * 
	 * @param particleSampleBean
	 * @throws Exception
	 */
	public void saveNanoparticleSample(ParticleBean particleBean)
			throws Exception {
		NanoparticleSample particleSample = particleBean.getParticleSample();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		NanoparticleSample dbParticle = (NanoparticleSample) appService
				.getObject(NanoparticleSample.class, "name", particleSample
						.getName());
		if (dbParticle != null
				&& !dbParticle.getId().equals(particleSample.getId())) {
			throw new DuplicateEntriesException(
					"This nanoparticle sample ID has already been used.  Please use a different one");
		}

		Source dbSource = (Source) appService.getObject(Source.class,
				"organizationName", particleSample.getSource()
						.getOrganizationName());
		if (dbSource != null) {
			particleSample.setSource(dbSource);
		}

		Collection<Keyword> keywords = new HashSet<Keyword>();
		for (Keyword keyword : particleSample.getKeywordCollection()) {
			Keyword dbKeyword = (Keyword) appService.getObject(Keyword.class,
					"name", keyword.getName());
			if (dbKeyword != null) {
				keywords.add(dbKeyword);
			} else {
				keywords.add(keyword);
			}
		}
		particleSample.setKeywordCollection(keywords);
		appService.saveOrUpdate(particleSample);
	}

	/**
	 * Return user-defined function types
	 * 
	 * @return
	 * @throws ParticleException
	 */
	public SortedSet<String> getAllOtherFunctionTypes()
			throws ParticleException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherFunction.class);
			for (Object obj : results) {
				OtherFunction other = (OtherFunction) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			logger.error("Error in retrieving other function types", e);
			throw new ParticleException();
		}
		return types;
	}

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleException
	 */
	public SortedSet<String> getAllOtherNanoparticleEntityTypes()
			throws ParticleException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherNanoparticleEntity.class);
			for (Object obj : results) {
				OtherNanoparticleEntity other = (OtherNanoparticleEntity) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			logger.error(
					"Error in retrieving other values for nanoparticle entity",
					e);
			throw new ParticleException();
		}
		return types;
	}

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleException
	 */
	public SortedSet<String> getAllOtherFunctionalizingEntityTypes()
			throws ParticleException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherFunctionalizingEntity.class);
			for (Object obj : results) {
				OtherFunctionalizingEntity other = (OtherFunctionalizingEntity) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in retrieving other values for functionalizing entity",
							e);
			throw new ParticleException();
		}
		return types;
	}

	/**
	 * 
	 * @param particleSource
	 * @param nanoparticleEntityClassNames
	 * @param functionalizingEntityClassNames
	 * @param functionClassNames
	 * @param characterizationClassNames
	 * @param keywords
	 * @param summaries
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public List<ParticleBean> findNanoparticleSamplesBy(String particleSource,
			String[] nanoparticleEntityClassNames,
			String[] functionalizingEntityClassNames,
			String[] functionClassNames, String[] characterizationClassNames,
			String[] wordList, UserBean user) throws ParticleException,
			CaNanoLabSecurityException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		AuthorizationService auth = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);

		try {
			DetachedCriteria crit = DetachedCriteria
					.forClass(NanoparticleSample.class);
			if (particleSource != null && particleSource.length() > 0) {
				crit.createAlias("source", "source").add(
						Restrictions.eq("source.organizationName",
								particleSource));
			}

			if (wordList != null && wordList.length > 0) {
				// turn words into upper case before searching keywords
				String[] upperKeywords = new String[wordList.length];
				for (int i = 0; i < wordList.length; i++) {
					upperKeywords[i] = wordList[i].toUpperCase();
				}

				crit.createAlias("keywordCollection", "keyword1",
						CriteriaSpecification.LEFT_JOIN);
				Criterion keywordCrit1 = Restrictions.in("keyword1.name",
						upperKeywords);

				crit.createAlias("characterizationCollection", "chara",
						CriteriaSpecification.LEFT_JOIN).createAlias(
						"chara.derivedBioAssayDataCollection", "derived",
						CriteriaSpecification.LEFT_JOIN).createAlias(
						"derived.labFile", "charFile",
						CriteriaSpecification.LEFT_JOIN).createAlias(
						"charFile.keywordCollection", "keyword2",
						CriteriaSpecification.LEFT_JOIN);
				;
				Criterion keywordCrit2 = Restrictions.in("keyword2.name",
						upperKeywords);
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(keywordCrit1).add(keywordCrit2);

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
			crit.setFetchMode(
					"sampleComposition.functionalizingEntityCollection",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List results = appService.query(crit);
			for (Object obj : results) {
				NanoparticleSample particle = (NanoparticleSample) obj;
				// check if user can see the particle
				boolean status = auth.checkReadPermission(user, particle
						.getName());
				if (status) {
					particles.add(new ParticleBean(particle));
				}
			}
			List<ParticleBean> functionFilteredParticles = filterByFunctions(
					functionClassNames, particles);
			List<ParticleBean> characterizationFilteredParticles = filterByCharacterizations(
					characterizationClassNames, functionFilteredParticles);
			List<ParticleBean> compositionFilteredParticles = filterByCompositions(
					nanoparticleEntityClassNames,
					functionalizingEntityClassNames,
					characterizationFilteredParticles);

			// TODO sort particles
			return compositionFilteredParticles;

		} catch (Exception e) {
			logger
					.error(
							"Problem finding particles with the given search parameters ",
							e);
			throw new ParticleException();
		}
	}

	private List<ParticleBean> filterByFunctions(String[] functionClassNames,
			List<ParticleBean> particles) {
		if (functionClassNames != null && functionClassNames.length > 0) {
			List<ParticleBean> filteredList = new ArrayList<ParticleBean>();
			for (ParticleBean particle : particles) {
				SortedSet<String> storedFunctions = getStoredFunctionClassNames(particle);
				for (String func : functionClassNames) {
					// if at least one function type matches, keep the particle
					if (storedFunctions.contains(func)) {
						filteredList.add(particle);
						break;
					}
				}
			}
			return filteredList;
		} else {
			return particles;
		}
	}

	private List<ParticleBean> filterByCharacterizations(
			String[] characterizationClassNames, List<ParticleBean> particles) {
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0) {
			List<ParticleBean> filteredList = new ArrayList<ParticleBean>();
			for (ParticleBean particle : particles) {
				SortedSet<String> storedChars = getStoredCharacterizationClassNames(particle);
				for (String func : characterizationClassNames) {
					// if at least one characterization type matches, keep the
					// particle
					if (storedChars.contains(func)) {
						filteredList.add(particle);
						break;
					}
				}
			}
			return filteredList;
		} else {
			return particles;
		}
	}

	private List<ParticleBean> filterByCompositions(
			String[] nanoparticleEntityClassNames,
			String[] functionalizingEntityClassNames,
			List<ParticleBean> particles) {

		List<ParticleBean> filteredList1 = new ArrayList<ParticleBean>();
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0) {
			for (ParticleBean particle : particles) {
				SortedSet<String> storedEntities = getStoredNanoparticleEntityClassNames(particle);
				for (String entity : nanoparticleEntityClassNames) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(entity)) {
						filteredList1.add(particle);
						break;
					}
				}
			}
		} else {
			filteredList1 = particles;
		}
		List<ParticleBean> filteredList2 = new ArrayList<ParticleBean>();
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0) {
			for (ParticleBean particle : particles) {
				SortedSet<String> storedEntities = getStoredFunctionalizingEntityClassNames(particle);
				for (String entity : functionalizingEntityClassNames) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(entity)) {
						filteredList2.add(particle);
						break;
					}
				}
			}
		} else {
			filteredList2 = particles;
		}
		if (filteredList1.size() > filteredList2.size()) {
			filteredList1.retainAll(filteredList2);
			return filteredList1;
		} else {
			filteredList2.retainAll(filteredList1);
			return filteredList2;
		}
	}

	public SortedSet<String> getStoredCharacterizationClassNames(
			ParticleBean particle) {
		SortedSet<String> storedChars = new TreeSet<String>();
		for (Characterization achar : particle.getParticleSample()
				.getCharacterizationCollection()) {
			storedChars.add(ClassUtils.getShortClassName(achar.getClass()
					.getCanonicalName()));
		}
		return storedChars;
	}

	public SortedSet<String> getStoredNanoparticleEntityClassNames(
			ParticleBean particle) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particle.getParticleSample().getSampleComposition() != null) {
			for (NanoparticleEntity entity : particle.getParticleSample()
					.getSampleComposition().getNanoparticleEntityCollection()) {
				storedEntities.add(ClassUtils.getShortClassName(entity
						.getClass().getCanonicalName()));
			}
		}
		return storedEntities;
	}

	public SortedSet<String> getStoredFunctionalizingEntityClassNames(
			ParticleBean particle) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particle.getParticleSample().getSampleComposition() != null) {
			for (FunctionalizingEntity entity : particle.getParticleSample()
					.getSampleComposition()
					.getFunctionalizingEntityCollection()) {
				storedEntities.add(ClassUtils.getShortClassName(entity
						.getClass().getCanonicalName()));
			}
		}
		return storedEntities;
	}

	public SortedSet<String> getStoredFunctionClassNames(ParticleBean particle) {
		SortedSet<String> storedFunctions = new TreeSet<String>();

		if (particle.getParticleSample().getSampleComposition() != null) {
			for (NanoparticleEntity entity : particle.getParticleSample()
					.getSampleComposition().getNanoparticleEntityCollection()) {
				for (ComposingElement element : entity
						.getComposingElementCollection()) {
					for (Function function : element
							.getInherentFunctionCollection()) {
						storedFunctions.add(ClassUtils
								.getShortClassName(function.getClass()
										.getCanonicalName()));
					}
				}
			}
			for (FunctionalizingEntity entity : particle.getParticleSample()
					.getSampleComposition()
					.getFunctionalizingEntityCollection()) {
				for (Function function : entity.getFunctionCollection()) {
					storedFunctions.add(ClassUtils.getShortClassName(function
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedFunctions;
	}

	public ParticleBean findNanoparticleSampleBy(String particleId,
			UserBean user) throws ParticleException, CaNanoLabSecurityException {
		ParticleBean particleBean = null;
		AuthorizationService auth = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleSample.class).add(
					Property.forName("id").eq(new Long(particleId)));

			crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.chemicalAssociationCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.functionalizingEntityCollection",
					FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.labFileCollection",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				NanoparticleSample particleSample = (NanoparticleSample) result
						.get(0);
				// check if user can see the particle
				boolean status = auth.checkReadPermission(user, particleSample
						.getName());
				if (status) {
					particleBean = new ParticleBean(particleSample);
				} else {
					throw new NoAccessException();
				}
				return particleBean;
			} else {
				return null;
			}

		} catch (Exception e) {
			logger
					.error("Problem finding the particle by id: " + particleId,
							e);
			throw new ParticleException();
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
	public SortedSet<NanoparticleSample> getOtherParticles(
			String particleSource, String particleName, UserBean user)
			throws ParticleException, CaNanoLabSecurityException {
		SortedSet<NanoparticleSample> otherParticles = new TreeSet<NanoparticleSample>();
		AuthorizationService auth = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		try {
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
				// check if user can see the particle
				boolean status = auth.checkReadPermission(user, particle
						.getName());
				if (status) {
					otherParticles.add(particle);
				}
			}
		} catch (Exception e) {
			String err = "Error in retrieving other particles from source "
					+ particleSource;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
		return otherParticles;
	}
}
