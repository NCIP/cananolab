package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.common.LookupService;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletException;

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

	public String[] getDatumNameOptions(String characterizationName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> names = InitCharacterizationSetup.getInstance()
				.getDatumNamesByCharName(wctx.getHttpServletRequest(),
						characterizationName);
		return names.toArray(new String[names.size()]);
	}

	public String[] getColumnValueUnitOptions(String name, String property)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		String valueName = name;
		if (property != null) {
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
