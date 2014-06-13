package gov.nih.nci.cananolab.restful.security;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;

public class Login {
	
	public String checkLogin(String username, String password) {
		
		Boolean useLDAP = new Boolean(PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "useLDAP"));
		
		if (!useLDAP && username.equals(password)) {
			
			//TODO: switch this when we have a service for change password
			
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
			
			//TODO: need to handle session
			
			// Invalidate the current session and create a new one
//			HttpSession session = request.getSession(false);
//			if (session != null) {
//				session.invalidate();
//				session = request.getSession(true);
//				session.setAttribute("securityService", service);
//				session.setAttribute("user", service.getUserBean());
//			}
		} catch (Exception e) {
			return "error";
		}
		
		return "success";
	}

}
