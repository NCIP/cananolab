package gov.nih.nci.cananolab.restful.util;

import java.util.List;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SecurityUtil {
	
	private Logger logger = Logger.getLogger(SecurityUtil.class);
	
	public static String MSG_SESSION_INVALID = "User session invalid. Session may have been expired";
	
	public static boolean isUserLoggedIn(HttpServletRequest request) {
		if (request == null)
			return false;
		
		HttpSession session = request.getSession();
		if (session == null)
			return false;
		
		UserBean user = (UserBean) session.getAttribute("user");
		if (user == null || user.getUserId() == null || user.getUserId().length() == 0 )
			return false;
		
		return true;
	}
	
	/**
	 * Evaluate whether user has edit right to the entity (sample, pulication, etc)
	 * @param userAccesses
	 * @param user
	 * @return
	 */
	public static boolean isEntityEditableForUser(List<AccessibilityBean> userAccesses, UserBean user) {
		if (user == null || userAccesses == null) 
			return false;
		
		if (user.isCurator())
			return true;
		
		String loginName = user.getLoginName();
		if (loginName == null || loginName.length() == 0)
			return false;
		
		for (AccessibilityBean access : userAccesses) {
			UserBean aUser = access.getUserBean();
			if (aUser == null) continue;
			
			if (aUser.getLoginName().equals(loginName)) 
				return true;
		}
		
		return false;
		
	} 
}
