package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.security.authorization.domainobjects.Group;

public class CollaborationGroupBean extends SecuredDataBean {
	private String id;
	private String name;
	private String description;
	private String ownerName;

	public CollaborationGroupBean() {
		for (AccessibilityBean access : this.getUserAccesses()) {
			access.setAccessBy(AccessibilityBean.ACCESS_BY_USER);
		}
	}

	public CollaborationGroupBean(Group group) {
		this.name = group.getGroupName();
		this.description = group.getGroupDesc();
		this.id = group.getGroupId().toString();
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

	public void addUserAccess(AccessibilityBean userAccess) {
		int index = this.getUserAccesses().indexOf(userAccess);
		if (index != -1) {
			this.getUserAccesses().remove(userAccess);
			// retain the original order
			this.getUserAccesses().add(index, userAccess);
		} else {
			this.getUserAccesses().add(userAccess);
		}
	}

	public void removeUserAccess(AccessibilityBean userAccess) {
		this.getUserAccesses().remove(userAccess);
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}
