package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This class allows users to submit composition files under sample composition.
 * 
 * @author pansu
 */
public class CompositionFileAction extends BaseAnnotationAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		LabFileBean fileBean = (LabFileBean) theForm.get("compFile");
		ParticleBean particleBean = setupParticle(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String internalUri = InitSetup.getInstance().getFileUriFromFormFile(
				fileBean.getUploadedFile(), CaNanoLabConstants.FOLDER_PARTICLE,
				particleBean.getDomainParticleSample().getName(),
				"Sample Composition");
		fileBean.setInternalUri(internalUri);
		fileBean.setupDomainFile(user.getLoginName());
		NanoparticleCompositionService service = new NanoparticleCompositionService();
		service.saveCompositionFile(particleBean.getDomainParticleSample(),
				fileBean.getDomainFile(), fileBean.getFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(fileBean.getDomainFile().getId()
				.toString(), fileBean.getVisibilityGroups());

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addCompositionFile",
				fileBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		setupDataTree(theForm, request);
		return forward;
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitNanoparticleSetup.getInstance().setSharedDropdowns(request);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("compositionFileForm");
		setLookups(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId = request.getParameter("dataId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		FileService fileService = new FileService();
		LabFileBean fileBean = fileService.findFile(fileId);
		fileService.retrieveVisibility(fileBean, user);
		theForm.set("compFile", fileBean);
		setLookups(request);
		setupParticle(theForm, request);
		ActionForward forward = mapping.getInputForward();
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		LabFileBean fileBean = (LabFileBean) theForm.get("compFile");
		ParticleBean particleBean = setupParticle(theForm, request);
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		compService.deleteCompositionFile(particleBean
				.getDomainParticleSample(), fileBean.getDomainFile());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteCompositionFile");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(theForm, request);
		return forward;
	}

	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String submitType = request.getParameter("submitType");
		ParticleBean particleBean = setupParticle(theForm, request);
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		String[] dataIds = (String[]) theForm.get("idsToDelete");
		FileService fileService = new FileService();
		for (String id : dataIds) {
			LabFileBean fileBean = fileService.findFile(id);
			compService.deleteCompositionFile(particleBean
					.getDomainParticleSample(), fileBean.getDomainFile());
		}
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteAnnotations",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(theForm, request);
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}
