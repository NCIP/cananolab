package gov.nih.nci.calab.service.particle;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.Morphology;
import gov.nih.nci.calab.domain.nano.characterization.physical.Shape;
import gov.nih.nci.calab.domain.nano.characterization.physical.Solubility;
import gov.nih.nci.calab.domain.nano.characterization.physical.Surface;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.domain.nano.characterization.toxicity.Cytotoxicity;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationSummaryBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
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
					.error("Problem finding particles with thet given search parameters ");
			throw e;
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
	 * @throws Exception
	 */
	public ParticleBean getGeneralInfo(String particleId) throws Exception {

		Nanoparticle particle = null;
		ParticleBean particleBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			particle = (Nanoparticle) session.load(Nanoparticle.class,
					new Long(particleId));
			if (particle == null) {
				throw new CalabException("No such particle in the database");
			}
			particleBean = new ParticleBean(particle);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding particle with ID: " + particleId, e);
			throw e;
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

	public ParticleBean getParticleInfo(String particleId) throws Exception {
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
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return particleBean;
	}

	public List<CharacterizationBean> getCharacterizationInfo(String particleId)
			throws Exception {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();

		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select chara.id, chara.name, chara.identificationName from Nanoparticle particle join particle.characterizationCollection chara where particle.id="
									+ particleId
									+ " order by chara.name, chara.identificationName")
					.list();
			for (Object obj : results) {
				String charId = ((Object[]) obj)[0].toString();
				String charName = (String) (((Object[]) obj)[1]);
				String viewTitle = (String) (((Object[]) obj)[2]);
				CharacterizationBean charBean = new CharacterizationBean(
						charId, charName, viewTitle);
				charBeans.add(charBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleId, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return charBeans;
	}

	// this would be replaced when composition model is separated from
	// characterization model
	public List<CompositionBean> getCompositionInfo(String particleId)
			throws Exception {
		List<CompositionBean> compBeans = new ArrayList<CompositionBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select chara.id, chara.name, chara.identificationName from Nanoparticle particle join particle.characterizationCollection chara where particle.id="
									+ particleId
									+ " and chara.name='Composition' "
									+ " order by chara.identificationName")
					.list();
			for (Object obj : results) {
				String charId = ((Object[]) obj)[0].toString();
				String charName = (String) (((Object[]) obj)[1]);
				String viewTitle = (String) (((Object[]) obj)[2]);
				CompositionBean compBean = new CompositionBean(charId,
						charName, viewTitle);
				compBeans.add(compBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleId, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return compBeans;
	}

	public Map<String, List<FunctionBean>> getFunctionInfo(String particleId)
			throws Exception {
		Map<String, List<FunctionBean>> funcTypeFuncs = new HashMap<String, List<FunctionBean>>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select func.id, func.type, func.identificationName from Nanoparticle particle join particle.functionCollection func where particle.id="
									+ particleId).list();
			List<FunctionBean> funcs = new ArrayList<FunctionBean>();
			for (Object obj : results) {
				String funcId = ((Object[]) obj)[0].toString();
				String funcType = ((Object[]) obj)[1].toString();
				String viewTitle = (String) (((Object[]) obj)[2]);
				FunctionBean funcBean = new FunctionBean(funcId, funcType,
						viewTitle);
				if (funcTypeFuncs.get(funcType) != null) {
					funcs = (funcTypeFuncs.get(funcType));
				} else {
					funcs = new ArrayList<FunctionBean>();
					funcTypeFuncs.put(funcType, funcs);
				}
				funcs.add(funcBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleId, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return funcTypeFuncs;
	}

	public CompositionBean getCompositionBy(String compositionId)
			throws Exception {

		CompositionBean comp = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// ParticleComposition doComp = (ParticleComposition) session.load(
			// ParticleComposition.class, new Long(compositionId));
			List results = session.createQuery(
					"from ParticleComposition where id=" + compositionId)
					.list();
			ParticleComposition doComp = null;
			for (Object obj : results) {
				doComp = (ParticleComposition) obj;
			}
			if (doComp == null) {
				return null;
			}
			if (doComp instanceof DendrimerComposition) {
				comp = new DendrimerBean((DendrimerComposition) doComp);
			} else if (doComp instanceof PolymerComposition) {
				comp = new PolymerBean((PolymerComposition) doComp);
			} else if (doComp instanceof LiposomeComposition) {
				comp = new LiposomeBean((LiposomeComposition) doComp);
			} else if (doComp instanceof FullereneComposition) {
				comp = new FullereneBean((FullereneComposition) doComp);
			} else if (doComp instanceof CarbonNanotubeComposition) {
				comp = new CarbonNanotubeBean(
						(CarbonNanotubeComposition) doComp);
			} else if (doComp instanceof EmulsionComposition) {
				comp = new EmulsionBean((EmulsionComposition) doComp);
			} else {
				comp = new CompositionBean(doComp);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding composition by ID " + compositionId,
					e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return comp;
	}

	public CharacterizationBean getCharacterizationBy(String charId)
			throws Exception {
		CharacterizationBean charBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// Characterization aChar = (Characterization) session.load(
			// Characterization.class, new Long(charId));
			List results = session.createQuery(
					"from Characterization where id=" + charId).list();
			Characterization aChar = null;
			for (Object obj : results) {
				aChar = (Characterization) obj;
			}
			if (aChar == null) {
				return null;
			}
			if (aChar instanceof Shape) {
				charBean = new ShapeBean((Shape) aChar);
			} else if (aChar instanceof Morphology) {
				charBean = new MorphologyBean((Morphology) aChar);
			} else if (aChar instanceof Solubility) {
				charBean = new SolubilityBean((Solubility) aChar);
			} else if (aChar instanceof Surface) {
				charBean = new SurfaceBean((Surface) aChar);
			} else if (aChar instanceof Cytotoxicity) {
				charBean = new CytotoxicityBean((Cytotoxicity) aChar);
			} else {
				charBean = new CharacterizationBean(aChar);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization of ID " + charId, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return charBean;
	}

	public List<CharacterizationSummaryBean> getParticleCharacterizationSummaryByName(
			String charName, String particleId) throws Exception {
		List<CharacterizationSummaryBean> charSummaryBeans = new ArrayList<CharacterizationSummaryBean>();
		List<CharacterizationBean> charBeans = getParticleCharacterizationsByName(
				charName, particleId);
		if (charBeans.isEmpty()) {
			return null;
		}
		for (CharacterizationBean charBean : charBeans) {
			if (charBean.getDerivedBioAssayDataList() != null
					&& !charBean.getDerivedBioAssayDataList().isEmpty()) {
				for (DerivedBioAssayDataBean charFile : charBean
						.getDerivedBioAssayDataList()) {
					Map<String, String> datumMap = new HashMap<String, String>();
					for (DatumBean data : charFile.getDatumList()) {
						String datumLabel = data.getName();
						if (data.getUnit() != null
								&& data.getUnit().length() > 0) {
							datumLabel += "(" + data.getUnit() + ")";
						}
						datumMap.put(datumLabel, data.getValue());
					}
					CharacterizationSummaryBean charSummaryBean = new CharacterizationSummaryBean();
					charSummaryBean.setCharBean(charBean);
					charSummaryBean.setDatumMap(datumMap);
					charSummaryBean.setCharFile(charFile);
					charSummaryBeans.add(charSummaryBean);
				}
			} else {
				CharacterizationSummaryBean charSummaryBean = new CharacterizationSummaryBean();
				charSummaryBean.setCharBean(charBean);
				charSummaryBeans.add(charSummaryBean);
			}
		}
		return charSummaryBeans;
	}

	public List<CharacterizationBean> getParticleCharacterizationsByName(
			String charName, String particleId) throws Exception {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// Characterization aChar = (Characterization) session.load(
			// Characterization.class, new Long(charId));
			List results = session
					.createQuery(
							"select chara from Nanoparticle particle join particle.characterizationCollection chara where particle.id="
									+ particleId
									+ " and chara.name='"
									+ charName
									+ "'"
									+ " order by chara.identificationName")
					.list();

			Characterization aChar = null;
			for (Object obj : results) {
				aChar = (Characterization) obj;
				CharacterizationBean charBean = null;
				if (aChar instanceof Shape) {
					charBean = new ShapeBean((Shape) aChar);
				} else if (aChar instanceof Morphology) {
					charBean = new MorphologyBean((Morphology) aChar);
				} else if (aChar instanceof Solubility) {
					charBean = new SolubilityBean((Solubility) aChar);
				} else if (aChar instanceof Surface) {
					charBean = new SurfaceBean((Surface) aChar);
				} else if (aChar instanceof Cytotoxicity) {
					charBean = new CytotoxicityBean((Cytotoxicity) aChar);
				} else {
					charBean = new CharacterizationBean(aChar);
				}
				if (charBean != null) {
					charBeans.add(charBean);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterizations with name "
					+ charName, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return charBeans;
	}

	public FunctionBean getFunctionBy(String funcId) throws Exception {
		FunctionBean functionBean = null;
		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			Function func = (Function) session.load(Function.class, new Long(
					funcId));
			if (func != null)
				functionBean = new FunctionBean(func);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding functions", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return functionBean;
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
			throw e;
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
	 * @throws Exception
	 */
	public List<ParticleBean> searchParticlesBy(Session session,
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

		} finally {
			HibernateUtil.closeSession();
		}
		return particles;
	}

	public ParticleBean getParticleBy(String particleName) throws Exception {
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
			throw e;
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
	 * @throws Exception
	 */
	public SortedSet<String> getOtherParticles(String particleSource,
			String particleName, UserBean user) throws Exception {

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
			throw new RuntimeException(
					"Error in retrieving all particle type particles. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return otherParticleNames;
	}
}
