package gov.nih.nci.calab.ui.search;

/**
 * This class remotely searches nanoparticle report across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: RemoteSearchReportAction.java,v 1.12 2007-06-19 20:14:29 pansu Exp $ */

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.exception.CalabException;
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

public class RemoteSearchReportAction extends AbstractDispatchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String reportTitle = (String) theForm.get("reportTitle");
		String reportType = (String) theForm.get("reportType");
		String particleType = (String) theForm.get("particleType");
		String[] functionTypes = (String[]) theForm.get("functionTypes");

		String[] gridNodeHosts = (String[]) theForm.get("gridNodes");
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		GridNodeBean[] gridNodes = GridService.getGridNodesFromHostNames(
				gridNodeMap, gridNodeHosts);
		GridSearchService searchService = new GridSearchService();
		List<LabFileBean> reports = new ArrayList<LabFileBean>();
		ActionMessages msgs = new ActionMessages();
		for (GridNodeBean gridNode : gridNodes) {
			try {
				List<LabFileBean> gridReports = searchService.getRemoteReports(
						reportTitle, reportType, particleType, functionTypes,
						gridNode);
				reports.addAll(gridReports);
			} catch (RemoteException e) {
				ActionMessage msg = new ActionMessage(
						"message.searchReport.grid.notAvailable", gridNode
								.getHostName(), e);
				msgs.add("message", msg);
				saveMessages(request, msgs);
			} catch (MalformedURLException e) {
				ActionMessage msg = new ActionMessage(
						"message.searchReport.grid.notAvailable", gridNode
								.getHostName(), e);
				msgs.add("message", msg);
				saveMessages(request, msgs);
			}
		}
		if (!reports.isEmpty()) {
			request.setAttribute("remoteReports", reports);
			forward = mapping.findForward("success");
		} else {
			ActionMessage msg = new ActionMessage(
					"message.searchReport.noresult");
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
		InitSessionSetup.getInstance().setAllFunctionTypes(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		ActionMessages msgs = new ActionMessages();
		try {
			Map<String, GridNodeBean> gridNodes = GridService.discoverServices(
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
				request.getSession().setAttribute("allGridNodes", gridNodes);
			}
		} catch (Exception e) {
			ActionMessage msg = new ActionMessage("message.grid.discovery",
					CaNanoLabConstants.DOMAIN_MODEL_NAME, e);
			msgs.add("message", msg);
			saveMessages(request, msgs);
			return mapping.findForward("remoteSearchMessage");
		}
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}

	/**
	 * Download action to handle report downloading and viewing through caGrid
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download0(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		String fileName = request.getParameter("fileName");
		String gridNodeHost = request.getParameter("gridNodeHost");
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		GridNodeBean gridNode = gridNodeMap.get(gridNodeHost);
		GridSearchService searchService = new GridSearchService();
		try {
			byte[] fileData = searchService.getRemoteFileContent(fileId,
					gridNode);

			if (fileData != null) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition",
						"attachment;filename=" + fileName);		
				response.setHeader("cache-control", "Private");

				java.io.OutputStream out = response.getOutputStream();
				out.write(fileData);
				out.close();
			} else {
				throw new CalabException(
						"File to download doesn't exist on the server");
			}
		} catch (Exception e) {
			throw new CalabException("Error retrieving remote file");
		}
		return null;
	}

	/**
	 * Download action to handle report downloading and viewing
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		String fileName = request.getParameter("fileName");
		String gridNodeHost = request.getParameter("gridNodeHost");
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		GridNodeBean gridNode = gridNodeMap.get(gridNodeHost);
		GridSearchService searchService = new GridSearchService();
		try {
			byte[] fileData = searchService.getRemoteFileContent(fileId,
					gridNode);

			if (fileData != null) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition",
						"attachment;filename=" + fileName);
				response.setHeader("cache-control", "Private");
				java.io.OutputStream out = response.getOutputStream();
				out.write(fileData);
				out.close();
			} else {
				throw new CalabException(
						"File to download doesn't exist on the server");
			}
		} catch (Exception e) {
			throw new CalabException("Error retrieving remote file:" + e);
		}
		return null;
	}
}
