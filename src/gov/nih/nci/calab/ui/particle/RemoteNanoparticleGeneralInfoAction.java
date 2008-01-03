package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up the nanoparticle general information page and allows users to submit/update
 * the general information.  
 *  
 * @author pansu
 */

/* CVS $Id: RemoteNanoparticleGeneralInfoAction.java,v 1.8 2008-01-03 21:25:29 pansu Exp $ */

import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.BaseRemoteSearchAction;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteNanoparticleGeneralInfoAction extends BaseRemoteSearchAction {

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, GridNodeBean> gridNodes = prepareSearch(request);		
		ActionMessages msgs = new ActionMessages();
		if (gridNodes == null) {
			ActionMessage msg = new ActionMessage(
					"message.grid.discovery.none",
					CaNanoLabConstants.DOMAIN_MODEL_NAME);
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.getInputForward();
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
}
