package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for composition forms.
 *
 * @author pansu, cais
 *
 */
public class InitCompositionSetup {

	public static InitCompositionSetup getInstance() {
		return new InitCompositionSetup();
	}

	public void setNanomaterialEntityDropdowns(HttpServletRequest request)
			throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		getEmulsionComposingElementTypes(request);
		InitSetup.getInstance().getDefaultAndOtherTypesByReflection(request,
				"defaultFunctionTypes", "functionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function",
				"gov.nih.nci.cananolab.domain.function.OtherFunction", true);

		InitSetup
				.getInstance()
				.getDefaultAndOtherTypesByReflection(
						request,
						"defaultNanomaterialEntityTypes",
						"nanomaterialEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity",
						"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity",
						true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"biopolymerTypes", "biopolymer", "type", "otherType", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"modalityTypes", "imaging function", "modality",
				"otherModality", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"composingElementTypes", "composing element", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"composingElementUnits", "composing element", "valueUnit",
				"otherValueUnit", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"molecularFormulaTypes", "molecular formula", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"dimensionUnits", "dimension", "unit", "otherUnit", true);
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

		ComposingElementBean theComposingElement = entityBean
				.getTheComposingElement();
		if (entityBean.getDomainEntity() instanceof Emulsion) {
			InitSetup.getInstance().persistLookup(request, "emulsion",
					"composingElementType", "otherComposingElementType",
					entityBean.getTheComposingElement().getDomain().getType());
		} else {
			InitSetup.getInstance().persistLookup(request, "composing element",
					"type", "otherType",
					theComposingElement.getDomain().getType());
		}
		InitSetup.getInstance().persistLookup(request, "composing element",
				"valueUnit", "otherValueUnit",
				theComposingElement.getDomain().getValueUnit());
		InitSetup.getInstance().persistLookup(request, "molecular formula",
				"type", "otherType",
				theComposingElement.getDomain().getMolecularFormulaType());

		InitSetup.getInstance().persistLookup(
				request,
				"imaging function",
				"modality",
				"otherModality",
				theComposingElement.getTheFunction().getImagingFunction()
						.getModality());

		InitSetup.getInstance().persistLookup(request, "file", "type",
				"otherType", entityBean.getTheFile().getDomainFile().getType());

		setNanomaterialEntityDropdowns(request);
	}

	public void setFunctionalizingEntityDropdowns(HttpServletRequest request)
			throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		// reload function types
		InitSetup.getInstance().getDefaultAndOtherTypesByReflection(request,
				"defaultFunctionTypes", "functionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function",
				"gov.nih.nci.cananolab.domain.function.OtherFunction", true);

		InitSetup
				.getInstance()
				.getDefaultAndOtherTypesByReflection(
						request,
						"defaultFunctionalizingEntityTypes",
						"functionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity",
						"gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity",
						true);
		InitSetup.getInstance().getDefaultAndOtherTypesByReflection(request,
				"defaultTargetTypes", "targetTypes",
				"gov.nih.nci.cananolab.domain.function.Target",
				"gov.nih.nci.cananolab.domain.function.OtherTarget", true);

		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"antibodyTypes", "antibody", "type", "otherType", true);
		InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(request, "antibodyIsotypes",
						"antibody", "isotype", "otherIsotype", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"activationMethods", "activation method", "type", "otherType",
				true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"functionalizingEntityUnits", "functionalizing entity",
				"valueUnit", "otherValueUnit", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"modalityTypes", "imaging function", "modality",
				"otherModality", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"molecularFormulaTypes", "molecular formula", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"biopolymerTypes", "biopolymer", "type", "otherType", true);
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
		InitSetup.getInstance().persistLookup(request, "imaging function",
				"modality", "otherModality",
				entityBean.getTheFunction().getImagingFunction().getModality());

		InitSetup.getInstance().persistLookup(request, "file", "type",
				"otherType", entityBean.getTheFile().getDomainFile().getType());

		setFunctionalizingEntityDropdowns(request);
	}

	public void setChemicalAssociationDropdowns(HttpServletRequest request,
			boolean hasFunctionalizingEntity) throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		HttpSession session = request.getSession();
		List<String> compositionTypes = new ArrayList<String>();
		compositionTypes.add("nanomaterial entity");
		if (hasFunctionalizingEntity) {
			compositionTypes.add("functionalizing entity");
		}
		session.setAttribute("associationCompositionTypes", compositionTypes);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"bondTypes", "attachment", "bondType", "otherBondType", true);
		InitSetup
				.getInstance()
				.getDefaultAndOtherTypesByReflection(
						request,
						"defaultChemicalAssociationTypes",
						"chemicalAssociationTypes",
						"gov.nih.nci.cananolab.domain.particle.ChemicalAssociation",
						"gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation",
						true);
	}

	public void persistChemicalAssociationDropdowns(HttpServletRequest request,
			ChemicalAssociationBean assocBean, Boolean hasFunctionalizingEntity)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "attachment",
				"bondType", "otherBondType",
				assocBean.getAttachment().getBondType());

		InitSetup.getInstance().persistLookup(request, "file", "type",
				"otherType", assocBean.getTheFile().getDomainFile().getType());

		setChemicalAssociationDropdowns(request, hasFunctionalizingEntity);
	}

	public void persistCompositionFileDropdowns(HttpServletRequest request,
			FileBean fileBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "file", "type",
				"otherType", fileBean.getDomainFile().getType());
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"fileTypes", "file", "type", "otherType", true);
	}

	public List<String> getEmulsionComposingElementTypes(
			HttpServletRequest request) throws BaseException {
		List<String> allTypes = new ArrayList<String>();
		SortedSet<String> emulsionCETypes = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(request,
						"emulsionComposingElementTypes", "emulsion",
						"composingElementType", "otherComposingElementType",
						true);
		allTypes.addAll(emulsionCETypes);
		SortedSet<String> ceTypes = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(request,
						"composingElementTypes", "composing element", "type",
						"otherType", true);
		allTypes.addAll(ceTypes);
		request.getSession().setAttribute("emulsionComposingElementTypes",
				allTypes);
		return allTypes;
	}

	public String getDetailPage(String entityType, String parentPath)
			throws Exception {
		String page = "/sample/composition/" + parentPath + "/body"
				+ ClassUtils.getShortClassNameFromDisplayName(entityType)
				+ "Info.jsp";
		return page;
	}
}
