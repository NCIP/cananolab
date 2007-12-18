package gov.nih.nci.calab.ui.particle;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationTypeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.common.InstrumentBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.exception.CaNanoLabException;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ParticleCharacterizationException;
import gov.nih.nci.calab.exception.ParticleCompositionException;
import gov.nih.nci.calab.exception.ParticleException;
import gov.nih.nci.calab.exception.ParticleFunctionException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.calab.service.particle.NanoparticleFunctionService;
import gov.nih.nci.calab.service.particle.NanoparticleService;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.InitSessionSetup;
import gov.nih.nci.calab.ui.report.InitReportSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for nanoparticle forms.
 * 
 * @author pansu
 * 
 */
public class InitParticleSetup {
	private static LookupService lookupService;

	private static NanoparticleService particleService;

	private static NanoparticleCharacterizationService charService;

	private static NanoparticleCompositionService compService;

	private InitParticleSetup() throws CaNanoLabSecurityException {
		particleService = new NanoparticleService();
		charService = new NanoparticleCharacterizationService();
		compService = new NanoparticleCompositionService();
		lookupService = new LookupService();
	}

	public static InitParticleSetup getInstance()
			throws CaNanoLabSecurityException {
		return new InitParticleSetup();
	}

	public void setNewParticleTypes(HttpSession session)
			throws ParticleException {
		SortedSet<String> particleTypes = particleService
				.getUnannotatedParticleTypes();
		session.setAttribute("allNewParticleTypes", particleTypes);
	}

	public void setAllCompositionDropdowns(HttpSession session)
			throws CaNanoLabException {
		SortedSet<String> composingElementTypes = lookupService
				.getAllLookupTypes("ComposingElementType");
		session.setAttribute("allComposingElementTypes", composingElementTypes);

		session.setAttribute("allComposingElementTypes", composingElementTypes);
		SortedSet<String> particleTypes = lookupService
				.getAllLookupTypes("SampleType");
		// remove complex particle as a type of composing element type.
		particleTypes.remove("Complex Particle");
		session.setAttribute("allParticleElementTypes", particleTypes);

		setAllDendrimers(session);

		SortedSet<String> initiators = compService.getAllPolymerInitiators();
		session.setAttribute("allPolymerInitiators", initiators);
	}

	private void setAllDendrimers(HttpSession session)
			throws ParticleCompositionException {
		SortedSet<String> dendrimerCores = compService.getAllDendrimerCores();
		session.setAttribute("allDendrimerCores", dendrimerCores);
		SortedSet<String> surfaceGroupNames = compService
				.getAllDendrimerSurfaceGroupNames();
		session
				.setAttribute("allDendrimerSurfaceGroupNames",
						surfaceGroupNames);
		SortedSet<String> branches = compService.getAllDendrimerBranches();
		session.setAttribute("allDendrimerBranches", branches);
		SortedSet<String> generations = compService
				.getAllDendrimerGenerations();
		session.setAttribute("allDendrimerGenerations", generations);
	}

	public void setAllMetalCompositions(HttpSession session)
			throws ParticleCompositionException {
		if (session.getServletContext().getAttribute("allMetalCompositions") == null) {
			String[] compositions = compService.getAllMetalCompositions();
			session.getServletContext().setAttribute("allMetalCompositions",
					compositions);
		}
	}

	public void setAllParticleSources(HttpSession session)
			throws ParticleException {
		SortedSet<String> particleSources = particleService
				.getAllParticleSources();
		session.setAttribute("allParticleSources", particleSources);
	}

	public void setSideParticleMenu(HttpServletRequest request,
			String particleId) throws CaNanoLabException {
		HttpSession session = request.getSession();
		InitReportSetup.getInstance().setAllReports(session, particleId);
		// not part of the side menu, but need to up
		// if (session.getAttribute("newParticleCreated") != null) {
		// setParticleTypeParticles(session);
		// }
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		setAllFunctionTypes(session);
		setFunctionTypeFunctions(session, particleId);
		setAllCompositions(session, particleId);
		setAllCharacterizations(session, particleId);
		session.removeAttribute("newParticleCreated");
		session.removeAttribute("newCharacterizationCreated");
		session.removeAttribute("newReportCreated");
		session.removeAttribute("newFunctionCreated");
		session.removeAttribute("detailPage");
	}

	private void setAllCompositions(HttpSession session, String particleId)
			throws ParticleCompositionException {
		if (session.getAttribute("allCharacterizations") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			// get saved characterizations based on the particle type and name
			NanoparticleCompositionService service = new NanoparticleCompositionService();
			List<CompositionBean> compBeans = service
					.getCompositionInfo(particleId);
			session.setAttribute("allCompositions", compBeans);
		}
	}

	/**
	 * Set characterizations stored in the database
	 * 
	 * @param session
	 * @param particleName
	 * @param particleType
	 * @throws ParticleCharacterizationException
	 * @throws CaNanoLabSecurityException
	 */
	private void setAllCharacterizations(HttpSession session, String particleId)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException {
		setAllCharacterizationTypes(session);
		Map<String, List<CharacterizationBean>> charTypeChars = (Map<String, List<CharacterizationBean>>) session
				.getServletContext().getAttribute("allCharTypeChars");
		if (session.getAttribute("allCharacterizations") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			// get saved characterizations based on the particle ID
			NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
			List<CharacterizationBean> charBeans = service
					.getCharacterizationInfo(particleId);
			Map<String, List<CharacterizationBean>> charMap = new HashMap<String, List<CharacterizationBean>>();

			Map<String, String> ascendTypeTreeMap = getAscendTypeTreeMap(charTypeChars);

			// get all characterization types that do not have children
			// key: characterization name, value: action name
			Map<String, String> charLeafActionNameMap = getCharTypeLeafMap(
					session, charTypeChars);

			// all characterization types
			Map<String, Set<String>> typeTreeMap = new HashMap<String, Set<String>>();

			/* charType: category column of table def_characterization_category */
			for (String charType : charTypeChars.keySet()) {
				List<CharacterizationBean> newCharBeans = new ArrayList<CharacterizationBean>();

				// get all characterizations for the characterization type
				List<CharacterizationBean> charList = (List<CharacterizationBean>) charTypeChars
						.get(charType);
				// set abbreviation for each saved characterization
				for (CharacterizationBean displayBean : charList) {
					for (CharacterizationBean charBean : charBeans) {
						if (displayBean.getName().equals(charBean.getName())) {
							charBean.setAbbr(displayBean.getAbbr());
							newCharBeans.add(charBean);
						}
					}
					String childType = displayBean.getName();
					while (childType != null
							&& !childType
									.equalsIgnoreCase(Characterization.PHYSICAL_CHARACTERIZATION)
							&& !childType
									.equalsIgnoreCase(Characterization.INVITRO_CHARACTERIZATION)) {
						String parentType = ascendTypeTreeMap.get(childType);

						if (typeTreeMap.containsKey(parentType)) {
							typeTreeMap.get(parentType).add(childType);
						} else {
							Set<String> typeSet = new TreeSet<String>();
							typeSet.add(childType);
							typeTreeMap.put(parentType, typeSet);
						}
						childType = parentType;
					}
				}
				if (!newCharBeans.isEmpty()) {
					charMap.put(charType, newCharBeans);
				}
			} // only characterization types that searched with
			// viewTitles
			Map<String, Set<String>> typeTreeSelectedMap = createCharaTypeTree(
					charMap, ascendTypeTreeMap);
			// set allCharacterizations based on user privilege
			UserService userService = new UserService(
					CaNanoLabConstants.CSM_APP_NAME);
			UserBean user = (UserBean) session.getAttribute("user");
			if (session.getAttribute("canCreateNanoparticle") == null) {
				Boolean createParticle = userService.checkCreatePermission(
						user, CaNanoLabConstants.CSM_PG_PARTICLE);
				session.setAttribute("canCreateNanoparticle", createParticle);
			}
			Boolean canCreateNanoparticle = (Boolean) session
					.getAttribute("canCreateNanoparticle");
			if (canCreateNanoparticle) {
				session.setAttribute("allCharacterizations", typeTreeMap);
			} else {
				session.setAttribute("allCharacterizations",
						typeTreeSelectedMap);
			}

			session.setAttribute("charaLeafActionName", charLeafActionNameMap);
			Map<String, List<CharacterizationBean>> nameCharMap = getLeafCharaMap(charMap);
			session.setAttribute("charaLeafToCharacterizations", nameCharMap);
		}
	}

	public Map<String, Set<String>> createCharaTypeTree(
			Map<String, List<CharacterizationBean>> charMap,
			Map<String, String> ascendTypeTreeMap) {

		// key: parent type; value: set of child types;
		Map<String, Set<String>> typeTreeMap = new HashMap<String, Set<String>>();

		for (String charType : charMap.keySet()) {
			List<CharacterizationBean> charBeans = (List<CharacterizationBean>) charMap
					.get(charType);

			for (CharacterizationBean charBean : charBeans) {

				String childType = charBean.getName();
				while (childType != null
						&& !childType
								.equalsIgnoreCase(Characterization.PHYSICAL_CHARACTERIZATION)
						&& !childType
								.equalsIgnoreCase(Characterization.INVITRO_CHARACTERIZATION)) {
					String parentType = ascendTypeTreeMap.get(childType);

					if (typeTreeMap.containsKey(parentType)) {
						typeTreeMap.get(parentType).add(childType);
					} else {
						Set<String> typeSet = new TreeSet<String>();
						typeSet.add(childType);
						typeTreeMap.put(parentType, typeSet);
					}
					childType = parentType;
				}
			}
		}
		return typeTreeMap;
	}

	public Map<String, List<CharacterizationBean>> getLeafCharaMap(
			Map<String, List<CharacterizationBean>> charMap) {
		/*
		 * nameCharMap key: the 'name' column of table
		 * def_characterization_category These names do not exist in the
		 * 'category' column, i.e. they are the lowest level in the category
		 * tree. value: list of CharacterizationBean
		 */
		Map<String, List<CharacterizationBean>> nameCharMap = new HashMap<String, List<CharacterizationBean>>();

		for (String charCategory : charMap.keySet()) {
			List<CharacterizationBean> charList = (List<CharacterizationBean>) charMap
					.get(charCategory);
			for (CharacterizationBean cbean : charList) {
				String pname = cbean.getName();
				if (!charMap.containsKey(pname)) {
					if (nameCharMap.containsKey(pname)) {
						List<CharacterizationBean> clist = (List<CharacterizationBean>) nameCharMap
								.get(pname);
						clist.add(cbean);
					} else {
						List<CharacterizationBean> charBeanList = new ArrayList<CharacterizationBean>();
						charBeanList.add(cbean);
						nameCharMap.put(pname, charBeanList);
					}
				}
			}
		}
		return nameCharMap;
	}

	public Map<String, String> getAscendTypeTreeMap(
			Map<String, List<CharacterizationBean>> charTypeMap) {
		Map<String, String> selectedCharTreeMap = new HashMap<String, String>();
		for (String ctype : charTypeMap.keySet()) {
			List<CharacterizationBean> cbeanList = (List<CharacterizationBean>) charTypeMap
					.get(ctype);
			for (CharacterizationBean cbean : cbeanList) {
				String cname = cbean.getName();
				selectedCharTreeMap.put(cname, ctype);
			}
		}
		return selectedCharTreeMap;
	}

	public Map<String, String> getCharTypeLeafMap(HttpSession session,
			Map<String, List<CharacterizationBean>> charTypeMap) {
		Map<String, String> leafMap = new HashMap<String, String>();
		for (String ctype : charTypeMap.keySet()) {
			List<CharacterizationBean> cbeanList = (List<CharacterizationBean>) charTypeMap
					.get(ctype);
			for (CharacterizationBean cbean : cbeanList) {
				String cname = cbean.getName();
				// set characterization type whether physical or in
				// vitro
				cbean.setCharacterizationType(getCharType(session, cbean.getName()));
				leafMap.put(cname, cbean.getDispatchActionName());
			}
		}
		return leafMap;
	}

	private void setFunctionTypeFunctions(HttpSession session, String particleId)
			throws ParticleFunctionException {
		if (session.getAttribute("allFuncTypeFuncs") == null
				|| session.getAttribute("newParticleCreated") != null
				|| session.getAttribute("newFunctionCreated") != null) {
			NanoparticleFunctionService service = new NanoparticleFunctionService();
			Map<String, List<FunctionBean>> funcTypeFuncs = service
					.getFunctionInfo(particleId);
			session.setAttribute("allFuncTypeFuncs", funcTypeFuncs);
		}
	}

	public void setRemoteSideParticleMenu(HttpServletRequest request,
			String particleName, GridNodeBean gridNode)
			throws CaNanoLabException {
		HttpSession session = request.getSession();
		GridSearchService service = new GridSearchService();
		if (session.getAttribute("remoteCharTypeChars") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newRemoteParticleCreated") != null) {

			Map<String, List<CharacterizationBean>> charTypeChars = service
					.getRemoteCharacterizationMap(particleName, gridNode);
			List<CharacterizationBean> remoteComps = charTypeChars
					.get("Composition");
			session.setAttribute("remoteCompositions", remoteComps);
			Map<String, List<CharacterizationBean>> nameCharMap = getLeafCharaMap(charTypeChars);
			session.setAttribute("remoteCharaLeafToCharacterizations",
					nameCharMap);

			Map<String, List<CharacterizationBean>> orderedCharTypeChars = charService
					.getCharacterizationTypeCharacterizations();
			Map<String, String> ascendTypeTreeMap = getAscendTypeTreeMap(orderedCharTypeChars);
			Map<String, Set<String>> typeTreeSelectedMap = createCharaTypeTree(
					charTypeChars, ascendTypeTreeMap);
			session.setAttribute("remoteSelectedCharacterizations",
					typeTreeSelectedMap);

			Map<String, String> charActionNameMap = getCharTypeLeafMap(session,
					orderedCharTypeChars);
			session.setAttribute("remoteCharaActionName", charActionNameMap);
		}

		if (session.getAttribute("remoteFuncTypeFuncs") == null
				|| session.getAttribute("newFunctionCreated") != null
				|| session.getAttribute("newRemoteParticleCreated") != null) {
			Map<String, List<FunctionBean>> funcTypeFuncs = service
					.getRemoteFunctionMap(particleName, gridNode);
			session.setAttribute("remoteFuncTypeFuncs", funcTypeFuncs);
		}

		if (session.getAttribute("remoteParticleReports") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newRemoteParticleCreated") != null) {

			List<LabFileBean> reportBeans = service.getRemoteReports(
					particleName, gridNode);
			session.setAttribute("remoteParticleReports", reportBeans);
		}

		if (session.getAttribute("remoteParticleAssociatedFiles") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newRemoteParticleCreated") != null) {
			List<LabFileBean> associatedBeans = service
					.getRemoteAssociatedFiles(particleName, gridNode);
			session.setAttribute("remoteParticleAssociatedFiles",
					associatedBeans);
		}
		session.removeAttribute("newCharacterizationCreated");
		session.removeAttribute("newFunctionCreated");
		session.removeAttribute("newRemoteParticleCreated");
		session.removeAttribute("newReportCreated");
		session.removeAttribute("detailPage");
		InitSessionSetup.getInstance().setStaticDropdowns(session);
	}

	public void setAllPhysicalDropdowns(HttpSession session)
			throws CaNanoLabException {
		// solubility
		SortedSet<String> solventTypes = lookupService
				.getAllLookupTypes("SolventType");
		session.setAttribute("allSolventTypes", solventTypes);
		SortedSet<String> concentrationUnits = lookupService
				.getAllMeasureUnits().get("Concentration");
		session.setAttribute("allConcentrationUnits", concentrationUnits);

		// shape
		SortedSet<String> shapeTypes = lookupService
				.getAllLookupTypes("ShapeType");
		session.setAttribute("allShapeTypes", shapeTypes);

		// morphology
		SortedSet<String> morphologyTypes = lookupService
				.getAllLookupTypes("MorphologyType");
		;
		session.setAttribute("allMorphologyTypes", morphologyTypes);

		// surface
		if (session.getAttribute("allChargeMeasureUnits") == null
				|| session.getAttribute("newSurfaceCreated") != null) {
			SortedSet<String> chargeUnits = lookupService.getAllMeasureUnits()
					.get("Charge");
			session.setAttribute("allChargeMeasureUnits", chargeUnits);
		}
		if (session.getAttribute("allAreaMeasureUnits") == null
				|| session.getAttribute("newSurfaceCreated") != null) {
			SortedSet<String> chargeUnits = lookupService.getAllMeasureUnits()
					.get("Area");
			session.setAttribute("allAreaMeasureUnits", chargeUnits);
		}
		if (session.getAttribute("allMolecularFormulaTypes") == null
				|| session.getAttribute("newSurfaceCreated") != null) {
			SortedSet<String> chargeUnits = lookupService
					.getAllLookupTypes("MolecularFormulaType");
			session.setAttribute("allMolecularFormulaTypes", chargeUnits);
		}
		if (session.getAttribute("allZetaPotentialUnits") == null
				|| session.getAttribute("newSurfaceCreated") != null) {
			SortedSet<String> chargeUnits = lookupService.getAllMeasureUnits()
					.get("Zeta Potential");
			session.setAttribute("allZetaPotentialUnits", chargeUnits);
		}
	}

	public void setAllAreaMeasureUnits(HttpSession session)
			throws CaNanoLabException {
		if (session.getServletContext().getAttribute("allAreaMeasureUnits") == null) {
			SortedSet<String> areaUnits = lookupService.getAllMeasureUnits()
					.get("Area");
			session.getServletContext().setAttribute("allAreaMeasureUnits",
					areaUnits);
		}
	}

	public void setAllInvitroDropdowns(HttpSession session)
			throws CaNanoLabException {
		SortedSet<String> cellLines = lookupService
				.getAllLookupTypes("CellLineType");
		session.setAttribute("allCellLines", cellLines);
	}

	public void setAllFunctionDropdowns(HttpSession session)
			throws CaNanoLabException {
		if (session.getServletContext().getAttribute("allSpecies") == null) {
			SortedSet<String> names = lookupService
					.getAllLookupTypes("SpeciesName");
			session.getServletContext().setAttribute("allSpecies", names);
		}

		SortedSet<String> constrastAgentTypes = lookupService
				.getAllLookupTypes("ImageContrastAgentType");
		session.setAttribute("allImageContrastAgentTypes", constrastAgentTypes);

		if (session.getServletContext().getAttribute("allActivationMethods") == null) {
			SortedSet<String> methods = lookupService
					.getAllLookupTypes("ActivationMethod");
			session.getServletContext().setAttribute("allActivationMethods",
					methods);
		}

		SortedSet<String> bondTypes = lookupService
				.getAllLookupTypes("BondType");
		session.setAttribute("allBondTypes", bondTypes);

		if (session.getServletContext().getAttribute("allLinkageTypes") == null) {
			SortedSet<String> linkageTypes = lookupService
					.getAllLookupTypes("FunctionLinkageType");
			List<String> linkageTypeList = new ArrayList<String>(linkageTypes);
			linkageTypeList.add(CaNanoLabConstants.OTHER);
			session.getServletContext().setAttribute("allLinkageTypes",
					linkageTypeList);
		}

		if (session.getServletContext().getAttribute("allAgentTargetTypes") == null) {
			SortedSet<String> agentTargetTypes = lookupService
					.getAllLookupTypes("FunctionAgentTargetType");
			List<String> agentTargetTypeList = new ArrayList<String>(
					agentTargetTypes);
			agentTargetTypeList.add(CaNanoLabConstants.OTHER);
			session.getServletContext().setAttribute("allAgentTargetTypes",
					agentTargetTypeList);
		}

		if (session.getServletContext().getAttribute("allAgentTypes") == null) {
			SortedSet<String> agentTypes = lookupService
					.getAllLookupTypes("FunctionAgentType");
			List<String> agentTypeList = new ArrayList<String>(agentTypes);
			agentTypeList.add(CaNanoLabConstants.OTHER);
			session.getServletContext().setAttribute("allAgentTypes",
					agentTypeList);
		}
	}

	public void setAllCharacterizationDropdowns(HttpSession session)
			throws ParticleCharacterizationException {
		if (session.getAttribute("characterizationSources") == null
				|| session.getAttribute("newCharacterizationSourceCreated") != null) {
			SortedSet<String> characterizationSources = charService
					.getAllCharacterizationSources();
			session.setAttribute("characterizationSources",
					characterizationSources);
		}
		session.removeAttribute("newCharacterizationSourceCreated");
	}

	public void setAllInstruments(HttpSession session)
			throws ParticleCharacterizationException {
		List<InstrumentBean> instruments = charService.getAllInstruments();
		SortedSet<String> instrumentTypes = new TreeSet<String>();
		for (InstrumentBean instrument : instruments) {
			instrumentTypes.add(instrument.getType());
		}
		SortedSet<String> manufacturers = charService.getAllManufacturers();
		session.setAttribute("allInstruments", instruments);
		session.setAttribute("allInstrumentTypes", instrumentTypes);
		session.setAttribute("allManufacturers", manufacturers);
	}

	public void setAllDerivedDataFileTypes(HttpSession session)
			throws ParticleCharacterizationException {
		SortedSet<String> fileTypes = charService
				.getAllCharacterizationFileTypes();
		session.setAttribute("allDerivedDataFileTypes", fileTypes);
	}

	public void setAllFunctionTypes(HttpSession session)
			throws CaNanoLabException {
		// set in application context
		if (session.getServletContext().getAttribute("allFunctionTypes") == null) {
			SortedSet<String> types = lookupService
					.getAllLookupTypes("FunctionType");
			session.getServletContext().setAttribute("allFunctionTypes", types);
		}
	}

	public void setAllCharacterizationTypes(HttpSession session)
			throws ParticleCharacterizationException {
		// set in application context
		if (session.getServletContext()
				.getAttribute("allCharacterizationTypes") == null) {
			List<CharacterizationTypeBean> types = charService
					.getAllCharacterizationTypes();
			session.getServletContext().setAttribute(
					"allCharacterizationTypes", types);
		}

		// set in application context mapping between characterization type and
		// child characterization name and abbrs
		if (session.getServletContext().getAttribute("allCharTypeChars") == null) {
			Map<String, List<CharacterizationBean>> charTypeChars = charService
					.getCharacterizationTypeCharacterizations();
			session.getServletContext().setAttribute("allCharTypeChars",
					charTypeChars);
		}
	}

	public void setDerivedDatumNames(HttpSession session,
			String characterizationName)
			throws ParticleCharacterizationException {
		SortedSet<String> categories = charService
				.getDerivedDataCategories(characterizationName);
		session.setAttribute("derivedDataCategories", categories);

		SortedSet<String> datumNames = charService
				.getDerivedDatumNames(characterizationName);
		session.setAttribute("datumNames", datumNames);
	}

	public void setAllCharacterizationMeasureUnitsTypes(HttpSession session,
			String charName) throws CaNanoLabException {
		Map<String, SortedSet<String>> unitMap = lookupService
				.getAllMeasureUnits();
		SortedSet<String> charUnits = unitMap.get(charName);
		if (charUnits == null) {
			charUnits = new TreeSet<String>();
			charUnits.add("");// add an empty one to indicate no unit
		}
		SortedSet<String> types = lookupService
				.getAllLookupTypes("MeasureType");
		session.setAttribute("charMeasureUnits", charUnits);
		session.setAttribute("charMeasureTypes", types);
	}

	public void setCharNameToCategory(ServletContext appContext)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException {
		if (appContext.getAttribute("characterizationCategoryMap") == null) {
			NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
			Map<String, String> charNameToCharCategory = charService
					.getCharacterizationCategoryMap();
			appContext.setAttribute("characterizationCategoryMap",
					charNameToCharCategory);
		}
	}

	public String getCharType(HttpSession session, String charName) {
		Map<String, String> charNameToCharCategory = new HashMap<String, String>(
				(Map<? extends String, String>) session.getServletContext()
						.getAttribute("characterizationCategoryMap"));
		String category = charNameToCharCategory.get(charName);
		String charType = null;
		if (category != null
				&& category.equals(Characterization.PHYSICAL_CHARACTERIZATION)) {
			charType = category;
		} else {
			// get the top level category
			String nextCategory = null;
			while (category != null) {
				nextCategory = category;
				category = charNameToCharCategory.get(nextCategory);
			}
			charType = nextCategory;
		}
		return charType;
	}
}
