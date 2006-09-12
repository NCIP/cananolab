package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.service.util.CananoConstants;

/**
 * This class represents properties of a Liposome composition to be shown in the
 * view page.
 * 
 * @author pansu
 * 
 */
public class LiposomeBean extends CompositionBean {
	private String polymerized = CananoConstants.BOOLEAN_NO;

	private String polymerName;

	public LiposomeBean() {
		super();
	}

	public LiposomeBean(LiposomeComposition liposome) {
		super(liposome);
		this.polymerized = (liposome.isPolymerized()) ? CananoConstants.BOOLEAN_YES
				: CananoConstants.BOOLEAN_NO;
		this.polymerName = liposome.getPolymerName();		
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

	public LiposomeComposition getDomainObj() {
		LiposomeComposition doComp = new LiposomeComposition();
		super.updateDomainObj(doComp);
		boolean polymerizedStatus = (polymerized.equalsIgnoreCase(CananoConstants.BOOLEAN_YES)) ? true
				: false;
		doComp.setPolymerized(polymerizedStatus);
		doComp.setPolymerName(polymerName);
		return doComp;
	}

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}

}
