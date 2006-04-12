package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * This action logs user out of the current session
 * 
 * @author pansn
 */

/* CVS $Id: */

public class LogoutAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(LogoutAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		HttpSession session = request.getSession(false);
		try {
			if (session != null) {			
				session.invalidate();
				ActionMessage error = new ActionMessage("msg.logout");
				msgs.add("msg", error);
				saveMessages(request, msgs);
			}
			forward = mapping.findForward("success");
		} catch (Exception e) {
			ActionMessage error = new ActionMessage("error.logout");
			msgs.add("error", error);
			saveMessages(request, msgs);
			logger.error("Error in logging out the user", e);
			forward = mapping.findForward("failure");
		}
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}
}
