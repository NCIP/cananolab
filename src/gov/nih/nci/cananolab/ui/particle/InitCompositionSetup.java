package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

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

	private NanoparticleCompositionService compService = new NanoparticleCompositionService();

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
		ServletContext appContext = request.getSession().getServletContext();
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"wallTypes", "CarbonNanotube", "wallType");
	}

	public void setFunctionalizingEntityDropdowns(HttpServletRequest request)
			throws Exception {
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
		ServletContext appContext = request.getSession().getServletContext();
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"antigenSpecies", "Antigen", "species");
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"antibodySpecies", "Antibody", "species");
	}

	public void setChemicalAssociationDropdowns(HttpServletRequest request)
			throws Exception {
		
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"bondTypes", "Attachment", "bondType", "otherBondType", true);
		
	}

	public SortedSet<String> getFunctionTypes(HttpServletRequest request)
			throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultFunctionTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
		SortedSet<String> otherTypes = compService.getAllOtherFunctionTypes();
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("functionTypes", types);
		return types;
	}

	public SortedSet<String> getNanoparticleEntityTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultNanoparticleEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
		SortedSet<String> otherTypes = compService
				.getAllOtherNanoparticleEntityTypes();
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("nanoparticleEntityTypes", types);
		return types;
	}

	public SortedSet<String> getFunctionalizingEntityTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultFunctionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
		SortedSet<String> otherTypes = compService
				.getAllOtherFunctionalizingEntityTypes();
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("functionalizingEntityTypes", types);
		return types;
	}

	public SortedSet<String> getChemicalAssociationTypes(
			HttpServletRequest request) throws Exception {
		SortedSet<String> defaultTypes = InitSetup
				.getInstance()
				.getServletContextDefaultTypesByReflection(
						request.getSession().getServletContext(),
						"defaultAssociationTypes",
						"gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation");
		SortedSet<String> otherTypes = compService
				.getAllOtherChemicalAssociationTypes();
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		types.addAll(otherTypes);
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
		SortedSet<String> otherTypes = compService.getAllOtherFunctionTypes();
		SortedSet<String> types = new TreeSet<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("targetTypes", types);
		return types;
	}
}
