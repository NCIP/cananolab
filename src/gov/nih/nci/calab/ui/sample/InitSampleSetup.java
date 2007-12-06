package gov.nih.nci.calab.ui.sample;

import gov.nih.nci.calab.dto.sample.AliquotBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.ContainerInfoBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.exception.CaNanoLabException;
import gov.nih.nci.calab.exception.SampleException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.sample.AliquotService;
import gov.nih.nci.calab.service.sample.SampleService;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for sample/aliquot forms.
 * 
 * @author pansu
 * 
 */
public class InitSampleSetup {
	private static SampleService sampleService;

	private static AliquotService aliquotService;

	private static LookupService lookupService;

	private InitSampleSetup() {
		sampleService = new SampleService();
		aliquotService = new AliquotService();
		lookupService = new LookupService();
	}

	public static InitSampleSetup getInstance() {
		return new InitSampleSetup();
	}

	public void setSampleSourceUnmaskedAliquots(HttpSession session)
			throws SampleException {
		// set map between sample source and samples that have unmasked aliquots
		if (session.getAttribute("sampleSourceSamplesWithUnmaskedAliquots") == null
				|| session.getAttribute("allSampleSourcesWithUnmaskedAliquots") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			Map<String, SortedSet<SampleBean>> sampleSourceSamples = sampleService
					.getSampleSourceSamplesWithUnmaskedAliquots();
			session.setAttribute("sampleSourceSamplesWithUnmaskedAliquots",
					sampleSourceSamples);
			List<String> sources = new ArrayList<String>(sampleSourceSamples
					.keySet());
			session.setAttribute("allSampleSourcesWithUnmaskedAliquots",
					sources);
		}
		setAllSampleUnmaskedAliquots(session);
		session.removeAttribute("newAliquotCreated");
	}

	public void clearSampleSourcesWithUnmaskedAliquotsSession(
			HttpSession session) {
		session.removeAttribute("allSampleSourcesWithUnmaskedAliquots");
	}

	public void setAllSampleUnmaskedAliquots(HttpSession session)
			throws SampleException {
		// set map between samples and unmasked aliquots
		if (session.getAttribute("allUnmaskedSampleAliquots") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			Map<String, SortedSet<AliquotBean>> sampleAliquots = aliquotService
					.getUnmaskedSampleAliquots();
			List<String> sampleNames = new ArrayList<String>(sampleAliquots
					.keySet());
			Collections.sort(sampleNames,
					new CaNanoLabComparators.SortableNameComparator());
			session.setAttribute("allUnmaskedSampleAliquots", sampleAliquots);
			session.setAttribute("allSampleNamesWithAliquots", sampleNames);
		}
		session.removeAttribute("newAliquotCreated");
	}

	public void clearSampleUnmaskedAliquotsSession(HttpSession session) {
		session.removeAttribute("allUnmaskedSampleAliquots");
		session.removeAttribute("allSampleNamesWithAliquots");
	}

	public void setAllSampleSources(HttpSession session) throws SampleException {

		if (session.getAttribute("allSampleSources") == null
				|| session.getAttribute("newSampleSourceCreated") != null) {
			SortedSet<String> sampleSources = sampleService
					.getAllSampleSources();
			session.setAttribute("allSampleSources", sampleSources);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleSourceCreated");
	}

	public void clearSampleSourcesSession(HttpSession session) {
		session.removeAttribute("allSampleSources");
	}

	public void setAllSampleContainerTypes(HttpSession session)
			throws SampleException {
		if (session.getAttribute("allSampleContainerTypes") == null
				|| session.getAttribute("newSampleCreated") != null) {
			SortedSet<String> containerTypes = sampleService
					.getAllSampleContainerTypes();
			session.setAttribute("allSampleContainerTypes", containerTypes);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void clearSampleContainerTypesSession(HttpSession session) {
		session.removeAttribute("allSampleContainerTypes");
	}

	public void setAllSampleTypes(ServletContext appContext)
			throws CaNanoLabException {
		if (appContext.getAttribute("allSampleTypes") == null) {
			SortedSet<String> types = LookupService
					.getAllLookupTypes("SampleType");
			appContext.setAttribute("allSampleTypes", types);
		}
	}

	public void clearSampleTypesSession(HttpSession session) {
		// session.removeAttribute("allSampleTypes");
	}

	public void setAllSampleSOPs(HttpSession session) throws SampleException {
		if (session.getServletContext().getAttribute("allSampleSOPs") == null) {
			List sampleSOPs = sampleService.getAllSampleSOPs();
			session.getServletContext().setAttribute("allSampleSOPs",
					sampleSOPs);
		}
	}

	public void setAllSampleContainerInfo(HttpSession session)
			throws SampleException {
		if (session.getAttribute("sampleContainerInfo") == null
				|| session.getAttribute("newSampleCreated") != null) {
			ContainerInfoBean containerInfo = sampleService
					.getSampleContainerInfo();
			session.setAttribute("sampleContainerInfo", containerInfo);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void setAllAliquotContainerTypes(HttpSession session)
			throws SampleException {
		if (session.getAttribute("allAliquotContainerTypes") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			SortedSet<String> containerTypes = aliquotService
					.getAllAliquotContainerTypes();
			session.setAttribute("allAliquotContainerTypes", containerTypes);
		}
		session.removeAttribute("newAliquotCreated");
	}

	public void clearAliquotContainerTypesSession(HttpSession session) {
		session.removeAttribute("allAliquotContainerTypes");
	}

	public void setAllAliquotContainerInfo(HttpSession session)
			throws SampleException {
		if (session.getAttribute("aliquotContainerInfo") == null
				|| session.getAttribute("newAliquotCreated") != null) {
			ContainerInfoBean containerInfo = aliquotService
					.getAliquotContainerInfo();
			session.setAttribute("aliquotContainerInfo", containerInfo);
		}
		session.removeAttribute("newAliquotCreated");
	}

	public void setAllAliquotCreateMethods(HttpSession session)
			throws SampleException {
		if (session.getServletContext().getAttribute("aliquotCreateMethods") == null) {
			List methods = aliquotService.getAliquotCreateMethods();
			session.getServletContext().setAttribute("aliquotCreateMethods",
					methods);
		}
	}

	public void setAllSampleContainers(HttpSession session)
			throws SampleException {
		if (session.getAttribute("allSampleContainers") == null
				|| session.getAttribute("newSampleCreated") != null) {
			Map<String, SortedSet<ContainerBean>> sampleContainers = sampleService
					.getAllSampleContainers();
			List<String> sampleNames = new ArrayList<String>(sampleContainers
					.keySet());
			Collections.sort(sampleNames,
					new CaNanoLabComparators.SortableNameComparator());

			session.setAttribute("allSampleContainers", sampleContainers);
			session.setAttribute("allSampleNames", sampleNames);
		}
		session.removeAttribute("newSampleCreated");
	}

	public void clearSampleContainersSession(HttpSession session) {
		session.removeAttribute("allSampleContainers");
		session.removeAttribute("allSampleNames");
	}

	public void setAllSourceSampleIds(HttpSession session)
			throws SampleException {
		if (session.getAttribute("allSourceSampleIds") == null
				|| session.getAttribute("newSampleCreated") != null) {
			List sourceSampleIds = sampleService.getAllSourceSampleIds();
			session.setAttribute("allSourceSampleIds", sourceSampleIds);
		}
		// clear the new sample created flag
		session.removeAttribute("newSampleCreated");
	}

	public void clearSourceSampleIdsSession(HttpSession session) {
		session.removeAttribute("allSourceSampleIds");
	}

	public void clearSampleSession(HttpSession session) {
		clearSampleTypesSession(session);
		clearSampleContainerTypesSession(session);
		clearAliquotContainerTypesSession(session);
		clearSourceSampleIdsSession(session);
		clearSampleSourcesSession(session);
		clearSampleContainersSession(session);
		clearSampleContainerTypesSession(session);
		clearSampleUnmaskedAliquotsSession(session);
		clearAliquotContainerTypesSession(session);
		session.removeAttribute("createSampleForm");
		session.removeAttribute("createAliquotForm");
		session.removeAttribute("aliquotMatrix");
		session.removeAttribute("aliquots");
		session.removeAttribute("sampleContainers");
	}

	public static LookupService getLookupService() {
		return lookupService;
	}

	public void updateEditableDropdown(HttpSession session,
			String formAttribute, String sessionAttributeName) {
		SortedSet<String> dropdown = (SortedSet<String>) session
				.getAttribute(sessionAttributeName);
		if (dropdown != null && formAttribute != null
				&& formAttribute.length() > 0) {
			dropdown.add(formAttribute);
		}
	}
}
