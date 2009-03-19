package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;
import java.util.SortedSet;

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
		pfileBean.setupDomainFile(Constants.FOLDER_PROTOCOL, user
				.getLoginName());
		ProtocolService service = new ProtocolServiceLocalImpl();
		ProtocolFile protocolFile = (ProtocolFile) pfileBean.getDomainFile();
		service.saveProtocolFile(protocolFile, pfileBean.getNewFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		authService.assignVisibility(pfileBean.getDomainFile().getId()
				.toString(), pfileBean.getVisibilityGroups(), null);

		// remove protocol visibility
		ProtocolServiceLocalImpl localService = new ProtocolServiceLocalImpl();
		Protocol dbProtocol = localService.findProtocolBy(protocolFile
				.getProtocol().getType(), protocolFile.getProtocol().getName());
		// assign protocol visibility
		if (pfileBean.getVisibilityStr() != null
				&& pfileBean.getVisibilityStr().contains(
						Constants.CSM_PUBLIC_GROUP)) {
			authService.assignVisibility(dbProtocol.getId().toString(),
					new String[] { Constants.CSM_PUBLIC_GROUP }, null);
		}

		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				pfileBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitProtocol.file",
				pfileBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}

	// for retaining user selected values during validation
	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolFileBean pfileBean = ((ProtocolFileBean) theForm.get("file"));
		String selectedProtocolType = ((ProtocolFile) pfileBean.getDomainFile())
				.getProtocol().getType();
		ProtocolService service = new ProtocolServiceLocalImpl();
		SortedSet<String> protocolNames = service
				.getProtocolNames(selectedProtocolType);
		request.getSession().setAttribute("protocolNamesByType", protocolNames);
		String selectedProtocolName = ((ProtocolFile) pfileBean.getDomainFile())
				.getProtocol().getName();
		List<ProtocolFileBean> pFiles = service.findProtocolFilesBy(
				selectedProtocolType, selectedProtocolName, null);
		request.getSession().setAttribute("protocolFilesByTypeName", pFiles);

		return mapping.findForward("inputPage");
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		return mapping.findForward("inputPage");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String fileId = request.getParameter("fileId");
		ProtocolService service = new ProtocolServiceLocalImpl();
		ProtocolFileBean pfileBean = service.findProtocolFileById(fileId);
		theForm.set("file", pfileBean);
		String selectedProtocolType = ((ProtocolFile) pfileBean.getDomainFile())
				.getProtocol().getType();
		String selectedProtocolName = ((ProtocolFile) pfileBean.getDomainFile())
				.getProtocol().getName();
		SortedSet<String> protocolNames = service
				.getProtocolNames(selectedProtocolType);
		request.getSession().setAttribute("protocolNamesByType", protocolNames);
		List<ProtocolFileBean> pFiles = service.findProtocolFilesBy(
				selectedProtocolType, selectedProtocolName, null);
		request.getSession().setAttribute("protocolFilesByTypeName", pFiles);
		FileService fileService = new FileServiceLocalImpl();
		fileService.retrieveVisibility(pfileBean, user);
		return mapping.findForward("inputPage");
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PROTOCOL);
	}
}
