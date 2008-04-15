package gov.nih.nci.cananolab.ui.security;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for CSM.
 * 
 * @author pansu
 * 
 */
public class InitSecuritySetup {
	private static AuthorizationService authorizationService;

	private InitSecuritySetup() throws CaNanoLabSecurityException {
		authorizationService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
	}

	public static InitSecuritySetup getInstance()
			throws CaNanoLabSecurityException {
		return new InitSecuritySetup();
	}

	public static AuthorizationService getAuthorizationService() {
		return authorizationService;
	}

	public boolean canUserExecuteClass(HttpSession session, Class classObj)
			throws CaNanoLabSecurityException {
		UserBean user = (UserBean) session.getAttribute("user");
		// assume the part of the package name containing the function domain
		// is the same as the protection element defined in CSM
		String[] nameStrs = classObj.getName().split("\\.");
		String domain = nameStrs[nameStrs.length - 2];
		return authorizationService.checkExecutePermission(user, domain);
	}

	public boolean userHasCreatePrivilege(UserBean user,
			String protectionElementObjectId) throws CaNanoLabSecurityException {
		boolean status = false;
		status = authorizationService.checkCreatePermission(user,
				protectionElementObjectId);
		return status;
	}

	public boolean userHasDeletePrivilege(UserBean user,
			String protectionElementObjectId) throws CaNanoLabSecurityException {
		boolean status = false;
		status = authorizationService.checkDeletePermission(user,
				protectionElementObjectId);
		return status;
	}

	public void setAllVisibilityGroups(HttpSession session)
			throws CaNanoLabSecurityException {
		List<String> groupNames = authorizationService.getAllVisibilityGroups();
		session.setAttribute("allVisibilityGroups", groupNames);
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
	public void createDefaultCSMGroups() throws CaNanoLabSecurityException {
		// create default groups
		for (String groupName : CaNanoLabConstants.VISIBLE_GROUPS) {
			authorizationService.createAGroup(groupName);
		}

		// assign PI group to role CURD on sample, protocol, nanopoarticle and
		// report
		authorizationService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_SAMPLE,
				CaNanoLabConstants.CSM_CURD_ROLE);
		authorizationService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_PROTOCOL,
				CaNanoLabConstants.CSM_CURD_ROLE);
		authorizationService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_REPORT,
				CaNanoLabConstants.CSM_CURD_ROLE);
		authorizationService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_PI, CaNanoLabConstants.CSM_PG_PARTICLE,
				CaNanoLabConstants.CSM_CURD_ROLE);

		// assign researcher to role CURD on sample
		authorizationService.assignGroupToProtectionGroupWithRole(
				CaNanoLabConstants.CSM_RESEARCHER,
				CaNanoLabConstants.CSM_PG_SAMPLE,
				CaNanoLabConstants.CSM_CURD_ROLE);
	}
}
