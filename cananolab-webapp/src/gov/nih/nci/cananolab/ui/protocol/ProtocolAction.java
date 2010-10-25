package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

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
public class ProtocolAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocolBean = (ProtocolBean) theForm.get("protocol");
		Boolean newProtocol = true;
		if (protocolBean.getDomain().getId() != null
				&& protocolBean.getDomain().getId() > 0) {
			newProtocol = false;
		}
		this.saveProtocol(request, protocolBean);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// retract from public if updating an existing public record and not
		// curator
		if (!newProtocol && !user.isCurator()) {
			retractFromPublic(theForm, request, protocolBean.getDomain()
					.getId().toString(), protocolBean.getDomain().getName(),
					"protocol");
			ActionMessages messages = new ActionMessages();
			ActionMessage msg = null;
			msg = new ActionMessage("message.updateProtocol.retractFromPublic");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("message.submitProtocol",
					protocolBean.getDisplayName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
		}
		forward = mapping.findForward("success");
		return forward;
	}

	public void saveProtocol(HttpServletRequest request,
			ProtocolBean protocolBean) throws Exception {
		ProtocolService service = this.setServiceInSession(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		protocolBean
				.setupDomain(Constants.FOLDER_PROTOCOL, user.getLoginName());
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		service.saveProtocol(protocolBean);
	}

	// for retaining user selected values during validation
	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		super.checkOpenAccessForm(theForm, request);
		ProtocolBean protocolBean = ((ProtocolBean) theForm.get("protocol"));
		this.setServiceInSession(request);
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		setupDynamicDropdowns(request, protocolBean);
		return mapping.findForward("inputPage");
	}

	private void setupDynamicDropdowns(HttpServletRequest request,
			ProtocolBean protocolBean) throws Exception {
		String selectedProtocolType = protocolBean.getDomain().getType();
		String selectedProtocolName = protocolBean.getDomain().getName();
		ProtocolServiceLocalImpl protocolService = (ProtocolServiceLocalImpl) (ProtocolService) request
				.getSession().getAttribute("protocolService");
		ProtocolServiceHelper helper = protocolService.getHelper();
		// retrieve user entered protocol names that haven't been saved as
		// protocols
		SortedSet<String> otherNames = LookupService.findLookupValues(
				selectedProtocolType + " protocol type", "otherName");
		if (!StringUtils.isEmpty(selectedProtocolType)) {
			SortedSet<String> protocolNames = helper
					.getProtocolNamesBy(selectedProtocolType);
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
					selectedProtocolType, selectedProtocolName);
			protocolVersions.addAll(otherVersions);
			request.getSession().setAttribute("protocolVersionsByTypeName",
					protocolVersions);
		} else {
			request.getSession().setAttribute("protocolVersionsByTypeName",
					otherVersions);
		}
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("protocolForm");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		super.checkOpenAccessForm(theForm, request);
		setServiceInSession(request);
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		request.getSession().removeAttribute("protocolNamesByType");
		request.getSession().removeAttribute("protocolVersionsByTypeName");
		request.getSession().removeAttribute("updateProtocol");
		return mapping.findForward("inputPage");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		super.checkOpenAccessForm(theForm, request);
		String protocolId = request.getParameter("protocolId");
		if (protocolId == null) {
			protocolId = (String) request.getAttribute("protocolId");
		}
		if (protocolId == null) {
			throw new NotExistException("No such protocol in the database");
		}
		ProtocolService service = this.setServiceInSession(request);
		ProtocolBean protocolBean = service.findProtocolById(protocolId);
		if (protocolBean == null) {
			throw new NotExistException("No such protocol in the database");
		}
		theForm.set("protocol", protocolBean);
		setupDynamicDropdowns(request, protocolBean);
		request.getSession().setAttribute("updateProtocol", "true");
		setUpSubmitForReviewButton(request, protocolBean.getDomain().getId()
				.toString(), protocolBean.getPublicStatus());
		return mapping.findForward("inputPage");
	}

	private void setAccesses(HttpServletRequest request,
			ProtocolBean protocolBean) throws Exception {
		ProtocolService service = this.setServiceInSession(request);
		List<AccessibilityBean> groupAccesses = service
				.findGroupAccessibilities(protocolBean.getDomain().getId()
						.toString());
		List<AccessibilityBean> userAccesses = service
				.findUserAccessibilities(protocolBean.getDomain().getId()
						.toString());
		protocolBean.setUserAccesses(userAccesses);
		protocolBean.setGroupAccesses(groupAccesses);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		protocolBean.setUser(user);
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
		ProtocolService service = this.setServiceInSession(request);
		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				protocolBean.getDomain().getId().toString(), protocolBean
						.getDomain().getName(), "protocol");
		service.deleteProtocol(protocolBean.getDomain());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteProtocol",
				protocolBean.getDisplayName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return mapping.findForward("success");
	}

	private ProtocolService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		ProtocolService protocolService = new ProtocolServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("protocolService", protocolService);
		return protocolService;
	}

	public ActionForward saveAccess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocol = (ProtocolBean) theForm.get("protocol");
		AccessibilityBean theAccess = protocol.getTheAccess();
		if (!super.validateAccess(request, theAccess)) {
			return input(mapping, form, request, response);
		}
		ProtocolService service = this.setServiceInSession(request);
		// if protocol is new, save protocol first
		if (protocol.getDomain().getId() == 0) {
			this.saveProtocol(request, protocol);
		}
		// if protocol is public, the access is not public, retract public
		// privilege would be handled in the service method
		service.assignAccessibility(theAccess, protocol.getDomain());
		// update status to retracted if the access is not public and protocol
		// is public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)
				&& protocol.getPublicStatus()) {
			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS,
					request, protocol.getDomain().getId().toString(), protocol
							.getDomain().getName(), "protocol");
		}
		// if access is public, pending review status, update review
		// status to public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
			this.switchPendingReviewToPublic(request, protocol.getDomain()
					.getId().toString());
		}

		if (protocol.getDomain().getId() == null) {
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			protocol
					.setupDomain(Constants.FOLDER_PROTOCOL, user.getLoginName());
			service.saveProtocol(protocol);
		}
		this.setAccesses(request, protocol);
		request.setAttribute("protocolId", protocol.getDomain().getId()
				.toString());
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward deleteAccess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocol = (ProtocolBean) theForm.get("protocol");
		AccessibilityBean theAccess = protocol.getTheAccess();
		ProtocolService service = this.setServiceInSession(request);
		service.removeAccessibility(theAccess, protocol.getDomain());
		this.setAccesses(request, protocol);
		request.setAttribute("protocolId", protocol.getDomain().getId()
				.toString());
		return setupUpdate(mapping, form, request, response);
	}

	protected void removePublicAccess(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		ProtocolBean protocol = (ProtocolBean) theForm.get("protocol");
		ProtocolService service = this.setServiceInSession(request);
		service.removeAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS,
				protocol.getDomain());
	}

	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProtocolService service = this.setServiceInSession(request);
		return downloadFile(service, mapping, form, request, response);
	}
}
