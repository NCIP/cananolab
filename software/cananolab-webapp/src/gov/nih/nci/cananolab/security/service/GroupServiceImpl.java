package gov.nih.nci.cananolab.security.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.security.Group;
import gov.nih.nci.cananolab.security.dao.GroupDao;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("groupService")
public class GroupServiceImpl implements GroupService
{
	protected Logger logger = Logger.getLogger(GroupServiceImpl.class);
	
	@Autowired
	private GroupDao groupDao;
	
	@Override
	public Group getGroupByName(String groupName)
	{
		return groupDao.getGroupByName(groupName);
	}
	
	@Override
	public Group getGroupById(Long groupId)
	{
		return groupDao.getGroupById(groupId);
	}

	@Override
	public Long createNewGroup(Group newGroup)
	{
		Long id = 0L;
		if (newGroup != null && !StringUtils.isEmpty(newGroup.getGroupName()) &&
			!StringUtils.isEmpty(newGroup.getGroupDesc()))
		{
			int status = groupDao.insertGroup(newGroup);
			Group insGroup = groupDao.getGroupByName(newGroup.getGroupName());
			if (insGroup != null)
				id = insGroup.getId();
		}
		return id;
	}

	@Override
	public int addGroupMember(Long groupId, String userName)
	{
		int status = 0;
		if (groupId != null && groupId > 0 && !StringUtils.isEmpty(userName))
			status = groupDao.inserGroupMember(groupId, userName);
		return status;
	}

	@Override
	public int updateGroup(Long groupId, String groupDesc)
	{
		int status = 0;
		if (groupId != null && groupId > 0 && !StringUtils.isEmpty(groupDesc))
			status = groupDao.updateGroup(groupId, groupDesc);
		return status;
	}

	@Override
	public List<String> getGroupMembers(Long groupId)
	{
		return groupDao.getGroupMembers(groupId);
	}

	@Override
	public int removeGroupMembers(Long groupId) {
		int status = 0;
		if (groupId != null && groupId > 0)
			status = groupDao.removeGroupMembers(groupId);
		return status;
	}
	
	@Override
	public int removeGroupMember(Long groupId, String userName)
	{
		int status = 0;
		if (groupId != null && groupId > 0)
			status = groupDao.removeGroupMember(groupId, userName);
		return status;
	}

	@Override
	public int removeGroup(Long groupId) {
		int status = 0;
		if (groupId != null && groupId > 0)
		{
			status = groupDao.removeGroupMembers(groupId);
			groupDao.deleteGroup(groupId);
		}
		return status;
	}

	@Override
	public List<Group> getGroupsAccessibleToUser(String userName) {
		List<Group> groups = groupDao.getGroupsWithMember(userName);
		return groups;
	}

	@Override
	public List<Group> getAllGroups() {
		List<Group> groups = groupDao.getAllGroups();
		return groups;
	}

}