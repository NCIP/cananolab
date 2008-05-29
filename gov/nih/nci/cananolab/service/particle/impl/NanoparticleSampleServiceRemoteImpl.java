package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

/**
 * Remote grid client implementation of NanoparticleSampleSearchInterface
 * 
 * @author pansu
 * 
 */
public class NanoparticleSampleServiceRemoteImpl implements
		NanoparticleSampleService {
	private static Logger logger = Logger
			.getLogger(NanoparticleSampleServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public NanoparticleSampleServiceRemoteImpl(String serviceUrl)
			throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	/**
	 * 
	 * @param particleSource
	 * @param nanoparticleEntityClassNames
	 * @param otherNanoparticleTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws ParticleException
	 */
	public List<ParticleBean> findNanoparticleSamplesBy(String particleSource,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws ParticleException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		try {
			NanoparticleSample[] particleSamples = gridClient
					.getNanoparticleSamplesBy(particleSource,
							nanoparticleEntityClassNames,
							functionalizingEntityClassNames,
							functionClassNames, characterizationClassNames,
							wordList);
			if (particleSamples != null) {
				for (NanoparticleSample particleSample : particleSamples) {
					// manually fetch the associated data since grid service
					// only pass one level association
					loadParticleSamplesAssociations(particleSample);
					particles.add(new ParticleBean(particleSample));
				}
			}
			return particles;
		} catch (Exception e) {
			String err = "Problem finding particles with the given search parameters.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * Get all the associated data of a nanoparticle sample
	 * 
	 * @param particleSample
	 * @throws Exception
	 */
	private void loadParticleSamplesAssociations(
			NanoparticleSample particleSample) throws Exception {
		String particleId = particleSample.getId().toString();
		// source
		Source source = findSourceByParticleSampleId(particleId);
		particleSample.setSource(source);

		// keyword
		Collection<Keyword> keywordCollection = findKeywordsByParticleSampleId(particleId);
		particleSample.setKeywordCollection(keywordCollection);

		// characterization, char.derivedBioAssayDataCollection,
		// derived.labFile, labFile.keywordCollection
		Collection<Characterization> characterizationCollection = findCharsByParticleSampleId(particleId);
		particleSample
				.setCharacterizationCollection(characterizationCollection);

		// sampleComposition
		// sampleComposition.nanoparticleEntityCollection,
		// nanoparticleEntityCollection.composingElementCollection,
		// composingElementCollection.inherentFucntionCollection
		// sampleComposition.functionalizingEntityCollection,
		// functionalizingEntityCollection.functionCollection
		SampleComposition sampleComposition = findCompositionByParticleSampleId(particleId);
		particleSample.setSampleComposition(sampleComposition);
	}

	public ParticleBean findNanoparticleSampleById(String particleId)
			throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleSample particleSample = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				particleSample = (NanoparticleSample) obj;
				loadParticleSamplesAssociations(particleSample);
			}
			ParticleBean particleBean = new ParticleBean(particleSample);
			return particleBean;
		} catch (Exception e) {
			String err = "Problem finding the remote particle by id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public NanoparticleSample findNanoparticleSampleByName(String particleName)
			throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			Attribute attribute = new Attribute();
			attribute.setName("name");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleName);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleSample particleSample = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				particleSample = (NanoparticleSample) obj;
				loadParticleSamplesAssociations(particleSample);
			}
			return particleSample;
		} catch (Exception e) {
			String err = "Problem finding the particle by name: "
					+ particleName;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public void retrieveVisibility(ParticleBean particleBean, UserBean user)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public void saveNanoparticleSample(NanoparticleSample particleSample)
			throws ParticleException, DuplicateEntriesException {
		throw new ParticleException("Not implemented for grid service");
	}

	public void deleteAnnotationById(String className, Long dataId)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<Source> findAllParticleSources() throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<Source> findAllParticleSources(UserBean user)
			throws ParticleException {
		return findAllParticleSources();
	}

	public SortedSet<SortableName> findOtherParticles(String particleSource,
			String particleName, UserBean user) throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<String> findAllNanoparticleSampleNames(UserBean user)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	/**
	 * return all sources with an associated NanoparticleSample whose id is
	 * equal to particleId
	 * 
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 * 
	 */
	private Source findSourceByParticleSampleId(String particleId)
			throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Source");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			association.setRoleName("nanoparticleSampleCollection");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Source");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Source source = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				source = (Source) obj;
			}
			return source;
		} catch (Exception e) {
			String err = "Problem finding the source by particle id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * return all keywords with an associated NanoparticleSample whose id is
	 * equal to particleId
	 * 
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<Keyword> findKeywordsByParticleSampleId(String particleId)
			throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Keyword");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			association.setRoleName("nanoparticleSampleCollection");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Keyword");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Keyword keyword = null;
			Collection<Keyword> keywordCollection = new ArrayList<Keyword>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				keyword = (Keyword) obj;
				keywordCollection.add(keyword);
			}
			return keywordCollection;
		} catch (Exception e) {
			String err = "Problem finding the keywordCollection by particle id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * return all characterization with an associated NanoparticleSample whose
	 * id is equal to particleId
	 * 
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<Characterization> findCharsByParticleSampleId(
			String particleId) throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.characterization");
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
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chars = null;
			Collection<Characterization> characterizationCollection = new ArrayList<Characterization>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chars = (Characterization) obj;
				// todo (may not needed for this search level)
				// findDerivedBioAssayDataByCharId is added in helper file
				// Collection<DerivedBioAssayData> derivedBioAssayDataCollection
				// =
				// gridClient.findDerivedBioAssayDataByCharId(chars.getId().toString());
				// chars.setDerivedBioAssayDataCollection(derivedBioAssayDataCollection);
				characterizationCollection.add(chars);
			}
			return characterizationCollection;
		} catch (Exception e) {
			String err = "Problem finding the characterizationCollection by particle id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * return all sample composition with an associated NanoparticleSample whose
	 * id is equal to particleId
	 * 
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 */
	private SampleComposition findCompositionByParticleSampleId(
			String particleId) throws ParticleException {
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
			throw new ParticleException(err, e);
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
	 * return all ComposingElement with an associated NanoparticleEntity whose
	 * id is equal to nanoEntityId
	 * 
	 * @param nanoEntityId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<ComposingElement> findComposingElementByNanoparticleEntityId(
			String nanoEntityId) throws ParticleException {
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
				composingElementCollection.add(composingElement);
			}
			return composingElementCollection;
		} catch (Exception e) {
			String err = "Problem finding the composingElementCollection by nanoparicleEntity id: "
					+ nanoEntityId;
			logger.error(err, e);
			throw new ParticleException(err, e);
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
			String composingElementId) throws ParticleException {
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
				Collection<Function> functionCollection = findFunctionCollectionByFunctionalizingEntityId(functionalizingEntity
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

	/**
	 * return all functionCollection with an associated FunctionalizingEntity
	 * whose id is equal to functionalizingEntityId
	 * 
	 * @param composingElementId
	 * @return
	 * @throws ParticleException
	 */
	private Collection<Function> findFunctionCollectionByFunctionalizingEntityId(
			String functionalizingEntityId) throws ParticleException {
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
			throw new ParticleException(err, e);
		}
	}

}
