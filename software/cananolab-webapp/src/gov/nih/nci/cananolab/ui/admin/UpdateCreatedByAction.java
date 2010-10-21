/**
 * The caNanoLab Software License, Version 1.5
 *
 * Copyright 2006 SAIC. This software was developed in conjunction with the National
 * Cancer Institute, and so to the extent government employees are co-authors, any
 * rights in such works shall be subject to Title 17 of the United States Code,
 * section 105.
 *
 */
package gov.nih.nci.cananolab.ui.admin;

/**
 * Action class for updating created by field in all data.
 *
 * @author pansu
 */
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.service.admin.impl.UpdateCreatedByServiceImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class UpdateCreatedByAction extends AbstractDispatchAction {

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("input");
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SecurityService securityService = getSecurityService(request);
		String currentCreatedBy = (String) theForm.get("currentCreatedBy");
		String newCreatedBy = (String) theForm.get("newCreatedBy");
		ActionMessages messages = new ActionMessages();

		UpdateCreatedByServiceImpl updateService = new UpdateCreatedByServiceImpl();
		int failures = updateService.update(securityService, currentCreatedBy,
				newCreatedBy);
		if (failures > 0) {
			ActionMessage msg = new ActionMessage(
					"message.updateCreatedBy.failed", failures);
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		} else {
			ActionMessage msg = new ActionMessage(
					"message.updateCreatedBy.success");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		}
		saveMessages(request, messages);
		return mapping.findForward("success");
	}

	private SecurityService getSecurityService(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");

		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		return securityService;
	}
}
