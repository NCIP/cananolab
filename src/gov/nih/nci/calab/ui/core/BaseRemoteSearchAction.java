package gov.nih.nci.calab.ui.core;

/**
 * This class remotely searches nanoparticle metadata across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: BaseRemoteSearchAction.java,v 1.2 2007-12-06 09:01:44 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class BaseRemoteSearchAction extends AbstractDispatchAction {
	protected Map<String, GridNodeBean> initSetup(HttpServletRequest request)
			throws Exception {
		Map<String, GridNodeBean> gridNodes = null;
		// set grid nodes map in the session

		gridNodes = GridService.discoverServices(
				CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
				CaNanoLabConstants.DOMAIN_MODEL_NAME);
		request.getSession().setAttribute("allGridNodes", gridNodes);

		return gridNodes;
	}

	protected Map<String, GridNodeBean> prepareSearch(HttpServletRequest request)
			throws Exception {
		Map<String, GridNodeBean> gridNodes = null;
		HttpSession session = request.getSession();
		// try to get from session if possible
		if (session.getAttribute("allGridNodes") == null) {
			gridNodes = initSetup(request);
		} else {
			gridNodes = new HashMap<String, GridNodeBean>(
					(Map<? extends String, ? extends GridNodeBean>) request
							.getSession().getAttribute("allGridNodes"));
		}
		return gridNodes;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, GridNodeBean> gridNodes = initSetup(request);
		if (gridNodes == null) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.grid.discovery.none",
					CaNanoLabConstants.DOMAIN_MODEL_NAME);
			msgs.add("message", msg);
			saveMessages(request, msgs);
		}
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return true;
	}
}
