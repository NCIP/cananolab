package gov.nih.nci.calab.ui.submit;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: SubmitReportAction.java,v 1.6 2006-12-07 17:48:00 pansu Exp $ */

import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CananoConstants;
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
		String reportType = (String) theForm.get("reportType");
		String comment = (String) theForm.get("comment");
		
		SubmitNanoparticleService service=new SubmitNanoparticleService();
		
		service.createReport(particleNames, reportType, reportFile, title, description, comment, visibilities);
		
		//display default visible groups
		if (visibilities.length==0) {
			visibilities=CananoConstants.DEFAULT_VISIBLE_GROUPS;
		}
		
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("message.submitReport.secure",
				StringUtils.join(visibilities, ", "));
		ActionMessage msg2 = new ActionMessage("message.submitReport.file",
				reportFile.getFileName());
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
		InitSessionSetup.getInstance().setStaticDropdowns(session);		
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);
		// clear session data from the input forms
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.getMap().clear();


		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
