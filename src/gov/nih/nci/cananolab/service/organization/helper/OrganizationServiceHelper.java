package gov.nih.nci.cananolab.service.organization.helper;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of OrganizationService and grid service *
 * 
 * @author tanq
 * 
 */
public class OrganizationServiceHelper {
	private static Logger logger = Logger
			.getLogger(OrganizationServiceHelper.class);

	public OrganizationBean findPrimaryOrganization(String particleId)
			throws PointOfContactException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Organization.class);
			crit.createAlias("primaryNanoparticleSampleCollection", "sample",
					CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("sample.id", new Long(particleId)));
			crit.setFetchMode("pointOfContactCollection", FetchMode.JOIN);
			//TODO: to verify if need?
			crit.setFetchMode("primaryNanoparticleSampleCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			OrganizationBean primaryOrganization = null;
			for (Object obj : results) {
				primaryOrganization = new OrganizationBean((Organization) obj);
				// if (loadAuthor) {
				// publicationCollection.add(new PublicationBean(publication,
				// false, true));
				// } else {
				// publicationCollection.add(new PublicationBean(publication));
				// }
			}
			return primaryOrganization;
		} catch (Exception e) {
			String err = "Problem finding primary organization with the given particle ID.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public List<OrganizationBean> findOtherOrganizationCollection(
			String particleId) throws PointOfContactException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria
					.forClass(Organization.class);
			crit.createAlias("nanoparticleSampleCollection", "sample",
					CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("sample.id", new Long(particleId)));
			crit.setFetchMode("pointOfContactCollection", FetchMode.JOIN);
			//TODO: to verify if need?
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);

			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			List<OrganizationBean> otherOrganizationCollection = new ArrayList<OrganizationBean>();
			for (Object obj : results) {
				Organization organization = (Organization) obj;
				otherOrganizationCollection.add(new OrganizationBean(
						organization));
				// if (loadAuthor) {
				// publicationCollection.add(new PublicationBean(publication,
				// false, true));
				// } else {
				// publicationCollection.add(new PublicationBean(publication));
				// }
			}
			return otherOrganizationCollection;
		} catch (Exception e) {
			String err = "Problem finding other organization collections with the given particle ID.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

}
