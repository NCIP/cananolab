package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationTypeBean;
import gov.nih.nci.calab.dto.common.InstrumentBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.ContainerInfoBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.search.GridSearchService;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.search.SearchReportService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.util.LabelValueBean;

/**
 * This class sets up session level or servlet context level variables to be
 * used in various actions during the setup of query forms.
 * 
 * @author pansu
 * 
 */
public class InitSessionSetup {
	private static LookupService lookupService;

	private static UserService userService;

	private InitSessionSetup() throws Exception {
		lookupService = new LookupService();
		userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	public static InitSessionSetup getInstance() throws Exception {
		return new InitSessionSetup();
	}

	public void setCurrentRun(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String runId = (String) request.getParameter("runId");

		if (runId == null && session.getAttribute("currentRun") == null) {
			throw new InvalidSessionException(
					"The session containing a run doesn't exists.");
		} else if (runId == null && session.getAttribute("currentRun") != null) {
			RunBean currentRun = (RunBean) session.getAttribute("currentRun");
			runId = currentRun.getId();
		}
		if (session.getAttribute("currentRun") == null
				|| session.getAttribute("newRunCreated") != null) {

			ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
			RunBean runBean = executeWorkflowService.getCurrentRun(runId);
			session.setAttribute("currentRun", runBean);
		}
		session.removeAttribute("newRunCreated");
	}

	public void clearRunSession(HttpSession session) {
		session.removeAttribute("currentRun");
	}

	public void setAllUsers(HttpSession session) throws Exception {
		if ((session.getAttribute("newUserCreated") != null)
				|| (session.getServletContext().getAttribute("allUsers") == null)) {
			List allUsers = userService.getAllUsers();
			session.getServletContext().setAttribute("allUsers", allUsers);
		}
		session.removeAttribute("newUserCreated");
	}

	public void setSampleSourceUnmaskedAliquots(HttpSession session)
			throws Exception {
		// set map between sample source and samples that have unmasked aliquots
		if (session.getAttribute("sampleSourceSamplesWithUnmaskedAliquots") == null
				|| session.getAttribute("allSampleSourcesWithUnmaskedAliquots") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			Map<String, SortedSet<SampleBean>> sampleSourceSamples = lookupService
					.getSampleSourceSamplesWithUnmaskedAliquots();
			session.setAttribute("sampleSourceSamplesWithUnmaskedAliquots",
					sampleSourceSamples);
			List<String> sources = new ArrayList<String>(sampleSourceSamples
					.keySet());
			session.setAttribute("allSampleSourcesWithUnmaskedAliquots",
					sources);
		}
		setAllSampleUnmaskedAliquots(session);
		session.removeAttribute("newAliquotCreated");
	}

	public void clearSampleSourcesWithUnmaskedAliquotsSession(
			HttpSession session) {
		session.removeAttribute("allSampleSourcesWithUnmaskedAliquots");
	}

	public void setAllSampleUnmaskedAliquots(HttpSession session)
			throws Exception {
		// set map between samples and unmasked aliquots
		if (session.getAttribute("allUnmaskedSampleAliquots") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			Map<String, SortedSet<AliquotBean>> sampleAliquots = lookupService
					.getUnmaskedSampleAliquots();
			List<String> sampleNames = new ArrayList<String>(sampleAliquots
					.keySet());
			Collections.sort(sampleNames,
					new CaNanoLabComparators.SortableNameComparator());
			session.setAttribute("allUnmaskedSampleAliquots", sampleAliquots);
			session.setAttribute("allSampleNamesWithAliquots", sampleNames);
		}
		session.removeAttribute("newAliquotCreated");
	}

	public void clearSampleUnmaskedAliquotsSession(HttpSession session) {
		session.removeAttribute("allUnmaskedSampleAliquots");
		session.removeAttribute("allSampleNamesWithAliquots");
	}

	public void setAllAssayTypeAssays(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allAssayTypes") == null) {
			List assayTypes = lookupService.getAllAssayTypes();
			session.getServletContext().setAttribute("allAssayTypes",
					assayTypes);
		}

		if (session.getServletContext().getAttribute("allAssayTypeAssays") == null) {
			Map<String, SortedSet<AssayBean>> assayTypeAssays = lookupService
					.getAllAssayTypeAssays();
			List<String> assayTypes = new ArrayList<String>(assayTypeAssays
					.keySet());
			session.getServletContext().setAttribute("allAssayTypeAssays",
					assayTypeAssays);

			session.getServletContext().setAttribute("allAvailableAssayTypes",
					assayTypes);
		}
	}

	public void setAllSampleSources(HttpSession session) throws Exception {

		if (session.getAttribute("allSampleSources") == null
				|| session.getAttribute("newSampleSourceCreated") != null) {
			SortedSet<String> sampleSources = lookupService
					.getAllSampleSources();
			session.setAttribute("allSampleSources", sampleSources);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleSourceCreated");
	}

	public void clearSampleSourcesSession(HttpSession session) {
		session.removeAttribute("allSampleSources");
	}

	public void setAllSampleContainerTypes(HttpSession session)
			throws Exception {
		if (session.getAttribute("allSampleContainerTypes") == null
				|| session.getAttribute("newSampleCreated") != null) {
			SortedSet<String> containerTypes = lookupService
					.getAllSampleContainerTypes();
			session.setAttribute("allSampleContainerTypes", containerTypes);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void clearSampleContainerTypesSession(HttpSession session) {
		session.removeAttribute("allSampleContainerTypes");
	}

	public void setAllSampleTypes(ServletContext appContext) throws Exception {
		if (appContext.getAttribute("allSampleTypes") == null) {
			SortedSet<String> types = lookupService
					.getAllLookupTypes("SampleType");
			appContext.setAttribute("allSampleTypes", types);
		}
	}

	public void clearSampleTypesSession(HttpSession session) {
		// session.removeAttribute("allSampleTypes");
	}

	public void setAllSampleSOPs(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allSampleSOPs") == null) {
			List sampleSOPs = lookupService.getAllSampleSOPs();
			session.getServletContext().setAttribute("allSampleSOPs",
					sampleSOPs);
		}
	}

	public void setAllSampleContainerInfo(HttpSession session) throws Exception {
		if (session.getAttribute("sampleContainerInfo") == null
				|| session.getAttribute("newSampleCreated") != null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.setAttribute("sampleContainerInfo", containerInfo);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void setCurrentUser(HttpSession session) throws Exception {

		// get user and date information
		String creator = "";
		if (session.getAttribute("user") != null) {
			UserBean user = (UserBean) session.getAttribute("user");
			creator = user.getLoginName();
		}
		String creationDate = StringUtils.convertDateToString(new Date(),
				CaNanoLabConstants.DATE_FORMAT);
		session.setAttribute("creator", creator);
		session.setAttribute("creationDate", creationDate);
	}

	public void setAllAliquotContainerTypes(HttpSession session)
			throws Exception {
		if (session.getAttribute("allAliquotContainerTypes") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			SortedSet<String> containerTypes = lookupService
					.getAllAliquotContainerTypes();
			session.setAttribute("allAliquotContainerTypes", containerTypes);
		}
		session.removeAttribute("newAliquotCreated");
	}

	public void clearAliquotContainerTypesSession(HttpSession session) {
		session.removeAttribute("allAliquotContainerTypes");
	}

	public void setAllAliquotContainerInfo(HttpSession session)
			throws Exception {
		if (session.getAttribute("aliquotContainerInfo") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			ContainerInfoBean containerInfo = lookupService
					.getAliquotContainerInfo();
			session.setAttribute("aliquotContainerInfo", containerInfo);
		}
		session.removeAttribute("newAliquotCreated");
	}

	public void setAllAliquotCreateMethods(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute("aliquotCreateMethods") == null) {
			List methods = lookupService.getAliquotCreateMethods();
			session.getServletContext().setAttribute("aliquotCreateMethods",
					methods);
		}
	}

	public void setAllSampleContainers(HttpSession session) throws Exception {
		if (session.getAttribute("allSampleContainers") == null
				|| session.getAttribute("newSampleCreated") != null) {
			Map<String, SortedSet<ContainerBean>> sampleContainers = lookupService
					.getAllSampleContainers();
			List<String> sampleNames = new ArrayList<String>(sampleContainers
					.keySet());
			Collections.sort(sampleNames,
					new CaNanoLabComparators.SortableNameComparator());

			session.setAttribute("allSampleContainers", sampleContainers);
			session.setAttribute("allSampleNames", sampleNames);
		}
		session.removeAttribute("newSampleCreated");
	}

	public void clearSampleContainersSession(HttpSession session) {
		session.removeAttribute("allSampleContainers");
		session.removeAttribute("allSampleNames");
	}

	public void setAllSourceSampleIds(HttpSession session) throws Exception {
		if (session.getAttribute("allSourceSampleIds") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sourceSampleIds = lookupService.getAllSourceSampleIds();
			session.setAttribute("allSourceSampleIds", sourceSampleIds);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void clearSourceSampleIdsSession(HttpSession session) {
		session.removeAttribute("allSourceSampleIds");
	}

	public void clearWorkflowSession(HttpSession session) {
		clearRunSession(session);
		clearSampleSourcesWithUnmaskedAliquotsSession(session);
		clearSampleUnmaskedAliquotsSession(session);
		session.removeAttribute("httpFileUploadSessionData");
	}

	public void clearSearchSession(HttpSession session) {
		clearSampleTypesSession(session);
		clearSampleContainerTypesSession(session);
		clearAliquotContainerTypesSession(session);
		clearSourceSampleIdsSession(session);
		clearSampleSourcesSession(session);
		session.removeAttribute("aliquots");
		session.removeAttribute("sampleContainers");
	}

	public void clearInventorySession(HttpSession session) {
		clearSampleTypesSession(session);
		clearSampleContainersSession(session);
		clearSampleContainerTypesSession(session);
		clearSampleUnmaskedAliquotsSession(session);
		clearAliquotContainerTypesSession(session);
		session.removeAttribute("createSampleForm");
		session.removeAttribute("createAliquotForm");
		session.removeAttribute("aliquotMatrix");
	}

	public static LookupService getLookupService() {
		return lookupService;
	}

	public static UserService getUserService() {
		return userService;
	}

	public boolean canUserExecuteClass(HttpSession session, Class classObj)
			throws CSException {
		UserBean user = (UserBean) session.getAttribute("user");
		// assume the part of the package name containing the function domain
		// is the same as the protection element defined in CSM
		String[] nameStrs = classObj.getName().split("\\.");
		String domain = nameStrs[nameStrs.length - 2];
		return userService.checkExecutePermission(user, domain);
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

	public void setAllVisibilityGroups(HttpSession session) throws Exception {
		if (session.getAttribute("allVisibilityGroups") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List<String> groupNames = userService.getAllVisibilityGroups();
			session.setAttribute("allVisibilityGroups", groupNames);
		}
		session.removeAttribute("newSampleCreated");
	}

	public void setAllCompositionDropdowns(HttpSession session)
			throws Exception {
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

	private void setAllReports(HttpSession session, String particleName,
			String particleType) throws Exception {
		UserBean user = (UserBean) session.getAttribute("user");
		SearchReportService searchReportService = new SearchReportService();
		if (session.getAttribute("particleReports") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {

			List<LabFileBean> reportBeans = searchReportService
					.getReportInfo(particleName, particleType,
							CaNanoLabConstants.REPORT, user);
			session.setAttribute("particleReports", reportBeans);
		}

		if (session.getAttribute("particleAssociatedFiles") == null
				|| session.getAttribute("newReportCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			List<LabFileBean> associatedBeans = searchReportService
					.getReportInfo(particleName, particleType,
							CaNanoLabConstants.ASSOCIATED_FILE, user);
			session.setAttribute("particleAssociatedFiles", associatedBeans);
		}
	}

	public void setSideParticleMenu(HttpServletRequest request,
			String particleName, String particleType) throws Exception {
		HttpSession session = request.getSession();
		setAllReports(session, particleName, particleType);
		// not part of the side menu, but need to up
		// if (session.getAttribute("newParticleCreated") != null) {
		// setParticleTypeParticles(session);
		// }
		setStaticDropdowns(session);
		setAllFunctionTypes(session);
		setFunctionTypeFunctions(session, particleName, particleType);
		setAllCharacterizations(session, particleName, particleType);
		session.removeAttribute("newParticleCreated");
		session.removeAttribute("newCharacterizationCreated");
		session.removeAttribute("newReportCreated");
		session.removeAttribute("newFunctionCreated");
		session.removeAttribute("detailPage");
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
			String particleName, String particleType) throws Exception {
		setAllCharacterizationTypes(session);
		Map<String, List<CharacterizationBean>> charTypeChars = (Map<String, List<CharacterizationBean>>) session
				.getServletContext().getAttribute("allCharTypeChars");
		if (session.getAttribute("allCharacterizations") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			// get saved characterizations based on the particle type and name
			SearchNanoparticleService service = new SearchNanoparticleService();
			List<CharacterizationBean> charBeans = service
					.getCharacterizationInfo(particleName, particleType);
			Map<String, List<CharacterizationBean>> charMap = new HashMap<String, List<CharacterizationBean>>();
			for (String charType : charTypeChars.keySet()) {
				List<CharacterizationBean> newCharBeans = new ArrayList<CharacterizationBean>();
				// get all characterizations for the characterization type
				List<CharacterizationBean> charList = (List<CharacterizationBean>) charTypeChars
						.get(charType);
				// set abbreviation for each saved characterization
				for (CharacterizationBean charBean : charBeans) {
					for (CharacterizationBean displayBean : charList) {
						if (displayBean.getName().equals(charBean.getName())) {
							charBean.setAbbr(displayBean.getAbbr());
							newCharBeans.add(charBean);
							break;
						}
					}
				}
				if (!newCharBeans.isEmpty()) {
					charMap.put(charType, newCharBeans);
				}
			}
			session.setAttribute("allCharacterizations", charMap);
		}
	}

	public void setFunctionTypeFunctions(HttpSession session,
			String particleName, String particleType) throws Exception {
		if (session.getAttribute("allFuncTypeFuncs") == null
				|| session.getAttribute("newParticleCreated") != null
				|| session.getAttribute("newFunctionCreated") != null) {
			SearchNanoparticleService service = new SearchNanoparticleService();
			Map<String, List<FunctionBean>> funcTypeFuncs = service
					.getFunctionInfo(particleName, particleType);
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
			session.setAttribute("remoteCharTypeChars", charTypeChars);
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
		setStaticDropdowns(session);
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

	public void setStaticDropdowns(HttpSession session) {
		// set static boolean yes or no and characterization source choices
		session.setAttribute("booleanChoices",
				CaNanoLabConstants.BOOLEAN_CHOICES);
		session.setAttribute("allCarbonNanotubeWallTypes",
				CaNanoLabConstants.CARBON_NANOTUBE_WALLTYPES);
		if (session.getAttribute("allReportTypes") == null) {
			String[] allReportTypes = lookupService.getAllReportTypes();
			session.setAttribute("allReportTypes", allReportTypes);
		}
		session.setAttribute("allFunctionLinkageTypes",
				CaNanoLabConstants.FUNCTION_LINKAGE_TYPES);
		session.setAttribute("allFunctionAgentTypes",
				CaNanoLabConstants.FUNCTION_AGENT_TYPES);
	}

	public void setProtocolType(HttpSession session) throws Exception {
		if (session.getAttribute("protocolTypes") == null
				|| session.getAttribute("newProtocolCreated") != null) {
			SortedSet<String> protocolTypes = lookupService
					.getAllLookupTypes("ProtocolType");
			session.setAttribute("protocolTypes", protocolTypes);
		}
		session.removeAttribute("newProtocolCreated");
	}

	public void setProtocolSubmitPage(HttpSession session, UserBean user)
			throws Exception {
		// set protocol types, and protocol names for all these types
		setProtocolType(session);
		SortedSet<String> protocolTypes = (SortedSet<String>) session
				.getAttribute("protocolTypes");
		SortedSet<ProtocolBean> pbs = lookupService.getAllProtocols(user);
		// Now generate two maps: one for type and nameList,
		// and one for type and protocolIdList (for the protocol name dropdown
		// box)
		Map<String, List<String>> typeNamesMap = new HashMap<String, List<String>>();
		Map<String, List<String>> typeIdsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> nameVersionsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> nameIdsMap = new HashMap<String, List<String>>();
		for (String type : protocolTypes) {
			for (ProtocolBean pb : pbs) {
				if (type.equals(pb.getType())) {
					List<String> nameList = typeNamesMap.get(type);
					List<String> idList = typeIdsMap.get(type);
					if (nameList == null) {
						nameList = new ArrayList<String>();
						nameList.add(pb.getName());
						typeNamesMap.put(type, nameList);
					} else {
						nameList.add(pb.getName());
					}
					if (idList == null) {
						idList = new ArrayList<String>();
						idList.add(pb.getId().toString());
						typeIdsMap.put(type, idList);
					} else {
						idList.add(pb.getId().toString());
					}
				}
			}
		}
		for (ProtocolBean pb : pbs) {
			String id = pb.getId();
			List<String> versionList = new ArrayList<String>();
			List<String> idList = new ArrayList<String>();
			List<ProtocolFileBean> fileBeanList = pb.getFileBeanList();
			Map<String, ProtocolFileBean> map = new HashMap<String, ProtocolFileBean>();

			for (ProtocolFileBean fb : fileBeanList) {
				versionList.add(fb.getVersion());
				map.put(fb.getVersion(), fb);
			}
			String[] vlist = versionList.toArray(new String[0]);
			Arrays.sort(vlist);
			versionList.clear();
			for (int i = 0; i < vlist.length; i++) {
				ProtocolFileBean fb = map.get(vlist[i]);
				versionList.add(fb.getVersion());
				idList.add(fb.getId());
			}

			nameVersionsMap.put(id, versionList);
			nameIdsMap.put(id, idList);
		}
		session.setAttribute("AllProtocolTypeNames", typeNamesMap);
		session.setAttribute("AllProtocolTypeIds", typeIdsMap);
		session.setAttribute("protocolNames", new ArrayList<String>());
		session.setAttribute("AllProtocolNameVersions", nameVersionsMap);
		session.setAttribute("AllProtocolNameFileIds", nameIdsMap);
		session.setAttribute("protocolVersions", new ArrayList<String>());
	}

	public void setAllProtocolNameVersionsByType(HttpSession session,
			String type) throws Exception {
		// set protocol name and its versions for a given protocol type.
		Map<ProtocolBean, List<ProtocolFileBean>> nameVersions = lookupService
				.getAllProtocolNameVersionByType(type);
		SortedSet<LabelValueBean> set = new TreeSet<LabelValueBean>();
		Set keySet = nameVersions.keySet();

		for (Iterator it = keySet.iterator(); it.hasNext();) {
			ProtocolBean pb = (ProtocolBean) it.next();
			List<ProtocolFileBean> fbList = nameVersions.get(pb);
			for (ProtocolFileBean fb : fbList) {
				set.add(new LabelValueBean(pb.getName() + " - "
						+ fb.getVersion(), fb.getId()));
			}
		}
		session.setAttribute("protocolNameVersionsByType", set);
	}

	public void setAllRunFiles(HttpSession session, String particleName)
			throws Exception {
		if (session.getAttribute("allRunFiles") == null
				|| session.getAttribute("newRunFileCreated") != null
				|| session.getAttribute("newFileLoaded") != null) {
			SubmitNanoparticleService service = new SubmitNanoparticleService();
			List<LabFileBean> runFileBeans = service
					.getAllRunFiles(particleName);
			session.setAttribute("allRunFiles", runFileBeans);
		}
		session.removeAttribute("newRunFileCreated");
		session.removeAttribute("newFileLoaded");
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

	public void setApplicationOwner(HttpSession session) {
		if (session.getServletContext().getAttribute("applicationOwner") == null) {
			session.getServletContext().setAttribute("applicationOwner",
					CaNanoLabConstants.APP_OWNER);
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

	/**
	 * Create default CSM groups for default visible groups and admin
	 * , and assign them with default protection groups and roles
	 */
	public void createDefaultCSMGroups() throws Exception {
		for (String groupName : CaNanoLabConstants.VISIBLE_GROUPS) {
			userService.createAGroup(groupName);
		}
		userService.createAGroup(CaNanoLabConstants.CSM_ADMIN);
		//assign admin to role D on characterization
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_ADMIN,
				CaNanoLabConstants.CSM_PG_CHARACTERIZATION,
				CaNanoLabConstants.CSM_DELETE_ROLE);
		//assign admin to role E on administration
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_ADMIN,
				CaNanoLabConstants.CSM_PG_ADMIN,
				CaNanoLabConstants.CSM_EXECUTE_ROLE);
		
		//assign pi to role E on submit, workflow, inventory
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI,
				CaNanoLabConstants.CSM_PG_SUBMIT,
				CaNanoLabConstants.CSM_EXECUTE_ROLE);
		
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI,
				CaNanoLabConstants.CSM_PG_INVENTORY,
				CaNanoLabConstants.CSM_EXECUTE_ROLE);
		
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI,
				CaNanoLabConstants.CSM_PG_WORKFLOW,
				CaNanoLabConstants.CSM_EXECUTE_ROLE);
		
		//assign researcher to role E on workflow, inventory		
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_RESEARCHER,
				CaNanoLabConstants.CSM_PG_INVENTORY,
				CaNanoLabConstants.CSM_EXECUTE_ROLE);
		
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_RESEARCHER,
				CaNanoLabConstants.CSM_PG_WORKFLOW,
				CaNanoLabConstants.CSM_EXECUTE_ROLE);
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

	public void updateEditableDropdown(HttpSession session,
			String formAttribute, String sessionAttributeName) {
		SortedSet<String> dropdown = (SortedSet<String>) session
				.getAttribute(sessionAttributeName);
		if (dropdown != null && formAttribute != null
				&& formAttribute.length() > 0) {
			dropdown.add(formAttribute);
		}
	}
}
