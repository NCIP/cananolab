package gov.nih.nci.cananolab.ui.report;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */
/* CVS $Id: SubmitReportAction.java,v 1.1 2008-04-30 22:11:37 pansu Exp $ */

import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.report.ReportService;
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
		} else {
			fileName = reportBean.getDomainReport().getName();
		}
		ReportService service = new ReportService();

		FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
		byte[] fileData = null;
		if (uploadedFile != null) {
			fileData = uploadedFile.getFileData();
			fileName = uploadedFile.getFileName();
			String fileUri = InitSetup.getInstance().getFileUriFromFormFile(
					uploadedFile, CaNanoLabConstants.FOLDER_REPORT, null, null);
			reportBean.getDomainReport().setName(fileName);
			reportBean.getDomainReport().setUri(fileUri);
		}
		service.saveReport((Report) reportBean.getDomainReport(), reportBean
				.getParticleNames(), fileData);
		// display default visible groups
		if (reportBean.getVisibilityGroups().length == 0) {
			reportBean.setVisibilityGroups(CaNanoLabConstants.VISIBLE_GROUPS);
		}

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("message.submitReport.secure",
				StringUtils.join(reportBean.getVisibilityGroups(), ", "));
		ActionMessage msg2 = new ActionMessage("message.submitReport.file",
				uploadedFile.getFileName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg1);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg2);
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
		fileService.setVisiblity(reportBean, user);
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
