package gov.nih.nci.cananolab.ui.document;

/**
 * This class submits publication and assigns visibility  
 *  
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.document.DocumentService;
import gov.nih.nci.cananolab.service.document.impl.DocumentServiceLocalImpl;
import gov.nih.nci.cananolab.service.document.impl.DocumentServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

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
		
		//TODO, tanq, publicaionService
		DocumentService service = new DocumentServiceLocalImpl();
		service.savePublication((Publication) publicationBean.getDomainFile(), publicationBean
				.getParticleNames(), publicationBean.getNewFileData());
		//end
		
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
		InitDocumentSetup.getInstance().setReportDropdowns(request);		
		String particleId = request.getParameter("particleId");
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
		String reportId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		
		//TODO, tanq
		//DocumentService DocumentService = new DocumentServiceLocalImpl();
		//PublicationBean publicationBean = DocumentService.findReportById(reportId);
		//FileService fileService = new FileServiceLocalImpl();
		//fileService.retrieveVisibility(publicationBean, user);
		//theForm.set("file", publicationBean);
		InitDocumentSetup.getInstance().setReportDropdowns(request);
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
		DocumentService documentService = null;
		if (location.equals("local")) {
			documentService = new DocumentServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			documentService = new DocumentServiceRemoteImpl(serviceUrl);
		}
		PublicationBean publicationBean = documentService.findPublicationById(publicationId);
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
		InitDocumentSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.findForward("view");
		if (particleId != null) {
			forward = mapping.findForward("particleViewReport");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_DOCUMENT);
	}
	
	private boolean validateReportFile(HttpServletRequest request,
			PublicationBean publicationBean) throws Exception {
		ActionMessages msgs = new ActionMessages();
		if (!validateFileBean(request, msgs, publicationBean)) {
			return false;
		}		
		return true;
	}
		

}
