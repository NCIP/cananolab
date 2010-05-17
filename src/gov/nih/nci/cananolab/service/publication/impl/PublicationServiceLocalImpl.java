package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PubMedXMLHandler;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Local implementation of PublicationService
 *
 * @author tanq, pansu
 *
 */
public class PublicationServiceLocalImpl implements PublicationService {
	private static Logger logger = Logger
			.getLogger(PublicationServiceLocalImpl.class);
	private PublicationServiceHelper helper;
	private FileService fileService;
	private SampleService sampleService;

	public PublicationServiceLocalImpl() {
		helper = new PublicationServiceHelper();
		sampleService = new SampleServiceLocalImpl();
		fileService = new FileServiceLocalImpl();
	}

	public PublicationServiceLocalImpl(UserBean user) {
		helper = new PublicationServiceHelper(user);
	}

	/**
	 * Persist a new publication or update an existing publication
	 *
	 * @param publication
	 * @param sampleNames
	 * @param fileData
	 * @param authors
	 * @throws Exception
	 */
	public void savePublication(PublicationBean publicationBean)
			throws PublicationException, NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		try {
			Publication publication = (Publication) publicationBean
					.getDomainFile();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// check if publication is already entered based on PubMedId or DOI
			if (publication.getPubMedId() != null
					&& publication.getPubMedId() != 0) {
				Publication dbPublication = (Publication) appService.getObject(
						Publication.class, "pubMedId", publication
								.getPubMedId());
				if (dbPublication != null
						&& !dbPublication.getId().equals(publication.getId())) {
					// throw new DuplicateEntriesException(
					// "PubMed id is already used");
					// duplicate pubMed ID, update new pub with old pub ID
					logger.info("PubMed ID " + publication.getPubMedId()
							+ " is already in use.  Resuse the database entry");
					publication.setId(dbPublication.getId());
				}
			}
			if (!StringUtils.isEmpty(publication.getDigitalObjectId())) {
				Publication dbPublication = (Publication) appService.getObject(
						Publication.class, "digitalObjectId", publication
								.getDigitalObjectId());
				if (dbPublication != null
						&& !dbPublication.getId().equals(publication.getId())) {
					// throw new DuplicateEntriesException(
					// "Digital Object ID is already used");
					logger.info("DOI " + publication.getDigitalObjectId()
							+ " is already in use.  Resuse the database entry");
					publication.setId(dbPublication.getId());
				}
			}
			fileService.prepareSaveFile(publication);
			appService.saveOrUpdate(publication);
			fileService.writeFile(publicationBean);

			// set visibility
			assignVisibility(publication, publicationBean.getVisibilityGroups());

			// update sample associations
			updateSampleAssociation(appService, publicationBean);
		} catch (Exception e) {
			String err = "Error in saving the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	private void updateSampleAssociation(
			CustomizedApplicationService appService,
			PublicationBean publicationBean) throws Exception {
		Publication publication = (Publication) publicationBean.getDomainFile();
		// if has associated sample, save sample to update the relationship
		// between sample and publication
		String[] newAssociatedSamples = publicationBean.getSampleNames();
		String[] existingAssociatedSamples = helper
				.findSampleNamesByPublicationId(publication.getId().toString());
		// when saving from publication from, find existing associated samples,
		// remove publications from samples that are no longer associated
		// with the publication
		Set<String> sampleNamesToRemove = new HashSet<String>(Arrays
				.asList(existingAssociatedSamples));
		sampleNamesToRemove.removeAll(Arrays.asList(newAssociatedSamples));

		// find newly associated samples, add publications to these samples
		Set<String> sampeNamesToAdd = new HashSet<String>(Arrays
				.asList(newAssociatedSamples));
		sampeNamesToAdd.removeAll(Arrays.asList(existingAssociatedSamples));

		// only remove unassociated samples if saving from publication form
		if (!publicationBean.getFromSamplePage() && sampleNamesToRemove != null
				&& sampleNamesToRemove.size() > 0) {
			Set<Sample> samplesToRemove = new HashSet<Sample>();
			for (String name : sampleNamesToRemove) {
				SampleBean sampleBean = sampleService.findSampleByName(name);
				if (sampleBean != null) {
					samplesToRemove.add(sampleBean.getDomain());
				}
			}
			for (Sample sample : samplesToRemove) {
				sample.getPublicationCollection().remove(publication);
				appService.saveOrUpdate(sample);
			}
		}

		Set<Sample> samplesToAdd = new HashSet<Sample>();
		if (sampeNamesToAdd != null && sampeNamesToAdd.size() > 0) {
			for (String name : sampeNamesToAdd) {
				SampleBean sampleBean = sampleService.findSampleByName(name);
				if (sampleBean != null) {
					samplesToAdd.add(sampleBean.getDomain());
				}
			}
		}
		for (Sample sample : samplesToAdd) {
			sample.getPublicationCollection().add(publication);
			appService.saveOrUpdate(sample);
		}
	}

	public List<String> findPublicationIdsBy(String title, String category,
			String sampleName, String[] researchAreas, String[] keywords,
			String pubMedId, String digitalObjectId, String[] authors,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException {
		try {
			return helper.findPublicationIdsBy(title, category, sampleName,
					researchAreas, keywords, pubMedId, digitalObjectId,
					authors, nanomaterialEntityClassNames,
					otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes);
		} catch (Exception e) {
			String err = "Problem finding publication info.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsBySampleId(String sampleId)
			throws PublicationException {
		try {
			List<Publication> publications = helper
					.findPublicationsBySampleId(sampleId);
			List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
			if (publications != null) {
				for (Publication publication : publications) {
					// retrieve sampleNames
					String[] sampleNames = helper
							.findSampleNamesByPublicationId(publication.getId()
									.toString());
					PublicationBean pubBean = new PublicationBean(publication,
							sampleNames);
					// retrieve visibility
					if (helper.getUser() != null)
						pubBean.setVisibilityGroups(helper.getAuthService()
								.getAccessibleGroups(
										publication.getId().toString(),
										Constants.CSM_READ_PRIVILEGE));
					publicationBeans.add(pubBean);
				}
			}
			return publicationBeans;
		} catch (Exception e) {
			String err = "Problem finding publication collections with the given sample ID.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public PublicationBean findPublicationById(String publicationId)
			throws PublicationException, NoAccessException {
		return findPublicationByKey("id", new Long(publicationId));
	}

	public PublicationBean findPublicationByKey(String keyName, Object keyValue)
			throws PublicationException, NoAccessException {
		PublicationBean publicationBean = null;
		try {
			Publication publication = helper.findPublicationByKey(keyName,
					keyValue);
			if (publication != null) {
				String[] sampleNames = helper
						.findSampleNamesByPublicationId(publication.getId()
								.toString());
				publicationBean = new PublicationBean(publication, sampleNames);
				if (helper.getUser() != null)
					publicationBean.setVisibilityGroups(helper.getAuthService()
							.getAccessibleGroups(
									publication.getId().toString(),
									Constants.CSM_READ_PRIVILEGE));
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the publication by key: " + keyName;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
		return publicationBean;
	}

	public List<Publication> findPublicationsBy(String title, String category,
			String sampleName, String[] researchAreas, String[] keywords,
			String pubMedId, String digitalObjectId, String[] authors,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException {
		try {
			return helper.findPublicationsBy(title, category, sampleName,
					researchAreas, keywords, pubMedId, digitalObjectId,
					authors, nanomaterialEntityClassNames,
					otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes);
		} catch (Exception e) {
			String err = "Problem finding publication info.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public int getNumberOfPublicPublications() throws PublicationException {
		try {
			int count = helper.getNumberOfPublicPublications();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	/**
	 * Remove sample-publication association for an existing publication.
	 *
	 * @param sampleId
	 * @param publication
	 * @param user
	 * @throws PublicationException ,
	 *             NoAccessException
	 */
	public void removePublicationFromSample(String sampleName,
			Publication publication) throws PublicationException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			SampleBean sampleBean = sampleService.findSampleByName(sampleName);
			Sample sample = sampleBean.getDomain();
			Collection<Publication> pubs = sample.getPublicationCollection();
			if (pubs != null) {
				pubs.remove(publication);
			}
			appService.saveOrUpdate(sample);
		} catch (Exception e) {
			String err = "Error removing publication from the sample";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<String> deletePublication(Publication publication,
			Boolean removeVisibility) throws PublicationException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// assume publication is loaded with authors
			// delete authors since authors were not shared across publications
			if (publication.getAuthorCollection() != null) {
				for (Author author : publication.getAuthorCollection()) {
					appService.delete(author);
				}
			}
			publication.setAuthorCollection(null);

			// find associated samples and remove publication association
			String[] sampleNames = helper
					.findSampleNamesByPublicationId(publication.getId()
							.toString());
			for (String name : sampleNames) {
				removePublicationFromSample(name, publication);
			}
			appService.delete(publication);
			entries.addAll(removeVisibility(publication, removeVisibility));
		} catch (Exception e) {
			String err = "Error in deleting the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
		return entries;
	}

	public PublicationBean getPublicationFromPubMedXML(String pubMedId)
			throws PublicationException {
		PublicationBean newPubBean = null;
		try {
			Long pubMedIDLong = Long.valueOf(pubMedId.trim());
			PubMedXMLHandler phandler = PubMedXMLHandler.getInstance();
			newPubBean = new PublicationBean();
			if (phandler.parsePubMedXML(pubMedIDLong, newPubBean)) {
				Publication newPub = (Publication) newPubBean.getDomainFile();
				newPub.setPubMedId(pubMedIDLong);
			}
		} catch (Exception e) {
			String err = "Invalid PubMed ID: " + pubMedId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
		return newPubBean;
	}

	public PublicationBean findNonPubMedNonDOIPublication(
			String publicationType, String title, String firstAuthorLastName,
			String firstAuthorFirstName) throws PublicationException {
		PublicationBean pubBean = null;
		try {
			Publication publication = helper.findNonPubMedNonDOIPublication(
					publicationType, title, firstAuthorLastName,
					firstAuthorFirstName);
			if (publication != null) {
				String[] sampleNames = helper
						.findSampleNamesByPublicationId(publication.getId()
								.toString());
				pubBean = new PublicationBean(publication, sampleNames);
				if (helper.getUser() != null)
					pubBean.setVisibilityGroups(helper.getAuthService()
							.getAccessibleGroups(
									publication.getId().toString(),
									Constants.CSM_READ_PRIVILEGE));
			}
		} catch (Exception e) {
			String err = "trouble finding non PubMed/DOI publication based on type: "
					+ publicationType
					+ ", title: "
					+ title
					+ ", and first author: "
					+ firstAuthorFirstName
					+ " "
					+ firstAuthorLastName;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
		return pubBean;
	}

	private void assignVisibility(Publication publication,
			String[] visibleGroups) throws Exception {
		helper.getAuthService().assignVisibility(
				publication.getId().toString(), visibleGroups, null);
		// set author visibility as well because didn't share authors
		// between publications
		if (publication.getAuthorCollection() != null) {
			for (Author author : publication.getAuthorCollection()) {
				if (author != null) {
					helper.getAuthService().assignVisibility(
							author.getId().toString(), visibleGroups, null);
				}
			}
		}
	}

	private List<String> removeVisibility(Publication publication,
			Boolean remove) throws Exception {
		List<String> entries = new ArrayList<String>();
		if (publication != null) {
			if (remove == null || remove)
				helper.getAuthService().removeCSMEntry(
						publication.getId().toString());
			entries.add(publication.getId().toString());
			if (publication.getAuthorCollection() != null) {
				for (Author author : publication.getAuthorCollection()) {
					if (remove == null || remove)
						helper.getAuthService().removeCSMEntry(
								author.getId().toString());
					entries.add(author.getId().toString());
				}
			}
		}
		return entries;
	}

	public PublicationServiceHelper getHelper() {
		return helper;
	}

	public SampleService getSampleService() {
		return sampleService;
	}
}
