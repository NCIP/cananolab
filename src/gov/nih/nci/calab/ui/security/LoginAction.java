package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.login.LoginService;
import gov.nih.nci.calab.service.login.PasswordService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * The LoginAction authenticates a user into the caLAB system.
 * 
 * @author doswellj
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
		String strEncryptedPass = PasswordService.getInstance().encrypt(
				strPassword);

		// Call CSM to authenticate the user.
		LoginService loginservice = new LoginService("calab");
		Boolean blnAuthenticated = loginservice.login(strLoginId,
				strEncryptedPass);
		if (blnAuthenticated == true) {
			// Invalidate the current session and create a new one.
			session = request.getSession(false);
			session.invalidate();
			session = request.getSession(true);

			// Save authenticated user information into User DTO.
			UserBean userBean = new UserBean();
			userBean.setLoginId(strLoginId);

			session.setAttribute("user", userBean);
			forward = mapping.findForward("success");
		}
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}

}
