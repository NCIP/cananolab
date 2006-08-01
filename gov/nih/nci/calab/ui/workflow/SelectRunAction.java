package gov.nih.nci.calab.ui.workflow;

/**
 * This Action class saves user entered new Run information and the assigned aliquots
 * into the database.
 * 
 * @author caLAB Team
 */

import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SelectRunAction extends AbstractBaseAction {

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// TODO fill in details for aliquot information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		
		String runId= (String) theForm.get("runId");
		request.getSession().setAttribute("newRunCreated", "true");
		// add parameters to forward
		String extra = "&menuType=run&runId="+runId;
		String newPath = mapping.findForward("success").getPath() + extra;
		ActionForward forward = new ActionForward();
		forward.setPath(newPath);
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
