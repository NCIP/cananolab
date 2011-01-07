package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

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
		// add other condition names stored in the session for the char
		SortedSet<String> otherCharConditionNames = (SortedSet<String>) wctx
				.getSession().getAttribute("otherCharConditionNames");
		if (otherCharConditionNames != null) {
			conditions.addAll(otherCharConditionNames);
		}
		return conditions.toArray(new String[conditions.size()]);
	}

	public String[] getConditionPropertyOptions(String conditionName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> properties = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(wctx.getHttpServletRequest(),
						"conditionProperties", conditionName, "property",
						"otherProperty", true);
		// add other condition properties stored in the session for the char
		SortedSet<String> otherConditionProperties = (SortedSet<String>) wctx
				.getSession().getAttribute("otherCharConditionProperties");
		if (otherConditionProperties != null) {
			properties.addAll(otherConditionProperties);
		}
		return properties.toArray(new String[properties.size()]);
	}

	public String[] getDatumNameOptions(String characterizationType,
			String characterizationName, String assayType) throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> names = InitCharacterizationSetup.getInstance()
				.getDatumNamesByCharName(wctx.getHttpServletRequest(),
						characterizationType, characterizationName, assayType);
		// add other datum names stored in the session for the char
		SortedSet<String> otherColumnNames = (SortedSet<String>) wctx
				.getSession().getAttribute("otherCharDatumNames");
		if (otherColumnNames != null) {
			names.addAll(otherColumnNames);
		}
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
		// add other value unit stored in the session for the char
		SortedSet<String> otherValueUnits = (SortedSet<String>) wctx
				.getSession().getAttribute("otherCharValueUnits");
		if (otherValueUnits != null) {
			units.addAll(otherValueUnits);
		}
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
		charBean.getTheFinding().setTheFile(fileBean);
		return fileBean;
	}

	public String addColumnHeader(ColumnHeader header, int columnNumber) {
		if (!validateColumnHeader(header)) {
			return "contain special characters";
		}
		if (!validateColumnHeader(header)) {
			return "contain special characters";
		}
		WebContext wctx = WebContextFactory.get();
		HttpSession session = wctx.getSession();
		// store existing columns in the session to prevent entering of
		// duplicate column
		List<ColumnHeader> columnHeaders = (List<ColumnHeader>) session
				.getAttribute("columnHeaders");
		if (columnHeaders == null) {
			columnHeaders = new ArrayList<ColumnHeader>();
			session.setAttribute("columnHeaders", columnHeaders);
		}
		if (columnHeaders.contains(header) && columnHeaders.indexOf(header)!=columnNumber) {
			return "duplicate column";
		}
		columnHeaders.add(header);

		if (header.getColumnType().equals(FindingBean.DATUM_TYPE)) {
			// remember other datum names in the session
			SortedSet<String> datumColumnNames = (SortedSet<String>) session
					.getAttribute("charDatumNames");
			SortedSet<String> otherDatumNames = new TreeSet<String>();
			if (session.getAttribute("otherCharDatumNames") != null) {
				otherDatumNames = (SortedSet<String>) session
						.getAttribute("otherCharDatumNames");
			}
			if (!datumColumnNames.contains(header.getColumnName())) {
				otherDatumNames.add(header.getColumnName());
				WebContextFactory.get().getSession().setAttribute(
						"otherCharDatumNames", otherDatumNames);
			}
		}
		if (header.getColumnType().equals(FindingBean.CONDITION_TYPE)) {
			// remember other condition names in the session
			SortedSet<String> conditionColumnNames = (SortedSet<String>) session
					.getAttribute("datumConditions");
			SortedSet<String> otherConditionNames = new TreeSet<String>();
			if (session.getAttribute("otherCharConditionNames") != null) {
				otherConditionNames = (SortedSet<String>) session
						.getAttribute("otherCharConditionNames");
			}
			if (!conditionColumnNames.contains(header.getColumnName())) {
				otherConditionNames.add(header.getColumnName());
				WebContextFactory.get().getSession().setAttribute(
						"otherCharConditionNames", otherConditionNames);
			}
		}

		if (header.getColumnType().equals(FindingBean.CONDITION_TYPE)) {
			// remember other condition properties in the session
			SortedSet<String> conditionProperties = (SortedSet<String>) session
					.getAttribute("conditionProperties");
			SortedSet<String> otherConditionProperties = new TreeSet<String>();
			if (session.getAttribute("otherCharConditionProperties") != null) {
				otherConditionProperties = (SortedSet<String>) session
						.getAttribute("otherCharConditionProperties");
			}
			if (!conditionProperties.contains(header.getConditionProperty())
					&& !StringUtils.isEmpty(header.getConditionProperty())) {
				otherConditionProperties.add(header.getConditionProperty());
				WebContextFactory.get().getSession().setAttribute(
						"otherCharConditionProperties",
						otherConditionProperties);
			}
		}
		// remember other value units in the session
		SortedSet<String> valueUnits = (SortedSet<String>) session
				.getAttribute("valueUnits");
		SortedSet<String> otherValueUnits = new TreeSet<String>();
		if (session.getAttribute("otherCharValueUnits") != null) {
			otherValueUnits = (SortedSet<String>) session
					.getAttribute("otherCharValueUnits");
		}
		if (!valueUnits.contains(header.getValueUnit())
				&& !StringUtils.isEmpty(header.getValueUnit())) {
			otherValueUnits.add(header.getValueUnit());
			WebContextFactory.get().getSession().setAttribute(
					"otherCharValueUnits", otherValueUnits);
		}

		// remember other value types in the session
		SortedSet<String> valueTypes = (SortedSet<String>) session
				.getAttribute("datumConditionValueTypes");
		SortedSet<String> otherValueTypes = new TreeSet<String>();
		if (session.getAttribute("otherCharValueTypes") != null) {
			otherValueTypes = (SortedSet<String>) session
					.getAttribute("otherCharValueTypes");
		}
		if (!valueTypes.contains(header.getValueType())
				&& !StringUtils.isEmpty(header.getValueType())) {
			otherValueTypes.add(header.getValueType());
			WebContextFactory.get().getSession().setAttribute(
					"otherCharValueTypes", otherValueTypes);
		}
		return header.getDisplayName();
	}

	private boolean validateColumnHeader(ColumnHeader header) {
		if (!header.getColumnName().matches(
				Constants.TEXTFIELD_WHITELIST_PATTERN)) {
			return false;
		}
		if (!header.getConditionProperty().matches(
				Constants.TEXTFIELD_WHITELIST_PATTERN)) {
			return false;
		}
		if (!header.getValueType().matches(
				Constants.TEXTFIELD_WHITELIST_PATTERN)) {
			return false;
		}
		if (!header.getValueUnit().matches(Constants.UNIT_PATTERN)) {
			return false;
		}
		if (!header.getConstantValue().matches(
				Constants.TEXTFIELD_WHITELIST_PATTERN)) {
			return false;
		}
		return true;
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
