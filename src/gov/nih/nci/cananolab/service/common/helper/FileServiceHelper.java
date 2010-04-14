package gov.nih.nci.cananolab.service.common.helper;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Utility service for file retrieving and writing.
 *
 * @author pansu, tanq
 *
 */
public class FileServiceHelper {
	private Logger logger = Logger.getLogger(FileServiceHelper.class);
	private AuthorizationService authService;

	public FileServiceHelper() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	/**
	 * Load the file for the given fileId from the database
	 *
	 * @param fileId
	 * @return
	 */
	public File findFileById(String fileId, UserBean user) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(File.class).add(
				Property.forName("id").eq(new Long(fileId)));
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		File file = null;
		if (!result.isEmpty()) {
			file = (File) result.get(0);
			if (authService.checkReadPermission(user, file.getId().toString())) {
				return file;
			} else {
				throw new NoAccessException();
			}
		}
		return file;
	}

	public AuthorizationService getAuthService() {
		return authService;
	}

	// retrieve file visibility
	public void retrieveVisibility(FileBean fileBean) throws Exception {
		if (fileBean != null) {
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);
			// get assigned visible groups
			List<String> accessibleGroups = auth.getAccessibleGroups(fileBean
					.getDomainFile().getId().toString(),
					Constants.CSM_READ_PRIVILEGE);
			String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
			fileBean.setVisibilityGroups(visibilityGroups);
		}
	}

	private void retrieveVisibilityAndContentForCopiedFile(FileBean copy,
			UserBean user) throws Exception {
		// obtain original id from created by
		String origId = copy.getDomainFile().getCreatedBy().substring(5);
		List<String> accessibleGroups = authService.getAccessibleGroups(origId,
				Constants.CSM_READ_PRIVILEGE);
		if (accessibleGroups != null) {
			copy.setVisibilityGroups(accessibleGroups.toArray(new String[0]));
		}
		if (origId != null) {
			byte[] content = this.getFileContent(new Long(origId), user);
			copy.setNewFileData(content);
		}
	}

	// update cloned file with existing visibility and file content, and new
	// file path
	public void updateClonedFileInfo(FileBean copy, String origSampleName,
			String newSampleName, UserBean user) throws Exception {
		// copy file visibility and file content
		retrieveVisibilityAndContentForCopiedFile(copy, user);
		// replace file URI with new sample name
		if (copy.getDomainFile().getUri() != null) {
			String newUri = copy.getDomainFile().getUri().replace(
					origSampleName, newSampleName);
			copy.getDomainFile().setUri(newUri);
		}
	}

	/**
	 * Get the content of the file into a byte array.
	 *
	 * @param fileId
	 * @return
	 * @throws FileException
	 */
	private byte[] getFileContent(Long fileId, UserBean user) throws Exception {
		File file = findFileById(fileId.toString(), user);
		if (file == null || file.getUri() == null) {
			return null;
		}
		// check if the file is external link
		if (file.getUri().startsWith("http")) {
			return null;
		}
		String fileRoot = PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "fileRepositoryDir");

		java.io.File fileObj = new java.io.File(fileRoot
				+ java.io.File.separator + file.getUri());
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
	}

	/**
	 * Check if file is accessible first. If so, retrieve visibility for files.
	 * If no, remove the file from the list.
	 *
	 * @param fileBeans
	 * @param user
	 * @throws CompositionException
	 */
	public void removeUnaccessibleFiles(Collection<File> files, UserBean user)
			throws FileException {
		try {
			Set<File> copiedFiles = new HashSet<File>(files);
			for (File file : copiedFiles) {
				// check whether user can access the file, if not remove from
				// the
				// list
				if (!authService.checkReadPermission(user, file.getId()
						.toString())) {
					files.remove(file);
					logger
							.debug("User can't access file of id:"
									+ file.getId());
				}
			}
		} catch (Exception e) {
			String err = "Error checking access permission for files ";
			logger.error(err, e);
			throw new FileException(err, e);
		}
	}
}
