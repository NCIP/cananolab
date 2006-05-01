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
	
	public UserBean() {}
	
	public UserBean(String strLoginId, String firstName, String lastName)
	{
		this.loginId = strLoginId;
		this.firstName= firstName;
		this.lastName= lastName;
		this.fullName = firstName + " " + lastName;
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
}
