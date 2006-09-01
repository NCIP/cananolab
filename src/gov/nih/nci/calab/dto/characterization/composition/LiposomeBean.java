package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

/**
 * This class represents properties of a Liposome composition to be shown in
 * the view page.
 * @author pansu
 *
 */
public class LiposomeBean extends CompositionBean {
	private String polymerized;
	private String polymerName;

	public LiposomeBean() {
		super();
	}

	public String getPolymerized() {
		return polymerized;
	}

	public void setPolymerized(String polymerized) {
		this.polymerized = polymerized;
	}


	public String getPolymerName() {
		return polymerName;
	}
	public Characterization getDomainObj() {
		//TODO fill in details;
		return null;
	}

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}
	
}
