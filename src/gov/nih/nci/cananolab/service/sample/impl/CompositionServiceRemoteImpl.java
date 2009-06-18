package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Local implementation of CompositionService.
 *
 * @author pansu
 *
 */
public class CompositionServiceRemoteImpl implements CompositionService {
	private static Logger logger = Logger
			.getLogger(CompositionServiceRemoteImpl.class);
	private CompositionServiceHelper helper = new CompositionServiceHelper();
	private FileServiceHelper fileHelper = new FileServiceHelper();

	private CaNanoLabServiceClient gridClient;

	public CompositionServiceRemoteImpl(String serviceUrl)
			throws CompositionException {
		try {
			gridClient = new CaNanoLabServiceClient(serviceUrl);
		} catch (Exception e) {
			throw new CompositionException(
					"Can't create grid client succesfully.");
		}
	}

	public void saveNanomaterialEntity(Sample sample,
			NanomaterialEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
			UserBean user) throws CompositionException, NoAccessException {
		NanomaterialEntityBean entityBean = null;
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(entityId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanomaterialEntity entity = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				entity = (NanomaterialEntity) obj;
			}
			if (entity != null)
				loadNanomaterialEntityAssociations(entity);
			entityBean = new NanomaterialEntityBean(entity);
			return entityBean;
		} catch (Exception e) {
			String err = "Problem finding the nanoparticle entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	private void loadNanomaterialEntityAssociations(NanomaterialEntity entity)
			throws Exception {
		File[] files = gridClient.getFilesByCompositionInfoId(entity.getId()
				.toString(), NanomaterialEntity.class.getCanonicalName());
		if (files != null && files.length > 0) {
			entity.setFileCollection(new HashSet<File>(Arrays.asList(files)));
		}
		loadComposingElementForNanomaterialEntity(entity);
	}

	/**
	 * load all ComposingElement for an associated NanomaterialEntity
	 *
	 * @param nanoEntityId
	 * @return
	 * @throws ParticleCompositionException
	 */
	private void loadComposingElementForNanomaterialEntity(
			NanomaterialEntity entity) throws Exception {
		CQLQuery query = new CQLQuery();

		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ComposingElement");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
		association.setRoleName("nanomaterialEntity");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(entity.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ComposingElement");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		ComposingElement composingElement = null;
		entity.setComposingElementCollection(new HashSet<ComposingElement>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			composingElement = (ComposingElement) obj;
			loadInherentFunctionForComposingElement(composingElement);
			entity.getComposingElementCollection().add(composingElement);
		}
	}

	/**
	 * load all InherentFunction for an associated ComposingElement
	 */
	private void loadInherentFunctionForComposingElement(
			ComposingElement composingElement) throws Exception {
		CQLQuery query = new CQLQuery();

		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.particle.Function");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.particle.ComposingElement");
		association.setRoleName("composingElement");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(composingElement.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Function");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		composingElement.setInherentFunctionCollection(new HashSet<Function>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Function function = (Function) obj;
			composingElement.getInherentFunctionCollection().add(function);
		}
	}

	public void saveFunctionalizingEntity(Sample sample,
			FunctionalizingEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void saveChemicalAssociation(Sample sample,
			ChemicalAssociationBean assocBean, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void saveCompositionFile(Sample particleSample, FileBean fileBean,
			UserBean user) throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, UserBean user) throws CompositionException,
			NoAccessException {
		FunctionalizingEntityBean entityBean = null;
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(entityId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			FunctionalizingEntity entity = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				entity = (FunctionalizingEntity) obj;
			}
			if (entity != null)
				loadFunctionalizingEntityAssociations(entity);
			entityBean = new FunctionalizingEntityBean(entity);
			return entityBean;
		} catch (Exception e) {
			String err = "Problem finding the functionalizing entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	/**
	 * load all FunctionalizingEntity Collection with an associated Composition
	 */
	private void loadFunctionalizingEntityForComposition(
			SampleComposition composition, String[] entityClassNames)
			throws Exception {

		composition
				.setFunctionalizingEntityCollection(new HashSet<FunctionalizingEntity>());
		for (String name : entityClassNames) {
			String fullClassName = ClassUtils.getFullClass(
					"functionalization." + name).getCanonicalName();
			if (fullClassName.equals("java.lang.Object")) {
				fullClassName = "gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity";
			}
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName(fullClassName);
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition");
			association.setRoleName("sampleComposition");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(composition.getId().toString());
			association.setAttribute(attribute);
			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results.setTargetClassname(fullClassName);
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			FunctionalizingEntity functionalizingEntity = null;

			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				functionalizingEntity = (FunctionalizingEntity) obj;
				// loadFunctionsForFunctionalizingEntity(functionalizingEntity);
				composition.getFunctionalizingEntityCollection().add(
						functionalizingEntity);
			}
		}
	}

	private void loadFunctionalizingEntityAssociations(
			FunctionalizingEntity entity) throws Exception {
		File[] files = gridClient.getFilesByCompositionInfoId(entity.getId()
				.toString(), FunctionalizingEntity.class.getCanonicalName());
		if (files != null && files.length > 0) {
			entity.setFileCollection(new HashSet<File>(Arrays.asList(files)));
		}
		loadActivationMethodForFunctionalizingEntity(entity);
		loadFunctionsForFunctionalizingEntity(entity);
	}

	private void loadActivationMethodForFunctionalizingEntity(
			FunctionalizingEntity entity) {
	}

	private void loadFunctionsForFunctionalizingEntity(
			FunctionalizingEntity entity) throws Exception {
		CQLQuery query = new CQLQuery();

		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.particle.Function");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
		association.setRoleName("functionalizingEntity");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(entity.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Function");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		entity.setFunctionCollection(new HashSet<Function>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Function function = (Function) obj;
			if (function instanceof TargetingFunction) {
				loadTargetsForTargetingFunction((TargetingFunction) function);
			}
			entity.getFunctionCollection().add(function);
		}
	}

	private void loadTargetsForTargetingFunction(TargetingFunction function)
			throws Exception {
		CQLQuery query = new CQLQuery();

		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.function.Target");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.function.TargingFunction");
		association.setRoleName("targetingFunction");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(function.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.function.Target");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		function.setTargetCollection(new HashSet<Target>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Target theTarget = (Target) obj;
			function.getTargetCollection().add(theTarget);
		}
	}

	public ChemicalAssociationBean findChemicalAssociationById(String assocId,
			UserBean user) throws CompositionException, NoAccessException {
		ChemicalAssociationBean assocBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ChemicalAssociation.class).add(
					Property.forName("id").eq(new Long(assocId)));
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.sample", FetchMode.JOIN);
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			crit.setFetchMode("associatedElementA", FetchMode.JOIN);
			crit.setFetchMode("associatedElementA.nanomaterialEntity",
					FetchMode.JOIN);
			crit.setFetchMode("associatedElementB", FetchMode.JOIN);
			crit.setFetchMode("associatedElementB.nanomaterialEntity",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			ChemicalAssociation assoc = null;
			if (!result.isEmpty()) {
				assoc = (ChemicalAssociation) result.get(0);
				if (helper.getAuthService().checkReadPermission(user,
						assoc.getSampleComposition().getSample().getName())) {
					assocBean = new ChemicalAssociationBean(assoc);
					fileHelper.checkReadPermissionAndRetrieveVisibility(
							assocBean.getFiles(), user);
				} else {
					throw new NoAccessException();
				}
			}
			return assocBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the chemical association by id: "
					+ assocId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteNanomaterialEntity(NanomaterialEntity entity,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void deleteChemicalAssociation(ChemicalAssociation assoc,
			UserBean user) throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void deleteCompositionFile(Sample particleSample, File file,
			UserBean user) throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public CompositionBean findCompositionBySampleId(String sampleId,
			UserBean user) throws CompositionException {
		CompositionBean compositionBean=null;
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			Association association = new Association();
			association.setName("gov.nih.nci.cananolab.domain.particle.Sample");
			association.setRoleName("sample");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(sampleId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			SampleComposition sampleComposition = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sampleComposition = (SampleComposition) obj;
				loadCompositionAssociations(sampleComposition);
				compositionBean=new CompositionBean(sampleComposition);
			}
		} catch (Exception e) {
			String err = "Problem finding the remote sampleComposition by sample id: "
					+ sampleId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return compositionBean;
	}

	private void loadCompositionAssociations(SampleComposition composition) throws Exception {
		loadNanomaterialEntitiesForComposition(composition);
		loadFunctionalizingEntitiesForComposition(composition);
		loadChemicalAssociationsForComposition(composition);
		File[] files = gridClient.getFilesByCompositionInfoId(composition.getId()
				.toString(), SampleComposition.class.getCanonicalName());
		if (files != null && files.length > 0) {
			composition.setFileCollection(new HashSet<File>(Arrays.asList(files)));
		}
	}

	private void loadNanomaterialEntitiesForComposition(SampleComposition composition) throws Exception {

	}

	private void loadFunctionalizingEntitiesForComposition(SampleComposition composition) throws Exception {

	}

	private void loadChemicalAssociationsForComposition(SampleComposition composition) throws Exception {

	}
	/**
	 * load all NanomaterialEntity Collection for an associated Composition
	 *
	 */
	private void loadNanomaterialEntityForComposition(
			SampleComposition composition, String[] entityClassNames)
			throws Exception {
		composition
				.setNanomaterialEntityCollection(new HashSet<NanomaterialEntity>());
		for (String className : entityClassNames) {
			String fullClassName = ClassUtils.getFullClass(className)
					.getCanonicalName();
			if (fullClassName.equals("java.lang.Object")) {
				fullClassName = "gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanomaterialEntity";
			}
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName(fullClassName);
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition");
			association.setRoleName("sampleComposition");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(composition.getId().toString());
			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results.setTargetClassname(fullClassName);
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanomaterialEntity nanoEntity = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				nanoEntity = (NanomaterialEntity) obj;
				composition.getNanomaterialEntityCollection().add(nanoEntity);
			}
		}
	}

	public void copyAndSaveNanomaterialEntity(
			NanomaterialEntityBean entityBean, Sample oldSample,
			Sample[] newSamples, UserBean user) throws CompositionException,
			NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void copyAndSaveFunctionalizingEntity(
			FunctionalizingEntityBean entityBean, Sample oldSample,
			Sample[] newSamples, UserBean user) throws CompositionException,
			NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}
}
