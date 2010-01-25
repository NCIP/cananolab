package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Material;
import gov.nih.nci.cananolab.domain.material.Antibody;
import gov.nih.nci.cananolab.domain.material.Biopolymer;
import gov.nih.nci.cananolab.domain.material.CarbonNanotube;
import gov.nih.nci.cananolab.domain.material.Dendrimer;
import gov.nih.nci.cananolab.domain.material.Emulsion;
import gov.nih.nci.cananolab.domain.material.Fullerene;
import gov.nih.nci.cananolab.domain.material.Liposome;
import gov.nih.nci.cananolab.domain.material.Polymer;
import gov.nih.nci.cananolab.domain.material.SmallMolecule;
import gov.nih.nci.cananolab.dto.common.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.MaterialBean;
import gov.nih.nci.cananolab.dto.common.SampleBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
	
	private static String fileRoot = PropertyUtils.getProperty(
			Constants.CANANOLAB_PROPERTY, Constants.FILE_REPOSITORY_DIR);

	private CompositionServiceHelper helper = new CompositionServiceHelper();
	private FileServiceHelper fileHelper = new FileServiceHelper();

	public CompositionServiceLocalImpl() {
	}

//	public void saveNanomaterialEntity(SampleBean sampleBean,
//			NanomaterialEntityBean entityBean, UserBean user)
//			throws CompositionException, NoAccessException {
//		if (user == null || !user.isCurator()) {
//			throw new NoAccessException();
//		}
//		try {
//			Sample sample = sampleBean.getDomain();
//			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//					.getApplicationService();
//			NanomaterialEntity entity = entityBean.getDomainEntity();
//			if (entity.getId() != null) {
//				try {
//					NanomaterialEntity dbEntity = (NanomaterialEntity) appService
//							.load(NanomaterialEntity.class, entity.getId());
//				} catch (Exception e) {
//					String err = "Object doesn't exist in the database anymore.  Please log in again.";
//					logger.error(err);
//					throw new CompositionException(err, e);
//				}
//			}
//
//			if (sample.getSampleComposition() == null) {
//				sample.setSampleComposition(new SampleComposition());
//				sample.getSampleComposition().setSample(sample);
//				// particleSample.getSampleComposition()
//				// .setNanomaterialEntityCollection(
//				// new HashSet<NanomaterialEntity>());
//
//			}
//			entity.setSampleComposition(sample.getSampleComposition());
//			// particleSample.getSampleComposition()
//			// .getNanomaterialEntityCollection().add(entity);
//
//			// save file and keyword
//			FileService fileService = new FileServiceLocalImpl();
//			for (FileBean fileBean : entityBean.getFiles()) {
//				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
//			}
//			appService.saveOrUpdate(entity);
//			// save file to the file system and assign visibility
//			for (FileBean fileBean : entityBean.getFiles()) {
//				fileService.writeFile(fileBean, user);
//			}
//
//			String[] visibleGroups = sampleBean.getVisibilityGroups();
//			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
//					.getOrganization().getName();
//			// assign visibility for composition
//			helper.getAuthService().assignVisibility(
//					sample.getSampleComposition().getId().toString(),
//					visibleGroups, owningGroup);
//			// assign visibility for chemical association
//			helper.assignVisibility(entity, visibleGroups, owningGroup);
//		} catch (Exception e) {
//			String err = "Error in saving a nanomaterial entity.";
//			logger.error(err, e);
//			throw new CompositionException(err, e);
//		}
//	}
//
//	public NanomaterialEntityBean findNanomaterialEntityById(String entityId,
//			UserBean user) throws CompositionException, NoAccessException {
//		NanomaterialEntityBean entityBean = null;
//		try {
//			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//					.getApplicationService();
//
//			DetachedCriteria crit = DetachedCriteria.forClass(
//					NanomaterialEntity.class).add(
//					Property.forName("id").eq(new Long(entityId)));
//			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
//			crit.setFetchMode(
//					"sampleComposition.chemicalAssociationCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"sampleComposition.chemicalAssociationCollection.associatedElementA",
//							FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"sampleComposition.chemicalAssociationCollection.associatedElementB",
//							FetchMode.JOIN);
//			crit.setFetchMode("fileCollection", FetchMode.JOIN);
//			crit.setFetchMode("fileCollection.keywordCollection",
//					FetchMode.JOIN);
//			crit.setFetchMode("composingElementCollection", FetchMode.JOIN);
//			crit.setFetchMode(
//					"composingElementCollection.inherentFunctionCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"composingElementCollection.inherentFunctionCollection.targetCollection",
//							FetchMode.JOIN);
//			crit
//					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
//			List result = appService.query(crit);
//			NanomaterialEntity entity = null;
//			if (!result.isEmpty()) {
//				entity = (NanomaterialEntity) result.get(0);
//				if (helper.getAuthService().checkReadPermission(user,
//						entity.getId().toString())) {
//					entityBean = new NanomaterialEntityBean(entity);
//					fileHelper.checkReadPermissionAndRetrieveVisibility(
//							entityBean.getFiles(), user);
//				} else {
//					throw new NoAccessException(
//							"User doesn't have access to the sample");
//				}
//			}
//			return entityBean;
//		} catch (NoAccessException e) {
//			throw e;
//		} catch (Exception e) {
//			String err = "Problem finding the nanomaterial entity by id: "
//					+ entityId;
//			logger.error(err, e);
//			throw new CompositionException(err, e);
//		}
//	}

	public void saveMaterial(SampleBean sampleBean,
			MaterialBean materialBean, UserBean user)
			throws CompositionException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
//			Sample sample = sampleBean.getDomain();
//			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//					.getApplicationService();
//			FunctionalizingEntity entity = entityBean.getDomainEntity();
//			if (entity.getId() != null) {
//				try {
//					FunctionalizingEntity dbEntity = (FunctionalizingEntity) appService
//							.load(FunctionalizingEntity.class, entity.getId());
//				} catch (Exception e) {
//					String err = "Object doesn't exist in the database anymore.  Please log in again.";
//					logger.error(err);
//					throw new CompositionException(err, e);
//				}
//			}
//
//			if (sample.getSampleComposition() == null) {
//				sample.setSampleComposition(new SampleComposition());
//				sample.getSampleComposition().setSample(sample);
//				// particleSample.getSampleComposition()
//				// .setFunctionalizingEntityCollection(
//				// new HashSet<FunctionalizingEntity>());
//
//			}
//			entity.setSampleComposition(sample.getSampleComposition());
//			// particleSample.getSampleComposition()
//			// .getFunctionalizingEntityCollection().add(entity);
//
//			// save file and keyword
//			FileService fileService = new FileServiceLocalImpl();
//			for (FileBean fileBean : entityBean.getFiles()) {
//				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
//			}
//			appService.saveOrUpdate(entity);
//			// save file to the file system and assign visibility
//			for (FileBean fileBean : entityBean.getFiles()) {
//				fileService.writeFile(fileBean, user);
//			}
//
//			String[] visibleGroups = sampleBean.getVisibilityGroups();
//			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
//					.getOrganization().getName();
//			// assign visibility for composition
//			helper.getAuthService().assignVisibility(
//					sample.getSampleComposition().getId().toString(),
//					visibleGroups, owningGroup);
//			// assign visibility for chemical association
//			helper.assignVisibility(entity, visibleGroups, owningGroup);

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
////			ChemicalAssociation assoc = assocBean.getDomain();
////			Sample sample = sampleBean.getDomain();
////			if (assoc.getId() != null) {
////				try {
////					ChemicalAssociation dbAssoc = (ChemicalAssociation) appService
////							.load(ChemicalAssociation.class, assoc.getId());
////				} catch (Exception e) {
////					String err = "Object doesn't exist in the database anymore.  Please log in again.";
////					logger.error(err);
////					throw new CompositionException(err, e);
////				}
////			}
////			if (sample.getSampleComposition() == null) {
////				sample.setSampleComposition(new SampleComposition());
////				sample.getSampleComposition().setSample(sample);
////				// particleSample.getSampleComposition()
////				// .setFunctionalizingEntityCollection(
////				// new HashSet<FunctionalizingEntity>());
////
////			}
////			// composition.getChemicalAssociationCollection().add(assoc);
////			assoc.setSampleComposition(sample.getSampleComposition());
////			// save file and keyword
//			FileService fileService = new FileServiceLocalImpl();
//			for (FileBean fileBean : assocBean.getFiles()) {
//				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
//			}
//			appService.saveOrUpdate(assoc);
//			// save file to the file system and assign visibility
//			for (FileBean fileBean : assocBean.getFiles()) {
//				fileService.writeFile(fileBean, user);
//			}
//
//			String[] visibleGroups = sampleBean.getVisibilityGroups();
//			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
//					.getOrganization().getName();
//			// assign visibility for composition
//			helper.getAuthService().assignVisibility(
//					sample.getSampleComposition().getId().toString(),
//					visibleGroups, owningGroup);
//			// assign visibility for chemical association
//			helper.assignVisibility(assoc, visibleGroups, owningGroup);
		} catch (Exception e) {
			String err = "Problem saving the chemical assocation.";
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

	public MaterialBean findMaterialById(
			String materialId, UserBean user) throws CompositionException,
			NoAccessException {
		MaterialBean materialBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

//			DetachedCriteria crit = DetachedCriteria.forClass(
//					FunctionalizingEntity.class).add(
//					Property.forName("id").eq(new Long(entityId)));
//			crit.setFetchMode("activationMethod", FetchMode.JOIN);
//			crit.setFetchMode("fileCollection", FetchMode.JOIN);
//			crit.setFetchMode("fileCollection.keywordCollection",
//					FetchMode.JOIN);
//			crit.setFetchMode("functionCollection", FetchMode.JOIN);
//			crit.setFetchMode("functionCollection.targetCollection",
//					FetchMode.JOIN);
//			crit.setFetchMode("sampleComposition", FetchMode.JOIN);
//			crit.setFetchMode(
//					"sampleComposition.chemicalAssociationCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"sampleComposition.chemicalAssociationCollection.associatedElementA",
//							FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"sampleComposition.chemicalAssociationCollection.associatedElementB",
//							FetchMode.JOIN);
//			crit
//					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
//
//			List result = appService.query(crit);
//			FunctionalizingEntity entity = null;
//			if (!result.isEmpty()) {
//				entity = (FunctionalizingEntity) result.get(0);
//				if (helper.getAuthService().checkReadPermission(user,
//						entity.getId().toString())) {
//					entityBean = new FunctionalizingEntityBean(entity);
//					fileHelper.checkReadPermissionAndRetrieveVisibility(
//							entityBean.getFiles(), user);
//				} else {
//					throw new NoAccessException(
//							"User doesn't have access to the sample");
//				}
//			}
			return materialBean;
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
//			String err = "Problem finding the functionalizing entity by id: "
//					+ entityId;
//			logger.error(err, e);
			//throw new CompositionException(err, e);
			throw new CompositionException();
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
						assoc.getId().toString())) {
					assocBean = new ChemicalAssociationBean(assoc);
					fileHelper.checkReadPermissionAndRetrieveVisibility(
							assocBean.getFiles(), user);
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

	public void deleteMaterial(Material material,
			UserBean user) throws CompositionException,
			ChemicalAssociationViolationException, NoAccessException {
		if (user == null || !user.isCurator() && !user.isAdmin()) {
			throw new NoAccessException();
		}
//		Boolean canDelete = this.checkChemicalAssociationBeforeDelete(entity);
//		if (!canDelete) {
//			throw new ChemicalAssociationViolationException(
//					"The functionalizing entity "
//							+ entity.getName()
//							+ " is used in a chemical association.  Please delete the chemcial association first before deleting the functionalizing entity.");
//		}
//		try {
//			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//					.getApplicationService();
//			appService.delete(entity);
//			helper.removeVisibility(entity);
//		} catch (Exception e) {
//			String err = "Error deleting functionalizing entity "
//					+ entity.getId();
//			logger.error(err, e);
//			throw new CompositionException(err, e);
//		}
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
//			helper.removeVisibility(assoc);
		} catch (Exception e) {
			String err = "Error deleting chemical association " + assoc.getId();
			logger.error(err, e);
			throw new CompositionException(err, e);
		}
	}

//	// check if any composing elements of the nanomaterial entity is invovled in
//	// the chemical association
//	public boolean checkChemicalAssociationBeforeDelete(
//			NanomaterialEntity entity) {
//		// need to delete chemical associations first if associated elements
//		// are composing elements
//		Collection<ChemicalAssociation> assocSet = entity
//				.getSampleComposition().getChemicalAssociationCollection();
//		if (assocSet != null) {
//			for (ChemicalAssociation assoc : assocSet) {
//				if (entity.getComposingElementCollection().contains(
//						assoc.getAssociatedElementA())
//						|| entity.getComposingElementCollection().contains(
//								assoc.getAssociatedElementB())) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	// check if the composing element is invovled in the chemical
//	// association
//	private boolean checkChemicalAssociationBeforeDelete(
//			FunctionalizingEntity entity) {
//		// need to delete chemical associations first if associated elements
//		// are functionalizing entities
//		Collection<ChemicalAssociation> assocSet = entity
//				.getSampleComposition().getChemicalAssociationCollection();
//		if (assocSet != null) {
//			for (ChemicalAssociation assoc : assocSet) {
//				if (entity.equals(assoc.getAssociatedElementA())
//						|| entity.equals(assoc.getAssociatedElementB())) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}

//	public CompositionBean findCompositionBySampleId(String sampleId,
//			UserBean user) throws CompositionException {
//		try {
//			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//					.getApplicationService();
//			DetachedCriteria crit = DetachedCriteria
//					.forClass(SampleComposition.class);
//			crit.createAlias("sample", "sample");
//			crit.add(Property.forName("sample.id").eq(new Long(sampleId)));
//			crit.setFetchMode("nanomaterialEntityCollection", FetchMode.JOIN);
//			crit.setFetchMode("nanomaterialEntityCollection.fileCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"nanomaterialEntityCollection.fileCollection.keywordCollection",
//							FetchMode.JOIN);
//			crit.setFetchMode(
//					"nanomaterialEntityCollection.composingElementCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
//							FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection.targetCollection",
//							FetchMode.JOIN);
//			crit
//					.setFetchMode("functionalizingEntityCollection",
//							FetchMode.JOIN);
//			crit.setFetchMode("functionalizingEntityCollection.fileCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"functionalizingEntityCollection.fileCollection.keywordCollection",
//							FetchMode.JOIN);
//			crit.setFetchMode(
//					"functionalizingEntityCollection.functionCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"functionalizingEntityCollection.functionCollection.targetCollection",
//							FetchMode.JOIN);
//			crit.setFetchMode(
//					"functionalizingEntityCollection.activationMethod",
//					FetchMode.JOIN);
//			crit.setFetchMode("chemicalAssociationCollection", FetchMode.JOIN);
//			crit.setFetchMode("chemicalAssociationCollection.fileCollection",
//					FetchMode.JOIN);
//			crit
//					.setFetchMode(
//							"chemicalAssociationCollection.fileCollection.keywordCollection",
//							FetchMode.JOIN);
//			crit.setFetchMode(
//					"chemicalAssociationCollection.associatedElementA",
//					FetchMode.JOIN);
//			crit.setFetchMode(
//					"chemicalAssociationCollection.associatedElementB",
//					FetchMode.JOIN);
//			crit.setFetchMode("fileCollection", FetchMode.JOIN);
//			crit.setFetchMode("fileCollection.keywordCollection",
//					FetchMode.JOIN);
//			crit
//					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
//			List result = appService.query(crit);
//			CompositionBean comp = null;
//			if (!result.isEmpty()) {
//				SampleComposition composition = (SampleComposition) result
//						.get(0);
//				if (helper.getAuthService().checkReadPermission(user,
//						composition.getId().toString())) {
//					comp = new CompositionBean(composition);
//					fileHelper.checkReadPermissionAndRetrieveVisibility(comp
//							.getFiles(), user);
//					for (NanomaterialEntityBean entity : comp
//							.getNanomaterialEntities()) {
//						fileHelper.checkReadPermissionAndRetrieveVisibility(
//								entity.getFiles(), user);
//					}
//					for (FunctionalizingEntityBean entity : comp
//							.getFunctionalizingEntities()) {
//						fileHelper.checkReadPermissionAndRetrieveVisibility(
//								entity.getFiles(), user);
//					}
//					for (ChemicalAssociationBean assoc : comp
//							.getChemicalAssociations()) {
//						fileHelper.checkReadPermissionAndRetrieveVisibility(
//								assoc.getFiles(), user);
//					}
//				} else {
//					throw new NoAccessException(
//							"User doesn't have access to the sample");
//				}
//			}
//			return comp;
//		} catch (Exception e) {
//			String err = "Error finding composition by sample ID: " + sampleId;
//			throw new CompositionException(err, e);
//		}
//	}

	
	public void copyAndSaveMaterial(
			MaterialBean materialBean, SampleBean oldSampleBean,
			SampleBean[] newSampleBeans, UserBean user)
			throws CompositionException, NoAccessException {		
		try {
			for (SampleBean sampleBean : newSampleBeans) {
//				FunctionalizingEntityBean copyBean = null;
//				try {
//					FunctionalizingEntity copy = entityBean.getDomainCopy();
//					copyBean = new FunctionalizingEntityBean(copy);
//					// copy file visibility and file content
//					for (FileBean fileBean : copyBean.getFiles()) {
//						fileHelper.retrieveVisibilityAndContentForCopiedFile(fileBean,
//								user);
//					}
//				} catch (Exception e) {
//					String error = "Error in copying the functionalizing entity.";
//					throw new CompositionException(error, e);
//				}
//				// replace file URI with new sample name
//				for (FileBean fileBean : copyBean.getFiles()) {
//					String newUri = fileBean.getDomainFile().getUri().replace(
//							oldSampleBean.getDomain().getName(),
//							sampleBean.getDomain().getName());
//					fileBean.getDomainFile().setUri(newUri);
//				}
//				if (copyBean != null)
//					saveFunctionalizingEntity(sampleBean, copyBean, user);
			}
//		} catch (NoAccessException e) {
//			throw e;
		} catch (Exception e) {
			String error = "Error in copying the characterization.";
			throw new CompositionException(error, e);
		}
	}

	/**
	 * Export sample composition summary report as Excel spread sheet.
	 *
	 * @param compBean
	 * @param out
	 * @throws CompositionException
	 */
//	public static void exportSummary(CompositionBean compBean,
//			String downloadURL, OutputStream out) throws IOException {
//		if (out != null) {
//			HSSFWorkbook wb = new HSSFWorkbook();
//			outputSummarySheet(compBean, downloadURL, wb);
//			wb.write(out);
//			out.flush();
//			out.close();
//		}
//	}

	/**
	 * Outputting ChemicalEntities data =>
	 * bodyChemicalAssociationSummaryView.jsp
	 *
	 * @param compBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
//	private static int outputChemicalEntities(CompositionBean compBean,
//			HSSFWorkbook wb, HSSFCellStyle headerStyle,
//			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
//		List<ChemicalAssociationBean> nanoList = compBean
//				.getChemicalAssociations();
//		if (nanoList != null && !nanoList.isEmpty()) {
//			StringBuilder sb = new StringBuilder();
//			for (ChemicalAssociationBean nanoEntity : nanoList) {
//				if (!StringUtils.isEmpty(nanoEntity.getType())) {
//					int rowIndex = 0;
//					sb.setLength(0);
//					sb.append(entityCount++).append('.').append(
//							nanoEntity.getType());
//	
//					// Create one work sheet for each Composition.
//					HSSFSheet sheet = wb.createSheet(sb.toString());
//					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//	
//					// 1. Output Composition type at (0, 0).
//					rowIndex = outputHeader(CompositionBean.CHEMICAL_SELECTION,
//							nanoEntity.getType(), sheet, headerStyle, rowIndex);
//	
//					// 2. Output Bond Type.
//					if (nanoEntity.getAttachment().getId() != null
//							&& !StringUtils.isEmpty(nanoEntity.getAttachment()
//									.getBondType())) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils
//								.createCell(row, 0, headerStyle, "Bond Type");
//						ExportUtils.createCell(row, 1, nanoEntity
//								.getAttachment().getBondType());
//					}
//	
//					// 3. Output Description.
//					if (!StringUtils.isEmpty(nanoEntity.getDescription())) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Description");
//						ExportUtils.createCell(row, 1, nanoEntity
//								.getDescription());
//					}
//	
//					// Output Associated Elements.
//					rowIndex++; // Create an empty line as seperator.
//					HSSFRow row = sheet.createRow(rowIndex++);
//					ExportUtils.createCell(row, 0, headerStyle,
//							"Associated Elements");
//	
//					// 4a. Output Associated Element A.
//					row = sheet.createRow(rowIndex++);
//					sb.setLength(0);
//					sb.append(nanoEntity.getAssociatedElementA()
//							.getCompositionType());
//					sb.append(' ');
//					sb.append(nanoEntity.getAssociatedElementA()
//							.getEntityDisplayName());
//					Long compElementId = nanoEntity.getAssociatedElementA()
//							.getComposingElement().getId();
//					if (compElementId != null) {
//						sb.append(" composing element of type ");
//						sb.append(nanoEntity.getAssociatedElementA()
//								.getComposingElement().getType());
//						sb.append(" (name: ");
//						sb.append(nanoEntity.getAssociatedElementA()
//								.getComposingElement().getName());
//						sb.append(')');
//					} else {
//						sb.append(" (name: ");
//						sb.append(nanoEntity.getAssociatedElementA()
//								.getDomainElement().getName());
//						sb.append(')');
//					}
//					ExportUtils.createCell(row, 0, sb.toString());
//	
//					ExportUtils.createCell(row, 1, "associated with");
//	
//					// 4b. Output Associated Element B.
//					sb.setLength(0);
//					sb.append(nanoEntity.getAssociatedElementB()
//							.getCompositionType());
//					sb.append(' ');
//					sb.append(nanoEntity.getAssociatedElementB()
//							.getEntityDisplayName());
//					compElementId = nanoEntity.getAssociatedElementB()
//							.getComposingElement().getId();
//					if (compElementId != null) {
//						sb.append(" composing element of type ");
//						sb.append(nanoEntity.getAssociatedElementB()
//								.getComposingElement().getType());
//						sb.append(" (name: ");
//						sb.append(nanoEntity.getAssociatedElementB()
//								.getComposingElement().getName());
//						sb.append(')');
//					} else {
//						sb.append(" (name: ");
//						sb.append(nanoEntity.getAssociatedElementB()
//								.getDomainElement().getName());
//						sb.append(')');
//					}
//					ExportUtils.createCell(row, 2, sb.toString());
//					rowIndex++; // Create an empty line as seperator.
//	
//					// 5. Output Files: bodyFileView.jsp
//					List<FileBean> fileBeans = nanoEntity.getFiles();
//					if (fileBeans != null && !fileBeans.isEmpty()) {
//						rowIndex++; // Create one empty line as separator.
//						row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle, "Files");
//						outputFiles(fileBeans, downloadURL, wb, sheet,
//								headerStyle, hlinkStyle, patriarch, rowIndex);
//					}
//				}
//			}
//		}
//		return entityCount;
//	}

	/**
	 * Output Composition File info => bodyCompositionFileSummaryView.jsp
	 *
	 * @param fileBeans
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputFile(FileBean fileBean, String downloadURL,
			HSSFWorkbook wb, HSSFSheet sheet, HSSFCellStyle headerStyle,
			HSSFCellStyle hlinkStyle, HSSFPatriarch patriarch, int rowIndex) {
		File file = fileBean.getDomainFile();
	
		// 1. output File Type.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, file.getType());
		rowIndex++; // Create one empty line as separator.
	
		// 2. output Title and Download Link.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Title and Download Link");
	
		// Construct the URL for downloading the file.
		StringBuilder sb = new StringBuilder(downloadURL);
		sb.append(file.getId());
		if (file.getUriExternal()) {
			ExportUtils.createCell(row, 1, hlinkStyle, file.getUri(), sb
					.toString());
		} else if (fileBean.isImage()) {
			ExportUtils.createCell(row, 1, file.getTitle());
			sb.setLength(0);
			sb.append(fileRoot).append(java.io.File.separator);
			sb.append(file.getUri());
			String filePath = sb.toString();
			java.io.File imgFile = new java.io.File(filePath);
			if (imgFile.exists()) {
				try {
					rowIndex = ExportUtils.createImage(rowIndex, (short) 1,
							filePath, wb, sheet, patriarch);
				} catch (Exception e) {
					logger.error("Error exporting Comp image file.", e);
				}
			} else {
				logger.error("Composition image file not exists: " + filePath);
			}
		} else {
			ExportUtils.createCell(row, 1, hlinkStyle, file.getTitle(), sb
					.toString());
		}
	
		// 3. output Keywords.
		Collection<Keyword> keywords = file.getKeywordCollection();
		if (keywords != null && !keywords.isEmpty()) {
			sb.setLength(0);
			for (Keyword keyword : keywords) {
				sb.append(',').append(' ').append(keyword.getName());
			}
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, "Keywords");
			ExportUtils.createCell(row, 1, sb.substring(2));
		}
	
		// 4. output Description.
		if (!StringUtils.isEmpty(file.getDescription())) {
			row = sheet.createRow(rowIndex++);
			ExportUtils.createCell(row, 0, headerStyle, "Description");
			ExportUtils.createCell(row, 1, file.getDescription());
		}
	
		return rowIndex;
	}

	/**
	 * Outputting Files info: => bodyFileView.jsp
	 *
	 * @param fileBeans
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputFiles(List<FileBean> fileBeans,
			String downloadURL, HSSFWorkbook wb, HSSFSheet sheet,
			HSSFCellStyle headerStyle, HSSFCellStyle hlinkStyle,
			HSSFPatriarch patriarch, int rowIndex) {
		// Output file table Header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "File Type");
		ExportUtils.createCell(row, 1, headerStyle, "Title and Download Link");
		ExportUtils.createCell(row, 2, headerStyle, "Keywords");
		ExportUtils.createCell(row, 3, headerStyle, "Description");
		for (FileBean fileBean : fileBeans) {
			File file = fileBean.getDomainFile();
			if (!StringUtils.isEmpty(file.getType())) {
				// 1. output File Type.
				row = sheet.createRow(rowIndex++);
				ExportUtils.createCell(row, 0, file.getType());
	
				/**
				 * 2. output Title and Download Link. Construct the URL for
				 * downloading the file.
				 */
				StringBuilder sb = new StringBuilder(downloadURL);
				sb.append(file.getId());
				if (file.getUriExternal().booleanValue()) {
					ExportUtils.createCell(row, 1, hlinkStyle, file.getUri(),
							sb.toString());
				} else if (fileBean.isImage()) {
					ExportUtils.createCell(row, 1, file.getTitle());
					sb.setLength(0);
					sb.append(fileRoot).append(java.io.File.separator);
					sb.append(file.getUri());
					String filePath = sb.toString();
					java.io.File imgFile = new java.io.File(filePath);
					if (imgFile.exists()) {
						try {
							rowIndex = ExportUtils.createImage(rowIndex,
									(short) 1, filePath, wb, sheet, patriarch);
						} catch (Exception e) {
							logger.error("Error exporting Comp image file.", e);
						}
					} else {
						logger.error("Composition image file not exists: "
								+ filePath);
					}
				} else {
					ExportUtils.createCell(row, 1, hlinkStyle, file.getTitle(),
							sb.toString());
				}
	
				// 3. output Keywords.
				Collection<Keyword> keywords = file.getKeywordCollection();
				if (keywords != null && !keywords.isEmpty()) {
					sb.setLength(0);
					for (Keyword keyword : keywords) {
						sb.append(',').append(' ').append(keyword.getName());
					}
					ExportUtils.createCell(row, 2, sb.substring(2));
				}
	
				// 4. output Description.
				if (!StringUtils.isEmpty(file.getDescription())) {
					ExportUtils.createCell(row, 3, file.getDescription());
				}
			}
		}
		return rowIndex;
	}

	/**
	 * Output Composition Files data => bodyCompositionFileSummaryView.jsp
	 *
	 * @param compBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
//	private static int outputFilesEntities(CompositionBean compBean,
//			HSSFWorkbook wb, HSSFCellStyle headerStyle,
//			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
//		List<FileBean> nanoList = compBean.getFiles();
//		if (nanoList != null && !nanoList.isEmpty()) {
//			StringBuilder sb = new StringBuilder();
//			for (FileBean nanoEntity : nanoList) {
//				if (!StringUtils.isEmpty(nanoEntity.getDomainFile().getType())) {
//					int rowIndex = 0;
//					sb.setLength(0);
//					sb.append(entityCount++).append('.').append(
//							nanoEntity.getDomainFile().getType());
//	
//					// Create one work sheet for each Composition File.
//					HSSFSheet sheet = wb.createSheet(sb.toString());
//					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//	
//					// 1. Output Composition type at (0, 0).
//					HSSFRow row = sheet.createRow(rowIndex++);
//					ExportUtils.createCell(row, 0, headerStyle,
//							CompositionBean.FILE_SELECTION);
//					rowIndex++; // Create one empty line as separator.
//	
//					// 2. Output File info, one File per sheet.
//					outputFile(nanoEntity, downloadURL, wb, sheet, headerStyle,
//							hlinkStyle, patriarch, rowIndex);
//				}
//			}
//		}
//		return entityCount;
//	}

	/**
	 * Output Antibody Info for FunctionalizingEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputFuncProperties(Antibody entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Type");
		ExportUtils.createCell(row, 1, headerStyle, "Isotype");
		ExportUtils.createCell(row, 2, headerStyle, "Species");
	
		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, entityBean.getType());
		ExportUtils.createCell(row, 1, entityBean.getIsotype());
		ExportUtils.createCell(row, 2, entityBean.getSpecies());
	
		return rowIndex;
	}

	/**
	 * Output Biopolymer Info for FunctionalizingEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
//	private static int outputFuncProperties(
//			gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer entityBean,
//			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
//	
//		// 1. Output table header.
//		HSSFRow row = sheet.createRow(rowIndex++);
//		ExportUtils.createCell(row, 0, headerStyle, "Type");
//		ExportUtils.createCell(row, 1, headerStyle, "Isotype");
//	
//		// 2. Output table data.
//		row = sheet.createRow(rowIndex++);
//		ExportUtils.createCell(row, 0, entityBean.getType());
//		ExportUtils.createCell(row, 1, entityBean.getSequence());
//	
//		return rowIndex;
//	}

	/**
	 * Output SmallMolecule Info for FunctionalizingEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputFuncProperties(SmallMolecule entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output SmallMolecule Info.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Alternate Name");
		ExportUtils.createCell(row, 1, entityBean.getAlternateName());
	
		return rowIndex;
	}

	/**
	 * Output FunctionalEntities data =>
	 * bodyFunctionalizingEntitySummaryView.jsp
	 *
	 * @param compBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
//	private static int outputFunctionalEntities(CompositionBean compBean,
//			HSSFWorkbook wb, HSSFCellStyle headerStyle,
//			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
//		List<FunctionalizingEntityBean> nanoList = compBean
//				.getFunctionalizingEntities();
//		if (nanoList != null && !nanoList.isEmpty()) {
//			StringBuilder sb = new StringBuilder();
//			for (FunctionalizingEntityBean nanoEntity : nanoList) {
//				if (!StringUtils.isEmpty(nanoEntity.getType())) {
//					int rowIndex = 0;
//					sb.setLength(0);
//					sb.append(entityCount++).append('.').append(
//							nanoEntity.getType());
//	
//					// Create 1 work sheet for each Functional Entity.
//					HSSFSheet sheet = wb.createSheet(sb.toString());
//					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//	
//					// 1. Output Composition type at (0, 0).
//					rowIndex = outputHeader(
//							CompositionBean.FUNCTIONALIZING_SELECTION,
//							nanoEntity.getType(), sheet, headerStyle, rowIndex);
//	
//					// 2. Output Name.
//					if (!StringUtils.isEmpty(nanoEntity.getName())) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle, "Name");
//						ExportUtils.createCell(row, 1, nanoEntity.getName());
//					}
//	
//					// 3. Output PubChem.
//					if (nanoEntity.getDomainEntity().getPubChemId() != null) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"PubChem ID");
//						Long pubChemId = nanoEntity.getDomainEntity()
//								.getPubChemId();
//						String pubChemDs = nanoEntity.getDomainEntity()
//								.getPubChemDataSourceName();
//						sb.setLength(0);
//						sb.append(pubChemId).append(' ');
//						sb.append('(').append(pubChemDs).append(')');
//						ExportUtils.createCell(row, 1, hlinkStyle, sb
//								.toString(), CompositionServiceHelper
//								.getPubChemURL(pubChemDs, pubChemId));
//					}
//	
//					// Output Amount.
//					if (nanoEntity.getValue() != null) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle, "Amount");
//						sb.setLength(0);
//						sb.append(nanoEntity.getValue()).append(' ');
//						sb.append(nanoEntity.getValueUnit());
//						ExportUtils.createCell(row, 1, sb.toString());
//					}
//	
//					// 5. Output Molecular Formula.
//					if (!StringUtils.isEmpty(nanoEntity
//							.getMolecularFormulaDisplayName())) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Molecular Formula");
//						ExportUtils.createCell(row, 1, nanoEntity
//								.getMolecularFormulaDisplayName());
//					}
//	
//					// 6. Output Properties: 3x) "functionalizingEntity/*.jsp"
//					if (nanoEntity.isWithProperties()) {
//						rowIndex++; // Create one empty line as separator.
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Properties");
//	
//						FunctionalizingEntity domainEntity = nanoEntity
//								.getDomainEntity();
//						if (domainEntity instanceof Antibody) {
//							rowIndex = outputFuncProperties(
//									(Antibody) domainEntity, sheet,
//									headerStyle, rowIndex);
//						} else if (domainEntity instanceof SmallMolecule) {
//							rowIndex = outputFuncProperties(
//									(SmallMolecule) domainEntity, sheet,
//									headerStyle, rowIndex);
//						} else if (domainEntity instanceof gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer) {
//							rowIndex = outputFuncProperties(
//									(gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer) domainEntity,
//									sheet, headerStyle, rowIndex);
//						}
//						rowIndex++; // Create one empty line as separator.
//					}
//	
//					// 7. Output Functions: bodyFunctionView.jsp
//					List<FunctionBean> functions = nanoEntity.getFunctions();
//					if (functions != null && !functions.isEmpty()) {
//						if (!nanoEntity.isWithProperties()) {
//							rowIndex++; // Create one empty line as separator.
//						}
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Function(s)");
//	
//						// 6.1. output Functions table header.
//						int colIndex = 0;
//						row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, colIndex++, headerStyle,
//								"Type");
//						if (nanoEntity.isWithImagingFunction()) {
//							ExportUtils.createCell(row, colIndex++,
//									headerStyle, "Image Modality");
//						}
//						if (nanoEntity.isWithTargetingFunction()) {
//							ExportUtils.createCell(row, colIndex++,
//									headerStyle, "Targets");
//						}
//						ExportUtils.createCell(row, colIndex, headerStyle,
//								"Description");
//	
//						// 6.2. output Functions table data.
//						for (FunctionBean function : functions) {
//							colIndex = 0;
//							row = sheet.createRow(rowIndex++);
//	
//							ExportUtils.createCell(row, colIndex++, function
//									.getType());
//							if (nanoEntity.isWithImagingFunction()) {
//								if (!StringUtils.isEmpty(function
//										.getImagingFunction().getModality())) {
//									ExportUtils.createCell(row, colIndex++,
//											function.getImagingFunction()
//													.getModality());
//								}
//							}
//							if (nanoEntity.isWithTargetingFunction()) {
//								String[] displayNames = function
//										.getTargetDisplayNames();
//								if (displayNames != null
//										&& displayNames.length > 0) {
//									sb.setLength(0);
//									for (String name : displayNames) {
//										sb.append(',').append(' ').append(name);
//									}
//									ExportUtils.createCell(row, colIndex++, sb
//											.substring(2));
//								}
//							}
//							ExportUtils.createCell(row, colIndex, function
//									.getDescription());
//						}
//						rowIndex++; // Create one empty line as separator.
//					}// 7. End of Output Functions: bodyFunctionView.jsp
//	
//					// 8. Output Activation Method.
//					if (!StringUtils.isEmpty(nanoEntity
//							.getActivationMethodDisplayName())) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Activation Method");
//						ExportUtils.createCell(row, 1, nanoEntity
//								.getActivationMethodDisplayName());
//					}
//	
//					// 9. Output Description.
//					if (!StringUtils.isEmpty(nanoEntity.getDescription())) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Description");
//						ExportUtils.createCell(row, 1, nanoEntity
//								.getDescription());
//					}
//	
//					// 10. Output Files: bodyFileView.jsp
//					List<FileBean> fileBeans = nanoEntity.getFiles();
//					if (fileBeans != null && !fileBeans.isEmpty()) {
//						rowIndex++; // Create one empty line as separator.
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle, "Files");
//						outputFiles(fileBeans, downloadURL, wb, sheet,
//								headerStyle, hlinkStyle, patriarch, rowIndex);
//					}
//				}
//			} // End of iterating nanoList.
//		}
//		return entityCount;
//	}

	/**
	 * Output header for work sheet.
	 *
	 * @param compType
	 * @param entityType
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
//	private static int outputHeader(String compType, String entityType,
//			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
//		// 1. Output Composition type at (0, 0).
//		HSSFRow row = sheet.createRow(rowIndex++);
//		ExportUtils.createCell(row, 0, headerStyle, compType);
//		rowIndex++; // Create one empty line as separator.
//	
//		// 2. Output Entity Type at (2, 0).
//		row = sheet.createRow(rowIndex++);
//		ExportUtils.createCell(row, 0, headerStyle, entityType);
//		rowIndex++; // Create one empty line as separator.
//	
//		return rowIndex;
//	}
//
//	/**
//	 * Output NanomaterialEntities data => bodyNanomaterialEntitySummaryView.jsp
//	 *
//	 * @param compBean
//	 * @param wb
//	 * @param headerStyle
//	 * @param entityCount
//	 */
//	private static int outputNanomaterialEntities(CompositionBean compBean,
//			HSSFWorkbook wb, HSSFCellStyle headerStyle,
//			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
//		List<NanomaterialEntityBean> nanoList = compBean
//				.getNanomaterialEntities();
//		if (nanoList != null && !nanoList.isEmpty()) {
//			StringBuilder sb = new StringBuilder();
//			for (NanomaterialEntityBean nanoEntity : nanoList) {
//				if (!StringUtils.isEmpty(nanoEntity.getType())) {
//					int rowIndex = 0;
//					sb.setLength(0);
//					sb.append(entityCount++).append('.').append(
//							nanoEntity.getType());
//	
//					// Create one work sheet for each Nanomaterial Entity.
//					HSSFSheet sheet = wb.createSheet(sb.toString());
//					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//	
//					// 1. Output Header: NanoMaterial at (0, 0), Composition
//					// Type at (2, 0).
//					rowIndex = outputHeader(
//							CompositionBean.NANOMATERIAL_SELECTION, nanoEntity
//									.getType(), sheet, headerStyle, rowIndex);
//	
//					// 2. Output Composition Description at (4, 0).
//					String description = nanoEntity.getEmulsion()
//							.getDescription();
//					if (!StringUtils.isEmpty(description)) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Description");
//						ExportUtils.createCell(row, 1, description);
//					}
//	
//					// 3. Output Composition Properties: 7x)
//					// "nanomaterialEntity/*.jsp"
//					if (nanoEntity.isWithProperties()) {
//						if (!StringUtils.isEmpty(description)) {
//							rowIndex++; // Create one empty line as separator.
//						}
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Properties");
//	
//						NanomaterialEntity domainEntity = nanoEntity
//								.getDomainEntity();
//						if (domainEntity instanceof Biopolymer) {
//							rowIndex = outputNanoProperties(
//									(Biopolymer) domainEntity, sheet,
//									headerStyle, rowIndex);
//						} else if (domainEntity instanceof Dendrimer) {
//							rowIndex = outputNanoProperties(
//									(Dendrimer) domainEntity, sheet,
//									headerStyle, rowIndex);
//						} else if (domainEntity instanceof CarbonNanotube) {
//							rowIndex = outputNanoProperties(
//									(CarbonNanotube) domainEntity, sheet,
//									headerStyle, rowIndex);
//						} else if (domainEntity instanceof Liposome) {
//							rowIndex = outputNanoProperties(
//									(Liposome) domainEntity, sheet,
//									headerStyle, rowIndex);
//						} else if (domainEntity instanceof Emulsion) {
//							rowIndex = outputNanoProperties(
//									(Emulsion) domainEntity, sheet,
//									headerStyle, rowIndex);
//						} else if (domainEntity instanceof Polymer) {
//							rowIndex = outputNanoProperties(
//									(Polymer) domainEntity, sheet, headerStyle,
//									rowIndex);
//						} else if (domainEntity instanceof Fullerene) {
//							rowIndex = outputNanoProperties(
//									(Fullerene) domainEntity, sheet,
//									headerStyle, rowIndex);
//						}
//						rowIndex++; // Create one empty line as separator.
//					}
//	
//					// 3. Output Composing Elements:
//					// bodyComposingElementView.jsp
//					List<ComposingElementBean> compElementBeans = nanoEntity
//							.getComposingElements();
//					if (compElementBeans != null && !compElementBeans.isEmpty()) {
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle,
//								"Composing Elements");
//	
//						row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle, "Type");
//						ExportUtils.createCell(row, 1, headerStyle,
//								"PubChem ID");
//						ExportUtils.createCell(row, 2, headerStyle, "Name");
//						ExportUtils.createCell(row, 3, headerStyle, "Amount");
//						ExportUtils.createCell(row, 4, headerStyle,
//								"Molecular Formula");
//						ExportUtils.createCell(row, 5, headerStyle, "Function");
//						ExportUtils.createCell(row, 6, headerStyle,
//								"Description");
//						for (ComposingElementBean compElementBean : compElementBeans) {
//							row = sheet.createRow(rowIndex++);
//							ComposingElement compElement = compElementBean
//									.getDomain();
//							ExportUtils.createCell(row, 0, compElement
//									.getType());
//	
//							if (compElement.getPubChemId() != null) {
//								Long pubChemId = compElement.getPubChemId();
//								String pubChemDs = compElement
//										.getPubChemDataSourceName();
//								sb.setLength(0);
//								sb.append(pubChemId).append(' ');
//								sb.append('(').append(pubChemDs).append(')');
//								ExportUtils.createCell(row, 1, hlinkStyle, sb
//										.toString(), CompositionServiceHelper
//										.getPubChemURL(pubChemDs, pubChemId));
//							}
//	
//							ExportUtils.createCell(row, 2, compElement
//									.getName());
//	
//							sb.setLength(0);
//							sb.append(compElement.getValue()).append(' ');
//							sb.append(compElement.getValueUnit());
//							ExportUtils.createCell(row, 3, sb.toString());
//	
//							if (!StringUtils.isEmpty(compElementBean
//									.getMolecularFormulaDisplayName())) {
//								ExportUtils.createCell(row, 4, compElementBean
//										.getMolecularFormulaDisplayName());
//							}
//	
//							String[] funcNames = compElementBean
//									.getFunctionDisplayNames();
//							if (funcNames != null && funcNames.length > 0) {
//								sb.setLength(0);
//								for (String funcName : funcNames) {
//									sb.append(',').append(' ').append(funcName);
//								}
//								ExportUtils.createCell(row, 5, sb.substring(2));
//							}
//	
//							if (!StringUtils.isEmpty(compElement
//									.getDescription())) {
//								ExportUtils.createCell(row, 6, compElement
//										.getDescription());
//							}
//						}
//						rowIndex++; // Create one empty line as separator.
//					}// 3. End of outputting Composing Elements.
//	
//					// Output Files: bodyFileView.jsp
//					List<FileBean> fileBeans = nanoEntity.getFiles();
//					if (fileBeans != null && !fileBeans.isEmpty()) {
//						rowIndex++; // Create one empty line as separator.
//						HSSFRow row = sheet.createRow(rowIndex++);
//						ExportUtils.createCell(row, 0, headerStyle, "Files");
//						outputFiles(fileBeans, downloadURL, wb, sheet,
//								headerStyle, hlinkStyle, patriarch, rowIndex);
//					}
//				}
//			} // End of iterating nanoList.
//		}
//	
//		return entityCount;
//	}

	/**
	 * Output Biopolymer Info for NanomaterialEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputNanoProperties(Biopolymer entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Name");
		ExportUtils.createCell(row, 1, headerStyle, "Type");
		ExportUtils.createCell(row, 2, headerStyle, "Sequence");
	
		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, entityBean.getName());
		ExportUtils.createCell(row, 1, entityBean.getType());
		ExportUtils.createCell(row, 2, entityBean.getSequence());
	
		return rowIndex;
	}

	/**
	 * Output CarbonNanotube Info for NanomaterialEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputNanoProperties(CarbonNanotube entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Average Length");
		ExportUtils.createCell(row, 1, headerStyle, "Chirality");
		ExportUtils.createCell(row, 2, headerStyle, "Diameter");
		ExportUtils.createCell(row, 3, headerStyle, "Wall Type");
	
		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		StringBuilder sb = new StringBuilder();
		sb.append(entityBean.getAverageLength());
		sb.append(' ');
		sb.append(entityBean.getAverageLengthUnit());
	
		ExportUtils.createCell(row, 0, sb.toString());
		ExportUtils.createCell(row, 1, entityBean.getChirality());
	
		sb.setLength(0);
		sb.append(entityBean.getDiameter());
		sb.append(' ');
		sb.append(entityBean.getDiameterUnit());
	
		ExportUtils.createCell(row, 2, sb.toString());
		ExportUtils.createCell(row, 3, entityBean.getWallType());
	
		return rowIndex;
	}

	/**
	 * Output Dendrimer Info for NanomaterialEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputNanoProperties(Dendrimer entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Branch");
		ExportUtils.createCell(row, 1, headerStyle, "Generation");
	
		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, entityBean.getBranch());
		ExportUtils.createCell(row, 1, String.valueOf(entityBean
				.getGeneration()));
	
		return rowIndex;
	}

	/**
	 * Output Emulsion Info for NanomaterialEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputNanoProperties(Emulsion entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Is Polymerized");
		ExportUtils.createCell(row, 1, headerStyle, "Polymer Name");
	
		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, String.valueOf(entityBean
				.getPolymerized()));
		ExportUtils.createCell(row, 1, entityBean.getPolymerName());
	
		return rowIndex;
	}

	/**
	 * Output Fullerene Info for NanomaterialEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputNanoProperties(Fullerene entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Average Diameter");
		ExportUtils.createCell(row, 1, headerStyle, "Number of Carbons");
	
		StringBuilder sb = new StringBuilder();
		sb.append(entityBean.getAverageDiameter());
		sb.append(' ');
		sb.append(entityBean.getAverageDiameterUnit());
		ExportUtils.createCell(row, 0, sb.toString());
		ExportUtils.createCell(row, 1, String.valueOf(entityBean
				.getNumberOfCarbon()));
	
		return rowIndex;
	}

	/**
	 * Output Liposome Info for NanomaterialEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputNanoProperties(Liposome entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Polymer Name");
		ExportUtils.createCell(row, 1, headerStyle, "Is Polymerized");
	
		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, entityBean.getPolymerName());
		ExportUtils.createCell(row, 1, String.valueOf(entityBean
				.getPolymerized()));
	
		return rowIndex;
	}

	/**
	 * Output Polymer Info for NanomaterialEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputNanoProperties(Polymer entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
	
		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Initiator");
		ExportUtils.createCell(row, 1, headerStyle, "Cross Link Degree");
		ExportUtils.createCell(row, 2, headerStyle, "Is Cross Linked");
	
		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, entityBean.getInitiator());
		ExportUtils.createCell(row, 1, String.valueOf(entityBean
				.getCrossLinkDegree()));
		ExportUtils.createCell(row, 2, String.valueOf(entityBean
				.getCrossLinked()));
	
		return rowIndex;
	}

	/**
	 * Output sample Composition Summary report =>
	 * bodyCompositionSummaryView.jsp
	 *
	 * @param compBean
	 * @param wb
	 */
//	private static void outputSummarySheet(CompositionBean compBean,
//			String downloadURL, HSSFWorkbook wb) throws IOException {
//		HSSFFont headerFont = wb.createFont();
//		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		HSSFCellStyle headerStyle = wb.createCellStyle();
//		headerStyle.setFont(headerFont);
//	
//		HSSFCellStyle hlinkStyle = wb.createCellStyle();
//		HSSFFont hlinkFont = wb.createFont();
//		hlinkFont.setUnderline(HSSFFont.U_SINGLE);
//		hlinkFont.setColor(HSSFColor.BLUE.index);
//		hlinkStyle.setFont(hlinkFont);
//	
//		int entityCount = 1;
//		entityCount = outputNanomaterialEntities(compBean, wb, headerStyle,
//				hlinkStyle, entityCount, downloadURL);
//	
//		entityCount = outputFunctionalEntities(compBean, wb, headerStyle,
//				hlinkStyle, entityCount, downloadURL);
//	
//		entityCount = outputChemicalEntities(compBean, wb, headerStyle,
//				hlinkStyle, entityCount, downloadURL);
//	
//		outputFilesEntities(compBean, wb, headerStyle, hlinkStyle, entityCount,
//				downloadURL);
//	}
}
