package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
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
 * Create or update protocol file and protocol
 * 
 * @author pansu
 * 
 */
public class SubmitProtocolAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ProtocolFileBean pfileBean = (ProtocolFileBean) theForm.get("file");
		String internalUri = InitSetup.getInstance().getFileUriFromFormFile(
				pfileBean.getUploadedFile(),
				CaNanoLabConstants.FOLDER_PROTOCOL, null, null);
		pfileBean.setInternalUri(internalUri);
		pfileBean.setupDomainFile(user.getLoginName());
		ProtocolService service = new ProtocolService();
		service.saveProtocolFile((ProtocolFile) pfileBean.getDomainFile(),
				pfileBean.getFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(pfileBean.getDomainFile().getId()
				.toString(), pfileBean.getVisibilityGroups());

		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				pfileBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitProtocl.file",
				pfileBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String fileId = request.getParameter("fileId");
		ProtocolService service = new ProtocolService();
		ProtocolFileBean pfileBean = service.findProtocolFileById(fileId);
		theForm.set("file", pfileBean);
		FileService fileService = new FileService();
		fileService.retrieveVisibility(pfileBean, user);
		return mapping.getInputForward();
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PROTOCOL);
	}
}
