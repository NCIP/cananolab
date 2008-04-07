package gov.nih.nci.cananolab.service.security;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.StringUtils;
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
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * This class takes care of authentication and authorization of a user and group
 * 
 * @author Pansu
 * 
 */
public class AuthorizationService {
	private Logger logger = Logger.getLogger(AuthorizationService.class);

	private AuthenticationManager authenticationManager = null;

	private AuthorizationManager authorizationManager = null;

	private UserProvisioningManager userManager = null;

	private String applicationName = null;

	private CustomizedApplicationService appService = null;

	public AuthorizationService(String applicationName)
			throws CaNanoLabSecurityException {
		try {
			this.applicationName = applicationName;
			this.authenticationManager = SecurityServiceProvider
					.getAuthenticationManager(applicationName);
			this.authorizationManager = SecurityServiceProvider
					.getAuthorizationManager(applicationName);
			this.userManager = SecurityServiceProvider
					.getUserProvisioningManager(applicationName);
			this.appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
		} catch (Exception e) {
			logger.error(e);
			throw new CaNanoLabSecurityException();
		}
	}

	public UserBean getUserBean(String userLogin) {
		User user = this.authorizationManager.getUser(userLogin);
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
		boolean adminStatus = this.authorizationManager.checkOwnership(user,
				this.applicationName);
		return adminStatus;
	}

	/**
	 * Check whether the given user belongs to the given group.
	 * 
	 * @param user
	 * @param groupName
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public boolean isUserInGroup(UserBean user, String groupName)
			throws CaNanoLabSecurityException {
		try {
			Set groups = this.userManager.getGroups(user.getUserId());
			for (Object obj : groups) {
				Group group = (Group) obj;
				if (group.getGroupName().equalsIgnoreCase(groupName)
						|| group.getGroupName().startsWith(groupName)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("Error in checking if user is in the group.", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Check whether the given user has the given privilege on the given
	 * protection element
	 * 
	 * @param user
	 * @param protectionElementObjectId
	 * @param privilege
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public boolean checkPermission(UserBean user,
			String protectionElementObjectId, String privilege)
			throws CaNanoLabSecurityException {
		try {
			boolean status = false;
			if (user == null) {
				return status;
			}
			status = this.authorizationManager.checkPermission(user
					.getLoginName(), protectionElementObjectId, privilege);
			return status;
		} catch (Exception e) {
			logger.error("Error in checking user permission.", e);
			throw new CaNanoLabSecurityException();
		}
	}

	public boolean checkCreatePermission(UserBean user,
			String protectionElementObjectId) throws CaNanoLabSecurityException {
		return checkPermission(user, protectionElementObjectId,
				CaNanoLabConstants.CSM_CREATE_PRIVILEGE);
	}

	/**
	 * Check whether the given user has execute privilege on the given
	 * protection element
	 * 
	 * @param user
	 * @param protectionElementObjectId
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public boolean checkExecutePermission(UserBean user,
			String protectionElementObjectId) throws CaNanoLabSecurityException {
		return checkPermission(user, protectionElementObjectId,
				CaNanoLabConstants.CSM_EXECUTE_PRIVILEGE);
	}

	/**
	 * Check whether the given user has read privilege on the given protection
	 * element
	 * 
	 * @param user
	 * @param protectionElementObjectId
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public boolean checkReadPermission(UserBean user,
			String protectionElementObjectId) throws Exception {
		return checkPermission(user, protectionElementObjectId,
				CaNanoLabConstants.CSM_READ_PRIVILEGE);
	}

	/**
	 * Check whether the given user has delete privilege on the given protection
	 * element
	 * 
	 * @param user
	 * @param protectionElementObjectId
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public boolean checkDeletePermission(UserBean user,
			String protectionElementObjectId) throws CaNanoLabSecurityException {
		return checkPermission(user, protectionElementObjectId,
				CaNanoLabConstants.CSM_DELETE_PRIVILEGE);
	}

	/**
	 * Set a new password for the given user login name
	 * 
	 * @param loginName
	 * @param newPassword
	 * @throws CaNanoLabSecurityException
	 */
	public void updatePassword(String loginName, String newPassword)
			throws CaNanoLabSecurityException {
		try {
			User user = this.authorizationManager.getUser(loginName);
			java.util.Map options = SecurityServiceProvider
					.getLoginModuleOptions(CaNanoLabConstants.CSM_APP_NAME);
			String encryptedPassword = EncryptedRDBMSHelper.encrypt(
					newPassword, (String) options.get("hashAlgorithm"));
			user.setPassword(encryptedPassword);
			this.userManager.modifyUser(user);
		} catch (Exception e) {
			logger.error("Error in updating password.", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Get all users in the application
	 * 
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public List<UserBean> getAllUsers() throws CaNanoLabSecurityException {
		try {
			List<UserBean> users = new ArrayList<UserBean>();
			User dummy = new User();
			dummy.setLoginName("*");
			SearchCriteria sc = new UserSearchCriteria(dummy);
			List results = this.userManager.getObjects(sc);
			for (Object obj : results) {
				User doUser = (User) obj;
				users.add(new UserBean(doUser));
			}
			return users;
		} catch (Exception e) {
			logger.error("Error in getting all users.", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Get all user groups in the application
	 * 
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public List<String> getAllGroups() throws CaNanoLabSecurityException {
		try {
			List<String> groups = new ArrayList<String>();
			Group dummy = new Group();
			dummy.setGroupName("*");
			SearchCriteria sc = new GroupSearchCriteria(dummy);
			List results = this.userManager.getObjects(sc);
			for (Object obj : results) {
				Group doGroup = (Group) obj;
				groups.add(doGroup.getGroupName());
			}
			return groups;
		} catch (Exception e) {
			logger.error("Error in getting all groups.", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Get all user visiblity groups in the application (filtering out all
	 * groups starting with APP_OWNER).
	 * 
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public List<String> getAllVisibilityGroups()
			throws CaNanoLabSecurityException {
		List<String> groups = getAllGroups();
		// filter out the ones starting with APP_OWNER
		List<String> filteredGroups = new ArrayList<String>();
		List<String> notShownGroups = Arrays
				.asList(CaNanoLabConstants.VISIBLE_GROUPS);
		for (String groupName : groups) {
			if (!notShownGroups.contains(groupName)
					&& !groupName.equals(CaNanoLabConstants.CSM_ADMIN)) {
				filteredGroups.add(groupName);
			}
		}
		return filteredGroups;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public AuthenticationManager getAuthenticationManager() {
		return this.authenticationManager;
	}

	public AuthorizationManager getAuthorizationManager() {
		return this.authorizationManager;
	}

	public UserProvisioningManager getUserManager() {
		return this.userManager;
	}

	/**
	 * Get a Group object for the given groupName.
	 * 
	 * @param groupName
	 * @return
	 */
	public Group getGroup(String groupName) {
		Group group = new Group();
		group.setGroupName(groupName);
		SearchCriteria sc = new GroupSearchCriteria(group);
		List results = this.userManager.getObjects(sc);
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
	 * @throws CaNanoLabSecurityException
	 */
	public void createAGroup(String groupName)
			throws CaNanoLabSecurityException {
		try {
			Group doGroup = getGroup(groupName);
			if (doGroup == null) {
				doGroup = new Group();
				doGroup.setGroupName(groupName);
				this.userManager.createGroup(doGroup);
			}
		} catch (Exception e) {
			logger.error("Error in creating a group.", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Get a Role object for the given roleName.
	 * 
	 * @param roleName
	 * @return
	 */
	public Role getRole(String roleName) {
		Role role = new Role();
		role.setName(roleName);
		SearchCriteria sc = new RoleSearchCriteria(role);
		List results = this.userManager.getObjects(sc);
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
	 * @throws CaNanoLabSecurityException
	 */
	public ProtectionElement getProtectionElement(String objectId)
			throws CaNanoLabSecurityException {
		try {
			ProtectionElement pe = new ProtectionElement();
			pe.setObjectId(objectId);
			pe.setProtectionElementName(objectId);
			SearchCriteria sc = new ProtectionElementSearchCriteria(pe);
			List results = this.userManager.getObjects(sc);
			ProtectionElement doPE = null;
			for (Object obj : results) {
				doPE = (ProtectionElement) obj;
				break;
			}
			if (doPE == null) {
				this.authorizationManager.createProtectionElement(pe);
				return pe;
			}
			return doPE;
		} catch (Exception e) {
			logger.error("Error in creating protection element", e);
			throw new CaNanoLabSecurityException();
		}

	}

	/**
	 * Get a ProtectionGroup object for the given protectionGroupName.
	 * 
	 * @param protectionGroupName
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public ProtectionGroup getProtectionGroup(String protectionGroupName)
			throws CaNanoLabSecurityException {

		ProtectionGroup pg = new ProtectionGroup();
		pg.setProtectionGroupName(protectionGroupName);
		try {
			SearchCriteria sc = new ProtectionGroupSearchCriteria(pg);
			List results = this.userManager.getObjects(sc);
			ProtectionGroup doPG = null;
			for (Object obj : results) {
				doPG = (ProtectionGroup) obj;
				break;
			}
			if (doPG == null) {
				this.userManager.createProtectionGroup(pg);
				return pg;
			}
			return doPG;
		} catch (Exception e) {
			logger.error("Error in getting protection group.", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Assign a ProtectionElement to a ProtectionGroup if not already assigned.
	 * 
	 * @param pe
	 * @param pg
	 * @throws CaNanoLabSecurityException
	 */
	public void assignProtectionElementToProtectionGroup(ProtectionElement pe,
			ProtectionGroup pg) throws CaNanoLabSecurityException {
		try {
			Set<ProtectionGroup> assignedPGs = new HashSet<ProtectionGroup>(
					this.authorizationManager.getProtectionGroups(pe
							.getProtectionElementId().toString()));
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
			this.authorizationManager.assignProtectionElement(pg
					.getProtectionGroupName(), pe.getObjectId());
		} catch (Exception e) {
			logger
					.error(
							"Error in assigning protection element to protection group",
							e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Direct CSM schema query to improve performance. Get the existing role IDs
	 * from database
	 * 
	 * @param objectName
	 * @param groupName
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public List<String> getExistingRoleIds(ProtectionGroup pg, Group group)
			throws CaNanoLabSecurityException {
		List<String> roleIds = new ArrayList<String>();

		String query = "select distinct role_id from csm_user_group_role_pg "
				+ "where protection_group_id=" + pg.getProtectionGroupId()
				+ " and group_id=" + group.getGroupId();
		Session session = null;
		try {
			session = appService.getCurrentSession();
			Transaction tx = session.beginTransaction();
			SQLQuery queryObj = session.createSQLQuery(query);
			queryObj.addScalar("role_id", Hibernate.STRING);
			List results = queryObj.list();
			for (Object obj : results) {
				String roleId = (String) obj;
				roleIds.add(roleId);
			}
			tx.commit();
		} catch (Exception e) {
			logger
					.error("Error in getting existing roles from CSM database",
							e);
			throw new CaNanoLabSecurityException();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return roleIds;
	}

	/**
	 * Get the existing role IDs from database
	 * 
	 * @param objectName
	 * @param groupName
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public List<String> getExistingRoleIdsSlow(ProtectionGroup pg, Group group)
			throws CaNanoLabSecurityException {
		List<String> roleIds = new ArrayList<String>();
		Set existingRoles = null;
		try {
			Set contexts = this.userManager
					.getProtectionGroupRoleContextForGroup(group.getGroupId()
							.toString());
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
		} catch (Exception e) {
			logger.error("Error in getting role IDs", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Assign the given objectName to the given groupName with the given
	 * roleName. Add to existing roles the object has for the group.
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws CaNanoLabSecurityException
	 */
	public void secureObject(String objectName, String groupName,
			String roleName) throws CaNanoLabSecurityException {

		// create protection element
		ProtectionElement pe = getProtectionElement(objectName);

		// create protection group
		ProtectionGroup pg = getProtectionGroup(objectName);

		// assign protection element to protection group if not already
		// exists
		assignProtectionElementToProtectionGroup(pe, pg);

		// get group and role
		Group group = getGroup(groupName);
		Role role = getRole(roleName);

		if (group == null) {
			throw new CaNanoLabSecurityException(
					"No such group defined in CSM: " + groupName);
		}
		if (role == null) {
			throw new CaNanoLabSecurityException(
					"No such role defined in CSM: " + roleName);
		}

		List<String> existingRoleIds = getExistingRoleIds(pg, group);
		List<String> allRoleIds = new ArrayList<String>(existingRoleIds);
		if (!existingRoleIds.contains(role.getId().toString())) {
			allRoleIds.add(role.getId().toString());
		}
		try {
			this.userManager.assignGroupRoleToProtectionGroup(pg
					.getProtectionGroupId().toString(), group.getGroupId()
					.toString(), allRoleIds.toArray(new String[0]));
		} catch (Exception e) {
			logger.error("Error in securing objects", e);
			throw new CaNanoLabSecurityException();
		}
	}

	/**
	 * Get a list of particle the user has read permission on.
	 * 
	 * @param user
	 * @param particles
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public List<ParticleBean> getFilteredParticles(UserBean user,
			List<ParticleBean> particles) throws Exception {

		List<ParticleBean> filteredParticles = new ArrayList<ParticleBean>();

		for (ParticleBean particle : particles) {
			boolean status = checkReadPermission(user, particle
					.getParticleSample().getName());
			if (status)
				filteredParticles.add(particle);
		}
		return filteredParticles;
	}

	/**
	 * Get a list of groups the given object is assgined to with the given role
	 * 
	 * @param objectName
	 * @param roleName
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public List<String> getAccessibleGroupsSlow(String objectName,
			String roleName) throws CaNanoLabSecurityException {
		List<String> groups = new ArrayList<String>();

		List<String> allGroups = getAllGroups();
		Role role = getRole(roleName);
		try {
			for (String groupName : allGroups) {
				Group group = getGroup(groupName);
				Set contexts = this.userManager
						.getProtectionGroupRoleContextForGroup(group
								.getGroupId().toString());
				for (Object obj : contexts) {
					ProtectionGroupRoleContext context = (ProtectionGroupRoleContext) obj;
					ProtectionGroup pg = context.getProtectionGroup();
					Set<Role> roles = new HashSet<Role>(context.getRoles());
					// contains doesn't work because CSM didn't implement
					// hashCode
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
		} catch (Exception e) {
			logger.error("Error in getting accessible groups", e);
			throw new CaNanoLabSecurityException();
		}

	}

	/**
	 * Directly query CSM schema to improve performance
	 * 
	 * @param objectName
	 * @param roleName
	 * @return
	 */
	public List<String> getAccessibleGroups(String objectName, String roleName)
			throws CaNanoLabSecurityException {
		List<String> groups = new ArrayList<String>();

		String query = "select d.group_name group_name from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d	"
				+ "where a.protection_group_id=c.protection_group_id and b.role_id=c.role_id and c.group_id=d.group_id and "
				+ "a.protection_group_name='"
				+ objectName
				+ "' and b.role_name='" + roleName + "'";
		Session session = null;
		try {
			session = appService.getCurrentSession();
			Transaction tx = session.beginTransaction();
			SQLQuery queryObj = session.createSQLQuery(query);
			queryObj.addScalar("group_name", Hibernate.STRING);
			List results = queryObj.list();
			for (Object obj : results) {
				String group = (String) obj;
				groups.add(group);
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Error in getting accessible groups", e);
			throw new CaNanoLabSecurityException();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return groups;
	}

	/**
	 * Remove the group the object is assigned to with the given role.
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws CaNanoLabSecurityException
	 */
	public void removeAccessibleGroupSlow(String objectName, String groupName,
			String roleName) throws CaNanoLabSecurityException {

		Group group = getGroup(groupName);
		Role role = getRole(roleName);
		ProtectionGroup pg = getProtectionGroup(objectName);

		// this method is not implemented in CSM API, try an alternative
		// userManager.removeGroupRoleFromProtectionGroup(pg
		// .getProtectionGroupId().toString(), group.getGroupId()
		// .toString(), new String[] { role.getId().toString() });

		// get existing roles.
		try {
			Set contexts = this.userManager
					.getProtectionGroupRoleContextForGroup(group.getGroupId()
							.toString());
			Set<Role> existingRoles = null;
			for (Object obj : contexts) {
				ProtectionGroupRoleContext context = (ProtectionGroupRoleContext) obj;
				if (context.getProtectionGroup().equals(pg)) {
					existingRoles = new HashSet<Role>(context.getRoles());
					break;
				}
			}
			// remove role from existing roles
			// remove doesn't work because CSM didn't implement hashCode for
			// Role
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
			this.userManager.assignGroupRoleToProtectionGroup(pg
					.getProtectionGroupId().toString(), group.getGroupId()
					.toString(), roleIds);
		} catch (Exception e) {
			logger.error("Error in remove accessible groups", e);
			throw new CaNanoLabSecurityException();
		}

	}

	/**
	 * Direct CSM schema query to improve performance
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws CaNanoLabSecurityException
	 */
	public void removeAccessibleGroup(String objectName, String groupName,
			String roleName) throws CaNanoLabSecurityException {
		Session session = null;
		Transaction tx = null;
		try {
			ProtectionGroup pg = this.getProtectionGroup(objectName);
			Role role = this.getRole(roleName);
			Group group = this.getGroup(groupName);

			String query = "delete from csm_user_group_role_pg "
					+ "where protection_group_id=" + pg.getProtectionGroupId()
					+ "and role_id=" + role.getId() + " and group_id="
					+ group.getGroupId();

			session = appService.getCurrentSession();
			tx = session.beginTransaction();
			Connection connection = session.connection();
			Statement stmt = connection.createStatement();
			stmt.execute(query);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger
					.error("Error getting accessible groups from CSM database",
							e);
			throw new CaNanoLabSecurityException();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * Direct CSM schema query to improve performance
	 * 
	 * @param objectName
	 * @param groupName
	 * @param roleName
	 * @throws CaNanoLabSecurityException
	 */
	public void removeAllAccessibleGroups(String objectName, String roleName,
			String[] exceptionGroupNames) throws CaNanoLabSecurityException {
		Session session = null;
		Transaction tx = null;

		try {

			ProtectionGroup pg = this.getProtectionGroup(objectName);
			Role role = this.getRole(roleName);
			List<String> exceptionGroupIds = new ArrayList<String>();
			if (exceptionGroupNames != null) {
				for (String groupName : exceptionGroupNames) {
					Group group = getGroup(groupName);
					exceptionGroupIds.add(group.getGroupId().toString());
				}
			}

			String query = "delete from csm_user_group_role_pg "
					+ "where protection_group_id=" + pg.getProtectionGroupId()
					+ " and role_id=" + role.getId();
			if (!exceptionGroupIds.isEmpty()) {
				query += " and group_id not in ("
						+ StringUtils.join(exceptionGroupIds, ",") + ")";
			}
			session = appService.getCurrentSession();
			tx = session.beginTransaction();
			Connection connection = session.connection();
			Statement stmt = connection.createStatement();
			stmt.execute(query);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("Error removing accessible groups from CSM database",
					e);
			throw new CaNanoLabSecurityException();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void setVisiblity(String dataToProtect, String[] visibilities)
			throws CaNanoLabSecurityException {
		try {

			removeAllAccessibleGroups(dataToProtect,
					CaNanoLabConstants.CSM_READ_ROLE, null);

			// set new visibilities
			for (String visibility : visibilities) {
				secureObject(dataToProtect, visibility,
						CaNanoLabConstants.CSM_READ_ROLE);
			}

			// set default visibilities
			for (String visibility : CaNanoLabConstants.VISIBLE_GROUPS) {
				secureObject(dataToProtect, visibility,
						CaNanoLabConstants.CSM_READ_ROLE);
			}
		} catch (Exception e) {
			logger.error("Error in setting visibility", e);
			throw new CaNanoLabSecurityException();
		}
	}

	public void assignGroupToProtectionGroupWithRole(String groupName,
			String protectionGroupName, String roleName)
			throws CaNanoLabSecurityException {
		try {
			Role role = getRole(roleName);
			ProtectionGroup pg = getProtectionGroup(protectionGroupName);
			Group group = getGroup(groupName);
			this.userManager.assignGroupRoleToProtectionGroup(pg
					.getProtectionGroupId().toString(), group.getGroupId()
					.toString(), new String[] { role.getId().toString() });
		} catch (Exception e) {
			logger
					.error(
							"Error in assigning group to protection group with role",
							e);
			throw new CaNanoLabSecurityException();
		}
	}
}
