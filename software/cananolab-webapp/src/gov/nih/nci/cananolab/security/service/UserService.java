package gov.nih.nci.cananolab.security.service;

import java.util.List;

import gov.nih.nci.cananolab.security.CananoUserDetails;

public interface UserService
{
	public List<CananoUserDetails> loadUsers(String matchStr);

	public List<String> getGroupsAccessibleToUser(String matchStr);
	
	public void createUserAccount(CananoUserDetails userDetails);
	
	public int resetPasswordForUser(String oldPassword, String newPassword, String userName) throws Exception;
	
	public void updateUserAccount(CananoUserDetails userDetails);

}
