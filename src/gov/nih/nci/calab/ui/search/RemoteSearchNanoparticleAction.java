package gov.nih.nci.calab.ui.search;

/**
 * This class remotely searches nanoparticle metadata across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: RemoteSearchNanoparticleAction.java,v 1.5 2007-03-12 18:49:47 pansu Exp $ */

import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.search.GridSearchService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteSearchNanoparticleAction extends AbstractDispatchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		String[] characterizations = (String[]) theForm
				.get("characterizations");

		String[] gridNodeHosts = (String[]) theForm.get("gridNodes");
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		GridNodeBean[] gridNodes = GridService.getGridNodesFromHostNames(
				gridNodeMap, gridNodeHosts);
		GridSearchService searchService = new GridSearchService();
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		ActionMessages msgs = new ActionMessages();
		for (GridNodeBean gridNode : gridNodes) {
			try {
				List<ParticleBean> gridParticles = searchService
						.getRemoteNanoparticles(particleType, functionTypes,
								characterizations, gridNode);
				particles.addAll(gridParticles);
			} catch (RemoteException e) {				
				ActionMessage msg = new ActionMessage(
						"message.searchNanoparticle.grid.notAvailable",
						gridNode.getHostName(), e.getMessage());
				msgs.add("message", msg);
				saveMessages(request, msgs);
			} catch (MalformedURLException e) {				
				ActionMessage msg = new ActionMessage(
						"message.searchNanoparticle.grid.notAvailable", gridNode
								.getHostName(), e.getMessage());
				msgs.add("message", msg);
				saveMessages(request, msgs);
			}
		}

		ActionForward forward = null;		
		if (!particles.isEmpty()) {
			request.setAttribute("remoteParticles", particles);
			forward = mapping.findForward("success");
		} else {
			// ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchNanoparticle.noresult");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		}

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();

		Map<String, GridNodeBean> gridNodes = GridService.discoverServices(
				CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
				CaNanoLabConstants.DOMAIN_MODEL_NAME);
		request.getSession().setAttribute("allGridNodes", gridNodes);
		InitSessionSetup.getInstance().setAllParticleFunctionTypes(session);
		InitSessionSetup.getInstance()
				.setCharacterizationTypeCharacterizations(session);
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
