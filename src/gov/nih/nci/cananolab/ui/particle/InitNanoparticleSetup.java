package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.dto.characterization.TreeNodeBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

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
		SortedSet<Source> sampleSources = particleService
				.getAllParticleSources();
		request.getSession().setAttribute("allParticleSources", sampleSources);
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

	public void setFunctionTypes(HttpServletRequest request) throws Exception {
		List<String> defaultTypes = getDefaultFunctionTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = particleService
				.getAllOtherFunctionTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("functionTypes", types);
	}

	public void setNanoparticleEntityTypes(HttpServletRequest request)
			throws Exception {
		List<String> defaultTypes = getDefaultNanoparticleEntityTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = particleService
				.getAllOtherNanoparticleEntityTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("nanoparticleEntityTypes", types);
	}

	public void setFunctionalizingEntityTypes(HttpServletRequest request)
			throws Exception {
		List<String> defaultTypes = getDefaultFunctionalizingEntityTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = particleService
				.getAllOtherFunctionalizingEntityTypes();
		List<String> types = new ArrayList<String>(defaultTypes);
		types.addAll(otherTypes);
		request.getSession().setAttribute("functionalizingEntityTypes", types);
	}

	@SuppressWarnings("unchecked")
	public Map<TreeNodeBean, List<String>> getDefaultPhysicalCharacterizationTypes(
			ServletContext appContext) throws Exception {

		Map<String, List<String>> physicalCharaMap = new HashMap<String, List<String>>();
		Map<TreeNodeBean, List<String>> searchTreeMap = new HashMap<TreeNodeBean, List<String>>();
		short indentLevel = -1;

		setSubclassMap(
				appContext,
				physicalCharaMap,
				searchTreeMap,
				"gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization",
				indentLevel);

		return searchTreeMap;
	}

	@SuppressWarnings("unchecked")
	public Map<TreeNodeBean, List<String>> getDefaultInvitroCharacterizationTypes(
			ServletContext appContext) throws Exception {

		Map<String, List<String>> invitroCharaMap = new HashMap<String, List<String>>();
		Map<TreeNodeBean, List<String>> searchTreeMap = new HashMap<TreeNodeBean, List<String>>();

		short indentLevel = -1;
		setSubclassMap(
				appContext,
				invitroCharaMap,
				searchTreeMap,
				"gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization",
				indentLevel);

		return searchTreeMap;

	}

	public SortedMap<TreeNodeBean, List<String>> getDefaultCharacterizationTypes(
			ServletContext appContext) throws Exception {
		if (appContext.getAttribute("characterizationTypes") == null) {

			SortedMap<TreeNodeBean, List<String>> charaMap = new TreeMap<TreeNodeBean, List<String>>();
			Map<TreeNodeBean, List<String>> physicalMap = getDefaultPhysicalCharacterizationTypes(appContext);
			Map<TreeNodeBean, List<String>> invitroMap = getDefaultInvitroCharacterizationTypes(appContext);

			charaMap.putAll(physicalMap);
			charaMap.putAll(invitroMap);

			appContext.setAttribute("characterizationTypes", charaMap);

			return charaMap;

		} else {
			return new TreeMap<TreeNodeBean, List<String>>(
					(TreeMap<? extends TreeNodeBean, List<String>>) appContext
							.getAttribute("characterizationTypes"));
		}

	}

	private static void setSubclassMap(ServletContext appContext,
			Map<String, List<String>> typeMap,
			Map<TreeNodeBean, List<String>> searchTreeMap,
			String parentClassName, int indentLevel) {
		try {
			List<String> subclassList = ClassUtils
					.getChildClassNames(parentClassName);

			if (subclassList == null || subclassList.size() == 0) {
				return;
			}
			List<String> tempList = new ArrayList<String>();

			indentLevel++;
			for (String sclassName : subclassList) {
				String displayName = InitSetup.getInstance().getDisplayName(
						ClassUtils.getShortClassName(sclassName), appContext);
				tempList.add(displayName);

				setSubclassMap(appContext, typeMap, searchTreeMap, sclassName,
						indentLevel);
			}

			String parentDisplayName = InitSetup.getInstance().getDisplayName(
					ClassUtils.getShortClassName(parentClassName), appContext);

			typeMap.put(parentDisplayName, tempList);

			TreeNodeBean nodeBean = new TreeNodeBean(parentDisplayName,
					CaNanoLabConstants.CHARACTERIZATION_ORDER_MAP
							.get(parentDisplayName), new Integer(indentLevel));
			searchTreeMap.put(nodeBean, tempList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
