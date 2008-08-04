package gov.nih.nci.cananolab.ui.document;

/**
 * This class submits publication and assigns visibility  
 *  
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.document.DocumentService;
import gov.nih.nci.cananolab.service.document.impl.DocumentServiceLocalImpl;
import gov.nih.nci.cananolab.service.document.impl.DocumentServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.PropertyReader;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.File;
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

public class SubmitPublicationAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean publicationBean = (PublicationBean) theForm.get("file");
		String[] researchAreas = publicationBean.getResearchAreas();
//		if (!validateResearchAreas(request, researchAreas)) {
//			return mapping.getInputForward();
//		}
//		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		publicationBean.setupDomainFile(CaNanoLabConstants.FOLDER_DOCUMENT, user
				.getLoginName());
		String researchAreasStr = null;
		if (researchAreas!=null && researchAreas.length>0) {
			researchAreasStr = StringUtils.join(researchAreas, ";");
		}
		Publication publication = (Publication) publicationBean.getDomainFile();
		publication.setResearchArea(researchAreasStr);
		
		PublicationService service = new PublicationServiceLocalImpl();
		service.savePublication(publication, publicationBean
				.getParticleNames(), publicationBean.getNewFileData(), 
				publicationBean.getAuthors());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(publicationBean.getDomainFile().getId()
				.toString(), publicationBean.getVisibilityGroups());

		InitDocumentSetup.getInstance().persistPublicationDropdowns(request,
				publicationBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitPublication.file",
				publicationBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");	
		if (particleId==null ||particleId.length()==0) {
			Object particleIdObj = session.getAttribute("particleId");
			if (particleIdObj!=null) {
				particleId = particleIdObj.toString();
				request.setAttribute("particleId", particleId);
			}else {
				request.removeAttribute("particleId");
			}
		}		
		if (particleId != null
				&& particleId.length() > 0) {
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			ParticleBean particleBean = sampleService
					.findNanoparticleSampleById(particleId);
			setupDataTree(particleBean, request);
			forward = mapping.findForward("particleSuccess");
		}
		session.removeAttribute("particleId");
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");		
		if (particleId != null) {
			session.setAttribute("particleId", particleId);
		}else {
			//if it is not calling from particle, remove previous set attribute if applicable
			session.removeAttribute("particleId");
		}
		InitDocumentSetup.getInstance().setPublicationDropdowns(request);	
		if (particleId!= null
				&& particleId.length() > 0
				&& session.getAttribute("otherParticleNames")==null) {
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			ParticleBean particleBean = sampleService
					.findNanoparticleSampleById(particleId);
			UserBean user = (UserBean) session.getAttribute("user");
			InitNanoparticleSetup.getInstance().getOtherParticleNames(
					request,
					particleBean.getDomainParticleSample().getName(),
					particleBean.getDomainParticleSample().getSource()
							.getOrganizationName(), user);
		}		
		ActionForward forward = mapping.getInputForward();
		
		if (particleId != null && !particleId.equals("null")) {
			forward = mapping.findForward("particleSubmitPublication");
			request.setAttribute("particleId", particleId);
		}else {
			request.removeAttribute("particleId");
		}
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");		
		if (particleId != null) {
			session.setAttribute("particleId", particleId);
		}else {
			session.removeAttribute("particleId");
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String publicationId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		
		
		PublicationService publicationService = new PublicationServiceLocalImpl();
		PublicationBean publicationBean = publicationService.findPublicationById(publicationId);
		FileService fileService = new FileServiceLocalImpl();
		fileService.retrieveVisibility(publicationBean, user);
		theForm.set("file", publicationBean);
		InitDocumentSetup.getInstance().setPublicationDropdowns(request);
		// if particleId is available direct to particle specific page
		ActionForward forward = mapping.getInputForward();
		if (particleId != null) {
			forward = mapping.findForward("particleSubmitPublication");
			request.setAttribute("particleId", particleId);
		}else {
			request.removeAttribute("particleId");
		}
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
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
		PublicationBean publicationBean = publicationService.findPublicationById(publicationId);
		if (location.equals("local")) {
			// retrieve visibility
			FileService fileService = new FileServiceLocalImpl();
			LabFileBean labFileBean = new LabFileBean(publicationBean.getDomainFile());
			fileService.retrieveVisibility(labFileBean, user);
			if (labFileBean.isHidden()){
				publicationBean.setHidden(true);
			}else{
				publicationBean.setHidden(false);
			}
		}		
		theForm.set("file", publicationBean);
		InitDocumentSetup.getInstance().setPublicationDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.findForward("view");
		if (particleId != null) {
			forward = mapping.findForward("particleViewPublication");
		}
		return forward;
	}
	
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		String submitType = request.getParameter("submitType");
		String[] dataIds = (String[]) theForm.get("idsToDelete");
		DocumentService documentService = new DocumentServiceLocalImpl();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
			.getApplicationService();
		NanoparticleSample particle = (NanoparticleSample)appService.getObject(
				NanoparticleSample.class, "id", new Long(particleId));
		
		ActionMessages msgs = new ActionMessages();
		for (String id : dataIds) {
			if (!checkDelete(request, msgs, id)) {
				return mapping.findForward("annotationDeleteView");
			}
			documentService.removeDocumentFromParticle(particle, new Long(id));
		}
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		setupDataTree(particleBean, request);
		ActionMessage msg = new ActionMessage("message.deleteDocuments",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		if (particleId!=null) {
			return mapping.findForward("particleSuccess");
		}else {
			return mapping.findForward("success");
		}
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		String location = request.getParameter("location");
		PublicationService publicationService = null;
		if (location.equals("local")) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}		
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(publicationId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("file", pubBean);	
	
		String particleId = request.getParameter("particleId");
		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
				+ "&publicationId=" + publicationId +
				"&submitType=" + submitType + "&location="
				+ location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);
		
		return mapping.findForward("particleDetailView");
	}
	
	
	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		String location = request.getParameter("location");
		PublicationService publicationService = null;
		if (location.equals("local")) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}		
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(publicationId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("file", pubBean);
		return mapping.findForward("publicationDetailPrintView");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		HttpSession session = request.getSession();	
		Object particleIdObj = session.getAttribute("particleId");
		String particleId = null;
		if (particleIdObj!=null) {
			particleId = particleIdObj.toString();
			request.setAttribute("particleId", particleId);
		}else {
			request.removeAttribute("particleId");
		}
				ActionForward forward = null;	
		if (particleId != null) {
			forward = mapping.findForward("particleSubmitPublication");
		}else {
			forward = mapping.findForward("documentSubmitPublication");
		}
		return forward;
	}

	public ActionForward addAuthor(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean pbean = (PublicationBean) theForm
				.get("file");
		pbean.addAuthor();

		return mapping.getInputForward();
	}
	
	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_DOCUMENT);
	}
	
//	private boolean validateReportFile(HttpServletRequest request,
//			PublicationBean publicationBean) throws Exception {
//		ActionMessages msgs = new ActionMessages();
//		if (!validateFileBean(request, msgs, publicationBean)) {
//			return false;
//		}		
//		return true;
//	}
	
	protected boolean validateResearchAreas(HttpServletRequest request,
			String[] researchAreas) throws Exception {
		ActionMessages msgs = new ActionMessages();
		boolean noErrors = true;
		if (researchAreas==null || researchAreas.length==0) {
			ActionMessage msg = new ActionMessage("submitPublicationForm.file.researchArea",
					"researchAreas");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			noErrors = false;
		}else {
			System.out.println("validateResearchAreas =="+Arrays.toString(researchAreas));
		}
		return noErrors;
	}
	
	public ActionForward exportDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String location = request.getParameter("location");
		PublicationService publicationService = null;
		if (location.equals("local")) {
			publicationService = new PublicationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		}		
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(publicationId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("file", pubBean);		
		String title = pubBean.getDomainFile().getTitle();
		if (title!=null && title.length()>10) {
			title = title.substring(0,10);
		}
		String fileName = this.getExportFileName(title, 
				"detailView");
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
		service.exportDetail(pubBean, response.getOutputStream());
		return null;
	}
	

	private String getExportFileName(String titleName, String viewType) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(titleName);
		nameParts.add("Publication");
		nameParts.add(viewType);
		nameParts.add(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}
		

}
