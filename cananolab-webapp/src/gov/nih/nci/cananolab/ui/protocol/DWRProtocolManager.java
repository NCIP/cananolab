package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

/**
 * This class loads protocol data for ajax
 *
 * @author tanq, pansu
 *
 */
public class DWRProtocolManager {

	Logger logger = Logger.getLogger(DWRProtocolManager.class);
	ProtocolServiceLocalImpl service;

	private ProtocolServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		service = new ProtocolServiceLocalImpl(user);
		return service;
	}

	public String[] getProtocolTypes() {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			SortedSet<String> types = null;
			types = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(
					request, "protocolTypes", "protocol", "type", "otherType",
					true);
			types.add("");
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting protocol types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public SortedSet<String> getProtocolNames(String protocolType) {
		try {
			if (StringUtils.isEmpty(protocolType)) {
				return null;
			}
			SortedSet<String> protocolNames = getService().getHelper()
					.getProtocolNamesBy(protocolType);
			return protocolNames;
		} catch (Exception e) {
			return null;
		}
	}

	public SortedSet<String> getProtocolVersions(String protocolType,
			String protocolName) {
		try {
			if (StringUtils.isEmpty(protocolName)) {
				return null;
			}
			SortedSet<String> protocolVersions = getService().getHelper()
					.getProtocolVersionsBy(protocolType, protocolName);
			return protocolVersions;
		} catch (Exception e) {
			return null;
		}
	}

	public ProtocolBean getProtocol(String protocolType, String protocolName,
			String protocolVersion) {
		// all three have to be present
		if (StringUtils.isEmpty(protocolType)
				|| StringUtils.isEmpty(protocolName)
				|| StringUtils.isEmpty(protocolVersion)) {
			return null;
		}
		try {
			Protocol protocol = getService().getHelper().findProtocolBy(
					protocolType, protocolName, protocolVersion);
			return new ProtocolBean(protocol);
		} catch (Exception e) {
			return null;
		}
	}

	public String getPublicCounts() {
		WebContext wctx = WebContextFactory.get();
		Integer counts = 0;
		try {
			counts = getService().getHelper().getNumberOfPublicProtocols();
		} catch (Exception e) {
			logger
					.error("Error obtaining counts of public protocols from local site.");
		}
		return counts.toString() + " Protocols";
	}
}
