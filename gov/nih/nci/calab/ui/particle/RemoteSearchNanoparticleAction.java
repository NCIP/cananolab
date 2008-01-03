package gov.nih.nci.calab.ui.particle;

/**
 * This class remotely searches nanoparticle metadata across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: RemoteSearchNanoparticleAction.java,v 1.7 2008-01-03 21:25:29 pansu Exp $ */

import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.BaseRemoteSearchAction;

import java.util.ArrayList;
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
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteSearchNanoparticleAction extends BaseRemoteSearchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		String[] characterizations = (String[]) theForm
				.get("characterizations");

		String[] gridNodeHosts = (String[]) theForm.get("gridNodes");
		ActionMessages msgs = new ActionMessages();

		Map<String, GridNodeBean> gridNodes = prepareSearch(request);
		if (gridNodes == null) {
			ActionMessage msg = new ActionMessage(
					"message.grid.discovery.none",
					CaNanoLabConstants.DOMAIN_MODEL_NAME);
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		GridNodeBean[] selectedGridNodes = GridService
				.getGridNodesFromHostNames(gridNodes, gridNodeHosts);

		GridSearchService searchService = new GridSearchService();
		List<ParticleBean> particles = new ArrayList<ParticleBean>();

		for (GridNodeBean gridNode : selectedGridNodes) {
			try {
				List<ParticleBean> gridParticles = searchService
						.getRemoteNanoparticles(particleType, functionTypes,
								characterizations, gridNode);
				if (gridParticles.size() == 0) {
					ActionMessage message = new ActionMessage(
							"message.remoteSearchNanoparticle.noresult",
							gridNode.getHostName());
					msgs.add(ActionMessages.GLOBAL_MESSAGE, message);
					saveMessages(request, msgs);
				}
				particles.addAll(gridParticles);
			} catch (Exception e) {
				ActionMessage error = new ActionMessage(
						"error.grid.notAvailable", gridNode.getHostName());
				msgs.add(ActionMessages.GLOBAL_MESSAGE, error);
				saveErrors(request, msgs);
				e.printStackTrace();
			}
		}
		ActionForward forward = null;
		if (!particles.isEmpty()) {
			request.getSession().setAttribute("remoteParticles", particles);
			forward = mapping.findForward("success");
		} else {
			// ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchNanoparticle.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		}

		return forward;
	}

	protected Map<String, GridNodeBean> initSetup(HttpServletRequest request)
			throws Exception {
		Map<String, GridNodeBean> gridNodes = super.initSetup(request);
		HttpSession session = request.getSession();
		InitParticleSetup.getInstance().setAllFunctionTypes(session);
		InitParticleSetup.getInstance().setAllCharacterizationTypes(session);
		return gridNodes;
	}
}
