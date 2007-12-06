package gov.nih.nci.calab.ui.protocol;

import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.exception.CaNanoLabException;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ParticleCharacterizationException;
import gov.nih.nci.calab.exception.ProtocolException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.service.protocol.SearchProtocolService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		if (session.getAttribute("protocolTypes") == null
				|| session.getAttribute("newProtocolCreated") != null) {
			SortedSet<String> protocolTypes = LookupService
					.getAllLookupTypes("ProtocolType");
			session.setAttribute("protocolTypes", protocolTypes);
		}
		session.removeAttribute("newProtocolCreated");
	}

	public void setProtocolFilesByCharName(HttpSession session, String charName)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException, ProtocolException {
		Map<String, String> charNameToCharCategory = null;
		// retrieve from application context if there
		if (session.getServletContext().getAttribute(
				"characterizationCategoryMap") == null) {
			NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
			charNameToCharCategory = charService
					.getCharacterizationCategoryMap();
			session.getServletContext().setAttribute(
					"characterizationCategoryMap", charNameToCharCategory);
		} else {
			charNameToCharCategory = new HashMap<String, String>(
					(Map<? extends String, String>) session.getServletContext()
							.getAttribute("characterizationCategoryMap"));
		}
		String protocolType = null;
		String category = charNameToCharCategory.get(charName);
		if (category != null
				&& category
						.equals(CaNanoLabConstants.PHYSICAL_CHARACTERIZATION_CATEGORY)) {
			protocolType = CaNanoLabConstants.PHYSICAL_ASSAY_PROTOCOL;
		} else {
			// get the top level category
			String nextCategory = null;
			while (category != null) {
				nextCategory = category;
				category = charNameToCharCategory.get(nextCategory);
			}
			if (nextCategory
					.equals(CaNanoLabConstants.IN_VITRO_CHARACTERIZATION_CATEGORY)) {
				protocolType = CaNanoLabConstants.INVITRO_ASSAY_PROTOCOL;
			} else {
				protocolType = null; // update if in vivo is implemented
			}
		}
		List<ProtocolFileBean> protocolFiles = null;
		protocolFiles = searchProtocolService
				.getProtocolFileBeans(protocolType);
		session.setAttribute("submitTypeProtocolFiles", protocolFiles);
	}
}
