package gov.nih.nci.calab.ui.core;

/**
 * This class initializes session data to prepopulate the drop-down lists required 
 * in different view pages. 
 * 
 * @author pansu
 */

/* CVS $Id: InitSessionAction.java,v 1.6 2006-04-07 15:26:17 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.security.SecurityBean;
import gov.nih.nci.calab.service.administration.ManageAliquotService;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.search.SearchSampleService;
import gov.nih.nci.calab.service.search.SearchWorkflowService;
import gov.nih.nci.calab.service.util.StringUtils;

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
		String urlPrefix=request.getContextPath();		

		try {
			DynaActionForm theForm = (DynaActionForm) form;
			forwardPage = (String) theForm.get("forwardPage");

			// retrieve from sesssion first if available assuming these values
			// are not likely to change within the same session. If changed, the
			// session should be updated.
			LookupService lookupService = new LookupService();
			if (forwardPage.equals("useAliquot")) {
				String runId = (String) request.getParameter("runId");
				session.setAttribute("runId", runId);
				setUseAliquotSession(session, lookupService);
			} else if (forwardPage.equals("createSample")) {
				setCreateSampleSession(session, lookupService);

			} else if (forwardPage.equals("createAliquot")) {
				setCreateAliquotSession(session, lookupService, urlPrefix);
			} else if (forwardPage.equals("searchWorkflow")) {
				setSearchWorkflowSession(session, lookupService);
			} else if (forwardPage.equals("searchSample")) {
				setSearchSampleSession(session, lookupService);
			}
			// get user and date information
			String creator = "";
			if (session.getAttribute("user") != null) {
				SecurityBean user = (SecurityBean) session.getAttribute("user");
				creator = user.getLoginId();
			}
			String creationDate = StringUtils.convertDateToString(new Date(),
					"MM/dd/yyyy");
			session.setAttribute("creator", creator);
			session.setAttribute("creationDate", creationDate);
			forward = mapping.findForward(forwardPage);

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
			LookupService lookupService) {
		if (session.getAttribute("allUnmaskedAliquotIds") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List<String> allAliquotIds = lookupService.getUnmaskedAliquots();
			session.setAttribute("allUnmaskedAliquotIds", allAliquotIds);
		}
	}

	/**
	 * Set up session attributes for create sample page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setCreateSampleSession(HttpSession session,
			LookupService lookupService) {
		ManageSampleService mangeSampleService = new ManageSampleService();
		// if values don't exist in the database or if no new samples created.
		// call the service
		if (session.getAttribute("allSampleTypes") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleTypes = lookupService.getAllSampleTypes();
			session.setAttribute("allSampleTypes", sampleTypes);

		}
		if (session.getAttribute("allSampleSOPs") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleSOPs = mangeSampleService.getAllSampleSOPs();
			session.setAttribute("allSampleSOPs", sampleSOPs);
		}
		if (session.getAttribute("sampleContainerInfo") == null
				|| session.getAttribute("newSampleCreated") != null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.setAttribute("sampleContainerInfo", containerInfo);
		}
		// clear the form in the session
		if (session.getAttribute("createSampleForm") != null
				|| session.getAttribute("newSampleCreated") != null) {
			session.removeAttribute("createSampleForm");
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	/**
	 * Set up session attributes for create aliquot page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setCreateAliquotSession(HttpSession session,
			LookupService lookupService, String urlPrefix) {
		ManageAliquotService manageAliquotService = new ManageAliquotService();

		if (session.getAttribute("allSampleIds") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleIds = lookupService.getAllSampleIds();
			session.setAttribute("allSampleIds", sampleIds);
		}
		if (session.getAttribute("allUnmaskedAliquotIds") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List aliquotIds = lookupService.getUnmaskedAliquots();
			session.setAttribute("allUnmaskedAliquotIds", aliquotIds);
		}
		if (session.getAttribute("aliquotContainerInfo") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			ContainerInfoBean containerInfo = lookupService
					.getAliquotContainerInfo();
			session.setAttribute("aliquotContainerInfo", containerInfo);
		}
		if (session.getAttribute("aliquotCreateMethods") == null) {
			List methods = manageAliquotService.getAliquotCreateMethods(urlPrefix);
			session.setAttribute("aliquotCreateMethods", methods);
		}
		// clear the form in the session
		if (session.getAttribute("createAliquotForm") != null) {
			session.removeAttribute("createAliquotForm");
		}
		if (session.getAttribute("aliquotMatrix") != null) {
			session.removeAttribute("aliquotMatrix");
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
			LookupService lookupService) {
		SearchWorkflowService searchWorkflowService = new SearchWorkflowService();
		if (session.getAttribute("allAssayTypes") == null) {
			List assayTypes = lookupService.getAllAssayTypes();
			session.setAttribute("allAssayTypes", assayTypes);
		}
		if (session.getAttribute("allFileSubmitters") == null) {
			List submitters = searchWorkflowService.getAllFileSubmitters();
			session.setAttribute("allFileSubmitters", submitters);
		}
	}

	/**
	 * Set up session attributes for search sample page
	 * 
	 * @param session
	 * @param lookupService
	 */
	private void setSearchSampleSession(HttpSession session,
			LookupService lookupService) {
		SearchSampleService searchSampleService = new SearchSampleService();
		if (session.getAttribute("allSampleIds") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleIds = lookupService.getAllSampleIds();
			session.setAttribute("allSampleIds", sampleIds);
		}
		if (session.getAttribute("allAliquotIds") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			List aliquotIds = lookupService.getAliquots();
			session.setAttribute("allAliquotIds", aliquotIds);
		}
		if (session.getAttribute("allSampleTypes") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sampleTypes = lookupService.getAllSampleTypes();
			session.setAttribute("allSampleTypes", sampleTypes);
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
		if (session.getAttribute("allSampleSubmitters") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List submitters = searchSampleService.getAllSampleSubmitters();
			session.setAttribute("allSampleSubmitters", submitters);
		}
		if (session.getAttribute("sampleContainerInfo") == null
				|| session.getAttribute("newSampleCreated") != null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.setAttribute("sampleContainerInfo", containerInfo);
		}
		if (session.getAttribute("aliquotContainerInfo") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			ContainerInfoBean containerInfo = lookupService
					.getAliquotContainerInfo();
			session.setAttribute("aliquotContainerInfo", containerInfo);
		}

		// clear new aliquot created flag and new sample created flag
		session.removeAttribute("newAliquotCreated");
		session.removeAttribute("newSampleCreated");

		// clear session attributes created during search sample
		session.removeAttribute("samples");
		session.removeAttribute("aliquots");
	}
}
