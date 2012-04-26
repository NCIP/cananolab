package gov.nih.nci.cananolab.service.community.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommunityServiceLocalImpl extends BaseServiceLocalImpl implements
		CommunityService {
	private AuthorizationManager authManager;

	public CommunityServiceLocalImpl() throws CommunityException {
		super();
		try {
			this.authManager = SecurityServiceProvider
					.getAuthorizationManager(AccessibilityBean.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error(e);
			throw new CommunityException(e);
		}
	}

	public CommunityServiceLocalImpl(UserBean user) throws CommunityException {
		super(user);
		try {
			this.authManager = SecurityServiceProvider
					.getAuthorizationManager(AccessibilityBean.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error(e);
			throw new CommunityException(e);
		}
	}

	public CommunityServiceLocalImpl(SecurityService securityService)
			throws CommunityException {
		super(securityService);
		try {
			this.authManager = SecurityServiceProvider
					.getAuthorizationManager(AccessibilityBean.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error(e);
			throw new CommunityException(e);
		}
	}

	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException,
			DuplicateEntriesException {
		if (user == null)
			throw new NoAccessException();
		try {
			// creating a new group
			if (StringUtils.isEmpty(collaborationGroup.getId())) {
				// check if group name is already used
				Group doGroup = securityService.getGroup(collaborationGroup
						.getName());
				// if group name already exists by another id
				if (doGroup != null) {
					throw new DuplicateEntriesException(
							"Group name is already in use");
				}
				// create a new group
				else {
					createNewCollaborationGroup(collaborationGroup);
				}
			}
			// update existing group
			else {
				// check if user has access to update the existing group
				// if user has access to update the group
				if (!securityService
						.checkCreatePermission(AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
								+ collaborationGroup.getId()))
					throw new NoAccessException(
							"You don't have access to update the collaboration group");
				updateCollaborationGroup(collaborationGroup);
			}
		} catch (Exception e) {
			String error = "Error finding existing collaboration groups accessible by the user";
			throw new CommunityException(error, e);
		}
	}

	private void createNewCollaborationGroup(
			CollaborationGroupBean collaborationGroup) throws Exception {
		Group doGroup = new Group();
		doGroup.setGroupName(collaborationGroup.getName());
		doGroup.setGroupDesc(collaborationGroup.getDescription());
		authManager.createGroup(doGroup);
		collaborationGroup.setId(doGroup.getGroupId().toString());

		// add owner access to the group
		AccessibilityBean ownerAccess = new AccessibilityBean(
				AccessibilityBean.ACCESS_BY_USER);
		ownerAccess.setUserBean(user);
		ownerAccess.setRoleName(AccessibilityBean.CSM_CURD_ROLE);
		collaborationGroup.getUserAccesses().add(ownerAccess);
		// assign current user to be owner of the collaboration group
		accessUtils.assignOwner(
				AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
						+ doGroup.getGroupId(), user.getLoginName());

		// update current user's associated groups shown on the side menu
		if (!user.getGroupNames().contains(collaborationGroup.getName())) {
			user.getGroupNames().add(collaborationGroup.getName());
		}

		// add users to the collaboration group
		addUsersToCollaborationGroup(collaborationGroup.getId(),
				collaborationGroup.getUserAccesses());

		// add curator default CURD access to the group
		saveAccessibility(
				AccessibilityBean.CSM_DEFAULT_ACCESS,
				AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
						+ doGroup.getGroupId());
		collaborationGroup.getGroupAccesses().add(
				AccessibilityBean.CSM_DEFAULT_ACCESS);
	}

	private void updateCollaborationGroup(
			CollaborationGroupBean collaborationGroup) throws Exception {
		// get existing database group based on the group id
		Group doGroup = authManager.getGroupById(collaborationGroup.getId());
		String oldGroupName = doGroup.getGroupName();

		// if either name or description is changed, update database
		// record with updated info
		if (!doGroup.getGroupName().equals(collaborationGroup.getName())
				|| !doGroup.getGroupDesc().equals(
						collaborationGroup.getDescription())) {

			doGroup.setGroupName(collaborationGroup.getName());
			doGroup.setGroupDesc(collaborationGroup.getDescription());
			authManager.modifyGroup(doGroup);
		}

		// remove old group name from current user's groups shown on the
		// side menu if the name changed
		if (user.getGroupNames() != null
				&& !oldGroupName.equals(collaborationGroup.getName())) {
			user.getGroupNames().remove(oldGroupName);
			// add the new one if user is not a curator
			if (!user.isCurator()) {
				user.getGroupNames().add(collaborationGroup.getName());
			}
		}

		// update user access
		CollaborationGroupBean existingGroupWithAccess = findCollaborationGroupById(collaborationGroup
				.getId());
		// remove the ones in the database that are not in the current
		// user list
		List<AccessibilityBean> accessToRemove = new ArrayList<AccessibilityBean>(
				existingGroupWithAccess.getUserAccesses());
		accessToRemove.removeAll(collaborationGroup.getUserAccesses());
		removeUsersFromCollaborationGroup(collaborationGroup.getId(),
				accessToRemove);

		// get new access to add
		List<AccessibilityBean> accessToAdd = new ArrayList<AccessibilityBean>(
				collaborationGroup.getUserAccesses());
		accessToAdd.removeAll(existingGroupWithAccess.getUserAccesses());
		// add new user accesses
		addUsersToCollaborationGroup(collaborationGroup.getId(), accessToAdd);
	}

	private void addUsersToCollaborationGroup(String groupId,
			List<AccessibilityBean> userAccesses) throws Exception {
		// add users to the collaboration group
		List<String> userIds = new ArrayList<String>();
		for (AccessibilityBean access : userAccesses) {
			// skip if user is curator because curator is treated as a default
			// group access
			if (!access.getUserBean().isCurator()) {
				saveAccessibility(access,
						AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
								+ groupId);
			}
			User user = authManager
					.getUser(access.getUserBean().getLoginName());
			String userId = user.getUserId().toString();
			userIds.add(userId);
		}
		if (!userIds.isEmpty())
			authManager
					.addUsersToGroup(groupId, userIds.toArray(new String[0]));

	}

	private void removeUsersFromCollaborationGroup(String groupId,
			List<AccessibilityBean> userAccesses) throws Exception {
		for (AccessibilityBean access : userAccesses) {
			authManager.removeUserFromGroup(groupId, access.getUserBean()
					.getUserId());
			accessUtils.removeSecureObjectForUser(
					AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX + groupId,
					access.getUserBean().getLoginName(),
					AccessibilityBean.CSM_CURD_ROLE);
		}
	}

	public List<CollaborationGroupBean> findCollaborationGroups()
			throws CommunityException {

		List<CollaborationGroupBean> collaborationGroups = new ArrayList<CollaborationGroupBean>();
		try {
			Group dummy = new Group();
			dummy.setGroupName("*");
			SearchCriteria sc = new GroupSearchCriteria(dummy);
			List results = authManager.getObjects(sc);
			for (Object obj : results) {
				Group doGroup = (Group) obj;
				CollaborationGroupBean cGroup = new CollaborationGroupBean(
						doGroup);
				if (securityService
						.checkCreatePermission(AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
								+ cGroup.getId())) {
					setAccesses(cGroup);
					collaborationGroups.add(cGroup);
				}
			}

		} catch (Exception e) {
			String error = "Error finding existing collaboration groups accessible by the user";
			throw new CommunityException(error, e);
		}
		return collaborationGroups;
	}

	public void assignOwner(String collaborationGroupId, String ownerLogin)
			throws CommunityException, NoAccessException {
		// user needs to be both curator and admin
		if (!(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		try {
			this.accessUtils.assignOwner(
					AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
							+ collaborationGroupId, ownerLogin);
			// add new owner to the colloborationGroup
			User user = authManager.getUser(ownerLogin);
			authManager.addUsersToGroup(collaborationGroupId,
					new String[] { user.getUserId().toString() });
			// if ownerLogin is not a curator, save owner access

		} catch (Exception e) {
			String error = "Error assigning an owner to the collaboration group by Id "
					+ collaborationGroupId;
			logger.error(error, e);
			throw new CommunityException(error, e);
		}
	}

	// set user accessibilities
	private void setAccesses(CollaborationGroupBean cGroup) throws Exception {
		// set user accessibilities
		Set<User> users = authManager.getUsers(cGroup.getId().toString());
		List<String> userNames = new ArrayList<String>(users.size());
		for (User user : users) {
			userNames.add(user.getLoginName());
		}
		Map<String, String> userRoles = securityService
				.getAllUserRoles(AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
						+ cGroup.getId());
		List<AccessibilityBean> userAccesses = new ArrayList<AccessibilityBean>();
		for (User aUser : users) {
			if (userRoles.get(aUser.getLoginName()) != null) {
				AccessibilityBean accessibility = new AccessibilityBean(
						AccessibilityBean.ACCESS_BY_USER);
				accessibility.setGroupName(cGroup.getName());
				accessibility.setRoleName(userRoles.get(aUser.getLoginName()));
				accessibility.setUserBean(new UserBean(aUser));
				userAccesses.add(accessibility);
			}
		}
		cGroup.setUserAccesses(userAccesses);
		List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();
		if (user.isCurator()) {
			groupAccesses.add(AccessibilityBean.CSM_DEFAULT_ACCESS);
			cGroup.setGroupAccesses(groupAccesses);
		}
		cGroup.setUser(user);
		List<String> ownerNames = accessUtils
				.getOwnerNames(AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
						+ cGroup.getId());
		cGroup.setOwnerName(StringUtils.join(ownerNames, ","));
	}

	public CollaborationGroupBean findCollaborationGroupById(String id)
			throws CommunityException {
		CollaborationGroupBean collaborationGroup = null;
		try {
			Group group = authManager.getGroupById(id);
			collaborationGroup = new CollaborationGroupBean(group);
			String pe = AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
					+ collaborationGroup.getId();
			if (securityService.checkCreatePermission(pe)) {
				setAccesses(collaborationGroup);
			} else {
				String error = "User has no access to the collaboration group "
						+ collaborationGroup.getName();
				logger.info(error);
			}
		} catch (Exception e) {
			String error = "Error retrieving the collaboration group by name";
			throw new CommunityException(error, e);
		}
		return collaborationGroup;
	}

	public void deleteCollaborationGroup(
			CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			// check if user has access to delete the group
			if (!securityService
					.checkDeletePermission(AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
							+ collaborationGroup.getId())) {
				throw new NoAccessException();
			} else {
				super.removeAllAccesses(AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
						+ collaborationGroup.getId());
				authManager.removeGroup(collaborationGroup.getId());
				// update user group list
				user.getGroupNames().remove(collaborationGroup.getName());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error deleting the collaboration group";
			throw new CommunityException(error, e);
		}
	}

	public List<String> findCollaborationGroupIdsByOwner(String owner)
			throws CommunityException {
		List<String> groupIds = new ArrayList<String>();
		try {
			User user = authManager.getUser(owner);
			user.getUserId();
			Set groups = authManager.getGroups(user.getUserId().toString());
			for (Object obj : groups) {
				Group group = (Group) obj;
				String groupId = group.getGroupId().toString();
				if (securityService.isOwner(owner,
						AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
								+ groupId)) {
					groupIds.add(groupId);
				}
			}
		} catch (Exception e) {
			String error = "Error finding CollaborationGroupByOwner: " + owner;
			logger.error(error, e);
			throw new CommunityException(error, e);
		}

		return groupIds;
	}

	public void assignAccessibility(AccessibilityBean access,
			String collaborationGroupId) throws CommunityException,
			NoAccessException {
		try {
			if (!user.isCurator()
					&& !securityService.isOwner(user.getLoginName(),
							AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
									+ collaborationGroupId)) {
				throw new NoAccessException();
			}
			this.saveAccessibility(access,
					AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
							+ collaborationGroupId);
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error assigning accessibility to collaboration group: "
					+ collaborationGroupId;
			throw new CommunityException(error, e);
		}
	}

	public void removeAccessibility(AccessibilityBean access,
			String collaborationGroupId) throws CommunityException,
			NoAccessException {
		try {
			if (!user.isCurator()
					&& !securityService.isOwner(user.getLoginName(),
							AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
									+ collaborationGroupId)) {
				throw new NoAccessException();
			}
			this.deleteAccessibility(access,
					AccessibilityBean.CSM_COLLABORATION_GROUP_PREFIX
							+ collaborationGroupId);
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error removing accessibility to collaboration group: "
					+ collaborationGroupId;
			throw new CommunityException(error, e);
		}
	}
}