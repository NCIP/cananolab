package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRCharacterizationManager {
	Logger logger = Logger.getLogger(DWRCharacterizationManager.class);
	private CharacterizationServiceLocalImpl service;

	private CharacterizationServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		service = new CharacterizationServiceLocalImpl(user);
		return service;
	}

	public String[] getCharacterizationOptions(String characterizationType)
			throws Exception {
		if (characterizationType.length() == 0) {
			return null;
		}
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> charNames = InitCharacterizationSetup.getInstance()
				.getCharNamesByCharType(wctx.getHttpServletRequest(),
						characterizationType);
		String[] charNameArray = new String[charNames.size()];
		charNames.toArray(charNameArray);
		return charNameArray;
	}

	public List<LabelValueBean> getDecoratedCharacterizationOptions(
			String characterizationType) throws Exception {
		if (StringUtils.isEmpty(characterizationType)) {
			return null;
		}
		WebContext wctx = WebContextFactory.get();
		List<LabelValueBean> charNames = InitCharacterizationSetup
				.getInstance().getDecoratedCharNamesByCharType(
						wctx.getHttpServletRequest(), characterizationType);
		List<LabelValueBean> charNamesWithAssayTypes = new ArrayList<LabelValueBean>();
		for (LabelValueBean bean : charNames) {
			String charName = bean.getValue();
			// setup Assay Type drop down.
			List<LabelValueBean> assayTypes = InitSetup.getInstance()
					.getDefaultAndOtherTypesByLookupAsOptions(charName,
							"assayType", "otherAssayType");
			if (!assayTypes.isEmpty()) {
				charNamesWithAssayTypes.add(bean);
				for (LabelValueBean assayTypeBean : assayTypes) {
					LabelValueBean labelValueWithAssay = new LabelValueBean(
							" --" + assayTypeBean.getLabel(), charName + ":"
									+ assayTypeBean.getValue());
					charNamesWithAssayTypes.add(labelValueWithAssay);
				}
			} else {
				SortedSet<String> datumNames = InitCharacterizationSetup
						.getInstance().getDatumNamesByCharName(
								wctx.getHttpServletRequest(),
								characterizationType, charName, null);
				// do not include if char name doesn't have any predefined datum
				// names
				if (datumNames != null && !datumNames.isEmpty()) {
					charNamesWithAssayTypes.add(bean);
				}
			}
		}
		return charNamesWithAssayTypes;
	}

	public String[] getAssayTypeOptions(String characterizationName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> assayTypes = // setup Assay Type drop down.
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(
				wctx.getHttpServletRequest(), "charNameAssays",
				characterizationName, "assayType", "otherAssayType", true);
		return assayTypes.toArray(new String[assayTypes.size()]);
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

	public String getPublicCharacterizationCounts(
			String characterizationClassName, String[] locations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		if (locations == null || locations.length == 0) {
			locations = new String[1];
			locations[0] = Constants.APP_OWNER;
			// return null;
		}
		Integer counts = 0;
		for (String location : locations) {
			if (location.equals(Constants.LOCAL_SITE)) {
				try {
					counts += getService().getNumberOfPublicCharacterizations(
							characterizationClassName);
				} catch (Exception e) {
					logger
							.error("Error obtaining counts of public characterizations of type "
									+ characterizationClassName
									+ " from local site.");
				}
			} else {
				try {
					String serviceUrl = InitSetup.getInstance()
							.getGridServiceUrl(request, location);

					CharacterizationService service = new CharacterizationServiceRemoteImpl(
							serviceUrl);
					counts += service
							.getNumberOfPublicCharacterizations(characterizationClassName);
				} catch (Exception e) {
					logger
							.error("Error obtaining counts of public characterizations of type "
									+ characterizationClassName
									+ " from "
									+ location);
				}
			}
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
