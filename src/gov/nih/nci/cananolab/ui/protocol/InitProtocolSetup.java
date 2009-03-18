package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;

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
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"protocolTypes", "Protocol", "type", "otherType", true);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public List<ProtocolFileBean> getProtocolFilesByChar(
			HttpServletRequest request, String characterizationType)
			throws Exception {
		String protocolType = null;
		if (characterizationType.equals("Physico-Chemical Characterization")) {
			protocolType = Constants.PHYSICOCHEMICAL_ASSAY_PROTOCOL;
		}
		else if (characterizationType.equals("In Vitro Characterization")) {
			protocolType = Constants.INVITRO_ASSAY_PROTOCOL;
		}
		else {
			protocolType = null; // update if in vivo is implemented
		}

		ProtocolService service = new ProtocolServiceLocalImpl();
		List<ProtocolFileBean> protocolFiles = service.findProtocolFilesBy(
				protocolType, null, null);
		request.getSession().setAttribute("characterizationProtocolFiles",
				protocolFiles);
		return protocolFiles;
	}

	public void persistProtocolDropdowns(HttpServletRequest request,
			ProtocolFileBean protocolFile) throws Exception {
		InitSetup.getInstance().persistLookup(
				request,
				"Protocol",
				"type",
				"otherType",
				((ProtocolFile) protocolFile.getDomainFile()).getProtocol()
						.getType());
		setProtocolDropdowns(request);
	}
}
