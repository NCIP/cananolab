package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

public class ExperimentConfigServiceLocalImpl implements
		ExperimentConfigService {
	private static Logger logger = Logger
			.getLogger(ExperimentConfigServiceLocalImpl.class);

	public void saveExperimentConfig(ExperimentConfig config)
			throws ExperimentConfigException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// TODO check if technique already exists;
			// TODO check if instrument already exists;
			appService.saveOrUpdate(config);
		} catch (Exception e) {
			String err = "Error in saving the technique and associated instruments.";
			logger.error(err, e);
			throw new ExperimentConfigException(err, e);
		}
	}

	public List<Technique> findAllTechniques() throws ExperimentConfigException {
		List<Technique> techniques = new ArrayList<Technique>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Technique.class);
			List results = appService.query(crit);
			for (Object obj : results) {
				Technique technique = (Technique) obj;
				techniques.add(technique);
			}
			Collections.sort(techniques,
					new CaNanoLabComparators.TechniqueComparator());
		} catch (Exception e) {
			String err = "Problem to retrieve all techniques.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return techniques;
	}

	public List<String> getAllManufacturers() throws ExperimentConfigException {
		List<String> manufacturers = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class)
					.setProjection(
							Projections.distinct(Property
									.forName("manufacturer")));
			;
			List results = appService.query(crit);
			for (Object obj : results) {
				String manufacturer = (String) obj;
				manufacturers.add(manufacturer);
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all manufacturers.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return manufacturers;
	}

	public ExperimentConfig findExperimentConfigById(String id)
			throws ExperimentConfigException {
		ExperimentConfig config = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					ExperimentConfig.class).add(
					Property.forName("id").eq(new Long(id)));
			crit.setFetchMode("instrumentCollection", FetchMode.JOIN);
			List results = appService.query(crit);
			for (Object obj : results) {
				config = (ExperimentConfig) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all manufacturers.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return config;
	}
}
