package gov.nih.nci.cananolab.service.community;

import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.exception.CommunityException;

import java.util.List;

public interface CommunityService {
	public void saveCollaborationGroup(CollaborationGroupBean collaborationGroup)
			throws CommunityException;

	public List<CollaborationGroupBean> findCollaborationGroups()
			throws CommunityException;
}
