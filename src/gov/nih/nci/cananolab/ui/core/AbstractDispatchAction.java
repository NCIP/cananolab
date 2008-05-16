package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public abstract class AbstractDispatchAction extends DispatchAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		if (!loginRequired()) {
			return super.execute(mapping, form, request, response);
		}

		String dispatch = request.getParameter("dispatch");
		if (Arrays.asList(CaNanoLabConstants.PUBLIC_DISPATCHES).contains(
				dispatch)) {
			return super.execute(mapping, form, request, response);
		}
		if (user != null) {
			// check whether user have access to the class
			boolean accessStatus = canUserExecute(user);
			if (accessStatus) {
				return super.execute(mapping, form, request, response);
			} else {
				request.getSession().removeAttribute("user");
				throw new NoAccessException();
			}
		} else {
			throw new InvalidSessionException();
		}
	}

	public abstract boolean loginRequired();

	/**
	 * Check whether the current user can execute the action
	 * 
	 * @param user
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public abstract boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException;
}