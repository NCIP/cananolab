package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DocumentException;
import gov.nih.nci.cananolab.exception.ReportException;

import java.io.OutputStream;
import java.util.Collection;
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
			byte[] fileData, Collection<DocumentAuthor> authors) throws DocumentException;

	
	public List<PublicationBean> findPublicationsBy(String reportTitle,
			String reportCategory, String nanoparticleName, 
			String[] researchArea, String keywordsStr,
			String pubMedId, String digitalObjectId, String authorsStr,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws DocumentException, CaNanoLabSecurityException;

	public PublicationBean findPublicationById(String publicationId) throws DocumentException;

	public Publication findDomainPublicationById(String publicationId) throws DocumentException;

	public void exportDetail(PublicationBean aPub, OutputStream out)
		throws DocumentException;

//	public int getNumberOfPublicDocuments() throws DocumentException;

	public Publication[] findPublicationsByParticleSampleId(String particleId)
		throws DocumentException;
}
