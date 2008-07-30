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
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
			byte[] fileData, List<DocumentAuthor> authors) throws DocumentException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(publication);
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			Set<NanoparticleSample> particleSamples = new HashSet<NanoparticleSample>();
			for (String name : particleNames) {
				NanoparticleSample sample = sampleService
						.findNanoparticleSampleByName(name);
				particleSamples.add(sample);
			}

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (publication.getNanoparticleSampleCollection() == null) {
				publication
						.setNanoparticleSampleCollection(new HashSet<NanoparticleSample>());
			}
			for (NanoparticleSample sample : particleSamples) {
				publication.getNanoparticleSampleCollection().add(sample);
				sample.getPublicationCollection().add(publication);
			}
			
			if (publication.getDocumentAuthorCollection() == null) {
				System.out.println("####### publication.getDocumentAuthorCollection() == null");
				publication
						.setDocumentAuthorCollection(new HashSet<DocumentAuthor>());
			}
			if (authors!=null) {
				System.out.println("####### authors!=null");
				for (DocumentAuthor author : authors) {
					publication.getDocumentAuthorCollection().add(author);
					author.getPublicationCollection().add(publication);
				}
			}else {
				System.out.println("####### authors=====null");				
			}
			appService.saveOrUpdate(publication);

			// save to the file system fileData is not empty
			fileService.writeFile(publication, fileData);

		} catch (Exception e) {
			String err = "Error in saving the publication.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}

/*	//TODO XXXX may create a documentBean
	public List findDocumentsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws DocumentException, CaNanoLabSecurityException {
		List<DocumentBean> documentBeans = new ArrayList<DocumentBean>();
		try {
			Collection documents = helper.findDocumentsBy(reportTitle,
					reportCategory, nanoparticleEntityClassNames,
					otherNanoparticleTypes, functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes);
			for (Object document : documents) {
				//TODO, tanq
				documentBeans.add(new DocumentBean(document));
			}
			return documentBeans;
		} catch (Exception e) {
			String err = "Problem finding report info.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}*/
	
	
	public List<PublicationBean> findPublicationsBy(String title,
			String category, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws DocumentException, CaNanoLabSecurityException {
		List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
		try {
			List<Publication> publications = helper.findPublicationsBy(title, category, nanoparticleEntityClassNames, 
					otherNanoparticleTypes, functionalizingEntityClassNames, 
					otherFunctionalizingEntityTypes, functionClassNames, otherFunctionTypes);
			if (publications!=null) {
				for (Publication publication: publications) {
					publicationBeans.add(new PublicationBean(publication));
				}
			}			
			return publicationBeans;
			
		} catch (Exception e) {
			String err = "Problem finding publication info.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
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
