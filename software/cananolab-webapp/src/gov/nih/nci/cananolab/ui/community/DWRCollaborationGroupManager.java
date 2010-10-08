package gov.nih.nci.cananolab.ui.community;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu
 *
 */
public class DWRCollaborationGroupManager {
	private CommunityService service;
	private Logger logger = Logger
			.getLogger(DWRCollaborationGroupManager.class);
	private SecurityService securityService;
	private CommunityService getService() throws Exception {
		WebContext wctx = WebContextFactory.get();
		securityService = (SecurityService) wctx.getSession()
				.getAttribute("securityService");
		service = new CommunityServiceLocalImpl(securityService);
		return service;
	}

	public CollaborationGroupBean resetTheCollaborationGroup() {
		DynaValidatorForm collaborationGroupForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("collaborationGroupForm"));
		if (collaborationGroupForm == null) {
			return null;
		}
		CollaborationGroupBean group = new CollaborationGroupBean();
		collaborationGroupForm.set("group", group);
		return group;
	}

	public CollaborationGroupBean getCollaborationGroupById(String id)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		CollaborationGroupBean group = getService().findCollaborationGroupById(
				id);
		DynaValidatorForm collaborationGroupForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("collaborationGroupForm"));
		collaborationGroupForm.set("group", group);
		return group;
	}

	public CollaborationGroupBean addUserAccess(AccessibilityBean userAccess)
			throws Exception {
		DynaValidatorForm collaborationGroupForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("collaborationGroupForm"));
		if (collaborationGroupForm == null) {
			return null;
		}
		CollaborationGroupBean group = (CollaborationGroupBean) (collaborationGroupForm
				.get("group"));
		group.addUserAccess(userAccess);
		return group;
	}

	public CollaborationGroupBean deleteUserAccess(AccessibilityBean userAccess)
			throws Exception {
		DynaValidatorForm collaborationGroupForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("collaborationGroupForm"));
		if (collaborationGroupForm == null) {
			return null;
		}
		CollaborationGroupBean group = (CollaborationGroupBean) (collaborationGroupForm
				.get("group"));
		group.removeUserAccess(userAccess);
		return group;
	}

	public UserBean[] getMatchedUsers(String groupOwner, String searchStr) throws Exception {
		try {
			List<UserBean> matchedUsers = getService().findUserBeans(
					searchStr);
			List<UserBean> updatedUsers = new ArrayList<UserBean>(matchedUsers);
			// remove current user from the list
			WebContext wctx = WebContextFactory.get();
			UserBean user = (UserBean) wctx.getSession().getAttribute("user");
			updatedUsers.remove(user);
			// remove data owner from the list if owner is not the current user
			if (!groupOwner.equalsIgnoreCase(user.getLoginName())) {
				for (UserBean userBean : matchedUsers) {
					if (userBean.getLoginName().equalsIgnoreCase(groupOwner)) {
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
			return updatedUsers.toArray(new UserBean[updatedUsers.size()]);
		} catch (Exception e) {
			logger.error("Problem getting matched user login names", e);
			return null;
		}
	}
}
