package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.util.Constants;

public class AccessibilityBean {
	private UserBean user = new UserBean();
	private String groupName;
	private String roleName;
	private String roleDisplayName;

	public static final String CURD_ROLE_DISPLAY_NAME = "read update delete";
	public static final String R_ROLE_DISPLAY_NAME = "read";

	public AccessibilityBean() {
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
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
		}
		else if (roleName.equals(Constants.CSM_CURD_ROLE)) {
			roleDisplayName = this.CURD_ROLE_DISPLAY_NAME;
		} else if (roleName.equals(Constants.CSM_READ_ROLE)) {
			roleDisplayName = this.R_ROLE_DISPLAY_NAME;
		}
		return roleDisplayName;
	}
}
