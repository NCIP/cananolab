package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

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
	 *            particleNames, fileData, authors
	 * @throws Exception
	 */
	public void savePublication(Publication publication,
			String[] particleNames, byte[] fileData, Collection<Author> authors)
			throws PublicationException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(publication);
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			Set<NanoparticleSample> particleSamples = new HashSet<NanoparticleSample>();
			if (particleNames != null && particleNames.length > 0) {
				for (String name : particleNames) {
					NanoparticleSample sample = sampleService
							.findNanoparticleSampleByName(name);
					particleSamples.add(sample);
				}
			}

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// TODO fix dependency on sample
			/*
			 * if (publication.getNanoparticleSampleCollection() == null) {
			 * publication .setNanoparticleSampleCollection(new HashSet<NanoparticleSample>()); }
			 * else { publication.getNanoparticleSampleCollection().clear(); }
			 * for (NanoparticleSample sample : particleSamples) {
			 * publication.getNanoparticleSampleCollection().add(sample);
			 * sample.getPublicationCollection().add(publication); }
			 */
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
			String category, String nanoparticleName, String[] researchArea,
			String keywordsStr, String pubMedId, String digitalObjectId,
			String authorsStr, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes)
			throws PublicationException, CaNanoLabSecurityException {
		List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
		try {
			List<Publication> publications = helper.findPublicationsBy(title,
					category, nanoparticleName, researchArea, keywordsStr,
					pubMedId, digitalObjectId, authorsStr,
					nanoparticleEntityClassNames, otherNanoparticleTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes);
			if (publications != null) {
				for (Publication publication : publications) {
					publicationBeans.add(new PublicationBean(publication, true,
							false));
				}
			}
			Collections
					.sort(
							publicationBeans,
							new CaNanoLabComparators.PublicationBeanCategoryTitleComparator());
			return publicationBeans;

		} catch (Exception e) {
			String err = "Problem finding publication info.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsByParticleSampleId(
			String particleId, boolean loadParticle, boolean loadAuthor)
			throws PublicationException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			String query = "select publicationCollection publication left join fetch publication.authorCollection from NanoparticleSample where id="
					+ particleId;
			DetachedCriteria crit = DetachedCriteria
					.forClass(Publication.class);
			crit.createAlias("nanoparticleSampleCollection", "sample",
					CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("sample.id", new Long(particleId)));
			if (loadAuthor) {
				crit.setFetchMode("authorCollection", FetchMode.JOIN);
			}
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			List<PublicationBean> publicationCollection = new ArrayList<PublicationBean>();
			for (Object obj : results) {
				Publication publication = (Publication) obj;
				if (loadAuthor) {
					publicationCollection.add(new PublicationBean(publication,
							false, true));
				} else {
					publicationCollection.add(new PublicationBean(publication));
				}
			}
			return publicationCollection;
		} catch (Exception e) {
			String err = "Problem finding publication collections with the given particle ID.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsByParticleSampleId(
			String particleId) throws PublicationException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// HQLCriteria crit = new HQLCriteria(
			// "select publicationCollection publication left join fetch
			// publication.authorCollection left join fetch
			// publication.keywordCollection from NanoparticleSample where id="
			// + particleId);
			HQLCriteria crit = new HQLCriteria(
					"select publicationCollection from gov.nih.nci.cananolab.domain.particle.NanoparticleSample sample where sample.id="
							+ particleId);
			List results = appService.query(crit);
			List<PublicationBean> publicationCollection = new ArrayList<PublicationBean>();
			for (Object obj : results) {
				Publication publication = (Publication) obj;
				publication.getAuthorCollection();
				publication.getKeywordCollection();
				publicationCollection.add(new PublicationBean(
						(Publication) obj, false, true));
			}
			Collections
					.sort(
							publicationCollection,
							new CaNanoLabComparators.PublicationBeanCategoryTitleComparator());
			return publicationCollection;
		} catch (Exception e) {
			String err = "Problem finding publication collections with the given particle ID.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public PublicationBean findPublicationById(String publcationId)
			throws PublicationException {
		try {
			Publication publication = helper.findPublicationById(publcationId);
			PublicationBean publicationBean = new PublicationBean(publication,
					false, true);
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

	// public Publication[] findDocumentsByParticleSampleId(String particleId)
	// throws DocumentException {
	// throw new DocumentException("Not implemented for local search");
	// }

	public void exportSummary(ParticleBean particleBean, OutputStream out)
			throws IOException {
		PublicationServiceHelper helper = new PublicationServiceHelper();
		helper.exportSummary(particleBean, out);
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
	public void removePublicationFromParticle(NanoparticleSample particle,
			Long dataId) throws PublicationException {
		try {
			PublicationService publicationService = new PublicationServiceLocalImpl();
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Object publicationObject = appService.getObject(Publication.class,
					"id", dataId);
			// TODO fix dependency on sample
			/*
			 * if (publicationObject != null) { Publication publication =
			 * publicationService .findDomainPublicationById(dataId.toString());
			 * Collection<NanoparticleSample> nanoparticleSampleCollection =
			 * publication .getNanoparticleSampleCollection(); if
			 * (nanoparticleSampleCollection == null ||
			 * nanoparticleSampleCollection.size() == 0) { // something wrong
			 * throw new PublicationException(); } else if
			 * (nanoparticleSampleCollection.size() == 1) { // delete
			 * authService.removePublicGroup(dataId.toString()); if
			 * (publication.getAuthorCollection() != null) { for (Author author :
			 * publication.getAuthorCollection()) {
			 * authService.removePublicGroup(author.getId() .toString()); } }
			 * appService.delete(publication); } else {// size>1 // remove
			 * nanoparticleSample association
			 * nanoparticleSampleCollection.remove(particle);
			 * appService.saveOrUpdate(publication); } }
			 */
		} catch (Exception e) {
			String err = "Error deleting publication by ID " + dataId;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}

	}
}
