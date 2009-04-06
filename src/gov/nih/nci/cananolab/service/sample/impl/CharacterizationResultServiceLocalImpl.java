package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.service.sample.CharacterizationResultService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class CharacterizationResultServiceLocalImpl implements
		CharacterizationResultService {

	private static Logger logger = Logger
			.getLogger(CharacterizationResultServiceLocalImpl.class);

	public Finding findFindingById(String findingId)
			throws CharacterizationResultException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Finding.class)
					.add(Property.forName("id").eq(new Long(findingId)));
			crit.setFetchMode("datumCollection", FetchMode.JOIN);
			crit.setFetchMode(
					"datumCollection.conditionCollection",
					FetchMode.JOIN);
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			List result = appService.query(crit);
			Finding finding = null;
			if (!result.isEmpty()) {
				finding = (Finding) result.get(0);
			}
			return finding;
		} catch (Exception e) {
			String err = "Error getting finding of ID " + findingId;
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}

	public List<Datum> getDataForFinding(String findingId)
			throws CharacterizationResultException {
		List<Datum> data = new ArrayList<Datum>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Datum.class).add(
					Restrictions.eq("findingCollection.id", new Long(findingId)))
					.addOrder(Order.asc("createdDate"));
			crit.setFetchMode("conditionCollection", FetchMode.JOIN);
			List result = appService.query(crit);

			for (Object obj : result) {
				Datum datum = (Datum) obj;
				data.add(datum);
			}
			return data;
		} catch (Exception e) {
			String err = "Error getting data from finding " + findingId;
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}

	public void saveData(List<Datum> data)
			throws CharacterizationResultException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			for (Datum datum : data) {
				appService.saveOrUpdate(datum);
			}
		} catch (Exception e) {
			String err = "Error saving characterization result data. ";
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}

	public void saveFinding(Finding finding)
			throws CharacterizationResultException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.saveOrUpdate(finding);
			// TODO special handling for file
		} catch (Exception e) {
			String err = "Error saving characterization result finding. ";
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}

	public void deleteFinding(Finding finding)
			throws CharacterizationResultException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			authService.removePublicGroup(finding.getId().toString());
			appService.delete(finding);

		} catch (Exception e) {
			String err = "Error deleting finding " + finding.getId();
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}
}
