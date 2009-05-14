package gov.nih.nci.cananolab.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportUtils {
	
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

	public static HSSFCell createCell(HSSFRow row, short index, String value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(new HSSFRichTextString(value));
		return cell;
	}

	public static HSSFCell createCell(HSSFRow row, short index, HSSFCellStyle cellStyle, String value) {
		HSSFCell cell = createCell(row, index, value);
		cell.setCellStyle(cellStyle);
		return cell;
	}
}
