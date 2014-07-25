package gov.nih.nci.cananolab.restful.view.edit;

public class SimpleAccessBean {
	
	long sampleId;
	
	String groupName;
	String roleDisplayName;
	String loginName;
	String accessBy;
	
	String accessRight;

	public long getSampleId() {
		return sampleId;
	}

	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
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

	public String getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
	}
	
	
}
