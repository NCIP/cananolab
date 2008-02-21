package gov.nih.nci.calab.ui.particle;

/**
 * This class remotely searches nanoparticle metadata across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: RemoteSearchNanoparticleAction.java,v 1.9 2008-02-21 16:58:36 cais Exp $ */

import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.BaseRemoteSearchAction;

import java.io.PrintWriter;
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
		
	public ActionForward publicCounts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String particleType = "";
		String[] functionTypes = new String[0];
		String[] characterizations = new String[0];
		String[] gridNodeHosts = new String[1];
		String gridNodeHost =(String) request.getParameter("gridNodeHost");

		if(!gridNodeHost.equals("allLocations")) {
			gridNodeHosts[0] = gridNodeHost;
		} else {
			gridNodeHosts = new String[0];
		}
		ActionMessages msgs = new ActionMessages();

		Map<String, GridNodeBean> gridNodes = prepareSearch(request);
		if (gridNodes == null || gridNodes.size() == 0) {
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

		//particle count
		int particleCount = 0;
		for (GridNodeBean gridNode : selectedGridNodes) {
			try {
				particleCount += searchService
						.getRemoteNanoparticleCount(particleType, functionTypes,
								characterizations, gridNode);
				
			} catch (Exception e) {
				ActionMessage error = new ActionMessage(
						"error.grid.notAvailable", gridNode.getHostName());
				msgs.add(ActionMessages.GLOBAL_MESSAGE, error);
				saveErrors(request, msgs);
				e.printStackTrace();
			}
		}
		
		// report count
		String reportTitle = "";
		int reportCount = 0;
		for (GridNodeBean gridNode : selectedGridNodes) {
			try {
				reportCount += searchService.getRemoteReportCount(
						reportTitle, particleType, functionTypes,
						gridNode);
			} catch (Exception e) {
				ActionMessage message = new ActionMessage(
						"error.grid.notAvailable", gridNode.getHostName());
				msgs.add(ActionMessages.GLOBAL_MESSAGE, message);
				saveErrors(request, msgs);
				e.printStackTrace();
			}
		}
		
		// protocol count
		int protocolCount = 0;
		
		PrintWriter out = response.getWriter();
	    out.print(particleCount + "\t" + reportCount + "\t" + protocolCount);
		return null;
	}
	
	public ActionForward publicSearch(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String particleType = "";
			String[] functionTypes = new String[0];
			String[] characterizations = new String[0];
			String[] gridNodeHosts = new String[1];
			String gridNodeHost =(String) request.getParameter("gridNodeHost");

			if(!gridNodeHost.equals("allLocations")) {
				gridNodeHosts[0] = gridNodeHost;
			} else {
				gridNodeHosts = new String[0];
			}
			ActionMessages msgs = new ActionMessages();

			Map<String, GridNodeBean> gridNodes = prepareSearch(request);
			if (gridNodes == null || gridNodes.size() == 0) {
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
				//if already errors, don't show message
				if (getErrors(request).isEmpty()) {
					ActionMessage msg = new ActionMessage(
							"message.searchNanoparticle.noresult");
					msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
					saveMessages(request, msgs);
				}
				forward = mapping.getInputForward();
			}
			return forward;
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		String[] characterizations = (String[]) theForm
				.get("characterizations");

		for(int i=0; i<functionTypes.length; i++) 
			System.out.println("ftype:" + i + ", " + functionTypes[i]);
		for(int i=0; i<characterizations.length; i++) 
			System.out.println("ctype:" + i + ", " + characterizations[i]);
		
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
			//if already errors, don't show message
			if (getErrors(request).isEmpty()) {
				ActionMessage msg = new ActionMessage(
						"message.searchNanoparticle.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
			}
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
