package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.physical.Morphology;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;

/**
 * This class represents the Morphology characterization information to be shown
 * in the view page.
 * 
 * @author pansu
 * 
 */
public class MorphologyBean extends CharacterizationBean {
	private String type;

	public MorphologyBean() {
		super();
	}

	public MorphologyBean(MorphologyBean propBean, CharacterizationBean charBean) {
		super(charBean);
		this.type = propBean.getType();
	}

	public MorphologyBean(Morphology aChar) {
		super(aChar);
		this.type = aChar.getType();
	}

	public void updateDomainObj(Morphology morphology) {
		super.updateDomainObj(morphology);
		morphology.setType(this.type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
