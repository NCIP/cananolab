package gov.nih.nci.calab.dto.characterization;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationProtocol;
import gov.nih.nci.calab.domain.nano.characterization.Condition;
import gov.nih.nci.calab.domain.nano.characterization.Control;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class represents the data within a characterization file to be shown in
 * the view page.
 * 
 * @author chand, beasleyj
 * 
 */
public class DatumBean {
	
	private String id;

	private String type;

	private String value;

	private String valueUnit;
	
	private ControlBean control;

	private List<ConditionBean> conditionList = new ArrayList<ConditionBean>();

	private String numberOfConditions;

	public DatumBean() {
	}
	
	public DatumBean(Datum datum) {
		this.id = datum.getId().toString();
		this.type = datum.getType();
		this.value = (datum.getValue()!=null)?StringUtils.convertToString(datum.getValue().getValue()):"";
		this.valueUnit = (datum.getValue()!=null)?StringUtils.convertToString(datum.getValue().getUnitOfMeasurement()):"";
		
		Control controlObj = datum.getControl();
		if (controlObj != null) {
			this.getControl().setId(controlObj.getId().toString());
			this.getControl().setName(controlObj.getName());
			this.getControl().setType(controlObj.getType());
		} 
		else {
			conditionList = new ArrayList<ConditionBean>();
			if ( datum.getConditionCollection() != null ) {
				for (Condition conditionData : datum.getConditionCollection()) {
					ConditionBean cBean = new ConditionBean(conditionData);
					conditionList.add(cBean);
				}
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
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

	public void setConditionList(
			List<ConditionBean> conditionList) {
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
		tableData.setType(type);
		Measurement measurement=new Measurement();
		measurement.setValue(value);
		measurement.setUnitOfMeasurement(valueUnit);
		tableData.setValue(measurement);
		
		if ( this.getConditionList().size() > 0 ) {
			for (ConditionBean condition : this.getConditionList()) {
				tableData.getConditionCollection().add(condition.getDomainObj());
			}
		}
		else {		
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
}
