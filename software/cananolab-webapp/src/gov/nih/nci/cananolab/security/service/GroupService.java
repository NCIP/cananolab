package gov.nih.nci.cananolab.security.service;

import java.util.List;

import gov.nih.nci.cananolab.security.Group;

public interface GroupService 
{
	public Group getGroupByName(String groupName);
	
	public Group getGroupById(Long groupId);
	
	public Long createNewGroup(Group newGroup);
	
	public int addGroupMember(Long groupId, String userName);
	
	public int updateGroup(Long groupId, String groupDesc);
	
	public List<String> getGroupMembers(Long groupId);
	
	public int removeGroupMembers(Long groupId);
	
	public int removeGroupMember(Long groupId, String userName);
	
	public int removeGroup(Long groupId);
	
	public List<Group> getGroupsAccessibleToUser(String userName);
	
	public List<Group> getAllGroups();

}
