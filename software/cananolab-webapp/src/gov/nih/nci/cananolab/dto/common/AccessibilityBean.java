package gov.nih.nci.cananolab.dto.common;


public class AccessibilityBean {
	public static final String CSM_APP_NAME = "caNanoLab";
	public static final String CSM_DATA_CURATOR = "Curator";
	public static final String CSM_PUBLIC_GROUP = "Public";
	public static final String[] VISIBLE_GROUPS = new String[] { CSM_DATA_CURATOR };
	public static final String CSM_READ_ROLE = "R";
	public static final String CSM_DELETE_ROLE = "D";
	public static final String CSM_EXECUTE_ROLE = "E";
	public static final String CSM_CURD_ROLE = "CURD";
	public static final String CSM_CUR_ROLE = "CUR";
	public static final String CSM_READ_PRIVILEGE = "READ";
	public static final String CSM_EXECUTE_PRIVILEGE = "EXECUTE";
	public static final String CSM_DELETE_PRIVILEGE = "DELETE";
	public static final String CSM_CREATE_PRIVILEGE = "CREATE";
	public static final String CSM_UPDATE_PRIVILEGE = "UPDATE";
	public static final String CSM_PG_CURATION = "curation";
	public static final String CSM_COLLABORATION_GROUP_PREFIX = "CollaborationGroup_";
	public static final String CURD_ROLE_DISPLAY_NAME = "read update delete";
	public static final String R_ROLE_DISPLAY_NAME = "read";
	public static final String ACCESS_BY_GROUP = "group";
	public static final String ACCESS_BY_USER = "user";
	public static final String ACCESS_BY_PUBLIC = "public";
	public static final AccessibilityBean CSM_DEFAULT_ACCESS;
	static {
		AccessibilityBean defaultAccess = new AccessibilityBean(
				AccessibilityBean.ACCESS_BY_GROUP);
		defaultAccess.setGroupName(CSM_DATA_CURATOR);
		defaultAccess.setRoleName(CSM_CURD_ROLE);
		CSM_DEFAULT_ACCESS = defaultAccess;

	}

	public static final AccessibilityBean CSM_PUBLIC_ACCESS;
	static {
		AccessibilityBean publicAccess = new AccessibilityBean(
				AccessibilityBean.ACCESS_BY_PUBLIC);
		publicAccess.setGroupName(CSM_PUBLIC_GROUP);
		publicAccess.setRoleName(CSM_READ_ROLE);
		CSM_PUBLIC_ACCESS = publicAccess;
	}

	private UserBean userBean = new UserBean();
	private String groupName;
	private String roleName;
	private String roleDisplayName;
	private String accessBy = ACCESS_BY_GROUP;

	public AccessibilityBean() {
	}

	public AccessibilityBean(String accessBy) {
		this.accessBy = accessBy;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
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

	public String getRoleDisplayName() {
		if (roleName == null) {
			return null;
		} else if (roleName.equals(CSM_CURD_ROLE)) {
			roleDisplayName = this.CURD_ROLE_DISPLAY_NAME;
		} else if (roleName.equals(CSM_READ_ROLE)) {
			roleDisplayName = this.R_ROLE_DISPLAY_NAME;
		}
		return roleDisplayName;
	}

	public boolean equals(Object obj) {
		if (obj instanceof AccessibilityBean) {
			AccessibilityBean access = (AccessibilityBean) obj;
			if (access.getAccessBy().equals(ACCESS_BY_GROUP)
					|| access.getAccessBy().equals(ACCESS_BY_PUBLIC)) {
				if (access.getGroupName().equals(getGroupName())
						&& access.getRoleName().equals(getRoleName())) {
					return true;
				}
			} else {
				if (access.getUserBean().getLoginName().equals(
						getUserBean().getLoginName())
						&& access.getRoleName().equals(getRoleName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns hash code for the primary key of the object
	 */
	public int hashCode() {
		if (accessBy.equals(ACCESS_BY_USER)) {
			if (getUserBean().getLoginName() != null)
				return getUserBean().getLoginName().hashCode();
			return 0;
		} else {
			if (getGroupName() != null)
				return getGroupName().hashCode();
			return 0;
		}
	}

	public String getAccessBy() {
		return accessBy;
	}

	public void setAccessBy(String accessBy) {
		this.accessBy = accessBy;
	}
}
