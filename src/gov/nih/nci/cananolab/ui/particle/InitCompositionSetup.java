package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.particle.CompositionService;
import gov.nih.nci.cananolab.service.particle.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for composition forms.
 *
 * @author pansu, cais
 *
 */
public class InitCompositionSetup {

	private CompositionService compService = new CompositionServiceLocalImpl();

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
				"biopolymerTypes", "Biopolymer", "type", "otherType", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"modalityTypes", "ImagingFunction", "modality",
				"otherModality", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"composingElementTypes", "ComposingElement", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"composingElementUnits", "ComposingElement", "valueUnit",
				"otherValueUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"ceMolecularFormulaTypes", "ComposingElement",
				"molecularFormulaType", "otherMolecularFormulaType", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"carbonNanotubeDiameterUnit", "CarbonNanotube", "diameterUnit",
				"otherDiameterUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"carbonNanotubeAverageLengthUnit", "CarbonNanotube",
				"averageLengthUnit", "otherAverageLengthUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fullereneAverageDiameterUnit", "Fullerene",
				"averageDiameterUnit", "otherAverageDiameterUnit", true);
		ServletContext appContext = request.getSession().getServletContext();
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"wallTypes", "CarbonNanotube", "wallType");
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void persistNanomaterialEntityDropdowns(HttpServletRequest request,
			NanomaterialEntityBean entityBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "Biopolymer", "type",
				"otherType", entityBean.getBiopolymer().getType());
		InitSetup.getInstance().persistLookup(request, "Fullerene",
				"averageDiameterUnit", "otherAverageDiameterUnit",
				entityBean.getFullerene().getAverageDiameterUnit());
		InitSetup.getInstance().persistLookup(request, "CarbonNanotube",
				"diameterUnit", "otherDiameterUnit",
				entityBean.getCarbonNanotube().getDiameterUnit());
		InitSetup.getInstance().persistLookup(request, "CarbonNanotube",
				"averageLengthUnit", "otherAverageLengthUnit",
				entityBean.getCarbonNanotube().getAverageLengthUnit());
		for (ComposingElementBean elementBean : entityBean
				.getComposingElements()) {
			if (entityBean.getDomainEntity() instanceof Emulsion) {
				InitSetup.getInstance().persistLookup(request, "Emulsion",
						"composingElementType", "otherComposingElementType",
						elementBean.getDomainComposingElement().getType());
			} else {
				InitSetup.getInstance().persistLookup(request,
						"ComposingElement", "type", "otherType",
						elementBean.getDomainComposingElement().getType());
			}
			InitSetup.getInstance().persistLookup(request, "ComposingElement",
					"valueUnit", "otherValueUnit",
					elementBean.getDomainComposingElement().getValueUnit());
			InitSetup.getInstance().persistLookup(
					request,
					"ComposingElement",
					"molecularFormulaType",
					"otherMolecularFormulaType",
					elementBean.getDomainComposingElement()
							.getMolecularFormulaType());
			for (FunctionBean functionBean : elementBean.getInherentFunctions()) {
				InitSetup.getInstance().persistLookup(request,
						"ImagingFunction", "modality", "otherModality",
						functionBean.getImagingFunction().getModality());
			}
		}
		for (FileBean fileBean : entityBean.getFiles()) {
			InitSetup.getInstance().persistLookup(request, "File", "type",
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
				"gov.nih.nci.cananolab.domain.Function.Target",
				"gov.nih.nci.cananolab.domain.Function.OtherTarget", true);

		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"antibodyTypes", "Antibody", "type", "otherType", true);
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "antibodyIsotypes",
						"Antibody", "isotype", "otherIsotype", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"activationMethods", "ActivationMethod", "type", "otherType",
				true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"functionalizingEntityUnits", "FunctionalizingEntity",
				"valueUnit", "otherValueUnit", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"modalityTypes", "ImagingFunction", "modality",
				"otherModality", true);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"feMolecularFormulaTypes", "FunctionalizingEntity",
				"molecularFormulaType", "otherMolecularFormulaType", true);

		ServletContext appContext = request.getSession().getServletContext();
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"antigenSpecies", "Antigen", "species");
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"antibodySpecies", "Antibody", "species");
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"biopolymerTypes", "Biopolymer", "type", "otherType", true);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void persistFunctionalizingEntityDropdowns(
			HttpServletRequest request, FunctionalizingEntityBean entityBean)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "Antibody", "type",
				"otherType", entityBean.getAntibody().getType());
		InitSetup.getInstance().persistLookup(request, "Antibody", "isotype",
				"otherIsoType", entityBean.getAntibody().getIsotype());
		InitSetup.getInstance().persistLookup(request, "Biopolymer", "type",
				"otherType", entityBean.getBiopolymer().getType());
		InitSetup.getInstance().persistLookup(request, "FunctionalizingEntity",
				"valueUnit", "otherValueUnit", entityBean.getValueUnit());
		InitSetup.getInstance().persistLookup(request, "FunctionalizingEntity",
				"molecularFormulaType", "otherMolecularFormulaType",
				entityBean.getMolecularFormulaType());
		InitSetup
				.getInstance()
				.persistLookup(request, "ActivationMethod", "type",
						"otherType", entityBean.getActivationMethod().getType());

		for (FunctionBean functionBean : entityBean.getFunctions()) {
			InitSetup.getInstance().persistLookup(request, "ImagingFunction",
					"modality", "otherModality",
					functionBean.getImagingFunction().getModality());
		}
		for (FileBean fileBean : entityBean.getFiles()) {
			InitSetup.getInstance().persistLookup(request, "File", "type",
					"otherType", fileBean.getDomainFile().getType());
		}
		setFunctionalizingEntityDropdowns(request);
	}

	public void setChemicalAssociationDropdowns(HttpServletRequest request,
			boolean hasFunctionalizingEntity) throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		List<String> compositionTypes = new ArrayList<String>();
		compositionTypes.add("Nanomaterial Entity");
		if (hasFunctionalizingEntity) {
			compositionTypes.add("Functionalizing Entity");
		}
		appContext
				.setAttribute("associationCompositionTypes", compositionTypes);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"bondTypes", "Attachment", "bondType", "otherBondType", true);
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
		InitSetup.getInstance().persistLookup(request, "Attachment",
				"bondType", "otherBondType",
				assocBean.getAttachment().getBondType());
		if (assocBean.getFiles() != null) {
			for (FileBean fileBean : assocBean.getFiles()) {
				InitSetup.getInstance().persistLookup(request, "File", "type",
						"otherType", fileBean.getDomainFile().getType());
			}
		}
		setChemicalAssociationDropdowns(request, hasFunctionalizingEntity);
	}

	public void persistCompositionFileDropdowns(HttpServletRequest request,
			FileBean fileBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "File", "type",
				"otherType", fileBean.getDomainFile().getType());
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "File", "type", "otherType", true);
	}

	public SortedSet<String> getEmulsionComposingElementTypes(
			HttpServletRequest request) throws BaseException {
		SortedSet<String> emulsionCETypes = LookupService
				.getDefaultAndOtherLookupTypes("Emulsion",
						"composingElementType", "otherComposingElementType");
		SortedSet<String> ceTypes = LookupService
				.getDefaultAndOtherLookupTypes("ComposingElement", "type",
						"otherType");
		emulsionCETypes.addAll(ceTypes);
		request.getSession().setAttribute("emulsionComposingElementTypes",
				emulsionCETypes);
		return emulsionCETypes;
	}
}
