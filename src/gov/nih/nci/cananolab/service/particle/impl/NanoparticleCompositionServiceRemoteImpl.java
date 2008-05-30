package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;

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

	public NanoparticleCompositionServiceRemoteImpl(String serviceUrl)
			throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
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
			// TODO load associations
			if (entity != null)
				entityBean = new NanoparticleEntityBean(entity);
			return entityBean;
		} catch (Exception e) {
			String err = "Problem finding the nanoparticle entity by id: "
					+ entityId;
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
}
