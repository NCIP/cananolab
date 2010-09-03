package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
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
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
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
public class CompositionServiceLocalImpl extends BaseServiceLocalImpl implements
		CompositionService {
	private static Logger logger = Logger
			.getLogger(CompositionServiceLocalImpl.class);

	private CompositionServiceHelper helper;

	public CompositionServiceLocalImpl() {
		super();
		helper = new CompositionServiceHelper(this.securityService);
	}

	public CompositionServiceLocalImpl(UserBean user) {
		super(user);
		helper = new CompositionServiceHelper(this.securityService);
	}

	public CompositionServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new CompositionServiceHelper(this.securityService);
	}

	public void saveNanomaterialEntity(SampleBean sampleBean,
			NanomaterialEntityBean entityBean) throws CompositionException,
			NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			if (!securityService.checkCreatePermission(sample.getId()
					.toString())) {
				throw new NoAccessException();
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			NanomaterialEntity entity = entityBean.getDomainEntity();
			Boolean newEntity = true;
			Boolean newComp = true;
			if (entity.getId() != null) {
				newEntity = false;
				try {
					if (!securityService.checkCreatePermission(entityBean
							.getDomainEntity().getId().toString())) {
						throw new NoAccessException();
					}
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
			if (sample.getSampleComposition().getId() != null) {
				newComp = false;
			}
			entity.setSampleComposition(sample.getSampleComposition());
			// particleSample.getSampleComposition()
			// .getNanomaterialEntityCollection().add(entity);

			// save file and keyword
			for (FileBean fileBean : entityBean.getFiles()) {
				fileUtils.prepareSaveFile(fileBean.getDomainFile());
			}
			appService.saveOrUpdate(entity);
			// save file to the file system
			for (FileBean fileBean : entityBean.getFiles()) {
				fileUtils.writeFile(fileBean);
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(entity.getSampleComposition()
							.getSample().getId().toString());
			// save sample accesses
			for (AccessibilityBean access : sampleAccesses) {
				if (newComp) {
					this.saveAccessibility(access, sample
							.getSampleComposition().getId().toString());
				}
				if (newEntity) {
					this.saveAccessibility(access, entity.getId().toString());
				}
			}
		} catch (NoAccessException e) {
			throw e;
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
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			if (!securityService.checkCreatePermission(sample.getId()
					.toString())) {
				throw new NoAccessException();
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			FunctionalizingEntity entity = entityBean.getDomainEntity();
			Boolean newEntity = true;
			Boolean newComp = true;
			if (entity.getId() != null) {
				newEntity = false;
				try {
					if (!securityService.checkCreatePermission(entityBean
							.getDomainEntity().getId().toString())) {
						throw new NoAccessException();
					}
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
			if (sample.getSampleComposition().getId() != null) {
				newComp = false;
			}
			entity.setSampleComposition(sample.getSampleComposition());
			// particleSample.getSampleComposition()
			// .getFunctionalizingEntityCollection().add(entity);

			// save file and keyword
			for (FileBean fileBean : entityBean.getFiles()) {
				fileUtils.prepareSaveFile(fileBean.getDomainFile());
			}
			appService.saveOrUpdate(entity);
			// save file to the file system
			for (FileBean fileBean : entityBean.getFiles()) {
				fileUtils.writeFile(fileBean);
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(entity.getSampleComposition()
							.getSample().getId().toString());
			// save sample accesses
			for (AccessibilityBean access : sampleAccesses) {
				if (newComp) {
					this.saveAccessibility(access, sample
							.getSampleComposition().getId().toString());
				}
				if (newEntity) {
					this.saveAccessibility(access, entity.getId().toString());
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem saving the functionalizing entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveChemicalAssociation(SampleBean sampleBean,
			ChemicalAssociationBean assocBean) throws CompositionException,
			NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			ChemicalAssociation assoc = assocBean.getDomainAssociation();
			Sample sample = sampleBean.getDomain();
			if (!securityService.checkCreatePermission(sample.getId()
					.toString())) {
				throw new NoAccessException();
			}
			Boolean newAssoc = true;
			Boolean newComp = true;
			if (assoc.getId() != null) {
				newAssoc = false;
				if (!securityService.checkCreatePermission(assocBean
						.getDomainAssociation().getId().toString())) {
					throw new NoAccessException();
				}
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
			if (sample.getSampleComposition().getId() != null) {
				newComp = false;
			}
			// composition.getChemicalAssociationCollection().add(assoc);
			assoc.setSampleComposition(sample.getSampleComposition());
			// save file and keyword
			for (FileBean fileBean : assocBean.getFiles()) {
				fileUtils.prepareSaveFile(fileBean.getDomainFile());
			}
			appService.saveOrUpdate(assoc);
			// save file to the file system
			for (FileBean fileBean : assocBean.getFiles()) {
				fileUtils.writeFile(fileBean);
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(assoc.getSampleComposition()
							.getSample().getId().toString());
			// save sample accesses
			for (AccessibilityBean access : sampleAccesses) {
				if (newComp) {
					this.saveAccessibility(access, sample
							.getSampleComposition().getId().toString());
				}
				if (newAssoc) {
					this.saveAccessibility(access, assoc.getId().toString());
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveCompositionFile(SampleBean sampleBean, FileBean fileBean)
			throws CompositionException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			if (!securityService.checkCreatePermission(sample.getId()
					.toString())) {
				throw new NoAccessException();
			}
			File file = fileBean.getDomainFile();
			Boolean newFile = true;
			Boolean newComp = true;
			if (file.getId() != null) {
				newFile = false;
			}
			if (!newFile
					&& !securityService.checkCreatePermission(file.getId()
							.toString())) {
				throw new NoAccessException();
			}
			fileUtils.prepareSaveFile(file);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			SampleComposition comp = null;
			if (sample.getSampleComposition() == null) {
				comp = new SampleComposition();
				comp.setSample(sampleBean.getDomain());
				comp.setFileCollection(new HashSet<File>());
			} else {
				newComp = false;
				// need to load the composition file collection to save
				// composition
				// because of
				// unidirectional relationship between composition and file

				comp = helper.findCompositionBySampleId(sample.getId()
						.toString());
			}
			comp.getFileCollection().add(file);
			sample.setSampleComposition(comp);
			if (file.getId() == null) { // because of unidirectional
				// relationship between composition
				// and lab files
				appService.saveOrUpdate(comp);
			} else {
				appService.saveOrUpdate(file);
			}
			// write file to file system
			fileUtils.writeFile(fileBean);
			// save default access
			// save default access
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(sample.getId().toString());
			// save sample accesses
			for (AccessibilityBean access : sampleAccesses) {
				if (newComp) {
					this.saveAccessibility(access, comp.getId().toString());
				}
				if (newFile) {
					this.saveAccessibility(access, file.getId().toString());
				}
			}
		} catch (NoAccessException e) {
			throw e;
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

	public void deleteNanomaterialEntity(NanomaterialEntity entity)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException {
		if (user == null) {
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
		} catch (Exception e) {
			String err = "Error deleting nanomaterial entity " + entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity)
			throws CompositionException, ChemicalAssociationViolationException,
			NoAccessException {
		if (user == null) {
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
		} catch (Exception e) {
			String err = "Error deleting functionalizing entity "
					+ entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteChemicalAssociation(ChemicalAssociation assoc)
			throws CompositionException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(assoc);
		} catch (Exception e) {
			String err = "Error deleting chemical association " + assoc.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteCompositionFile(SampleComposition comp, File file)
			throws CompositionException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// load files first
			List<File> fileList = helper.findFilesByCompositionInfoId(comp
					.getId().toString(), "SampleComposition");
			comp.setFileCollection(new HashSet<File>(fileList));
			comp.getFileCollection().remove(file);
			appService.saveOrUpdate(comp);
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

	public CompositionBean findCompositionBySampleId(String sampleId)
			throws CompositionException {
		CompositionBean comp = null;
		try {
			SampleComposition composition = helper
					.findCompositionBySampleId(sampleId);
			if (composition != null) {
				comp = new CompositionBean(composition);
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
					// copy file file content
					for (FileBean fileBean : copyBean.getFiles()) {
						fileUtils.updateClonedFileInfo(fileBean, oldSampleBean
								.getDomain().getName(), sampleBean.getDomain()
								.getName());
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
						fileUtils.updateClonedFileInfo(fileBean, oldSampleBean
								.getDomain().getName(), sampleBean.getDomain()
								.getName());
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

	public void deleteComposition(SampleComposition comp)
			throws ChemicalAssociationViolationException, CompositionException,
			NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		// delete composition files
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				deleteCompositionFile(comp, file);
			}
		}
		// delete chemical association
		if (comp.getChemicalAssociationCollection() != null) {
			for (ChemicalAssociation assoc : comp
					.getChemicalAssociationCollection()) {
				deleteChemicalAssociation(assoc);
			}
		}
		comp.setChemicalAssociationCollection(null);

		// delete nanomaterial entities
		if (comp.getNanomaterialEntityCollection() != null) {
			for (NanomaterialEntity entity : comp
					.getNanomaterialEntityCollection()) {
				deleteNanomaterialEntity(entity);
			}
		}
		// delete functionalizing entities
		if (comp.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : comp
					.getFunctionalizingEntityCollection()) {
				deleteFunctionalizingEntity(entity);
			}
		}
		// delete composition files
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				deleteCompositionFile(comp, file);
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
	}

	public CompositionServiceHelper getHelper() {
		return helper;
	}

	public void assignAccesses(ComposingElement composingElement)
			throws CompositionException, NoAccessException {
		try {
			if (!isUserOwner(composingElement.getCreatedBy())) {
				throw new NoAccessException();
			}
			// find sample accesses, already contains owner for composing
			// element
			List<AccessibilityBean> sampleAccesses = this
					.findSampleAccesses(composingElement
							.getNanomaterialEntity().getSampleComposition()
							.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.assignAccessibility(access, composingElement);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning nanomaterial entity accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void removeAccesses(NanomaterialEntity entity,
			ComposingElement composingElement) throws CompositionException,
			NoAccessException {
		try {
			if (!securityService.checkCreatePermission(composingElement.getId()
					.toString())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(entity.getSampleComposition()
							.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.removeAccessibility(access, composingElement, false);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning nanomaterial entity accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void assignAccesses(Function function)
			throws CompositionException, NoAccessException {
		try {
			if (!isUserOwner(function.getCreatedBy())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = this
					.findSampleAccesses(function.getFunctionalizingEntity()
							.getSampleComposition().getSample().getId()
							.toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.assignAccessibility(access, function);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning functionalizing entity accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void removeAccesses(FunctionalizingEntity entity,
			Function function) throws CompositionException, NoAccessException {
		try {
			if (!securityService.checkCreatePermission(function.getId()
					.toString())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(entity.getSampleComposition()
							.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.removeAccessibility(access, function, false);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning functionalizing entity accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void assignAccessibility(ChemicalAssociation assoc)
			throws CompositionException, NoAccessException {
		try {
			if (!isUserOwner(assoc.getCreatedBy())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(assoc.getSampleComposition()
							.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				this.saveAccessibility(access, assoc.getSampleComposition()
						.getId().toString());
				accessUtils.assignAccessibility(access, assoc);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning chemical association accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void assignAccesses(SampleComposition comp, File file)
			throws CompositionException, NoAccessException {
		try {
			if (!isUserOwner(file.getCreatedBy())) {
				throw new NoAccessException();
			}
			// TODO check if file is in the comp fileCollection

			// find sample accesses
			List<AccessibilityBean> sampleAccesses = this
					.findSampleAccesses(comp.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				this.saveAccessibility(access, file.getId().toString());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in assigning composition file accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void removeAccesses(NanomaterialEntity entity)
			throws CompositionException, NoAccessException {
		try {
			if (!securityService.checkCreatePermission(entity.getId()
					.toString())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(entity.getSampleComposition()
							.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.removeAccessibility(access, entity, false);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in removing nanomaterial entity accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void removeAccesses(FunctionalizingEntity entity)
			throws CompositionException, NoAccessException {
		try {
			if (!securityService.checkCreatePermission(entity.getId()
					.toString())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(entity.getSampleComposition()
							.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.removeAccessibility(access, entity, false);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in removing functionalizing entity accessibility";
			throw new CompositionException(error, e);
		}

	}

	public void removeAccesses(ChemicalAssociation assoc)
			throws CompositionException, NoAccessException {
		try {
			if (!securityService
					.checkCreatePermission(assoc.getId().toString())) {
				throw new NoAccessException();
			}
			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(assoc.getSampleComposition()
							.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				accessUtils.removeAccessibility(access, assoc, false);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in removing chemical association accessibility";
			throw new CompositionException(error, e);
		}
	}

	public void removeAccesses(SampleComposition comp, File file)
			throws CompositionException, NoAccessException {
		try {
			if (!securityService.checkCreatePermission(file.getId().toString())) {
				throw new NoAccessException();
			}
			// TODO check if file is in the comp fileCollection

			// find sample accesses
			List<AccessibilityBean> sampleAccesses = super
					.findSampleAccesses(comp.getSample().getId().toString());
			for (AccessibilityBean access : sampleAccesses) {
				super.deleteAccessibility(access, file.getId().toString());
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in removing composition file accessibility";
			throw new CompositionException(error, e);
		}
	}
}
