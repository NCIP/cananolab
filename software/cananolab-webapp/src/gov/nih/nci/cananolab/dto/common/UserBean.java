package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * This class represents properties of a user object to be shown in the view
 * page.
 *
 * @author pansu
 *
 */
public class UserBean {

	private String userId;

	/**
	 * This string is used for login into the application.
	 */
	private String loginName = "";

	/**
	 * The first name of the user
	 */
	private String firstName;

	/**
	 * The last name of the user
	 */
	private String lastName;

	private String fullName;

	/**
	 * The name of the organization that this user belongs to.
	 */
	private String organization;

	/**
	 * The name of the department that this user belongs to.
	 */
	private String department;

	/**
	 * The name of the title for this user.
	 */
	private String title;

	/**
	 * This is the work phone of the user.
	 */
	private String phoneNumber;

	/**
	 * The password used to login into the application
	 */
	private String password;

	/**
	 * Email id for this user.
	 */
	private String emailId;

	private boolean admin;

	private boolean curator;

	private User domain;

	public UserBean() {
	}

	public UserBean(String loginName, String password) {
		this.loginName = loginName;
		this.password = password;
	}

	public UserBean(User user) {
		this.department = user.getDepartment();
		this.emailId = user.getEmailId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.loginName = user.getLoginName();
		this.organization = user.getOrganization();
		this.password = user.getPassword();
		this.phoneNumber = user.getPhoneNumber();
		this.title = user.getTitle();
		this.userId = user.getUserId().toString();
		this.fullName = this.lastName + ", " + this.firstName;
		this.domain = user;
	}

	public String getDepartment() {
		return this.department;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getOrganization() {
		return this.organization;
	}

	public String getPassword() {
		return this.password;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getTitle() {
		return this.title;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getFullName() {
		return this.fullName;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isCurator() {
		return curator;
	}

	public void setCurator(boolean curator) {
		this.curator = curator;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof UserBean) {
			UserBean u = (UserBean) obj;
			String thisId = this.getUserId();
			if (thisId != null && thisId.equals(u.getUserId())) {
				eq = true;
			}
		}
		return eq;
	}

	public int compareTo(Object obj) {
		int diff = 0;
		if (obj instanceof UserBean) {
			UserBean u = (UserBean) obj;
			if (u.getLastName().equals(this.getLastName())) {
				return u.getFirstName().toLowerCase().compareTo(
						this.getFirstName().toLowerCase());
			} else {
				return u.getLastName().toLowerCase().compareTo(
						this.getLastName().toLowerCase());
			}
		}
		return diff;
	}

	public User getDomain() {
		return domain;
	}
}
