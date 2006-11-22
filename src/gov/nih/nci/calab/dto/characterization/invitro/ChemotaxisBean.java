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
		for (DerivedBioAssayDataBean table: getDerivedBioAssayDataList()) {
			DatumBean rfu=new DatumBean();
			rfu.setType("Relative Fluorescent Values");
			rfu.setValueUnit("RFU");
			table.getDatumList().add(rfu);
		}
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
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
