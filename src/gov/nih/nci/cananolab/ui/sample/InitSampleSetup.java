package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.characterization.physical.PhysicoChemicalCharacterization;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
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
 * This class sets up information required for canano forms.
 *
 * @author pansu, cais
 *
 */
public class InitSampleSetup {
	private InitSampleSetup() {
	}

	private SampleService sampleService = new SampleServiceLocalImpl();

	public static InitSampleSetup getInstance() {
		return new InitSampleSetup();
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
						"defaultNanomaterialEntityTypes",
						"nanomaterialEntityTypes",
						"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity",
						"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity",
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
				appContext, "defaultNanomaterialEntityTypes",
				"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
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
					new Comparators.SamplePointOfContactBeanComparator());
			for (PointOfContact poc : pointOfContacts) {
				pointOfContactBeans.add(new PointOfContactBean(poc));
			}
		}
		request.getSession().setAttribute("allPointOfContacts",
				pointOfContactBeans);
		return pointOfContactBeans;
	}

	public SortedSet<String> getAllSampleNames(
			HttpServletRequest request, UserBean user) throws Exception {
		SortedSet<String> sampleNames = sampleService
				.findAllSampleNames(user);
		request.getSession().setAttribute("allUserSampleNames", sampleNames);
		return sampleNames;
	}

	public SortedSet<String> getAllSampleNames(HttpServletRequest request)
			throws Exception {
		SortedSet<String> sampleNames = sampleService.findAllSampleNames();
		request.getSession().setAttribute("allSampleNames", sampleNames);
		return sampleNames;
	}

	public Map<String, SortedSet<DataLinkBean>> getDataTree(
			SampleBean sampleBean, HttpServletRequest request)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Map<String, SortedSet<DataLinkBean>> dataTree = new HashMap<String, SortedSet<DataLinkBean>>();
		if (request.getAttribute("updateDataTree") != null
				&& request.getAttribute("updateDataTree").equals("true")) {
			ServletContext appContext = request.getSession()
					.getServletContext();
			Sample particleSample = sampleBean
					.getDomain();
			request.getSession().setAttribute("theSample", sampleBean);
			// composition
			if (particleSample.getSampleComposition() != null) {
				SortedSet<DataLinkBean> ndataBeans = new TreeSet<DataLinkBean>(
						new Comparators.DataLinkTypeDateComparator());
				if (particleSample.getSampleComposition()
						.getNanomaterialEntityCollection() != null) {
					for (NanomaterialEntity entity : particleSample
							.getSampleComposition()
							.getNanomaterialEntityCollection()) {
						DataLinkBean dataBean = new DataLinkBean(entity.getId()
								.toString(), "Composition",
								"nanomaterialEntity", entity.getCreatedBy(),
								entity.getCreatedDate());
						if (entity instanceof OtherNanomaterialEntity) {
							dataBean
									.setDataDisplayType(((OtherNanomaterialEntity) entity)
											.getType());
							dataBean
									.setDataClassName("OtherNanomaterialEntity");
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
				dataTree.put("Nanomaterial Entity", ndataBeans);

				SortedSet<DataLinkBean> fdataBeans = new TreeSet<DataLinkBean>(
						new Comparators.DataLinkTypeDateComparator());
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
						new Comparators.DataLinkTypeDateComparator());
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
						new Comparators.DataLinkTypeDateComparator());
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
								new Comparators.DataLinkTypeDateComparator());
						dataTree.put(charName, cdataBeans);
					}
					cdataBeans.add(dataBean);
				}
				boolean hasPublicationData = false;

				// publication
				SortedSet<DataLinkBean> pdataBeans = new TreeSet<DataLinkBean>(
						new Comparators.DataLinkTypeDateComparator());
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
									.get(Constants.FOLDER_PUBLICATION) != null) {
								pdataBeans = (TreeSet<DataLinkBean>) dataTree
										.get(Constants.FOLDER_PUBLICATION);
							} else {
								pdataBeans = new TreeSet<DataLinkBean>(
										new Comparators.DataLinkTypeDateComparator());
								dataTree.put(
										Constants.FOLDER_PUBLICATION,
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
			request.getSession().setAttribute("sampleDataTree", dataTree);

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
							.getSession().getAttribute("sampleDataTree")));
		}
		return dataTree;
	}

	public SortedSet<SortableName> getOtherSampleNames(
			HttpServletRequest request, String sampleId) throws Exception {
		SortedSet<SortableName> names = sampleService
				.findOtherSamplesFromSamePointOfContact(sampleId);
		request.getSession().setAttribute("otherSampleNames", names);
		return names;
	}

	public void setSharedDropdowns(HttpServletRequest request) throws Exception {
		// set static boolean yes or no and characterization source choices
		ServletContext appContext = request.getSession().getServletContext();
		LabelValueBean trueBean = new LabelValueBean();
		trueBean.setLabel(Constants.BOOLEAN_YES);
		trueBean.setValue("true");
		LabelValueBean falseBean = new LabelValueBean();
		falseBean.setLabel(Constants.BOOLEAN_NO);
		falseBean.setValue("false");
		LabelValueBean[] booleanBeans = new LabelValueBean[2];
		booleanBeans[0] = trueBean;
		booleanBeans[1] = falseBean;

		appContext.setAttribute("booleanChoices", booleanBeans);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"fileTypes", "File", "type", "otherType", true);
	}
}
