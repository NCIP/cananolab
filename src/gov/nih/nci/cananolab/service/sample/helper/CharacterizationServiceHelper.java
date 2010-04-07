package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

/**
 * Service methods involving characterizations
 * 
 * @author tanq, pansu
 */
public class CharacterizationServiceHelper {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceHelper.class);
	private AuthorizationService authService;
	private FileServiceHelper fileHelper = new FileServiceHelper();

	public CharacterizationServiceHelper() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public List<String> findOtherCharacterizationByAssayCategory(
			String assayCategory) throws Exception {
		List<String> charNames = new ArrayList<String>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				OtherCharacterization.class).add(
				Property.forName("assayCategory").eq(assayCategory));
		List result = appService.query(crit);
		for (Object obj : result) {
			String charName = ((OtherCharacterization) obj).getName();
			if (!charNames.contains(charName)) {
				charNames.add(charName);
			}
		}
		return charNames;
	}

	public Protocol findProtocolByCharacterizationId(
			java.lang.String characterizationId, UserBean user)
			throws Exception {
		Protocol protocol = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select aChar.protocol from gov.nih.nci.cananolab.domain.particle.Characterization aChar where aChar.id="
				+ characterizationId;
		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		for (Object obj : results) {
			protocol = (Protocol) obj;
			if (authService.checkReadPermission(user, protocol.getId()
					.toString())) {
				return protocol;
			} else {
				logger.debug("User doesn't have access to the protocol.");
			}
		}
		return protocol;
	}

	public List<Finding> findFindingsByCharacterizationId(String charId,
			UserBean user) throws Exception {
		List<Finding> findings = new ArrayList<Finding>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				Characterization.class).add(
				Property.forName("id").eq(new Long(charId)));
		crit.setFetchMode("findingCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection.keywordCollection",
				FetchMode.JOIN);
		crit.setFetchMode("findingCollection.datumCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"findingCollection.datumCollection.conditionCollection",
				FetchMode.JOIN);
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			Characterization achar = (Characterization) result.get(0);
			// check whether user has access to the characterization
			if (authService.checkReadPermission(user, achar.getId().toString())) {
				findings.addAll(achar.getFindingCollection());
				return findings;
			} else {
				logger.debug("USer doesn't have access to the sample");
			}
		}
		return findings;
	}

	public List<ExperimentConfig> findExperimentConfigsByCharacterizationId(
			String charId, UserBean user) throws Exception {
		List<ExperimentConfig> configs = new ArrayList<ExperimentConfig>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				Characterization.class).add(
				Property.forName("id").eq(new Long(charId)));
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique",
				FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.instrumentCollection",
				FetchMode.JOIN);
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			Characterization achar = (Characterization) result.get(0);
			// check whether user has access to the characterization
			if (authService.checkReadPermission(user, achar.getId().toString())) {
				configs.addAll(achar.getExperimentConfigCollection());
				return configs;
			} else {
				logger.debug("USer doesn't have access to the sample");
			}
		}
		return configs;
	}

	public List<File> findFilesByCharacterizationId(String charId, UserBean user)
			throws Exception {
		List<File> fileCollection = new ArrayList<File>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.fileCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		List filteredResults = new ArrayList(results);
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			File file = (File) obj;
			if (user == null
					|| authService.checkReadPermission(user, file.getId()
							.toString())) {
				fileCollection.add(file);
			} else {
				logger.debug("USer doesn't have access to file of id: "
						+ file.getId());
			}
		}
		return fileCollection;
	}

	public ExperimentConfig findExperimentConfigById(String id, UserBean user)
			throws ExperimentConfigException, NoAccessException {
		ExperimentConfig config = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					ExperimentConfig.class).add(
					Property.forName("id").eq(new Long(id)));
			crit.setFetchMode("technique", FetchMode.JOIN);
			crit.setFetchMode("instrumentCollection", FetchMode.JOIN);
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				config = (ExperimentConfig) result.get(0);
				if (authService.checkReadPermission(user, config.getId()
						.toString())) {
					return config;
				} else {
					throw new NoAccessException();
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem to retrieve experiment config by id.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return config;
	}

	public AuthorizationService getAuthService() {
		return authService;
	}

	public void assignVisibility(Characterization aChar,
			String[] visibleGroups, String owningGroup) throws Exception {
		// characterization
		if (aChar != null && aChar.getId() != null) {
			authService.assignVisibility(aChar.getId().toString(),
					visibleGroups, owningGroup);
			if (aChar.getFindingCollection() != null) {
				for (Finding finding : aChar.getFindingCollection()) {
					if (finding != null) {
						authService.assignVisibility(
								finding.getId().toString(), visibleGroups,
								owningGroup);
					}
					// datum, need to check for null for copy bean.
					if (finding.getDatumCollection() != null) {
						for (Datum datum : finding.getDatumCollection()) {
							if (datum != null && datum.getId() != null) {
								authService
										.assignVisibility(datum.getId()
												.toString(), visibleGroups,
												owningGroup);
							}
							// condition
							if (datum.getConditionCollection() != null) {
								for (Condition condition : datum
										.getConditionCollection()) {
									authService.assignVisibility(condition
											.getId().toString(), visibleGroups,
											owningGroup);
								}
							}
						}
					}
				}
			}
			// ExperimentConfiguration
			if (aChar.getExperimentConfigCollection() != null) {
				for (ExperimentConfig config : aChar
						.getExperimentConfigCollection()) {
					authService.assignVisibility(config.getId().toString(),
							visibleGroups, owningGroup);
					// assign instruments and technique to public visibility
					if (config.getTechnique() != null) {
						authService.assignVisibility(config.getTechnique()
								.getId().toString(),
								new String[] { Constants.CSM_PUBLIC_GROUP },
								null);
					}
					if (config.getInstrumentCollection() != null) {
						for (Instrument instrument : config
								.getInstrumentCollection()) {
							authService
									.assignVisibility(
											instrument.getId().toString(),
											new String[] { Constants.CSM_PUBLIC_GROUP },
											null);
						}
					}
				}
			}
		}
	}

	public List<String> removeVisibility(Characterization aChar, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		// characterization
		if (aChar != null) {
			if (remove == null || remove) {
				authService.removeCSMEntry(aChar.getId().toString());
			}
			entries.add(aChar.getId().toString());
			for (Finding finding : aChar.getFindingCollection()) {
				if (finding != null) {
					entries.addAll(removeVisibility(finding, remove));
				}
			}

			// ExperimentConfiguration
			for (ExperimentConfig config : aChar
					.getExperimentConfigCollection()) {
				entries.addAll(removeVisibility(config, remove));
			}
		}
		return entries;
	}

	public List<String> removeVisibility(ExperimentConfig config, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (remove == null || remove) {
			authService.removeCSMEntry(config.getId().toString());
			authService
					.removeCSMEntry(config.getTechnique().getId().toString());
		}
		entries.add(config.getId().toString());
		entries.add(config.getTechnique().getId().toString());
		if (config.getInstrumentCollection() != null) {
			for (Instrument instrument : config.getInstrumentCollection()) {
				if (remove == null || remove) {
					authService.removeCSMEntry(instrument.getId().toString());
				}
				entries.add(instrument.getId().toString());
			}
		}
		return entries;
	}

	public List<String> removeVisibility(Finding finding, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (remove == null || remove) {
			authService.removeCSMEntry(finding.getId().toString());
		}
		entries.add(finding.getId().toString());

		// datum
		if (finding.getDatumCollection() != null) {
			for (Datum datum : finding.getDatumCollection()) {
				if (datum != null) {
					if (remove == null || remove) {
						authService.removeCSMEntry(datum.getId().toString());
					}
					entries.add(datum.getId().toString());
				}
				if (datum.getConditionCollection() != null) {
					for (Condition condition : datum.getConditionCollection()) {
						if (remove == null || remove) {
							authService.removeCSMEntry(condition.getId()
									.toString());
						}
						entries.add(condition.getId().toString());
					}
				}
			}
		}
		// file
		if (finding.getFileCollection() != null) {
			for (File file : finding.getFileCollection()) {
				if (remove == null || remove) {
					authService.removeCSMEntry(file.getId().toString());
				}
				entries.add(file.getId().toString());
			}
		}
		return entries;
	}

	public List<Characterization> findCharacterizationsBySampleId(
			String sampleId, UserBean user) throws CharacterizationException {
		List<Characterization> chars = new ArrayList<Characterization>();
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
			crit.setFetchMode("protocol", FetchMode.JOIN);
			crit.setFetchMode("protocol.file", FetchMode.JOIN);
			crit
					.setFetchMode("protocol.file.keywordCollection",
							FetchMode.JOIN);
			crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
			crit.setFetchMode("experimentConfigCollection.technique",
					FetchMode.JOIN);
			crit.setFetchMode(
					"experimentConfigCollection.instrumentCollection",
					FetchMode.JOIN);
			crit.setFetchMode("findingCollection", FetchMode.JOIN);
			crit.setFetchMode("findingCollection.datumCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"findingCollection.datumCollection.conditionCollection",
					FetchMode.JOIN);
			crit.setFetchMode("findingCollection.fileCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"findingCollection.fileCollection.keywordCollection",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);

			for (Object obj : results) {
				Characterization achar = (Characterization) obj;
				if (authService.checkReadPermission(user, achar.getId()
						.toString())) {
					if (achar.getProtocol() != null) {
						if (!authService.checkReadPermission(user, achar
								.getProtocol().getId().toString())) {
							achar.setProtocol(null);
						}
					}
					for (Finding finding : achar.getFindingCollection()) {
						fileHelper.removeUnaccessibleFiles(finding
								.getFileCollection(), user);
					}
					chars.add(achar);
				}
			}
			return chars;
		} catch (Exception e) {
			String err = "Error finding characterization by sample ID "
					+ sampleId;
			logger.error(err, e);
			throw new CharacterizationException(err);
		}
	}

	public Characterization findCharacterizationById(String charId,
			UserBean user) throws Exception {

		Characterization achar = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				Characterization.class).add(
				Property.forName("id").eq(new Long(charId)));
		// fully load characterization
		crit.setFetchMode("pointOfContact", FetchMode.JOIN);
		crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("protocol", FetchMode.JOIN);
		crit.setFetchMode("protocol.file", FetchMode.JOIN);
		crit.setFetchMode("protocol.file.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique",
				FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.instrumentCollection",
				FetchMode.JOIN);
		crit.setFetchMode("findingCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.datumCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"findingCollection.datumCollection.conditionCollection",
				FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection.keywordCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			achar = (Characterization) result.get(0);
			if (authService.checkReadPermission(user, achar.getId().toString())) {
				if (achar.getFindingCollection() != null) {
					for (Finding finding : achar.getFindingCollection()) {
						if (achar.getFindingCollection() != null) {
							fileHelper.removeUnaccessibleFiles(finding
									.getFileCollection(), user);
						}
					}
				}
			} else {
				throw new NoAccessException(
						"User doesn't have access to the sample");
			}
		}
		return achar;
	}

	public Finding findFindingById(String findingId, UserBean user)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Finding.class).add(
				Property.forName("id").eq(new Long(findingId)));
		crit.setFetchMode("datumCollection", FetchMode.JOIN);
		crit
				.setFetchMode("datumCollection.conditionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection.keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		Finding finding = null;
		if (!result.isEmpty()) {
			finding = (Finding) result.get(0);
			if (authService.checkReadPermission(user, finding.getId()
					.toString())) {
				if (finding.getFileCollection() != null) {
					fileHelper.removeUnaccessibleFiles(finding
							.getFileCollection(), user);
				}
			} else {
				throw new NoAccessException(
						"User doesn't have access to the sample");
			}
		}
		return finding;
	}

	public int getNumberOfPublicCharacterizations(
			String characterizationClassName) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getAllPublicData();
		DetachedCriteria crit = DetachedCriteria.forClass(
				ClassUtils.getFullClass(characterizationClassName))
				.setProjection(Projections.distinct(Property.forName("id")));
		List results = appService.query(crit);
		int count = 0;
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, id)) {
				count++;
			}
		}
		return count;
	}
}