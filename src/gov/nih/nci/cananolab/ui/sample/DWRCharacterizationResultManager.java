package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletException;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 * 
 * @author pansu, tanq
 * 
 */
public class DWRCharacterizationResultManager {
	public String[] getConditionOptions() throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> conditions = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(wctx.getHttpServletRequest(),
						"datumConditions", "condition", "name", "otherName",
						true);
		return conditions.toArray(new String[conditions.size()]);
	}

	public String[] getConditionPropertyOptions(String conditionName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> properties = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(wctx.getHttpServletRequest(),
						"conditionProperties", conditionName, "property",
						"otherProperty", true);
		return properties.toArray(new String[properties.size()]);
	}

	public String[] getDatumNameOptions(String characterizationType,
			String characterizationName, String assayType) throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> names = InitCharacterizationSetup.getInstance()
				.getDatumNamesByCharName(wctx.getHttpServletRequest(),
						characterizationType, characterizationName, assayType);
		return names.toArray(new String[names.size()]);
	}

	public List<LabelValueBean> getDecoratedDatumNameOptions(
			String characterizationType, String characterizationName,
			String assayType) throws Exception {
		// extract assayType from characterizationName
		if (characterizationName.contains(":")) {
			int ind = characterizationName.indexOf(":");
			assayType = characterizationName.substring(ind + 1);
			characterizationName = characterizationName.substring(0, ind);
		}
		List<LabelValueBean> allDatumNames = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookupAsOptions(characterizationName,
						"datumName", "otherDatumName");
		// if assayType is empty, use charName to look up datums, as well as
		// look up all assay types and use assay type to look up datum
		if (StringUtils.isEmpty(assayType)) {
			List<LabelValueBean> assayTypeBeans = InitSetup
					.getInstance()
					.getDefaultAndOtherTypesByLookupAsOptions(
							characterizationName, "assayType", "otherAssayType");
			if (assayTypeBeans != null && !assayTypeBeans.isEmpty()) {
				for (LabelValueBean bean : assayTypeBeans) {
					List<LabelValueBean> datumNamesByAssayTypes = InitSetup
							.getInstance()
							.getDefaultAndOtherTypesByLookupAsOptions(
									bean.getValue(), "datumName",
									"otherDatumName");
					for (LabelValueBean lv : datumNamesByAssayTypes) {
						if (!allDatumNames.contains(lv)) {
							allDatumNames.add(lv);
						}
					}
				}
			}
		} else {
			allDatumNames.addAll(InitSetup.getInstance()
					.getDefaultAndOtherTypesByLookupAsOptions(assayType,
							"datumName", "otherDatumName"));
		}
		return allDatumNames;
	}

	public String[] getColumnValueUnitOptions(String name, String property)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		String valueName = name;
		if (!StringUtils.isEmpty(property)) {
			valueName = property;
		}
		SortedSet<String> units = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(wctx.getHttpServletRequest(),
						"valueUnits", valueName, "unit", "otherUnit", true);
		return units.toArray(new String[units.size()]);
	}

	public FileBean getFileFromList(int index) {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		if (charForm == null) {
			return null;
		}
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		List<FileBean> files = charBean.getTheFinding().getFiles();
		FileBean theFile = files.get(index);
		return theFile;
	}

	public FileBean resetTheFile() throws Exception {
		WebContext wctx = WebContextFactory.get();
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		if (charForm == null) {
			return null;
		}
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FileBean fileBean = new FileBean();

		// set file default visibilities
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		// get assigned visible groups for samples
		String sampleName = (String) wctx.getSession().getAttribute(
				"sampleName");
		if (sampleName == null) {
			return null;
		}

		List<String> accessibleGroups = authService.getAccessibleGroups(
				sampleName, Constants.CSM_READ_PRIVILEGE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		fileBean.setVisibilityGroups(visibilityGroups);
		charBean.getTheFinding().setTheFile(fileBean);
		return fileBean;
	}

	public String addColumnHeader(ColumnHeader header) {
		// added other valueType to the dropdown list in the session
		SortedSet<String> valueTypes = (SortedSet<String>) WebContextFactory
				.get().getSession().getAttribute("datumConditionValueTypes");
		if (!valueTypes.contains(header.getValueType())) {
			valueTypes.add(header.getValueType());
		}
		return header.getDisplayName();
	}

	public String getSubmitColumnPage(int columnNumber)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			String page = "/sample/characterization/shared/bodySubmitDataConditionMatrixColumn.jsp?cInd="
					+ columnNumber;
			String content = wctx.forwardToString(page);
			return content;
		} catch (Exception e) {
			return "";
		}
	}
}
