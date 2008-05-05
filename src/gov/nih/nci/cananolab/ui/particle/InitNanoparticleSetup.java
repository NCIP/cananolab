package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.SortableName;
import gov.nih.nci.cananolab.dto.common.TreeNodeBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.ParticleDataLinkBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for nanoparticle forms.
 * 
 * @author pansu, cais
 * 
 */
public class InitNanoparticleSetup {
	private InitNanoparticleSetup() {
	}

	private NanoparticleSampleService particleService = new NanoparticleSampleService();

	public static InitNanoparticleSetup getInstance() {
		return new InitNanoparticleSetup();
	}

	public SortedSet<Source> getAllNanoparticleSampleSources(
			HttpServletRequest request) throws Exception {
		SortedSet<Source> sampleSources = particleService
				.findAllParticleSources();
		request.getSession().setAttribute("allParticleSources", sampleSources);
		return sampleSources;
	}

	public SortedSet<Source> getNanoparticleSampleSources(
			HttpServletRequest request, UserBean user) throws Exception {
		SortedSet<Source> sampleSources = particleService
				.findAllParticleSources(user);
		request.getSession().setAttribute("allUserParticleSources",
				sampleSources);
		return sampleSources;
	}

	public SortedSet<String> getAllNanoparticleSampleNames(
			HttpServletRequest request, UserBean user) throws Exception {
		SortedSet<String> sampleNames = particleService
				.findAllNanoparticleSampleNames(user);
		request.getSession().setAttribute("allUserParticleNames", sampleNames);
		return sampleNames;
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

	public Map<String, List<ParticleDataLinkBean>> getDefaultCompositionTypes(
			ServletContext appContext) throws Exception {

		Map<String, List<ParticleDataLinkBean>> compositionMap = new HashMap<String, List<ParticleDataLinkBean>>();
		List<ParticleDataLinkBean> compList = new ArrayList<ParticleDataLinkBean>(
				4);

		compList.add(new ParticleDataLinkBean("Nanoparticle Entity",
				"NanoparticleEntity", "nanoparticleEntity", "composition"));
		compList
				.add(new ParticleDataLinkBean("Functionalizing Entity",
						"FunctionalizingEntity", "functionalizingEntity",
						"composition"));
		compList.add(new ParticleDataLinkBean("Chemical Association",
				"ChemicalAssociation", "chemicalAssociation", "composition"));
		compList.add(new ParticleDataLinkBean("Composition File",
				"CompositionFile", "compositionFile", "composition"));

		compositionMap.put("Composition", compList);
		appContext.setAttribute("compositionTypes", compositionMap);
		return compositionMap;
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

	public Map<String, SortedSet<ParticleDataLinkBean>> getDataTree(
			String particleId, HttpServletRequest request) throws Exception {
		Map<String, SortedSet<ParticleDataLinkBean>> dataTree = new HashMap<String, SortedSet<ParticleDataLinkBean>>();
		if (request.getAttribute("updateDataTree") != null
				&& request.getAttribute("updateDataTree").equals("true")) {
			ServletContext appContext = request.getSession()
					.getServletContext();
			// reload nanoparticle sample
			NanoparticleSampleService particleService = new NanoparticleSampleService();
			ParticleBean particleBean = particleService
					.findNanoparticleSampleById(particleId);
			NanoparticleSample particleSample = particleBean
					.getDomainParticleSample();
			request.getSession().setAttribute("theParticle", particleBean);
			// composition
			if (particleSample.getSampleComposition() != null) {
				SortedSet<ParticleDataLinkBean> ndataBeans = new TreeSet<ParticleDataLinkBean>(
						new CaNanoLabComparators.ParticleDataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getNanoparticleEntityCollection() != null) {
					for (NanoparticleEntity entity : particleSample
							.getSampleComposition()
							.getNanoparticleEntityCollection()) {
						ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
								entity.getId().toString(), "Composition",
								"nanoparticleEntity", entity.getCreatedDate());
						dataBean.setDataClassName(ClassUtils
								.getShortClassName(entity.getClass()
										.getCanonicalName()));
						dataBean.setDataDisplayType(InitSetup.getInstance()
								.getDisplayName(dataBean.getDataClassName(),
										appContext));
						dataBean.setViewTitle(dataBean.getDataDisplayType());
						ndataBeans.add(dataBean);
					}
				}
				dataTree.put("Nanoparticle Entity", ndataBeans);

				SortedSet<ParticleDataLinkBean> fdataBeans = new TreeSet<ParticleDataLinkBean>(
						new CaNanoLabComparators.ParticleDataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getFunctionalizingEntityCollection() != null) {
					for (FunctionalizingEntity entity : particleSample
							.getSampleComposition()
							.getFunctionalizingEntityCollection()) {
						ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
								entity.getId().toString(), "Composition",
								"functionalizingEntity", entity
										.getCreatedDate());
						dataBean.setDataClassName(ClassUtils
								.getShortClassName(entity.getClass()
										.getCanonicalName()));
						dataBean.setDataDisplayType(InitSetup.getInstance()
								.getDisplayName(dataBean.getDataClassName(),
										appContext));
						fdataBeans.add(dataBean);
						dataBean.setViewTitle(dataBean.getDataDisplayType());
					}
				}
				dataTree.put("Functionalizing Entity", fdataBeans);

				SortedSet<ParticleDataLinkBean> adataBeans = new TreeSet<ParticleDataLinkBean>(
						new CaNanoLabComparators.ParticleDataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getChemicalAssociationCollection() != null) {
					int i = 1;
					for (ChemicalAssociation association : particleSample
							.getSampleComposition()
							.getChemicalAssociationCollection()) {
						ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
								association.getId().toString(), "Composition",
								"chemicalAssociation", association
										.getCreatedDate());
						dataBean.setDataClassName(ClassUtils
								.getShortClassName(association.getClass()
										.getCanonicalName()));
						dataBean.setDataDisplayType(InitSetup.getInstance()
								.getDisplayName(dataBean.getDataClassName(),
										appContext));
						dataBean.setViewTitle(dataBean.getDataDisplayType());
						adataBeans.add(dataBean);
					}
				}
				dataTree.put("Chemical Association", adataBeans);

				SortedSet<ParticleDataLinkBean> ldataBeans = new TreeSet<ParticleDataLinkBean>(
						new CaNanoLabComparators.ParticleDataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getLabFileCollection() != null) {
					for (LabFile file : particleSample.getSampleComposition()
							.getLabFileCollection()) {
						ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
								file.getId().toString(), "Composition",
								"compositionFile", file.getCreatedDate());
						dataBean.setDataClassName("LabFile");
						dataBean.setDataDisplayType(file.getType());
						dataBean.setViewTitle(dataBean.getDataDisplayType());
						ldataBeans.add(dataBean);
					}
				}
				dataTree.put("Composition File", ldataBeans);

				request.getSession().setAttribute("hasCompositionData", "true");
			} else {
				request.getSession()
						.setAttribute("hasCompositionData", "false");
			}

			// characterization
			boolean hasPhysicalData = false;
			boolean hasInVitroData = false;
			SortedSet<ParticleDataLinkBean> cdataBeans = null;
			if (particleSample.getCharacterizationCollection() != null) {
				for (Characterization achar : particleSample
						.getCharacterizationCollection()) {
					String category = "";
					String link = "";
					if (achar instanceof PhysicalCharacterization) {
						category = "Physical Characterization";
						link = "physicalCharacterization";
						hasPhysicalData = true;
					} else if (achar instanceof InvitroCharacterization) {
						category = "In Vitro Characterization";
						link = "invitroCharacterization";
						hasInVitroData = true;

					}
					ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
							achar.getId().toString(), category, link, achar
									.getCreatedDate());
					dataBean.setDataClassName(ClassUtils
							.getShortClassName(achar.getClass()
									.getCanonicalName()));
					String charName = InitSetup.getInstance().getDisplayName(
							dataBean.getDataClassName(), appContext);
					dataBean.setDataDisplayType(charName);
					dataBean.setViewTitle(achar.getIdentificationName());
					if (dataTree.get(charName) != null) {
						cdataBeans = (TreeSet<ParticleDataLinkBean>) dataTree
								.get(charName);
					} else {
						cdataBeans = new TreeSet<ParticleDataLinkBean>(
								new CaNanoLabComparators.ParticleDataLinkTypeDateComparator());
						dataTree.put(charName, cdataBeans);
					}
					cdataBeans.add(dataBean);
				}

				// report
				SortedSet<ParticleDataLinkBean> rdataBeans = new TreeSet<ParticleDataLinkBean>(
						new CaNanoLabComparators.ParticleDataLinkTypeDateComparator());
				if (particleSample.getReportCollection() != null) {
					for (Report report : particleSample.getReportCollection()) {
						String reportCategory = report.getCategory();
						ParticleDataLinkBean dataBean = new ParticleDataLinkBean(
								report.getId().toString(), "Report",
								"submitReport", report.getCreatedDate());
						dataBean.setDataDisplayType(reportCategory);
						dataBean.setViewTitle(report.getUri());
						if (dataTree.get(reportCategory) != null) {
							rdataBeans = (TreeSet<ParticleDataLinkBean>) dataTree
									.get(reportCategory);
						} else {
							rdataBeans = new TreeSet<ParticleDataLinkBean>(
									new CaNanoLabComparators.ParticleDataLinkTypeDateComparator());
							dataTree.put(reportCategory, rdataBeans);
						}
						rdataBeans.add(dataBean);
					}
				}
			}
			request.getSession().setAttribute("particleDataTree", dataTree);

			if (hasPhysicalData)
				request.getSession().setAttribute("hasPhysicalData", "true");
			else
				request.getSession().setAttribute("hasPhysicalData", "false");

			if (hasInVitroData)
				request.getSession().setAttribute("hasInVitroData", "true");
			else
				request.getSession().setAttribute("hasInVitroData", "false");

		} else {
			dataTree = new HashMap<String, SortedSet<ParticleDataLinkBean>>(
					(Map<? extends String, SortedSet<ParticleDataLinkBean>>) (request
							.getSession().getAttribute("particleDataTree")));
		}
		return dataTree;
	}

	public SortedSet<SortableName> getOtherParticleNames(
			HttpServletRequest request, String particleName,
			String particleSource, UserBean user) throws Exception {
		SortedSet<SortableName> names = particleService.findOtherParticles(
				particleSource, particleName, user);
		request.getSession().setAttribute("otherParticleNames", names);
		return names;
	}

	public void setSharedDropdowns(HttpServletRequest request) throws Exception {
		// set static boolean yes or no and characterization source choices
		ServletContext appContext = request.getSession().getServletContext();
		appContext.setAttribute("booleanChoices",
				CaNanoLabConstants.BOOLEAN_CHOICES);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "LabFile", "type", "otherType", true);
		InitSetup.getInstance().getServletContextDefaultLookupTypes(appContext,
				"molecularFormulaTypes", "ComposingElement",
				"molecularFormulaType");
	}

	// public void setProtocolFilesByCharType(HttpSession session, String
	// charType)
	// throws ParticleCharacterizationException,
	// CaNanoLabSecurityException, ProtocolException {
	// String protocolType = null;
	// if (charType != null
	// && charType
	// .equalsIgnoreCase(Characterization.PHYSICAL_CHARACTERIZATION)) {
	// protocolType = CaNanoLabConstants.PHYSICAL_ASSAY_PROTOCOL;
	// } else if (charType != null
	// && charType
	// .equalsIgnoreCase(Characterization.INVITRO_CHARACTERIZATION)) {
	// protocolType = CaNanoLabConstants.INVITRO_ASSAY_PROTOCOL;
	// } else {
	// protocolType = null; // update if in vivo is implemented
	// }
	// List<ProtocolFileBean> protocolFiles = null;
	// protocolFiles = searchProtocolService
	// .getProtocolFileBeans(protocolType);
	// session.setAttribute("submitTypeProtocolFiles", protocolFiles);
	// }

}
