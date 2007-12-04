package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up the nanoparticle general information page and allows users to submit/update
 * the general information.  
 *  
 * @author pansu
 */

/* CVS $Id: RemoteNanoparticleGeneralInfoAction.java,v 1.6 2007-12-04 15:32:21 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

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
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteNanoparticleGeneralInfoAction extends AbstractDispatchAction {

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, GridNodeBean> gridNodes = null;
		ActionMessages msgs = new ActionMessages();
		HttpSession session = request.getSession();
		if (session.getAttribute("allGridNodes") == null) {
			try {
				gridNodes = GridService.discoverServices(
						CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
						CaNanoLabConstants.DOMAIN_MODEL_NAME);
				if (gridNodes == null) {
					ActionMessage msg = new ActionMessage(
							"message.grid.discovery.none",
							CaNanoLabConstants.DOMAIN_MODEL_NAME);
					msgs.add("message", msg);
					saveMessages(request, msgs);
					return mapping.findForward("remoteSearchMessage");
				} else {
					request.getSession()
							.setAttribute("allGridNodes", gridNodes);
				}
			} catch (Exception e) {
				ActionMessage msg = new ActionMessage("message.grid.discovery",
						CaNanoLabConstants.DOMAIN_MODEL_NAME, e);
				msgs.add("message", msg);
				saveMessages(request, msgs);
				return mapping.findForward("remoteSearchMessage");
			}
		} else {
			gridNodes = new HashMap<String, GridNodeBean>(
					(Map<? extends String, ? extends GridNodeBean>) request
							.getSession().getAttribute("allGridNodes"));
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = theForm.getString("particleName");
		String gridNodeHost = request.getParameter("gridNodeHost");
		GridNodeBean gridNode = gridNodes.get(gridNodeHost);
		// force data refresh on the side menu
		request.getSession().setAttribute("newRemoteParticleCreated", "true");

		InitParticleSetup.getInstance().setRemoteSideParticleMenu(request,
				particleName, gridNode);

		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return true;
	}
}
