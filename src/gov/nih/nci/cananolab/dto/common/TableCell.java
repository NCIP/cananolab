package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;

/**
 * View bean for a cell in a table
 *
 * @author pansu
 *
 */
public class TableCell {
	private String value;
	private String datumOrCondition;
	private Datum datum=new Datum();
	private Condition condition=new Condition();

	public TableCell() {

	}

	public TableCell(Datum datum) {
		this.datumOrCondition = FindingBean.DATUM_TYPE;
		// Allow empty Datum (associated with a Condition).
		if (datum.getValue() != null) {
			this.value = datum.getValue().toString();
		}
		this.datum = datum;
		this.condition = null;
	}

	public TableCell(Condition condition) {
		this.datumOrCondition = "condition";
		this.value = condition.getValue();
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
