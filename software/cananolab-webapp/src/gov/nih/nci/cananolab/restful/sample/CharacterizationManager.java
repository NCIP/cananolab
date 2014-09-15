/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.view.characterization.properties.CharacterizationPropertyUtil;
import gov.nih.nci.cananolab.restful.view.characterization.properties.SimpleCharacterizationProperty;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class CharacterizationManager {
	Logger logger = Logger.getLogger(CharacterizationManager.class);
	private CharacterizationServiceLocalImpl service;

	private CharacterizationServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		SecurityService securityService = (SecurityService) wctx.getSession()
				.getAttribute("securityService");
		service = new CharacterizationServiceLocalImpl(securityService);
		return service;
	}

	/**
	 * Return characterization name by characterization type
	 * 
	 * @param request
	 * @param characterizationType
	 * @return
	 * @throws Exception
	 */
	public List<String> getCharacterizationNames(HttpServletRequest request, String characterizationType)
			throws Exception {
		if (characterizationType.length() == 0) {
			return null;
		}
		SortedSet<String> charNames = InitCharacterizationSetup.getInstance()
				.getCharNamesByCharType(request,
						characterizationType);
		String[] charNameArray = new String[charNames.size()];
		charNames.toArray(charNameArray);
		
		List<String> charNameList = new ArrayList<String>();
		charNameList.addAll(charNames);
		
		return charNameList;
	}

	//public List<LabelValueBean> getDecoratedCharacterizationOptions(
	public void getDecoratedCharacterizationOptions(
			String characterizationType) throws Exception {
//		if (StringUtils.isEmpty(characterizationType)) {
//			return null;
//		}
//		WebContext wctx = WebContextFactory.get();
//		List<LabelValueBean> charNames = InitCharacterizationSetup
//				.getInstance().getDecoratedCharNamesByCharType(
//						wctx.getHttpServletRequest(), characterizationType);
//		List<LabelValueBean> charNamesWithAssayTypes = new ArrayList<LabelValueBean>();
//		for (LabelValueBean bean : charNames) {
//			String charName = bean.getValue();
//			// setup Assay Type drop down.
//			List<LabelValueBean> assayTypes = InitSetup.getInstance()
//					.getDefaultAndOtherTypesByLookupAsOptions(charName,
//							"assayType", "otherAssayType");
//			if (!assayTypes.isEmpty()) {
//				charNamesWithAssayTypes.add(bean);
//				for (LabelValueBean assayTypeBean : assayTypes) {
//					LabelValueBean labelValueWithAssay = new LabelValueBean(
//							" --" + assayTypeBean.getLabel(), charName + ":"
//									+ assayTypeBean.getValue());
//					charNamesWithAssayTypes.add(labelValueWithAssay);
//				}
//			} else {
//				SortedSet<String> datumNames = InitCharacterizationSetup
//						.getInstance().getDatumNamesByCharName(
//								wctx.getHttpServletRequest(),
//								characterizationType, charName, null);
//				// do not include if char name doesn't have any predefined datum
//				// names
//				if (datumNames != null && !datumNames.isEmpty()) {
//					charNamesWithAssayTypes.add(bean);
//				}
//			}
//		}
//		return charNamesWithAssayTypes;
	}

	public List<String> getAssayTypes(HttpServletRequest request, String characterizationName)
			throws Exception {
		SortedSet<String> assayTypes = // setup Assay Type drop down.
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(
				request, "charNameAssays",
				characterizationName, "assayType", "otherAssayType", true);
		
		List<String> types = new ArrayList<String>();
		types.addAll(assayTypes);
		return types;
	}

	public String getCharacterizationDetailPage(String charType, String charName)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			String includePage = InitCharacterizationSetup.getInstance()
					.getDetailPage(charType, charName);
			if (includePage != null) {
				String content = wctx.forwardToString(includePage);
				return content;
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	public SimpleCharacterizationProperty getCharacterizationProperties(HttpServletRequest request, 
			String charName)
			throws Exception {
		
		if (charName == null || charName.length() == 0)
			throw new Exception("CharName can't be null or empty.");
		
		String shortClassName = ClassUtils.getShortClassNameFromDisplayName(charName);
		String displayName = StringUtils.getCamelCaseFormatInWords(charName);
		
		SimpleCharacterizationProperty simpleProp = CharacterizationPropertyUtil.getPropertyClassByCharName(charName);
		if (simpleProp == null)
			return null;
		
		simpleProp.setPropertyName(shortClassName);
		simpleProp.setPropertyDisplayName(displayName);
		simpleProp.setLookups(request);
		
		return simpleProp;
		
	}
	
	

	/**
	 * This moved to initSetup rest service. To delete ...
	 * @param characterizationClassName
	 * @return
	 */
	public String getPublicCharacterizationCounts(
			String characterizationClassName) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();

		Integer counts = 0;
		try {
			counts += getService().getNumberOfPublicCharacterizations(
					characterizationClassName);
		} catch (Exception e) {
			logger
					.error("Error obtaining counts of public characterizations of type "
							+ characterizationClassName + " from local site.");
		}
		String charDisplayName = ClassUtils
				.getDisplayName(characterizationClassName);
		// remove word characterization from the display name
		if (!charDisplayName.equals("characterization")) {
			charDisplayName = charDisplayName.replaceAll(
					"(.+) characterization", "$1");
		} else {
			charDisplayName = "Characterizations";
		}
		return counts.toString() + " " + charDisplayName;
	}
}
