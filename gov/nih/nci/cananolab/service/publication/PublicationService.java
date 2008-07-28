package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;

import java.util.List;

/**
 * Interface defining methods invovled in submiting and searching documents.
 * 
 * @author tanq
 * 
 */
public interface PublicationService {

	/**
	 * Persist a new report or update an existing report
	 * 
	 * @param report
	 * @throws Exception
	 */
	public void savePublication(Publication publication, String[] particleNames,
			byte[] fileData) throws DocumentException;

/*	public List<LabFileBean> findDocumentsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws DocumentException, CaNanoLabSecurityException;*/
	
	public List<PublicationBean> findPublicationsBy(String reportTitle,
			String reportCategory, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws DocumentException, CaNanoLabSecurityException;

	public PublicationBean findPublicationById(String publicationId) throws DocumentException;

//	public int getNumberOfPublicDocuments() throws DocumentException;

//	public LabFile[] findDocumentsByParticleSampleId(String particleId)
		//	throws DocumentException;
}
