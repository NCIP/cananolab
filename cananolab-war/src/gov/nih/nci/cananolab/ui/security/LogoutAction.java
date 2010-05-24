package gov.nih.nci.cananolab.ui.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
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

public class LogoutAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			// invalidate the old one
			session.invalidate();
		}
		ActionMessages msgs = new ActionMessages();
		ActionMessage message = new ActionMessage("msg.logout");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, message);
		saveMessages(request, msgs);

		forward = mapping.findForward("success");
		resetToken(request);
		
		//FR# 26489,Visitor Count: set marker in request to avoid counting.
		request.setAttribute("justLogout", Boolean.TRUE);
		return forward;
	}
}
