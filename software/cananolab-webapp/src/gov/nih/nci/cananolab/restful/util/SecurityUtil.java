package gov.nih.nci.cananolab.restful.util;

import gov.nih.nci.cananolab.service.security.UserBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SecurityUtil {
	
	private Logger logger = Logger.getLogger(SecurityUtil.class);
	
	public static String MSG_SESSION_INVALID = "User session invalidate. Session may have been expired";
	
	public static boolean isUserLoggedIn(HttpServletRequest request) {
		if (request == null)
			return false;
		
		HttpSession session = request.getSession(false);
		if (session == null)
			return false;
		
		UserBean user = (UserBean) session.getAttribute("user");
		if (user == null)
			return false;
		
		return true;
	}
}
