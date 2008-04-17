package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.TreeNodeBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.ParticleDataLinkBean;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;
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
		appContext.setAttribute("physicalTypes", physicalCharaMap);
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
		appContext.setAttribute("invitroTypes", invitroCharaMap);
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
					(SortedMap<? extends TreeNodeBean, List<String>>) appContext
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

			String parentDisplayName = InitSetup.getInstance().getDisplayName(
					ClassUtils.getShortClassName(parentClassName), appContext);

			TreeNodeBean nodeBean = new TreeNodeBean(parentDisplayName,
					CaNanoLabConstants.CHARACTERIZATION_ORDER_MAP
							.get(parentDisplayName), new Integer(indentLevel));

			indentLevel++;
			String subclassName = null;
			for (String sclassName : subclassList) {
				String displayName = InitSetup.getInstance().getDisplayName(
						ClassUtils.getShortClassName(sclassName), appContext);
				tempList.add(displayName);

				setSubclassMap(appContext, typeMap, searchTreeMap, sclassName,
						indentLevel);
				subclassName = sclassName;
			}

			nodeBean.setHasGrandChildrenFlag(ClassUtils
					.hasChildrenClasses(subclassName));
			typeMap.put(parentDisplayName, tempList);
			searchTreeMap.put(nodeBean, tempList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, List<ParticleDataLinkBean>> getDataTree(
			ParticleBean particleBean, HttpServletRequest request)
			throws Exception {
		Map<String, List<ParticleDataLinkBean>> dataTree = new HashMap<String, List<ParticleDataLinkBean>>();
		if (request.getAttribute("updateDataTree") != null
				&& request.getAttribute("updateDataTree").equals("true")) {
			ServletContext appContext = request.getSession()
					.getServletContext();
			NanoparticleSample particleSample = particleBean
					.getParticleSample();
			// composition
			if (particleSample.getSampleComposition() != null) {
				List<ParticleDataLinkBean> ndataBeans = new ArrayList<ParticleDataLinkBean>();
				for (NanoparticleEntity entity : particleSample
						.getSampleComposition()
						.getNanoparticleEntityCollection()) {
					ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
							entity.getId().toString(), "Composition",
							"nanoparticleEntity.do");
					dataBean.setDataClassName(ClassUtils
							.getShortClassName(entity.getClass()
									.getCanonicalName()));
					dataBean.setDataDisplayType(InitSetup.getInstance()
							.getDisplayName(dataBean.getDataClassName(),
									appContext));
					ndataBeans.add(dataBean);
				}
				dataTree.put("Nanoparticle Entity", ndataBeans);

				List<ParticleDataLinkBean> fdataBeans = new ArrayList<ParticleDataLinkBean>();
				for (FunctionalizingEntity entity : particleSample
						.getSampleComposition()
						.getFunctionalizingEntityCollection()) {
					ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
							entity.getId().toString(), "Composition",
							"functionalizingEntity.do");
					dataBean.setDataClassName(ClassUtils
							.getShortClassName(entity.getClass()
									.getCanonicalName()));
					dataBean.setDataDisplayType(InitSetup.getInstance()
							.getDisplayName(dataBean.getDataClassName(),
									appContext));
					fdataBeans.add(dataBean);
				}
				dataTree.put("Functionalizing Entity", fdataBeans);

				List<ParticleDataLinkBean> adataBeans = new ArrayList<ParticleDataLinkBean>();
				for (ChemicalAssociation association : particleSample
						.getSampleComposition()
						.getChemicalAssociationCollection()) {
					ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
							association.getId().toString(), "Composition",
							"chemicalAssociation.do");
					dataBean.setDataClassName(ClassUtils
							.getShortClassName(association.getClass()
									.getCanonicalName()));
					dataBean.setDataDisplayType(InitSetup.getInstance()
							.getDisplayName(dataBean.getDataClassName(),
									appContext));
					adataBeans.add(dataBean);
				}
				dataTree.put("Chemical Association", adataBeans);

				List<ParticleDataLinkBean> ldataBeans = new ArrayList<ParticleDataLinkBean>();
				for (LabFile file : particleSample.getSampleComposition()
						.getLabFileCollection()) {
					ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
							file.getId().toString(), "Composition",
							"compositionFile.do");
					dataBean.setDataClassName("LabFile");
					dataBean.setDataDisplayType(file.getType());
					ldataBeans.add(dataBean);
				}
				dataTree.put("Composition File", ldataBeans);
			}

			// characterization
			List<ParticleDataLinkBean> cdataBeans = null;
			if (particleSample.getCharacterizationCollection() != null) {
				for (Characterization achar : particleSample
						.getCharacterizationCollection()) {
					String category = "";
					String link = "";
					if (achar instanceof PhysicalCharacterization) {
						category = "Physical Characterization";
						link = "physicalCharacterization.do";
					} else if (achar instanceof InvitroCharacterization) {
						category = "In Vitro Characterization";
						link = "invitroCharacterization.do";
					}
					ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
							achar.getId().toString(), category, link);
					dataBean.setDataClassName(ClassUtils
							.getShortClassName(achar.getClass()
									.getCanonicalName()));
					String charName = InitSetup.getInstance().getDisplayName(
							dataBean.getDataClassName(), appContext);
					dataBean.setDataDisplayType(achar.getIdentificationName());
					if (dataTree.get(charName) != null) {
						cdataBeans = (List<ParticleDataLinkBean>) dataTree
								.get(charName);
					} else {
						cdataBeans = new ArrayList<ParticleDataLinkBean>();
						dataTree.put(charName, cdataBeans);
					}
					cdataBeans.add(dataBean);
				}
			}
		} else {
			dataTree = new HashMap<String, List<ParticleDataLinkBean>>(
					(Map<? extends String, List<ParticleDataLinkBean>>) (request
							.getSession().getAttribute("particleDataTree")));
		}
		return dataTree;
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

	public void setComposingElementTypes(HttpServletRequest request)
			throws CaNanoLabException {
		List<String> composingElementTypes = getDefaultComposingElementTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = LookupService.getLookupValues(
				"ComposingElement", "otherType");
		composingElementTypes.addAll(otherTypes);
		request.getSession().setAttribute("composingElementTypes",
				composingElementTypes);
	}

	public void setEmulsionComposingElementTypes(HttpServletRequest request)
			throws CaNanoLabException {
		List<String> composingElementTypes = getDefaultComposingElementTypes(request
				.getSession().getServletContext());
		List<String> emulsionComposingElementTypes = getDefaultEmulsionComposingElementTypes(request
				.getSession().getServletContext());
		SortedSet<String> otherTypes = LookupService.getLookupValues(
				"ComposingElement", "otherType");
		List<String> allTypes = new ArrayList<String>();
		allTypes.addAll(composingElementTypes);
		allTypes.addAll(emulsionComposingElementTypes);
		allTypes.addAll(otherTypes);
		request.getSession().setAttribute("emulsionComposingElementTypes",
				allTypes);
	}
}
