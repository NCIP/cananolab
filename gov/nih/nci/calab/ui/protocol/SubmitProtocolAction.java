package gov.nih.nci.calab.ui.protocol;

/**
 * This class uploads a protocol file and assigns visibility  
 *  
 * @author chenhang
 */
/* CVS $Id: SubmitProtocolAction.java,v 1.9 2007-12-06 22:16:05 pansu Exp $ */

import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.service.protocol.SearchProtocolService;
import gov.nih.nci.calab.service.protocol.SubmitProtocolService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitProtocolAction extends AbstractDispatchAction {

	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String protocolName = (String) theForm.get("protocolName");
		String protocolType = (String) theForm.get("protocolType");
		ProtocolFileBean fileBean = (ProtocolFileBean) theForm.get("file");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		fileBean.setCreatedBy(user.getLoginName());
		fileBean.setCreatedDate(new Date());
		// String version = fileBean.getId();

		ProtocolBean pBean = new ProtocolBean();

		pBean.setId(protocolName);
		pBean.setType(protocolType);

		FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");
		fileBean.setProtocolBean(pBean);
		SubmitProtocolService service = new SubmitProtocolService();

		service.createProtocol(fileBean, uploadedFile);

		if (fileBean.getVisibilityGroups().length == 0) {
			fileBean.setVisibilityGroups(CaNanoLabConstants.VISIBLE_GROUPS);
		}
		ActionMessage msg1 = new ActionMessage("message.submitProtocol.secure",
				StringUtils.join(fileBean.getVisibilityGroups(), ", "));
		ActionMessage msg2 = new ActionMessage("message.submitProtocol.file",
				uploadedFile.getFileName());
		msgs.add("message", msg1);
		msgs.add("message", msg2);
		saveMessages(request, msgs);

		request.getSession().setAttribute("newProtocolCreated", "true");
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitProtocolSetup.getInstance().setAllProtocolTypes(session);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);
		ActionForward forward = mapping.findForward("setup");
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId = request.getParameter("fileId");
		if (fileId == null)
			fileId = (String) request.getAttribute("fileId");
		UserBean user=(UserBean)session.getAttribute("user");
		SearchProtocolService service = new SearchProtocolService();
		ProtocolFileBean fileBean = service.getProtocolFileBean(fileId, user);
		theForm.set("file", fileBean);
		theForm.set("protocolName", fileBean.getProtocolBean().getName());
		theForm.set("protocolType", fileBean.getProtocolBean().getType());
		request.setAttribute("filename", fileBean.getName());
		return mapping.findForward("setup");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages msgs = new ActionMessages();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolFileBean fileBean = (ProtocolFileBean) theForm.get("file");

		// FormFile uploadedFile = (FormFile) theForm.get("uploadedFile");

		SubmitProtocolService service = new SubmitProtocolService();

		try {
			service.updateProtocol(fileBean, null);

			if (fileBean.getVisibilityGroups().length == 0) {
				fileBean.setVisibilityGroups(CaNanoLabConstants.VISIBLE_GROUPS);
			}
		} catch (Exception e) {
			ActionMessage msg = new ActionMessage("error.updateProtocol", e
					.getMessage());
			msgs.add("message", msg);
			saveMessages(request, msgs);
			request.setAttribute("fileId", fileBean.getId());
			return setupUpdate(mapping, form, request, response);
		}

		ActionMessage msg = new ActionMessage("message.updateProtocol",
				fileBean.getTitle());

		msgs.add("message", msg);
		saveMessages(request, msgs);

		// request.getSession().setAttribute("newProtocolCreated", "true");

		return mapping.findForward("success");
	}

	public ActionForward reSetup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// updateEditableDropDownList(request, theForm);
		return mapping.findForward("setup");
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
