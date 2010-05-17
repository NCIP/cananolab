package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.function.Target;
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
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
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

	private CompositionServiceHelper helper;
	private FileServiceLocalImpl fileService;

	public CompositionServiceLocalImpl() {
		helper = new CompositionServiceHelper();
		fileService = new FileServiceLocalImpl();
	}

	public CompositionServiceLocalImpl(UserBean user) {
		helper = new CompositionServiceHelper(user);
		fileService = new FileServiceLocalImpl(user);
	}

	public void saveNanomaterialEntity(SampleBean sampleBean,
			NanomaterialEntityBean entityBean) throws CompositionException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
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
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile());
			}
			appService.saveOrUpdate(entity);
			// save file to the file system and assign visibility
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.writeFile(fileBean);
			}

			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			// assign visibility for composition
			helper.getAuthService().assignVisibility(
					sample.getSampleComposition().getId().toString(),
					visibleGroups, owningGroup);
			// assign visibility for nanomaterial entity
			assignVisibility(entity, visibleGroups, owningGroup);
		} catch (Exception e) {
			String err = "Error in saving a nanomaterial entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public NanomaterialEntityBean findNanomaterialEntityById(String entityId)
			throws CompositionException, NoAccessException {
		NanomaterialEntityBean entityBean = null;
		try {
			NanomaterialEntity entity = helper
					.findNanomaterialEntityById(entityId);
			if (entity != null) {
				entityBean = new NanomaterialEntityBean(entity);
				if (entityBean.getFiles() != null && helper.getUser() != null) {
					for (FileBean fileBean : entityBean.getFiles()) {
						fileBean.setVisibilityGroups(helper.getAuthService()
								.getAccessibleGroups(
										fileBean.getDomainFile().getId()
												.toString(),
										Constants.CSM_READ_PRIVILEGE));
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
			FunctionalizingEntityBean entityBean) throws CompositionException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
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
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile());
			}
			appService.saveOrUpdate(entity);
			// save file to the file system and assign visibility
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.writeFile(fileBean);
			}

			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			// assign visibility for composition
			helper.getAuthService().assignVisibility(
					sample.getSampleComposition().getId().toString(),
					visibleGroups, owningGroup);
			// assign visibility for chemical association
			assignVisibility(entity, visibleGroups, owningGroup);

		} catch (Exception e) {
			String err = "Problem saving the functionalizing entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveChemicalAssociation(SampleBean sampleBean,
			ChemicalAssociationBean assocBean) throws CompositionException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
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
			for (FileBean fileBean : assocBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile());
			}
			appService.saveOrUpdate(assoc);
			// save file to the file system and assign visibility
			for (FileBean fileBean : assocBean.getFiles()) {
				fileService.writeFile(fileBean);
			}

			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			// assign visibility for composition
			helper.getAuthService().assignVisibility(
					sample.getSampleComposition().getId().toString(),
					visibleGroups, owningGroup);
			// assign visibility for chemical association
			assignVisibility(assoc, visibleGroups, owningGroup);
		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveCompositionFile(SampleBean sampleBean, FileBean fileBean)
			throws CompositionException, NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			File file = fileBean.getDomainFile();
			fileService.prepareSaveFile(file);
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
						"SampleComposition");
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
			fileService.writeFile(fileBean);
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
			String entityId) throws CompositionException, NoAccessException {
		FunctionalizingEntityBean entityBean = null;
		try {
			FunctionalizingEntity entity = helper
					.findFunctionalizingEntityById(entityId);
			if (entity != null) {
				entityBean = new FunctionalizingEntityBean(entity);
				if (entityBean.getFiles() != null && helper.getUser() != null) {
					for (FileBean fileBean : entityBean.getFiles()) {
						fileBean.setVisibilityGroups(helper.getAuthService()
								.getAccessibleGroups(
										fileBean.getDomainFile().getId()
												.toString(),
										Constants.CSM_READ_PRIVILEGE));
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

	public ChemicalAssociationBean findChemicalAssociationById(String assocId)
			throws CompositionException, NoAccessException {
		ChemicalAssociationBean assocBean = null;
		try {
			ChemicalAssociation assoc = helper
					.findChemicalAssociationById(assocId);
			if (assoc != null) {
				assocBean = new ChemicalAssociationBean(assoc);
				if (assocBean.getFiles() != null && helper.getUser() != null) {
					for (FileBean fileBean : assocBean.getFiles()) {
						fileBean.setVisibilityGroups(helper.getAuthService()
								.getAccessibleGroups(
										fileBean.getDomainFile().getId()
												.toString(),
										Constants.CSM_READ_PRIVILEGE));
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

	public List<String> deleteNanomaterialEntity(NanomaterialEntity entity,
			Boolean removeVisibility) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		Boolean canDelete = this.checkChemicalAssociationBeforeDelete(entity);
		if (!canDelete) {
			throw new ChemicalAssociationViolationException(
					"The nanomaterial entity is used in a chemical association.  Please delete the chemcial association first before deleting the nanomaterial entity.");
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(entity);
			entries = removeVisibility(entity, removeVisibility);
		} catch (Exception e) {
			String err = "Error deleting nanomaterial entity " + entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return entries;
	}

	public List<String> deleteFunctionalizingEntity(
			FunctionalizingEntity entity, Boolean removeVisibility)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
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
			entries = removeVisibility(entity, removeVisibility);
		} catch (Exception e) {
			String err = "Error deleting functionalizing entity "
					+ entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return entries;
	}

	public List<String> deleteChemicalAssociation(ChemicalAssociation assoc,
			Boolean removeVisibility) throws CompositionException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(assoc);
			entries = removeVisibility(assoc, removeVisibility);
		} catch (Exception e) {
			String err = "Error deleting chemical association " + assoc.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return entries;
	}

	public List<String> deleteCompositionFile(SampleComposition comp,
			File file, Boolean removeVisibility) throws CompositionException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// load files first
			List<File> fileList = helper.findFilesByCompositionInfoId(comp
					.getId().toString(), "SampleComposition");
			comp.setFileCollection(new HashSet<File>(fileList));
			comp.getFileCollection().remove(file);
			appService.saveOrUpdate(comp);
			if (removeVisibility == null || removeVisibility)
				fileService.getHelper().getAuthService().removeCSMEntry(
						file.getId().toString());
			entries.add(file.getId().toString());
		} catch (Exception e) {
			String err = "Error deleting composition file " + file.getUri();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return entries;
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

	public CompositionBean findCompositionBySampleId(String sampleId)
			throws CompositionException {
		CompositionBean comp = null;
		try {
			SampleComposition composition = helper
					.findCompositionBySampleId(sampleId);
			if (composition != null) {
				comp = new CompositionBean(composition);
				if (comp.getFiles() != null && helper.getUser() != null) {
					for (FileBean fileBean : comp.getFiles()) {
						fileBean.setVisibilityGroups(helper.getAuthService()
								.getAccessibleGroups(
										fileBean.getDomainFile().getId()
												.toString(),
										Constants.CSM_READ_PRIVILEGE));
					}
				}
				if (comp.getNanomaterialEntities() != null) {
					for (NanomaterialEntityBean entity : comp
							.getNanomaterialEntities()) {
						if (entity.getFiles() != null
								&& helper.getUser() != null) {
							for (FileBean fileBean : entity.getFiles()) {
								fileBean.setVisibilityGroups(helper
										.getAuthService().getAccessibleGroups(
												fileBean.getDomainFile()
														.getId().toString(),
												Constants.CSM_READ_PRIVILEGE));
							}
						}
					}
				}
				if (comp.getFunctionalizingEntities() != null) {
					for (FunctionalizingEntityBean entity : comp
							.getFunctionalizingEntities()) {
						if (entity.getFiles() != null
								&& helper.getUser() != null) {
							for (FileBean fileBean : entity.getFiles()) {
								fileBean.setVisibilityGroups(helper
										.getAuthService().getAccessibleGroups(
												fileBean.getDomainFile()
														.getId().toString(),
												Constants.CSM_READ_PRIVILEGE));
							}
						}
					}
				}
				if (comp.getChemicalAssociations() != null) {
					for (ChemicalAssociationBean assoc : comp
							.getChemicalAssociations()) {
						if (assoc.getFiles() != null
								&& helper.getUser() != null) {
							for (FileBean fileBean : assoc.getFiles()) {
								fileBean.setVisibilityGroups(helper
										.getAuthService().getAccessibleGroups(
												fileBean.getDomainFile()
														.getId().toString(),
												Constants.CSM_READ_PRIVILEGE));
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
			SampleBean[] newSampleBeans) throws CompositionException,
			NoAccessException {

		try {
			for (SampleBean sampleBean : newSampleBeans) {
				NanomaterialEntityBean copyBean = null;
				try {
					NanomaterialEntity copy = entityBean.getDomainCopy();
					copyBean = new NanomaterialEntityBean(copy);
					// copy file visibility and file content
					for (FileBean fileBean : copyBean.getFiles()) {
						fileService.updateClonedFileInfo(fileBean,
								oldSampleBean.getDomain().getName(), sampleBean
										.getDomain().getName());
					}
				} catch (Exception e) {
					String error = "Error in copying the nanomaterial entity.";
					throw new CompositionException(error, e);
				}
				if (copyBean != null)
					saveNanomaterialEntity(sampleBean, copyBean);
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
			SampleBean[] newSampleBeans) throws CompositionException,
			NoAccessException {
		try {
			for (SampleBean sampleBean : newSampleBeans) {
				FunctionalizingEntityBean copyBean = null;
				try {
					FunctionalizingEntity copy = entityBean.getDomainCopy();
					copyBean = new FunctionalizingEntityBean(copy);
					// copy file visibility and file content
					for (FileBean fileBean : copyBean.getFiles()) {
						fileService.updateClonedFileInfo(fileBean,
								oldSampleBean.getDomain().getName(), sampleBean
										.getDomain().getName());
					}
				} catch (Exception e) {
					String error = "Error in copying the functionalizing entity.";
					throw new CompositionException(error, e);
				}
				if (copyBean != null)
					saveFunctionalizingEntity(sampleBean, copyBean);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in copying the characterization.";
			throw new CompositionException(error, e);
		}
	}

	public List<String> deleteComposition(SampleComposition comp,
			Boolean removeVisibility)
			throws ChemicalAssociationViolationException, CompositionException,
			NoAccessException {
		if (helper.getUser() == null || !helper.getUser().isCurator()) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		// delete composition files
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				entries.addAll(deleteCompositionFile(comp, file,
						removeVisibility));
			}
		}
		// delete chemical association
		if (comp.getChemicalAssociationCollection() != null) {
			for (ChemicalAssociation assoc : comp
					.getChemicalAssociationCollection()) {
				entries.addAll(deleteChemicalAssociation(assoc,
						removeVisibility));
			}
		}
		comp.setChemicalAssociationCollection(null);

		// delete nanomaterial entities
		if (comp.getNanomaterialEntityCollection() != null) {
			for (NanomaterialEntity entity : comp
					.getNanomaterialEntityCollection()) {
				entries.addAll(deleteNanomaterialEntity(entity,
						removeVisibility));
			}
		}
		// delete functionalizing entities
		if (comp.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : comp
					.getFunctionalizingEntityCollection()) {
				entries.addAll(deleteFunctionalizingEntity(entity,
						removeVisibility));
			}
		}
		// delete composition files
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				entries.addAll(deleteCompositionFile(comp, file,
						removeVisibility));
			}
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(comp);
		} catch (Exception e) {
			String err = "Problem deleting composition by id: " + comp.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
		return entries;
	}

	protected List<String> removeVisibility(SampleComposition comp,
			Boolean remove) throws Exception {
		List<String> entries = new ArrayList<String>();
		if (remove == null || remove)
			helper.getAuthService().removeCSMEntry(comp.getId().toString());
		entries.add(comp.getId().toString());
		if (comp.getNanomaterialEntityCollection() != null)
			for (NanomaterialEntity entity : comp
					.getNanomaterialEntityCollection()) {
				entries.addAll(removeVisibility(entity, remove));
			}
		if (comp.getFunctionalizingEntityCollection() != null)
			for (FunctionalizingEntity entity : comp
					.getFunctionalizingEntityCollection()) {
				entries.addAll(removeVisibility(entity, remove));
			}
		if (comp.getChemicalAssociationCollection() != null)
			for (ChemicalAssociation assoc : comp
					.getChemicalAssociationCollection()) {
				entries.addAll(removeVisibility(assoc, remove));
			}
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				if (remove == null || remove)
					helper.getAuthService().removeCSMEntry(
							file.getId().toString());
				entries.add(file.getId().toString());
			}
		}
		return entries;
	}

	private List<String> removeVisibility(
			ChemicalAssociation chemicalAssociation, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (chemicalAssociation != null) {
			if (remove == null || remove)
				helper.getAuthService().removeCSMEntry(
						chemicalAssociation.getId().toString());
			entries.add(chemicalAssociation.getId().toString());
			// // chemicalAssociation.associatedElementA
			// if (chemicalAssociation.getAssociatedElementA() != null) {
			// if (remove == null || remove)
			// helper.getAuthService().removeCSMEntry(chemicalAssociation
			// .getAssociatedElementA().getId().toString());
			// }
			// // chemicalAssociation.associatedElementB
			// if (chemicalAssociation.getAssociatedElementB() != null) {
			// if (remove == null || remove)
			// helper.getAuthService().removeCSMEntry(chemicalAssociation
			// .getAssociatedElementB().getId().toString());
			// entries.add(chemicalAssociation.getAssociatedElementB().getId()
			// .toString());
			// }
			if (chemicalAssociation.getFileCollection() != null) {
				for (File file : chemicalAssociation.getFileCollection()) {
					if (remove == null || remove)
						helper.getAuthService().removeCSMEntry(
								file.getId().toString());
					entries.add(file.getId().toString());
				}
			}
		}
		return entries;
	}

	private List<String> removeVisibility(
			FunctionalizingEntity functionalizingEntity, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (functionalizingEntity != null) {
			if (remove == null || remove)
				helper.getAuthService().removeCSMEntry(
						functionalizingEntity.getId().toString());
			entries.add(functionalizingEntity.getId().toString());
			if (functionalizingEntity.getActivationMethod() != null) {
				if (remove == null || remove)
					helper.getAuthService().removeCSMEntry(
							functionalizingEntity.getActivationMethod().getId()
									.toString());
				entries.add(functionalizingEntity.getActivationMethod().getId()
						.toString());
			}
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						if (remove == null || remove)
							helper.getAuthService().removeCSMEntry(
									function.getId().toString());
						entries.add(function.getId().toString());
						if (function instanceof TargetingFunction) {
							for (Target target : ((TargetingFunction) function)
									.getTargetCollection()) {
								if (remove == null || remove)
									helper.getAuthService().removeCSMEntry(
											target.getId().toString());
								entries.add(target.getId().toString());
							}
						}
					}
				}
			}
			if (functionalizingEntity.getFileCollection() != null) {
				for (File file : functionalizingEntity.getFileCollection()) {
					if (remove == null || remove)
						helper.getAuthService().removeCSMEntry(
								file.getId().toString());
					entries.add(file.getId().toString());
				}
			}
		}
		return entries;
	}

	private List<String> removeVisibility(NanomaterialEntity entity,
			Boolean remove) throws Exception {
		List<String> entries = new ArrayList<String>();
		if (entity != null) {
			if (remove == null || remove)
				helper.getAuthService().removeCSMEntry(
						entity.getId().toString());
			entries.add(entity.getId().toString());
			// nanomaterialEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = entity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						if (remove == null || remove)
							helper.getAuthService().removeCSMEntry(
									composingElement.getId().toString());
						entries.add(composingElement.getId().toString());
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								if (remove == null || remove)
									helper.getAuthService().removeCSMEntry(
											function.getId().toString());
								entries.add(function.getId().toString());
							}
						}
					}
				}
			}
			if (entity.getFileCollection() != null) {
				for (File file : entity.getFileCollection()) {
					if (remove == null || remove)
						helper.getAuthService().removeCSMEntry(
								file.getId().toString());
				}
			}
		}
		return entries;
	}

	private void assignVisibility(NanomaterialEntity entity,
			String[] visibleGroups, String owningGroup) throws Exception {
		if (entity != null) {
			helper.getAuthService().assignVisibility(entity.getId().toString(),
					visibleGroups, owningGroup);
			// nanomaterialEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = entity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						helper.getAuthService().assignVisibility(
								composingElement.getId().toString(),
								visibleGroups, owningGroup);
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								helper.getAuthService().assignVisibility(
										function.getId().toString(),
										visibleGroups, owningGroup);
							}
						}
					}
				}
			}
		}
	}

	private void assignVisibility(FunctionalizingEntity functionalizingEntity,
			String[] visibleGroups, String owningGroup) throws Exception {
		if (functionalizingEntity != null) {
			helper.getAuthService().assignVisibility(
					functionalizingEntity.getId().toString(), visibleGroups,
					owningGroup);
			// activation method
			if (functionalizingEntity.getActivationMethod() != null) {
				helper.getAuthService().assignVisibility(
						functionalizingEntity.getActivationMethod().getId()
								.toString(), visibleGroups, owningGroup);
			}
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						helper.getAuthService().assignVisibility(
								function.getId().toString(), visibleGroups,
								owningGroup);
						if (function instanceof TargetingFunction) {
							for (Target target : ((TargetingFunction) function)
									.getTargetCollection()) {
								helper.getAuthService().assignVisibility(
										target.getId().toString(),
										visibleGroups, owningGroup);
							}
						}
					}
				}
			}
		}
	}

	protected void assignVisibility(SampleComposition comp,
			String[] visibleGroups, String owningGroup) throws Exception {
		helper.getAuthService().assignVisibility(comp.getId().toString(),
				visibleGroups, owningGroup);
		for (NanomaterialEntity entity : comp.getNanomaterialEntityCollection()) {
			assignVisibility(entity, visibleGroups, owningGroup);
		}
		for (FunctionalizingEntity entity : comp
				.getFunctionalizingEntityCollection()) {
			assignVisibility(entity, visibleGroups, owningGroup);
		}
		for (ChemicalAssociation assoc : comp
				.getChemicalAssociationCollection()) {
			assignVisibility(assoc, visibleGroups, owningGroup);
		}
	}

	private void assignVisibility(ChemicalAssociation chemicalAssociation,
			String[] visibleGroups, String owningGroup) throws Exception {
		if (chemicalAssociation != null) {
			helper.getAuthService().assignVisibility(
					chemicalAssociation.getId().toString(), visibleGroups,
					owningGroup);
			// visibility of the associated elements should already be assigned
			// when creating the entities
			// chemicalAssociation.associatedElementA
			// if (chemicalAssociation.getAssociatedElementA() != null) {
			// helper.getAuthService().assignVisibility(chemicalAssociation
			// .getAssociatedElementA().getId().toString(),
			// visibleGroups, owningGroup);
			// }
			// // chemicalAssociation.associatedElementB
			// if (chemicalAssociation.getAssociatedElementB() != null) {
			// helper.getAuthService().assignVisibility(chemicalAssociation
			// .getAssociatedElementB().getId().toString(),
			// visibleGroups, owningGroup);
			// }
		}
	}

	public CompositionServiceHelper getHelper() {
		return helper;
	}

	public FileServiceLocalImpl getFileService() {
		return fileService;
	}
}
