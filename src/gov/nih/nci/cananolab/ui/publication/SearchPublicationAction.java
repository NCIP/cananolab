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

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;

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

		String invokeMethod = request.getParameter("invokeMethod");
		if (theForm != null
				&& (invokeMethod == null || !invokeMethod.equals("back"))) {
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

			session.setAttribute("docTitle", title);
			session.setAttribute("docCategory", category);
			session.setAttribute("docKeywordsStr", keywordsStr);
			session.setAttribute("docPubMedId", pubMedId);
			session.setAttribute("docDigitalObjectId", digitalObjectId);
			session.setAttribute("docAuthorsStr", authorsStr);
			session.setAttribute("docNanoparticleName", sampleName);

			session.setAttribute("docResearchArea", researchAreas);
			// session.setAttribute("docPublicationOrReport",
			// publicationOrReport);
			session.setAttribute("docNanomaterialEntityTypes",
					nanomaterialEntityTypes);
			session.setAttribute("docFunctionalizingEntityTypes",
					functionalizingEntityTypes);
			session.setAttribute("docFunctionTypes", functionTypes);
		}

		if (invokeMethod != null && invokeMethod.equals("back")) {
			title = (String) session.getAttribute("docTitle");
			category = (String) session.getAttribute("docCategory");
			String keywordsStr = (String) session
					.getAttribute("docKeywordsStr");
			pubMedId = (String) session.getAttribute("docPubMedId");
			digitalObjectId = (String) session
					.getAttribute("docDigitalObjectId");
			String authorsStr = (String) session.getAttribute("docAuthorsStr");
			sampleName = (String) session.getAttribute("docNanoparticleName");

			researchAreas = (String[]) session.getAttribute("docResearchArea");
			// publicationOrReport = (String[])
			// session.getAttribute("docPublicationOrReport");
			nanomaterialEntityTypes = (String[]) session
					.getAttribute("docNanomaterialEntityTypes");
			functionalizingEntityTypes = (String[]) session
					.getAttribute("docFunctionalizingEntityTypes");
			functionTypes = (String[]) session.getAttribute("docfunctionTypes");
			searchLocations = (String[]) session
					.getAttribute("docSearchLocations");
		}

		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (searchLocations[0].indexOf("~") != -1 && gridNodeHostStr != null
				&& gridNodeHostStr.trim().length() > 0) {
			searchLocations = gridNodeHostStr.split("~");
		}

		if (invokeMethod == null || !invokeMethod.equals("back")) {
			session.setAttribute("docSearchLocations", searchLocations);
		}

		List<String> nanomaterialEntityClassNames = new ArrayList<String>();
		List<String> otherNanomaterialEntityTypes = new ArrayList<String>();
		for (int i = 0; i < nanomaterialEntityTypes.length; i++) {
			String className = InitSetup.getInstance().getClassName(
					nanomaterialEntityTypes[i], session.getServletContext());
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
			String className = InitSetup.getInstance().getClassName(
					functionalizingEntityTypes[i], session.getServletContext());
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
				String className = InitSetup.getInstance().getClassName(
						functionTypes[i], session.getServletContext());
				if (className.length() == 0) {
					className = "OtherFunction";
					otherFunctionTypes.add(functionTypes[i]);
				} else {
					functionClassNames.add(className);
				}
			}
		}

		// Publication
		List<PublicationBean> publications = null;
		PublicationService publicationService = null;
		for (String location : searchLocations) {
			if (Constants.LOCAL_SITE.equals(location)) {
				publicationService = new PublicationServiceLocalImpl();
			} else {
				String serviceUrl = 
					InitSetup.getInstance().getGridServiceUrl(request, location);
				publicationService = new PublicationServiceRemoteImpl(serviceUrl);
			}
			publications = publicationService.findPublicationsBy(title,
				category, sampleName, researchAreas, keywords, pubMedId,
				digitalObjectId, authors, nanomaterialEntityClassNames.toArray(new String[0]),
				otherNanomaterialEntityTypes.toArray(new String[0]),
				functionalizingEntityClassNames.toArray(new String[0]),
				otherFunctionalizingTypes.toArray(new String[0]),
				functionClassNames.toArray(new String[0]),
				otherFunctionTypes.toArray(new String[0]), user);
			for (PublicationBean publication : publications) {
				publication.setLocation(location);
			}
		}
		if (publications != null && !publications.isEmpty()) {
			request.getSession().setAttribute("publications", publications);
			forward = mapping.findForward("success");
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchPublication.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		InitSetup.getInstance().getGridNodesInContext(request);

		String[] selectedLocations = new String[] { Constants.LOCAL_SITE };
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (gridNodeHostStr != null && gridNodeHostStr.length() > 0) {
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
		session.removeAttribute("docSampleId");
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
		String fileName = 
			this.getExportFileName(sampleBean.getDomain().getName(), "summaryView");
		ExportUtils.prepareReponseForExcell(response, fileName);
		
		PublicationService service = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			service = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = 
				InitSetup.getInstance().getGridServiceUrl(request, location);
			service = new PublicationServiceRemoteImpl(serviceUrl);
		}

		List<PublicationBean> publications = 
			service.findPublicationsBySampleId(sampleId, user);
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				publications);

		PublicationServiceHelper.exportSummary(summaryView, response.getOutputStream());
		
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
			String serviceUrl = 
				InitSetup.getInstance().getGridServiceUrl(request, location);
			service = new PublicationServiceRemoteImpl(serviceUrl);
		}

		List<PublicationBean> publications = 
			service.findPublicationsBySampleId(sampleId, user);

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
