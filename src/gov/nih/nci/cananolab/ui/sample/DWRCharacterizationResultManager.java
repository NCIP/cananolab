package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.ColumnBean;
import gov.nih.nci.cananolab.dto.common.DataRowBean;
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
	int tempDataRowId = -1;
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
		DataRowBean dataRowBean = null;
		if (data != null) {
			boolean hasData = false;
			int i = 0;
			for (Datum datum : data) {
				if (theFinding.getDomain().getId() != null) {
					datum.setFinding(theFinding.getDomain());
				}
				if (i == 0) {
					dataRowBean = theFinding.getDataRowBean(datum);
					if (dataRowBean == null) {
						dataRowBean = new DataRowBean();
					}
				}
				// if (conditions != null) {
				// datum.setConditionCollection(conditions);
				// }
				dataRowBean.setConditions(conditions);
				dataRowBean.addDatum(datum);
				hasData = true;
				i++;
			}
			if (hasData) {

				// if dataRowBean.rowNumber is null, set a negative id temporary
				// need to handle set nagative id to null when save to db
				if (dataRowBean.getRowNumber() == -1) {
					dataRowBean.setRowNumber(tempDataRowId--);
				}
				theFinding.addDataRow(dataRowBean);
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
			DataRowBean dataRowBean = null;
			int i = 0;
			for (Datum datum : data) {
				if (theFinding.getDomain().getId() != null) {
					datum.setFinding(theFinding.getDomain());
				}
				if (i == 0) {
					dataRowBean = theFinding.getDataRowBean(datum);
					if (dataRowBean == null) {
						return theFinding;
					}
				}
				if (conditions != null && conditions.size() > 0) {
					for (Condition condition : conditions) {
						dataRowBean.removeCondition(condition);
					}
				}
				dataRowBean.removeDatum(datum);
				i++;
			}

			if (dataRowBean != null
					&& (dataRowBean.getData() == null || dataRowBean.getData()
							.size() == 0)) {
				theFinding.removeDataRow(dataRowBean);
			}
		}
		return theFinding;
	}

	public FindingBean findFindingById(String findingId)
			throws CharacterizationResultException {
		// List<Datum> data = service.getDataForFinding(findingId);
		// FindingBean findingBean = new FindingBean(data);
		Finding finding = service.findFindingById(findingId);
		FindingBean findingBean = new FindingBean(finding);
		findingBean.setTheDataRow(findingBean.getDataRows().get(0));
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
		// TODO::: CHANGE TO QUERY UNIT HERE (Qina)
		// SortedSet<String> units = LookupService
		// .getDefaultAndOtherLookupTypes(name, "property",
		// "otherProperty");
		SortedSet<String> units = InitCharacterizationSetup.getInstance()
				.getConditions(wctx.getHttpServletRequest());
		return units.toArray(new String[units.size()]);
	}

}
