package gov.nih.nci.cananolab.restful.core;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.DWRAccessibilityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class AccessibilityManager {
	private BaseService service;
	private Logger logger = Logger.getLogger(DWRAccessibilityManager.class);
	SecurityService securityService;

	private BaseService getService() {
		WebContext wctx = WebContextFactory.get();
		securityService = (SecurityService) wctx.getSession().getAttribute(
				"securityService");
		service = new BaseServiceLocalImpl(securityService);
		return service;
	}

	public AccessibilityBean resetTheAccess(String parentFormName,
			String parentType) {
		DynaValidatorForm accessForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute(parentFormName));
		if (accessForm == null) {
			return null;
		}
		AccessibilityBean access = new AccessibilityBean();
		if (parentType.equalsIgnoreCase("sample")) {
			SampleBean sampleBean = (SampleBean) accessForm.get("sampleBean");
			sampleBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("protocol")) {
			ProtocolBean protocolBean = (ProtocolBean) accessForm
					.get("protocol");
			protocolBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("publication")) {
			PublicationBean publicationBean = (PublicationBean) accessForm
					.get("publication");
			publicationBean.setTheAccess(access);
		}
		return access;
	}

	public AccessibilityBean getGroupAccess(String parentFormName,
			String groupName, String parentType, String protectedData)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		AccessibilityBean access = getService().findAccessibilityByGroupName(
				groupName, protectedData);
		DynaValidatorForm accessForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute(parentFormName));
		if (accessForm == null) {
			return null;
		}
		if (parentType.equalsIgnoreCase("sample")) {
			SampleBean sampleBean = (SampleBean) accessForm.get("sampleBean");
			sampleBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("protocol")) {
			ProtocolBean protocolBean = (ProtocolBean) accessForm
					.get("protocol");
			protocolBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("publication")) {
			PublicationBean publicationBean = (PublicationBean) accessForm
					.get("publication");
			publicationBean.setTheAccess(access);
		}
		return access;
	}

	public AccessibilityBean getUserAccess(String parentFormName,
			String userLoginName, String parentType, String protectedData)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		AccessibilityBean access = getService()
				.findAccessibilityByUserLoginName(userLoginName, protectedData);
		DynaValidatorForm accessForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute(parentFormName));
		if (accessForm == null) {
			return null;
		}
		if (parentType.equalsIgnoreCase("sample")) {
			SampleBean sampleBean = (SampleBean) accessForm.get("sampleBean");
			sampleBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("protocol")) {
			ProtocolBean protocolBean = (ProtocolBean) accessForm
					.get("protocol");
			protocolBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("publication")) {
			PublicationBean publicationBean = (PublicationBean) accessForm
					.get("publication");
			publicationBean.setTheAccess(access);
		}
		return access;
	}

	public Map<String, String> getMatchedUsers(String dataOwner, String searchStr, HttpServletRequest request)
			throws Exception {
		try {

			SecurityService securityService = (SecurityService) request
					.getSession().getAttribute("securityService");
			BaseService service = new BaseServiceLocalImpl(securityService);
			List<UserBean> matchedUsers = service.findUserBeans(
					searchStr);
			List<UserBean> updatedUsers = new ArrayList<UserBean>(matchedUsers);
			// remove current user from the list
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			updatedUsers.remove(user);

			// remove data owner from the list if owner is not the current user
			if (!dataOwner.equalsIgnoreCase(user.getLoginName())) {
				for (UserBean userBean : matchedUsers) {
					if (userBean.getLoginName().equalsIgnoreCase(dataOwner)) {
						updatedUsers.remove(userBean);
						break;
					}
				}
			}
			// exclude curators;
			List<String> curators = securityService
					.getUserNames(AccessibilityBean.CSM_DATA_CURATOR);
			for (UserBean userBean : matchedUsers) {
				for (String curator : curators) {
					if (userBean.getLoginName().equalsIgnoreCase(curator)) {
						updatedUsers.remove(userBean);
					}
				}
			}

			//return updatedUsers.toArray(new UserBean[updatedUsers.size()]);
			Map<String, String> userMap = new HashMap<String, String>();
			for(UserBean bean : updatedUsers){
				userMap.put(bean.getLoginName(), bean.getLastName() + " " + bean.getFirstName());
			}
			return userMap;
		} catch (Exception e) {
			logger.error("Problem getting matched user login names", e);
			return null;
		}
	}

	public UserBean[] getUsers(String searchStr)
			throws Exception {
		try {
			List<UserBean> matchedUsers = getService().findUserBeans(
					searchStr);

			return matchedUsers.toArray(new UserBean[matchedUsers.size()]);
		} catch (Exception e) {
			logger.error("Problem getting user login names", e);
			return null;
		}
	}

	public String[] getMatchedGroupNames(String searchStr, HttpServletRequest request) throws Exception {
		try {
			SecurityService securityService = (SecurityService) request
					.getSession().getAttribute("securityService");
			BaseService service = new BaseServiceLocalImpl(securityService);
			List<String> matchGroupNames = service.findGroupNames(
					searchStr);
			return matchGroupNames.toArray(new String[matchGroupNames.size()]);
		} catch (Exception e) {
			logger.error("Problem getting matched group names", e);
			return null;
		}
	}
}


