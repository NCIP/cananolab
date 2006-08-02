package gov.nih.nci.calab.ui.submit;

/**
 * This class creates nanoparticle metadata and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: CreateNanoparticleAction.java,v 1.1 2006-08-02 21:28:10 pansu Exp $ */

import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;
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
import org.apache.struts.validator.DynaValidatorForm;

public class CreateNanoparticleAction extends AbstractDispatchAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;		
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String[] particleNames = (String[]) theForm.get("particleNames");
		String visibility = (String)theForm.get("visibility");

		UserService userService=new UserService(CalabConstants.CSM_APP_NAME);
		for (String particleName: particleNames) {
			userService.secureObject(particleName, visibility, "R");
		}
		
		ActionMessages msgs=new ActionMessages();
		ActionMessage msg=new ActionMessage("message.secure", visibility);
		msgs.add("message", msg);
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
