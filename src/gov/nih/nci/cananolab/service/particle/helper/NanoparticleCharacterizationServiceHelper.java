package gov.nih.nci.cananolab.service.particle.helper;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.SurfaceChemistry;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryRowBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedDatumBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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

public class NanoparticleCharacterizationServiceHelper {
	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationServiceHelper.class);

	public NanoparticleCharacterizationServiceHelper() {
	}

	public Characterization findCharacterizationById(String charId)
			throws Exception {
		Characterization achar = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				Characterization.class).add(
				Property.forName("id").eq(new Long(charId)));
		crit.createAlias("derivedBioAssayDataCollection", "bioassay",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("bioassay.file", "file",
				CriteriaSpecification.LEFT_JOIN);
		crit.setFetchMode("protocolFile", FetchMode.JOIN);
		crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			achar = (Characterization) result.get(0);
		}
		return achar;
	}

	// for dwr ajax
	public String getTechniqueAbbreviation(String techniqueType) {
		String techniqueAbbreviation = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select distinct technique.abbreviation from gov.nih.nci.cananolab.domain.common.Technique technique where technique.type='"
							+ techniqueType
							+ "' and technique.abbreviation!=null");
			List results = appService.query(crit);
			for (Object obj : results) {
				techniqueAbbreviation = (String) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve technique abbreviation.";
			logger.error(err, e);
			return "";
		}
		return techniqueAbbreviation;
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

	public List<Characterization> findParticleCharacterizationsByClass(
			String particleName, String className) throws Exception {
		List<Characterization> charas = new ArrayList<Characterization>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Class
				.forName(className));
		crit.createAlias("nanoparticleSample", "sample",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("derivedBioAssayDataCollection", "bioassay",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("bioassay.file", "file",
				CriteriaSpecification.LEFT_JOIN);
		crit.add(Restrictions.eq("sample.name", particleName));
		crit.setFetchMode("protocolFile", FetchMode.JOIN);
		crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		for (Object obj : result) {
			charas.add((Characterization) obj);
		}
		return charas;
	}

	public void exportDetail(CharacterizationBean achar, OutputStream out)
			throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("detailSheet");
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		short startRow = 0;
		setDetailSheet(achar, wb, sheet, patriarch, startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	private short setDetailSheet(CharacterizationBean achar, HSSFWorkbook wb,
			HSSFSheet sheet, HSSFPatriarch patriarch, short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFRow row = null;
		HSSFCell cell = null;
		// description row
		String description = achar.getDescription();
		if (description != null) {
			row = sheet.createRow(rowCount++);
			short cellCount = 0;
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Description"));

			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(description));
		}

		// protocol row
		ProtocolFileBean protocolFileBean = achar.getProtocolFileBean();
		String protocolId = null;
		if (protocolFileBean.getDomainFile() != null
				&& protocolFileBean.getDomainFile().getId() != null) {
			protocolId = protocolFileBean.getDomainFile().getId().toString();
		}
		if (protocolId != null) {
			row = sheet.createRow(rowCount++);
			short cellCount = 0;
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Protocol"));

			if (protocolFileBean.isHidden()) {
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString("Private protocol"));
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append(protocolFileBean.getDisplayName());
				if (protocolFileBean.getDomainFile().getUri() != null) {
					buf.append(" ");
					buf.append(protocolFileBean.getDomainFile().getUri());
				}
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(buf.toString()));
			}
		}

		//TODO instrument and technique in exported file
		// instrument
//		InstrumentConfiguration instrumentConfiguration = achar
//				.getInstrumentConfiguration();
//		Instrument instrument = instrumentConfiguration.getInstrument();
//		if (instrumentConfiguration != null && instrument.getType() != null) {
//			short cellCount = 0;
//			row = sheet.createRow(rowCount++);
//			cell = row.createCell(cellCount++);
//			cell.setCellStyle(headerStyle);
//			cell.setCellValue(new HSSFRichTextString("Instrument"));
//
//			StringBuffer ibuf = new StringBuffer();
//			ibuf.append(instrument.getType());
//			ibuf.append("-");
//			ibuf.append(instrument.getManufacturer());
//			if (instrument.getAbbreviation() != null
//					&& instrument.getAbbreviation().length() > 0) {
//				ibuf.append(" (" + instrument.getAbbreviation() + ")");
//			}
//			row.createCell(cellCount++).setCellValue(
//					new HSSFRichTextString(ibuf.toString()));
//
//			if (instrumentConfiguration.getDescription() != null) {
//				row.createCell(cellCount).setCellValue(
//						new HSSFRichTextString(instrumentConfiguration
//								.getDescription()));
//			}
//		}

		List<DerivedBioAssayDataBean> derivedBioAssayDataList = achar
				.getDerivedBioAssayDataList();
		int fileIndex = 1;

		for (DerivedBioAssayDataBean derivedBioAssayData : derivedBioAssayDataList) {
			short cellCount = 0;
			if (derivedBioAssayData.getFileBean() != null) {
				String dDescription = derivedBioAssayData.getFileBean()
						.getDomainFile().getDescription();
				if (dDescription != null) {
					row = sheet.createRow(rowCount++);
					cell = row.createCell(cellCount++);
					cell.setCellStyle(headerStyle);
					cell.setCellValue(new HSSFRichTextString(
							"Characterization File #" + fileIndex
									+ " Description"));
					row.createCell(cellCount++).setCellValue(
							new HSSFRichTextString(dDescription));

				}
			}
			if (derivedBioAssayData != null
					&& derivedBioAssayData.getFileBean() != null
					&& derivedBioAssayData.getFileBean().getDomainFile()
							.getUri() != null) {
				row = sheet.createRow(rowCount++);
				cellCount = 0;
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(
						"Characterization File #" + fileIndex));
				/*
				 * if(derivedBioAssayData.getType() != null) {
				 * row.createCell(cellCount++).setCellValue(new
				 * HSSFRichTextString(derivedBioAssayData.getType())); }
				 */
				if (derivedBioAssayData.getFileBean().isHidden()) {
					row.createCell(cellCount++).setCellValue(
							new HSSFRichTextString("Private file"));
				} else {
					if (derivedBioAssayData.getFileBean().isImage()) {
						row.createCell(cellCount).setCellValue(
								new HSSFRichTextString(derivedBioAssayData
										.getFileBean().getDomainFile()
										.getTitle()));
						try {
							String filePath = derivedBioAssayData
									.getFileBean().getFullPath();
							HSSFClientAnchor anchor;
							short topLeftCell = cellCount;
							short bottomRightCell = (short) (cellCount + 7);
							int topLeftRow = rowCount + 1;
							int bottomRightRow = rowCount + 22;
							anchor = new HSSFClientAnchor(0, 0, 0, 255,
									topLeftCell, topLeftRow, bottomRightCell,
									bottomRightRow);
							anchor.setAnchorType(2);
							patriarch.createPicture(anchor, ExportUtils
									.loadPicture(filePath, wb));
							cellCount = bottomRightCell;
							rowCount = (short) (bottomRightRow + 3);
						} catch (IOException ioe) {
							logger
									.error(
											"Input/output problem to export detail view image data ",
											ioe);
						}

					} else { // question? download the whole file to a cell
						// if not image?
						row.createCell(cellCount++).setCellValue(
								new HSSFRichTextString(derivedBioAssayData
										.getFileBean().getDomainFile()
										.getTitle()));
					}

				}
			}

			List<DerivedDatumBean> datumList = derivedBioAssayData
					.getDatumList();
			if (datumList != null && !datumList.isEmpty()) {
				cellCount = 0;
				row = sheet.createRow(rowCount);
				rowCount++;
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(
						"Characterization Derived Data #" + fileIndex));

				row = sheet.createRow(rowCount++);
				row = sheet.createRow(rowCount++);

				short ccount = 0;
				for (DerivedDatumBean datum : datumList) {
					if (datum.getDomainDerivedDatum().getValueUnit() != null
							&& datum.getDomainDerivedDatum().getValueUnit()
									.trim().length() > 0) {
						cell = row.createCell(ccount++);
						cell.setCellStyle(headerStyle);
						cell.setCellValue(new HSSFRichTextString(datum
								.getDomainDerivedDatum().getName()
								+ " ("
								+ datum.getDomainDerivedDatum().getValueUnit()
								+ ")"));
					} else {
						cell = row.createCell(ccount++);
						cell.setCellStyle(headerStyle);
						cell.setCellValue(new HSSFRichTextString(datum
								.getDomainDerivedDatum().getName()));
					}
				}

				row = sheet.createRow(rowCount++);
				ccount = 0;
				for (DerivedDatumBean datum : datumList) {
					row.createCell(ccount++).setCellValue(
							new HSSFRichTextString(datum.getValueStr()));
				}
				rowCount++;
			}
			fileIndex++;
		}
		return rowCount;
	}

	public void exportFullSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		short rowCount = setSummarySheet(summaryBean, wb, sheet, startRow);
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		rowCount += 2;
		for (CharacterizationBean cbean : summaryBean.getCharBeans()) {
			rowCount = setDetailSheet(cbean, wb, sheet, patriarch, rowCount);
			rowCount += 2;
		}
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	public void exportSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		setSummarySheet(summaryBean, wb, sheet, startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	private short setSummarySheet(CharacterizationSummaryBean summaryBean,
			HSSFWorkbook wb, HSSFSheet sheet, short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFCellStyle newLineStyle = wb.createCellStyle();
		// Word Wrap MUST be turned on
		newLineStyle.setWrapText(true);

		short cellCount = 0;
		HSSFRow row = null;
		HSSFCell cell = null;

		// summary header
		row = sheet.createRow(rowCount);
		rowCount++;

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("View Title"));

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Description"));

		for (String label : summaryBean.getColumnLabels()) {
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString(label));
		}
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Characterization File"));

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Instrument Info"));

		// data
		for (CharacterizationSummaryRowBean sbean : summaryBean
				.getSummaryRows()) {
			row = sheet.createRow(rowCount);
			rowCount++;
			cellCount = 0;

			// view title
			CharacterizationBean achar = (CharacterizationBean) sbean
					.getCharBean();
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(achar.getViewTitle()));

			// description
			String description = achar.getDescription();
			if (description == null)
				description = "";
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(description));

			Map datumMap = sbean.getDatumMap();
			for (String label : summaryBean.getColumnLabels()) {
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString((String) datumMap.get(label)));
			}

			DerivedBioAssayDataBean charFile = sbean
					.getDerivedBioAssayDataBean();
			if (charFile != null) {
				StringBuffer sbuf = new StringBuffer();
				if (charFile.getFileBean() != null
						&& charFile.getFileBean().getDomainFile().getType() != null
						&& charFile.getFileBean().getDomainFile().getType()
								.length() > 0) {
					sbuf.append(charFile.getFileBean().getDomainFile()
							.getType()
							+ " ");
				}
				if (charFile.getFileBean() != null
						&& charFile.getFileBean().getDomainFile().getUri() != null) {
					if (charFile.getFileBean().isHidden()) {
						sbuf.append("Private file");
					} else if (charFile.getFileBean().getDomainFile()
							.getTitle() != null) {
						sbuf.append(charFile.getFileBean().getDomainFile()
								.getTitle());
					}
				}
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(sbuf.toString()));
			} else {
				row.createCell(cellCount++); // empty cell
			}

			//TODO instrument and technique in summary view
			// instrument
//			InstrumentConfiguration instrumentConfiguration = achar
//					.getInstrumentConfiguration();
//			Instrument instrument = instrumentConfiguration.getInstrument();
//			if (instrumentConfiguration != null && instrument.getType() != null) {
//				StringBuffer sb = new StringBuffer();
//				sb.append(instrument.getType());
//				sb.append("-");
//				sb.append(instrument.getManufacturer());
//
//				if (instrument.getAbbreviation() != null
//						&& instrument.getAbbreviation().length() > 0)
//					sb.append(" (" + instrument.getAbbreviation() + ")");
//
//				if (instrumentConfiguration.getDescription() != null)
//					sb.append("  " + instrumentConfiguration.getDescription());
//
//				cell = row.createCell(cellCount++);
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(new HSSFRichTextString(sb.toString()));
//				// cell.setCellStyle(newLineStyle);
//			}

		} // for sbean
		return rowCount;
	}

	public ProtocolFile findProtocolFileByCharacterizationId(
			java.lang.String characterizationId) throws Exception {
		ProtocolFile protocolFile = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select aChar.protocolFile from gov.nih.nci.cananolab.domain.particle.characterization.Characterization aChar where aChar.id="
				+ characterizationId;
		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		for (Object obj : results) {
			protocolFile = (ProtocolFile) obj;
		}
		return protocolFile;
	}

	public List<DerivedBioAssayData> findDerivedBioAssayDataByCharacterizationId(
			java.lang.String characterizationId) throws Exception {
		List<DerivedBioAssayData> derivedBioAssayDataCollection = new ArrayList<DerivedBioAssayData>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.derivedBioAssayDataCollection from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar where achar.id = "
						+ characterizationId);
		List results = appService.query(crit);
		FileServiceHelper fileHelper = new FileServiceHelper();
		for (Object obj : results) {
			DerivedBioAssayData derivedBioAssayData = (DerivedBioAssayData) obj;
			// derivedBioAssayData's File
			File File = findFileByDerivedBioAssayDataId(derivedBioAssayData
					.getId().toString());

			// File's keyword
			if (File != null) {
				List<Keyword> keywordCollection = fileHelper
						.findKeywordsByFileId(File.getId().toString());
				File.setKeywordCollection(keywordCollection);
				derivedBioAssayData.setFile(File);
			}
			derivedBioAssayDataCollection.add(derivedBioAssayData);
		}
		return derivedBioAssayDataCollection;
	}

	//TODO update for grid service
//	public Instrument findInstrumentByInstrumentConfigurationId(
//			java.lang.String instrumentConfigurationId) throws Exception {
//		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//				.getApplicationService();
//		String hql = "select config.instrument from gov.nih.nci.cananolab.domain.common.InstrumentConfiguration config where config.id="
//				+ instrumentConfigurationId;
//		HQLCriteria crit = new HQLCriteria(hql);
//		List results = appService.query(crit);
//		Instrument instrument = null;
//		for (Object obj : results) {
//			instrument = (Instrument) obj;
//		}
//		return instrument;
//	}


//	public InstrumentConfiguration findInstrumentConfigurationByCharacterizationId(
//			java.lang.String characterizationId) throws Exception {
//		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//				.getApplicationService();
//		String hql = "select aChar.instrumentConfiguration from gov.nih.nci.cananolab.domain.particle.characterization.Characterization aChar where aChar.id="
//				+ characterizationId;
//		HQLCriteria crit = new HQLCriteria(hql);
//		List results = appService.query(crit);
//		InstrumentConfiguration instrumentConfiguration = null;
//		for (Object obj : results) {
//			instrumentConfiguration = (InstrumentConfiguration) obj;
//		}
//		return instrumentConfiguration;
//	}

	public List<SurfaceChemistry> findSurfaceChemistriesBySurfaceId(
			java.lang.String surfaceId) throws Exception {
		List<SurfaceChemistry> chemistries = new ArrayList<SurfaceChemistry>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select surface.surfaceChemistryCollection from gov.nih.nci.cananolab.domain.particle.characterization.physical.Surface surface where surface.id="
				+ surfaceId;
		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		for (Object obj : results) {
			SurfaceChemistry surfaceChemistry = (SurfaceChemistry) obj;
			chemistries.add(surfaceChemistry);
		}
		return chemistries;
	}

	public File findFileByDerivedBioAssayDataId(String derivedId)
			throws Exception {
		File File = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select bioassay.file from gov.nih.nci.cananolab.domain.common.DerivedBioAssayData bioassay where bioassay.id = "
						+ derivedId);
		List results = appService.query(crit);
		for (Object obj : results) {
			File = (File) obj;
		}
		return File;
	}

	public List<DerivedDatum> findDerivedDatumListByDerivedBioAssayDataId(
			String derivedBioAssayDataId) throws Exception {
		List<DerivedDatum> datumList = new ArrayList<DerivedDatum>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select bioassay.derivedDatumCollection from gov.nih.nci.cananolab.domain.common.DerivedBioAssayData bioassay where bioassay.id = "
						+ derivedBioAssayDataId);
		List results = appService.query(crit);
		for (Object obj : results) {
			datumList.add((DerivedDatum) obj);
		}
		return datumList;
	}
}