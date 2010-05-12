package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.StringUtils;
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
public class ProtocolAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ProtocolBean protocolBean = (ProtocolBean) theForm.get("protocol");
		protocolBean
				.setupDomain(Constants.FOLDER_PROTOCOL, user.getLoginName());
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		ProtocolService service = new ProtocolServiceLocalImpl();
		service.saveProtocol(protocolBean, user);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitProtocol",
				protocolBean.getDisplayName());
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
		ProtocolBean protocolBean = ((ProtocolBean) theForm.get("protocol"));
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		setupDynamicDropdowns(request, protocolBean);
		return mapping.findForward("inputPage");
	}

	private void setupDynamicDropdowns(HttpServletRequest request,
			ProtocolBean protocolBean) throws Exception {
		String selectedProtocolType = protocolBean.getDomain().getType();
		String selectedProtocolName = protocolBean.getDomain().getName();
		ProtocolServiceHelper helper = new ProtocolServiceHelper();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// retrieve user entered protocol names that haven't been saved as
		// protocols
		SortedSet<String> otherNames = LookupService.findLookupValues(
				selectedProtocolType + " protocol type", "otherName");
		if (!StringUtils.isEmpty(selectedProtocolType)) {
			SortedSet<String> protocolNames = helper.getProtocolNamesBy(
					selectedProtocolType, user);
			protocolNames.addAll(otherNames);
			request.getSession().setAttribute("protocolNamesByType",
					protocolNames);
		} else {
			request.getSession()
					.setAttribute("protocolNamesByType", otherNames);
		}

		// retrieve user entered protocol versions that haven't been saved
		// as protocols
		SortedSet<String> otherVersions = LookupService.findLookupValues(
				selectedProtocolType + " protocol type", "otherVersion");
		if (!StringUtils.isEmpty(selectedProtocolName)) {
			SortedSet<String> protocolVersions = helper.getProtocolVersionsBy(
					selectedProtocolType, selectedProtocolName, user);
			protocolVersions.addAll(otherVersions);
			request.getSession().setAttribute("protocolVersionsByTypeName",
					protocolVersions);
		} else {
			request.getSession().setAttribute("protocolVersionsByTypeName",
					otherVersions);
		}
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		request.getSession().removeAttribute("protocolNamesByType");
		request.getSession().removeAttribute("protocolVersionsByTypeName");
		return mapping.findForward("inputPage");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String protocolId = request.getParameter("protocolId");
		ProtocolService service = new ProtocolServiceLocalImpl();
		ProtocolBean protocolBean = service.findProtocolById(protocolId, user);
		theForm.set("protocol", protocolBean);
		setupDynamicDropdowns(request, protocolBean);
		return mapping.findForward("inputPage");
	}

	/**
	 * Delete a protocol from Protocol update form
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocolBean = (ProtocolBean) theForm.get("protocol");
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		ProtocolService service = new ProtocolServiceLocalImpl();
		service.deleteProtocol(protocolBean.getDomain(), user, true);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteProtocol",
				protocolBean.getDisplayName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return mapping.findForward("success");
	}

	public Boolean canUserExecutePrivateDispatch(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PROTOCOL);
	}
}
