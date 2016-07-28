/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.restful.RestfulConstants;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;

/**
 * The LoginAction authenticates a user into the system.
 * 
 * @author pansu
 */

@Component("loginBO")
public class LoginBO  {
	
	private Logger logger = Logger.getLogger(LoginBO.class);
	
	public String login(String username, String password, HttpServletRequest request) {		
		
		// if not using LDAP, check if the password is the initial password
		// redirect to change password page
		Boolean useLDAP = new Boolean(PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "useLDAP"));
		
		UserBean user = new UserBean(username, password);
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME, user);
			// Invalidate the current session and create a new one
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			session = request.getSession(true);
			session.setAttribute("securityService", service);
			logger.debug("Is user bean null: " + (service.getUserBean() == null));
			session.setAttribute("user", service.getUserBean());
			
		} catch (Exception e) {
			logger.error("Error while logging in user: " + username + ". " + e.getMessage());
			logger.debug(e.getMessage());
			if(e.getMessage().contains("User logging in first time, Password should be changed"))
				return "User logging in first time, Password should be changed";
			else
				return "Username or Password invalid";
		}
		
		return RestfulConstants.SUCCESS;
		
	}
	
	public String updatePassword(String loginId, String password, String newPassword, String confirmPassword) {

		UserBean user = new UserBean(loginId, password);
		boolean flag = false;
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
			if (user != null) {
				flag = service.resetPassword(loginId, password, newPassword, confirmPassword);				
			}
			if(flag)
				return RestfulConstants.SUCCESS;
			return "Password reset failed";
		} catch (Exception e) {
			logger.error("Password change failed. " + e.getMessage());
			return e.getMessage();
		}
	}

}
