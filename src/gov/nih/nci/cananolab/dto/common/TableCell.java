package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.util.Constants;

/**
 * View bean for a cell in a table
 *
 * @author pansu
 *
 */
public class TableCell {
	private String value;
	private String datumOrCondition;
	private Datum datum = new Datum();
	private Condition condition = new Condition();

	public TableCell() {

	}

	public TableCell(Datum datum) {
		this.datumOrCondition = FindingBean.DATUM_TYPE;
		// display bogus placeholder datum as emtpy string
		if (datum.getValue() == null
				|| datum.getValue() == 0
				&& datum.getCreatedBy().equals(
						Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
			this.value = "";
		}
		// remove .0 from boolean
		else if (datum.getValueType().equals("boolean")) {
			if (datum.getValue() == 1) {
				// remove .0 from number
				this.value = "1";
			} else if (datum.getValue() == 0) {
				this.value = "0";
			}
		} else {
			this.value = datum.getValue().toString();
		}
		this.datum = datum;
		this.condition = null;
	}

	public TableCell(Condition condition) {
		this.datumOrCondition = "condition";
		if (condition.getValue().equals(
				Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)
				&& condition.getCreatedBy().equals(
						Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
			this.value = "";
		} else {
			this.value = condition.getValue();
		}
		this.condition = condition;
		this.datum = null;
	}

	/**
	 * @return the datumOrCondition
	 */
	public String getDatumOrCondition() {
		return datumOrCondition;
	}

	/**
	 * @param datumOrCondition
	 *            the datumOrCondition to set
	 */
	public void setDatumOrCondition(String datumOrCondition) {
		this.datumOrCondition = datumOrCondition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Datum getDatum() {
		return datum;
	}

	public void setDatum(Datum datum) {
		this.datum = datum;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

}
