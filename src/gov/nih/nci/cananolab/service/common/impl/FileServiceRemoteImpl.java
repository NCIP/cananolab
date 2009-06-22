package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;

import java.util.HashSet;

import org.apache.log4j.Logger;

/**
 * Local implementation of FileService
 *
 * @author pansu
 *
 */
public class FileServiceRemoteImpl implements FileService {
	private Logger logger = Logger.getLogger(FileServiceRemoteImpl.class);

	private FileServiceHelper helper = new FileServiceHelper();

	private CaNanoLabServiceClient gridClient;

	public FileServiceRemoteImpl(String serviceUrl) throws FileException {
		try {
			gridClient = new CaNanoLabServiceClient(serviceUrl);
		} catch (Exception e) {
			throw new FileException("Can't create grid client succesfully.");
		}
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
			results.setTargetClassname("gov.nih.nci.cananolab.domain.File");
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

	public void loadKeywordsForFile(File file) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Keyword");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.common.File");
		association.setRoleName("fileCollection");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(file.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Keyword");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		file.setKeywordCollection(new HashSet<Keyword>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Keyword keyword = (Keyword) obj;
			file.getKeywordCollection().add(keyword);
		}
	}

	public void writeFile(FileBean fileBean, UserBean user)
			throws FileException, NoAccessException {
		throw new FileException("Not implemented for grid service");
	}

	/**
	 * Preparing keywords and other information prior to saving a file
	 *
	 * @param file
	 * @throws FileException
	 */
	public void prepareSaveFile(File file, UserBean user) throws FileException,
			NoAccessException {
		throw new FileException("Not implemented for grid service");
	}

	private void assignVisibility(FileBean fileBean) throws FileException {
		throw new FileException("Not implemented for grid service");
	}
}
