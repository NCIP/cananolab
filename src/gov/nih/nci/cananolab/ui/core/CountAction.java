package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceRemoteImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceRemoteImpl;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceLocalImpl;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceRemoteImpl;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class CountAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		String[] searchLocations = new String[0];
		if (gridNodeHostStr != null) {
			searchLocations = gridNodeHostStr.split("~");
		}

		int particleCount = 0;
		NanoparticleSampleService service = null;
		for (String location : searchLocations) {
			if (location.equals("local")) {
				service = new NanoparticleSampleServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				service = new NanoparticleSampleServiceRemoteImpl(serviceUrl);
			}
			try {
				particleCount += service.getNumberOfPublicNanoparticleSamples();
			} catch (Exception ex) {
				ex.printStackTrace();
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage("error.nanoparticleCount", location);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
			}
		}

		// report count
		int reportCount = 0;
		ReportService reportService = null;
		for (String location : searchLocations) {
			if (location.equals("local")) {
				reportService = new ReportServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				reportService = new ReportServiceRemoteImpl(serviceUrl);
			}
			try {
				reportCount += reportService.getNumberOfPublicReports();
			} catch (Exception ex) {
				ex.printStackTrace();
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage("error.reportCount", location);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
			}
		}

		// protocol count
		int protocolCount = 0;
		ProtocolService protocolService = null;
		for (String location : searchLocations) {
			if (location.equals("local")) {
				protocolService = new ProtocolServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				protocolService = new ProtocolServiceRemoteImpl(serviceUrl);
			}

			try {
				protocolCount += protocolService
						.getNumberOfPublicProtocolFiles();
			} catch (Exception ex) {
				ex.printStackTrace();
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage("error.protocolCount", location);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
			}
		}

		PrintWriter out = response.getWriter();
		out.print(particleCount + "\t" + reportCount + "\t" + protocolCount);
		return null;
	}
}
