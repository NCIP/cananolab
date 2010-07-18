package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.security.authorization.domainobjects.Group;

import java.util.ArrayList;
import java.util.List;

public class CollaborationGroupBean {
	private String id;
	private String name;
	private String description;
	private List<AccessibilityBean> userAccessibilities = new ArrayList<AccessibilityBean>();
	private AccessibilityBean theUserAccessibility = new AccessibilityBean();

	public CollaborationGroupBean() {
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

	public List<AccessibilityBean> getUserAccessibilities() {
		return userAccessibilities;
	}

	public void setUserAccessibilities(
			List<AccessibilityBean> userAccessibilities) {
		this.userAccessibilities = userAccessibilities;
	}

	public AccessibilityBean getTheUserAccessibility() {
		return theUserAccessibility;
	}

	public void setTheUserAccessibility(AccessibilityBean theUserAccessibility) {
		this.theUserAccessibility = theUserAccessibility;
	}

	public void addUserAccess(AccessibilityBean userAccess) {
		int index = userAccessibilities.indexOf(userAccess);
		if (index != -1) {
			userAccessibilities.remove(userAccess);
			// retain the original order
			userAccessibilities.add(index, userAccess);
		} else {
			userAccessibilities.add(userAccess);
		}
	}

	public void removeUserAccess(AccessibilityBean userAccess) {
		userAccessibilities.remove(userAccess);
	}
}
