package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;

/**
 * Interface defining methods invovled in submiting and searching publications.
 *
 * @author tanq
 *
 */
public interface PublicationService extends BaseService {

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
	public void savePublication(PublicationBean publicationBean)
			throws PublicationException, NoAccessException;

	public PublicationBean findPublicationById(String publicationId)
			throws PublicationException, NoAccessException;

	public PublicationBean findPublicationByKey(String keyName, Object keyValue)
			throws PublicationException, NoAccessException;

	public List<PublicationBean> findPublicationsBySampleId(String sampleId)
			throws PublicationException;

	public int getNumberOfPublicPublications() throws PublicationException;

	public List<String> findPublicationIdsBy(String title, String category,
			String sampleName, String[] researchAreas, String[] keywords,
			String pubMedId, String digitalObjectId, String[] authors,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException;

	public void deletePublication(Publication publication)
			throws PublicationException, NoAccessException;

	/**
	 * Parse PubMed XML file and store the information into a PublicationBean
	 *
	 * @param pubMedId
	 * @return
	 * @throws PublicationException
	 */
	public PublicationBean getPublicationFromPubMedXML(String pubMedId)
			throws PublicationException;

	public PublicationBean findNonPubMedNonDOIPublication(
			String publicationType, String title, String firstAuthorLastName,
			String firstAuthorFirstName) throws PublicationException;

	public void removePublicationFromSample(String sampleName,
			Publication publication) throws PublicationException,
			NoAccessException;

	public void assignAccessibility(AccessibilityBean access,
			Publication publication) throws PublicationException,
			NoAccessException;

	public void removeAccessibility(AccessibilityBean access,
			Publication publication) throws PublicationException,
			NoAccessException;
}
