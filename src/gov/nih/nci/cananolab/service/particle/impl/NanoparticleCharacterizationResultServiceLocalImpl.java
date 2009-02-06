package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class NanoparticleCharacterizationResultServiceLocalImpl implements
		NanoparticleCharacterizationResultService {

	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationResultServiceLocalImpl.class);

	public List<Datum> getDataForDataSet(String dataSetId)
			throws CharacterizationResultException {
		List<Datum> data = new ArrayList<Datum>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Datum.class).add(
					Restrictions.eq("DataSet.id", dataSetId)).addOrder(
					Order.asc("createdDate"));
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
}
