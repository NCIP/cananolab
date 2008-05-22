package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
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
	public List<ParticleBean> findNanoparticleSamplesBy(
			String particleSource, String[] nanoparticleEntityClassNames,
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
			for (NanoparticleSample particleSample : particleSamples) {
				particles.add(new ParticleBean(particleSample));
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

			SortedSet<String> names = new TreeSet<String>();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select sample.name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample sample");
			List results = appService.query(crit);
			for (Object obj : results) {
				String name = (String) obj;
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
}
