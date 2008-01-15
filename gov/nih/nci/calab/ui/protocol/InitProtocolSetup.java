package gov.nih.nci.calab.ui.protocol;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.exception.CaNanoLabException;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ParticleCharacterizationException;
import gov.nih.nci.calab.exception.ProtocolException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.protocol.SearchProtocolService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

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

	private InitProtocolSetup() throws CaNanoLabSecurityException {
		searchProtocolService = new SearchProtocolService();
	}

	public static InitProtocolSetup getInstance()
			throws CaNanoLabSecurityException {
		return new InitProtocolSetup();
	}

	public void setAllProtocolTypes(HttpSession session)
			throws CaNanoLabException {
		SortedSet<String> protocolTypes = LookupService
				.getAllLookupTypes("ProtocolType");
		session.setAttribute("protocolTypes", protocolTypes);
	}

	public void setProtocolFilesByCharType(HttpSession session, String charType)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException, ProtocolException {
		String protocolType = null;
		if (charType != null
				&& charType
						.equalsIgnoreCase(Characterization.PHYSICAL_CHARACTERIZATION)) {
			protocolType = CaNanoLabConstants.PHYSICAL_ASSAY_PROTOCOL;
		} else if (charType != null
				&& charType
						.equalsIgnoreCase(Characterization.INVITRO_CHARACTERIZATION)) {
			protocolType = CaNanoLabConstants.INVITRO_ASSAY_PROTOCOL;
		} else {
			protocolType = null; // update if in vivo is implemented
		}
		List<ProtocolFileBean> protocolFiles = null;
		protocolFiles = searchProtocolService
				.getProtocolFileBeans(protocolType);
		session.setAttribute("submitTypeProtocolFiles", protocolFiles);
	}
}
