package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.TargetingFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;

import java.util.Collection;
import java.util.HashSet;
import java.util.SortedSet;

import org.apache.log4j.Logger;

/**
 * Service methods involving composition.
 * 
 * @author pansu
 * 
 */
public class NanoparticleCompositionServiceRemoteImpl implements
		NanoparticleCompositionService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCompositionServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;
	private String serviceUrl;

	public NanoparticleCompositionServiceRemoteImpl(String serviceUrl)
			throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
		this.serviceUrl = serviceUrl;
	}

	public void saveNanoparticleEntity(NanoparticleSample particleSample,
			NanoparticleEntity entity) throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public NanoparticleEntityBean findNanoparticleEntityById(String entityId)
			throws ParticleCompositionException {
		NanoparticleEntityBean entityBean = null;
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(entityId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleEntity entity = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				entity = (NanoparticleEntity) obj;
			}
			if (entity != null)
				loadNanoparticleEntityAssociations(entity);
			entityBean = new NanoparticleEntityBean(entity);
			return entityBean;
		} catch (Exception e) {
			String err = "Problem finding the nanoparticle entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	private void loadNanoparticleEntityAssociations(NanoparticleEntity entity)
			throws Exception {
		// crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		// crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
		// FetchMode.JOIN);
		// crit
		// .setFetchMode(
		// "sampleComposition.chemicalAssociationCollection.associatedElementA",
		// FetchMode.JOIN);
		// crit
		// .setFetchMode(
		// "sampleComposition.chemicalAssociationCollection.associatedElementB",
		// FetchMode.JOIN);
		FileService fileService = new FileServiceRemoteImpl(serviceUrl);
		Collection<LabFile> files = fileService.findFilesByCompositionInfoId(
				entity.getId().toString(), NanoparticleEntity.class
						.getCanonicalName());
		entity.setLabFileCollection(files);
		loadComposingElementForNanoparticleEntity(entity);
	}

	/**
	 * load all ComposingElement for an associated NanoparticleEntity
	 * 
	 * @param nanoEntityId
	 * @return
	 * @throws ParticleCompositionException
	 */
	private void loadComposingElementForNanoparticleEntity(
			NanoparticleEntity entity) throws ParticleCompositionException {
		try {
			CQLQuery query = new CQLQuery();

			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			association.setRoleName("nanoparticleEntity");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(entity.getId().toString());
			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ComposingElement composingElement = null;
			entity
					.setComposingElementCollection(new HashSet<ComposingElement>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				composingElement = (ComposingElement) obj;
				loadInherentFunctionByComposingElement(composingElement);
				entity.getComposingElementCollection().add(composingElement);
			}
		} catch (Exception e) {
			String err = "Problem finding the composingElementCollection for nanoparicleEntity id: "
					+ entity.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	/**
	 * load all InherentFunction for an associated ComposingElement
	 */
	private void loadInherentFunctionByComposingElement(
			ComposingElement composingElement)
			throws ParticleCompositionException {
		try {
			Function[] functions = gridClient
					.getInherentFunctionsByComposingElementId(composingElement
							.getId().toString());
			if (functions != null) {
				composingElement
						.setInherentFunctionCollection(new HashSet<Function>());
				for (Function function : functions) {
					composingElement.getInherentFunctionCollection().add(
							function);
				}
			}
		} catch (Exception e) {
			String err = "Problem finding the inherentFunctionCollection for composingElement id: "
					+ composingElement.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void saveFunctionalizingEntity(NanoparticleSample particleSample,
			FunctionalizingEntity entity) throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void saveChemicalAssociation(NanoparticleSample particleSample,
			ChemicalAssociation assoc) throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void saveCompositionFile(NanoparticleSample particleSample,
			LabFile file, byte[] fileData) throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId) throws ParticleCompositionException {
		FunctionalizingEntityBean entityBean = null;
		try {
			try {
				CQLQuery query = new CQLQuery();
				gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
				target
						.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
				Attribute attribute = new Attribute();
				attribute.setName("id");
				attribute.setPredicate(Predicate.EQUAL_TO);
				attribute.setValue(entityId);
				target.setAttribute(attribute);
				query.setTarget(target);
				CQLQueryResults results = gridClient.query(query);
				results
						.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
				CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
						results);
				FunctionalizingEntity entity = null;
				while (iter.hasNext()) {
					java.lang.Object obj = iter.next();
					entity = (FunctionalizingEntity) obj;
				}
				// TODO load associations
				if (entity != null)
					loadFunctionalizingEntityAssociations(entity);
				entityBean = new FunctionalizingEntityBean(entity);
				return entityBean;
			} catch (Exception e) {
				String err = "Problem finding the functionalizing entity by id: "
						+ entityId;
				logger.error(err, e);
				throw new ParticleCompositionException(err, e);
			}
		} catch (Exception e) {
			String err = "Problem finding the functionalizing entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	/**
	 * load all NanoparticleEntity Collection for an associated Composition
	 * 
	 */
	private void loadNanoparticleEntityForComposition(
			SampleComposition composition) throws ParticleCompositionException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
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
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleEntity nanoEntity = null;
			composition
					.setNanoparticleEntityCollection(new HashSet<NanoparticleEntity>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				nanoEntity = (NanoparticleEntity) obj;
				loadComposingElementForNanoparticleEntity(nanoEntity);
				composition.getNanoparticleEntityCollection().add(nanoEntity);
			}
		} catch (Exception e) {
			String err = "Problem finding the NanoparticleEntity Collection for composition id: "
					+ composition.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	/**
	 * load all FunctionalizingEntity Collection with an associated Composition
	 */
	private void loadFunctionalizingEntityForComposition(
			SampleComposition composition) throws ParticleCompositionException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
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
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			FunctionalizingEntity functionalizingEntity = null;
			composition
					.setFunctionalizingEntityCollection(new HashSet<FunctionalizingEntity>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				functionalizingEntity = (FunctionalizingEntity) obj;
				loadFunctionsForFunctionalizingEntity(functionalizingEntity);
				composition.getFunctionalizingEntityCollection().add(
						functionalizingEntity);
			}

		} catch (Exception e) {
			String err = "Problem finding the FunctionalizingEntity Collection for Composition Id: "
					+ composition.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	private void loadFunctionalizingEntityAssociations(
			FunctionalizingEntity entity) throws Exception {
		// crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		// crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
		// FetchMode.JOIN);
		// crit
		// .setFetchMode(
		// "sampleComposition.chemicalAssociationCollection.associatedElementA",
		// FetchMode.JOIN);
		// crit
		// .setFetchMode(
		// "sampleComposition.chemicalAssociationCollection.associatedElementB",
		// FetchMode.JOIN);
		FileService fileService = new FileServiceRemoteImpl(serviceUrl);
		Collection<LabFile> files = fileService.findFilesByCompositionInfoId(
				entity.getId().toString(), NanoparticleEntity.class
						.getCanonicalName());
		entity.setLabFileCollection(files);
		loadFunctionsForFunctionalizingEntity(entity);
	}

	private void loadFunctionsForFunctionalizingEntity(
			FunctionalizingEntity entity) throws ParticleCompositionException {
		try {
			Function[] functions = gridClient
					.getFunctionsByFunctionalizingEntityId(entity.getId()
							.toString());
			if (functions != null) {
				entity.setFunctionCollection(new HashSet<Function>());
				for (Function function : functions) {
					if (function instanceof TargetingFunction) {
						Target[] targets = gridClient
								.getTargetsByFunctionId(function.getId()
										.toString());
						if (targets != null) {
							((TargetingFunction) function)
									.setTargetCollection(new HashSet<Target>());
							for (Target target : targets) {
								((TargetingFunction) function)
										.getTargetCollection().add(target);
							}
						}
					}
					entity.getFunctionCollection().add(function);
				}
			}
		} catch (Exception e) {
			String err = "Problem loading the functionCollection for FunctionalizingEntity id: "
					+ entity.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public ChemicalAssociationBean findChemicalAssocationById(String assocId)
			throws ParticleCompositionException {
		ChemicalAssociationBean assocBean = null;
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(assocId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ChemicalAssociation assoc = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				assoc = (ChemicalAssociation) obj;
			}
			// TODO load associations
			if (assoc != null) {
				loadChemicalAssociationAssociations(assoc);
				assocBean = new ChemicalAssociationBean(assoc);
			}
			return assocBean;
		} catch (Exception e) {
			String err = "Problem finding the chemical association by id: "
					+ assocId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	private void loadChemicalAssociationAssociations(ChemicalAssociation assoc)
			throws Exception {
		// crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		// crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
		// FetchMode.JOIN);
		// crit
		// .setFetchMode(
		// "sampleComposition.chemicalAssociationCollection.associatedElementA",
		// FetchMode.JOIN);
		// crit
		// .setFetchMode(
		// "sampleComposition.chemicalAssociationCollection.associatedElementB",
		// FetchMode.JOIN);
		FileService fileService = new FileServiceRemoteImpl(serviceUrl);
		Collection<LabFile> files = fileService.findFilesByCompositionInfoId(
				assoc.getId().toString(), ChemicalAssociation.class
						.getCanonicalName());
		assoc.setLabFileCollection(files);
	}

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherFunctionalizingEntityTypes()
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	/**
	 * Return user-defined function types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherFunctionTypes()
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherNanoparticleEntityTypes()
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	/**
	 * Return user-defined chemical association types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherChemicalAssociationTypes()
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void retrieveVisibility(NanoparticleEntityBean entity, UserBean user)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void retrieveVisibility(FunctionalizingEntityBean entity,
			UserBean user) throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void retrieveVisibility(ChemicalAssociationBean assoc, UserBean user)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void deleteNanoparticleEntity(NanoparticleEntity entity)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void deleteChemicalAssociation(ChemicalAssociation assoc)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	public void deleteCompositionFile(NanoparticleSample particleSample,
			LabFile file) throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	// check if any composing elements of the nanoparticle entity is invovled in
	// the chemical association
	public boolean checkChemicalAssociationBeforeDelete(
			NanoparticleEntityBean entityBean)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			ComposingElementBean ceBean) throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			FunctionalizingEntityBean entityBean)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for grid service");
	}

	/**
	 * return all sample composition with an associated NanoparticleSample whose
	 * id is equal to particleId
	 * 
	 * @param particleId
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SampleComposition findCompositionByParticleSampleId(String particleId)
			throws ParticleCompositionException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			association.setRoleName("nanoparticleSample");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.sampleComposition.SampleComposition");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			SampleComposition sampleComposition = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sampleComposition = (SampleComposition) obj;
				loadNanoparticleEntityForComposition(sampleComposition);
				loadFunctionalizingEntityForComposition(sampleComposition);
			}
			return sampleComposition;
		} catch (Exception e) {
			String err = "Problem finding the sampleComposition by particle id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}
	
	public void assignChemicalAssociationVisibility(AuthorizationService authService,
			ChemicalAssociation chemicalAssociation, String[] visibleGroups)
		throws ParticleCompositionException {
		throw new ParticleCompositionException(
		"Not implemented for grid service");
	}
	
	public void assignNanoparicleEntityVisibility(AuthorizationService authService,
			NanoparticleEntity nanoparticleEntity, String[] visibleGroups)
		throws ParticleCompositionException {
		throw new ParticleCompositionException(
		"Not implemented for grid service");
	}
	
	public void assignFunctionalizingEntityVisibility(AuthorizationService authService,
				FunctionalizingEntity functionalizingEntity, String[] visibleGroups)
		throws ParticleCompositionException {
		throw new ParticleCompositionException(
		"Not implemented for grid service");
	}
		
	public void removeNanoparticleEntityVisibility(AuthorizationService authService,
				NanoparticleEntity nanoparticleEntity)
		throws ParticleCompositionException {
		throw new ParticleCompositionException(
		"Not implemented for grid service");
	}

	public void removeFunctionalizingEntityVisibility(AuthorizationService authService,
				FunctionalizingEntity functionalizingEntity)
		throws ParticleCompositionException {
		throw new ParticleCompositionException(
		"Not implemented for grid service");
	}
	public void removeChemicalAssociationVisibility(AuthorizationService authService,
			ChemicalAssociation chemicalAssociation)
		throws ParticleCompositionException {
		throw new ParticleCompositionException(
		"Not implemented for grid service");
	}
	public void assignSampleCompositionVisibility(AuthorizationService authService,
			NanoparticleSample particleSample, String[] visibleGroups)
		throws ParticleCompositionException {
		throw new ParticleCompositionException(
		"Not implemented for grid service");
	}
}
