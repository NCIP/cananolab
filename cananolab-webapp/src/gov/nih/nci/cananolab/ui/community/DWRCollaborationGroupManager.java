package gov.nih.nci.cananolab.ui.community;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.ui.publication.DWRPublicationManager;

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
	private Logger logger = Logger.getLogger(DWRPublicationManager.class);

	private CommunityService getService() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		service = new CommunityServiceLocalImpl(user);
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

	public UserBean[] getMatchedUsers(String searchStr)
			throws Exception {
		try {
			List<UserBean> matchedUsers = getService().findUserLoginNames(
					searchStr);
			return matchedUsers.toArray(new UserBean[matchedUsers.size()]);
		} catch (Exception e) {
			logger
					.error(
							"Problem getting matched user login names",
							e);
			return null;
		}
	}
}
