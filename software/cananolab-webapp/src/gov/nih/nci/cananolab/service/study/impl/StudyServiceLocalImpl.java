/**
 * 
 */
package gov.nih.nci.cananolab.service.study.impl;

import java.util.List;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.StudyException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.helper.AdvancedSampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.service.study.StudyService;

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

	@Override
	public StudyBean findStudyById(String id, Boolean loadAccessInfo)
			throws StudyException, NoAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findStudyIdsBy() throws StudyException,
			NoAccessException {
		// TODO Auto-generated method stub
		return null;
	}

}
