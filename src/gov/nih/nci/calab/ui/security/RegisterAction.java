package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.login.PasswordService;
import gov.nih.nci.calab.service.login.RegisterService;
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
 * @author doswellj
 */

public class RegisterAction extends AbstractBaseAction {

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;

		PasswordService pService = PasswordService.getInstance();
		
		RegisterService registerService = new RegisterService();
		
		UserBean userBean = new UserBean(theForm.getString("title"), theForm.getString("firstName"),
										 theForm.getString("middleName"), theForm.getString("lastName"),
										 theForm.getString("email"), theForm.getString("phoneNumber"),
										 theForm.getString("organization"), theForm.getString("department"),
										 theForm.getString("loginId"), pService.encrypt(theForm.getString("password")));
		
		ActionMessages msgs = new ActionMessages();
		
		registerService.register(userBean);
		
		ActionMessage msg = new ActionMessage("message.register", theForm.getString("loginId"));
		msgs.add("message", msg);
		saveMessages(request,msgs);
		forward = mapping.findForward("success");
		session.setAttribute("newUserCreated", "true");
		
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}

}
