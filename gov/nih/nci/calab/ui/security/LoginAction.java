package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.security.LoginService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * The LoginAction authenticates a user into the caLAB system.
 * 
 * @author doswellj, pansu
 */

public class LoginAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String strLoginId = (String) theForm.get("loginId");
		String strPassword = (String) theForm.get("password");

		// Encrypt the password.
		// String strEncryptedPass = PasswordService.getInstance().encrypt(
		// strPassword);

		// Call CSM to authenticate the user.
		LoginService loginservice = new LoginService(
				CaNanoLabConstants.CSM_APP_NAME);
		Boolean blnAuthenticated = loginservice.login(strLoginId, strPassword);
		// strEncryptedPass);
		if (blnAuthenticated == true) {
			// check if the password is the initial password
			// redirect to change password page
			if (strLoginId.equals(strPassword)) {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.login.changepassword");
				msgs.add("message", msg);
				saveMessages(request, msgs);
				return mapping.findForward("changePassword");
			}
			// Invalidate the current session and create a new one.
			session = request.getSession(false);
			session.invalidate();
			session = request.getSession(true);
			setUserSessionInfo(session, strLoginId);

			forward = mapping.findForward("success");
		}
		return forward;
	}

	private void setUserSessionInfo(HttpSession session, String loginName)
			throws Exception {
		UserService userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
		UserBean user = userService.getUserBean(loginName);
		session.setAttribute("user", user);
		session.setAttribute("userService", userService);
		
		boolean canSubmit = userService.checkExecutePermission(user, "submit");
		if (canSubmit) {
			session.setAttribute("canUserSubmit", "true");
		} else {
			session.setAttribute("canUserSubmit", "false");
		}
	}

	public boolean loginRequired() {
		return false;
	}

	/* overwrite parent */
	public boolean canUserExecute() {
		return true;
	}

}
