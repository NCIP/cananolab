package gov.nih.nci.calab.service.security;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authentication.helper.EncryptedRDBMSHelper;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.beans.MenuItem;

/**
 * This class takes care of authentication and authorization of a user
 * 
 * @author Pansu
 * 
 */
public class UserService {
	private AuthenticationManager authenticationManager = null;

	private AuthorizationManager authorizationManager = null;

	private UserProvisioningManager userManager = null;

	private String applicationName = null;

	public UserService(String applicationName) throws CSException {
		this.applicationName = applicationName;
		this.authenticationManager = SecurityServiceProvider
				.getAuthenticationManager(applicationName);
		this.authorizationManager = SecurityServiceProvider
				.getAuthorizationManager(applicationName);
		this.userManager = SecurityServiceProvider
				.getUserProvisioningManager(applicationName);
	}

	public UserBean getUserBean(String userLogin) {
		User user = authorizationManager.getUser(userLogin);
		return new UserBean(user); // or
		// userManger.getUser(userLoginId);
	}

	public boolean isAdmin(String user) {
		boolean adminStatus = authorizationManager.checkOwnership(user,
				applicationName);
		return adminStatus;
	}

	public boolean isUserInGroup(UserBean user, String groupName)
			throws Exception {
		Set groups = userManager.getGroups(user.getUserId() + "");
		for (Object obj : groups) {
			Group group = (Group) obj;
			if (group.getGroupName().equalsIgnoreCase(groupName)
					|| group.getGroupName().startsWith(groupName)) {
				return true;
			}
		}
		return false;
	}

	public boolean accessProtectionGroup(UserBean user,
			String protectionGroupName) {
		try {
			Set<ProtectionGroupRoleContext> protectionGroupRoleContexts = userManager
					.getProtectionGroupRoleContextForUser(user.getUserId()
							.toString());
			for (ProtectionGroupRoleContext context : protectionGroupRoleContexts) {
				ProtectionGroup pg = context.getProtectionGroup();
				while (pg.getParentProtectionGroup() != null) {
					if (pg.getParentProtectionGroup().getProtectionGroupName()
							.trim().equalsIgnoreCase(protectionGroupName)) {
						return true;
					}
					pg = pg.getParentProtectionGroup();
				}
			}
		} catch (CSObjectNotFoundException e) {
			return false;
		}
		return false;
	}

	/**
	 * Check whether the given user has execute privilege on the given
	 * protection element
	 * 
	 * @param user
	 * @param protectionElementObjectId
	 * @return
	 * @throws CSException
	 */
	public boolean checkExecutePermission(UserBean user,
			String protectionElementObjectId) throws CSException {
		boolean status = false;
		status = authorizationManager.checkPermission(user.getLoginName(),
				protectionElementObjectId.toLowerCase(), "EXECUTE");
		return status;
	}

	/**
	 * Check whether user can execute the menuItems in session, each defined as
	 * a protection element using UPT tool. The excluded menuItems are not
	 * checked.
	 * 
	 * @param session
	 * @throws CSException
	 */
	public void setFilteredMenuItem(HttpSession session) throws CSException {
		if (session.getAttribute("filteredItems") != null) {
			return;
		}

		List<MenuItem> filteredItems = new ArrayList<MenuItem>();
		List<MenuItem> items = (List) session.getAttribute("items");
		UserBean user = (UserBean) session.getAttribute("user");
		if (user != null) {
			for (MenuItem item : items) {
				boolean executeStatus = checkExecutePermission(user, item
						.getValue().toLowerCase());
				if (executeStatus) {
					filteredItems.add(item);
				}
			}
			session.setAttribute("filteredItems", filteredItems);
		}
	}

	public void updatePassword(String loginName, String newPassword)
			throws Exception {
		User user = authorizationManager.getUser(loginName);
		java.util.Map options = SecurityServiceProvider
				.getLoginModuleOptions(CalabConstants.CSM_APP_NAME);
		String encryptedPassword = EncryptedRDBMSHelper.encrypt(newPassword,
				(String) options.get("hashAlgorithm"));
		user.setPassword(encryptedPassword);
		userManager.modifyUser(user);
	}

	/*
	 * return all users in the database
	 */
	public List<UserBean> getAllUsers() throws Exception {
		List<UserBean> users = new ArrayList<UserBean>();
		User dummy = new User();
		dummy.setLoginName("*");
		SearchCriteria sc = new UserSearchCriteria(dummy);
		List results = userManager.getObjects(sc);
		for (Object obj : results) {
			User doUser = (User) obj;
			users.add(new UserBean(doUser));
		}
		return users;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public UserProvisioningManager getUserManager() {
		return userManager;
	}
}
