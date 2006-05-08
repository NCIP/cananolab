package gov.nih.nci.calab.ui.workflow;

/**
 * This class saves the association between a run and the user selected aliquot IDs and comments .
 * 
 * @author pansu
 */

/* CVS $Id: UseAliquotAction.java,v 1.13 2006-05-08 19:39:18 pansu Exp $*/

import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class UseAliquotAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		String[] aliquotIds = null;
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String runId = (String) theForm.get("runId");		
		aliquotIds = (String[]) theForm.get("aliquotIds");
		String comments = (String) theForm.get("comments");

		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		executeWorkflowService.saveRunAliquots(runId, aliquotIds, comments,
				(String) session.getAttribute("creator"), (String) session
						.getAttribute("creationDate"));
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.useAliquot");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		session.setAttribute("newWorkflowCreated", "true");
		forward = mapping.findForward("success");
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
