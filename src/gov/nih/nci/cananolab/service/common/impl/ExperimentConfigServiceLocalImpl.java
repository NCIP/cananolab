package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.DateUtil;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class ExperimentConfigServiceLocalImpl implements
		ExperimentConfigService {
	private static Logger logger = Logger
			.getLogger(ExperimentConfigServiceLocalImpl.class);

	public void saveExperimentConfig(ExperimentConfig config)
			throws ExperimentConfigException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				ExperimentConfig dbConfig = findExperimentConfigById(config
						.getId().toString());
				if (dbConfig != null) {
					config.setCreatedBy(dbConfig.getCreatedBy());
					config.setCreatedDate(dbConfig.getCreatedDate());
				}
			}
			Technique technique = config.getTechnique();
			// check if technique already exists;
			Technique dbTechnique = findTechniqueByType(technique.getType());
			if (dbTechnique != null) {
				technique.setId(dbTechnique.getId());
				technique.setCreatedBy(dbTechnique.getCreatedBy());
				technique.setCreatedDate(dbTechnique.getCreatedDate());
			} else {
				technique.setCreatedBy(config.getCreatedBy());
				technique.setCreatedDate(new Date());
			}
			// check if instrument already exists;
			if (config.getInstrumentCollection() != null) {
				Collection<Instrument> instruments = new HashSet<Instrument>(
						config.getInstrumentCollection());
				config.getInstrumentCollection().clear();
				int i = 0;
				for (Instrument instrument : instruments) {
					Instrument dbInstrument = findInstrumentBy(instrument
							.getType(), instrument.getManufacturer(),
							instrument.getModelName());
					if (dbInstrument != null) {
						instrument.setId(dbInstrument.getId());
						instrument.setCreatedBy(dbInstrument.getCreatedBy());
						instrument
								.setCreatedDate(dbInstrument.getCreatedDate());
					} else {
						instrument.setCreatedBy(config.getCreatedBy());
						instrument.setCreatedDate(DateUtil
								.addSecondsToCurrentDate(i));
					}
					config.getInstrumentCollection().add(instrument);
				}
			}
			appService.saveOrUpdate(config);
		} catch (Exception e) {
			String err = "Error in saving the technique and associated instruments.";
			logger.error(err, e);
			throw new ExperimentConfigException(err, e);
		}
	}

	public void deleteExperimentConfig(ExperimentConfig config)
			throws ExperimentConfigException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				ExperimentConfig dbConfig = findExperimentConfigById(config
						.getId().toString());
				if (dbConfig != null) {
					config.setCreatedBy(dbConfig.getCreatedBy());
					config.setCreatedDate(dbConfig.getCreatedDate());
				}
			}
			Technique technique = config.getTechnique();
			// check if technique already exists;
			Technique dbTechnique = findTechniqueByType(technique.getType());
			if (dbTechnique != null) {
				technique.setId(dbTechnique.getId());
				technique.setCreatedBy(dbTechnique.getCreatedBy());
				technique.setCreatedDate(dbTechnique.getCreatedDate());
			} else {
				technique.setCreatedBy(config.getCreatedBy());
				technique.setCreatedDate(new Date());
				// need to save the transient object before deleting the
				// experiment config
				appService.saveOrUpdate(technique);
			}
			// check if instrument already exists;
			if (config.getInstrumentCollection() != null) {
				Collection<Instrument> instruments = new HashSet<Instrument>(
						config.getInstrumentCollection());
				config.getInstrumentCollection().clear();
				int i = 0;
				for (Instrument instrument : instruments) {
					Instrument dbInstrument = findInstrumentBy(instrument
							.getType(), instrument.getManufacturer(),
							instrument.getModelName());
					if (dbInstrument != null) {
						instrument.setId(dbInstrument.getId());
						instrument.setCreatedBy(dbInstrument.getCreatedBy());
						instrument
								.setCreatedDate(dbInstrument.getCreatedDate());
					} else {
						instrument.setCreatedBy(config.getCreatedBy());
						instrument.setCreatedDate(DateUtil
								.addSecondsToCurrentDate(i));
						// need to save the transient object before deleting the
						// experiment config
						appService.saveOrUpdate(instrument);
					}
					config.getInstrumentCollection().add(instrument);
				}
			}

			appService.delete(config);
		} catch (Exception e) {
			String err = "Error in deleting the technique and associated instruments";
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
				if (manufacturer != null && manufacturer.trim().length() > 0) {
					manufacturers.add(manufacturer);
				}
			}
			Collections.sort(manufacturers);
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

	public Technique findTechniqueByType(String type)
			throws ExperimentConfigException {
		Technique technique = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Technique.class)
					.add(Property.forName("type").eq(new String(type)));
			List results = appService.query(crit);
			for (Object obj : results) {
				technique = (Technique) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve technique by type.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return technique;
	}

	public Instrument findInstrumentBy(String type, String manufacturer,
			String modelName) throws ExperimentConfigException {
		Instrument instrument = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class);
			if (type != null || type.length() > 0) {
				crit.add(Restrictions.eq("type", type));
			}
			if (manufacturer != null || manufacturer.length() > 0) {
				crit.add(Restrictions.eq("manufacturer", manufacturer));
			}
			if (modelName != null || modelName.length() > 0) {
				crit.add(Restrictions.eq("modelName", modelName));
			}
			List results = appService.query(crit);
			for (Object obj : results) {
				instrument = (Instrument) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve instrument by type, manufacturer, and model name.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return instrument;
	}

	public String[] findInstrumentTypesByTechniqueType(String techniqueType)
			throws ExperimentConfigException, CaNanoLabException {
		SortedSet<String> types = null;
		types = LookupService.getDefaultAndOtherLookupTypes(techniqueType,
				"instrument", "otherInstrument");
		if (types != null && types.size() > 0) {
			String[] typeArray = new String[types.size()];
			types.toArray(typeArray);
			return typeArray;
		} else {
			return null;
		}
	}

	public String[] findInstrumentTypesByConfigId(String configId)
			throws ExperimentConfigException, CaNanoLabException {
		String techniqueType = null;
		SortedSet<String> types = null;
		ExperimentConfig config = findExperimentConfigById(configId);

		if (config != null && config.getTechnique() != null
				&& config.getTechnique().getType() != null) {
			techniqueType = config.getTechnique().getType();
			types = LookupService.getDefaultAndOtherLookupTypes(techniqueType,
					"instrument", "otherInstrument");
		}
		if (types != null && types.size() > 0) {
			String[] typeArray = new String[types.size()];
			types.toArray(typeArray);
			return typeArray;
		} else {
			return null;
		}
	}
}
