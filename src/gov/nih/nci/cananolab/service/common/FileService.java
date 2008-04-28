package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.PropertyReader;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Utility service for file retrieving and writing.
 * 
 * @author pansu
 * 
 */
public class FileService {
	Logger logger = Logger.getLogger(FileService.class);

	public FileService() {
	}

	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public LabFileBean findFile(String fileId) throws FileException {
		LabFileBean fileBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(LabFile.class)
					.add(Property.forName("id").eq(new Long(fileId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				fileBean = new LabFileBean((LabFile) result.get(0));
			}
			return fileBean;
		} catch (Exception e) {
			logger.error("Problem finding the file by id: " + fileId, e);
			throw new FileException();
		}
	}

	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public LabFileBean findFile(String fileId, UserBean user)
			throws FileException, CaNanoLabSecurityException {
		LabFileBean fileBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(LabFile.class)
					.add(Property.forName("id").eq(new Long(fileId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				fileBean = new LabFileBean((LabFile) result.get(0));
			}
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (auth.isUserAllowed(fileBean.getDomainFile().getId().toString(),
					user)) {
				return fileBean;
			} else {
				throw new NoAccessException();
			}
		} catch (Exception e) {
			logger.error("Problem finding the file by id: " + fileId, e);
			throw new FileException();
		}
	}

	/**
	 * Write content of the file to the given output stream
	 * 
	 * @param fileId
	 * @param out
	 * @throws FileException
	 */
	public void writeFileContent(Long fileId, OutputStream out)
			throws FileException {

		try {
			LabFileBean fileBean = findFile(fileId.toString());
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");

			File fileObj = new File(fileRoot + File.separator
					+ fileBean.getDomainFile().getUri());
			InputStream in = new FileInputStream(fileObj);
			byte[] bytes = new byte[32768];
			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} catch (HibernateException e) {
			String err = "Error getting file meta data from the database.";
			this.logger.error(err, e);
			throw new FileException(err, e);
		} catch (IOException e) {
			String err = "Error getting file content from the file system and writing to the output stream.";
			this.logger.error(err, e);
			throw new FileException(err, e);
		}
	}

	/**
	 * Get the content of the file into a byte array.
	 * 
	 * @param fileId
	 * @return
	 * @throws FileException
	 */
	public byte[] getFileContent(Long fileId) throws FileException {
		try {
			LabFileBean fileBean = findFile(fileId.toString());
			if (fileBean == null || fileBean.getDomainFile().getUri() == null) {
				return null;
			}
			// check if the file is external link
			if (fileBean.getDomainFile().getUri().startsWith("http")) {
				return null;
			}
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");

			File fileObj = new File(fileRoot + File.separator
					+ fileBean.getDomainFile().getUri());
			long fileLength = fileObj.length();

			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
			if (fileLength > Integer.MAX_VALUE) {
				logger
						.error("The file is too big. Byte array can't be longer than Java Integer MAX_VALUE");
				throw new FileException(
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
				throw new FileException("Could not completely read file "
						+ fileObj.getName());
			}

			// Close the input stream and return bytes
			is.close();

			return fileData;
		} catch (IOException e) {
			String err = "Error getting file content from the file system and writing to the output stream.";
			this.logger.error(err, e);
			throw new FileException(err, e);
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
		File file = new File(fullFileName);
		if (file.exists()) {
			return; // don't save again
		}
		FileOutputStream oStream = new FileOutputStream(new File(fullFileName));
		oStream.write(fileContent);
	}

	private void writeFile(InputStream is, FileOutputStream os)
			throws IOException {
		byte[] bytes = new byte[32768];

		int numRead = 0;
		while ((numRead = is.read(bytes)) > 0) {
			os.write(bytes, 0, numRead);
		}
		os.close();
	}

	public static String prefixFileNameWithTimeStamp(String fileName) {
		String newFileName = StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS")
				+ "_" + fileName;
		return newFileName;
	}

	/**
	 * save the meta data associated with a file stored in the database
	 * 
	 * @param file
	 * @throws FileException
	 */
	public void saveFile(LabFile file) throws FileException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.saveOrUpdate(file);
		} catch (Exception e) {
			logger.error("Problem saving file: ", e);
			throw new FileException();
		}
	}

	// set file visibility
	public void setVisiblity(LabFileBean fileBean, UserBean user)
			throws FileException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (fileBean.getDomainFile().getId() != null
					&& auth.isUserAllowed(fileBean.getDomainFile().getId()
							.toString(), user)) {
				fileBean.setHidden(false);
				// get assigned visible groups
				List<String> accessibleGroups = auth.getAccessibleGroups(
						fileBean.getDomainFile().getId().toString(),
						CaNanoLabConstants.CSM_READ_ROLE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				fileBean.setVisibilityGroups(visibilityGroups);
			} else {
				fileBean.setHidden(true);
			}
		} catch (Exception e) {
			String err = "Error in setting file visibility for "
					+ fileBean.getDisplayName();
			logger.error(err, e);
			throw new FileException(err, e);
		}
	}
}
