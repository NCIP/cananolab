package gov.nih.nci.cananolab.security.service;

import java.util.List;

import gov.nih.nci.cananolab.security.CananoUserDetails;

public interface UserService
{
	public List<CananoUserDetails> loadUsers(String matchStr);

	public List<String> getGroupsAccessibleToUser(String matchStr);

}
