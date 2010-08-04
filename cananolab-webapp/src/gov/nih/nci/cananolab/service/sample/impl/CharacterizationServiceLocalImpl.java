package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving local characterizations
 *
 * @author tanq, pansu
 *
 */
public class CharacterizationServiceLocalImpl extends BaseServiceLocalImpl
		implements CharacterizationService {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceLocalImpl.class);
	private CharacterizationServiceHelper helper;
	private FileServiceLocalImpl fileService;

	public CharacterizationServiceLocalImpl() {
		super();
		helper = new CharacterizationServiceHelper(this.securityService);
		fileService = new FileServiceLocalImpl(this.securityService);
	}

	public CharacterizationServiceLocalImpl(UserBean user) {
		super(user);
		helper = new CharacterizationServiceHelper(this.securityService);
		fileService = new FileServiceLocalImpl(this.securityService);
	}

	public CharacterizationServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new CharacterizationServiceHelper(this.securityService);
		fileService = new FileServiceLocalImpl(this.securityService);
	}

	public void saveCharacterization(SampleBean sampleBean,
			CharacterizationBean charBean) throws CharacterizationException,
			NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			Characterization achar = charBean.getDomainChar();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Boolean newChar = true;
			if (achar.getId() != null) {
				newChar = false;
				if (!securityService.checkCreatePermission(achar.getId()
						.toString())) {
					throw new NoAccessException();
				}
				try {
					Characterization dbChar = (Characterization) appService
							.load(Characterization.class, achar.getId());
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new CharacterizationException(err, e);
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

			// save file data to file system
			List<FindingBean> findingBeans = charBean.getFindings();
			if (findingBeans != null && !findingBeans.isEmpty()) {
				for (FindingBean findingBean : findingBeans) {
					for (FileBean fileBean : findingBean.getFiles()) {
						fileService.prepareSaveFile(fileBean.getDomainFile());
						fileService.writeFile(fileBean);
					}
				}
			}
			appService.saveOrUpdate(achar);
			// save default access if it's new
			if (newChar) {
				super.saveDefaultAccessibility(achar.getId().toString());
			}

		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem in saving the characterization.";
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public CharacterizationBean findCharacterizationById(String charId)
			throws CharacterizationException, NoAccessException {
		CharacterizationBean charBean = null;
		try {
			Characterization achar = helper.findCharacterizationById(charId);
			if (achar != null) {
				charBean = new CharacterizationBean(achar);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new CharacterizationException();
		}
		return charBean;
	}

	private CharacterizationBean loadCharacterizationBean(Characterization achar)
			throws Exception {
		CharacterizationBean charBean = new CharacterizationBean(achar);
		ProtocolBean protocolBean = charBean.getProtocolBean();
		if (user != null && protocolBean.getDomain().getId() != null) {
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(protocolBean.getDomain().getId()
							.toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(protocolBean.getDomain().getId()
							.toString());
			protocolBean.setUserAccesses(userAccesses);
			protocolBean.setGroupAccesses(groupAccesses);
		}
		return charBean;
	}

	public void deleteCharacterization(Characterization chara)
			throws CharacterizationException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(chara);
		} catch (Exception e) {
			String err = "Error deleting characterization " + chara.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public List<CharacterizationBean> findCharacterizationsBySampleId(
			String sampleId) throws CharacterizationException {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();
		try {
			List<Characterization> chars = helper
					.findCharacterizationsBySampleId(sampleId);
			for (Characterization achar : chars) {
				CharacterizationBean charBean = new CharacterizationBean(achar);
				charBeans.add(charBean);
			}
			return charBeans;
		} catch (Exception e) {
			String err = "Error finding characterization by sample ID "
					+ sampleId;
			logger.error(err, e);
			throw new CharacterizationException(err);
		}
	}

	public FindingBean findFindingById(String findingId)
			throws CharacterizationException, NoAccessException {
		FindingBean findingBean = null;
		try {
			Finding finding = helper.findFindingById(findingId);
			if (finding != null) {
				findingBean = new FindingBean(finding);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error getting finding of ID " + findingId;
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
		return findingBean;
	}

	public void saveFinding(FindingBean finding)
			throws CharacterizationException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Boolean newFinding = true;
			if (finding.getDomain().getId() != null) {
				newFinding = false;
			}
			for (FileBean fileBean : finding.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile());
			}
			appService.saveOrUpdate(finding.getDomain());
			if (newFinding) {
				for (AccessibilityBean access : this.getDefaultAccesses()) {
					this.assignAccessibility(access, finding.getDomain());
				}
			}
			// save file data to file system and assign visibility
			for (FileBean fileBean : finding.getFiles()) {
				fileService.writeFile(fileBean);
			}

		} catch (Exception e) {
			String err = "Error saving characterization result finding. ";
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public void deleteFinding(Finding finding)
			throws CharacterizationException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(finding);
		} catch (Exception e) {
			String err = "Error deleting finding " + finding.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public void saveExperimentConfig(ExperimentConfigBean configBean)
			throws ExperimentConfigException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			ExperimentConfig config = configBean.getDomain();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Boolean newConfig = true;
			Boolean newTechnique = true;
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				newConfig = false;
				ExperimentConfig dbConfig = helper
						.findExperimentConfigById(config.getId().toString());
				if (dbConfig != null) {
					// reuse original createdBy if it is not COPY
					if (!dbConfig.getCreatedBy().equals(
							Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
						config.setCreatedBy(dbConfig.getCreatedBy());
					}
					// reuse original created date
					config.setCreatedDate(dbConfig.getCreatedDate());
				}
			}
			Technique technique = config.getTechnique();
			// check if technique already exists;
			Technique dbTechnique = findTechniqueByType(technique.getType());
			if (dbTechnique != null) {
				newTechnique = false;
				technique.setId(dbTechnique.getId());
				technique.setCreatedBy(dbTechnique.getCreatedBy());
				technique.setCreatedDate(dbTechnique.getCreatedDate());
			} else {
				technique.setId(null);
				technique.setCreatedBy(config.getCreatedBy());
				technique.setCreatedDate(new Date());
			}
			if (config.getInstrumentCollection() != null) {
				Collection<Instrument> instruments = new HashSet<Instrument>(
						config.getInstrumentCollection());
				config.setInstrumentCollection(new HashSet<Instrument>());
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
						instrument.setId(null);
					}
					config.getInstrumentCollection().add(instrument);
				}
			}
			appService.saveOrUpdate(config);
			if (newConfig) {
				this.saveDefaultAccessibility(config.getId().toString());
				// assign public access to instrument
				for (Instrument instrument : config.getInstrumentCollection()) {
					this.saveAccessibility(Constants.CSM_PUBLIC_ACCESS,
							instrument.getId().toString());
				}
			}
			if (newTechnique) {
				//assign public access to technique
				this.saveAccessibility(Constants.CSM_PUBLIC_ACCESS, config
						.getTechnique().getId().toString());
			}

		} catch (Exception e) {
			String err = "Error in saving the technique and associated instruments.";
			logger.error(err, e);
			throw new ExperimentConfigException(err, e);
		}
	}

	public void deleteExperimentConfig(ExperimentConfig config)
			throws ExperimentConfigException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				ExperimentConfig dbConfig = helper
						.findExperimentConfigById(config.getId().toString());
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
						instrument.setCreatedDate(DateUtils
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

	private Technique findTechniqueByType(String type)
			throws ExperimentConfigException {
		Technique technique = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Technique.class)
					.add(
							Property.forName("type").eq(new String(type))
									.ignoreCase());
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

	private Instrument findInstrumentBy(String type, String manufacturer,
			String modelName) throws Exception {
		Instrument instrument = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class);
		crit.add(Restrictions.eq("type", type).ignoreCase());
		crit.add(Restrictions.eq("manufacturer", manufacturer).ignoreCase());
		crit.add(Restrictions.eq("modelName", modelName).ignoreCase());
		List results = appService.query(crit);
		for (Object obj : results) {
			instrument = (Instrument) obj;
		}
		return instrument;
	}

	public void copyAndSaveCharacterization(CharacterizationBean charBean,
			SampleBean oldSampleBean, SampleBean[] newSampleBeans,
			boolean copyData) throws CharacterizationException,
			NoAccessException {
		try {
			for (SampleBean sampleBean : newSampleBeans) {
				// create a deep copy
				Characterization copy = charBean.getDomainCopy(copyData);
				CharacterizationBean copyBean = new CharacterizationBean(copy);
				// try {
				// // copy file visibility
				// for (FindingBean findingBean : copyBean.getFindings()) {
				// for (FileBean fileBean : findingBean.getFiles()) {
				// fileHelper
				// .retrieveVisibilityAndContentForCopiedFile(
				// fileBean, user);
				// }
				// }
				// } catch (Exception e) {
				// String error = "Error setting visibility of the copy.";
				// throw new CharacterizationException(error, e);
				// }
				/**
				 * Need to save associate Config & Finding in copy bean first,
				 * otherwise will get "transient object" Hibernate exception.
				 */
				List<ExperimentConfigBean> expConfigs = copyBean
						.getExperimentConfigs();
				if (expConfigs != null && !expConfigs.isEmpty()) {
					for (ExperimentConfigBean configBean : expConfigs) {
						this.saveExperimentConfig(configBean);
					}
				}
				List<FindingBean> findings = copyBean.getFindings();
				// copy file visibility and replace file URI with new sample
				// name
				if (findings != null && !findings.isEmpty()) {
					for (FindingBean findingBean : findings) {
						for (FileBean fileBean : findingBean.getFiles()) {
							fileService.updateClonedFileInfo(fileBean,
									oldSampleBean.getDomain().getName(),
									sampleBean.getDomain().getName());
						}
						this.saveFinding(findingBean);
					}
				}
				this.saveCharacterization(sampleBean, copyBean);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error saving the copy of characterization.";
			throw new CharacterizationException(error, e);
		}
	}

	public int getNumberOfPublicCharacterizations(
			String characterizationClassName) throws CharacterizationException {
		try {
			int count = helper
					.getNumberOfPublicCharacterizations(characterizationClassName);
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public characterizations of type "
					+ characterizationClassName;
			logger.error(err, e);
			throw new CharacterizationException(err, e);

		}
	}

	public List<String> findOtherCharacterizationByAssayCategory(
			String assayCategory) throws CharacterizationException {
		List<String> charNames = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					OtherCharacterization.class).add(
					Property.forName("assayCategory").eq(assayCategory)
							.ignoreCase());
			List result = appService.query(crit);
			for (Object obj : result) {
				String charName = ((OtherCharacterization) obj).getName();
				if (!charNames.contains(charName)) {
					charNames.add(charName);
				}
			}
		} catch (Exception e) {
			String err = "Error finding other characterizations by assay cateogry "
					+ assayCategory;
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
		return charNames;
	}

	public void assignAccessibility(AccessibilityBean access,
			Characterization achar) throws CharacterizationException,
			NoAccessException {
		String sampleCreatedBy = achar.getSample().getCreatedBy();
		// if current user is not owner of the sample and not the owner of the
		// characterization can't assign access
		if (!isUserOwner(sampleCreatedBy) && !isUserOwner(achar.getCreatedBy())) {
			throw new NoAccessException();
		}
		String charId = achar.getId().toString();
		try {
			// characterization
			super.saveAccessibility(access, charId);
			if (achar.getFindingCollection() != null) {
				for (Finding finding : achar.getFindingCollection()) {
					if (finding != null) {
						this.assignAccessibility(access, finding);
					}
				}
			}
			// ExperimentConfiguration
			if (achar.getExperimentConfigCollection() != null) {
				for (ExperimentConfig config : achar
						.getExperimentConfigCollection()) {
					this.assignAccessibility(access, config);
				}
			}
		} catch (Exception e) {
			String err = "Error in assigning accessibility to the characterization "
					+ achar.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	private void assignAccessibility(AccessibilityBean access, Finding finding)
			throws Exception {
		super.saveAccessibility(access, finding.getId().toString());
		// files
		if (finding.getFileCollection() != null) {
			for (File file : finding.getFileCollection()) {
				if (file != null && file.getId() != null) {
					super.saveAccessibility(access, file.getId().toString());
				}
			}
		}
		// datum, need to check for null for copy bean.
		if (finding.getDatumCollection() != null) {
			for (Datum datum : finding.getDatumCollection()) {
				if (datum != null && datum.getId() != null) {
					super.saveAccessibility(access, datum.getId().toString());
				}
				// condition
				if (datum.getConditionCollection() != null) {
					for (Condition condition : datum.getConditionCollection()) {
						super.saveAccessibility(access, condition.getId()
								.toString());
					}
				}
			}
		}
	}

	private void assignAccessibility(AccessibilityBean access,
			ExperimentConfig config) throws Exception {
		super.saveAccessibility(access, config.getId().toString());
		// // assign instruments and technique to public visibility
		// if (config.getTechnique() != null) {
		// super.saveAccessibility(Constants.CSM_PUBLIC_ACCESS, config
		// .getTechnique().getId().toString());
		// }
		// if (config.getInstrumentCollection() != null) {
		// for (Instrument instrument : config.getInstrumentCollection()) {
		// super.saveAccessibility(Constants.CSM_PUBLIC_ACCESS, instrument
		// .getId().toString());
		// }
		// }
	}

	public CharacterizationServiceHelper getHelper() {
		return helper;
	}

	public void assignAccesses(Characterization achar)
			throws CharacterizationException, NoAccessException {
		try {
			if (!securityService
					.checkCreatePermission(achar.getId().toString())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(achar.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.assignAccessibility(access, achar);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning characterization accessibility";
			throw new CharacterizationException(error, e);
		}
	}

	public void removeAccess(Characterization achar)
			throws CharacterizationException, NoAccessException {
		try {
			if (!securityService
					.checkCreatePermission(achar.getId().toString())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(achar.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.removeAccessibility(access, achar);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning characterization accessibility";
			throw new CharacterizationException(error, e);
		}
	}
}