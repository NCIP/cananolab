package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.service.util.CalabConstants;

/**
 * This class represents properties of a Dendrimer composition to be shown in
 * the view page.
 * @author pansu
 *
 */
public class EmulsionBean extends CompositionBean {
	private String emulsionType;
	private String molecularFormula;	
	private String polymerized;
	private String polymerName;
	
	public EmulsionBean() {
		super();
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
	public Characterization getDomainObj() {
		EmulsionComposition doComp = new EmulsionComposition();		
		doComp.setType(emulsionType);		
		doComp.setMolecularFormula(molecularFormula);
		boolean polymerizedStatus=(polymerized.equalsIgnoreCase("yes"))?true:false;
		doComp.setPolymerized(polymerizedStatus);
		doComp.setPolymerName(polymerName);
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
