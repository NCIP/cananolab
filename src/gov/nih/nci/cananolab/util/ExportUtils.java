package gov.nih.nci.cananolab.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;

public class ExportUtils {
	
	/**
	 * Constant for setting response header. 
	 */
	public static final String IMAGE_CONTENT_TYPE = "application/octet-stream";
	public static final String EXCEL_CONTENT_TYPE = "application/vnd.ms-execel";
	public static final String CACHE_CONTROL = "cache-control";
	public static final String PRIVATE = "Private";
	public static final String CONTENT_DISPOSITION = "Content-disposition";
	public static final String ATTACHMENT = "attachment;filename=\"";
	public static final String EXCELL_EXTENTION = ".xls\"";
	
	/**
	 * Set content type and header in response for exporting.
	 *
	 * @param response HttpServletResponse
	 * @param fileName String
	 */
	public static void prepareReponseForExcell(HttpServletResponse response, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ATTACHMENT).append(fileName).append(EXCELL_EXTENTION);
		
		response.setContentType(EXCEL_CONTENT_TYPE);
		response.setHeader(CACHE_CONTROL, PRIVATE);
		response.setHeader(CONTENT_DISPOSITION, sb.toString());
	}
	
	public static int loadPicture(String path, HSSFWorkbook wb) throws IOException {
		int pictureIndex;
		InputStream fis = null;
		ByteArrayOutputStream bos = null;
		
		if (path.startsWith("http:")){
			URL url = new URL(path);
			fis = url.openStream();  
		}else{
			fis = new FileInputStream(path);
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
	public static int createImage(int rowIndex, String filePath,
			HSSFWorkbook wb, HSSFSheet sheet) throws IOException {
		short topLeftCell = 0;
		short bottomRightCell = 7;
		int topLeftRow = rowIndex + 1;
		int bottomRightRow = rowIndex + 22;
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
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
	public static HSSFCell createCell(HSSFRow row, int index, String value, String url) {
		HSSFCell cell = createCell(row, index, value);
		
	    HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
	    link.setAddress(url);
	    cell.setHyperlink(link);
		
		return cell;
	}
}
