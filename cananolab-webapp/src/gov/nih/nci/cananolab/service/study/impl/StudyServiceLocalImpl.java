/**
 * 
 */
package gov.nih.nci.cananolab.service.study.impl;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Sample;
import gov.nih.nci.cananolab.domain.common.Study;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.StudyException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.service.study.StudyService;
import gov.nih.nci.cananolab.service.study.helper.StudyServiceHelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Service methods for studies
 * 
 * @author lethai *
 */
public class StudyServiceLocalImpl extends BaseServiceLocalImpl implements
		StudyService {

	private static Logger logger = Logger
			.getLogger(StudyServiceLocalImpl.class);

	private StudyServiceHelper helper;

	// private AdvancedSampleServiceHelper advancedHelper;
	// private CharacterizationServiceLocalImpl charService;
	// private CompositionServiceLocalImpl compService;
	// private PublicationServiceLocalImpl publicationService;

	public StudyServiceLocalImpl() {
		super();
		helper = new StudyServiceHelper(this.securityService);
		// charService = new
		// CharacterizationServiceLocalImpl(this.securityService);
		// compService = new CompositionServiceLocalImpl(this.securityService);
		// publicationService = new PublicationServiceLocalImpl(
		// this.securityService);
		// advancedHelper = new
		// AdvancedSampleServiceHelper(this.securityService);
	}

	public StudyServiceLocalImpl(UserBean user) {
		super(user);
		helper = new StudyServiceHelper(user);
		// charService = new
		// CharacterizationServiceLocalImpl(this.securityService);
		// compService = new CompositionServiceLocalImpl(this.securityService);
		// publicationService = new PublicationServiceLocalImpl(
		// this.securityService);
	}

	public StudyServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new StudyServiceHelper(this.securityService);
	}

	public StudyBean findStudyById(String id, Boolean loadAccessInfo)
			throws StudyException, NoAccessException {

		StudyBean studyBean = null;

		try {
			Study study = helper.findStudyById(id);
			if (study != null) {
				if (loadAccessInfo) {
					studyBean = loadStudyBean(study);
				} else {
					studyBean = new StudyBean(study);
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the study by id: " + id;
			logger.error(err, e);
			throw new StudyException(err, e);
		}
		return studyBean;

		// testing
		/*
		 * StudyBean studyBean = new StudyBean(); Study domain = new Study();
		 * domain.setId(new Long(id)); domain.setName("MIT_KELLY");
		 * domain.setTitle("in vitro profiling of nanoparticle libraries"); //
		 * study1.setStudySample(new SampleBean());
		 * domain.setCreatedBy("michal"); PointOfContact primaryPoc = new
		 * PointOfContact(); primaryPoc.setId(new Long(2));
		 * primaryPoc.setFirstName("Stanley"); primaryPoc.setLastName("Saw");
		 * Organization org = new Organization(); org.setId(2L);
		 * org.setName("MIT_MGH"); primaryPoc.setOrganization(org);
		 * 
		 * domain.setPrimaryPointOfContact(primaryPoc);
		 * 
		 * List<SampleBean> studySamples = new ArrayList<SampleBean>(); Sample
		 * sample1 = new Sample(); sample1.setName("MIT_MGH-KKellyIB2009-01");
		 * SampleBean sampleBean1 = new SampleBean();
		 * sampleBean1.setDomain(sample1);
		 * 
		 * Sample sample2 = new Sample();
		 * sample1.setName("MIT_MGH-KKellyIB2009-02"); SampleBean sampleBean2 =
		 * new SampleBean(); sampleBean2.setDomain(sample2);
		 * 
		 * Publication pub = new Publication(); pub.setId(new Long(1));
		 * pub.setDescription("Testing data"); pub.setName("Publication name");
		 * pub.setType("report"); pub.setStatus("published"); List<Publication>
		 * studyPublications = new ArrayList<Publication>();
		 * studyPublications.add(pub);
		 * domain.setPublicationCollection(studyPublications);
		 * 
		 * studySamples.add(sampleBean1); studySamples.add(sampleBean2);
		 * studyBean.setDomain(domain); studyBean.setStudySample(studySamples);
		 * return studyBean;
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cananolab.service.study.StudyService#findStudyIdsBy(java.
	 * lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.Boolean, java.lang.String,
	 * java.lang.String[], java.lang.String)
	 */
	public List<String> findStudyIdsBy(String studyName,
			String studyPointOfContact, String studyType,
			String studyDesignType, String sampleName, Boolean isAnimalStudy,
			String diseases, String[] wordList, String studyOwner)
			throws Exception {

		try {
			List<String> studyIds = new ArrayList<String>();
			studyIds = helper.findStudyIdsBy(studyName, studyPointOfContact,
					studyType, studyDesignType, sampleName, isAnimalStudy,
					diseases, wordList, studyOwner);
			return studyIds;
		} catch (Exception e) {
			String err = "Problem finding studies with the given search parameters.";
			logger.error(err, e);
			throw new StudyException(err, e);
		}

		// testing
		/*
		 * List<String> results = new ArrayList<String>(); results.add("1");
		 * results.add("2"); results.add("3"); results.add("4");
		 * results.add("5"); results.add("6"); results.add("7");
		 * results.add("8"); results.add("9"); results.add("10");
		 * results.add("11"); results.add("12"); return results;
		 */
	}

	public int getNumberOfPublicStudies() throws StudyException {
		try {
			int count = helper.getNumberOfPublicStudies();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public studies.";
			logger.error(err, e);
			throw new StudyException(err, e);

		}
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

	public List<SampleBean> findSamplesByStudyId(String studyId)
			throws StudyException {
		List<SampleBean> samplesBean = new ArrayList<SampleBean>();

		try {
			List<Sample> samples = helper.findSamplesByStudyId(studyId);
			if (samples != null) {
				for (Sample sample : samples) {
					SampleBean sampleBean = new SampleBean(sample);
					samplesBean.add(sampleBean);
				}
			}
		} catch (Exception e) {
			String err = "Problem finding the samples by study id: " + studyId;
			logger.error(err, e);
			throw new StudyException(err, e);
		}
		return samplesBean;
	}

	public List<PublicationBean> findPublicationsByStudyId(String studyId)
			throws StudyException {
		List<PublicationBean> publicationsBean = new ArrayList<PublicationBean>();
		try {
			List<Publication> publications = helper
					.findPublicationsByStudyId(studyId);

			if (publications != null) {
				for (Publication pub : publications) {
					PublicationBean pubBean = new PublicationBean(pub);
					publicationsBean.add(pubBean);
				}
			}
		} catch (Exception e) {
			String err = "Problem finding the study by id: " + studyId;
			logger.error(err, e);
			throw new StudyException(err, e);
		}
		return publicationsBean;

	}

	public StudyServiceHelper getHelper() {
		return helper;
	}

	private StudyBean loadStudyBean(Study study) throws Exception {
		StudyBean studyBean = new StudyBean(study);
		if (user != null) {
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(study.getId().toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(study.getId().toString());
			studyBean.setUserAccesses(userAccesses);
			studyBean.setGroupAccesses(groupAccesses);
			studyBean.setUser(user);
		}
		return studyBean;
	}

}
