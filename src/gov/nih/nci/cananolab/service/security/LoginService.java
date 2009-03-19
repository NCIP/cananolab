package gov.nih.nci.cananolab.service.security;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * The LoginService authenticates users into the caNanoLab system.
 *
 * @author pansu
 *
 */

public class LoginService {
	private static Logger logger = Logger.getLogger(LoginService.class);

	private String applicationName = null;

	private AuthenticationManager authenticationManager = null;

	private AuthorizationManager authorizationManager = null;

	/**
	 * LoginService Constructor
	 *
	 * @param strname
	 *            name of the application
	 */

	public LoginService(String strname) throws SecurityException {
		this.applicationName = strname;
		try {
			authenticationManager = SecurityServiceProvider
					.getAuthenticationManager(this.applicationName);
			authorizationManager = SecurityServiceProvider
					.getAuthorizationManager(applicationName);
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
	 * @return boolean indicating whether the user successfully authenticated
	 */
	public boolean login(String strUsername, String strPassword)
			throws SecurityException {
		try {
			return authenticationManager.login(strUsername, strPassword);
		} catch (Exception e) {
			logger.error(e);
			throw new SecurityException("Invalid Credentials");
		}
	}

	/**
	 * Get all users in the application
	 *
	 * @return
	 * @throws SecurityException
	 */
	public List<UserBean> getAllUsers() throws SecurityException {
		try {
			List<UserBean> users = new ArrayList<UserBean>();
			User dummy = new User();
			dummy.setLoginName("*");
			SearchCriteria sc = new UserSearchCriteria(dummy);
			List results = authorizationManager.getObjects(sc);
			for (Object obj : results) {
				User doUser = (User) obj;
				users.add(new UserBean(doUser));
			}
			return users;
		} catch (Exception e) {
			logger.error("Error in getting all users.", e);
			throw new SecurityException();
		}
	}

	/**
	 * Set a new password for the given user login name
	 *
	 * @param loginName
	 * @param newPassword
	 * @throws SecurityException
	 */
	public void updatePassword(String loginName, String newPassword)
			throws SecurityException {
		try {
			User user = this.authorizationManager.getUser(loginName);
			user.setPassword(newPassword);
			authorizationManager.modifyUser(user);
		} catch (Exception e) {
			logger.error("Error in updating password.", e);
			throw new SecurityException();
		}
	}

	/**
	 * Initialize user's password to be the same as the user name
	 *
	 * @param userName
	 * @return
	 */
	public void initializePassword(String userName) throws SecurityException {
		try {
			User user = authorizationManager.getUser(userName);
			// encryption is included in setPassword already
			user.setPassword(userName);
			authorizationManager.modifyUser(user);
		} catch (Exception e) {
			logger.error(e);
			throw new SecurityException("Can't initialize password");
		}
	}

	public void initializeAllUserPasswords() {
		try {
			List<UserBean> users = getAllUsers();
			for (UserBean user : users) {
				initializePassword(user.getLoginName());
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void main(String[] args) {
		try {
			LoginService service = new LoginService(Constants.CSM_APP_NAME);
			service.initializeAllUserPasswords();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
