package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Search protocol file and protocol
 * 
 * @author pansu
 * 
 */
public class SearchProtocolAction extends BaseAnnotationAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileTitle = (String) theForm.get("fileTitle");
		String protocolType = (String) theForm.get("protocolType");
		String protocolName = (String) theForm.get("protocolName");

		String[] searchLocations = new String[0];
		if (theForm.get("searchLocations") != null) {
			searchLocations = (String[]) theForm.getStrings("searchLocations");
		} else {
			String gridNodeHostStr = (String) request
					.getParameter("searchLocations");
			if (gridNodeHostStr != null) {
				searchLocations = gridNodeHostStr.split("~");
			}
		}
		List<ProtocolFileBean> foundProtocolFiles = new ArrayList<ProtocolFileBean>();
		ProtocolService service = null;
		for (String location : searchLocations) {
			if (location.equals("local")) {
				service = new ProtocolServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				service = new ProtocolServiceRemoteImpl(serviceUrl);
			}

			List<ProtocolFileBean> protocolFiles = service.findProtocolFilesBy(
					protocolType, protocolName, fileTitle);
			for (ProtocolFileBean protocolFile : protocolFiles) {
				protocolFile.setLocation(location);
			}
			if (location.equals("local")) {
				List<ProtocolFileBean> filteredProtocolFiles = new ArrayList<ProtocolFileBean>();
				// retrieve visibility
				FileService fileService = new FileServiceLocalImpl();
				for (ProtocolFileBean protocolFile : protocolFiles) {
					fileService.retrieveVisibility(protocolFile, user);
					if (!protocolFile.isHidden()) {
						filteredProtocolFiles.add(protocolFile);
					}
				}
				foundProtocolFiles.addAll(filteredProtocolFiles);
			} else {
				foundProtocolFiles.addAll(protocolFiles);
			}
		}
		if (foundProtocolFiles != null && !foundProtocolFiles.isEmpty()) {
			Collections
					.sort(
							foundProtocolFiles,
							new CaNanoLabComparators.ProtocolFileBeanNameVersionComparator());
			request.getSession().setAttribute("protocolFiles",
					foundProtocolFiles);
			forward = mapping.findForward("success");
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchProtocol.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		String[] selectedLocations = new String[] { "local" };
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (gridNodeHostStr != null && gridNodeHostStr.length() > 0) {
			selectedLocations = gridNodeHostStr.split("~");
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("searchLocations", selectedLocations);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return true;
	}
}
