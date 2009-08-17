package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.function.OtherFunction;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.CharacterizationQueryBean;
import gov.nih.nci.cananolab.dto.particle.CompositionQueryBean;
import gov.nih.nci.cananolab.dto.particle.SampleQueryBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of SampleService and grid service *
 * 
 * @author pansu, tanq
 * 
 */
public class SampleServiceHelper {
	private AuthorizationService authService;
	private static Logger logger = Logger.getLogger(SampleServiceHelper.class);

	public SampleServiceHelper() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public List<Sample> findSamplesBy(String samplePointOfContact,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList,
			UserBean user) throws Exception {
		List<Sample> samples = new ArrayList<Sample>();

		// can't query for the entire Sample object due to
		// limitations in pagination in SDK
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
				.setProjection(Projections.distinct(Property.forName("name")));

		if (samplePointOfContact != null && samplePointOfContact.length() > 0) {
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
				Criterion pocCrit = Restrictions.ilike(critStr, pocMatchMode
						.getUpdatedText(), pocMatchMode.getMatchMode());
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
						.get("otherFunctionalizingEntity");
				Criterion otherFuncCrit1 = Restrictions.eq("funcEntity.class",
						classOrderNumber);
				Criterion otherFuncCrit2 = Restrictions.in("funcEntity.type",
						otherNanomaterialEntityTypes);
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
				Criterion otherFuncCrit1 = Restrictions.and(Restrictions.eq(
						"inFunc.class", "OtherFunction"), Restrictions.in(
						"inFunc.type", otherFunctionTypes));
				Criterion otherFuncCrit2 = Restrictions.and(Restrictions.eq(
						"func.class", "OtherFunction"), Restrictions.in(
						"func.type", otherFunctionTypes));
				disjunction.add(otherFuncCrit1).add(otherFuncCrit2);
			}
			crit.add(disjunction);
		}

		// join characterization
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0 || wordList != null
				&& wordList.length > 0) {
			crit.createAlias("characterizationCollection", "chara",
					CriteriaSpecification.LEFT_JOIN);
		}

		// characterization
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0) {
			crit
					.add(Restrictions.in("chara.class",
							characterizationClassNames));
		}

		// join keyword, finding, publication
		if (wordList != null && wordList.length > 0) {
			crit.createAlias("keywordCollection", "keyword1");
			crit.createAlias("chara.findingCollection", "finding",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"finding.fileCollection", "charFile",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"charFile.keywordCollection", "keyword2",
					CriteriaSpecification.LEFT_JOIN);
			// publication keywords
			crit.createAlias("publicationCollection", "pub1",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("pub1.keywordCollection", "keyword3",
					CriteriaSpecification.LEFT_JOIN);
		}

		// keyword
		if (wordList != null && wordList.length > 0) {
			// turn words into upper case before searching keywords
			String[] upperKeywords = new String[wordList.length];
			for (int i = 0; i < wordList.length; i++) {
				upperKeywords[i] = wordList[i].toUpperCase();
			}
			Disjunction disjunction = Restrictions.disjunction();
			for (String keyword : upperKeywords) {
				Criterion keywordCrit1 = Restrictions.like("keyword1.name",
						keyword, MatchMode.ANYWHERE);
				Criterion keywordCrit2 = Restrictions.like("keyword2.name",
						keyword, MatchMode.ANYWHERE);
				Criterion keywordCrit3 = Restrictions.like("keyword3.name",
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

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		List filteredResults = new ArrayList(results);
		// get public data
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			String sampleName = obj.toString();
			try {
				Sample sample = findSampleByName(sampleName, user);
				samples.add(sample);
			} catch (NoAccessException e) {
				// ignore no access exception
				logger.debug("User doesn't have access to sample with name "
						+ sampleName);
			}
		}
		Collections.sort(samples, new Comparators.SampleNameComparator());
		return samples;
	}

	public List<String> findSampleNamesBy(String samplePointOfContact,
			String[] nanomaterialEntityClassNames,
			String[] otherNanomaterialEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList,
			UserBean user) throws Exception {
		List<String> sampleNames = new ArrayList<String>();

		// can't query for the entire Sample object due to
		// limitations in pagination in SDK
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
				.setProjection(Projections.distinct(Property.forName("name")));

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
				Criterion pocCrit = Restrictions.ilike(critStr, pocMatchMode
						.getUpdatedText(), pocMatchMode.getMatchMode());
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
						.get("otherFunctionalizingEntity");
				Criterion otherFuncCrit1 = Restrictions.eq("funcEntity.class",
						classOrderNumber);
				Criterion otherFuncCrit2 = Restrictions.in("funcEntity.type",
						otherNanomaterialEntityTypes);
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
				Criterion otherFuncCrit1 = Restrictions.and(Restrictions.eq(
						"inFunc.class", "OtherFunction"), Restrictions.in(
						"inFunc.type", otherFunctionTypes));
				Criterion otherFuncCrit2 = Restrictions.and(Restrictions.eq(
						"func.class", "OtherFunction"), Restrictions.in(
						"func.type", otherFunctionTypes));
				disjunction.add(otherFuncCrit1).add(otherFuncCrit2);
			}
			crit.add(disjunction);
		}

		// join characterization
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0 || wordList != null
				&& wordList.length > 0) {
			crit.createAlias("characterizationCollection", "chara",
					CriteriaSpecification.LEFT_JOIN);
		}

		// characterization
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0) {
			crit
					.add(Restrictions.in("chara.class",
							characterizationClassNames));
		}

		// join keyword, finding, publication
		if (wordList != null && wordList.length > 0) {
			crit.createAlias("keywordCollection", "keyword1");
			crit.createAlias("chara.findingCollection", "finding",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"finding.fileCollection", "charFile",
					CriteriaSpecification.LEFT_JOIN).createAlias(
					"charFile.keywordCollection", "keyword2",
					CriteriaSpecification.LEFT_JOIN);
			// publication keywords
			crit.createAlias("publicationCollection", "pub1",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("pub1.keywordCollection", "keyword3",
					CriteriaSpecification.LEFT_JOIN);
		}

		// keyword
		if (wordList != null && wordList.length > 0) {
			// turn words into upper case before searching keywords
			String[] upperKeywords = new String[wordList.length];
			for (int i = 0; i < wordList.length; i++) {
				upperKeywords[i] = wordList[i].toUpperCase();
			}
			Disjunction disjunction = Restrictions.disjunction();
			for (String keyword : upperKeywords) {
				Criterion keywordCrit1 = Restrictions.like("keyword1.name",
						keyword, MatchMode.ANYWHERE);
				Criterion keywordCrit2 = Restrictions.like("keyword2.name",
						keyword, MatchMode.ANYWHERE);
				Criterion keywordCrit3 = Restrictions.like("keyword3.name",
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

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		List filteredResults = new ArrayList(results);
		// get public data
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			String sampleName = obj.toString();
			if (user == null
					|| authService.checkReadPermission(user, sampleName)) {
				sampleNames.add(sampleName);
			} else { // ignore no access exception
				logger.debug("User doesn't have access to sample with name "
						+ sampleName);
			}
		}
		Collections.sort(sampleNames, new Comparators.SortableNameComparator());
		return sampleNames;
	}

	/**
	 * Return all stored functionalizing entity class names. In case of
	 * OtherFunctionalizingEntity, store the OtherFunctionalizingEntity type
	 * 
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredFunctionalizingEntityClassNames(
			Sample particleSample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null
				&& particleSample.getSampleComposition()
						.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : particleSample
					.getSampleComposition()
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
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredFunctionClassNames(Sample particleSample) {
		SortedSet<String> storedFunctions = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null) {
			if (particleSample.getSampleComposition()
					.getNanomaterialEntityCollection() != null) {
				for (NanomaterialEntity entity : particleSample
						.getSampleComposition()
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
			if (particleSample.getSampleComposition()
					.getFunctionalizingEntityCollection() != null) {
				for (FunctionalizingEntity entity : particleSample
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
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredNanomaterialEntityClassNames(
			Sample particleSample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null
				&& particleSample.getSampleComposition()
						.getNanomaterialEntityCollection() != null) {
			for (NanomaterialEntity entity : particleSample
					.getSampleComposition().getNanomaterialEntityCollection()) {
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

	public SortedSet<String> getStoredCharacterizationClassNames(Sample particle) {
		SortedSet<String> storedChars = new TreeSet<String>();
		if (particle.getCharacterizationCollection() != null) {
			for (Characterization achar : particle
					.getCharacterizationCollection()) {
				storedChars.add(ClassUtils.getShortClassName(achar.getClass()
						.getCanonicalName()));
			}
		}
		return storedChars;
	}

	public Sample findSampleByName(String sampleName, UserBean user)
			throws Exception {
		Sample sample = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("name").eq(sampleName));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
						FetchMode.JOIN);

		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
			if (authService.checkReadPermission(user, sample.getName())) {
				return sample;
			} else {
				throw new NoAccessException();
			}
		}
		return sample;
	}

	public List<Keyword> findKeywordsBySampleId(String sampleId, UserBean user)
			throws Exception {
		List<Keyword> keywords = new ArrayList<Keyword>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		Sample sample = null;
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
			// check whether user has access to the sample
			if (authService.checkReadPermission(user, sample.getName())) {
				keywords.addAll(sample.getKeywordCollection());
			} else {
				throw new NoAccessException(
						"User doesn't have access to the sample.");
			}
		}
		return keywords;
	}

	public PointOfContact findPrimaryPointOfContactBySampleId(String sampleId,
			UserBean user) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select aSample.primaryPointOfContact from gov.nih.nci.cananolab.domain.particle.Sample aSample where aSample.id = "
						+ sampleId);
		List result = appService.query(crit);
		PointOfContact poc = null;
		if (!result.isEmpty()) {
			poc = (PointOfContact) result.get(0);
			if (authService.checkReadPermission(user, poc.getId().toString())) {
				return poc;
			} else {
				throw new NoAccessException();
			}
		}
		return poc;
	}

	public List<PointOfContact> findOtherPointOfContactBySampleId(
			String sampleId, UserBean user) throws Exception {
		List<PointOfContact> pocs = new ArrayList<PointOfContact>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select aSample.otherPointOfContactCollection from gov.nih.nci.cananolab.domain.particle.Sample aSample where aSample.id = "
						+ sampleId);
		List results = appService.query(crit);
		List filteredResults = new ArrayList(results);
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			PointOfContact poc = (PointOfContact) obj;

		}
		return pocs;
	}

	public int getNumberOfPublicSamples() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getAllPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select name from gov.nih.nci.cananolab.domain.particle.Sample");
		List results = appService.query(crit);
		List<String> publicNames = new ArrayList<String>();
		for (Object obj : results) {
			String name = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, name)) {
				publicNames.add(name);
			}
		}
		return publicNames.size();
	}

	public String[] getCharacterizationClassNames(String sampleId)
			throws Exception {
		String hql = "select distinct achar.class from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar"
				+ " where achar.sample.id = " + sampleId;
		return this.getClassNames(hql);
	}

	public String[] getFunctionalizingEntityClassNames(String sampleId)
			throws Exception {
		SortedSet<String> names = new TreeSet<String>();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			Sample particleSample = (Sample) obj;
			names = this
					.getStoredFunctionalizingEntityClassNames(particleSample);
		}
		return names.toArray(new String[0]);
	}

	public String[] getFunctionClassNames(String sampleId) throws Exception {
		SortedSet<String> names = new TreeSet<String>();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			Sample particleSample = (Sample) obj;
			names = this.getStoredFunctionClassNames(particleSample);
		}
		return names.toArray(new String[0]);
	}

	public String[] getNanomaterialEntityClassNames(String sampleId)
			throws Exception {
		String hql = "select distinct entity.class from "
				+ " gov.nih.nci.cananolab.domain.particle.NanomaterialEntity entity"
				+ " where entity.class!='OtherNanomaterialEntity' and entity.sampleComposition.sample.id = "
				+ sampleId;

		String[] classNames = this.getClassNames(hql);
		SortedSet<String> names = new TreeSet<String>();
		if (classNames.length > 0) {
			names.addAll(Arrays.asList(classNames));
		}
		String hql2 = "select distinct entity.type from "
				+ " gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity entity"
				+ " where entity.sampleComposition.sample.id = " + sampleId;
		String[] otherTypes = this.getClassNames(hql2);
		if (otherTypes.length > 0) {
			names.addAll(Arrays.asList(otherTypes));
		}
		return names.toArray(new String[0]);
	}

	private String[] getClassNames(String hql) throws Exception {
		String[] classNames = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		if (results != null) {
			classNames = new String[results.size()];
		} else {
			classNames = new String[0];
		}
		int i = 0;
		for (Object obj : results) {
			classNames[i] = (String) obj.toString();
			i++;
		}
		return classNames;
	}

	public String[] getSampleViewStrs(List<Sample> samples) {
		List<String> sampleStrings = new ArrayList<String>(samples.size());

		List<String> columns = new ArrayList<String>(7);
		for (Sample sample : samples) {
			columns.clear();
			columns.add(sample.getId().toString());
			columns.add(sample.getName());
			PointOfContactBean primaryPOC = new PointOfContactBean(sample
					.getPrimaryPointOfContact());
			columns.add(primaryPOC.getDomain().getFirstName());
			columns.add(primaryPOC.getDomain().getLastName());
			columns.add(primaryPOC.getDomain().getOrganization().getName());
			// nanomaterial entities and functionalizing entities are in one
			// column.
			SortedSet<String> entities = new TreeSet<String>();
			entities.addAll(getStoredNanomaterialEntityClassNames(sample));
			entities.addAll(getStoredFunctionalizingEntityClassNames(sample));
			columns.add(StringUtils.join(entities,
					Constants.VIEW_CLASSNAME_DELIMITER));
			columns.add(StringUtils.join(getStoredFunctionClassNames(sample),
					Constants.VIEW_CLASSNAME_DELIMITER));
			columns.add(StringUtils.join(
					getStoredCharacterizationClassNames(sample),
					Constants.VIEW_CLASSNAME_DELIMITER));

			sampleStrings.add(StringUtils.joinEmptyItemIncluded(columns,
					Constants.VIEW_COL_DELIMITER));
		}
		return sampleStrings.toArray(new String[0]);
	}

	public String[] getSampleViewStrs(Sample sample) {
		List<String> columns = new ArrayList<String>(7);
		columns.clear();
		columns.add(sample.getId().toString());
		columns.add(sample.getName());
		PointOfContactBean primaryPOC = new PointOfContactBean(sample
				.getPrimaryPointOfContact());
		columns.add(primaryPOC.getDomain().getFirstName());
		columns.add(primaryPOC.getDomain().getLastName());
		columns.add(primaryPOC.getDomain().getOrganization().getName());
		// nanomaterial entities and functionalizing entities are in one
		// column.
		SortedSet<String> entities = new TreeSet<String>();
		entities.addAll(getStoredNanomaterialEntityClassNames(sample));
		entities.addAll(getStoredFunctionalizingEntityClassNames(sample));
		columns.add(StringUtils.join(entities,
				Constants.VIEW_CLASSNAME_DELIMITER));
		columns.add(StringUtils.join(getStoredFunctionClassNames(sample),
				Constants.VIEW_CLASSNAME_DELIMITER));
		columns.add(StringUtils.join(
				getStoredCharacterizationClassNames(sample),
				Constants.VIEW_CLASSNAME_DELIMITER));
		return columns.toArray(new String[0]);
	}

	public AuthorizationService getAuthService() {
		return authService;
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

	public Organization findOrganizationByName(String orgName, UserBean user)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Organization.class);
		crit.add(Restrictions.eq("name", orgName));
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List results = appService.query(crit);
		Organization org = null;
		for (Object obj : results) {
			org = (Organization) obj;
			if (authService.checkReadPermission(user, org.getId().toString())) {
				return org;
			} else {
				throw new NoAccessException();
			}
		}
		return org;
	}

	public PointOfContact findPointOfContactByNameAndOrg(String firstName,
			String lastName, String orgName, UserBean user) throws Exception {
		PointOfContact poc = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
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
			if (authService.checkReadPermission(user, poc.getId().toString())) {
				return poc;
			} else {
				throw new NoAccessException();
			}
		}
		return poc;
	}

	private Junction getSampleJunction(AdvancedSampleSearchBean searchBean,
			DetachedCriteria crit) throws Exception {
		if (searchBean.getSampleQueries().isEmpty()) {
			return null;
		}
		Junction junction = null;
		Disjunction sampleDisjunction = Restrictions.disjunction();
		Conjunction sampleConjunction = Restrictions.conjunction();
		// join POC
		if (searchBean.getHasPOC()) {
			crit.createAlias("primaryPointOfContact", "pointOfContact");
			crit.createAlias("pointOfContact.organization", "organization");
			crit.createAlias("otherPointOfContactCollection", "otherPoc",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("otherPoc.organization", "otherOrg",
					CriteriaSpecification.LEFT_JOIN);
		}
		String pocCritStrs[] = { "pointOfContact.lastName",
				"pointOfContact.firstName", "organization.name",
				"otherPoc.lastName", "otherPoc.firstName", "otherOrg.name" };
		for (SampleQueryBean query : searchBean.getSampleQueries()) {
			TextMatchMode nameMatchMode = null;
			if (query.getOperand().equals("equals")) {
				nameMatchMode = new TextMatchMode(query.getName());
			} else if (query.getOperand().equals("contains")) {
				nameMatchMode = new TextMatchMode("*" + query.getName() + "*");
			}
			Criterion sampleNameCrit = null;
			Disjunction pocDisjunction = null;
			if (query.getNameType().equals("sample name")) {
				sampleNameCrit = Restrictions.ilike("name", nameMatchMode
						.getUpdatedText(), nameMatchMode.getMatchMode());
			} else if (query.getNameType().equals("point of contact name")) {
				pocDisjunction = Restrictions.disjunction();
				for (String critStr : pocCritStrs) {
					Criterion pocCrit = Restrictions.ilike(critStr,
							nameMatchMode.getUpdatedText(), nameMatchMode
									.getMatchMode());
					pocDisjunction.add(pocCrit);
				}
			}
			if (sampleNameCrit != null) {
				sampleDisjunction.add(sampleNameCrit);
				sampleConjunction.add(sampleNameCrit);
			}
			if (pocDisjunction != null) {
				sampleDisjunction.add(pocDisjunction);
				sampleConjunction.add(pocDisjunction);
			}
		}
		if (searchBean.getSampleLogicalOperator().equals("and")) {
			junction = sampleConjunction;
		} else if (searchBean.getSampleLogicalOperator().equals("or")) {
			junction = sampleDisjunction;
		}
		return junction;
	}

	private Junction getCompositionJunction(
			AdvancedSampleSearchBean searchBean, DetachedCriteria crit)
			throws Exception {
		if (searchBean.getCompositionQueries().isEmpty()) {
			return null;
		}
		Disjunction compDisjunction = Restrictions.disjunction();
		Conjunction compConjunction = Restrictions.conjunction();
		// composition queries
		crit.createAlias("sampleComposition", "comp",
				CriteriaSpecification.LEFT_JOIN);
		Boolean hasChemicalName = searchBean.getHasChemicalName();
		// join function
		if (searchBean.getHasFunction()) {
			crit.createAlias("comp.nanomaterialEntityCollection", "nanoEntity",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("nanoEntity.composingElementCollection",
					"compElement", CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("compElement.inherentFunctionCollection",
					"inherentFunction", CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("comp.functionalizingEntityCollection",
					"funcEntity", CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("funcEntity.functionCollection", "function",
					CriteriaSpecification.LEFT_JOIN);
		}
		// join nanomaterialEntity
		if (searchBean.getHasNanomaterial() && !searchBean.getHasFunction()) {
			crit.createAlias("comp.nanomaterialEntityCollection", "nanoEntity",
					CriteriaSpecification.LEFT_JOIN);
			if (hasChemicalName) {
				crit.createAlias("nanoEntity.composingElementCollection",
						"compElement", CriteriaSpecification.LEFT_JOIN);
			}
		}
		// join functionalizing entity
		if (searchBean.getHasAgentMaterial() && !searchBean.getHasFunction()) {
			crit.createAlias("comp.functionalizingEntityCollection",
					"funcEntity", CriteriaSpecification.LEFT_JOIN);
		}

		for (CompositionQueryBean compQuery : searchBean
				.getCompositionQueries()) {
			TextMatchMode chemicalNameMatchMode = null;
			if (compQuery.getOperand().equals("equals")) {
				chemicalNameMatchMode = new TextMatchMode(compQuery
						.getChemicalName());
			} else if (compQuery.getOperand().equals("contains")) {
				chemicalNameMatchMode = new TextMatchMode("*"
						+ compQuery.getChemicalName() + "*");
			}
			// function
			if (compQuery.getCompositionType().equals("function")) {
				Criterion funcCrit = this.getFunctionCriterion(compQuery
						.getEntityType());
				if (funcCrit != null) {
					compConjunction.add(funcCrit);
					compDisjunction.add(funcCrit);
				}
			}
			// nanomaterial entity
			else if (compQuery.getCompositionType().equals(
					"nanomaterial entity")) {
				String nanoEntityClassName = ClassUtils
						.getShortClassNameFromDisplayName(compQuery
								.getEntityType());
				Class clazz = ClassUtils.getFullClass("nanomaterial."
						+ nanoEntityClassName);
				Criterion nanoEntityCrit = null;
				// other entity type
				if (clazz == null) {
					Criterion otherNanoCrit1 = Restrictions.eq(
							"nanoEntity.class", "OtherNanomaterialEntity");
					Criterion otherNanoCrit2 = Restrictions.eq(
							"nanoEntity.type", compQuery.getEntityType());
					nanoEntityCrit = Restrictions.and(otherNanoCrit1,
							otherNanoCrit2);
				} else {
					nanoEntityCrit = Restrictions.eq("nanoEntity.class",
							nanoEntityClassName);
				}
				if (hasChemicalName) {
					Criterion chemicalNameCrit = Restrictions.ilike(
							"compElement.name", chemicalNameMatchMode
									.getUpdatedText(), chemicalNameMatchMode
									.getMatchMode());
					nanoEntityCrit = Restrictions.and(nanoEntityCrit,
							chemicalNameCrit);
				}
				if (nanoEntityCrit != null) {
					compConjunction.add(nanoEntityCrit);
					compDisjunction.add(nanoEntityCrit);
				}
			}
			// functionalizing entity
			else if (compQuery.getCompositionType().equals(
					"functionalizing entity")) {
				String funcEntityClassName = ClassUtils
						.getShortClassNameFromDisplayName(compQuery
								.getEntityType());
				Class clazz = ClassUtils.getFullClass("agentmaterial."
						+ funcEntityClassName);
				Criterion funcEntityCrit = null;
				// other entity type
				if (clazz == null) {
					Criterion otherFuncCrit1 = Restrictions.eq(
							"funcEntity.class", "OtherFunctionalizingEntity");
					Criterion otherFuncCrit2 = Restrictions.eq(
							"funcEntity.type", compQuery.getEntityType());
					funcEntityCrit = Restrictions.and(otherFuncCrit1,
							otherFuncCrit2);
				} else {
					Integer funcClassNameInteger = Constants.FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP
							.get(funcEntityClassName);
					funcEntityCrit = Restrictions.eq("funcEntity.class",
							funcClassNameInteger);
				}
				if (hasChemicalName) {
					Criterion chemicalNameCrit = Restrictions.ilike(
							"funcEntity.name", chemicalNameMatchMode
									.getUpdatedText(), chemicalNameMatchMode
									.getMatchMode());
					funcEntityCrit = Restrictions.and(funcEntityCrit,
							chemicalNameCrit);
				}
				if (funcEntityCrit != null) {
					compConjunction.add(funcEntityCrit);
					compDisjunction.add(funcEntityCrit);
				}
			}
		}
		Junction junction = (searchBean.getCompositionLogicalOperator()
				.equals("and")) ? compConjunction : compDisjunction;
		return junction;
	}

	private Criterion getFunctionCriterion(String functionName)
			throws Exception {
		String funcClassName = ClassUtils
				.getShortClassNameFromDisplayName(functionName);
		Class clazz = ClassUtils.getFullClass(funcClassName);
		Criterion funcCrit, funcCrit1, funcCrit2 = null;
		// other function type
		if (clazz == null) {
			// inherent function
			Criterion otherFuncCrit1 = Restrictions.eq(
					"inherentFunction.class", "OtherNanomaterialEntity");
			Criterion otherFuncCrit2 = Restrictions.eq("inherentFunction.type",
					functionName);
			funcCrit1 = Restrictions.and(otherFuncCrit1, otherFuncCrit2);
			// function
			Criterion otherFuncCrit3 = Restrictions.eq("function.class",
					"OtherFunctionalizingEntity");
			Criterion otherFuncCrit4 = Restrictions.eq("function.type",
					functionName);
			funcCrit2 = Restrictions.and(otherFuncCrit3, otherFuncCrit4);
		} else {
			funcCrit1 = Restrictions
					.eq("inherentFunction.class", funcClassName);
			funcCrit2 = Restrictions.eq("function.class", funcClassName);
		}
		funcCrit = Restrictions.or(funcCrit1, funcCrit2);
		return funcCrit;
	}

	private Junction getCharacterizationJunction(
			AdvancedSampleSearchBean searchBean, DetachedCriteria crit) {
		if (searchBean.getCharacterizationQueries().isEmpty()) {
			return null;
		}
		Disjunction charDisjunction = Restrictions.disjunction();
		Conjunction charConjunction = Restrictions.conjunction();
		// join characterization
		crit.createAlias("characterizationCollection", "chara",
				CriteriaSpecification.LEFT_JOIN);

		// join finding and datum
		if (searchBean.getHasDatum()) {
			crit.createAlias("chara.findingCollection", "finding",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("finding.datumCollection", "datum",
					CriteriaSpecification.LEFT_JOIN);
		}
		boolean hasDatum = searchBean.getHasDatum();
		for (CharacterizationQueryBean charQuery : searchBean
				.getCharacterizationQueries()) {
			String charClassName = ClassUtils
					.getShortClassNameFromDisplayName(charQuery
							.getCharacterizationName());
			Criterion charCrit = null;
			if (charClassName == null) {
				Criterion otherCharCrit1 = Restrictions.eq("chara.class",
						"OtherCharacterization");
				Criterion otherCharCrit2 = Restrictions.eq("chara.name",
						charQuery.getCharacterizationName());
				charCrit = Restrictions.and(otherCharCrit1, otherCharCrit2);
			} else {
				charCrit = Restrictions.eq("chara.class", charClassName);
			}
			// datum name
			if (hasDatum && !StringUtils.isEmpty(charQuery.getDatumName())) {
				charCrit = Restrictions.and(charCrit, Restrictions.eq(
						"datum.name", charQuery.getDatumName()));
			}
			// datum value
			if (hasDatum && !StringUtils.isEmpty(charQuery.getDatumValue())) {
				Float datumValue = new Float(charQuery.getDatumValue());
				charCrit = Restrictions.and(charCrit, Restrictions.eq(
						"datum.valueUnit", charQuery.getDatumValueUnit()));
				if (charQuery.getOperand().equals("=")) {
					charCrit = Restrictions.and(charCrit, Expression.eq(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals(">")) {
					charCrit = Restrictions.and(charCrit, Expression.gt(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals(">=")) {
					charCrit = Restrictions.and(charCrit, Expression.ge(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals("<")) {
					charCrit = Restrictions.and(charCrit, Expression.lt(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals("<=")) {
					charCrit = Restrictions.and(charCrit, Expression.le(
							"datum.value", datumValue));
				}
			}
			if (charCrit != null) {
				charConjunction.add(charCrit);
				charDisjunction.add(charCrit);
			}

		}
		Junction junction = (searchBean.getCharacterizationLogicalOperator()
				.equals("and")) ? charConjunction : charDisjunction;
		return junction;
	}

	public List<String> findSampleNamesByAdvancedSearch(
			AdvancedSampleSearchBean searchBean, UserBean user)
			throws Exception {
		List<String> sampleNames = new ArrayList<String>();
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class)
				.setProjection(Projections.distinct(Property.forName("name")));
		Disjunction disjunction = Restrictions.disjunction();
		Conjunction conjunction = Restrictions.conjunction();
		Junction sampleJunction = getSampleJunction(searchBean, crit);
		Junction compJunction = getCompositionJunction(searchBean, crit);
		Junction charJunction = getCharacterizationJunction(searchBean, crit);

		if (searchBean.getLogicalOperator().equals("and")) {
			if (sampleJunction != null)
				conjunction.add(sampleJunction);
			if (compJunction != null)
				conjunction.add(compJunction);
			if (charJunction != null)
				conjunction.add(charJunction);
			crit.add(conjunction);
		} else if (searchBean.getLogicalOperator().equals("or")) {
			if (sampleJunction != null)
				disjunction.add(sampleJunction);
			if (compJunction != null)
				disjunction.add(compJunction);
			if (charJunction != null)
				disjunction.add(charJunction);
			crit.add(disjunction);
		}

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		List filteredResults = new ArrayList(results);

		// get public data
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			String sampleName = obj.toString();
			if (user == null
					|| authService.checkReadPermission(user, sampleName)) {
				sampleNames.add(sampleName);
			} else { // ignore no access exception
				logger.debug("User doesn't have access to sample with name "
						+ sampleName);
			}
		}
		Collections.sort(sampleNames, new Comparators.SortableNameComparator());
		return sampleNames;
	}

	public List<PointOfContact> findPointOfContactsBy(Sample sample,
			AdvancedSampleSearchBean searchBean) throws Exception {
		if (!searchBean.getHasPOC()) {
			return null;
		}
		List<PointOfContact> pocs = new ArrayList<PointOfContact>();

		// get POCs associated with the sample
		List<PointOfContact> samplePOCs = new ArrayList<PointOfContact>();
		samplePOCs.add(sample.getPrimaryPointOfContact());
		samplePOCs.addAll(sample.getOtherPointOfContactCollection());

		DetachedCriteria crit = DetachedCriteria.forClass(PointOfContact.class);

		crit.createAlias("organization", "organization");

		String pocCritStrs[] = { "lastName", "firstName", "organization.name" };
		Conjunction sampleConjunction = Restrictions.conjunction();
		Disjunction sampleDisjunction = Restrictions.disjunction();
		Junction junction = null;
		for (SampleQueryBean query : searchBean.getSampleQueries()) {
			TextMatchMode nameMatchMode = null;
			if (query.getOperand().equals("equals")) {
				nameMatchMode = new TextMatchMode(query.getName());
			} else if (query.getOperand().equals("contains")) {
				nameMatchMode = new TextMatchMode("*" + query.getName() + "*");
			}
			Disjunction pocDisjunction = null;
			if (query.getNameType().equals("point of contact name")) {
				pocDisjunction = Restrictions.disjunction();
				for (String critStr : pocCritStrs) {
					Criterion pocCrit = Restrictions.ilike(critStr,
							nameMatchMode.getUpdatedText(), nameMatchMode
									.getMatchMode());
					pocDisjunction.add(pocCrit);
				}
			}
			if (pocDisjunction != null) {
				sampleDisjunction.add(pocDisjunction);
				sampleConjunction.add(pocDisjunction);
			}
		}
		if (searchBean.getSampleLogicalOperator().equals("and")) {
			junction = sampleConjunction;
		} else if (searchBean.getSampleLogicalOperator().equals("or")) {
			junction = sampleDisjunction;
		}
		crit.add(junction);
		crit.setFetchMode("organization", FetchMode.JOIN);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			PointOfContact poc = (PointOfContact) result.get(0);
			// check if in sample POCs
			if (samplePOCs.contains(poc)) {
				pocs.add(poc);
			}
		}
		return pocs;
	}

	public AdvancedSampleBean findAdvancedSampleByAdvancedSearch(
			String sampleName, AdvancedSampleSearchBean searchBean,
			UserBean user) throws Exception {
		// load sample first with point of contact info and datum info
		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Restrictions.eq("name", sampleName));
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection", FetchMode.JOIN);
		crit.setFetchMode("otherPointOfContactCollection.organization",
				FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("characterizationCollection.findingCollection",
				FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.findingCollection.datumCollection",
				FetchMode.JOIN);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		Sample sample = null;
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
			if (!getAuthService().checkReadPermission(user, sample.getName())) {
				throw new NoAccessException();
			}
		}

		List<PointOfContact> pocs = findPointOfContactsBy(sample, searchBean);
		List<Function> functions = findFunctionsBy(sampleName, searchBean);
		List<NanomaterialEntity> nanoEntities = findNanomaterialEntitiesBy(
				sampleName, searchBean);
		List<FunctionalizingEntity> funcEntities = findFunctionalizingEntitiesBy(
				sampleName, searchBean);
		List<Characterization> charas = this.findCharacterizationsBy(
				sampleName, searchBean);
		List<Datum> data = findDataBy(sample, searchBean);
		String sampleId = sample.getId().toString();
		AdvancedSampleBean advancedSampleBean = new AdvancedSampleBean(
				sampleName, sampleId, pocs, functions, nanoEntities,
				funcEntities, charas, data, searchBean);
		return advancedSampleBean;
	}

	private List<Function> findFunctionsBy(String sampleName,
			AdvancedSampleSearchBean searchBean) throws Exception {
		List<Function> functions = new ArrayList<Function>();
		if (!searchBean.getHasFunction()) {
			return functions;
		}
		DetachedCriteria crit = DetachedCriteria.forClass(Function.class);
		crit.createAlias("composingElement", "ce",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("ce.nanomaterialEntity", "nanoEntity",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("nanoEntity.sampleComposition", "comp",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("comp.sample", "sample",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("functionalizingEntity", "funcEntity",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("funcEntity.sampleComposition", "comp2",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("comp2.sample", "sample2",
				CriteriaSpecification.LEFT_JOIN);
		crit.add(Restrictions.or(Restrictions.eq("sample.name", sampleName),
				Restrictions.eq("sample2.name", sampleName)));

		Disjunction functionDisjunction = Restrictions.disjunction();
		Conjunction functionConjunction = Restrictions.conjunction();
		for (CompositionQueryBean query : searchBean.getCompositionQueries()) {
			String functionName = query.getEntityType();
			String funcClassName = ClassUtils
					.getShortClassNameFromDisplayName(functionName);
			Class clazz = ClassUtils.getFullClass(funcClassName);
			Criterion funcCrit, funcCrit1, funcCrit2 = null;
			// other function type
			if (clazz == null) {
				funcCrit = Restrictions
						.and(Restrictions.eq("class", "OtherFunction"),
								Restrictions.eq("type", functionName));
			} else {
				funcCrit = Restrictions.eq("class", funcClassName);
			}
			if (funcCrit != null) {
				functionDisjunction.add(funcCrit);
				functionConjunction.add(funcCrit);
			}
		}
		Junction functionJunction = (searchBean.getCompositionLogicalOperator()
				.equals("and")) ? functionConjunction : functionDisjunction;
		crit.add(functionJunction);
		crit.setFetchMode("targetCollection", FetchMode.JOIN);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			Function function = (Function) result.get(0);
			functions.add(function);
		}
		return functions;
	}

	private List<NanomaterialEntity> findNanomaterialEntitiesBy(
			String sampleName, AdvancedSampleSearchBean searchBean)
			throws Exception {
		List<NanomaterialEntity> entities = new ArrayList<NanomaterialEntity>();
		if (!searchBean.getHasNanomaterial()) {
			return entities;
		}
		DetachedCriteria crit = DetachedCriteria
				.forClass(NanomaterialEntity.class);
		crit.createAlias("sampleComposition", "comp",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("comp.sample", "sample",
				CriteriaSpecification.LEFT_JOIN);
		crit.add(Restrictions.eq("sample.name", sampleName));
		Boolean hasChemicalName = searchBean.getHasChemicalName();
		if (hasChemicalName) {
			crit.createAlias("composingElementCollection", "ce",
					CriteriaSpecification.LEFT_JOIN);
		}

		Disjunction entityDisjunction = Restrictions.disjunction();
		Conjunction entityConjunction = Restrictions.conjunction();
		for (CompositionQueryBean query : searchBean.getCompositionQueries()) {
			String entityType = query.getEntityType();
			String nanoEntityClassName = ClassUtils
					.getShortClassNameFromDisplayName(entityType);
			Class clazz = ClassUtils.getFullClass(nanoEntityClassName);
			Criterion nanoEntityCrit = null;
			// other function type
			if (clazz == null) {
				nanoEntityCrit = Restrictions.and(Restrictions.eq("class",
						"OtherNanomaterialEntity"), Restrictions.eq("type",
						entityType));
			} else {
				nanoEntityCrit = Restrictions.eq("class", nanoEntityClassName);
			}
			if (nanoEntityCrit != null) {
				entityDisjunction.add(nanoEntityCrit);
				entityConjunction.add(nanoEntityCrit);
			}
			if (hasChemicalName) {
				TextMatchMode chemicalNameMatchMode = null;
				if (query.getOperand().equals("equals")) {
					chemicalNameMatchMode = new TextMatchMode(query
							.getChemicalName());
				} else if (query.getOperand().equals("contains")) {
					chemicalNameMatchMode = new TextMatchMode("*"
							+ query.getChemicalName() + "*");
				}
				Criterion chemicalNameCrit = Restrictions.ilike(
						"compElement.name", chemicalNameMatchMode
								.getUpdatedText(), chemicalNameMatchMode
								.getMatchMode());
				nanoEntityCrit = Restrictions.and(nanoEntityCrit,
						chemicalNameCrit);
			}

		}
		Junction nanoEntityJunction = (searchBean
				.getCompositionLogicalOperator().equals("and")) ? entityConjunction
				: entityDisjunction;

		crit.add(nanoEntityJunction);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
		crit.setFetchMode("composingElementCollection.inherentFunctionCollection", FetchMode.JOIN);
		crit.setFetchMode("composingElementCollection.inherentFunctionCollection.targetCollection", FetchMode.JOIN);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			NanomaterialEntity entity = (NanomaterialEntity) result.get(0);
			entities.add(entity);
		}
		return entities;
	}

	private List<FunctionalizingEntity> findFunctionalizingEntitiesBy(
			String sampleName, AdvancedSampleSearchBean searchBean)
			throws Exception {
		List<FunctionalizingEntity> entities = new ArrayList<FunctionalizingEntity>();
		if (!searchBean.getHasAgentMaterial()) {
			return entities;
		}
		DetachedCriteria crit = DetachedCriteria
				.forClass(FunctionalizingEntity.class);
		crit.createAlias("sampleComposition", "comp",
				CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("comp.sample", "sample",
				CriteriaSpecification.LEFT_JOIN);
		crit.add(Restrictions.eq("sample.name", sampleName));

		Disjunction entityDisjunction = Restrictions.disjunction();
		Conjunction entityConjunction = Restrictions.conjunction();
		Boolean hasChemicalName = searchBean.getHasChemicalName();
		for (CompositionQueryBean query : searchBean.getCompositionQueries()) {
			String entityType = query.getEntityType();
			String funcEntityClassName = ClassUtils
					.getShortClassNameFromDisplayName(entityType);
			Class clazz = ClassUtils.getFullClass(funcEntityClassName);
			Criterion funcEntityCrit = null;
			// other function type
			if (clazz == null) {
				funcEntityCrit = Restrictions.and(Restrictions.eq("class",
						"OtherFunctionalizingEntity"), Restrictions.eq("type",
						entityType));
			} else {
				Integer funcClassNameInteger = Constants.FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP
						.get(funcEntityClassName);
				funcEntityCrit = Restrictions.eq("class",
						funcClassNameInteger);
			}
			if (funcEntityCrit != null) {
				entityDisjunction.add(funcEntityCrit);
				entityConjunction.add(funcEntityCrit);
			}
			if (hasChemicalName) {
				TextMatchMode chemicalNameMatchMode = null;
				if (query.getOperand().equals("equals")) {
					chemicalNameMatchMode = new TextMatchMode(query
							.getChemicalName());
				} else if (query.getOperand().equals("contains")) {
					chemicalNameMatchMode = new TextMatchMode("*"
							+ query.getChemicalName() + "*");
				}
				Criterion chemicalNameCrit = Restrictions.ilike("name",
						chemicalNameMatchMode.getUpdatedText(),
						chemicalNameMatchMode.getMatchMode());
				funcEntityCrit = Restrictions.and(funcEntityCrit,
						chemicalNameCrit);
			}
		}
		Junction funcEntityJunction = (searchBean
				.getCompositionLogicalOperator().equals("and")) ? entityConjunction
				: entityDisjunction;

		crit.add(funcEntityJunction);
		crit.setFetchMode("functionCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			FunctionalizingEntity entity = (FunctionalizingEntity) result
					.get(0);
			entities.add(entity);
		}
		return entities;
	}

	private List<Characterization> findCharacterizationsBy(String sampleName,
			AdvancedSampleSearchBean searchBean) throws Exception {
		List<Characterization> chars = new ArrayList<Characterization>();
		// if hasDatum, directly query datum
		if (searchBean.getCharacterizationQueries().isEmpty()
				|| searchBean.getHasDatum()) {
			return chars;
		}
		DetachedCriteria crit = DetachedCriteria
				.forClass(FunctionalizingEntity.class);
		crit.createAlias("sample", "sample", CriteriaSpecification.LEFT_JOIN);
		crit.add(Restrictions.eq("sample.name", sampleName));

		Disjunction charDisjunction = Restrictions.disjunction();
		Conjunction charConjunction = Restrictions.conjunction();
		for (CharacterizationQueryBean query : searchBean
				.getCharacterizationQueries()) {
			String charName = query.getCharacterizationName();
			String className = ClassUtils
					.getShortClassNameFromDisplayName(charName);
			Class clazz = ClassUtils.getFullClass(className);
			Criterion charCrit = null;
			// other characterization type
			if (clazz == null) {
				charCrit = Restrictions.and(Restrictions.eq("class",
						"OtherCharacterization"), Restrictions.eq("name",
						className));
			} else {
				charCrit = Restrictions.eq("class", className);
			}
			if (charCrit != null) {
				charDisjunction.add(charCrit);
				charConjunction.add(charCrit);
			}
		}
		Junction charJunction = (searchBean.getCompositionLogicalOperator()
				.equals("and")) ? charConjunction : charDisjunction;

		crit.add(charJunction);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			Characterization achar = (Characterization) result.get(0);
			chars.add(achar);
		}
		return chars;
	}

	private List<Datum> findDataBy(Sample sample,
			AdvancedSampleSearchBean searchBean) throws Exception {
		List<Datum> data = new ArrayList<Datum>();
		Boolean hasDatum = searchBean.getHasDatum();
		if (!hasDatum) {
			return data;
		}
		Criterion datumCrit = null;
		DetachedCriteria crit = DetachedCriteria.forClass(Datum.class);
		Disjunction datumDisjunction = Restrictions.disjunction();
		Conjunction datumConjunction = Restrictions.conjunction();
		for (CharacterizationQueryBean charQuery : searchBean
				.getCharacterizationQueries()) {
			// datum name
			if (hasDatum && !StringUtils.isEmpty(charQuery.getDatumName())) {
				datumCrit = Restrictions.and(datumCrit, Restrictions.eq(
						"datum.name", charQuery.getDatumName()));
			}
			// datum value
			if (hasDatum && !StringUtils.isEmpty(charQuery.getDatumValue())) {
				Float datumValue = new Float(charQuery.getDatumValue());
				datumCrit = Restrictions.and(datumCrit, Restrictions.eq(
						"datum.valueUnit", charQuery.getDatumValueUnit()));
				if (charQuery.getOperand().equals("=")) {
					datumCrit = Restrictions.and(datumCrit, Expression.eq(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals(">")) {
					datumCrit = Restrictions.and(datumCrit, Expression.gt(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals(">=")) {
					datumCrit = Restrictions.and(datumCrit, Expression.ge(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals("<")) {
					datumCrit = Restrictions.and(datumCrit, Expression.lt(
							"datum.value", datumValue));
				} else if (charQuery.getOperand().equals("<=")) {
					datumCrit = Restrictions.and(datumCrit, Expression.le(
							"datum.value", datumValue));
				}
			}
			if (datumCrit != null) {
				datumConjunction.add(datumCrit);
				datumDisjunction.add(datumCrit);
			}
		}
		Junction datumJunction = (searchBean.getCompositionLogicalOperator()
				.equals("and")) ? datumConjunction : datumDisjunction;
		crit.add(datumJunction);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			Datum datum = (Datum) result.get(0);
			data.add(datum);
		}
		return data;
	}
}