package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

/**
 * This class sets up information required for all forms.
 * 
 * @author pansu
 * 
 */
public class InitSessionSetup {
	private static LookupService lookupService;

	private InitSessionSetup() throws Exception {
		lookupService = new LookupService();
	}

	public static InitSessionSetup getInstance() throws Exception {
		return new InitSessionSetup();
	}

	public void setApplicationOwner(HttpSession session) {
		if (session.getServletContext().getAttribute("applicationOwner") == null) {
			session.getServletContext().setAttribute("applicationOwner",
					CaNanoLabConstants.APP_OWNER);
		}
	}

	public void setStaticDropdowns(HttpSession session) {
		// set static boolean yes or no and characterization source choices
		session.setAttribute("booleanChoices",
				CaNanoLabConstants.BOOLEAN_CHOICES);
		session.setAttribute("allCarbonNanotubeWallTypes",
				CaNanoLabConstants.CARBON_NANOTUBE_WALLTYPES);
		if (session.getAttribute("allReportTypes") == null) {
			String[] allReportTypes = lookupService.getAllReportTypes();
			session.setAttribute("allReportTypes", allReportTypes);
		}
		session.setAttribute("allFunctionLinkageTypes",
				CaNanoLabConstants.FUNCTION_LINKAGE_TYPES);
		session.setAttribute("allFunctionAgentTypes",
				CaNanoLabConstants.FUNCTION_AGENT_TYPES);
	}

	public void updateEditableDropdown(HttpSession session,
			String formAttributeValue, String sessionAttributeName) {
		SortedSet<String> dropdown = (SortedSet<String>) session
				.getAttribute(sessionAttributeName);
		if (dropdown == null) {
			dropdown = new TreeSet<String>();			
		}
		if (formAttributeValue != null && formAttributeValue.length() > 0) {
			dropdown.add(formAttributeValue);
		}
		session.setAttribute(sessionAttributeName, dropdown);
	}
}
