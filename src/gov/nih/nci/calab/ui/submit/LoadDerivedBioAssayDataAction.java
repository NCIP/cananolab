package gov.nih.nci.calab.ui.submit;

/**
 * This class associates a assay result file with a characterization.  
 *  
 * @author pansu
 */

/* CVS $Id: LoadDerivedBioAssayDataAction.java,v 1.26 2007-08-10 15:05:06 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class LoadDerivedBioAssayDataAction extends AbstractDispatchAction {
	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileSource = (String) theForm.get("fileSource");
		DerivedBioAssayDataBean fileBean = (DerivedBioAssayDataBean) theForm
				.get("file");
		if (fileSource.equals("new")) {
			FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
			if (uploadedFile.getFileName().length() > 0) {
				fileBean.setFileContent(uploadedFile.getFileData());
				fileBean.setName(uploadedFile.getFileName());
				fileBean
						.setTimeStampedName(FileService
								.prefixFileNameWithTimeStamp(uploadedFile
										.getFileName()));
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
			}
		} else if (fileSource.equals("chooseExisting")) {
			String runFileId = theForm.getString("runFileId");
			if (runFileId != null && runFileId.length() > 0) {
				SubmitNanoparticleService service = new SubmitNanoparticleService();
				LabFileBean existingFileBean = (LabFileBean) service
						.getFile(runFileId);
				fileBean.setName(existingFileBean.getName());
				fileBean.setCreatedBy(existingFileBean.getCreatedBy());
				fileBean.setCreatedDate(existingFileBean.getCreatedDate());
				fileBean.setUri(existingFileBean.getUri());			
				FileService fileService = new FileService();
				byte[] content = fileService
						.getFileContent(new Long(runFileId));
				fileBean.setFileContent(content);

			}
		}
		String forwardPage = (String) theForm.get("forwardPage");
		DynaValidatorForm charForm = (DynaValidatorForm) request.getSession()
				.getAttribute("nanoparticleCharacterizationForm");
		CharacterizationBean achar = (CharacterizationBean) charForm
				.get("achar");
		int fileNum = (Integer) theForm.get("fileNumber");
		DerivedBioAssayDataBean derivedBioAssayDataBean = achar
				.getDerivedBioAssayDataList().get(fileNum);
		derivedBioAssayDataBean.setName(fileBean.getName());
		derivedBioAssayDataBean.setTitle(fileBean.getTitle());
		derivedBioAssayDataBean.setFileContent(fileBean.getFileContent());
		derivedBioAssayDataBean.setUri(fileBean.getUri());
		derivedBioAssayDataBean.setVisibilityGroups(fileBean
				.getVisibilityGroups());
		derivedBioAssayDataBean.setKeywords(fileBean.getKeywords());
		ActionForward forward = new ActionForward();
		forward.setPath(forwardPage);
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		DerivedBioAssayDataBean file = (DerivedBioAssayDataBean) request
				.getAttribute("file");
		// set fileSource radio button
		if (file.getUri() != null
				&& file.getUri().length() > 0
				&& file.getUri().contains(
						CaNanoLabConstants.FOLDER_WORKFLOW_DATA)) {
			theForm.set("fileSource", "chooseExisting");
		}
		InitSessionSetup.getInstance().setAllRunFiles(session,
				file.getParticleName());
		theForm.set("file", file);
		theForm.set("forwardPage", (String) request
				.getAttribute("loadFileForward"));
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
