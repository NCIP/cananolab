package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.core.InitSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleTechniqueAndInstrument {
	
	List<SimpleExperimentBean> simpleExperiments = new ArrayList<SimpleExperimentBean>();
	
	List<String> techniquesLookup = new ArrayList<String>();
	List<String> manufacturerLookup = new ArrayList<String>();
	
	
	public void setupLookups(HttpServletRequest request) 
			throws Exception {
		// instrument manufacturers and techniques
		SortedSet<String> values = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"manufacturers", "instrument", "manufacturer",
				"otherManufacturer", true);
		manufacturerLookup.addAll(values);

		values = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"techniqueTypes", "technique", "type", "otherType", true);

		techniquesLookup.addAll(values);
	}
	
	public List<SimpleExperimentBean> getSimpleExperiments() {
		return simpleExperiments;
	}
	public void setSimpleExperiments(List<SimpleExperimentBean> simpleExperiments) {
		this.simpleExperiments = simpleExperiments;
	}
	public List<String> getTechniquesLookup() {
		return techniquesLookup;
	}
	public void setTechniquesLookup(List<String> techniquesLookup) {
		this.techniquesLookup = techniquesLookup;
	}
	public List<String> getManufacturerLookup() {
		return manufacturerLookup;
	}
	public void setManufacturerLookup(List<String> manufacturerLookup) {
		this.manufacturerLookup = manufacturerLookup;
	}
	
	
	
}
