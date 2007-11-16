package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * This class represents properties of a Polymer composition to be shown in the
 * view page.
 * 
 * @author pansu
 * 
 */
public class PolymerBean extends CompositionBean {
	private String crosslinked = CaNanoLabConstants.BOOLEAN_NO;

	private String crosslinkDegree;

	private String initiator;

	public PolymerBean() {

	}

	public PolymerBean(PolymerComposition polymer) {
		super(polymer);
		this.crosslinked = (polymer.getCrossLinked()) ? CaNanoLabConstants.BOOLEAN_YES
				: CaNanoLabConstants.BOOLEAN_NO;
		this.crosslinkDegree = (polymer.getCrossLinkDegree() == null) ? ""
				: polymer.getCrossLinkDegree().toString();
		this.initiator = polymer.getInitiator();
	}

	public String getCrosslinkDegree() {
		return this.crosslinkDegree;
	}

	public String getCrosslinked() {
		return this.crosslinked;
	}

	public String getInitiator() {
		return this.initiator;
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

	public void updateDomainObj(PolymerComposition doComp) {
		super.updateDomainObj(doComp);
		boolean crosslinkedStatus = (this.crosslinked
				.equalsIgnoreCase(CaNanoLabConstants.BOOLEAN_YES)) ? true
				: false;
		doComp.setCrossLinked(crosslinkedStatus);
		if (this.crosslinkDegree.length() > 0) {
			doComp.setCrossLinkDegree(new Float(this.crosslinkDegree));
		}
		if (this.initiator.length() > 0) {
			doComp.setInitiator(this.initiator);
		}		
	}
}