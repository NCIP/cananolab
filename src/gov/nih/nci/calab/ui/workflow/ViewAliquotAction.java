package gov.nih.nci.calab.ui.workflow;

/**
 * This class shows the view aliquot page .
 * 
 * @author pansu
 */

/* CVS $Id: ViewAliquotAction.java,v 1.2 2006-04-07 15:30:05 pansu Exp $*/

import gov.nih.nci.calab.dto.administration.AliquotBean;
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
import org.apache.struts.action.DynaActionForm;

public class ViewAliquotAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(ViewAliquotAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		String aliquotId = null;
		try {
			DynaActionForm theForm = (DynaActionForm) form;
			aliquotId = (String) theForm.get("aliquotId");

			ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
			AliquotBean aliquot=executeWorkflowService.getAliquot(aliquotId);
			request.setAttribute("aliquot", aliquot);
			forward = mapping.findForward("success");
		} catch (Exception e) {
			logger.error("Caught exception when showing aliquot.", e);
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage("error.viewAliquot",
					aliquotId);
			errors.add("error", error);
			saveMessages(request, errors);
			forward = mapping.findForward("failure");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

}
