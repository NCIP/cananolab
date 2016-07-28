/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;

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
	}

	public void setLocalSearchDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request, "protocolTypes", "protocol", "type", "otherType", true);
	}

	public void persistProtocolDropdowns(HttpServletRequest request,
			ProtocolBean protocol) throws Exception {
		InitSetup.getInstance().persistLookup(request, "protocol", "type",
				"otherType", protocol.getDomain().getType());
		InitSetup.getInstance().persistLookup(request,
				protocol.getDomain().getType() + " protocol type", "name",
				"otherName", protocol.getDomain().getName());
		InitSetup.getInstance().persistLookup(request,
				protocol.getDomain().getType() + " protocol type", "version",
				"otherVersion", protocol.getDomain().getVersion());
		setProtocolDropdowns(request);
	}
	
}
