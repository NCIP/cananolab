package gov.nih.nci.calab.ui.workflow;

/**
 * This class prepares the data to show in the page resulting from a workflow actions
 * 
 * @author pansu
 */

/* CVS $Id: WorkflowMessageAction.java,v 1.3 2006-12-13 19:33:09 pansu Exp $ */

import gov.nih.nci.calab.ui.core.AbstractBaseAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class WorkflowMessageAction extends AbstractBaseAction {

	public ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		response.setHeader("Cache-Control", "no-cache"); //fix for IE only
		InitSessionSetup.getInstance().setCurrentRun(request);
		ActionForward forward = mapping.findForward("success");
		return forward;
	}
	
	public boolean loginRequired() {
		return true;
	}
}
