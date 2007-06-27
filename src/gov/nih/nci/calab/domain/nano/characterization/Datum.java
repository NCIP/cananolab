/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.Measurement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zengje
 * 
 */
public class Datum implements Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String name;

	// private Boolean control;
	private Measurement value;

	private String derivedBioAssayDataCategory;
	
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

	public String getName() {
		return name;
	}

	public void setName(String type) {
		this.name = type;
	}

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

	public void setConditionCollection(Collection<Condition> conditionCollection) {
		this.conditionCollection = conditionCollection;
	}

	public String getDerivedBioAssayDataCategory() {
		return derivedBioAssayDataCategory;
	}

	public void setDerivedBioAssayDataCategory(String derivedBioAssayDataCategory) {
		this.derivedBioAssayDataCategory = derivedBioAssayDataCategory;
	}
}
