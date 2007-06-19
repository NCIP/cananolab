package gov.nih.nci.calab.ui.submit;

/**
 * This class associates a assay result file with a characterization.  
 *  
 * @author pansu
 */

/* CVS $Id: LoadDerivedBioAssayDataAction.java,v 1.14 2007-06-19 20:15:51 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
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

public class LoadDerivedBioAssayDataAction extends AbstractDispatchAction {
	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String fileSource = (String) theForm.get("fileSource");
		DerivedBioAssayDataBean fileBean = (DerivedBioAssayDataBean) theForm
				.get("file");
		String fileNumber = (String) theForm.get("fileNumber");
		String characterizationName = (String) theForm
				.get("characterizationName");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		fileBean.setCharacterizationName(characterizationName);
		fileBean.setParticleName(particleName);

		DerivedBioAssayDataBean savedFileBean = null;
		if (fileSource.equals("new")) {
			FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
			fileBean.setUploadedFile(uploadedFile);
			savedFileBean = service.saveCharacterizationFile(fileBean);
		} else {
			// updating existingFileBean with form data
			if (fileBean.getId() != null) {
				DerivedBioAssayDataBean existingFileBean = (DerivedBioAssayDataBean) service
						.getFile(fileBean.getId());

				existingFileBean.setTitle(fileBean.getTitle());
				existingFileBean.setDescription(fileBean.getDescription());
				existingFileBean.setVisibilityGroups(fileBean
						.getVisibilityGroups());

				existingFileBean.setKeywords(fileBean.getKeywords());
				savedFileBean = service
						.saveCharacterizationFile(existingFileBean);
			} else {
				throw new Exception(
						"Please upload a new file if existing file drop-down list is empty or select a file from the drop-down list.");
			}
		}
		request.getSession().setAttribute("characterizationFile" + fileNumber,
				savedFileBean);

		String forwardPage = (String) theForm.get("forwardPage");
		forward = mapping.findForward(forwardPage);

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);
		String particleName = request.getParameter("particleName");
		InitSessionSetup.getInstance().setAllRunFiles(session, particleName);
		String fileNumber = request.getParameter("fileNumber");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("particleName", particleName);
		theForm.set("fileNumber", fileNumber);
		theForm.set("forwardPage", (String) request
				.getAttribute("loadFileForward"));
		theForm.set("characterizationName", (String) request
				.getAttribute("characterization"));
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);
		String fileId = request.getParameter("fileId");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		DerivedBioAssayDataBean fileBean = service
				.getDerivedBioAssayData(fileId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("file", fileBean);
		String actionName = request.getParameter("actionName");
		String formName = request.getParameter("formName");
		request.setAttribute("actionName", actionName);
		request.setAttribute("formName", formName);
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
		DerivedBioAssayDataBean fileBean = (DerivedBioAssayDataBean) theForm
				.get("file");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.updateDerivedBioAssayDataMetaData(fileBean);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.updateDerivedBioAssayData", fileBean.getPath());

		msgs.add("message", msg);
		saveMessages(request, msgs);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
