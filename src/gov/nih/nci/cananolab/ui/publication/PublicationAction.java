package gov.nih.nci.cananolab.ui.publication;

/**
 * This class submits publication and assigns visibility
 *
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PubMedXMLHandler;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.sample.InitSampleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DataLinkBean;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class PublicationAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		PublicationForm theForm = (PublicationForm) form;

		PublicationBean publicationBean = (PublicationBean) theForm.get("file");
		String[] researchAreas = publicationBean.getResearchAreas();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		publicationBean.setupDomainFile(Constants.FOLDER_PUBLICATION,
				user.getLoginName(), 0);
		String researchAreasStr = null;
		if (researchAreas != null && researchAreas.length > 0) {
			researchAreasStr = StringUtils.join(researchAreas, ";");
		}
		Publication publication = (Publication) publicationBean.getDomainFile();
		change0ToNull(publication);
		publication.setResearchArea(researchAreasStr);

		if (publicationBean.getAuthors() != null
				&& publicationBean.getAuthors().size() > 0) {
			for (Author author : publicationBean.getAuthors()) {
				if (author.getCreatedBy() == null
						|| author.getCreatedBy().trim().length() == 0) {
					author.setCreatedBy(user.getLoginName());
				}
			}
		}
		PublicationService service = new PublicationServiceLocalImpl();
		service.savePublication(publication,
				publicationBean.getSampleNames(), publicationBean
						.getNewFileData(), publicationBean.getAuthors());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		authService.assignVisibility(publicationBean.getDomainFile().getId()
				.toString(), publicationBean.getVisibilityGroups(), null);

		// set author visibility
		if (publicationBean.getVisibilityGroups() != null
				&& Arrays.asList(publicationBean.getVisibilityGroups())
						.contains(Constants.CSM_PUBLIC_GROUP)) {
			if (publication.getAuthorCollection() != null) {
				for (Author author : publication.getAuthorCollection()) {
					if (author != null) {
						if (author.getId() != null
								&& author.getId().toString().trim().length() > 0) {
							authService.assignPublicVisibility(author.getId()
									.toString());
						}

					}
				}
			}
		}
		InitPublicationSetup.getInstance().persistPublicationDropdowns(request,
				publicationBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitPublication.file",
				publicationBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		HttpSession session = request.getSession();
		String sampleId = (String) session.getAttribute("docSampleId");
		// String sampleId = request.getParameter("sampleId");
		// if (sampleId != null) {
		// session.setAttribute("docSampleId", sampleId);
		// }else {
		// //if it is not calling from particle, remove previous set attribute
		// if applicable
		// session.removeAttribute("docSampleId");
		// }

		// if (sampleId==null ||sampleId.length()==0) {
		// Object sampleIdObj = session.getAttribute("sampleId");
		// if (sampleIdObj!=null) {
		// sampleId = sampleIdObj.toString();
		// request.setAttribute("sampleId", sampleId);
		// }else {
		// request.removeAttribute("sampleId");
		// }
		// }
		if (sampleId != null && sampleId.length() > 0) {
			SampleService sampleService = new SampleServiceLocalImpl();
			SampleBean sampleBean = sampleService
					.findSampleById(sampleId);
			sampleBean.setLocation("local");
			setupDataTree(sampleBean, request);
			forward = mapping.findForward("sampleSuccess");
		}
		// session.removeAttribute("sampleId");
		return forward;
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("publicationForm");
		String sampleId = request.getParameter("sampleId");
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitSampleSetup.getInstance().getAllSampleNames(request);
		if (sampleId != null && sampleId.trim().length() > 0
				&& session.getAttribute("otherSampleNames") == null) {
			InitSampleSetup.getInstance().getOtherSampleNames(request,
					sampleId);
		}
		ActionForward forward = mapping.getInputForward();

		if (sampleId != null && !sampleId.equals("null")
				&& sampleId.trim().length() > 0) {
			forward = mapping.findForward("sampleSubmitPublication");
			session.setAttribute("docSampleId", sampleId);
		} else {
			session.removeAttribute("docSampleId");
		}
		return forward;
	}

	public ActionForward setupReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();

		String sampleId = request.getParameter("sampleId");
		ActionForward forward = null;

		if (sampleId != null && !sampleId.equals("null")
				&& sampleId.trim().length() > 0) {
			// forward = mapping.findForward("sampleSubmitReport");
			forward = mapping.findForward("sampleSubmitPublication");
			session.setAttribute("docSampleId", sampleId);
		} else {
			session.removeAttribute("docSampleId");
			forward = mapping.findForward("publicationSubmitPublication");
			// forward = mapping.findForward("publicationSubmitReport");
		}

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		// PublicationBean publicationBean = ((PublicationBean)
		// theForm.get("file"));
		PublicationBean publicationBean = new PublicationBean();
		Publication pub = (Publication) publicationBean.getDomainFile();
		pub.setStatus("published");
		pub.setCategory("report");
		publicationBean.setDomainFile(pub);
		theForm.set("file", publicationBean);
		return forward;
	}

	public ActionForward setupPubmed(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationForm theForm = (PublicationForm) form;
		PublicationBean pbean = (PublicationBean) theForm.get("file");
		pbean.setFoundPubMedArticle(false);

		PubMedXMLHandler phandler = PubMedXMLHandler.getInstance();
		String pubmedID = request.getParameter("pubmedId");
		String sampleId = request.getParameter("sampleId");
		HttpSession session = request.getSession();
		ActionForward forward = null;
		if (sampleId != null && sampleId.trim().length() > 0) {
			forward = mapping.findForward("sampleSubmitPublication");
			session.setAttribute("docSampleId", sampleId);
		} else {
			forward = mapping.findForward("publicationSubmitPublication");
			session.removeAttribute("docSampleId");
		}

		// clear publication data fields
		Publication publication = (Publication) pbean.getDomainFile();
		publication.setTitle("");
		publication.setDigitalObjectId("");
		publication.setJournalName("");
		publication.setStartPage(null);
		publication.setEndPage(null);
		publication.setYear(null);
		publication.setVolume("");
		List<Author> authors = new ArrayList<Author>();
		authors.add(new Author());
		pbean.setAuthors(authors);

		if (pubmedID != null && pubmedID.length() > 0 && !pubmedID.equals("0")) {
			Long pubMedIDLong = 0L;
			try {
				pubMedIDLong = Long.valueOf(pubmedID);
			} catch (Exception ex) {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.submitPublication.invalidPubmedId");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveErrors(request, msgs);
				return forward;
			}
			phandler.parsePubMedXML(pubMedIDLong, pbean);
			if (!pbean.isFoundPubMedArticle()) {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.submitPublication.pubmedArticleNotFound",
						pubmedID);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return forward;
			} else {
				change0ToNull(publication);
			}
			theForm.set("file", pbean);
			if (sampleId != null && sampleId.length() > 0) {
				forward = mapping
						.findForward("sampleSubmitPubmedPublication");
			} else {
				forward = mapping
						.findForward("publicationSubmitPubmedPublication");
			}
		} else {
			publication.setPubMedId(null);
			theForm.set("file", pbean);
		}
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String sampleId = request.getParameter("sampleId");
		PublicationForm theForm = (PublicationForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (sampleId != null && sampleId.trim().length() > 0) {
			session.setAttribute("docSampleId", sampleId);
			// set up other particles with the same primary point of contact
			InitSampleSetup.getInstance().getOtherSampleNames(request,
					sampleId);
		} else {
			session.removeAttribute("docSampleId");
		}
		String publicationId = request.getParameter("publicationId");

		PublicationService publicationService = new PublicationServiceLocalImpl();
		PublicationBean publicationBean = publicationService
				.findPublicationById(publicationId);
		FileService fileService = new FileServiceLocalImpl();
		fileService.retrieveVisibility(publicationBean, user);
		theForm.set("file", publicationBean);
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		// if sampleId is available direct to particle specific page
		Publication pub = (Publication) publicationBean.getDomainFile();
		Long pubMedId = pub.getPubMedId();
		ActionForward forward = getReturnForward(mapping, sampleId, pubMedId);

		return forward;
	}

	private ActionForward getReturnForward(ActionMapping mapping,
			String sampleId, Long pubMedId) {
		ActionForward forward = null;
		if (sampleId != null && sampleId.trim().length() > 0) {
			if (pubMedId != null && pubMedId > 0) {
				forward = mapping
						.findForward("sampleSubmitPubmedPublication");
			} else {
				forward = mapping.findForward("sampleSubmitPublication");
			}
			// request.setAttribute("sampleId", sampleId);
		} else {
			if (pubMedId != null && pubMedId > 0) {
				forward = mapping
						.findForward("publicationSubmitPubmedPublication");
			} else {
				forward = mapping.findForward("publicationSubmitPublication");
			}
			// request.removeAttribute("sampleId");
		}
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationForm theForm = (PublicationForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String publicationId = request.getParameter("fileId");
		String location = request.getParameter("location");
		PublicationService publicationService = null;
		if (location.equals("local")) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}
		PublicationBean publicationBean = publicationService
				.findPublicationById(publicationId);
		this.checkVisibility(request, location, user, publicationBean);
		theForm.set("file", publicationBean);
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		// if sampleId is available direct to particle specific page
		String sampleId = request.getParameter("sampleId");
		ActionForward forward = mapping.findForward("view");
		if (sampleId != null) {
			forward = mapping.findForward("sampleViewPublication");
		}
		return forward;
	}

	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationForm theForm = (PublicationForm) form;
		String sampleId = request.getParameter("sampleId");
		String submitType = request.getParameter("submitType");
		String[] dataIds = (String[]) theForm.get("idsToDelete");
		PublicationService publicationService = new PublicationServiceLocalImpl();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		Sample particle = (Sample) appService
				.getObject(Sample.class, "id", new Long(sampleId));

		ActionMessages msgs = new ActionMessages();
		for (String id : dataIds) {
			if (!checkDelete(request, msgs, id)) {
				return mapping.findForward("annotationDeleteView");
			}
			publicationService.removePublicationFromSample(particle,
					new Long(id));
		}
		SampleBean sampleBean = setupSample(theForm, request, "local");
		setupDataTree(sampleBean, request);
		ActionMessage msg = new ActionMessage("message.deletePublications",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		if (sampleId != null) {
			return mapping.findForward("sampleSuccess");
		} else {
			return mapping.findForward("success");
		}
	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"publicationCategories", "Publication", "category",
				"otherCategory", true);
		PublicationService publicationService = new PublicationServiceLocalImpl();
		String sampleId = request.getParameter("sampleId");
		List<PublicationBean> publications = publicationService
				.findPublicationsBySampleId(sampleId);
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				publications);
		request.setAttribute("publicationSummaryView", summaryView);

		// TODO fill in detail
		// String location = request.getParameter("location");
		// UserBean user = (UserBean) request.getSession().getAttribute("user");
		// PublicationService publicationService = null;
		// if (location.equals("local")) {
		// publicationService = new PublicationServiceLocalImpl();
		// } else {
		// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
		// request, location);
		// publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		// }
		// String publicationId = request.getParameter("publicationId");
		// PublicationBean pubBean =
		// publicationService.findPublicationById(publicationId);
		// checkVisibility(request, location, user, pubBean);
		// PublicationForm theForm = (PublicationForm) form;
		// theForm.set("file", pubBean);
		//
		// String sampleId = request.getParameter("sampleId");
		// ActionForward forward = null;
		// if(sampleId == null || sampleId.length() == 0) {
		// forward = mapping.findForward("publicationDetailView");
		// } else {
		// forward = mapping.findForward("sampleDetailView");
		// }
		//
		// String submitType = request.getParameter("submitType");
		// String requestUrl = request.getRequestURL().toString();
		// String printLinkURL = requestUrl
		// + "?page=0&dispatch=printDetailView&sampleId=" + sampleId
		// + "&publicationId=" + publicationId +
		// "&submitType=" + submitType + "&location="
		// + location;
		// request.getSession().setAttribute("printDetailViewLinkURL",
		// printLinkURL);

		return mapping.findForward("summaryView");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"publicationCategories", "Publication", "category",
				"otherCategory", true);
		PublicationService publicationService = new PublicationServiceLocalImpl();
		String sampleId = request.getParameter("sampleId");
		List<PublicationBean> publications = publicationService
				.findPublicationsBySampleId(sampleId);
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				publications);
		request.setAttribute("publicationSummaryView", summaryView);
		// TODO fill in detail
		// String location = request.getParameter("location");
		// UserBean user = (UserBean) request.getSession().getAttribute("user");
		// PublicationService publicationService = null;
		// if (location.equals("local")) {
		// publicationService = new PublicationServiceLocalImpl();
		// } else {
		// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
		// request, location);
		// publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		// }
		// String publicationId = request.getParameter("publicationId");
		// PublicationBean pubBean =
		// publicationService.findPublicationById(publicationId);
		// checkVisibility(request, location, user, pubBean);
		// PublicationForm theForm = (PublicationForm) form;
		// theForm.set("file", pubBean);
		//
		// String sampleId = request.getParameter("sampleId");
		// ActionForward forward = null;
		// if(sampleId == null || sampleId.length() == 0) {
		// forward = mapping.findForward("publicationDetailView");
		// } else {
		// forward = mapping.findForward("sampleDetailView");
		// }
		//
		// String submitType = request.getParameter("submitType");
		// String requestUrl = request.getRequestURL().toString();
		// String printLinkURL = requestUrl
		// + "?page=0&dispatch=printDetailView&sampleId=" + sampleId
		// + "&publicationId=" + publicationId +
		// "&submitType=" + submitType + "&location="
		// + location;
		// request.getSession().setAttribute("printDetailViewLinkURL",
		// printLinkURL);

		return mapping.findForward("summaryEdit");
	}

	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String location = request.getParameter("location");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PublicationService publicationService = null;
		if (location.equals("local")) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService
				.findPublicationById(publicationId);
		checkVisibility(request, location, user, pubBean);
		PublicationForm theForm = (PublicationForm) form;
		theForm.set("file", pubBean);
		return mapping.findForward("publicationDetailPrintView");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		String sampleId = (String) session.getAttribute("docSampleId");

		// save new entered other types
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		PublicationForm theForm = (PublicationForm) form;

		PublicationBean publicationBean = ((PublicationBean) theForm
				.get("file"));
		String selectedPublicationType = ((Publication) publicationBean
				.getDomainFile()).getCategory();
		if (selectedPublicationType != null) {
			SortedSet<String> types = (SortedSet<String>) request.getSession()
					.getAttribute("publicationCategories");
			if (types != null) {
				types.add(selectedPublicationType);
				request.getSession().setAttribute("publicationCategories",
						types);
			}
		}
		String selectedPublicationStatus = ((Publication) publicationBean
				.getDomainFile()).getStatus();
		if (selectedPublicationStatus != null) {
			SortedSet<String> statuses = (SortedSet<String>) request
					.getSession().getAttribute("publicationStatuses");
			if (statuses != null) {
				statuses.add(selectedPublicationStatus);
				request.getSession().setAttribute("publicationStatuses",
						statuses);
			}
		}
		Publication pub = (Publication) publicationBean.getDomainFile();
		// remove 0
		change0ToNull(pub);
		theForm.set("file", publicationBean);

		// if pubMedId is available, the related fields should be set to read
		// only.

		Long pubMedId = pub.getPubMedId();
		ActionForward forward = getReturnForward(mapping, sampleId, pubMedId);

		return forward;
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter("location");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PublicationService publicationService = null;
		if (location.equals("local")) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService
				.findPublicationById(publicationId);
		checkVisibility(request, location, user, pubBean);
		PublicationForm theForm = (PublicationForm) form;
		theForm.set("file", pubBean);

		String sampleId = request.getParameter("sampleId");
		ActionForward forward = null;
		if (sampleId == null || sampleId.length() == 0) {
			forward = mapping.findForward("publicationDetailView");
		} else {
			forward = mapping.findForward("sampleDetailView");
		}

		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&sampleId=" + sampleId
				+ "&publicationId=" + publicationId + "&location=" + location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);

		return forward;
	}

	public ActionForward addAuthor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationForm theForm = (PublicationForm) form;
		PublicationBean pbean = (PublicationBean) theForm.get("file");
		pbean.addAuthor();

		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PUBLICATION);
	}

	protected boolean validateResearchAreas(HttpServletRequest request,
			String[] researchAreas) throws Exception {
		ActionMessages msgs = new ActionMessages();
		boolean noErrors = true;
		if (researchAreas == null || researchAreas.length == 0) {
			ActionMessage msg = new ActionMessage(
					"submitPublicationForm.file.researchArea", "researchAreas");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			noErrors = false;
		} else {
			System.out.println("validateResearchAreas =="
					+ Arrays.toString(researchAreas));
		}
		return noErrors;
	}

	public ActionForward exportDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter("location");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PublicationService publicationService = null;
		if (location.equals("local")) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService
				.findPublicationById(publicationId);
		checkVisibility(request, location, user, pubBean);
		PublicationForm theForm = (PublicationForm) form;
		theForm.set("file", pubBean);
		String title = pubBean.getDomainFile().getTitle();
		if (title != null && title.length() > 10) {
			title = title.substring(0, 10);
		}
		String fileName = this.getExportFileName(title, "detailView");
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename=\""
				+ fileName + ".xls\"");

		publicationService.exportDetail(pubBean, response.getOutputStream());
		return null;
	}

	private String getExportFileName(String titleName, String viewType) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(titleName);
		nameParts.add("Publication");
		nameParts.add(viewType);
		nameParts.add(DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}

	private void change0ToNull(Publication pub) {
		if (pub.getPubMedId() != null && pub.getPubMedId() == 0)
			pub.setPubMedId(null);
		if (pub.getYear() != null && pub.getYear() == 0)
			pub.setYear(null);
	}

	public ActionForward setupDeleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String submitType = request.getParameter("submitType");
		PublicationForm theForm = (PublicationForm) form;
		SampleBean sampleBean = setupSample(theForm, request, "local");
		Map<String, SortedSet<DataLinkBean>> dataTree = setupDataTree(
				sampleBean, request);
		SortedSet<DataLinkBean> dataToDelete = dataTree.get(submitType);
		request.getSession().setAttribute("actionName",
				dataToDelete.first().getDataLink());
		request.getSession().setAttribute("dataToDelete", dataToDelete);
		return mapping.findForward("annotationDeleteView");
	}

}
