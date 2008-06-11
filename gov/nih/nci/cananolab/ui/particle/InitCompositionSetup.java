package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Emulsion;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCompositionServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for composition forms.
 * 
 * @author pansu, cais
 * 
 */
public class InitCompositionSetup {

	private NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();

	public static InitCompositionSetup getInstance() {
		return new InitCompositionSetup();
	}

	public void setNanoparticleEntityDropdowns(HttpServletRequest request)
			throws Exception {
		getNanoparticleEntityTypes(request);
		getEmulsionComposingElementTypes(request);
		getFunctionTypes(request);
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

	public void persistNanoparticleEntityDropdowns(HttpServletRequest request,
			NanoparticleEntityBean entityBean) throws Exception {
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
		for (LabFileBean fileBean : entityBean.getFiles()) {
			InitSetup.getInstance().persistLookup(request, "LabFile", "type",
					"otherType", fileBean.getDomainFile().getType());
		}
		
		setNanoparticleEntityDropdowns(request);
	}

	public void setFunctionalizingEntityDropdowns(HttpServletRequest request)
			throws Exception {
		// reload function types
		getFunctionTypes(request);
		getFunctionalizingEntityTypes(request);
		getTargetTypes(request);
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
		for (LabFileBean fileBean : entityBean.getFiles()) {
			InitSetup.getInstance().persistLookup(request, "LabFile", "type",
					"otherType", fileBean.getDomainFile().getType());
		}
		setFunctionalizingEntityDropdowns(request);
	}

	public void setChemicalAssociationDropdowns(HttpServletRequest request,
			boolean hasFunctionalizingEntity) throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		List<String> compositionTypes = new ArrayList<String>();
		compositionTypes.add("Nanoparticle Entity");
		if (hasFunctionalizingEntity) {
			compositionTypes.add("Functionalizing Entity");
		}
		appContext
				.setAttribute("associationCompositionTypes", compositionTypes);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"bondTypes", "Attachment", "bondType", "otherBondType", true);
		InitCompositionSetup.getInstance().getChemicalAssociationTypes(request);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void persistChemicalAssociationDropdowns(HttpServletRequest request,
			ChemicalAssociationBean assocBean, Boolean hasFunctionalizingEntity)
			throws Exception {
		InitSetup.getInstance().persistLookup(request, "Attachment",
				"bondType", "otherBondType",
				assocBean.getAttachment().getBondType());
		if (assocBean.getFiles() != null) {
			for (LabFileBean fileBean : assocBean.getFiles()) {
				InitSetup.getInstance()
						.persistLookup(request, "LabFile", "type", "otherType",
								fileBean.getDomainFile().getType());
			}
		}
		setChemicalAssociationDropdowns(request, hasFunctionalizingEntity);
	}

	public void persistCompositionFileDropdowns(HttpServletRequest request,
			LabFileBean fileBean) throws Exception {
		InitSetup.getInstance().persistLookup(request, "LabFile", "type",
				"otherType", fileBean.getDomainFile().getType());
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "LabFile", "type", "otherType", true);
	}

	public SortedSet<String> getFunctionTypes(HttpServletRequest request)
			throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultFunctionTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		SortedSet<String> otherTypes = compService.getAllOtherFunctionTypes();
		if (otherTypes != null)
			types.addAll(otherTypes);
		request.getSession().setAttribute("functionTypes", types);
		return types;
	}

	public SortedSet<String> getDefaultFunctionTypes(HttpServletRequest request)
			throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultFunctionTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
		request.getSession().setAttribute("functionTypes", defaultTypes);
		return defaultTypes;
	}

	public SortedSet<String> getNanoparticleEntityTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultNanoparticleEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		SortedSet<String> otherTypes = compService
				.getAllOtherNanoparticleEntityTypes();
		if (otherTypes != null)
			types.addAll(otherTypes);
		request.getSession().setAttribute("nanoparticleEntityTypes", types);
		return types;
	}

	public SortedSet<String> getDefaultNanoparticleEntityTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultNanoparticleEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
		request.getSession().setAttribute("nanoparticleEntityTypes",
				defaultTypes);
		return defaultTypes;
	}

	public SortedSet<String> getFunctionalizingEntityTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultFunctionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		SortedSet<String> otherTypes = compService
				.getAllOtherFunctionalizingEntityTypes();
		if (otherTypes != null)
			types.addAll(otherTypes);
		request.getSession().setAttribute("functionalizingEntityTypes", types);
		return types;
	}

	public SortedSet<String> getDefaultFunctionalizingEntityTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultFunctionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
		request.getSession().setAttribute("functionalizingEntityTypes",
				defaultTypes);
		return defaultTypes;
	}

	public SortedSet<String> getChemicalAssociationTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultAssociationTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation");
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		SortedSet<String> otherTypes = compService
				.getAllOtherChemicalAssociationTypes();
		if (otherTypes != null) {
			types.addAll(otherTypes);
		}
		request.getSession().setAttribute("chemicalAssociationTypes", types);
		return types;
	}

	public SortedSet<String> getEmulsionComposingElementTypes(
			HttpServletRequest request) throws CaNanoLabException {
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

	public SortedSet<String> getTargetTypes(HttpServletRequest request)
			throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultTargetTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.Target");
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		SortedSet<String> otherTypes = compService.getAllOtherFunctionTypes();
		if (otherTypes != null)
			types.addAll(otherTypes);
		request.getSession().setAttribute("targetTypes", types);
		return types;
	}

}
