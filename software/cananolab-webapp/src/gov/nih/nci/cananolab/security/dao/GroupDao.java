package gov.nih.nci.cananolab.security.dao;

import java.util.List;

import gov.nih.nci.cananolab.security.Group;

public interface GroupDao
{
	public Group getGroupByName(String groupName);
	
	public Group getGroupById(Long groupId);
	
	public int insertGroup(Group newGroup);
	
	public int inserGroupMember(Long groupId, String userName);
	
	public int updateGroup(Long groupId, String groupDesc);
	
	public List<String> getGroupMembers(Long groupId);
	
	public int removeGroupMembers(Long groupId);
	
	public int removeGroupMember(Long groupId, String userName);
	
	public int deleteGroup(Long groupId);
	
	public List<Group> getGroupsWithMember(String username);
	
	public List<Group> getAllGroups();

}
