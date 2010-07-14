package gov.nih.nci.cananolab.service.community.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
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
			String[] userIds = new String[accessibilities.size()];
			int i = 0;
			for (AccessibilityBean access : accessibilities) {
				UserBean user = access.getUser();
				userIds[i] = user.getUserId();
				// assign user accessibility to the collaboration group
				Role role = authService.getRole(access.getRoleName());
				authManager.assignUserRoleToProtectionGroup(user.getUserId(),
						new String[] { role.getId().toString() },
						"CollaborationGroup_" + group.getGroupId());
				i++;
			}
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
			dummy.setGroupName("*");
			SearchCriteria sc = new GroupSearchCriteria(dummy);
			List results = authManager.getObjects(sc);
			for (Object obj : results) {
				Group doGroup = (Group) obj;
				String pe = "CollaborationGroup_" + doGroup.getGroupId();
				if (authService.checkCreatePermission(getUser(), pe)) {
					CollaborationGroupBean cGroup = new CollaborationGroupBean();
					cGroup.setName(doGroup.getName());
					cGroup.setDescription(doGroup.getGroupDesc());

					// set user accessibilities
					Set<User> users = authManager.getUsers(doGroup.getGroupId()
							.toString());
					List<String> userNames = new ArrayList<String>(users.size());
					for (User user : users) {
						userNames.add(user.getLoginName());
					}
					Map<String, String> userRoles = authService
							.getUserRoles(pe);
					List<AccessibilityBean> access = new ArrayList<AccessibilityBean>();
					for (User user : users) {
						AccessibilityBean accessibility = new AccessibilityBean();
						accessibility.setGroupName(doGroup.getGroupName());
						accessibility.setRoleName(userRoles.get(user
								.getLoginName()));
						accessibility.setUser(new UserBean(user));
						access.add(accessibility);
					}
					cGroup.setUserAccessibilities(access);
					collaborationGroups.add(cGroup);
				}
			}
		} catch (Exception e) {
			String error = "Error finding existing collaboration groups accessible by the user";
			throw new CommunityException(error, e);
		}
		return collaborationGroups;
	}
}
