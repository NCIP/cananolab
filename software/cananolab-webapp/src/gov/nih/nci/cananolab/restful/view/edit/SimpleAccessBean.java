package gov.nih.nci.cananolab.restful.view.edit;

public class SimpleAccessBean {
	
	String groupName = "";
	String roleName = "";
	String roleDisplayName = "";
	String loginName = "";
	String accessBy = "";
	
	boolean dirty = false;

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRoleDisplayName() {
		return roleDisplayName;
	}

	public void setRoleDisplayName(String roleDisplayName) {
		this.roleDisplayName = roleDisplayName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getAccessBy() {
		return accessBy;
	}

	public void setAccessBy(String accessBy) {
		this.accessBy = accessBy;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
