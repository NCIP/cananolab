package gov.nih.nci.calab.ui.workflow;

/**
 * This Action class searches for assay runs for the given parameters.
 *  
 * @author pansu
 */

import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class CreateRunAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		// TODO fill in details for aliquot information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		// Get Parameters from form elements
		// Run info

		String assayName = (String) theForm.get("assayName");
		String runBy = (String) theForm.get("runBy");
		String runDateStr = (String) theForm.get("runDate");
		Date runDate = StringUtils.convertToDate(runDateStr,
				CaNanoLabConstants.ACCEPT_DATE_FORMAT);
		// get user and date information from session
		String creator = (String) session.getAttribute("creator");
		String creationDate = (String) session.getAttribute("creationDate");
		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		RunBean runBean = executeWorkflowService.saveRun(assayName, runBy,
				runDate, creator, creationDate);
		// Save Aliquots assigned
		String[] assignedAliquots = (String[]) theForm.get("assignedAliquots");
		String comments = (String) theForm.get("aliquotComment");

		executeWorkflowService.saveRunAliquots(runBean.getId(),
				assignedAliquots, comments, creator, creationDate);
		session.setAttribute("newRunCreated", "true");

		// add parameters to forward

		String extra = "?menuType=run&runId=" + runBean.getId();
		String newPath = mapping.findForward("success").getPath() + extra;
		ActionForward forward = new ActionForward();
		forward.setPath(newPath);

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session=request.getSession();	
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setAllAssayTypeAssays(session);
		InitSessionSetup.getInstance().setAllUsers(session);
		InitSessionSetup.getInstance().setSampleSourceUnmaskedAliquots(session);	
		
		return mapping.getInputForward();
	}
	
	public boolean loginRequired() {
		return true;
	}
}
