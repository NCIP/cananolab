package gov.nih.nci.cananolab.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportUtils {
	
	/**
	 * Constant for setting response header. 
	 */
	public static final String CONTENT_TYPE = "application/vnd.ms-execel";
	public static final String CACHE_CONTROL = "cache-control";
	public static final String PRIVATE = "Private";
	public static final String CONTENT_DISPOSITION = "Content-disposition";
	public static final String ATTACHMENT = "attachment;filename=\"";
	public static final String EXCELL_EXTENTION = ".xls\"";
	
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
			bos = new ByteArrayOutputStream();
			int c;
			while ((c = fis.read()) != -1)
				bos.write(c);
			pictureIndex = wb.addPicture(bos.toByteArray(),
					HSSFWorkbook.PICTURE_TYPE_PNG);
		} finally {
			if (fis != null)
				fis.close();
			if (bos != null)
				bos.close();
		}
		return pictureIndex;
	}

	/**
	 * Create a HSSFCell in row with specified index and value.
	 *
	 * @param row HSSFRow
	 * @param index short
	 * @param value String
	 */
	public static HSSFCell createCell(HSSFRow row, short index, String value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(new HSSFRichTextString(value));
		return cell;
	}

	/**
	 * Create a HSSFCell in row with specified cellStyle, index and value.
	 *
	 * @param row HSSFRow
	 * @param index short
	 * @param cellStyle HSSFCellStyle
	 * @param value String
	 */
	public static HSSFCell createCell(HSSFRow row, short index, HSSFCellStyle cellStyle, String value) {
		HSSFCell cell = createCell(row, index, value);
		cell.setCellStyle(cellStyle);
		return cell;
	}
	
	/**
	 * Set content type and header in response for exporting.
	 *
	 * @param response HttpServletResponse
	 * @param fileName String
	 */
	public static void prepareReponseForExport(HttpServletResponse response, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ATTACHMENT).append(fileName).append(EXCELL_EXTENTION);
		
		response.setContentType(CONTENT_TYPE);
		response.setHeader(CACHE_CONTROL, PRIVATE);
		response.setHeader(CONTENT_DISPOSITION, sb.toString());
	}
}
