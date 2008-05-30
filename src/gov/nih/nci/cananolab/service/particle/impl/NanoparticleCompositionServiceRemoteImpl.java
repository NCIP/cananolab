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
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;

import java.util.ArrayList;
import java.util.Collection;
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
		Collection<ComposingElement> composingElements = findComposingElementByNanoparticleEntityId(entity
				.getId().toString());
		entity.setComposingElementCollection(composingElements);
	}

	/**
	 * return all ComposingElement with an associated NanoparticleEntity whose
	 * id is equal to nanoEntityId
	 * 
	 * @param nanoEntityId
	 * @return
	 * @throws ParticleException
	 */
	public Collection<ComposingElement> findComposingElementByNanoparticleEntityId(
			String nanoEntityId) throws ParticleCompositionException {
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
			attribute.setValue(nanoEntityId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ComposingElement composingElement = null;
			Collection<ComposingElement> composingElementCollection = new ArrayList<ComposingElement>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				composingElement = (ComposingElement) obj;
				Collection<Function> inherentFunctions = findInherentFunctionByComposingElementId(composingElement
						.getId().toString());
				composingElement
						.setInherentFunctionCollection(inherentFunctions);
				composingElementCollection.add(composingElement);
			}
			return composingElementCollection;
		} catch (Exception e) {
			String err = "Problem finding the composingElementCollection by nanoparicleEntity id: "
					+ nanoEntityId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	/**
	 * return all InherentFunction with an associated ComposingElement whose id
	 * is equal to composingElementId
	 * 
	 * @param composingElementId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<Function> findInherentFunctionByComposingElementId(
			String composingElementId) throws ParticleCompositionException {
		try {
			CQLQuery query = new CQLQuery();

			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement");
			association.setRoleName("composingElement");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(composingElementId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Function function = null;
			Collection<Function> inherentFunctionCollection = new ArrayList<Function>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				function = (Function) obj;
				inherentFunctionCollection.add(function);
			}
			return inherentFunctionCollection;
		} catch (Exception e) {
			String err = "Problem finding the inherentFunctionCollection by composingElement id: "
					+ composingElementId;
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
	 * return all NanoparticleEntity Collection with an associated Composition
	 * whose id is equal to compositionId
	 * 
	 * @param compositionId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<NanoparticleEntity> findNanoparticleEntityByCompositionId(
			String compositionId) throws ParticleException {
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
			attribute.setValue(compositionId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleEntity nanoEntity = null;
			Collection<NanoparticleEntity> nanoparticleEntityCollection = new ArrayList<NanoparticleEntity>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				nanoEntity = (NanoparticleEntity) obj;
				Collection<ComposingElement> composingElementCollection = findComposingElementByNanoparticleEntityId(nanoEntity
						.getId().toString());

				for (ComposingElement composingElement : composingElementCollection) {
					Collection<Function> inherentFunctionCollection = findInherentFunctionByComposingElementId(composingElement
							.getId().toString());
					composingElement
							.setInherentFunctionCollection(inherentFunctionCollection);
				}
				nanoEntity
						.setComposingElementCollection(composingElementCollection);
				nanoparticleEntityCollection.add(nanoEntity);
			}
			return nanoparticleEntityCollection;
		} catch (Exception e) {
			String err = "Problem finding the NanoparticleEntity Collection by composition id: "
					+ compositionId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * return all FunctionalizingEntity Collection with an associated
	 * Composition whose id is equal to compositionId
	 * 
	 * @param compositionId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<FunctionalizingEntity> findFunctionalizingEntityByCompositionId(
			String compositionId) throws ParticleException {
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
			attribute.setValue(compositionId);
			association.setAttribute(attribute);
			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			FunctionalizingEntity functionalizingEntity = null;
			Collection<FunctionalizingEntity> functionalizingEntityCollection = new ArrayList<FunctionalizingEntity>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				functionalizingEntity = (FunctionalizingEntity) obj;
				Collection<Function> functionCollection = findFunctionsByFunctionalizingEntityId(functionalizingEntity
						.getId().toString());
				functionalizingEntity.setFunctionCollection(functionCollection);
			}
			return functionalizingEntityCollection;
		} catch (Exception e) {
			String err = "Problem finding the FunctionalizingEntity Collection  by Composition Id: "
					+ compositionId;
			logger.error(err, e);
			throw new ParticleException(err, e);
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
		Collection<Function> functions = findFunctionsByFunctionalizingEntityId(entity
				.getId().toString());
		entity.setFunctionCollection(functions);
	}

	/**
	 * return all functionCollection with an associated FunctionalizingEntity
	 * whose id is equal to functionalizingEntityId
	 * 
	 * @param composingElementId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<Function> findFunctionsByFunctionalizingEntityId(
			String functionalizingEntityId) throws ParticleCompositionException {
		try {
			CQLQuery query = new CQLQuery();

			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
			association.setRoleName("functionalizingEntity");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(functionalizingEntityId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Function function = null;
			Collection<Function> functionCollection = new ArrayList<Function>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				function = (Function) obj;
				functionCollection.add(function);
			}
			return functionCollection;
		} catch (Exception e) {
			String err = "Problem finding the functionCollection by FunctionalizingEntity id: "
					+ functionalizingEntityId;
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
	 * @throws ParticleException
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
				Collection<NanoparticleEntity> nanoparticleEntityCollection = findNanoparticleEntityByCompositionId(sampleComposition
						.getId().toString());
				sampleComposition
						.setNanoparticleEntityCollection(nanoparticleEntityCollection);
				Collection<FunctionalizingEntity> functionalizingEntityCollection = findFunctionalizingEntityByCompositionId(sampleComposition
						.getId().toString());
				sampleComposition
						.setFunctionalizingEntityCollection(functionalizingEntityCollection);

			}
			return sampleComposition;
		} catch (Exception e) {
			String err = "Problem finding the sampleComposition by particle id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}
}
