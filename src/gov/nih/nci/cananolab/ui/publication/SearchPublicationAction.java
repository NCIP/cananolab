package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitCompositionSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * This class searches nanoparticle publication based on user supplied criteria
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
		//publication type
		String category = "";
		String keywordsStr = "";
		String pubMedId = "";
		String digitalObjectId = "";
		String authorsStr = "";
		String nanoparticleName = "";
		String[] publicationOrReport = new String[0];
		String[] researchArea = new String[0];
		String[] nanoparticleEntityTypes = new String[0];
		String[] functionalizingEntityTypes = new String[0];
		String[] functionTypes = new String[0];
		String[] searchLocations = new String[0];
		
		String invokeMethod = request.getParameter("invokeMethod");
		if (theForm != null &&
				(invokeMethod == null || !invokeMethod.equals("back"))) {
			title = (String) theForm.get("title");
			category = (String) theForm.get("category");
			keywordsStr = (String) theForm.get("keywordsStr");
			pubMedId = (String) theForm.get("pubMedId");
			digitalObjectId = (String) theForm.get("digitalObjectId");
			authorsStr = (String) theForm.get("authorsStr");
			nanoparticleName = (String) theForm.get("nanoparticleName");
			
			researchArea = (String[]) theForm
				.get("researchArea");
			publicationOrReport = (String[]) theForm
				.get("publicationOrReport");
			nanoparticleEntityTypes = (String[]) theForm
					.get("nanoparticleEntityTypes");
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
			session.setAttribute("docNanoparticleName", nanoparticleName);
			
			session.setAttribute("docResearchArea", researchArea);
			session.setAttribute("docPublicationOrReport", publicationOrReport);
			session.setAttribute("docNanoparticleEntityTypes", nanoparticleEntityTypes);
			session.setAttribute("docFunctionalizingEntityTypes", functionalizingEntityTypes);
			session.setAttribute("docFunctionTypes", functionTypes);
		} 
	
		if(invokeMethod != null && invokeMethod.equals("back")) {
			title = (String) session.getAttribute("docTitle");
			category = (String) session.getAttribute("docCategory");
			keywordsStr = (String) session.getAttribute("docKeywordsStr");
			pubMedId = (String) session.getAttribute("docPubMedId");
			digitalObjectId = (String) session.getAttribute("docDigitalObjectId");
			authorsStr = (String) session.getAttribute("docAuthorsStr");
			nanoparticleName = (String) session.getAttribute("docNanoparticleName");
			
			researchArea = (String[]) session.getAttribute("docResearchArea");
			publicationOrReport = (String[]) session.getAttribute("docPublicationOrReport");
			nanoparticleEntityTypes = (String[]) session.getAttribute("docNanoparticleEntityTypes");
			functionalizingEntityTypes = (String[]) session.getAttribute("docFunctionalizingEntityTypes");
			functionTypes = (String[]) session.getAttribute("docfunctionTypes");
			searchLocations = (String[]) session.getAttribute("docSearchLocations");
		}
		
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (searchLocations[0].indexOf("~") != -1 && 
				gridNodeHostStr != null && gridNodeHostStr.trim().length() > 0 ) {
			searchLocations = gridNodeHostStr.split("~");
		}
		
		if(invokeMethod == null || !invokeMethod.equals("back")) {
			session.setAttribute("docSearchLocations", searchLocations);
		}
		
		List<String> nanoparticleEntityClassNames = new ArrayList<String>();
		List<String> otherNanoparticleEntityTypes = new ArrayList<String>();
		for (int i = 0; i < nanoparticleEntityTypes.length; i++) {
			String className = InitSetup.getInstance().getObjectName(
					nanoparticleEntityTypes[i], session.getServletContext());
			if (className.length() == 0) {
				className = "OtherNanoparticleEntity";
				otherNanoparticleEntityTypes.add(nanoparticleEntityTypes[i]);
			} else {
				nanoparticleEntityClassNames.add(className);
			}
		}
		List<String> functionalizingEntityClassNames = new ArrayList<String>();
		List<String> otherFunctionalizingTypes = new ArrayList<String>();
		for (int i = 0; i < functionalizingEntityTypes.length; i++) {
			String className = InitSetup.getInstance().getObjectName(
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
				String className = InitSetup.getInstance().getObjectName(
						functionTypes[i], session.getServletContext());
				if (className.length() == 0) {
					className = "OtherFunction";
					otherFunctionTypes.add(functionTypes[i]);
				} else {
					functionClassNames.add(className);
				}
			}
		}
		
		List<LabFileBean> foundPublications = new ArrayList<LabFileBean>();

		//Publication
		if (publicationOrReport==null || publicationOrReport.length==0 ||
				Arrays.toString(publicationOrReport).contains("publication")){
			PublicationService publicationService = null;
			for (String location : searchLocations) {
				if (location.equals("local")) {
					publicationService = new PublicationServiceLocalImpl();
				} else {
					String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
							request, location);
					publicationService = new PublicationServiceRemoteImpl(serviceUrl);
				}
				List<PublicationBean> publications = publicationService.findPublicationsBy(title,
						category, nanoparticleName, researchArea, keywordsStr,
						pubMedId, digitalObjectId, authorsStr,
						nanoparticleEntityClassNames
								.toArray(new String[0]),
						otherNanoparticleEntityTypes.toArray(new String[0]),
						functionalizingEntityClassNames.toArray(new String[0]),
						otherFunctionalizingTypes.toArray(new String[0]),
						functionClassNames.toArray(new String[0]),
						otherFunctionTypes.toArray(new String[0]));
				for (PublicationBean publication : publications) {
					publication.setLocation(location);
				}
				
				if (location.equals("local")) {
					List<LabFileBean> filteredPublications = new ArrayList<LabFileBean>();
					// retrieve visibility
					FileService fileService = new FileServiceLocalImpl();
					for (PublicationBean publication : publications) {
						fileService.retrieveVisibility(publication, user);
						if (!publication.isHidden()) {
							filteredPublications.add((LabFileBean)publication);
						}
					}
					foundPublications.addAll(filteredPublications);
				} else {
					foundPublications.addAll(publications);
				}
			}
		}
		if (foundPublications != null && !foundPublications.isEmpty()) {
			request.getSession().setAttribute("publications", foundPublications);
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
		String[] selectedLocations = new String[] { "local" };
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (gridNodeHostStr != null && gridNodeHostStr.length() > 0) {
			selectedLocations = gridNodeHostStr.split("~");
		}
		
		if ("local".equals(selectedLocations[0]) &&
				selectedLocations.length == 1) {
			
			InitCompositionSetup.getInstance()
					.getNanoparticleEntityTypes(request);
			
			InitCompositionSetup.getInstance().getFunctionalizingEntityTypes(request);
			InitCompositionSetup.getInstance().getFunctionTypes(request);
		} else {
			InitCompositionSetup.getInstance()
					.getDefaultNanoparticleEntityTypes(request);
			
			InitCompositionSetup.getInstance().getDefaultFunctionalizingEntityTypes(request);
			InitCompositionSetup.getInstance().getDefaultFunctionTypes(request);
		}
		
		InitPublicationSetup.getInstance().setDefaultResearchAreas(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("searchLocations", selectedLocations);
		
		HttpSession session = request.getSession();
		session.removeAttribute("docParticleId");
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return true;
	}

	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter("location");
		String fileId = request.getParameter("fileId");
		if (location.equals("local")) {
			return super.download(mapping, form, request, response);
		} else {
			//TODO, to test
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			PublicationService publicationService = new PublicationServiceRemoteImpl(
					serviceUrl);
			PublicationBean fileBean = publicationService.findPublicationById(fileId);
			if (fileBean.getDomainFile().getUriExternal()) {
				response.sendRedirect(fileBean.getDomainFile().getUri());
				return null;
			}
			// assume grid service is located on the same server and port as
			// webapp
			URL url = new URL(serviceUrl);
			String remoteServerHostUrl = url.getProtocol() + "://"
					+ url.getHost() + ":" + url.getPort();
			String remoteDownloadUrl = remoteServerHostUrl + "/"
					+ CaNanoLabConstants.CSM_APP_NAME
					+ "/searchPublication.do?dispatch=download" + "&fileId="
					+ fileId + "&location=local";

			response.sendRedirect(remoteDownloadUrl);
			return null;
		}
	}
	
	public ActionForward exportSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		ParticleBean particleBean = setupParticle(theForm, request, location);
		String fileName = getExportFileName(particleBean
				.getDomainParticleSample().getName(), 
			"summaryView");
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename=\""
				+ fileName + ".xls\"");
		PublicationService service = null;
		if (location.equals("local")) {
			service = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new PublicationServiceRemoteImpl(
					serviceUrl);
		}
		service.exportSummary(particleBean, response.getOutputStream());
		return null;
	}
	
	
	private String getExportFileName(String titleName, String viewType) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(titleName);
		nameParts.add("Document");
		nameParts.add(viewType);
		nameParts.add(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}

	public ActionForward setupPublicationView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		ParticleBean particleSampleBean = setupParticle(theForm, request,
				location);
		theForm.set("particleSampleBean", particleSampleBean);
		
		HttpSession session = request.getSession();		
		session.setAttribute("particleSampleBean", particleSampleBean);
		setupDataTree(particleSampleBean, request);
		String particleId = request.getParameter("particleId");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl + "?page=0&particleId=" + particleId
				+ "&dispatch=printSummaryView"
				+ "&location=" + location;
		request.getSession().setAttribute("printSummaryViewLinkURL",
				printLinkURL);
		return mapping.findForward("publicationView");
	}

	public ActionForward printSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		//return mapping.findForward("publicationDetailPrintView");
		return mapping.findForward("publicationSummaryPrintView");
	}

}
