package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryRowBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving characterizations
 * 
 * @author pansu
 * 
 */
public class NanoparticleCharacterizationService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationService.class);

	public NanoparticleCharacterizationService() {
	}

	public void saveCharacterization(NanoparticleSample particleSample,
			Characterization achar, String createdBy) throws Exception {

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		if (achar.getId() != null) {
			try {
				Characterization dbChar = (Characterization) appService.load(
						Characterization.class, achar.getId());
			} catch (Exception e) {
				String err = "Object doesn't exist in the database anymore.  Please log in again.";
				logger.error(err);
				throw new ParticleCharacterizationException(err, e);
			}
		}

		if (particleSample.getCharacterizationCollection() != null) {
			particleSample.getCharacterizationCollection().clear();
		} else {
			particleSample
					.setCharacterizationCollection(new HashSet<Characterization>());
		}
		achar.setNanoparticleSample(particleSample);
		particleSample.getCharacterizationCollection().add(achar);

		appService.saveOrUpdate(achar);
	}

	public Characterization findCharacterizationById(String charId)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException {
		Characterization achar = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					PhysicalCharacterization.class).add(
					Property.forName("id").eq(new Long(charId)));
			crit.createAlias("derivedBioAssayDataCollection", "bioassay",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("bioassay.labFile", "file",
					CriteriaSpecification.LEFT_JOIN);
			crit.setFetchMode("protocolFile", FetchMode.JOIN);
			crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
			crit.setFetchMode("instrumentConfiguration", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				achar = (Characterization) result.get(0);
			}
			return achar;
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new ParticleCharacterizationException();
		}
	}

	public SortedSet<String> findAllCharacterizationSources()
			throws ParticleCharacterizationException {
		SortedSet<String> sources = new TreeSet<String>();

		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select distinct achar.source from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar where achar.source is not null");
			List results = appService.query(crit);
			for (Object obj : results) {
				String source = (String) obj;
				sources.add(source);
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

	public List<Instrument> findAllInstruments()
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

	public String[] getDerivedDatumValueUnits(String derivedDatumName)
			throws ParticleCharacterizationException {
		try {
			SortedSet<String> units = LookupService.findLookupValues(
					derivedDatumName, "unit");
			return units.toArray(new String[0]);
		} catch (Exception e) {
			String err = "Error getting value unit for " + derivedDatumName;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	public SortedSet<Characterization> findParticleCharacterizationsByClass(
			String particleName, String className)
			throws ParticleCharacterizationException {
		SortedSet<Characterization> charas = new TreeSet<Characterization>(
				new CaNanoLabComparators.CharacterizationDateComparator());
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Class
					.forName(className));
			crit.createAlias("nanoparticleSample", "sample",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("derivedBioAssayDataCollection", "bioassay",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("bioassay.labFile", "file",
					CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("sample.name", particleName));
			crit.setFetchMode("protocolFile", FetchMode.JOIN);
			crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
			crit.setFetchMode("instrumentConfiguration", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			for (Object obj : result) {
				charas.add((Characterization) obj);
			}
			return charas;
		} catch (Exception e) {
			String err = "Error getting " + particleName
					+ " characterizations of type " + className;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	public CharacterizationSummaryBean getParticleCharacterizationSummaryByClass(
			String particleName, String className, UserBean user)
			throws ParticleCharacterizationException {
		CharacterizationSummaryBean charSummary = new CharacterizationSummaryBean();
		charSummary.setCharacterizationClassName(className);
		try {
			SortedSet<Characterization> charas = findParticleCharacterizationsByClass(
					particleName, className);
			if (charas.isEmpty()) {
				return null;
			}
			FileService fileService = new FileService();
			for (Characterization chara : charas) {
				CharacterizationBean charBean = new CharacterizationBean(chara);
				charSummary.getCharBeans().add(charBean);
				if (charBean.getDerivedBioAssayDataList() != null
						&& !charBean.getDerivedBioAssayDataList().isEmpty()) {
					for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
							.getDerivedBioAssayDataList()) {
						fileService.setVisiblity(derivedBioAssayDataBean
								.getLabFileBean(), user);
						Map<String, String> datumMap = new HashMap<String, String>();
						for (DerivedDatum data : derivedBioAssayDataBean
								.getDatumList()) {
							String datumLabel = data.getName();
							if (data.getValueUnit() != null
									&& data.getValueUnit().length() > 0) {
								datumLabel += "(" + data.getValueUnit() + ")";
							}
							datumMap
									.put(datumLabel, data.getValue().toString());
						}
						CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
						charSummaryRow.setCharBean(charBean);
						charSummaryRow.setDatumMap(datumMap);
						charSummaryRow
								.setDerivedBioAssayDataBean(derivedBioAssayDataBean);
						charSummary.getSummaryRows().add(charSummaryRow);
					}
				} else {
					CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
					charSummaryRow.setCharBean(charBean);
					charSummary.getSummaryRows().add(charSummaryRow);
				}
			}
			return charSummary;
		} catch (Exception e) {
			String err = "Error getting " + particleName
					+ " characterization summary of type " + className;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	// set lab file visibility of a characterization
	public void setVisiblity(CharacterizationBean charBean, UserBean user)
			throws ParticleCharacterizationException {
		try {
			FileService fileService = new FileService();
			for (DerivedBioAssayDataBean bioAssayData : charBean
					.getDerivedBioAssayDataList()) {
				fileService.setVisiblity(bioAssayData.getLabFileBean(), user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for characterization "
					+ charBean.getViewTitle();
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}
}