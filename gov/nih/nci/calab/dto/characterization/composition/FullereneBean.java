package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.service.util.CalabConstants;

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
		super();
	}

	public String getNumberOfCarbons() {
		return numberOfCarbons;
	}

	public void setNumberOfCarbons(String numberOfCarbons) {
		this.numberOfCarbons = numberOfCarbons;
	}

	public Characterization getDomainObj() {
		FullereneComposition doComp = new FullereneComposition();
		doComp.setNumberOfCarbon(new Integer(numberOfCarbons));
			doComp.setSource(getCharacterizationSource());
		doComp.setClassification(getCharacterizationClassification());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());
		doComp.setName(CalabConstants.COMPOSITION_CHARACTERIZATION);
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}
}
