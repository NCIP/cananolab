package gov.nih.nci.cananolab.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportUtils {

	// Partial URL for downloading an image file.
	public static final String DOWNLOAD_URL = "?dispatch=download";

	/**
	 * Constant for setting response header.
	 */
	public static final String IMAGE_CONTENT_TYPE = "application/octet-stream";
	public static final String EXCEL_CONTENT_TYPE = "application/vnd.ms-execel";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String PRIVATE_CACHE = "private";
	public static final String CACHE="cache";
	public static final String CONTENT_DISPOSITION = "Content-disposition";
	public static final String ATTACHMENT = "attachment;filename=\"";
	public static final String EXCEL_EXTENTION = ".xls\"";

	/**
	 * Get file name for exporting report as an Excel file.
	 *
	 * @param sampleName
	 * @param viewType
	 * @param subType
	 * @return
	 */
	public static String getExportFileName(String sampleName, String viewType,
			String subType) {
		List<String> nameParts = new ArrayList<String>(4);
		nameParts.add(sampleName);
		nameParts.add(viewType);
		if (!StringUtils.isEmpty(subType)) {
			nameParts.add(StringUtils.getOneWordUpperCaseFirstLetter(subType));
		}
		nameParts.add(DateUtils.convertDateToString(Calendar.getInstance()
				.getTime()));
		String exportFileName = StringUtils.join(nameParts, "_");

		return exportFileName;
	}

	/**
	 * Set content type and header in response for exporting Excel.
	 *
	 * @param response HttpServletResponse
	 * @param fileName String
	 */
	public static void prepareReponseForExcel(HttpServletResponse response, String fileName) {
		prepareDownloadResponse(response);
		StringBuilder sb = new StringBuilder();
		sb.append(ATTACHMENT).append(fileName).append(EXCEL_EXTENTION);
		response.setContentType(EXCEL_CONTENT_TYPE);
		response.setHeader(CONTENT_DISPOSITION, sb.toString());
	}

	/**
	 * Set content type and header in response for image file download.
	 *
	 * @param response HttpServletResponse
	 * @param fileName String
	 */
	public static void prepareReponseForImage(HttpServletResponse response, String fileName) {
		prepareDownloadResponse(response);
		StringBuilder sb = new StringBuilder();
		sb.append(ATTACHMENT).append(fileName).append("\"");
		response.setContentType(IMAGE_CONTENT_TYPE);
		response.setHeader(CONTENT_DISPOSITION, sb.toString());
	}

	private static void prepareDownloadResponse(HttpServletResponse response) {
		response.setHeader("Pragma", "");
		response.setHeader(CACHE_CONTROL, PRIVATE_CACHE);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE,5);
		response.setDateHeader("Expires", cal.getTimeInMillis());
	}

	public static int loadPicture(String path, HSSFWorkbook wb) throws IOException {
		int pictureIndex;
		InputStream fis = null;
		ByteArrayOutputStream bos = null;
		if (path.startsWith("http:")){
			URL url = new URL(path);
			fis = url.openStream();
		}else{
			fis = new BufferedInputStream(new FileInputStream(path));
		}
		try {
			int c;
			bos = new ByteArrayOutputStream();
			while ((c = fis.read()) != -1) {
				bos.write(c);
			}
			pictureIndex =
				wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
		} finally {
			if (fis != null)
				fis.close();
			if (bos != null)
				bos.close();
		}
		return pictureIndex;
	}

	/**
	 * Output Datums in Characterization Results for work sheet.
	 *
	 * @param rowIndex
	 * @param filePath
	 * @param wb
	 * @param sheet
	 */
	public static int createImage(int rowIndex, short colIndex,
			String filePath, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFPatriarch patriarch) throws IOException {
		short topLeftCell = colIndex;
		short bottomRightCell = (short) (colIndex + 7);
		int topLeftRow = rowIndex + 1;
		int bottomRightRow = rowIndex + 22;
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 255,
				topLeftCell, topLeftRow, bottomRightCell, bottomRightRow);
		anchor.setAnchorType(2); // 2 = Move but don't size with cells
		patriarch.createPicture(anchor, loadPicture(filePath, wb));
		rowIndex = bottomRightRow + 3;

		return rowIndex;
	}

	/**
	 * Create a HSSFCell in row with index and value.
	 *
	 * @param row HSSFRow
	 * @param index int
	 * @param value String
	 * @return HSSFCell
	 */
	public static HSSFCell createCell(HSSFRow row, int index, String value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(new HSSFRichTextString(value));
		return cell;
	}

	/**
	 * Create a HSSFCell in row with cellStyle, index and value.
	 *
	 * @param row HSSFRow
	 * @param index int
	 * @param cellStyle HSSFCellStyle
	 * @param value String
	 * @return HSSFCell
	 */
	public static HSSFCell createCell(HSSFRow row, int index, HSSFCellStyle cellStyle, String value) {
		HSSFCell cell = createCell(row, index, value);
		cell.setCellStyle(cellStyle);
		return cell;
	}

	/**
	 * Create a HSSFCell in row with hyperlink, index and value.
	 *
	 * @param row HSSFRow
	 * @param index int
	 * @param value String
	 * @param url String
	 * @return HSSFCell
	 */
	public static HSSFCell createCell(HSSFRow row, int index,
			HSSFCellStyle cellStyle, String value, String url) {
		HSSFCell cell = createCell(row, index, value);

	    HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
	    link.setAddress(url);
	    cell.setHyperlink(link);

		cell.setCellStyle(cellStyle);

		return cell;
	}
}
