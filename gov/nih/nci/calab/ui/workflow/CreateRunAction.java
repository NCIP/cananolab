package gov.nih.nci.calab.ui.workflow;

/**
 * This class saves user entered new Run information 
 * into the database.
 * 
 * @author caLAB Team
 */

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
import org.apache.struts.validator.DynaValidatorForm;


import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;

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
			String assayId	= (String) theForm.get("assayId"); 
			String runBy	= (String) theForm.get("runBy"); 
			String runDate	= (String) theForm.get("runDate");
			//SimpleDateFormatter dateFormat = new SimpleDateFormatter("MM/dd/yyyy");
			//Get Current session user
			String createdBy	= "Current User"; 
			String createdDate	= "12/12/2006";			
			
			ExecuteWorkflowService workflowService=new ExecuteWorkflowService();
			//Save Run
			String runId = workflowService.saveRun(assayId,runBy,runDate,createdBy,createdDate );
			
			//File info
			String inFileName	= (String) theForm.get("inFileName");
			String outFileName	= (String) theForm.get("outFileName");
			String fileURI = ""; //Get from File Upload module			
			//Save File info
			workflowService.saveFileInfo(fileURI, inFileName,runId);
			workflowService.saveFileInfo(fileURI, outFileName,runId);
			
			
			//Other info
			String assayType	= (String) theForm.get("assayType");
			String assayName	= (String) theForm.get("assayName");
			String runName	= "Run 1";
			String aliquotIds	= (String) theForm.get("aliquotIds");
			String allAliquotIds	= (String) theForm.get("allAliquotIds");			
			String fileSubmissionDate	= (String) theForm.get("fileSubmissionDate");
			String fileSubmitter	= (String) theForm.get("fileSubmitter");
			String fileMaskStatus	= (String) theForm.get("fileMaskStatus");
			
			/*, assayName,
			runName, runDate, runBy, createdDate, createdBy, aliquotIds,
			allAliquotIds, fileSubmissionDate, fileSubmitter, fileMaskStatus);
			*/
			
			//Bean 
			//ExecuteWorkflowBean executeworkflow = new ExecuteWorkflowBean(inFileName, outFileName, assayType, assayName,runName, runDate, runBy, createdDate,	createdBy, aliquotIds,allAliquotIds, fileSubmissionDate, fileSubmitter, fileMaskStatus);
			
			//set to Request object			
			//request.setAttribute("executeworkflow", executeworkflow);
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
		// temporarily set to false until login module is working
		return false;
		// return true;
	}
}

