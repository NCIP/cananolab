/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.security;

import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.service.security.MailService;

//import org.apache.commons.validator.routines;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Component;

/**
 * This class allow users to register to caNanoLab.
 *
 * @author lethai
 */

@Component("registerUserBO")
public class RegisterUserBO  {
	
	public List<String> register(String title, 
    		String firstName,
    		String lastName,
    		String email,
    		String phone,
    		String organization,
    		String fax,
    		String comment,
    		String registerToUserList) {

		System.out.println("register to user list: " + registerToUserList);
		
		List<String> errors = validateInput(firstName, lastName, email, phone, organization, fax, comment);
		if (errors != null && errors.size() > 0)
			return errors;
		
		MailService mailService = new MailService();
		mailService.sendRegistrationEmail(firstName, lastName, email, phone, organization, title, fax, comment);
		if("checked".equalsIgnoreCase(registerToUserList)){
			mailService.sendUsersListRegistration( email, firstName + " " + lastName);
		}
		
		return null;
	}
	
	protected List<String> validateInput(String firstName,
    		String lastName,
    		String email,
    		String phone,
    		String organization,
    		String fax,
    		String comment) {
		
		List<String> errors = new ArrayList<String>();
		if (firstName == null || !InputValidationUtil.isAlphabetic(firstName))
			errors.add(PropertyUtil.getProperty("application", "firstName.invalid"));
		
		if (lastName == null || !InputValidationUtil.isAlphabetic(lastName))
			errors.add(PropertyUtil.getProperty("application", "lastName.invalid"));
		
		EmailValidator emailValidator = EmailValidator.getInstance();
		if (email == null || !emailValidator.isValid(email))
			errors.add("Email is invalid");
		
		if (phone == null || !InputValidationUtil.isPhoneValid(phone))
			errors.add(PropertyUtil.getProperty("application", "phone.invalid"));
		
		if (fax == null || !InputValidationUtil.isPhoneValid(fax))
			errors.add(PropertyUtil.getProperty("application", "fax.invalid"));
		
		if (organization == null || organization.length() == 0)
			errors.add(PropertyUtil.getProperty("application", "organization.name.invalid"));
		
		if (comment != null && comment.length() > 4000)
			errors.add("Comment exceeded the max length of 4000");
		
		return errors;
	}
}
