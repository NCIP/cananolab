package gov.nih.nci.cananolab.ui.document;

/**
 * This class submits publication and assigns visibility  
 *  
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.document.DocumentService;
import gov.nih.nci.cananolab.service.document.impl.DocumentServiceLocalImpl;
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
import gov.nih.nci.system.client.ApplicationServiceProvider;

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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		publicationBean.setupDomainFile(CaNanoLabConstants.FOLDER_DOCUMENT, user
				.getLoginName());
		
		PublicationService service = new PublicationServiceLocalImpl();
		service.savePublication((Publication) publicationBean.getDomainFile(), publicationBean
				.getParticleNames(), publicationBean.getNewFileData(), publicationBean.getAuthors());
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
		if (request.getParameter("particleId") != null
				&& request.getParameter("particleId").length() > 0) {
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			ParticleBean particleBean = sampleService
					.findNanoparticleSampleById(request
							.getParameter("particleId"));
			setupDataTree(particleBean, request);
			forward = mapping.findForward("particleSuccess");
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//TODO
		InitDocumentSetup.getInstance().setPublicationDropdowns(request);	
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");
		if (request.getParameter("particleId") != null
				&& request.getParameter("particleId").length() > 0
				&& session.getAttribute("otherParticleNames")==null) {
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			ParticleBean particleBean = sampleService
					.findNanoparticleSampleById(request
							.getParameter("particleId"));
			UserBean user = (UserBean) session.getAttribute("user");
			InitNanoparticleSetup.getInstance().getOtherParticleNames(
					request,
					particleBean.getDomainParticleSample().getName(),
					particleBean.getDomainParticleSample().getSource()
							.getOrganizationName(), user);
		}		
		ActionForward forward = mapping.getInputForward();
		if (particleId != null) {
			forward = mapping.findForward("particleSubmitPublication");
			request.setAttribute("particleId", particleId);
		}
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String publicationId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		
		//TODO, tanq
		PublicationService publicationService = new PublicationServiceLocalImpl();
		PublicationBean publicationBean = publicationService.findPublicationById(publicationId);
		FileService fileService = new FileServiceLocalImpl();
		fileService.retrieveVisibility(publicationBean, user);
		theForm.set("file", publicationBean);
		theForm.set("documentAuthors",publicationBean.getAuthors());

		request.setAttribute("documentAuthors",publicationBean.getAuthors());

		InitDocumentSetup.getInstance().setPublicationDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.getInputForward();
		if (particleId != null) {
			//TODO, particleSubmitReport?? particleSubmitPublication??
			forward = mapping.findForward("particleSubmitPublication");
			request.setAttribute("particleId", particleId);
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
		return mapping.findForward("success");
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
//		setupParticle(theForm, request, location);
//		Characterization chara = prepareCharacterization(theForm, request,
//				location);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
//		getCharacterizationBean(theForm, chara, user, location);
//		String particleId = request.getParameter("particleId");
//		String publicationId = request.getParameter("dataId");
//		String className = request.getParameter("dataClassName");
//		String submitType = request.getParameter("submitType");
//		String requestUrl = request.getRequestURL().toString();
//		String printLinkURL = requestUrl
//				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
//				+ "&dataId=" + publicationId + "&dataClassName="
//				+ className + "&submitType=" + submitType + "&location="
//				+ location;
//		request.getSession().setAttribute("printDetailViewLinkURL",
//				printLinkURL);
		return mapping.findForward("detailView");
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
		

}
