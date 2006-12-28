package gov.nih.nci.calab.ui.security;

import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;

		HttpSession session = request.getSession(false);
		if (session != null) {
			//invalidate the old one
			session.invalidate();
			//create a new one
			session=request.getSession();
			ActionMessages msgs = new ActionMessages();
			ActionMessage error = new ActionMessage("msg.logout");
			msgs.add("msg", error);
			saveMessages(request, msgs);
		}		
		forward = mapping.findForward("success");
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}
}
