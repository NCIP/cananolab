package gov.nih.nci.calab.service.particle;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.common.SearchableBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CaNanoLabException;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ParticleException;
import gov.nih.nci.calab.service.sample.SampleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * This class includes methods invovled in searching and creating nanoparticles.
 * 
 * @author pansu
 * 
 */
public class NanoparticleService {
	private static Logger logger = Logger.getLogger(NanoparticleService.class);

	// remove existing visibilities for the data
	private UserService userService;

	public NanoparticleService() throws CaNanoLabSecurityException {
		this.userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	/**
	 * Search for nanoparticles based on particle source, type, function types,
	 * characterizationType, characterizations, keywords and filter the
	 * nanoparticles by user visibility.
	 * 
	 * @param particleSource
	 * @param particleType
	 * @param functionTypes
	 * @param characterizationType
	 * @param characterizations
	 * @param keywords
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public List<ParticleBean> basicSearch(String particleSource,
			String particleType, String[] functionTypes,
			String[] characterizations, String[] keywords, String keywordType,
			String[] summaries, String summaryType, UserBean user)
			throws ParticleException, CaNanoLabSecurityException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();

		try {
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();

			String where = "";
			String keywordFrom = "";
			String functionFrom = "";
			String characterizationFrom = "";
			String summaryForm = "";

			if (particleSource != null && particleSource.length() > 0) {
				where = "where ";
				whereList.add("particle.source.organizationName=? ");
				paramList.add(particleSource);
			}
			if (particleType != null && particleType.length() > 0) {
				paramList.add(particleType);
				where = "where ";
				whereList.add("particle.type=? ");
			}

			if (functionTypes != null && functionTypes.length > 0) {
				List<String> inList = new ArrayList<String>();
				where = "where ";
				for (String functionType : functionTypes) {
					paramList.add(functionType);
					inList.add("?");
				}
				functionFrom = "join particle.functionCollection function ";
				whereList.add("function.type in ("
						+ StringUtils.join(inList, ", ") + ") ");
			}

			if (keywords != null && keywords.length > 0) {
				List<String> inList = new ArrayList<String>();
				where = "where ";
				for (String keyword : keywords) {
					paramList.add(keyword);
					inList.add("?");
				}

				if (keywordType.equals("nanoparticle")) {
					keywordFrom = "join particle.keywordCollection keyword ";
				} else {
					keywordFrom = "join particle.characterizationCollection characterization "
							+ "join characterization.derivedBioAssayDataCollection  dataCollection "
							+ "join dataCollection.keywordCollection keyword ";
				}

				whereList.add("keyword.name in ("
						+ StringUtils.join(inList, ", ") + ") ");
			}

			if (summaries != null && summaries.length > 0) {
				List<String> summaryList = new ArrayList<String>();
				where = "where ";
				for (String summary : summaries) {
					paramList.add("%" + summary + "%");
					summaryList.add("?");
				}

				if (summaryType.equals("characterization")) {
					summaryForm = "join particle.characterizationCollection data ";
				} else {
					summaryForm = "join particle.characterizationCollection characterization "
							+ "join characterization.derivedBioAssayDataCollection  data ";
				}
				List<String> summaryWhere = new ArrayList<String>();
				for (String summary : summaryList) {
					summaryWhere.add("data.description like " + summary);
				}
				whereList.add(StringUtils.join(summaryWhere, " or "));
			}

			if (characterizations != null && characterizations.length > 0) {
				List<String> inList = new ArrayList<String>();
				where = "where ";
				for (String characterization : characterizations) {
					paramList.add(characterization);
					inList.add("?");
				}
				// to have the if statment, the keyword will only apply to the
				// characterization it specified.
				if (keywords == null
						|| (keywords.length > 0 && keywordType
								.equals("nanoparticle"))) {
					characterizationFrom = "join particle.characterizationCollection characterization ";
				}
				whereList.add("characterization.name in ("
						+ StringUtils.join(inList, ", ") + ") ");
			}
			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select particle from Nanoparticle particle "
					+ functionFrom + keywordFrom + summaryForm
					+ characterizationFrom + where + whereStr;
			HibernateUtil.beginTransaction();

			List<? extends Object> results = (HibernateUtil.createQueryByParam(
					hqlString, paramList)).list();
			for (Object obj : new HashSet<Object>(results)) {
				Nanoparticle particle = (Nanoparticle) obj;
				ParticleBean particleBean = new ParticleBean(particle);
				particles.add(particleBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger
					.error("Problem finding particles with the given search parameters ", e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}

		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<ParticleBean> filteredParticles = userService
				.getFilteredParticles(user, particles);

		// sort the list by IDs
		Collections.sort(filteredParticles,
				new CaNanoLabComparators.SampleBeanComparator());
		return filteredParticles;
	}

	/**
	 * Query nanoparticle general information such as name, type, keywords and
	 * visibilities.
	 * 
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public ParticleBean getGeneralInfo(String particleId)
			throws ParticleException, CaNanoLabSecurityException {

		Nanoparticle particle = null;
		ParticleBean particleBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			particle = (Nanoparticle) session.load(Nanoparticle.class,
					new Long(particleId));
			if (particle == null) {
				throw new ParticleException("No such particle in the database");
			}
			particleBean = new ParticleBean(particle);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding particle with ID: " + particleId, e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}

		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(
				particleBean.getSampleName(), CaNanoLabConstants.CSM_READ_ROLE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		particleBean.setVisibilityGroups(visibilityGroups);

		return particleBean;
	}

	public ParticleBean getParticleInfo(String particleId)
			throws ParticleException {
		ParticleBean particleBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			Nanoparticle particle = (Nanoparticle) session.load(
					Nanoparticle.class, new Long(particleId));
			if (particle != null)
				particleBean = new ParticleBean(particle);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding particle with ID: " + particleId, e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}
		return particleBean;
	}

	/**
	 * Avanced nanoparticle search based on more detailed meta data.
	 * 
	 * @param particleType
	 * @param functionTypes
	 * @param searchCriteria
	 * @return
	 */
	public List<ParticleBean> advancedSearch(String particleType,
			String[] functionTypes, List<SearchableBean> searchCriteria,
			UserBean user) throws ParticleException {
		List<ParticleBean> particleList = null;

		try {
			// query by particle type and function types first
			particleList = this.basicSearch(null, particleType, functionTypes,
					null, null, null, null, null, user);
			// return if no particles found or no other search criteria entered
			if (searchCriteria.isEmpty() || particleList.isEmpty()) {
				return particleList;
			}
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			for (SearchableBean searchBean : searchCriteria) {
				List<ParticleBean> theParticles = searchParticlesBy(session,
						searchBean);
				particleList.retainAll(theParticles);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding particles.", e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}
		return particleList;
	}

	/**
	 * Return particles based on search criteria defined in SearchableBean. Used
	 * in the advanced search function.
	 * 
	 * @param ida
	 * @param charInfo
	 * @return
	 * @throws ParticleException
	 */
	public List<ParticleBean> searchParticlesBy(Session session,
			SearchableBean charInfo) throws ParticleException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		// if no value range, don't query
		if (charInfo.getLowValue().length() == 0
				&& charInfo.getHighValue().length() == 0) {
			return particles;
		}
		String hqlSelect = "select distinct particle from Nanoparticle particle "
				+ "join particle.characterizationCollection char join char.derivedBioAssayDataCollection chart "
				+ "join chart.datumCollection data ";
		String hqlWhere = "where char.name=? and data.type=?";
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(charInfo.getClassification());
		paramList.add(charInfo.getType());

		if (charInfo.getLowValue().length() > 0) {
			hqlWhere += " and data.value.value>=?";
			paramList.add(charInfo.getLowValue());
		}
		if (charInfo.getHighValue().length() > 0) {
			hqlWhere += " and data.value.value<=?";
			paramList.add(charInfo.getHighValue());
		}

		String hqlString = hqlSelect + hqlWhere;
		try {
			List results = HibernateUtil.createQueryByParam(hqlString,
					paramList).list();
			for (Object obj : results) {
				Nanoparticle particle = (Nanoparticle) obj;
				ParticleBean particleBean = new ParticleBean(particle);
				// get a unique list of characterization
				Collection<Characterization> characterizationCol = particle
						.getCharacterizationCollection();

				Set<String> charcterizationSet = new HashSet<String>();
				for (Characterization charObj : characterizationCol) {
					charcterizationSet.add(charObj.getClassification() + ":"
							+ charObj.getName());
				}
				particleBean.setCharacterizations(charcterizationSet
						.toArray(new String[0]));

				// get a unique list of function
				Collection<Function> functionCol = particle
						.getFunctionCollection();

				Set<String> functionTypeSet = new HashSet<String>();
				for (Function funcObj : functionCol) {
					functionTypeSet.add(funcObj.getType());
				}
				particleBean.setFunctionTypes(functionTypeSet
						.toArray(new String[0]));
				particles.add(particleBean);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}
		return particles;
	}

	public ParticleBean getParticleBy(String particleName)
			throws ParticleException {
		ParticleBean particleBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session.createQuery(
					" from Nanoparticle particle where particle.name='"
							+ particleName + "'").list();
			Nanoparticle particle = null;
			for (Object obj : results) {
				particle = (Nanoparticle) obj;
			}
			if (particle != null) {
				particleBean = new ParticleBean(particle);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger
					.error("Problem finding particles by name:" + particleName,
							e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}
		return particleBean;
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
	public SortedSet<String> getOtherParticles(String particleSource,
			String particleName, UserBean user) throws ParticleException,
			CaNanoLabSecurityException {

		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		SortedSet<String> otherParticleNames = new TreeSet<String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select particle.name from Nanoparticle particle where particle.source.organizationName='"
					+ particleSource
					+ "' and particle.name !='"
					+ particleName
					+ "'";

			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				String otherParticleName = (String) obj;
				// check if user can see the particle
				boolean status = userService.checkReadPermission(user,
						otherParticleName);
				if (status) {
					otherParticleNames.add(otherParticleName);
				}
			}

		} catch (Exception e) {
			logger
					.error("Error in retrieving all particle type particles. ",
							e);
			throw new ParticleException(
					"Error in retrieving all particle type particles. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return otherParticleNames;
	}

	/**
	 * Update keywords and visibilities for the particle with the given id
	 * 
	 * @param particle
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public void addParticleGeneralInfo(ParticleBean particle)
			throws ParticleException, CaNanoLabSecurityException {
		Nanoparticle doParticle = null;
		// save nanoparticle to the database
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = null;
			if (particle.getSampleId() == null
					|| particle.getSampleId().length() == 0) {
				// get exising particle saved during creating sample
				results = session.createQuery(
						"from Nanoparticle where type='"
								+ particle.getSampleType() + "' and name='"
								+ particle.getSampleName() + "'").list();
			} else {
				results = session.createQuery(
						"from Nanoparticle where id=" + particle.getSampleId())
						.list();
			}
			for (Object obj : results) {
				doParticle = (Nanoparticle) obj;
			}
			if (doParticle == null) {
				throw new ParticleException("No such particle in the database");
			}
			doParticle.setClassification(getParticleClassification(particle
					.getSampleType()));
			doParticle.getKeywordCollection().clear();
			if (particle.getKeywords() != null) {
				for (String keyword : particle.getKeywords()) {
					Keyword keywordObj = new Keyword();
					if (keyword.length() > 0) {
						keywordObj.setName(keyword);
						doParticle.getKeywordCollection().add(keywordObj);
					}
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Problem saving particle general information. ", e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}
		particle.setSampleId(doParticle.getId() + "");
		particle.setSampleSource(doParticle.getSource().getOrganizationName());

		userService.setVisiblity(particle.getSampleName(), particle
				.getVisibilityGroups());
	}

	public String getParticleClassification(String particleType) {
		String classification = CaNanoLabConstants.PARTICLE_CLASSIFICATION_MAP
				.get(particleType);
		return classification;
	}

	public SortedSet<String> getAllParticleSources() throws ParticleException {
		try {
			SampleService service = new SampleService();
			return service.getAllSampleSources();
		} catch (Exception e) {
			logger.error("Eror getting particle sources.", e);
			throw new ParticleException();
		}
	}

	public SortedSet<String> getUnannotatedParticleTypes()
			throws ParticleException {
		SortedSet<String> particleTypes = new TreeSet<String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "from Nanoparticle particle order by particle.type";
			List results = session.createQuery(hqlString).list();

			for (Object obj : results) {
				Nanoparticle particle = (Nanoparticle) obj;
				// if particle has been assigned visibility groups, already
				// annotated
				List<String> groups = userService.getAccessibleGroups(particle
						.getName(), CaNanoLabConstants.CSM_READ_ROLE);
				if (groups.isEmpty()) {
					particleTypes.add(particle.getType());
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error retrieving unannotated particle types", e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}
		return particleTypes;
	}

	public SortedSet<String> getNewParticleNamesByType(String particleType)
			throws ParticleException {
		SortedSet<String> particleNames = new TreeSet<String>(
				new CaNanoLabComparators.SortableNameComparator());
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select particle.name from Nanoparticle particle where particle.type='"
					+ particleType + "'";
			List results = session.createQuery(hqlString).list();

			for (Object obj : results) {
				String name = (String) obj;
				// if particle has been assigned visibility groups, already
				// annotated
				List<String> groups = userService.getAccessibleGroups(name,
						CaNanoLabConstants.CSM_READ_ROLE);
				if (groups.isEmpty()) {
					particleNames.add(name);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error retrieving unannotated particles", e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}
		return particleNames;
	}

	public Map<String, SortedSet<String>> getAllParticleTypeParticles()
			throws ParticleException, CaNanoLabSecurityException {
		Map<String, SortedSet<String>> particleTypeParticles = new HashMap<String, SortedSet<String>>();
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// String hqlString = "select particle.type, particle.name from
			// Nanoparticle particle";
			String hqlString = "select particle.type, particle.name from Nanoparticle particle where size(particle.characterizationCollection) = 0";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			SortedSet<String> particleNames = null;
			for (Object obj : results) {
				Object[] objArray = (Object[]) obj;
				String particleType = (String) objArray[0];
				String particleName = (String) objArray[1];

				if (particleType != null) {
					if (particleTypeParticles.get(particleType) != null) {
						particleNames = particleTypeParticles.get(particleType);
					} else {
						particleNames = new TreeSet<String>(
								new CaNanoLabComparators.SortableNameComparator());
						particleTypeParticles.put(particleType, particleNames);
					}
					particleNames.add(particleName);
				}
			}

		} catch (Exception e) {
			logger.error("Error retrieving particle type particles", e);
			throw new ParticleException();
		} finally {
			HibernateUtil.closeSession();
		}

		// check if the particle already has visibility group
		// assigned, if yes, do NOT add to the list
		Map<String, SortedSet<String>> fixedParticleTypeParticles = new HashMap<String, SortedSet<String>>(
				particleTypeParticles);
		for (String particleType : fixedParticleTypeParticles.keySet()) {
			SortedSet<String> particleNames = particleTypeParticles
					.get(particleType);
			SortedSet<String> fixedParticleNames = new TreeSet<String>(
					particleNames);
			for (String particleName : fixedParticleNames) {
				List<String> groups = userService.getAccessibleGroups(
						particleName, CaNanoLabConstants.CSM_READ_ROLE);
				if (groups.size() > 0)
					particleNames.remove(particleName);
			}
			if (particleNames.size() == 0) {
				particleTypeParticles.remove(particleType);
			}
		}
		return particleTypeParticles;
	}
}
