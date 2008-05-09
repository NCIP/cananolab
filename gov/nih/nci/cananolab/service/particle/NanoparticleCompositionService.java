package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.exception.ReportException;
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
			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setNanoparticleSample(
						particleSample);
				particleSample.getSampleComposition()
						.setNanoparticleEntityCollection(
								new HashSet<NanoparticleEntity>());
			}
			entity.setSampleComposition(particleSample.getSampleComposition());
			particleSample.getSampleComposition()
					.getNanoparticleEntityCollection().add(entity);
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
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.chemicalAssociationCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.chemicalAssociationCollection.associatedElementA",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.chemicalAssociationCollection.associatedElementB",
							FetchMode.JOIN);
			crit.setFetchMode("labFileCollection", FetchMode.JOIN);
			crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
			crit.setFetchMode(
					"composingElementCollection.inherentFunctionCollection",
					FetchMode.JOIN);
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
			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setNanoparticleSample(
						particleSample);
				particleSample.getSampleComposition()
						.setFunctionalizingEntityCollection(
								new HashSet<FunctionalizingEntity>());
			}
			entity.setSampleComposition(particleSample.getSampleComposition());
			particleSample.getSampleComposition()
					.getFunctionalizingEntityCollection().add(entity);
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

			composition.getChemicalAssociationCollection().add(assoc);
			if (assoc.getId() == null) { // because of unidirectional
				// relationship between composition
				// and chemical associations
				appService.saveOrUpdate(composition);
			} else {
				appService.saveOrUpdate(assoc);
			}
			if (assoc instanceof OtherChemicalAssociation) {
				// TODO save other chemical association type
			}
		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void saveCompositionFile(NanoparticleSample particleSample,
			LabFile file, byte[] fileData) throws ParticleCompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (file.getId() != null) {
				try {
					LabFile dbFile = (LabFile) appService.get(LabFile.class,
							file.getId());
					// don't change createdBy and createdDate it is already
					// persisted
					file.setCreatedBy(dbFile.getCreatedBy());
					file.setCreatedDate(dbFile.getCreatedDate());
					// load fileName and uri if no new data has been uploaded or
					// no new url has been entered
					if (fileData == null || !file.getUriExternal()) {
						file.setName(dbFile.getName());
					}
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new ReportException(err, e);
				}
			}
			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setNanoparticleSample(
						particleSample);
				particleSample.getSampleComposition().setLabFileCollection(
						new HashSet<LabFile>());
			}
			particleSample.getSampleComposition().getLabFileCollection().add(
					file);
			if (file.getId() == null) { // because of unidirectional
				// relationship between composition
				// and lab files
				appService.saveOrUpdate(particleSample.getSampleComposition());
			} else {
				appService.saveOrUpdate(file);
			}
			// save to the file system fileData is not empty

			FileService fileService = new FileService();
			fileService.writeFile(file, fileData);
			// TODO save other file type

		} catch (Exception e) {
			String err = "Error in saving the composition file.";
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
			crit.setFetchMode("functionCollection", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.chemicalAssociationCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.chemicalAssociationCollection.associatedElementA",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.chemicalAssociationCollection.associatedElementB",
							FetchMode.JOIN);
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
			crit.setFetchMode("associatedElementA", FetchMode.JOIN);
			crit.setFetchMode("associatedElementB", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				ChemicalAssociation assoc = (ChemicalAssociation) result.get(0);
				// load nanoparticle entity associated with composing element in
				// an association
				if (assoc.getAssociatedElementA() instanceof ComposingElement) {
					NanoparticleEntityBean entityBean = findNanoparticleEntityById(((ComposingElement) assoc
							.getAssociatedElementA()).getNanoparticleEntity()
							.getId().toString());
					((ComposingElement) assoc.getAssociatedElementA())
							.setNanoparticleEntity(entityBean.getDomainEntity());
				}
				if (assoc.getAssociatedElementB() instanceof ComposingElement) {
					NanoparticleEntityBean entityBean = findNanoparticleEntityById(((ComposingElement) assoc
							.getAssociatedElementB()).getNanoparticleEntity()
							.getId().toString());
					((ComposingElement) assoc.getAssociatedElementB())
							.setNanoparticleEntity(entityBean.getDomainEntity());
				}
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

	public void retrieveVisibility(FunctionalizingEntityBean entity,
			UserBean user) throws ParticleCompositionException {
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

	public void deleteChemicalAssociation(ChemicalAssociation assoc)
			throws ParticleCompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(assoc);
		} catch (Exception e) {
			String err = "Error deleting chemical association " + assoc.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public void deleteCompositionFile(NanoparticleSample particleSample,
			LabFile file) throws ParticleCompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			particleSample.getSampleComposition().getLabFileCollection()
					.remove(file);
			appService.saveOrUpdate(particleSample.getSampleComposition());
		} catch (Exception e) {
			String err = "Error deleting composition file " + file.getUri();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	// check if any composing elements of the nanoparticle entity is invovled in
	// the chemical association
	public boolean checkChemicalAssociationBeforeDelete(
			NanoparticleEntityBean entityBean) {
		// need to delete chemical associations first if associated elements
		// are composing elements
		Collection<ChemicalAssociation> assocSet = entityBean.getDomainEntity()
				.getSampleComposition().getChemicalAssociationCollection();
		if (assocSet != null) {
			for (ChemicalAssociation assoc : assocSet) {
				if (entityBean.getDomainEntity()
						.getComposingElementCollection().contains(
								assoc.getAssociatedElementA())
						|| entityBean.getDomainEntity()
								.getComposingElementCollection().contains(
										assoc.getAssociatedElementB())) {
					return false;
				}
			}
		}
		return true;
	}

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			ComposingElementBean ceBean) {
		if (ceBean.getDomainComposingElement().getId() != null) {
			Collection<ChemicalAssociation> assocSet = ceBean
					.getDomainComposingElement().getNanoparticleEntity()
					.getSampleComposition().getChemicalAssociationCollection();
			if (assocSet != null) {
				for (ChemicalAssociation assoc : assocSet) {
					if (ceBean.getDomainComposingElement().equals(
							assoc.getAssociatedElementA())
							|| ceBean.getDomainComposingElement().equals(
									assoc.getAssociatedElementB())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// check if the composing element is invovled in the chemical
	// association
	public boolean checkChemicalAssociationBeforeDelete(
			FunctionalizingEntityBean entityBean) {
		// need to delete chemical associations first if associated elements
		// are functionalizing entities
		Collection<ChemicalAssociation> assocSet = entityBean.getDomainEntity()
				.getSampleComposition().getChemicalAssociationCollection();
		if (assocSet != null) {
			for (ChemicalAssociation assoc : assocSet) {
				if (entityBean.getDomainEntity().equals(
						assoc.getAssociatedElementA())
						|| entityBean.getDomainEntity().equals(
								assoc.getAssociatedElementB())) {
					return false;
				}
			}
		}
		return true;
	}
}
