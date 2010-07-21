package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.util.Constants;

public class AccessibilityBean {
	private UserBean userBean = new UserBean();
	private String groupName;
	private String roleName;
	private String roleDisplayName;
	private String accessBy = "group";

	public static final String CURD_ROLE_DISPLAY_NAME = "read update delete";
	public static final String R_ROLE_DISPLAY_NAME = "read";

	public AccessibilityBean() {
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDisplayName() {
		if (roleName == null) {
			return null;
		} else if (roleName.equals(Constants.CSM_CURD_ROLE)) {
			roleDisplayName = this.CURD_ROLE_DISPLAY_NAME;
		} else if (roleName.equals(Constants.CSM_READ_ROLE)) {
			roleDisplayName = this.R_ROLE_DISPLAY_NAME;
		}
		return roleDisplayName;
	}

	public boolean equals(Object obj) {
		if (obj instanceof AccessibilityBean) {
			AccessibilityBean access = (AccessibilityBean) obj;
			if (access.getUserBean().getLoginName().equals(
					getUserBean().getLoginName()))
				return true;
		}
		return false;
	}

	/**
	 * Returns hash code for the primary key of the object
	 */
	public int hashCode() {
		if (getUserBean().getLoginName() != null)
			return getUserBean().getLoginName().hashCode();
		return 0;
	}

	public String getAccessBy() {
		return accessBy;
	}

	public void setAccessBy(String accessBy) {
		this.accessBy = accessBy;
	}

}
