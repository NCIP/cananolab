package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class NanoparticleCharacterizationResultServiceLocalImpl implements
		NanoparticleCharacterizationResultService {

	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationResultServiceLocalImpl.class);

	public DataSet findDataSetById(String dataSetId)
			throws CharacterizationResultException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(DataSet.class)
					.add(Property.forName("id").eq(new Long(dataSetId)));
			crit.setFetchMode("datumCollection", FetchMode.JOIN);
			crit.setFetchMode("datumCollection.dataRow", FetchMode.JOIN);
			crit.setFetchMode("datumCollection.conditionCollection",
					FetchMode.JOIN);
			crit.setFetchMode("file", FetchMode.JOIN);
			List result = appService.query(crit);
			DataSet dataSet = null;
			if (!result.isEmpty()) {
				dataSet = (DataSet) result.get(0);
			}
			return dataSet;
		} catch (Exception e) {
			String err = "Error getting data set of ID " + dataSetId;
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}

	public List<Datum> getDataForDataSet(String dataSetId)
			throws CharacterizationResultException {
		List<Datum> data = new ArrayList<Datum>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Datum.class).add(
					Restrictions.eq("dataSet.id", new Long(dataSetId)))
					.addOrder(Order.asc("createdDate"));
			crit.setFetchMode("dataRow", FetchMode.JOIN);
			crit.setFetchMode("dataSet", FetchMode.JOIN);
			crit.setFetchMode("conditionCollection", FetchMode.JOIN);
			List result = appService.query(crit);

			for (Object obj : result) {
				Datum datum = (Datum) obj;
				data.add(datum);
			}
			return data;
		} catch (Exception e) {
			String err = "Error getting data from data set " + dataSetId;
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

	public void saveDataSet(DataSet dataSet)
			throws CharacterizationResultException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.saveOrUpdate(dataSet);
			//TODO special handling for file
		} catch (Exception e) {
			String err = "Error saving characterization result data set. ";
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}

	public void deleteDataSet(DataSet dataSet)
			throws CharacterizationResultException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			authService.removePublicGroup(dataSet.getId().toString());
			appService.delete(dataSet);

		} catch (Exception e) {
			String err = "Error deleting data set " + dataSet.getId();
			logger.error(err, e);
			throw new CharacterizationResultException(err, e);
		}
	}
}
