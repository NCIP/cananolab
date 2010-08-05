package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;

public class SecuredDataBean {
	private List<AccessibilityBean> userAccesses = new ArrayList<AccessibilityBean>();

	private List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();

	private AccessibilityBean theAccess = new AccessibilityBean();

	private List<AccessibilityBean> allAccesses = new ArrayList<AccessibilityBean>();

	private Boolean publicStatus = false;

	private String securedId;

	private Boolean userUpdatable = false;

	private Boolean userDeletable = false;

	private Boolean userIsOwner = false;

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
		for (AccessibilityBean access : groupAccesses) {
			if (access.getGroupName()
					.equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
				publicStatus = true;
			}
		}
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

	public Boolean getPublicStatus() {
		return publicStatus;
	}

	public String getSecuredId() {
		return securedId;
	}

	public void setSecuredId(String securedId) {
		this.securedId = securedId;
	}

	public Boolean getUserDeletable() {
		return userDeletable;
	}

	public void setUserDeletable(Boolean userDeletable) {
		this.userDeletable = userDeletable;
	}

	public Boolean getUserIsOwner() {
		return userIsOwner;
	}

	public void setUserIsOwner(Boolean userIsOwner) {
		this.userIsOwner = userIsOwner;
	}
}
