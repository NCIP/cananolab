package gov.nih.nci.cananolab.ui.security;

import gov.nih.nci.cananolab.service.security.MailService;

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
 * This class allow users to register to caNanoLab.
 *
 * @author lethai
 */

public class RegisterUserAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		System.out.println("........ RegisterUserAction..............");
		String title = (String) theForm.get("title");
		String firstName = (String) theForm.get("firstName");
		String lastName = (String) theForm.get("lastName");
		String email = (String) theForm.get("email");
		String phone = (String) theForm.get("phone");
		String organization = (String) theForm.get("organization");
		String fax = (String) theForm.get("fax");
		String comment = (String) theForm.get("comment");
		String registerToUserList =(String)theForm.get("registerToUserList");
		System.out.println("register to user list: " + registerToUserList);
		
		MailService mailService = new MailService();
		mailService.sendRegistrationEmail(firstName, lastName, email, phone, organization, title, fax, comment);
		if("checked".equalsIgnoreCase(registerToUserList)){
			mailService.sendUsersListRegistration( email, firstName + " " + lastName);
		}
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage("message.register");
		messages.add("message", message);
		saveMessages(request, messages);
		return mapping.findForward("success");
	}
}
