package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Control;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the data within a characterization file to be shown in
 * the view page.
 * 
 * @author chand, beasleyj, pansu
 * 
 */
public class DatumBean {

	private String id;

	private String name;

	private String value;

	private String valueType;

	private String unit;

	private String std;

	private String category;

	private String isAControl = CaNanoLabConstants.BOOLEAN_NO;

	private ControlBean control = new ControlBean();

	private List<ConditionBean> conditionList = new ArrayList<ConditionBean>();

	private String numberOfConditions;

	public DatumBean() {
	}

	public DatumBean(Datum datum) {
		this.id = datum.getId().toString();
		this.name = datum.getName();
		this.value = (datum.getValue() != null) ? StringUtils
				.convertToString(datum.getValue().getValue()) : "";
		this.valueType = (datum.getValue().getValueType() != null) ? StringUtils
				.convertToString(datum.getValue().getValueType()) : "";

		this.unit = (datum.getValue() != null) ? StringUtils
				.convertToString(datum.getValue().getUnitOfMeasurement()) : "";

		// Control controlObj = datum.getControl();
		// if (controlObj != null) {
		// control = new ControlBean();
		// control.setName(controlObj.getName());
		// control.setType(controlObj.getType());
		// isAControl = CaNanoLabConstants.BOOLEAN_YES;
		// } else {
		// isAControl = CaNanoLabConstants.BOOLEAN_NO;
		// }
		// if (datum.getConditionCollection() != null
		// && datum.getConditionCollection().size() > 0) {
		// conditionList = new ArrayList<ConditionBean>();
		// for (Condition conditionData : datum.getConditionCollection()) {
		// ConditionBean cBean = new ConditionBean(conditionData);
		// conditionList.add(cBean);
		// }
		// }

		this.numberOfConditions = conditionList.size() + "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String type) {
		this.name = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public ControlBean getControl() {
		return control;
	}

	public void setControl(ControlBean control) {
		this.control = control;
	}

	public List<ConditionBean> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<ConditionBean> conditionList) {
		this.conditionList = conditionList;
	}

	public String getNumberOfConditions() {
		return numberOfConditions;
	}

	public void setNumberOfConditions(String numberOfConditions) {
		this.numberOfConditions = numberOfConditions;
	}

	public Datum getDomainObj() {
		Datum tableData = new Datum();
		if (getId() != null && getId().length() > 0) {
			tableData.setId(new Long(getId()));
		}
		tableData.setName(name);
		Measurement measurement = new Measurement();
		if (value.length() > 0)
			measurement.setValue(new Float(value));
		measurement.setUnitOfMeasurement(unit);
		tableData.setValue(measurement);

		if (this.getConditionList() != null
				&& this.getConditionList().size() > 0) {
			for (ConditionBean condition : this.getConditionList()) {
				tableData.getConditionCollection()
						.add(condition.getDomainObj());
			}
		}

		if (isAControl.equals(CaNanoLabConstants.BOOLEAN_YES)) {
			Control control = new Control();
			if (getControl() != null) {
				if (getControl().getId() != null)
					control.setId(new Long(getControl().getId()));
				control.setName(getControl().getName());
				control.setType(getControl().getType());
				tableData.setControl(control);
			}
		}

		return tableData;
	}

	public String getIsAControl() {
		return isAControl;
	}

	public void setIsAControl(String isAControl) {
		this.isAControl = isAControl;
	}

	public String getStd() {
		return std;
	}

	public void setStd(String std) {
		this.std = std;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
}
