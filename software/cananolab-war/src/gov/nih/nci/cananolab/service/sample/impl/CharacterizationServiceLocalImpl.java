package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.characterization.invitro.EnzymeInduction;
import gov.nih.nci.cananolab.domain.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.characterization.physical.Surface;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.Row;
import gov.nih.nci.cananolab.dto.common.TableCell;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving local characterizations
 * 
 * @author tanq, pansu
 * 
 */
public class CharacterizationServiceLocalImpl implements
		CharacterizationService {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceLocalImpl.class);
	private CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
	private FileServiceHelper fileHelper = new FileServiceHelper();
	public static final String ANALYSIS_CONCLUSION = "Analysis and Conclusion";
	public static final String ASPECT_RATIO = "Aspect Ratio";
	
	/**
	 * Constants for generating Excel report for summary view.
	 */
	private static String fileRoot = PropertyUtils.getProperty(
			Constants.CANANOLAB_PROPERTY, Constants.FILE_REPOSITORY_DIR);
	public static final String ASSAY_TYPE = "Assay Type";
	public static final String CELL_LINE = "Cell Line";
	public static final String CHAR_DATE = "Characterization Date";
	public static final String CHAR_RESULT = "Characterizaiton Result #";
	public static final String CONCENTRATION = "Critical Concentration";
	public static final String DATA_CONDITIONS = "Data and Conditions";
	public static final String DESCRIPTION = "Description";
	public static final String DESIGN_DESCRIPTION = "Design Description";
	public static final String ENZYME_NAME = "Enzyme Name";
	public static final String FILES = "File(s)";
	public static final String INSTRUMENT = "Instruments";
	public static final String IS_HYDROPHOBIC = "Is Hydrophobic?";
	public static final String IS_SOLUBLE = "Is Soluble?";
	public static final String MAXIMUM_DIMENSION = "Maximum Dimension";
	public static final String MINIMUM_DIMENSION = "Minimum Dimension";
	public static final String POC = "Point of Contact";
	public static final String PRIVATE_FILE = "Private File";
	public static final String PROPERTIES = "Properties";
	public static final String PROTOCOL = "Protocol";
	public static final String SOLVENT = "Solvent";
	public static final String TECH_INSTRUMENTS = "Techniques and Instruments";
	public static final String TECHNIQUE = "Technique";
	public static final String TYPE = "Type";

	public CharacterizationServiceLocalImpl() {
	}

	public void saveCharacterization(SampleBean sampleBean,
			CharacterizationBean charBean, UserBean user)
			throws CharacterizationException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			Characterization achar = charBean.getDomainChar();
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

			// save file data to file system and assign visibility
			List<FindingBean> findingBeans = charBean.getFindings();
			if (findingBeans != null && !findingBeans.isEmpty()) {
				FileService fileService = new FileServiceLocalImpl();
				for (FindingBean findingBean : findingBeans) {
					for (FileBean fileBean : findingBean.getFiles()) {
						fileService.prepareSaveFile(fileBean.getDomainFile(),
								user);
						fileService.writeFile(fileBean, user);
					}
				}
			}
			appService.saveOrUpdate(achar);

			// set visibility
			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			helper.assignVisibility(charBean.getDomainChar(), visibleGroups,
					owningGroup);
		} catch (Exception e) {
			String err = "Problem in saving the characterization.";
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public CharacterizationBean findCharacterizationById(String charId,
			UserBean user) throws CharacterizationException, NoAccessException {
		try {
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

			List result = appService.query(crit);
			CharacterizationBean charBean = null;
			if (!result.isEmpty()) {
				achar = (Characterization) result.get(0);
				if (helper.getAuthService().checkReadPermission(user,
						achar.getId().toString())) {
					charBean = new CharacterizationBean(achar);
					for (FindingBean finding : charBean.getFindings()) {
						fileHelper.checkReadPermissionAndRetrieveVisibility(
								finding.getFiles(), user);
					}
				} else {
					throw new NoAccessException(
							"User doesn't have access to the sample");
				}
			}
			return charBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new CharacterizationException();
		}
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

	public void deleteCharacterization(Characterization chara, UserBean user)
			throws CharacterizationException, NoAccessException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(chara);
			helper.removeVisibility(chara);

		} catch (Exception e) {
			String err = "Error deleting characterization " + chara.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public List<CharacterizationBean> findCharacterizationsBySampleId(
			String sampleId, UserBean user) throws CharacterizationException {
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
			List<CharacterizationBean> chars = new ArrayList<CharacterizationBean>();
			for (Object obj : results) {
				Characterization achar = (Characterization) obj;
				if (helper.getAuthService().checkReadPermission(user,
						achar.getId().toString())) {
					if (achar.getProtocol() != null) {
						if (!helper.getAuthService().checkReadPermission(user,
								achar.getProtocol().getId().toString())) {
							achar.setProtocol(null);
						}
					}
					CharacterizationBean charBean = new CharacterizationBean(
							achar);
					for (FindingBean finding : charBean.getFindings()) {
						fileHelper.checkReadPermissionAndRetrieveVisibility(
								finding.getFiles(), user);
					}
					chars.add(charBean);
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

	public FindingBean findFindingById(String findingId, UserBean user)
			throws CharacterizationException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Finding.class)
					.add(Property.forName("id").eq(new Long(findingId)));
			crit.setFetchMode("datumCollection", FetchMode.JOIN);
			crit.setFetchMode("datumCollection.conditionCollection",
					FetchMode.JOIN);
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			List result = appService.query(crit);
			Finding finding = null;
			FindingBean findingBean = null;
			if (!result.isEmpty()) {
				finding = (Finding) result.get(0);
				findingBean = new FindingBean(finding);
			}
			return findingBean;
		} catch (Exception e) {
			String err = "Error getting finding of ID " + findingId;
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public void saveFinding(FindingBean finding, UserBean user)
			throws CharacterizationException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : finding.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
			}
			appService.saveOrUpdate(finding.getDomain());
			// save file data to file system and assign visibility
			for (FileBean fileBean : finding.getFiles()) {
				fileService.writeFile(fileBean, user);
			}
			// visibility assignment is handled by saving characterization
		} catch (Exception e) {
			String err = "Error saving characterization result finding. ";
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public void deleteFinding(Finding finding, UserBean user)
			throws CharacterizationException, NoAccessException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			helper.removeVisibility(finding);
			appService.delete(finding);

		} catch (Exception e) {
			String err = "Error deleting finding " + finding.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public void saveExperimentConfig(ExperimentConfigBean configBean,
			UserBean user) throws ExperimentConfigException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			ExperimentConfig config = configBean.getDomain();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				ExperimentConfig dbConfig = helper.findExperimentConfigById(
						config.getId().toString(), user);
				if (dbConfig != null) {
					// don't change createdBy if it is not COPY
					if (!dbConfig.getCreatedBy().equals(
							Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
						config.setCreatedBy(dbConfig.getCreatedBy());
					}
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
				technique.setId(null);
				technique.setCreatedBy(config.getCreatedBy());
				technique.setCreatedDate(new Date());
			}
			// check if instrument already exists;
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
			// visibility assignment is handled by saving characterization

		} catch (Exception e) {
			String err = "Error in saving the technique and associated instruments.";
			logger.error(err, e);
			throw new ExperimentConfigException(err, e);
		}
	}

	public void deleteExperimentConfig(ExperimentConfig config, UserBean user)
			throws ExperimentConfigException, NoAccessException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				ExperimentConfig dbConfig = helper.findExperimentConfigById(
						config.getId().toString(), user);
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

	private Instrument findInstrumentBy(String type, String manufacturer,
			String modelName) throws ExperimentConfigException {
		Instrument instrument = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class);
			if (!StringUtils.isEmpty(type)) {
				crit.add(Restrictions.eq("type", type));
			}
			if (!StringUtils.isEmpty(manufacturer)) {
				crit.add(Restrictions.eq("manufacturer", manufacturer));
			}
			if (!StringUtils.isEmpty(modelName)) {
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

	public void copyAndSaveCharacterization(CharacterizationBean charBean,
			SampleBean oldSampleBean, SampleBean[] newSampleBeans,
			boolean copyData, UserBean user) throws CharacterizationException,
			NoAccessException {
		try {
			for (SampleBean sampleBean : newSampleBeans) {
				// create a deep copy
				Characterization copy = charBean.getDomainCopy(copyData);
				CharacterizationBean copyBean = new CharacterizationBean(copy);
				try {
					// copy file visibility
					for (FindingBean findingBean : copyBean.getFindings()) {
						for (FileBean fileBean : findingBean.getFiles()) {
							fileHelper
									.retrieveVisibilityAndContentForCopiedFile(
											fileBean, user);
						}
					}
				} catch (Exception e) {
					String error = "Error setting visibility of the copy.";
					throw new CharacterizationException(error, e);
				}
				/**
				 * Need to save associate Config & Finding in copy bean first,
				 * otherwise will get "transient object" Hibernate exception.
				 */
				List<ExperimentConfigBean> expConfigs = copyBean
						.getExperimentConfigs();
				if (expConfigs != null && !expConfigs.isEmpty()) {
					for (ExperimentConfigBean configBean : expConfigs) {
						this.saveExperimentConfig(configBean, user);
					}
				}
				List<FindingBean> findings = copyBean.getFindings();
				// replace file URI with new sample name
				if (findings != null && !findings.isEmpty()) {
					for (FindingBean findingBean : findings) {
						for (FileBean fileBean : findingBean.getFiles()) {
							String newUri = fileBean
									.getDomainFile()
									.getUri()
									.replace(
											oldSampleBean.getDomain().getName(),
											sampleBean.getDomain().getName());
							fileBean.getDomainFile().setUri(newUri);
						}
						this.saveFinding(findingBean, user);
					}
				}
				this.saveCharacterization(sampleBean, copyBean, user);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error saving the copy of characterization.";
			throw new CharacterizationException(error, e);
		}
	}

	/**
	 * Export sample characterization summary report as Excel spread sheet.
	 *
	 * @param summaryBean
	 * @param out
	 * @throws IOException
	 */
	public static void exportSummary(List<String> charTypes,
			CharacterizationSummaryViewBean summaryBean, String downloadURL,
			OutputStream out) throws IOException {
		if (out != null) {
			HSSFWorkbook wb = new HSSFWorkbook();
			outputSummarySheet(charTypes, summaryBean, downloadURL, wb);
			wb.write(out);
			out.flush();
			out.close();
		}
	}

	/**
	 * Output Sample Characterization Summary report (==>
	 * bodyCharacterizationSummaryPrintViewTable.jsp)
	 *
	 * @param summaryBean
	 * @param wb
	 * @throws IOException
	 */
	private static void outputSummarySheet(List<String> charTypes,
			CharacterizationSummaryViewBean summaryBean, String downloadURL,
			HSSFWorkbook wb) throws IOException {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);
	
		HSSFCellStyle hlinkStyle = wb.createCellStyle();
		HSSFFont hlinkFont = wb.createFont();
		hlinkFont.setUnderline(HSSFFont.U_SINGLE);
		hlinkFont.setColor(HSSFColor.BLUE.index);
		hlinkStyle.setFont(hlinkFont);
	
		int charCount = 1;
		Map<String, SortedSet<CharacterizationBean>> charBeanMap = summaryBean
				.getType2Characterizations();
		for (String type : charTypes) {
			// Output data of report
			SortedSet<CharacterizationBean> charBeans = charBeanMap.get(type);
			if (charBeans != null && !charBeans.isEmpty()) {
				for (CharacterizationBean charBean : charBeans) {
					int rowIndex = 0;
	
					// Create one work sheet for each Characterization.
					HSSFSheet sheet = wb.createSheet(charCount++ + "."
							+ charBean.getCharacterizationName());
					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	
					// 1. Output Characterization type at (0, 0).
					rowIndex = outputHeader(charBean, sheet, headerStyle,
							rowIndex);
					// 2. Output Assay Type (2, 0).
					rowIndex = outputAssayType(charBean, sheet, headerStyle,
							rowIndex);
					// 3. Output POC at (3, 0).
					rowIndex = outputPOC(charBean, sheet, headerStyle, rowIndex);
					// 4. Output Characterization Date at (4, 0).
					rowIndex = outputCharDate(charBean, sheet, headerStyle,
							rowIndex);
					// 5. Output Protocol at (5, 0).
					rowIndex = outputProtocol(charBean, sheet, headerStyle,
							rowIndex);
					// 6. Output Properties at (6, 0).
					rowIndex = outputProperties(charBean, sheet, headerStyle,
							rowIndex);
					// 7. Output Design Description at (7, 0).
					rowIndex = outputDesignDescription(charBean, sheet,
							headerStyle, rowIndex);
					// 8. Output Technique and Instruments at (8, 0).
					rowIndex = outputTechInstruments(charBean, sheet,
							headerStyle, rowIndex);
					// 9. Output Characterization Results at (9, 0).
					rowIndex = outputCharResults(charBean, downloadURL, wb,
							sheet, headerStyle, hlinkStyle, patriarch, rowIndex);
					// 10.Output Analysis and Conclusion at (10, 0).
					rowIndex = outputConclusion(charBean, sheet, headerStyle,
							rowIndex);
				}
			}
		}
	}

	/**
	 * Output header for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputHeader(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 1. Output Characterization type at (0, 0).
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, charBean
				.getCharacterizationType());
		rowIndex++; // Create one empty line as separator.
	
		// 2. Output Characterization name at (1, 0).
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, charBean
				.getCharacterizationName());
		rowIndex++; // Create one empty line as separator.
	
		return rowIndex;
	}

	/**
	 * Output AssayType for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputAssayType(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		Characterization charactization = (Characterization) charBean
				.getDomainChar();
		// 3. Output Assay Type (2, 0).
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(charactization.getAssayType())) {
			if (Constants.PHYSICOCHEMICAL_CHARACTERIZATION.equals(charBean
					.getCharacterizationType())) {
				sb.append(charBean.getCharacterizationName());
			}
		} else {
			sb.append(charactization.getAssayType());
		}
		if (sb.length() > 0) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, ASSAY_TYPE);
			ExportUtils.createCell(row, 1, sb.toString());
		}
		return rowIndex;
	}

	/**
	 * Output Characterization Date for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputCharDate(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 5. Output Characterization Date at (4, 0).
		if (!StringUtils.isEmpty(charBean.getDateString())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, CHAR_DATE);
			ExportUtils.createCell(row, 1, charBean.getDateString());
		}
		return rowIndex;
	}

	/**
	 * Output Characterization Results for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @throws IOException
	 */
	private static int outputCharResults(CharacterizationBean charBean,
			String downloadURL, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFCellStyle headerStyle, HSSFCellStyle hlinkStyle,
			HSSFPatriarch patriarch, int rowIndex) throws IOException {
		// 9. Output Characterization Results at (8, 0).
		List<FindingBean> findings = charBean.getFindings();
		if (findings != null && !findings.isEmpty()) {
			int count = 1;
			for (FindingBean findingBean : findings) {
				rowIndex++; // Create one empty line as separator.
				HSSFRow row = sheet.createRow(rowIndex++);
				ExportUtils
						.createCell(row, 0, headerStyle, CHAR_RESULT + count);
	
				// 9a. Output Characterization Datum Results.
				rowIndex = outputDatumResult(findingBean, sheet, headerStyle,
						rowIndex);
	
				// 9b. Output Characterization File Results.
				rowIndex = outputFileResult(findingBean, downloadURL, wb,
						sheet, headerStyle, hlinkStyle, patriarch, rowIndex);
				count++;
			}
		}
		return rowIndex;
	}

	/**
	 * Output Analysis and Conclusion for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputConclusion(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 9. Output Analysis and Conclusion at last.
		if (!StringUtils.isEmpty(charBean.getConclusion())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, ANALYSIS_CONCLUSION);
			ExportUtils.createCell(row, 1, charBean.getConclusion());
		}
		return rowIndex;
	}

	/**
	 * Output Cytotoxicity Info, => bodyCytotoxicityInfo.jsp
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputCytotoxicity(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7a. Output Cytotoxicity Info.
		if (!StringUtils.isEmpty(charBean.getCytotoxicity().getCellLine())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, CELL_LINE);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean.getCytotoxicity()
					.getCellLine());
		}
		return rowIndex;
	}

	/**
	 * Output Datums in Characterization Results for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputDatumResult(FindingBean findingBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// Get list of Rows from findingBean.
		List<Row> rows = findingBean.getRows();
		if (rows != null && !rows.isEmpty()) {
			// Output general header "Data and Conditions".
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, DATA_CONDITIONS);
	
			// Output header of each column.
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			for (ColumnHeader column : findingBean.getColumnHeaders()) {
				String displayName = column.getDisplayName().replaceAll("<br>",
						" ");
				ExportUtils.createCell(row, cellIndex++, headerStyle,
						displayName);
			}
			// Output value of each row.
			for (Row rowBean : rows) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				for (TableCell cell : rowBean.getCells()) {
					ExportUtils.createCell(row, cellIndex++, cell.getValue());
				}
			}
		}
		return rowIndex;
	}

	/**
	 * Output Design Description for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputDesignDescription(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		Characterization charactization = (Characterization) charBean
				.getDomainChar();
	
		// 7. Output Design Description at (6, 0).
		if (!StringUtils.isEmpty(charactization.getDesignMethodsDescription())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, DESIGN_DESCRIPTION);
			ExportUtils.createCell(row, 1, charactization
					.getDesignMethodsDescription());
		}
		return rowIndex;
	}

	/**
	 * Output EnzymeInduction Info, => bodyEnzymeInductionInfo.jsp
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputEnzymeInduction(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7b. Output EnzymeInduction Info.
		if (!StringUtils.isEmpty(charBean.getEnzymeInduction().getEnzyme())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, ENZYME_NAME);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean.getEnzymeInduction()
					.getEnzyme());
		}
		return rowIndex;
	}

	/**
	 * Output Files Results for report (=> bodyFindingView.jsp).
	 *
	 * @param findingBean
	 * @param request
	 * @param wb
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @throws IOException
	 */
	private static int outputFileResult(FindingBean findingBean,
			String downloadURL, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFCellStyle headerStyle, HSSFCellStyle hlinkStyle,
			HSSFPatriarch patriarch, int rowIndex) throws IOException {
		// Get list of FileBeans from findingBean.
		List<FileBean> files = findingBean.getFiles();
		if (files != null && !files.isEmpty()) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, FILES);
			for (FileBean fileBean : files) {
				row = sheet.createRow(rowIndex++);
				File file = fileBean.getDomainFile();
	
				// output 4x) titles for File.
				ExportUtils.createCell(row, 0, headerStyle, "File Type");
				ExportUtils.createCell(row, 1, headerStyle,
						"Title and Download Link");
				ExportUtils.createCell(row, 2, headerStyle, "Keywords");
				ExportUtils.createCell(row, 3, headerStyle, "Description");
	
				// 1. output File Type.
				row = sheet.createRow(rowIndex++);
				ExportUtils.createCell(row, 0, file.getType());
	
				// 2. output Title and Download Link.
				StringBuilder sb = new StringBuilder(downloadURL);
				sb.append(file.getId());
				if (file.getUriExternal()) {
					ExportUtils.createCell(row, 1, hlinkStyle, file.getUri(),
							sb.toString());
				} else if (fileBean.isImage()) {
					ExportUtils.createCell(row, 1, file.getTitle());
					sb.setLength(0);
					sb.append(fileRoot).append(java.io.File.separator);
					sb.append(file.getUri());
					String filePath = sb.toString();
					java.io.File imgFile = new java.io.File(filePath);
					if (imgFile.exists()) {
						try {
							rowIndex = ExportUtils.createImage(rowIndex,
									(short) 1, filePath, wb, sheet, patriarch);
						} catch (Exception e) {
							logger.error("Error exporting Char image file.", e);
						}
					} else {
						logger.error("Characterization image file not exists: "
								+ filePath);
					}
				} else {
					ExportUtils.createCell(row, 1, hlinkStyle, file.getTitle(),
							sb.toString());
				}
	
				// 3. output Keywords.
				Collection<Keyword> keywords = file.getKeywordCollection();
				if (keywords != null && !keywords.isEmpty()) {
					sb.setLength(0);
					for (Keyword keyword : keywords) {
						sb.append(',').append(' ').append(keyword.getName());
					}
					ExportUtils.createCell(row, 2, sb.substring(2));
				}
	
				// 4. output Description.
				if (!StringUtils.isEmpty(file.getDescription())) {
					ExportUtils.createCell(row, 3, file.getDescription());
				}
			}
		}
		return rowIndex;
	}

	/**
	 * Output PhysicalState Info for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputPhysicalState(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7c. Output PhysicalState Info.
		if (!StringUtils.isEmpty(charBean.getPhysicalState().getType())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, TYPE);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean.getPhysicalState()
					.getType());
		}
		return rowIndex;
	}

	/**
	 * Output POC for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputPOC(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 4. Output POC at (3, 0).
		if (!StringUtils.isEmpty(charBean.getPocBean().getDisplayName())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, POC);
			ExportUtils.createCell(row, 1, charBean.getPocBean()
					.getDisplayName());
		}
		return rowIndex;
	}

	/**
	 * Output Properties for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputProperties(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7. Output Properties at (7, 0).
		if (charBean.isWithProperties()) {
			rowIndex++; // Leave one empty line as separator.
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, PROPERTIES);
			Characterization domainChar = charBean.getDomainChar();
			if (domainChar instanceof Cytotoxicity) {
				rowIndex = outputCytotoxicity(charBean, sheet, headerStyle,
						rowIndex);
			} else if (domainChar instanceof EnzymeInduction) {
				rowIndex = outputEnzymeInduction(charBean, sheet, headerStyle,
						rowIndex);
			} else if (domainChar instanceof PhysicalState) {
				rowIndex = outputPhysicalState(charBean, sheet, headerStyle,
						rowIndex);
			} else if (domainChar instanceof Shape) {
				rowIndex = outputShape(charBean, sheet, headerStyle, rowIndex);
			} else if (domainChar instanceof Solubility) {
				rowIndex = outputSolubility(charBean, sheet, headerStyle,
						rowIndex);
			} else if (domainChar instanceof Surface) {
				rowIndex = outputSurface(charBean, sheet, headerStyle, rowIndex);
			}
	
			rowIndex++; // Leave one empty line as separator.
		}
		return rowIndex;
	}

	/**
	 * Output Protocol for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputProtocol(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 6. Output Protocol at (6, 0).
		if (!StringUtils.isEmpty(charBean.getProtocolBean().getDisplayName())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, PROTOCOL);
			ExportUtils.createCell(row, 1, charBean.getProtocolBean()
					.getDisplayName());
		}
		return rowIndex;
	}

	/**
	 * Output Shape Info for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputShape(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7d. Output Shape Info.
		if (!StringUtils.isEmpty(charBean.getShape().getType())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, TYPE);
			ExportUtils.createCell(row, 1, headerStyle, ASPECT_RATIO);
			ExportUtils.createCell(row, 2, headerStyle, MINIMUM_DIMENSION);
			ExportUtils.createCell(row, 3, headerStyle, MAXIMUM_DIMENSION);
	
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean.getShape().getType());
			ExportUtils.createCell(row, 1, String.valueOf(charBean.getShape()
					.getAspectRatio()));
	
			StringBuilder sb = new StringBuilder();
			sb.append(charBean.getShape().getMinDimension()).append(' ');
			sb.append(charBean.getShape().getMinDimensionUnit());
			ExportUtils.createCell(row, 2, sb.toString());
	
			sb.setLength(0);
			sb.append(charBean.getShape().getMaxDimension()).append(' ');
			sb.append(charBean.getShape().getMaxDimensionUnit());
			ExportUtils.createCell(row, 3, sb.toString());
		}
		return rowIndex;
	}

	/**
	 * Output Solubility Info for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputSolubility(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7e. Output Solubility Info.
		if (!StringUtils.isEmpty(charBean.getSolubility().getSolvent())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, SOLVENT);
			ExportUtils.createCell(row, 1, headerStyle, IS_SOLUBLE);
			ExportUtils.createCell(row, 2, headerStyle, CONCENTRATION);
	
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean.getSolubility()
					.getSolvent());
			ExportUtils.createCell(row, 1, String.valueOf(charBean
					.getSolubility().getIsSoluble()));
	
			StringBuilder sb = new StringBuilder();
			sb.append(charBean.getSolubility().getCriticalConcentration())
					.append(' ');
			sb.append(charBean.getSolubility().getCriticalConcentrationUnit());
			ExportUtils.createCell(row, 2, sb.toString());
		}
		return rowIndex;
	}

	/**
	 * Output Surface Info for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputSurface(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7f. Output Solubility Info.
		if (charBean.getSurface().getIsHydrophobic() != null) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, IS_HYDROPHOBIC);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, String.valueOf(charBean.getSurface()
					.getIsHydrophobic()));
		}
		return rowIndex;
	}

	/**
	 * Output Technique and Instruments for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputTechInstruments(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 8. Output Technique and Instruments at (7, 0).
		List<ExperimentConfigBean> configList = charBean.getExperimentConfigs();
		if (configList != null && !configList.isEmpty()) {
			rowIndex++; // Leave one empty line as separator.
			StringBuilder sb = new StringBuilder();
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, TECH_INSTRUMENTS);
	
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, TECHNIQUE);
			ExportUtils.createCell(row, 1, headerStyle, INSTRUMENT);
			ExportUtils.createCell(row, 2, headerStyle, DESCRIPTION);
			for (ExperimentConfigBean config : configList) {
				row = sheet.createRow(rowIndex++);
				ExportUtils
						.createCell(row, 0, config.getTechniqueDisplayName());
	
				String[] names = config.getInstrumentDisplayNames();
				if (names != null && names.length > 0) {
					sb.setLength(0);
					for (String name : names) {
						sb.append(name).append(' ');
					}
					sb.deleteCharAt(sb.length() - 1);
					ExportUtils.createCell(row, 1, sb.toString());
				}
	
				if (!StringUtils.isEmpty(config.getDomain().getDescription())) {
					ExportUtils.createCell(row, 2, config.getDomain()
							.getDescription());
				}
			}
		}
		return rowIndex;
	}
}