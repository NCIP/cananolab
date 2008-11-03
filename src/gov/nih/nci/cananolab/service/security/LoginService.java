package gov.nih.nci.cananolab.service.security;

import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.SecurityServiceProvider;

import org.apache.log4j.Logger;

/**
 * The LoginService authenticates users into the calab system.
 * 
 * @author doswellj
 * @param applicationName
 *            sets the application name for use by CSM
 * @param am
 *            Authentication Manager for CSM.
 */

public class LoginService {
	private Logger logger = Logger.getLogger(LoginService.class);

	String applicationName = null;

	AuthenticationManager am = null;

	/**
	 * LoginService Constructor
	 * 
	 * @param strname
	 *            name of the application
	 */

	public LoginService(String strname) throws SecurityException {
		this.applicationName = strname;
		try {
			this.am = SecurityServiceProvider
					.getAuthenticationManager(this.applicationName);

		} catch (Exception e) {
			logger.error(e);
			throw new SecurityException();
		}

	}

	/**
	 * The login method uses CSM to authenticated the user with LoginId and
	 * Password credentials
	 * 
	 * @param strusername
	 *            LoginId of the user
	 * @param strpassword
	 *            Encrypted password of the user
	 * @return boolean identicating whether the user successfully authenticated
	 */
	public boolean login(String strUsername, String strPassword)
			throws CaNanoLabSecurityException {
		try {
			return this.am.login(strUsername, strPassword);
		} catch (Exception e) {
			logger.error(e);
			throw new CaNanoLabSecurityException("Invalid Credentials");
		}
	}
}
