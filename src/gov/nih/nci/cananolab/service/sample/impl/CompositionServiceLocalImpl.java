package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.File;
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
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
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
public class CompositionServiceLocalImpl implements
		CompositionService {
	private static Logger logger = Logger
			.getLogger(CompositionServiceLocalImpl.class);
	private CompositionServiceHelper helper = new CompositionServiceHelper();

	public CompositionServiceLocalImpl() {
	}

	public void saveNanomaterialEntity(Sample particleSample,
			NanomaterialEntity entity) throws CompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (entity.getId() != null) {
				try {
					NanomaterialEntity dbEntity = (NanomaterialEntity) appService
							.load(NanomaterialEntity.class, entity.getId());
				} catch (Exception e) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new CompositionException(err, e);
				}
			}
			boolean newSampleComposition = false;
			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setSample(
						particleSample);
				particleSample.getSampleComposition()
						.setNanomaterialEntityCollection(
								new HashSet<NanomaterialEntity>());
				newSampleComposition = true;

			}
			entity.setSampleComposition(particleSample.getSampleComposition());
			particleSample.getSampleComposition()
					.getNanomaterialEntityCollection().add(entity);

			FileService service = new FileServiceLocalImpl();
			Collection<File> Files = entity.getFileCollection();
			if (Files != null) {
				for (File file : Files) {
					service.prepareSaveFile(file);
				}
			}
			appService.saveOrUpdate(entity);
		} catch (Exception e) {
			String err = "Error in saving a nanomaterial entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId)
			throws CompositionException {
		NanomaterialEntityBean entityBean = null;
		try {
			NanomaterialEntity entity = helper
					.findNanomaterialEntityById(entityId);
			if (entity != null)
				entityBean = new NanomaterialEntityBean(entity);
			return entityBean;
		} catch (Exception e) {
			String err = "Problem finding the nanomaterial entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
			String entityClassName) throws CompositionException {
		return findNanomaterialEntityById(entityId);
	}

	public void saveFunctionalizingEntity(Sample particleSample,
			FunctionalizingEntity entity) throws CompositionException {
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
					throw new CompositionException(err, e);
				}
			}
			boolean newSampleComposition = false;
			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setSample(
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
			Collection<File> Files = entity.getFileCollection();
			if (Files != null) {
				for (File file : Files) {
					service.prepareSaveFile(file);
				}
			}
			appService.saveOrUpdate(entity);
		} catch (Exception e) {
			String err = "Problem saving the functionalizing entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveChemicalAssociation(Sample particleSample,
			ChemicalAssociation assoc) throws CompositionException {
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
					throw new CompositionException(err, e);
				}
			}
			SampleComposition composition = particleSample
					.getSampleComposition();

			composition.getChemicalAssociationCollection().add(assoc);

			FileService service = new FileServiceLocalImpl();
			Collection<File> Files = assoc.getFileCollection();
			if (Files != null) {
				for (File file : Files) {
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
		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveCompositionFile(Sample particleSample,
			File file, byte[] fileData) throws CompositionException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(file);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setSample(
						particleSample);
				particleSample.getSampleComposition().setFileCollection(
						new HashSet<File>());
			}
			particleSample.getSampleComposition().getFileCollection().add(file);
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
			throw new CompositionException(err, e);
		}
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId) throws CompositionException {
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
			throw new CompositionException(err, e);
		}
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, String entityClassName)
			throws CompositionException {
		return findFunctionalizingEntityById(entityId);
	}

	public ChemicalAssociationBean findChemicalAssociationById(String assocId)
			throws CompositionException {
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
			throw new CompositionException(err, e);
		}
	}

	public ChemicalAssociationBean findChemicalAssociationById(
			String sampleId, String assocId, String assocClassName)
			throws CompositionException {
		return findChemicalAssociationById(assocId);
	}

	public void retrieveVisibility(NanomaterialEntityBean entity, UserBean user)
			throws CompositionException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean file : entity.getFiles()) {
				fileService.retrieveVisibility(file, user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for nanomaterial entity "
					+ entity.getType();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void retrieveVisibility(FunctionalizingEntityBean entity,
			UserBean user) throws CompositionException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean file : entity.getFiles()) {
				fileService.retrieveVisibility(file, user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for functionalizing entity "
					+ entity.getType();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void retrieveVisibility(ChemicalAssociationBean assoc, UserBean user)
			throws CompositionException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean file : assoc.getFiles()) {
				fileService.retrieveVisibility(file, user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for chemical association "
					+ assoc.getType();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteNanomaterialEntity(NanomaterialEntity entity)
			throws CompositionException {
		try {
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			removeNanomaterialEntityPublicVisibility(authService, entity);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(entity);
		} catch (Exception e) {
			String err = "Error deleting nanomaterial entity " + entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity)
			throws CompositionException {
		try {
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			removeFunctionalizingEntityPublicVisibility(authService, entity);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(entity);
		} catch (Exception e) {
			String err = "Error deleting functionalizing entity "
					+ entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteChemicalAssociation(ChemicalAssociation assoc)
			throws CompositionException {
		try {
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			removeChemicalAssociationPublicVisibility(authService, assoc);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(assoc);
		} catch (Exception e) {
			String err = "Error deleting chemical association " + assoc.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteCompositionFile(Sample particleSample,
			File file) throws CompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			particleSample.getSampleComposition().getFileCollection().remove(
					file);
			appService.saveOrUpdate(particleSample.getSampleComposition());
		} catch (Exception e) {
			String err = "Error deleting composition file " + file.getUri();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	// check if any composing elements of the nanomaterial entity is invovled in
	// the chemical association
	public boolean checkChemicalAssociationBeforeDelete(
			NanomaterialEntityBean entityBean) {
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
					.getDomainComposingElement().getNanomaterialEntity()
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

	public CompositionBean findCompositionBySampleId(String sampleId)
			throws CompositionException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(SampleComposition.class);
			crit.createAlias("sample", "sample");
			crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
			// fully load composition
			crit.setFetchMode("nanomaterialEntityCollection", FetchMode.JOIN);
			crit.setFetchMode("nanomaterialEntityCollection.fileCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"nanomaterialEntityCollection.fileCollection.keywordCollection",
							FetchMode.JOIN);
			crit.setFetchMode(
					"nanomaterialEntityCollection.composingElementCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
							FetchMode.JOIN);
			crit
					.setFetchMode("functionalizingEntityCollection",
							FetchMode.JOIN);
			crit.setFetchMode("functionalizingEntityCollection.fileCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"functionalizingEntityCollection.fileCollection.keywordCollection",
							FetchMode.JOIN);
			crit.setFetchMode(
					"functionalizingEntityCollection.functionCollection",
					FetchMode.JOIN);
			crit.setFetchMode("chemicalAssociationCollection", FetchMode.JOIN);
			crit.setFetchMode("chemicalAssociationCollection.fileCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"chemicalAssociationCollection.associatedElementA",
					FetchMode.JOIN);
			crit.setFetchMode(
					"chemicalAssociationCollection.associatedElementB",
					FetchMode.JOIN);
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List result = appService.query(crit);
			CompositionBean comp = null;
			if (!result.isEmpty()) {
				SampleComposition composition = (SampleComposition) result
						.get(0);
				loadTargetsForTargetingFunction(composition);
				comp = new CompositionBean(composition);

			}
			return comp;
		} catch (Exception e) {
			throw new CompositionException(
					"Error finding composition by sample ID: " + sampleId);
		}
	}

	private void loadTargetsForTargetingFunction(SampleComposition composition)
			throws Exception {
		// load targetCollection
		if (composition.getNanomaterialEntityCollection() != null) {
			for (NanomaterialEntity entity : composition
					.getNanomaterialEntityCollection()) {
				if (entity.getComposingElementCollection() != null) {
					for (ComposingElement element : entity
							.getComposingElementCollection()) {
						if (element.getInherentFunctionCollection() != null) {
							for (Function function : element
									.getInherentFunctionCollection()) {
								if (function instanceof TargetingFunction) {
									helper
											.loadTargetsForTargetingFunction((TargetingFunction) function);
								}
							}
						}
					}
				}
			}
		}

		if (composition.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : composition
					.getFunctionalizingEntityCollection()) {
				if (entity.getFunctionCollection() != null) {
					for (Function function : entity.getFunctionCollection()) {
						if (function instanceof TargetingFunction) {
							helper
									.loadTargetsForTargetingFunction((TargetingFunction) function);
						}
					}
				}
			}
		}
	}

	public void assignChemicalAssociationPublicVisibility(
			AuthorizationService authService,
			ChemicalAssociation chemicalAssociation)
			throws SecurityException {
		if (chemicalAssociation != null) {
			removeChemicalAssociationPublicVisibility(authService,
					chemicalAssociation);
			authService.assignPublicVisibility(chemicalAssociation.getId()
					.toString());
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
			NanomaterialEntity nanomaterialEntity)
			throws SecurityException {
		if (nanomaterialEntity != null) {
			authService.assignPublicVisibility(nanomaterialEntity.getId()
					.toString());
			// nanomaterialEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = nanomaterialEntity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						authService.assignPublicVisibility(composingElement
								.getId().toString());
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								authService.assignPublicVisibility(function
										.getId().toString());
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
			throws SecurityException {
		if (functionalizingEntity != null) {
			authService.assignPublicVisibility(functionalizingEntity.getId()
					.toString());
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
			// TODO activation method
		}
	}

	public void removeNanomaterialEntityPublicVisibility(
			AuthorizationService authService,
			NanomaterialEntity nanomaterialEntity)
			throws SecurityException {
		if (nanomaterialEntity != null) {
			authService
					.removePublicGroup(nanomaterialEntity.getId().toString());

			// nanomaterialEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = nanomaterialEntity
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
			throws SecurityException {
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
			throws SecurityException {
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

	public void assignPublicVisibility(AuthorizationService authService,
			SampleComposition composition) throws SecurityException {
		// sampleComposition.nanomaterialEntityCollection,
		Collection<NanomaterialEntity> nanomaterialEntityCollection = composition
				.getNanomaterialEntityCollection();
		if (nanomaterialEntityCollection != null) {
			for (NanomaterialEntity nanomaterialEntity : nanomaterialEntityCollection) {
				assignNanoparicleEntityPublicVisibility(authService,
						nanomaterialEntity);
			}
		}
		// sampleComposition.functionalizingEntityCollection,
		Collection<FunctionalizingEntity> functionalizingEntityCollection = composition
				.getFunctionalizingEntityCollection();
		if (functionalizingEntityCollection != null) {
			for (FunctionalizingEntity functionalizingEntity : functionalizingEntityCollection) {
				assignFunctionalizingEntityPublicVisibility(authService,
						functionalizingEntity);

			}
		}
		// sampleComposition.chemicalAssociationCollection
		Collection<ChemicalAssociation> chemicalAssociationCollection = composition
				.getChemicalAssociationCollection();
		if (functionalizingEntityCollection != null) {
			for (ChemicalAssociation chemicalAssociation : chemicalAssociationCollection) {
				assignChemicalAssociationPublicVisibility(authService,
						chemicalAssociation);
			}
		}
	}
}
