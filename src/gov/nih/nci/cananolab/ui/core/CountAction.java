package gov.nih.nci.cananolab.ui.core;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceRemoteImpl;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceLocalImpl;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceRemoteImpl;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

public class CountAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		
		String gridNodeHostStr = (String) request.getParameter("searchLocations");
		String[] searchLocations = new String[0];
		if(gridNodeHostStr != null) {
			searchLocations = gridNodeHostStr.split("~");
		}
		
		ActionMessages msgs = new ActionMessages();


		// particle count
		// TODO update auto-discovery to exclude local grid node
		List<ParticleBean> foundParticles = new ArrayList<ParticleBean>();

		String particleSource = "";
		List<String> nanoparticleEntityClassNames = new ArrayList<String>();
		List<String> otherNanoparticleEntityTypes = new ArrayList<String>();
		List<String> functionalizingEntityClassNames = new ArrayList<String>();
		List<String> otherFunctionalizingTypes = new ArrayList<String>();
		List<String> functionClassNames = new ArrayList<String>();
		List<String> otherFunctionTypes = new ArrayList<String>();
		String[] charaClassNames = new String[0];
		String[] words = null;

		for (String location : searchLocations) {
			List<ParticleBean> particles = null;
			NanoparticleSampleService service = null;
			if (location.equals("local")) {
				service = new NanoparticleSampleServiceLocalImpl();
			} else {
				// TODO get serviceUrl
				String serviceUrl = "";
				service = new NanoparticleSampleServiceRemoteImpl(serviceUrl);
			}
			particles = service.findNanoparticleSamplesBy(particleSource,
					nanoparticleEntityClassNames.toArray(new String[0]),
					otherNanoparticleEntityTypes.toArray(new String[0]),
					functionalizingEntityClassNames.toArray(new String[0]),
					otherFunctionalizingTypes.toArray(new String[0]),
					functionClassNames.toArray(new String[0]),
					otherFunctionTypes.toArray(new String[0]), charaClassNames,
					words);
			for (ParticleBean particle : particles) {
				particle.setLocation(location);
			}
			if (location.equals("local")) {
				List<ParticleBean> filteredParticles = new ArrayList<ParticleBean>();
				// set visibility
				for (ParticleBean particle : particles) {
					service.retrieveVisibility(particle, user);
					if (!particle.isHidden()) {
						filteredParticles.add(particle);
					}
				}
				foundParticles.addAll(filteredParticles);
			} else {
				if (particles == null || particles.size() == 0) {
					ActionMessage msg = new ActionMessage(
							"message.grid.discovery.none",
							CaNanoLabConstants.DOMAIN_MODEL_NAME);
					msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
					saveMessages(request, msgs);
					return mapping.getInputForward();
				}
				foundParticles.addAll(particles);
			}
		}

		int particleCount = foundParticles.size();
		
//		for (GridNodeBean gridNode : selectedGridNodes) {
//			try {
//				particleCount += searchService.getRemoteNanoparticleCount(
//						particleType, functionTypes, characterizations,
//						gridNode);
//
//			} catch (Exception e) {
//				ActionMessage error = new ActionMessage(
//						"error.grid.notAvailable", gridNode.getHostName());
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, error);
//				saveErrors(request, msgs);
//				e.printStackTrace();
//			}
//		}

		// report count
		String reportTitle = "";
		String reportCategory = "";
		List<ReportBean> foundReports = new ArrayList<ReportBean>();
		ReportService service = null;
		for (String location : searchLocations) {
			if (location.equals("local")) {
				service = new ReportServiceLocalImpl();
			} else {
				// TODO get serviceUrl
				String serviceUrl = "";
				service = new ReportServiceRemoteImpl(serviceUrl);
			}
			List<ReportBean> reports = service.findReportsBy(reportTitle,
					reportCategory, nanoparticleEntityClassNames
							.toArray(new String[0]),
					otherNanoparticleEntityTypes.toArray(new String[0]),
					functionalizingEntityClassNames.toArray(new String[0]),
					otherFunctionalizingTypes.toArray(new String[0]),
					functionClassNames.toArray(new String[0]),
					otherFunctionTypes.toArray(new String[0]));
			for (ReportBean report : reports) {
				report.setLocation(location);
			}
			if (location.equals("local")) {
				List<ReportBean> filteredReports = new ArrayList<ReportBean>();
				// retrieve visibility
				FileService fileService = new FileService();
				for (ReportBean report : reports) {
					fileService.retrieveVisibility(report, user);
					if (!report.isHidden()) {
						filteredReports.add(report);
					}
				}
				foundReports.addAll(filteredReports);
			} else {
				foundReports.addAll(reports);
			}
		}
		
		int reportCount = foundReports.size();
//		for (GridNodeBean gridNode : selectedGridNodes) {
//			try {
//				reportCount += searchService.getRemoteReportCount(reportTitle,
//						particleType, functionTypes, gridNode);
//			} catch (Exception e) {
//				ActionMessage message = new ActionMessage(
//						"error.grid.notAvailable", gridNode.getHostName());
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, message);
//				saveErrors(request, msgs);
//				e.printStackTrace();
//			}
//		}

		// protocol count
		int protocolCount = 0;

		PrintWriter out = response.getWriter();
		out.print(particleCount + "\t" + reportCount + "\t" + protocolCount);
		return null;
	}
}
