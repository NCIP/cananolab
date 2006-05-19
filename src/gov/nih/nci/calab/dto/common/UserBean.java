package gov.nih.nci.calab.dto.common;

/**
 * This class includes properties for User Authentication and Authorization .
 * 
 * 
 * @author doswellj
 * 
 */

public class UserBean 
{
	
	private String loginId;
	private String fullName;
	private String firstName;
	private String lastName;
	private String title;
	private String middleName;
	private String email;
	private String phoneNumber;
	private String organization;
	private String department;
	private String password;
	
	public UserBean() {}
	
	public UserBean(String strLoginId, String firstName, String lastName)
	{
		this.loginId = strLoginId;
		this.firstName= firstName;
		this.lastName= lastName;
		this.fullName = firstName + " " + lastName;
	}	
	
	public UserBean(String title, String firstName, String middleName, String lastName, String email, String phoneNumber, String organization, String department, String loginId, String password) {
		super();
		// TODO Auto-generated constructor stub
		this.loginId = loginId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.middleName = middleName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.organization = organization;
		this.department = department;
		this.password = password;
	}

	public String getLoginId() 
	{
		return loginId;
	}
	public void setLoginId(String loginId) 
	{
		this.loginId = loginId;
	}

	public String getFirstName() 
	{
		return firstName;
	}
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	public String getLastName() 
	{
		return lastName;
	}
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName =fullName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
