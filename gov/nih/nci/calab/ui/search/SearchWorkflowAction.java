package gov.nih.nci.calab.ui.search;

/**
 * This class initializes session data to prepopulate the drop-down lists required 
 * in different view pages. 
 * 
 * @author pansu
 */

/* CVS $Id: SearchWorkflowAction.java,v 1.2 2006-03-24 21:49:51 pansu Exp $ */

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
			String assayType = (String) theForm.get("assayType");
			String assayRunDateBeginStr = (String) theForm
					.get("assayRunDateBegin");
			String assayRunDateEndStr = (String) theForm.get("assayRunDateEnd");

			Date assayRunDateBegin = assayRunDateBeginStr.length() == 0 ? null
					: StringUtils.convertToDate(assayRunDateBeginStr,
							"MM/dd/yyyy");
			Date assayRunDateEnd = assayRunDateEndStr.length() == 0 ? null
					: StringUtils.convertToDate(assayRunDateEndStr,
							"MM/dd/yyyy");

			String aliquotId = (String) theForm.get("aliquotId");
			boolean includeMaskedAliquots = ((String) theForm
					.get("includeMaskedAliquots")).equals("on") ? true : false;
			String fileName = (String) theForm.get("fileName");
			boolean isFileInput = ((String) theForm.get("isFileIn"))
					.equals("in") ? true : false;
			boolean isFileOutput = ((String) theForm.get("isFileOut"))
					.equals("out") ? true : false;
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
							assayRunDateEnd, aliquotId, includeMaskedAliquots,
							fileName, isFileInput, isFileOutput, fileSubmissionDateBegin,
							fileSubmissionDateEnd, fileSubmitter,
							includeMaskedFiles, criteriaJoin);
			if (workflows == null || workflows.isEmpty()) {
				ActionMessage msg = new ActionMessage(
						"message.searchworkflow.noresult");
				msgs.add("message", msg);
				saveMessages(request, msgs);
				forward = mapping.getInputForward();
			} else {
				request.setAttribute("workflows", workflows);
				forward = mapping.findForward("success");
			}
		} catch (Exception e) {
			ActionMessage error = new ActionMessage("error.searchworkflow");
			msgs.add("error", error);
			saveMessages(request, msgs);
			logger.error("Caught exception searching workflow data", e);
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
