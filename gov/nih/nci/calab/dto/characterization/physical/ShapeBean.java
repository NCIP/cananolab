package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Shape;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;

/**
 * This class represents the Shape characterization information to be shown in
 * the view page.
 * 
 * @author chande
 * 
 */
public class ShapeBean extends CharacterizationBean {
	private String type;

	private String maxDimension;

	private String minDimension;

	private String minDimensionUnit="nm";

	private String maxDimensionUnit="nm";

	//this is used for validation only
	private String validateValue;
	
	public String getValidateValue() {
		return validateValue;
	}

	public void setValidateValue(String validateValue) {
		this.validateValue = validateValue;
	}

	public ShapeBean() {
		super();
	}

	public ShapeBean(ShapeBean propBean, CharacterizationBean charBean) {
		super(charBean);
		this.type = propBean.getType();
		this.maxDimension = propBean.getMaxDimension();
		this.minDimension = propBean.getMinDimension();
		this.minDimensionUnit = propBean.getMinDimensionUnit();
		this.maxDimensionUnit = propBean.getMaxDimensionUnit();
	}

	public ShapeBean(Shape aChar) {
		super(aChar);

		this.type = aChar.getType();
		this.minDimension = (aChar.getMinDimension() != null) ? aChar
				.getMinDimension().getValue().toString() : "";
		this.maxDimension = (aChar.getMaxDimension() != null) ? aChar
				.getMaxDimension().getValue().toString() : "";
		this.minDimensionUnit = "nm";
		this.maxDimensionUnit = "nm";
	}

	public void updateDomainObj(Shape shape) {
		super.updateDomainObj(shape);

		shape.setType(this.type);
		if ((this.minDimension == null) || (this.minDimension.length() == 0)){
			shape.setMinDimension(null);
		} else {
			shape.setMinDimension(new Measurement(new Float(this.minDimension), this.minDimensionUnit));
		}
		
		if ((this.maxDimension == null) || (this.maxDimension.length() == 0)) {
			shape.setMaxDimension(null);
		} else {
			shape.setMaxDimension(new Measurement(new Float(this.maxDimension), this.maxDimensionUnit));
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaxDimension() {
		return maxDimension;
	}

	public void setMaxDimension(String maxDimension) {
		this.maxDimension = maxDimension;
	}

	public String getMinDimension() {
		return minDimension;
	}

	public void setMinDimension(String minDimension) {
		this.minDimension = minDimension;
	}

	public String getMaxDimensionUnit() {
		return maxDimensionUnit;
	}

	public void setMaxDimensionUnit(String maxDimensionUnit) {
		this.maxDimensionUnit = maxDimensionUnit;
	}

	public String getMinDimensionUnit() {
		return minDimensionUnit;
	}

	public void setMinDimensionUnit(String minDimensionUnit) {
		this.minDimensionUnit = minDimensionUnit;
	}
}
