package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.dto.common.LinkableItem;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.util.ExportUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Service methods involving exporting samples
 *
 * @author pansu, houy
 *
 */
public class SampleExporter {
	private static Logger logger = Logger.getLogger(SampleExporter.class);

	/**
	 * Export advance sample summary report as Excel spread sheet.
	 *
	 * @param searchBean
	 * @param out
	 * @throws CompositionException
	 */
	public static void exportSummary(AdvancedSampleSearchBean searchBean,
			List<AdvancedSampleBean> sampleBeans, String viewSampleUrl,
			OutputStream out) throws IOException {
		if (out != null) {
			HSSFWorkbook wb = new HSSFWorkbook();
			outputSummarySheet(searchBean, sampleBeans, viewSampleUrl, wb);
			wb.write(out);
			out.flush();
			out.close();
		}
	}

	/**
	 * Output advance sample summary report, representing,
	 * bodyAdvancedSampleSearchResult.jsp
	 *
	 * @param searchBean
	 * @param wb
	 */
	private static void outputSummarySheet(AdvancedSampleSearchBean searchBean,
			List<AdvancedSampleBean> sampleBeans, String viewSampleUrl,
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

		int rowIndex = 0;
		HSSFSheet sheet = wb.createSheet("Advanced Sample Search Report");

		// 1.Output Search Criteria. comment out as per Sharon.
		// rowIndex = outputCriteria(searchBean, sheet, headerStyle, rowIndex);

		// 2.Output table column headers.
		rowIndex = outputHeader(sampleBeans.get(0), sheet, headerStyle,
				rowIndex);

		// 3.Output each table row.
		for (AdvancedSampleBean sampleBean : sampleBeans) {
			rowIndex = outputRow(sampleBean, viewSampleUrl, sheet, hlinkStyle,
					rowIndex);
		}
	}

	/**
	 * Output Search Criteria for advance sample search work sheet.
	 *
	 * @param compType
	 * @param entityType
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 *
	 *            private static int outputCriteria(AdvancedSampleSearchBean
	 *            searchBean, HSSFSheet sheet, HSSFCellStyle headerStyle, int
	 *            rowIndex) { // 1. Output "Selected Criteria" at (0, 0).
	 *            HSSFRow row = sheet.createRow(rowIndex++);
	 *            ExportUtils.createCell(row, 0, headerStyle,
	 *            "Selected Criteria"); // 2. Output Criteria Display Name at(1,
	 *            0). row = sheet.createRow(rowIndex++);
	 *            ExportUtils.createCell(row, 0, searchBean.getDisplayName());
	 *            rowIndex++; // Create one empty line as separator. return
	 *            rowIndex; }
	 */

	/**
	 * Output headers for work sheet.
	 *
	 * @param compType
	 * @param entityType
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	public static int outputHeader(AdvancedSampleBean sampleBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 1. Output "Sample Name" at first column.
		int columnIndex = 0;
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, columnIndex++, headerStyle, "Sample Name");

		// 2. Output selected column(s).
		Map<String, List<LinkableItem>> columns = sampleBean.getAttributeMap();
		if (columns != null && !columns.isEmpty()) {
			for (String header : columns.keySet()) {
				ExportUtils.createCell(row, columnIndex++, headerStyle, header
						.replaceAll("<br>", " "));
			}
		}

		// 3. Output "Site" at last column.
		ExportUtils.createCell(row, columnIndex, headerStyle, "Site");

		return rowIndex;
	}

	/**
	 * Output one table row for work sheet.
	 *
	 * @param compType
	 * @param entityType
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	public static int outputRow(AdvancedSampleBean sampleBean,
			String viewSampleUrl, HSSFSheet sheet, HSSFCellStyle hlinkStyle,
			int rowIndex) {
		// 1. Output first column - Sample Name at index 0.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, hlinkStyle, sampleBean.getSampleName(),
				getViewSampleURL(sampleBean, viewSampleUrl));

		// 2. Output selected column(s) starting from index 1.
		int columnIndex = 1;
		Map<String, List<LinkableItem>> columns = sampleBean.getAttributeMap();
		if (columns != null && !columns.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String key : columns.keySet()) {
				sb.setLength(0);
				List<LinkableItem> items = (List<LinkableItem>) columns
						.get(key);
				for (LinkableItem item : items) {
					sb.append(item.getDisplayName()).append(' ');
				}
				ExportUtils.createCell(row, columnIndex++, sb.toString());
			}
		}

		return rowIndex;
	}

	/**
	 * Return complete view sample URL including sample id.
	 *
	 * @param sample
	 * @return
	 */
	private static String getViewSampleURL(AdvancedSampleBean sample,
			String viewSampleUrl) {
		StringBuilder sb = new StringBuilder(viewSampleUrl);
		sb.append("&sampleId=").append(sample.getSampleId());

		return sb.toString();
	}
}
