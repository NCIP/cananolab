package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.exception.GridDownException;
import gov.nih.nci.cananolab.service.common.GridService;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

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
	 * Queries and common_lookup table and creates a map in application context
	 * 
	 * @param appContext
	 * @return
	 * @throws BaseException
	 */
	public Map<String, Map<String, SortedSet<String>>> getDefaultLookupTable(
			ServletContext appContext) throws BaseException {
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
	 * Retrieve default lookup values from lookup table in the database and
	 * store in the application context
	 * 
	 * @param appContext
	 * @param contextAttribute
	 * @param lookupName
	 * @param lookupAttribute
	 * @return
	 * @throws BaseException
	 */
	public SortedSet<String> getDefaultTypesByLookup(ServletContext appContext,
			String contextAttribute, String lookupName, String lookupAttribute)
			throws BaseException {
		Map<String, Map<String, SortedSet<String>>> defaultLookupTable = getDefaultLookupTable(appContext);
		SortedSet<String> types = new TreeSet<String>();
		if (defaultLookupTable.get(lookupName) != null) {
			types = defaultLookupTable.get(lookupName).get(lookupAttribute);
			appContext.setAttribute(contextAttribute, types);
			return types;
		} else {
			return types;
		}
	}

	/**
	 * Retrieve default lookup and other values from lookup table in the
	 * database and store in the session
	 * 
	 * @param request
	 * @param sessionAttribute
	 * @param lookupName
	 * @param lookupAttribute
	 * @param otherTypeAttribute
	 * @aparam updateSession
	 * @return
	 * @throws BaseException
	 */
	public SortedSet<String> getDefaultAndOtherTypesByLookup(
			HttpServletRequest request, String sessionAttribute,
			String lookupName, String lookupAttribute,
			String otherTypeAttribute, boolean updateSession)
			throws BaseException {
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

	/**
	 * Retrieve other values from lookup table in the database and store in the
	 * session
	 * 
	 * @param request
	 * @param sessionAttribute
	 * @param lookupName
	 * @param lookupAttribute
	 * @param otherTypeAttribute
	 * @aparam updateSession
	 * @return
	 * @throws BaseException
	 */
	public SortedSet<String> getOtherTypesByLookup(HttpServletRequest request,
			String sessionAttribute, String lookupName,
			String otherTypeAttribute, boolean updateSession)
			throws BaseException {
		SortedSet<String> types = null;
		if (updateSession) {
			types = LookupService.findLookupValues(lookupName,
					otherTypeAttribute);
			request.getSession().setAttribute(sessionAttribute, types);
		} else {
			types = new TreeSet<String>((SortedSet<? extends String>) (request
					.getSession().getAttribute(sessionAttribute)));
		}
		return types;
	}

	/**
	 * Retrieve default lookup values by reflection and store in the app context
	 * 
	 * @param appContext
	 * @param contextAttribute
	 * @param lookupName
	 * @param fullParentClassName
	 * @return
	 * @throws Exception
	 */
	public SortedSet<String> getDefaultTypesByReflection(
			ServletContext appContext, String contextAttribute,
			String fullParentClassName) throws Exception {
		SortedSet<String> types = new TreeSet<String>();
		List<String> classNames = ClassUtils
				.getChildClassNames(fullParentClassName);
		for (String name : classNames) {
			if (!name.contains("Other")) {
				String shortClassName = ClassUtils.getShortClassName(name);
				String displayName = ClassUtils.getDisplayName(shortClassName);
				types.add(displayName);
			}
		}
		appContext.setAttribute(contextAttribute, types);
		return types;
	}

	/**
	 * Retrieve default lookup and other values by reflection and store in the
	 * session
	 * 
	 * @param request
	 * @param contextAttributeForDefaults
	 * @param sessionAttribute
	 * @param fullParentClassName
	 * @param otherFullParentClassName
	 * @param updateSession
	 * @return
	 * @throws Exception
	 */
	public SortedSet<String> getDefaultAndOtherTypesByReflection(
			HttpServletRequest request, String contextAttributeForDefaults,
			String sessionAttribute, String fullParentClassName,
			String otherFullParentClassName, boolean updateSession)
			throws Exception {

		ServletContext appContext = request.getSession().getServletContext();
		SortedSet<String> defaultTypes = getDefaultTypesByReflection(
				appContext, contextAttributeForDefaults, fullParentClassName);

		SortedSet<String> types = null;
		if (updateSession) {
			types = new TreeSet<String>(defaultTypes);
			SortedSet<String> otherTypes = LookupService
					.getAllOtherObjectTypes(otherFullParentClassName);
			if (otherTypes != null)
				types.addAll(otherTypes);
			request.getSession().setAttribute(sessionAttribute, types);
		} else {
			types = new TreeSet<String>((SortedSet<? extends String>) (request
					.getSession().getAttribute(sessionAttribute)));
		}
		return types;
	}

	public String getFileUriFromFormFile(FormFile file, String folderType,
			String sampleName, String submitType) {
		if (file != null && !StringUtils.isEmpty(file.getFileName())) {
			String prefix = folderType;

			if (!StringUtils.isEmpty(sampleName)
					&& !StringUtils.isEmpty(submitType)
					&& folderType.equals(Constants.FOLDER_PARTICLE)) {
				prefix += "/" + sampleName + "/";
				prefix += StringUtils
						.getOneWordLowerCaseFirstLetter(submitType);
			}
			String timestamp = DateUtils.convertDateToString(new Date(),
					"yyyyMMdd_HH-mm-ss-SSS");

			return prefix + "/" + timestamp + "_" + file.getFileName();
		} else {
			return null;
		}
	}

	// check whether the value is already stored in context
	private Boolean isLookupInContext(HttpServletRequest request,
			String lookupName, String attribute, String otherAttribute,
			String value) throws BaseException {
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
			throws BaseException {
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
		// URL localURL = new URL(request.getRequestURL().toString());
		// int port = (localURL.getPort() == -1) ? 80 : localURL.getPort();
		// String localGridURL = localURL.getProtocol() + "://"
		// + localURL.getHost() + ":" + port + "/"
		// + Constants.GRID_SERVICE_PATH;
		// GridDiscoveryServiceJob gridDiscoveryJob = new
		// GridDiscoveryServiceJob();
		// List<GridNodeBean> gridNodes = gridDiscoveryJob.getAllGridNodes();
		// GridNodeBean localGrid = GridService.getGridNodeByURL(gridNodes,
		// localGridURL);

		GridDiscoveryServiceJob gridDiscoveryJob = new GridDiscoveryServiceJob();
		List<GridNodeBean> gridNodes = gridDiscoveryJob.getAllGridNodes();
		GridNodeBean localGrid = GridService.getGridNodeByHostName(gridNodes,
				Constants.LOCAL_SITE);
		// don't remove from original list
		List<GridNodeBean> remoteNodes = new ArrayList<GridNodeBean>();
		remoteNodes.addAll(gridNodes);
		if (localGrid != null) {
			remoteNodes.remove(localGrid);
		}
		Collections.sort(remoteNodes,
				new Comparators.GridNodeHostNameComparator());

		request.getSession().getServletContext().setAttribute("allGridNodes",
				remoteNodes);
		return gridNodes;
	}

	public PublicDataCountBean getPublicDataCountsInContext(
			HttpServletRequest request) throws Exception {
		PublicDataCountServiceJob dataCountJob = new PublicDataCountServiceJob();
		PublicDataCountBean dataCounts = dataCountJob.getPublicDataCounts();
		request.getSession().getServletContext().setAttribute(
				"allPublicDataCounts", dataCounts);
		return dataCounts;
	}

	public void updateCSMCleanupEntriesInContext(
			List<String> csmEntriesToRemove, HttpServletRequest request)
			throws Exception {
		CSMCleanupJob job = new CSMCleanupJob();
		Set<String> secureObjects = job.getAllSecureObjectsToRemove();
		secureObjects.addAll(csmEntriesToRemove);
		request.getSession().getServletContext().setAttribute(
				"allCSMEntriesToRemove", secureObjects);
	}

	public List<LabelValueBean> getDefaultAndOtherTypesByLookupAsOptions(
			String lookupName, String lookupAttribute, String otherTypeAttribute)
			throws Exception {
		List<LabelValueBean> lvBeans = new ArrayList<LabelValueBean>();
		SortedSet<String> defaultValues = LookupService.findLookupValues(
				lookupName, lookupAttribute);
		// annotate the label of the default ones with *s.
		for (String name : defaultValues) {
			LabelValueBean lv = new LabelValueBean(name, name);
			lvBeans.add(lv);
		}
		SortedSet<String> otherValues = LookupService.findLookupValues(
				lookupName, otherTypeAttribute);
		for (String name : otherValues) {
			LabelValueBean lv = new LabelValueBean("[" + name + "]", name);
			lvBeans.add(lv);
		}
		return lvBeans;
	}

	public List<LabelValueBean> getDefaultAndOtherTypesByReflectionAsOptions(
			ServletContext appContext, String contextAttributeForDefaults,
			String fullParentClassName, String otherFullParentClassName)
			throws Exception {
		List<LabelValueBean> lvBeans = new ArrayList<LabelValueBean>();
		SortedSet<String> defaultTypes = getDefaultTypesByReflection(
				appContext, contextAttributeForDefaults, fullParentClassName);
		for (String type : defaultTypes) {
			LabelValueBean lv = new LabelValueBean(type, type);
			lvBeans.add(lv);
		}

		SortedSet<String> otherTypes = LookupService
				.getAllOtherObjectTypes(otherFullParentClassName);
		if (otherTypes != null) {
			for (String type : otherTypes) {
				LabelValueBean lv = new LabelValueBean("[" + type + "]", type);
				lvBeans.add(lv);
			}
		}
		return lvBeans;
	}

	public void setStaticOptions(ServletContext appContext) {
		LabelValueBean[] booleanOptions = new LabelValueBean[] {
				new LabelValueBean("true", "1"),
				new LabelValueBean("false", "0") };
		appContext.setAttribute("booleanOptions", booleanOptions);

		LabelValueBean[] stringOperands = new LabelValueBean[] {
				new LabelValueBean(Constants.STRING_OPERAND_CONTAINS,
						Constants.STRING_OPERAND_CONTAINS),
				new LabelValueBean(Constants.STRING_OPERAND_EQUALS,
						Constants.STRING_OPERAND_EQUALS) };
		appContext.setAttribute("stringOperands", stringOperands);

		LabelValueBean[] booleanOperands = new LabelValueBean[] { new LabelValueBean(
				"equals", "is") };
		appContext.setAttribute("booleanOperands", booleanOperands);

		LabelValueBean[] numberOperands = new LabelValueBean[] {
				new LabelValueBean("=", "="), new LabelValueBean(">", ">"),
				new LabelValueBean(">=", ">="), new LabelValueBean("<", "<"),
				new LabelValueBean("<=", "<=") };
		appContext.setAttribute("numberOperands", numberOperands);

		appContext.setAttribute("allCompositionSections",
				CompositionBean.ALL_COMPOSITION_SECTIONS);
	}
}
