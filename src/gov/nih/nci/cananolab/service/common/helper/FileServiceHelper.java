package gov.nih.nci.cananolab.service.common.helper;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

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
public class FileServiceHelper extends BaseServiceHelper {
	private Logger logger = Logger.getLogger(FileServiceHelper.class);

	public FileServiceHelper() {
		super();
	}

	public FileServiceHelper(UserBean user) {
		super(user);
	}

	public FileServiceHelper(AuthorizationService authService) {
		super(authService);
	}

	public FileServiceHelper(AuthorizationService authService, UserBean user) {
		super(authService, user);
	}

	/**
	 * Load the file for the given fileId from the database
	 *
	 * @param fileId
	 * @return
	 */
	public File findFileById(String fileId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(File.class).add(
				Property.forName("id").eq(new Long(fileId)));
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		File file = null;
		if (!result.isEmpty()) {
			file = (File) result.get(0);
			if (getAccessibleData().contains(file.getId().toString())) {
				return file;
			} else {
				throw new NoAccessException();
			}
		}
		return file;
	}

	/**
	 * Get the content of the file into a byte array.
	 *
	 * @param fileId
	 * @return
	 * @throws FileException
	 */
	public byte[] getFileContent(Long fileId) throws Exception {
		File file = findFileById(fileId.toString());
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
}
