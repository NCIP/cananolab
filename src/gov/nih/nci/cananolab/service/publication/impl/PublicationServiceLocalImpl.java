package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.common.FileService;
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
			throws PublicationException, NoAccessException,
			DuplicateEntriesException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			Publication publication = (Publication) publicationBean.getDomainFile();
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(publication, user);
			CustomizedApplicationService appService =
				(CustomizedApplicationService) ApplicationServiceProvider.getApplicationService();
			// check if publication is already entered based on PubMedId or DOI
			if (publication.getPubMedId() != null && publication.getPubMedId() != 0) {
				Publication dbPublication = (Publication) appService.getObject(
						Publication.class, "pubMedId", publication
								.getPubMedId());
				if (dbPublication != null
						&& !dbPublication.getId().equals(publication.getId())) {
					throw new DuplicateEntriesException(
							"PubMed ID is already used");
				}
			}
			if (!StringUtils.isEmpty(publication.getDigitalObjectId())) {
				Publication dbPublication = (Publication) appService.getObject(
						Publication.class, "digitalObjectId", publication
								.getDigitalObjectId());
				if (dbPublication != null
						&& !dbPublication.getId().equals(publication.getId())) {
					throw new DuplicateEntriesException(
							"Digital Object ID is already used");
				}
			}
			appService.saveOrUpdate(publication);
			fileService.writeFile(publicationBean, user);

			// if has associated sample, save sample to update the relationship
			// between sample and publication
			if (publicationBean.getSampleNames() != null
					&& publicationBean.getSampleNames().length > 0) {

				SampleService sampleService = new SampleServiceLocalImpl();
				Set<Sample> samples = new HashSet<Sample>();
				String[] sampleNames = publicationBean.getSampleNames();
				if (sampleNames != null && sampleNames.length > 0) {
					for (String name : sampleNames) {
						SampleBean sampleBean = sampleService.findSampleByName(
								name, user);
						samples.add(sampleBean.getDomain());
					}
				}
				for (Sample sample : samples) {
					sample.getPublicationCollection().add(publication);
					appService.saveOrUpdate(sample);
				}
			}

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
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in saving the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
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

	// retrieve publication visibility
	private void retrieveVisibility(PublicationBean publicationBean,
			UserBean user) throws FileException {
		try {
			if (publicationBean != null) {
				// get assigned visible groups
				List<String> accessibleGroups = helper.getAuthService()
						.getAccessibleGroups(
								publicationBean.getDomainFile().getId()
										.toString(),
								Constants.CSM_READ_PRIVILEGE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				publicationBean.setVisibilityGroups(visibilityGroups);
			}
		} catch (Exception e) {
			String err = "Error in setting file visibility for "
					+ publicationBean.getDisplayName();
			logger.error(err, e);
			throw new FileException(err, e);
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
						retrieveVisibility(pubBean, user);
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
		try {
			Publication publication = helper.findPublicationById(publicationId,
					user);
			String[] sampleNames = helper.findSampleNamesByPublicationId(
					publicationId, user);
			PublicationBean publicationBean = new PublicationBean(publication,
					sampleNames);
			if (user != null)
				retrieveVisibility(publicationBean, user);
			return publicationBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the publication by id: "
					+ publicationId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public Publication findPublicationByKey(String keyName, Object keyValue,
			UserBean user) throws PublicationException, NoAccessException {
		Publication publication = null;
		CustomizedApplicationService appService = null;
		try {
			appService = (CustomizedApplicationService) ApplicationServiceProvider.getApplicationService();
			publication = (Publication) appService.getObject(
					Publication.class, keyName, keyValue);
		} catch (Exception e) {
			String err = "Error finding the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
		return publication;
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
	 * @throws PublicationException, NoAccessException
	 */
	public void removePublicationFromSample(String sampleId, PublicationBean pubBean, UserBean user)
			throws PublicationException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService =
				(CustomizedApplicationService) ApplicationServiceProvider.getApplicationService();

			SampleService sampleService = new SampleServiceLocalImpl();
			SampleBean sampleBean = sampleService.findSampleById(sampleId, user);
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
