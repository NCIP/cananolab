package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.security.exceptions.CSException;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for CSM.
 * 
 * @author pansu
 * 
 */
public class InitSecuritySetup {
	private static LookupService lookupService;

	private static UserService userService;

	private InitSecuritySetup() throws Exception {
		lookupService = new LookupService();
		userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	public static InitSecuritySetup getInstance() throws Exception {
		return new InitSecuritySetup();
	}

	public void setAllUsers(HttpSession session) throws Exception {
		if ((session.getAttribute("newUserCreated") != null)
				|| (session.getServletContext().getAttribute("allUsers") == null)) {
			List allUsers = userService.getAllUsers();
			session.getServletContext().setAttribute("allUsers", allUsers);
		}
		session.removeAttribute("newUserCreated");
	}

	public void setCurrentUser(HttpSession session) throws Exception {

		// get user and date information
		String creator = "";
		if (session.getAttribute("user") != null) {
			UserBean user = (UserBean) session.getAttribute("user");
			creator = user.getLoginName();
		}
		String creationDate = StringUtils.convertDateToString(new Date(),
				CaNanoLabConstants.DATE_FORMAT);
		session.setAttribute("creator", creator);
		session.setAttribute("creationDate", creationDate);
	}

	public static LookupService getLookupService() {
		return lookupService;
	}

	public static UserService getUserService() {
		return userService;
	}

	public boolean canUserExecuteClass(HttpSession session, Class classObj)
			throws CSException {
		UserBean user = (UserBean) session.getAttribute("user");
		// assume the part of the package name containing the function domain
		// is the same as the protection element defined in CSM
		String[] nameStrs = classObj.getName().split("\\.");
		String domain = nameStrs[nameStrs.length - 2];
		return userService.checkExecutePermission(user, domain);
	}

	public boolean userHasCreatePrivilege(UserBean user,
			String protectionElementObjectId) throws CSException {
		boolean status = false;
		status = userService.checkCreatePermission(user,
				protectionElementObjectId);
		return status;
	}

	public boolean userHasDeletePrivilege(UserBean user,
			String protectionElementObjectId) throws CSException {
		boolean status = false;
		status = userService.checkDeletePermission(user,
				protectionElementObjectId);
		return status;
	}

	public void setAllVisibilityGroups(HttpSession session) throws Exception {
		if (session.getAttribute("allVisibilityGroups") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List<String> groupNames = userService.getAllVisibilityGroups();
			session.setAttribute("allVisibilityGroups", groupNames);
		}
		session.removeAttribute("newSampleCreated");
	}

	public void setApplicationOwner(HttpSession session) {
		if (session.getServletContext().getAttribute("applicationOwner") == null) {
			session.getServletContext().setAttribute("applicationOwner",
					CaNanoLabConstants.APP_OWNER);
		}
	}

	/**
	 * Create default CSM groups for default visible groups and admin , and
	 * assign them with default protection groups and roles
	 */
	public void createDefaultCSMGroups() throws Exception {
		for (String groupName : CaNanoLabConstants.VISIBLE_GROUPS) {
			userService.createAGroup(groupName);
		}

		// assign PI group to role CURAD on sample, protocol, nanopoarticle and
		// report
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_SAMPLE,
				CaNanoLabConstants.CSM_CURD_ROLE);
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_PROTOCOL,
				CaNanoLabConstants.CSM_CURD_ROLE);
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_REPORT,
				CaNanoLabConstants.CSM_CURD_ROLE);
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_PARTICLE,
				CaNanoLabConstants.CSM_CURD_ROLE);

		// assign researcher to role CURA on sample
		userService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_RESEARCHER,
				CaNanoLabConstants.CSM_PG_SAMPLE,
				CaNanoLabConstants.CSM_CURD_ROLE);
	}
}
