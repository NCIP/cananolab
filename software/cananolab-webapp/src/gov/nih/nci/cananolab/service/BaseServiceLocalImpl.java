package gov.nih.nci.cananolab.service;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class BaseServiceLocalImpl implements BaseService {
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
				Group group = authService.getGroup(groupName);
				// include Public group and groups that user has access to
				if (group.getGroupName().equals(Constants.CSM_PUBLIC_GROUP)
						|| authService.checkReadPermission(user,
								Constants.CSM_COLLABORATION_GROUP_PREFIX
										+ group.getGroupId())) {
					String roleName = entry.getValue();
					AccessibilityBean access = new AccessibilityBean();
					access.setRoleName(roleName);
					access.setGroupName(groupName);
					groupAccesses.add(access);
				}
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
			Group group = authService.getGroup(groupName);
			// exclude groups that user has no access to
			if (authService.checkReadPermission(user,
					Constants.CSM_COLLABORATION_GROUP_PREFIX
							+ group.getGroupId())) {
				String roleName = authService.getGroupRole(protectedData,
						groupName);
				access = new AccessibilityBean();
				access.setRoleName(roleName);
				access.setGroupName(groupName);
				access.setGroupAccess(true);
			} else {
				throw new NoAccessException();
			}
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

	public List<UserBean> findUserLoginNames(String loginNameSearchStr)
			throws SecurityException {
		List<UserBean> matchedUsers = new ArrayList<UserBean>();
		try {
			User user = new User();
			user.setLoginName("*");
			SearchCriteria sc = new UserSearchCriteria(user);
			List result = authService.getAuthorizationManager().getObjects(sc);
			for (Object obj : result) {
				User aUser = (User) obj;
				if (StringUtils.isEmpty(loginNameSearchStr)
						|| aUser.getLoginName().toLowerCase().contains(
								loginNameSearchStr.toLowerCase())
						|| aUser.getFirstName().toLowerCase().contains(
								loginNameSearchStr.toLowerCase())
						|| aUser.getLastName().toLowerCase().contains(
								loginNameSearchStr.toLowerCase())) {
					matchedUsers.add(new UserBean(aUser));
				}
			}
		} catch (Exception e) {
			String error = "Error finding users based on login name search string";
			throw new SecurityException(error, e);
		}
		Collections.sort(matchedUsers, new Comparator<UserBean>() {
			public int compare(UserBean u1, UserBean u2) {
				return u1.compareTo(u2);
			}
		});

		return matchedUsers;
	}

	public List<String> findGroupNames(String groupNameSearchStr)
			throws SecurityException {
		List<String> matchedGroupNames = new ArrayList<String>();
		try {
			Group group = new Group();
			group.setGroupName("*");
			SearchCriteria sc = new GroupSearchCriteria(group);
			List result = authService.getAuthorizationManager().getObjects(sc);
			for (Object obj : result) {
				Group aGroup = (Group) obj;
				if (StringUtils.isEmpty(groupNameSearchStr)
						|| aGroup.getGroupName().toLowerCase().contains(
								groupNameSearchStr.toLowerCase())) {
					matchedGroupNames.add(aGroup.getGroupName());
				}
			}
		} catch (Exception e) {
			String error = "Error finding groups based on group name search string";
			throw new SecurityException(error, e);
		}
		Collections.sort(matchedGroupNames);
		return matchedGroupNames;
	}

	public AuthorizationService getAuthService() {
		return authService;
	}
}
