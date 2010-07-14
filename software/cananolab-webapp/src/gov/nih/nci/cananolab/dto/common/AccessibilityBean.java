package gov.nih.nci.cananolab.dto.common;

public class AccessibilityBean {
	private UserBean user;
	private String groupName;
	private String roleName;

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

}
