package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.common.AccessService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.security.authorization.domainobjects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccessServiceLocalImpl extends BaseServiceHelper implements
		AccessService {
	public AccessServiceLocalImpl() {

	}

	public AccessServiceLocalImpl(UserBean user) {
		super(user);
	}

	public List<AccessibilityBean> findGroupAccessibilities(String protectedData)
			throws SecurityException {
		List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();
		try {
			AuthorizationService authService = super.getAuthService();
			Map<String, String> groupRoles = authService
					.getGroupRoles(protectedData);
			for (Map.Entry<String, String> entry : groupRoles.entrySet()) {
				String groupName = entry.getKey();
				String roleName = entry.getValue();
				AccessibilityBean access = new AccessibilityBean();
				access.setRoleName(roleName);
				access.setGroupName(groupName);
				groupAccesses.add(access);
			}
		} catch (Exception e) {
			String error = "Error getting group access for protected data";
			throw new SecurityException(error, e);
		}
		return groupAccesses;
	}

	public List<AccessibilityBean> findUserAccessibilities(String protectedData)
			throws SecurityException {
		List<AccessibilityBean> userAccesses = new ArrayList<AccessibilityBean>();
		try {
			AuthorizationService authService = super.getAuthService();
			Map<String, String> userRoles = authService
					.getUserRoles(protectedData);
			for (Map.Entry<String, String> entry : userRoles.entrySet()) {
				String userLoginName = entry.getKey();
				String roleName = entry.getValue();
				User user = authService.getAuthorizationManager().getUser(
						userLoginName);
				AccessibilityBean access = new AccessibilityBean();
				access.setRoleName(roleName);
				access.setUserBean(new UserBean(user));
				userAccesses.add(access);
			}
		} catch (Exception e) {
			String error = "Error getting user access for protected data";
			throw new SecurityException(error, e);
		}
		return userAccesses;
	}

	public void saveAccessibility(AccessibilityBean access, String protectedData)
			throws SecurityException {
		try {
			AuthorizationService authService = super.getAuthService();
			String roleName = access.getRoleName();
			if (access.getAccessBy().equals("user")) {
				UserBean user = access.getUserBean();
				authService.secureObjectForUser(protectedData, user
						.getLoginName(), roleName);
			} else {
				String groupName = access.getGroupName();
				authService.secureObject(protectedData, groupName, roleName);
			}
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
	}
}