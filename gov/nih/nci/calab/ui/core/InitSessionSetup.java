package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.ContainerInfoBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabComparators;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.security.authorization.domainobjects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class InitSessionSetup {
	private static LookupService lookupService;
	private static UserService userService;

	private InitSessionSetup() throws Exception {
		lookupService = new LookupService();
		userService=new UserService(CalabConstants.CSM_APP_NAME);
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

	public void setAllUsers(HttpSession session) throws Exception {
		if ((session.getAttribute("newUserCreated") != null)
				|| (session.getServletContext().getAttribute("allUsers") == null)) {
			List allUsers = userService.getAllUsers();
			session.getServletContext().setAttribute("allUsers",
					allUsers);
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

	public void setAllSourceSampleIds(HttpSession session) throws Exception {
		if (session.getAttribute("allSourceSampleIds") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sourceSampleIds = lookupService.getAllSourceSampleIds();
			session.setAttribute("allSourceSampleIds", sourceSampleIds);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");

	}
	
	private void clearSessionData(HttpServletRequest request, String forwardPage) {
		HttpSession session = request.getSession();
		if (!(forwardPage.equals("createAliquot") || forwardPage
				.equals("createProject"))) {
			// clear session attributes created during create project or create
			// aliquot
			session.removeAttribute("allSamples");
			session.removeAttribute("allAliquotContainerTypes");
		}

		session.removeAttribute("aliquotMatrix");
		session.removeAttribute("createAliquotForm");
		session.removeAttribute("createSampleForm");

		// add a request parameter so the back button from search results don't
		// clear the forms
		if (request.getParameter("rememberSearch") == null) {
			session.removeAttribute("searchSampleForm");
			session.removeAttribute("searchAliquotForm");
			session.removeAttribute("searchWorkflowForm");
		}

		if (!forwardPage.equals("createSample")) {
			// clear session attributes created during create sample
			session.removeAttribute("allSampleContainerTypes");
		}

		if (!forwardPage.equals("searchSample")) {
			// clear session attributes created during search sample
			session.removeAttribute("sampleContainers");
			session.removeAttribute("aliquots");
		}

		if (!forwardPage.equals("uploadForward")) {
			// clear session attributes creatd during fileUpload
			session.removeAttribute("httpFileUploadSessionData");
		}

		if (forwardPage.equals("createSample")
				|| forwardPage.equals("createAliquot")
				|| forwardPage.equals("searchSample")
				|| forwardPage.equals("searchWorkflow")) {
			// clear session attributes created during execute workflow pages
			session.removeAttribute("workflow");
			session.removeAttribute("runId");
			session.removeAttribute("runName");
			session.removeAttribute("assayName");
			session.removeAttribute("assayType");
			session.removeAttribute("menuType");
			session.removeAttribute("inout");
		}

		// get user and date information
		String creator = "";
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			creator = user.getLoginName();
		}
		String creationDate = StringUtils.convertDateToString(new Date(),
				CalabConstants.DATE_FORMAT);
		session.setAttribute("creator", creator);
		session.setAttribute("creationDate", creationDate);
	}

	public static LookupService getLookupService() {
		return lookupService;
	}

	public static UserService getUserService() {
		return userService;
	}
}
