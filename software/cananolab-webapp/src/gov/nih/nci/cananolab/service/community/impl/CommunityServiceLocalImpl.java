package gov.nih.nci.cananolab.service.community.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class CommunityServiceLocalImpl extends BaseServiceLocalImpl implements
		CommunityService {
	public CommunityServiceLocalImpl() {
		super();
	}

	public CommunityServiceLocalImpl(UserBean user) {
		super(user);
	}

	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException,
			DuplicateEntriesException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			List<AccessibilityBean> accessibilities = collaborationGroup
					.getUserAccessibilities();
			AuthorizationManager authManager = authService
					.getAuthorizationManager();
			Group doGroup = authService.getGroup(collaborationGroup.getName());
			// group name already exists by another id
			if (doGroup != null
					&& !doGroup.getGroupId().toString().equals(
							collaborationGroup.getId())) {
				throw new DuplicateEntriesException(
						"Group name is already in use.");
			}
			// create a new group if none exists.
			if (StringUtils.isEmpty(collaborationGroup.getId())) {
				doGroup = new Group();
				doGroup.setGroupName(collaborationGroup.getName());
				doGroup.setGroupDesc(collaborationGroup.getDescription());
				authManager.createGroup(doGroup);

				// assign CURD access to user who created the group
				AccessibilityBean ownerAccess = new AccessibilityBean(false);
				ownerAccess.setUserBean(user);
				ownerAccess.setRoleName(Constants.CSM_CURD_ROLE);
				saveAccessibility(ownerAccess,
						Constants.CSM_COLLABORATION_GROUP_PREFIX
								+ doGroup.getGroupId());
				// assign CURD access to Curator group
				saveAccessibility(Constants.CSM_DEFAULT_ACCESS,
						Constants.CSM_COLLABORATION_GROUP_PREFIX
								+ doGroup.getGroupId());
			}
			// update existing group
			else {
				if (doGroup == null) {
					doGroup = authService.getAuthorizationManager()
							.getGroupById(collaborationGroup.getId());
					doGroup.setGroupName(collaborationGroup.getName());
				}
				doGroup.setGroupDesc(collaborationGroup.getDescription());
				authManager.modifyGroup(doGroup);
				CollaborationGroupBean existingGroup = findCollaborationGroupById(collaborationGroup
						.getId());
				// update user access
				List<AccessibilityBean> existingAccess = existingGroup
						.getUserAccessibilities();
				List<AccessibilityBean> accessToRemove = new ArrayList<AccessibilityBean>(
						existingAccess);
				accessToRemove.removeAll(accessibilities);
				for (AccessibilityBean access : accessToRemove) {
					authManager.removeUserFromGroup(doGroup.getGroupId()
							.toString(), access.getUserBean().getUserId());
					authService.removeSecureObjectForUser(
							Constants.CSM_COLLABORATION_GROUP_PREFIX
									+ doGroup.getGroupId(), access
									.getUserBean().getLoginName(),
							Constants.CSM_CURD_ROLE);
				}
			}
			// check if user has access to update the group
			if (authService.checkCreatePermission(user,
					Constants.CSM_COLLABORATION_GROUP_PREFIX
							+ doGroup.getGroupId())) {
				String[] userIds = new String[accessibilities.size() + 1];
				int i = 0;
				for (AccessibilityBean access : accessibilities) {
					saveAccessibility(access,
							Constants.CSM_COLLABORATION_GROUP_PREFIX
									+ doGroup.getGroupId());
					User user = authManager.getUser(access.getUserBean()
							.getLoginName());
					String userId = user.getUserId().toString();
					userIds[i] = userId;
					i++;
				}
				// add current user to the group
				userIds[i] = user.getUserId();
				authManager.addUsersToGroup(doGroup.getGroupId().toString(),
						userIds);
			} else {
				throw new NoAccessException(
						"Collaboration Group already exists and you don't have access to update it");
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error finding existing collaboration groups accessible by the user";
			throw new CommunityException(error, e);
		}
	}

	public List<CollaborationGroupBean> findCollaborationGroups()
			throws CommunityException {

		List<CollaborationGroupBean> collaborationGroups = new ArrayList<CollaborationGroupBean>();
		try {
			AuthorizationManager authManager = authService
					.getAuthorizationManager();
			Group dummy = new Group();
			dummy.setGroupName("*");
			SearchCriteria sc = new GroupSearchCriteria(dummy);
			List results = authManager.getObjects(sc);
			for (Object obj : results) {
				Group doGroup = (Group) obj;
				CollaborationGroupBean cGroup = new CollaborationGroupBean(
						doGroup);
				if (authService.checkCreatePermission(user,
						Constants.CSM_COLLABORATION_GROUP_PREFIX
								+ cGroup.getId())) {
					setUserAccesses(cGroup);
					collaborationGroups.add(cGroup);
				} else {
					String error = "User has no access to the collaboration group "
							+ cGroup.getName();
					logger.info(error);
				}
			}

		} catch (Exception e) {
			String error = "Error finding existing collaboration groups accessible by the user";
			throw new CommunityException(error, e);
		}
		return collaborationGroups;
	}

	// set user accessibilities
	private void setUserAccesses(CollaborationGroupBean cGroup)
			throws Exception {
		AuthorizationManager authManager = authService
				.getAuthorizationManager();
		// set user accessibilities
		Set<User> users = authManager.getUsers(cGroup.getId().toString());
		List<String> userNames = new ArrayList<String>(users.size());
		for (User user : users) {
			userNames.add(user.getLoginName());
		}
		Map<String, String> userRoles = authService
				.getUserRoles(Constants.CSM_COLLABORATION_GROUP_PREFIX
						+ cGroup.getId());
		List<AccessibilityBean> access = new ArrayList<AccessibilityBean>();
		for (User aUser : users) {
			AccessibilityBean accessibility = new AccessibilityBean();
			accessibility.setGroupName(cGroup.getName());
			accessibility.setRoleName(userRoles.get(aUser.getLoginName()));
			accessibility.setUserBean(new UserBean(aUser));
			access.add(accessibility);
		}
		cGroup.setUserAccessibilities(access);
	}

	public CollaborationGroupBean findCollaborationGroupById(String id)
			throws CommunityException {
		CollaborationGroupBean collaborationGroup = null;
		try {
			Group group = authService.getUserManager().getGroupById(id);
			collaborationGroup = new CollaborationGroupBean(group);
			String pe = Constants.CSM_COLLABORATION_GROUP_PREFIX
					+ collaborationGroup.getId();
			if (authService.checkCreatePermission(user, pe)) {
				setUserAccesses(collaborationGroup);
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
			if (!authService.checkPermission(user,
					Constants.CSM_COLLABORATION_GROUP_PREFIX
							+ collaborationGroup.getId(),
					Constants.CSM_DELETE_PRIVILEGE)) {
				throw new NoAccessException();
			} else {
				authService
						.removeCSMEntry(Constants.CSM_COLLABORATION_GROUP_PREFIX
								+ collaborationGroup.getId());
				authService.getAuthorizationManager().removeGroup(
						collaborationGroup.getId());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error deleting the collaboration group";
			throw new CommunityException(error, e);
		}
	}
}