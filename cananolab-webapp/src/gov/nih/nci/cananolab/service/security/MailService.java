package gov.nih.nci.cananolab.service.security;

import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;


public class MailService {
	private static Logger logger = Logger.getLogger(MailService.class);

	private static final String CANANOLAB_USER_MAILING_LIST = "CANANOLAB-USERS-L@LIST.NIH.GOV";
	private static final String MAILING_LIST="LISTSERV@LIST.NIH.GOV";
	private String HOST = "mailfwd.nih.gov"; 
	private String FROM = "caNanoLab <do.not.reply@mail.nih.gov>";
	private String TO = "ncicbmb@mail.nih.gov";

	public MailService() {
		HOST = PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "mailServerHost");
		
		TO = PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "adminEmail");
		System.out.println("Admin email: " + TO);
		FROM = PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "doNoReply");
	}

	/**
	 * Send email to request LDAP account for user to use NCIA
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param phone
	 * @param organization
	 * @param title
	 */
	public  void sendRegistrationEmail(String firstName, String lastName, String emailAddress,
			String phone, String organization, String title, String fax, String reason){

		String greeting="caNanoLab Administrator";
		String body1="Dear " + greeting + ",\n\n";
		String body2= "Please setup account for the following user to access caNanoLab\n\n";
		String body3="First Name: " + firstName + "\nLast Name: " + lastName;
		String body4="\nEmail: " + emailAddress; 
		String body5 ="\nPhone number: " + phone;
		String body6="\nOrganization: " + organization;
		String body7="\nTitle: " + title;
		String body8="\nFax number: " + fax;
		String body9="\n\nRegards,\ncaNanoLab Team";

		//if title or fax are not empty, include them in email as well
		String message = body1 +	body2 + body3 + body4 + body5;
		if(!StringUtils.isEmpty(organization)){
			message +=body6;
		}	       

		if(!StringUtils.isEmpty(title)){
			message +=body7;
		}
		if(!StringUtils.isEmpty(fax)){
			message +=body8;
		}

		message +=body9;
		if(!StringUtils.isEmpty(reason)){
			message += "\n\nReason for requesting registration:\n"+ reason;
		}
		System.out.println("\n" +message);

		// Send the message
		System.out.println("TO: " + TO);
		sendMail(TO, emailAddress, message, "caNanoLab User Registration");
		//sendMail("lethai@mail.nih.gov", emailAddress, message, "caNanoLab User Registration");
		//sendMail("thai.t.le@gmail.com", emailAddress, message, "caNanoLab User Registration");
	}

	public void sendUsersListRegistration(String emailAddress, String name) {
		/*
		 * How do I subscribe to a LISTSERV list?
		 * Send e-mail to LISTSERV@LIST.NIH.GOV with the following text in the message body:
		 * subscribe listname your name
		 * where listname is the name of the list you wish to subscribe to, and your name is your name.
		 * (LISTSERV will get your e-mail address from the "From:" address of your e-mail message.)
		 */

		String message = "subscribe "+CANANOLAB_USER_MAILING_LIST+" "+name;
		sendMail(MAILING_LIST,  emailAddress, message, "");
	}

	/**
	 * Send a mail message where the FROM is set to what is is from variable.
	 */
	public void sendMail(String mailTo, 
			String mailBody,
			String subject) {
		try {
			//get system properties
			Properties props = System.getProperties();
			
			// Set up mail server
			props.put("mail.smtp.host", HOST);

			//Get session
			Session session = Session.getDefaultInstance(props, null);

			//Define Message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM));
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(mailTo));
			message.setSubject(subject);
			message.setText(mailBody);			

			//Send Message
			Transport.send(message);
		} catch (Exception e) {
			logger.error("Send Mail error", e);
		}
	}

	public void sendMail(String mailTo, 
			String mailFrom, 
			String mailBody,
			String subject) {
		try {
			//get system properties
			Properties props = System.getProperties();

			// Set up mail server
			props.put("mail.smtp.host", HOST);

			//Get session
			Session session = Session.getDefaultInstance(props, null);

			//Define Message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(mailTo));
			message.setSubject(subject);
			message.setText(mailBody);

			//Send Message
			Transport.send(message);
			System.out.println("message sent");
		} catch (Exception e) {
			logger.error("Send Mail error", e);
		} 
	} 
}
