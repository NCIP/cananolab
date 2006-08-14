package gov.nih.nci.calab.service.security;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authentication.helper.EncryptedRDBMSHelper;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
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
		Set groups = userManager.getGroups(user.getUserId());
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
					.getProtectionGroupRoleContextForUser(user.getUserId());
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
	 * Check whether the given user has the given privilege on the given
	 * protection element
	 * 
	 * @param user
	 * @param protectionElementObjectId
	 * @param privilege
	 * @return
	 * @throws CSException
	 */
	public boolean checkPermission(UserBean user,
			String protectionElementObjectId, String privilege)
			throws CSException {
		boolean status = false;
		status = authorizationManager.checkPermission(user.getLoginName(),
				protectionElementObjectId, privilege);
		return status;
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
		return checkPermission(user, protectionElementObjectId, "EXECUTE");
	}

	/**
	 * Check whether the given user has read privilege on the given protection
	 * element
	 * 
	 * @param user
	 * @param protectionElementObjectId
	 * @return
	 * @throws CSException
	 */
	public boolean checkReadPermission(UserBean user,
			String protectionElementObjectId) throws CSException {
		return checkPermission(user, protectionElementObjectId, "READ");
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
				// make sure change menu item values to lower case since
				// pre-defined
				// pes and pgs in the UPT tool are entered as lower case and
				// CSM API is case sensitive
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

	/*
	 * return all user groups in the database
	 */
	public List<String> getAllGroups() throws Exception {
		List<String> groups = new ArrayList<String>();
		Group dummy = new Group();
		dummy.setGroupName("*");
		SearchCriteria sc = new GroupSearchCriteria(dummy);
		List results = userManager.getObjects(sc);
		for (Object obj : results) {
			Group doGroup = (Group) obj;
			groups.add(doGroup.getGroupName());
		}
		return groups;
	}

	/*
	 * return all user groups in the database
	 */
	public List<String> getAllVisibilityGroups() throws Exception {
		List<String> groups = getAllGroups();
		//filter out the ones starting with NCL
		List<String>filteredGroups=new ArrayList<String>();
		for(String groupName: groups) {
			if (!groupName.startsWith("NCL")) {
				filteredGroups.add(groupName);
			}
		}
		return filteredGroups;
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

	public Group getGroup(String groupName) throws Exception {
		Group group = new Group();
		group.setGroupName(groupName);
		SearchCriteria sc = new GroupSearchCriteria(group);
		List results = userManager.getObjects(sc);
		Group doGroup = null;
		for (Object obj : results) {
			doGroup = (Group) obj;
			break;
		}
		return doGroup;
	}

	public Role getRole(String roleName) throws Exception {
		Role role = new Role();
		role.setName(roleName);
		SearchCriteria sc = new RoleSearchCriteria(role);
		List results = userManager.getObjects(sc);
		Role doRole = null;
		for (Object obj : results) {
			doRole = (Role) obj;
			break;
		}
		return doRole;
	}

	public ProtectionElement getProtectionElement(String objectId)
			throws Exception {
		ProtectionElement pe = new ProtectionElement();
		pe.setObjectId(objectId);
		pe.setProtectionElementName(objectId);
		SearchCriteria sc = new ProtectionElementSearchCriteria(pe);
		List results = userManager.getObjects(sc);
		ProtectionElement doPE = null;
		for (Object obj : results) {
			doPE = (ProtectionElement) obj;
			break;
		}
		if (doPE == null) {
			authorizationManager.createProtectionElement(pe);
			return pe;
		}
		return doPE;
	}

	public ProtectionGroup getProtectionGroup(String protectionGroupName)
			throws Exception {
		ProtectionGroup pg = new ProtectionGroup();
		pg.setProtectionGroupName(protectionGroupName);
		SearchCriteria sc = new ProtectionGroupSearchCriteria(pg);
		List results = userManager.getObjects(sc);
		ProtectionGroup doPG = null;
		for (Object obj : results) {
			doPG = (ProtectionGroup) obj;
			break;
		}
		if (doPG == null) {
			userManager.createProtectionGroup(pg);
			return pg;
		}
		return doPG;
	}

	public void assignProtectionElementToProtectionGroup(ProtectionElement pe,
			ProtectionGroup pg) throws Exception {
		Set assignedPGs = authorizationManager.getProtectionGroups(pe
				.getProtectionElementId().toString());
		for (Object obj : assignedPGs) {
			if (((ProtectionGroup) obj).equals(pg)) {
				return;
			}
		}
		authorizationManager.assignProtectionElement(pg
				.getProtectionGroupName(), pe.getObjectId());
	}

	public void secureObject(String objectName, String groupName,
			String roleName) throws Exception {
		// create protection element
		ProtectionElement pe = getProtectionElement(objectName);

		// create protection group
		ProtectionGroup pg = getProtectionGroup(objectName);

		// assign protection element to protection group if not already exists
		assignProtectionElementToProtectionGroup(pe, pg);

		// get group and role
		Group group = getGroup(groupName);
		Role role = getRole(roleName);
		if (group != null && role != null) {
			String[] roleIds = new String[] { role.getId().toString() };
			userManager.assignGroupRoleToProtectionGroup(pg
					.getProtectionGroupId().toString(), group.getGroupId()
					.toString(), roleIds);
		} else {
			if (group == null) {
				throw new CalabException("No such group defined in CSM: "
						+ groupName);
			}
			if (role == null) {
				throw new CalabException("No such role defined in CSM: "
						+ roleName);
			}
		}
	}

	public List<ParticleBean> getFilteredParticles(UserBean user,
			List<ParticleBean> particles) throws Exception {
		List<ParticleBean> filteredParticles = new ArrayList<ParticleBean>();
		for (ParticleBean particle : particles) {
			boolean status = checkReadPermission(user, particle.getSampleName());
			if (status) {
				filteredParticles.add(particle);
			}
		}
		return filteredParticles;
	}
}
