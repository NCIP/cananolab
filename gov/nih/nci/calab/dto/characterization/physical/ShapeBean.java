package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.physical.Shape;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

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

	private String minDimensionUnit = "nm";

	private String maxDimensionUnit = "nm";

	private String otherShapeType;

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
		this.otherShapeType = propBean.getOtherShapeType();
	}

	public ShapeBean(Shape aChar) {
		super(aChar);

		this.type = aChar.getType();
		this.minDimension = (aChar.getMinDimension() != null) ? aChar
				.getMinDimension().toString() : "";
		this.maxDimension = (aChar.getMaxDimension() != null) ? aChar
				.getMaxDimension().toString() : "";
		this.minDimensionUnit = "nm";
		this.maxDimensionUnit = "nm";
	}

	public void updateDomainObj(Shape shape) {
		super.updateDomainObj(shape);

		if (this.type.equals(CaNanoLabConstants.OTHER)) {
			shape.setType(this.otherShapeType);
		} else {
			shape.setType(this.type);
		}

		shape.setMinDimension((this.minDimension.length() == 0) ? null : Float
				.valueOf(this.minDimension));
		shape.setMaxDimension((this.maxDimension.length() == 0) ? null : Float
				.valueOf(this.maxDimension));
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

	public String getOtherShapeType() {
		return otherShapeType;
	}

	public void setOtherShapeType(String otherShapeType) {
		this.otherShapeType = otherShapeType;
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
