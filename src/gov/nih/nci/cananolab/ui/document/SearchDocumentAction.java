package gov.nih.nci.cananolab.ui.document;

import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceLocalImpl;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitCompositionSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * This class searches nanoparticle document based on user supplied criteria
 * 
 * @author tanq
 */

public class SearchDocumentAction extends BaseAnnotationAction {

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;

		String title = "";
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
		if (theForm != null) {
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
		}
		
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (searchLocations[0].indexOf("~") != -1 && gridNodeHostStr != null
				&& gridNodeHostStr.trim().length() > 0) {
			searchLocations = gridNodeHostStr.split("~");
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
		
		List<LabFileBean> foundDocuments = new ArrayList<LabFileBean>();
		//report
		//List<ReportBean> foundReports = new ArrayList<ReportBean>();
		if (publicationOrReport==null || publicationOrReport.length==0 ||
				Arrays.toString(publicationOrReport).contains("report")){		
			ReportService service = null;
			for (String location : searchLocations) {
				if (location.equals("local")) {
					service = new ReportServiceLocalImpl();
				} else {
					String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
							request, location);
					service = new ReportServiceRemoteImpl(serviceUrl);
				}
				List<ReportBean> reports = service.findReportsBy(title,
						category, nanoparticleName, nanoparticleEntityClassNames
								.toArray(new String[0]),
						otherNanoparticleEntityTypes.toArray(new String[0]),
						functionalizingEntityClassNames.toArray(new String[0]),
						otherFunctionalizingTypes.toArray(new String[0]),
						functionClassNames.toArray(new String[0]),
						otherFunctionTypes.toArray(new String[0]));
				for (ReportBean report : reports) {
					report.setLocation(location);
				}
				if (location.equals("local")) {
					List<LabFileBean> filteredReports = new ArrayList<LabFileBean>();
					// retrieve visibility
					FileService fileService = new FileServiceLocalImpl();
					for (ReportBean report : reports) {
						fileService.retrieveVisibility(report, user);
						if (!report.isHidden()) {
							filteredReports.add((LabFileBean)report);
						}
					}
					foundDocuments.addAll(filteredReports);
				} else {
					foundDocuments.addAll(reports);
				}
			}
		}
		
		//Publication
		if (publicationOrReport==null || publicationOrReport.length==0 ||
				Arrays.toString(publicationOrReport).contains("publication")){
			PublicationService documentService = null;
			for (String location : searchLocations) {
				if (location.equals("local")) {
					documentService = new PublicationServiceLocalImpl();
				} else {
					String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
							request, location);
					documentService = new PublicationServiceRemoteImpl(serviceUrl);
				}
				List<PublicationBean> publications = documentService.findPublicationsBy(title,
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
					foundDocuments.addAll(filteredPublications);
				} else {
					foundDocuments.addAll(publications);
				}
			}
		}
		if (foundDocuments != null && !foundDocuments.isEmpty()) {
			request.getSession().setAttribute("documents", foundDocuments);
			forward = mapping.findForward("success");
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchDocument.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitDocumentSetup.getInstance().setDocumentDropdowns(request);
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
		
		InitDocumentSetup.getInstance().setDefaultResearchAreas(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("searchLocations", selectedLocations);
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
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			ReportService protocolService = new ReportServiceRemoteImpl(
					serviceUrl);
			ReportBean fileBean = protocolService.findReportById(fileId);
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
					+ "/searchDocument.do?dispatch=download" + "&fileId="
					+ fileId + "&location=local";

			response.sendRedirect(remoteDownloadUrl);
			return null;
		}
	}
}
