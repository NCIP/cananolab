package gov.nih.nci.calab.ui.workflow;

/**
 * This Action class saves user entered new Run information and the assigned aliquots
 * into the database.
 * 
 * @author caLAB Team
 */

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

public class CreateRunAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(CreateRunAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ActionMessages messages=new ActionMessages();
		try {
			// TODO fill in details for aliquot information */
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			
			//Get Prameters from form elements
			//Run info 
			
			String assayId = (String) theForm.get("assayId");			
			String runBy = (String) theForm.get("runBy");
			String runDate = (String) theForm.get("runDate");
			// get user and date information from session
			String creator = (String)session.getAttribute("creator");
			String creationDate=(String)session.getAttribute("creationDate");
			ExecuteWorkflowService workflowService=new ExecuteWorkflowService();
			String runId = workflowService.saveRun(assayId,runBy,runDate,creator,creationDate);
			
			//Save Aliquots assigned
			String[] aliquotIds = (String[]) theForm.get("assignedAliquot");
			String comments =  (String)theForm.get("aliquotComment");
			
			workflowService.saveRunAliquots(runId, aliquotIds, comments,
					creator,creationDate);	
			
			session.setAttribute("newWorkflowCreated", "true");
			session.setAttribute("runId", runId);			
			//set Forward
			forward = mapping.findForward("success");
		} catch (Exception e) {
			ActionMessage error=new ActionMessage("error.createRun");
			messages.add("error", error);
			saveMessages(request, messages);
			logger.error("Caught exception when executing Run", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {		
		 return true;
	}
}

