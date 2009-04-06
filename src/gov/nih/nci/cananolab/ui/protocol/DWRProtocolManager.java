package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.impl.DefaultWebContextBuilder;

/**
 * This class loads protocol data for ajax
 *
 * @author tanq, pansu
 *
 */
public class DWRProtocolManager {

	Logger logger = Logger.getLogger(DWRProtocolManager.class);
	ProtocolServiceHelper helper = new ProtocolServiceHelper();

	public DWRProtocolManager() {
	}

	// for ajax
	public String[] getProtocolTypes(String searchLocations) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if ("local".equals(searchLocations)) {
				isLocal = true;
			}
			SortedSet<String> types = null;
			if (isLocal) {
				types = InitSetup.getInstance().getDefaultAndOtherLookupTypes(
						request, "protocolTypes", "Protocol", "type",
						"otherType", true);
			} else {
				types = LookupService.findLookupValues("Protocol", "type");
			}
			types.add("");
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting protocol types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	// for ajax on linux
//	public String getProtocolFileUriById(String fileId) {
//		return helper.getProtocolFileUriById(fileId);
//	}

	// for ajax on linux
//	public String getProtocolFileNameById(String fileId) {
//		return helper.getProtocolFileNameById(fileId);
//	}

	// for ajax on linux
	public String getProtocolVersionById(String fileId) {
		return helper.getProtocolVersionById(fileId);
	}

	// used for Ajax
	public SortedSet<String> getProtocolNames(String protocolType) {
		return helper.getProtocolNames(protocolType);
	}

	// for dwr ajax
	public List<ProtocolBean> getProtocols(String protocolType,
			String protocolName) {
		if (protocolType == null || protocolType.length() == 0
				|| protocolName == null || protocolName.length() == 0) {
			return null;
		}
		return helper.getProtocols(protocolType, protocolName);
	}
}
