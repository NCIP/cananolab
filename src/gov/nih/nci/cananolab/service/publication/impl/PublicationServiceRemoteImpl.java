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
		return publicationBeans;		
	}



	public Publication[] findPublicationsByParticleSampleId(String particleId)
		throws DocumentException {	
		return new Publication[0];
	}
	

	public PublicationBean findPublicationById(String publicationId) throws DocumentException {
		return null;
	}
	
	public Publication findDomainPublicationById(String publicationId) throws DocumentException{
		return null;
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
