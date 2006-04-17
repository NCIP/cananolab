package gov.nih.nci.calab.ui.workflow;

/**
 * This class prepares the data required for the workflow tree
 * 
 * @author pansu
 */

/* CVS $Id: ExecuteWorkflowAction.java,v 1.5 2006-04-17 14:46:31 pansu Exp $*/

import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
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

public class ExecuteWorkflowAction extends AbstractBaseAction {
	private static Logger logger = Logger
			.getLogger(ExecuteWorkflowAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();		
		try {			
			ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
			ExecuteWorkflowBean workflowBean=executeWorkflowService.getExecuteWorkflowBean();
			if (workflowBean.getAssayBeanMap().isEmpty()) {
				ActionMessage error = new ActionMessage("error.noworkflow");
				msgs.add("error", error);
				saveMessages(request, msgs);
				forward = mapping.findForward("failure");
			} else {
				request.setAttribute("workflow", workflowBean);
				forward = mapping.findForward("success");
			}
		} catch (Exception e) {
			logger.error(
					"Caught exception when searching for existing worklows", e);
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.executeWorkflow");
			msgs.add("error", error);
			saveMessages(request, errors);
			forward = mapping.findForward("failure");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

}
