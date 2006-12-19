package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.service.util.CananoConstants;

/**
 * This class represents properties of a Polymer composition to be shown in the
 * view page.
 * 
 * @author pansu
 * 
 */
public class PolymerBean extends CompositionBean {
	private String crosslinked = CananoConstants.BOOLEAN_NO;

	private String crosslinkDegree;

	private String initiator;
	
	private String otherInitiator;

	public PolymerBean() {
		super();
	}

	public PolymerBean(PolymerComposition polymer) {
		super(polymer);
		this.crosslinked = (polymer.isCrossLinked()) ? CananoConstants.BOOLEAN_YES
				: CananoConstants.BOOLEAN_NO;
		this.crosslinkDegree = (polymer.getCrossLinkDegree() == null) ? ""
				: polymer.getCrossLinkDegree().toString();
		this.initiator = polymer.getInitiator();		
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
	
	public PolymerComposition getDomainObj() {
		PolymerComposition doComp = new PolymerComposition();
		super.updateDomainObj(doComp);
		boolean crosslinkedStatus = (crosslinked.equalsIgnoreCase(CananoConstants.BOOLEAN_YES)) ? true
				: false;
		doComp.setCrossLinked(crosslinkedStatus);
		if (crosslinkDegree.length() > 0) {
			doComp.setCrossLinkDegree(new Float(crosslinkDegree));
		}
		if (initiator.length() > 0) {
			if (initiator.equalsIgnoreCase(CananoConstants.OTHER)){
				initiator = otherInitiator;
			}
			doComp.setInitiator(initiator);
		}
		return doComp;
	}

	public String getOtherInitiator() {
		return otherInitiator;
	}

	public void setOtherInitiator(String otherInitiator) {
		this.otherInitiator = otherInitiator;
	}
}
