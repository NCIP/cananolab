package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.sample.InitSampleSetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This class searches canano publication based on user supplied criteria
 * 
 * @author tanq
 */

public class SearchPublicationAction extends BaseAnnotationAction {

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		// get the page number from request
		int displayPage = getDisplayPage(request);

		UserBean user = (UserBean) session.getAttribute("user");
		List<PublicationBean> publicationBeans = null;
		// retrieve from session if it's not null and not the first page
		if (session.getAttribute("publicationSearchResults") != null
				&& displayPage > 0) {
			publicationBeans = new ArrayList<PublicationBean>((List) session
					.getAttribute("publicationSearchResults"));
		} else {
			publicationBeans = queryPublications(form, request);
			if (publicationBeans != null && !publicationBeans.isEmpty()) {
				session.setAttribute("publicationSearchResults",
						publicationBeans);
			} else {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.searchPublication.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
		}
		// load publicationBean details 25 at a time for displaying
		// pass in page and size
		List<PublicationBean> pubBeansPerPage = getPublicationsPerPage(
				publicationBeans, displayPage,
				Constants.DISPLAY_TAG_TABLE_SIZE, request);
		request.setAttribute("publications", pubBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request
				.setAttribute("resultSize",
						new Integer(publicationBeans.size()));
		return mapping.findForward("success");
	}

	private List<PublicationBean> getPublicationsPerPage(
			List<PublicationBean> publicationBeans, int page, int pageSize,
			HttpServletRequest request) throws Exception {
		List<PublicationBean> loadedPublicationBeans = new ArrayList<PublicationBean>();
		PublicationService service = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if (i < publicationBeans.size()) {
				String location = publicationBeans.get(i).getLocation();
				if (location.equals(Constants.LOCAL_SITE)) {
					service = new PublicationServiceLocalImpl();
				} else {
					String serviceUrl = InitSetup.getInstance()
							.getGridServiceUrl(request, location);
					service = new PublicationServiceRemoteImpl(serviceUrl);
				}
				String publicationId = publicationBeans.get(i).getDomainFile()
						.getId().toString();
				PublicationBean pubBean = service.findPublicationById(
						publicationId, user);
				pubBean.setLocation(location);
				loadedPublicationBeans.add(pubBean);
			}
		}
		return loadedPublicationBeans;
	}

	public List<PublicationBean> queryPublications(ActionForm form,
			HttpServletRequest request) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String title = "";
		// publication type
		String category = "";
		String[] keywords = new String[0];
		String pubMedId = "";
		String digitalObjectId = "";
		String[] authors = new String[0];
		String sampleName = "";
		// String[] publicationOrReport = new String[0];
		String[] researchAreas = new String[0];
		String[] nanomaterialEntityTypes = new String[0];
		String[] functionalizingEntityTypes = new String[0];
		String[] functionTypes = new String[0];
		String[] searchLocations = new String[0];

		title = (String) theForm.get("title");
		category = (String) theForm.get("category");
		String keywordsStr = (String) theForm.get("keywordsStr");
		List<String> wordList = StringUtils.parseToWords(keywordsStr);
		if (wordList != null) {
			keywords = new String[wordList.size()];
			wordList.toArray(keywords);
		}
		pubMedId = (String) theForm.get("pubMedId");
		digitalObjectId = (String) theForm.get("digitalObjectId");
		String authorsStr = (String) theForm.get("authorsStr");
		List<String> authorList = StringUtils.parseToWords(authorsStr);
		if (authorList != null) {
			authors = new String[authorList.size()];
			authorList.toArray(authors);
		}
		sampleName = (String) theForm.get("sampleName");

		researchAreas = (String[]) theForm.get("researchArea");
		// publicationOrReport = (String[]) theForm
		// .get("publicationOrReport");
		nanomaterialEntityTypes = (String[]) theForm
				.get("nanomaterialEntityTypes");
		functionalizingEntityTypes = (String[]) theForm
				.get("functionalizingEntityTypes");
		functionTypes = (String[]) theForm.get("functionTypes");
		searchLocations = (String[]) theForm.get("searchLocations");
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (searchLocations[0].indexOf("~") != -1 && gridNodeHostStr != null
				&& gridNodeHostStr.trim().length() > 0) {
			searchLocations = gridNodeHostStr.split("~");
		}

		List<String> nanomaterialEntityClassNames = new ArrayList<String>();
		List<String> otherNanomaterialEntityTypes = new ArrayList<String>();
		for (int i = 0; i < nanomaterialEntityTypes.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(nanomaterialEntityTypes[i]);
			if (className.length() == 0) {
				className = "OtherNanomaterialEntity";
				otherNanomaterialEntityTypes.add(nanomaterialEntityTypes[i]);
			} else {
				nanomaterialEntityClassNames.add(className);
			}
		}
		List<String> functionalizingEntityClassNames = new ArrayList<String>();
		List<String> otherFunctionalizingTypes = new ArrayList<String>();
		for (int i = 0; i < functionalizingEntityTypes.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(functionalizingEntityTypes[i]);
			if (className.length() == 0) {
				className = "OtherFunctionalizingEntity";
				otherFunctionalizingTypes.add(functionalizingEntityTypes[i]);
			} else {
				functionalizingEntityClassNames.add(className);
			}
		}

		List<String> functionClassNames = new ArrayList<String>();
		List<String> otherFunctionTypes = new ArrayList<String>();
		if (functionTypes != null) {
			for (int i = 0; i < functionTypes.length; i++) {
				String className = ClassUtils
						.getShortClassNameFromDisplayName(functionTypes[i]);
				if (className.length() == 0) {
					className = "OtherFunction";
					otherFunctionTypes.add(functionTypes[i]);
				} else {
					functionClassNames.add(className);
				}
			}
		}

		// Publication
		List<PublicationBean> publications = new ArrayList<PublicationBean>();
		PublicationService publicationService = null;
		for (String location : searchLocations) {
			if (Constants.LOCAL_SITE.equals(location)) {
				publicationService = new PublicationServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				publicationService = new PublicationServiceRemoteImpl(
						serviceUrl);
			}
			List<String> publicationIds = publicationService
					.findPublicationIdsBy(
							title,
							category,
							sampleName,
							researchAreas,
							keywords,
							pubMedId,
							digitalObjectId,
							authors,
							nanomaterialEntityClassNames.toArray(new String[0]),
							otherNanomaterialEntityTypes.toArray(new String[0]),
							functionalizingEntityClassNames
									.toArray(new String[0]),
							otherFunctionalizingTypes.toArray(new String[0]),
							functionClassNames.toArray(new String[0]),
							otherFunctionTypes.toArray(new String[0]), user);
			for (String id : publicationIds) {
				PublicationBean pubBean = new PublicationBean(id, location);
				publications.add(pubBean);
			}
		}
		return publications;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		InitSetup.getInstance().getGridNodesInContext(request);

		String[] selectedLocations = new String[] { Constants.LOCAL_SITE };
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (!StringUtils.isEmpty(gridNodeHostStr)) {
			selectedLocations = gridNodeHostStr.split("~");
		}

		if (Constants.LOCAL_SITE.equals(selectedLocations[0])
				&& selectedLocations.length == 1) {
			InitSampleSetup.getInstance().setLocalSearchDropdowns(request);
		} else {
			InitSampleSetup.getInstance().setRemoteSearchDropdowns(request);
		}

		InitPublicationSetup.getInstance().setDefaultResearchAreas(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("searchLocations", selectedLocations);

		HttpSession session = request.getSession();
		session.removeAttribute("publicationSearchResults");
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return true;
	}

	public ActionForward exportSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter(Constants.LOCATION);
		String sampleId = request.getParameter("sampleId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request, location);
		String fileName = this.getExportFileName(sampleBean.getDomain()
				.getName(), "summaryView");
		ExportUtils.prepareReponseForExcel(response, fileName);

		PublicationService service = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			service = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new PublicationServiceRemoteImpl(serviceUrl);
		}

		List<PublicationBean> publications = service
				.findPublicationsBySampleId(sampleId, user);
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				publications);

		PublicationServiceHelper.exportSummary(summaryView, response
				.getOutputStream());

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

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter(Constants.LOCATION);
		String sampleId = request.getParameter("sampleId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PublicationService service = null;
		if (location.equals(Constants.LOCAL_SITE)) {
			service = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new PublicationServiceRemoteImpl(serviceUrl);
		}

		List<PublicationBean> publications = service
				.findPublicationsBySampleId(sampleId, user);

		HttpSession session = request.getSession();
		session.setAttribute("publicationCollection", publications);
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl + "?page=0&sampleId=" + sampleId
				+ "&dispatch=printSummaryView" + "&location=" + location;
		request.getSession().setAttribute("printSummaryViewLinkURL",
				printLinkURL);
		return mapping.findForward("publicationView");
	}

	public ActionForward printSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// return mapping.findForward("publicationDetailPrintView");
		return mapping.findForward("publicationSummaryPrintView");
	}

}
