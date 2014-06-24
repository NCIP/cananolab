package gov.nih.nci.cananolab.restful.helper;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.restful.RestfulConstants;
import gov.nih.nci.cananolab.restful.SecurityServices;
import gov.nih.nci.cananolab.service.security.MailService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;

public class SecurityHelper {
	private Logger logger = Logger.getLogger(SecurityHelper.class);
	
	public String checkLogin(String username, String password, HttpServletRequest httpRequest) {
		
		Boolean useLDAP = new Boolean(PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "useLDAP"));
		if (!useLDAP && username.equals(password)) {
			
			//TODO: seems username==password is a way to tell "changePassword" is the 
			//actuall request. Invest. to see if we need to keep this.
			
			
//			ActionMessage msg = new ActionMessage(
//					"message.login.changepassword");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
//			return mapping.findForward("changePassword");
		}
		
		UserBean user = new UserBean(username, password);
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME, user);
			
			// Invalidate the current session and create a new one
			HttpSession session = httpRequest.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			session = httpRequest.getSession(true);
			session.setAttribute("securityService", service);
			session.setAttribute("user", service.getUserBean());
		} catch (Exception e) {
			logger.error("Erro while logging in user: " + username + "|" + password + ". " + e.getMessage());
			return e.getMessage();
		}
		
		return RestfulConstants.SUCCESS;
	}
	
	
	public String register(String title, String firstName, String lastName, String email, 
			String phone, String organization, String fax, String comment, String registerToUserList) {

		logger.info("........ RegisterUserAction..............");
		//logger.info("register to user list: " + registerToUserList);


		MailService mailService = new MailService();
		mailService.sendRegistrationEmail(firstName, lastName, email, phone, organization, title, fax, comment);
		if("checked".equalsIgnoreCase(registerToUserList))
			mailService.sendUsersListRegistration( email, firstName + " " + lastName);		

		//		ActionMessages messages = new ActionMessages();
		//		ActionMessage message = new ActionMessage("message.register");
		//		messages.add("message", message);
		//		saveMessages(request, messages);

		return RestfulConstants.SUCCESS;
	}
	
	public String updatePassword(String loginId, String password, String newPassword) {

		UserBean user = new UserBean(loginId, password);
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME, user);
			if (user != null) {
				service.updatePassword(newPassword);				
//				ActionMessage message = new ActionMessage("message.password");
//				messages.add("message", message);
//				saveMessages(request, messages);
			}
			return RestfulConstants.SUCCESS;
		} catch (Exception e) {
//			ActionMessage msg = new ActionMessage("erros.login.failed");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveErrors(request, messages);
			logger.error("Password change failed. " + e.getMessage());
			return e.getMessage();
		}
	}

}
