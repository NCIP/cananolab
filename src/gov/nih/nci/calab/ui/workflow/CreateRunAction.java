package gov.nih.nci.calab.ui.workflow;

/**
 * This Action class saves user entered new Run information and the assigned aliquots
 * into the database.
 * 
 * @author caLAB Team
 */

import java.util.Date;

import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class CreateRunAction extends AbstractBaseAction {

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		// TODO fill in details for aliquot information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		// Get Prameters from form elements
		// Run info

		String assayId = (String) theForm.get("assayId");
		String runBy = (String) theForm.get("runBy");
		String runDateStr = (String) theForm.get("runDate");
		Date runDate=StringUtils.convertToDate(runDateStr, CalabConstants.ACCEPT_DATE_FORMAT);
		// get user and date information from session
		String creator = (String) session.getAttribute("creator");
		String creationDate = (String) session.getAttribute("creationDate");
		ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
		RunBean runBean = workflowService.saveRun(assayId, runBy, runDate,
				creator, creationDate);

		// Save Aliquots assigned
		String[] aliquotIds = (String[]) theForm.get("assignedAliquot");
		String comments = (String) theForm.get("aliquotComment");

		workflowService.saveRunAliquots(runBean.getId(), aliquotIds, comments,
				creator, creationDate);

		session.setAttribute("newWorkflowCreated", "true");

		// add parameters to forward
		String extra = constructGetParams(runBean);
		String newPath = mapping.findForward("success").getPath() + extra;
		ActionForward forward = new ActionForward();
		forward.setPath(newPath);
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	private String constructGetParams(RunBean runBean) {
		String menuType = "&menuType=upload";

		String runId = "&runId=" + runBean.getId();

		String runName = "&runName=" + runBean.getName();

		String assayName = "&assayName="
				+ runBean.getAssayBean().getAssayName();

		String assayType = "&assayType="
				+ runBean.getAssayBean().getAssayType();

		String extra = menuType + runId + runName + assayName + assayType;
		return extra;
	}
}
