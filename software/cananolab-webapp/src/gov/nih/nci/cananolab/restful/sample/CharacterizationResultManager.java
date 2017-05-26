/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.restful.bean.LabelValueBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Methods for DWR Ajax
 *
 * @author pansu, tanq
 *
 */
@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("characterizationResultManager")
public class CharacterizationResultManager {
	
	private Logger logger = Logger.getLogger(CharacterizationResultManager.class);
	
	public List<String> getConditionOptions(HttpServletRequest request) throws Exception {
		SortedSet<String> conditions = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(request,
						"datumConditions", "condition", "name", "otherName",
						true);
		// add other condition names stored in the session for the char
		SortedSet<String> otherCharConditionNames = (SortedSet<String>) request
				.getSession().getAttribute("otherCharConditionNames");
		if (otherCharConditionNames != null) {
			conditions.addAll(otherCharConditionNames);
		}
		
		List<String> nms = new ArrayList<String>();
		nms.addAll(conditions);
		nms.add("other");
		return nms;
	}

	/**
	 * conditionName == column name == char name ?
	 */
	public List<String> getConditionPropertyOptions(HttpServletRequest request, String conditionName)
			throws Exception {
		SortedSet<String> properties = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(request,
						"conditionProperties", conditionName, "property",
						"otherProperty", true);
		// add other condition properties stored in the session for the char
		SortedSet<String> otherConditionProperties = (SortedSet<String>) request
				.getSession().getAttribute("otherCharConditionProperties");
		if (otherConditionProperties != null) {
			properties.addAll(otherConditionProperties);
		}
		
		List<String> props = new ArrayList<String>();
		props.addAll(properties);
		CommonUtil.addOtherToList(props);
		
		return props;
	}

	public List<String> getColumnNameOptionsByType(HttpServletRequest request, String columnType, String charType,
			String charName, String assayType)
			throws Exception {
		
		if (columnType.equals("datum"))
			return getDatumNameOptions(request, charType, charName, assayType);
		else if (columnType.equals("condition"))
			return getConditionOptions(request);
		
		return new ArrayList<String>();
		
	}
	public List<String> getDatumNameOptions(HttpServletRequest request, String characterizationType,
			String characterizationName, String assayType) throws Exception {
		SortedSet<String> names = InitCharacterizationSetup.getInstance()
				.getDatumNamesByCharName(request,
						characterizationType, characterizationName, assayType);
		// add other datum names stored in the session for the char
		SortedSet<String> otherColumnNames = (SortedSet<String>) request
				.getSession().getAttribute("otherCharDatumNames");
		if (otherColumnNames != null) {
			names.addAll(otherColumnNames);
		}
		
		List<String> nms = new ArrayList<String>();
		nms.addAll(names);
		nms.add("other");
		return nms;
	}

	/**
	 * Get datum options based on char type, name and assay type for the 3rd drop down in 
	 * Advanced Sample Search -> Characterization Criteria
	 * 
	 * @param request
	 * @param characterizationType
	 * @param characterizationName
	 * @param assayType
	 * @return
	 * @throws Exception
	 */
	public List<LabelValueBean> getDecoratedDatumNameOptions(
			HttpServletRequest request,
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

	public List<String> getColumnValueUnitOptions(HttpServletRequest request, 
			String name, String property, boolean addOther)
			throws Exception {
		String valueName = name;
		
		if (!StringUtils.isEmpty(property)) {
			if (!property.equalsIgnoreCase("null")) //quick fix
				valueName = property;
		}
		
		SortedSet<String> units = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(request,
						"valueUnits", valueName, "unit", "otherUnit", true);
		// add other value unit stored in the session for the char
		SortedSet<String> otherValueUnits = (SortedSet<String>) request
				.getSession().getAttribute("otherCharValueUnits");
		if (otherValueUnits != null) {
			units.addAll(otherValueUnits);
		}
		
		List<String> unitList = new ArrayList<String>();
		unitList.addAll(units);
		if (addOther)
			CommonUtil.addOtherToList(unitList);
		return unitList;
	}

	public FileBean getFileFromList(int index) {
//		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("characterizationForm"));
//		if (charForm == null) {
//			return null;
//		}
//		CharacterizationBean charBean = (CharacterizationBean) (charForm
//				.get("achar"));
//		List<FileBean> files = charBean.getTheFinding().getFiles();
//		FileBean theFile = files.get(index);
//		return theFile;
		
		return null;
	}

	public FileBean resetTheFile() throws Exception {
//		WebContext wctx = WebContextFactory.get();
//		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("characterizationForm"));
//		if (charForm == null) {
//			return null;
//		}
//		CharacterizationBean charBean = (CharacterizationBean) (charForm
//				.get("achar"));
//		FileBean fileBean = new FileBean();
//		charBean.getTheFinding().setTheFile(fileBean);
//		return fileBean;
		
		return null;
	}

	public String addColumnHeader(HttpServletRequest request, ColumnHeader header, int columnNumber) {
		//TODO: untested and unused for angular impl.
		
		if (!validateColumnHeader(header)) {
			return "contain special characters";
		}
		
		HttpSession session =  request.getSession();
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
				session.setAttribute(
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
				session.setAttribute(
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
				session.setAttribute(
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
			session.setAttribute(
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
			session.setAttribute(
					"otherCharValueTypes", otherValueTypes);
		}
		return header.getDisplayName();
	}

	private boolean validateColumnHeader(ColumnHeader header) {
		if (!StringUtils.xssValidate(header.getColumnName())) {
			return false;
		}
		if (!StringUtils.xssValidate(header.getConditionProperty())) {
			return false;
		}
		if (!StringUtils.xssValidate(header.getValueType())) {
			return false;
		}
		if (!header.getValueUnit().matches(Constants.UNIT_PATTERN)) {
			return false;
		}
		if (!StringUtils.xssValidate(header.getConstantValue())) {
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
