/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.publication.impl;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.dao.AclDao;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PubMedXMLHandler;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Local implementation of PublicationService
 *
 * @author tanq, pansu, lethai
 *
 */
@Component("publicationService")
public class PublicationServiceLocalImpl extends BaseServiceLocalImpl implements PublicationService
{
	private static Logger logger = Logger.getLogger(PublicationServiceLocalImpl.class);
	
	@Autowired
	private PublicationServiceHelper publicationServiceHelper;
	
	@Autowired
	private SampleServiceHelper sampleServiceHelper;
	
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private AclDao aclDao;

	/**
	 * Persist a new publication or update an existing publication
	 *
	 * @param publication
	 * @param sampleNames
	 * @param fileData
	 * @param authors
	 * @throws Exception
	 */
	public void savePublication(PublicationBean publicationBean) throws PublicationException, NoAccessException
	{
		if (SpringSecurityUtil.getPrincipal() == null) {
			throw new NoAccessException();
		}
		try {
			Publication publication = (Publication) publicationBean.getDomainFile();
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
			Boolean newPub = true;
			if (publication.getId() != null)
				newPub = false;
			// check if publication is already entered based on PubMedId or DOI
			if (publication.getPubMedId() != null && publication.getPubMedId() != 0) {
				Publication dbPublication = (Publication) appService.getObject(
						Publication.class, "pubMedId", publication.getPubMedId());
				if (dbPublication != null && !dbPublication.getId().equals(publication.getId())) {
					// throw new DuplicateEntriesException(
					// "PubMed id is already used");
					// duplicate pubMed ID, update new pub with old pub ID
					logger.info("PubMed ID " + publication.getPubMedId() + " is already in use.  Resuse the database entry");
					publication.setId(dbPublication.getId());
					newPub = false;
				}
			}
			if (!StringUtils.isEmpty(publication.getDigitalObjectId())) {
				Publication dbPublication = (Publication) appService.getObject(
						Publication.class, "digitalObjectId", publication.getDigitalObjectId());
				if (dbPublication != null && !dbPublication.getId().equals(publication.getId())) {
					// throw new DuplicateEntriesException(
					// "Digital Object ID is already used");
					logger.info("DOI " + publication.getDigitalObjectId() + " is already in use.  Resuse the database entry");
					publication.setId(dbPublication.getId());
					newPub = false;
				}
			}
			fileUtils.prepareSaveFile(publication);
			appService.saveOrUpdate(publication);

			// save default accesses
			if (newPub) {
				springSecurityAclService.saveDefaultAccessForNewObject(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz());
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

	private void updateSampleAssociation(CaNanoLabApplicationService appService,
			PublicationBean publicationBean) throws Exception {
		Publication publication = (Publication) publicationBean.getDomainFile();
		// if has associated sample, save sample to update the relationship
		// between sample and publication
		String[] newAssociatedSamples = publicationBean.getSampleNames();
		String[] existingAssociatedSamples = publicationServiceHelper.findSampleNamesByPublicationId(publication.getId().toString());
		// when saving from publication from, find existing associated samples,
		// remove publications from samples that are no longer associated
		// with the publication
		Set<String> sampleNamesToRemove = new HashSet<String>(Arrays.asList(existingAssociatedSamples));
		sampleNamesToRemove.removeAll(Arrays.asList(newAssociatedSamples));

		// find newly associated samples, add publications to these samples
		Set<String> sampeNamesToAdd = new HashSet<String>(Arrays.asList(newAssociatedSamples));
		sampeNamesToAdd.removeAll(Arrays.asList(existingAssociatedSamples));

		// only remove unassociated samples if saving from publication form
		if (!publicationBean.getFromSamplePage() && sampleNamesToRemove != null
				&& sampleNamesToRemove.size() > 0) {
			Set<Sample> samplesToRemove = new HashSet<Sample>();
			for (String name : sampleNamesToRemove) {
				if (!StringUtils.isEmpty(name)) {
					Sample sample = sampleServiceHelper.findSampleByName(name);
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
					Sample sample = sampleServiceHelper.findSampleByName(name);
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
			return publicationServiceHelper.findPublicationIdsBy(title, category, sampleName,
					researchAreas, keywords, pubMedId, digitalObjectId,
					authors, nanomaterialEntityClassNames,
					otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes);
		} catch (Exception e) {
			String err = "No publications were found for the given parameters.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public List<PublicationBean> findPublicationsBySampleId(String sampleId)
			throws PublicationException {
		try {
			List<Publication> publications = publicationServiceHelper.findPublicationsBySampleId(sampleId);
			Collections.sort(publications, new Comparators.PublicationCategoryTitleComparator());
			List<PublicationBean> publicationBeans = new ArrayList<PublicationBean>();
			if (publications != null) {
				for (Publication publication : publications) {
					// retrieve sampleNames
					String[] sampleNames = publicationServiceHelper.findSampleNamesByPublicationId(publication.getId().toString());
					PublicationBean pubBean = this.loadPublicationBean(publication);
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

	public PublicationBean findPublicationById(String publicationId, Boolean loadAccessInfo) throws PublicationException,
			NoAccessException {
		return findPublicationByKey("id", new Long(publicationId), loadAccessInfo);
	}

	public PublicationBean findPublicationByKey(String keyName,
			Object keyValue, Boolean loadAccessInfo)
			throws PublicationException, NoAccessException {
		PublicationBean publicationBean = null;
		try {
			Publication publication = publicationServiceHelper.findPublicationByKey(keyName, keyValue);
			if (publication != null) {
				if (loadAccessInfo) {
					publicationBean = loadPublicationBean(publication);
				} else {
					publicationBean = new PublicationBean(publication);
				}
				String[] sampleNames = publicationServiceHelper.findSampleNamesByPublicationId(publication.getId().toString());
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

	private PublicationBean loadPublicationBean(Publication publication) throws Exception
	{
		if (publication == null) {
			return null;
		}
		PublicationBean publicationBean = new PublicationBean(publication);
		if (SpringSecurityUtil.getPrincipal() != null) {
			springSecurityAclService.loadAccessControlInfoForObject(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz(), 
																	publicationBean);
		}
		return publicationBean;
	}

	public int getNumberOfPublicPublications() throws PublicationException {
		try {
			int count = publicationServiceHelper.getNumberOfPublicPublications();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public publication.";
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
	}

	public int getNumberOfPublicPublicationsForJob() throws PublicationException {
		try {
			int count = publicationServiceHelper.getNumberOfPublicPublicationsForJob();
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
	public void removePublicationFromSample(String sampleName, Publication publication) throws PublicationException, NoAccessException
	{
		if (SpringSecurityUtil.getPrincipal() == null) {
			throw new NoAccessException();
		}
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
			Sample sample = sampleServiceHelper.findSampleByName(sampleName);
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

	public void deletePublication(Publication publication) throws PublicationException, NoAccessException
	{
		if (SpringSecurityUtil.getPrincipal() == null) {
			throw new NoAccessException();
		}
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
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
			String[] sampleNames = publicationServiceHelper.findSampleNamesByPublicationId(publication.getId().toString());
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

	public PublicationBean getPublicationFromPubMedXML(String pubMedId) throws PublicationException
	{
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

	public PublicationBean findNonPubMedNonDOIPublication(String publicationType, String title, String firstAuthorLastName,
			String firstAuthorFirstName) throws PublicationException
	{
		PublicationBean publicationBean = null;
		try {
			Publication publication = publicationServiceHelper.findNonPubMedNonDOIPublication(publicationType, title, firstAuthorLastName,
					firstAuthorFirstName);
			publicationBean = loadPublicationBean(publication);
			if (publication != null) {
				String[] sampleNames = publicationServiceHelper.findSampleNamesByPublicationId(publication.getId().toString());
				publicationBean.setSampleNames(sampleNames);
			}
		} catch (Exception e) {
			String err = "trouble finding non PubMed/DOI publication based on type: "
					+ publicationType + ", title: " + title + ", and first author: " + firstAuthorFirstName + " "
					+ firstAuthorLastName;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
		return publicationBean;
	}

	public void assignAccessibility(AccessControlInfo access, Publication publication) throws PublicationException, NoAccessException
	{
		if (!springSecurityAclService.isOwnerOfObject(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz())) {
			throw new NoAccessException();
		}
		try {
			// if access is Public, remove all other access except Public, Curator and owner
			if (CaNanoRoleEnum.ROLE_ANONYMOUS.toString().equalsIgnoreCase(access.getRecipient()))
			{
				springSecurityAclService.deleteAllAccessExceptPublicAndDefault(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz());
			}
			// if publication is already public, retract from public
			else {
				if (springSecurityAclService.checkObjectPublic(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz()))
					springSecurityAclService.retractObjectFromPublic(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz());
			}
			springSecurityAclService.saveAccessForObject(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz(), 
														access.getRecipient(), access.isPrincipal(), access.getRoleName());

			// set author accessibility as well because didn't share authors
			// between publications
			if (publication.getAuthorCollection() != null) {
				for (Author author : publication.getAuthorCollection()) {
					if (author != null) {
						springSecurityAclService.saveAccessForChildObject(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz(), 
																		  author.getId(), SecureClassesEnum.AUTHOR.getClazz());
					}
				}
			}
		} catch (Exception e) {
			String error = "Error in assigning access to publication";
			throw new PublicationException(error, e);
		}
	}

	public void removeAccessibility(AccessControlInfo access, Publication publication) throws PublicationException, NoAccessException
	{
		if (!springSecurityAclService.isOwnerOfObject(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz()))
		{
			throw new NoAccessException();
		}
		try
		{
			if (publication != null) {
				springSecurityAclService.retractAccessToObjectForSid(publication.getId(), SecureClassesEnum.PUBLICATION.getClazz(), access.getRecipient());
				//Author access does not have to be explicitly deleted because of the inheritance setup in ACL
			}
		} catch (Exception e) {
			String error = "Error in assigning access to publication";
			throw new PublicationException(error, e);
		}
	}

	public List<String> findPublicationIdsByOwner(String currentOwner) throws PublicationException
	{
		List<String> publicationIds = new ArrayList<String>();
		try {
			publicationIds = publicationServiceHelper.findPublicationIdsByOwner(currentOwner);
		} catch (Exception e) {
			String error = "Error in retrieving publicationIds by owner";
			throw new PublicationException(error, e);
		}
		return publicationIds;
	}
	
	@Override
	public List<String> findPublicationIdsSharedWithUser(CananoUserDetails userDetails) throws PublicationException
	{
		List<String> pubIds = new ArrayList<String>();
		try
		{
			pubIds = aclDao.getIdsOfClassSharedWithSid(SecureClassesEnum.SAMPLE, userDetails.getUsername(), userDetails.getGroups());
		}catch (Exception e) {
			String error = "Error in retrieving publicationIds shared with logged in user. " + e.getMessage();
			throw new PublicationException(error, e);
		}
		return pubIds;
	}

	@Override
	public PublicationBean findPublicationByIdWorkspace(String id, boolean loadAccessInfo) throws PublicationException
	{
		PublicationBean publicationBean = null;
		try {
			Publication publication = publicationServiceHelper.findPublicationByKey("id", new Long(id));
			if (publication != null) {
				if (loadAccessInfo) {
					publicationBean = loadPublicationBean(publication);
				} else {
					publicationBean = new PublicationBean(publication);
				}
			}
		} catch (Exception e) {
			String err = "Problem finding the publication by key: " + id;
			logger.error(err, e);
			throw new PublicationException(err, e);
		}
		return publicationBean;
	}

	public PublicationServiceHelper getPublicationServiceHelper() {
		return publicationServiceHelper;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}
	
	
}
