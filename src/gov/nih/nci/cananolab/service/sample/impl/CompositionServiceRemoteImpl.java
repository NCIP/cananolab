package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;

/**
 * Local implementation of CompositionService.
 * 
 * @author pansu
 * 
 */
public class CompositionServiceRemoteImpl implements CompositionService {
	private static Logger logger = Logger
			.getLogger(CompositionServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;
	private String serviceUrl;

	public CompositionServiceRemoteImpl(String serviceUrl)
			throws CompositionException {
		try {
			this.serviceUrl = serviceUrl;
			gridClient = new CaNanoLabServiceClient(serviceUrl);
		} catch (Exception e) {
			throw new CompositionException(
					"Can't create grid client succesfully.");
		}
	}

	public void saveNanomaterialEntity(SampleBean sampleBean,
			NanomaterialEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
			UserBean user) throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	private void loadNanomaterialEntityAssociations(NanomaterialEntity entity)
			throws Exception {
		File[] files = gridClient.getFilesByCompositionInfoId(entity.getId()
				.toString(), "NanomaterialEntity");
		if (files != null && files.length > 0) {
			loadKeywordsForFiles(files);
			entity.setFileCollection(new HashSet<File>(Arrays.asList(files)));
		}
		loadComposingElementForNanomaterialEntity(entity);
	}

	private void loadKeywordsForFiles(File[] files) throws Exception {
		FileServiceRemoteImpl fileService = new FileServiceRemoteImpl(
				serviceUrl);
		for (File file : files) {
			fileService.loadKeywordsForFile(file);
		}
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
			loadInherentFunctionsForComposingElement(composingElement);
			entity.getComposingElementCollection().add(composingElement);
		}
	}

	/**
	 * load all InherentFunction for an associated ComposingElement
	 */
	private void loadInherentFunctionsForComposingElement(
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

	public void saveFunctionalizingEntity(SampleBean sampleBean,
			FunctionalizingEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void saveChemicalAssociation(SampleBean sample,
			ChemicalAssociationBean assocBean, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void saveCompositionFile(SampleBean sampleBean, FileBean fileBean,
			UserBean user) throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, UserBean user) throws CompositionException,
			NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	private void loadFunctionalizingEntityAssociations(
			FunctionalizingEntity entity) throws Exception {
		File[] files = gridClient.getFilesByCompositionInfoId(entity.getId()
				.toString(), "FunctionalizingEntity");
		if (files != null && files.length > 0) {
			loadKeywordsForFiles(files);
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
		throw new CompositionException("Not implemented for grid service");
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
		CompositionBean compositionBean = null;
		try {
			// find all nanomaterial entity class names, functionalizing entity
			// class names first
			String[] sampleViewStrs = gridClient.getSampleViewStrs(sampleId);
			// column 8 contains characterization short class names
			String[] nanoEntityClassNames = null;
			if (sampleViewStrs.length > 5
					&& !StringUtils.isEmpty(sampleViewStrs[5])) {
				nanoEntityClassNames = sampleViewStrs[5]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}
			String[] funcEntityClassNames = null;
			if (sampleViewStrs.length > 6
					&& !StringUtils.isEmpty(sampleViewStrs[6])) {
				funcEntityClassNames = sampleViewStrs[6]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}
			String[] assocClassNames = null;
			if (sampleViewStrs.length > 8
					&& !StringUtils.isEmpty(sampleViewStrs[8])) {
				assocClassNames = sampleViewStrs[8]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}
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
				loadCompositionAssociations(sampleComposition,
						nanoEntityClassNames, funcEntityClassNames,
						assocClassNames);
				compositionBean = new CompositionBean(sampleComposition);
			}
		} catch (Exception e) {
			String err = "Problem finding the remote sampleComposition by sample id: "
					+ sampleId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return compositionBean;
	}

	private void loadCompositionAssociations(SampleComposition composition,
			String[] nanoEntityClassNames, String[] funcEntityClassNames,
			String[] assocClassNames) throws Exception {
		loadNanomaterialEntitiesForComposition(composition,
				nanoEntityClassNames);
		loadFunctionalizingEntitiesForComposition(composition,
				funcEntityClassNames);
		loadChemicalAssociationsForComposition(composition, assocClassNames);
		File[] files = gridClient.getFilesByCompositionInfoId(composition
				.getId().toString(), ClassUtils
				.getShortClassName("SampleComposition"));
		if (files != null && files.length > 0) {
			loadKeywordsForFiles(files);
			composition.setFileCollection(new HashSet<File>(Arrays
					.asList(files)));
		}
	}

	private void loadNanomaterialEntitiesForComposition(
			SampleComposition composition, String[] nanoEntityClassNames)
			throws Exception {
		if (nanoEntityClassNames != null) {
			for (String name : nanoEntityClassNames) {
				String fullClassName = null;
				if (ClassUtils.getFullClass(name) != null) {
					fullClassName = ClassUtils.getFullClass(name)
							.getCanonicalName();
				} else {
					fullClassName = ClassUtils.getFullClass(
							OtherNanomaterialEntity.class.getCanonicalName())
							.getCanonicalName();
				}
				CQLQuery query = new CQLQuery();
				gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
				target.setName(fullClassName);
				Association association = new Association();
				association
						.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
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
				CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
						results);
				NanomaterialEntity nanoEntity = null;
				composition
						.setNanomaterialEntityCollection(new HashSet<NanomaterialEntity>());
				while (iter.hasNext()) {
					java.lang.Object obj = iter.next();
					nanoEntity = (NanomaterialEntity) obj;
					loadNanomaterialEntityAssociations(nanoEntity);
					composition.getNanomaterialEntityCollection().add(
							nanoEntity);
				}
			}
		}
	}

	private void loadFunctionalizingEntitiesForComposition(
			SampleComposition composition, String[] funcEntityClassNames)
			throws Exception {
		if (funcEntityClassNames != null) {
			for (String name : funcEntityClassNames) {
				String fullClassName = null;
				if (ClassUtils.getFullClass(name) != null) {
					fullClassName = ClassUtils.getFullClass(name)
							.getCanonicalName();
				} else {
					fullClassName = ClassUtils
							.getFullClass(
									OtherFunctionalizingEntity.class
											.getCanonicalName())
							.getCanonicalName();
				}
				CQLQuery query = new CQLQuery();
				gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
				target.setName(fullClassName);

				Association association = new Association();
				association
						.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
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
				CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
						results);
				FunctionalizingEntity funcEntity = null;
				composition
						.setFunctionalizingEntityCollection(new HashSet<FunctionalizingEntity>());
				while (iter.hasNext()) {
					java.lang.Object obj = iter.next();
					funcEntity = (FunctionalizingEntity) obj;
					loadFunctionalizingEntityAssociations(funcEntity);
					composition.getFunctionalizingEntityCollection().add(
							funcEntity);
				}
			}
		}
	}

	private void loadChemicalAssociationsForComposition(
			SampleComposition composition, String[] assocClassNames)
			throws Exception {
		if (assocClassNames != null) {
			for (String name : assocClassNames) {
				String fullClassName = null;
				if (ClassUtils.getFullClass(name) != null) {
					fullClassName = ClassUtils.getFullClass(name)
							.getCanonicalName();
				} else {
					fullClassName = ClassUtils.getFullClass(
							OtherChemicalAssociation.class.getCanonicalName())
							.getCanonicalName();
				}
				CQLQuery query = new CQLQuery();
				gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
				target.setName(fullClassName);

				Association association = new Association();
				association
						.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
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
				CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
						results);
				ChemicalAssociation assoc = null;
				composition
						.setChemicalAssociationCollection(new HashSet<ChemicalAssociation>());
				while (iter.hasNext()) {
					java.lang.Object obj = iter.next();
					assoc = (ChemicalAssociation) obj;
					loadChemicalAssociationAssociations(assoc);
					composition.getChemicalAssociationCollection().add(assoc);
				}
			}
		}
	}

	private void loadChemicalAssociationAssociations(ChemicalAssociation assoc)
			throws Exception {
		File[] files = gridClient.getFilesByCompositionInfoId(assoc.getId()
				.toString(), "ChemicalAssociation");
		if (files != null && files.length > 0) {
			loadKeywordsForFiles(files);
			assoc.setFileCollection(new HashSet<File>(Arrays.asList(files)));
		}
		loadAssociatedElementsForChemicalAssociation(assoc, "A");
		loadAssociatedElementsForChemicalAssociation(assoc, "B");
	}

	private void loadAssociatedElementsForChemicalAssociation(
			ChemicalAssociation assoc, String elementNumber) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.AssociatedElement");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		String roleName = "chemicalAssociation" + elementNumber + "Collection";
		association.setRoleName(roleName);
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(assoc.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.AssociatedElement");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			AssociatedElement element = (AssociatedElement) obj;
			if (element instanceof ComposingElement) {
				loadInherentFunctionsForComposingElement((ComposingElement) element);
			} else if (element instanceof FunctionalizingEntity) {
				loadFunctionalizingEntityAssociations((FunctionalizingEntity) element);
			}
			if (elementNumber.equals("A")) {
				assoc.setAssociatedElementA(element);
			} else if (elementNumber.equals("B")) {
				assoc.setAssociatedElementB(element);
			}
		}
	}

	public void copyAndSaveNanomaterialEntity(
			NanomaterialEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}

	public void copyAndSaveFunctionalizingEntity(
			FunctionalizingEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException {
		throw new CompositionException("Not implemented for grid service");
	}
}
