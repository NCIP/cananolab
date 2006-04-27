package gov.nih.nci.calab.ui.workflow;

/**
 * This class saves the association between a run and the user selected aliquot IDs and comments .
 * 
 * @author pansu
 */

/* CVS $Id: UseAliquotAction.java,v 1.10 2006-04-27 15:56:15 pansu Exp $*/

import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class UseAliquotAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(UseAliquotAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		String runId=null;
		String runName=null;
		String[] aliquotIds=null;
		HttpSession session = request.getSession();
		try {
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			runId = (String) theForm.get("runId");
			runName = (String) theForm.get("runName");
			aliquotIds = (String[]) theForm.get("aliquotIds");
			String comments = (String) theForm.get("comments");

			ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
			executeWorkflowService.saveRunAliquots(runId, aliquotIds, comments,
							(String)session.getAttribute("creator"),(String)session.getAttribute("creationDate") );
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("message.useAliquot", runName);
			msgs.add("message", msg);
			saveMessages(request, msgs);
			session.setAttribute("newWorkflowCreated", "true");
			forward = mapping.findForward("success");
		} catch (Exception e) {
			logger.error("Caught exception when saving selected aliquot IDs.",
					e);
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.useAliquot", runName);
			errors.add("error", error);
			saveMessages(request, errors);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

}
