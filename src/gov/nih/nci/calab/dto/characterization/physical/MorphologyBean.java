package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.physical.Morphology;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * This class represents the Morphology characterization information to be shown
 * in the view page.
 * 
 * @author pansu
 * 
 */
public class MorphologyBean extends CharacterizationBean {
	private String type;

	private String otherType;

	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	public MorphologyBean() {
		super();
	}

	public MorphologyBean(MorphologyBean propBean, CharacterizationBean charBean) {
		super(charBean);
		this.type = propBean.getType();
		this.otherType = propBean.getOtherType();
	}

	public MorphologyBean(Morphology aChar) {
		super(aChar);
		this.type = aChar.getType();
	}

	public void updateDomainObj(Morphology morphology) {
		super.updateDomainObj(morphology);

		if (this.type.equals(CaNanoLabConstants.OTHER)
				&& !this.otherType.equalsIgnoreCase("")) {
			morphology.setType(this.otherType);
		} else {
			morphology.setType(this.type);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
