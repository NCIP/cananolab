package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Purity;
import gov.nih.nci.calab.domain.nano.characterization.physical.Stressor;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;

import java.util.List;


/**
 * This class represents the Purity characterization information to be shown in
 * the view page.
 * 
 * @author chande
 * 
 */
public class PurityBean extends CharacterizationBean {
	private String id;
	/*
	private String homogeneityInLigand;
	private String residualSolvents;
	private String freeComponents;
	private String freeComponentsUnit;
	*/
	
	public PurityBean() {
		super();
		initSetup();
	}
	
	public PurityBean(Purity aChar) {
		super(aChar);

		this.id = aChar.getId().toString();
		/*
		if (aChar.getHomogeneityInLigand() != null)
			this.homogeneityInLigand = aChar.getHomogeneityInLigand().toString();
		this.residualSolvents = aChar.getResidualSolvents();
		if (aChar.getFreeComponents() != null) {
			this.freeComponents = aChar.getFreeComponents().getValue();
			this.freeComponentsUnit = aChar.getFreeComponents().getUnitOfMeasurement();
		}
		*/
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		initSetup();
	}
	
	public void initSetup() {
		/*
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
		*/
	}
	
	public Purity getDomainObj() {
		Purity purity = new Purity();
		super.updateDomainObj(purity);
		
		if (this.id != null && !this.id.equals(""))
			purity.setId(new Long(this.id));
		
		/*
		purity.setHomogeneityInLigand(new Float(this.homogeneityInLigand));
		purity.setResidualSolvents(this.residualSolvents);
		purity.setFreeComponents(new Measurement(this.freeComponents, this.freeComponentsUnit));
		*/
		return purity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
/*
	public String getFreeComponents() {
		return freeComponents;
	}

	public void setFreeComponents(String freeComponents) {
		this.freeComponents = freeComponents;
	}

	public String getFreeComponentsUnit() {
		return freeComponentsUnit;
	}

	public void setFreeComponentsUnit(String freeComponentsUnit) {
		this.freeComponentsUnit = freeComponentsUnit;
	}

	public String getHomogeneityInLigand() {
		return homogeneityInLigand;
	}

	public void setHomogeneityInLigand(String homogeneityInLigand) {
		this.homogeneityInLigand = homogeneityInLigand;
	}

	public String getResidualSolvents() {
		return residualSolvents;
	}

	public void setResidualSolvents(String residualSolvents) {
		this.residualSolvents = residualSolvents;
	}
*/

}
