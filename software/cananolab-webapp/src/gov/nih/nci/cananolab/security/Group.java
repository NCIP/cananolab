package gov.nih.nci.cananolab.security;

import java.io.Serializable;

public class Group implements Serializable
{
	private static final long serialVersionUID = 7724945766896131023L;
	
	private Long id;
	private String groupName;
	private String groupDesc;
	private String createdBy;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getGroupDesc() {
		return groupDesc;
	}
	
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", groupName=" + groupName + ", groupDesc=" + groupDesc + "]";
	}

}
