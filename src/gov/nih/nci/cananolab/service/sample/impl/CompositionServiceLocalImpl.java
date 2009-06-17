package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
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
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
public class CompositionServiceLocalImpl implements CompositionService {
	private static Logger logger = Logger
			.getLogger(CompositionServiceLocalImpl.class);
	private CompositionServiceHelper helper = new CompositionServiceHelper();

	public CompositionServiceLocalImpl() {
	}

	public void saveNanomaterialEntity(Sample sample,
			NanomaterialEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
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

			// save file data to file system and assign visibility
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
				fileService.writeFile(fileBean, user);
			}
			appService.saveOrUpdate(entity);

			// set visibility
			List<String> accessibleGroups = helper.getAuthService()
					.getAccessibleGroups(sample.getName(),
							Constants.CSM_READ_PRIVILEGE);
			if (accessibleGroups != null
					&& accessibleGroups.contains(Constants.CSM_PUBLIC_GROUP)) {
				// set composition public
				helper.getAuthService().assignPublicVisibility(
						sample.getSampleComposition().getId().toString());
				helper.assignPublicVisibility(entity);
			}

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
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					NanomaterialEntity.class).add(
					Property.forName("id").eq(new Long(entityId)));
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.sample", FetchMode.JOIN);
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
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
			crit.setFetchMode(
					"composingElementCollection.inherentFunctionCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"composingElementCollection.inherentFunctionCollection.targetCollection",
							FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List result = appService.query(crit);
			NanomaterialEntity entity = null;
			if (!result.isEmpty()) {
				entity = (NanomaterialEntity) result.get(0);
				// check whether has access to the sample
				if (helper.getAuthService().checkReadPermission(
						user,
						entity.getSampleComposition().getSample().getName()
								.toString())) {
					entityBean = new NanomaterialEntityBean(entity);
					retrieveFilesVisibilities(entityBean.getFiles(), user);
				} else {
					throw new NoAccessException(
							"User doesn't have access to the sample");
				}
			}
			return entityBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the nanomaterial entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveFunctionalizingEntity(Sample sample,
			FunctionalizingEntityBean entityBean, UserBean user)
			throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
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

			// save file data to file system and assign visibility
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : entityBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
				fileService.writeFile(fileBean, user);
			}
			appService.saveOrUpdate(entity);

			// set visibility
			List<String> accessibleGroups = helper.getAuthService()
					.getAccessibleGroups(sample.getName(),
							Constants.CSM_READ_PRIVILEGE);
			if (accessibleGroups != null
					&& accessibleGroups.contains(Constants.CSM_PUBLIC_GROUP)) {
				// set composition public
				helper.getAuthService().assignPublicVisibility(
						sample.getSampleComposition().getId().toString());
				helper.assignPublicVisibility(entity);
			}

		} catch (Exception e) {
			String err = "Problem saving the functionalizing entity.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveChemicalAssociation(Sample sample,
			ChemicalAssociationBean assocBean, UserBean user)
			throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			ChemicalAssociation assoc = assocBean.getDomainAssociation();
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
			// save file data to file system and set visibility
			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : assocBean.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
				fileService.writeFile(fileBean, user);
			}
			appService.saveOrUpdate(assoc);

			// set public visibility
			List<String> accessibleGroups = helper.getAuthService()
					.getAccessibleGroups(sample.getName(),
							Constants.CSM_READ_PRIVILEGE);
			if (accessibleGroups != null
					&& accessibleGroups.contains(Constants.CSM_PUBLIC_GROUP)) {
				// set composition public
				helper.getAuthService().assignPublicVisibility(
						sample.getSampleComposition().getId().toString());
				helper.assignPublicVisibility(assoc);
			}

		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void saveCompositionFile(Sample particleSample, FileBean fileBean,
			UserBean user) throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			File file = fileBean.getDomainFile();
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(file, user);
			// write file to file system and assign visibility
			fileService.writeFile(fileBean, user);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			if (particleSample.getSampleComposition() == null) {
				particleSample.setSampleComposition(new SampleComposition());
				particleSample.getSampleComposition().setSample(particleSample);
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
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					FunctionalizingEntity.class).add(
					Property.forName("id").eq(new Long(entityId)));
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.sample", FetchMode.JOIN);
			crit.setFetchMode("activationMethod", FetchMode.JOIN);
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			crit.setFetchMode("functionCollection", FetchMode.JOIN);
			crit.setFetchMode("functionCollection.targetCollection",
					FetchMode.JOIN);
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
			FunctionalizingEntity entity = null;
			if (!result.isEmpty()) {
				entity = (FunctionalizingEntity) result.get(0);
				if (helper.getAuthService().checkReadPermission(user,
						entity.getSampleComposition().getSample().getName())) {
					entityBean = new FunctionalizingEntityBean(entity);
					retrieveFilesVisibilities(entityBean.getFiles(), user);
				} else {
					throw new NoAccessException(
							"User doesn't have access to the sample");
				}
			}
			return entityBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the functionalizing entity by id: "
					+ entityId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public ChemicalAssociationBean findChemicalAssociationById(String assocId,
			UserBean user) throws CompositionException, NoAccessException {
		ChemicalAssociationBean assocBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ChemicalAssociation.class).add(
					Property.forName("id").eq(new Long(assocId)));
			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.sample", FetchMode.JOIN);
			crit.setFetchMode("fileCollection", FetchMode.JOIN);
			crit.setFetchMode("fileCollection.keywordCollection",
					FetchMode.JOIN);
			crit.setFetchMode("associatedElementA", FetchMode.JOIN);
			crit.setFetchMode("associatedElementA.nanomaterialEntity",
					FetchMode.JOIN);
			crit.setFetchMode("associatedElementB", FetchMode.JOIN);
			crit.setFetchMode("associatedElementB.nanomaterialEntity",
					FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			ChemicalAssociation assoc = null;
			if (!result.isEmpty()) {
				assoc = (ChemicalAssociation) result.get(0);
				if (helper.getAuthService().checkReadPermission(user,
						assoc.getSampleComposition().getSample().getName())) {
					assocBean = new ChemicalAssociationBean(assoc);
					retrieveFilesVisibilities(assocBean.getFiles(), user);
				} else {
					throw new NoAccessException();
				}
			}
			return assocBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the chemical association by id: "
					+ assocId;
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	private void retrieveFilesVisibilities(Collection<FileBean> fileBeans,
			UserBean user) throws CompositionException {
		try {
			FileServiceHelper fileServiceHelper = new FileServiceHelper();
			for (FileBean fileBean : fileBeans) {
				fileServiceHelper.retrieveVisibility(fileBean, user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for files ";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteNanomaterialEntity(NanomaterialEntity entity,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException {
		if (user == null || !user.isCurator() && !user.isAdmin()) {
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
			helper.removePublicVisibility(entity);
		} catch (Exception e) {
			String err = "Error deleting nanomaterial entity " + entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteFunctionalizingEntity(FunctionalizingEntity entity,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException {
		if (user == null || !user.isCurator() && !user.isAdmin()) {
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
			helper.removePublicVisibility(entity);
		} catch (Exception e) {
			String err = "Error deleting functionalizing entity "
					+ entity.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteChemicalAssociation(ChemicalAssociation assoc,
			UserBean user) throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator() && !user.isAdmin()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(assoc);
			helper.removePublicVisibility(assoc);
		} catch (Exception e) {
			String err = "Error deleting chemical association " + assoc.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public void deleteCompositionFile(Sample particleSample, File file,
			UserBean user) throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator() && !user.isAdmin()) {
			throw new NoAccessException();
		}
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
	private boolean checkChemicalAssociationBeforeDelete(
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
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(SampleComposition.class);
			crit.createAlias("sample", "sample");
			crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
			// fully load composition
			crit.setFetchMode("sample", FetchMode.JOIN);
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
			crit
					.setFetchMode(
							"functionalizingEntityCollection.functionCollection.targetCollection",
							FetchMode.JOIN);
			crit.setFetchMode(
					"functionalizingEntityCollection.activationMethod",
					FetchMode.JOIN);
			crit.setFetchMode("chemicalAssociationCollection", FetchMode.JOIN);
			crit.setFetchMode("chemicalAssociationCollection.fileCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"chemicalAssociationCollection.fileCollection.keywordCollection",
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
				if (helper.getAuthService().checkReadPermission(user,
						composition.getSample().getName())) {
					comp = new CompositionBean(composition);
					retrieveFilesVisibilities(comp.getFiles(), user);
					for (NanomaterialEntityBean entity : comp
							.getNanomaterialEntities()) {
						retrieveFilesVisibilities(entity.getFiles(), user);
					}
					for (FunctionalizingEntityBean entity : comp
							.getFunctionalizingEntities()) {
						retrieveFilesVisibilities(entity.getFiles(), user);
					}
					for (ChemicalAssociationBean assoc : comp
							.getChemicalAssociations()) {
						retrieveFilesVisibilities(assoc.getFiles(), user);
					}
				} else {
					throw new NoAccessException(
							"User doesn't have access to the sample");
				}
			}
			return comp;
		} catch (Exception e) {
			String err = "Error finding composition by sample ID: " + sampleId;
			throw new CompositionException(err, e);
		}
	}

	/**
	 * Export sample composition summary report as Excel spread sheet.
	 *
	 * @param summaryBean
	 *            CompositionBean
	 * @param out
	 *            OutputStream
	 * @throws CompositionException
	 *             if error occurred.
	 */
	public void exportSummary(CompositionBean summaryBean,
			HttpServletRequest request, OutputStream out)
			throws CompositionException {
		try {
			helper.exportSummary(summaryBean, request, out);
		} catch (Exception e) {
			String err = "Error exporting Sample Composition Summary Excell report.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}
}
