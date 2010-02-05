package gov.nih.nci.cananolab.ui.security;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for CSM.
 *
 * @author pansu
 *
 */
public class InitSecuritySetup {
	private static AuthorizationService authorizationService;

	private InitSecuritySetup() throws SecurityException {
		authorizationService = new AuthorizationService(Constants.CSM_APP_NAME);
	}

	public static InitSecuritySetup getInstance()
			throws SecurityException {
		return new InitSecuritySetup();
	}

	public static AuthorizationService getAuthorizationService() {
		return authorizationService;
	}

	public boolean userHasCreatePrivilege(UserBean user,
			String protectionElementObjectId) throws SecurityException {
		boolean status = false;
		status = authorizationService.checkCreatePermission(user,
				protectionElementObjectId);
		return status;
	}

	public boolean userHasAdminPrivilege(UserBean user) throws SecurityException {
		boolean status=false;
		status=authorizationService.isAdmin(user);
		return status;
	}

	public boolean userHasDeletePrivilege(UserBean user,
			String protectionElementObjectId) throws SecurityException {
		boolean status = false;
		status = authorizationService.checkDeletePermission(user,
				protectionElementObjectId);
		return status;
	}

	public List<String> getAllVisibilityGroups(HttpServletRequest request)
			throws SecurityException {
		List<String> groupNames = authorizationService
				.getAllVisibilityGroups();
		request.getSession().setAttribute("allVisibilityGroups", groupNames);
		return groupNames;
	}

	public List<String> getAllVisibilityGroupsWithoutOrg(
			HttpServletRequest request, String sampleOrg)
			throws SecurityException {
		List<String> groupNames = getAllVisibilityGroups(request);
		if (!StringUtils.isEmpty(sampleOrg))
			groupNames.remove(sampleOrg);
		request.getSession().setAttribute("allVisibilityGroupsNoOrg",
				groupNames);
		return groupNames;
	}

	/**
	 * Create default CSM groups for default visible groups, and
	 * assign them with default protection groups and roles
	 */
	public void createDefaultCSMGroups() throws SecurityException {
		// create default groups
		for (String groupName : Constants.VISIBLE_GROUPS) {
			authorizationService.createAGroup(groupName);
		}

		// assign Curator group to role CURD on protocol, sample and
		// publication
		authorizationService.assignGroupToProtectionGroupWithRole(
				Constants.CSM_DATA_CURATOR, Constants.CSM_PG_PROTOCOL,
				Constants.CSM_CURD_ROLE);
		authorizationService.assignGroupToProtectionGroupWithRole(
				Constants.CSM_DATA_CURATOR, Constants.CSM_PG_PUBLICATION,
				Constants.CSM_CURD_ROLE);
		authorizationService.assignGroupToProtectionGroupWithRole(
				Constants.CSM_DATA_CURATOR, Constants.CSM_PG_SAMPLE,
				Constants.CSM_CURD_ROLE);
	}
}
