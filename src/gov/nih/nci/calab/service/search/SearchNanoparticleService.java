package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.common.SearchableBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class includes methods invovled in searching nanoparticles.
 * 
 * @author pansu
 * 
 */
public class SearchNanoparticleService {
	private static Logger logger = Logger
			.getLogger(SearchNanoparticleService.class);

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
	 * @throws Exception
	 */
	public List<ParticleBean> basicSearch(String particleSource,
			String particleType, String[] functionTypes,
			String[] characterizations, String[] keywords, String keywordType,
			String[] summaries, String summaryType, UserBean user)
			throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
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

			ida.open();

			List<? extends Object> results = (List<? extends Object>) ida
					.searchByParam(hqlString, paramList);
			for (Object obj : new HashSet<Object>(results)) {
				Nanoparticle particle = (Nanoparticle) obj;
				ParticleBean particleBean = new ParticleBean(particle);
				particles.add(particleBean);
			}
		} catch (Exception e) {
			logger
					.error("Problem finding particles with thet given search parameters ");
			throw e;
		} finally {
			ida.close();
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
	 * @param particleName
	 * @param particleType
	 * @return
	 * @throws Exception
	 */
	public ParticleBean getGeneralInfo(String particleName, String particleType)
			throws Exception {

		Nanoparticle particle = null;
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			// get the existing particle from database created during sample
			// creation
			List results = ida
					.search("from Nanoparticle as particle left join fetch particle.keywordCollection where particle.name='"
							+ particleName
							+ "' and particle.type='"
							+ particleType + "'");
			for (Object obj : results) {
				particle = (Nanoparticle) obj;
			}
			if (particle == null) {
				throw new CalabException("No such particle in the database");
			}
		} catch (Exception e) {
			logger.error("Problem finding particle with name: " + particleName);
			throw e;
		} finally {
			ida.close();
		}

		ParticleBean particleBean = new ParticleBean(particle);
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(
				particleName, CaNanoLabConstants.CSM_READ_ROLE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		particleBean.setVisibilityGroups(visibilityGroups);

		return particleBean;
	}

	public List<CharacterizationBean> getCharacterizationInfo(
			String particleName, String particleType) throws Exception {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {

			ida.open();
			List results = ida
					.search("select chara.id, chara.name, chara.identificationName from Nanoparticle particle join particle.characterizationCollection chara where particle.name='"
							+ particleName
							+ "' and particle.type='"
							+ particleType
							+ "' order by chara.name, chara.identificationName");
			for (Object obj : results) {
				String charId = ((Object[]) obj)[0].toString();
				String charName = (String) (((Object[]) obj)[1]);
				String viewTitle = (String) (((Object[]) obj)[2]);
				CharacterizationBean charBean = new CharacterizationBean(
						charId, charName, viewTitle);
				charBeans.add(charBean);
			}
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleName);
			throw e;
		} finally {
			ida.close();
		}
		return charBeans;
	}

	public Characterization getCharacterizationBy(String charId)
			throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		Characterization aChar = null;
		try {

			ida.open();
			List results = ida
					.search(" from Characterization chara left join fetch chara.composingElementCollection left join fetch chara.derivedBioAssayDataCollection where chara.id="
							+ charId);
			for (Object obj : results) {
				aChar = (Characterization) obj;
			}
		} catch (Exception e) {
			logger.error("Problem finding characterization");
			throw e;
		} finally {
			ida.close();
		}
		return aChar;
	}

	public Characterization getCharacterizationAndDerivedDataBy(String charId)
			throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		Characterization aChar = null;
		try {

			ida.open();
			List results = ida
					.search(" from Characterization chara left join fetch chara.derivedBioAssayDataCollection assayData"
							+ " left join fetch assayData.datumCollection datum"
							+ " where chara.id=" + charId);
			for (Object obj : results) {
				aChar = (Characterization) obj;
			}
		} catch (Exception e) {
			logger.error("Problem finding characterization");
			throw e;
		} finally {
			ida.close();
		}
		return aChar;
	}

	public Map<String, List<FunctionBean>> getFunctionInfo(String particleName,
			String particleType) throws Exception {
		Map<String, List<FunctionBean>> funcTypeFuncs = new HashMap<String, List<FunctionBean>>();

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {

			ida.open();
			List results = ida
					.search("select func.id, func.type, func.identificationName from Nanoparticle particle join particle.functionCollection func where particle.name='"
							+ particleName
							+ "' and particle.type='"
							+ particleType + "'");
			List<FunctionBean> funcs = new ArrayList<FunctionBean>();
			for (Object obj : results) {
				String funcId = ((Object[]) obj)[0].toString();
				String funcType = ((Object[]) obj)[1].toString();
				String viewTitle = (String) (((Object[]) obj)[2]);
				FunctionBean funcBean = new FunctionBean(funcId, funcType,
						viewTitle);
				if (funcTypeFuncs.get(funcType) != null) {
					funcs = (List<FunctionBean>) (funcTypeFuncs.get(funcType));
				} else {
					funcs = new ArrayList<FunctionBean>();
					funcTypeFuncs.put(funcType, funcs);
				}
				funcs.add(funcBean);

			}
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleName);
			throw e;
		} finally {
			ida.close();
		}
		return funcTypeFuncs;
	}

	public Function getFunctionBy(String funcId) throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		Function func = null;
		try {

			ida.open();
			List results = ida
					.search(" from Function func left join fetch func.linkageCollection link left join fetch link.agent.agentTargetCollection where func.id="
							+ funcId);
			for (Object obj : results) {
				func = (Function) obj;
			}
		} catch (Exception e) {
			logger.error("Problem finding functions");
			throw e;
		} finally {
			ida.close();
		}
		return func;
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
			UserBean user) throws Exception {

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		List<ParticleBean> particleList = null;

		try {
			// query by particle type and function types first
			particleList = this.basicSearch(null, particleType, functionTypes,
					null, null, null, null, null, user);
			// return if no particles found or no other search criteria entered
			if (searchCriteria.isEmpty() || particleList.isEmpty()) {
				return particleList;
			}
			ida.open();
			for (SearchableBean searchBean : searchCriteria) {
				List<ParticleBean> theParticles = searchParticlesBy(ida,
						searchBean);
				particleList.retainAll(theParticles);
			}

		} catch (Exception e) {
			logger.error("Problem finding particles.");
			throw e;
		} finally {
			ida.close();
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
	 * @throws Exception
	 */
	public List<ParticleBean> searchParticlesBy(IDataAccess ida,
			SearchableBean charInfo) throws Exception {
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

		List results = ida.searchByParam(hqlString, paramList);
		for (Object obj : results) {
			Nanoparticle particle = (Nanoparticle) obj;
			ParticleBean particleBean = new ParticleBean(particle);
			particles.add(particleBean);
		}

		return particles;
	}
}
