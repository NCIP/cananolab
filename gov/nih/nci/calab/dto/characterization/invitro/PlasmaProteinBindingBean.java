package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.invitro.PlasmaProteinBinding;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the ProteinBinding characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class PlasmaProteinBindingBean extends CharacterizationBean {
	public PlasmaProteinBindingBean() {
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
	
	public void setDerivedBioAssayData(
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
	
	public PlasmaProteinBinding getDomainObj() {
		PlasmaProteinBinding proteinBinding = new PlasmaProteinBinding();
		super.updateDomainObj(proteinBinding);
		return proteinBinding;
	}
}
