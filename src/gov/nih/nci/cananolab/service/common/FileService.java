package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.FileException;

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
	public LabFileBean findFileById(String fileId) throws FileException;

	/**
	 * Load the file for the given fileId from the database. Also check whether
	 * user can do it.
	 * 
	 * @param fileId
	 * @return
	 */
	public LabFileBean findFileById(String fileId, UserBean user)
			throws FileException, CaNanoLabSecurityException;

	public List<LabFile> findFilesByCompositionInfoId(String id,
			String className) throws FileException;

	public void saveCopiedFileAndSetVisibility(LabFile copy, UserBean user,
			String oldSampleName, String newSampleName) throws FileException;

	public void writeFile(LabFile file, byte[] fileData) throws FileException;

	/**
	 * Preparing keywords and other information prior to saving a file
	 * 
	 * @param file
	 * @throws FileException
	 */
	public void prepareSaveFile(LabFile file) throws FileException;

	// retrieve file visibility
	public void retrieveVisibility(LabFileBean fileBean, UserBean user)
			throws FileException;
	
	public void retrieveAccessibility(LabFileBean fileBean, UserBean user)
	throws FileException;
}
