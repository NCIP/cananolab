package gov.nih.nci.calab.ui.workflow;

/**
 * This class saves the association fields in a run.
 * 
 * @author caLAB Team
 */



import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class ExecuteWorkflowAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(ExecuteWorkflowAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		String runId=null;
		try {
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			//runId = (String) theForm.get("runId");
			String[] aliquotIds = (String[]) theForm.get("aliquotIds");
			String comments = (String) theForm.get("comments");

			ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
			executeWorkflowService.saveRunAliquots(runId, aliquotIds, comments);
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("message.executeWorkflow", runId);
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward = mapping.findForward("success");
		} catch (Exception e) {
			logger.error("Caught exception when saving selected Run.",
					e);
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.executeWorkflow", runId);
			errors.add("error", error);
			saveMessages(request, errors);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		// temporarily set to false until login module is working
		return false;
		// return true;
	}

}
