package gov.nih.nci.calab.ui.submit;

/**
 * This class associates a assay result file with a characterization.  
 *  
 * @author pansu
 */

/* CVS $Id: LoadCharacterizationTableAction.java,v 1.2 2006-09-15 21:06:04 pansu Exp $ */

import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class LoadCharacterizationTableAction extends AbstractDispatchAction {
	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileSource = (String) theForm.get("fileSource");
		if (fileSource.equals("new")) {
			FormFile file = (FormFile) theForm.get("file");
			//TODO write file to filesystem and database
		} else {
			String fileId = (String) theForm.get("fileId");
			//TODO load file from database
		}

		//TODO get the fileName
		String fileName="this is a test";
		request.setAttribute("characterizationFile", fileName);
		String title = (String) theForm.get("title");
		String description = (String) theForm.get("description");
		String comments = (String) theForm.get("comments");
		String keywords = (String) theForm.get("keywords");
		String visibilities = (String) theForm.get("visibilities");
		String[] keywordList = keywords.split("\r\n");
		
		String forwardPage = (String) theForm.get("forwardPage");
		// TODO add service codes
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
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
