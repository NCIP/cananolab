/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.function.OtherFunction;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.web.struts.action.Criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of SampleService and grid service *
 * 
 * @author pansu, tanq
 * 
 */
public class SampleServiceHelper extends BaseServiceHelper {
	private static Logger logger = Logger.getLogger(SampleServiceHelper.class);

	public SampleServiceHelper() {
		super();
	}

	public SampleServiceHelper(UserBean user) {
		super(user);
	}

	public SampleServiceHelper(SecurityService securityService) {
		super(securityService);
	}

	public List<String> findSampleIdsBy(String sampleName,
			String samplePointOfContact, String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList)
			throws Exception {
		List<String> sampleIds = new ArrayList<String>();
		
		//logger.error("Processing: " + sampleName);

		// can't query for the entire Sample object due to
		// limitations in pagination in SDK

		// added createdDate and sample name in the results so data can be
		// sorted by date and name
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("id"))
								.add(Projections.property("name"))
								.add(Projections.property("createdDate")));
		if (!StringUtils.isEmpty(sampleName)) {
			TextMatchMode nameMatchMode = new TextMatchMode(sampleName);
			crit.add(Restrictions.ilike("name", nameMatchMode.getUpdatedText(),
					nameMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(samplePointOfContact)) {
			TextMatchMode pocMatchMode = new TextMatchMode(samplePointOfContact);
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("primaryPointOfContact", "pointOfContact");
			crit.createAlias("pointOfContact.organization", "organization");
			crit.createAlias("otherPointOfContactCollection", "otherPoc",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("otherPoc.organization", "otherOrg",
					CriteriaSpecification.LEFT_JOIN);
			String critStrs[] = { "pointOfContact.lastName",
					"pointOfContact.firstName", "pointOfContact.role",
					"organization.name", "otherPoc.lastName",
					"otherPoc.firstName", "otherOrg.name" };
			for (String critStr : critStrs) {
				Criterion pocCrit = Restrictions.ilike(critStr,
						pocMatchMode.getUpdatedText(),
						pocMatchMode.getMatchMode());
				disjunction.add(pocCrit);
			}
			crit.add(disjunction);
		}

		// join composition
		if (nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0
				|| functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| otherFunctionalizingEntityTypes != null
				&& otherFunctionalizingEntityTypes.length > 0) {
			crit.createAlias("sampleComposition", "comp",
					CriteriaSpecification.LEFT_JOIN);
		}
		// join nanomaterial entity
		if (nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			crit.createAlias("comp.nanomaterialEntityCollection", "nanoEntity",
					CriteriaSpecification.LEFT_JOIN);
		}

		// join functionalizing entity
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| otherFunctionalizingEntityTypes != null
				&& otherFunctionalizingEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			crit.createAlias("comp.functionalizingEntityCollection",
					"funcEntity", CriteriaSpecification.LEFT_JOIN);
		}

		// nanomaterial entity
		if (nanomaterialEntityClassNames != null
				&& nanomaterialEntityClassNames.length > 0
				|| otherNanomaterialEntityTypes != null
				&& otherNanomaterialEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			if (nanomaterialEntityClassNames != null
					&& nanomaterialEntityClassNames.length > 0) {
				Criterion nanoEntityCrit = Restrictions.in("nanoEntity.class",
						nanomaterialEntityClassNames);
				disjunction.add(nanoEntityCrit);
			}
			if (otherNanomaterialEntityTypes != null
					&& otherNanomaterialEntityTypes.length > 0) {
				Criterion otherNanoCrit1 = Restrictions.eq("nanoEntity.class",
						"OtherNanomaterialEntity");
				Criterion otherNanoCrit2 = Restrictions.in("nanoEntity.type",
						otherNanomaterialEntityTypes);
				Criterion otherNanoCrit = Restrictions.and(otherNanoCrit1,
						otherNanoCrit2);
				disjunction.add(otherNanoCrit);
			}
			crit.add(disjunction);
		}

		// functionalizing entity
		// need to turn class names into integers in order for the .class
		// clause to work
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| otherFunctionalizingEntityTypes != null
				&& otherFunctionalizingEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			if (functionalizingEntityClassNames != null
					&& functionalizingEntityClassNames.length > 0) {
				Integer[] functionalizingEntityClassNameIntegers = this
						.convertToFunctionalizingEntityClassOrderNumber(functionalizingEntityClassNames);
				Criterion funcEntityCrit = Restrictions.in("funcEntity.class",
						functionalizingEntityClassNameIntegers);
				disjunction.add(funcEntityCrit);
			}
			if (otherFunctionalizingEntityTypes != null
					&& otherFunctionalizingEntityTypes.length > 0) {
				Integer classOrderNumber = Constants.FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP
						.get("OtherFunctionalizingEntity");
				Criterion otherFuncCrit1 = Restrictions.eq("funcEntity.class",
						classOrderNumber);
				Criterion otherFuncCrit2 = Restrictions.in("funcEntity.type",
						otherFunctionalizingEntityTypes);
				Criterion otherFuncCrit = Restrictions.and(otherFuncCrit1,
						otherFuncCrit2);
				disjunction.add(otherFuncCrit);
			}
			crit.add(disjunction);
		}

		// function
		if (functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias("nanoEntity.composingElementCollection",
					"compElement", CriteriaSpecification.LEFT_JOIN)
					.createAlias("compElement.inherentFunctionCollection",
							"inFunc", CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("funcEntity.functionCollection", "func",
					CriteriaSpecification.LEFT_JOIN);
			if (functionClassNames != null && functionClassNames.length > 0) {
				Criterion funcCrit1 = Restrictions.in("inFunc.class",
						functionClassNames);
				Criterion funcCrit2 = Restrictions.in("func.class",
						functionClassNames);
				disjunction.add(funcCrit1).add(funcCrit2);
			}
			if (otherFunctionTypes != null && otherFunctionTypes.length > 0) {
				Criterion otherFuncCrit1 = Restrictions.and(
						Restrictions.eq("inFunc.class", "OtherFunction"),
						Restrictions.in("inFunc.type", otherFunctionTypes));
				Criterion otherFuncCrit2 = Restrictions.and(
						Restrictions.eq("func.class", "OtherFunction"),
						Restrictions.in("func.type", otherFunctionTypes));
				disjunction.add(otherFuncCrit1).add(otherFuncCrit2);
			}
			crit.add(disjunction);
		}

		// join characterization
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0
				|| otherCharacterizationTypes != null
				&& otherCharacterizationTypes.length > 0 || wordList != null
				&& wordList.length > 0) {
			crit.createAlias("characterizationCollection", "chara",
					CriteriaSpecification.LEFT_JOIN);
		}
		// characterization
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0
				|| otherCharacterizationTypes != null
				&& otherCharacterizationTypes.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			if (characterizationClassNames != null
					&& characterizationClassNames.length > 0) {
				Criterion charCrit = Restrictions.in("chara.class",
						characterizationClassNames);
				disjunction.add(charCrit);
			}
			if (otherCharacterizationTypes != null
					&& otherCharacterizationTypes.length > 0) {
				Criterion otherCharCrit1 = Restrictions.eq("chara.class",
						"OtherCharacterization");
				Criterion otherCharCrit2 = Restrictions.in("chara.name",
						otherCharacterizationTypes);
				Criterion otherCharCrit = Restrictions.and(otherCharCrit1,
						otherCharCrit2);
				disjunction.add(otherCharCrit);
			}
			crit.add(disjunction);
		}
		// join keyword, finding, publication
		if (wordList != null && wordList.length > 0) {
			crit.createAlias("keywordCollection", "keyword1",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("chara.findingCollection", "finding",
					CriteriaSpecification.LEFT_JOIN)
					.createAlias("finding.fileCollection", "charFile",
							CriteriaSpecification.LEFT_JOIN)
					.createAlias("charFile.keywordCollection", "keyword2",
							CriteriaSpecification.LEFT_JOIN);
			// publication keywords
			crit.createAlias("publicationCollection", "pub1",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("pub1.keywordCollection", "keyword3",
					CriteriaSpecification.LEFT_JOIN);
		}

		// keyword
		if (wordList != null && wordList.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String keyword : wordList) {
				// strip wildcards from either ends of keyword
				keyword = StringUtils.stripWildcards(keyword);
				Criterion keywordCrit1 = Restrictions.ilike("keyword1.name",
						keyword, MatchMode.ANYWHERE);
				Criterion keywordCrit2 = Restrictions.ilike("keyword2.name",
						keyword, MatchMode.ANYWHERE);
				Criterion keywordCrit3 = Restrictions.ilike("keyword3.name",
						keyword, MatchMode.ANYWHERE);
				disjunction.add(keywordCrit1);
				disjunction.add(keywordCrit2);
				disjunction.add(keywordCrit3);
			}
			for (String word : wordList) {
				Criterion summaryCrit1 = Restrictions.ilike(
						"chara.designMethodsDescription", word,
						MatchMode.ANYWHERE);
				Criterion summaryCrit2 = Restrictions.ilike(
						"charFile.description", word, MatchMode.ANYWHERE);
				Criterion summaryCrit = Restrictions.or(summaryCrit1,
						summaryCrit2);
				disjunction.add(summaryCrit);
			}
			crit.add(disjunction);
		}

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		
		List results = appService.query(crit);
		Set<Sample> samples = new HashSet<Sample>();
		for (Object obj : results) {
			
			try {
				/*
				 * There is a bug when searching with keyword "tes", where the following line
				 * whould trigger a ClassCastException. Reason unknow but suspected to be reaching
				 * the last row of a dataset. 
				 */
				Object[] row = (Object[]) obj;

				Long sampleId = (Long) row[0];

				if (StringUtils.containsIgnoreCase(getAccessibleData(),
						sampleId.toString())) {
					Sample sample = new Sample();
					sample.setId(sampleId);
					sample.setName((String) row[1]);
					sample.setCreatedDate((Date) row[2]);
					samples.add(sample);
				} else {
					logger.debug("User doesn't have access to sample of ID: "
							+ sampleId);
				}

			} catch (ClassCastException e) {
				logger.error("Got ClassCastException: " + e.getMessage());
				break;
			}
		}
		
		List<Sample> orderedSamples = new ArrayList<Sample>(samples);
		// Collections.sort(orderedSamples,
		// Collections.reverseOrder(new Comparators.SampleDateComparator()));

		Collections
				.sort(orderedSamples, new Comparators.SampleDateComparator());

		for (Sample sample : orderedSamples) {
			sampleIds.add(sample.getId().toString());
		}
		

		return sampleIds;
	}
	

	/**
	 * Return all stored functionalizing entity class names. In case of
	 * OtherFunctionalizingEntity, store the OtherFunctionalizingEntity type
	 * 
	 * @param sample
	 * @return
	 */
	public SortedSet<String> getStoredFunctionalizingEntityClassNames(
			Sample sample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (sample.getSampleComposition() != null
				&& sample.getSampleComposition()
						.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : sample.getSampleComposition()
					.getFunctionalizingEntityCollection()) {
				if (entity instanceof OtherFunctionalizingEntity) {
					storedEntities.add(((OtherFunctionalizingEntity) entity)
							.getType());
				} else {
					storedEntities.add(ClassUtils.getShortClassName(entity
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedEntities;
	}

	/**
	 * Return all stored function class names. In case of OtherFunction, store
	 * the otherFunction type
	 * 
	 * @param sample
	 * @return
	 */
	public SortedSet<String> getStoredFunctionClassNames(Sample sample) {
		SortedSet<String> storedFunctions = new TreeSet<String>();

		if (sample.getSampleComposition() != null) {
			if (sample.getSampleComposition().getNanomaterialEntityCollection() != null) {
				for (NanomaterialEntity entity : sample.getSampleComposition()
						.getNanomaterialEntityCollection()) {
					if (entity.getComposingElementCollection() != null) {
						for (ComposingElement element : entity
								.getComposingElementCollection()) {
							if (element.getInherentFunctionCollection() != null) {
								for (Function function : element
										.getInherentFunctionCollection()) {
									if (function instanceof OtherFunction) {
										storedFunctions
												.add(((OtherFunction) function)
														.getType());
									} else {
										storedFunctions.add(ClassUtils
												.getShortClassName(function
														.getClass()
														.getCanonicalName()));
									}
								}
							}
						}
					}
				}
			}
			if (sample.getSampleComposition()
					.getFunctionalizingEntityCollection() != null) {
				for (FunctionalizingEntity entity : sample
						.getSampleComposition()
						.getFunctionalizingEntityCollection()) {
					if (entity.getFunctionCollection() != null) {
						for (Function function : entity.getFunctionCollection()) {
							if (function instanceof OtherFunction) {
								storedFunctions.add(((OtherFunction) function)
										.getType());
							} else {
								storedFunctions.add(ClassUtils
										.getShortClassName(function.getClass()
												.getCanonicalName()));
							}
						}
					}
				}
			}
		}
		return storedFunctions;
	}

	/**
	 * Return all stored nanomaterial entity class names. In case of
	 * OtherNanomaterialEntity, store the otherNanomaterialEntity type
	 * 
	 * @param sample
	 * @return
	 */
	public SortedSet<String> getStoredNanomaterialEntityClassNames(Sample sample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (sample.getSampleComposition() != null
				&& sample.getSampleComposition()
						.getNanomaterialEntityCollection() != null) {
			for (NanomaterialEntity entity : sample.getSampleComposition()
					.getNanomaterialEntityCollection()) {
				if (entity instanceof OtherNanomaterialEntity) {
					storedEntities.add(((OtherNanomaterialEntity) entity)
							.getType());
				} else {
					storedEntities.add(ClassUtils.getShortClassName(entity
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedEntities;
	}

	public SortedSet<String> getStoredChemicalAssociationClassNames(
			Sample sample) {
		SortedSet<String> storedAssocs = new TreeSet<String>();
		if (sample.getSampleComposition() != null
				&& sample.getSampleComposition()
						.getChemicalAssociationCollection() != null) {
			for (ChemicalAssociation assoc : sample.getSampleComposition()
					.getChemicalAssociationCollection()) {
				if (assoc instanceof OtherChemicalAssociation) {
					storedAssocs.add(((OtherChemicalAssociation) assoc)
							.getType());
				} else {
					storedAssocs.add(ClassUtils.getShortClassName(assoc
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedAssocs;
	}

	public SortedSet<String> getStoredCharacterizationClassNames(Sample sample) {
		SortedSet<String> storedChars = new TreeSet<String>();
		if (sample.getCharacterizationCollection() != null) {
			for (Characterization achar : sample
					.getCharacterizationCollection()) {
				if (achar instanceof OtherCharacterization) {
					storedChars.add(((OtherCharacterization) achar).getName());
				} else {
					storedChars.add(ClassUtils.getShortClassName(achar
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedChars;
	}

	public Sample findSampleByName(String sampleName) throws Exception {
		Sample sample = null;
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("name").eq(sampleName).ignoreCase());
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"sampleComposition.nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);

		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"sampleComposition.functionalizingEntityCollection.functionCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
			if (!StringUtils.containsIgnoreCase(getAccessibleData(), sample
					.getId().toString())) {
				throw new NoAccessException("User has no access to the sample "
						+ sampleName);
			}
		}
		return sample;
	}

	public List<Keyword> findKeywordsBySampleId(String sampleId)
			throws Exception {
		// check whether user has access to the sample
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException(
					"User doesn't have access to the sample.");
		}
		List<Keyword> keywords = new ArrayList<Keyword>();

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		Sample sample = null;
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
			keywords.addAll(sample.getKeywordCollection());
		}
		return keywords;
	}

	public PointOfContact findPrimaryPointOfContactBySampleId(String sampleId)
			throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		PointOfContact poc = null;
		for (Object obj : results) {
			Sample sample = (Sample) obj;
			poc = sample.getPrimaryPointOfContact();
		}
		return poc;
	}

	public List<PointOfContact> findOtherPointOfContactsBySampleId(
			String sampleId) throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		List<PointOfContact> pointOfContacts = new ArrayList<PointOfContact>();
		for (Object obj : results) {
			Sample sample = (Sample) obj;
			Collection<PointOfContact> otherPOCs = sample
					.getOtherPointOfContactCollection();
			for (PointOfContact poc : otherPOCs) {
				pointOfContacts.add(poc);
			}
		}
		return pointOfContacts;
	}

	public Sample findSampleById(String sampleId) throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		
		logger.debug("===============Finding a sample by id: " + System.currentTimeMillis());
		Sample sample = null;
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"sampleComposition.nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);

		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"sampleComposition.functionalizingEntityCollection.functionCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		return sample;
	}
	
	
	public Sample findSampleBasicById(String sampleId) throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		Sample sample = null;
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
	
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
		}
		return sample;
	}

	public int getNumberOfPublicSamples() throws Exception {
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getAllPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.particle.Sample");
		List results = appService.query(crit);
		List<String> publicIds = new ArrayList<String>();
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, id)) {
				publicIds.add(id);
			}
		}
		return publicIds.size();
	}

	public int getNumberOfPublicSampleSources() throws Exception {
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getAllPublicData();
		DetachedCriteria crit = DetachedCriteria.forClass(PointOfContact.class);
		crit.setFetchMode("organization", FetchMode.JOIN);
		List results = appService.query(crit);
		// get organizations associated with public point of contacts
		List<PointOfContact> publicPOCs = new ArrayList<PointOfContact>();
		for (Object obj : results) {
			PointOfContact poc = (PointOfContact) obj;
			if (StringUtils.containsIgnoreCase(publicData, poc.getId()
					.toString())) {
				publicPOCs.add(poc);
			}
		}
		Set<Organization> publicOrgs = new HashSet<Organization>();
		for (PointOfContact poc : publicPOCs) {
			publicOrgs.add(poc.getOrganization());
		}
		return publicOrgs.size();
	}

	public String[] getSampleViewStrs(Sample sample) {
		List<String> columns = new ArrayList<String>(7);
		columns.clear();
		columns.add(sample.getId().toString());
		columns.add(sample.getName());
		if (sample.getPrimaryPointOfContact() != null) {
			PointOfContact primaryPOC = sample.getPrimaryPointOfContact();
			columns.add(primaryPOC.getFirstName());
			columns.add(primaryPOC.getLastName());
			columns.add(primaryPOC.getOrganization().getName());
		} else {
			columns.add(null);
			columns.add(null);
			columns.add(null);
		}
		columns.add(StringUtils.join(
				getStoredNanomaterialEntityClassNames(sample),
				Constants.VIEW_CLASSNAME_DELIMITER));
		columns.add(StringUtils.join(
				getStoredFunctionalizingEntityClassNames(sample),
				Constants.VIEW_CLASSNAME_DELIMITER));
		columns.add(StringUtils.join(getStoredFunctionClassNames(sample),
				Constants.VIEW_CLASSNAME_DELIMITER));
		columns.add(StringUtils.join(
				getStoredChemicalAssociationClassNames(sample),
				Constants.VIEW_CLASSNAME_DELIMITER));
		columns.add(StringUtils.join(
				getStoredCharacterizationClassNames(sample),
				Constants.VIEW_CLASSNAME_DELIMITER));
		return columns.toArray(new String[0]);
	}

	public Integer[] convertToFunctionalizingEntityClassOrderNumber(
			String[] classNames) {
		Integer[] orderNumbers = new Integer[classNames.length];
		int i = 0;
		for (String name : classNames) {
			orderNumbers[i] = Constants.FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP
					.get(name);
			i++;
		}
		return orderNumbers;
	}

	public Organization findOrganizationByName(String orgName) throws Exception {
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Organization.class);
		crit.add(Restrictions.eq("name", orgName).ignoreCase());
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List results = appService.query(crit);
		Organization org = null;
		for (Object obj : results) {
			org = (Organization) obj;
		}
		return org;
	}

	public PointOfContact findPointOfContactById(String pocId) throws Exception {
		PointOfContact poc = null;

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(PointOfContact.class)
				.add(Property.forName("id").eq(new Long(pocId)));
		crit.setFetchMode("organization", FetchMode.JOIN);
		List results = appService.query(crit);
		for (Object obj : results) {
			poc = (PointOfContact) obj;
		}
		return poc;
	}

	public List<PointOfContact> findPointOfContactsBySampleId(String sampleId)
			throws Exception {
		if (!StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
			throw new NoAccessException("User has no access to the sample "
					+ sampleId);
		}
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		List<PointOfContact> pointOfContacts = new ArrayList<PointOfContact>();
		for (Object obj : results) {
			Sample sample = (Sample) obj;
			PointOfContact primaryPOC = sample.getPrimaryPointOfContact();
			pointOfContacts.add(primaryPOC);
			Collection<PointOfContact> otherPOCs = sample
					.getOtherPointOfContactCollection();
			pointOfContacts.addAll(otherPOCs);
		}
		return pointOfContacts;
	}

	/**
	 * search sampleNames based on sample name str. The str can contain just a
	 * partial sample name or multiple partial names one per line
	 * 
	 * @param nameStr
	 * @return
	 * @throws Exception
	 */
	public List<String> findSampleNamesBy(String nameStr) throws Exception {
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class);
		if (!StringUtils.isEmpty(nameStr)) {
			// split nameStr to multiple words if needed
			List<String> nameStrs = StringUtils.parseToWords(nameStr, "\n");
			if (nameStrs.size() == 1) {
				crit.add(Restrictions
						.ilike("name", nameStr, MatchMode.ANYWHERE));
			} else {
				Disjunction disjunction = Restrictions.disjunction();
				for (String str : nameStrs) {
					Criterion strCrit = Restrictions.ilike("name", str,
							MatchMode.ANYWHERE);
					disjunction.add(strCrit);
				}
				crit.add(disjunction);
			}
		}
		List results = appService.query(crit);
		List<String> sampleNames = new ArrayList<String>();
		for (Object obj : results) {
			Sample sample = (Sample) obj;
			if (StringUtils.containsIgnoreCase(getAccessibleData(), sample
					.getId().toString())) {
				sampleNames.add(sample.getName());
			} else {
				logger.debug("User doesn't have access to sample of name: "
						+ sample.getName());
			}
		}
		return sampleNames;
	}

	public Set<String> findOtherSamplesFromSamePrimaryOrganization(
			String sampleId) throws Exception {
		Set<String> otherSamples = new TreeSet<String>();

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select other.name, other.id from gov.nih.nci.cananolab.domain.particle.Sample as other "
						+ "where exists ("
						+ "select sample.name from gov.nih.nci.cananolab.domain.particle.Sample as sample "
						+ "where sample.primaryPointOfContact.organization.name=other.primaryPointOfContact.organization.name and sample.id="
						+ sampleId + " and other.name!=sample.name)");
		List results = appService.query(crit);
		for (Object obj : results) {
			Object[] row = (Object[]) obj;
			String name = row[0].toString();
			String id = row[1].toString();
			if (StringUtils.containsIgnoreCase(getAccessibleData(), id)) {
				otherSamples.add(name);
			} else {
				logger.debug("User doesn't have access to sample of name: "
						+ name);
			}
		}
		return otherSamples;
	}

	public PointOfContact findPointOfContactByNameAndOrg(String firstName,
			String lastName, String orgName) throws Exception {
		PointOfContact poc = null;

		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(PointOfContact.class);
		crit.createAlias("organization", "organization");
		if (!StringUtils.isEmpty(lastName))
			crit.add(Restrictions.eq("lastName", lastName));
		if (!StringUtils.isEmpty(firstName))
			crit.add(Restrictions.eq("firstName", firstName));
		if (!StringUtils.isEmpty(orgName))
			crit.add(Restrictions.eq("organization.name", orgName));
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List results = appService.query(crit);
		for (Object obj : results) {
			poc = (PointOfContact) obj;
		}
		return poc;
	}

	public List<String> findSampleIdsByOwner(String currentOwner)
			throws Exception {
		List<String> sampleIds = new ArrayList<String>();

		// can't query for the entire Sample object due to
		// limitations in pagination in SDK
		// Sample sample = new Sample();
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("id"));
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class);
		crit.setProjection(Projections.distinct(projectionList));
		Criterion crit1 = Restrictions.eq("createdBy", currentOwner);
		// in case of copy createdBy is like lijowskim:COPY
		Criterion crit2 = Restrictions.like("createdBy", currentOwner + ":",
				MatchMode.START);
		crit.add(Expression.or(crit1, crit2));
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();

		List results = appService.query(crit);
		for (Object obj : results) {
			String id = obj.toString();
			if (StringUtils.containsIgnoreCase(getAccessibleData(), id)) {
				sampleIds.add(id);
			} else {
				logger.debug("User doesn't have access to sample of ID: " + id);
			}
		}
		return sampleIds;
	}
}