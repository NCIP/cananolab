package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.QuantumDotComposition;

/**
 * This class represents properties of a Quantum Dot composition to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class QuantumDotBean extends BaseCoreShellCoatingBean {
	public QuantumDotBean() {
		super();
	}

	public QuantumDotBean(QuantumDotComposition doComp) {
		super(doComp);
	}
	
	public QuantumDotComposition getDomainObj() {
		QuantumDotComposition doComp = new QuantumDotComposition();
		super.updateDomainObj(doComp);
		return doComp;
	}
}
