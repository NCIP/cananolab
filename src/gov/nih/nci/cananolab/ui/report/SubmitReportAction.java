package gov.nih.nci.cananolab.ui.report;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */
/* CVS $Id: SubmitReportAction.java,v 1.11 2008-05-11 21:16:20 pansu Exp $ */

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitReportAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ReportBean reportBean = (ReportBean) theForm.get("file");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		reportBean.setupDomainFile(CaNanoLabConstants.FOLDER_REPORT, user
				.getLoginName());
		ReportService service = new ReportService();
		service.saveReport((Report) reportBean.getDomainFile(), reportBean
				.getParticleNames(), reportBean.getNewFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(reportBean.getDomainFile().getId()
				.toString(), reportBean.getVisibilityGroups());

		InitReportSetup.getInstance().persistReportDropdowns(request,
				reportBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitReport.file",
				reportBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		if (request.getParameter("particleId") != null
				&& request.getParameter("particleId").length() > 0) {
			setupDataTree(theForm, request);
			forward = mapping.findForward("particleSuccess");
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitReportSetup.getInstance().setReportDropdowns(request);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String reportId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ReportService reportService = new ReportService();
		ReportBean reportBean = reportService.findReportById(reportId);
		FileService fileService = new FileService();
		fileService.retrieveVisibility(reportBean, user);
		theForm.set("file", reportBean);
		InitReportSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.getInputForward();
		if (particleId != null) {
			forward = mapping.findForward("particleSubmitReport");
			request.setAttribute("particleId", particleId);
		}
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String reportId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ReportService reportService = new ReportService();
		ReportBean reportBean = reportService.findReportById(reportId);
		FileService fileService = new FileService();
		fileService.retrieveVisibility(reportBean, user);
		theForm.set("file", reportBean);
		InitReportSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.findForward("view");
		if (particleId != null) {
			forward = mapping.findForward("particleViewReport");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_REPORT);
	}
}
