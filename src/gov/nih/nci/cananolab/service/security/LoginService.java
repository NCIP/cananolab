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

	private AuthorizationService authService = null;

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
			authService = new AuthorizationService(applicationName);
		} catch (Exception e) {
			logger.error(e);
			throw new SecurityException();
		}

	}

	/**
	 * Uses CSM to authenticate the given user and password.  If
	 * user is authenticated, check if the user is an admin or is a curator.
	 * @param userName
	 * @param password
	 * @return
	 * @throws SecurityException
	 */
	public UserBean login(String userName, String password)
			throws SecurityException {
		UserBean userBean = null;
		try {
			boolean authenticated = authenticationManager.login(userName,
					password);
			if (authenticated) {
				User user = authorizationManager.getUser(userName);
				userBean = new UserBean(user);
				// check if user is curator and if user is admin
				userBean.setAdmin(authService.isAdmin(userBean));
				userBean.setCurator(authService.isUserInGroup(userBean,
						Constants.CSM_DATA_CURATOR));
				return userBean;
			}
		} catch (Exception e) {
			logger.error(e);
			throw new SecurityException("Invalid Credentials");
		}
		return userBean;
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
