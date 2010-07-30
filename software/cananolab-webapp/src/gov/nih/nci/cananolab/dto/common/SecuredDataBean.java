package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;

public class SecuredDataBean {
	private List<AccessibilityBean> userAccesses = new ArrayList<AccessibilityBean>();

	private List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();

	private AccessibilityBean theAccess = new AccessibilityBean();

	private List<AccessibilityBean> allAccesses = new ArrayList<AccessibilityBean>();

	public List<AccessibilityBean> getUserAccess() {
		return userAccesses;
	}

	public void setUserAccesses(List<AccessibilityBean> userAccesses) {
		this.userAccesses = userAccesses;
	}

	public List<AccessibilityBean> getGroupAccesses() {
		return groupAccesses;
	}

	public void setGroupAccesses(List<AccessibilityBean> groupAccesses) {
		this.groupAccesses = groupAccesses;
	}

	public AccessibilityBean getTheAccess() {
		return theAccess;
	}

	public void setTheAccess(AccessibilityBean theAccess) {
		this.theAccess = theAccess;
	}

	public List<AccessibilityBean> getUserAccesses() {
		return userAccesses;
	}

	private Boolean userUpdatable = false;

	public Boolean getUserUpdatable() {
		return userUpdatable;
	}

	public void setUserUpdatable(Boolean userUpdatable) {
		this.userUpdatable = userUpdatable;
	}

	public List<AccessibilityBean> getAllAccesses() {
		allAccesses.addAll(getGroupAccesses());
		allAccesses.addAll(getUserAccesses());
		return allAccesses;
	}
}
