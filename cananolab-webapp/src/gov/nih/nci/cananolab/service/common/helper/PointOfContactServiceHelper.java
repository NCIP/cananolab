package gov.nih.nci.cananolab.service.common.helper;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Sample;
import gov.nih.nci.cananolab.domain.common.Study;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class PointOfContactServiceHelper extends BaseServiceHelper {
	public PointOfContactServiceHelper() {
		super();
	}

	public PointOfContactServiceHelper(UserBean user) {
		super(user);
	}

	public PointOfContactServiceHelper(SecurityService securityService) {
		super(securityService);
	}

	public PointOfContact findPointOfContactById(String pocId) throws Exception {
		PointOfContact poc = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(PointOfContact.class)
				.add(Property.forName("id").eq(new Long(pocId)));
		crit.setFetchMode("organization", FetchMode.JOIN);
		List results = appService.query(crit);
		for (Object obj : results) {
			poc = (PointOfContact) obj;
		}
		return poc;
	}

	public PointOfContact findPointOfContactByNameAndOrg(String firstName,
			String lastName, String orgName) throws Exception {
		PointOfContact poc = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(PointOfContact.class);
		crit.createAlias("organization", "organization");
		if (!StringUtils.isEmpty(lastName))
			crit.add(Restrictions.eq("lastName", lastName));
		if (!StringUtils.isEmpty(firstName))
			crit.add(Restrictions.eq("firstName", firstName));
		if (!StringUtils.isEmpty(orgName))
			crit.add(Restrictions.eq("organization.name", orgName));
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List results = appService.query(crit);
		for (Object obj : results) {
			poc = (PointOfContact) obj;
			if (getAccessibleData().contains(poc.getId().toString())) {
				return poc;
			} else {
				throw new NoAccessException(
						"User has no access to the point of contact");
			}
		}
		return poc;
	}

	public List<PointOfContact> findPointOfContactsBySampleId(String sampleId)
			throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		List<PointOfContact> pointOfContacts = new ArrayList<PointOfContact>();
		for (Object obj : results) {
			Sample sample = (Sample) obj;
			PointOfContact primaryPOC = sample.getPrimaryPointOfContact();
			if (getAccessibleData().contains(primaryPOC.getId().toString())) {
				pointOfContacts.add(primaryPOC);
			} else { // ignore no access exception
				logger
						.debug("User doesn't have access to the primary POC with org name "
								+ primaryPOC.getOrganization().getName());
			}
			Collection<PointOfContact> otherPOCs = sample
					.getOtherPointOfContactCollection();
			for (PointOfContact poc : otherPOCs) {
				if (getAccessibleData().contains(poc.getId().toString())) {
					pointOfContacts.add(poc);
				} else { // ignore no access exception
					logger
							.debug("User doesn't have access to the POC with org name "
									+ poc.getOrganization().getName());
				}
			}
		}
		return pointOfContacts;
	}

	public List<PointOfContact> findPointOfContactsByStudyId(String studyId)
			throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), studyId)) {
			throw new NoAccessException("User has no access to the study "
					+ studyId);
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Study.class).add(
				Property.forName("id").eq(new Long(studyId)));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		List<PointOfContact> pointOfContacts = new ArrayList<PointOfContact>();
		for (Object obj : results) {
			Study study= (Study) obj;
			PointOfContact primaryPOC = study.getPrimaryPointOfContact();
			if (getAccessibleData().contains(primaryPOC.getId().toString())) {
				pointOfContacts.add(primaryPOC);
			} else { // ignore no access exception
				logger
						.debug("User doesn't have access to the primary POC with org name "
								+ primaryPOC.getOrganization().getName());
			}
			Collection<PointOfContact> otherPOCs = study
					.getOtherPointOfContactCollection();
			for (PointOfContact poc : otherPOCs) {
				if (getAccessibleData().contains(poc.getId().toString())) {
					pointOfContacts.add(poc);
				} else { // ignore no access exception
					logger
							.debug("User doesn't have access to the POC with org name "
									+ poc.getOrganization().getName());
				}
			}
		}
		return pointOfContacts;
	}

	public Organization findOrganizationByName(String orgName) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Organization.class);
		crit.add(Restrictions.eq("name", orgName).ignoreCase());
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List results = appService.query(crit);
		Organization org = null;
		for (Object obj : results) {
			org = (Organization) obj;
		}
		return org;
	}

	public PointOfContact findPrimaryPointOfContactBySampleId(String sampleId)
			throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		PointOfContact poc = null;
		for (Object obj : results) {
			Sample sample = (Sample) obj;
			poc = sample.getPrimaryPointOfContact();
		}
		if (poc != null) {
			if (!getAccessibleData().contains(poc.getId().toString())) {
				throw new NoAccessException(
						"User has no access to the point of contact "
								+ poc.getId());
			}
		}
		return poc;
	}
}
