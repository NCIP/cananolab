package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.StringUtils;

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
		FileBean fileBean = (FileBean) theForm.get("compFile");
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String internalUriPath = CaNanoLabConstants.FOLDER_PARTICLE
				+ "/"
				+ particleBean.getDomainParticleSample().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Composition File");

		fileBean.setupDomainFile(internalUriPath, user.getLoginName(), 0);
		NanoparticleCompositionService service = new NanoparticleCompositionServiceLocalImpl();
		service.saveCompositionFile(particleBean.getDomainParticleSample(),
				fileBean.getDomainFile(), fileBean.getNewFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(fileBean.getDomainFile().getId()
				.toString(), fileBean.getVisibilityGroups(), null);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addCompositionFile",
				fileBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		setupDataTree(particleBean, request);
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
		setupParticle(theForm, request, "local");
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId = request.getParameter("dataId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		FileService fileService = new FileServiceLocalImpl();
		FileBean fileBean = fileService.findFileById(fileId);
		fileService.retrieveVisibility(fileBean, user);
		theForm.set("compFile", fileBean);
		setLookups(request);
		setupParticle(theForm, request, "local");
		ActionForward forward = mapping.getInputForward();
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		String fileId = request.getParameter("dataId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		FileService fileService = null;
		if (location.equals("local")) {
			fileService = new FileServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			fileService = new FileServiceRemoteImpl(serviceUrl);
		}
		FileBean fileBean = fileService.findFileById(fileId);
		if (location.equals("local")) {
			fileService.retrieveVisibility(fileBean, user);
		}
		theForm.set("compFile", fileBean);
		setupParticle(theForm, request, location);
		ActionForward forward = mapping.getInputForward();
		return forward;
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FileBean fileBean = (FileBean) theForm.get("compFile");
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();
		compService.deleteCompositionFile(particleBean
				.getDomainParticleSample(), fileBean.getDomainFile());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteCompositionFile");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(particleBean, request);
		return forward;
	}

	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String submitType = request.getParameter("submitType");
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();
		String[] dataIds = (String[]) theForm.get("idsToDelete");
		FileService fileService = new FileServiceLocalImpl();
		for (String id : dataIds) {
			FileBean fileBean = fileService.findFileById(id);
			compService.deleteCompositionFile(particleBean
					.getDomainParticleSample(), fileBean.getDomainFile());
		}
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteAnnotations",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(particleBean, request);
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
