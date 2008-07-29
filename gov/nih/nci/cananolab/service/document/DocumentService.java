package gov.nih.nci.cananolab.service.document;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.exception.DocumentException;

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
	
	public void removeDocumentFromParticle(NanoparticleSample particle,
			Long dataId) 	throws DocumentException;
}
