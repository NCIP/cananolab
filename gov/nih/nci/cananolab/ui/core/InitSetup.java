package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.exception.GridAutoDiscoveryException;
import gov.nih.nci.cananolab.exception.GridDownException;
import gov.nih.nci.cananolab.service.common.GridService;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

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

	public Map<String, Map<String, SortedSet<String>>> getDefaultLookupTable(
			ServletContext appContext) throws CaNanoLabException {
		Map<String, Map<String, SortedSet<String>>> defaultLookupTable = null;
		if (appContext.getAttribute("defaultLookupTable") == null) {
			defaultLookupTable = LookupService.findAllLookups();
			appContext.setAttribute("defaultLookupTable", defaultLookupTable);
		} else {
			defaultLookupTable = new HashMap<String, Map<String, SortedSet<String>>>(
					(Map<? extends String, Map<String, SortedSet<String>>>) appContext
							.getAttribute("defaultLookupTable"));
		}
		return defaultLookupTable;
	}

	/**
	 * Returns a map between an object name and its display name
	 * 
	 * @param appContext
	 * @return
	 * @throws CaNanoLabException
	 */
	public Map<String, String> getClassNameToDisplayNameLookup(
			ServletContext appContext) throws CaNanoLabException {
		Map<String, String> lookup = null;
		if (appContext.getAttribute("displayNameLookup") == null) {
			lookup = LookupService.findSingleAttributeLookupMap("displayName");
			appContext.setAttribute("displayNameLookup", lookup);
		} else {
			lookup = new HashMap<String, String>(
					(Map<? extends String, String>) (appContext
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
	public Map<String, String> getDisplayNameToClassNameLookup(
			ServletContext appContext) throws Exception {
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
					(Map<? extends String, String>) (appContext
							.getAttribute("displayNameReverseLookup")));
		}
		return lookup;
	}

	/**
	 * Returns a map between a display name and its corresponding full class
	 * name
	 * 
	 * @param appContext
	 * @return
	 * @throws CaNanoLabException
	 */
	public Map<String, String> getDisplayNameToFullClassNameLookup(
			ServletContext appContext) throws Exception {
		Map<String, String> lookup = null;
		if (appContext.getAttribute("displayNameReverseLookup") == null) {
			Map<String, String> displayLookup = LookupService
					.findSingleAttributeLookupMap("displayName");
			lookup = new HashMap<String, String>();
			for (Map.Entry entry : displayLookup.entrySet()) {
				String className = entry.getKey().toString();
				String fullClassName = ClassUtils.getFullClass(className)
						.getCanonicalName();
				lookup.put(entry.getValue().toString(), fullClassName);
			}
			appContext.setAttribute("displayNameReverseLookup", lookup);
		} else {
			lookup = new HashMap<String, String>(
					(Map<? extends String, String>) (appContext
							.getAttribute("displayNameReverseLookup")));
		}
		return lookup;
	}

	public String getDisplayName(String objectName, ServletContext appContext)
			throws CaNanoLabException {
		Map<String, String> lookup = getClassNameToDisplayNameLookup(appContext);
		if (lookup.get(objectName) != null) {
			return lookup.get(objectName);
		} else {
			return "";
		}
	}

	public String getObjectName(String displayName, ServletContext appContext)
			throws Exception {
		Map<String, String> lookup = getDisplayNameToClassNameLookup(appContext);
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
		Map<String, Map<String, SortedSet<String>>> defaultLookupTable = getDefaultLookupTable(appContext);
		SortedSet<String> types = defaultLookupTable.get(lookupName).get(
				lookupAttribute);
		appContext.setAttribute(contextAttribute, types);
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

	public String getFileUriFromFormFile(FormFile file, String folderType,
			String particleName, String submitType) {
		if (file != null && file.getFileName().length() > 0) {
			String prefix = folderType;

			if (particleName != null && submitType != null
					&& folderType.equals(CaNanoLabConstants.FOLDER_PARTICLE)) {
				prefix += "/" + particleName + "/";
				prefix += StringUtils
						.getOneWordLowerCaseFirstLetter(submitType);
			}
			String timestamp = StringUtils.convertDateToString(new Date(),
					"yyyyMMdd_HH-mm-ss-SSS");

			return prefix + "/" + timestamp + "_" + file.getFileName();
		} else {
			return null;
		}
	}

	// check whether the value is already stored in context
	private Boolean isLookupInContext(HttpServletRequest request,
			String lookupName, String attribute, String otherAttribute,
			String value) throws CaNanoLabException {
		Map<String, Map<String, SortedSet<String>>> defaultLookupTable = getDefaultLookupTable(request
				.getSession().getServletContext());
		SortedSet<String> defaultValues = null;
		if (defaultLookupTable.get(lookupName) != null) {
			defaultValues = defaultLookupTable.get(lookupName).get(attribute);
		}
		if (defaultValues != null && defaultValues.contains(value)) {
			return true;
		} else {
			SortedSet<String> otherValues = null;
			if (defaultLookupTable.get(lookupName) != null) {
				otherValues = defaultLookupTable.get(lookupName).get(
						otherAttribute);
			}
			if (otherValues != null && otherValues.contains(value)) {
				return true;
			}
		}
		return false;
	}

	public void persistLookup(HttpServletRequest request, String lookupName,
			String attribute, String otherAttribute, String value)
			throws CaNanoLabException {
		if (value == null || value.length() == 0) {
			return;
		}
		if (isLookupInContext(request, lookupName, attribute, otherAttribute,
				value)) {
			return;
		} else {
			LookupService.saveOtherType(lookupName, otherAttribute, value);
		}
	}

	public String getGridServiceUrl(HttpServletRequest request,
			String gridHostName) throws Exception {
		List<GridNodeBean> remoteNodes = getGridNodesInContext(request);
		GridNodeBean theNode = GridService.getGridNodeByHostName(remoteNodes,
				gridHostName);
		if (theNode == null) {
			throw new GridDownException("Grid node " + gridHostName
					+ " is not available at this time.");
		}
		return theNode.getAddress();
	}

	public List<GridNodeBean> getGridNodesInContext(HttpServletRequest request)
			throws Exception {
		URL localURL = new URL(request.getRequestURL().toString());
		String localGridURL = localURL.getProtocol() + "://"
				+ localURL.getHost() + ":" + localURL.getPort() + "/"
				+ CaNanoLabConstants.GRID_SERVICE_PATH;
		GridDiscoveryServiceJob gridDiscoveryJob = new GridDiscoveryServiceJob();
		List<GridNodeBean> gridNodes = gridDiscoveryJob.getAllGridNodes();
		if (gridNodes.isEmpty()) {
			throw new GridAutoDiscoveryException("No remote grid nodes found");
		}
		// remove local grid from the list
		GridNodeBean localGrid = GridService.getGridNodeByURL(gridNodes,
				localGridURL);
		if (localGrid != null) {
			gridNodes.remove(localGrid);
		}
		request.getSession().getServletContext().setAttribute("allGridNodes",
				gridNodes);
		return gridNodes;
	}
}
