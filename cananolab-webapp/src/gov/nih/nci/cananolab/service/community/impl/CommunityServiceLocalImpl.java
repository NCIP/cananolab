package gov.nih.nci.cananolab.service.community.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class CommunityServiceLocalImpl extends BaseServiceHelper implements
		CommunityService {
	public CommunityServiceLocalImpl() {

	}

	public CommunityServiceLocalImpl(UserBean user) {
		super(user);
	}

	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException {
		if (getUser() == null) {
			throw new NoAccessException();
		}
		try {
			List<AccessibilityBean> accessibilities = collaborationGroup
					.getUserAccessibilities();
			AuthorizationService authService = super.getAuthService();
			AuthorizationManager authManager = authService
					.getAuthorizationManager();

			Group group = null;
			// create a new group if none exists.
			if (StringUtils.isEmpty(collaborationGroup.getId())) {
				group = new Group();
				group.setGroupName(collaborationGroup.getName());
				group.setGroupDesc(collaborationGroup.getDescription());
				authManager.createGroup(group);
				// assign CURD access to user who created the group
				authService.secureObjectForUser(
						Constants.CSM_COLLABORATION_GROUP_PREFIX
								+ group.getGroupId(), getUser().getLoginName(),
						Constants.CSM_CURD_ROLE);
				// assign CURD access to Curator group
				authService.secureObject(
						Constants.CSM_COLLABORATION_GROUP_PREFIX
								+ group.getGroupId(),
						Constants.CSM_DATA_CURATOR, Constants.CSM_CURD_ROLE);
			}
			// update existing group
			else {
				group = authService.getGroup(collaborationGroup.getName());
				// update group description
				group.setGroupDesc(collaborationGroup.getDescription());
				authManager.modifyGroup(group);

				// update user access
				CollaborationGroupBean existingGroup = findCollaborationGroupById(collaborationGroup
						.getId());
				List<AccessibilityBean> existingAccess = existingGroup
						.getUserAccessibilities();
				List<AccessibilityBean> accessToRemove = new ArrayList<AccessibilityBean>(
						existingAccess);
				accessToRemove.removeAll(accessibilities);
				for (AccessibilityBean access : accessToRemove) {
					authManager.removeUserFromGroup(group.getGroupId()
							.toString(), access.getUserBean().getUserId());
					authService.removeSecureObjectForUser(
							Constants.CSM_COLLABORATION_GROUP_PREFIX
									+ group.getGroupId(), access.getUserBean()
									.getLoginName(), Constants.CSM_CURD_ROLE);
				}
			}
			// check if user has access to update the group
			if (authService.checkCreatePermission(getUser(),
					Constants.CSM_COLLABORATION_GROUP_PREFIX
							+ group.getGroupId())) {
				String[] userIds = new String[accessibilities.size() + 1];
				int i = 0;
				for (AccessibilityBean access : accessibilities) {
					UserBean userBean = access.getUserBean();
					User user = authManager.getUser(userBean.getLoginName());
					userBean.setUserId(user.getUserId().toString());
					String userId = userBean.getUserId();
					userIds[i] = userId;
					// assign user accessibility to the collaboration group
					authService.secureObjectForUser(
							Constants.CSM_COLLABORATION_GROUP_PREFIX
									+ group.getGroupId(), user.getLoginName(),
							access.getRoleName());
					i++;
				}
				// add current user to the group
				userIds[i] = getUser().getUserId();
				authManager.addUsersToGroup(group.getGroupId().toString(),
						userIds);
			} else {
				throw new NoAccessException(
						"Collaboration Group already exists and you don't have access to update it");
			}
		} catch (NoAccessException e) {
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
			AuthorizationService authService = super.getAuthService();
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
				if (authService.checkCreatePermission(getUser(),
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
		AuthorizationService authService = super.getAuthService();
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
		for (User user : users) {
			// remove the group owner from the list
			if (!user.getLoginName().equalsIgnoreCase(getUser().getLoginName())) {
				AccessibilityBean accessibility = new AccessibilityBean();
				accessibility.setGroupName(cGroup.getName());
				accessibility.setRoleName(userRoles.get(user.getLoginName()));
				accessibility.setUserBean(new UserBean(user));
				access.add(accessibility);
			}
		}
		cGroup.setUserAccessibilities(access);
	}

	public CollaborationGroupBean findCollaborationGroupById(String id)
			throws CommunityException {
		CollaborationGroupBean collaborationGroup = null;
		try {
			AuthorizationService authService = super.getAuthService();
			Group group = authService.getUserManager().getGroupById(id);
			collaborationGroup = new CollaborationGroupBean(group);
			String pe = Constants.CSM_COLLABORATION_GROUP_PREFIX
					+ collaborationGroup.getId();
			if (authService.checkCreatePermission(getUser(), pe)) {
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
		if (getUser() == null) {
			throw new NoAccessException();
		}
		try {
			AuthorizationService authService = super.getAuthService();
			// check if user has access to delete the group
			if (!authService.checkPermission(getUser(),
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
		} catch (Exception e) {
			String error = "Error deleting the collaboration group";
			throw new CommunityException(error, e);
		}
	}
}
