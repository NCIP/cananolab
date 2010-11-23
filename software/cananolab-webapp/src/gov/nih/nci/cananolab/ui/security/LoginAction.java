package gov.nih.nci.cananolab.ui.security;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * The LoginAction authenticates a user into the system.
 *
 * @author pansu
 */

public class LoginAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Invalidate the current session and create a new one
		HttpSession session = request.getSession(false);
		// if comes in from back and refresh
		if (!session.isNew() && !isTokenValid(request)) {
			throw new InvalidSessionException(
					"Session doesn't exist.  Please start again");
		}
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String loginName = (String) theForm.get("loginId");
		String password = (String) theForm.get("password");
		// check if the password is the initial password
		// redirect to change password page
		if (loginName.equals(password)) {
			ActionMessage msg = new ActionMessage(
					"message.login.changepassword");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.findForward("changePassword");
		}
		if (session != null) {
			session.invalidate();
		}
		UserBean user = new UserBean(loginName, password);
		SecurityService service = new SecurityService(AccessibilityBean.CSM_APP_NAME,
				user);
		request.getSession().setAttribute("securityService", service);
		request.getSession().setAttribute("user", service.getUserBean());
		forward = mapping.findForward("success");
		resetToken(request);
		response.setHeader("Cache-Control","no-cache");
		return forward;
	}
}
