package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.SortableName;
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
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
			List results = appService.query(crit);
			for (Object obj : results) {
				Source source = (Source) obj;
				boolean status = false;
				// if user can access at least one particle from the source, set
				// access to true
				for (NanoparticleSample particle : source
						.getNanoparticleSampleCollection()) {
					if (isAllowed(auth, particle.getName(), user)) {
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
	public void saveNanoparticleSample(NanoparticleSample particleSample)
			throws Exception {
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
			particleSample.getSource().setId(dbSource.getId());
		}

		for (Keyword keyword : particleSample.getKeywordCollection()) {
			Keyword dbKeyword = (Keyword) appService.getObject(Keyword.class,
					"name", keyword.getName());
			if (dbKeyword != null) {
				keyword.setId(dbKeyword.getId());
			}
		}
		appService.saveOrUpdate(particleSample);
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
	public List<ParticleBean> findNanoparticleSamplesBy(
			String[] particleSources, String[] nanoparticleEntityClassNames,
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
			if (particleSources != null && particleSources.length > 0) {
				crit.createAlias("source", "source").add(
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
			crit.setFetchMode("source", FetchMode.JOIN);
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
				particles.add(new ParticleBean(particle));
			}
			List<ParticleBean> functionFilteredParticles = filterByFunctions(
					functionClassNames, particles);
			List<ParticleBean> characterizationFilteredParticles = filterByCharacterizations(
					characterizationClassNames, functionFilteredParticles);
			List<ParticleBean> theParticles = filterByCompositions(
					nanoparticleEntityClassNames,
					functionalizingEntityClassNames,
					characterizationFilteredParticles);
			// TODO sort particles
			return getAllowedParticles(auth, theParticles, user);
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

	public ParticleBean findNanoparticleSampleById(String particleId,
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
			crit.setFetchMode("source", FetchMode.JOIN);
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
				particleBean = new ParticleBean(particleSample);
				if (isAllowed(auth, particleSample.getName(), user)) {
					return particleBean;
				} else {
					throw new NoAccessException();
				}
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

	public ParticleBean findNanoparticleSampleByName(String particleName,
			UserBean user) throws ParticleException, CaNanoLabSecurityException {
		ParticleBean particleBean = null;
		AuthorizationService auth = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleSample.class).add(
					Property.forName("name").eq(new Long(particleName)));
			crit.setFetchMode("source", FetchMode.JOIN);
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
				particleBean = new ParticleBean(particleSample);
				if (isAllowed(auth, particleSample.getName(), user)) {
					return particleBean;
				} else {
					throw new NoAccessException();
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.error("Problem finding the particle by name: "
					+ particleName, e);
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
	public SortedSet<SortableName> getOtherParticles(String particleSource,
			String particleName, UserBean user) throws ParticleException,
			CaNanoLabSecurityException {
		SortedSet<SortableName> otherParticles = new TreeSet<SortableName>();
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
				if (isAllowed(auth, particle.getName(), user)) {
					otherParticles.add(new SortableName(particle.getName()));
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

	public List<ParticleBean> getAllowedParticles(AuthorizationService auth,
			List<ParticleBean> particles, UserBean user) throws Exception {
		List<String> publicData = auth.getPublicData();
		List<ParticleBean> allowedParticles = new ArrayList<ParticleBean>();
		for (ParticleBean particle : particles) {
			if (publicData.contains(particle.getDomainParticleSample()
					.getName())) {
				allowedParticles.add(particle);
			} else if (user != null) {
				if (auth.checkReadPermission(user, particle
						.getDomainParticleSample().getName())) {
					allowedParticles.add(particle);
				}
			}
		}
		return allowedParticles;
	}

	public boolean isAllowed(AuthorizationService auth, String particleName,
			UserBean user) throws Exception {
		List<String> publicData = auth.getPublicData();
		if (publicData.contains(particleName)) {
			return true;
		} else if (user != null && auth.checkReadPermission(user, particleName)) {
			return true;
		}
		return false;
	}

	public SortedSet<String> getStoredCharacterizationClassNames(
			ParticleBean particle) {
		SortedSet<String> storedChars = new TreeSet<String>();
		if (particle.getDomainParticleSample().getCharacterizationCollection() != null) {
			for (Characterization achar : particle.getDomainParticleSample()
					.getCharacterizationCollection()) {
				storedChars.add(ClassUtils.getShortClassName(achar.getClass()
						.getCanonicalName()));
			}
		}
		return storedChars;
	}

	public SortedSet<String> getStoredFunctionalizingEntityClassNames(
			ParticleBean particle) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particle.getDomainParticleSample().getSampleComposition() != null
				&& particle.getDomainParticleSample().getSampleComposition()
						.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : particle
					.getDomainParticleSample().getSampleComposition()
					.getFunctionalizingEntityCollection()) {
				storedEntities.add(ClassUtils.getShortClassName(entity
						.getClass().getCanonicalName()));
			}
		}
		return storedEntities;
	}

	public SortedSet<String> getStoredFunctionClassNames(ParticleBean particle) {
		SortedSet<String> storedFunctions = new TreeSet<String>();

		if (particle.getDomainParticleSample().getSampleComposition() != null) {
			if (particle.getDomainParticleSample().getSampleComposition()
					.getNanoparticleEntityCollection() != null) {
				for (NanoparticleEntity entity : particle
						.getDomainParticleSample().getSampleComposition()
						.getNanoparticleEntityCollection()) {
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
			}
			if (particle.getDomainParticleSample().getSampleComposition()
					.getFunctionalizingEntityCollection() != null) {
				for (FunctionalizingEntity entity : particle
						.getDomainParticleSample().getSampleComposition()
						.getFunctionalizingEntityCollection()) {
					for (Function function : entity.getFunctionCollection()) {
						storedFunctions.add(ClassUtils
								.getShortClassName(function.getClass()
										.getCanonicalName()));
					}
				}
			}
		}
		return storedFunctions;
	}

	public SortedSet<String> getStoredNanoparticleEntityClassNames(
			ParticleBean particle) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particle.getDomainParticleSample().getSampleComposition() != null
				&& particle.getDomainParticleSample().getSampleComposition()
						.getNanoparticleEntityCollection() != null) {
			for (NanoparticleEntity entity : particle.getDomainParticleSample()
					.getSampleComposition().getNanoparticleEntityCollection()) {
				storedEntities.add(ClassUtils.getShortClassName(entity
						.getClass().getCanonicalName()));
			}
		}
		return storedEntities;
	}

}
