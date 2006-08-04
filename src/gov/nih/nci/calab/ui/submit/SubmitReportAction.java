package gov.nih.nci.calab.ui.submit;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: SubmitReportAction.java,v 1.1 2006-08-04 20:21:47 pansu Exp $ */

import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;
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
		String[] visibilities = (String[]) theForm.get("visibilities");
		FormFile reportFile = (FormFile) theForm.get("reportFile");
		String title=(String)theForm.get("title");
		String description=(String)theForm.get("description");
		
		//TODO saves reportFile to the file system
		//TODO daves reportFile path to the database
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);

		String fileName=reportFile.getFileName();
		for (String visibility : visibilities) {
			// by default, always set visibility to NCL_PI and NCL_Researcher to
			// be true
			//TODO once the files is successfully saved, use fileId instead of fileName
			userService.secureObject(fileName, "NCL_PI", "R");
			userService.secureObject(fileName, "NCL_Researcher", "R");
			userService.secureObject(fileName, visibility, "R");
		}

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("message.submitReport.secure",
				StringUtils.join(visibilities, ", "));
		ActionMessage msg2 = new ActionMessage("message.submitReport.file",
				fileName);
		msgs.add("message", msg1);
		msgs.add("message", msg2);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setAllSampleContainers(session);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
