package gov.nih.nci.calab.service.security;

import gov.nih.nci.calab.db.HibernateDataAccess;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
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

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.beans.MenuItem;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

/**
 * This class takes care of authentication and authorization of a user and group
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

	/**
	 * Check whether the given user is the admin of the application.
	 * 
	 * @param user
	 * @return
	 */
	public boolean isAdmin(String user) {
		boolean adminStatus = authorizationManager.checkOwnership(user,
				applicationName);
		return adminStatus;
	}

	/**
	 * Check whether the given user belongs to the given group.
	 * 
	 * @param user
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
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
		return checkPermission(user, protectionElementObjectId,
				CaNanoLabConstants.CSM_READ_PRIVILEGE);
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
		List<MenuItem> items = new ArrayList<MenuItem>(
				(List<? extends MenuItem>) session.getAttribute("items"));
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

	/**
	 * Set a new password for the given user login name
	 * 
	 * @param loginName
	 * @param newPassword
	 * @throws Exception
	 */
	public void updatePassword(String loginName, String newPassword)
			throws Exception {
		User user = authorizationManager.getUser(loginName);
		java.util.Map options = SecurityServiceProvider
				.getLoginModuleOptions(CaNanoLabConstants.CSM_APP_NAME);
		String encryptedPassword = EncryptedRDBMSHelper.encrypt(newPassword,
				(String) options.get("hashAlgorithm"));
		user.setPassword(encryptedPassword);
		userManager.modifyUser(user);
	}

	/**
	 * Get all users in the application
	 * 
	 * @return
	 * @throws Exception
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

	/**
	 * Get all user groups in the application
	 * 
	 * @return
	 * @throws Exception
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

	/**
	 * Get all user visiblity groups in the application (filtering out all
	 * groups starting with APP_OWNER).
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllVisibilityGroups() throws Exception {
		List<String> groups = getAllGroups();
		// filter out the ones starting with APP_OWNER
		List<String> filteredGroups = new ArrayList<String>();
		for (String groupName : groups) {
			if (!groupName.startsWith(CaNanoLabConstants.APP_OWNER)) {
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

	/**
	 * Get a Group object for the given groupName.
	 * 
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * Create a user group in the CSM database if it's not already created
	 * 
	 * @param groupName
	 * @throws Exception
	 */
	public void createAGroup(String groupName) throws Exception {
		Group doGroup = getGroup(groupName);
		if (doGroup == null) {
			doGroup = new Group();
			doGroup.setGroupName(groupName);
			userManager.createGroup(doGroup);
		}
	}

	/**
	 * Get a Role object for the given roleName.
	 * 
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * Get a ProtectionElement object for the given objectId.
	 * 
	 * @param objectId
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * Get a ProtectionGroup object for the given protectionGroupName.
	 * 
	 * @param protectionGroupName
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * Assign a ProtectionElement to a ProtectionGroup if not already assigned.
	 * 
	 * @param pe
	 * @param pg
	 * @throws Exception
	 */
	public void assignProtectionElementToProtectionGroup(ProtectionElement pe,
			ProtectionGroup pg) throws Exception {
		Set<ProtectionGroup> assignedPGs = new HashSet<ProtectionGroup>(
				(Set<? extends ProtectionGroup>) authorizationManager
						.getProtectionGroups(pe.getProtectionElementId()
								.toString()));
		// check to see if the assignment is already made to ignore CSM
		// exception.

		// contains doesn't work because CSM didn't implement hashCode in
		// ProtectionGroup.
		// if (assignedPGs.contains(pg)) {
		// return;
		// }
		for (ProtectionGroup aPg : assignedPGs) {
			if (aPg.equals(pg)) {
				return;
			}
		}
		authorizationManager.assignProtectionElement(pg
				.getProtectionGroupName(), pe.getObjectId());
	}

	/**
	 * Direct CSM schema query to improve performance. Get the existing role IDs
	 * from database
	 * 
	 * @param objectName
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	public List<String> getExistingRoleIds(ProtectionGroup pg, Group group)
			throws Exception {
		List<String> roleIds = new ArrayList<String>();		
		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		String query = "select distinct role_id from csm_user_group_role_pg "
				+ "where protection_group_id=" + pg.getProtectionGroupId()
				+ " and group_id=" + group.getGroupId();
		try {
			hda.open();
			SQLQuery queryObj = hda.getNativeQuery(query);
			queryObj.addScalar("ROLE_ID", Hibernate.STRING);
			List results = queryObj.list();
			for (Object obj : results) {
				String roleId = (String) obj;
				roleIds.add(roleId);
			}
		} catch (Exception e) {
			throw new Exception(
					"error getting existing roles from CSM database:" + e);

		} finally {
			hda.close();
		}

		return roleIds;
	}

	/**
	 * Get the existing role IDs from database
	 * 
	 * @param objectName
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	public List<String> getExistingRoleIdsSlow(ProtectionGroup pg, Group group)
			throws Exception {
		List<String> roleIds = new ArrayList<String>();
		Set existingRoles = null;
		Set contexts = userManager.getProtectionGroupRoleContextForGroup(group
				.getGroupId().toString());
		for (Object obj : contexts) {
			ProtectionGroupRoleContext context = (ProtectionGroupRoleContext) obj;
			if (context.getProtectionGroup().equals(pg)) {
				existingRoles = context.getRoles();
				break;
			}
		}
		if (existingRoles != null) {
			for (Object obj : existingRoles) {
				Role aRole = (Role) obj;
				roleIds.add(aRole.getId().toString());
			}
		}
		return roleIds;
	}

	/**
	 * Assign the given objectName to the given groupName with the given
	 * roleName. Add to existing roles the object has for the group.
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws Exception
	 */
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

		if (group == null) {
			throw new CalabException("No such group defined in CSM: "
					+ groupName);
		}
		if (role == null) {
			throw new CalabException("No such role defined in CSM: " + roleName);
		}

		List<String> existingRoleIds = getExistingRoleIds(pg, group);
		List<String> allRoleIds = new ArrayList<String>(existingRoleIds);
		if (!existingRoleIds.contains(role.getId().toString())) {
			allRoleIds.add(role.getId().toString());
		}
		userManager.assignGroupRoleToProtectionGroup(pg.getProtectionGroupId()
				.toString(), group.getGroupId().toString(), allRoleIds
				.toArray(new String[0]));

	}

	/**
	 * Get a list of particle the user has read permission on.
	 * 
	 * @param user
	 * @param particles
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * Get a list of files the user has read permission on.
	 * 
	 * @param user
	 * @param particles
	 * @return
	 * @throws Exception
	 */
	public List<LabFileBean> getFilteredFiles(UserBean user,
			List<LabFileBean> files) throws Exception {
		List<LabFileBean> filteredReports = new ArrayList<LabFileBean>();
		for (LabFileBean file : files) {
			boolean status = checkReadPermission(user, file.getId());
			if (status) {
				filteredReports.add(file);
			}
		}
		return filteredReports;
	}

	/**
	 * Get a list of groups the given object is assgined to with the given role
	 * 
	 * @param objectName
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	public List<String> getAccessibleGroupsSlow(String objectName,
			String roleName) throws Exception {
		List<String> groups = new ArrayList<String>();
		List<String> allGroups = getAllGroups();
		Role role = getRole(roleName);
		for (String groupName : allGroups) {
			Group group = getGroup(groupName);
			Set contexts = userManager
					.getProtectionGroupRoleContextForGroup(group.getGroupId()
							.toString());
			for (Object obj : contexts) {
				ProtectionGroupRoleContext context = (ProtectionGroupRoleContext) obj;
				ProtectionGroup pg = context.getProtectionGroup();
				Set<Role> roles = new HashSet<Role>(
						(Set<? extends Role>) context.getRoles());
				// contains doesn't work because CSM didn't implement hashCode
				// in Role.
				// if (pg.getProtectionGroupName().equals(objectName)
				// && roles.contains(role)) {
				// groups.add(groupName);
				// }
				if (pg.getProtectionGroupName().equals(objectName)) {
					for (Role aRole : roles) {
						if (aRole.equals(role)) {
							groups.add(groupName);
						}
					}
				}
			}
		}
		return groups;
	}

	/**
	 * Directly query CSM schema to improve performance
	 * 
	 * @param objectName
	 * @param roleName
	 * @return
	 */
	public List<String> getAccessibleGroups(String objectName, String roleName)
			throws Exception {
		List<String> groups = new ArrayList<String>();
		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		String query = "select d.GROUP_NAME GROUP_NAME from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d	"
				+ "where a.PROTECTION_GROUP_ID=c.PROTECTION_GROUP_ID and b.ROLE_ID=c.ROLE_ID and c.GROUP_ID=d.GROUP_ID and "
				+ "a.PROTECTION_GROUP_NAME='"
				+ objectName
				+ "' and b.ROLE_NAME='" + roleName + "'";
		try {
			hda.open();
			SQLQuery queryObj = hda.getNativeQuery(query);
			queryObj.addScalar("GROUP_NAME", Hibernate.STRING);
			List results = queryObj.list();
			for (Object obj : results) {
				String group = (String) obj;
				groups.add(group);
			}
		} catch (Exception e) {
			throw new Exception(
					"error getting accessible groups from CSM database:" + e);

		} finally {
			hda.close();
		}
		return groups;
	}

	/**
	 * Remove the group the object is assigned to with the given role.
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws Exception
	 */
	public void removeAccessibleGroupSlow(String objectName, String groupName,
			String roleName) throws Exception {
		Group group = getGroup(groupName);
		Role role = getRole(roleName);
		ProtectionGroup pg = getProtectionGroup(objectName);

		// this method is not implemented in CSM API, try an alternative
		// userManager.removeGroupRoleFromProtectionGroup(pg
		// .getProtectionGroupId().toString(), group.getGroupId()
		// .toString(), new String[] { role.getId().toString() });

		// get existing roles.
		Set contexts = userManager.getProtectionGroupRoleContextForGroup(group
				.getGroupId().toString());
		Set<Role> existingRoles = null;
		for (Object obj : contexts) {
			ProtectionGroupRoleContext context = (ProtectionGroupRoleContext) obj;
			if (context.getProtectionGroup().equals(pg)) {
				existingRoles = new HashSet<Role>((Set<? extends Role>) context
						.getRoles());
				break;
			}
		}
		// remove role from existing roles
		// remove doesn't work because CSM didn't implement hashCode for Role
		// existingRoles.remove(role);

		Set<Role> updatedRoles = new HashSet<Role>();
		for (Role aRole : existingRoles) {
			if (!aRole.equals(role)) {
				updatedRoles.add(aRole);
			}
		}
		// reassign the roles.
		String[] roleIds = new String[updatedRoles.size()];
		int i = 0;
		for (Object obj : updatedRoles) {
			Role aRole = (Role) obj;
			roleIds[i] = aRole.getId().toString();
			i++;
		}
		userManager.assignGroupRoleToProtectionGroup(pg.getProtectionGroupId()
				.toString(), group.getGroupId().toString(), roleIds);
	}

	/**
	 * Direct CSM schema query to improve performance
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws Exception
	 */
	public void removeAccessibleGroup(String objectName, String groupName,
			String roleName) throws Exception {
		ProtectionGroup pg = this.getProtectionGroup(objectName);
		Role role = this.getRole(roleName);
		Group group = this.getGroup(groupName);

		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		String query = "delete from csm_user_group_role_pg "
				+ "where PROTECTION_GROUP_ID=" + pg.getProtectionGroupId()
				+ "and ROLE_ID=" + role.getId() + " and GROUP_ID="
				+ group.getGroupId();
		try {
			hda.open();
			Connection connection = hda.getConnection();
			Statement stmt = connection.createStatement();
			stmt.execute(query);
		} catch (Exception e) {
			throw new Exception(
					"error getting accessible groups from CSM database:" + e);
		} finally {
			hda.close();
		}
	}

	/**
	 * Direct CSM schema query to improve performance
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws Exception
	 */
	public void removeAllAccessibleGroups(String objectName, String roleName,
			String[] exceptionGroupNames) throws Exception {
		ProtectionGroup pg = this.getProtectionGroup(objectName);
		Role role = this.getRole(roleName);
		List<String> exceptionGroupIds = new ArrayList<String>();
		if (exceptionGroupNames != null) {
			for (String groupName : exceptionGroupNames) {
				Group group = getGroup(groupName);
				exceptionGroupIds.add(group.getGroupId().toString());
			}
		}

		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		String query = "delete from csm_user_group_role_pg "
				+ "where PROTECTION_GROUP_ID=" + pg.getProtectionGroupId()
				+ "and ROLE_ID=" + role.getId();
		if (!exceptionGroupIds.isEmpty()) {
			query += " and GROUP_ID not in ("
					+ StringUtils.join(exceptionGroupIds, ",") + ")";
		}
		try {
			hda.open();
			Connection connection = hda.getConnection();
			Statement stmt = connection.createStatement();
			stmt.execute(query);
		} catch (Exception e) {
			throw new Exception(
					"error getting accessible groups from CSM database:" + e);
		} finally {
			hda.close();
		}
	}
}
