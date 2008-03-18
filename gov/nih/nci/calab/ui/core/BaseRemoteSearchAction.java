package gov.nih.nci.calab.ui.core;

/**
 * This class remotely searches nanoparticle metadata across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: BaseRemoteSearchAction.java,v 1.5 2008-03-18 15:32:46 cais Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

		if(request.getSession().getAttribute("allGridNodes") == null) {
			gridNodes = GridService.discoverServices(
				CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
				CaNanoLabConstants.DOMAIN_MODEL_NAME);
			request.getSession().setAttribute("allGridNodes", gridNodes);
		}
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
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
		} else {
			String gridNodeHostStr =(String) request.getParameter("gridNodeHost");
			if(gridNodeHostStr != null) {
				String[] selectedGridNodeHosts = gridNodeHostStr.split("~");
				List<String> gridlist = Arrays.asList(selectedGridNodeHosts);
				request.getSession().setAttribute("selectedGridNodeHosts", gridlist);
			
				List<String> unselectedGridNodeHosts = new ArrayList<String>();
				for(String hostNode: gridNodes.keySet()) {
					if(!gridlist.contains(hostNode)) {
						unselectedGridNodeHosts.add(hostNode);
					}
				}
				request.getSession().setAttribute("unselectedGridNodeHosts", unselectedGridNodeHosts);
			}
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
