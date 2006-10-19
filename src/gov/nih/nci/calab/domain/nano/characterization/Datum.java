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
public class Datum {

	private static final long serialVersionUID = 1234567890L;

	private Long id;
	private String type;
//	private Boolean control;
	private Measurement value;
	private Control control;
	
	private Collection<Condition> conditionCollection = new ArrayList<Condition>();
	
	/**
	 * 
	 */
	public Datum() {
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
//	public Boolean getControl() {
//		return control;
//	}
//	public void setControl(Boolean control) {
//		this.control = control;
//	}
	public Measurement getValue() {
		return value;
	}
	public void setValue(Measurement value) {
		this.value = value;
	}
	public Control getControl() {
		return control;
	}
	public void setControl(Control control) {
		this.control = control;
	}
	public Collection<Condition> getConditionCollection() {
		return conditionCollection;
	}
	public void setConditionCollection(
			Collection<Condition> conditionCollection) {
		this.conditionCollection = conditionCollection;
	}
}
