package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.db.HibernateDataAccess;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.hibernate.ObjectNotFoundException;

/**
 * Utility service for file retrieving and writing.
 * 
 * @author pansu
 * 
 */
public class FileService {
	Logger logger = Logger.getLogger(FileService.class);

	/**
	 * Write content of the file to the given output stream
	 * 
	 * @param fileId
	 * @param out
	 * @throws Exception
	 */
	public void writeFileContent(Long fileId, OutputStream out)
			throws Exception {
		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		try {
			hda.open();
			LabFile labFile = (LabFile) hda.load(LabFile.class, fileId);
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");

			File fileObj = new File(fileRoot + File.separator
					+ labFile.getUri());
			InputStream in = new FileInputStream(fileObj);
			byte[] bytes = new byte[32768];
			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} catch (SQLException e) {
			throw new Exception(
					"error getting file meta data from the database:" + e);
		} catch (IOException e) {
			throw new Exception(
					"error getting file content from the file system and writing to the output stream:"
							+ e);
		} finally {
			hda.close();
		}
	}

	/**
	 * Get the content of the file into a byte array.
	 * 
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public byte[] getFileContent(Long fileId) throws Exception {
		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		try {
			hda.open();
			LabFile labFile = (LabFile) hda.load(LabFile.class, fileId);
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");
			if (labFile==null || labFile.getUri()==null) {
				return null;
			}
			File fileObj = new File(fileRoot + File.separator
					+ labFile.getUri());
			long fileLength = fileObj.length();

			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
			if (fileLength > Integer.MAX_VALUE) {
				throw new Exception(
						"The file is too big. Byte array can't be longer than Java Integer MAX_VALUE");
			}

			// Create the byte array to hold the data
			byte[] fileData = new byte[(int) fileLength];

			// Read in the bytes
			InputStream is = new FileInputStream(fileObj);
			int offset = 0;
			int numRead = 0;
			while (offset < fileData.length
					&& (numRead = is.read(fileData, offset, fileData.length
							- offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < fileData.length) {
				throw new IOException("Could not completely read file "
						+ fileObj.getName());
			}

			// Close the input stream and return bytes
			is.close();
			return fileData;
		} catch (SQLException e) {
			throw new Exception(
					"error getting file meta data from the database:" + e);
		} catch (IOException e) {
			throw new Exception(
					"error getting file content from the file system and writing to the output stream:"
							+ e);
		}
		finally {
			hda.close();
		}
	}

	public String writeUploadedFile(FormFile uploadedFile, String filePath,
			boolean addTimeStampPrefix) throws IOException {
		File pathDir = new File(filePath);
		if (!pathDir.exists())
			pathDir.mkdirs();

		String fileName = uploadedFile.getFileName();
		if (addTimeStampPrefix) {
			fileName = prefixFileNameWithTimeStamp(fileName);
		}
		String fullFileName = filePath + File.separator + fileName;
		FileOutputStream oStream = new FileOutputStream(new File(fullFileName));
		writeFile(uploadedFile.getInputStream(), oStream);
		return fileName;
	}

	public void writeFile(byte[] fileContent, String fullFileName)
			throws IOException {
		String path = fullFileName.substring(0, fullFileName
				.lastIndexOf(File.separator));
		File pathDir = new File(path);
		if (!pathDir.exists())
			pathDir.mkdirs();
		FileOutputStream oStream = new FileOutputStream(new File(fullFileName));
		oStream.write(fileContent);
	}

	public void writeFile(InputStream is, FileOutputStream os)
			throws IOException {
		byte[] bytes = new byte[32768];

		int numRead = 0;
		while ((numRead = is.read(bytes)) > 0) {
			os.write(bytes, 0, numRead);
		}
		os.close();
	}

	public static String prefixFileNameWithTimeStamp(String fileName) {
		String newFileName = StringUtils.convertDateToString(new Date(), "yyyyMMdd_HH-mm-ss-SSS")
				+ "_" + fileName;
		return newFileName;
	}

}
