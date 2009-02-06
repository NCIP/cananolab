package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.DataRow;
import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class NanoparticleCharacterizationResultServiceLocalImpl implements
		NanoparticleCharacterizationResultService {

	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationResultServiceLocalImpl.class);

	public SortedMap<DataRow, List<Datum>> getDataForDataSet(String dataSetId)
			throws CharacterizationResultException {

		SortedMap<DataRow, List<Datum>> dataMap = new TreeMap<DataRow, List<Datum>>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Datum.class).add(
					Restrictions.eq("DataSet.id", dataSetId)).addOrder(
					Order.asc("createdDate"));
			List result = appService.query(crit);
			List<Datum> data = null;
			for (Object obj : result) {
				Datum datum = (Datum) obj;
				if (dataMap.containsKey(datum.getDataRow())) {
					data = dataMap.get(datum.getDataRow());
				} else {
					data = new ArrayList<Datum>();
					dataMap.put(datum.getDataRow(), data);
				}
				data.add(datum);
			}
			return dataMap;
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
