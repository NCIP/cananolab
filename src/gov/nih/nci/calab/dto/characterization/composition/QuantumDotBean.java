/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

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
