package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.exception.SecurityException;
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Local implementation of PublicationService
 *
 * @author tanq
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
	public void savePublication(Publication publication,
			String[] sampleNames, byte[] fileData, Collection<Author> authors)
			throws PublicationException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(publication);
			SampleService sampleService = new SampleServiceLocalImpl();
			Set<Sample> particleSamples = new HashSet<Sample>();
			if (sampleNames != null && sampleNames.length > 0) {
				for (String name : sampleNames) {
					Sample sample = sampleService
							.findSampleByName(name);
					particleSamples.add(sample);
				}
			}

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			for (Sample sample : particleSamples) {
				sample.getPublicationCollection().add(publication);
				appService.saveOrUpdate(sample);
			}

			if (publication.getAuthorCollection() == null) {
				publication.setAuthorCollection(new HashSet<Author>());
			} else {
				publication.getAuthorCollection().clear();
			}
			if (authors != null) {
				Calendar myCal = Calendar.getInstance();
				for (Author author : authors) {
					if (!StringUtils.isBlank(author.getFirstName())
							|| !StringUtils.isBlank(author.getLastName())
							|| !StringUtils.isBlank(author.getInitial())) {
						if (author.getCreatedDate() == null) {
							myCal.add(Calendar.SECOND, 1);
							author.setCreatedDate(myCal.getTime());
						}
						publication.getAuthorCollection().add(author);
					}
				}
			}
			appService.saveOrUpdate(publication);
			fileService.writeFile(publication, fileData);

		} catch (Exception e) {
			String err = "Error in saving the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsBy(String title,
			String category, String sampleName, String[] researchArea,
			String keywordsStr, String pubMedId, String digitalObjectId,
			String authorsStr, String[] nanomaterialEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException, SecurityException {
		List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
		try {
			List<Publication> publications = helper.findPublicationsBy(title,
					category, sampleName, researchArea, keywordsStr,
					pubMedId, digitalObjectId, authorsStr,
					nanomaterialEntityClassNames, otherNanoparticleTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes);
			if (publications != null) {
				SampleService sampleService = new SampleServiceLocalImpl();
				for (Publication publication : publications) {
					// retrieve sampleNames
					SortedSet<String> sampleNames = sampleService
							.findSampleNamesByPublicationId(publication
									.getId().toString());
					PublicationBean pubBean = new PublicationBean(publication,
							sampleNames.toArray(new String[sampleNames
									.size()]));
					publicationBeans.add(pubBean);
				}
			}
			Collections
					.sort(
							publicationBeans,
							new Comparators.PublicationBeanCategoryTitleComparator());
			return publicationBeans;

		} catch (Exception e) {
			String err = "Problem finding publication info.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsBySampleId(
			String sampleId) throws PublicationException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					Sample.class).add(
					Property.forName("id").eq(new Long(sampleId)));
			crit.setFetchMode("publicationCollection", FetchMode.JOIN);
			crit.setFetchMode("publicationCollection.authorCollection",
					FetchMode.JOIN);
			crit.setFetchMode("publicationCollection.keywordCollection",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			List<PublicationBean> publications = new ArrayList<PublicationBean>();
			for (Object obj : results) {
				Sample sample = (Sample) obj;
				for (Publication pub : sample.getPublicationCollection()) {
					publications.add(new PublicationBean(pub));
				}
			}
			Collections
					.sort(
							publications,
							new Comparators.PublicationBeanCategoryTitleComparator());
			return publications;
		} catch (Exception e) {
			String err = "Problem finding publication collections with the given sample ID.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public PublicationBean findPublicationById(String publcationId)
			throws PublicationException {
		try {
			Publication publication = helper.findPublicationById(publcationId);
			PublicationBean publicationBean = new PublicationBean(publication);
			return publicationBean;
		} catch (Exception e) {
			String err = "Problem finding the publcation by id: "
					+ publcationId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public Publication findDomainPublicationById(String publcationId)
			throws PublicationException {
		try {
			Publication publication = helper.findPublicationById(publcationId);
			return publication;
		} catch (Exception e) {
			String err = "Problem finding the publcation by id: "
					+ publcationId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public void exportDetail(PublicationBean aPub, OutputStream out)
			throws PublicationException {
		try {
			PublicationServiceHelper helper = new PublicationServiceHelper();
			helper.exportDetail(aPub, out);
		} catch (Exception e) {
			String err = "error exporting detail view for "
					+ aPub.getDomainFile().getTitle();
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	// public int getNumberOfPublicPublcations() throws PublicationException {
	// try {
	// int count = helper.getNumberOfPublicPublications();
	// return count;
	// } catch (Exception e) {
	// String err = "Error finding counts of public publications.";
	// logger.error(err, e);
	// throw new PublicationException(err, e);
	// }
	// }

	// public Publication[] findDocumentsBySampleId(String sampleId)
	// throws DocumentException {
	// throw new DocumentException("Not implemented for local search");
	// }

	public void exportSummary(PublicationSummaryViewBean summaryBean, OutputStream out)
			throws PublicationException {
		try {
			helper.exportSummary(summaryBean, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void removePublicationFromSample(Sample particle,
			Long dataId) throws PublicationException {
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
			 * Collection<Sample> sampleCollection =
			 * publication .getSampleCollection(); if
			 * (sampleCollection == null ||
			 * sampleCollection.size() == 0) { // something wrong
			 * throw new PublicationException(); } else if
			 * (sampleCollection.size() == 1) { // delete
			 * authService.removePublicGroup(dataId.toString()); if
			 * (publication.getAuthorCollection() != null) { for (Author author :
			 * publication.getAuthorCollection()) {
			 * authService.removePublicGroup(author.getId() .toString()); } }
			 * appService.delete(publication); } else {// size>1 // remove
			 * sample association
			 * sampleCollection.remove(particle);
			 * appService.saveOrUpdate(publication); } }
			 */
		} catch (Exception e) {
			String err = "Error deleting publication by ID " + dataId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}

	}
}
