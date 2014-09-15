package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleShape extends SimpleCharacterizationProperty {
	String type; //says required but empty don't give error
	float minDimension;
	float maxDimension;
	float aspectRatio;
	
	List<String> unitOptions = new ArrayList<String>();
	List<String> shapeTypeOptions = new ArrayList<String>();
	
	@Override
	public void setLookups(HttpServletRequest request) 
	throws Exception {
		SortedSet<String> options = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"dimensionUnits", "dimension", "unit", "otherUnit", true);
		
		if (options != null)
			unitOptions.addAll(options);
		
		options = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"shapeTypes", "shape", "type", "otherType", true);
		
		if (options != null)
			shapeTypeOptions.addAll(options);
		
	}
	
	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		// TODO Auto-generated method stub
		
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
	public float getMinDimension() {
		return minDimension;
	}
	public void setMinDimension(float minDimension) {
		this.minDimension = minDimension;
	}
	public float getMaxDimension() {
		return maxDimension;
	}
	public void setMaxDimension(float maxDimension) {
		this.maxDimension = maxDimension;
	}
	public float getAspectRatio() {
		return aspectRatio;
	}
	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}
	
	
	
}
