package gov.nih.nci.calab.ui.search;

/**
 * This class searches workflows based on user supplied criteria.
 * 
 * @author pansu
 */

/* CVS $Id: SearchWorkflowAction.java,v 1.7 2006-04-19 21:19:04 pansu Exp $ */

import gov.nih.nci.calab.dto.search.WorkflowResultBean;
import gov.nih.nci.calab.service.search.SearchWorkflowService;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchWorkflowAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(SearchWorkflowAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		try {
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			String assayName = (String) theForm.get("assayName");
			String assayType = ((String) theForm.get("assayType")).trim();
			String assayRunDateBeginStr = (String) theForm
					.get("assayRunDateBegin");
			String assayRunDateEndStr = (String) theForm.get("assayRunDateEnd");

			Date assayRunDateBegin = assayRunDateBeginStr.length() == 0 ? null
					: StringUtils.convertToDate(assayRunDateBeginStr,
							"MM/dd/yyyy");
			Date assayRunDateEnd = assayRunDateEndStr.length() == 0 ? null
					: StringUtils.convertToDate(assayRunDateEndStr,
							"MM/dd/yyyy");

			String aliquotName = (String) theForm.get("aliquotName");
			boolean includeMaskedAliquots = ((String) theForm
					.get("includeMaskedAliquots")).equals("on") ? true : false;
			String fileName = (String) theForm.get("fileName");
			boolean isFileInput = ((String) theForm.get("isFileIn"))
					.equals("on") ? true : false;
			boolean isFileOutput = ((String) theForm.get("isFileOut"))
					.equals("on") ? true : false;
			
			//if both are false set them to true
			if (!isFileInput&&!isFileOutput) {
				isFileInput=true;
				isFileOutput=true;
			}
			
			String fileSubmissionDateBeginStr = (String) theForm
					.get("fileSubmissionDateBegin");
			String fileSubmissionDateEndStr = (String) theForm
					.get("fileSubmissionDateEnd");
			Date fileSubmissionDateBegin = fileSubmissionDateBeginStr.length() == 0 ? null
					: StringUtils.convertToDate(fileSubmissionDateBeginStr,
							"MM/dd/yyyy");
			Date fileSubmissionDateEnd = fileSubmissionDateEndStr.length() == 0 ? null
					: StringUtils.convertToDate(fileSubmissionDateEndStr,
							"MM/dd/yyyy");

			String fileSubmitter = (String) theForm.get("fileSubmitter");
			boolean includeMaskedFiles = ((String) theForm
					.get("includeMaskedFiles")).equals("on") ? true : false;
			String criteriaJoin = (String) theForm.get("criteriaJoin");

			// pass the parameters to the searchWorkflowService
			SearchWorkflowService searchWorkflowService = new SearchWorkflowService();
			List<WorkflowResultBean> workflows = searchWorkflowService
					.searchWorkflows(assayName, assayType, assayRunDateBegin,
							assayRunDateEnd, aliquotName, includeMaskedAliquots,
							fileName, isFileInput, isFileOutput, fileSubmissionDateBegin,
							fileSubmissionDateEnd, fileSubmitter,
							includeMaskedFiles, criteriaJoin);
			if (workflows == null || workflows.isEmpty()) {
				ActionMessage msg = new ActionMessage(
						"message.searchWorkflow.noResult");
				msgs.add("message", msg);
				saveMessages(request, msgs);
				forward = mapping.getInputForward();
			} else {
				request.setAttribute("workflows", workflows);
				forward = mapping.findForward("success");
			}
		} catch (Exception e) {
			ActionMessage error = new ActionMessage("error.searchWorkflow");
			msgs.add("error", error);
			saveMessages(request, msgs);
			logger.error("Caught exception searching workflow data", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

}
