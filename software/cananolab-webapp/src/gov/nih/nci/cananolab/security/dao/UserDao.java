package gov.nih.nci.cananolab.security.dao;

import java.util.List;

import gov.nih.nci.cananolab.security.CananoUserDetails;

public interface UserDao 
{
	public CananoUserDetails getUserByName(String username);
	
	public List<String> getUserGroups(String username);

	public List<String> getUserRoles(String username);

	public List<CananoUserDetails> getUsers(String likeStr);

	public int insertUser(CananoUserDetails user);
	
	public int updateUser(CananoUserDetails user);

	public int insertUserAuthority(String userName, String authority);
	
	public int resetPassword(String userName, String password);
	
	public String readPassword(String userName);
	
	public int deleteUserAssignedRoles(String username);

}
