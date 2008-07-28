package gov.nih.nci.cananolab.service.document;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;

import java.util.List;

/**
 * Interface defining methods invovled in searching documents.
 * 
 * @author tanq
 * 
 */
public interface DocumentService {

	public int getNumberOfPublicDocuments() throws DocumentException;

	public LabFile[] findDocumentsByParticleSampleId(String particleId)
			throws DocumentException;
}
