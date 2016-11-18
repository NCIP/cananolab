package gov.nih.nci.cananolab.restful.protocol;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("protocolManager")
public class ProtocolManager
{
	private Logger logger = Logger.getLogger(ProtocolManager.class);
	
	@Autowired
	private ProtocolService protocolService;

	public String[] getProtocolTypes()
	{
		WebContext webContext = WebContextFactory.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			SortedSet<String> types = null;
			types = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(
					request, "protocolTypes", "protocol", "type", "otherType", true);
			types.add("");
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting protocol types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public ProtocolBean getProtocol(HttpServletRequest request, String protocolType, String protocolName,
			String protocolVersion) {
		// protocolType and protocolName have to be present
		if (StringUtils.isEmpty(protocolType) || StringUtils.isEmpty(protocolName))
		{
			return null;
		}
		
		try {
			ProtocolBean protocolBean = protocolService.findProtocolBy(protocolType, protocolName, protocolVersion);
			if (protocolBean != null
					&& protocolBean.getDomain().getFile() != null
					&& !StringUtils.xssValidate(protocolBean.getDomain().getFile().getUri())) {
				return null;
			}
			return protocolBean;
		} catch (Exception e) {
			logger.error("Error in retrieving the protocol " + protocolName);
		}
		return null;
	}

	public ProtocolBean getProtocolById(String protocolId)
	{
		if (StringUtils.isEmpty(protocolId)) {
			return null;
		}
		try {
			ProtocolBean protocolBean = protocolService.findProtocolById(protocolId);
			if (protocolBean != null
					&& protocolBean.getDomain().getFile() != null
					&& !StringUtils.xssValidate(protocolBean.getDomain().getFile().getUri())) {
				return null;
			}
			return protocolBean;
		} catch (Exception e) {
			logger.error("Error in retrieving the protocol " + protocolId);
		}
		return null;
	}

	public String getPublicCounts()
	{
		Integer counts = 0;
		try {
			counts = protocolService.getHelper().getNumberOfPublicProtocols();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public protocols from local site.");
		}
		return counts.toString() + " Protocols";
	}

}


