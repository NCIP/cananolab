package gov.nih.nci.cananolab.service.community;

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.exception.CommunityException;
import gov.nih.nci.cananolab.exception.NoAccessException;

import java.util.List;

public interface CommunityService {
	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException;

	public List<CollaborationGroupBean> findCollaborationGroups()
			throws CommunityException;

	public CollaborationGroupBean findCollaborationGroupById(String id)
			throws CommunityException;

	public void deleteCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException, NoAccessException;
}
