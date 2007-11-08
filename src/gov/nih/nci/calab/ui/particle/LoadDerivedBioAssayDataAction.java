package gov.nih.nci.calab.ui.particle;

/**
 * This class associates a assay result file with a characterization.  
 *  
 * @author pansu
 */

/* CVS $Id: LoadDerivedBioAssayDataAction.java,v 1.2 2007-11-08 20:41:34 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		DerivedBioAssayDataBean fileBean = (DerivedBioAssayDataBean) theForm
				.get("file");
		FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
		if (uploadedFile.getFileName().length() > 0) {
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

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		DerivedBioAssayDataBean file = (DerivedBioAssayDataBean) request
				.getAttribute("file");
		theForm.set("file", file);
		theForm.set("forwardPage", request
				.getAttribute("loadFileForward"));
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}
