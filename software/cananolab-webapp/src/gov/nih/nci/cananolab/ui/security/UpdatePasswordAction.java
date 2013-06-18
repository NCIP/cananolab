/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.security;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This class allow users to update their passwords.
 * 
 * @author pansu
 */

public class UpdatePasswordAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String loginId = (String) theForm.get("loginId");
		String password = (String) theForm.get("password");
		String newPassword = (String) theForm.get("newPassword");
		ActionMessages messages = new ActionMessages();
		UserBean user = new UserBean(loginId, password);
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME, user);
			if (user != null) {
				service.updatePassword(newPassword);				
				ActionMessage message = new ActionMessage("message.password");
				messages.add("message", message);
				saveMessages(request, messages);
			}
			return mapping.findForward("success");
		} catch (Exception e) {
			ActionMessage msg = new ActionMessage("erros.login.failed");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, messages);
			return mapping.findForward("input");
		}
	}
}
