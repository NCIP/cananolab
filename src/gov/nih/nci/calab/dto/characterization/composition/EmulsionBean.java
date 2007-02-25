package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * This class represents properties of a Dendrimer composition to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class EmulsionBean extends CompositionBean {
	private String emulsionType;

	private String molecularFormula;

	private String polymerized = CaNanoLabConstants.BOOLEAN_NO;

	private String polymerName;

	public EmulsionBean() {
		super();
	}

	public EmulsionBean(EmulsionComposition emulsion) {
		super(emulsion);
		this.emulsionType = emulsion.getType();
		this.molecularFormula = emulsion.getMolecularFormula();
		this.polymerized = (emulsion.getPolymerized()) ? CaNanoLabConstants.BOOLEAN_YES
				: CaNanoLabConstants.BOOLEAN_NO;
		this.polymerName = emulsion.getPolymerName();
	}

	public String getEmulsionType() {
		return emulsionType;
	}

	public void setEmulsionType(String emulsionType) {
		this.emulsionType = emulsionType;
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
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

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}

	public EmulsionComposition getDomainObj() {
		EmulsionComposition doComp = new EmulsionComposition();
		super.updateDomainObj(doComp);
		doComp.setType(emulsionType);
		doComp.setMolecularFormula(molecularFormula);
		boolean polymerizedStatus = (polymerized.equalsIgnoreCase(CaNanoLabConstants.BOOLEAN_YES)) ? true
				: false;
		doComp.setPolymerized(polymerizedStatus);
		doComp.setPolymerName(polymerName);
		return doComp;
	}
}
