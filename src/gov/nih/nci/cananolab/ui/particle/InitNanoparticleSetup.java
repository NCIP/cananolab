package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.util.ClassUtils;

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

	public void setDefaultFunctionTypes(ServletContext appContext)
			throws Exception {
		List<String> functionTypes = new ArrayList<String>();
		List<String> functionClassNames = ClassUtils
				.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
		for (String name : functionClassNames) {
			if (!name.contains("Other")) {
				functionTypes.add(ClassUtils.getClassDisplayName(name));
			}
		}
		appContext.setAttribute("defaultFunctionTypes", functionTypes);
	}
}
