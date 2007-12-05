package gov.nih.nci.calab.ui.report;

/**
 * This class remotely searches nanoparticle report across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: RemoteSearchReportAction.java,v 1.5 2007-12-05 20:01:08 pansu Exp $ */

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.exception.FileNotFoundException;
import gov.nih.nci.calab.exception.GridQueryException;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.BaseRemoteSearchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;
import gov.nih.nci.calab.ui.particle.InitParticleSetup;

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

public class RemoteSearchReportAction extends BaseRemoteSearchAction {
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
		Map<String, GridNodeBean> gridNodeMap = prepareSearch(request);
		GridNodeBean[] gridNodes = GridService.getGridNodesFromHostNames(
				gridNodeMap, gridNodeHosts);
		ActionMessages msgs = new ActionMessages();		
		if (gridNodes == null) {
			ActionMessage msg = new ActionMessage(
					"message.grid.discovery.none",
					CaNanoLabConstants.DOMAIN_MODEL_NAME);
			msgs.add("message", msg);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		GridSearchService searchService = new GridSearchService();
		List<LabFileBean> reports = new ArrayList<LabFileBean>();
		for (GridNodeBean gridNode : gridNodes) {
			try {
				List<LabFileBean> gridReports = searchService.getRemoteReports(
						reportTitle, reportType, particleType, functionTypes,
						gridNode);		
				if (gridReports.size() == 0) {
					ActionMessage message = new ActionMessage(
							"message.remoteSearchReport.noresult",
							gridNode.getHostName());
					msgs.add("message", message);
					saveMessages(request, msgs);
				}
				reports.addAll(gridReports);
			} catch (Exception e) {				
				ActionMessage message = new ActionMessage(
						"error.grid.notAvailable", gridNode.getHostName());
				msgs.add("message", message);
				saveMessages(request, msgs);
				e.printStackTrace();
			}
		}
		if (!reports.isEmpty()) {
			request.getSession().setAttribute("remoteReports", reports);
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

	protected Map<String, GridNodeBean> initSetup(HttpServletRequest request)
			throws Exception {
		Map<String, GridNodeBean> gridNodes = super.initSetup(request);
		HttpSession session = request.getSession();
		InitParticleSetup.getInstance().setAllFunctionTypes(session);
		InitReportSetup.getInstance().setAllReportTypes(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
		return gridNodes;
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
		Map<String, GridNodeBean> gridNodes = initSetup(request);		
		ActionMessages msgs = new ActionMessages();
		if (gridNodes == null) {
			ActionMessage msg = new ActionMessage(
					"message.grid.discovery.none",
					CaNanoLabConstants.DOMAIN_MODEL_NAME);
			msgs.add("message", msg);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		GridNodeBean gridNode = gridNodes.get(gridNodeHost);
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
				throw new FileNotFoundException(
						"File to download doesn't exist on the server");
			}
		} catch (Exception e) {
			throw new GridQueryException("Error retrieving remote file", e);
		}
		return null;
	}
}
