package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class includes methods invovled in searching nanoparticles and reports.
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
			UserBean user) throws Exception {
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
							+ "join dataCollection.file.keywordCollection keyword ";
				}

				whereList.add("keyword.name in ("
						+ StringUtils.join(inList, ", ") + ") ");
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
					+ functionFrom + keywordFrom + characterizationFrom + where
					+ whereStr;

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

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);

		List<ParticleBean> filteredParticles = userService
				.getFilteredParticles(user, particles);

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
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(
				particleName, CalabConstants.CSM_READ_ROLE);
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
							+ particleType + "'");
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

	public Characterization getCharacterizationAndTableBy(String charId)
			throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		Characterization aChar = null;
		try {

			ida.open();
			List results = ida
					.search(" from Characterization chara left join fetch chara.derivedBioAssayDataCollection assayData"
							+ " left join fetch assayData.datumCollection datum left join fetch datum.conditionCollection"
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
	 * internal method to retrieve sample report information
	 * 
	 * @param particleName
	 * @param particleType
	 * @param wCollection -
	 *            which collection, reportCollection or AssociatedFileCollection
	 * @return List of LabFileBean
	 * @throws Exception
	 */
	private List<LabFileBean> getReport(String particleName,
			String particleType, String wCollection) throws Exception {
		List<LabFileBean> fileBeans = new ArrayList<LabFileBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {

			ida.open();
			List results = ida
					.search("select report from Nanoparticle particle join particle."
							+ wCollection
							+ " report where particle.name='"
							+ particleName
							+ "' and particle.type='"
							+ particleType + "'");

			for (Object obj : results) {
				LabFileBean fileBean=new LabFileBean((LabFile)obj);
				fileBeans.add(fileBean);
			}
		} catch (Exception e) {
			logger.error("Problem finding report info for particle: "
					+ particleName);
			throw e;
		} finally {
			ida.close();
		}
		return fileBeans;
	}

	/**
	 * retrieve sample report information including reportCollection and
	 * associatedFileCollection
	 * 
	 * @param particleName
	 * @param particleType
	 * @return List of LabFileBean
	 * @throws Exception
	 */
	public List<LabFileBean> getReportInfo(String particleName,
			String particleType) throws Exception {
		List<LabFileBean> fileBeans = new ArrayList<LabFileBean>();

		fileBeans.addAll(getReport(particleName, particleType,
				"reportCollection"));
		fileBeans.addAll(getReport(particleName, particleType,
				"associatedFileCollection"));
		return fileBeans;
	}

	public List<LabFileBean> searchReports(String reportTitle,
			String particleType, String[] functionTypes, UserBean user)
			throws Exception {
		List<LabFileBean> reports = new ArrayList<LabFileBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {

			ida.open();
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();
			String where = "";
			String functionTypeFrom = "";

			if (reportTitle.length() > 0) {
				paramList.add(reportTitle.toUpperCase());
				where = "where ";
				whereList.add("report.title=? ");
			}
			if (particleType.length() > 0) {
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
				functionTypeFrom = "join particle.functionCollection function ";
				whereList.add("function.type in ("
						+ StringUtils.join(inList, ", ") + ") ");
			}
			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select distinct report from Nanoparticle particle join particle.reportCollection report "
					+ functionTypeFrom + where + whereStr;

			List results = ida.searchByParam(hqlString, paramList);

			for (Object obj : results) {
				LabFileBean fileBean = new LabFileBean((Report)obj);
				reports.add(fileBean);
			}
		} catch (Exception e) {
			logger.error("Problem finding report info.");
			throw e;
		} finally {
			ida.close();
		}
		
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);

		List<LabFileBean> filteredReports = userService
				.getFilteredReports(user, reports);

		return filteredReports;
	}

}
