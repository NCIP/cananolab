package gov.nih.nci.calab.ui.workflow;

/**
 * This class searches workflows based on user supplied criteria.
 * 
 * @author pansu
 */

/* CVS $Id: SearchWorkflowAction.java,v 1.1 2007-02-28 21:56:15 pansu Exp $ */

import gov.nih.nci.calab.dto.workflow.WorkflowResultBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.workflow.SearchWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchWorkflowAction extends AbstractDispatchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String assayName = (String) theForm.get("assayName");
		String assayType = ((String) theForm.get("assayType")).trim();
		String assayRunDateBeginStr = (String) theForm.get("assayRunDateBegin");
		String assayRunDateEndStr = (String) theForm.get("assayRunDateEnd");
		
		Date assayRunDateBegin = assayRunDateBeginStr.length() == 0 ? null
				: StringUtils.convertToDate(assayRunDateBeginStr, CaNanoLabConstants.ACCEPT_DATE_FORMAT);
		Date assayRunDateEnd = assayRunDateEndStr.length() == 0 ? null
				: StringUtils.convertToDate(assayRunDateEndStr, CaNanoLabConstants.ACCEPT_DATE_FORMAT);

		String aliquotName = (String) theForm.get("aliquotName");
		boolean excludeMaskedAliquots = ((String) theForm
				.get("excludeMaskedAliquots")).equals("on") ? true : false;
		String fileName = (String) theForm.get("fileName");
		boolean isFileInput = ((String) theForm.get("isFileIn")).equals("on") ? true
				: false;
		boolean isFileOutput = ((String) theForm.get("isFileOut")).equals("on") ? true
				: false;

		String fileSubmissionDateBeginStr = (String) theForm
				.get("fileSubmissionDateBegin");
		String fileSubmissionDateEndStr = (String) theForm
				.get("fileSubmissionDateEnd");
		
		Date fileSubmissionDateBegin = fileSubmissionDateBeginStr.length() == 0 ? null
				: StringUtils.convertToDate(fileSubmissionDateBeginStr,
						CaNanoLabConstants.ACCEPT_DATE_FORMAT);
		Date fileSubmissionDateEnd = fileSubmissionDateEndStr.length() == 0 ? null
				: StringUtils.convertToDate(fileSubmissionDateEndStr,
						CaNanoLabConstants.ACCEPT_DATE_FORMAT);
		
		// Add one day to the fileSubmissionDateEnd to include all the files
		// files during the day
		if (fileSubmissionDateEnd != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fileSubmissionDateEnd);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			fileSubmissionDateEnd = calendar.getTime();	
		}
		

		String fileSubmitter = (String) theForm.get("fileSubmitter");
		boolean excludeMaskedFiles = ((String) theForm
				.get("excludeMaskedFiles")).equals("on") ? true : false;
		String criteriaJoin = (String) theForm.get("criteriaJoin");

		// pass the parameters to the searchWorkflowService
		SearchWorkflowService searchWorkflowService = new SearchWorkflowService();
		List<WorkflowResultBean> workflows = searchWorkflowService
				.searchWorkflows(assayName, assayType, assayRunDateBegin,
						assayRunDateEnd, aliquotName, excludeMaskedAliquots,
						fileName, isFileInput, isFileOutput,
						fileSubmissionDateBegin, fileSubmissionDateEnd,
						fileSubmitter, excludeMaskedFiles, criteriaJoin);
		if (workflows == null || workflows.isEmpty()) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchWorkflow.noResult");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		} else {
			request.setAttribute("workflows", workflows);
			forward = mapping.findForward("success");
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session=request.getSession();		
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);
		
		InitSessionSetup.getInstance().setAllAssayTypeAssays(session);
		InitSessionSetup.getInstance().setAllUsers(session);
		return mapping.getInputForward();
	}
	
	public boolean loginRequired() {
		return true;
	}
}
