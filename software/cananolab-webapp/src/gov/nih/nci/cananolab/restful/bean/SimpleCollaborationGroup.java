package gov.nih.nci.cananolab.restful.bean;

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;

public class SimpleCollaborationGroup {
	
	String id;
	String name;
	String description;
	String ownerName;
	
	public void transferFromCollaborationGroupBean(CollaborationGroupBean groupBean) {
		if (groupBean == null) return;
		
		this.id = groupBean.getId();
		this.name = groupBean.getName();
		this.description = groupBean.getDescription();
		this.ownerName = groupBean.getOwnerName();
	}
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	

}
