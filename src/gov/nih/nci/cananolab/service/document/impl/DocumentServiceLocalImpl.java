package gov.nih.nci.cananolab.service.document.impl;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.service.document.DocumentService;
import gov.nih.nci.cananolab.service.document.helper.DocumentServiceHelper;

import org.apache.log4j.Logger;

/**
 * Local implementation of DocumentService
 * 
 * @author tanq
 * 
 */
public class DocumentServiceLocalImpl implements DocumentService {
	private static Logger logger = Logger
			.getLogger(DocumentServiceLocalImpl.class);
	private DocumentServiceHelper helper = new DocumentServiceHelper();


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
		
	public int getNumberOfPublicDocuments() throws DocumentException {
		try {
			int count = helper.getNumberOfPublicDocuments();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public reports.";
			logger.error(err, e);
			throw new DocumentException(err, e);
		}
	}

	public Publication[] findDocumentsByParticleSampleId(String particleId)
			throws DocumentException {
		throw new DocumentException("Not implemented for local search");
	}
}
