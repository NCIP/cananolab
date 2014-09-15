package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimplePhysicalState extends SimpleCharacterizationProperty{
	String type = ""; //not required but can't be null;
				//Default to "3D-cylinder?
	
	List<String> typeOptions = new ArrayList<String>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void setLookups(HttpServletRequest request) 
	throws Exception {
		
		SortedSet<String> types = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
						"physicalStateTypes", "physical state", "type", "otherType",
						true);
		
		if (types != null)
			typeOptions.addAll(types);
	}

	public List<String> getTypeOptions() {
		return typeOptions;
	}

	public void setTypeOptions(List<String> typeOptions) {
		this.typeOptions = typeOptions;
	}
	
}
