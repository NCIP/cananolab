/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.security;

import static gov.nih.nci.cananolab.restful.RestfulConstants.SUCCESS;
import gov.nih.nci.cananolab.restful.RestfulConstants;
import gov.nih.nci.cananolab.service.security.MailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;

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

public class RegisterUserBO  {
	public String register(String title, 
    		String firstName,
    		String lastName,
    		String email,
    		String phone,
    		String organization,
    		String fax,
    		String comment,
    		String registerToUserList) {

		
		System.out.println("register to user list: " + registerToUserList);
		
		MailService mailService = new MailService();
		mailService.sendRegistrationEmail(firstName, lastName, email, phone, organization, title, fax, comment);
		if("checked".equalsIgnoreCase(registerToUserList)){
			mailService.sendUsersListRegistration( email, firstName + " " + lastName);
		}
//		ActionMessages messages = new ActionMessages();
//		ActionMessage message = new ActionMessage("message.register");
//		messages.add("message", message);
//		saveMessages(request, messages);
		return RestfulConstants.SUCCESS;
	}
}
