package gov.nih.nci.calab.ui.core;

/**
 * This class initializes session data to prepopulate the drop-down lists required 
 * in different view pages. 
 * 
 * @author pansu
 */

/* CVS $Id: InitSessionAction.java,v 1.2 2006-03-23 21:33:31 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.service.administration.ManageAliquotService;
import gov.nih.nci.calab.service.administration.ManageSampleService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.search.SearchWorkflowService;

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
		String forwardPage=null;
		try {
			DynaActionForm theForm = (DynaActionForm) form;
			forwardPage = (String) theForm.get("forwardPage");
            
			// retrieve from sesssion first if available assuming these values
			// are not likely to change within the same session. If changed, the
			// session should be updated.
			LookupService lookupService = new LookupService();
			if (forwardPage.equals("useAliquot")) {
				String runId=(String)request.getParameter("runId");
				session.setAttribute("runId", runId);
				setUseAliquotSession(session, lookupService);
			} else if (forwardPage.equals("createSample")) {
				setCreateSampleSession(session, lookupService);

			} else if (forwardPage.equals("createAliquot")) {
				setCreateAliquotSession(session, lookupService);
			} else if (forwardPage.equals("searchWorkflow")) {
				setSearchWorkflowSession(session, lookupService);
			} else if (forwardPage.equals("searchSample")) {
				setSearchSampleSession(session, lookupService);
			}
			forward = mapping.findForward(forwardPage);
			
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.initSession", forwardPage);
			errors.add("error", error);
			saveMessages(request, errors);
			logger.error(
					"Caught exception loading initial drop-down lists data", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		// temporarily set to false until login module is working
		return false;
		// return true;
	}

	/**
	 * Set up session attributes for use aliquot page
	 * @param session
	 * @param lookupService
	 */
	private void setUseAliquotSession(HttpSession session,
			LookupService lookupService) {
		if (session.getAttribute("allAliquotIds") == null) {
			List<String> allAliquotIds = lookupService.getAliquots();
			session.setAttribute("allAliquotIds", allAliquotIds);
		}
	}

	/**
	 * Set up session attributes for create sample page
	 * @param session
	 * @param lookupService
	 */
	private void setCreateSampleSession(HttpSession session,
			LookupService lookupService) {
		ManageSampleService mangeSampleService = new ManageSampleService();
		if (session.getAttribute("allSampleTypes") == null) {
			List sampleTypes = lookupService.getAllSampleTypes();
			session.setAttribute("allSampleTypes", sampleTypes);
		}
		if (session.getAttribute("allSampleSOPs") == null) {
			List sampleSOPs = mangeSampleService.getAllSampleSOPs();
			session.setAttribute("allSampleSOPs", sampleSOPs);
		}
		if (session.getAttribute("sampleContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.setAttribute("sampleContainerInfo", containerInfo);
		}
		//clear the form in the session
		if (session.getAttribute("createSampleForm")!=null) {
			session.removeAttribute("createSampleForm");
		}
	}

	/**
	 * Set up session attributes for create aliquot page
	 * @param session
	 * @param lookupService
	 */
	private void setCreateAliquotSession(HttpSession session,
			LookupService lookupService) {
		ManageAliquotService manageAliquotService = new ManageAliquotService();

		if (session.getAttribute("allSampleIds") == null) {
			List sampleIds = lookupService.getAllSampleIds();
			session.setAttribute("allSampleIds", sampleIds);
		}
		if (session.getAttribute("allLotIds") == null) {
			List lotIds = lookupService.getAllLotIds();
			session.setAttribute("allLotIds", lotIds);
		}
		if (session.getAttribute("allAliquotIds") == null) {
			List aliquotIds = lookupService.getAliquots();
			session.setAttribute("allAliquotIds", aliquotIds);
		}
		if (session.getAttribute("aliquotContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getAliquotContainerInfo();
			session.setAttribute("aliquotContainerInfo", containerInfo);
		}
		if (session.getAttribute("aliquotCreateMethods") == null) {
			List methods = manageAliquotService.getAliquotCreateMethods();
			session.setAttribute("aliquotCreateMethods", methods);
		}
		//clear the form in the session
		if (session.getAttribute("createAliquotForm")!=null) {
			session.removeAttribute("createAliquotForm");
		}
		if (session.getAttribute("aliquotMatrix")!=null) {
			session.removeAttribute("aliquotMatrix");
		}
	}
	
	/**
	 * Set up session attributes for search workflow page
	 * @param session
	 * @param lookupService
	 */
	private void setSearchWorkflowSession(HttpSession session,
			LookupService lookupService) {
		SearchWorkflowService searchWorkflowService=new SearchWorkflowService();
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
	 * @param session
	 * @param lookupService
	 */
	private void setSearchSampleSession(HttpSession session,
			LookupService lookupService) {
		if (session.getAttribute("allSampleIds") == null) {
			List sampleIds = lookupService.getAllSampleIds();
			session.setAttribute("allSampleIds", sampleIds);
		}
		if (session.getAttribute("allLotIds") == null) {
			List lotIds = lookupService.getAllLotIds();
			session.setAttribute("allLotIds", lotIds);
		}
		if (session.getAttribute("allAliquotIds") == null) {
			List aliquotIds = lookupService.getAliquots();
			session.setAttribute("allAliquotIds", aliquotIds);
		}
		if (session.getAttribute("sampleContainerInfo") == null) {
			ContainerInfoBean containerInfo = lookupService
					.getSampleContainerInfo();
			session.setAttribute("sampleContainerInfo", containerInfo);
		}
	}
	
}
