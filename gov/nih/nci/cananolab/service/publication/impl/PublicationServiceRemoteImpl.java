package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.exception.ReportException;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

	public PublicationServiceRemoteImpl(String serviceUrl) throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	/**
	 * Persist a new publication or update an existing publication
	 * 
	 * @param report
	 * @throws Exception
	 */
	public void savePublication(Publication publication, String[] particleNames,
			byte[] fileData, Collection<DocumentAuthor> authors) throws DocumentException {
		throw new DocumentException("not implemented for grid service.");
	}

	//TODO, XXXXXXXX tanq
	//remote search did not implement findPublicationsBy yet
	//wait for update on gridclient
	
	public List<PublicationBean> findPublicationsBy(String title,
			String category, String nanoparticleName, 
			String[] researchArea, String keywordsStr,
			String pubMedId, String digitalObjectId, String authorsStr,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws DocumentException, CaNanoLabSecurityException {
		List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
		try {
			//TODO, uncomment catch (RemoteException e)
			/*Publication[] publications = gridClient.getPublicationsBy(title,
					category, nanoparticleEntityClassNames,
					functionalizingEntityClassNames, functionClassNames);*/
			Publication[] publications = null;
			if (publications != null) {
				for (Publication publication : publications) {
					loadParticleSamplesForPublication(publication);
					publicationBeans.add(new PublicationBean(publication));
				}
			}
			return publicationBeans;
		/*} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new DocumentException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	*/
		} catch (Exception e) {
			String err = "Problem finding report info.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}



	public Publication[] findPublicationsByParticleSampleId(String particleId)
		throws DocumentException {	
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Publication");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			association.setRoleName("nanoparticleSampleCollection");
	
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);
			association.setAttribute(attribute);
	
			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Publication");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
					results);
			Publication publication = null;
			List<Publication> publications = new ArrayList<Publication>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				publication = (Publication) obj;
				publications.add(publication);
			}
			return publications.toArray(new Publication[0]);
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new DocumentException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	
		} catch (Exception e) {
			String err = "Error finding publications for particle.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}
	
	//TODO, tanq
	private void loadParticleSamplesForPublication(Publication publication)
		throws DocumentException {
	try {
		CQLQuery query = new CQLQuery();
	
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
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
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		NanoparticleSample particleSample = null;
		publication
				.setNanoparticleSampleCollection(new HashSet<NanoparticleSample>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			particleSample = (NanoparticleSample) obj;
			publication.getNanoparticleSampleCollection().add(particleSample);
		}
	} catch (Exception e) {
		String err = "Problem loading nanoparticle samples for the publication : "
				+ publication.getId();
		logger.error(err, e);
		throw new DocumentException(err, e);
	}
	}
	public PublicationBean findPublicationById(String publicationId) throws DocumentException {
		try {
			Publication publication = findDomainPublicationById(publicationId);
			PublicationBean publicationBean = new PublicationBean(publication);
			return publicationBean;
		} catch (Exception e) {
			String err = "Problem finding the publication by id: " + publicationId;
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}
	
	public Publication findDomainPublicationById(String publicationId) throws DocumentException{
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
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				publication = (Publication) obj;
			}
			loadParticleSamplesForPublication(publication);
			return publication;
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new DocumentException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	
		} catch (Exception e) {
			String err = "Problem finding the publication by id: " + publicationId;
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}
	
	public void exportDetail(PublicationBean aPub, OutputStream out)
		throws DocumentException{		
		try {
			PublicationServiceHelper helper = new PublicationServiceHelper();
			helper.exportDetail(aPub, out);
		} catch (Exception e) {
			String err = "error exporting detail view for "
					+ aPub.getDomainFile().getTitle();
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}
}
