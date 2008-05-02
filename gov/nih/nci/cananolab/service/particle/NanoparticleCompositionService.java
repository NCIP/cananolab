package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving composition.
 * 
 * @author pansu
 * 
 */
public class NanoparticleCompositionService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCompositionService.class);

	public NanoparticleCompositionService() {
	}

	public void saveNanoparticleEntity(NanoparticleSample particleSample,
			NanoparticleEntity entity) throws ParticleCompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (entity.getId() != null) {
				try {
					NanoparticleEntity dbEntity = (NanoparticleEntity) appService
							.load(NanoparticleEntity.class, entity.getId());
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new ParticleCompositionException(err, e);
				}
			}
			SampleComposition composition = particleSample
					.getSampleComposition();
			if (composition == null) {
				particleSample.setSampleComposition(new SampleComposition());
				entity.setSampleComposition(composition);
				particleSample.setSampleComposition(composition);
				Collection<NanoparticleEntity> entityCollection = new HashSet<NanoparticleEntity>();
				entityCollection.add(entity);
				composition.setNanoparticleEntityCollection(entityCollection);
				composition.setNanoparticleSample(particleSample);
			} else {
				entity.setSampleComposition(composition);
			}
			appService.saveOrUpdate(entity);
			if (entity instanceof OtherNanoparticleEntity) {
				// TODO save other entity type
			}
		} catch (Exception e) {
			String err = "Error in saving a nanoparticle entity.";
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public NanoparticleEntityBean findNanoparticleEntityById(String entityId)
			throws ParticleCompositionException {
		NanoparticleEntityBean entityBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleEntity.class).add(
					Property.forName("id").eq(new Long(entityId)));
			crit.setFetchMode("labFileCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				NanoparticleEntity entity = (NanoparticleEntity) result.get(0);
				entityBean = new NanoparticleEntityBean(entity);
			}
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
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (entity.getId() != null) {
				try {
					FunctionalizingEntity dbEntity = (FunctionalizingEntity) appService
							.load(FunctionalizingEntity.class, entity.getId());
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new ParticleCompositionException(err, e);
				}
			}
			SampleComposition composition = particleSample
					.getSampleComposition();
			if (composition != null) {
				entity.setSampleComposition(composition);
				composition.getFunctionalizingEntityCollection().add(entity);
			} else {
				composition = new SampleComposition();
				entity.setSampleComposition(composition);
				particleSample.setSampleComposition(composition);
				Collection<FunctionalizingEntity> entityCollection = new HashSet<FunctionalizingEntity>();
				entityCollection.add(entity);
				composition
						.setFunctionalizingEntityCollection(entityCollection);
				composition.setNanoparticleSample(particleSample);
			}
			appService.saveOrUpdate(entity);
			if (entity instanceof OtherFunctionalizingEntity) {
				// TODO save other entity type
			}
		} catch (Exception e) {
			String err = "Problem saving the functionalizing entity.";
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void saveChemicalAssociation(NanoparticleSample particleSample,
			ChemicalAssociation assoc) throws ParticleCompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (assoc.getId() != null) {
				try {
					ChemicalAssociation dbAssoc = (ChemicalAssociation) appService
							.load(ChemicalAssociation.class, assoc.getId());
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new ParticleCompositionException(err, e);
				}
			}
			SampleComposition composition = particleSample
					.getSampleComposition();
			if (composition != null) {
				composition.getChemicalAssociationCollection().add(assoc);
			} else {
				composition = new SampleComposition();
				particleSample.setSampleComposition(composition);
				Collection<ChemicalAssociation> asssocCollection = new HashSet<ChemicalAssociation>();
				asssocCollection.add(assoc);
				composition
						.setChemicalAssociationCollection(asssocCollection);
				composition.setNanoparticleSample(particleSample);
			}
			appService.saveOrUpdate(assoc);
			if (assoc instanceof OtherChemicalAssociation) {
				// TODO save other chemical association type
			}
		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId) throws ParticleCompositionException {
		FunctionalizingEntityBean entityBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					FunctionalizingEntity.class).add(
					Property.forName("id").eq(new Long(entityId)));
			crit.setFetchMode("labFileCollection", FetchMode.JOIN);
			crit.createAlias("activationMethod", "activation",
					CriteriaSpecification.LEFT_JOIN);
			crit.setFetchMode("activationMethod", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				FunctionalizingEntity entity = (FunctionalizingEntity) result
						.get(0);
				entityBean = new FunctionalizingEntityBean(entity);
			}
			return entityBean;
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
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ChemicalAssociation.class).add(
					Property.forName("id").eq(new Long(assocId)));
			crit.setFetchMode("labFileCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				ChemicalAssociation assoc = (ChemicalAssociation) result.get(0);
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
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherFunctionalizingEntity.class);
			for (Object obj : results) {
				OtherFunctionalizingEntity other = (OtherFunctionalizingEntity) obj;
				types.add(other.getType());
			}
			return types;
		} catch (Exception e) {
			String err = "Error in retrieving other values for functionalizing entity";
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	/**
	 * Return user-defined function types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherFunctionTypes()
			throws ParticleCompositionException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherFunction.class);
			for (Object obj : results) {
				OtherFunction other = (OtherFunction) obj;
				types.add(other.getType());
			}
			return types;
		} catch (Exception e) {
			String err = "Error in retrieving other function types";
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
	public SortedSet<String> getAllOtherNanoparticleEntityTypes()
			throws ParticleCompositionException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherNanoparticleEntity.class);
			for (Object obj : results) {
				OtherNanoparticleEntity other = (OtherNanoparticleEntity) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			String err = "Error in retrieving other values for nanoparticle entity";
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
		return types;
	}
	
	/**
	 * Return user-defined chemical association types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherChemicalAssociationTypes()
			throws ParticleCompositionException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherChemicalAssociation.class);
			for (Object obj : results) {
				OtherChemicalAssociation other = (OtherChemicalAssociation) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			String err = "Error in retrieving other values for chemical association";
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
		return types;
	}

	public void retrieveVisibility(NanoparticleEntityBean entity, UserBean user)
			throws ParticleCompositionException {
		try {
			FileService fileService = new FileService();
			for (LabFileBean file : entity.getFiles()) {
				fileService.retrieveVisibility(file, user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for nanoparticle entity "
					+ entity.getType();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void retrieveVisibility(FunctionalizingEntityBean entity, UserBean user)
			throws ParticleCompositionException {
		try {
			FileService fileService = new FileService();
			for (LabFileBean file : entity.getFiles()) {
				fileService.retrieveVisibility(file, user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for functionalizing entity "
					+ entity.getType();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void retrieveVisibility(ChemicalAssociationBean assoc, UserBean user)
			throws ParticleCompositionException {
		try {
			FileService fileService = new FileService();
			for (LabFileBean file : assoc.getFiles()) {
				fileService.retrieveVisibility(file, user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for chemical association "
					+ assoc.getType();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void deleteNanoparticleEntity(NanoparticleEntity entity)
			throws ParticleCompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(entity);
		} catch (Exception e) {
			String err = "Error deleting nanoparticle entity " + entity.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity)
			throws ParticleCompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(entity);
		} catch (Exception e) {
			String err = "Error deleting functionalizing entity "
					+ entity.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}
}
