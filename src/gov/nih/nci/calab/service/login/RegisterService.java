package gov.nih.nci.calab.service.login;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSTransactionException;

import org.apache.log4j.Logger;

/**
 * @author zengje
 *
 */
public class RegisterService {
	
	private String applicationName = null;
	private UserProvisioningManager upm = null;

	private static Logger logger = Logger.getLogger(RegisterService.class);
	
	public RegisterService() throws CalabException {
		super();
		this.applicationName = PropertyReader.getProperty(CalabConstants.CALAB_PROPERTY,"application");
		try {
			this.upm = SecurityServiceProvider.getUserProvisioningManager(this.applicationName);	
		} catch (CSException csEx) {
			csEx.printStackTrace();
			logger.error("Error while retrieving CSM UserProvisioningManager: " + csEx.getMessage());
			throw new CalabException("Error while retrieving CSM UserProvisioningManager: " + csEx.getMessage());
		}
	}

	public void register(UserBean userBean) throws CalabException{
		User user = new User();
		user.setDepartment(userBean.getDepartment());
		user.setEmailId(userBean.getEmail());
		user.setFirstName(userBean.getFirstName());
		user.setLastName(userBean.getLastName());
		user.setLoginName(userBean.getLoginId());
		user.setOrganization(userBean.getOrganization());
		user.setPassword(userBean.getPassword());
		user.setPhoneNumber(userBean.getPhoneNumber());
		user.setTitle(userBean.getTitle());
	
		try {
			upm.createUser(user);
		} catch (CSTransactionException cstEx) {
			logger.error("Error creating user: " + cstEx.getMessage());
			throw new CalabException("The login ID is already in use");
		}


	}
}
