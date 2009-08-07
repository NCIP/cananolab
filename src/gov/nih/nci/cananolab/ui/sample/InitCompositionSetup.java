package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for composition forms.
 * 
 * @author pansu, cais
 * 
 */
public class InitCompositionSetup {

	// private CompositionService compService = new
	// CompositionServiceLocalImpl();

	public static InitCompositionSetup getInstance() {
		return new InitCompositionSetup();
	}

	public void setNanomaterialEntityDropdowns(HttpServletRequest request)
			throws Exception {
		getEmulsionComposingElementTypes(request);
		InitSetup.getInstance().getReflectionDefaultAndOtherLookupTypes(
				request, "defaultFunctionTypes", "functionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function",
				"gov.nih.nci.cananolab.domain.function.OtherFunction", true);

		InitSetup
				.getInstance()
				.getReflectionDefaultAndOtherLookupTypes(
						request,
						"defaultNanomaterialEntityTypes",
						"nanomaterialEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity",
						"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity",
						true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"biopolymerTypes", "biopolymer", "type", "otherType", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"modalityTypes", "imaging function", "modality",
				"otherModality", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"composingElementTypes", "composing element", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"composingElementUnits", "composing element", "valueUnit",
				"otherValueUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"molecularFormulaTypes", "molecular formula", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"dimensionUnits", "dimension", "unit", "otherUnit", true);
		ServletContext appContext = request.getSession().getServletContext();
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"wallTypes", "carbon nanotube", "wallType");
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void persistNanomaterialEntityDropdowns(HttpServletRequest request,
			NanomaterialEntityBean entityBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "biopolymer", "type",
				"otherType", entityBean.getBiopolymer().getType());
		InitSetup.getInstance().persistLookup(request, "fullerene",
				"averageDiameterUnit", "otherAverageDiameterUnit",
				entityBean.getFullerene().getAverageDiameterUnit());
		InitSetup.getInstance().persistLookup(request, "carbon nanotube",
				"diameterUnit", "otherDiameterUnit",
				entityBean.getCarbonNanotube().getDiameterUnit());
		InitSetup.getInstance().persistLookup(request, "carbon nanotube",
				"averageLengthUnit", "otherAverageLengthUnit",
				entityBean.getCarbonNanotube().getAverageLengthUnit());
		for (ComposingElementBean elementBean : entityBean
				.getComposingElements()) {
			if (entityBean.getDomainEntity() instanceof Emulsion) {
				InitSetup.getInstance().persistLookup(request, "emulsion",
						"composingElementType", "otherComposingElementType",
						elementBean.getDomain().getType());
			} else {
				InitSetup.getInstance().persistLookup(request,
						"composing element", "type", "otherType",
						elementBean.getDomain().getType());
			}
			InitSetup.getInstance().persistLookup(request, "composing element",
					"valueUnit", "otherValueUnit",
					elementBean.getDomain().getValueUnit());
			InitSetup.getInstance().persistLookup(request, "molecular formula",
					"type", "otherType",
					elementBean.getDomain().getMolecularFormulaType());
			for (FunctionBean functionBean : elementBean.getInherentFunctions()) {
				InitSetup.getInstance().persistLookup(request,
						"imaging function", "modality", "otherModality",
						functionBean.getImagingFunction().getModality());
			}
		}
		for (FileBean fileBean : entityBean.getFiles()) {
			InitSetup.getInstance().persistLookup(request, "file", "type",
					"otherType", fileBean.getDomainFile().getType());
		}

		setNanomaterialEntityDropdowns(request);
	}

	public void setFunctionalizingEntityDropdowns(HttpServletRequest request)
			throws Exception {
		// reload function types
		InitSetup.getInstance().getReflectionDefaultAndOtherLookupTypes(
				request, "defaultFunctionTypes", "functionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function",
				"gov.nih.nci.cananolab.domain.function.OtherFunction", true);

		InitSetup
				.getInstance()
				.getReflectionDefaultAndOtherLookupTypes(
						request,
						"defaultFunctionalizingEntityTypes",
						"functionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity",
						"gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity",
						true);
		InitSetup.getInstance().getReflectionDefaultAndOtherLookupTypes(
				request, "defaultTargetTypes", "targetTypes",
				"gov.nih.nci.cananolab.domain.function.Target",
				"gov.nih.nci.cananolab.domain.function.OtherTarget", true);

		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"antibodyTypes", "antibody", "type", "otherType", true);
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "antibodyIsotypes",
						"antibody", "isotype", "otherIsotype", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"activationMethods", "activation method", "type", "otherType",
				true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"functionalizingEntityUnits", "functionalizing entity",
				"valueUnit", "otherValueUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"modalityTypes", "imaging function", "modality",
				"otherModality", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"molecularFormulaTypes", "molecular formula", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"biopolymerTypes", "biopolymer", "type", "otherType", true);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void persistFunctionalizingEntityDropdowns(
			HttpServletRequest request, FunctionalizingEntityBean entityBean)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "antibody", "type",
				"otherType", entityBean.getAntibody().getType());
		InitSetup.getInstance().persistLookup(request, "antibody", "isotype",
				"otherIsoType", entityBean.getAntibody().getIsotype());
		InitSetup.getInstance().persistLookup(request, "biopolymer", "type",
				"otherType", entityBean.getBiopolymer().getType());
		InitSetup.getInstance().persistLookup(request,
				"functionalizing entity", "valueUnit", "otherValueUnit",
				entityBean.getValueUnit());
		InitSetup.getInstance().persistLookup(request, "molecular formula",
				"type", "otherType", entityBean.getMolecularFormulaType());
		InitSetup
				.getInstance()
				.persistLookup(request, "activation method", "type",
						"otherType", entityBean.getActivationMethod().getType());

		for (FunctionBean functionBean : entityBean.getFunctions()) {
			InitSetup.getInstance().persistLookup(request, "imaging function",
					"modality", "otherModality",
					functionBean.getImagingFunction().getModality());
		}
		for (FileBean fileBean : entityBean.getFiles()) {
			InitSetup.getInstance().persistLookup(request, "file", "type",
					"otherType", fileBean.getDomainFile().getType());
		}
		setFunctionalizingEntityDropdowns(request);
	}

	public void setChemicalAssociationDropdowns(HttpServletRequest request,
			boolean hasFunctionalizingEntity) throws Exception {
		HttpSession session = request.getSession();
		List<String> compositionTypes = new ArrayList<String>();
		compositionTypes.add("nanomaterial entity");
		if (hasFunctionalizingEntity) {
			compositionTypes.add("functionalizing entity");
		}
		session
				.setAttribute("associationCompositionTypes", compositionTypes);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"bondTypes", "attachment", "bondType", "otherBondType", true);
		InitSetup
				.getInstance()
				.getReflectionDefaultAndOtherLookupTypes(
						request,
						"defaultChemicalAssociationTypes",
						"chemicalAssociationTypes",
						"gov.nih.nci.cananolab.domain.particle.ChemicalAssociation",
						"gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation",
						true);

		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void persistChemicalAssociationDropdowns(HttpServletRequest request,
			ChemicalAssociationBean assocBean, Boolean hasFunctionalizingEntity)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "attachment",
				"bondType", "otherBondType",
				assocBean.getAttachment().getBondType());
		if (assocBean.getFiles() != null) {
			for (FileBean fileBean : assocBean.getFiles()) {
				InitSetup.getInstance().persistLookup(request, "file", "type",
						"otherType", fileBean.getDomainFile().getType());
			}
		}
		setChemicalAssociationDropdowns(request, hasFunctionalizingEntity);
	}

	public void persistCompositionFileDropdowns(HttpServletRequest request,
			FileBean fileBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "file", "type",
				"otherType", fileBean.getDomainFile().getType());
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "file", "type", "otherType", true);
	}

	public SortedSet<String> getEmulsionComposingElementTypes(
			HttpServletRequest request) throws BaseException {
		SortedSet<String> emulsionCETypes = LookupService
				.getDefaultAndOtherLookupTypes("emulsion",
						"composingElementType", "otherComposingElementType");
		SortedSet<String> ceTypes = LookupService
				.getDefaultAndOtherLookupTypes("composing element", "type",
						"otherType");
		emulsionCETypes.addAll(ceTypes);
		request.getSession().setAttribute("emulsionComposingElementTypes",
				emulsionCETypes);
		return emulsionCETypes;
	}

	public String getDetailPage(String entityType, String parentPath)
			throws Exception {
		String page = "/sample/composition/" + parentPath + "/body"
				+ ClassUtils.getShortClassNameFromDisplayName(entityType)
				+ "Info.jsp";
		return page;
	}
}
