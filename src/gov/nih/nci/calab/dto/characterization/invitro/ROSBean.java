package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.ROS;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the ros characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class ROSBean extends CharacterizationBean {
	public ROSBean() {
		super();
	}
	
	public ROSBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent ROS");
				datum.setValueUnit("%");
			}
		}
	}
	
	public ROS getDomainObj() {
		ROS ros = new ROS();
		super.updateDomainObj(ros);
		return ros;
	}
}
