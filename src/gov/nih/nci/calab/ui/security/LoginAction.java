package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
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
		// logout first
		HttpSession session = request.getSession();
		if (!session.isNew()) {
			// invalidate the old one
			session.invalidate();
		}

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
			session = request.getSession();
			setUserSessionInfo(session, strLoginId);

			forward = mapping.findForward("success");
		}
		return forward;
	}

	private void setUserSessionInfo(HttpSession session, String loginName)
			throws Exception {
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		UserBean user = userService.getUserBean(loginName);
		session.setAttribute("user", user);
		session.setAttribute("userService", userService);

		Boolean createSample = userService.checkCreatePermission(user,
				CaNanoLabConstants.CSM_PG_SAMPLE);
		session.setAttribute("canCreateSample", createSample);
		Boolean createProtocol = userService.checkCreatePermission(user,
				CaNanoLabConstants.CSM_PG_PROTOCOL);
		session.setAttribute("canCreateProtocol", createProtocol);
		Boolean createReport = userService.checkCreatePermission(user,
				CaNanoLabConstants.CSM_PG_REPORT);
		session.setAttribute("canCreateReport", createReport);
		Boolean createParticle = userService.checkCreatePermission(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
		session.setAttribute("canCreateNanoparticle", createParticle);

		boolean isAdmin = userService.isAdmin(user.getLoginName());
		session.setAttribute("isAdmin", isAdmin);

		boolean canDelete = userService.checkDeletePermission(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
		if (canDelete && isAdmin) {
			session.setAttribute("canUserDeleteChars", "true");
		} else {
			session.setAttribute("canUserDeleteChars", "false");
		}
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) {
		return true;
	}

}
