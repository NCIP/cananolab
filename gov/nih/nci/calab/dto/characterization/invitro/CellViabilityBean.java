package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CellViability;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the cellViability characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CellViabilityBean extends CharacterizationBean {
	public CellViabilityBean() {
		super();
		initSetup();
	}
	
	public CellViabilityBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentCellViability=new DatumBean();
			percentCellViability.setType("Percent Cell Viability");
			percentCellViability.setValueUnit("%");
			table.getDatumList().add(percentCellViability);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentCellViability=new DatumBean();
			percentCellViability.setType("Percent Cell Viability");
			percentCellViability.setValueUnit("%");
			table.getDatumList().add(percentCellViability);
		}
	}
	
	public CellViability getDomainObj() {
		CellViability cellViability = new CellViability();
		super.updateDomainObj(cellViability);
		return cellViability;
	}
}
