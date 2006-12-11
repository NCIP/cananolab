package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.invitro.Caspase3Activation;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.List;


/**
 * This class represents the caspase3Activation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class Caspase3ActivationBean extends CharacterizationBean {
	
	private String cellLine;	
	private String otherCellLine;
//	private String cellDeathMethod;
	
	public Caspase3ActivationBean() {
		super();
	}
	
	public Caspase3ActivationBean(Caspase3Activation aChar) {
		super(aChar);

		this.cellLine = aChar.getCellLine();
//		this.cellDeathMethod = aChar.getCellDeathMethod();
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Caspase 3 Activation");
				datum.setValueUnit("%");
			}			
		}
	}

	public String getCellLine() {
		return cellLine;
	}

	public void setCellLine(String cellLine) {
		this.cellLine = cellLine;
	}
	
	public Caspase3Activation getDomainObj() {
		Caspase3Activation caspase3Activation = new Caspase3Activation();
		super.updateDomainObj(caspase3Activation);
		if (cellLine.equals(CananoConstants.OTHER)) {
			caspase3Activation.setCellLine(this.otherCellLine);
		} else {
			caspase3Activation.setCellLine(this.cellLine);
		}
		return caspase3Activation;
	}

	public String getOtherCellLine() {
		return otherCellLine;
	}

	public void setOtherCellLine(String otherCellLine) {
		this.otherCellLine = otherCellLine;
	}
}
