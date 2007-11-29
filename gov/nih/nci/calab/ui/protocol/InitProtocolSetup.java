package gov.nih.nci.calab.ui.protocol;

import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.protocol.SearchProtocolService;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpSession;

/**
 * This class sets up session level or servlet context level variables to be
 * used in various actions during the setup of query forms.
 * 
 * @author pansu
 * 
 */
public class InitProtocolSetup {
	private static SearchProtocolService searchProtocolService;

	private static LookupService lookupService;

	private InitProtocolSetup() throws Exception {
		searchProtocolService = new SearchProtocolService();
		lookupService = new LookupService();
	}

	public static InitProtocolSetup getInstance() throws Exception {
		return new InitProtocolSetup();
	}

	public void setAllProtocolTypes(HttpSession session) throws Exception {
		if (session.getAttribute("protocolTypes") == null
				|| session.getAttribute("newProtocolCreated") != null) {
			SortedSet<String> protocolTypes = LookupService
					.getAllLookupTypes("ProtocolType");
			session.setAttribute("protocolTypes", protocolTypes);
		}
		session.removeAttribute("newProtocolCreated");
	}

	public void setProtocolFilesBySubmitType(HttpSession session,
			String submitType) throws Exception {
		List<ProtocolFileBean> protocolFiles = null;
		if (submitType.equalsIgnoreCase("size")) {
			protocolFiles = searchProtocolService
					.getProtocolFileBeans("Physical assay");
		} else {
			protocolFiles = searchProtocolService
					.getProtocolFileBeans("In Vitro assay");
		}
		session.setAttribute("submitTypeProtocolFiles", protocolFiles);
	}
}
