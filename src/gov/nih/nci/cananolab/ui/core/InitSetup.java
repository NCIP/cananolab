package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for all forms.
 * 
 * @author pansu, cais
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
			lookup = LookupService.findSingleAttributeLookupMap("displayName");
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
					.findSingleAttributeLookupMap("displayName");
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

	/**
	 * Retrieve lookup attribute and other attribute for lookup name from the
	 * database and store in the application context
	 * 
	 * @param appContext
	 * @param contextAttribute
	 * @param lookupName
	 * @param lookupAttribute
	 * @return
	 * @throws CaNanoLabException
	 */
	public SortedSet<String> getServletContextDefaultLookupTypes(
			ServletContext appContext, String contextAttribute,
			String lookupName, String lookupAttribute)
			throws CaNanoLabException {
		SortedSet<String> types = null;
		if (appContext.getAttribute(contextAttribute) == null) {
			types = LookupService.findLookupValues(lookupName, lookupAttribute);
			appContext.setAttribute(contextAttribute, types);
			return types;
		} else {
			types = new TreeSet<String>(
					(SortedSet<? extends String>) appContext
							.getAttribute(contextAttribute));
		}
		return types;
	}

	/**
	 * Retrieve lookup attribute and other attribute for lookup name from the
	 * database and store in the session
	 * 
	 * @param request
	 * @param sessionAttribute
	 * @param lookupName
	 * @param lookupAttribute
	 * @param otherTypeAttribute
	 * @aparam updateSession
	 * @return
	 * @throws CaNanoLabException
	 */
	public SortedSet<String> getDefaultAndOtherLookupTypes(
			HttpServletRequest request, String sessionAttribute,
			String lookupName, String lookupAttribute,
			String otherTypeAttribute, boolean updateSession)
			throws CaNanoLabException {
		SortedSet<String> types = null;
		if (updateSession) {
			types = LookupService.getDefaultAndOtherLookupTypes(lookupName,
					lookupAttribute, otherTypeAttribute);
			request.getSession().setAttribute(sessionAttribute, types);
		} else {
			types = new TreeSet<String>((SortedSet<? extends String>) (request
					.getSession().getAttribute(sessionAttribute)));
		}
		return types;
	}

	public List<String> getDefaultFunctionTypes(ServletContext appContext)
			throws Exception {
		if (appContext.getAttribute("defaultFunctionTypes") == null) {
			List<String> functionTypes = new ArrayList<String>();
			List<String> functionClassNames = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			for (String name : functionClassNames) {
				if (!name.contains("Other")) {
					String displayName = InitSetup.getInstance()
							.getDisplayName(ClassUtils.getShortClassName(name),
									appContext);
					functionTypes.add(displayName);
				}
			}
			appContext.setAttribute("defaultFunctionTypes", functionTypes);
			return functionTypes;
		} else {
			return new ArrayList<String>((List<? extends String>) appContext
					.getAttribute("defaultFunctionTypes"));
		}

	}

	/**
	 * Retrieve lookup attribute and other attribute for lookup name based on
	 * reflection and store in the application context
	 * 
	 * @param appContext
	 * @param contextAttribute
	 * @param lookupName
	 * @param fullParentClassName
	 * @return
	 * @throws Exception
	 */
	public SortedSet<String> getServletContextDefaultTypesByReflection(
			ServletContext appContext, String contextAttribute,
			String fullParentClassName) throws Exception {
		if (appContext.getAttribute(contextAttribute) == null) {
			SortedSet<String> types = new TreeSet<String>();
			List<String> classNames = ClassUtils
					.getChildClassNames(fullParentClassName);
			for (String name : classNames) {
				if (!name.contains("Other")) {
					String displayName = InitSetup.getInstance()
							.getDisplayName(ClassUtils.getShortClassName(name),
									appContext);
					types.add(displayName);
				}
			}
			appContext.setAttribute(contextAttribute, types);
			return types;
		} else {
			return new TreeSet<String>((SortedSet<? extends String>) appContext
					.getAttribute(contextAttribute));
		}
	}
}
