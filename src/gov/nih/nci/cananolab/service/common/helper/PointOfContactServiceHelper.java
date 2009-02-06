package gov.nih.nci.cananolab.service.common.helper;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of OrganizationService and grid service *
 *
 * @author tanq
 *
 */
public class PointOfContactServiceHelper {
	private static Logger logger = Logger
			.getLogger(PointOfContactServiceHelper.class);

	public List<PointOfContactBean> findOtherPointOfContactCollection(
			String particleId) throws PointOfContactException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleSample.class).add(
					Property.forName("id").eq(new Long(particleId)));
			crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
			crit
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);		
			List<PointOfContactBean> otherPointOfContactCollection = new ArrayList<PointOfContactBean>();
			for (Object obj : results) {				
				NanoparticleSample particle = (NanoparticleSample) obj;
				Collection<PointOfContact> otherPOCs = particle
						.getOtherPointOfContactCollection();
				for (PointOfContact poc : otherPOCs) {
					otherPointOfContactCollection.add(new PointOfContactBean(
							poc));
				}
			}
			return otherPointOfContactCollection;
		} catch (Exception e) {
			String err = "Problem finding other PointOfContact collections with the given particle ID.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public PointOfContactBean findPointOfContactById(String POCId)
			throws PointOfContactException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(PointOfContact.class);
			crit.setFetchMode("organization", FetchMode.JOIN);
			crit.add(Restrictions.eq("id", new Long(POCId)));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			PointOfContactBean poc = null;
			for (Object obj : results) {
				poc = new PointOfContactBean((PointOfContact) obj);
			}
			return poc;
		} catch (Exception e) {
			String err = "Problem finding PointOfContact with the given POC ID "
					+ POCId;
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

}
