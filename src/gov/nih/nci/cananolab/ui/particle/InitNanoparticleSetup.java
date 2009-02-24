package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.characterization.physical.PhysicoChemicalCharacterization;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.DataLinkBean;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
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

	public void setLocalSearchDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getReflectionDefaultAndOtherLookupTypes(
				request, "defaultFunctionTypes", "functionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function",
				"gov.nih.nci.cananolab.domain.function.OtherFunction", true);
		InitSetup
				.getInstance()
				.getReflectionDefaultAndOtherLookupTypes(
						request,
						"defaultNanoparticleEntityTypes",
						"nanoparticleEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.NanoparticleEntity",
						"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanoparticleEntity",
						true);
		InitSetup
				.getInstance()
				.getReflectionDefaultAndOtherLookupTypes(
						request,
						"defaultFunctionalizingEntityTypes",
						"functionalizingEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity",
						"gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity",
						true);
		InitCharacterizationSetup.getInstance().getCharacterizationTypes(
				request);
	}

	public void setRemoteSearchDropdowns(HttpServletRequest request)
			throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		InitSetup.getInstance().getServletContextDefaultTypesByReflection(
				appContext, "defaultFunctionalizingEntityTypes",
				"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
		InitSetup.getInstance().getServletContextDefaultTypesByReflection(
				appContext, "defaultNanoparticleEntityTypes",
				"gov.nih.nci.cananolab.domain.particle.NanoparticleEntity");
		InitSetup.getInstance().getServletContextDefaultTypesByReflection(
				appContext, "defaultFunctionTypes",
				"gov.nih.nci.cananolab.domain.particle.Function");

		InitCharacterizationSetup.getInstance().getCharacterizationTypes(
				request);
	}

	public SortedSet<PointOfContactBean> getAllPointOfContacts(
			HttpServletRequest request) throws Exception {
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		SortedSet<PointOfContact> pointOfContacts = pocService
				.findAllPointOfContacts();
		SortedSet<PointOfContactBean> pointOfContactBeans = null;
		if (pointOfContacts != null && pointOfContacts.size() > 0) {
			pointOfContactBeans = new TreeSet<PointOfContactBean>(
					new CaNanoLabComparators.ParticlePointOfContactBeanComparator());
			for (PointOfContact poc : pointOfContacts) {
				pointOfContactBeans.add(new PointOfContactBean(poc));
			}
		}
		request.getSession().setAttribute("allPointOfContacts",
				pointOfContactBeans);
		return pointOfContactBeans;
	}

	public SortedSet<String> getAllNanoparticleSampleNames(
			HttpServletRequest request, UserBean user) throws Exception {
		SortedSet<String> sampleNames = particleService
				.findAllNanoparticleSampleNames(user);
		request.getSession().setAttribute("allUserParticleNames", sampleNames);
		return sampleNames;
	}

	public SortedSet<String> getAllParticleNames(HttpServletRequest request)
			throws Exception {
		SortedSet<String> sampleNames = particleService.findAllParticleNames();
		request.getSession().setAttribute("allParticleNames", sampleNames);
		return sampleNames;
	}

	public Map<String, SortedSet<DataLinkBean>> getDataTree(
			ParticleBean particleBean, HttpServletRequest request)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
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
							dataBean
									.setDataClassName("OtherNanoparticleEntity");
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
							dataBean
									.setDataClassName("OtherFunctionalizingEntity");
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
							dataBean
									.setDataClassName("OtherChemicalAssociation");
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
				if (particleSample.getSampleComposition().getFileCollection() != null) {
					for (File file : particleSample.getSampleComposition()
							.getFileCollection()) {
						DataLinkBean dataBean = new DataLinkBean(file.getId()
								.toString(), "Composition", "compositionFile",
								file.getCreatedBy(), file.getCreatedDate());
						dataBean.setDataClassName("File");
						dataBean.setDataDisplayType(file.getType());

						if (file.getTitle().length() <= 20)
							dataBean.setViewTitle(dataBean.getDataDisplayType()
									+ ": " + file.getTitle());
						else {
							String sideMenuTitle = file.getTitle().substring(0,
									20);
							dataBean.setViewTitle(dataBean.getDataDisplayType()
									+ ": " + sideMenuTitle);
						}

						// dataBean.setViewTitle(dataBean.getDataDisplayType()
						// + ": " + file.getName());
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
					if (achar instanceof PhysicoChemicalCharacterization) {
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
				boolean hasPublicationData = false;

				// publication
				SortedSet<DataLinkBean> pdataBeans = new TreeSet<DataLinkBean>(
						new CaNanoLabComparators.DataLinkTypeDateComparator());
				Collection<Publication> publicationCollection = particleSample
						.getPublicationCollection();
				if (publicationCollection != null
						&& publicationCollection.size() > 0) {
					Long pubmedid = 0L;
					String doi = null, title = null;
					FileService fileService = new FileServiceLocalImpl();
					for (Publication publication : publicationCollection) {
						PublicationBean pubBean = new PublicationBean(
								publication);
						fileService.retrieveVisibility(pubBean, user);
						if (!pubBean.isHidden()) {
							String publicationCategory = publication
									.getCategory();
							DataLinkBean dataBean = new DataLinkBean(
									publication.getId().toString(),
									"Publication", "publication", publication
											.getCreatedBy(), publication
											.getCreatedDate());
							dataBean.setDataDisplayType(publicationCategory);
							pubmedid = publication.getPubMedId();

							if (pubmedid != null && pubmedid > 0) {
								dataBean.setViewTitle("PMID: " + pubmedid);
							} else {
								doi = publication.getDigitalObjectId();
								if (doi != null && doi.length() > 0) {
									if (doi.length() > 20) {
										dataBean.setViewTitle("DOI: "
												+ doi.substring(0, 20));
									} else {
										dataBean.setViewTitle("DOI: " + doi);
									}
								} else {
									title = publication.getTitle();
									if (title != null && title.length() > 0) {
										String type = publication.getCategory();
										if (title.length() > 20) {
											dataBean.setViewTitle(type + ": "
													+ title.substring(0, 20));
										} else {
											dataBean.setViewTitle(type + ": "
													+ title);
										}
									}
								}
							}
							// dataBean.setViewTitle(report.getUri());
							if (dataTree
									.get(CaNanoLabConstants.FOLDER_PUBLICATION) != null) {
								pdataBeans = (TreeSet<DataLinkBean>) dataTree
										.get(CaNanoLabConstants.FOLDER_PUBLICATION);
							} else {
								pdataBeans = new TreeSet<DataLinkBean>(
										new CaNanoLabComparators.DataLinkTypeDateComparator());
								dataTree.put(
										CaNanoLabConstants.FOLDER_PUBLICATION,
										pdataBeans);
							}
							pdataBeans.add(dataBean);
						}
						hasPublicationData = true;

					}
				}
				if (hasPublicationData) {
					request.getSession().setAttribute("hasPublicationData",
							"true");
				} else {
					request.getSession().setAttribute("hasPublicationData",
							"false");
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
			String particleOrganization, UserBean user) throws Exception {
		SortedSet<SortableName> names = particleService.findOtherParticles(
				particleOrganization, particleName, user);
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
				"fileTypes", "File", "type", "otherType", true);
	}
}
