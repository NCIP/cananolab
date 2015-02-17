/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.SecuredDataBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.SampleBasicBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.AdvancedSampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving samples
 *
 * @author pansu
 * 
 */
public class SampleServiceLocalImpl extends BaseServiceLocalImpl implements
		SampleService {
	private static Logger logger = Logger
			.getLogger(SampleServiceLocalImpl.class);

	private SampleServiceHelper helper;
	private AdvancedSampleServiceHelper advancedHelper;
	private CharacterizationServiceLocalImpl charService;
	private CompositionServiceLocalImpl compService;
	private PublicationServiceLocalImpl publicationService;

	public SampleServiceLocalImpl() {
		super();
		helper = new SampleServiceHelper(this.securityService);
		charService = new CharacterizationServiceLocalImpl(this.securityService);
		compService = new CompositionServiceLocalImpl(this.securityService);
		publicationService = new PublicationServiceLocalImpl(
				this.securityService);
		advancedHelper = new AdvancedSampleServiceHelper(this.securityService);
	}

	public SampleServiceLocalImpl(UserBean user) {
		super(user);
		helper = new SampleServiceHelper(user);
		charService = new CharacterizationServiceLocalImpl(this.securityService);
		compService = new CompositionServiceLocalImpl(this.securityService);
		publicationService = new PublicationServiceLocalImpl(
				this.securityService);
		advancedHelper = new AdvancedSampleServiceHelper(this.securityService);
	}

	public SampleServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new SampleServiceHelper(this.securityService);
		charService = new CharacterizationServiceLocalImpl(this.securityService);
		compService = new CompositionServiceLocalImpl(this.securityService);
		publicationService = new PublicationServiceLocalImpl(
				this.securityService);
		advancedHelper = new AdvancedSampleServiceHelper(this.securityService);
	}

	/**
	 * Persist a new sample or update an existing canano sample
	 *
	 * @param sample
	 *
	 * @throws SampleException
	 *             , DuplicateEntriesException
	 */
	public void saveSample(SampleBean sampleBean) throws SampleException,
			DuplicateEntriesException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		Boolean newSample = true;
		if (sampleBean.getDomain().getId() != null) {
			newSample = false;
		}
		Sample sample = sampleBean.getDomain();
		try {
			if (!newSample
					&& !securityService.checkCreatePermission(sampleBean
							.getDomain().getId().toString())) {
				throw new NoAccessException();
			}
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample dbSample = (Sample) appService.getObject(Sample.class,
					"name", sample.getName());
			if (dbSample != null && !dbSample.getId().equals(sample.getId())) {
				throw new DuplicateEntriesException();
			}
			if (sample.getKeywordCollection() != null) {
				Collection<Keyword> keywords = new HashSet<Keyword>(sample
						.getKeywordCollection());
				sample.getKeywordCollection().clear();
				for (Keyword keyword : keywords) {
					Keyword dbKeyword = (Keyword) appService.getObject(
							Keyword.class, "name", keyword.getName());
					if (dbKeyword != null) {
						keyword.setId(dbKeyword.getId());
					} else {
						keyword.setId(null);
					}
					// turned off cascade save-update in order to share the same
					// keyword instance with File keywords.
					appService.saveOrUpdate(keyword);
					sample.getKeywordCollection().add(keyword);
				}
			}
			appService.saveOrUpdate(sample);
			// save default access
			if (newSample) {
				super.saveDefaultAccessibilities(sample.getId().toString());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in saving the sample";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void savePointOfContact(PointOfContactBean pocBean)
			throws PointOfContactException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			PointOfContact dbPointOfContact = null;
			Long oldPOCId = null;
			String oldOrgName = null;
			Boolean newPOC = true;
			Boolean newOrg = true;
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			PointOfContact domainPOC = pocBean.getDomain();
			Organization domainOrg = domainPOC.getOrganization();
			// get existing organization from database and reuse ID,
			// created by and created date
			// address information will be updated
			Organization dbOrganization = helper
					.findOrganizationByName(domainOrg.getName());
			if (dbOrganization != null) {
				domainOrg.setId(dbOrganization.getId());
				domainOrg.setCreatedBy(dbOrganization.getCreatedBy());
				domainOrg.setCreatedDate(dbOrganization.getCreatedDate());
				newOrg = false;
			}
			// create a new org if not an existing one
			else {
				domainOrg.setId(null);
			}
			// if point of contact has no ID
			if (domainPOC.getId() == null) {
				// check if org name, first name and last name matches existing
				// one
				dbPointOfContact = helper.findPointOfContactByNameAndOrg(
						domainPOC.getFirstName(), domainPOC.getLastName(),
						domainPOC.getOrganization().getName());
				// if found, reuse ID, created_date and created_by
				if (dbPointOfContact != null) {
					domainPOC.setId(dbPointOfContact.getId());
					domainPOC.setCreatedDate(dbPointOfContact.getCreatedDate());
					domainPOC.setCreatedBy(dbPointOfContact.getCreatedBy());
					newPOC = false;
				}
			} else {
				// check if organization is changed
				dbPointOfContact = helper.findPointOfContactById(domainPOC
						.getId().toString());
				Organization dbOrg = dbPointOfContact.getOrganization();
				// if organization information is changed, create a new POC
				if (!dbOrg.getName().equals(domainOrg.getName())) {
					oldPOCId = domainPOC.getId();
					oldOrgName = dbOrg.getName();
					domainPOC.setId(null);
					newPOC = true;
				}
				// if name information is changed, create a new POC
				else if (domainPOC.getFirstName() != null
						&& !domainPOC.getFirstName().equalsIgnoreCase(
								dbPointOfContact.getFirstName())
						|| domainPOC.getLastName() != null
						&& !domainPOC.getLastName().equalsIgnoreCase(
								dbPointOfContact.getLastName())) {
					newPOC = true;
				} else {
					domainPOC.setId(dbPointOfContact.getId());
					domainPOC.setCreatedBy(dbPointOfContact.getCreatedBy());
					domainPOC.setCreatedDate(dbPointOfContact.getCreatedDate());
					newPOC = false;
				}
			}
			appService.saveOrUpdate(domainPOC);

			if (newPOC) {
				this.saveAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS,
						domainPOC.getId().toString());
			}
			if (newOrg) {
				this.saveAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS,
						domainPOC.getOrganization().getId().toString());
			}
		} catch (Exception e) {
			String err = "Error in saving the PointOfContact.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	/**
	 *
	 * @param nanomaterialEntityClassNames
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @param samplePointOfContacts
	 * @return
	 * @throws SampleException
	 */
	public List<String> findSampleIdsBy(String sampleName,
			String samplePointOfContact, String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList)
			throws SampleException {
		try {
			List<String> sampleIds = helper.findSampleIdsBy(sampleName,
					samplePointOfContact, nanomaterialEntityClassNames,
					otherNanomaterialEntityTypes,
					functionalizingEntityClassNames,
					otherFunctionalizingEntityTypes, functionClassNames,
					otherFunctionTypes, characterizationClassNames,
					otherCharacterizationTypes, wordList);
			return sampleIds;
		} catch (Exception e) {
			String err = "Problem finding samples with the given search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findSampleById(String sampleId, Boolean loadAccessInfo)
			throws SampleException, NoAccessException {
		SampleBean sampleBean = null;
		try {
			Sample sample = helper.findSampleById(sampleId);
			if (sample != null) {
				if (loadAccessInfo) {
					sampleBean = loadSampleBean(sample);
				} else {
					sampleBean = new SampleBean(sample);
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the sample by id: " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return sampleBean;
	}

	/**
	 * Only load sample core data.
	 * 
	 * Do not check read permission because workspace items are owned by user.
	 */
	public SampleBasicBean findSWorkspaceSampleById(String sampleId, boolean loadAccessInfo)
			throws SampleException, NoAccessException {
		SampleBasicBean sampleBean = null;
		try {
			Sample sample = helper.findSampleBasicById(sampleId);
			if (sample != null) {
				if (loadAccessInfo) {
					sampleBean = loadSampleBean(sample, false);
				} else {
					sampleBean = new SampleBasicBean(sample);
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the sample by id: " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return sampleBean;
	}

	private Sample findFullyLoadedSampleByName(String sampleName)
			throws Exception {
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		// load composition and characterization separate because of Hibernate
		// join limitation
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("name").eq(sampleName).ignoreCase());
		Sample sample = null;

		// load composition and characterization separate because of
		// Hibernate join limitation
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.authorCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.keywordCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		if (sample == null) {
			throw new NotExistException("Sample doesn't exist in the database");
		}

		// fully load composition
		SampleComposition comp = this
				.loadComposition(sample.getId().toString());
		sample.setSampleComposition(comp);

		// fully load characterizations
		List<Characterization> chars = this.loadCharacterizations(sample
				.getId().toString());
		if (chars != null && !chars.isEmpty()) {
			sample.setCharacterizationCollection(new HashSet<Characterization>(
					chars));
		} else {
			sample.setCharacterizationCollection(null);
		}
		return sample;
	}

	private SampleComposition loadComposition(String sampleId) throws Exception {
		SampleComposition composition = null;

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(SampleComposition.class);
		crit.createAlias("sample", "sample");
		crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
		crit.setFetchMode("nanomaterialEntityCollection", FetchMode.JOIN);
		crit.setFetchMode("nanomaterialEntityCollection.fileCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"nanomaterialEntityCollection.fileCollection.keywordCollection",
						FetchMode.JOIN);
		crit.setFetchMode(
				"nanomaterialEntityCollection.composingElementCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection.targetCollection",
						FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection", FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.fileCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"functionalizingEntityCollection.fileCollection.keywordCollection",
						FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.functionCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"functionalizingEntityCollection.functionCollection.targetCollection",
						FetchMode.JOIN);
		crit.setFetchMode("functionalizingEntityCollection.activationMethod",
				FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection", FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.fileCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"chemicalAssociationCollection.fileCollection.keywordCollection",
						FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.associatedElementA",
				FetchMode.JOIN);
		crit.setFetchMode("chemicalAssociationCollection.associatedElementB",
				FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection.keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);

		if (!result.isEmpty()) {
			composition = (SampleComposition) result.get(0);
		}
		return composition;
	}

	private List<Characterization> loadCharacterizations(String sampleId)
			throws Exception {
		List<Characterization> chars = new ArrayList<Characterization>();

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(Characterization.class);
		crit.createAlias("sample", "sample");
		crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
		// fully load characterization
		crit.setFetchMode("pointOfContact", FetchMode.JOIN);
		crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("protocol", FetchMode.JOIN);
		crit.setFetchMode("protocol.file", FetchMode.JOIN);
		crit.setFetchMode("protocol.file.keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique",
				FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.instrumentCollection",
				FetchMode.JOIN);
		crit.setFetchMode("findingCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.datumCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"findingCollection.datumCollection.conditionCollection",
				FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("findingCollection.fileCollection.keywordCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);

		for (Object obj : results) {
			Characterization achar = (Characterization) obj;
			chars.add(achar);
		}
		return chars;
	}

	
	private SampleBean loadSampleBean(Sample sample) throws Exception {
		SampleBean sampleBean = new SampleBean(sample);
		if (user != null) {
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(sample.getId().toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(sample.getId().toString());
			sampleBean.setUserAccesses(userAccesses);
			sampleBean.setGroupAccesses(groupAccesses);
			sampleBean.setUser(user);
		}
		return sampleBean;
	}

	public void loadAccessesForBasicSampleBean(SampleBasicBean sampleBean) throws Exception {
		Sample sample = sampleBean.getDomain();
		if (user != null) {
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(sample.getId().toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(sample.getId().toString());
			sampleBean.setUserAccesses(userAccesses);
			sampleBean.setGroupAccesses(groupAccesses);
			sampleBean.setUser(user);
		}
		
	}
	
	private SampleBasicBean loadSampleBean(Sample sample, boolean checkReadPermission) throws Exception {
		SampleBasicBean sampleBean = new SampleBasicBean(sample);
		if (user != null) {
			logger.debug("=== Loading group accesses");
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(sample.getId().toString(), checkReadPermission);
			logger.debug("=== Done Loading group accesses");
			
			logger.debug("=== Loading user accesses");
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(sample.getId().toString(), checkReadPermission);
			logger.debug("=== Done Loading user accesses");
			sampleBean.setUserAccesses(userAccesses);
			sampleBean.setGroupAccesses(groupAccesses);
			sampleBean.setUser(user);
		}
		return sampleBean;
	}

	public SampleBean findSampleByName(String sampleName)
			throws SampleException, NoAccessException {
		try {
			Sample sample = helper.findSampleByName(sampleName);
			SampleBean sampleBean = null;
			if (sample != null) {
				sampleBean = loadSampleBean(sample);
			}
			return sampleBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the sample by name: " + sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public int getNumberOfPublicSamples() throws SampleException {
		try {
			int count = helper.getNumberOfPublicSamples();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public samples.";
			logger.error(err, e);
			throw new SampleException(err, e);

		}
	}

	public int getNumberOfPublicSampleSources() throws SampleException {
		try {
			int count = helper.getNumberOfPublicSampleSources();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public sample sources.";
			logger.error(err, e);
			throw new SampleException(err, e);

		}
	}

	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException {
		PointOfContactBean pocBean = null;
		try {
			PointOfContact poc = helper.findPointOfContactById(pocId);
			pocBean = new PointOfContactBean(poc);
		} catch (Exception e) {
			String err = "Problem finding point of contact for the given id.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
		return pocBean;
	}

	public List<PointOfContactBean> findPointOfContactsBySampleId(
			String sampleId) throws PointOfContactException {
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
					.add(Property.forName("id").eq(new Long(sampleId)));
			crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
			crit.setFetchMode("primaryPointOfContact.organization",
					FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
			crit.setFetchMode("otherPointOfContactCollection.organization",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			List<PointOfContactBean> pointOfContactCollection = new ArrayList<PointOfContactBean>();
			for (Object obj : results) {
				Sample particle = (Sample) obj;
				PointOfContact primaryPOC = particle.getPrimaryPointOfContact();
				Collection<PointOfContact> otherPOCs = particle
						.getOtherPointOfContactCollection();
				pointOfContactCollection
						.add(new PointOfContactBean(primaryPOC));
				for (PointOfContact poc : otherPOCs) {
					pointOfContactCollection.add(new PointOfContactBean(poc));
				}
			}
			return pointOfContactCollection;
		} catch (Exception e) {
			String err = "Problem finding all PointOfContact collections with the given sample ID.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public SortedSet<String> getAllOrganizationNames()
			throws PointOfContactException {
		try {
			SortedSet<String> names = new TreeSet<String>(
					new Comparators.SortableNameComparator());
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select org.name from gov.nih.nci.cananolab.domain.common.Organization org");
			List results = appService.query(crit);
			
			
			logger.debug("Completed select org.name from gov.nih.nci.cananolab.domain.common.Organization org");
			for (Object obj : results) {
				String name = ((String) obj).trim();
				names.add(name);
			}
			return names;
		} catch (Exception e) {
			String err = "Error finding organization for "
					+ helper.getUser().getLoginName();
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public List<String> findSampleIdsByAdvancedSearch(
			AdvancedSampleSearchBean searchBean) throws SampleException {
		try {
			return advancedHelper.findSampleIdsByAdvancedSearch(searchBean);
		} catch (Exception e) {
			String err = "Problem finding samples with the given advanced search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(
			String sampleId, AdvancedSampleSearchBean searchBean)
			throws SampleException {
		try {
			return advancedHelper.findAdvancedSampleByAdvancedSearch(sampleId,
					searchBean);
		} catch (Exception e) {
			String err = "Problem finding advanced sample details with the given advanced search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean cloneSample(String originalSampleName,
			String newSampleName) throws SampleException, NoAccessException,
			DuplicateEntriesException, NotExistException {
		if (user == null) {
			throw new NoAccessException();
		}
		SampleBean newSampleBean = null;
		Sample origSample = null;
		SampleBean origSampleBean = null;
		Sample newSample0 = new Sample();
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Sample dbNewSample = (Sample) appService.getObject(Sample.class,
					"name", newSampleName);
			if (dbNewSample != null) {
				throw new DuplicateEntriesException();
			}
			// fully load original sample
			origSample = findFullyLoadedSampleByName(originalSampleName);
			origSampleBean = new SampleBean(origSample);
			newSample0.setName(newSampleName);
			newSample0.setCreatedBy(user.getLoginName() + ":"
					+ Constants.AUTO_COPY_ANNOTATION_PREFIX);
			newSample0.setCreatedDate(new Date());
			// save the sample so later up just update the cloned the
			// associations.
			SampleBean newSampleBean0 = new SampleBean(newSample0);
			// save the sample to get an ID before saving associations
			saveSample(newSampleBean0);
		} catch (NotExistException e) {
			throw e;
		} catch (DuplicateEntriesException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in loading the original sample "
					+ originalSampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		try {
			// clone the sample
			Sample newSample = origSampleBean
					.getDomainCopy(user.getLoginName());
			newSample.setName(newSampleName);
			// keep the id
			newSample.setId(newSample0.getId());
			newSampleBean = new SampleBean(newSample);

			// retrieve accessibilities of the original sample
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(origSample.getId().toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(origSample.getId().toString());
			origSampleBean.setGroupAccesses(groupAccesses);
			origSampleBean.setUserAccesses(userAccesses);

			// need to save associations one by one (except keywords)
			// Hibernate mapping settings for most use cases
			saveClonedPOCs(newSampleBean);
			saveClonedCharacterizations(origSample.getName(), newSampleBean);
			saveClonedComposition(origSampleBean, newSampleBean);
			saveClonedPublications(origSampleBean, newSampleBean);
			saveSample(newSampleBean);
			newSampleBean.setUser(user);
			// assign accessibility for the new sample
			for (AccessibilityBean access : origSampleBean.getAllAccesses()) {
				this.assignAccessibility(access, newSampleBean.getDomain());
			}
		} catch (Exception e) {
			// delete the already persisted new sample in case of error
			try {
				this.deleteSampleWhenError(newSample0.getName());
			} catch (Exception ex) {
				String err = "Error in deleting the errored cloned-sample "
						+ newSample0.getName();
				logger.error(err, e);
				throw new SampleException(err, ex);
			}
			String err = "Error in cloning the sample " + originalSampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return newSampleBean;
	}

	private void saveClonedPOCs(SampleBean sampleBean) throws Exception {
		savePointOfContact(sampleBean.getPrimaryPOCBean());
		if (sampleBean.getOtherPOCBeans() != null
				&& !sampleBean.getOtherPOCBeans().isEmpty()) {
			for (PointOfContactBean pocBean : sampleBean.getOtherPOCBeans()) {
				savePointOfContact(pocBean);
			}
		}
	}

	private void saveClonedCharacterizations(String origSampleName,
			SampleBean sampleBean) throws Exception {
		if (sampleBean.getDomain().getCharacterizationCollection() != null) {
			String newSampleName = sampleBean.getDomain().getName();
			for (Characterization achar : sampleBean.getDomain()
					.getCharacterizationCollection()) {
				CharacterizationBean charBean = new CharacterizationBean(achar);
				if (charBean.getExperimentConfigs() != null) {
					for (ExperimentConfigBean configBean : charBean
							.getExperimentConfigs()) {
						charService.saveExperimentConfig(configBean);
					}
				}
				if (charBean.getFindings() != null) {
					for (FindingBean findingBean : charBean.getFindings()) {
						for (FileBean fileBean : findingBean.getFiles()) {
							fileUtils.updateClonedFileInfo(fileBean,
									origSampleName, newSampleName);
						}
						charService.saveFinding(findingBean);
					}
				}
				charService.saveCharacterization(sampleBean, charBean);
			}
		}
	}

	private void saveClonedComposition(SampleBean origSampleBean,
			SampleBean sampleBean) throws Exception {
		String origSampleName = origSampleBean.getDomain().getName();
		String newSampleName = sampleBean.getDomain().getName();

		if (sampleBean.getDomain().getSampleComposition() != null) {
			// save files
			if (sampleBean.getDomain().getSampleComposition()
					.getFileCollection() != null) {
				for (File file : sampleBean.getDomain().getSampleComposition()
						.getFileCollection()) {
					FileBean fileBean = new FileBean(file);
					fileUtils.updateClonedFileInfo(fileBean, origSampleName,
							newSampleName);
					compService.saveCompositionFile(sampleBean, fileBean);
				}
			}

			// save nanomaterial entities
			if (sampleBean.getDomain().getSampleComposition()
					.getNanomaterialEntityCollection() != null) {
				for (NanomaterialEntity entity : sampleBean.getDomain()
						.getSampleComposition()
						.getNanomaterialEntityCollection()) {
					NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
							entity);
					for (FileBean fileBean : entityBean.getFiles()) {
						fileUtils.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName);
					}
					compService.saveNanomaterialEntity(sampleBean, entityBean);
				}
			}
			// save functionalizing entities
			if (sampleBean.getDomain().getSampleComposition()
					.getFunctionalizingEntityCollection() != null) {
				for (FunctionalizingEntity entity : sampleBean.getDomain()
						.getSampleComposition()
						.getFunctionalizingEntityCollection()) {
					FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
							entity);
					for (FileBean fileBean : entityBean.getFiles()) {
						fileUtils.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName);
					}
					compService.saveFunctionalizingEntity(sampleBean,
							entityBean);
				}
			}
			// save chemical association
			if (sampleBean.getDomain().getSampleComposition()
					.getChemicalAssociationCollection() != null) {
				for (ChemicalAssociation assoc : sampleBean.getDomain()
						.getSampleComposition()
						.getChemicalAssociationCollection()) {
					ChemicalAssociationBean assocBean = new ChemicalAssociationBean(
							assoc);
					// set the correct IDs for associated elements
					updateAssociatedElementId(sampleBean.getDomain()
							.getSampleComposition(), assoc
							.getAssociatedElementA());
					updateAssociatedElementId(sampleBean.getDomain()
							.getSampleComposition(), assoc
							.getAssociatedElementB());
					for (FileBean fileBean : assocBean.getFiles()) {
						fileUtils.updateClonedFileInfo(fileBean,
								origSampleName, newSampleName);
					}
					compService.saveChemicalAssociation(sampleBean, assocBean);
				}
			}
		}
	}

	private void updateAssociatedElementId(SampleComposition comp,
			AssociatedElement associatedElement) {
		if (associatedElement != null) {
			int copyInd = associatedElement.getCreatedBy().indexOf(
					Constants.AUTO_COPY_ANNOTATION_PREFIX);
			String origId = null;
			if (copyInd != -1) {
				origId = associatedElement.getCreatedBy()
						.substring(copyInd + 5);
			}

			// finding the matching functionalizing entity
			if (associatedElement instanceof FunctionalizingEntity) {
				for (FunctionalizingEntity entity : comp
						.getFunctionalizingEntityCollection()) {
					int copyEInd = entity.getCreatedBy().indexOf(
							Constants.AUTO_COPY_ANNOTATION_PREFIX);
					String entityOrigId = null;
					if (copyEInd != -1) {
						entityOrigId = entity.getCreatedBy().substring(
								copyEInd + 5);
					}
					if (entityOrigId.equals(origId)) {
						associatedElement.setId(entity.getId());
						break;
					}
				}
			}
			if (associatedElement instanceof ComposingElement) {
				for (NanomaterialEntity entity : comp
						.getNanomaterialEntityCollection()) {
					if (entity.getComposingElementCollection() != null) {
						for (ComposingElement ce : entity
								.getComposingElementCollection()) {
							int copyCEInd = ce.getCreatedBy().indexOf(
									Constants.AUTO_COPY_ANNOTATION_PREFIX);
							String ceOrigId = null;
							if (copyCEInd != -1) {
								ceOrigId = ce.getCreatedBy().substring(
										copyCEInd + 5);
							}
							if (ceOrigId.equals(origId)) {
								associatedElement.setId(ce.getId());
								break;
							}
						}
					}
				}
			}
		}
	}

	private void saveClonedPublications(SampleBean origSampleBean,
			SampleBean sampleBean) throws Exception {
		if (sampleBean.getDomain().getPublicationCollection() != null) {
			for (Publication pub : sampleBean.getDomain()
					.getPublicationCollection()) {
				PublicationBean pubBean = new PublicationBean(pub);
				pubBean.setFromSamplePage(true);
				// don't need to reset sample names because savePublication
				// takes care of empty sample names.
				publicationService.savePublication(pubBean);
				// don't need to save access because the cloned publications
				// shared the same IDs with the source publications
			}
		}
	}

	private void deleteSampleWhenError(String sampleName) throws Exception {
		Sample sample = this.findFullyLoadedSampleByName(sampleName);
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		// delete characterizations
		if (sample.getCharacterizationCollection() != null) {
			for (Characterization achar : sample
					.getCharacterizationCollection()) {
				charService.deleteCharacterization(achar);
			}
		}

		// delete composition
		if (sample.getSampleComposition() != null) {
			compService.deleteComposition(sample.getSampleComposition());
		}
		sample.setSampleComposition(null);

		// remove publication associations
		if (sample.getPublicationCollection() != null) {
			sample.setPublicationCollection(null);
		}
		// remove keyword associations
		if (sample.getKeywordCollection() != null) {
			sample.setKeywordCollection(null);
		}
		appService.saveOrUpdate(sample);
		appService.delete(sample);
		// remove all csm entries associated with sample
		this.accessUtils.removeCSMEntries(sample.getId().toString());
	}

	public void deleteSample(String sampleName) throws SampleException,
			NoAccessException, NotExistException {
		if (user == null) {
			throw new NoAccessException();
		}
		Sample sample = null;
		try {
			// / / fully load original sample
			sample = findFullyLoadedSampleByName(sampleName);
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// / / delete characterizations
			if (sample.getCharacterizationCollection() != null) {
				for (Characterization achar : sample
						.getCharacterizationCollection()) {
					charService.deleteCharacterization(achar);
				}
			}

			// / / delete composition
			if (sample.getSampleComposition() != null) {
				compService.deleteComposition(sample.getSampleComposition());
			}
			sample.setSampleComposition(null);

			// / / remove publication associations
			if (sample.getPublicationCollection() != null) {
				sample.setPublicationCollection(null);
			}
			// / / remove keyword associations
			if (sample.getKeywordCollection() != null) {
				sample.setKeywordCollection(null);
			}
			appService.saveOrUpdate(sample);
			appService.delete(sample);
		} catch (NotExistException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in deleting the sample " + sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void updatePOCAssociatedWithCharacterizations(String sampleName,
			Long oldPOCId, Long newPOCId) throws SampleException {
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Characterization.class);
			crit.createAlias("sample", "sample");
			crit.createAlias("pointOfContact", "poc");
			crit.add(Property.forName("poc.id").eq(oldPOCId));
			crit.add(Property.forName("sample.name").eq(sampleName));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				Characterization achar = (Characterization) obj;
				// update POC to the new ID
				achar.getPointOfContact().setId(newPOCId);
				appService.saveOrUpdate(achar);
			}
		} catch (Exception e) {
			String err = "Error in updating POC associated sample characterizations "
					+ sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	// public void updateSampleVisibilityWithPOCChange(SampleBean sampleBean,
	// String oldPOCId) throws SampleException {
	// try {
	// // remove oldOrg from sample visibility
	// PointOfContact oldPOC = getHelper()
	// .findPointOfContactById(oldPOCId);
	// String oldOrgName = oldPOC.getOrganization().getName();
	// String[] sampleVisGroups = sampleBean.getVisibilityGroups();
	// String[] updatedGroups = StringUtils.removeFromArray(
	// sampleVisGroups, oldOrgName);
	// sampleBean.setVisibilityGroups(updatedGroups);
	// } catch (Exception e) {
	// String err = "Error in updating sample visibility with POC change for "
	// + sampleBean.getDomain().getName();
	// logger.error(err, e);
	// throw new SampleException(err, e);
	// }
	// }

	public List<String> findOtherSampleNamesFromSamePrimaryOrganization(
			String sampleId) throws SampleException {
		List<String> sortedNames = null;
		try {
			Set<String> names = helper
					.findOtherSamplesFromSamePrimaryOrganization(sampleId);
			sortedNames = new ArrayList<String>(names);
			Collections.sort(sortedNames,
					new Comparators.SortableNameComparator());

		} catch (Exception e) {
			String err = "Error in deleting the sample " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
		return sortedNames;
	}

	public SampleServiceHelper getHelper() {
		return helper;
	}

	public void assignAccessibility(AccessibilityBean access, Sample sample)
			throws SampleException, NoAccessException {
		if (!isOwnerByCreatedBy(sample.getCreatedBy())) {
			throw new NoAccessException();
		}
		String sampleId = sample.getId().toString();
		try {
			if (!isOwnerByCreatedBy(sample.getCreatedBy())) {
				throw new NoAccessException();
			}
			// get existing accessibilities
			List<AccessibilityBean> groupAccesses = this
					.findGroupAccessibilities(sampleId);
			List<AccessibilityBean> userAccesses = this
					.findUserAccessibilities(sampleId);

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
						this.removeAccessibility(acc, sample);
					}
				}
				SecuredDataBean securedDataBean = new SecuredDataBean();
				for (AccessibilityBean acc : userAccesses) {
					// remove accesses that are not owner
					if (!securedDataBean.retrieveUserIsOwner(acc.getUserBean(),
							sample.getCreatedBy())) {
						this.removeAccessibility(acc, sample);
					}
				}
			}
			// if sample is already public, retract from public
			else {
				if (groupAccesses.contains(AccessibilityBean.CSM_PUBLIC_ACCESS)) {
					this.removeAccessibility(
							AccessibilityBean.CSM_PUBLIC_ACCESS, sample);
				}
			}
			super.saveAccessibility(access, sampleId);

			// fully load sample
			sample = this.findFullyLoadedSampleByName(sample.getName());
			// assign POC to public is handled when adding POC
			// TODO check this logic when working with COPPA on organization

			// assign characterization accessibility
			if (sample.getCharacterizationCollection() != null) {
				for (Characterization achar : sample
						.getCharacterizationCollection()) {
					accessUtils.assignAccessibility(access, achar);
				}
			}
			// assign composition accessibility
			if (sample.getSampleComposition() != null) {
				accessUtils.assignAccessibility(access, sample
						.getSampleComposition());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in assigning accessibility to the sample "
					+ sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void removeAccessibility(AccessibilityBean access, Sample sample)
			throws SampleException, NoAccessException {
		if (!isOwnerByCreatedBy(sample.getCreatedBy())) {
			throw new NoAccessException();
		}
		String sampleId = sample.getId().toString();
		try {
			super.deleteAccessibility(access, sampleId);
			// fully load sample
			sample = this.findFullyLoadedSampleByName(sample.getName());
			// keep POC public
			// remove characterization accessibility
			if (sample.getCharacterizationCollection() != null) {
				for (Characterization achar : sample
						.getCharacterizationCollection()) {
					accessUtils.removeAccessibility(access, achar, false);
				}
			}
			// remove composition accessibility
			if (sample.getSampleComposition() != null) {
				accessUtils.removeAccessibility(access, sample
						.getSampleComposition(), false);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error in deleting the access for sample " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public List<String> findSampleIdsByOwner(String currentOwner)
			throws SampleException {
		List<String> sampleIds = new ArrayList<String>();
		try {
			sampleIds = helper.findSampleIdsByOwner(currentOwner);
		} catch (Exception e) {
			String error = "Error in retrieving sampleIds by owner";
			throw new SampleException(error, e);
		}
		return sampleIds;
	}

	public List<String> removeAccesses(Sample sample, Boolean removeLater)
			throws SampleException, NoAccessException {
		List<String> ids = new ArrayList<String>();
		try {
			if (!securityService.checkCreatePermission(sample.getId()
					.toString())) {
				throw new NoAccessException();
			}
			ids.add(sample.getId().toString());
			// fully load sample
			Sample fullSample = this.findFullyLoadedSampleByName(sample
					.getName());
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(sample.getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				if (fullSample.getCharacterizationCollection() != null) {
					for (Characterization achar : fullSample
							.getCharacterizationCollection()) {
						ids.addAll(accessUtils.removeAccessibility(access,
								achar, removeLater));
					}
				}
				if (fullSample.getSampleComposition() != null) {
					ids.addAll(accessUtils.removeAccessibility(access,
							fullSample.getSampleComposition(), removeLater));
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in removing sample accesses";
			throw new SampleException(error, e);
		}
		return ids;
	}

	@Override
	public SampleBasicBean findSampleBasicById(String sampleId,
			Boolean loadAccessInfo) throws SampleException, NoAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> findSampleIdNamesByAdvancedSearch(
			AdvancedSampleSearchBean searchBean) throws SampleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadAccessesForSampleBean(SampleBean sampleBean)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
