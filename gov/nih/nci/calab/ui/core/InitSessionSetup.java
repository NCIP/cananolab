package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.ContainerInfoBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CalabComparators;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.common.util.StringHelper;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		userService = new UserService(CalabConstants.CSM_APP_NAME);
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
					new CalabComparators.SortableNameComparator());
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
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleSources = lookupService.getAllSampleSources();
			session.setAttribute("allSampleSources", sampleSources);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void clearSampleSourcesSession(HttpSession session) {
		session.removeAttribute("allSampleSources");
	}

	public void setAllSampleContainerTypes(HttpSession session)
			throws Exception {
		if (session.getAttribute("allSampleContainerTypes") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List containerTypes = lookupService.getAllSampleContainerTypes();
			session.setAttribute("allSampleContainerTypes", containerTypes);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void clearSampleContainerTypesSession(HttpSession session) {
		session.removeAttribute("allSampleContainerTypes");
	}

	public void setAllSampleTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allSampleTypes") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleTypes = lookupService.getAllSampleTypes();
			session.getServletContext().setAttribute("allSampleTypes",
					sampleTypes);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void clearSampleTypesSession(HttpSession session) {
		session.removeAttribute("allSampleTypes");
	}

	public void setAllSampleSOPs(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allSampleSOPs") == null) {
			List sampleSOPs = lookupService.getAllSampleSOPs();
			session.getServletContext().setAttribute("allSampleSOPs",
					sampleSOPs);
		}
	}

	public void setAllSampleContainerInfo(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("sampleContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.getServletContext().setAttribute("sampleContainerInfo",
					containerInfo);
		}
	}

	public void setCurrentUser(HttpSession session) throws Exception {

		// get user and date information
		String creator = "";
		if (session.getAttribute("user") != null) {
			UserBean user = (UserBean) session.getAttribute("user");
			creator = user.getLoginName();
		}
		String creationDate = StringUtils.convertDateToString(new Date(),
				CalabConstants.DATE_FORMAT);
		session.setAttribute("creator", creator);
		session.setAttribute("creationDate", creationDate);
	}

	public void setAllAliquotContainerTypes(HttpSession session)
			throws Exception {
		if (session.getAttribute("allAliquotContainerTypes") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List containerTypes = lookupService.getAllAliquotContainerTypes();
			session.setAttribute("allAliquotContainerTypes", containerTypes);
		}
	}

	public void clearAliquotContainerTypesSession(HttpSession session) {
		session.removeAttribute("allAliquotContainerTypes");
	}

	public void setAllAliquotContainerInfo(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute("aliquotContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getAliquotContainerInfo();
			session.getServletContext().setAttribute("aliquotContainerInfo",
					containerInfo);
		}
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
					new CalabComparators.SortableNameComparator());

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

	public void setAllParticleTypeParticles(HttpSession session)
			throws Exception {
		if (session.getAttribute("allParticleTypeParticles") == null
				|| session.getAttribute("newSampleCreated") != null) {
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
	}

	public void setAllDendrimerCores(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allDendrimerCores") == null) {
			String[] dendrimerCores = lookupService.getAllDendrimerCores();
			session.getServletContext().setAttribute("allDendrimerCores",
					dendrimerCores);
		}
	}

	public void setAllDendrimerSurfaceGroupNames(HttpSession session)
			throws Exception {
		System.out.println("sesion attribute = "
				+ session.getAttribute("newCharacterizationCreated"));
		System.out.println("allDendrimerSurfaceGroupNames = "
				+ session.getServletContext().getAttribute(
						"allDendrimerSurfaceGroupNames"));
		if (session.getServletContext().getAttribute(
				"allDendrimerSurfaceGroupNames") == null
				|| session.getAttribute("newCharacterizationCreated") != null) {
			String[] surfaceGroupNames = lookupService
					.getAllDendrimerSurfaceGroupNames();
			session.getServletContext().setAttribute(
					"allDendrimerSurfaceGroupNames", surfaceGroupNames);
		}
	}

	public void setAllDendrimerBranches(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allDendrimerBranches") == null
				|| session.getAttribute("newCharacterizationCreated") != null) {
			String[] branches = lookupService.getAllDendrimerBranches();
			session.getServletContext().setAttribute("allDendrimerBranches",
					branches);
		}
	}

	public void setAllDendrimerGenerations(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute("allDendrimerGenerations") == null) {
			String[] generations = lookupService.getAllDendrimerGenerations();
			session.getServletContext().setAttribute("allDendrimerGenerations",
					generations);
		}
	}

	public void addSessionAttributeElement(HttpSession session,
			String attributeName, String newElement) throws Exception {
		String[] attributeValues = (String[]) session.getServletContext()
				.getAttribute(attributeName);
		if (!StringHelper.contains(attributeValues, newElement, true)) {
			session.getServletContext().setAttribute(attributeName,
					StringHelper.add(attributeValues, newElement));
		}
	}

	public void setAllMetalCompositions(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allMetalCompositions") == null) {
			String[] compositions = lookupService.getAllMetalCompositions();
			session.getServletContext().setAttribute("allMetalCompositions",
					compositions);
		}
	}

	public void setAllPolymerInitiators(HttpSession session) throws Exception {
		if ((session.getServletContext().getAttribute("allPolymerInitiators") == null)
				|| (session.getAttribute("newCharacterizationCreated") != null)) {
			String[] initiators = lookupService.getAllPolymerInitiators();
			session.getServletContext().setAttribute("allPolymerInitiators",
					initiators);
		}
	}

	public void setAllParticleFunctionTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext()
				.getAttribute("allParticleFunctionTypes") == null) {
			String[] functions = lookupService.getAllParticleFunctions();
			session.getServletContext().setAttribute(
					"allParticleFunctionTypes", functions);
		}
	}

	public void setAllParticleSources(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allParticleSources") == null) {
			List<String> sources = lookupService.getAllParticleSources();
			session.getServletContext().setAttribute("allParticleSources",
					sources);
		}
	}

	public void setAllParticleCharacterizationTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext()
				.getAttribute("allCharacterizationTypes") == null) {
			String[] charTypes = lookupService.getAllCharacterizationTypes();
			session.getServletContext().setAttribute(
					"allCharacterizationTypes", charTypes);
		}
	}

	public void setSideParticleMenu(HttpServletRequest request,
			String particleName, String particleType) throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute("charTypeChars") == null
				|| session.getAttribute("newCharacterizationCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			SearchNanoparticleService service = new SearchNanoparticleService();
			List<CharacterizationBean> charBeans = service
					.getCharacterizationInfo(particleName, particleType);
			Map<String, List<CharacterizationBean>> existingCharTypeChars = new HashMap<String, List<CharacterizationBean>>();
			if (!charBeans.isEmpty()) {
				Map<String, String[]> charTypeChars = lookupService
						.getCharacterizationTypeCharacterizations();

				for (String charType : charTypeChars.keySet()) {
					List<CharacterizationBean> newCharBeans = new ArrayList<CharacterizationBean>();
					List<String> charList = Arrays.asList(charTypeChars
							.get(charType));
					for (CharacterizationBean charBean : charBeans) {
						if (charList.contains(charBean.getName())) {
							newCharBeans.add(charBean);
						}
					}
					if (!newCharBeans.isEmpty()) {
						existingCharTypeChars.put(charType, newCharBeans);
					}
				}
			}
			session.setAttribute("charTypeChars", existingCharTypeChars);

			UserBean user = (UserBean) session.getAttribute("user");
			List<LabFileBean> reportBeans = service.getReportInfo(particleName,
					particleType, CananoConstants.NCL_REPORT, user);
			session.setAttribute("charTypeReports", reportBeans);
			
			List<LabFileBean> associatedBeans = service.getReportInfo(particleName,
					particleType, CananoConstants.ASSOCIATED_FILE, user);
			session.setAttribute("charTypeAssociatedFiles", associatedBeans);
		}
		session.removeAttribute("newCharacterizationCreated");

		if (session.getAttribute("funcTypeFuncs") == null
				|| session.getAttribute("newFunctionCreated") != null
				|| session.getAttribute("newParticleCreated") != null) {
			SearchNanoparticleService service = new SearchNanoparticleService();
			Map<String, List<FunctionBean>> funcTypeFuncs = service
					.getFunctionInfo(particleName, particleType);
			session.setAttribute("funcTypeFuncs", funcTypeFuncs);
		}

		session.removeAttribute("newFunctionCreated");
		session.removeAttribute("newParticleCreated");
		session.removeAttribute("detailPage");
		setStaticDropdowns(session);
	}

	public void setCharacterizationTypeCharacterizations(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allCharacterizationTypeCharacterizations") == null) {
			Map<String, String[]> charTypeChars = lookupService
					.getCharacterizationTypeCharacterizations();
			session.getServletContext().setAttribute(
					"allCharacterizationTypeCharacterizations", charTypeChars);
		}
	}

	public String setAllInstrumentTypes(HttpSession session) throws Exception {
		String rv = "";
		if (session.getServletContext().getAttribute("allInstrumentTypes") == null) {
			String[] instrumentTypes = lookupService.getAllInstrumentTypes();
			session.getServletContext().setAttribute("allInstrumentTypes",
					instrumentTypes);
			if (instrumentTypes != null && instrumentTypes.length > 0)
				rv = instrumentTypes[0];
		}

		return rv;
	}

	public void setAllInstrumentTypeManufacturers(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute(
				"allInstrumentTypeManufacturers") == null) {
			Map<String, SortedSet<String>> instrumentManufacturers = lookupService.getAllInstrumentManufacturers();
			session.getServletContext().setAttribute(
					"allInstrumentTypeManufacturers", instrumentManufacturers);
		}
	}

	public void setAllSizeDistributionGraphTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allSizeDistributionGraphTypes") == null) {
			String[] graphTypes = lookupService.getSizeDistributionGraphTypes();
			session.getServletContext().setAttribute(
					"allSizeDistributionGraphTypes", graphTypes);
		}
	}

	public void setAllMolecularWeightDistributionGraphTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allMolecularWeightDistributionGraphTypes") == null) {
			String[] graphTypes = lookupService
					.getMolecularWeightDistributionGraphTypes();
			session.getServletContext().setAttribute(
					"allMolecularWeightDistributionGraphTypes", graphTypes);
		}
	}

	public void setAllMorphologyDistributionGraphTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allMorphologyDistributionGraphTypes") == null) {
			String[] graphTypes = lookupService
					.getMorphologyDistributionGraphTypes();
			session.getServletContext().setAttribute(
					"allMorphologyDistributionGraphTypes", graphTypes);
		}
	}

	public void setAllShapeDistributionGraphTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allShapeDistributionGraphTypes") == null) {
			String[] graphTypes = lookupService
					.getShapeDistributionGraphTypes();
			session.getServletContext().setAttribute(
					"allShapeDistributionGraphTypes", graphTypes);
		}
	}

	public void setAllStabilityDistributionGraphTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allStabilityDistributionGraphTypes") == null) {
			String[] graphTypes = lookupService
					.getStabilityDistributionGraphTypes();
			session.getServletContext().setAttribute(
					"allStabilityDistributionGraphTypes", graphTypes);
		}
	}

	public void setAllPurityDistributionGraphTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allPurityDistributionGraphTypes") == null) {
			String[] graphTypes = lookupService
					.getPurityDistributionGraphTypes();
			session.getServletContext().setAttribute(
					"allPurityDistributionGraphTypes", graphTypes);
		}
	}

	public void setAllSolubilityDistributionGraphTypes(HttpSession session)
			throws Exception {
		if (session.getServletContext().getAttribute(
				"allSolubilityDistributionGraphTypes") == null) {
			String[] graphTypes = lookupService
					.getSolubilityDistributionGraphTypes();
			session.getServletContext().setAttribute(
					"allSolubilityDistributionGraphTypes", graphTypes);
		}
	}

	public void setAllMorphologyTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allMorphologyTypes") == null) {
			String[] morphologyTypes = lookupService.getAllMorphologyTypes();
			session.getServletContext().setAttribute("allMorphologyTypes",
					morphologyTypes);
		}
	}

	public void setAllShapeTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allShapeTypes") == null) {
			String[] shapeTypes = lookupService.getAllShapeTypes();
			session.getServletContext().setAttribute("allShapeTypes",
					shapeTypes);
		}
	}

	public void setAllStressorTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allStessorTypes") == null) {
			String[] stressorTypes = lookupService.getAllStressorTypes();
			session.getServletContext().setAttribute("allStressorTypes",
					stressorTypes);
		}
	}

	public void setStaticDropdowns(HttpSession session) {
		// set static boolean yes or no and characterization source choices
		session.setAttribute("booleanChoices", CananoConstants.BOOLEAN_CHOICES);
		session.setAttribute("characterizationSources",
				CananoConstants.CHARACTERIZATION_SOURCES);
		session.setAttribute("allCarbonNanotubeWallTypes",
				CananoConstants.CARBON_NANOTUBE_WALLTYPES);
		session.setAttribute("allReportTypes", CananoConstants.REPORT_TYPES);
	}

	public void setAllRunFiles(HttpSession session, String particleName)
			throws Exception {
		if (session.getAttribute("allRunFiles") == null
				|| session.getAttribute("newParticleCreated") != null
				|| session.getAttribute("newRunCreated") != null) {
			SubmitNanoparticleService service = new SubmitNanoparticleService();
			List<LabFileBean> runFileBeans = service
					.getAllRunFiles(particleName);
			session.setAttribute("allRunFiles", runFileBeans);
		}
		session.removeAttribute("newParticleCreated");
		session.removeAttribute("newRunCreated");
	}

	public void setAllAreaMeasureUnits(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allAreaMeasureUnits") == null) {
			String[] areaUnits = lookupService.getAllAreaMeasureUnits();
			session.getServletContext().setAttribute("allAreaMeasureUnits",
					areaUnits);
		}
	}

	public void setAllChargeMeasureUnits(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allChargeMeasureUnits") == null) {
			String[] chargeUnits = lookupService.getAllChargeMeasureUnits();
			session.getServletContext().setAttribute("allChargeMeasureUnits",
					chargeUnits);
		}
	}

	public void setAllControlTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allControlTypes") == null) {
			String[] controlTypes = lookupService.getAllControlTypes();
			session.getServletContext().setAttribute("allControlTypes",
					controlTypes);
		}
	}

	public void setAllConditionTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allConditionTypes") == null) {
			String[] conditionTypes = lookupService.getAllConditionTypes();
			session.getServletContext().setAttribute("allConditionTypes",
					conditionTypes);
		}
	}

	public void setAllConditionUnits(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allConditionTypeUnits") == null) {
			Map<String, String[]> conditionTypeUnits = lookupService
					.getAllConditionUnits();
			session.getServletContext().setAttribute("allConditionTypeUnits",
					conditionTypeUnits);
		}
	}

	public void setAllAgentTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allAgentTypes") == null) {
			Map<String, String[]> agentTypes = lookupService.getAllAgentTypes();
			session.getServletContext().setAttribute("allAgentTypes",
					agentTypes);
		}
	}

	public void setAllAgentTargetTypes(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allAgentTargetTypes") == null) {
			Map<String, String[]> agentTargetTypes = lookupService
					.getAllAgentTargetTypes();
			session.getServletContext().setAttribute("allAgentTargetTypes",
					agentTargetTypes);
		}
	}

	public void setAllTimeUnits(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allTimeUnits") == null) {
			String[] timeUnits = lookupService.getAllTimeUnits();
			session.getServletContext().setAttribute("allTimeUnits", timeUnits);
		}
	}

	public void setAllConcentrationUnits(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allConcentrationUnits") == null) {
			String[] concentrationUnits = lookupService
					.getAllConcentrationUnits();
			session.getServletContext().setAttribute("allConcentrationUnits",
					concentrationUnits);
		}
	}

	public void setAllCellLines(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allCellLines") == null) {
			String[] cellLines = lookupService.getAllCellLines();
			session.getServletContext().setAttribute("allCellLines", cellLines);
		}
	}

	public void setAllActivationMethods(HttpSession session) throws Exception {
		if (session.getServletContext().getAttribute("allActivationMethods") == null) {
			String[] activationMethods = lookupService
					.getAllActivationMethods();
			session.getServletContext().setAttribute("allActivationMethods",
					activationMethods);
		}
	}

	// public void addCellLine(HttpSession session, String option) throws
	// Exception {
	// String[] cellLines = (String[])
	// session.getServletContext().getAttribute("allCellLines");
	// if (!StringHelper.contains(cellLines, option, true)) {
	// session.getServletContext().setAttribute("allCellLines",
	// StringHelper.add(cellLines, option));
	// }
	// }

}
