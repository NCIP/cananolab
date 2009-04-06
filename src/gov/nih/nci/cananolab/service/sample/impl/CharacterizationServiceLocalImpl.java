package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving local characterizations
 *
 * @author tanq, pansu
 *
 */
public class CharacterizationServiceLocalImpl extends
		CharacterizationServiceBaseImpl implements CharacterizationService {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceLocalImpl.class);
	private CharacterizationServiceHelper helper = new CharacterizationServiceHelper();

	public CharacterizationServiceLocalImpl() {
		fileService = new FileServiceLocalImpl();
	}

	public void saveCharacterization(Sample sample, Characterization achar)
			throws Exception {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (achar.getId() != null) {
				Characterization dbChar = (Characterization) appService.load(
						Characterization.class, achar.getId());
				if (dbChar == null) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new CharacterizationException(err);
				}
			}
			// if (sample.getCharacterizationCollection() != null) {
			// sample.getCharacterizationCollection().clear();
			// } else {
			// sample
			// .setCharacterizationCollection(new HashSet<Characterization>());
			// }
			achar.setSample(sample);
			// sample.getCharacterizationCollection().add(achar);
			appService.saveOrUpdate(achar);
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem in saving the characterization.";
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public Characterization findCharacterizationById(String charId)
			throws CharacterizationException {
		try {
			Characterization achar = helper.findCharacterizationById(charId);
			return achar;
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new CharacterizationException();
		}
	}

	public Characterization findCharacterizationById(String charId,
			String className) throws CharacterizationException {
		return findCharacterizationById(charId);
	}

	// private Boolean checkRedundantViewTitle(Sample
	// sample,
	// Characterization chara) throws CharacterizationException {
	// Boolean exist = false;
	// try {
	// CustomizedApplicationService appService = (CustomizedApplicationService)
	// ApplicationServiceProvider
	// .getApplicationService();
	// DetachedCriteria crit = DetachedCriteria.forClass(
	// Characterization.class).add(
	// Property.forName("identificationName").eq(
	// chara.getIdentificationName()));
	// crit.createAlias("nanosample", "sample").add(
	// Restrictions.eq("sample.name", sample.getName()));
	// crit
	// .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	// List results = appService.query(crit);
	// if (!results.isEmpty()) {
	// for (Object obj : results) {
	// Characterization achar = (Characterization) obj;
	// // same characterization class with different IDs can't have
	// // the same view titles.
	// if (achar.getClass().getCanonicalName().equals(
	// chara.getClass().getCanonicalName())
	// && !achar.getId().equals(chara.getId())) {
	// return true;
	// }
	// }
	// }
	// return exist;
	// } catch (Exception e) {
	// logger
	// .error("Problem checking whether the view title already exists.");
	// throw new CharacterizationException();
	// }
	// }

	public SortedSet<String> findAllCharacterizationSources()
			throws CharacterizationException {
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
					.error(
							"Problem to retrieve all Characterization PrimaryOrganizations.",
							e);
			throw new CharacterizationException(
					"Problem to retrieve all Characterization Sources ");
		}
		sources.addAll(Arrays
				.asList(Constants.DEFAULT_CHARACTERIZATION_SOURCES));

		return sources;
	}

	protected List<Characterization> findSampleCharacterizationsByClass(
			String particleName, String className)
			throws CharacterizationException {

		try {
			List<Characterization> charList = helper
					.findSampleCharacterizationsByClass(particleName, className);
			Collections.sort(charList,
					new Comparators.CharacterizationDateComparator());
			return charList;
		} catch (Exception e) {
			String err = "Error getting " + particleName
					+ " characterizations of type " + className;
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	// set lab file visibility of a characterization
	public void retrieveVisiblity(CharacterizationBean charBean, UserBean user)
			throws CharacterizationException {
		try {
			// for (DerivedBioAssayDataBean bioAssayData : charBean
			// .getDerivedBioAssayDataList()) {
			// fileService
			// .retrieveVisibility(bioAssayData.getFileBean(), user);
			// }
			fileService.retrieveVisibility(charBean.getProtocolBean().getFileBean(), user);
		} catch (Exception e) {
			String err = "Error setting visiblity for characterization "
					+ charBean.getDomainChar().getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public void deleteCharacterization(Characterization chara)
			throws CharacterizationException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			removePublicVisibility(authService, chara);
			appService.delete(chara);

		} catch (Exception e) {
			String err = "Error deleting characterization " + chara.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public List<CharacterizationBean> findCharsBySampleId(String sampleId)
			throws CharacterizationException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Characterization.class);
			crit.createAlias("sample", "sample");
			crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
			// fully load characterization
			crit.setFetchMode("pointOfContact", FetchMode.JOIN);
			crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
			crit.setFetchMode("protocolFile", FetchMode.JOIN);
			crit.setFetchMode("protocolFile.protocol", FetchMode.JOIN);
			crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
			crit.setFetchMode("experimentConfigCollection.technique",
					FetchMode.JOIN);
			crit.setFetchMode(
					"experimentConfigCollection.instrumentCollection",
					FetchMode.JOIN);
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			crit.setFetchMode("datumCollection", FetchMode.JOIN);
			crit.setFetchMode("datumCollection.conditionCollection",
					FetchMode.JOIN);
			crit.setFetchMode("datumCollection.dataSet", FetchMode.JOIN);
			crit.setFetchMode("datumCollection.dataSet.file", FetchMode.JOIN);
			crit.setFetchMode("datumCollection.dataSet.file.keywordCollection",
					FetchMode.JOIN);
			crit.setFetchMode("datumCollection.dataRow", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			List<CharacterizationBean> chars = new ArrayList<CharacterizationBean>();
			for (Object obj : results) {
				Characterization chara = (Characterization) obj;
				chars.add(new CharacterizationBean(chara));
			}
			return chars;
		} catch (Exception e) {
			String err = "Error finding characterization by sample ID "
					+ sampleId;
			logger.error(err, e);
			throw new CharacterizationException(err);
		}
	}

	public void assignPublicVisibility(AuthorizationService authService,
			Characterization aChar) throws SecurityException {
		// characterization
		if (aChar != null) {
			authService.assignPublicVisibility(aChar.getId().toString());
			// char.derivedBioAssayDataCollection
			// Collection<DerivedBioAssayData> derivedBioAssayDataCollection =
			// aChar
			// .getDerivedBioAssayDataCollection();
			// if (derivedBioAssayDataCollection != null) {
			// for (DerivedBioAssayData aDerived :
			// derivedBioAssayDataCollection) {
			// if (aDerived != null) {
			// authService.assignPublicVisibility(aDerived.getId()
			// .toString());
			// }
			// // derived.derivedDatum
			// Collection<DerivedDatum> derivedDatumCollection = aDerived
			// .getDerivedDatumCollection();
			// if (derivedDatumCollection != null) {
			// for (DerivedDatum aDerivedDatum : derivedDatumCollection) {
			// if (aDerivedDatum != null) {
			// authService
			// .assignPublicVisibility(aDerivedDatum
			// .getId().toString());
			// }
			// }
			// }
			// }
			// }

			// TODO visiblity for instrument and technique
			// // InstrumentConfiguration
			// if (aChar.getInstrumentConfiguration() != null) {
			// authService.assignPublicVisibility(aChar
			// .getInstrumentConfiguration().getId().toString());
			// // InstrumentConfiguration.Instrument
			// if (aChar.getInstrumentConfiguration().getInstrument() != null) {
			// authService.assignPublicVisibility(aChar
			// .getInstrumentConfiguration().getInstrument()
			// .getId().toString());
			// }
			// }
		}
	}

	public void removePublicVisibility(AuthorizationService authService,
			Characterization aChar) throws SecurityException {
		if (aChar != null) {
			authService.removePublicGroup(aChar.getId().toString());
			// char.derivedBioAssayDataCollection
			// Collection<DerivedBioAssayData> derivedBioAssayDataCollection =
			// aChar
			// .getDerivedBioAssayDataCollection();
			// if (derivedBioAssayDataCollection != null) {
			// for (DerivedBioAssayData aDerived :
			// derivedBioAssayDataCollection) {
			// if (aDerived != null) {
			// authService.removePublicGroup(aDerived.getId()
			// .toString());
			// }
			// // derived.derivedDatum
			// Collection<DerivedDatum> derivedDatumCollection = aDerived
			// .getDerivedDatumCollection();
			// if (derivedDatumCollection != null) {
			// for (DerivedDatum aDerivedDatum : derivedDatumCollection) {
			// if (aDerivedDatum != null) {
			// authService.removePublicGroup(aDerivedDatum
			// .getId().toString());
			// }
			// }
			// }
			// }
			// }
			// TODO remove visibility for instrument and technique
			// InstrumentConfiguration
			// InstrumentConfiguration instrumentConfiguration = aChar
			// .getInstrumentConfiguration();
			// if (instrumentConfiguration != null) {
			// authService.removePublicGroup(instrumentConfiguration.getId()
			// .toString());
			// // InstrumentConfiguration.Instrument
			// if (instrumentConfiguration.getInstrument() != null) {
			// authService.removePublicGroup(aChar
			// .getInstrumentConfiguration().getInstrument()
			// .getId().toString());
			// }
			// }
		}
	}
}