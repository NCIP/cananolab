package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
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
	 * Persist a new nanoparticle sample
	 * 
	 * @param particleSampleBean
	 * @throws Exception
	 */
	public void createNewNanoparticleSample(ParticleBean particleBean)
			throws Exception {
		NanoparticleSample particleSample = particleBean.getParticleSample();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		NanoparticleSample dbParticle = (NanoparticleSample) appService
				.getObject(NanoparticleSample.class, "name", particleSample
						.getName());
		if (dbParticle != null) {
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

	public List<ParticleBean> findNanoparticleSamplesBy(String particleSource,
			String[] nanoparticleEntityTypes,
			String[] functionalizingEntityTypes, String[] functionTypes,
			String[] characterizations, String[] keywords, String[] summaries,
			UserBean user) throws ParticleException, CaNanoLabSecurityException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();

		try {
			DetachedCriteria crit = DetachedCriteria
					.forClass(NanoparticleSample.class);
			if (particleSource != null && particleSource.length() > 0) {
				crit.createAlias("source", "source").add(
						Restrictions.eq("source.organizationName",
								particleSource));
			}

			if (keywords != null && keywords.length > 0) {
				// turn keywords into upper case before search
				String[] upperKeywords = new String[keywords.length];
				for (int i = 0; i < keywords.length; i++) {
					upperKeywords[i] = keywords[i].toUpperCase();
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

				crit.add(Restrictions.or(keywordCrit1, keywordCrit2));

			}
			if (functionTypes != null && functionTypes.length > 0) {

			}
			if (summaries != null && summaries.length > 0) {
				for (String summary : summaries) {

				}
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
				ParticleBean particleBean = new ParticleBean(particle);
				particles.add(particleBean);
			}
			return particles;

		} catch (Exception e) {
			logger
					.error(
							"Problem finding particles with the given search parameters ",
							e);
			throw new ParticleException();
		}
	}
}
