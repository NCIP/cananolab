package gov.nih.nci.cananolab.service.security;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

/**
 * This class extends CSM API with convenient methods to query access control information
 *
 * @author Pansu
 *
 */
public class SecurityService {
	private Logger logger = Logger.getLogger(SecurityService.class);

	private AuthenticationManager authenticationManager = null;

	private AuthorizationManager authorizationManager = null;

	private String applicationName = null;

	private UserBean userBean;

	public SecurityService(String applicationName) throws SecurityException {
		try {
			this.applicationName = applicationName;
			this.authenticationManager = SecurityServiceProvider
					.getAuthenticationManager(applicationName);
			this.authorizationManager = SecurityServiceProvider
					.getAuthorizationManager(applicationName);
		} catch (Exception e) {
			logger.error(e);
			throw new SecurityException(e);
		}
	}

	public SecurityService(String applicationName, UserBean userBean)
			throws SecurityException {
		this(applicationName);
		if (userBean != null) {
			try {
				this.userBean = login(userBean.getLoginName(), userBean
						.getPassword());
				// if user is not in Public group, add the user
				if (!this.userBean.getGroupNames().contains(
						AccessibilityBean.CSM_PUBLIC_GROUP)) {
					this.assignUserToGroup(AccessibilityBean.CSM_PUBLIC_GROUP);
					this.userBean.getGroupNames().add(
							AccessibilityBean.CSM_PUBLIC_GROUP);
				}
			} catch (Exception e) {
				logger.error(e);
				throw new SecurityException(e);
			}
		}
	}

	public UserBean getUserBean() {
		return userBean;
	}

	/**
	 * Uses CSM to authenticate the given user name and password.
	 *
	 * @return
	 * @throws SecurityException
	 */
	public UserBean login(String loginName, String password)
			throws SecurityException, InvalidSessionException {
		try {
			boolean authenticated = authenticationManager.login(loginName,
					password);
			if (authenticated) {
				User user = authorizationManager.getUser(loginName);
				UserBean userBean = new UserBean(user);
				if (isAdmin(user.getLoginName())) {
					userBean.setAdmin(true);
				}
				if (isCurator(user.getLoginName())) {
					userBean.setCurator(true);
				}
				return userBean;
			} else {
				throw new SecurityException("Invalid Credentials");
			}
		} catch (Exception e) {
			String error = "Error in logging";
			logger.error(error);
			throw new SecurityException(error, e);
		}
	}

	/**
	 * Check whether the given userBean is the admin of the application.
	 *
	 * @param userBean
	 * @return
	 */
	public boolean isAdmin(String loginName) {
		boolean adminStatus = this.authorizationManager.checkOwnership(
				loginName, this.applicationName);
		return adminStatus;
	}

	/**
	 * Check whether the given userBean is the admin of the application.
	 *
	 * @param userBean
	 * @return
	 */
	public boolean isCurator(String loginName) {
		User dbUser = this.authorizationManager.getUser(loginName);
		if (dbUser != null) {
			try {
				SortedSet<String> groupNames = this.getUserGroupNames(dbUser
						.getUserId().toString());
				if (groupNames.contains(AccessibilityBean.CSM_DATA_CURATOR)) {
					return true;
				}
			} catch (Exception e) {
				logger.info("no groups were found for user.");
				return false;
			}
		}
		return false;
	}

	private SortedSet<String> getUserGroupNames(String userId)
			throws SecurityException {
		SortedSet<String> groupNames = new TreeSet<String>();
		try {
			Set groups = this.authorizationManager.getGroups(userId);
			for (Object obj : groups) {
				Group group = (Group) obj;
				groupNames.add(group.getGroupName());
			}
		} catch (Exception e) {
			logger.error("Error in getting the groups user is in.", e);
			throw new SecurityException();
		}
		return groupNames;
	}

	private void assignUserToGroup(String groupName) throws SecurityException {
		try {
			Group group = this.getGroup(AccessibilityBean.CSM_PUBLIC_GROUP);
			this.authorizationManager.addUsersToGroup(group.getGroupId()
					.toString(), new String[] { userBean.getUserId() });
		} catch (Exception e) {
			logger.error("Error in assigning user to group.", e);
			throw new SecurityException();
		}
	}

	/**
	 * Set a new password for the given userBean login name
	 *
	 * @param loginName
	 * @param newPassword
	 * @throws SecurityException
	 */
	public void updatePassword(String newPassword) throws SecurityException {
		try {
			if (userBean != null) {
				userBean.getDomain().setPassword(newPassword);
				authorizationManager.modifyUser(userBean.getDomain());
			} else {
				throw new NoAccessException("You need to login first");
			}
		} catch (Exception e) {
			logger.error("Error in updating password.", e);
			throw new SecurityException();
		}
	}

	/**
	 * Check whether the given userBean has the given privilege on the given
	 * protection element
	 *
	 * @param protectionElementObjectId
	 * @param privilege
	 * @return
	 * @throws SecurityException
	 */
	private boolean checkPermission(String protectionElementObjectId,
			String privilege) throws SecurityException {
		try {
			boolean status = false;
			if (userBean == null) {
				return status;
			}
			status = this.authorizationManager.checkPermission(userBean
					.getLoginName(), protectionElementObjectId, privilege);
			return status;
		} catch (Exception e) {
			logger.error("Error in checking userBean permission.", e);
			throw new SecurityException();
		}
	}

	public boolean checkCreatePermission(String protectionElementObjectId)
			throws SecurityException {
		return checkPermission(protectionElementObjectId,
				AccessibilityBean.CSM_CREATE_PRIVILEGE);
	}

	/**
	 * Check whether the given userBean has execute privilege on the given
	 * protection element
	 *
	 * @param protectionElementObjectId
	 * @return
	 * @throws SecurityException
	 */
	public boolean checkExecutePermission(String protectionElementObjectId)
			throws SecurityException {
		return checkPermission(protectionElementObjectId,
				AccessibilityBean.CSM_EXECUTE_PRIVILEGE);
	}

	/**
	 * Check whether the given userBean has read privilege on the given
	 * protection element
	 *
	 * @param protectionElementObjectId
	 * @return
	 * @throws SecurityException
	 */
	public boolean checkReadPermission(String protectionElementObjectId)
			throws Exception {
		if (protectionElementObjectId == null) {
			return false;
		}
		boolean publicStatus = isPublic(protectionElementObjectId);
		if (publicStatus) {
			return true;
		} else {
			return checkPermission(protectionElementObjectId,
					AccessibilityBean.CSM_READ_PRIVILEGE);
		}
	}

	/**
	 * Check whether the given userBean has delete privilege on the given
	 * protection element
	 *
	 * @param protectionElementObjectId
	 * @return
	 * @throws SecurityException
	 */
	public boolean checkDeletePermission(String protectionElementObjectId)
			throws SecurityException {
		return checkPermission(protectionElementObjectId,
				AccessibilityBean.CSM_DELETE_PRIVILEGE);
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
		List results = this.authorizationManager.getObjects(sc);
		Group doGroup = null;
		for (Object obj : results) {
			doGroup = (Group) obj;
			break;
		}
		return doGroup;
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
		List results = this.authorizationManager.getObjects(sc);
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
	 * @throws SecurityException
	 */
	public ProtectionElement getProtectionElement(String objectId)
			throws SecurityException {
		try {
			ProtectionElement pe = new ProtectionElement();
			pe.setObjectId(objectId);
			pe.setProtectionElementName(objectId);
			SearchCriteria sc = new ProtectionElementSearchCriteria(pe);
			List results = this.authorizationManager.getObjects(sc);
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
			throw new SecurityException();
		}

	}

	/**
	 * Get a ProtectionGroup object for the given protectionGroupName.
	 *
	 * @param protectionGroupName
	 * @return
	 * @throws SecurityException
	 */
	public ProtectionGroup getProtectionGroup(String protectionGroupName)
			throws SecurityException {

		ProtectionGroup pg = new ProtectionGroup();
		pg.setProtectionGroupName(protectionGroupName);
		try {
			SearchCriteria sc = new ProtectionGroupSearchCriteria(pg);
			List results = this.authorizationManager.getObjects(sc);
			ProtectionGroup doPG = null;
			for (Object obj : results) {
				doPG = (ProtectionGroup) obj;
				break;
			}
			if (doPG == null) {
				this.authorizationManager.createProtectionGroup(pg);
				return pg;
			}
			return doPG;
		} catch (Exception e) {
			logger.error("Error in getting protection group.", e);
			throw new SecurityException();
		}
	}

	public String[] getAccessibleGroups(String objectName, String privilegeName)
			throws SecurityException {
		List<String> groupNames = new ArrayList<String>();
		try {
			List groups = authorizationManager.getAccessibleGroups(objectName,
					privilegeName);
			if (groups != null)
				for (Object obj : groups) {
					Group group = (Group) obj;
					groupNames.add(group.getGroupName());
				}
		} catch (Exception e) {
			logger.error("Error in getting accessible groups", e);
			throw new SecurityException();
		}
		return groupNames.toArray(new String[0]);
	}

	public void updateDatabaseConnectionForCSMApplications(String dbDialect,
			String dbDriver, String dbURL, String dbUserName, String dbPassword)
			throws SecurityException {
		try {
			if (userBean != null && userBean.isAdmin()) {
				Application caNanoLabApp = authorizationManager
						.getApplication(AccessibilityBean.CSM_APP_NAME);
				caNanoLabApp.setDatabaseURL(dbURL);
				caNanoLabApp.setDatabaseDialect(dbDialect);
				caNanoLabApp.setDatabaseDriver(dbDriver);
				caNanoLabApp.setDatabaseUserName(dbUserName);
				caNanoLabApp.setDatabasePassword(dbPassword);
				authorizationManager.modifyApplication(caNanoLabApp);

				Application csmupt = authorizationManager
						.getApplication("csmupt");
				csmupt.setDatabaseURL(dbURL);
				csmupt.setDatabaseDialect(dbDialect);
				csmupt.setDatabaseDriver(dbDriver);
				csmupt.setDatabaseUserName(dbUserName);
				csmupt.setDatabasePassword(dbPassword);
				authorizationManager.modifyApplication(csmupt);
			} else {
				throw new NoAccessException("You need to be a curator.");
			}
		} catch (Exception e) {
			String error = "Can't update database connections for CSM applications";
			logger.error(error);
			throw new SecurityException(error, e);
		}
	}

	public boolean isPublic(String dataId) throws Exception {
		boolean status = false;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			String query = "select a.protection_group_name protection_group_name from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d	"
					+ "where a.protection_group_id=c.protection_group_id and b.role_id=c.role_id and c.group_id=d.group_id and "
					+ "d.group_name='"
					+ AccessibilityBean.CSM_PUBLIC_GROUP
					+ "' and b.role_name='"
					+ AccessibilityBean.CSM_READ_ROLE
					+ "'" + " and protection_group_name='" + dataId + "'";
			String[] columns = new String[] { "protection_group_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING };
			List results = appService.directSQL(query, columns, columnTypes);
			if (results.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			String err = "Could not execute direct sql query to check whether data is public.";
			logger.error(err);
			throw new SecurityException(err, e);
		}
	}

	/**
	 * Return a list of data (csm protected_group_name) accessible to the
	 * userBean in the database (R, CUR and CURD roles)
	 *
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllUserAccessibleData() throws ApplicationException {
		List<String> data = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (userBean == null) {
				return new ArrayList<String>(appService.getAllPublicData());
			}
			String query1 = "SELECT DISTINCT pg.protection_group_name "
					+ "FROM csm_user_group_role_pg ugrp, "
					+ "csm_protection_group pg, "
					+ "csm_user u, "
					+ "csm_role r "
					+ "WHERE     ugrp.protection_group_id = pg.protection_group_id "
					+ "AND ugrp.role_id = r.role_id "
					+ "AND ugrp.user_id = u.user_id " + "AND u.login_name = '"
					+ userBean.getLoginName() + "' " + "AND r.role_name IN ('"
					+ AccessibilityBean.CSM_READ_ROLE + "', '"
					+ AccessibilityBean.CSM_CUR_ROLE + "', '"
					+ AccessibilityBean.CSM_CURD_ROLE + "')";

			String query2 = "select distinct pg.protection_group_name  "
					+ "from csm_user_group_role_pg ugrp, csm_protection_group pg, csm_user u, csm_group g, csm_user_group ug, csm_role r "
					+ "where ugrp.protection_group_id=pg.protection_group_id "
					+ "and ugrp.group_id=g.group_id "
					+ "and ugrp.role_id=r.role_id "
					+ "and ug.user_id=u.user_id "
					+ "and ug.group_id=g.group_id " + "and u.login_name='"
					+ userBean.getLoginName() + "' " + "and r.role_name in ('"
					+ AccessibilityBean.CSM_READ_ROLE + "', '"
					+ AccessibilityBean.CSM_CUR_ROLE + "', '"
					+ AccessibilityBean.CSM_CURD_ROLE + "')";
			String query = query1 + " union " + query2;
			String[] columns = new String[] { "protection_group_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING };
			List results = appService.directSQL(query, columns, columnTypes);
			for (Object obj : results) {
				if (obj != null) {
					data.add(((String) obj));
				}
			}
		} catch (Exception e) {
			String err = "Could not execute direct sql query to find all userBean accessible data";
			logger.error(err);
			throw new ApplicationException(err, e);
		}
		return data;
	}

	/**
	 * Return a map of data (csm protected_group_name) against roles accessible
	 * to the userBean in the database
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllUserAccessibleDataAndRole()
			throws ApplicationException {
		Map<String, String> data2role = new HashMap<String, String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (userBean == null) {
				for (String data : appService.getAllPublicData()) {
					data2role.put(data, AccessibilityBean.CSM_READ_ROLE);
				}
				return data2role;
			}
			String query1 = "SELECT DISTINCT pg.protection_group_name, r.role_name "
					+ "FROM csm_user_group_role_pg ugrp, "
					+ "csm_protection_group pg, "
					+ "csm_user u, "
					+ "csm_role r "
					+ "WHERE     ugrp.protection_group_id = pg.protection_group_id "
					+ "AND ugrp.role_id = r.role_id "
					+ "AND ugrp.user_id = u.user_id "
					+ "AND u.login_name = '"
					+ userBean.getLoginName()
					+ "' "
					+ "AND r.role_name IN ('"
					+ AccessibilityBean.CSM_READ_ROLE
					+ "', '"
					+ AccessibilityBean.CSM_CUR_ROLE
					+ "', '"
					+ AccessibilityBean.CSM_CURD_ROLE + "')";

			String query2 = "select distinct pg.protection_group_name, r.role_name  "
					+ "from csm_user_group_role_pg ugrp, csm_protection_group pg, csm_user u, csm_group g, csm_user_group ug, csm_role r "
					+ "where ugrp.protection_group_id=pg.protection_group_id "
					+ "and ugrp.group_id=g.group_id "
					+ "and ugrp.role_id=r.role_id "
					+ "and ug.user_id=u.user_id "
					+ "and ug.group_id=g.group_id "
					+ "and u.login_name='"
					+ userBean.getLoginName()
					+ "' "
					+ "and r.role_name in ('"
					+ AccessibilityBean.CSM_READ_ROLE
					+ "', '"
					+ AccessibilityBean.CSM_CUR_ROLE
					+ "', '"
					+ AccessibilityBean.CSM_CURD_ROLE + "')";
			String query = query1 + " union " + query2;
			String[] columns = new String[] { "protection_group_name",
					"role_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING,
					Hibernate.STRING };
			List results = appService.directSQL(query, columns, columnTypes);
			for (Object obj : results) {
				if (obj != null) {
					String[] row = (String[]) obj;
					String data = row[0];
					String role = row[1];
					data2role.put(data, role);
				}
			}
		} catch (Exception e) {
			String err = "Could not execute direct sql query to find all userBean accessible data";
			logger.error(err);
			throw new ApplicationException(err, e);
		}
		return data2role;
	}

	public Map<String, List<String>> getPrivilegeMap(List<String> protectedData)
			throws SecurityException {
		Map<String, List<String>> privilegeMap = new HashMap<String, List<String>>();
		try {
			if (userBean == null) {
				throw new NoAccessException();
			}
			List<ProtectionElement> pes = new ArrayList<ProtectionElement>();
			for (String item : protectedData) {
				ProtectionElement pe = getProtectionElement(item);
				pes.add(pe);
			}
			Collection<ObjectPrivilegeMap> opms = authorizationManager
					.getPrivilegeMap(userBean.getLoginName(), pes);

			List<String> privileges = null;
			for (ObjectPrivilegeMap pm : opms) {
				String pe = pm.getProtectionElement()
						.getProtectionElementName();
				privileges = new ArrayList<String>();
				for (Object priv : pm.getPrivileges()) {
					privileges.add(((Privilege) priv).getName());
				}
				privilegeMap.put(pe, privileges);
			}
		} catch (Exception e) {
			String error = "Error getting privilege map for user and data";
			throw new SecurityException(error, e);
		}
		return privilegeMap;
	}

	public Map<String, String> getAllUserRoles(String protectedData)
			throws SecurityException {
		String query = "select distinct u.login_name, r.role_name "
				+ "from csm_user_group_role_pg ugrp, csm_protection_group pg, csm_user u, csm_role r "
				+ "where ugrp.protection_group_id=pg.protection_group_id  "
				+ "and ugrp.user_id=u.user_id " + "and ugrp.role_id=r.role_id "
				+ "and pg.protection_group_name='" + protectedData + "'";
		Map<String, String> user2Role = new HashMap<String, String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			String[] columns = new String[] { "login_name", "role_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING,
					Hibernate.STRING };
			List results = appService.directSQL(query, columns, columnTypes);
			for (Object obj : results) {
				Object[] row = (Object[]) obj;
				String user = row[0].toString();
				String role = row[1].toString();
				user2Role.put(user, role);
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in getting existing userBean access for the given data from CSM database",
							e);
			throw new SecurityException();
		}
		return user2Role;
	}

	public Map<String, String> getAllGroupRoles(String protectedData)
			throws SecurityException {
		String query = "select distinct g.group_name, r.role_name "
				+ "from csm_user_group_role_pg ugrp, csm_protection_group pg, csm_group g, csm_role r "
				+ "where ugrp.protection_group_id=pg.protection_group_id  "
				+ "and ugrp.group_id=g.group_id "
				+ "and ugrp.role_id=r.role_id "
				+ "and pg.protection_group_name='" + protectedData + "'";
		Map<String, String> group2Role = new HashMap<String, String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			String[] columns = new String[] { "group_name", "role_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING,
					Hibernate.STRING };
			List results = appService.directSQL(query, columns, columnTypes);
			for (Object obj : results) {
				Object[] row = (Object[]) obj;
				String group = row[0].toString();
				String role = row[1].toString();
				group2Role.put(group, role);
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in getting existing group access for the given data from CSM database",
							e);
			throw new SecurityException();
		}
		return group2Role;
	}

	public String getUserRole(String protectedData, String userLoginName)
			throws SecurityException {
		String query = "select distinct r.role_name "
				+ "from csm_user_group_role_pg ugrp, csm_protection_group pg, csm_user u, csm_role r "
				+ "where ugrp.protection_group_id=pg.protection_group_id  "
				+ "and ugrp.user_id=u.user_id " + "and ugrp.role_id=r.role_id "
				+ "and u.login_name='" + userLoginName + "' "
				+ "and pg.protection_group_name='" + protectedData + "'";
		String roleName = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			String[] columns = new String[] { "role_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING };
			List results = appService.directSQL(query, columns, columnTypes);
			for (Object obj : results) {
				roleName = obj.toString();
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in getting existing role for the given data and given userBean name from CSM database",
							e);
			throw new SecurityException();
		}
		return roleName;
	}

	public String getGroupRole(String protectedData, String groupName)
			throws SecurityException {
		String query = "select distinct r.role_name "
				+ "from csm_user_group_role_pg ugrp, csm_protection_group pg, csm_group g, csm_role r "
				+ "where ugrp.protection_group_id=pg.protection_group_id  "
				+ "and ugrp.group_id=g.group_id "
				+ "and ugrp.role_id=r.role_id " + "and g.group_name='"
				+ groupName + "' " + "and pg.protection_group_name='"
				+ protectedData + "'";
		String roleName = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			String[] columns = new String[] { "role_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING };
			List results = appService.directSQL(query, columns, columnTypes);
			for (Object obj : results) {
				roleName = obj.toString();
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in getting existing role for the given data and given group name from CSM database",
							e);
			throw new SecurityException();
		}

		return roleName;
	}

	public Boolean isOwner(String protectedData) throws SecurityException {
		Boolean isOwner = false;
		try {
			ProtectionElement pe = this.getProtectionElement(protectedData);
			isOwner = authorizationManager.checkOwnership(userBean
					.getLoginName(), pe.getObjectId());
		} catch (Exception e) {
			logger.error("Error in assigning an owner", e);
			throw new SecurityException();
		}
		return isOwner;
	}

	public List<String> getUserNames(String groupName) throws SecurityException {
		List<String> userNames = new ArrayList<String>();
		try {
			Group group = this.getGroup(groupName);
			Set users = authorizationManager.getUsers(group.getGroupId()
					.toString());
			for (Object obj : users) {
				User user = (User) obj;
				userNames.add(user.getLoginName());
			}
		} catch (Exception e) {
			logger.error("Error in getting users of the given group", e);
			throw new SecurityException();
		}
		return userNames;
	}

	public Boolean isGroupAccessibleByUser(String groupName) throws Exception {
		Group group = this.getGroup(groupName);
		if (group == null) {
			return false;
		} else if (checkReadPermission(AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
				+ group.getGroupId())) {
			return true;
		}
		return false;
	}

	public Boolean isUserValid(String userLoginName) throws Exception {
		User user = authorizationManager.getUser(userLoginName);
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
			service.updateDatabaseConnectionForCSMApplications(args[0],
					args[1], args[2], args[3], args[4]);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
