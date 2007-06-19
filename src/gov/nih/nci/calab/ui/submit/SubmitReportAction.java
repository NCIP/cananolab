package gov.nih.nci.calab.ui.submit;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: SubmitReportAction.java,v 1.12 2007-06-19 20:15:51 pansu Exp $ */

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitReportService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitReportAction extends AbstractDispatchAction {

	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String[] particleNames = (String[]) theForm.get("particleNames");
		LabFileBean fileBean = (LabFileBean) theForm.get("file");
		FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
		SubmitReportService service = new SubmitReportService();

		service.createReport(particleNames, uploadedFile, fileBean);

		// display default visible groups
		if (fileBean.getVisibilityGroups().length == 0) {
			fileBean
					.setVisibilityGroups(CaNanoLabConstants.VISIBLE_GROUPS);
		}

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("message.submitReport.secure",
				StringUtils.join(fileBean.getVisibilityGroups(), ", "));
		ActionMessage msg2 = new ActionMessage("message.submitReport.file",
				uploadedFile.getFileName());
		msgs.add("message", msg1);
		msgs.add("message", msg2);
		saveMessages(request, msgs);

		request.getSession().setAttribute("newReportCreated", "true");
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitSessionSetup.getInstance().setAllSampleContainers(session);
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().setAllSampleContainers(session);
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId = request.getParameter("fileId");
		String fileType = request.getParameter("fileType");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		LabFileBean fileBean=service.getFile(fileId);
		fileBean.setInstanceType(fileType);
		theForm.set("file", fileBean);
		return mapping.getInputForward();
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		LabFileBean fileBean = (LabFileBean) theForm.get("file");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.updateFileMetaData(fileBean);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.updateReport", fileBean
				.getPath());

		msgs.add("message", msg);
		saveMessages(request, msgs);

		request.getSession().setAttribute("newReportCreated", "true");

		return mapping.findForward("success");
	}

	public boolean loginRequired() {
		return true;
	}
}
