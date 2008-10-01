package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherTarget;
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
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleCompositionServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * Local implementation of NanoparticleCompositionService.
 * 
 * @author pansu
 * 
 */
public class NanoparticleCompositionServiceLocalImpl implements
		NanoparticleCompositionService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCompositionServiceLocalImpl.class);
	private NanoparticleCompositionServiceHelper helper = new NanoparticleCompositionServiceHelper();

	public NanoparticleCompositionServiceLocalImpl() {
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
			boolean newSampleComposition = false;
			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setNanoparticleSample(
						particleSample);
				particleSample.getSampleComposition()
						.setNanoparticleEntityCollection(
								new HashSet<NanoparticleEntity>());
				newSampleComposition = true;

			}
			entity.setSampleComposition(particleSample.getSampleComposition());
			particleSample.getSampleComposition()
					.getNanoparticleEntityCollection().add(entity);

			FileService service = new FileServiceLocalImpl();
			Collection<LabFile> labFiles = entity.getLabFileCollection();
			if (labFiles != null) {
				for (LabFile file : labFiles) {
					service.prepareSaveFile(file);
				}
			}
			appService.saveOrUpdate(entity);

			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			removeNanoparticleEntityPublicVisibility(authService, entity);
//			if (!newSampleComposition) {
//				authService.removePublicGroup(particleSample
//						.getSampleComposition().getId().toString());
//			}
			List<String> accessibleGroups = authService.getAccessibleGroups(
					particleSample.getName(), CaNanoLabConstants.CSM_READ_ROLE);
			if (accessibleGroups != null
					&& accessibleGroups
							.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
				assignNanoparicleEntityPublicVisibility(authService, entity);
				if (newSampleComposition) {
					assignSampleCompositionPublicVisibility(authService,
							particleSample);
				}
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
			NanoparticleEntity entity = helper
					.findNanoparticleEntityById(entityId);
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

	public NanoparticleEntityBean findNanoparticleEntityById(String entityId,
			String entityClassName) throws ParticleCompositionException {
		return findNanoparticleEntityById(entityId);
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
			boolean newSampleComposition = false;
			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setNanoparticleSample(
						particleSample);
				particleSample.getSampleComposition()
						.setFunctionalizingEntityCollection(
								new HashSet<FunctionalizingEntity>());
				newSampleComposition = true;
			}
			entity.setSampleComposition(particleSample.getSampleComposition());
			particleSample.getSampleComposition()
					.getFunctionalizingEntityCollection().add(entity);

			FileService service = new FileServiceLocalImpl();
			Collection<LabFile> labFiles = entity.getLabFileCollection();
			if (labFiles != null) {
				for (LabFile file : labFiles) {
					service.prepareSaveFile(file);
				}
			}
			appService.saveOrUpdate(entity);

			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			removeFunctionalizingEntityPublicVisibility(authService, entity);
//			if (!newSampleComposition) {
//				authService.removePublicGroup(particleSample
//						.getSampleComposition().getId().toString());
//			}
			List<String> accessibleGroups = authService.getAccessibleGroups(
					particleSample.getName(), CaNanoLabConstants.CSM_READ_ROLE);
			if (accessibleGroups != null
					&& accessibleGroups
							.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
				assignFunctionalizingEntityPublicVisibility(authService, entity);
				if (newSampleComposition) {
					assignSampleCompositionPublicVisibility(authService,
							particleSample);
				}
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

			FileService service = new FileServiceLocalImpl();
			Collection<LabFile> labFiles = assoc.getLabFileCollection();
			if (labFiles != null) {
				for (LabFile file : labFiles) {
					service.prepareSaveFile(file);
				}
			}

			if (assoc.getId() == null) { // because of unidirectional
				// relationship between composition
				// and chemical associations
				appService.saveOrUpdate(composition);
			} else {
				appService.saveOrUpdate(assoc);
			}

			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (assoc.getId() != null) {
				removeChemicalAssociationPublicVisibility(authService, assoc);
			}
			List<String> accessibleGroups = authService.getAccessibleGroups(
					particleSample.getName(), CaNanoLabConstants.CSM_READ_ROLE);
			if (assoc.getId() != null && accessibleGroups != null
					&& accessibleGroups
							.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
				assignChemicalAssociationPublicVisibility(authService, assoc);
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
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(file);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

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
			fileService.writeFile(file, fileData);

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

			FunctionalizingEntity entity = helper
					.findFunctionalizingEntityById(entityId);
			if (entity != null)
				entityBean = new FunctionalizingEntityBean(entity);

			return entityBean;
		} catch (Exception e) {
			String err = "Problem finding the functionalizing entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, String entityClassName)
			throws ParticleCompositionException {
		return findFunctionalizingEntityById(entityId);
	}

	public ChemicalAssociationBean findChemicalAssociationById(String assocId)
			throws ParticleCompositionException {
		ChemicalAssociationBean assocBean = null;
		try {
			ChemicalAssociation assoc = helper
					.findChemicalAssocationById(assocId);
			if (assoc != null)
				assocBean = new ChemicalAssociationBean(assoc);
			return assocBean;
		} catch (Exception e) {
			String err = "Problem finding the chemical association by id: "
					+ assocId;
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	public ChemicalAssociationBean findChemicalAssociationById(
			String particleId, String assocId, String assocClassName)
			throws ParticleCompositionException {
		return findChemicalAssociationById(assocId);
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
	 * Return user-defined function types
	 * 
	 * @return
	 * @throws ParticleCompositionException
	 */
	public SortedSet<String> getAllOtherTargetTypes()
			throws ParticleCompositionException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherTarget.class);
			for (Object obj : results) {
				OtherTarget other = (OtherTarget) obj;
				types.add(other.getType());
			}
			return types;
		} catch (Exception e) {
			String err = "Error in retrieving other target types";
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
			FileService fileService = new FileServiceLocalImpl();
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
			FileService fileService = new FileServiceLocalImpl();
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
			FileService fileService = new FileServiceLocalImpl();
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
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			removeNanoparticleEntityPublicVisibility(authService, entity);
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
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			removeFunctionalizingEntityPublicVisibility(authService, entity);
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
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			removeChemicalAssociationPublicVisibility(authService, assoc);
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

	public SampleComposition findCompositionByParticleSampleId(String particleId)
			throws ParticleCompositionException {
		throw new ParticleCompositionException(
				"Not implemented for local service");
	}

	public void assignChemicalAssociationPublicVisibility(
			AuthorizationService authService,
			ChemicalAssociation chemicalAssociation)
			throws CaNanoLabSecurityException {
		if (chemicalAssociation != null) {
			removeChemicalAssociationPublicVisibility(authService,
					chemicalAssociation);
			authService.assignPublicVisibility(
					chemicalAssociation.getId().toString());
			// chemicalAssociation.associatedElementA
			if (chemicalAssociation.getAssociatedElementA() != null) {
				authService.assignPublicVisibility(chemicalAssociation
						.getAssociatedElementA().getId().toString());
			}
			// chemicalAssociation.associatedElementB
			if (chemicalAssociation.getAssociatedElementB() != null) {
				authService.assignPublicVisibility(chemicalAssociation
						.getAssociatedElementB().getId().toString());
			}
		}
	}

	public void assignNanoparicleEntityPublicVisibility(
			AuthorizationService authService,
			NanoparticleEntity nanoparticleEntity)
			throws CaNanoLabSecurityException {
		if (nanoparticleEntity != null) {
			authService.assignPublicVisibility(nanoparticleEntity.getId().toString());
			// nanoparticleEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = nanoparticleEntity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						authService.assignPublicVisibility(composingElement.getId()
								.toString());
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								authService.assignPublicVisibility(function.getId()
										.toString());
							}
						}
					}
				}
			}
		}
	}

	public void assignFunctionalizingEntityPublicVisibility(
			AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity)
			throws CaNanoLabSecurityException {
		if (functionalizingEntity != null) {
			authService.assignPublicVisibility(functionalizingEntity.getId().toString());
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						authService.assignPublicVisibility(function.getId()
								.toString());
					}
				}
			}
		}
	}

	public void removeNanoparticleEntityPublicVisibility(
			AuthorizationService authService,
			NanoparticleEntity nanoparticleEntity)
			throws CaNanoLabSecurityException {
		if (nanoparticleEntity != null) {
			authService
					.removePublicGroup(nanoparticleEntity.getId().toString());

			// nanoparticleEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = nanoparticleEntity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						authService.removePublicGroup(composingElement.getId()
								.toString());
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								authService.removePublicGroup(function.getId()
										.toString());
							}
						}
					}
				}
			}
		}
	}

	public void removeFunctionalizingEntityPublicVisibility(
			AuthorizationService authService,
			FunctionalizingEntity functionalizingEntity)
			throws CaNanoLabSecurityException {
		if (functionalizingEntity != null) {
			authService.removePublicGroup(functionalizingEntity.getId()
					.toString());
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						authService.removePublicGroup(function.getId()
								.toString());
					}
				}
			}
		}
	}

	public void removeChemicalAssociationPublicVisibility(
			AuthorizationService authService,
			ChemicalAssociation chemicalAssociation)
			throws CaNanoLabSecurityException {
		if (chemicalAssociation != null) {
			authService.removePublicGroup(chemicalAssociation.getId()
					.toString());
			// chemicalAssociation.associatedElementA
			if (chemicalAssociation.getAssociatedElementA() != null) {
				authService.removePublicGroup(chemicalAssociation
						.getAssociatedElementA().getId().toString());
			}
			// chemicalAssociation.associatedElementB
			if (chemicalAssociation.getAssociatedElementB() != null) {
				authService.removePublicGroup(chemicalAssociation
						.getAssociatedElementB().getId().toString());
			}
		}
	}

	public void assignSampleCompositionPublicVisibility(
			AuthorizationService authService,
			NanoparticleSample particleSample)
			throws CaNanoLabSecurityException {
		authService.removePublicGroup(particleSample.getSampleComposition()
				.getId().toString());
		authService.assignPublicVisibility(particleSample.getSampleComposition()
				.getId().toString());

	}
}
