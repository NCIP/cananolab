package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.Row;
import gov.nih.nci.cananolab.dto.common.TableCell;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving characterizations
 *
 * @author tanq, pansu
 */
public class CharacterizationServiceHelper {
	/**
	 * Constants for generating Excel report for summary view.
	 */
	public static final String ASSAY_TYPE = "Assay Type";
	public static final String PHY_CHM_CHAR = "Physico-Chemical Characterization";
	public static final String POC = "Point of Contact";
	public static final String CHAR_DATE = "Characterization Date";
	public static final String PROTOCOL = "Protocol";
	public static final String PROPERTIES = "Properties";
	public static final String DESIGN_DESCRIPTION = "Design Description";
	public static final String TECH_INSTRUMENTS = "Techniques and Instruments";
	public static final String CHAR_RESULT = "Characterizaiton Result #";
	public static final String ANALYSIS_CONCLUSION = "Analysis and Conclusion";
	public static final String TECHNIQUE = "Technique";
	public static final String INSTRUMENT = "Instruments";
	public static final String DESCRIPTION = "Description";
	public static final String FILES = "File(s)";
	public static final String PRIVATE_FILE = "Private File";
	public static final String DATA_CONDITIONS = "Data and Conditions";
	public static final String CYTOTOXICITY = "Cytotoxicity";
	public static final String CELL_LINE = "Cell Line";
	public static final String ENZYMEINDUCTION = "EnzymeInduction";
	public static final String ENZYME_NAME = "Enzyme Name";
	public static final String PHYSICALSTATE = "PhysicalState";
	public static final String TYPE = "Type";
	public static final String SHAPE = "Shape";
	public static final String ASPECT_RATIO = "Aspect Ratio";
	public static final String MINIMUM_DIMENSION = "Minimum Dimension";
	public static final String MAXIMUM_DIMENSION = "Maximum Dimension";
	public static final String SOLUBILITY = "Solubility";
	public static final String SOLVENT = "Solvent";
	public static final String IS_SOLUBLE = "Is Soluble?";
	public static final String CONCENTRATION = "Critical Concentration";
	public static final String SURFACE = "Surface";
	public static final String IS_HYDROPHOBIC = "Is Hydrophobic?";

	private static String fileRoot = PropertyUtils.getProperty(
			Constants.FILEUPLOAD_PROPERTY, Constants.FILE_REPOSITORY_DIR);

	private static Logger logger = Logger
			.getLogger(CharacterizationServiceHelper.class);

	public CharacterizationServiceHelper() {
	}

	public Characterization findCharacterizationById(String charId)
			throws Exception {
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
		}
		return achar;
	}

	public List<Characterization> findSampleCharacterizationsByClass(
			String sampleName, String className) throws Exception {
		List<Characterization> charas = new ArrayList<Characterization>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Class
				.forName(className));
		crit.add(Restrictions.eq("sample.name", sampleName));
		crit.setFetchMode("pointOfContact", FetchMode.JOIN);
		crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("protocol", FetchMode.JOIN);
		crit.setFetchMode("protocol.file", FetchMode.JOIN);
		crit.setFetchMode("protocol.file.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique",
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
		for (Object obj : result) {
			charas.add((Characterization) obj);
		}
		return charas;
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
			charNames.add(((OtherCharacterization) obj).getName());
		}
		return charNames;
	}

	public Protocol findProtocolByCharacterizationId(
			java.lang.String characterizationId, Boolean filterPublic) throws Exception {
		Protocol protocol = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select aChar.protocol from gov.nih.nci.cananolab.domain.particle.characterization.Characterization aChar where aChar.id="
				+ characterizationId;
		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		List filteredResults=new ArrayList(results);
		if (filterPublic) {
			AuthorizationService authService=new AuthorizationService(Constants.CSM_APP_NAME);
			filteredResults=authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			protocol = (Protocol) obj;
		}
		return protocol;
	}

	public List<Finding> findFindingsByCharacterizationId(String charId, Boolean filterPublic)
			throws Exception {
		List<Finding> findingCollection = new ArrayList<Finding>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.findingCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		List filteredResults=new ArrayList(results);
		if (filterPublic) {
			AuthorizationService authService=new AuthorizationService(Constants.CSM_APP_NAME);
			filteredResults=authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			Finding finding = (Finding) obj;
			findingCollection.add(finding);
		}
		return findingCollection;
	}

	public List<ExperimentConfig> findExperimentConfigsByCharacterizationId(String charId, Boolean filterPublic)
			throws Exception {
		List<ExperimentConfig> configCollection = new ArrayList<ExperimentConfig>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.experimentConfigCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);

		List results = appService.query(crit);
		List filteredResults=new ArrayList(results);
		if (filterPublic) {
			AuthorizationService authService=new AuthorizationService(Constants.CSM_APP_NAME);
			filteredResults=authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			ExperimentConfig config = (ExperimentConfig) obj;
			configCollection.add(config);
		}
		return configCollection;
	}

	public List<File> findFilesByCharacterizationId(String charId, Boolean filterPublic)
			throws Exception {
		List<File> fileCollection = new ArrayList<File>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.fileCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		List filteredResults=new ArrayList(results);
		if (filterPublic) {
			AuthorizationService authService=new AuthorizationService(Constants.CSM_APP_NAME);
			filteredResults=authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			File file = (File) obj;
			fileCollection.add(file);
		}
		return fileCollection;
	}

	/**
	 * Export sample characterization summary report as Excel spread sheet.
	 *
	 * @param summaryBean
	 * @param out
	 * @throws IOException
	 */
	public void exportSummary(CharacterizationSummaryViewBean summaryBean,
			HttpServletRequest request, OutputStream out) throws IOException {
		if (out != null) {
			HSSFWorkbook wb = new HSSFWorkbook();
			this.outputSummarySheet(summaryBean, request, wb);
			wb.write(out);
			out.flush();
			out.close();
		}
	}

	/**
	 * Output Sample Characterization Summary report
	 * (==> bodyCharacterizationSummaryPrintViewTable.jsp)
	 *
	 * @param summaryBean
	 * @param wb
	 * @throws IOException
	 */
	private void outputSummarySheet(
			CharacterizationSummaryViewBean summaryBean,
			HttpServletRequest request, HSSFWorkbook wb) throws IOException {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		int charCount = 1;
		Map<String, SortedSet<CharacterizationBean>> pubs =
			summaryBean.getType2Characterizations();
		for (String type : summaryBean.getCharacterizationTypes()) {
			// Output data of report
			SortedSet<CharacterizationBean> charBeans = pubs.get(type);
			for (CharacterizationBean charBean : charBeans) {
				int rowIndex = 0;

				// Create one work sheet for each Characterization.
				HSSFSheet sheet = wb.createSheet(charCount++ + "."
						+ charBean.getCharacterizationName());

				// 1. Output Characterization type at (0, 0).
				rowIndex = this.outputHeader(charBean, sheet, headerStyle,
						rowIndex);

				// 2. Output Assay Type (2, 0).
				rowIndex = this.outputAssayType(charBean, sheet, headerStyle,
						rowIndex);

				// 3. Output POC at (3, 0).
				rowIndex = this.outputPOC(charBean, sheet, headerStyle,
						rowIndex);

				// 4. Output Characterization Date at (4, 0).
				rowIndex = this.outputCharDate(charBean, sheet, headerStyle,
						rowIndex);

				// 5. Output Protocol at (5, 0).
				rowIndex = this.outputProtocol(charBean, sheet, headerStyle,
						rowIndex);

				// 6. Output Properties at (6, 0).
				rowIndex = this.outputProperties(charBean, sheet, headerStyle,
						rowIndex);

				// 7. Output Design Description at (7, 0).
				rowIndex = this.outputDesignDescription(charBean, sheet,
						headerStyle, rowIndex);

				// 8. Output Technique and Instruments at (8, 0).
				rowIndex = this.outputTechInstruments(charBean, sheet,
						headerStyle, rowIndex);

				// 9. Output Characterization Results at (9, 0).
				rowIndex = this.outputCharResults(charBean, request, wb, sheet,
						headerStyle, rowIndex);

				// 10.Output Analysis and Conclusion at (10, 0).
				rowIndex = this.outputConclusion(charBean, sheet, headerStyle,
						rowIndex);
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
	private int outputHeader(CharacterizationBean charBean, HSSFSheet sheet,
			HSSFCellStyle headerStyle, int rowIndex) {
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
	private int outputAssayType(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		Characterization charactization = (Characterization) charBean
				.getDomainChar();

		// 3. Output Assay Type (2, 0).
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(charactization.getAssayType())) {
			if (PHY_CHM_CHAR.equals(charBean.getCharacterizationType())) {
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
	 * Output POC for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private int outputPOC(CharacterizationBean charBean, HSSFSheet sheet,
			HSSFCellStyle headerStyle, int rowIndex) {
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
	 * Output Characterization Date for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private int outputCharDate(CharacterizationBean charBean,
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
	 * Output Protocol for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private int outputProtocol(CharacterizationBean charBean,
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
	 * Output Properties for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private int outputProperties(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7. Output Properties at (7, 0).
		if (charBean.isWithProperties()) {
			rowIndex++; // Leave one empty line as separator.
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, PROPERTIES);

			String detailPage = gov.nih.nci.cananolab.ui.sample.InitCharacterizationSetup
					.getInstance().getDetailPage(charBean.getDomainChar());
			if (!StringUtils.isEmpty(detailPage)) {
				if (detailPage.indexOf(CYTOTOXICITY) != -1) {
					rowIndex = this.outputCytotoxicity(charBean, sheet,
							headerStyle, rowIndex);
				} else if (detailPage.indexOf(ENZYMEINDUCTION) != -1) {
					rowIndex = this.outputEnzymeInduction(charBean, sheet,
							headerStyle, rowIndex);
				} else if (detailPage.indexOf(PHYSICALSTATE) != -1) {
					rowIndex = this.outputPhysicalState(charBean, sheet,
							headerStyle, rowIndex);
				} else if (detailPage.indexOf(SHAPE) != -1) {
					rowIndex = this.outputShape(charBean, sheet, headerStyle,
							rowIndex);
				} else if (detailPage.indexOf(SOLUBILITY) != -1) {
					rowIndex = this.outputSolubility(charBean, sheet,
							headerStyle, rowIndex);
				} else if (detailPage.indexOf(SURFACE) != -1) {
					rowIndex = this.outputSurface(charBean, sheet, headerStyle,
							rowIndex);
				}
			}
			rowIndex++; // Leave one empty line as separator.
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
	private int outputCytotoxicity(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7a. Output Cytotoxicity Info.
		if (!StringUtils.isEmpty(charBean.getCytotoxicity().getCellLine())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, CYTOTOXICITY);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean.getCytotoxicity()
					.getCellLine());
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
	private int outputEnzymeInduction(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7b. Output EnzymeInduction Info.
		if (!StringUtils.isEmpty(charBean.getEnzymeInduction().getEnzyme())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, ENZYME_NAME);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean
					.getEnzymeInduction().getEnzyme());
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
	private int outputPhysicalState(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 7c. Output PhysicalState Info.
		if (!StringUtils.isEmpty(charBean.getPhysicalState().getType())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, TYPE);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0,
					charBean.getPhysicalState().getType());
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
	private int outputShape(CharacterizationBean charBean, HSSFSheet sheet,
			HSSFCellStyle headerStyle, int rowIndex) {
		// 7d. Output Shape Info.
		if (!StringUtils.isEmpty(charBean.getShape().getType())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, TYPE);
			ExportUtils.createCell(row, 1, headerStyle, ASPECT_RATIO);
			ExportUtils.createCell(row, 2, headerStyle,
					MINIMUM_DIMENSION);
			ExportUtils.createCell(row, 3, headerStyle,
					MAXIMUM_DIMENSION);

			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, charBean.getShape()
					.getType());
			ExportUtils.createCell(row, 1, String.valueOf(charBean
					.getShape().getAspectRatio()));

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
	private int outputSolubility(CharacterizationBean charBean,
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
	private int outputSurface(CharacterizationBean charBean, HSSFSheet sheet,
			HSSFCellStyle headerStyle, int rowIndex) {
		// 7f. Output Solubility Info.
		if (charBean.getSurface().getIsHydrophobic() != null) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, IS_HYDROPHOBIC);
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, String.valueOf(charBean
					.getSurface().getIsHydrophobic()));
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
	private int outputDesignDescription(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		Characterization charactization = (Characterization) charBean
				.getDomainChar();

		// 7. Output Design Description at (6, 0).
		if (!StringUtils.isEmpty(charactization.getDesignMethodsDescription())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle,
					DESIGN_DESCRIPTION);
			ExportUtils.createCell(row, 1, charactization
					.getDesignMethodsDescription());
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
	private int outputTechInstruments(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 8. Output Technique and Instruments at (7, 0).
		List<ExperimentConfigBean> configList = charBean.getExperimentConfigs();
		if (configList != null && !configList.isEmpty()) {
			rowIndex++; // Leave one empty line as separator.
			StringBuilder sb = new StringBuilder();
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle,
					TECH_INSTRUMENTS);

			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, TECHNIQUE);
			ExportUtils.createCell(row, 1, headerStyle, INSTRUMENT);
			ExportUtils.createCell(row, 2, headerStyle, DESCRIPTION);
			for (ExperimentConfigBean config : configList) {
				row = sheet.createRow(rowIndex++);
				ExportUtils.createCell(row, 0, config
						.getTechniqueDisplayName());

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

	/**
	 * Output Characterization Results for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @throws IOException
	 */
	private int outputCharResults(CharacterizationBean charBean,
			HttpServletRequest request, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFCellStyle headerStyle, int rowIndex) throws IOException {
		// 9. Output Characterization Results at (8, 0).
		List<FindingBean> findings = charBean.getFindings();
		if (findings != null && !findings.isEmpty()) {
			int count = 1;
			for (FindingBean findingBean : findings) {
				rowIndex++; // Create one empty line as separator.
				HSSFRow row = sheet.createRow(rowIndex++);
				ExportUtils.createCell(row, 0, headerStyle, CHAR_RESULT
						+ count);

				// 9a. Output Characterization File Results.
				this.outputFileResult(findingBean, request, wb, sheet,
						headerStyle, rowIndex);

				// 9b. Output Characterization Datum Results.
				this.outputDatumResult(findingBean, sheet, headerStyle,
						rowIndex);
			}
		}
		return rowIndex;
	}

	/**
	 * Output Files in Characterization Results for work sheet.
	 *
	 * @param findingBean
	 * @param request
	 * @param wb
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @throws IOException
	 */
	private int outputFileResult(FindingBean findingBean,
			HttpServletRequest request, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFCellStyle headerStyle, int rowIndex) throws IOException {
		// Get list of FileBeans from findingBean.
		List<FileBean> files = findingBean.getFiles();
		if (files != null && !files.isEmpty()) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, FILES);
			for (FileBean fileBean : files) {
				row = sheet.createRow(rowIndex++);
				if (fileBean.getDomainFile().getUriExternal().booleanValue()) {
					ExportUtils.createCell(row, 0, fileBean.getDomainFile().getUri());
					/**
					 * sb.append(request.getRequestURL().toString());
					 * sb.append("?dispatch=download&fileId=");
					 * sb.append(fileBean.getDomainFile().getId());
					 * sb.append("&location=").append(request.getParameter("location"));
					 * rowIndex = this.outputPicture(rowIndex, sb.toString(),
					 * wb, sheet);
					 */
				} else if (fileBean.isHidden()) {
					ExportUtils.createCell(row, 0, PRIVATE_FILE);
				} else {
					ExportUtils.createCell(row, 0, fileBean
							.getDomainFile().getTitle());

					StringBuilder sb = new StringBuilder();
					if (fileBean.isImage()) {
						sb.append(fileRoot).append(java.io.File.separator);
						sb.append(fileBean.getDomainFile().getUri());
						String filePath = sb.toString();
						java.io.File imgFile = new java.io.File(filePath);
						if (imgFile.exists()) {
							try {
								rowIndex =
									ExportUtils.createImage(rowIndex, (short) 0, filePath, wb, sheet);
							}
							catch (Exception e) {
								logger.error(
										"Error exporting Characterization image data.", e);
							}
						} else {
							logger.error("Characterization image file not exists: " + filePath);
						}
					}
				}
			}
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
	private int outputDatumResult(FindingBean findingBean, HSSFSheet sheet,
			HSSFCellStyle headerStyle, int rowIndex) {

		// Get list of Rows from findingBean.
		List<Row> rows = findingBean.getRows();
		if (rows != null && !rows.isEmpty()) {
			// Output general header "Data and Conditions".
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils
					.createCell(row, 0, headerStyle, DATA_CONDITIONS);

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
	 * Output Analysis and Conclusion for work sheet.
	 *
	 * @param charBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private int outputConclusion(CharacterizationBean charBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 9. Output Analysis and Conclusion at last.
		if (!StringUtils.isEmpty(charBean.getConclusion())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle,
					ANALYSIS_CONCLUSION);
			ExportUtils.createCell(row, 1, charBean.getConclusion());
		}
		return rowIndex;
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
			crit.setFetchMode("technique", FetchMode.JOIN);
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