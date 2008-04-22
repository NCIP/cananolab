package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

/**
 * This class sets up information required for all forms.
 * 
 * @author pansu
 * 
 */
public class InitSetup {

	private InitSetup() {
	}

	public static InitSetup getInstance() {
		return new InitSetup();
	}

	/**
	 * Returns a map between an object name and its display name
	 * 
	 * @param appContext
	 * @return
	 * @throws CaNanoLabException
	 */
	public Map<String, String> getDisplayNameLookup(ServletContext appContext)
			throws CaNanoLabException {
		Map<String, String> lookup = null;
		if (appContext.getAttribute("displayNameLookup") == null) {
			lookup = LookupService.getSingleAttributeLookupMap("displayName");
			appContext.setAttribute("displayNameLookup", lookup);
		} else {
			lookup = new HashMap<String, String>(
					(HashMap<? extends String, String>) (appContext
							.getAttribute("displayNameLookup")));
		}
		return lookup;
	}

	/**
	 * Returns a map between a display name and its corresponding object name
	 * 
	 * @param appContext
	 * @return
	 * @throws CaNanoLabException
	 */
	public Map<String, String> getDisplayNameReverseLookup(
			ServletContext appContext) throws CaNanoLabException {
		Map<String, String> lookup = null;
		if (appContext.getAttribute("displayNameReverseLookup") == null) {
			Map<String, String> displayLookup = LookupService
					.getSingleAttributeLookupMap("displayName");
			lookup = new HashMap<String, String>();
			for (Map.Entry entry : displayLookup.entrySet()) {
				lookup.put(entry.getValue().toString(), entry.getKey()
						.toString());
			}
			appContext.setAttribute("displayNameReverseLookup", lookup);
		} else {
			lookup = new HashMap<String, String>(
					(HashMap<? extends String, String>) (appContext
							.getAttribute("displayNameReverseLookup")));
		}
		return lookup;
	}

	public String getDisplayName(String objectName, ServletContext appContext)
			throws CaNanoLabException {
		Map<String, String> lookup = getDisplayNameLookup(appContext);
		if (lookup.get(objectName) != null) {
			return lookup.get(objectName);
		} else {
			return "";
		}
	}

	public String getObjectName(String displayName, ServletContext appContext)
			throws CaNanoLabException {
		Map<String, String> lookup = getDisplayNameReverseLookup(appContext);
		if (lookup.get(displayName) != null) {
			return lookup.get(displayName);
		} else {
			return "";
		}
	}

	public void setSharedDropdowns(ServletContext appContext) {
		// set static boolean yes or no and characterization source choices
		appContext.setAttribute("booleanChoices",
				CaNanoLabConstants.BOOLEAN_CHOICES);

	}
}
