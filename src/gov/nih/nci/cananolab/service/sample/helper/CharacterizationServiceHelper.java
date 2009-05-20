package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.File;
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
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyReader;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
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
 *
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
	
	private String fileRoot = 
		PropertyReader.getProperty(Constants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
	
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
		crit.setFetchMode("protocolFile", FetchMode.JOIN);
		crit.setFetchMode("protocolFile.protocol", FetchMode.JOIN);
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

	// use in dwr ajax
	public String[] getDerivedDatumValueUnits(String derivedDatumName) {
		try {
			SortedSet<String> units = LookupService.findLookupValues(
					derivedDatumName, "unit");
			SortedSet<String> otherUnits = LookupService.findLookupValues(
					derivedDatumName, "otherUnit");
			units.addAll(otherUnits);
			return units.toArray(new String[0]);
		} catch (Exception e) {
			String err = "Error getting value unit for " + derivedDatumName;
			logger.error(err, e);
			return null;
		}
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
		crit.setFetchMode("protocolFile", FetchMode.JOIN);
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
			java.lang.String characterizationId) throws Exception {
		Protocol protocol = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select aChar.protocol from gov.nih.nci.cananolab.domain.particle.characterization.Characterization aChar where aChar.id="
				+ characterizationId;
		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		for (Object obj : results) {
			protocol = (Protocol) obj;
		}
		return protocol;
	}

	public List<Datum> findDataByCharacterizationId(String charId)
			throws Exception {
		List<Datum> datumCollection = new ArrayList<Datum>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.datumCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		for (Object obj : results) {
			Datum datum = (Datum) obj;
			datumCollection.add(datum);
		}
		return datumCollection;
	}

	public List<File> findFilesByCharacterizationId(String charId)
			throws Exception {
		List<File> fileCollection = new ArrayList<File>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.fileCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		for (Object obj : results) {
			File file = (File) obj;
			fileCollection.add(file);
		}
		return fileCollection;
	}

	/**
	 * Export sample characterization summary report as Excel spread sheet.
	 *
	 * @param summaryBean CharacterizationSummaryViewBean
	 * @param out OutputStream
	 * @throws IOException if error occurred.
	 */
	public void exportSummary(CharacterizationSummaryViewBean summaryBean,
			HttpServletRequest request, OutputStream out) throws IOException {
		if (out != null) {
			HSSFWorkbook wb = new HSSFWorkbook();
			try {
				this.outputSummarySheet(summaryBean, request, wb);
				wb.write(out);
				out.flush();
			} finally {
				out.close();
			}
		}
	}

	/**
	 * Output Excel report for sample Characterization Summary report.
	 *
	 * @param summaryBean CharacterizationSummaryViewBean
	 * @param wb HSSFWorkbook
	 */
	private void outputSummarySheet(
			CharacterizationSummaryViewBean summaryBean,
			HttpServletRequest request, HSSFWorkbook wb) throws IOException {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		SortedMap<String, List<CharacterizationBean>> pubs = summaryBean.getType2Characterizations();
		for (String type : pubs.keySet()) {
			// Output data of report
			List<CharacterizationBean> charBeans = pubs.get(type);
			for (CharacterizationBean charBean : charBeans) {
				int charCount = 0;
				short rowIndex = 0;
				
				// Create one work sheet for each Characterization. 
				HSSFSheet sheet = wb.createSheet(++charCount + '.' + charBean.getCharacterizationName());
				
				//1. Output Characterization type at (0, 0).
				rowIndex = this.outputHeader(charBean, sheet, headerStyle, rowIndex);
				
				//3. Output Assay Type (2, 0).
				rowIndex = this.outputAssayType(charBean, sheet, headerStyle, rowIndex);
				
				//4. Output POC at (3, 0).
				rowIndex = this.outputPOC(charBean, sheet, headerStyle, rowIndex);
					
				//5. Output Characterization Date at (4, 0).
				rowIndex = this.outputCharDate(charBean, sheet, headerStyle, rowIndex);
				
				//6. Output Protocol at (5, 0).
				rowIndex = this.outputProtocol(charBean, sheet, headerStyle, rowIndex);
				
				//7. Output Design Description at (6, 0).
				rowIndex = this.outputDesignDescription(charBean, sheet, headerStyle, rowIndex);
				
				//8. Output Technique and Instruments at (7, 0).
				rowIndex = this.outputTechInstruments(charBean, sheet, headerStyle, rowIndex);
				
				//9. Output Characterization Results at (8, 0).
				rowIndex = this.outputCharResults(charBean, request, wb, sheet, headerStyle, rowIndex);

				//10.Output Analysis and Conclusion at (9, 0).
				rowIndex = this.outputConclusion(charBean, sheet, headerStyle, rowIndex);
			}
		}
	}
	
	/**
	 * Output header for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputHeader(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		//1. Output Characterization type at (0, 0).
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, (short) 0, headerStyle, charBean.getCharacterizationType());
		rowIndex++; // Create one empty line as separator.
		
		//2. Output Characterization name at (1, 0).
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, (short) 0, headerStyle, charBean.getCharacterizationName());
		rowIndex++; // Create one empty line as separator.
		
		return rowIndex;
	}	
	
	/**
	 * Output AssayType for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputAssayType(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		Characterization charactization = (Characterization) charBean.getDomainChar();
		
		//3. Output Assay Type (2, 0).
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
			ExportUtils.createCell(row, (short) 0, headerStyle, ASSAY_TYPE);
			ExportUtils.createCell(row, (short) 1, sb.toString());
		}
		return rowIndex;
	}	
	
	/**
	 * Output POC for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputPOC(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		//4. Output POC at (3, 0).
		if (!StringUtils.isEmpty(charBean.getPocBean().getDisplayName())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, POC);
			ExportUtils.createCell(row, (short) 1, charBean.getPocBean().getDisplayName());
		}
		return rowIndex;
	}	
	
	/**
	 * Output Characterization Date for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputCharDate(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		//5. Output Characterization Date at (4, 0).
		if (!StringUtils.isEmpty(charBean.getDateString())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, CHAR_DATE);
			ExportUtils.createCell(row, (short) 1, charBean.getDateString());
		}
		return rowIndex;
	}	
	
	/**
	 * Output Protocol for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputProtocol(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		//6. Output Protocol at (5, 0).
		if (!StringUtils.isEmpty(charBean.getProtocolBean().getDisplayName())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, PROTOCOL);
			ExportUtils.createCell(row, (short) 1, charBean.getProtocolBean().getDisplayName());
		}
		return rowIndex;
	}	
	
	/**
	 * Output Design Description for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputDesignDescription(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		Characterization charactization = (Characterization) charBean.getDomainChar();
		
		//7. Output Design Description at (6, 0).
		if (!StringUtils.isEmpty(charactization.getDesignMethodsDescription())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, DESIGN_DESCRIPTION);
			ExportUtils.createCell(row, (short) 1, charactization.getDesignMethodsDescription());
		}
		return rowIndex;
	}	
	
	/**
	 * Output Technique and Instruments for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputTechInstruments(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		//8. Output Technique and Instruments at (7, 0).
		List<ExperimentConfigBean> configList = charBean.getExperimentConfigs();
		if (configList != null && !configList.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, TECH_INSTRUMENTS);
			
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, TECHNIQUE);
			ExportUtils.createCell(row, (short) 1, headerStyle, INSTRUMENT);
			ExportUtils.createCell(row, (short) 2, headerStyle, DESCRIPTION);
			for (ExperimentConfigBean config : configList) {
				row = sheet.createRow(rowIndex++);
				ExportUtils.createCell(row, (short) 0, config.getTechniqueDisplayName());
				
				String[] names = config.getInstrumentDisplayNames();
				if (names != null && names.length > 0) {
					sb.setLength(0);
					for (String name : names) {
						sb.append(name).append(' ');
					}
					sb.deleteCharAt(sb.length() - 1);
					ExportUtils.createCell(row, (short) 1, sb.toString());
				}
				
				if (!StringUtils.isEmpty(config.getDomain().getDescription())) {
					ExportUtils.createCell(row, (short) 2, config.getDomain().getDescription());
				}
			}
		}
		return rowIndex;
	}	

	/**
	 * Output Characterization Results for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 * @throws IOException 
	 */
	private short outputCharResults(CharacterizationBean charBean,
			HttpServletRequest request, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFCellStyle headerStyle, short rowIndex) throws IOException {
		//9. Output Characterization Results at (8, 0).
		List<FindingBean> findings = charBean.getFindings();
		if (findings != null && !findings.isEmpty()) {
			int count = 1;
			for (FindingBean findingBean : findings) {
				rowIndex++; // Create one empty line as separator.
				HSSFRow row = sheet.createRow(rowIndex++);
				ExportUtils.createCell(row, (short) 0, headerStyle, CHAR_RESULT + count);
				
				//9a. Output Characterization File Results.
				this.outputFileResult(findingBean, request, wb, sheet, headerStyle, rowIndex);
				
				//9b. Output Characterization Datum Results.
				this.outputDatumResult(findingBean, sheet, headerStyle, rowIndex);
			}
		}
		return rowIndex;
	}	

	/**
	 * Output Files in Characterization Results for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param request HttpServletRequest
	 * @param wb HSSFWorkbook
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 * @throws IOException 
	 */
	private short outputFileResult(FindingBean findingBean,
			HttpServletRequest request, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFCellStyle headerStyle, short rowIndex) throws IOException {
		// Get list of FileBeans from findingBean.
		List<FileBean> files = findingBean.getFiles();
		if (files != null && !files.isEmpty()) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, FILES);
			for (FileBean fileBean : files) {
				row = sheet.createRow(rowIndex++);
				if (fileBean.getDomainFile().getUriExternal().booleanValue()) {
					ExportUtils.createCell(row, (short) 0, fileBean.getDomainFile().getUri());
					/**
					sb.append(request.getRequestURL().toString());
					sb.append("?dispatch=download&fileId=");
					sb.append(fileBean.getDomainFile().getId());
					sb.append("&location=").append(request.getParameter("location"));
					rowIndex = this.outputPicture(rowIndex, sb.toString(), wb, sheet);
					*/
				} else if (fileBean.isHidden()) {
					ExportUtils.createCell(row, (short) 0, PRIVATE_FILE);
				} else {
					ExportUtils.createCell(row, (short) 0, fileBean.getDomainFile().getTitle());
					
					StringBuilder sb = new StringBuilder();
					if (fileBean.isImage()) {
						sb.append(fileRoot).append(java.io.File.separator);
						sb.append(fileBean.getDomainFile().getUri());
						String filePath = sb.toString();
						java.io.File imgFile = new java.io.File(filePath);
						if (imgFile.exists()) {
							rowIndex = this.outputPicture(rowIndex, filePath, wb, sheet);
						} else {
							logger.error("File not exists: " + filePath);
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
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputPicture(short rowIndex, String filePath,
			HSSFWorkbook wb, HSSFSheet sheet) throws IOException {
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		try {
			short topLeftCell = 0;
			short bottomRightCell = (short) 7;
			int topLeftRow = rowIndex + 1;
			int bottomRightRow = rowIndex + 22;
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 255,
					topLeftCell, topLeftRow, bottomRightCell, bottomRightRow);
			anchor.setAnchorType(2);
			patriarch.createPicture(anchor, ExportUtils.loadPicture(filePath, wb));
			rowIndex = (short) (bottomRightRow + 3);
		} catch (IOException ioe) {
			logger.error("Input/output problem to export detail view image data ", ioe);
			throw ioe;
		}
		return rowIndex;
	}
	
	/**
	 * Output Datums in Characterization Results for work sheet.
	 *
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputDatumResult(FindingBean findingBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		
		// Get list of Rows from findingBean.
		List<Row> rows = findingBean.getRows();
		if (rows != null && !rows.isEmpty()) {
			// Output general header "Data and Conditions".
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, DATA_CONDITIONS);
			
			// Output header of each column.
			short cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			for (ColumnHeader column : findingBean.getColumnHeaders()) {
				ExportUtils.createCell(row, cellIndex++, headerStyle, column.getDisplayName());
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
	 * @param charBean CharacterizationBean
	 * @param sheet HSSFSheet
	 * @param headerStyle HSSFCellStyle
	 * @param rowIndex rowIndex
	 */
	private short outputConclusion(CharacterizationBean charBean, HSSFSheet sheet, HSSFCellStyle headerStyle, short rowIndex) {
		//9. Output Analysis and Conclusion at last.
		if (!StringUtils.isEmpty(charBean.getConclusion())) {
			HSSFRow row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, (short) 0, headerStyle, ANALYSIS_CONCLUSION);
			ExportUtils.createCell(row, (short) 1, charBean.getConclusion());
		}
		return rowIndex;
	}	
}