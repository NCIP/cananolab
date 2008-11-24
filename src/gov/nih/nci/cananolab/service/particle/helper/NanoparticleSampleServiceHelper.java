package gov.nih.nci.cananolab.service.particle.helper;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Helper class providing implementations of search methods needed for both
 * local implementation of NanoparticleSampleService and grid service *
 * 
 * @author pansu, tanq
 * 
 */
public class NanoparticleSampleServiceHelper {
	public List<NanoparticleSample> findNanoparticleSamplesBy(
			String particleSource, String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleEntityTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws Exception {
		List<NanoparticleSample> particles = new ArrayList<NanoparticleSample>();

		// can't query for the entire NanoparticleSample object due to
		// limitations in
		// pagination in SDK
		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).setProjection(
				Projections.distinct(Property.forName("id")));

		// source
		if (particleSource != null && particleSource.length() > 0) {
			TextMatchMode sourceMatchMode = new TextMatchMode(particleSource);
			crit.createAlias("source", "source").add(
					Restrictions.ilike("source.organizationName",
							sourceMatchMode.getUpdatedText(), sourceMatchMode
									.getMatchMode()));
		}

		// join composition and nanoparticle entity
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0
				|| otherNanoparticleEntityTypes != null
				&& otherNanoparticleEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			crit.createAlias("sampleComposition", "comp");
			crit.createAlias("comp.nanoparticleEntityCollection", "nanoEntity");
		}

		// nanoparticle entity
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0
				|| otherNanoparticleEntityTypes != null
				&& otherNanoparticleEntityTypes.length > 0
				|| functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			if (nanoparticleEntityClassNames != null
					&& nanoparticleEntityClassNames.length > 0) {
				Criterion nanoEntityCrit = Restrictions.in("nanoEntity.class",
						nanoparticleEntityClassNames);
				disjunction.add(nanoEntityCrit);
			}
			if (otherNanoparticleEntityTypes != null
					&& otherNanoparticleEntityTypes.length > 0) {
				Criterion otherNanoCrit1 = Restrictions.eq("nanoEntity.class",
						"OtherNanoparticleEntity");
				Criterion otherNanoCrit2 = Restrictions.in("nanoEntity.type",
						otherNanoparticleEntityTypes);
				Criterion otherNanoCrit = Restrictions.and(otherNanoCrit1,
						otherNanoCrit2);
				disjunction.add(otherNanoCrit);
			}
			crit.add(disjunction);
		}

		// function
		if (functionClassNames != null && functionClassNames.length > 0
				|| otherFunctionTypes != null && otherFunctionTypes.length > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			crit.createAlias(
					"sampleComposition.functionalizingEntityCollection",
					"funcEntity", CriteriaSpecification.LEFT_JOIN);
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

		// characterization and text
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0 || wordList != null
				&& wordList.length > 0) {
			crit.createAlias("characterizationCollection", "chara",
					CriteriaSpecification.LEFT_JOIN);
			if (characterizationClassNames != null
					&& characterizationClassNames.length > 0) {
				crit.add(Restrictions.in("chara.class",
						characterizationClassNames));
			}

			if (wordList != null && wordList.length > 0) {
				// turn words into upper case before searching keywords
				String[] upperKeywords = new String[wordList.length];
				for (int i = 0; i < wordList.length; i++) {
					upperKeywords[i] = wordList[i].toUpperCase();
				}
				Disjunction disjunction = Restrictions.disjunction();
				crit.createAlias("keywordCollection", "keyword1",
						CriteriaSpecification.LEFT_JOIN);
				for (String keyword : upperKeywords) {
					Criterion keywordCrit1 = Restrictions.like("keyword1.name",
							keyword, MatchMode.ANYWHERE);
					disjunction.add(keywordCrit1);
				}
				crit.createAlias("chara.derivedBioAssayDataCollection",
						"derived", CriteriaSpecification.LEFT_JOIN)
						.createAlias("derived.File", "charFile",
								CriteriaSpecification.LEFT_JOIN).createAlias(
								"charFile.keywordCollection", "keyword2",
								CriteriaSpecification.LEFT_JOIN);
				;
				for (String keyword : upperKeywords) {
					Criterion keywordCrit2 = Restrictions.like("keyword2.name",
							keyword, MatchMode.ANYWHERE);
					disjunction.add(keywordCrit2);
				}
				for (String word : wordList) {
					Criterion summaryCrit1 = Restrictions.ilike(
							"chara.description", word, MatchMode.ANYWHERE);
					Criterion summaryCrit2 = Restrictions.ilike(
							"charFile.description", word, MatchMode.ANYWHERE);
					Criterion summaryCrit = Restrictions.or(summaryCrit1,
							summaryCrit2);
					disjunction.add(summaryCrit);
				}

				// publication keywords
				crit.createAlias("publicationCollection", "pub1",
						CriteriaSpecification.LEFT_JOIN);
				crit.createAlias("pub1.keywordCollection", "keyword3",
						CriteriaSpecification.LEFT_JOIN);
				for (String keyword : upperKeywords) {
					Criterion keywordCrit3 = Restrictions.like("keyword3.name",
							keyword, MatchMode.ANYWHERE);
					disjunction.add(keywordCrit3);
				}
				crit.add(disjunction);
			}
		}

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);

		// don't need this if Hibernate would've allowed for functionalizing
		// entities in the where clause
		List<String> functionalizingEntityTypes = null;
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0
				|| otherFunctionalizingEntityTypes != null
				&& otherFunctionalizingEntityTypes.length > 0) {
			functionalizingEntityTypes = new ArrayList<String>(Arrays
					.asList(functionalizingEntityClassNames));
			functionalizingEntityTypes.addAll(Arrays
					.asList(otherFunctionalizingEntityTypes));
		}
		for (Object obj : results) {
			NanoparticleSample particle = loadNanoparticleSample(obj.toString());
			// don't need this if Hibernate would've allowed for functionalizing
			// entities in the where clause
			boolean matching = hasMatchingFunctionalizingEntities(particle,
					functionalizingEntityTypes);
			if (matching) {
				particles.add(particle);
			}
		}
		return particles;
	}

	// workaround to filter out publications that don't have matching
	// functionalizing entities due to bug in hibernate in handling having
	// .class in
	// where clause in multi-level inheritance
	private boolean hasMatchingFunctionalizingEntities(
			NanoparticleSample particle, List<String> functionalizingEntityTypes)
			throws Exception {
		if (functionalizingEntityTypes == null) {
			return true;
		}
		boolean status = false;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(FunctionalizingEntity.class);
		crit.createAlias("sampleComposition", "comp");
		crit.createAlias("comp.nanoparticleSample", "sample");
		crit.add(Restrictions.eq("sample.id", particle.getId()));
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			FunctionalizingEntity entity = (FunctionalizingEntity) result
					.get(0);
			String entityName;
			if (entity instanceof OtherFunctionalizingEntity) {
				entityName = ((OtherFunctionalizingEntity) entity).getType();
			} else {
				entityName = ClassUtils.getShortClassName(entity.getClass()
						.getName());
			}
			if (functionalizingEntityTypes.contains(entityName)) {
				return true;
			}
		}
		return status;
	}

	/**
	 * Return all stored functionalizing entity class names. In case of
	 * OtherFunctionalizingEntity, store the OtherFunctionalizingEntity type
	 * 
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredFunctionalizingEntityClassNames(
			NanoparticleSample particleSample) {
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
	public SortedSet<String> getStoredFunctionClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedFunctions = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null) {
			if (particleSample.getSampleComposition()
					.getNanoparticleEntityCollection() != null) {
				for (NanoparticleEntity entity : particleSample
						.getSampleComposition()
						.getNanoparticleEntityCollection()) {
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
	 * Return all stored nanoparticle entity class names. In case of
	 * OtherNanoparticleEntity, store the otherNanoparticleEntity type
	 * 
	 * @param particleSample
	 * @return
	 */
	public SortedSet<String> getStoredNanoparticleEntityClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null
				&& particleSample.getSampleComposition()
						.getNanoparticleEntityCollection() != null) {
			for (NanoparticleEntity entity : particleSample
					.getSampleComposition().getNanoparticleEntityCollection()) {
				if (entity instanceof OtherNanoparticleEntity) {
					storedEntities.add(((OtherNanoparticleEntity) entity)
							.getType());
				} else {
					storedEntities.add(ClassUtils.getShortClassName(entity
							.getClass().getCanonicalName()));
				}
			}
		}
		return storedEntities;
	}

	public SortedSet<String> getStoredCharacterizationClassNames(
			NanoparticleSample particle) {
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

	public NanoparticleSample findNanoparticleSampleById(String particleId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("id").eq(new Long(particleId)));
		crit.setFetchMode("source", FetchMode.JOIN); // eager load not set in
		// caDSR
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode("sampleComposition.fileCollection",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementA",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementB",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		// crit.setFetchMode("publicationCollection.authorCollection",
		// FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		NanoparticleSample particleSample = null;
		if (!result.isEmpty()) {
			particleSample = (NanoparticleSample) result.get(0);
		}
		return particleSample;
	}

	private NanoparticleSample loadNanoparticleSample(String particleId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("id").eq(new Long(particleId)));
		crit.setFetchMode("source", FetchMode.JOIN); // eager load not set in
		// caDSR
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanoparticleEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanoparticleEntityCollection.composingElementCollection.inherentFunctionCollection",
						FetchMode.JOIN);

		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		NanoparticleSample particleSample = null;
		if (!result.isEmpty()) {
			particleSample = (NanoparticleSample) result.get(0);
		}
		return particleSample;
	}

	public NanoparticleSample findNanoparticleSampleByName(String particleName)
			throws Exception {
		NanoparticleSample particleSample = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("name").eq(particleName));
		crit.setFetchMode("source", FetchMode.JOIN); // eager load not set in
		// caDSR
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode("sampleComposition.fileCollection",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementA",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementB",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection.authorCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			particleSample = (NanoparticleSample) result.get(0);
		}
		return particleSample;
	}

	public List<DerivedBioAssayData> findDerivedBioAssayDataByCharId(
			String charId) throws Exception {
		List<DerivedBioAssayData> derivedBioAssayDataCollection = new ArrayList<DerivedBioAssayData>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.derivedBioAssayDataCollection from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		FileServiceHelper fileHelper = new FileServiceHelper();
		for (Object obj : results) {
			DerivedBioAssayData derivedBioAssayData = (DerivedBioAssayData) obj;
			// derivedBioAssayData's File
			File File = findDerivedBioAssayDataFile(derivedBioAssayData
					.getId().toString());

			// File's keyword
			if (File != null) {
				List<Keyword> keywords = fileHelper
						.findKeywordsByFileId(File.getId().toString());
				File.setKeywordCollection(new HashSet<Keyword>(keywords));
				derivedBioAssayData.setFile(File);
			}
			derivedBioAssayDataCollection.add(derivedBioAssayData);
		}
		return derivedBioAssayDataCollection;
	}

	public File findDerivedBioAssayDataFile(String derivedId)
			throws Exception {
		File File = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select bioassay.File from gov.nih.nci.cananolab.domain.common.DerivedBioAssayData bioassay where bioassay.id = "
						+ derivedId);
		List results = appService.query(crit);
		for (Object obj : results) {
			File = (File) obj;
		}
		return File;
	}

	public List<Keyword> findKeywordsForNanoparticleSampleId(
			String particleSampleId) throws Exception {
		List<Keyword> keywords = new ArrayList<Keyword>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select aParticle.keywordCollection from gov.nih.nci.cananolab.domain.particle.NanoparticleSample aParticle where aParticle.id = "
						+ particleSampleId);
		List results = appService.query(crit);
		for (Object obj : results) {
			Keyword keyword = (Keyword) obj;
			keywords.add(keyword);
		}
		return keywords;
	}

	public int getNumberOfPublicNanoparticleSamples() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
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

	public String[] getCharacterizationClassNames(String particleId)
			throws Exception {
		String hql = "select distinct achar.class from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar"
				+ " where achar.nanoparticleSample.id = " + particleId;
		return this.getClassNames(hql);
	}

	public String[] getFunctionalizingEntityClassNames(String particleId)
			throws Exception {
		SortedSet<String> names = new TreeSet<String>();
		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("id").eq(new Long(particleId)));
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			NanoparticleSample particleSample = (NanoparticleSample) obj;
			names = this
					.getStoredFunctionalizingEntityClassNames(particleSample);
		}
		return names.toArray(new String[0]);
	}

	public String[] getFunctionClassNames(String particleId) throws Exception {
		SortedSet<String> names = new TreeSet<String>();
		DetachedCriteria crit = DetachedCriteria.forClass(
				NanoparticleSample.class).add(
				Property.forName("id").eq(new Long(particleId)));
		crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanoparticleEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanoparticleEntityCollection.composingElementCollection.inherentFunctionCollection",
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
			NanoparticleSample particleSample = (NanoparticleSample) obj;
			names = this.getStoredFunctionClassNames(particleSample);
		}
		return names.toArray(new String[0]);
	}

	public String[] getNanoparticleEntityClassNames(String particleId)
			throws Exception {
		String hql = "select distinct entity.class from "
				+ " gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity entity"
				+ " where entity.class!='OtherNanoparticleEntity' and entity.sampleComposition.nanoparticleSample.id = "
				+ particleId;

		String[] classNames = this.getClassNames(hql);
		SortedSet<String> names = new TreeSet<String>();
		if (classNames.length > 0) {
			names.addAll(Arrays.asList(classNames));
		}
		String hql2 = "select distinct entity.type from "
				+ " gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity entity"
				+ " where entity.sampleComposition.nanoparticleSample.id = "
				+ particleId;
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

	public String[] getNanoparticleSampleViewStrs(
			List<NanoparticleSample> particleSamples) {
		List<String> particleStrings = new ArrayList<String>(particleSamples
				.size());
		// 6 columns
		List<String> columns = new ArrayList<String>(6);
		for (NanoparticleSample particleSample : particleSamples) {
			columns.clear();
			columns.add(particleSample.getId().toString());
			columns.add(particleSample.getName());
			columns.add(particleSample.getPrimaryOrganization().getName());

			// nanoparticle entities and functionalizing entities are in one
			// column.
			SortedSet<String> entities = new TreeSet<String>();
			entities
					.addAll(getStoredNanoparticleEntityClassNames(particleSample));
			entities
					.addAll(getStoredFunctionalizingEntityClassNames(particleSample));
			columns.add(StringUtils.join(entities,
					CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER));
			columns.add(StringUtils.join(
					getStoredFunctionClassNames(particleSample),
					CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER));
			columns.add(StringUtils.join(
					getStoredCharacterizationClassNames(particleSample),
					CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER));

			particleStrings.add(StringUtils.joinEmptyItemIncluded(columns,
					CaNanoLabConstants.VIEW_COL_DELIMITER));
		}
		String[] particleStrArray = new String[particleStrings.size()];
		return particleStrings.toArray(particleStrArray);
	}
}