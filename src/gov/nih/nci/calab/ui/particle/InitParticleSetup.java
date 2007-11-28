package gov.nih.nci.calab.ui.particle;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationTypeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.common.InstrumentBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.particle.SearchNanoparticleService;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.report.SearchReportService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

	private InitParticleSetup() throws Exception {
		lookupService = new LookupService();
	}

	public static InitParticleSetup getInstance() throws Exception {
		return new InitParticleSetup();
	}

	public static LookupService getLookupService() {
		return lookupService;
	}

	public void setParticleTypeParticles(HttpSession session) throws Exception {
		if (session.getAttribute("particleTypeParticles") == null
				|| session.getAttribute("newSampleCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			Map<String, SortedSet<String>> particleTypeParticles = lookupService
					.getAllParticleTypeParticles();
			List<String> particleTypes = new ArrayList<String>(
					particleTypeParticles.keySet());
			Collections.sort(particleTypes);

			session.setAttribute("allParticleTypeParticles",
					particleTypeParticles);
			session.setAttribute("allParticleTypes", particleTypes);
		}
		session.removeAttribute("newParticleCreated");
	}

	public void setAllCompositionDropdowns(HttpSession session)
			throws Exception {
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

		if ((session.getAttribute("allPolymerInitiators") == null)
				|| (session.getAttribute("newPolymerCreated") != null)) {
			SortedSet<String> initiators = lookupService
					.getAllPolymerInitiators();
			session.setAttribute("allPolymerInitiators", initiators);
		}
		session.removeAttribute("newPolymerCreated");
	}

	private void setAllDendrimers(HttpSession session) throws Exception {
		if ((session.getAttribute("allDendrimerCores") == null)
				|| session.getAttribute("newDendrimerCreated") != null) {
			SortedSet<String> dendrimerCores = lookupService
					.getAllDendrimerCores();
			session.setAttribute("allDendrimerCores", dendrimerCores);
		}
		if (session.getAttribute("allDendrimerSurfaceGroupNames") == null
				|| session.getAttribute("newDendrimerCreated") != null) {
			SortedSet<String> surfaceGroupNames = lookupService
					.getAllDendrimerSurfaceGroupNames();
			session.setAttribute("allDendrimerSurfaceGroupNames",
					surfaceGroupNames);
		}
		if (session.getAttribute("allDendrimerBranches") == null
				|| session.getAttribute("newDendrimerCreated") != null) {
			SortedSet<String> branches = lookupService
					.getAllDendrimerBranches();
			session.setAttribute("allDendrimerBranches", branches);
		}
		if (session.getAttribute("allDendrimerGenerations") == null
				|| session.getAttribute("newDendrimerCreated") != null) {
			SortedSet<String> generations = lookupService
					.getAllDendrimerGenerations();
			session.setAttribute("allDendrimerGenerations", generations);
		}

		session.removeAttribute("newDendrimerCreated");
	}

	public void setAllMetalCompositions(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allMetalCompositions") == null) {
			String[] compositions = lookupService.getAllMetalCompositions();
			session.getServletContext().setAttribute("allMetalCompositions",
					compositions);
		}
	}

	public void setAllParticleSources(HttpSession session) throws Exception {
		if (session.getAttribute("allParticleSources") == null
				|| session.getAttribute("newSampleCreated") != null) {
			SortedSet<String> particleSources = lookupService
					.getAllParticleSources();
			session.setAttribute("allParticleSources", particleSources);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void setSideParticleMenu(HttpServletRequest request,
			String particleId) throws Exception {
		HttpSession session = request.getSession();
		setAllReports(session, particleId);
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

	private void setAllReports(HttpSession session, String particleId)
			throws Exception {
		UserBean user = (UserBean) session.getAttribute("user");
		SearchReportService searchReportService = new SearchReportService();
		if (session.getAttribute("particleReports") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {

			List<LabFileBean> reportBeans = searchReportService.getReportInfo(
					particleId, CaNanoLabConstants.REPORT, user);
			session.setAttribute("particleReports", reportBeans);
		}

		if (session.getAttribute("particleAssociatedFiles") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			List<LabFileBean> associatedBeans = searchReportService
					.getReportInfo(particleId,
							CaNanoLabConstants.ASSOCIATED_FILE, user);
			session.setAttribute("particleAssociatedFiles", associatedBeans);
		}
	}

	private void setAllCompositions(HttpSession session,
			String particleId) throws Exception {
		if (session.getAttribute("allCharacterizations") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			// get saved characterizations based on the particle type and name
			SearchNanoparticleService service = new SearchNanoparticleService();
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
	 * @throws Exception
	 */
	private void setAllCharacterizations(HttpSession session,
			String particleId) throws Exception {
		setAllCharacterizationTypes(session);
		Map<String, List<CharacterizationBean>> charTypeChars = (Map<String, List<CharacterizationBean>>) session
				.getServletContext().getAttribute("allCharTypeChars");
		if (session.getAttribute("allCharacterizations") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			// get saved characterizations based on the particle ID
			SearchNanoparticleService service = new SearchNanoparticleService();
			List<CharacterizationBean> charBeans = service
					.getCharacterizationInfo(particleId);
			Map<String, List<CharacterizationBean>> charMap = new HashMap<String, List<CharacterizationBean>>();

			Map<String, String> ascendTypeTreeMap = getAscendTypeTreeMap(charTypeChars);

			// get all characterization types that do not have children
			// key: characterization name, value: action name
			Map<String, String> charLeafActionNameMap = getCharTypeLeafMap(charTypeChars);

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
							&& !childType.equalsIgnoreCase("Physical")
							&& !childType.equalsIgnoreCase("in vitro")) {
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
			}
			session.setAttribute("allCharacterizations", typeTreeMap);
			
			// only characterization types that searched with viewTitles
			Map<String, Set<String>> typeTreeSelectedMap = createCharaTypeTree(charMap, ascendTypeTreeMap);
			session.setAttribute("selectedCharacterizations", typeTreeSelectedMap);
			
			session.setAttribute("charaLeafActionName", charLeafActionNameMap);
			
			Map<String, List<CharacterizationBean>> nameCharMap = getLeafCharaMap(charMap);
			session.setAttribute("charaLeafToCharacterizations", nameCharMap);
		}
	}
	
	public Map<String, Set<String>> createCharaTypeTree(
			Map<String, List<CharacterizationBean>> charMap, Map<String, String> ascendTypeTreeMap)
	{
		
		//key: parent type; value: set of child types;
		Map<String, Set<String>> typeTreeMap = new HashMap<String, Set<String>>();
		
		for (String charType : charMap.keySet()) {
			List<CharacterizationBean> charBeans = 
				(List<CharacterizationBean>) charMap.get(charType);
		
			for(CharacterizationBean charBean : charBeans) {
		
				String childType = charBean.getName();
				while (childType != null
						&& !childType.equalsIgnoreCase("Physical")
						&& !childType.equalsIgnoreCase("in vitro")) {
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
	
	public Map<String, List<CharacterizationBean>> getLeafCharaMap(Map<String, List<CharacterizationBean>> charMap)
	{
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

	public Map<String, String> getCharTypeLeafMap(
			Map<String, List<CharacterizationBean>> charTypeMap) {
		Map<String, String> leafMap = new HashMap<String, String>();
		for (String ctype : charTypeMap.keySet()) {
			List<CharacterizationBean> cbeanList = (List<CharacterizationBean>) charTypeMap
					.get(ctype);
			for (CharacterizationBean cbean : cbeanList) {
				String cname = cbean.getName();
				leafMap.put(cname, cbean.getActionName());
			}
		}
		return leafMap;
	}

	public void setFunctionTypeFunctions(HttpSession session,
			String particleId) throws Exception {
		if (session.getAttribute("allFuncTypeFuncs") == null
				|| session.getAttribute("newParticleCreated") != null
				|| session.getAttribute("newFunctionCreated") != null) {
			SearchNanoparticleService service = new SearchNanoparticleService();
			Map<String, List<FunctionBean>> funcTypeFuncs = service
					.getFunctionInfo(particleId);
			session.setAttribute("allFuncTypeFuncs", funcTypeFuncs);
		}
	}

	public void setRemoteSideParticleMenu(HttpServletRequest request,
			String particleName, GridNodeBean gridNode) throws Exception {
		HttpSession session = request.getSession();
		GridSearchService service = new GridSearchService();
		if (session.getAttribute("remoteCharTypeChars") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newRemoteParticleCreated") != null) {

			Map<String, List<CharacterizationBean>> charTypeChars = service
					.getRemoteCharacterizationMap(particleName, gridNode);
			
			Map<String, List<CharacterizationBean>> nameCharMap = 
									getLeafCharaMap(charTypeChars);
			session.setAttribute("remoteCharaLeafToCharacterizations", nameCharMap);
			
			LookupService lookupService = new LookupService();
			Map<String, List<CharacterizationBean>> orderedCharTypeChars = lookupService
					.getCharacterizationTypeCharacterizations();
			Map<String, String> ascendTypeTreeMap = getAscendTypeTreeMap(orderedCharTypeChars);
			Map<String, Set<String>> typeTreeSelectedMap = 
				createCharaTypeTree(charTypeChars, ascendTypeTreeMap);
			session.setAttribute("remoteSelectedCharacterizations", typeTreeSelectedMap);
			
			Map<String, String> charActionNameMap = getCharTypeLeafMap(orderedCharTypeChars);
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

	public void setAllPhysicalDropdowns(HttpSession session) throws Exception {
		// solubility
		if (session.getAttribute("allSolventTypes") == null
				|| session.getAttribute("newSolubilityCreated") != null) {
			SortedSet<String> solventTypes = lookupService
					.getAllLookupTypes("SolventType");
			session.setAttribute("allSolventTypes", solventTypes);
		}
		if (session.getAttribute("allConcentrationUnits") == null
				|| session.getAttribute("newSolubilityCreated") != null) {
			SortedSet<String> concentrationUnits = lookupService
					.getAllMeasureUnits().get("Concentration");
			session.setAttribute("allConcentrationUnits", concentrationUnits);
		}
		session.removeAttribute("newSolubilityCreated");

		// shape
		if (session.getAttribute("allShapeTypes") == null
				|| session.getAttribute("newShapeCreated") != null) {
			SortedSet<String> shapeTypes = lookupService
					.getAllLookupTypes("ShapeType");
			session.setAttribute("allShapeTypes", shapeTypes);
		}
		session.removeAttribute("newShapeCreated");

		// morphology
		if (session.getAttribute("allMorphologyTypes") == null
				|| session.getAttribute("newMorphologyCreated") != null) {
			SortedSet<String> morphologyTypes = lookupService
					.getAllLookupTypes("MorphologyType");
			;
			session.setAttribute("allMorphologyTypes", morphologyTypes);
		}
		session.removeAttribute("newMorphologyCreated");

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
		session.removeAttribute("newSurfaceCreated");
	}

	public void setAllAreaMeasureUnits(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allAreaMeasureUnits") == null) {
			SortedSet<String> areaUnits = lookupService.getAllMeasureUnits()
					.get("Area");
			session.getServletContext().setAttribute("allAreaMeasureUnits",
					areaUnits);
		}
	}

	public void setAllInvitroDropdowns(HttpSession session) throws Exception {
		if (session.getAttribute("allCellLines") == null
				|| session.getAttribute("newCytoCreated") != null) {
			SortedSet<String> cellLines = lookupService
					.getAllLookupTypes("CellLineType");
			session.setAttribute("allCellLines", cellLines);
		}
		session.removeAttribute("newCytoCreated");
	}

	public void setAllFunctionDropdowns(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allSpecies") == null) {
			SortedSet<String> names = lookupService
					.getAllLookupTypes("SpeciesName");
			session.getServletContext().setAttribute("allSpecies", names);
		}

		if (session.getAttribute("allImageContrastAgentTypes") == null
				|| session.getAttribute("newContrastAgentTypeCreated") != null) {
			SortedSet<String> agentTypes = lookupService
					.getAllLookupTypes("ImageContrastAgentType");
			session.setAttribute("allImageContrastAgentTypes", agentTypes);
		}
		session.removeAttribute("newContrastAgentTypeCreated");

		if (session.getServletContext().getAttribute("allActivationMethods") == null) {
			SortedSet<String> methods = lookupService
					.getAllLookupTypes("ActivationMethod");
			session.getServletContext().setAttribute("allActivationMethods",
					methods);
		}

		if (session.getAttribute("allBondTypes") == null
				|| session.getAttribute("newBondTypeCreated") != null) {
			SortedSet<String> bondTypes = lookupService
					.getAllLookupTypes("BondType");
			session.setAttribute("allBondTypes", bondTypes);
		}
		session.removeAttribute("newBondTypeCreated");

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
			throws Exception {
		if (session.getAttribute("characterizationSources") == null
				|| session.getAttribute("newCharacterizationSourceCreated") != null) {
			SortedSet<String> characterizationSources = lookupService
					.getAllCharacterizationSources();
			session.setAttribute("characterizationSources",
					characterizationSources);
		}
		session.removeAttribute("newCharacterizationSourceCreated");
	}

	public void setAllInstruments(HttpSession session) throws Exception {
		if (session.getAttribute("allInstruments") == null
				|| session.getAttribute("newInstrumentCreated") != null) {
			List<InstrumentBean> instruments = lookupService
					.getAllInstruments();
			SortedSet<String> instrumentTypes = new TreeSet<String>();
			for (InstrumentBean instrument : instruments) {
				instrumentTypes.add(instrument.getType());
			}
			SortedSet<String> manufacturers = lookupService
					.getAllManufacturers();
			session.setAttribute("allInstruments", instruments);
			session.setAttribute("allInstrumentTypes", instrumentTypes);
			session.setAttribute("allManufacturers", manufacturers);
		}
		session.removeAttribute("newInstrumentCreated");
	}

	public void setAllDerivedDataFileTypes(HttpSession session)
			throws Exception {
		if (session.getAttribute("allDerivedDataFileTypes") == null
				|| session.getAttribute("newCharacterizationFileTypeCreated") != null) {

			SortedSet<String> fileTypes = lookupService
					.getAllCharacterizationFileTypes();
			session.setAttribute("allDerivedDataFileTypes", fileTypes);
		}
		session.removeAttribute("newCharacterizationFileTypeCreated");
	}

	public void setAllFunctionTypes(HttpSession session) throws Exception {
		// set in application context
		if (session.getServletContext().getAttribute("allFunctionTypes") == null) {
			SortedSet<String> types = lookupService
					.getAllLookupTypes("FunctionType");
			session.getServletContext().setAttribute("allFunctionTypes", types);
		}
	}

	public void setAllCharacterizationTypes(HttpSession session)
			throws Exception {
		// set in application context
		if (session.getServletContext()
				.getAttribute("allCharacterizationTypes") == null) {
			List<CharacterizationTypeBean> types = lookupService
					.getAllCharacterizationTypes();
			session.getServletContext().setAttribute(
					"allCharacterizationTypes", types);
		}

		// set in application context mapping between characterization type and
		// child characterization name and abbrs
		if (session.getServletContext().getAttribute("allCharTypeChars") == null) {
			Map<String, List<CharacterizationBean>> charTypeChars = lookupService
					.getCharacterizationTypeCharacterizations();
			session.getServletContext().setAttribute("allCharTypeChars",
					charTypeChars);
		}
	}

	public void setDerivedDatumNames(HttpSession session,
			String characterizationName) throws Exception {
		SortedSet<String> categories = lookupService
				.getDerivedDataCategories(characterizationName);
		session.setAttribute("derivedDataCategories", categories);

		SortedSet<String> datumNames = lookupService
				.getDerivedDatumNames(characterizationName);
		session.setAttribute("datumNames", datumNames);
	}

	public void setAllCharacterizationMeasureUnitsTypes(HttpSession session,
			String charName) throws Exception {
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
}
