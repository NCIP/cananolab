package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

/**
 * This class represents properties of a Fullerene composition to be shown in
 * the view page.
 * @author pansu
 *
 */
public class FullereneBean extends CompositionBean {
	private String numberOfCarbons;
	
	public FullereneBean(){
		super();
	}

	public String getNumberOfCarbons() {
		return numberOfCarbons;
	}

	public void setNumberOfCarbons(String numberOfCarbons) {
		this.numberOfCarbons = numberOfCarbons;
	}
	public Characterization getDomainObj() {
		//TODO fill in details;
		return null;
	}
}
