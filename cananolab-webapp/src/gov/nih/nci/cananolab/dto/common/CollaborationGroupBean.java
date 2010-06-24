package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;

public class CollaborationGroupBean {
	private String name;
	private String description;
	private List<AccessibilityBean> userAccessibilities = new ArrayList<AccessibilityBean>();
	private AccessibilityBean theUserAccessibility =new AccessibilityBean();

	public CollaborationGroupBean() {
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
}
