package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Local implementation of PublicationService
 * 
 * @author tanq
 * 
 */
public class PublicationServiceLocalImpl implements PublicationService {
	private static Logger logger = Logger
			.getLogger(PublicationServiceLocalImpl.class);
	private PublicationServiceHelper helper = new PublicationServiceHelper();

	/**
	 * Persist a new publication or update an existing publication
	 * 
	 * @param publication, particleNames, fileData, authors
	 * @throws Exception
	 */
	public void savePublication(Publication publication, String[] particleNames,
			byte[] fileData, Collection<DocumentAuthor> authors) throws DocumentException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(publication);
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			Set<NanoparticleSample> particleSamples = new HashSet<NanoparticleSample>();
			if (particleNames!=null && particleNames.length>0) {
				for (String name : particleNames) {
					NanoparticleSample sample = sampleService
							.findNanoparticleSampleByName(name);
					particleSamples.add(sample);
				}
			}

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (publication.getNanoparticleSampleCollection() == null) {
				publication
						.setNanoparticleSampleCollection(new HashSet<NanoparticleSample>());
			}else {
				publication.getNanoparticleSampleCollection().clear();
			}
			for (NanoparticleSample sample : particleSamples) {
				publication.getNanoparticleSampleCollection().add(sample);
				sample.getPublicationCollection().add(publication);
			}			
			
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			
			if (publication.getDocumentAuthorCollection() == null) {
				publication
						.setDocumentAuthorCollection(new TreeSet<DocumentAuthor>());
			}else {
				for (DocumentAuthor author: publication.getDocumentAuthorCollection()) {
					authService.removePublicGroup(author.getId().toString());
				}
				publication.getDocumentAuthorCollection().clear();
			}
			if (authors!=null) {
				for (DocumentAuthor author : authors) {
					if (!StringUtils.isBlank(author.getFirstName()) || 
						!StringUtils.isBlank(author.getLastName())||
						!StringUtils.isBlank(author.getMiddleInitial())){
						publication.getDocumentAuthorCollection().add(author);
					}
				}			
			}
			appService.saveOrUpdate(publication);
			fileService.writeFile(publication, fileData);

		} catch (Exception e) {
			String err = "Error in saving the publication.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
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
		try {
			List<Publication> publications = helper.findPublicationsBy(title, category,
					nanoparticleName, researchArea, keywordsStr,
					pubMedId, digitalObjectId, authorsStr,
					nanoparticleEntityClassNames, 
					otherNanoparticleTypes, functionalizingEntityClassNames, 
					otherFunctionalizingEntityTypes, functionClassNames, otherFunctionTypes);
			if (publications!=null) {
				for (Publication publication: publications) {
					publicationBeans.add(new PublicationBean(publication));
				}
			}			
			Collections.sort(publicationBeans,
					new CaNanoLabComparators.PublicationBeanTitleComparator());
			return publicationBeans;
			
		} catch (Exception e) {
			String err = "Problem finding publication info.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}
	
	public Publication[] findPublicationsByParticleSampleId(String particleId)
		throws DocumentException{
		throw new DocumentException("Not implemented for local search");
	}
	
	public PublicationBean findPublicationById(String publcationId) throws DocumentException {
		try {
			Publication publication = helper.findPublicationById(publcationId);
			PublicationBean publicationBean = new PublicationBean(publication);
			return publicationBean;
		} catch (Exception e) {
			String err = "Problem finding the publcation by id: " + publcationId;
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}
	
	public Publication findDomainPublicationById(String publcationId) throws DocumentException {
		try {
			Publication publication = helper.findPublicationById(publcationId);
			return publication;
		} catch (Exception e) {
			String err = "Problem finding the publcation by id: " + publcationId;
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
//	public int getNumberOfPublicDocuments() throws DocumentException {
//		try {
//			int count = helper.getNumberOfPublicDocuments();
//			return count;
//		} catch (Exception e) {
//			String err = "Error finding counts of public reports.";
//			logger.error(err, e);
//			throw new DocumentException(err, e);
//		}
//	}

//	public Publication[] findDocumentsByParticleSampleId(String particleId)
//			throws DocumentException {
//		throw new DocumentException("Not implemented for local search");
//	}
}
