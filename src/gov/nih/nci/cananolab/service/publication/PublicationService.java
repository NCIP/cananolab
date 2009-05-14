package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.exception.SecurityException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * Interface defining methods invovled in submiting and searching publications.
 *
 * @author tanq
 *
 */
public interface PublicationService {

	/**
	 * Persist a new publication or update an existing publication
	 *
	 * @param publication
	 * @param sampleNames
	 * @param fileData
	 * @param authors
	 *
	 * @throws Exception
	 */
	public void savePublication(Publication publication,
			String[] sampleNames, byte[] fileData, Collection<Author> authors)
			throws PublicationException;

	public List<PublicationBean> findPublicationsBy(String publicationTitle,
			String publicationCategory, String sampleName,
			String[] researchArea, String keywordsStr, String pubMedId,
			String digitalObjectId, String authorsStr,
			String[] nanomaterialEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException, SecurityException;

	public PublicationBean findPublicationById(String publicationId)
			throws PublicationException;

	public Publication findDomainPublicationById(String publicationId)
			throws PublicationException;

	public void exportDetail(PublicationBean aPub, OutputStream out)
			throws PublicationException;

	public List<PublicationBean> findPublicationsBySampleId(
			String sampleId) throws PublicationException;

	public int getNumberOfPublicPublications() throws PublicationException;

	public void removePublicationFromSample(Sample particle,
			Long dataId) throws PublicationException;

	public void exportSummary(PublicationSummaryViewBean summaryBean, OutputStream out)
			throws IOException;
}
