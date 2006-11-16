package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Chemotaxis;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the chemotaxis characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class ChemotaxisBean extends CharacterizationBean {
	public ChemotaxisBean() {
		super();
		initSetup();
	}
	
	public ChemotaxisBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean rfu=new DatumBean();
			rfu.setType("Relative Fluorescent Values");
			rfu.setValueUnit("RFU");
			table.getDatumList().add(rfu);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean rfu=new DatumBean();
			rfu.setType("Relative Fluorescent Values");
			rfu.setValueUnit("RFU");
			table.getDatumList().add(rfu);
		}
	}
	
	public Chemotaxis getDomainObj() {
		Chemotaxis chemotaxis = new Chemotaxis();
		super.updateDomainObj(chemotaxis);
		return chemotaxis;
	}
}
