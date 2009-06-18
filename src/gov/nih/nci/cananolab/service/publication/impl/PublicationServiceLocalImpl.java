package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
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
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
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
	 * @param publication,
	 * @param sampleNames,
	 * @param fileData,
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
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(publication, user);

			// finding corresponding samples
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

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			for (Sample sample : samples) {
				sample.getPublicationCollection().add(publication);
				appService.saveOrUpdate(sample);
			}

			appService.saveOrUpdate(publication);
			fileService.writeFile(publicationBean, user);

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
						authService.assignPublicVisibility(author.getId()
								.toString());
					}
				}
			}

		} catch (Exception e) {
			String err = "Error in saving the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsBy(String title,
			String category, String sampleName, String[] researchAreas,
			String[] keywords, String pubMedId, String digitalObjectId,
			String[] authors, String[] nanomaterialEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			UserBean user) throws PublicationException {
		List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
		try {
			List<Publication> publications = helper.findPublicationsBy(title,
					category, sampleName, researchAreas, keywords, pubMedId,
					digitalObjectId, authors, nanomaterialEntityClassNames,
					otherNanoparticleTypes, functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, user);
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
			Collections.sort(publicationBeans,
					new Comparators.PublicationBeanCategoryTitleComparator());
			return publicationBeans;

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
			Collections.sort(publicationBeans,
					new Comparators.PublicationBeanCategoryTitleComparator());
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
	 * if publication associates with multiple particle remove the entry from
	 * nanoparticle_sample_publication otherwise, remove publicVisibility and
	 * delete publication
	 */
	public void removePublicationFromSample(Sample particle, Long dataId)
			throws PublicationException, NoAccessException {
		try {
			PublicationService publicationService = new PublicationServiceLocalImpl();
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Object publicationObject = appService.getObject(Publication.class,
					"id", dataId);
			// TODO fix dependency on sample
			/*
			 * if (publicationObject != null) { Publication publication =
			 * publicationService .findDomainPublicationById(dataId.toString());
			 * Collection<Sample> sampleCollection = publication
			 * .getSampleCollection(); if (sampleCollection == null ||
			 * sampleCollection.size() == 0) { // something wrong throw new
			 * PublicationException(); } else if (sampleCollection.size() == 1) { //
			 * delete authService.removePublicGroup(dataId.toString()); if
			 * (publication.getAuthorCollection() != null) { for (Author author :
			 * publication.getAuthorCollection()) {
			 * authService.removePublicGroup(author.getId() .toString()); } }
			 * appService.delete(publication); } else {// size>1 // remove
			 * sample association sampleCollection.remove(particle);
			 * appService.saveOrUpdate(publication); } }
			 */
		} catch (Exception e) {
			String err = "Error deleting publication by ID " + dataId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}
}
