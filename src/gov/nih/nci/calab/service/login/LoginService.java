package gov.nih.nci.calab.service.login;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import gov.nih.nci.security.AuthenticationManager;
//import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;
import gov.nih.nci.security.SecurityServiceProvider;
//import gov.nih.nci.ncia.util.NCIAConfig;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;

public class LoginService 
{

	String applicationName = null;
	AuthenticationManager am = null;
	
//	 Singleton instance of the security manager
//private static LoginService manager = null;
	
	
	/*
	 * Constructor
	 */
	public LoginService(String strname) throws Exception 
	{
		this.applicationName = strname;
		am = SecurityServiceProvider.getAuthenticationManager(this.applicationName);
        //TODO Add Role implementation
	 }
	
	public boolean login(String strusername, String strpassword ) throws CSException {
		return am.login( strusername, strpassword );
	}
	

	
}
