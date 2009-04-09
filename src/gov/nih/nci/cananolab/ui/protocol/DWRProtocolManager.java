package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
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
	ProtocolService service = new ProtocolServiceLocalImpl();

	public DWRProtocolManager() {
	}

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

	public ProtocolBean getProtocol(String protocolType, String protocolName,
			String protocolVersion) {
		// all three have to be present
		if (protocolType == null || protocolType.length() == 0
				|| protocolName == null || protocolName.length() == 0
				|| protocolVersion == null || protocolVersion.length() == 0) {
			return null;
		}
		try {
			Protocol protocol = service.findProtocolBy(protocolType,
					protocolName, protocolVersion);
			ProtocolBean protocolBean = new ProtocolBean(protocol);
			DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
			org.directwebremoting.WebContext webContext = dwcb.get();
			UserBean user = (UserBean) webContext.getHttpServletRequest()
					.getSession().getAttribute("user");
			service.retrieveVisibility(protocolBean, user);
			return protocolBean;
		} catch (Exception e) {
			return null;
		}
	}

	public List<ProtocolBean> getProtocols(String protocolType) {
		try {
			if (protocolType == null || protocolType.length() == 0) {
				return null;
			}
			List<ProtocolBean> protocols = service.findProtocolsBy(
					protocolType, null, null, null);
			return protocols;
		} catch (Exception e) {
			return null;
		}
	}
}
