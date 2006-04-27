package gov.nih.nci.calab.ui.core;

/**
 * This class initializes session and application scope data to prepopulate the drop-down lists required
 * in different view pages.
 *
 * @author pansu
 */

/* CVS $Id: InitSessionAction.java,v 1.31 2006-04-27 20:26:25 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.security.SecurityBean;
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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class InitSessionAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(InitSessionAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		ActionForward forward = null;
		String forwardPage = null;
		String urlPrefix = request.getContextPath();

		try {
			DynaActionForm theForm = (DynaActionForm) form;
			forwardPage = (String) theForm.get("forwardPage");
			// clean up session data
			clearSessionData(session, forwardPage);

			// get user and date information
			String creator = "";
			if (session.getAttribute("user") != null) {
				SecurityBean user = (SecurityBean) session.getAttribute("user");
				creator = user.getLoginId();
			}
			String creationDate = StringUtils.convertDateToString(new Date(),
					CalabConstants.DATE_FORMAT);
			session.setAttribute("creator", creator);
			session.setAttribute("creationDate", creationDate);

			// retrieve from sesssion first if available assuming these values
			// are not likely to change within the same session. If changed, the
			// session should be updated.

			LookupService lookupService = new LookupService();
			if (forwardPage.equals("useAliquot")) {
				setUseAliquotSession(session, lookupService);
			} else if (forwardPage.equals("createSample")) {
				setCreateSampleSession(session, lookupService);
			} else if (forwardPage.equals("createAliquot")) {
				setCreateAliquotSession(session, lookupService, urlPrefix);
			} else if (forwardPage.equals("searchWorkflow")) {
				setSearchWorkflowSession(session, lookupService);
			} else if (forwardPage.equals("searchSample")) {
				setSearchSampleSession(session, lookupService);
			} else if (forwardPage.equals("createRun")
					|| forwardPage.equals("createAssayRun")) {
				setCreateRunSession(session, lookupService);
			} else if (forwardPage.equals("workflowMessage")
					|| forwardPage.equals("fileUploadOption")
					|| forwardPage.equals("fileDownload")
					|| forwardPage.equals("fileMask")
					|| forwardPage.equals("fileMaskSetup")) {
				setWorkflowMessageSession(session);
			} else if (forwardPage.equals("uploadForward")) {
				// refresh tree view
				setWorkflowMessageSession(session);

				// read HttpFileUploadSessionData from session
				HttpFileUploadSessionData hFileUploadData = (HttpFileUploadSessionData) request
						.getSession().getAttribute("httpFileUploadSessionData");

				// based on the type=in/out/upload and runId to modify the
				// forwardPage
				String type = hFileUploadData.getFromType();
				String runId = hFileUploadData.getRunId();
				String inout = hFileUploadData.getInout();

				session.removeAttribute("httpFileUploadSessionData");

				if (type.equalsIgnoreCase("in") || type.equalsIgnoreCase("out")) {
					// cannot forward to a page with the request parameter, so
					// use response
					response.sendRedirect(urlPrefix
							+ "/workflowForward.do?type=" + type + "&runId="
							+ runId + "&inout=" + inout);
					forwardPage = null;
				} else if (type.equalsIgnoreCase("upload")) {
					session.setAttribute("runId", runId);
					forwardPage = "fileUploadOption";
				}
			}
			if (forwardPage == null) {
				// for response.setRedirect()
				forward = null;
			} else {
				forward = mapping.findForward(forwardPage);
			}

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.initSession",
					forwardPage);
			errors.add("error", error);
			saveMessages(request, errors);
			logger.error(
					"Caught exception loading initial drop-down lists data", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	/**
	 * Set up session attributes for use aliquot page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setUseAliquotSession(HttpSession session,
			LookupService lookupService) throws Exception {
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
	}

	/**
	 * Set up session and application attributes for create sample page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setCreateSampleSession(HttpSession session,
			LookupService lookupService) throws Exception {
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
	}

	/**
	 * Set up session and application attributes for create aliquot page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setCreateAliquotSession(HttpSession session,
			LookupService lookupService, String urlPrefix) throws Exception {
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
	}

	/**
	 * Set up session attributes for search workflow page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setSearchWorkflowSession(HttpSession session,
			LookupService lookupService) throws Exception {

		if (session.getServletContext().getAttribute("allAssayTypes") == null) {
			List assayTypes = lookupService.getAllAssayTypes();
			session.getServletContext().setAttribute("allAssayTypes",
					assayTypes);
		}
		if (session.getServletContext().getAttribute("allUsernames") == null) {
			List allUsernames = lookupService.getAllUsernames();
			session.getServletContext().setAttribute("allUsernames",
					allUsernames);
		}
	}

	/**
	 * Set up session attributes for search sample page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setSearchSampleSession(HttpSession session,
			LookupService lookupService) throws Exception {
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
		if (session.getServletContext().getAttribute("allUsernames") == null) {
			List allUsernames = lookupService.getAllUsernames();
			session.getServletContext().setAttribute("allUsernames",
					allUsernames);
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
		session.removeAttribute("newSampleCreated");
	}

	/**
	 * Set up session attributes for create Run
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setCreateRunSession(HttpSession session,
			LookupService lookupService) throws Exception {
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

		if (session.getServletContext().getAttribute("allUsernames") == null) {
			List allUsernames = lookupService.getAllUsernames();
			session.getServletContext().setAttribute("allUsernames",
					allUsernames);
		}

		session.removeAttribute("newWorkflowCreated");
		session.removeAttribute("newAliquotCreated");

	}

	private void setWorkflowMessageSession(HttpSession session)
			throws Exception {

		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		if (session.getAttribute("workflow") == null
				|| session.getAttribute("newWorkflowCreated") != null) {
			ExecuteWorkflowBean workflowBean = executeWorkflowService
					.getExecuteWorkflowBean();
			session.setAttribute("workflow", workflowBean);
		}
		session.removeAttribute("newWorkflowCreated");
	}

	private void clearSessionData(HttpSession session, String forwardPage) {
		if (!forwardPage.equals("createAliquot")) {
			// clear session attributes created during create aliquot
			session.removeAttribute("aliquotMatrix");
			session.removeAttribute("allSamples");
			session.removeAttribute("allAliquotContainerTypes");
		}

		session.removeAttribute("createAliquotForm");
		session.removeAttribute("createSampleForm");

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
		}
	}
}
