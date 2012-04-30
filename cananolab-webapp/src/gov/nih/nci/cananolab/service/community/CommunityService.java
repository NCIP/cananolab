package gov.nih.nci.cananolab.service.community;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;

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

	public List<String> findCollaborationGroupIdsByOwner(String owner)
			throws CommunityException;

	public void assignOwner(String collaborationGroupId, String ownerLogin)
			throws CommunityException, NoAccessException;

	public void assignAccessibility(AccessibilityBean access,
			String collaborationGroupId) throws CommunityException,
			NoAccessException;

	public void removeAccessibility(AccessibilityBean access,
			String collaborationGroupId) throws CommunityException,
			NoAccessException;
}