package gov.nih.nci.cananolab.service.community;

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommunityService extends BaseService {
	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException,
			DuplicateEntriesException;

	public List<CollaborationGroupBean> findCollaborationGroups()
			throws CommunityException;

	public CollaborationGroupBean findCollaborationGroupById(String id)
			throws CommunityException;

	public void deleteCollaborationGroup(
			CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException;
	
	public Map<String, String> findCollaborationGroupByGroupName(String groupName)
			throws Exception, NoAccessException;
	public void transferOwner(Set<String> collaborationGroupIds, String newGroupName) 
			throws CommunityException, NoAccessException;
}