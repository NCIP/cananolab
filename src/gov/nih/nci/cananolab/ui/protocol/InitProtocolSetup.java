package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * This class sets up session level or servlet context level variables to be
 * used in various actions during the setup of query forms.
 * 
 * @author pansu
 * 
 */
public class InitProtocolSetup {
	Logger logger = Logger.getLogger(InitProtocolSetup.class);

	private InitProtocolSetup() {
	}

	public static InitProtocolSetup getInstance() {
		return new InitProtocolSetup();
	}

	public void setProtocolDropdowns(HttpServletRequest request)
			throws Exception {
		setLocalSearchDropdowns(request);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public void setLocalSearchDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"protocolTypes", "protocol", "type", "otherType", true);
	}

	public void setRemoteSearchDropdowns(HttpServletRequest request)
			throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		SortedSet<String> types = InitSetup.getInstance().getDefaultTypesByLookup(
				appContext, "defaultProtocolTypes", "protocol", "type");
		request.getSession().setAttribute("protocolTypes", types);
	}

	public List<ProtocolBean> getProtocolsByChar(HttpServletRequest request,
			String characterizationType) throws Exception {
		String protocolType = null;
		if (characterizationType
				.equals(Constants.PHYSICOCHEMICAL_CHARACTERIZATION)) {
			protocolType = Constants.PHYSICOCHEMICAL_ASSAY_PROTOCOL;
		} else if (characterizationType.equals("In Vitro Characterization")) {
			protocolType = Constants.INVITRO_ASSAY_PROTOCOL;
		} else {
			protocolType = null; // update if in vivo is implemented
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ProtocolService service = new ProtocolServiceLocalImpl();
		List<ProtocolBean> protocols = service.findProtocolsBy(protocolType,
				null, null, null, user);
		request.getSession().setAttribute("characterizationProtocols",
				protocols);
		return protocols;
	}

	public void persistProtocolDropdowns(HttpServletRequest request,
			ProtocolBean protocol) throws Exception {
		InitSetup.getInstance().persistLookup(request, "protocol", "type",
				"otherType", protocol.getDomain().getType());
		InitSetup.getInstance().persistLookup(request, protocol.getDomain().getType()+" protocol type", "name",
				"otherName", protocol.getDomain().getName());
		InitSetup.getInstance().persistLookup(request, protocol.getDomain().getType()+" protocol type", "version",
				"otherVersion", protocol.getDomain().getVersion());
		setProtocolDropdowns(request);
	}
}
