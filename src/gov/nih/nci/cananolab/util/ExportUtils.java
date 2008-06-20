package gov.nih.nci.cananolab.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

}
