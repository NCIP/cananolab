package gov.nih.nci.calab.ui.workflow;

/**
 * This class shows the view aliquot page .
 * 
 * @author pansu
 */

/* CVS $Id: ViewAliquotAction.java,v 1.5 2006-06-30 20:56:30 pansu Exp $*/

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ViewAliquotAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaActionForm theForm = (DynaActionForm) form;
		String aliquotId = (String) theForm.get("aliquotId");

		ExecuteWorkflowService executeWorkflowService = new ExecuteWorkflowService();
		AliquotBean aliquot = executeWorkflowService.getAliquot(aliquotId);
		if (aliquot != null) {
			request.setAttribute("aliquot", aliquot);
			forward = mapping.findForward("success");
		} else {
			throw new CalabException("Can't find an aliquot by the given ID");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
