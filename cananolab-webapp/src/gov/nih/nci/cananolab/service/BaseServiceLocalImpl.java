package gov.nih.nci.cananolab.service;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.security.authorization.domainobjects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class BaseServiceLocalImpl {
	protected AuthorizationService authService;
	protected Logger logger = Logger.getLogger(BaseServiceLocalImpl.class);
	protected UserBean user;

	public BaseServiceLocalImpl() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public BaseServiceLocalImpl(UserBean user) {
		this.user = user;
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public BaseServiceLocalImpl(AuthorizationService authService) {
		this.authService = authService;
	}

	public BaseServiceLocalImpl(AuthorizationService authService, UserBean user) {
		this.authService = authService;
		this.user = user;
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public List<AccessibilityBean> findGroupAccessibilities(String protectedData)
			throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();
		try {
			if (!authService.checkCreatePermission(user, protectedData)) {
				throw new NoAccessException();
			}
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
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error getting group access for protected data";
			throw new SecurityException(error, e);
		}
		return groupAccesses;
	}

	public List<AccessibilityBean> findUserAccessibilities(String protectedData)
			throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		List<AccessibilityBean> userAccesses = new ArrayList<AccessibilityBean>();
		try {
			if (!authService.checkCreatePermission(user, protectedData)) {
				throw new NoAccessException();
			}
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
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error getting user access for protected data";
			throw new SecurityException(error, e);
		}
		return userAccesses;
	}

	public AccessibilityBean findAccessibilityByGroupName(String groupName,
			String protectedData) throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		AccessibilityBean access = null;
		try {
			if (!authService.checkCreatePermission(user, protectedData)) {
				throw new NoAccessException();
			}
			String roleName = authService
					.getGroupRole(protectedData, groupName);
			access = new AccessibilityBean();
			access.setRoleName(roleName);
			access.setGroupName(groupName);
			access.setGroupAccess(true);
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
		return access;
	}

	public AccessibilityBean findAccessibilityByUserLoginName(
			String userLoginName, String protectedData)
			throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		AccessibilityBean access = null;
		try {
			if (!authService.checkCreatePermission(user, protectedData)) {
				throw new NoAccessException();
			}
			String roleName = authService.getUserRole(protectedData,
					userLoginName);
			access = new AccessibilityBean();
			access.setRoleName(roleName);
			User user = authService.getAuthorizationManager().getUser(
					userLoginName);
			access.setUserBean(new UserBean(user));
			access.setGroupAccess(false);
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
		return access;
	}

	protected void saveAccessibility(AccessibilityBean access,
			String protectedData) throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			String roleName = access.getRoleName();
			if (access.isGroupAccess()) {
				String groupName = access.getGroupName();
				authService.secureObject(protectedData, groupName, roleName);
			} else {
				UserBean user = access.getUserBean();
				authService.secureObjectForUser(protectedData, user
						.getLoginName(), roleName);
			}
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
	}

	protected void saveDefaultAccessibility(String protectedData)
			throws SecurityException, NoAccessException {
		// add default Curator CURD access
		saveAccessibility(Constants.CSM_DEFAULT_ACCESS, protectedData);
		// add default user CURD access if user is not curator
		if (user.isCurator()) {
			AccessibilityBean userAccess = new AccessibilityBean();
			userAccess.setUserBean(user);
			userAccess.setRoleName(Constants.CSM_CURD_ROLE);
			saveAccessibility(userAccess, protectedData);
		}
	}

	protected void deleteAccessibility(AccessibilityBean access,
			String protectedData) throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			String roleName = access.getRoleName();
			if (access.isGroupAccess()) {
				String groupName = access.getGroupName();
				authService.removeSecureObject(protectedData, groupName,
						roleName);
			} else {
				UserBean user = access.getUserBean();
				authService.removeSecureObjectForUser(protectedData, user
						.getLoginName(), roleName);
			}
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
	}

	protected void assignAccessibility(List<AccessibilityBean> accesses,
			String protectedData) throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			// add default Curator CURD access
			if (accesses.isEmpty()) {
				accesses.add(Constants.CSM_DEFAULT_ACCESS);
			}
			// add default user CURD access if user is not curator
			if (user.isCurator()) {
				AccessibilityBean userAccess = new AccessibilityBean();
				userAccess.setUserBean(user);
				userAccess.setRoleName(Constants.CSM_CURD_ROLE);
				accesses.add(userAccess);
			}
			for (AccessibilityBean access : accesses) {
				saveAccessibility(access, protectedData);
			}
		} catch (Exception e) {
			String error = "Error assigning accessibility for the protected data";
			throw new SecurityException(error, e);
		}
	}
}
