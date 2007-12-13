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
		Map<String, SortedSet<SampleBean>> sampleSourceSamples = sampleService
				.getSampleSourceSamplesWithUnmaskedAliquots();
		session.setAttribute("sampleSourceSamplesWithUnmaskedAliquots",
				sampleSourceSamples);
		List<String> sources = new ArrayList<String>(sampleSourceSamples
				.keySet());
		session.setAttribute("allSampleSourcesWithUnmaskedAliquots", sources);
		setAllSampleUnmaskedAliquots(session);
	}

	public void setAllSampleUnmaskedAliquots(HttpSession session)
			throws SampleException {
		// set map between samples and unmasked aliquots
		Map<String, SortedSet<AliquotBean>> sampleAliquots = aliquotService
				.getUnmaskedSampleAliquots();
		List<String> sampleNames = new ArrayList<String>(sampleAliquots
				.keySet());
		Collections.sort(sampleNames,
				new CaNanoLabComparators.SortableNameComparator());
		session.setAttribute("allUnmaskedSampleAliquots", sampleAliquots);
		session.setAttribute("allSampleNamesWithAliquots", sampleNames);
	}

	public void clearSampleUnmaskedAliquotsSession(HttpSession session) {
		session.removeAttribute("allUnmaskedSampleAliquots");
		session.removeAttribute("allSampleNamesWithAliquots");
	}

	public void setAllSampleSources(HttpSession session) throws SampleException {
		SortedSet<String> sampleSources = sampleService.getAllSampleSources();
		session.setAttribute("allSampleSources", sampleSources);
	}

	public void clearSampleSourcesSession(HttpSession session) {
		session.removeAttribute("allSampleSources");
	}

	public void setAllSampleContainerTypes(HttpSession session)
			throws SampleException {
		SortedSet<String> containerTypes = sampleService
				.getAllSampleContainerTypes();
		session.setAttribute("allSampleContainerTypes", containerTypes);
	}

	public void setAllSampleTypes(ServletContext appContext)
			throws CaNanoLabException {
		if (appContext.getAttribute("allSampleTypes") == null) {
			SortedSet<String> types = LookupService
					.getAllLookupTypes("SampleType");
			appContext.setAttribute("allSampleTypes", types);
		}
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

		ContainerInfoBean containerInfo = sampleService
				.getSampleContainerInfo();
		session.setAttribute("sampleContainerInfo", containerInfo);
	}

	public void setAllAliquotContainerTypes(HttpSession session)
			throws SampleException {
		SortedSet<String> containerTypes = aliquotService
				.getAllAliquotContainerTypes();
		session.setAttribute("allAliquotContainerTypes", containerTypes);
	}

	public void setAllAliquotContainerInfo(HttpSession session)
			throws SampleException {
		ContainerInfoBean containerInfo = aliquotService
				.getAliquotContainerInfo();
		session.setAttribute("aliquotContainerInfo", containerInfo);
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
		Map<String, SortedSet<ContainerBean>> sampleContainers = sampleService
				.getAllSampleContainers();
		List<String> sampleNames = new ArrayList<String>(sampleContainers
				.keySet());
		Collections.sort(sampleNames,
				new CaNanoLabComparators.SortableNameComparator());

		session.setAttribute("allSampleContainers", sampleContainers);
		session.setAttribute("allSampleNames", sampleNames);
	}

	public void setAllSourceSampleIds(HttpSession session)
			throws SampleException {
		List sourceSampleIds = sampleService.getAllSourceSampleIds();
		session.setAttribute("allSourceSampleIds", sourceSampleIds);
	}

	public void clearSampleSession(HttpSession session) {
		session.removeAttribute("createSampleForm");
		session.removeAttribute("createAliquotForm");
		session.removeAttribute("aliquotMatrix");
		session.removeAttribute("aliquots");
		session.removeAttribute("allSampleContainers");
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
