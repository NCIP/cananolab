package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;

/**
 * Interface defining methods for file retrieving and writing.
 *
 * @author pansu
 *
 */
public interface FileService {
	/**
	 * Load the file for the given fileId from the database. Also check whether
	 * user can do it.
	 *
	 * @param fileId
	 * @return
	 */
	public FileBean findFileById(String fileId, UserBean user)
			throws FileException, NoAccessException;

	public void writeFile(FileBean fileBean, UserBean user)
			throws FileException, NoAccessException;

	/**
	 * Preparing keywords and other information prior to saving a file
	 *
	 * @param file
	 * @throws FileException
	 */
	public void prepareSaveFile(File file, UserBean user) throws FileException, NoAccessException;
}
