package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
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
	 * Retrieve lookup Map from lookup table and store in the application context
	 * @param appContext
	 * @param contextAttribute
	 * @param name
	 * @return
	 * @throws BaseException
	 */
	public Map<String, String> getLookupByName(
			ServletContext appContext, String contextAttribute,String name) throws BaseException{
		Map<String, Map<String, SortedSet<String>>> defaultLookupTable = getDefaultLookupTable(appContext);
		Map<String, SortedSet<String>> lookupByNameMap = defaultLookupTable.get(name);
		Map<String, String> lookupMap = new HashMap<String, String>();
		Set<String> keySet = lookupByNameMap.keySet();
		for(String key: keySet){			
			lookupMap.put(key, (String)lookupByNameMap.get(key).first());
		}
		appContext.setAttribute(contextAttribute, lookupMap);
		return lookupMap;

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

		// register page
		LabelValueBean[] titleOperands = new LabelValueBean[] {
				new LabelValueBean(" ", " "), new LabelValueBean("Dr.", "Dr."),
				new LabelValueBean("Mr.", "Mr."),
				new LabelValueBean("Mrs.", "Mrs."),
				new LabelValueBean("Miss", "Miss"),
				new LabelValueBean("Ms.", "Ms.") };
		appContext.setAttribute("titleOperands", titleOperands);

		LabelValueBean[] csmRoleNames = new LabelValueBean[] {
				new LabelValueBean(AccessibilityBean.R_ROLE_DISPLAY_NAME,
						AccessibilityBean.CSM_READ_ROLE),
				new LabelValueBean(AccessibilityBean.CURD_ROLE_DISPLAY_NAME,
						AccessibilityBean.CSM_CURD_ROLE) };
		appContext.setAttribute("csmRoleNames", csmRoleNames);
	}
}
