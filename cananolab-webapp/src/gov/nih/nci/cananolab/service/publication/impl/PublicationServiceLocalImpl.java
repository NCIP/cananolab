package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PubMedXMLHandler;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Local implementation of PublicationService
 *
 * @author tanq, pansu, lethai
 *
 */
public class PublicationServiceLocalImpl extends BaseServiceLocalImpl implements
		PublicationService {
	private static Logger logger = Logger
			.getLogger(PublicationServiceLocalImpl.class);
	private PublicationServiceHelper helper;
	private SampleServiceHelper sampleHelper;

	public PublicationServiceLocalImpl() {
		super();
		helper = new PublicationServiceHelper(this.securityService);
		sampleHelper = new SampleServiceHelper(this.securityService);
	}

	public PublicationServiceLocalImpl(UserBean user) {
		super(user);
		helper = new PublicationServiceHelper(this.securityService);
		sampleHelper = new SampleServiceHelper(this.securityService);
	}

	public PublicationServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new PublicationServiceHelper(this.securityService);
		sampleHelper = new SampleServiceHelper(this.securityService);
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
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			Publication publication = (Publication) publicationBean
					.getDomainFile();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Boolean newPub = true;
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
					newPub = false;
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
					newPub = false;
				}
			}
			fileUtils.prepareSaveFile(publication);
			appService.saveOrUpdate(publication);

			// save default accesses
			if (newPub) {
				super.saveDefaultAccessibilities(publicationBean
						.getDomainFile().getId().toString());
			}
			fileUtils.writeFile(publicationBean);

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
				if (!StringUtils.isEmpty(name)) {
					Sample sample = sampleHelper.findSampleByName(name);
					if (sample != null) {
						samplesToRemove.add(sample);
					}
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
				if (!StringUtils.isEmpty(name)) {
					Sample sample = sampleHelper.findSampleByName(name);
					if (sample != null) {
						samplesToAdd.add(sample);
					}
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
					PublicationBean pubBean = this
							.loadPublicationBean(publication);
					pubBean.setSampleNames(sampleNames);
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
			Boolean loadAccessInfo) throws PublicationException,
			NoAccessException {
		return findPublicationByKey("id", new Long(publicationId),
				loadAccessInfo);
	}

	public PublicationBean findPublicationByKey(String keyName,
			Object keyValue, Boolean loadAccessInfo)
			throws PublicationException, NoAccessException {
		PublicationBean publicationBean = null;
		try {
			Publication publication = helper.findPublicationByKey(keyName,
					keyValue);
			if (publication != null) {
				if (loadAccessInfo) {
					publicationBean = loadPublicationBean(publication);
				} else {
					publicationBean = new PublicationBean(publication);
				}
				String[] sampleNames = helper
						.findSampleNamesByPublicationId(publication.getId()
								.toString());
				publicationBean.setSampleNames(sampleNames);
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

	private PublicationBean loadPublicationBean(Publication publication)
			throws Exception {
		if (publication == null) {
			return null;
		}
		PublicationBean publicationBean = new PublicationBean(publication);
		if (user != null) {
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(publication.getId().toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(publication.getId().toString());
			publicationBean.setUserAccesses(userAccesses);
			publicationBean.setGroupAccesses(groupAccesses);
			publicationBean.setUserUpdatable(this.checkUserUpdatable(
					groupAccesses, userAccesses));
			publicationBean.setUserDeletable(this.checkUserDeletable(
					groupAccesses, userAccesses));
			publicationBean.setUserIsOwner(this.checkUserOwner(publicationBean
					.getDomainFile().getCreatedBy()));
		}
		return publicationBean;
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
	 * @throws PublicationException
	 *             , NoAccessException
	 */
	public void removePublicationFromSample(String sampleName,
			Publication publication) throws PublicationException,
			NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample sample = sampleHelper.findSampleByName(sampleName);
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

	public void deletePublication(Publication publication)
			throws PublicationException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
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
		} catch (Exception e) {
			String err = "Error in deleting the publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
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
		PublicationBean publicationBean = null;
		try {
			Publication publication = helper.findNonPubMedNonDOIPublication(
					publicationType, title, firstAuthorLastName,
					firstAuthorFirstName);
			publicationBean = loadPublicationBean(publication);
			if (publication != null) {
				String[] sampleNames = helper
						.findSampleNamesByPublicationId(publication.getId()
								.toString());
				publicationBean.setSampleNames(sampleNames);
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
		return publicationBean;
	}

	public void assignAccessibility(AccessibilityBean access,
			Publication publication) throws PublicationException,
			NoAccessException {
		if (!isUserOwner(publication.getCreatedBy())) {
			throw new NoAccessException();
		}
		try {
			// get existing accessibilities
			List<AccessibilityBean> groupAccesses = this
					.findGroupAccessibilities(publication.getId().toString());
			List<AccessibilityBean> userAccesses = this
					.findUserAccessibilities(publication.getId().toString());
			// do nothing is access already exist
			if (groupAccesses.contains(access)) {
				return;
			} else if (userAccesses.contains(access)) {
				return;
			}

			// if access is Public, remove all other access except Public
			// Curator and owner
			if (access.getGroupName()
					.equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
				for (AccessibilityBean acc : groupAccesses) {
					// remove group accesses that are not public or curator
					if (!acc.getGroupName().equals(
							AccessibilityBean.CSM_PUBLIC_GROUP)
							&& !acc.getGroupName().equals(
									(AccessibilityBean.CSM_DATA_CURATOR))) {
						this.removeAccessibility(acc, publication);
					}
				}
				for (AccessibilityBean acc : userAccesses) {
					// remove accesses that are not owner
					if (!acc.getUserBean().getLoginName().equals(
							publication.getCreatedBy())) {
						this.removeAccessibility(acc, publication);
					}
				}
			}
			// if publication is already public, retract from public
			else {
				if (groupAccesses.contains(AccessibilityBean.CSM_PUBLIC_ACCESS)) {
					this.removeAccessibility(
							AccessibilityBean.CSM_PUBLIC_ACCESS, publication);
				}
			}
			super.saveAccessibility(access, publication.getId().toString());
			// set author accessibility as well because didn't share authors
			// between publications
			if (publication.getAuthorCollection() != null) {
				for (Author author : publication.getAuthorCollection()) {
					if (author != null) {
						super.saveAccessibility(access, author.getId()
								.toString());
					}
				}
			}
		} catch (Exception e) {
			String error = "Error in assigning access to publication";
			throw new PublicationException(error, e);
		}
	}

	public void removeAccessibility(AccessibilityBean access,
			Publication publication) throws PublicationException,
			NoAccessException {
		if (!isUserOwner(publication.getCreatedBy())) {
			throw new NoAccessException();
		}
		try {
			if (publication != null) {
				super.deleteAccessibility(access, publication.getId()
						.toString());
				if (publication.getAuthorCollection() != null) {
					for (Author author : publication.getAuthorCollection()) {
						super.deleteAccessibility(access, author.getId()
								.toString());
					}
				}
			}
		} catch (Exception e) {
			String error = "Error in assigning access to publication";
			throw new PublicationException(error, e);
		}
	}

	public PublicationServiceHelper getHelper() {
		return helper;
	}

	public SampleServiceHelper getSampleHelper() {
		return sampleHelper;
	}

	public Map<String, String> findPublicationsByOwner(String currentOwner)
			throws Exception {

		Map<String, String> publications = new HashMap<String, String>();
		Publication p = new Publication();

		DetachedCriteria crit = DetachedCriteria.forClass(Publication.class)
				.setProjection(
						Projections.projectionList().add(
								Projections.property("id")).add(
								Projections.property("title")));
		crit.add(Restrictions.eq("createdBy", currentOwner));
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			Object[] row = (Object[]) obj;
			String id = row[0].toString();
			String title = row[1].toString();
			publications.put(id, title);
		}
		return publications;
	}

	public void transferOwner(Set<String> publicationIds, String currentOwner,
			String newOwner) throws Exception {
		if (!this.securityService.getUserBean().isCurator()) {
			throw new NoAccessException();
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		for (String publicationId : publicationIds) {
			Publication publication = helper.findPublicationById(publicationId);
			publication.setCreatedBy(newOwner);
			appService.saveOrUpdate(publication);
		}
	}

}
