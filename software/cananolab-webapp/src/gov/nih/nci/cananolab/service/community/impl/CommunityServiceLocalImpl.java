package gov.nih.nci.cananolab.service.community.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.util.Constants;
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

import org.apache.commons.lang.StringUtils;

public class CommunityServiceLocalImpl extends BaseServiceLocalImpl implements
		CommunityService {
	private AuthorizationManager authManager;

	public CommunityServiceLocalImpl() throws CommunityException {
		super();
		try {
			this.authManager = SecurityServiceProvider
					.getAuthorizationManager(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error(e);
			throw new CommunityException(e);
		}
	}

	public CommunityServiceLocalImpl(UserBean user) throws CommunityException {
		super(user);
		try {
			this.authManager = SecurityServiceProvider
					.getAuthorizationManager(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error(e);
			throw new CommunityException(e);
		}
	}

	public CommunityServiceLocalImpl(SecurityService securityService) throws CommunityException {
		super(securityService);
		try {
			this.authManager = SecurityServiceProvider
					.getAuthorizationManager(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error(e);
			throw new CommunityException(e);
		}
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
			Group doGroup = securityService.getGroup(collaborationGroup
					.getName());
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
				AccessibilityBean ownerAccess = new AccessibilityBean(
						AccessibilityBean.ACCESS_BY_USER);
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
					doGroup = authManager.getGroupById(collaborationGroup
							.getId());
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
					accessUtils.removeSecureObjectForUser(
							Constants.CSM_COLLABORATION_GROUP_PREFIX
									+ doGroup.getGroupId(), access
									.getUserBean().getLoginName(),
							Constants.CSM_CURD_ROLE);
				}
			}
			// check if user has access to update the group
			if (securityService
					.checkCreatePermission(Constants.CSM_COLLABORATION_GROUP_PREFIX
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
			Group dummy = new Group();
			dummy.setGroupName("*");
			SearchCriteria sc = new GroupSearchCriteria(dummy);
			List results = authManager.getObjects(sc);
			for (Object obj : results) {
				Group doGroup = (Group) obj;
				CollaborationGroupBean cGroup = new CollaborationGroupBean(
						doGroup);
				if (securityService
						.checkCreatePermission(Constants.CSM_COLLABORATION_GROUP_PREFIX
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
		// set user accessibilities
		Set<User> users = authManager.getUsers(cGroup.getId().toString());
		List<String> userNames = new ArrayList<String>(users.size());
		for (User user : users) {
			userNames.add(user.getLoginName());
		}
		Map<String, String> userRoles = securityService
				.getAllUserRoles(Constants.CSM_COLLABORATION_GROUP_PREFIX
						+ cGroup.getId());
		List<AccessibilityBean> access = new ArrayList<AccessibilityBean>();
		for (User aUser : users) {
			AccessibilityBean accessibility = new AccessibilityBean(
					AccessibilityBean.ACCESS_BY_USER);
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
			Group group = authManager.getGroupById(id);
			collaborationGroup = new CollaborationGroupBean(group);
			String pe = Constants.CSM_COLLABORATION_GROUP_PREFIX
					+ collaborationGroup.getId();
			if (securityService.checkCreatePermission(pe)) {
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
			if (!securityService
					.checkDeletePermission(Constants.CSM_COLLABORATION_GROUP_PREFIX
							+ collaborationGroup.getId())) {
				throw new NoAccessException();
			} else {
				super
						.removeAllAccesses(Constants.CSM_COLLABORATION_GROUP_PREFIX
								+ collaborationGroup.getId());
				authManager.removeGroup(collaborationGroup.getId());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error deleting the collaboration group";
			throw new CommunityException(error, e);
		}
	}
}