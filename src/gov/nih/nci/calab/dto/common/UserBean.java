package gov.nih.nci.calab.dto.common;

import gov.nih.nci.security.authorization.domainobjects.User;

public class UserBean {

	private String userId;
	/**
	 * This string is used for login into the application.
	 */
	private String loginName;
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
		
	public String getDepartment() {
		return department;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getOrganization() {
		return organization;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getTitle() {
		return title;
	}


	public String getUserId() {
		return userId;
	}
	
	public String getFullName() {
		return fullName;
	}

	public UserBean(User user) {
		this.department=user.getDepartment();
		this.emailId=user.getEmailId();
		this.firstName=user.getFirstName();
		this.lastName=user.getLastName();
		this.loginName=user.getLoginName();
		this.organization=user.getOrganization();
		this.password=user.getPassword();
		this.phoneNumber=user.getPhoneNumber();
		this.title=user.getTitle();
		this.userId=user.getUserId().toString();
		this.fullName=firstName+" "+lastName;		
	}

}
