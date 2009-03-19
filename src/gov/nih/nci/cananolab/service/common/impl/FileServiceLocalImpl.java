package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyReader;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Local implementation of FileService
 * 
 * @author pansu
 * 
 */
public class FileServiceLocalImpl implements FileService {
	Logger logger = Logger.getLogger(FileServiceLocalImpl.class);

	FileServiceHelper helper = new FileServiceHelper();

	public FileServiceLocalImpl() {
	}

	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public FileBean findFileById(String fileId) throws FileException {
		FileBean fileBean = null;
		try {
			File file = helper.findFile(fileId);
			if (file != null) {
				fileBean = new FileBean(file);
			}
			return fileBean;
		} catch (Exception e) {
			logger.error("Problem finding the file by id: " + fileId, e);
			throw new FileException();
		}
	}

	/**
	 * Load the file for the given fileId from the database. Also check whether
	 * user can do it.
	 * 
	 * @param fileId
	 * @return
	 */
	public FileBean findFileById(String fileId, UserBean user)
			throws FileException, SecurityException {
		FileBean fileBean = null;
		try {
			fileBean = findFileById(fileId);
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);
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

	public void saveCopiedFileAndSetVisibility(File copy, UserBean user,
			String oldSampleName, String newSampleName) throws FileException {
		try {
			// the copied file has been persisted with the same URI but
			// createdBy is
			// COPY
			File file = findFileByUri(copy.getUri());
			copy.setUri(copy.getUri()
					.replaceFirst(oldSampleName, newSampleName));
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.saveOrUpdate(copy);
			if (file != null) {
				byte[] content = this.getFileContent(file.getId());
				writeFile(copy, content);

				AuthorizationService auth = new AuthorizationService(
						Constants.CSM_APP_NAME);
				FileBean fileBean = new FileBean(file);
				this.retrieveVisibility(fileBean, user);
				auth.assignVisibility(copy.getId().toString(), fileBean
						.getVisibilityGroups(), null);
			}
		} catch (Exception e) {
			String err = "Error in saving copied file to the file system and setting visibility of the copied file.";
			logger.error(err, e);
			throw new FileException(err, e);
		}
	}

	private File findFileByUri(String uri) throws FileException {
		File file = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria
					.forClass(File.class)
					.add(Property.forName("uri").eq(uri))
					.add(
							Property
									.forName("createdBy")
									.ne(
											Constants.AUTO_COPY_ANNOTATION_PREFIX));
			List results = appService.query(crit);
			if (!results.isEmpty()) {
				file = (File) results.get(0);
			}
			return file;
		} catch (Exception e) {
			String err = "Could not find the file by uri";
			logger.error(err, e);
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
	private byte[] getFileContent(Long fileId) throws FileException {
		try {
			FileBean fileBean = findFileById(fileId.toString());
			if (fileBean == null || fileBean.getDomainFile().getUri() == null) {
				return null;
			}
			// check if the file is external link
			if (fileBean.getDomainFile().getUri().startsWith("http")) {
				return null;
			}
			String fileRoot = PropertyReader
					.getProperty(Constants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");

			java.io.File fileObj = new java.io.File(fileRoot + java.io.File.separator
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

	private void writeFile(byte[] fileContent, String fullFileName)
			throws IOException {
		String path = fullFileName.substring(0, fullFileName.lastIndexOf("/"));
		java.io.File pathDir = new java.io.File(path);
		if (!pathDir.exists())
			pathDir.mkdirs();
		java.io.File file = new java.io.File(fullFileName);
		if (file.exists()) {
			return; // don't save again
		}
		FileOutputStream oStream = new FileOutputStream(new java.io.File(fullFileName));
		oStream.write(fileContent);
	}

	// save to the file system fileData is not empty
	public void writeFile(File file, byte[] fileData) throws FileException {
		try {
			if (fileData != null) {
				FileServiceLocalImpl fileService = new FileServiceLocalImpl();
				String rootPath = PropertyReader.getProperty(
						Constants.FILEUPLOAD_PROPERTY,
						"fileRepositoryDir");
				String fullFileName = rootPath + "/" + file.getUri();
				fileService.writeFile(fileData, fullFileName);
			}
		} catch (Exception e) {
			logger.error("Problem writing file " + file.getUri()
					+ " to the file system.");
			throw new FileException();
		}
	}

	/**
	 * Preparing keywords and other information prior to saving a file
	 * 
	 * @param file
	 * @throws FileException
	 */
	public void prepareSaveFile(File file) throws FileException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (file.getId() != null) {
				File dbFile = (File) appService.get(File.class, file
						.getId());
				if (dbFile != null) {
					// don't change createdBy and createdDate if it is already
					// persisted
					file.setCreatedBy(dbFile.getCreatedBy());
					file.setCreatedDate(dbFile.getCreatedDate());
				} else {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new FileException(err);
				}
			}
			if (file.getKeywordCollection() != null) {
				Collection<Keyword> keywords = new HashSet<Keyword>(file
						.getKeywordCollection());
				file.getKeywordCollection().clear();
				for (Keyword keyword : keywords) {
					Keyword dbKeyword = (Keyword) appService.getObject(
							Keyword.class, "name", keyword.getName());
					if (dbKeyword != null) {
						keyword.setId(dbKeyword.getId());
					}
					appService.saveOrUpdate(keyword);
					file.getKeywordCollection().add(keyword);
				}
			}
		} catch (Exception e) {
			logger.error("Problem in preparing saving a file: ", e);
			throw new FileException();
		}
	}

	// retrieve file visibility
	public void retrieveVisibility(FileBean fileBean, UserBean user)
			throws FileException {
		try {
			if (fileBean!=null) {
				AuthorizationService auth = new AuthorizationService(
						Constants.CSM_APP_NAME);
				if (fileBean.getDomainFile().getId() != null
						&& auth.isUserAllowed(fileBean.getDomainFile().getId()
								.toString(), user)) {
					fileBean.setHidden(false);
					// get assigned visible groups
					List<String> accessibleGroups = auth.getAccessibleGroups(
							fileBean.getDomainFile().getId().toString(),
							Constants.CSM_READ_PRIVILEGE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					fileBean.setVisibilityGroups(visibilityGroups);
				} else {
					fileBean.setHidden(true);
				}
			}
		} catch (Exception e) {
			String err = "Error in setting file visibility for "
					+ fileBean.getDisplayName();
			logger.error(err, e);
			throw new FileException(err, e);
		}
	}
	
	// retrieve file accessibility
	public void retrieveAccessibility(FileBean fileBean, UserBean user)
			throws FileException {
		try {
			if (fileBean!=null) {
				AuthorizationService auth = new AuthorizationService(
						Constants.CSM_APP_NAME);
				if (fileBean.getDomainFile().getId() != null
						&& auth.isUserAllowed(fileBean.getDomainFile().getId()
								.toString(), user)) {
					fileBean.setHidden(false);
				} else {
					fileBean.setHidden(true);
				}
			}
		} catch (Exception e) {
			String err = "Error in setting file accessibility for "
					+ fileBean.getDisplayName();
			logger.error(err, e);
			throw new FileException(err, e);
		}
	}

	public List<File> findFilesByCompositionInfoId(String id,
			String className) throws FileException {
		throw new FileException("Not implemented for local service");
	}
}
