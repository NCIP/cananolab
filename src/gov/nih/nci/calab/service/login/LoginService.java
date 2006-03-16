package gov.nih.nci.calab.service.login;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.UserProvisioningManager;
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
	UserProvisioningManager upm = null;
	AuthenticationManager am = null;
	
    private static Logger logger = Logger.getLogger(LoginService.class);	
	
	// Name of the "READ" Role
	//private static final String readRoleName = NCIAConfig.getProtectionElementPrefix() + "READ";
	
	// Name of the public protection group
	//private static final String publicProtectionGroupName = NCIAConfig.getProtectionElementPrefix() + "PUBLIC";	
	
	// Singleton instance of the security manager
	private static LoginService manager = null;
	
	// The primary key of the read role name
	//private static Long readRoleId;
	
	// Primary key of the public protection group
	private static Long publicProtGroupId;
	
	/**
	 * Returns an instance for the specified application name
	 * 
	 * @return
	 * @throws CSException
	 */
	public static LoginService getInstance(String name) throws Exception {
		
		if ( manager == null )
			manager = new LoginService(name);
		
		return manager; 
	}
	
	/**
	 * Returns an instance for the default application name
	 * 
	 * @return
	 * @throws CSException
	 */
	
	/*
	public static LoginService getInstance() throws Exception {
		
		return getInstance(NCIAConfig.getCsmApplicationName());
		
	}
	*/	
	
	
	/*
	 * Constructor
	 */
	private LoginService( String name ) throws Exception 
	{
		this.applicationName = name;
		//upm = SecurityServiceProvider.getUserProvisioningManager(this.applicationName);
		am = SecurityServiceProvider.getAuthenticationManager(this.applicationName);

   /*
		
		try
		{
			// Get IDs for read role			
		    Role exampleRole = new Role();
		    exampleRole.setName(readRoleName);
		    List rolesResult = upm.getObjects(new RoleSearchCriteria(exampleRole));
		    readRoleId = ((Role)rolesResult.get(0)).getId();
    	}
		catch(Exception e)
		{
			System.out.println("A CSM role must be defined with the name "+readRoleName);
			throw e;
		}
	    
		try
		{
		    // Get ID for public protection group
		    ProtectionGroup exampleProtGroup = new ProtectionGroup();
		    exampleProtGroup.setProtectionGroupName(publicProtectionGroupName);
		    List protGroupResult = upm.getObjects(new ProtectionGroupSearchCriteria(exampleProtGroup));
		    publicProtGroupId = ((ProtectionGroup)protGroupResult.get(0)).getProtectionGroupId();
    	}
		catch(Exception e)
		{
			System.out.println("A CSM protection group must be defined with the name "+readRoleName);
			throw e;
		}	    
	
	*/
	}
	
	/*
	 * login method is to check CSM database to validate user's username and password
	 * @param	username,password
	 * @return	boolean
	 */
	public boolean login(String strusername, String strpassword ) throws CSException {
		return am.login( strusername, strpassword );
	}
	
	/*
	 * createUser is to create a new user and assign it to public user
	 * @param	- User object
	 * @param   - the name of the public group to add the user to
	 * @return	- none
	 */
	/*
	public void createUser( User u, String publicGroupName ) throws CSTransactionException
	{
		upm.createUser( u );

		// Grant user access to the public protection group
        assignUserPublicAccess(u, publicGroupName);
	} */
    
    /*
     * This is used to assign a user to the public group.
     * @param - User object
     */
	//public void assignUserPublicAccess(User u) throws CSTransactionException {
     //   assignUserPublicAccess(u, publicProtectionGroupName);
    //}
    
    /*
     * Used to assign a user to a specified group.
     * @param - User object
     * @param - Specified group
     */
   // public void assignUserPublicAccess(User u, String groupName) throws CSTransactionException {
    //    upm.assignUserRoleToProtectionGroup(u.getUserId().toString(), new String[] {readRoleId.toString()}, publicProtGroupId.toString());
   // }
    
	/*
	 * checkPermission method is to check permission
	 * @param	-	( String username, ProtectionElement element, String privilege )
	 * @return	-	return boolean to indicate the right permission
	 */
	public boolean checkPermission( String username, ProtectionElement element, String priviliegeName ) throws Exception {
		boolean retval = false;
		
		ProtectionElementSearchCriteria pec = new ProtectionElementSearchCriteria(element);
		List peList = upm.getObjects(pec);
		Iterator tempIter = peList.iterator();
		while( tempIter.hasNext()) {
			ProtectionElement tempPE = (ProtectionElement) tempIter.next();
			retval = upm.checkPermission( username, tempPE.getObjectId(), priviliegeName );
			if ( retval )
				return retval;
		}

		return retval;
	}
	
	/*
	 * getSecurityMap method is to get all ProtectionElementPrivilegeContext for a given user
	 * @param	-	String userId
	 * @return	-	A set of ProtectionElement
	 * 
	 */
/*
	public Set<TableProtectionElement> getSecurityMap( String userId ) throws CSObjectNotFoundException 
	{
		Set tempSet = upm.getProtectionElementPrivilegeContextForUser( userId );
		Set<TableProtectionElement> retSet = new HashSet<TableProtectionElement>();
		
		Iterator iter = tempSet.iterator();
		while ( iter.hasNext() ) {
			ProtectionElementPrivilegeContext pepc = (ProtectionElementPrivilegeContext) iter.next();
			ProtectionElement tempPe = pepc.getProtectionElement();
			retSet.add( new TableProtectionElement(tempPe));
		}
		
		return retSet;
	}
	*/
	/*
	 * getUserId method is to get userId from a given user login name
	 * @param	-	String loginName
	 * @return	-	String userId
	 */
	public String getUserId( String loginName ) {
		String userId = null;
		User user = new User();
		user.setLoginName(loginName);
		UserSearchCriteria usc = new UserSearchCriteria(user); 
		List userList = upm.getObjects( usc );
		Iterator ite = userList.iterator();

		if ( ite.hasNext() ) {
			User tempUser = (User) ite.next();
			userId = tempUser.getUserId().toString();
		}
		
		return userId;
	}
    
    /*
     * Get a list of all users.  To be used to update all users to public
     * access group.
     * @return - A list of User objects
     */
    
	
	public List<User> getAllUsers() {
        User user = new User();
        user.setLoginName("*");
        UserSearchCriteria usc = new UserSearchCriteria(user); 
        List<User> userList = upm.getObjects( usc );
        return userList;
    }
	
	/*
	 * getSecurityMap method is to get a hashtable security information for all users 
	 * @param	-	none
	 * @return	-	Hashtable: key is username, value is a set of ProtectionElement
	 */
	/*
    public Hashtable getSecurityMap() throws CSObjectNotFoundException 
    {
		
		Hashtable<User, Set> retval = new Hashtable<User, Set>();
				
		User user = new User();
		user.setLoginName("*");
		UserSearchCriteria usc = new UserSearchCriteria(user); 
		List userList = upm.getObjects( usc );
		
		Iterator ite = userList.iterator();
		while( ite.hasNext() ) {
			User tempUser = (User) ite.next();
			Set tempSet = getSecurityMap(tempUser.getUserId().toString());
			
			Iterator tempIter = tempSet.iterator();
			while ( tempIter.hasNext() ) {
				ProtectionElementPrivilegeContext pepc = (ProtectionElementPrivilegeContext) tempIter.next();
				logger.debug( "PE: " + pepc.getProtectionElement().getProtectionElementName() + "- P: " + pepc.getPrivileges() );
			}
			
			retval.put( tempUser, tempSet);
		}
		
		return retval;
	}
*/
}
