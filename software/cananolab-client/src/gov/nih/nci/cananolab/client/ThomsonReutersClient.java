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
	private static String FILE_DELIMITER = "\t";
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
				strBuilder.append(FILE_DELIMITER);
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
//			if (j == 222) {
				/* populate each row */
				logger.info("*** Start sample " + j + "(ID: " + sampleId
						+ ") ***");
				try {
					ThomsonReutersData data = this
							.populateThomsonReutersData(sampleId);
					strBuilder.append(data.getSource()).append(FILE_DELIMITER);
					strBuilder.append(data.getSourceURL()).append(
							FILE_DELIMITER);
					strBuilder.append(data.getDataDistributer()).append(
							FILE_DELIMITER);
					strBuilder.append(data.getAuthorship()).append(
							FILE_DELIMITER);
					strBuilder.append(data.getAbstractText()).append(
							FILE_DELIMITER);
					strBuilder.append(data.getCitation());
					out.write(strBuilder.toString());
					out.newLine();
				} catch (Exception e) {
					logger.error("Error processing sample " + sampleId + ": "
							+ e);
				}
//			}
			j++;
		}
		out.close();
		long endTime = System.currentTimeMillis();
		logger.info("Finished generating the output in "
				+ (endTime - startTime) / 1000f + " seconds");
	}

	private ThomsonReutersData populateThomsonReutersData(String sampleId)
			throws RemoteException {
		ThomsonReutersData data = null;
		try {
			PointOfContact primaryPOC = gridClient
					.getPrimaryPointOfContactBySampleId(sampleId);
			loadOrganizationForPointOfContact(primaryPOC);
			logger.info("loaded organization for POC");
			SampleComposition comp = this.retrieveComposition(sampleId);
			logger.info("loaded nanomaterial entities");
			Publication[] publications = gridClient
					.getPublicationsBySampleId(sampleId);
			List<Publication> pubs = null;
			if (publications != null) {
				pubs = new ArrayList<Publication>();
				for (Publication pub : publications) {
					loadAuthorsForPublication(pub);
					pubs.add(pub);
				}
				logger.info("loaded publications with authors");
			}
			data = new ThomsonReutersData(sampleId, primaryPOC,
					comp.getNanomaterialEntityCollection(), pubs);
		} catch (Exception e) {
			logger.error("Error retrieving data for sample Id " + sampleId
					+ ":" + e);
		}
		return data;
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
						//add the package name in case of biopolymer which can also be in the agentmaterial package
						fullClassName = ClassUtils.getFullClass("nanomaterial."+name)
								.getCanonicalName();
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
		// String gridUrl=args[0];
		String gridUrl = "http://cananolab.nci.nih.gov:80/wsrf-canano/services/cagrid/CaNanoLabService";
		String outputFileName = "TR_caNanoLab_data.txt";
		try {
			CaNanoLabServiceClient gridClient = new CaNanoLabServiceClient(
					gridUrl);
			ThomsonReutersClient client = new ThomsonReutersClient(gridClient);
			client.generateOutputFile("C:/dist", outputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}