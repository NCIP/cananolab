package gov.nih.nci.cananolab.service.document.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.service.document.DocumentService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Remote implementation of DocumentService
 * 
 * @author tanq
 * 
 */
public class DocumentServiceRemoteImpl implements DocumentService {
	private static Logger logger = Logger
			.getLogger(DocumentServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public DocumentServiceRemoteImpl(String serviceUrl) throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	/**
	 * Persist a new publication or update an existing publication
	 * 
	 * @param report
	 * @throws Exception
	 */
	public void savePublication(Publication publication, String[] particleNames,
			byte[] fileData) throws DocumentException {
		throw new DocumentException("not implemented for grid service.");
	}

	//TODO, XXXXXXXX tanq
	public List<PublicationBean> findPublicationsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws DocumentException, CaNanoLabSecurityException {
		List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
		try {
			//TODO, XXXXXXXX tanq
			//TODO, uncomment catch (RemoteException e)
			/*Publication[] publications = gridClient.getPublicationsBy(reportTitle,
					reportCategory, nanoparticleEntityClassNames,
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
			PublicationBean publicationBean = new PublicationBean(publication);
			return publicationBean;
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new DocumentException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	
		} catch (Exception e) {
			String err = "Problem finding the report by id: " + publicationId;
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}

	//TODO
	public int getNumberOfPublicDocuments() throws DocumentException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Report");
			query.setTarget(target);
			QueryModifier modifier = new QueryModifier();
			modifier.setCountOnly(true);
			query.setQueryModifier(modifier);

			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			int count = 0;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				count = ((Long) obj).intValue();
			}
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of remote public reports.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}

	//TODO XXXXXXXXXXXXXX
	public LabFile[] findDocumentsByParticleSampleId(String particleId)
			throws DocumentException {
		{
			try {
				CQLQuery query = new CQLQuery();
				gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
				target.setName("gov.nih.nci.cananolab.domain.common.Report");
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
						.setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
				CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
						results);
				Report report = null;
				List<Report> reports = new ArrayList<Report>();
				while (iter.hasNext()) {
					java.lang.Object obj = iter.next();
					report = (Report) obj;
					reports.add(report);
				}
				//return reports.toArray(new Report[0]);
				//TODO
				return null;
			} catch (RemoteException e) {
				logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
				throw new DocumentException(CaNanoLabConstants.NODE_UNAVAILABLE, e);	
			} catch (Exception e) {
				String err = "Error finding reports for particle.";
				logger.error(err, e);
				throw new DocumentException(err, e);
			}
		}
	}
}
