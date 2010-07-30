package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Enumeration;

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
		String dispatch = (String) getValueFromRequest(request, "dispatch");
		UserBean user = (UserBean) session.getAttribute("user");
		// private dispatch and session expired
		boolean privateDispatch = isDispatchPublic(dispatch);
		if (session.isNew() && (dispatch == null || privateDispatch)) {
			throw new InvalidSessionException();
		}
		String protectedData = request.getParameter("sampleId");
		if (protectedData == null) {
			protectedData = request.getParameter("publicationId");
		}
		if (protectedData == null) {
			protectedData = request.getParameter("protocolId");
		}
		boolean executeStatus = canUserExecute(request, dispatch, protectedData);
		if (executeStatus) {
			return super.execute(mapping, form, request, response);
		} else {
			if (user == null) {
				throw new NoAccessException("Log in is required");
			}
			request.getSession().removeAttribute("user");
			throw new NoAccessException();
		}
	}

	/**
	 * Check whether the current user can execute the action with the specified
	 * dispatch
	 *
	 * @param user
	 * @return
	 * @throws SecurityException
	 */
	public boolean canUserExecute(HttpServletRequest request, String dispatch,
			String protectedData) throws SecurityException {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// private dispatch in public actions
		boolean privateDispatch = isDispatchPublic(dispatch);
		if (!privateDispatch) {
			return true;
		} else if (user == null && privateDispatch) {
			return false;
		} else {
			return canUserExecutePrivateDispatch(request, protectedData);
		}
	}

	public Boolean canUserExecutePrivateDispatch(HttpServletRequest request,
			String protectedData) throws SecurityException {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null) {
			return false;
		}
		return true;
	}

	public boolean isDispatchPublic(String dispatch) {
		if (dispatch != null) {
			for (String theDispatch : Constants.PRIVATE_DISPATCHES) {
				if (dispatch.startsWith(theDispatch)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the page number used in display tag library pagination
	 *
	 * @param request
	 * @return
	 */
	public int getDisplayPage(HttpServletRequest request) {
		int page = 0;
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String name = (String) paramNames.nextElement();
			if (name != null && name.startsWith("d-") && name.endsWith("-p")) {
				String pageValue = request.getParameter(name);
				if (pageValue != null) {
					page = Integer.parseInt(pageValue) - 1;
				}
			}
		}
		return page;
	}

	public String getBrowserDispatch(HttpServletRequest request) {
		String dispatch = request.getParameter("dispatch");
		// get the dispatch value from the URL in the browser address bar
		// used in case of validation
		if (dispatch != null
				&& request.getAttribute("javax.servlet.forward.query_string") != null) {
			String browserQueryString = request.getAttribute(
					"javax.servlet.forward.query_string").toString();
			if (!StringUtils.isEmpty(browserQueryString)) {
				String browserDispatch = browserQueryString.replaceAll(
						"dispatch=(.+)&(.+)", "$1");
				return browserDispatch;
			}
		}
		return "";
	}

	/**
	 * Retrieve a value from request by name in the order of Parameter - Request
	 * Attribute - Session Attribute
	 *
	 * @param request
	 * @param name
	 * @return
	 */
	public Object getValueFromRequest(HttpServletRequest request, String name) {
		Object value = request.getParameter(name);
		if (value == null) {
			value = request.getAttribute(name);
		}
		if (value == null) {
			value = request.getSession().getAttribute(name);
		}
		return value;
	}

	protected SecurityService getSecurityServiceFromSession(
			HttpServletRequest request) throws Exception {
		if (request.getSession().getAttribute("securityService") == null) {
			UserBean user = (UserBean) request.getAttribute("user");
			SecurityService service = new SecurityService(
					Constants.CSM_APP_NAME, user);
			return service;
		}
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		return securityService;
	}
}