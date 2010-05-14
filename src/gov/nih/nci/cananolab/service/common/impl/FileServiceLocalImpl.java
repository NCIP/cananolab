package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Local implementation of FileService
 *
 * @author pansu
 *
 */
public class FileServiceLocalImpl implements FileService {
	private static Logger logger = Logger.getLogger(FileServiceLocalImpl.class);

	private FileServiceHelper helper = new FileServiceHelper();

	public FileServiceLocalImpl() {
	}

	/**
	 * Load the file for the given fileId from the database
	 *
	 * @param fileId
	 * @return
	 */
	public FileBean findFileById(String fileId, UserBean user)
			throws FileException, NoAccessException {
		FileBean fileBean = null;
		try {
			File file = helper.findFileById(fileId, user);
			if (file != null) {
				fileBean = new FileBean(file);
				if (user != null) {
					helper.retrieveVisibility(fileBean);
				}
				return fileBean;
			}
			return fileBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Problem finding the file by id: " + fileId, e);
			throw new FileException();
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
		OutputStream oStream = null;
		try {
			oStream = new BufferedOutputStream(new FileOutputStream(file));
			oStream.write(fileContent);
			oStream.flush();
		} finally {
			if (oStream != null) {
				try {
					oStream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// save to the file system if fileData is not empty
	public void writeFile(FileBean fileBean, UserBean user)
			throws FileException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			if (fileBean.getNewFileData() != null) {
				String rootPath = PropertyUtils.getProperty(
						Constants.CANANOLAB_PROPERTY, "fileRepositoryDir");
				String fullFileName = rootPath + "/"
						+ fileBean.getDomainFile().getUri();
				writeFile(fileBean.getNewFileData(), fullFileName);
			}
			assignVisibility(fileBean.getDomainFile(), fileBean
					.getVisibilityGroups());
		} catch (Exception e) {
			logger.error("Problem writing file "
					+ fileBean.getDomainFile().getUri()
					+ " to the file system.");
			throw new FileException();
		}
	}

	private void assignVisibility(File file, String[] visibilityGroups)
			throws FileException {
		try {
			helper.getAuthService().assignVisibility(file.getId().toString(),
					visibilityGroups, null);
			// assign keyword to public visibility
			if (file.getKeywordCollection() != null) {
				for (Keyword keyword : file.getKeywordCollection()) {
					helper.getAuthService().assignVisibility(
							keyword.getId().toString(),
							new String[] { Constants.CSM_PUBLIC_GROUP }, null);
				}
			}
		} catch (Exception e) {
			String err = "Error in setting file visibility for " + file.getId();
			logger.error(err, e);
			throw new FileException(err, e);
		}
	}

	/**
	 * Preparing keywords and other information prior to saving a file
	 *
	 * @param file
	 * @throws FileException
	 */
	public void prepareSaveFile(File file, UserBean user) throws FileException,
			NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (file.getId() != null) {
				File dbFile = (File) appService.get(File.class, file.getId());
				if (dbFile != null) {
					// use original createdBy if it is not COPY
					if (!dbFile.getCreatedBy().equals(
							Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
						file.setCreatedBy(dbFile.getCreatedBy());
					}
					// use original createdDate
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
					} else {
						keyword.setId(null);
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

	// update cloned file with existing visibility and file content, and new
	// file path
	public void updateClonedFileInfo(FileBean copy, String origSampleName,
			String newSampleName, UserBean user) throws Exception {
		// copy file visibility and file content obtain original id from created
		// by
		String origId = copy.getDomainFile().getCreatedBy().substring(5);
		List<String> accessibleGroups = helper.getAuthService()
				.getAccessibleGroups(origId, Constants.CSM_READ_PRIVILEGE);
		if (accessibleGroups != null) {
			copy.setVisibilityGroups(accessibleGroups.toArray(new String[0]));
		}
		if (origId != null) {
			byte[] content = helper.getFileContent(new Long(origId), user);
			copy.setNewFileData(content);
		}
		// replace file URI with new sample name
		if (copy.getDomainFile().getUri() != null) {
			String newUri = copy.getDomainFile().getUri().replace(
					origSampleName, newSampleName);
			copy.getDomainFile().setUri(newUri);
		}
	}
}
