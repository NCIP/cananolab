package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletException;

import org.apache.axis.utils.StringUtils;
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
		SortedSet<String> conditions = InitCharacterizationSetup.getInstance()
				.getConditions(wctx.getHttpServletRequest());
		return conditions.toArray(new String[conditions.size()]);
	}

	public String[] getConditionPropertyOptions(String conditionName)
			throws Exception {
		SortedSet<String> properties = LookupService
				.getDefaultAndOtherLookupTypes(conditionName, "property",
						"otherProperty");
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
		WebContext wctx = WebContextFactory.get();
		List<LabelValueBean> allDatumNames = new ArrayList<LabelValueBean>();
		//extract assayType from characterizationName
		if (characterizationName.contains(":")) {
			int ind=characterizationName.indexOf(":");
			characterizationName=characterizationName.substring(ind+1);
		}
		// if assayType is empty, use charName to look up datums, as well as
		// look up all assay types and use assay type to look up datum
		if (StringUtils.isEmpty(assayType)) {
			allDatumNames = InitSetup.getInstance().getLookupValuesAsOptions(
					characterizationName, "datumName", "otherDatumName");
			SortedSet<String> assayTypes = LookupService
					.getDefaultAndOtherLookupTypes(characterizationName,
							"assayType", "otherAssayType");
			if (assayTypes != null && !assayTypes.isEmpty()) {
				for (String type : assayTypes) {
					List<LabelValueBean> datumNamesByAssayTypes = InitSetup
							.getInstance().getLookupValuesAsOptions(type,
									"datumName", "otherDatumName");
					allDatumNames.addAll(datumNamesByAssayTypes);
				}
			}
		} else {
			allDatumNames = InitSetup.getInstance().getLookupValuesAsOptions(
					assayType, "datumName", "otherDatumName");
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
		SortedSet<String> units = InitCharacterizationSetup.getInstance()
				.getValueUnits(wctx.getHttpServletRequest(), valueName);
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

	public FileBean resetTheFile() {
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
