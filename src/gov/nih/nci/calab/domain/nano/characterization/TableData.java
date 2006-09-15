/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.Measurement;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zengje
 *
 */
public class TableData {
	
	private Long id;
	private String type;
	private Boolean control;
	private Measurement value;
	private TableDataControl tableDataControl;
	
	private Collection<TableDataCondition> tableDataConditionCollection = new ArrayList<TableDataCondition>();
	
	/**
	 * 
	 */
	public TableData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getControl() {
		return control;
	}
	public void setControl(Boolean control) {
		this.control = control;
	}
	public Measurement getValue() {
		return value;
	}
	public void setValue(Measurement value) {
		this.value = value;
	}
	public TableDataControl getTableDataControl() {
		return tableDataControl;
	}
	public void setTableDataControl(TableDataControl tableDataControl) {
		this.tableDataControl = tableDataControl;
	}
	public Collection<TableDataCondition> getTableDataConditionCollection() {
		return tableDataConditionCollection;
	}
	public void setTableDataConditionCollection(
			Collection<TableDataCondition> tableDataConditionCollection) {
		this.tableDataConditionCollection = tableDataConditionCollection;
	}
}
