package gov.nih.nci.cananolab.service;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class BaseServiceHelper {
	private SecurityService securityService;
	protected Logger logger = Logger.getLogger(BaseServiceHelper.class);
	protected List<String> accessibleData;
	protected Map<String, String> accessibleDataRole;
	private UserBean user;

	public BaseServiceHelper() {
		try {
			securityService = new SecurityService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public BaseServiceHelper(UserBean user) {
		this.user = user;
		try {
			securityService = new SecurityService(Constants.CSM_APP_NAME, user);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public BaseServiceHelper(SecurityService securityService) {
		if (securityService == null) {
			try {
				securityService = new SecurityService(Constants.CSM_APP_NAME);
			} catch (Exception e) {
				logger.error("Can't create authorization service: " + e);
			}
		} else {
			this.securityService = securityService;
			this.user = securityService.getUserBean();
		}
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	protected List<String> getAccessibleData() throws Exception {
		if (accessibleData == null) {
			// when user is null, accessible data are public data
			accessibleData = getSecurityService().getAllUserAccessibleData();
		}
		return accessibleData;
	}

	public Map<String, String> getAccessibleDataRole() throws Exception {
		if (accessibleDataRole == null) {
			accessibleDataRole = getSecurityService()
					.getAllUserAccessibleDataAndRole();
		}
		return accessibleDataRole;
	}
}
