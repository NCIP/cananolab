/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.community.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.Group;
import gov.nih.nci.cananolab.security.dao.AclDao;
import gov.nih.nci.cananolab.security.enums.AccessTypeEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.GroupService;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("communityService")
public class CommunityServiceLocalImpl extends BaseServiceLocalImpl implements CommunityService
{
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private AclDao aclDao;

	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup) throws CommunityException, NoAccessException, DuplicateEntriesException
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		if (userDetails == null)
			throw new NoAccessException();
		try {
			// creating a new group
			if (StringUtils.isEmpty(collaborationGroup.getId()))
			{
				// check if group name is already used
				Group doGroup = groupService.getGroupByName(collaborationGroup.getName());
				// if group name already exists by another id
				if (doGroup != null) {
					throw new DuplicateEntriesException("Group name is already in use");
				}
				// create a new group
				else {
					createNewCollaborationGroup(collaborationGroup);
				}
			}
			// update existing group
			else {
				// check if user has access to update the existing group
				// if user has access to update the group
				if (!userDetails.getGroups().contains(collaborationGroup.getName()))
					throw new NoAccessException("You don't have access to update the collaboration group");
				updateCollaborationGroup(collaborationGroup);
			}
		} catch (Exception e) {
			String error = "Error finding existing collaboration groups accessible by the user";
			throw new CommunityException(error, e);
		}
	}

	private void createNewCollaborationGroup(CollaborationGroupBean collaborationGroup) throws Exception
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		
		Group doGroup = new Group();
		doGroup.setGroupName(collaborationGroup.getName());
		doGroup.setGroupDesc(collaborationGroup.getDescription());
		doGroup.setCreatedBy(userDetails.getUsername());
		Long newId = groupService.createNewGroup(doGroup);
		collaborationGroup.setId(newId.toString());
		doGroup.setId(newId);
		
		AccessControlInfo acinfo = new AccessControlInfo();
		acinfo.setRecipient(userDetails.getUsername());
		acinfo.setAccessType(AccessTypeEnum.USER.getAccessType());
		collaborationGroup.addUserAccess(acinfo);
		
		// add owner access to the group
		// add curator default CURD access to the group
		springSecurityAclService.saveDefaultAccessForNewObject(newId, SecureClassesEnum.COLLABORATIONGRP.getClazz());

		// update current user's associated groups shown on the side menu
		userDetails.addGroup(collaborationGroup.getName());

		// add users to the collaboration group
		addUsersToCollaborationGroup(collaborationGroup.getId(), collaborationGroup.getUserAccesses());
	}

	private void updateCollaborationGroup(CollaborationGroupBean collaborationGroup) throws Exception 
	{
		Long groupId = Long.valueOf(collaborationGroup.getId());
		
		//allow for only update of group desc. else delete group and recreate it.
		// get existing database group based on the group id
		if (!StringUtils.isEmpty(collaborationGroup.getDescription()))
		{
			groupService.updateGroup(groupId, collaborationGroup.getDescription());
		}

		groupService.removeGroupMembers(groupId);
		
		// add users to the collaboration group
		addUsersToCollaborationGroup(collaborationGroup.getId(), collaborationGroup.getUserAccesses());
	}

	private void addUsersToCollaborationGroup(String groupId, List<AccessControlInfo> userAccesses) throws Exception
	{
		// add users to the collaboration group
		if (userAccesses != null && userAccesses.size() > 0)
			for (AccessControlInfo access : userAccesses) {
				// CSM implementation - skip if user is curator because curator is treated as a default group access
				// new implementation - removing the check to see if user is curator, 
				// to handle the condition if at a later time curator role is dropped for the user
				groupService.addGroupMember(Long.valueOf(groupId), access.getRecipient());
			}
	}

	public List<CollaborationGroupBean> findCollaborationGroups() throws CommunityException
	{
		CananoUserDetails currUser = SpringSecurityUtil.getPrincipal();
		List<Group> groupList = new ArrayList<>();
		if (currUser.isCurator())
			groupList = groupService.getAllGroups();
		else
			groupList = groupService.getGroupsAccessibleToUser(currUser.getUsername());
		
		List<CollaborationGroupBean> collaborationGroups = new ArrayList<CollaborationGroupBean>();
		try {
			if (groupList != null && groupList.size() > 0)
			{
				for (Group group : groupList)
				{
					CollaborationGroupBean cGroup = new CollaborationGroupBean();
					collaborationGroups.add(cGroup);
					cGroup.setId(group.getId() + "");
					cGroup.setName(group.getGroupName());
					cGroup.setDescription(group.getGroupDesc());
					cGroup.setCreatedBy(group.getCreatedBy());
					cGroup.setOwnerName(group.getCreatedBy());
					List<String> members = groupService.getGroupMembers(group.getId());
					//TODO: should they be fetched as AccessControlInfo or changed to List<String>
					for (String member : members)
					{
						AccessControlInfo access = new AccessControlInfo();
						access.setRecipient(member);
						access.setRecipientDisplayName(member);
						access.setAccessType(AccessTypeEnum.USER.getAccessType());
						cGroup.addUserAccess(access);
					}
				}
			}
		} catch (Exception e) {
			String error = "Error finding existing collaboration groups accessible by the user";
			throw new CommunityException(error, e);
		}
		return collaborationGroups;
	}

	public void assignOwner(String collaborationGroupId, String ownerLogin) throws CommunityException, NoAccessException
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		// user needs to be both curator and admin
		if (!(userDetails.isCurator() && userDetails.isAdmin())) {
			throw new NoAccessException();
		}
		try {
			Long cGroupId = Long.valueOf(collaborationGroupId);
			springSecurityAclService.updateObjectOwner(cGroupId, SecureClassesEnum.COLLABORATIONGRP.getClazz(), ownerLogin);
			
			// add new owner to the colloborationGroup
			groupService.addGroupMember(cGroupId, ownerLogin);
			// if ownerLogin is not a curator, save owner access

		} catch (Exception e) {
			String error = "Error assigning an owner to the collaboration group by Id " + collaborationGroupId;
			logger.error(error, e);
			throw new CommunityException(error, e);
		}
	}

	public CollaborationGroupBean findCollaborationGroupById(String id) throws CommunityException
	{
		CollaborationGroupBean cGroup = null;
		
		if (!StringUtils.isEmpty(id))
		{
			try{
				Long groupId = Long.valueOf(id);
				Group group = groupService.getGroupById(groupId);
				cGroup = new CollaborationGroupBean();
				cGroup.setId(group.getId() + "");
				cGroup.setName(group.getGroupName());
				cGroup.setDescription(group.getGroupDesc());
				
				//check if the user has access to the group else log error and return null
				CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
				if (userDetails.getGroups().contains(group.getGroupName()))
				{
					List<String> members = groupService.getGroupMembers(group.getId());
					//TODO: should they be fetched as AccessControlInfo or changed to List<String>
					for (String member : members)
					{
						AccessControlInfo access = new AccessControlInfo();
						access.setRecipient(member);
						access.setAccessType(AccessTypeEnum.USER.getAccessType());
						cGroup.addUserAccess(access);
					}
				}
				else {
					String error = "User has no access to the collaboration group " + group.getGroupName();
					logger.info(error);
				}
				
			} catch (Exception e) {
				String error = "Error retrieving the collaboration group by name";
				throw new CommunityException(error, e);
			}
		}
		return cGroup;
	}

	public void deleteCollaborationGroup(CollaborationGroupBean collaborationGroup) throws CommunityException, NoAccessException
	{
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			throw new NoAccessException();
		}
		try {
			// check if user has access to delete the group
			Long id = Long.valueOf(collaborationGroup.getId());
			if (!springSecurityAclService.isOwnerOfObject(id, SecureClassesEnum.COLLABORATIONGRP.getClazz())) {
				throw new NoAccessException();
			} else {
				//delete all access for objects to group
				aclDao.deleteAllAccessToSid(collaborationGroup.getName());
				
				groupService.removeGroup(id);
				
				// update current user's associated groups shown on the side menu
				CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
				userDetails.removeGroup(collaborationGroup.getName());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error deleting the collaboration group";
			throw new CommunityException(error, e);
		}
	}

	public void assignAccessibility(AccessControlInfo access, String collaborationGroupId) throws CommunityException, NoAccessException
	{
		try {
			Long groupId = Long.valueOf(collaborationGroupId);
			if (!springSecurityAclService.isOwnerOfObject(groupId, SecureClassesEnum.COLLABORATIONGRP.getClazz())) {
				throw new NoAccessException();
			}
			springSecurityAclService.saveAccessForObject(Long.valueOf(collaborationGroupId), SecureClassesEnum.COLLABORATIONGRP.getClazz(), SpringSecurityUtil.getLoggedInUserName(), access.isPrincipal(), access.getRoleName());

		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error assigning accessibility to collaboration group: "
					+ collaborationGroupId;
			throw new CommunityException(error, e);
		}
	}

	public void removeAccessibility(AccessControlInfo access, String collaborationGroupId) throws CommunityException, NoAccessException
	{
		try {
			Long groupId = Long.valueOf(collaborationGroupId);
			if (!springSecurityAclService.isOwnerOfObject(groupId, SecureClassesEnum.COLLABORATIONGRP.getClazz())) {
				throw new NoAccessException();
			}
			springSecurityAclService.retractAccessToObjectForSid(groupId, SecureClassesEnum.COLLABORATIONGRP.getClazz(), access.getRecipient());
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error removing accessibility to collaboration group: " + collaborationGroupId;
			throw new CommunityException(error, e);
		}
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

}