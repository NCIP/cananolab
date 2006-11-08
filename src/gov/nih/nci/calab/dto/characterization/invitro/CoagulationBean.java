package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Coagulation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the coagulation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CoagulationBean extends CharacterizationBean {
	public CoagulationBean() {
		super();
		initSetup();
	}
	
	public CoagulationBean(Characterization aChar) {
		super(aChar);
	}
		
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentCoagulation=new DatumBean();
			percentCoagulation.setType("Percent Coagulation");
			percentCoagulation.setValueUnit("%");
			table.getDatumList().add(percentCoagulation);
		}
	}
	
	public void setCharacterizationTables(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean coagulation=new DatumBean();
			coagulation.setType("Percent Coagulation");
			coagulation.setValueUnit("%");
			table.getDatumList().add(coagulation);
		}
	}
	
	public Coagulation getDomainObj() {
		Coagulation coagulation = new Coagulation();
		super.updateDomainObj(coagulation);
		return coagulation;
	}
}
