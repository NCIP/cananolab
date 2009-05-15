package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;
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
public class CompositionFileAction extends CompositionAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FileBean fileBean = (FileBean) theForm.get("compFile");
		SampleBean sampleBean = setupSample(theForm, request, "local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Composition File");

		fileBean.setupDomainFile(internalUriPath, user.getLoginName(), 0);
		CompositionService service = new CompositionServiceLocalImpl();
		service.saveCompositionFile(sampleBean.getDomain(),
				fileBean.getDomainFile(), fileBean.getNewFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		authService.assignVisibility(fileBean.getDomainFile().getId()
				.toString(), fileBean.getVisibilityGroups(), null);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addCompositionFile",
				fileBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("compositionFileForm");
		setLookups(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupSample(theForm, request, "local");
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
		setupSample(theForm, request, "local");
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
		setupSample(theForm, request, location);
		ActionForward forward = mapping.getInputForward();
		return forward;
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FileBean fileBean = (FileBean) theForm.get("compFile");
		SampleBean sampleBean = setupSample(theForm, request, "local");
		CompositionService compService = new CompositionServiceLocalImpl();
		compService.deleteCompositionFile(sampleBean
				.getDomain(), fileBean.getDomainFile());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteCompositionFile");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String submitType = request.getParameter("submitType");
		SampleBean sampleBean = setupSample(theForm, request, "local");
		CompositionService compService = new CompositionServiceLocalImpl();
		String[] dataIds = (String[]) theForm.get("idsToDelete");
		FileService fileService = new FileServiceLocalImpl();
		for (String id : dataIds) {
			FileBean fileBean = fileService.findFileById(id);
			compService.deleteCompositionFile(sampleBean
					.getDomain(), fileBean.getDomainFile());
		}
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteAnnotations",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PARTICLE);
	}
}
