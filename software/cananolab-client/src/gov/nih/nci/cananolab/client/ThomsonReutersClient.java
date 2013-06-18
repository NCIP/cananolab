/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.client;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class represents a client that retrieves caNanoLab data for Thomson
 * Reuters from the caNanoLab grid service
 * 
 * @author pansu
 * 
 */
public class ThomsonReutersClient {
	private CaNanoLabServiceClient gridClient;
	private static String FIELD_DELIMITER = "\t";
	private static Logger logger = Logger.getLogger(ThomsonReutersClient.class);

	public ThomsonReutersClient(CaNanoLabServiceClient gridClient) {
		this.gridClient = gridClient;
	}

	public void generateOutputFile(String filePath, String fileName)
			throws IOException {
		long startTime = System.currentTimeMillis();
		File outputFile = new File(filePath, fileName);
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
		StringBuilder strBuilder = new StringBuilder();
		int i = 0;
		/* write the column headers */
		for (String header : ThomsonReutersData.headers) {
			strBuilder.append(header);
			if (i < ThomsonReutersData.headers.length) {
				strBuilder.append(FIELD_DELIMITER);
			}
			i++;
		}
		out.write(strBuilder.toString());
		out.newLine();
		/* retrieve sample IDs */
		logger.info("Started retrieving Sample IDs");
		String[] sampleIds = gridClient.getSampleIds("", "", null, null, null,
				null, null);
		logger.info("Finished retrieving " + sampleIds.length + " sample IDs");
		int j = 1;
		for (String sampleId : sampleIds) {
			strBuilder = new StringBuilder();
			/* populate each row */
			logger.info("*** Start sample " + j + "(ID: " + sampleId + ") ***");
			ThomsonReutersData data = this.populateThomsonReutersData(sampleId);
			strBuilder.append(data.getRecordId()).append(FIELD_DELIMITER);
			strBuilder.append(data.getTitle()).append(FIELD_DELIMITER);
			strBuilder.append(data.getSource()).append(FIELD_DELIMITER);
			strBuilder.append(data.getSourceURL()).append(FIELD_DELIMITER);
			strBuilder.append(data.getDataDistributer())
					.append(FIELD_DELIMITER);
			strBuilder.append(data.getAuthorship()).append(FIELD_DELIMITER);
			strBuilder.append(data.getAbstractText()).append(FIELD_DELIMITER);
			strBuilder.append(data.getCitation());
			out.write(strBuilder.toString());
			out.newLine();
			j++;
		}
		out.close();
		long endTime = System.currentTimeMillis();
		logger.info("Finished generating the output in "
				+ (endTime - startTime) / 1000f + " seconds");
	}

	private ThomsonReutersData populateThomsonReutersData(String sampleId) {
		ThomsonReutersData data = null;
		PointOfContact primaryPOC = null;
		List<Publication> pubs = null;
		Sample sample = this.loadSample(sampleId);
		try {
			primaryPOC = gridClient
					.getPrimaryPointOfContactBySampleId(sampleId);
			if (primaryPOC != null) {
				loadOrganizationForPointOfContact(primaryPOC);
				logger.info("loaded organization for POC");
			}
		} catch (Exception e) {
			logger.error("Error loading primary POC for sample " + sampleId
					+ ": " + e);
		}
		SampleComposition comp = this.retrieveComposition(sampleId);
		logger.info("loaded nanomaterial entities");

		try {
			Publication[] publications = gridClient
					.getPublicationsBySampleId(sampleId);
			if (publications != null) {
				pubs = new ArrayList<Publication>();
				for (Publication pub : publications) {
					loadAuthorsForPublication(pub);
					pubs.add(pub);
				}
				logger.info("loaded publications with authors");

			}
		} catch (Exception e) {
			logger.error("Error loading publications for sample " + sampleId
					+ ": " + e);
		}
		String sampleName = "";
		if (sample != null) {
			sampleName = sample.getName();
		}
		data = new ThomsonReutersData(sampleId, sampleName, primaryPOC,
				comp.getNanomaterialEntityCollection(), pubs);

		return data;
	}

	private Sample loadSample(String sampleId) {
		Sample sample = null;
		try {
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.particle.Sample");
			CQLQuery query = new CQLQuery();
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(sampleId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results;
			results = gridClient.query(query);

			results.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Sample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sample = (Sample) obj;
				logger.info("loaded sample info for " + sampleId);
			}
		} catch (Exception e) {
			logger.error("Error loading sample info for " + sampleId);
		}
		return sample;
	}

	private void loadAuthorsForPublication(Publication publication) {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Author");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.Publication");
		association.setRoleName("publicationCollection");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(publication.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results.setTargetClassname("gov.nih.nci.cananolab.domain.common.Author");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Author author = null;
			publication.setAuthorCollection(new HashSet<Author>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				author = (Author) obj;
				publication.getAuthorCollection().add(author);
			}
		} catch (Exception e) {
			logger.error("Exception getting Authors for Publication id="
					+ publication.getId() + ": " + e);
		}
	}

	private SampleComposition retrieveComposition(String sampleId) {
		SampleComposition sampleComposition = null;
		/* find NanomaterialEntity class names */
		try {
			String[] sampleViewStrs = gridClient.getSampleViewStrs(sampleId);

			String[] nanoEntityClassNames = null;
			if (sampleViewStrs.length > 5
					&& !StringUtils.isEmpty(sampleViewStrs[5])) {
				nanoEntityClassNames = sampleViewStrs[5]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}

			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			Association association = new Association();
			association.setName("gov.nih.nci.cananolab.domain.particle.Sample");
			association.setRoleName("sample");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(sampleId);
			association.setAttribute(attribute);
			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results.setTargetClassname("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);

			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sampleComposition = (SampleComposition) obj;
				// load nanomaterial entities
				loadNanomaterialEntitiesForComposition(sampleComposition,
						nanoEntityClassNames);
			}
		} catch (Exception e) {
			logger.error("Exception getting composition for sample " + sampleId
					+ ": " + e);
		}
		return sampleComposition;
	}

	private void loadNanomaterialEntitiesForComposition(
			SampleComposition composition, String[] nanoEntityClassNames) {
		try {
			if (nanoEntityClassNames != null) {
				for (String name : nanoEntityClassNames) {
					String fullClassName = null;
					if (ClassUtils.getFullClass(name) != null) {
						// add the package name in case of biopolymer which can
						// also be in the agentmaterial package
						fullClassName = ClassUtils.getFullClass(
								"nanomaterial." + name).getCanonicalName();
					} else {
						fullClassName = ClassUtils.getFullClass(
								OtherNanomaterialEntity.class
										.getCanonicalName()).getCanonicalName();
					}
					CQLQuery query = new CQLQuery();
					gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
					target.setName(fullClassName);
					Association association = new Association();
					association
							.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
					association.setRoleName("sampleComposition");

					Attribute attribute = new Attribute();
					attribute.setName("id");
					attribute.setPredicate(Predicate.EQUAL_TO);
					attribute.setValue(composition.getId().toString());
					association.setAttribute(attribute);

					target.setAssociation(association);
					query.setTarget(target);
					CQLQueryResults results = gridClient.query(query);
					results.setTargetClassname(fullClassName);
					CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
							results);
					NanomaterialEntity nanoEntity = null;
					composition
							.setNanomaterialEntityCollection(new HashSet<NanomaterialEntity>());
					while (iter.hasNext()) {
						java.lang.Object obj = iter.next();
						nanoEntity = (NanomaterialEntity) obj;
						if (nanoEntity != null) {
							composition.getNanomaterialEntityCollection().add(
									nanoEntity);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in getting nanomaterial entities for composition: "
					+ e);
		}
	}

	/** retrieve Organization associated with PointOfContact */
	private void loadOrganizationForPointOfContact(PointOfContact poc)
			throws RemoteException {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Organization");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.common.PointOfContact");
		association.setRoleName("pointOfContactCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(poc.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);

		CQLQueryResults results = gridClient.query(query);
		results.setTargetClassname("gov.nih.nci.cananolab.domain.common.Organization");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Organization org = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			org = (Organization) obj;
			if (org != null) {
				poc.setOrganization(org);
			}
		}
	}

	public static void main(String args[]) {
		// String gridUrl =
		// "http://cananolab.nci.nih.gov:80/wsrf-canano/services/cagrid/CaNanoLabService";
		String gridUrl = args[0];
		// String outputFileDirectory = "C:/dist";
		String outputFileDirectory = args[1];
		// String outputFileName = "TR_caNanoLab_data.txt";
		String outputFileName = args[2];
		try {
			CaNanoLabServiceClient gridClient = new CaNanoLabServiceClient(
					gridUrl);
			ThomsonReutersClient client = new ThomsonReutersClient(gridClient);
			client.generateOutputFile(outputFileDirectory, outputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}