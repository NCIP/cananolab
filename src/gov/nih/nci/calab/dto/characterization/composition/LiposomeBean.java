package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * This class represents properties of a Liposome composition to be shown in the
 * view page.
 * 
 * @author pansu
 * 
 */
public class LiposomeBean extends CompositionBean {
	private String polymerized = CaNanoLabConstants.BOOLEAN_NO;

	private String polymerName;

	public LiposomeBean() {

	}

	public LiposomeBean(LiposomeComposition liposome) {
		super(liposome);
		this.polymerized = (liposome.getPolymerized()) ? CaNanoLabConstants.BOOLEAN_YES
				: CaNanoLabConstants.BOOLEAN_NO;
		this.polymerName = liposome.getPolymerName();
	}

	public String getPolymerized() {
		return this.polymerized;
	}

	public void setPolymerized(String polymerized) {
		this.polymerized = polymerized;
	}

	public String getPolymerName() {
		return this.polymerName;
	}

	public LiposomeComposition getDomainObj() {
		LiposomeComposition doComp = new LiposomeComposition();
		super.updateDomainObj(doComp);
		boolean polymerizedStatus = (this.polymerized
				.equalsIgnoreCase(CaNanoLabConstants.BOOLEAN_YES)) ? true
				: false;
		doComp.setPolymerized(polymerizedStatus);
		doComp.setPolymerName(this.polymerName);
		return doComp;
	}

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}

}
