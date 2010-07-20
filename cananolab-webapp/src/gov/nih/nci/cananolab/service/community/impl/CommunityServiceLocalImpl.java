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

public class CommunityServiceLocalImpl extends BaseServiceHelper implements
		CommunityService {
	public CommunityServiceLocalImpl() {

	}

	public CommunityServiceLocalImpl(UserBean user) {
		super(user);
	}

	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException {
		try {
			if (getUser() == null) {
				throw new NoAccessException();
			}
			AuthorizationService authService = super.getAuthService();
			AuthorizationManager authManager = authService
					.getAuthorizationManager();
			Group group = authService
					.createAGroup(collaborationGroup.getName());
			List<AccessibilityBean> accessibilities = collaborationGroup
					.getUserAccessibilities();
			String[] userIds = new String[accessibilities.size() + 1];
			int i = 0;
			for (AccessibilityBean access : accessibilities) {
				UserBean userBean = access.getUserBean();
				User user = authManager.getUser(userBean.getLoginName());
				userBean.setUserId(user.getUserId().toString());
				String userId = userBean.getUserId();
				userIds[i] = userId;
				// assign user accessibility to the collaboration group
				Role role = authService.getRole(access.getRoleName());
				authManager.assignUserRoleToProtectionGroup(userId,
						new String[] { role.getId().toString() },
						"CollaborationGroup_" + group.getGroupId());
				i++;
			}
			// assign user who created the collaboration group CURD access to the group
			userIds[i] = super.getUser().getUserId();
			Role role = authService.getRole(Constants.CSM_CURD_ROLE);
			authManager.assignUserRoleToProtectionGroup(super.getUser()
					.getUserId(), new String[] { role.getId().toString() },
					"CollaborationGroup_" + group.getGroupId());
			authManager.addUsersToGroup(group.getGroupId().toString(), userIds);
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
			dummy.setGroupName("CollaborationGroup_*");
			SearchCriteria sc = new GroupSearchCriteria(dummy);
			List results = authManager.getObjects(sc);
			for (Object obj : results) {
				Group doGroup = (Group) obj;
				CollaborationGroupBean cGroup = new CollaborationGroupBean(
						doGroup);
				if (authService.checkCreatePermission(getUser(),
						"CollaborationGroup_" + cGroup.getId())) {
					setUserAccesses(cGroup);
					collaborationGroups.add(cGroup);
				} else {
					String error = "User has no access to the collaboration group "
							+ cGroup.getName();
					throw new CommunityException(error);
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
				.getUserRoles("CollaborationGroup_" + cGroup.getId());
		List<AccessibilityBean> access = new ArrayList<AccessibilityBean>();
		for (User user : users) {
			AccessibilityBean accessibility = new AccessibilityBean();
			accessibility.setGroupName(cGroup.getName());
			accessibility.setRoleName(userRoles.get(user.getLoginName()));
			accessibility.setUserBean(new UserBean(user));
			access.add(accessibility);
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
			String pe = "CollaborationGroup_" + collaborationGroup.getId();
			if (authService.checkCreatePermission(getUser(), pe)) {
				setUserAccesses(collaborationGroup);
			} else {
				String error = "User has no access to the collaboration group "
						+ collaborationGroup.getName();
				return null;
			}
		} catch (Exception e) {
			String error = "Error retrieving the collaboration group by name";
			throw new CommunityException(error, e);
		}
		return collaborationGroup;
	}
}
