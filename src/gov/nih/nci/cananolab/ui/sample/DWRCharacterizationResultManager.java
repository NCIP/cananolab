package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.ColumnBean;
import gov.nih.nci.cananolab.dto.common.RowBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.CharacterizationResultService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationResultServiceLocalImpl;

import java.util.List;
import java.util.SortedSet;

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
	int tempRowId = -1;
	private CharacterizationResultService service = new CharacterizationResultServiceLocalImpl();

	public FindingBean resetFinding() {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FindingBean newFindingBean = new FindingBean();
		charBean.setTheFinding(newFindingBean);
		return newFindingBean;
	}

	public FindingBean addColumnHeader(ColumnBean columnBean)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FindingBean theFinding = charBean.getTheFinding();
		if (columnBean.getDatumOrCondition().equalsIgnoreCase("Condition")) {
			theFinding.addConditionColumnBean(columnBean);
		} else {
			theFinding.addDatumColumnBean(columnBean);
		}
		return theFinding;
	}

	public FindingBean removeColumnHeader(ColumnBean columnBean)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FindingBean theFinding = charBean.getTheFinding();
		if (columnBean.getDatumOrCondition().equalsIgnoreCase("Condition")) {
			theFinding.removeConditionColumnBean(columnBean);
		} else {
			theFinding.removeDatumColumnBean(columnBean);
		}
		return theFinding;
	}

	// addRow includes the function add and edit
	public FindingBean addRow(List<Datum> data, List<Condition> conditions)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FindingBean theFinding = charBean.getTheFinding();
		RowBean rowBean = null;
		if (data != null) {
			boolean hasData = false;
			int i = 0;
			for (Datum datum : data) {
				if (theFinding.getDomain().getId() != null) {
					datum.setFinding(theFinding.getDomain());
				}
				if (i == 0) {
					rowBean = theFinding.getRowBean(datum);
					if (rowBean == null) {
						rowBean = new RowBean();
					}
				}
				// if (conditions != null) {
				// datum.setConditionCollection(conditions);
				// }
				rowBean.setConditions(conditions);
				rowBean.addDatum(datum);
				hasData = true;
				i++;
			}
			if (hasData) {

				// if rowBean.rowNumber is null, set a negative id temporary
				// need to handle set nagative id to null when save to db
				if (rowBean.getRowNumber() == -1) {
					rowBean.setRowNumber(tempRowId--);
				}
				theFinding.addRow(rowBean);
			}
		}
		return theFinding;
	}

	// addRow includes the function add and edit
	public FindingBean deleteRow(List<Datum> data, List<Condition> conditions)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FindingBean theFinding = charBean.getTheFinding();
		if (data != null) {
			RowBean rowBean = null;
			int i = 0;
			for (Datum datum : data) {
				if (theFinding.getDomain().getId() != null) {
					datum.setFinding(theFinding.getDomain());
				}
				if (i == 0) {
					rowBean = theFinding.getRowBean(datum);
					if (rowBean == null) {
						return theFinding;
					}
				}
				if (conditions != null && conditions.size() > 0) {
					for (Condition condition : conditions) {
						rowBean.removeCondition(condition);
					}
				}
				rowBean.removeDatum(datum);
				i++;
			}

			if (rowBean != null
					&& (rowBean.getData() == null || rowBean.getData().size() == 0)) {
				theFinding.removeRow(rowBean);
			}
		}
		return theFinding;
	}

	public FindingBean findFindingById(String findingId)
			throws CharacterizationResultException {
		Finding finding = service.findFindingById(findingId);
		FindingBean findingBean = new FindingBean(finding);
		findingBean.setTheRow(findingBean.getRows().get(0));
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		charBean.setTheFinding(findingBean);
		return findingBean;
	}

	public String[] getConditionOptions() throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> conditions = InitCharacterizationSetup.getInstance()
				.getConditions(wctx.getHttpServletRequest());
		return conditions.toArray(new String[conditions.size()]);
	}

	public String[] getConditionPropertyOptions(String conditionName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();

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

}
