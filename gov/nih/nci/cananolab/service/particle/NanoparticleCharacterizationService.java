package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.PhysicalCharacterizationBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class NanoparticleCharacterizationService {
	private static Logger logger = Logger
			.getLogger(CopyOfNanoparticleCharacterizationService.class);

	public NanoparticleCharacterizationService() {
	}

	public void savePhysicalCharacterization(ParticleBean particleBean,
			PhysicalCharacterizationBean charBean) throws Exception {

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		if (charBean.getDomainChar().getId() != null) {
			try {
				PhysicalCharacterization dbChar = (PhysicalCharacterization) appService
						.load(PhysicalCharacterization.class, charBean
								.getDomainChar().getId());
			} catch (Exception e) {
				String err = "Object doesn't exist in the database anymore.  Please log in again.";
				logger.error(err);
				throw new ParticleCharacterizationException(err, e);
			}
		}
		PhysicalCharacterization achar = (PhysicalCharacterization) charBean
				.getDomainChar();

		if (particleBean.getParticleSample().getCharacterizationCollection() != null) {
			particleBean.getParticleSample().getCharacterizationCollection()
					.clear();
		} else {
			particleBean.getParticleSample().setCharacterizationCollection(
					new HashSet<Characterization>());
		}
		achar.setNanoparticleSample(particleBean.getParticleSample());
		particleBean.getParticleSample().getCharacterizationCollection().add(
				achar);
		appService.saveOrUpdate(achar);
	}

	public PhysicalCharacterizationBean findPhysicalCharacterizationBy(
			String charId, UserBean user)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException {
		PhysicalCharacterizationBean charBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					PhysicalCharacterization.class).add(
					Property.forName("id").eq(new Long(charId)));
			crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				PhysicalCharacterization achar = (PhysicalCharacterization) result
						.get(0);
				charBean = new PhysicalCharacterizationBean(achar);
			}
			return charBean;
		} catch (Exception e) {
			logger.error(
					"Problem finding the physical characterization by id: "
							+ charId, e);
			throw new ParticleCharacterizationException();
		}
	}

	public SortedSet<String> getAllCharacterizationSources()
			throws ParticleCharacterizationException {
		SortedSet<String> sources = new TreeSet<String>();

		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select distinct achar.source from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar where achar.source is not null");
			List results = appService.query(crit);
			for (Object obj : results) {
				Characterization achar = (Characterization) obj;
				sources.add(achar.getSource());
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve all Characterization Sources.",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all Characterization Sources ");
		}
		sources.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CHARACTERIZATION_SOURCES));

		return sources;
	}

	public List<Instrument> getAllInstruments()
			throws ParticleCharacterizationException {
		List<Instrument> instruments = new ArrayList<Instrument>();

		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class)
					.add(Restrictions.isNotNull("type"));
			List results = appService.query(crit);
			for (Object obj : results) {
				Instrument instrument = (Instrument) obj;
				instruments.add(instrument);
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all instruments.";
			logger.error(err, e);
			throw new ParticleCharacterizationException(err);
		}
		return instruments;
	}

	public String getInstrumentAbbreviation(String instrumentType)
			throws ParticleCharacterizationException {
		String instrumentAbbreviation = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select distinct instrument.abbreviation from gov.nih.nci.cananolab.domain.common.Instrument instrument where instrument.type='"
							+ instrumentType + "'");
			List results = appService.query(crit);
			for (Object obj : results) {
				instrumentAbbreviation = (String) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve instrument abbreviation.";
			logger.error(err, e);
			throw new ParticleCharacterizationException(err);
		}
		return instrumentAbbreviation;
	}
}