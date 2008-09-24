package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.DocumentSummaryBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.exception.ReportException;

import java.io.IOException;
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
			byte[] fileData, Collection<Author> authors) throws PublicationException;

	
	public List<PublicationBean> findPublicationsBy(String reportTitle,
			String reportCategory, String nanoparticleName, 
			String[] researchArea, String keywordsStr,
			String pubMedId, String digitalObjectId, String authorsStr,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException, CaNanoLabSecurityException;

	public PublicationBean findPublicationById(String publicationId) throws PublicationException;

	public Publication findDomainPublicationById(String publicationId) throws PublicationException;

	public void exportDetail(PublicationBean aPub, OutputStream out)
		throws PublicationException;

	public Publication[] findPublicationsByParticleSampleId(String particleId)
		throws PublicationException;
	
	public int getNumberOfPublicPublications() throws PublicationException;

	public void removePublicationFromParticle(NanoparticleSample particle,
			Long dataId) 	throws PublicationException;

	//TODO, not need??
//	public void exportFullSummary(DocumentSummaryBean summaryBean,
//			OutputStream out) throws IOException ;
	//TODO
	public void exportSummary(ParticleBean particleBean,
			OutputStream out) throws IOException ;
}
