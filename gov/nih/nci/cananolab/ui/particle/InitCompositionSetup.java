package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for composition forms.
 * 
 * @author pansu
 * 
 */
public class InitCompositionSetup {

	private NanoparticleCompositionService compService = new NanoparticleCompositionService();

	public static InitCompositionSetup getInstance() {
		return new InitCompositionSetup();
	}

	public void setFunctionTypes(HttpServletRequest request) throws Exception {
		List<String> defaultTypes = getDefaultFunctionTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = compService.getAllOtherFunctionTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("functionTypes", types);
	}

	public void setNanoparticleEntityTypes(HttpServletRequest request)
			throws Exception {
		List<String> defaultTypes = getDefaultNanoparticleEntityTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = compService
				.getAllOtherNanoparticleEntityTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("nanoparticleEntityTypes", types);
	}

	public void setFunctionalizingEntityTypes(HttpServletRequest request)
			throws Exception {
		List<String> defaultTypes = getDefaultFunctionalizingEntityTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = compService
				.getAllOtherFunctionalizingEntityTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("functionalizingEntityTypes", types);
	}

	public List<String> getDefaultComposingElementTypes(
			ServletContext appContext) throws CaNanoLabException {
		List<String> composingElementTypes = null;
		if (appContext.getAttribute("defaultComposingElementsTypes") == null) {
			composingElementTypes = new ArrayList<String>(LookupService
					.getLookupValues("ComposingElement", "type"));
			appContext.setAttribute("defaultComposingElementTypes",
					composingElementTypes);
		} else {
			composingElementTypes = new ArrayList<String>(
					(List<? extends String>) appContext
							.getAttribute("defaultComposingElementsTypes"));
		}
		return composingElementTypes;
	}

	public List<String> getDefaultEmulsionComposingElementTypes(
			ServletContext appContext) throws CaNanoLabException {
		List<String> composingElementTypes = null;
		if (appContext.getAttribute("defaultEmulsionComposingElementsTypes") == null) {
			composingElementTypes = new ArrayList<String>(LookupService
					.getLookupValues("Emulsion", "composingElementType"));
			appContext.setAttribute("defaultEmulsionComposingElementTypes",
					composingElementTypes);
		} else {
			composingElementTypes = new ArrayList<String>(
					(List<? extends String>) appContext
							.getAttribute("defaultEmulsionComposingElementsTypes"));
		}
		return composingElementTypes;
	}

	public List<String> getComposingElementTypes(HttpServletRequest request)
			throws CaNanoLabException {
		List<String> composingElementTypes = null;
		if (request.getAttribute("composingElementTypes") == null) {
			composingElementTypes = getDefaultComposingElementTypes(request
					.getSession().getServletContext());
			SortedSet<String> otherTypes = LookupService.getLookupValues(
					"ComposingElement", "otherType");
			composingElementTypes.addAll(otherTypes);
			request.getSession().setAttribute("composingElementTypes",
					composingElementTypes);
		} else {
			composingElementTypes = new ArrayList<String>(
					(List<? extends String>) request
							.getAttribute("composingElementTypes"));
		}
		return composingElementTypes;
	}

	public List<String> getEmulsionComposingElementTypes(
			HttpServletRequest request) throws CaNanoLabException {
		List<String> allTypes = null;
		if (request.getAttribute("emulsionComposingElementTypes") == null) {
			List<String> composingElementTypes = getDefaultComposingElementTypes(request
					.getSession().getServletContext());
			List<String> emulsionComposingElementTypes = getDefaultEmulsionComposingElementTypes(request
					.getSession().getServletContext());
			SortedSet<String> otherTypes = LookupService.getLookupValues(
					"ComposingElement", "otherType");
			allTypes = new ArrayList<String>();
			allTypes.addAll(composingElementTypes);
			allTypes.addAll(emulsionComposingElementTypes);
			allTypes.addAll(otherTypes);
			request.getSession().setAttribute("emulsionComposingElementTypes",
					allTypes);
		} else {
			allTypes = new ArrayList<String>((List<? extends String>) request
					.getAttribute("emulsionComposingElementTypes"));
		}
		return allTypes;
	}

	public SortedSet<String> getAntigenSpecies(ServletContext appContext)
			throws CaNanoLabException {
		SortedSet<String> types = LookupService.getLookupValues("Antigen",
				"species");
		appContext.setAttribute("antigenSpecies", types);
		return types;
	}

	public SortedSet<String> getBiopolymerTypes(HttpServletRequest request)
			throws CaNanoLabException {
		SortedSet<String> types = LookupService.getLookupValues("Biopolymer",
				"type");
		SortedSet<String> otherTypes = LookupService.getLookupValues(
				"Biopolymer", "otherType");
		types.addAll(otherTypes);
		request.getSession().setAttribute("biopolymerTypes", types);
		return types;
	}

	public SortedSet<String> getModalityTypes(HttpServletRequest request)
			throws CaNanoLabException {
		SortedSet<String> types = LookupService.getLookupValues(
				"ImagingFunction", "modality");
		SortedSet<String> otherTypes = LookupService.getLookupValues(
				"ImagingFunction", "otherModality");
		types.addAll(otherTypes);
		request.getSession().setAttribute("modalityTypes", types);
		return types;
	}

	public List<String> getDefaultNanoparticleEntityTypes(
			ServletContext appContext) throws Exception {
		if (appContext.getAttribute("defaultNanoparticleEntityTypes") == null) {
			List<String> nanoparticleEntityTypes = new ArrayList<String>();
			List<String> classNames = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			for (String name : classNames) {
				if (!name.contains("Other")) {
					String displayName = InitSetup.getInstance()
							.getDisplayName(ClassUtils.getShortClassName(name),
									appContext);
					nanoparticleEntityTypes.add(displayName);
				}
			}
			appContext.setAttribute("defaultNanoparticleEntityTypes",
					nanoparticleEntityTypes);
			return nanoparticleEntityTypes;
		} else {
			return new ArrayList<String>((List<? extends String>) appContext
					.getAttribute("defaultNanoparticleEntityTypes"));
		}
	}

	public List<String> getDefaultFunctionalizingEntityTypes(
			ServletContext appContext) throws Exception {
		if (appContext.getAttribute("defaultFunctionalizingEntityTypes") == null) {
			List<String> functionalizingEntityTypes = new ArrayList<String>();
			List<String> classNames = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
			for (String name : classNames) {
				if (!name.contains("Other")) {
					String displayName = InitSetup.getInstance()
							.getDisplayName(ClassUtils.getShortClassName(name),
									appContext);
					functionalizingEntityTypes.add(displayName);
				}
			}
			appContext.setAttribute("defaultFunctionalizingEntityTypes",
					functionalizingEntityTypes);
			return functionalizingEntityTypes;
		} else {
			return new ArrayList<String>((List<? extends String>) appContext
					.getAttribute("defaultFunctionalizingEntityTypes"));
		}
	}

	public List<String> getDefaultFunctionTypes(ServletContext appContext)
			throws Exception {
		if (appContext.getAttribute("defaultFunctionTypes") == null) {
			List<String> functionTypes = new ArrayList<String>();
			List<String> functionClassNames = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			for (String name : functionClassNames) {
				if (!name.contains("Other")) {
					String displayName = InitSetup.getInstance()
							.getDisplayName(ClassUtils.getShortClassName(name),
									appContext);
					functionTypes.add(displayName);
				}
			}
			appContext.setAttribute("defaultFunctionTypes", functionTypes);
			return functionTypes;
		} else {
			return new ArrayList<String>((List<? extends String>) appContext
					.getAttribute("defaultFunctionTypes"));
		}

	}

}
