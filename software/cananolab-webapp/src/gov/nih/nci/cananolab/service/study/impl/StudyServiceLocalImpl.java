/**
 * 
 */
package gov.nih.nci.cananolab.service.study.impl;

import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.StudyException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.service.study.StudyService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author lethai
 *
 */
public class StudyServiceLocalImpl extends BaseServiceLocalImpl implements StudyService {
	
	private static Logger logger = Logger
	.getLogger(StudyServiceLocalImpl.class);

	//private SampleServiceHelper helper;
	//private AdvancedSampleServiceHelper advancedHelper;
	//private CharacterizationServiceLocalImpl charService;
	//private CompositionServiceLocalImpl compService;
	//private PublicationServiceLocalImpl publicationService;
	
	public StudyServiceLocalImpl() {
	super();
	//helper = new SampleServiceHelper(this.securityService);
	//charService = new CharacterizationServiceLocalImpl(this.securityService);
	//compService = new CompositionServiceLocalImpl(this.securityService);
	//publicationService = new PublicationServiceLocalImpl(
	//		this.securityService);
	//advancedHelper = new AdvancedSampleServiceHelper(this.securityService);
	}
	
	public StudyServiceLocalImpl(UserBean user) {
	super(user);
	//helper = new SampleServiceHelper(user);
	//charService = new CharacterizationServiceLocalImpl(this.securityService);
	//compService = new CompositionServiceLocalImpl(this.securityService);
	//publicationService = new PublicationServiceLocalImpl(
	//		this.securityService);
	}
	
	public StudyServiceLocalImpl(SecurityService securityService) {
	super(securityService);
	
	}


	public StudyBean findStudyById(String id, Boolean loadAccessInfo)
			throws StudyException, NoAccessException {
		// TODO Auto-generated method stub
		return new StudyBean();
	}


	public List<String> findStudyIdsBy() throws StudyException{
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}


	public int getNumberOfPublicStudies() throws StudyException {
		// TODO Auto-generated method stub
		return 0;
	}

}
