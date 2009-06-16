package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.util.Constants;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Count the number of public protocols, particles and publications
 *
 * @author cais, pansu
 *
 */
public class CountAction extends AbstractDispatchAction {

	public ActionForward countProtocols(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		String[] searchLocations = new String[0];
		if (gridNodeHostStr != null) {
			searchLocations = gridNodeHostStr.split("~");
		}

		int protocolCount = 0;
		ProtocolService protocolService = null;
		for (String location : searchLocations) {
			if (location.equals(Constants.LOCAL_SITE)) {
				protocolService = new ProtocolServiceLocalImpl();
			}
			//TODO grid service
//			else {
//				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
//						request, location);
//				protocolService = new ProtocolServiceRemoteImpl(serviceUrl);
//			}
			try {
				protocolCount += protocolService
						.getNumberOfPublicProtocols();
			} catch (Exception ex) {
				ex.printStackTrace();
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage("error.protocolCount",
						location);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
			}
		}

		PrintWriter out = response.getWriter();
		out.print(protocolCount);
		return null;
	}

	public ActionForward countSamples(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		String[] searchLocations = new String[0];
		if (gridNodeHostStr != null) {
			searchLocations = gridNodeHostStr.split("~");
		}

		int sampleCount = 0;
		SampleService service = null;
		for (String location : searchLocations) {
			if (location.equals(Constants.LOCAL_SITE)) {
				service = new SampleServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				//TODO update grid service
//				service = new SampleServiceRemoteImpl(serviceUrl);
			}
			try {
				sampleCount += service.getNumberOfPublicSamples();
			} catch (Exception ex) {
				ex.printStackTrace();
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"error.sampleCount", location);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
			}
		}

		PrintWriter out = response.getWriter();
		out.print(sampleCount);
		return null;
	}

	public ActionForward countPublications(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		String[] searchLocations = new String[0];
		if (gridNodeHostStr != null) {
			searchLocations = gridNodeHostStr.split("~");
		}
		int publicationCount = 0;
		PublicationService publicationService = null;
		for (String location : searchLocations) {
			if (location.equals(Constants.LOCAL_SITE)) {
				publicationService = new PublicationServiceLocalImpl();
			}
			//TODO grid service
//				else {
//				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
//						request, location);
//				publicationService = new PublicationServiceRemoteImpl(
//						serviceUrl);
//			}
			try {
				publicationCount += publicationService
						.getNumberOfPublicPublications();
			} catch (Exception ex) {
				ex.printStackTrace();
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage("error.publicationCount",
						location);
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
			}
		}
		PrintWriter out = response.getWriter();
		out.print(publicationCount);
		return null;
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) {
		return true;
	}

}
