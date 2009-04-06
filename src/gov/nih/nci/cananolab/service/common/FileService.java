package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.SecurityException;

import java.util.List;

/**
 * Interface defining methods for file retrieving and writing.
 * 
 * @author pansu
 * 
 */
public interface FileService {
	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public FileBean findFileById(String fileId) throws FileException;

	/**
	 * Load the file for the given fileId from the database. Also check whether
	 * user can do it.
	 * 
	 * @param fileId
	 * @return
	 */
	public FileBean findFileById(String fileId, UserBean user)
			throws FileException, SecurityException;

	public List<File> findFilesByCompositionInfoId(String id,
			String className) throws FileException;

	public void saveCopiedFileAndSetVisibility(File copy, UserBean user,
			String oldSampleName, String newSampleName) throws FileException;

	public void writeFile(File file, byte[] fileData) throws FileException;

	/**
	 * Preparing keywords and other information prior to saving a file
	 * 
	 * @param file
	 * @throws FileException
	 */
	public void prepareSaveFile(File file) throws FileException;

	// retrieve file visibility
	public void retrieveVisibility(FileBean fileBean, UserBean user)
			throws FileException;
	
	public void retrieveAccessibility(FileBean fileBean, UserBean user)
	throws FileException;
}
