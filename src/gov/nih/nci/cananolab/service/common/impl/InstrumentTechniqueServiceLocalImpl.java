package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.dto.common.InstrumentBean;
import gov.nih.nci.cananolab.dto.common.TechniqueBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.InstrumentTechniqueException;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.service.common.InstrumentTechniqueService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

public class InstrumentTechniqueServiceLocalImpl implements
		InstrumentTechniqueService {
	private static Logger logger = Logger
			.getLogger(InstrumentTechniqueServiceLocalImpl.class);

	public void saveInstrument(Instrument instrument)
			throws InstrumentTechniqueException, DuplicateEntriesException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (instrument.getId() != null) {
				Instrument dbInstrument = (Instrument) appService.load(
						Instrument.class, instrument.getId());
				if (dbInstrument == null) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new InstrumentTechniqueException(err);
				}
			}
			appService.saveOrUpdate(instrument);
		} catch (Exception e) {
			String err = "Problem in saving the instrument.";
			logger.error(err, e);
			throw new InstrumentTechniqueException(err, e);
		}
	}

	public List<InstrumentBean> findAllInstruments()
			throws InstrumentTechniqueException {
		List<InstrumentBean> instruments = new ArrayList<InstrumentBean>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class);
			List results = appService.query(crit);
			for (Object obj : results) {
				Instrument instrument = (Instrument) obj;
				instruments.add(new InstrumentBean(instrument));
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all instruments.";
			logger.error(err, e);
			throw new InstrumentTechniqueException(err);
		}
		return instruments;
	}

	public void saveTechnique(Technique Technique)
			throws InstrumentTechniqueException, DuplicateEntriesException {

	}

	public List<TechniqueBean> findAllTechniques()
			throws InstrumentTechniqueException {
		List<TechniqueBean> techniques = new ArrayList<TechniqueBean>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Technique.class);
			List results = appService.query(crit);
			for (Object obj : results) {
				Technique technique = (Technique) obj;
				techniques.add(new TechniqueBean(technique));
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all techniques.";
			logger.error(err, e);
			throw new InstrumentTechniqueException(err);
		}
		return techniques;
	}

	public Instrument findInstrumentBy(String instrumentType,
			String manufacturer) throws ParticleCharacterizationException {
		Instrument instrument = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class)
					.add(Property.forName("type").eq(instrumentType)).add(
							Property.forName("manufacturer").eq(manufacturer));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				instrument = (Instrument) obj;

			}
			return instrument;
		} catch (Exception e) {
			String err = "Problem to retrieve instrument.";
			logger.error(err, e);
			throw new ParticleCharacterizationException(err);
		}
	}

}
