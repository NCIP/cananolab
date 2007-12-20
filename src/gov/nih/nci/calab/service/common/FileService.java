package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.FileException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;

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
import org.hibernate.Session;

/**
 * Utility service for file retrieving and writing.
 * 
 * @author pansu
 * 
 */
public class FileService {
	Logger logger = Logger.getLogger(FileService.class);

	// remove existing visibilities for the data
	private UserService userService;

	public FileService() throws CaNanoLabSecurityException {
		this.userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public LabFileBean getFile(String fileId) throws FileException,
			CaNanoLabSecurityException {
		LabFileBean fileBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			LabFile file = (LabFile) session.load(LabFile.class, StringUtils
					.convertToLong(fileId));
			fileBean = new LabFileBean(file);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem getting file with file ID: " + fileId, e);
			throw new FileException();
		} finally {
			HibernateUtil.closeSession();
		}
		// get visibilities
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(
				fileBean.getId(), CaNanoLabConstants.CSM_READ_ROLE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		fileBean.setVisibilityGroups(visibilityGroups);
		return fileBean;
	}

	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public LabFileBean getFile(String fileId, UserBean user)
			throws FileException, CaNanoLabSecurityException {
		LabFileBean fileBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			LabFile file = (LabFile) session.load(LabFile.class, StringUtils
					.convertToLong(fileId));
			fileBean = new LabFileBean(file);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem getting file with file ID: " + fileId, e);
			throw new FileException();
		} finally {
			HibernateUtil.closeSession();
		}
		if (fileBean != null) {
			// get visibilities
			UserService userService = new UserService(
					CaNanoLabConstants.CSM_APP_NAME);
			List<String> accessibleGroups = userService.getAccessibleGroups(
					fileBean.getId(), CaNanoLabConstants.CSM_READ_ROLE);
			String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
			fileBean.setVisibilityGroups(visibilityGroups);
			boolean accessStatus = userService.checkReadPermission(user,
					fileBean.getId());
			if (!accessStatus) {
				throw new CaNanoLabSecurityException(
						"You don't have access to the file");
			}
		}
		return fileBean;
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
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			LabFile labFile = (LabFile) session.load(LabFile.class, fileId);
			HibernateUtil.commitTransaction();
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
		} catch (HibernateException e) {
			this.logger.error(
					"Error getting file meta data from the database.", e);
			throw new FileException(
					"error getting file meta data from the database");
		} catch (IOException e) {
			this.logger
					.error(
							"Error getting file content from the file system and writing to the output stream",
							e);
			throw new FileException(
					"Error getting file content from the file system and writing to the output stream");
		} finally {
			HibernateUtil.closeSession();
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
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			LabFile labFile = (LabFile) session.load(LabFile.class, fileId);
			HibernateUtil.commitTransaction();
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");
			if (labFile == null || labFile.getUri() == null) {
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
			this.logger
					.error(
							"Error getting file content from the file system and writing to the output stream",
							e);
			throw new FileException(
					"Error getting file content from the file system and writing to the output stream");
		} finally {
			HibernateUtil.closeSession();
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
	 * Update the meta data associated with a file stored in the database
	 * 
	 * @param fileBean
	 * @throws FileException
	 * @throws CaNanoLabSecurityException
	 */
	public void updateFileMetaData(LabFileBean fileBean) throws FileException,
			CaNanoLabSecurityException {
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			LabFile file = (LabFile) session.load(LabFile.class, StringUtils
					.convertToLong(fileBean.getId()));

			file.setTitle(fileBean.getTitle().toUpperCase());
			file.setDescription(fileBean.getDescription());
			file.setComments(fileBean.getComments());
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Problem updating file meta data: ", e);
			throw new FileException();
		} finally {
			HibernateUtil.closeSession();
		}

		userService.setVisiblity(fileBean.getId(), fileBean
				.getVisibilityGroups());
	}

}
