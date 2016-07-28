package gov.nih.nci.cananolab.security.dao;

import java.util.List;

import gov.nih.nci.cananolab.security.CananoUserDetails;

public interface UserDao 
{
	public CananoUserDetails getUserByName(String username);
	
	public List<String> getUserGroups(String username);

	public List<String> getUserRoles(String username);

	public List<CananoUserDetails> getUsers(String likeStr);

}
