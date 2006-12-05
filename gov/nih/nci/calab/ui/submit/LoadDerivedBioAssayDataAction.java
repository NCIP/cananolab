package gov.nih.nci.calab.ui.submit;

/**
 * This class associates a assay result file with a characterization.  
 *  
 * @author pansu
 */

/* CVS $Id: LoadDerivedBioAssayDataAction.java,v 1.6 2006-12-05 23:42:09 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
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
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String title = (String) theForm.get("title");
		String description = (String) theForm.get("description");
		String comments = (String) theForm.get("comments");
		String keywords = (String) theForm.get("keywords");
		String[] visibilities = (String[]) theForm.get("visibilities");
		String[] keywordList = keywords.split("\r\n");

		String fileSource = (String) theForm.get("fileSource");
		CharacterizationFileBean fileBean = null;
		SubmitNanoparticleService service = new SubmitNanoparticleService();

		String fileNumber = (String) theForm.get("fileNumber");
		String characterizationName = (String) theForm.get("characterization");

		if (fileSource.equals("new")) {
			FormFile file = (FormFile) theForm.get("file");
			String rootPath = PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
			if (rootPath.charAt(rootPath.length()-1) == File.separatorChar)
				rootPath = rootPath.substring(0, rootPath.length()-1);

			String path = File.separator + "particles" + File.separator + particleName + File.separator + characterizationName + File.separator;
        	
			File pathDir = new File (rootPath + path);
            if ( !pathDir.exists() ) pathDir.mkdirs();

			fileBean = service.saveCharacterizationFile(particleName, file,
					title, description, comments, keywordList, visibilities, path, fileNumber, rootPath);
		} else {
			String fileId = (String) theForm.get("fileId");
			fileBean = service.saveCharacterizationFile(fileId, title, description, keywordList, visibilities);		
		}

		request.getSession().setAttribute("characterizationFile" + fileNumber,
				fileBean);
		
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
		InitSessionSetup.getInstance().setAllAssayTypeAssays(session);
		String particleName = (String) request.getAttribute("particleName");
		InitSessionSetup.getInstance().setAllRunFiles(session, particleName);		
		String fileNumber = (String) request.getAttribute("fileNumber");
		String loadFileForward = (String) request
				.getAttribute("loadFileForward");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("particleName", particleName);
		theForm.set("fileNumber", fileNumber);
		theForm.set("forwardPage", loadFileForward);
		theForm.set("characterization", (String) request.getAttribute("characterization"));
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
