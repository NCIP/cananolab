package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.physical.Shape;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleShape extends SimpleCharacterizationProperty {
	String type; //says required but empty don't give error
	Float minDimension;
	Float maxDimension;
	Float aspectRatio;
	
	String minDimensionUnit;
	String maxDimensionUnit;
	
	List<String> unitOptions = new ArrayList<String>();
	List<String> shapeTypeOptions = new ArrayList<String>();
	
	@Override
	public void setLookups(HttpServletRequest request) 
	throws Exception {
		SortedSet<String> options = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"dimensionUnits", "dimension", "unit", "otherUnit", true);
		
		if (options != null)
			unitOptions.addAll(options);
		CommonUtil.addOtherToList(unitOptions);
		
		options = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"shapeTypes", "shape", "type", "otherType", true);
		
		if (options != null)
			shapeTypeOptions.addAll(options);
		
		CommonUtil.addOtherToList(shapeTypeOptions);
		
	}
	
	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		super.transferFromPropertyBean(request, charBean);
		
		Shape shape = charBean.getShape();
		
		this.aspectRatio = shape.getAspectRatio();
		this.maxDimension = shape.getMaxDimension();
		this.maxDimensionUnit = shape.getMaxDimensionUnit();
		this.minDimension = shape.getMinDimension();
		this.minDimensionUnit = shape.getMinDimensionUnit();
		
		this.setLookups(request);
	}

	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		
		Shape shape = charBean.getShape();
		shape.setAspectRatio(this.aspectRatio);
		shape.setMaxDimension(this.maxDimension);
		shape.setMaxDimensionUnit(this.maxDimensionUnit);
		shape.setMinDimension(this.minDimension);
		shape.setMinDimensionUnit(this.minDimensionUnit);
	}




	public List<String> getUnitOptions() {
		return unitOptions;
	}

	public void setUnitOptions(List<String> unitOptions) {
		this.unitOptions = unitOptions;
	}

	public List<String> getShapeTypeOptions() {
		return shapeTypeOptions;
	}

	public void setShapeTypeOptions(List<String> shapeTypeOptions) {
		this.shapeTypeOptions = shapeTypeOptions;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Float getMinDimension() {
		return minDimension;
	}

	public void setMinDimension(Float minDimension) {
		this.minDimension = minDimension;
	}

	public Float getMaxDimension() {
		return maxDimension;
	}

	public void setMaxDimension(Float maxDimension) {
		this.maxDimension = maxDimension;
	}

	public Float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(Float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public String getMinDimensionUnit() {
		return minDimensionUnit;
	}

	public void setMinDimensionUnit(String minDimensionUnit) {
		this.minDimensionUnit = minDimensionUnit;
	}

	public String getMaxDimensionUnit() {
		return maxDimensionUnit;
	}

	public void setMaxDimensionUnit(String maxDimensionUnit) {
		this.maxDimensionUnit = maxDimensionUnit;
	}
	
	
}
