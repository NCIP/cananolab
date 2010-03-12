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
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
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
	private PublicationServiceHelper helper = new PublicationServiceHelper();
	private FileServiceHelper fileHelper = new FileServiceHelper();

	/**
	 * Persist a new publication or update an existing publication
	 * 
	 * @param publication
	 * @param sampleNames
	 * @param fileData
	 * @param authors
	 * @throws Exception
	 */
	public void savePublication(PublicationBean publicationBean, UserBean user)
			throws PublicationException, NoAccessException {
		if (user == null || !user.isCurator()) {
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
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(publication, user);
			appService.saveOrUpdate(publication);
			fileService.writeFile(publicationBean, user);

			// update sample associations
			updateSampleAssociation(appService, publicationBean, user);

			// set visibility
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			authService.assignVisibility(publicationBean.getDomainFile()
					.getId().toString(), publicationBean.getVisibilityGroups(),
					null);
			// set author visibility as well
			if (publication.getAuthorCollection() != null) {
				for (Author author : publication.getAuthorCollection()) {
					if (author != null) {
						authService.assignVisibility(author.getId().toString(),
								publicationBean.getVisibilityGroups(), null);
					}
				}
			}
		} catch (Exception e) {
			String err = "Error in saving the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	private void updateSampleAssociation(
			CustomizedApplicationService appService,
			PublicationBean publicationBean, UserBean user) throws Exception {
		Publication publication = (Publication) publicationBean.getDomainFile();
		// if has associated sample, save sample to update the relationship
		// between sample and publication
		String[] newAssociatedSamples = publicationBean.getSampleNames();
		SampleService sampleService = new SampleServiceLocalImpl();

		String[] existingAssociatedSamples = helper
				.findSampleNamesByPublicationId(publication.getId().toString(),
						user);
		// find existing associated samples, remove publications from samples
		// that are no longer associated with the publication
		Set<String> sampleNamesToRemove = new HashSet<String>(Arrays
				.asList(existingAssociatedSamples));
		sampleNamesToRemove.removeAll(Arrays.asList(newAssociatedSamples));

		// find newly associated samples, add publications to these samples
		Set<String> sampeNamesToAdd = new HashSet<String>(Arrays
				.asList(newAssociatedSamples));
		sampeNamesToAdd.removeAll(Arrays.asList(existingAssociatedSamples));

		if (sampleNamesToRemove != null && sampleNamesToRemove.size() > 0) {
			Set<Sample> samplesToRemove = new HashSet<Sample>();
			for (String name : sampleNamesToRemove) {
				SampleBean sampleBean = sampleService.findSampleByName(name,
						user);
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
				SampleBean sampleBean = sampleService.findSampleByName(name,
						user);
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
			String[] functionClassNames, String[] otherFunctionTypes,
			UserBean user) throws PublicationException {
		try {
			return helper.findPublicationIdsBy(title, category, sampleName,
					researchAreas, keywords, pubMedId, digitalObjectId,
					authors, nanomaterialEntityClassNames,
					otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, user);
		} catch (Exception e) {
			String err = "Problem finding publication info.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsBySampleId(String sampleId,
			UserBean user) throws PublicationException {
		try {
			List<Publication> publications = helper.findPublicationsBySampleId(
					sampleId, user);
			List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
			if (publications != null) {
				SampleService sampleService = new SampleServiceLocalImpl();
				for (Publication publication : publications) {
					// retrieve sampleNames
					String[] sampleNames = helper
							.findSampleNamesByPublicationId(publication.getId()
									.toString(), user);
					PublicationBean pubBean = new PublicationBean(publication,
							sampleNames);
					// retrieve visibility
					if (user != null)
						fileHelper.retrieveVisibility(pubBean);
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

	public PublicationBean findPublicationById(String publicationId,
			UserBean user) throws PublicationException, NoAccessException {
		return findPublicationByKey("id", new Long(publicationId), user);
	}

	public PublicationBean findPublicationByKey(String keyName,
			Object keyValue, UserBean user) throws PublicationException,
			NoAccessException {
		PublicationBean publicationBean = null;
		try {
			Publication publication = helper.findPublicationByKey(keyName,
					keyValue, user);
			if (publication != null) {
				String[] sampleNames = helper.findSampleNamesByPublicationId(
						publication.getId().toString(), user);
				publicationBean = new PublicationBean(publication, sampleNames);
				if (user != null)
					fileHelper.retrieveVisibility(publicationBean);
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
			String[] functionClassNames, String[] otherFunctionTypes,
			UserBean user) throws PublicationException {
		try {
			return helper.findPublicationsBy(title, category, sampleName,
					researchAreas, keywords, pubMedId, digitalObjectId,
					authors, nanomaterialEntityClassNames,
					otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, user);
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
	 * @param pubBean
	 * @param user
	 * @throws PublicationException
	 *             , NoAccessException
	 */
	public void removePublicationFromSample(String sampleId,
			PublicationBean pubBean, UserBean user)
			throws PublicationException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			SampleService sampleService = new SampleServiceLocalImpl();
			SampleBean sampleBean = sampleService
					.findSampleById(sampleId, user);
			Sample sample = sampleBean.getDomain();
			Collection<Publication> pubs = sample.getPublicationCollection();
			if (pubs != null && pubs.size() > 0) {
				for (Publication pub : pubs) {
					if (pub.getId().equals(pubBean.getDomainFile().getId())) {
						pubs.remove(pubBean.getDomainFile());
						break;
					}
				}
				appService.saveOrUpdate(sample);
			}
		} catch (Exception e) {
			String err = "Error in removing the sample publication association.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}
}
