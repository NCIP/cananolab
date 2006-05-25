package gov.nih.nci.calab.ui.core;

/**
 * This class initializes session and application scope data to prepopulate the drop-down lists required
 * in different view pages.
 *
 * @author pansu
 */

/* CVS $Id: InitSessionAction.java,v 1.46 2006-05-25 18:37:56 zengje Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.service.administration.ManageAliquotService;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.search.SearchSampleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class InitSessionAction extends AbstractDispatchAction {

	public ActionForward createRun(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "createRun");
		setCreateRunSession(request);
		return mapping.findForward("createRun");
	}

	public ActionForward createAssayRun(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "createAssayRun");
		setCreateRunSession(request);
		return mapping.findForward("createAssayRun");
	}

	private void setCreateRunSession(HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession();
		LookupService lookupService = new LookupService();

		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		if (session.getServletContext().getAttribute("allAssayTypes") == null) {
			List assayTypes = lookupService.getAllAssayTypes();
			session.getServletContext().setAttribute("allAssayTypes",
					assayTypes);
		}

		if (session.getAttribute("workflow") == null
				|| session.getAttribute("newWorkflowCreated") != null) {
			ExecuteWorkflowBean workflowBean = executeWorkflowService
					.getExecuteWorkflowBean();
			session.setAttribute("workflow", workflowBean);
		}
		if (session.getAttribute("allUnmaskedAliquots") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List aliquots = lookupService.getUnmaskedAliquots();
			session.setAttribute("allUnmaskedAliquots", aliquots);
		}
		if (session.getAttribute("allAssignedAliquots") == null) {
			List allAssignedAliquots = lookupService.getAllAssignedAliquots();
			session.setAttribute("allAssignedAliquots", allAssignedAliquots);
		}

		if (session.getServletContext().getAttribute("allAssayBeans") == null) {
			List allAssayBeans = lookupService.getAllAssayBeans();
			session.getServletContext().setAttribute("allAssayBeans",
					allAssayBeans);
		}

		if ((session.getAttribute("newUserCreated") != null) 
				|| (session.getServletContext().getAttribute("allUserBeans") == null)) {
			List allUserBeans = lookupService.getAllUserBeans();
			session.getServletContext().setAttribute("allUserBeans",
					allUserBeans);
		}

		session.removeAttribute("newWorkflowCreated");
		session.removeAttribute("newAliquotCreated");
		session.removeAttribute("newUserCreated");
	}

	public ActionForward useAliquot(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "useAliquot");
		HttpSession session = request.getSession();
		LookupService lookupService = new LookupService();
		if (session.getAttribute("allUnmaskedAliquots") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List<AliquotBean> allAliquots = lookupService.getUnmaskedAliquots();
			session.setAttribute("allUnmaskedAliquots", allAliquots);
		}
		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		if (session.getAttribute("workflow") == null
				|| session.getAttribute("newWorkflowCreated") != null) {
			ExecuteWorkflowBean workflowBean = executeWorkflowService
					.getExecuteWorkflowBean();
			session.setAttribute("workflow", workflowBean);
		}
		// clear the new aliquote created flag
		session.removeAttribute("newAliquotCreated");
		// clear the new workflow created flag
		session.removeAttribute("newWorkflowcreated");
		ActionForward forward = mapping.findForward("useAliquot");
		return forward;
	}

	public ActionForward createSample(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "createSample");
		HttpSession session = request.getSession();
		LookupService lookupService = new LookupService();
		ManageSampleService mangeSampleService = new ManageSampleService();
		// if values don't exist in the database or if no new samples created.
		// call the service
		if (session.getAttribute("allSampleContainerTypes") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List containerTypes = lookupService.getAllSampleContainerTypes();
			session.setAttribute("allSampleContainerTypes", containerTypes);
		}

		if (session.getServletContext().getAttribute("allSampleTypes") == null) {
			List sampleTypes = lookupService.getAllSampleTypes();
			session.getServletContext().setAttribute("allSampleTypes",
					sampleTypes);

		}
		if (session.getServletContext().getAttribute("allSampleSOPs") == null) {
			List sampleSOPs = mangeSampleService.getAllSampleSOPs();
			session.getServletContext().setAttribute("allSampleSOPs",
					sampleSOPs);
		}
		if (session.getServletContext().getAttribute("sampleContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.getServletContext().setAttribute("sampleContainerInfo",
					containerInfo);
		}

		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
		return mapping.findForward("createSample");
	}

	public ActionForward createAliquot(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "createAliquot");
		HttpSession session = request.getSession();
		LookupService lookupService = new LookupService();
		ManageAliquotService manageAliquotService = new ManageAliquotService();

		if (session.getAttribute("allSamples") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List samples = lookupService.getAllSamples();
			session.setAttribute("allSamples", samples);
		}
		if (session.getAttribute("allAliquotContainerTypes") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List containerTypes = lookupService.getAllAliquotContainerTypes();
			session.setAttribute("allAliquotContainerTypes", containerTypes);
		}

		if (session.getAttribute("allUnmaskedAliquots") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List aliquots = lookupService.getUnmaskedAliquots();
			session.setAttribute("allUnmaskedAliquots", aliquots);
		}
		if (session.getServletContext().getAttribute("aliquotContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getAliquotContainerInfo();
			session.getServletContext().setAttribute("aliquotContainerInfo",
					containerInfo);
		}
		if (session.getServletContext().getAttribute("aliquotCreateMethods") == null) {
			List methods = manageAliquotService.getAliquotCreateMethods();
			session.getServletContext().setAttribute("aliquotCreateMethods",
					methods);
		}

		// clear new aliquot created flag and new sample created flag
		session.removeAttribute("newAliquotCreated");
		session.removeAttribute("newSampleCreated");
		return mapping.findForward("createAliquot");
	}

	public ActionForward searchWorkflow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "searchWorkflow");
		HttpSession session = request.getSession();
		LookupService lookupService = new LookupService();

		if (session.getServletContext().getAttribute("allAssayTypes") == null) {
			List assayTypes = lookupService.getAllAssayTypes();
			session.getServletContext().setAttribute("allAssayTypes",
					assayTypes);
		}
		if ((session.getAttribute("newUserCreated") != null) 
				|| (session.getServletContext().getAttribute("allUserBeans") == null)) {
			List allUserBeans = lookupService.getAllUserBeans();
			session.getServletContext().setAttribute("allUserBeans",
					allUserBeans);
		}
		session.removeAttribute("newUserCreated");
		return mapping.findForward("searchWorkflow");
	}

	public ActionForward searchSample(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "searchSample");
		HttpSession session = request.getSession();
		LookupService lookupService = new LookupService();
		SearchSampleService searchSampleService = new SearchSampleService();

		if (session.getServletContext().getAttribute("allSampleTypes") == null) {
			List sampleTypes = lookupService.getAllSampleTypes();
			session.getServletContext().setAttribute("allSampleTypes",
					sampleTypes);
		}
		if (session.getAttribute("allSampleSources") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleSources = searchSampleService.getAllSampleSources();
			session.setAttribute("allSampleSources", sampleSources);
		}
		if (session.getAttribute("allSourceSampleIds") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sourceSampleIds = searchSampleService.getAllSourceSampleIds();
			session.setAttribute("allSourceSampleIds", sourceSampleIds);
		}
		if ((session.getAttribute("newUserCreated") != null) 
				|| (session.getServletContext().getAttribute("allUserBeans") == null)) {
			List allUserBeans = lookupService.getAllUserBeans();
			session.getServletContext().setAttribute("allUserBeans",
					allUserBeans);
		}
		if (session.getServletContext().getAttribute("sampleContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.getServletContext().setAttribute("sampleContainerInfo",
					containerInfo);
		}
		if (session.getServletContext().getAttribute("aliquotContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getAliquotContainerInfo();
			session.getServletContext().setAttribute("aliquotContainerInfo",
					containerInfo);
		}
		// clear the new sample created flag
		session.removeAttribute("newUserCreated");
		session.removeAttribute("newSampleCreated");
		return mapping.findForward("searchSample");
	}

	public ActionForward workflowMessage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		clearSessionData(request, "workflowMessage");
		setWorkflowMessageSession(request);
		ActionForward forward = mapping.findForward("workflowMessage");
		return forward;
	}

	public ActionForward fileDownload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "fileDownload");
		setWorkflowMessageSession(request);
		// add inout and run information to parameters
		String extra = constructFileParams(request);
		String newPath = mapping.findForward("fileDownload").getPath() + extra;
		ActionForward forward = new ActionForward();
		forward.setPath(newPath);
		return forward;
	}

	public ActionForward fileUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "fileUpload");
		String menuType = (request.getSession().getAttribute("menuType") == null) ? ""
				: "&menuType=" + request.getSession().getAttribute("menuType");
		// add inout, run information and menuType to parameters
		String extra = constructFileParams(request) + menuType;
		String newPath = mapping.findForward("fileUpload").getPath() + extra;
		ActionForward forward = new ActionForward();
		forward.setPath(newPath);
		return forward;
	}

	public ActionForward runFileMask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "runFileMask");
		setWorkflowMessageSession(request);
		//add inout and run information to parameters
		String extra = constructFileParams(request);
		String newPath = mapping.findForward("runFileMask").getPath() + "?"
				+ extra;
		ActionForward forward = new ActionForward();
		forward.setPath(newPath);
		return forward;
	}

	private String constructFileParams(HttpServletRequest request) {
		// try getting from session first
		String inout = (request.getSession().getAttribute("inout") == null) ? ""
				: "&inout=" + request.getSession().getAttribute("inout");
		String runId = (request.getSession().getAttribute("runId") == null) ? ""
				: "&runId=" + request.getSession().getAttribute("runId");
		String runName = (request.getSession().getAttribute("runName") == null) ? ""
				: "&runName=" + request.getSession().getAttribute("runName");
		String assayName = (request.getSession().getAttribute("assayName") == null) ? ""
				: "&assayName="
						+ request.getSession().getAttribute("assayName");
		String assayType = (request.getSession().getAttribute("assayType") == null) ? ""
				: "&assayType="
						+ request.getSession().getAttribute("assayType");
		
		//then trying getting from request parameters if any
		inout = (request.getParameter("inout") == null) ? inout
				: "&inout=" + request.getParameter("inout");
		runId = (request.getParameter("runId") == null) ? runId
				: "&runId=" + request.getParameter("runId");
		runName = (request.getParameter("runName") == null) ? runName
				: "&runName=" + request.getParameter("runName");
		assayName = (request.getParameter("assayName") == null) ? assayName
				: "&assayName="
						+ request.getParameter("assayName");
		assayType = (request.getParameter("assayType") == null) ? assayType
				: "&assayType="
						+ request.getParameter("assayType");
		String params = inout + runId + runName + assayName + assayType;
		return params;
	}

	private String constructGetParams(HttpFileUploadSessionData hFileUploadData) {
		if (hFileUploadData == null) {
			return "";
		}
		String menuType = hFileUploadData.getFromType();
		String inout = (hFileUploadData.getInout() == null || menuType
				.equals("upload")) ? "" : "&inout="
				+ hFileUploadData.getInout();
		String params = "?menuType=" + hFileUploadData.getFromType()
				+ "&assayType=" + hFileUploadData.getAssayType()
				+ "&assayName=" + hFileUploadData.getAssay() + "&runId="
				+ hFileUploadData.getRunId() + "&runName="
				+ hFileUploadData.getRun() + inout;
		return params;
	}

	public ActionForward uploadForward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		clearSessionData(request, "uploadForward");
		setWorkflowMessageSession(request);

		HttpSession session = request.getSession();
		// read HttpFileUploadSessionData from session
		HttpFileUploadSessionData hFileUploadData = (HttpFileUploadSessionData) session
				.getAttribute("httpFileUploadSessionData");
		session.removeAttribute("httpFileUploadSessionData");
		String newPath = mapping.findForward("workflowMessage").getPath()
				+ constructGetParams(hFileUploadData);
		ActionForward forward = new ActionForward();
		forward.setPath(newPath);
		return forward;
	}

	private void setWorkflowMessageSession(HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession();
		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		if (session.getAttribute("workflow") == null
				|| session.getAttribute("newWorkflowCreated") != null) {
			ExecuteWorkflowBean workflowBean = executeWorkflowService
					.getExecuteWorkflowBean();
			session.setAttribute("workflow", workflowBean);
		}
		session.removeAttribute("newWorkflowCreated");
	}

	public boolean loginRequired() {
		return true;
	}

	private void clearSessionData(HttpServletRequest request, String forwardPage) {
		HttpSession session = request.getSession();
		if (!forwardPage.equals("createAliquot")) {
			// clear session attributes created during create aliquot
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
			session.removeAttribute("samples");
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
			UserBean user = (UserBean) session.getAttribute("user");
			creator = user.getLoginId();
		}
		String creationDate = StringUtils.convertDateToString(new Date(),
				CalabConstants.DATE_FORMAT);
		session.setAttribute("creator", creator);
		session.setAttribute("creationDate", creationDate);
	}
}
