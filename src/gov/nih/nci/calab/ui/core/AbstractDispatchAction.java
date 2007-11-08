package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.exception.NoAccessException;

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

		// response.setHeader("Cache-Control", "no-cache");

		if (!loginRequired()) {
			return super.execute(mapping, form, request, response);
		}
		String dispatch = request.getParameter("dispatch");
		if (dispatch.equals("setupView") || dispatch.equals("download")
				|| dispatch.equals("loadFile")) {
			return super.execute(mapping, form, request, response);
		}
		if (user != null) {
			// check whether user have access to the class
			boolean accessStatus = canUserExecute(user);
			// if have access or if have no access but dispatch is either
			// setupView or download or loadfile
			// do forward
			if (accessStatus) {
				return super.execute(mapping, form, request, response);
			} else {
				request.getSession().removeAttribute("user");
				throw new NoAccessException("You don't have access to class: "
						+ this.getClass().getName());
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
	 * @throws Exception
	 */
	public abstract boolean canUserExecute(UserBean user) throws Exception;
}