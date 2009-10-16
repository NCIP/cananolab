package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.agentmaterial.Antibody;
import gov.nih.nci.cananolab.domain.agentmaterial.SmallMolecule;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.nanomaterial.Biopolymer;
import gov.nih.nci.cananolab.domain.nanomaterial.CarbonNanotube;
import gov.nih.nci.cananolab.domain.nanomaterial.Dendrimer;
import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.domain.nanomaterial.Fullerene;
import gov.nih.nci.cananolab.domain.nanomaterial.Liposome;
import gov.nih.nci.cananolab.domain.nanomaterial.Polymer;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.CompositionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.SampleConstants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving composition.
 *
 * @author pansu, tanq
 *
 */
public class CompositionServiceHelper {

	private static Logger logger = Logger
			.getLogger(CompositionServiceHelper.class);

	private static String fileRoot = PropertyUtils.getProperty(
			Constants.CANANOLAB_PROPERTY, Constants.FILE_REPOSITORY_DIR);

	private AuthorizationService authService;

	public CompositionServiceHelper() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	// for DWR Ajax
	public Function findFunctionById(String funcId, UserBean user)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Function.class).add(
				Property.forName("id").eq(new Long(funcId)));
		crit.setFetchMode("targetCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		Function func = null;
		if (!result.isEmpty()) {
			func = (Function) result.get(0);
			if (authService.checkReadPermission(user, func.getId().toString())) {
				return func;
			} else {
				throw new NoAccessException(
						"User doesn't have access to the function");
			}
		}
		return func;
	}

	// for DWR Ajax
	public ComposingElement findComposingElementById(String ceId, UserBean user)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(
				ComposingElement.class).add(
				Property.forName("id").eq(new Long(ceId)));
		crit.setFetchMode("inherentFunctionCollection", FetchMode.JOIN);
		crit.setFetchMode("inherentFunctionCollection.targetCollection",
				FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List result = appService.query(crit);
		ComposingElement ce = null;
		if (!result.isEmpty()) {
			ce = (ComposingElement) result.get(0);
			if (authService.checkReadPermission(user, ce.getId().toString())) {
				return ce;
			} else {
				throw new NoAccessException(
						"User doesn't have access to the composing element");
			}
		}
		return ce;
	}

	/**
	 * Get PubChem URL in format of
	 * http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?pubChemDS=pubchemId
	 *
	 * @param pubChemDS
	 * @param pubChemId
	 * @return PubChem URL
	 */
	public static String getPubChemURL(String pubChemDS, Long pubChemId) {
		StringBuffer sb = new StringBuffer(SampleConstants.PUBCHEM_URL);

		if (SampleConstants.BIOASSAY.equals(pubChemDS)) {
			sb.append(SampleConstants.BIOASSAY_ID);
		} else if (SampleConstants.COMPOUND.equals(pubChemDS)) {
			sb.append(SampleConstants.COMPOUND_ID);
		} else if (SampleConstants.SUBSTANCE.equals(pubChemDS)) {
			sb.append(SampleConstants.SUBSTANCE_ID);
		}

		sb.append('=').append(pubChemId);

		return sb.toString();
	}

	public List<File> findFilesByCompositionInfoId(String id, String className,
			UserBean user) throws Exception {
		List<File> fileCollection = new ArrayList<File>();
		String fullClassName = null;
		if (ClassUtils.getFullClass(className) != null) {
			fullClassName = ClassUtils.getFullClass(className).getName();
		} else {
			return null;
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select anEntity.fileCollection from " + fullClassName
				+ " anEntity where anEntity.id = " + id;

		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		List filteredResults = new ArrayList(results);
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		for (Object obj : filteredResults) {
			File file = (File) obj;
			if (user == null
					|| authService.checkReadPermission(user, file.getId()
							.toString())) {
				fileCollection.add(file);
			} else {
				logger.debug("User doesn't have access to file of id:"
						+ file.getId());
			}

		}
		return fileCollection;
	}

	/**
	 * Export sample composition summary report as Excel spread sheet.
	 *
	 * @param compBean
	 * @param out
	 * @throws CompositionException
	 */
	public static void exportSummary(CompositionBean compBean,
			String downloadURL, OutputStream out) throws IOException {
		if (out != null) {
			HSSFWorkbook wb = new HSSFWorkbook();
			outputSummarySheet(compBean, downloadURL, wb);
			wb.write(out);
			out.flush();
			out.close();
		}
	}

	/**
	 * Output sample Composition Summary report =>
	 * bodyCompositionSummaryView.jsp
	 *
	 * @param compBean
	 * @param wb
	 */
	private static void outputSummarySheet(CompositionBean compBean,
			String downloadURL, HSSFWorkbook wb) throws IOException {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFCellStyle hlinkStyle = wb.createCellStyle();
		HSSFFont hlinkFont = wb.createFont();
		hlinkFont.setUnderline(HSSFFont.U_SINGLE);
		hlinkFont.setColor(HSSFColor.BLUE.index);
		hlinkStyle.setFont(hlinkFont);

		int entityCount = 1;
		entityCount = outputNanomaterialEntities(compBean, wb, headerStyle,
				hlinkStyle, entityCount, downloadURL);

		entityCount = outputFunctionalEntities(compBean, wb, headerStyle,
				hlinkStyle, entityCount, downloadURL);

		entityCount = outputChemicalEntities(compBean, wb, headerStyle,
				hlinkStyle, entityCount, downloadURL);

		outputFilesEntities(compBean, wb, headerStyle, hlinkStyle, entityCount,
				downloadURL);
	}

	/**
	 * Output NanomaterialEntities data => bodyNanomaterialEntitySummaryView.jsp
	 *
	 * @param compBean
	 * @param wb
	 * @param headerStyle
	 * @param entityCount
	 */
	private static int outputNanomaterialEntities(CompositionBean compBean,
			HSSFWorkbook wb, HSSFCellStyle headerStyle,
			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
		List<NanomaterialEntityBean> nanoList = compBean
				.getNanomaterialEntities();
		if (nanoList != null && !nanoList.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (NanomaterialEntityBean nanoEntity : nanoList) {
				if (!StringUtils.isEmpty(nanoEntity.getType())) {
					int rowIndex = 0;
					sb.setLength(0);
					sb.append(entityCount++).append('.').append(
							nanoEntity.getType());

					// Create one work sheet for each Nanomaterial Entity.
					HSSFSheet sheet = wb.createSheet(sb.toString());
					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

					// 1. Output Header: NanoMaterial at (0, 0), Composition
					// Type at (2, 0).
					rowIndex = outputHeader(
							CompositionBean.NANOMATERIAL_SELECTION, nanoEntity
									.getType(), sheet, headerStyle, rowIndex);

					// 2. Output Composition Description at (4, 0).
					String description = nanoEntity.getEmulsion()
							.getDescription();
					if (!StringUtils.isEmpty(description)) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Description");
						ExportUtils.createCell(row, 1, description);
					}

					// 3. Output Composition Properties: 7x)
					// "nanomaterialEntity/*.jsp"
					if (nanoEntity.isWithProperties()) {
						if (!StringUtils.isEmpty(description)) {
							rowIndex++; // Create one empty line as separator.
						}
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Properties");

						NanomaterialEntity domainEntity = nanoEntity
								.getDomainEntity();
						if (domainEntity instanceof Biopolymer) {
							rowIndex = outputNanoProperties(
									(Biopolymer) domainEntity, sheet,
									headerStyle, rowIndex);
						} else if (domainEntity instanceof Dendrimer) {
							rowIndex = outputNanoProperties(
									(Dendrimer) domainEntity, sheet,
									headerStyle, rowIndex);
						} else if (domainEntity instanceof CarbonNanotube) {
							rowIndex = outputNanoProperties(
									(CarbonNanotube) domainEntity, sheet,
									headerStyle, rowIndex);
						} else if (domainEntity instanceof Liposome) {
							rowIndex = outputNanoProperties(
									(Liposome) domainEntity, sheet,
									headerStyle, rowIndex);
						} else if (domainEntity instanceof Emulsion) {
							rowIndex = outputNanoProperties(
									(Emulsion) domainEntity, sheet,
									headerStyle, rowIndex);
						} else if (domainEntity instanceof Polymer) {
							rowIndex = outputNanoProperties(
									(Polymer) domainEntity, sheet, headerStyle,
									rowIndex);
						} else if (domainEntity instanceof Fullerene) {
							rowIndex = outputNanoProperties(
									(Fullerene) domainEntity, sheet,
									headerStyle, rowIndex);
						}
						rowIndex++; // Create one empty line as separator.
					}

					// 3. Output Composing Elements:
					// bodyComposingElementView.jsp
					List<ComposingElementBean> compElementBeans = nanoEntity
							.getComposingElements();
					if (compElementBeans != null && !compElementBeans.isEmpty()) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Composing Elements");

						row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle, "Type");
						ExportUtils.createCell(row, 1, headerStyle,
								"PubChem ID");
						ExportUtils.createCell(row, 2, headerStyle, "Name");
						ExportUtils.createCell(row, 3, headerStyle, "Amount");
						ExportUtils.createCell(row, 4, headerStyle,
								"Molecular Formula");
						ExportUtils.createCell(row, 5, headerStyle, "Function");
						ExportUtils.createCell(row, 6, headerStyle,
								"Description");
						for (ComposingElementBean compElementBean : compElementBeans) {
							row = sheet.createRow(rowIndex++);
							ComposingElement compElement = compElementBean
									.getDomain();
							ExportUtils.createCell(row, 0, compElement
									.getType());

							if (compElement.getPubChemId() != null) {
								Long pubChemId = compElement.getPubChemId();
								String pubChemDs = compElement
										.getPubChemDataSourceName();
								sb.setLength(0);
								sb.append(pubChemId).append(' ');
								sb.append('(').append(pubChemDs).append(')');
								ExportUtils.createCell(row, 1, hlinkStyle, sb
										.toString(), getPubChemURL(pubChemDs,
										pubChemId));
							}

							ExportUtils.createCell(row, 2, compElement
									.getName());

							sb.setLength(0);
							sb.append(compElement.getValue()).append(' ');
							sb.append(compElement.getValueUnit());
							ExportUtils.createCell(row, 3, sb.toString());

							if (!StringUtils.isEmpty(compElementBean
									.getMolecularFormulaDisplayName())) {
								ExportUtils.createCell(row, 4, compElementBean
										.getMolecularFormulaDisplayName());
							}

							String[] funcNames = compElementBean
									.getFunctionDisplayNames();
							if (funcNames != null && funcNames.length > 0) {
								sb.setLength(0);
								for (String funcName : funcNames) {
									sb.append(',').append(' ').append(funcName);
								}
								ExportUtils.createCell(row, 5, sb.substring(2));
							}

							if (!StringUtils.isEmpty(compElement
									.getDescription())) {
								ExportUtils.createCell(row, 6, compElement
										.getDescription());
							}
						}
						rowIndex++; // Create one empty line as separator.
					}// 3. End of outputting Composing Elements.

					// Output Files: bodyFileView.jsp
					List<FileBean> fileBeans = nanoEntity.getFiles();
					if (fileBeans != null && !fileBeans.isEmpty()) {
						rowIndex++; // Create one empty line as separator.
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle, "Files");
						outputFiles(fileBeans, downloadURL, wb, sheet,
								headerStyle, hlinkStyle, patriarch, rowIndex);
					}
				}
			} // End of iterating nanoList.
		}

		return entityCount;
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
	private static int outputFunctionalEntities(CompositionBean compBean,
			HSSFWorkbook wb, HSSFCellStyle headerStyle,
			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
		List<FunctionalizingEntityBean> nanoList = compBean
				.getFunctionalizingEntities();
		if (nanoList != null && !nanoList.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (FunctionalizingEntityBean nanoEntity : nanoList) {
				if (!StringUtils.isEmpty(nanoEntity.getType())) {
					int rowIndex = 0;
					sb.setLength(0);
					sb.append(entityCount++).append('.').append(
							nanoEntity.getType());

					// Create 1 work sheet for each Functional Entity.
					HSSFSheet sheet = wb.createSheet(sb.toString());
					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

					// 1. Output Composition type at (0, 0).
					rowIndex = outputHeader(
							CompositionBean.FUNCTIONALIZING_SELECTION,
							nanoEntity.getType(), sheet, headerStyle, rowIndex);

					// 2. Output Name.
					if (!StringUtils.isEmpty(nanoEntity.getName())) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle, "Name");
						ExportUtils.createCell(row, 1, nanoEntity.getName());
					}

					// 3. Output PubChem.
					if (nanoEntity.getDomainEntity().getPubChemId() != null) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"PubChem ID");
						Long pubChemId = nanoEntity.getDomainEntity()
								.getPubChemId();
						String pubChemDs = nanoEntity.getDomainEntity()
								.getPubChemDataSourceName();
						sb.setLength(0);
						sb.append(pubChemId).append(' ');
						sb.append('(').append(pubChemDs).append(')');
						ExportUtils.createCell(row, 1, hlinkStyle, sb
								.toString(),
								getPubChemURL(pubChemDs, pubChemId));
					}

					// Output Amount.
					if (nanoEntity.getValue() != null) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle, "Amount");
						sb.setLength(0);
						sb.append(nanoEntity.getValue()).append(' ');
						sb.append(nanoEntity.getValueUnit());
						ExportUtils.createCell(row, 1, sb.toString());
					}

					// 5. Output Molecular Formula.
					if (!StringUtils.isEmpty(nanoEntity
							.getMolecularFormulaDisplayName())) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Molecular Formula");
						ExportUtils.createCell(row, 1, nanoEntity
								.getMolecularFormulaDisplayName());
					}

					// 6. Output Properties: 3x) "functionalizingEntity/*.jsp"
					if (nanoEntity.isWithProperties()) {
						rowIndex++; // Create one empty line as separator.
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Properties");

						FunctionalizingEntity domainEntity = nanoEntity
								.getDomainEntity();
						if (domainEntity instanceof Antibody) {
							rowIndex = outputFuncProperties(
									(Antibody) domainEntity, sheet,
									headerStyle, rowIndex);
						} else if (domainEntity instanceof SmallMolecule) {
							rowIndex = outputFuncProperties(
									(SmallMolecule) domainEntity, sheet,
									headerStyle, rowIndex);
						} else if (domainEntity instanceof gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer) {
							rowIndex = outputFuncProperties(
									(gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer) domainEntity,
									sheet, headerStyle, rowIndex);
						}
						rowIndex++; // Create one empty line as separator.
					}

					// 7. Output Functions: bodyFunctionView.jsp
					List<FunctionBean> functions = nanoEntity.getFunctions();
					if (functions != null && !functions.isEmpty()) {
						if (!nanoEntity.isWithProperties()) {
							rowIndex++; // Create one empty line as separator.
						}
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Function(s)");

						// 6.1. output Functions table header.
						int colIndex = 0;
						row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, colIndex++, headerStyle,
								"Type");
						if (nanoEntity.isWithImagingFunction()) {
							ExportUtils.createCell(row, colIndex++,
									headerStyle, "Image Modality");
						}
						if (nanoEntity.isWithTargetingFunction()) {
							ExportUtils.createCell(row, colIndex++,
									headerStyle, "Targets");
						}
						ExportUtils.createCell(row, colIndex, headerStyle,
								"Description");

						// 6.2. output Functions table data.
						for (FunctionBean function : functions) {
							colIndex = 0;
							row = sheet.createRow(rowIndex++);

							ExportUtils.createCell(row, colIndex++, function
									.getType());
							if (nanoEntity.isWithImagingFunction()) {
								if (!StringUtils.isEmpty(function
										.getImagingFunction().getModality())) {
									ExportUtils.createCell(row, colIndex++,
											function.getImagingFunction()
													.getModality());
								}
							}
							if (nanoEntity.isWithTargetingFunction()) {
								String[] displayNames = function
										.getTargetDisplayNames();
								if (displayNames != null
										&& displayNames.length > 0) {
									sb.setLength(0);
									for (String name : displayNames) {
										sb.append(',').append(' ').append(name);
									}
									ExportUtils.createCell(row, colIndex++, sb
											.substring(2));
								}
							}
							ExportUtils.createCell(row, colIndex, function
									.getDescription());
						}
						rowIndex++; // Create one empty line as separator.
					}// 7. End of Output Functions: bodyFunctionView.jsp

					// 8. Output Activation Method.
					if (!StringUtils.isEmpty(nanoEntity
							.getActivationMethodDisplayName())) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Activation Method");
						ExportUtils.createCell(row, 1, nanoEntity
								.getActivationMethodDisplayName());
					}

					// 9. Output Description.
					if (!StringUtils.isEmpty(nanoEntity.getDescription())) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Description");
						ExportUtils.createCell(row, 1, nanoEntity
								.getDescription());
					}

					// 10. Output Files: bodyFileView.jsp
					List<FileBean> fileBeans = nanoEntity.getFiles();
					if (fileBeans != null && !fileBeans.isEmpty()) {
						rowIndex++; // Create one empty line as separator.
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle, "Files");
						outputFiles(fileBeans, downloadURL, wb, sheet,
								headerStyle, hlinkStyle, patriarch, rowIndex);
					}
				}
			} // End of iterating nanoList.
		}
		return entityCount;
	}

	/**
	 * Outputting ChemicalEntities data =>
	 * bodyChemicalAssociationSummaryView.jsp
	 *
	 * @param compBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputChemicalEntities(CompositionBean compBean,
			HSSFWorkbook wb, HSSFCellStyle headerStyle,
			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
		List<ChemicalAssociationBean> nanoList = compBean
				.getChemicalAssociations();
		if (nanoList != null && !nanoList.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ChemicalAssociationBean nanoEntity : nanoList) {
				if (!StringUtils.isEmpty(nanoEntity.getType())) {
					int rowIndex = 0;
					sb.setLength(0);
					sb.append(entityCount++).append('.').append(
							nanoEntity.getType());

					// Create one work sheet for each Composition.
					HSSFSheet sheet = wb.createSheet(sb.toString());
					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

					// 1. Output Composition type at (0, 0).
					rowIndex = outputHeader(CompositionBean.CHEMICAL_SELECTION,
							nanoEntity.getType(), sheet, headerStyle, rowIndex);

					// 2. Output Bond Type.
					if (nanoEntity.getAttachment().getId() != null
							&& !StringUtils.isEmpty(nanoEntity.getAttachment()
									.getBondType())) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils
								.createCell(row, 0, headerStyle, "Bond Type");
						ExportUtils.createCell(row, 1, nanoEntity
								.getAttachment().getBondType());
					}

					// 3. Output Description.
					if (!StringUtils.isEmpty(nanoEntity.getDescription())) {
						HSSFRow row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle,
								"Description");
						ExportUtils.createCell(row, 1, nanoEntity
								.getDescription());
					}

					// Output Associated Elements.
					rowIndex++; // Create an empty line as seperator.
					HSSFRow row = sheet.createRow(rowIndex++);
					ExportUtils.createCell(row, 0, headerStyle,
							"Associated Elements");

					// 4a. Output Associated Element A.
					row = sheet.createRow(rowIndex++);
					sb.setLength(0);
					sb.append(nanoEntity.getAssociatedElementA()
							.getCompositionType());
					sb.append(' ');
					sb.append(nanoEntity.getAssociatedElementA()
							.getEntityDisplayName());
					Long compElementId = nanoEntity.getAssociatedElementA()
							.getComposingElement().getId();
					if (compElementId != null) {
						sb.append(" composing element of type ");
						sb.append(nanoEntity.getAssociatedElementA()
								.getComposingElement().getType());
						sb.append(" (name: ");
						sb.append(nanoEntity.getAssociatedElementA()
								.getComposingElement().getName());
						sb.append(')');
					} else {
						sb.append(" (name: ");
						sb.append(nanoEntity.getAssociatedElementA()
								.getDomainElement().getName());
						sb.append(')');
					}
					ExportUtils.createCell(row, 0, sb.toString());

					ExportUtils.createCell(row, 1, "associated with");

					// 4b. Output Associated Element B.
					sb.setLength(0);
					sb.append(nanoEntity.getAssociatedElementB()
							.getCompositionType());
					sb.append(' ');
					sb.append(nanoEntity.getAssociatedElementB()
							.getEntityDisplayName());
					compElementId = nanoEntity.getAssociatedElementB()
							.getComposingElement().getId();
					if (compElementId != null) {
						sb.append(" composing element of type ");
						sb.append(nanoEntity.getAssociatedElementB()
								.getComposingElement().getType());
						sb.append(" (name: ");
						sb.append(nanoEntity.getAssociatedElementB()
								.getComposingElement().getName());
						sb.append(')');
					} else {
						sb.append(" (name: ");
						sb.append(nanoEntity.getAssociatedElementB()
								.getDomainElement().getName());
						sb.append(')');
					}
					ExportUtils.createCell(row, 2, sb.toString());
					rowIndex++; // Create an empty line as seperator.

					// 5. Output Files: bodyFileView.jsp
					List<FileBean> fileBeans = nanoEntity.getFiles();
					if (fileBeans != null && !fileBeans.isEmpty()) {
						rowIndex++; // Create one empty line as separator.
						row = sheet.createRow(rowIndex++);
						ExportUtils.createCell(row, 0, headerStyle, "Files");
						outputFiles(fileBeans, downloadURL, wb, sheet,
								headerStyle, hlinkStyle, patriarch, rowIndex);
					}
				}
			}
		}
		return entityCount;
	}

	/**
	 * Output Composition Files data => bodyCompositionFileSummaryView.jsp
	 *
	 * @param compBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputFilesEntities(CompositionBean compBean,
			HSSFWorkbook wb, HSSFCellStyle headerStyle,
			HSSFCellStyle hlinkStyle, int entityCount, String downloadURL) {
		List<FileBean> nanoList = compBean.getFiles();
		if (nanoList != null && !nanoList.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (FileBean nanoEntity : nanoList) {
				if (!StringUtils.isEmpty(nanoEntity.getDomainFile().getType())) {
					int rowIndex = 0;
					sb.setLength(0);
					sb.append(entityCount++).append('.').append(
							nanoEntity.getDomainFile().getType());

					// Create one work sheet for each Composition File.
					HSSFSheet sheet = wb.createSheet(sb.toString());
					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

					// 1. Output Composition type at (0, 0).
					HSSFRow row = sheet.createRow(rowIndex++);
					ExportUtils.createCell(row, 0, headerStyle,
							CompositionBean.FILE_SELECTION);
					rowIndex++; // Create one empty line as separator.

					// 2. Output File info, one File per sheet.
					outputFile(nanoEntity, downloadURL, wb, sheet, headerStyle,
							hlinkStyle, patriarch, rowIndex);
				}
			}
		}
		return entityCount;
	}

	/**
	 * Output header for work sheet.
	 *
	 * @param compType
	 * @param entityType
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 */
	private static int outputHeader(String compType, String entityType,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {
		// 1. Output Composition type at (0, 0).
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, compType);
		rowIndex++; // Create one empty line as separator.

		// 2. Output Entity Type at (2, 0).
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, entityType);
		rowIndex++; // Create one empty line as separator.

		return rowIndex;
	}

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
	 * Output Biopolymer Info for FunctionalizingEntityBean.
	 *
	 * @param entityBean
	 * @param sheet
	 * @param headerStyle
	 * @param rowIndex
	 * @return
	 */
	private static int outputFuncProperties(
			gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer entityBean,
			HSSFSheet sheet, HSSFCellStyle headerStyle, int rowIndex) {

		// 1. Output table header.
		HSSFRow row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, headerStyle, "Type");
		ExportUtils.createCell(row, 1, headerStyle, "Isotype");

		// 2. Output table data.
		row = sheet.createRow(rowIndex++);
		ExportUtils.createCell(row, 0, entityBean.getType());
		ExportUtils.createCell(row, 1, entityBean.getSequence());

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

	public AuthorizationService getAuthService() {
		return authService;
	}

	public void assignVisibility(NanomaterialEntity entity,
			String[] visibleGroups, String owningGroup) throws Exception {
		if (entity != null) {
			authService.assignVisibility(entity.getId().toString(),
					visibleGroups, owningGroup);
			// nanomaterialEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = entity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						authService.assignVisibility(composingElement.getId()
								.toString(), visibleGroups, owningGroup);
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								authService
										.assignVisibility(function.getId()
												.toString(), visibleGroups,
												owningGroup);
							}
						}
					}
				}
			}
		}
	}

	public void assignVisibility(FunctionalizingEntity functionalizingEntity,
			String[] visibleGroups, String owningGroup) throws Exception {
		if (functionalizingEntity != null) {
			authService.assignVisibility(functionalizingEntity.getId()
					.toString(), visibleGroups, owningGroup);
			// activation method
			if (functionalizingEntity.getActivationMethod() != null) {
				authService.assignVisibility(functionalizingEntity
						.getActivationMethod().getId().toString(),
						visibleGroups, owningGroup);
			}
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						authService.assignVisibility(function.getId()
								.toString(), visibleGroups, owningGroup);
						if (function instanceof TargetingFunction) {
							for (Target target : ((TargetingFunction) function)
									.getTargetCollection()) {
								authService
										.assignVisibility(target.getId()
												.toString(), visibleGroups,
												owningGroup);
							}
						}
					}
				}
			}
		}
	}

	public void assignVisibility(SampleComposition comp,
			String[] visibleGroups, String owningGroup) throws Exception {
		authService.assignVisibility(comp.getId().toString(), visibleGroups,
				owningGroup);
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

	public void assignVisibility(ChemicalAssociation chemicalAssociation,
			String[] visibleGroups, String owningGroup) throws Exception {
		if (chemicalAssociation != null) {
			authService.assignVisibility(
					chemicalAssociation.getId().toString(), visibleGroups,
					owningGroup);
			// chemicalAssociation.associatedElementA
			if (chemicalAssociation.getAssociatedElementA() != null) {
				authService.assignVisibility(chemicalAssociation
						.getAssociatedElementA().getId().toString(),
						visibleGroups, owningGroup);
			}
			// chemicalAssociation.associatedElementB
			if (chemicalAssociation.getAssociatedElementB() != null) {
				authService.assignVisibility(chemicalAssociation
						.getAssociatedElementB().getId().toString(),
						visibleGroups, owningGroup);
			}
		}
	}

	public void removeVisibility(SampleComposition comp) throws Exception {
		authService.removeCSMEntry(comp.getId().toString());
		for (NanomaterialEntity entity : comp.getNanomaterialEntityCollection()) {
			removeVisibility(entity);
		}
		for (FunctionalizingEntity entity : comp
				.getFunctionalizingEntityCollection()) {
			removeVisibility(entity);
		}
		for (ChemicalAssociation assoc : comp
				.getChemicalAssociationCollection()) {
			removeVisibility(assoc);
		}
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				authService.removeCSMEntry(file.getId().toString());
			}
		}
	}

	public void removeVisibility(ChemicalAssociation chemicalAssociation)
			throws Exception {
		if (chemicalAssociation != null) {
			authService.removeCSMEntry(chemicalAssociation.getId().toString());
			// chemicalAssociation.associatedElementA
			if (chemicalAssociation.getAssociatedElementA() != null) {
				authService.removeCSMEntry(chemicalAssociation
						.getAssociatedElementA().getId().toString());
			}
			// chemicalAssociation.associatedElementB
			if (chemicalAssociation.getAssociatedElementB() != null) {
				authService.removeCSMEntry(chemicalAssociation
						.getAssociatedElementB().getId().toString());
			}
			if (chemicalAssociation.getFileCollection() != null) {
				for (File file : chemicalAssociation.getFileCollection()) {
					authService.removeCSMEntry(file.getId().toString());
				}
			}
		}
	}

	public void removeVisibility(FunctionalizingEntity functionalizingEntity)
			throws Exception {
		if (functionalizingEntity != null) {
			authService
					.removeCSMEntry(functionalizingEntity.getId().toString());
			if (functionalizingEntity.getActivationMethod() != null) {
				authService.removeCSMEntry(functionalizingEntity
						.getActivationMethod().getId().toString());
			}
			// functionalizingEntityCollection.functionCollection
			Collection<Function> functionCollection = functionalizingEntity
					.getFunctionCollection();
			if (functionCollection != null) {
				for (Function function : functionCollection) {
					if (function != null) {
						authService.removeCSMEntry(function.getId().toString());
						if (function instanceof TargetingFunction) {
							for (Target target : ((TargetingFunction) function)
									.getTargetCollection()) {
								authService.removeCSMEntry(target.getId()
										.toString());
							}
						}
					}
				}
			}
			if (functionalizingEntity.getFileCollection() != null) {
				for (File file : functionalizingEntity.getFileCollection()) {
					authService.removeCSMEntry(file.getId().toString());
				}
			}
		}
	}

	public void removeVisibility(NanomaterialEntity entity) throws Exception {
		if (entity != null) {
			authService.removeCSMEntry(entity.getId().toString());
			// nanomaterialEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = entity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						authService.removeCSMEntry(composingElement.getId()
								.toString());
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								authService.removeCSMEntry(function.getId()
										.toString());
							}
						}
					}
				}
			}
			if (entity.getFileCollection() != null) {
				for (File file : entity.getFileCollection()) {
					authService.removeCSMEntry(file.getId().toString());
				}
			}
		}
	}
}
