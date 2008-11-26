package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.service.common.FileService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Remote implementation of FileService
 * 
 * @author pansu
 * 
 */
public class FileServiceRemoteImpl implements FileService {
	Logger logger = Logger.getLogger(FileServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public FileServiceRemoteImpl(String serviceUrl) throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
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
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.File");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(fileId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.File");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			File file = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				file = (File) obj;
				loadKeywordsForFile(file); 
			}
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
			throws FileException, CaNanoLabSecurityException {
		FileBean fileBean = null;
		try {
			// remote service already filtered out non-public files
			fileBean = findFileById(fileId);
			return fileBean;
		} catch (Exception e) {
			logger.error("Problem finding the file by id: " + fileId, e);
			throw new FileException();
		}
	}

	public List<File> findFilesByCompositionInfoId(String id,
			String className) throws FileException {
		try {
			List<File> fileSet = new ArrayList<File>();
			/**TODO temporarily commented 
			File files = gridClient.getFilesByCompositionInfoId(id,
					className);
			if (files != null) {
				for (File file : files) {
					loadKeywordsForFile(file);
					fileSet.add(file);
				}
			}
			*/			
			return fileSet;
		} catch (Exception e) {
			String err = "Error finding files by " + className + "and id " + id;
			logger.error(err, e);
			throw new FileException(err, e);
		}
	}

	private void loadKeywordsForFile(File file) throws Exception {
		Keyword[] keywords = gridClient.getKeywordsByFileId(file.getId()
				.toString());
		if (keywords != null && keywords.length > 0) {
			file.setKeywordCollection(new HashSet<Keyword>(Arrays
					.asList(keywords)));
		}
	}

	public void saveCopiedFileAndSetVisibility(File copy, UserBean user,
			String oldSampleName, String newSampleName) throws FileException {
		throw new FileException("Not implemented for grid service");
	}

	// save to the file system fileData is not empty
	public void writeFile(File file, byte[] fileData) throws FileException {
		throw new FileException("Not implemented for grid service");
	}

	/**
	 * Preparing keywords and other information prior to saving a file
	 * 
	 * @param file
	 * @throws FileException
	 */
	public void prepareSaveFile(File file) throws FileException {
		throw new FileException("Not implemented for grid service");
	}

	// retrieve file visibility
	public void retrieveVisibility(FileBean fileBean, UserBean user)
			throws FileException {
		throw new FileException("Not implemented for grid service");
	}
	
	// retrieve file retrieve accessibility
	public void retrieveAccessibility(FileBean fileBean, UserBean user)
			throws FileException {
		throw new FileException("Not implemented for grid service");
	}
}
