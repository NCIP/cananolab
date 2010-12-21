package gov.nih.nci.cananolab.service.study;

import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.StudyException;

import java.util.List;

/**
 * 
 * @author lethai
 *
 */
public interface StudyService {
	
	public StudyBean findStudyById(String id, Boolean loadAccessInfo)
		throws StudyException, NoAccessException;
	
	public List<String> findStudyIdsBy() 
		throws StudyException, NoAccessException;

}
