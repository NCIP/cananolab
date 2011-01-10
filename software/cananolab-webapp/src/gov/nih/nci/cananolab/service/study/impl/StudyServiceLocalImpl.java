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
		StudyBean studyBean = new StudyBean();
		
	//	studyBean.set
		return new StudyBean();
	}


	public List<String> findStudyIdsBy() throws StudyException{
		List<String> results = new ArrayList<String>();
		results.add("1");
		results.add("2");
		results.add("3");
		results.add("4");
		results.add("5");
		results.add("6");
		results.add("7");
		results.add("8");
		results.add("9");
		results.add("10");
		results.add("11");
		results.add("12");
		return results;
	}


	public int getNumberOfPublicStudies() throws StudyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public StudyBean cloneStudy(String orginalName, String newName)
			throws StudyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteStudy(String id) throws StudyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveStudy(StudyBean studyBean) throws StudyException {
		// TODO Auto-generated method stub
		
	}

}
