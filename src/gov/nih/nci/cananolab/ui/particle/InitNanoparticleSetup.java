package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.DataLinkBean;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.TreeNodeBean;

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

import org.apache.struts.util.LabelValueBean;

/**
 * This class sets up information required for nanoparticle forms.
 * 
 * @author pansu, cais
 * 
 */
public class InitNanoparticleSetup {
	private InitNanoparticleSetup() {
	}

	private NanoparticleSampleService particleService = new NanoparticleSampleServiceLocalImpl();

	public static InitNanoparticleSetup getInstance() {
		return new InitNanoparticleSetup();
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
			Map<String, String> charaClassNameMap = new HashMap<String, String>();
			Map<TreeNodeBean, List<String>> physicalMap = getDefaultPhysicalCharacterizationTypes(
					appContext, charaClassNameMap);
			Map<TreeNodeBean, List<String>> invitroMap = getDefaultInvitroCharacterizationTypes(
					appContext, charaClassNameMap);

			charaMap.putAll(physicalMap);
			charaMap.putAll(invitroMap);

			appContext.setAttribute("charaClassNameMap", charaClassNameMap);
			appContext.setAttribute("characterizationTypes", charaMap);

			return charaMap;

		} else {
			return new TreeMap<TreeNodeBean, List<String>>(
					(SortedMap<? extends TreeNodeBean, List<String>>) appContext
							.getAttribute("characterizationTypes"));
		}
	}

	public Map<String, List<DataLinkBean>> getDefaultCompositionTypes(
			ServletContext appContext) throws Exception {

		Map<String, List<DataLinkBean>> compositionMap = new HashMap<String, List<DataLinkBean>>();
		List<DataLinkBean> compList = new ArrayList<DataLinkBean>(4);

		compList.add(new DataLinkBean("Nanoparticle Entity",
				"NanoparticleEntity", "nanoparticleEntity", "composition"));
		compList
				.add(new DataLinkBean("Functionalizing Entity",
						"FunctionalizingEntity", "functionalizingEntity",
						"composition"));
		compList.add(new DataLinkBean("Chemical Association",
				"ChemicalAssociation", "chemicalAssociation", "composition"));
		compList.add(new DataLinkBean("Composition File", "CompositionFile",
				"compositionFile", "composition"));

		compositionMap.put("Composition", compList);
		appContext.setAttribute("compositionTypes", compositionMap);
		return compositionMap;
	}

	@SuppressWarnings("unchecked")
	public Map<TreeNodeBean, List<String>> getDefaultPhysicalCharacterizationTypes(
			ServletContext appContext, Map<String, String> charaClassNameMap)
			throws Exception {

		Map<String, List<String>> physicalCharaMap = new HashMap<String, List<String>>();
		Map<TreeNodeBean, List<String>> searchTreeMap = new HashMap<TreeNodeBean, List<String>>();
		short indentLevel = -1;

		setSubclassMap(
				appContext,
				charaClassNameMap,
				physicalCharaMap,
				searchTreeMap,
				"gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization",
				indentLevel);
		appContext.setAttribute("physicalTypes", physicalCharaMap);

		return searchTreeMap;
	}

	@SuppressWarnings("unchecked")
	public Map<TreeNodeBean, List<String>> getDefaultInvitroCharacterizationTypes(
			ServletContext appContext, Map<String, String> charaClassNameMap)
			throws Exception {

		Map<String, List<String>> invitroCharaMap = new HashMap<String, List<String>>();
		Map<TreeNodeBean, List<String>> searchTreeMap = new HashMap<TreeNodeBean, List<String>>();

		short indentLevel = -1;
		setSubclassMap(
				appContext,
				charaClassNameMap,
				invitroCharaMap,
				searchTreeMap,
				"gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization",
				indentLevel);
		appContext.setAttribute("invitroTypes", invitroCharaMap);
		return searchTreeMap;
	}

	private static void setSubclassMap(ServletContext appContext,
			Map<String, String> classNameMap,
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

			String shortParentClassName = ClassUtils
					.getShortClassName(parentClassName);
			String parentDisplayName = InitSetup.getInstance().getDisplayName(
					shortParentClassName, appContext);

			classNameMap.put(parentDisplayName, shortParentClassName);

			TreeNodeBean nodeBean = new TreeNodeBean(parentDisplayName,
					CaNanoLabConstants.CHARACTERIZATION_ORDER_MAP
							.get(parentDisplayName), new Integer(indentLevel));

			indentLevel++;
			String subclassName = null;
			for (String sclassName : subclassList) {
				String displayName = InitSetup.getInstance().getDisplayName(
						ClassUtils.getShortClassName(sclassName), appContext);
				tempList.add(displayName);

				String shortClassName = ClassUtils
						.getShortClassName(sclassName);
				classNameMap.put(displayName, shortClassName);

				setSubclassMap(appContext, classNameMap, typeMap,
						searchTreeMap, sclassName, indentLevel);
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

	public Map<String, SortedSet<DataLinkBean>> getDataTree(
			ParticleBean particleBean, HttpServletRequest request)
			throws Exception {
		Map<String, SortedSet<DataLinkBean>> dataTree = new HashMap<String, SortedSet<DataLinkBean>>();
		if (request.getAttribute("updateDataTree") != null
				&& request.getAttribute("updateDataTree").equals("true")) {
			ServletContext appContext = request.getSession()
					.getServletContext();
			NanoparticleSample particleSample = particleBean
					.getDomainParticleSample();
			request.getSession().setAttribute("theParticle", particleBean);
			// composition
			if (particleSample.getSampleComposition() != null) {
				SortedSet<DataLinkBean> ndataBeans = new TreeSet<DataLinkBean>(
						new CaNanoLabComparators.DataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getNanoparticleEntityCollection() != null) {
					for (NanoparticleEntity entity : particleSample
							.getSampleComposition()
							.getNanoparticleEntityCollection()) {
						DataLinkBean dataBean = new DataLinkBean(entity.getId()
								.toString(), "Composition",
								"nanoparticleEntity", entity.getCreatedBy(),
								entity.getCreatedDate());
						if (entity instanceof OtherNanoparticleEntity) {
							dataBean
									.setDataDisplayType(((OtherNanoparticleEntity) entity)
											.getType());
							dataBean.setDataClassName("OtherNanoparticleEntity");
						} else {
							dataBean.setDataClassName(ClassUtils
									.getShortClassName(entity.getClass()
											.getCanonicalName()));
							dataBean.setDataDisplayType(InitSetup.getInstance()
									.getDisplayName(
											dataBean.getDataClassName(),
											appContext));
						}
						dataBean.setViewTitle(dataBean.getDataDisplayType());
						ndataBeans.add(dataBean);
					}
				}
				dataTree.put("Nanoparticle Entity", ndataBeans);

				SortedSet<DataLinkBean> fdataBeans = new TreeSet<DataLinkBean>(
						new CaNanoLabComparators.DataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getFunctionalizingEntityCollection() != null) {
					for (FunctionalizingEntity entity : particleSample
							.getSampleComposition()
							.getFunctionalizingEntityCollection()) {
						DataLinkBean dataBean = new DataLinkBean(entity.getId()
								.toString(), "Composition",
								"functionalizingEntity", entity.getCreatedBy(),
								entity.getCreatedDate());
						if (entity instanceof OtherFunctionalizingEntity) {
							dataBean
									.setDataDisplayType(((OtherFunctionalizingEntity) entity)
											.getType());
							dataBean.setDataClassName("OtherFunctionalizingEntity");
						} else {
							dataBean.setDataClassName(ClassUtils
									.getShortClassName(entity.getClass()
											.getCanonicalName()));
							dataBean.setDataDisplayType(InitSetup.getInstance()
									.getDisplayName(
											dataBean.getDataClassName(),
											appContext));
						}
						fdataBeans.add(dataBean);
						dataBean.setViewTitle(dataBean.getDataDisplayType());
					}
				}
				dataTree.put("Functionalizing Entity", fdataBeans);

				SortedSet<DataLinkBean> adataBeans = new TreeSet<DataLinkBean>(
						new CaNanoLabComparators.DataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getChemicalAssociationCollection() != null) {
					for (ChemicalAssociation association : particleSample
							.getSampleComposition()
							.getChemicalAssociationCollection()) {
						DataLinkBean dataBean = new DataLinkBean(association
								.getId().toString(), "Composition",
								"chemicalAssociation", association
										.getCreatedBy(), association
										.getCreatedDate());
						if (association instanceof OtherChemicalAssociation) {
							dataBean
									.setDataDisplayType(((OtherChemicalAssociation) association)
											.getType());
							dataBean.setDataClassName("OtherChemicalAssociation");
						} else {
							dataBean.setDataClassName(ClassUtils
									.getShortClassName(association.getClass()
											.getCanonicalName()));
							dataBean.setDataDisplayType(InitSetup.getInstance()
									.getDisplayName(
											dataBean.getDataClassName(),
											appContext));
						}
						dataBean.setViewTitle(dataBean.getDataDisplayType());
						adataBeans.add(dataBean);
					}
				}
				dataTree.put("Chemical Association", adataBeans);

				SortedSet<DataLinkBean> ldataBeans = new TreeSet<DataLinkBean>(
						new CaNanoLabComparators.DataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getLabFileCollection() != null) {
					for (LabFile file : particleSample.getSampleComposition()
							.getLabFileCollection()) {
						DataLinkBean dataBean = new DataLinkBean(file.getId()
								.toString(), "Composition", "compositionFile",
								file.getCreatedBy(), file.getCreatedDate());
						dataBean.setDataClassName("LabFile");
						dataBean.setDataDisplayType(file.getType());
						
						if(file.getTitle().length() <= 20)
							dataBean.setViewTitle(dataBean.getDataDisplayType() + ": " + file.getTitle());
						else {
							String sideMenuTitle = file.getTitle().substring(0, 20);
							dataBean.setViewTitle(dataBean.getDataDisplayType() + ": " + sideMenuTitle);
						}
						
//						dataBean.setViewTitle(dataBean.getDataDisplayType()
//								+ ": " + file.getName());
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
			SortedSet<DataLinkBean> cdataBeans = null;
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
					DataLinkBean dataBean = new DataLinkBean(achar.getId()
							.toString(), category, link, achar.getCreatedBy(),
							achar.getCreatedDate());
					dataBean.setDataClassName(ClassUtils
							.getShortClassName(achar.getClass()
									.getCanonicalName()));
					String charName = InitSetup.getInstance().getDisplayName(
							dataBean.getDataClassName(), appContext);
					dataBean.setDataDisplayType(charName);
					dataBean.setViewTitle(achar.getIdentificationName());
					if (dataTree.get(charName) != null) {
						cdataBeans = (TreeSet<DataLinkBean>) dataTree
								.get(charName);
					} else {
						cdataBeans = new TreeSet<DataLinkBean>(
								new CaNanoLabComparators.DataLinkTypeDateComparator());
						dataTree.put(charName, cdataBeans);
					}
					cdataBeans.add(dataBean);
				}

				// report
				SortedSet<DataLinkBean> rdataBeans = new TreeSet<DataLinkBean>(
						new CaNanoLabComparators.DataLinkTypeDateComparator());
				if (particleSample.getReportCollection() != null &&
						particleSample.getReportCollection().size() > 0 ) {
					for (Report report : particleSample.getReportCollection()) {
						String reportCategory = report.getCategory();
						DataLinkBean dataBean = new DataLinkBean(report.getId()
								.toString(), "Report", "submitReport", report
								.getCreatedBy(), report.getCreatedDate());
						dataBean.setDataDisplayType(reportCategory);
						if(report.getTitle().length() <= 20)
							dataBean.setViewTitle(report.getTitle());
						else {
							String sideMenuTitle = report.getTitle().substring(0, 20);
							dataBean.setViewTitle(sideMenuTitle);
						}
						//dataBean.setViewTitle(report.getUri());
						if (dataTree.get(reportCategory) != null) {
							rdataBeans = (TreeSet<DataLinkBean>) dataTree
									.get(reportCategory);
						} else {
							rdataBeans = new TreeSet<DataLinkBean>(
									new CaNanoLabComparators.DataLinkTypeDateComparator());
							dataTree.put(reportCategory, rdataBeans);
						}
						rdataBeans.add(dataBean);
					}
					request.getSession().setAttribute("hasReportData", "true");
				} else {
					request.getSession()
							.setAttribute("hasReportData", "false");
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
			dataTree = new HashMap<String, SortedSet<DataLinkBean>>(
					(Map<? extends String, SortedSet<DataLinkBean>>) (request
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
		LabelValueBean trueBean = new LabelValueBean();
		trueBean.setLabel(CaNanoLabConstants.BOOLEAN_YES);
		trueBean.setValue("true");
		LabelValueBean falseBean = new LabelValueBean();
		falseBean.setLabel(CaNanoLabConstants.BOOLEAN_NO);
		falseBean.setValue("false");
		LabelValueBean[] booleanBeans = new LabelValueBean[2];
		booleanBeans[0] = trueBean;
		booleanBeans[1] = falseBean;

		appContext.setAttribute("booleanChoices", booleanBeans);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "LabFile", "type", "otherType", true);
	}
}
