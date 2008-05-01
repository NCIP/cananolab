package gov.nih.nci.cananolab.ui.report;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */
/* CVS $Id: SubmitReportAction.java,v 1.4 2008-05-01 19:32:21 pansu Exp $ */

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitReportAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ReportBean reportBean = (ReportBean) theForm.get("file");

		String fileName = null;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (reportBean.getDomainReport().getId() == null) {
			reportBean.getDomainReport().setCreatedBy(user.getLoginName());
			reportBean.getDomainReport().setCreatedDate(new Date());
		}
		byte[] fileData = null;
		// if entered external url
		if (reportBean.getDomainReport().getUriExternal()
				&& reportBean.getExternalUrl().length() > 0) {
			reportBean.getDomainReport().setUri(reportBean.getExternalUrl());
			reportBean.getDomainReport().setName(reportBean.getExternalUrl());
		} else {
			FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
			fileName = uploadedFile.getFileName();
			// if new report has been uploaded
			if (fileName.length() > 0) {
				fileData = uploadedFile.getFileData();
				String fileUri = InitSetup.getInstance()
						.getFileUriFromFormFile(uploadedFile,
								CaNanoLabConstants.FOLDER_REPORT, null, null);
				reportBean.getDomainReport().setName(fileName);
				reportBean.getDomainReport().setUri(fileUri);
			}
		}
		ReportService service = new ReportService();
		service.saveReport((Report) reportBean.getDomainReport(), reportBean
				.getParticleNames(), fileData);
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(reportBean.getDomainReport().getId()
				.toString(), reportBean.getVisibilityGroups());

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitReport.file",
				reportBean.getDomainReport().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitReportSetup.getInstance().getReportCategories(request);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitNanoparticleSetup.getInstance().getAllNanoparticleSampleNames(
				request, user);
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setLookups(request);
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
		fileService.retrieveVisiblity(reportBean, user);
		theForm.set("file", reportBean);
		setLookups(request);
		return mapping.getInputForward();
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
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
