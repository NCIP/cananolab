package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.service.security.LoginService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This class allow users to update their passwords.
 * 
 * @author pansu
 */

public class UpdatePasswordAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String loginId = (String) theForm.get("loginId");
		String password = (String) theForm.get("password");
		String newPassword = (String) theForm.get("newPassword");

		LoginService loginservice = new LoginService(
				CaNanoLabConstants.CSM_APP_NAME);
		Boolean isAuthenticated = loginservice.login(loginId, password);

		if (isAuthenticated) {
			UserService userService = new UserService(
					CaNanoLabConstants.CSM_APP_NAME);
			userService.updatePassword(loginId, newPassword);	
			ActionMessages messages=new ActionMessages();
			ActionMessage message=new ActionMessage("message.password");
			messages.add("message", message);
			saveMessages(request, messages);
		}
		return mapping.findForward("success");
	}

	public boolean loginRequired() {
		return false;
	}

	/* overwrite parent */
	public boolean canUserExecute() {
		return true;
	}
}
