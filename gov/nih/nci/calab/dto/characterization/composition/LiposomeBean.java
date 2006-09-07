package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;

/**
 * This class represents properties of a Liposome composition to be shown in the
 * view page.
 * 
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
		LiposomeComposition doComp = new LiposomeComposition();
		boolean polymerizedStatus = (polymerized.equalsIgnoreCase("yes")) ? true
				: false;
		doComp.setPolymerized(polymerizedStatus);
		doComp.setPolymerName(polymerName);
		doComp.setSource(getCharacterizationSource());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}

}
