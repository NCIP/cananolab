/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.publication.PublicationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Remote implementation of PublicationService
 *
 * @author tanq
 *
 */
public class PublicationServiceRemoteImpl implements PublicationService {
	private static Logger logger = Logger
			.getLogger(PublicationServiceRemoteImpl.class);

	private CaNanoLabServiceClient gridClient;

	public PublicationServiceRemoteImpl(String serviceUrl)
			throws PublicationException {
		try {
			gridClient = new CaNanoLabServiceClient(serviceUrl);
		} catch (Exception e) {
			throw new PublicationException(
					"Can't create grid client succesfully.");
		}
	}

	/**
	 * Persist a new publication or update an existing publication
	 *
	 * @param publication
	 *            ,
	 * @param sampleNames
	 *            ,
	 * @param fileData
	 *            ,
	 * @param authors
	 * @throws Exception
	 */
	public void savePublication(PublicationBean publicationBean)
			throws PublicationException, NoAccessException {
		throw new PublicationException("Not implemented for grid service");
	}

	// public List<PublicationBean> findPublicationsBy(String title,
	// String category, String sampleName, String[] researchAreas,
	// String[] keywords, String pubMedId, String digitalObjectId,
	// String[] authors, String[] nanomaterialEntityClassNames,
	// String[] otherNanomaterialEntityTypes,
	// String[] functionalizingEntityClassNames,
	// String[] otherFunctionalizingEntityTypes,
	// String[] functionClassNames, String[] otherFunctionTypes,
	// UserBean user) throws PublicationException {
	// List<PublicationBean> publicationBeans = new
	// ArrayList<PublicationBean>();
	// try {
	// Publication[] publications = gridClient.getPublicationsBy(title,
	// category, sampleName, researchAreas, keywords, pubMedId,
	// digitalObjectId, authors, nanomaterialEntityClassNames,
	// functionalizingEntityClassNames, functionClassNames);
	// if (publications.length > 0) {
	// for (Publication publication : publications) {
	// loadPublicationAssociations(publication);
	// publicationBeans.add(getPublicationBean(publication));
	// }
	// }
	// return publicationBeans;
	//
	// } catch (Exception e) {
	// String err = "Problem finding publication info.";
	// logger.error(err, e);
	// throw new PublicationException(err, e);
	// }
	// }

	private PublicationBean getPublicationBean(Publication publication)
			throws Exception {
		// retrieve sampleNames
		String[] sampleNames = gridClient
				.getSampleNamesByPublicationId(publication.getId().toString());
		PublicationBean pubBean = new PublicationBean(publication, sampleNames);
		return pubBean;
	}

	public List<PublicationBean> findPublicationsBySampleId(String sampleId)
			throws PublicationException {
		List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
		try {
			Publication[] publications = gridClient
					.getPublicationsBySampleId(sampleId);
			if (publications.length > 0) {
				for (Publication publication : publications) {
					loadPublicationAssociations(publication);
					publicationBeans.add(getPublicationBean(publication));
				}
			}
			return publicationBeans;
		} catch (Exception e) {
			String err = "Problem finding remote publications by the given sample ID.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public PublicationBean findPublicationById(String publicationId)
			throws PublicationException, NoAccessException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Publication");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(publicationId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Publication");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Publication publication = null;
			PublicationBean publicationBean = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				publication = (Publication) obj;
				if (publication != null) {
					loadPublicationAssociations(publication);
					publicationBean = getPublicationBean(publication);
				}
			}
			return publicationBean;
		} catch (Exception e) {
			String err = "Problem finding the remote publcation by id: "
					+ publicationId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	private void loadPublicationAssociations(Publication publication)
			throws Exception {
		// load authors
		loadAuthorsForPublication(publication);
		// load keywords
		// loadKeywordsForPublication(publication);
	}

	private void loadAuthorsForPublication(Publication publication)
			throws Exception {
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
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Author");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Author author = null;
		publication.setAuthorCollection(new HashSet<Author>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			author = (Author) obj;
			publication.getAuthorCollection().add(author);
		}
	}

	private void loadKeywordsForPublication(Publication publication)
			throws Exception {
		// load keywords for a file
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Keyword");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.File");
		association.setRoleName("fileCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(publication.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Keyword");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Set<Keyword> keywords = new HashSet<Keyword>();
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Keyword keyword = (Keyword) obj;
			keywords.add(keyword);
		}
		publication.setKeywordCollection(keywords);
	}

	public int getNumberOfPublicPublications() throws PublicationException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Publication");
			query.setTarget(target);
			QueryModifier modifier = new QueryModifier();
			modifier.setCountOnly(true);
			query.setQueryModifier(modifier);

			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Publication");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			int count = 0;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				count = ((Long) obj).intValue();
			}
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of remote public publications.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<String> findPublicationIdsBy(String title, String category,
			String sampleName, String[] researchAreas, String[] keywords,
			String pubMedId, String digitalObjectId, String[] authors,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException {
		try {
			String[] publicationIds = gridClient.getPublicationIdsBy(title,
					category, sampleName, researchAreas, keywords, pubMedId,
					digitalObjectId, authors, nanomaterialEntityClassNames,
					functionalizingEntityClassNames, functionClassNames);
			if (publicationIds != null) {
				return Arrays.asList(publicationIds);
			} else {
				return new ArrayList<String>();
			}
		} catch (Exception e) {
			String err = "Error finding remote public publications.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public PublicationBean findPublicationByKey(String keyName, Object keyValue)
			throws PublicationException, NoAccessException {
		throw new PublicationException("Not implemented for grid service");
	}

	public List<String> deletePublication(Publication publication,
			Boolean removeVisibility) throws PublicationException,
			NoAccessException {
		throw new PublicationException("Not implemented for grid service");
	}

	public PublicationBean getPublicationFromPubMedXML(String pubMedId)
			throws PublicationException {
		throw new PublicationException("Not implemented for grid service");
	}

	public PublicationBean findNonPubMedNonDOIPublication(
			String publicationType, String title, String firstAuthorLastName,
			String firstAuthorFirstName) throws PublicationException {
		throw new PublicationException("Not implemented for grid service");
	}

	public void removePublicationFromSample(String sampleName,
			Publication publication) throws PublicationException,
			NoAccessException {
		throw new PublicationException("Not implemented for grid service");
	}
}
