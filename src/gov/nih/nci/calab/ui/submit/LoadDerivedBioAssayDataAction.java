package gov.nih.nci.calab.ui.submit;

/**
 * This class associates a assay result file with a characterization.  
 *  
 * @author pansu
 */

/* CVS $Id: LoadDerivedBioAssayDataAction.java,v 1.20 2007-07-18 20:59:20 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.io.File;

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

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String fileSource = (String) theForm.get("fileSource");
		DerivedBioAssayDataBean fileBean = (DerivedBioAssayDataBean) theForm
				.get("file");
		String characterizationName = (String) theForm
				.get("characterizationName");
		fileBean.setCharacterizationName(characterizationName);
		fileBean.setParticleName(particleName);
		if (fileSource.equals("new")) {
			FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
			fileBean.setFileContent(uploadedFile.getFileData());
			fileBean.setName(uploadedFile.getFileName());
			fileBean.setTimeStampedName(FileService
					.prefixFileNameWithTimeStamp(uploadedFile.getFileName()));
						// add charaterizationName to the path
			String filePath = File.separator
					+ CaNanoLabConstants.FOLDER_PARTICLE
					+ File.separator
					+ fileBean.getParticleName()
					+ File.separator
					+ StringUtils.getOneWordLowerCaseFirstLetter(fileBean
							.getCharacterizationName());
			
			fileBean.setUri(filePath + File.separator
					+ fileBean.getTimeStampedName());
		} else {
			if (fileBean.getId() != null) {
				SubmitNanoparticleService service = new SubmitNanoparticleService();
				DerivedBioAssayDataBean existingFileBean = (DerivedBioAssayDataBean) service
						.getFile(fileBean.getId());
				fileBean.setParticleName(existingFileBean.getUri());
			}
		}
		String forwardPage = (String) theForm.get("forwardPage");
		ActionForward forward = new ActionForward();
		forward.setPath(forwardPage);
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);
		String particleName = (String) request.getAttribute("particleName");
		InitSessionSetup.getInstance().setAllRunFiles(session, particleName);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		DerivedBioAssayDataBean file = (DerivedBioAssayDataBean) request
				.getAttribute("file");
		theForm.set("file", file);
		theForm.set("particleName", particleName);
		theForm.set("forwardPage", (String) request
				.getAttribute("loadFileForward"));
		theForm.set("characterizationName", (String) request
				.getAttribute("characterizationName"));
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
				"message.updateDerivedBioAssayData", fileBean.getUri());

		msgs.add("message", msg);
		saveMessages(request, msgs);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
