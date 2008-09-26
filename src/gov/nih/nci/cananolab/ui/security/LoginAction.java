package gov.nih.nci.cananolab.ui.security;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.service.security.LoginService;
import gov.nih.nci.cananolab.ui.core.AbstractBaseAction;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

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
		//if comes in from back and refresh			
		if (!isTokenValid(request)) {			
			throw new InvalidSessionException(
			"Session doesn't exist.  Please start again");
		}
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();		
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
				ActionMessage msg = new ActionMessage(
						"message.login.changepassword");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.findForward("changePassword");
			}			
			// Invalide the current session and create a new one
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			session = request.getSession(true);

			setUserSessionInfo(session, strLoginId);

			forward = mapping.findForward("success");
			resetToken(request);
		}
		return forward;
	}

	private void setUserSessionInfo(HttpSession session, String loginName)
			throws Exception {
		AuthorizationService authorizationService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		UserBean user = authorizationService.getUserBean(loginName);
		session.setAttribute("user", user);
		session.setAttribute("userService", authorizationService);

		Boolean createProtocol = authorizationService.checkCreatePermission(user,
				CaNanoLabConstants.CSM_PG_PROTOCOL);
		session.setAttribute("canCreateProtocol", createProtocol);
		Boolean createPublication = authorizationService.checkCreatePermission(user,
				CaNanoLabConstants.CSM_PG_PUBLICATION);
		session.setAttribute("canCreatePublication", createPublication);
		Boolean createParticle = authorizationService.checkCreatePermission(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
		session.setAttribute("canCreateNanoparticle", createParticle);

		boolean isAdmin = authorizationService.isAdmin(user.getLoginName());
		session.setAttribute("isAdmin", isAdmin);

		boolean canDelete = authorizationService.checkDeletePermission(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
		if (canDelete && isAdmin) {
			session.setAttribute("canUserDelete", "true");
		} else {
			session.setAttribute("canUserDelete", "false");
		}
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) {
		return true;
	}

}
