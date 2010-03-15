package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
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
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Local implementation of CompositionService.
 * 
 * @author pansu
 * 
 */
public class CompositionServiceLocalImpl implements CompositionService {
	private static Logger logger = Logger
			.getLogger(CompositionServiceLocalImpl.class);

	private CompositionServiceHelper helper = new CompositionServiceHelper();
	private FileServiceHelper fileHelper = new FileServiceHelper();

	public CompositionServiceLocalImpl() {
	}

	public void saveNanomaterialEntity(SampleBean sampleBean,
			NanomaterialEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			NanomaterialEntity entity = entityBean.getDomainEntity();
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

			if (sample.getSampleComposition() == null) {
				sample.setSampleComposition(new SampleComposition());
				sample.getSampleComposition().setSample(sample);
				// particleSample.getSampleComposition()
				// .setNanomaterialEntityCollection(
				// new HashSet<NanomaterialEntity>());

			}
			entity.setSampleComposition(sample.getSampleComposition());
			// particleSample.getSampleComposition()
			// .getNanomaterialEntityCollection().add(entity);

			// save file and keyword
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
			}
			appService.saveOrUpdate(entity);
			// save file to the file system and assign visibility
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.writeFile(fileBean, user);
			}

			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			// assign visibility for composition
			helper.getAuthService().assignVisibility(
					sample.getSampleComposition().getId().toString(),
					visibleGroups, owningGroup);
			// assign visibility for chemical association
			helper.assignVisibility(entity, visibleGroups, owningGroup);
		} catch (Exception e) {
			String err = "Error in saving a nanomaterial entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
			UserBean user) throws CompositionException, NoAccessException {
		NanomaterialEntityBean entityBean = null;
		try {
			NanomaterialEntity entity = helper.findNanomaterialEntityById(
					entityId, user);
			if (entity != null) {
				entityBean = new NanomaterialEntityBean(entity);
				if (entityBean.getFiles() != null && user != null) {
					for (FileBean fileBean : entityBean.getFiles()) {
						fileHelper.retrieveVisibility(fileBean);
					}
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the nanomaterial entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return entityBean;
	}

	public void saveFunctionalizingEntity(SampleBean sampleBean,
			FunctionalizingEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			FunctionalizingEntity entity = entityBean.getDomainEntity();
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

			if (sample.getSampleComposition() == null) {
				sample.setSampleComposition(new SampleComposition());
				sample.getSampleComposition().setSample(sample);
				// particleSample.getSampleComposition()
				// .setFunctionalizingEntityCollection(
				// new HashSet<FunctionalizingEntity>());

			}
			entity.setSampleComposition(sample.getSampleComposition());
			// particleSample.getSampleComposition()
			// .getFunctionalizingEntityCollection().add(entity);

			// save file and keyword
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
			}
			appService.saveOrUpdate(entity);
			// save file to the file system and assign visibility
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.writeFile(fileBean, user);
			}

			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			// assign visibility for composition
			helper.getAuthService().assignVisibility(
					sample.getSampleComposition().getId().toString(),
					visibleGroups, owningGroup);
			// assign visibility for chemical association
			helper.assignVisibility(entity, visibleGroups, owningGroup);

		} catch (Exception e) {
			String err = "Problem saving the functionalizing entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveChemicalAssociation(SampleBean sampleBean,
			ChemicalAssociationBean assocBean, UserBean user)
			throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			ChemicalAssociation assoc = assocBean.getDomainAssociation();
			Sample sample = sampleBean.getDomain();
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
			if (sample.getSampleComposition() == null) {
				sample.setSampleComposition(new SampleComposition());
				sample.getSampleComposition().setSample(sample);
				// particleSample.getSampleComposition()
				// .setFunctionalizingEntityCollection(
				// new HashSet<FunctionalizingEntity>());

			}
			// composition.getChemicalAssociationCollection().add(assoc);
			assoc.setSampleComposition(sample.getSampleComposition());
			// save file and keyword
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : assocBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
			}
			appService.saveOrUpdate(assoc);
			// save file to the file system and assign visibility
			for (FileBean fileBean : assocBean.getFiles()) {
				fileService.writeFile(fileBean, user);
			}

			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			// assign visibility for composition
			helper.getAuthService().assignVisibility(
					sample.getSampleComposition().getId().toString(),
					visibleGroups, owningGroup);
			// assign visibility for chemical association
			helper.assignVisibility(assoc, visibleGroups, owningGroup);
		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveCompositionFile(SampleBean sampleBean, FileBean fileBean,
			UserBean user) throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			File file = fileBean.getDomainFile();
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(file, user);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (sample.getSampleComposition() == null) {
				sample.setSampleComposition(new SampleComposition());
				sample.getSampleComposition().setSample(sampleBean.getDomain());
				sample.getSampleComposition().setFileCollection(
						new HashSet<File>());
			}
			// need to load the composition file collection to save composition
			// because of
			// unidirectional relationship between composition and file
			else {
				List<File> fileList = helper.findFilesByCompositionInfoId(
						sample.getSampleComposition().getId().toString(),
						"SampleComposition", user);
				sample.getSampleComposition().setFileCollection(
						new HashSet<File>(fileList));
			}
			sample.getSampleComposition().getFileCollection().add(file);
			if (file.getId() == null) { // because of unidirectional
				// relationship between composition
				// and lab files
				appService.saveOrUpdate(sample.getSampleComposition());
			} else {
				appService.saveOrUpdate(file);
			}
			// write file to file system and assign visibility
			fileService.writeFile(fileBean, user);
			// assign visibility for composition

			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();

			helper.getAuthService().assignVisibility(
					sample.getSampleComposition().getId().toString(),
					visibleGroups, owningGroup);

		} catch (Exception e) {
			String err = "Error in saving the composition file.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public FunctionalizingEntityBean findFunctionalizingEntityById(
			String entityId, UserBean user) throws CompositionException,
			NoAccessException {
		FunctionalizingEntityBean entityBean = null;
		try {
			FunctionalizingEntity entity = helper
					.findFunctionalizingEntityById(entityId, user);
			if (entity != null) {
				entityBean = new FunctionalizingEntityBean(entity);
				if (entityBean.getFiles() != null && user != null) {
					for (FileBean fileBean : entityBean.getFiles()) {
						fileHelper.retrieveVisibility(fileBean);
					}
				}
			} else {
				throw new NoAccessException(
						"User doesn't have access to the sample");
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the functionalizing entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return entityBean;
	}

	public ChemicalAssociationBean findChemicalAssociationById(String assocId,
			UserBean user) throws CompositionException, NoAccessException {
		ChemicalAssociationBean assocBean = null;
		try {
			ChemicalAssociation assoc = helper.findChemicalAssociationById(
					assocId, user);
			if (assoc != null) {
				assocBean = new ChemicalAssociationBean(assoc);
				if (assocBean.getFiles() != null && user != null) {
					for (FileBean fileBean : assocBean.getFiles()) {
						fileHelper.retrieveVisibility(fileBean);
					}
				}
			} else {
				throw new NoAccessException();
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the chemical association by id: "
					+ assocId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return assocBean;
	}

	public void deleteNanomaterialEntity(NanomaterialEntity entity,
			UserBean user, Boolean removeVisibility)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		Boolean canDelete = this.checkChemicalAssociationBeforeDelete(entity);
		if (!canDelete) {
			throw new ChemicalAssociationViolationException(
					"The nanomaterial entity is used in a chemical association.  Please delete the chemcial association first before deleting the nanomaterial entity.");
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(entity);
			if (removeVisibility == null || removeVisibility)
				helper.removeVisibility(entity);
		} catch (Exception e) {
			String err = "Error deleting nanomaterial entity " + entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity,
			UserBean user, Boolean removeVisibility)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		Boolean canDelete = this.checkChemicalAssociationBeforeDelete(entity);
		if (!canDelete) {
			throw new ChemicalAssociationViolationException(
					"The functionalizing entity "
							+ entity.getName()
							+ " is used in a chemical association.  Please delete the chemcial association first before deleting the functionalizing entity.");
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(entity);
			if (removeVisibility == null || removeVisibility)
				helper.removeVisibility(entity);
		} catch (Exception e) {
			String err = "Error deleting functionalizing entity "
					+ entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteChemicalAssociation(ChemicalAssociation assoc,
			UserBean user, Boolean removeVisibility)
			throws CompositionException, NoAccessException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(assoc);
			if (removeVisibility == null || removeVisibility)
				helper.removeVisibility(assoc);
		} catch (Exception e) {
			String err = "Error deleting chemical association " + assoc.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteCompositionFile(Sample sample, File file, UserBean user,
			Boolean removeVisibility) throws CompositionException,
			NoAccessException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// load files first
			List<File> fileList = helper.findFilesByCompositionInfoId(sample
					.getSampleComposition().getId().toString(),
					"SampleComposition", user);
			sample.getSampleComposition().setFileCollection(
					new HashSet<File>(fileList));
			sample.getSampleComposition().getFileCollection().remove(file);
			appService.saveOrUpdate(sample.getSampleComposition());
		} catch (Exception e) {
			String err = "Error deleting composition file " + file.getUri();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	// check if any composing elements of the nanomaterial entity is invovled in
	// the chemical association
	public boolean checkChemicalAssociationBeforeDelete(
			NanomaterialEntity entity) {
		// need to delete chemical associations first if associated elements
		// are composing elements
		Collection<ChemicalAssociation> assocSet = entity
				.getSampleComposition().getChemicalAssociationCollection();
		if (assocSet != null) {
			for (ChemicalAssociation assoc : assocSet) {
				if (entity.getComposingElementCollection().contains(
						assoc.getAssociatedElementA())
						|| entity.getComposingElementCollection().contains(
								assoc.getAssociatedElementB())) {
					return false;
				}
			}
		}
		return true;
	}

	// check if the composing element is invovled in the chemical
	// association
	private boolean checkChemicalAssociationBeforeDelete(
			FunctionalizingEntity entity) {
		// need to delete chemical associations first if associated elements
		// are functionalizing entities
		Collection<ChemicalAssociation> assocSet = entity
				.getSampleComposition().getChemicalAssociationCollection();
		if (assocSet != null) {
			for (ChemicalAssociation assoc : assocSet) {
				if (entity.equals(assoc.getAssociatedElementA())
						|| entity.equals(assoc.getAssociatedElementB())) {
					return false;
				}
			}
		}
		return true;
	}

	public CompositionBean findCompositionBySampleId(String sampleId,
			UserBean user) throws CompositionException {
		CompositionBean comp = null;
		try {
			SampleComposition composition = helper.findCompositionBySampleId(
					sampleId, user);
			if (composition != null) {
				comp = new CompositionBean(composition);
				if (comp.getFiles() != null && user != null) {
					for (FileBean fileBean : comp.getFiles()) {
						fileHelper.retrieveVisibility(fileBean);
					}
				}
				if (comp.getNanomaterialEntities() != null) {
					for (NanomaterialEntityBean entity : comp
							.getNanomaterialEntities()) {
						if (entity.getFiles() != null && user != null) {
							for (FileBean fileBean : entity.getFiles()) {
								fileHelper.retrieveVisibility(fileBean);
							}
						}
					}
				}
				if (comp.getFunctionalizingEntities() != null) {
					for (FunctionalizingEntityBean entity : comp
							.getFunctionalizingEntities()) {
						if (entity.getFiles() != null && user != null) {
							for (FileBean fileBean : entity.getFiles()) {
								fileHelper.retrieveVisibility(fileBean);
							}
						}
					}
				}
				if (comp.getChemicalAssociations() != null) {
					for (ChemicalAssociationBean assoc : comp
							.getChemicalAssociations()) {
						if (assoc.getFiles() != null && user != null) {
							for (FileBean fileBean : assoc.getFiles()) {
								fileHelper.retrieveVisibility(fileBean);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			String err = "Error finding composition by sample ID: " + sampleId;
			throw new CompositionException(err, e);
		}
		return comp;
	}

	public void copyAndSaveNanomaterialEntity(
			NanomaterialEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException {

		try {
			for (SampleBean sampleBean : newSampleBeans) {
				NanomaterialEntityBean copyBean = null;
				try {
					NanomaterialEntity copy = entityBean.getDomainCopy();
					copyBean = new NanomaterialEntityBean(copy);
					// copy file visibility and file content
					for (FileBean fileBean : copyBean.getFiles()) {
						fileHelper.updateClonedFileInfo(fileBean, oldSampleBean
								.getDomain().getName(), sampleBean.getDomain()
								.getName(), user);
					}
				} catch (Exception e) {
					String error = "Error in copying the nanomaterial entity.";
					throw new CompositionException(error, e);
				}
				if (copyBean != null)
					saveNanomaterialEntity(sampleBean, copyBean, user);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in copying the characterization.";
			throw new CompositionException(error, e);
		}
	}

	public void copyAndSaveFunctionalizingEntity(
			FunctionalizingEntityBean entityBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException {
		try {
			for (SampleBean sampleBean : newSampleBeans) {
				FunctionalizingEntityBean copyBean = null;
				try {
					FunctionalizingEntity copy = entityBean.getDomainCopy();
					copyBean = new FunctionalizingEntityBean(copy);
					// copy file visibility and file content
					for (FileBean fileBean : copyBean.getFiles()) {
						fileHelper.updateClonedFileInfo(fileBean, oldSampleBean
								.getDomain().getName(), sampleBean.getDomain()
								.getName(), user);
					}
				} catch (Exception e) {
					String error = "Error in copying the functionalizing entity.";
					throw new CompositionException(error, e);
				}
				if (copyBean != null)
					saveFunctionalizingEntity(sampleBean, copyBean, user);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in copying the characterization.";
			throw new CompositionException(error, e);
		}
	}
}
