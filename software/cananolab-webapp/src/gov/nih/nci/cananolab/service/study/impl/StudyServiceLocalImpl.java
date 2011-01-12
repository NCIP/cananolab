/**
 * 
 */
package gov.nih.nci.cananolab.service.study.impl;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Sample;
import gov.nih.nci.cananolab.domain.common.Study;
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
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
		//testing
		Study domain = new Study();
		domain.setId(new Long(id));
		domain.setName("MIT_KELLY");		
		domain.setTitle("in vitro profiling of nanoparticle libraries");
		//study1.setStudySample(new SampleBean());
		domain.setCreatedBy("michal");
		PointOfContact primaryPoc = new PointOfContact();
		primaryPoc.setFirstName("Stanley");
		primaryPoc.setLastName("Saw");
		Organization org = new Organization();
		org.setName("MIT_MGH");
		primaryPoc.setOrganization(org);
		
		domain.setPrimaryPointOfContact(primaryPoc);
		
		List<SampleBean> studySamples = new ArrayList<SampleBean>();
		Sample sample1 = new Sample();
		sample1.setName("MIT_MGH-KKellyIB2009-01");
		SampleBean sampleBean1 = new SampleBean();
		sampleBean1.setDomain(sample1);
		
		Sample sample2 = new Sample();
		sample1.setName("MIT_MGH-KKellyIB2009-02");
		SampleBean sampleBean2 = new SampleBean();
		sampleBean2.setDomain(sample2);
		studySamples.add(sampleBean1);
		studySamples.add(sampleBean2);
		studyBean.setDomain(domain);
		studyBean.setStudySample(studySamples);
		return studyBean;
	}


	public List<String> findStudyIdsBy(String studyName, String studyPointOfContact, String studyType, String studyDesignType,
			String sampleName, Boolean isAnimalStudy, String diseases, String[] wordList, String studyOwner) throws StudyException{
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


	public StudyBean cloneStudy(String orginalName, String newName)
			throws StudyException {
		return null;
	}

	
	public void deleteStudy(String id) throws StudyException {
		// TODO Auto-generated method stub
		
	}


	public void saveStudy(StudyBean studyBean) throws StudyException {
		// TODO Auto-generated method stub
		
	}

}
