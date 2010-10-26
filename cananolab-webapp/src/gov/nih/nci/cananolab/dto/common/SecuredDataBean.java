package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SecuredDataBean {
	private List<AccessibilityBean> userAccesses = new ArrayList<AccessibilityBean>();

	private List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();

	private AccessibilityBean theAccess = new AccessibilityBean();

	private List<AccessibilityBean> allAccesses = new ArrayList<AccessibilityBean>();

	private Boolean publicStatus = false;

	private UserBean user;

	protected String createdBy;

	private Boolean userUpdatable = false;
	private Boolean userDeletable = false;
	private Boolean userIsOwner = false;

	public List<AccessibilityBean> getUserAccesses() {
		return userAccesses;
	}

	public void setUserAccesses(List<AccessibilityBean> userAccesses) {
		this.userAccesses = userAccesses;
	}

	public List<AccessibilityBean> getGroupAccesses() {
		return groupAccesses;
	}

	public void setGroupAccesses(List<AccessibilityBean> groupAccesses) {

		this.groupAccesses = groupAccesses;
	}

	public AccessibilityBean getTheAccess() {
		return theAccess;
	}

	public void setTheAccess(AccessibilityBean theAccess) {
		this.theAccess = theAccess;
	}

	public List<AccessibilityBean> getAllAccesses() {
		allAccesses.addAll(getGroupAccesses());
		allAccesses.addAll(getUserAccesses());
		return allAccesses;
	}

	public Boolean getPublicStatus() {
		publicStatus = this.retrievPublicStatus();
		return publicStatus;
	}

	public Boolean getUserUpdatable() {
		if (userAccesses.isEmpty() && groupAccesses.isEmpty()) {
			return userUpdatable;
		}
		userUpdatable = this.retrieveUserUpdatable(user);
		return userUpdatable;
	}

	public void setUserUpdatable(Boolean userUpdatable) {
		this.userUpdatable = userUpdatable;
	}

	public Boolean getUserDeletable() {
		if (userAccesses.isEmpty() && groupAccesses.isEmpty()) {
			return userDeletable;
		}
		userDeletable = this.retrieveUserDeletable(user);
		return userDeletable;
	}

	public void setUserDeletable(Boolean userDeletable) {
		this.userDeletable = userDeletable;
	}

	public Boolean getUserIsOwner() {
		userIsOwner = this.retrieveUserIsOwner(user, createdBy);
		return userIsOwner;
	}

	private Boolean retrieveUserUpdatable(UserBean user) {
		if (user == null) {
			return false;
		}
		if (user.isCurator()) {
			return true;
		}
		for (AccessibilityBean access : userAccesses) {
			if (access.getUserBean().getLoginName().equals(user.getLoginName())
					&& (access.getRoleName().equals(
							AccessibilityBean.CSM_CURD_ROLE) || access
							.getRoleName().equals(
									AccessibilityBean.CSM_CUR_ROLE))) {
				return true;
			}
		}
		for (AccessibilityBean access : groupAccesses) {
			for (String groupName : user.getGroupNames()) {
				if (access.getGroupName().equals(groupName)
						&& access.getRoleName().equals(
								AccessibilityBean.CSM_CURD_ROLE)
						|| access.getRoleName().equals(
								AccessibilityBean.CSM_CUR_ROLE)) {
					return true;
				}
			}
		}

		return false;
	}

	private Boolean retrieveUserDeletable(UserBean user) {
		if (user == null) {
			return false;
		}
		if (user.isCurator()) {
			return true;
		}
		for (AccessibilityBean access : userAccesses) {
			if (access.getUserBean().getLoginName().equals(user.getLoginName())
					&& (access.getRoleName().equals(
							AccessibilityBean.CSM_CURD_ROLE) || access
							.getRoleName().equals(
									AccessibilityBean.CSM_DELETE_ROLE))) {
				return true;
			}
		}
		for (AccessibilityBean access : groupAccesses) {
			for (String groupName : user.getGroupNames()) {
				if (access.getGroupName().equals(groupName)
						&& access.getRoleName().equals(
								AccessibilityBean.CSM_CURD_ROLE)
						|| access.getRoleName().equals(
								AccessibilityBean.CSM_DELETE_ROLE)) {
					return true;
				}
			}
		}

		return false;
	}

	public Boolean retrieveUserIsOwner(UserBean user, String createdBy) {
		if (user == null || createdBy == null) {
			return false;
		}
		// user is either a curator or the creator of the data
		// or if the data created from COPY and contains the creator info
		if (user != null
				&& (user.getLoginName().equalsIgnoreCase(createdBy)
						|| createdBy.contains(createdBy + ":"
								+ Constants.AUTO_COPY_ANNOTATION_PREFIX) || user
						.isCurator())) {
			return true;
		} else {
			return false;
		}
	}

	private Boolean retrievPublicStatus() {
		for (AccessibilityBean access : groupAccesses) {
			if (access.getGroupName()
					.equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
				return true;
			}
		}
		return false;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public UserBean getUser() {
		return user;
	}
}
