package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;

/**
 * This class represents properties of a Fullerene composition to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class FullereneBean extends CompositionBean {
	private String numberOfCarbons;

	public FullereneBean() {
		
	}

	public FullereneBean(FullereneComposition fullerene) {
		super(fullerene);
		this.numberOfCarbons = (fullerene.getNumberOfCarbon() == null) ? ""
				: fullerene.getNumberOfCarbon().toString();
	}

	public String getNumberOfCarbons() {
		return numberOfCarbons;
	}

	public void setNumberOfCarbons(String numberOfCarbons) {
		this.numberOfCarbons = numberOfCarbons;
	}

	public FullereneComposition getDomainObj() {
		FullereneComposition doComp = new FullereneComposition();
		super.updateDomainObj(doComp);
		doComp.setNumberOfCarbon(new Integer(numberOfCarbons));
		return doComp;
	}
}
