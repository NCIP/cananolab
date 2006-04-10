package gov.nih.nci.calab.ui.workflow;

/**
 * This class saves user edited run data. 
 * 
 * @author caLAB Team
 */


import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

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
import org.apache.struts.validator.DynaValidatorActionForm;

public class EditRunAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(EditRunAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ActionMessages messages = new ActionMessages();
		try {
			
			DynaValidatorActionForm theForm = (DynaValidatorActionForm) form;
			String assayId	= (String) theForm.get("assayId"); 
			String runBy	= (String) theForm.get("runBy"); 
			String runDate	= (String) theForm.get("runDate");
			//SimpleDateFormatter dateFormat = new SimpleDateFormatter("MM/dd/yyyy");
			//Get Current session user
			String createdBy	= "Current User"; 
			String createdDate	= "12/12/2006";	
			//Do nothing
			ExecuteWorkflowService workflowService=new ExecuteWorkflowService();
			forward = mapping.findForward("success");
			
		} catch (Exception e) {
			ActionMessages errors=new ActionMessages();
			ActionMessage error=new ActionMessage("error.editRun");
			errors.add("error", error);
			saveMessages(request, errors);
			logger.error("Caught exception when showing edit aliquot page", e);
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
