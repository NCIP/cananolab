package gov.nih.nci.calab.dto.security;

import gov.nih.nci.calab.dto.administration.ContainerBean;

/**
 * This class includes properties for User Authentication and Authorization .
 * 
 * 
 * @author doswellj
 * 
 */

public class SecurityBean 
{
	
	private String strLogin;
	private String strPass;
	private String strFName;
	private String strLName;
	
	public SecurityBean() {}
	
	public SecurityBean(String strLoginId, String strPassword, String strFirstName, String strLastName)
	{
	
		this.strLogin = strLoginId;
		this.strPass= strPassword;
		this.strFName= strFirstName;
		this.strLName= strLastName;
	}
	
	public String getLoginId() 
	{
		return strLogin;
	}
	public void setLoginId(String strLoginId) 
	{
		this.strLogin = strLoginId;
	}

	public String getPassword() 
	{
		return strPass;
	}
	public void setPassword(String strPassword) 
	{
		this.strPass = strPassword;
	}

	public String getFirstName() 
	{
		return strFName;
	}
	public void setFirstName(String strFirstName) 
	{
		this.strFName = strFirstName;
	}

	public String getLastName() 
	{
		return strLName;
	}
	public void setLastName(String strLastName) 
	{
		this.strLName = strLastName;
	}
}
