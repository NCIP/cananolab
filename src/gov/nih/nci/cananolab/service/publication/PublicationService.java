package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.exception.SecurityException;

import java.io.OutputStream;
import java.util.List;
import java.util.SortedSet;

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
	public void savePublication(PublicationBean publicationBean, UserBean user)
			throws PublicationException, NoAccessException;

	public List<PublicationBean> findPublicationsBy(String publicationTitle,
			String publicationCategory, String sampleName,
			String[] researchAreas, String[] keywords, String pubMedId,
			String digitalObjectId, String[] authors,
			String[] nanomaterialEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes, UserBean user)
			throws PublicationException;

	public PublicationBean findPublicationById(String publicationId,
			UserBean user) throws PublicationException, NoAccessException;

	public void exportDetail(PublicationBean aPub, OutputStream out)
			throws PublicationException;

	public List<PublicationBean> findPublicationsBySampleId(String sampleId,
			UserBean user) throws PublicationException;

	public int getNumberOfPublicPublications() throws PublicationException;

	public void removePublicationFromSample(Sample particle, Long dataId)
			throws PublicationException, NoAccessException;

	public void exportSummary(PublicationSummaryViewBean summaryBean,
			OutputStream out) throws PublicationException;

	public SortedSet<String> findSampleNamesByPublicationId(
			String publicationId, UserBean user) throws PublicationException;

}
