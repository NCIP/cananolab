package gov.nih.nci.calab.service.login;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;

import gov.nih.nci.calab.dto.security.SecurityBean;

/** 
 * The LoginService authenticates users into the calab system.
 * 
 * @author      doswellj
 * @param applicationName sets the application name for use by CSM
 * @param am  Authentication Manager for CSM.
 */

public class LoginService 
{

	String applicationName = null;
	AuthenticationManager am = null;
	
// TODO Make a singleton 

	
	/**
	 * LoginService Constructor
	 * @param strname name of the application
	 */

	public LoginService(String strname) throws Exception 
	{
		this.applicationName = strname;
		am = SecurityServiceProvider.getAuthenticationManager(this.applicationName);
        //TODO Add Role implementation
	 }
	
	/**
	 * The login method uses CSM to authenticated the user with LoginId and Password credentials 
	 * @param	strusername  LoginId of the user
	 * @param 	strpassword  Encrypted password of the user
	 * @return	boolean identicating whether the user successfully authenticated
	 */
	public boolean login(String strUsername, String strPassword ) throws CSException 
	{
		return am.login( strUsername,strPassword);
	}
	
	/**
	 * The userInfo method sets and authenticated user's information 
	 * @param	strLoginId LoginId of the authenticated user
	 * @return	SecurityBean containing an authenticated user's information
	 */
    public SecurityBean setUserInfo(String strLoginId)
    {
    	//TODO Implement method to query CSM_USER table and get logged in user's recordset
    	SecurityBean securityBean = new SecurityBean();
    	
    	securityBean.setLoginId(strLoginId);
    	//set remaining info
    	
    	return securityBean;
    	
    }
	
}
