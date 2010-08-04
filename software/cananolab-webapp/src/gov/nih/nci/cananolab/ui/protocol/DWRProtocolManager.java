package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * This class loads protocol data for ajax
 *
 * @author tanq, pansu
 *
 */
public class DWRProtocolManager {

	Logger logger = Logger.getLogger(DWRProtocolManager.class);
	ProtocolServiceLocalImpl service;
	SecurityService securityService;

	private ProtocolServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		securityService = (SecurityService) wctx.getSession().getAttribute(
				"securityService");
		service = new ProtocolServiceLocalImpl(securityService);
		return service;
	}

	public String[] getProtocolTypes() {
		WebContext webContext = WebContextFactory.get();
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
			List<ProtocolBean> protocols = getService().findProtocolsBy(
					protocolType, null, null, null);
			SortedSet<String> protocolNames = new TreeSet<String>();
			for (ProtocolBean protocolBean : protocols) {
				if (securityService.checkCreatePermission(protocolBean
						.getDomain().getId().toString())) {
					protocolNames.add(protocolBean.getDomain().getName());
				}
			}
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
			if (!securityService.checkCreatePermission(protocol.getId()
					.toString())) {
				throw new NoAccessException();
			}
			return new ProtocolBean(protocol);
		} catch (NoAccessException ne) {
			logger.info("User can't access the protocol " + protocolName);
		} catch (Exception e) {
			logger.info("Error in retrieving the protocol " + protocolName);
		}
		return null;
	}

	public String getPublicCounts() {
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
