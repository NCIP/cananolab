package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.physical.Solubility;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleSolubility extends SimpleCharacterizationProperty {
	String solvent;  //all are not required
	String isSoluble; //this could be empty
	Float criticalConcentration;
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
		CommonUtil.addOtherToList(solventOptions);
		
		options = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"concentrationUnits", "sample concentration", "unit",
				"otherUnit", true);
		
		if (options != null)
			concentrationUnitOptions.addAll(options);
		CommonUtil.addOtherToList(concentrationUnitOptions);
	}
	
	

	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		super.transferFromPropertyBean(request, charBean);
		
		Solubility solubility = charBean.getSolubility();
		if (solubility == null) return;
		
		this.criticalConcentration = solubility.getCriticalConcentration();
		this.concentrationUnit = solubility.getCriticalConcentrationUnit();
		if (solubility.getIsSoluble() != null)
			this.isSoluble = solubility.getIsSoluble().toString();
		else
			this.isSoluble = "";
		
		this.solvent = solubility.getSolvent();
		
	}

	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
	
		Solubility solubility = charBean.getSolubility();
		solubility.setCriticalConcentration(this.criticalConcentration);
		solubility.setCriticalConcentrationUnit(this.concentrationUnit);
		solubility.setSolvent(solvent);
		
		if (this.isSoluble != null && this.isSoluble.trim().length() > 0) {
			solubility.setIsSoluble(Boolean.parseBoolean(isSoluble));
		}
			
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

	

	public Float getCriticalConcentration() {
		return criticalConcentration;
	}



	public void setCriticalConcentration(Float criticalConcentration) {
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
