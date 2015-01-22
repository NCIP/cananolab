package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.core.InitSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleTechniqueAndInstrument {
	
	List<SimpleExperimentBean> experiments = new ArrayList<SimpleExperimentBean>();
	
	List<String> techniqueTypeLookup = new ArrayList<String>();
	List<String> manufacturerLookup = new ArrayList<String>();
	List<String> instrumentTypeLookup = new ArrayList<String>();
	
	
	public void setupLookups(HttpServletRequest request) 
			throws Exception {
		// instrument manufacturers and techniques
		SortedSet<String> values = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"manufacturers", "instrument", "manufacturer",
				"otherManufacturer", true);
		manufacturerLookup.addAll(values);
		manufacturerLookup.add("other");

		values = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"techniqueTypes", "technique", "type", "otherType", true);

		techniqueTypeLookup.addAll(values);
		techniqueTypeLookup.add("other");
		
		instrumentTypeLookup.add("TestType1");
		instrumentTypeLookup.add("other");
	}
	
	
	public List<SimpleExperimentBean> getExperiments() {
		return experiments;
	}


	public void setExperiments(List<SimpleExperimentBean> experiments) {
		this.experiments = experiments;
	}


	public List<String> getTechniqueTypeLookup() {
		return techniqueTypeLookup;
	}


	public void setTechniqueTypeLookup(List<String> techniqueTypeLookup) {
		this.techniqueTypeLookup = techniqueTypeLookup;
	}


	public List<String> getManufacturerLookup() {
		return manufacturerLookup;
	}
	public void setManufacturerLookup(List<String> manufacturerLookup) {
		this.manufacturerLookup = manufacturerLookup;
	}
	
	
	
}
