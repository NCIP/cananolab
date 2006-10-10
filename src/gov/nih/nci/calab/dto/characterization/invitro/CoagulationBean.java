package gov.nih.nci.calab.dto.characterization.invitro;

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
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean average=new DatumBean();
			average.setType("Average");
			DatumBean zaverage=new DatumBean();
			average.setType("Z-Average");
			DatumBean pdi=new DatumBean();
			average.setType("PDI");
			table.getDatumList().add(average);
			table.getDatumList().add(zaverage);
			table.getDatumList().add(pdi);
		}
	}
	
	public void setCharacterizationTables(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean average=new DatumBean();
			average.setType("Average");
			average.setValueUnit("nm");
			DatumBean zaverage=new DatumBean();
			zaverage.setType("Z-Average");
			zaverage.setValueUnit("nm");
			DatumBean pdi=new DatumBean();
			pdi.setType("PDI");
			table.getDatumList().add(average);
			table.getDatumList().add(zaverage);
			table.getDatumList().add(pdi);
		}
	}
	
	public Coagulation getDomainObj() {
		Coagulation coagulation = new Coagulation();
		super.updateDomainObj(coagulation);
		return coagulation;
	}
}
