/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.study.helper;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Sample;
import gov.nih.nci.cananolab.domain.common.Study;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class StudyServiceHelper extends BaseServiceHelper {

	private static Logger logger = Logger.getLogger(SampleServiceHelper.class);

	public StudyServiceHelper() {
		super();
	}

	public StudyServiceHelper(UserBean user) {
		super(user);
	}

	public StudyServiceHelper(SecurityService securityService) {
		super(securityService);
	}

	public List<String> findStudyIdsBy(String studyName,
			String studyPointOfContact, String studyType,
			String studyDesignType, String sampleName, Boolean isAnimalStudy,
			String diseases, String[] wordList, String studyOwner)
			throws Exception {
		List<String> studyIds = new ArrayList<String>();
		Study study = new Study();

		DetachedCriteria crit = DetachedCriteria.forClass(Study.class)
				.setProjection(Projections.distinct(Property.forName("id")));
		if (!StringUtils.isEmpty(studyName)) {
			TextMatchMode nameMatchMode = new TextMatchMode(studyName);
			crit.add(Restrictions.ilike("name", nameMatchMode.getUpdatedText(),
					nameMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(studyType)) {
			TextMatchMode nameMatchMode = new TextMatchMode(studyType);
			crit.add(Restrictions.ilike("type", nameMatchMode.getUpdatedText(),
					nameMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(studyOwner)) {
			TextMatchMode nameMatchMode = new TextMatchMode(studyOwner);
			crit.add(Restrictions.ilike("createdBy", nameMatchMode
					.getUpdatedText(), nameMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(studyPointOfContact)) {
			TextMatchMode pocMatchMode = new TextMatchMode(studyPointOfContact);
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("primaryPointOfContact", "pointOfContact");
			crit.createAlias("pointOfContact.organization", "organization");
			crit.createAlias("otherPointOfContactCollection", "otherPoc",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("otherPoc.organization", "otherOrg",
					CriteriaSpecification.LEFT_JOIN);
			String critStrs[] = { "pointOfContact.lastName",
					"pointOfContact.firstName", "pointOfContact.role",
					"organization.name", "otherPoc.lastName",
					"otherPoc.firstName", "otherOrg.name" };
			for (String critStr : critStrs) {
				Criterion pocCrit = Restrictions.ilike(critStr, pocMatchMode
						.getUpdatedText(), pocMatchMode.getMatchMode());
				disjunction.add(pocCrit);
			}
			crit.add(disjunction);
		}

		// keyword search on description and outcome fields
		if (wordList != null && wordList.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();

			for (String word : wordList) {
				Criterion summaryCrit1 = Restrictions.ilike("description",
						word, MatchMode.ANYWHERE);
				Criterion summaryCrit2 = Restrictions.ilike("outcome", word,
						MatchMode.ANYWHERE);
				Criterion summaryCrit = Restrictions.or(summaryCrit1,
						summaryCrit2);
				disjunction.add(summaryCrit);
			}
			crit.add(disjunction);
		}

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			String id = (obj.toString()).trim();
			studyIds.add(id);			
			if (StringUtils.containsIgnoreCase(getAccessibleData(), id)) {
				studyIds.add(id); 
			}else {
				logger.debug("User doesn't have access to study of ID: " + id);
			}
			 
		}
		return studyIds;
	}
	public Study findStudyById(String studyId) throws Exception{
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), studyId)) {
			throw new NoAccessException("User has no access to the study "
					+ studyId);
		}
		Study study = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Study.class).add(
				Property.forName("id").eq(new Long(studyId)));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("sampleCollection", FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection",
				FetchMode.JOIN);
		crit.setFetchMode("protocolCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"childStudyCollection",
						FetchMode.JOIN);		
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			study = (Study) result.get(0);
		}
		return study;
	}
	
	
	public List<Study> findStudiesBySampleId(String sampleId) throws Exception{
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		List<Study> studies = new ArrayList<Study>();
		Sample sample = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("studyCollection", FetchMode.JOIN);
		
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		for (Object obj : sample.getStudyCollection()) {
			Study study = (Study) obj;
			if (getAccessibleData().contains(study.getId().toString())) {
				studies.add(study);
			} else {
				logger.debug("User doesn't have access to study with id "
						+ study.getId());
			}
		}
		return studies;
	}
	
	public int getNumberOfPublicStudies() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getAllPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.Study");
		List results = appService.query(crit);
		List<String> publicIds = new ArrayList<String>();
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, id)) {
				publicIds.add(id);
			}
		}
		return publicIds.size();
	}
}