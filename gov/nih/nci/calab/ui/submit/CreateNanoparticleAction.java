package gov.nih.nci.calab.ui.submit;

/**
 * This class creates nanoparticle general information and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: CreateNanoparticleAction.java,v 1.4 2006-08-10 16:33:18 pansu Exp $ */

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
import org.apache.struts.validator.DynaValidatorForm;

public class CreateNanoparticleAction extends AbstractDispatchAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;		
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		HttpSession session = request.getSession();
		session.setAttribute("particleName", particleName);
		session.setAttribute("particleType", particleType);
		
		String keywords = (String) theForm.get("keywords");
		String[] visibilities = (String[])theForm.get("visibilities");
		String[] keywordList=keywords.split("\r\n");
		
		UserService userService=new UserService(CalabConstants.CSM_APP_NAME);
		http://www.aahealth.org/news.asp?id=145
		for (String visibility: visibilities) {
			//by default, always set visibility to NCL_PI and NCL_Researcher to be true
			userService.secureObject(particleName, "NCL_PI", "R");
			userService.secureObject(particleName, "NCL_Researcher", "R");
			userService.secureObject(particleName, visibility, "R");
		}
		
		ActionMessages msgs=new ActionMessages();
		ActionMessage msg=new ActionMessage("message.createNanoparticle.secure", StringUtils.join(visibilities, ", "));
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
	
		InitSessionSetup.getInstance().setAllParticleTypeParticles(session);	
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);	
		InitSessionSetup.getInstance().setParticleMenu(session);
		
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
