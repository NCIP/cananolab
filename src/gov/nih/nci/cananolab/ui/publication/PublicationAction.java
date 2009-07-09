package gov.nih.nci.cananolab.ui.publication;

/**
 * This class submits publication and assigns visibility
 *
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.sample.InitSampleSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PublicationAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		PublicationForm theForm = (PublicationForm) form;
		PublicationBean publicationBean = (PublicationBean) theForm
				.get("publication");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		publicationBean.setupDomain(Constants.FOLDER_PUBLICATION, user
				.getLoginName(), 0);
		PublicationService service = new PublicationServiceLocalImpl();
		service.savePublication(publicationBean, user);
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		authService.assignVisibility(publicationBean.getDomainFile().getId()
				.toString(), publicationBean.getVisibilityGroups(), null);

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
					.findSampleById(sampleId, user);
			sampleBean.setLocation(Constants.LOCAL_SITE);
			forward = mapping.findForward("sampleSuccess");
		}
		// session.removeAttribute("sampleId");
		// to preselect the same characterization type after returning to the
		// summary page
		SortedSet<String> publicationCategories = InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request,
						"publicationCategories", "Publication", "category",
						"otherCategory", true);
		List<String> allPublicationTypes = new ArrayList<String>(
				publicationCategories);
		int ind = allPublicationTypes.indexOf(((Publication) publicationBean
				.getDomainFile()).getCategory()) + 1;
		request.getSession().setAttribute(
				"onloadJavascript",
				"showSummary('" + ind + "', " + allPublicationTypes.size()
						+ ")");
		return forward;
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationBean pubBean = new PublicationBean();
		String sampleId = request.getParameter("sampleId");
		PublicationForm theForm = (PublicationForm) form;
		theForm.set("sampleId", sampleId);
		String type = request.getParameter("type");
		if (type != null) {
			((Publication) pubBean.getDomainFile()).setCategory(type);
		}
		theForm.set("publication", pubBean);
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ActionForward forward = mapping.getInputForward();
		if (sampleId != null && sampleId.trim().length() > 0) {
			InitSampleSetup.getInstance()
					.getOtherSampleNames(request, sampleId);
			forward = mapping.findForward("sampleSubmitPublication");
		} else {
			InitSampleSetup.getInstance().getAllSampleNames(request, user);
		}
		request.getSession().setAttribute(
				"onloadJavascript",
				"updateFormBasedOnCategory()");
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationForm theForm = (PublicationForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String publicationId = request.getParameter("publicationId");
		PublicationService publicationService = new PublicationServiceLocalImpl();
		PublicationBean pubBean = publicationService.findPublicationById(
				publicationId, user);
		theForm.set("publication", pubBean);
		String sampleId = request.getParameter("sampleId");
		theForm.set("sampleId", sampleId);
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		request.getSession().setAttribute(
				"onloadJavascript",
				"updateFormBasedOnCategory();fillPubMedInfo();");
		if (sampleId != null && sampleId.trim().length() > 0) {
			InitSampleSetup.getInstance()
					.getOtherSampleNames(request, sampleId);
			return mapping.findForward("sampleSubmitPublication");
		} else {
			InitSampleSetup.getInstance().getAllSampleNames(request, user);
			return mapping.findForward("publicationSubmitPublication");
		}
	}

	// if sampleId is available direct to particle specific page
	// Publication pub = (Publication) publicationBean.getDomainFile();
	// Long pubMedId = pub.getPubMedId();
	// ActionForward forward = this.getReturnForward(mapping, sampleId,
	// pubMedId);
	// }

	// private ActionForward getReturnForward(ActionMapping mapping,
	// String sampleId, Long pubMedId) {
	// ActionForward forward = null;
	// if (StringUtils.isEmpty(sampleId)) {
	// if (pubMedId != null && pubMedId > 0) {
	// forward =
	// mapping.findForward("publicationSubmitPubmedPublication");
	// } else {
	// forward = mapping.findForward("publicationSubmitPublication");
	// }
	// // request.removeAttribute("sampleId");
	// } else {
	// if (pubMedId != null && pubMedId > 0) {
	// forward = mapping.findForward("sampleSubmitPubmedPublication");
	// } else {
	// forward = mapping.findForward("sampleSubmitPublication");
	// }
	// // request.setAttribute("sampleId", sampleId);
	// }
	// return forward;
	// }

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationForm theForm = (PublicationForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String publicationId = request.getParameter("fileId");
		String location = request.getParameter(Constants.LOCATION);
		PublicationService publicationService = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}
		PublicationBean publicationBean = publicationService
				.findPublicationById(publicationId, user);
		theForm.set("publication", publicationBean);
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		// if sampleId is available direct to particle specific page
		ActionForward forward = mapping.findForward("view");
		String sampleId = request.getParameter("sampleId");
		if (sampleId != null) {
			forward = mapping.findForward("sampleViewPublication");
		}
		return forward;
	}

	/**
	 * Handle summary report print request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);

		// Marker that indicates page is for printing (hide tabs, links, etc).
		request.setAttribute("printView", Boolean.TRUE);

		PublicationSummaryViewBean summaryBean = (PublicationSummaryViewBean) request
				.getAttribute("publicationSummaryView");

		// Filter out categories that not selected.
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			SortedMap<String, List<PublicationBean>> categories = summaryBean
					.getCategory2Publications();
			List<PublicationBean> pubs = categories.get(type);
			if (pubs != null) {
				categories.clear();
				categories.put(type, pubs);
				summaryBean.setPublicationCategories(categories.keySet());
			}
		}

		return mapping.findForward("summaryPrint");
	}

	/**
	 * Handle summary report view request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);

		// "actionName" is for constructing the Print/Export URL.
		request.setAttribute("actionName", request.getRequestURL().toString());

		return mapping.findForward("summaryView");
	}

	/**
	 * Handle summary report edit request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// if session is expired or the url is clicked on directly
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null) {
			return summaryView(mapping, form, request, response);
		}
		this.prepareSummary(mapping, form, request, response);

		// "actionName" is for constructing the Print/Export URL.
		request.setAttribute("actionName", request.getRequestURL().toString());

		return mapping.findForward("summaryEdit");
	}

	/**
	 * Handle summary report export request.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryExport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		prepareSummary(mapping, form, request, response);

		PublicationSummaryViewBean summaryBean = (PublicationSummaryViewBean) request
				.getAttribute("publicationSummaryView");

		// Filter out categories that not selected.
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			SortedMap<String, List<PublicationBean>> categories = summaryBean
					.getCategory2Publications();
			List<PublicationBean> pubs = categories.get(type);
			if (pubs != null) {
				categories.clear();
				categories.put(type, pubs);
				summaryBean.setPublicationCategories(categories.keySet());
			}
		}

		// Get sample name for constructing file name.
		PublicationForm theForm = (PublicationForm) form;
		String location = request.getParameter(Constants.LOCATION);
		SampleBean sampleBean = setupSample(theForm, request, location);

		String fileName = ExportUtils.getExportFileName(sampleBean.getDomain()
				.getName(), "PublicationSummaryView", type);
		ExportUtils.prepareReponseForExcell(response, fileName);
		PublicationServiceHelper.exportSummary(summaryBean, response
				.getOutputStream());

		return null;
	}

	/**
	 * Shared function for summaryView(), summaryEdit(), summaryExport() and
	 * summaryPrint().
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	private void prepareSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationForm theForm = (PublicationForm) form;
		String sampleId = theForm.getString("sampleId");
		String location = theForm.getString(Constants.LOCATION);
		setupSample(theForm, request, location);
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"publicationCategories", "Publication", "category",
				"otherCategory", true);
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		PublicationService publicationService = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}
		List<PublicationBean> publications = publicationService
				.findPublicationsBySampleId(sampleId, user);
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				publications);
		request.setAttribute("publicationSummaryView", summaryView);

		if (request.getParameter("clearTab") != null
				&& request.getParameter("clearTab").equals("true")) {
			request.getSession().removeAttribute("onloadJavascript");
		}
	}

	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String location = request.getParameter(Constants.LOCATION);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PublicationService publicationService = null;
		if (location.equals(Constants.LOCAL_SITE)) {
			publicationService = new PublicationServiceLocalImpl();
		}
		// else {
		// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
		// request, location);
		// publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		// }
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(
				publicationId, user);
		PublicationForm theForm = (PublicationForm) form;
		theForm.set("publication", pubBean);
		return mapping.findForward("publicationDetailPrintView");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// save new entered other types
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		PublicationForm theForm = (PublicationForm) form;

		PublicationBean publicationBean = (PublicationBean) theForm
				.get("publication");
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
		theForm.set("publication", publicationBean);

		return mapping.findForward("publicationSubmitPublication");

		// if pubMedId is available, the related fields should be set to read
		// only.
		// HttpSession session = request.getSession();
		// String sampleId = (String) session.getAttribute("docSampleId");
		// Publication pub = (Publication) publicationBean.getDomainFile();
		// Long pubMedId = pub.getPubMedId();
		// ActionForward forward = getReturnForward(mapping, sampleId,
		// pubMedId);
		// return forward;
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter(Constants.LOCATION);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PublicationService publicationService = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(
				publicationId, user);
		PublicationForm theForm = (PublicationForm) form;
		theForm.set("publication", pubBean);

		ActionForward forward = null;
		String sampleId = request.getParameter("sampleId");
		if (StringUtils.isEmpty(sampleId)) {
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
		PublicationBean pbean = (PublicationBean) theForm.get("publication");
		pbean.addAuthor();

		return mapping.getInputForward();
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
		String location = request.getParameter(Constants.LOCATION);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PublicationService publicationService = null;
		if (location.equals(Constants.LOCAL_SITE)) {
			publicationService = new PublicationServiceLocalImpl();
		}
		// else {
		// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
		// request, location);
		// publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		// }
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(
				publicationId, user);
		PublicationForm theForm = (PublicationForm) form;
		theForm.set("publication", pubBean);
		String title = pubBean.getDomainFile().getTitle();
		if (title != null && title.length() > 10) {
			title = title.substring(0, 10);
		}

		String fileName = this.getExportFileName(title, "detailView");
		ExportUtils.prepareReponseForExcell(response, fileName);
		PublicationServiceHelper.exportDetail(pubBean, response
				.getOutputStream());

		return null;
	}

	private String getExportFileName(String titleName, String viewType) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(titleName);
		nameParts.add("Publication");
		nameParts.add(viewType);
		nameParts.add(DateUtils.convertDateToString(Calendar.getInstance()
				.getTime()));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}
}
