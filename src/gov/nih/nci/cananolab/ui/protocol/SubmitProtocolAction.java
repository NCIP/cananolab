package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
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
		ProtocolBean protocolBean = (ProtocolBean) theForm.get("file");
		protocolBean
				.setupDomain(Constants.FOLDER_PROTOCOL, user.getLoginName());
		ProtocolService service = new ProtocolServiceLocalImpl();
		service.saveProtocol(protocolBean.getDomain(), protocolBean
				.getFileBean().getNewFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		authService.assignVisibility(protocolBean.getFileBean().getDomainFile()
				.getId().toString(), protocolBean.getFileBean()
				.getVisibilityGroups(), null);

		// remove protocol visibility
		ProtocolServiceLocalImpl localService = new ProtocolServiceLocalImpl();
		Protocol dbProtocol = localService.findProtocolBy(protocolBean
				.getDomain().getType(), protocolBean.getDomain().getName());
		// assign protocol visibility
		if (protocolBean.getFileBean().getVisibilityStr() != null
				&& protocolBean.getFileBean().getVisibilityStr().contains(
						Constants.CSM_PUBLIC_GROUP)) {
			authService.assignVisibility(dbProtocol.getId().toString(),
					new String[] { Constants.CSM_PUBLIC_GROUP }, null);
		}

		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitProtocol.file",
				protocolBean.getFileBean().getDomainFile().getTitle());
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
		ProtocolBean protocolBean = ((ProtocolBean) theForm.get("file"));
		String selectedProtocolType = protocolBean.getDomain().getType();
		ProtocolService service = new ProtocolServiceLocalImpl();
		SortedSet<String> protocolNames = service
				.getProtocolNames(selectedProtocolType);
		request.getSession().setAttribute("protocolNamesByType", protocolNames);
		String selectedProtocolName = protocolBean.getDomain().getName();
		List<ProtocolBean> pFiles = service.findProtocolsBy(
				selectedProtocolType, selectedProtocolName, null);
		request.getSession().setAttribute("protocolsByTypeName", pFiles);

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
		ProtocolBean protocolBean = service.findProtocolById(fileId);
		theForm.set("file", protocolBean);
		String selectedProtocolType = protocolBean.getDomain().getType();
		String selectedProtocolName = protocolBean.getDomain().getName();
		SortedSet<String> protocolNames = service
				.getProtocolNames(selectedProtocolType);
		request.getSession().setAttribute("protocolNamesByType", protocolNames);
		List<ProtocolBean> pFiles = service.findProtocolsBy(
				selectedProtocolType, selectedProtocolName, null);
		request.getSession().setAttribute("protocolsByTypeName", pFiles);
		FileService fileService = new FileServiceLocalImpl();
		fileService.retrieveVisibility(protocolBean.getFileBean(), user);
		return mapping.findForward("inputPage");
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PROTOCOL);
	}
}
