package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;

/**
 * This class represents properties of a Polymer composition to be shown in the
 * view page.
 * 
 * @author pansu
 * 
 */
public class PolymerBean extends CompositionBean {
	private String crosslinked;

	private String crosslinkDegree;

	private String initiator;

	public PolymerBean() {
		super();
	}

	public String getCrosslinkDegree() {
		return crosslinkDegree;
	}

	public String getCrosslinked() {
		return crosslinked;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setCrosslinkDegree(String crosslinkDegree) {
		this.crosslinkDegree = crosslinkDegree;
	}

	public void setCrosslinked(String crosslinked) {
		this.crosslinked = crosslinked;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public Characterization getDomainObj() {
		PolymerComposition doComp = new PolymerComposition();
		boolean crosslinkedStatus = (crosslinked.equalsIgnoreCase("yes")) ? true
				: false;
		doComp.setCrossLinked(crosslinkedStatus);
		if(crosslinkDegree.length()>0) {
			doComp.setCrossLinkDegree(new Float(crosslinkDegree));
		}
		doComp.setInitiator(initiator);
		doComp.setSource(getCharacterizationSource());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());		
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}
}
