package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.restful.core.InitSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleSolubility extends SimpleCharacterizationProperty {
	String solvent;  //all are not required
	String isSoluble; //this could be empty
	float criticalConcentration;
	String concentrationUnit;
	
	List<String> solventOptions = new ArrayList<String>();
	List<String> concentrationUnitOptions = new ArrayList<String>();

	@Override
	public void setLookups(HttpServletRequest request) 
	throws Exception {
		SortedSet<String> options = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"solventTypes", "solubility", "solvent", "otherSolvent", true);
		
		if (options != null)
			solventOptions.addAll(options);
		
		options = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"concentrationUnits", "sample concentration", "unit",
				"otherUnit", true);
		
		if (options != null)
			concentrationUnitOptions.addAll(options);
	}

	public String getSolvent() {
		return solvent;
	}

	public void setSolvent(String solvent) {
		this.solvent = solvent;
	}

	public String getIsSoluble() {
		return isSoluble;
	}

	public void setIsSoluble(String isSoluble) {
		this.isSoluble = isSoluble;
	}

	public float getCriticalConcentration() {
		return criticalConcentration;
	}

	public void setCriticalConcentration(float criticalConcentration) {
		this.criticalConcentration = criticalConcentration;
	}

	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	public void setConcentrationUnit(String concentrationUnit) {
		this.concentrationUnit = concentrationUnit;
	}

	public List<String> getSolventOptions() {
		return solventOptions;
	}

	public void setSolventOptions(List<String> solventOptions) {
		this.solventOptions = solventOptions;
	}

	public List<String> getConcentrationUnitOptions() {
		return concentrationUnitOptions;
	}

	public void setConcentrationUnitOptions(List<String> concentrationUnitOptions) {
		this.concentrationUnitOptions = concentrationUnitOptions;
	}
	
	
}
