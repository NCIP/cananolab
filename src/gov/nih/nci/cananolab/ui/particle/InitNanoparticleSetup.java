package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for nanoparticle forms.
 * 
 * @author pansu
 * 
 */
public class InitNanoparticleSetup {
	private InitNanoparticleSetup() {
	}

	private NanoparticleSampleService particleService = new NanoparticleSampleService();

	public static InitNanoparticleSetup getInstance() {
		return new InitNanoparticleSetup();
	}

	public void setAllNanoparticleSampleSources(HttpServletRequest request)
			throws Exception {
		SortedSet<Source> sampleSources = particleService.getAllSampleSources();
		request.setAttribute("allSampleSources", sampleSources);
	}

	public List<String> getDefaultFunctionTypes(ServletContext appContext)
			throws Exception {
		if (appContext.getAttribute("defaultFunctionTypes") == null) {
			List<String> functionTypes = new ArrayList<String>();
			List<String> functionClassNames = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			for (String name : functionClassNames) {
				if (!name.contains("Other")) {
					functionTypes.add(ClassUtils.getClassDisplayName(name));
				}
			}
			appContext.setAttribute("defaultFunctionTypes", functionTypes);
			return functionTypes;
		} else {
			return new ArrayList<String>((List<? extends String>) appContext
					.getAttribute("defaultFunctionTypes"));
		}

	}

	public List<String> getDefaultNanoparticleEntityTypes(
			ServletContext appContext) throws Exception {
		if (appContext.getAttribute("defaultNanoparticleEntityTypes") == null) {
			List<String> nanoparticleEntityTypes = new ArrayList<String>();
			List<String> classNames = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			for (String name : classNames) {
				if (!name.contains("Other")) {
					nanoparticleEntityTypes.add(ClassUtils
							.getClassDisplayName(name));
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
					functionalizingEntityTypes.add(ClassUtils
							.getClassDisplayName(name));
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

	public void setFunctionTypes(HttpServletRequest request) throws Exception {
		List<String> defaultTypes = getDefaultFunctionTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = particleService
				.getAllOtherFunctionTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.setAttribute("functionTypes", types);
	}

	public void setNanoparticleEntityTypes(HttpServletRequest request)
			throws Exception {
		List<String> defaultTypes = getDefaultNanoparticleEntityTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = particleService
				.getAllOtherNanoparticleEntityTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.setAttribute("nanoparticleEntityTypes", types);
	}

	public void setFunctionalizingEntityTypes(HttpServletRequest request)
			throws Exception {
		List<String> defaultTypes = getDefaultFunctionalizingEntityTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = particleService
				.getAllOtherFunctionalizingEntityTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.setAttribute("functionalizingEntityTypes", types);
	}
}
