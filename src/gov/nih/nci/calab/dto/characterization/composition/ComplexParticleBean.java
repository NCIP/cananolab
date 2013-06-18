/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComplexComposition;

/**
 * This class represents properties of a Complext Nanoparticle composition to be
 * shown in the view page.
 * 
 * @author pansu
 * 
 */
public class ComplexParticleBean extends CompositionBean {

	public ComplexParticleBean() {
		super();
	}

	public ComplexParticleBean(ComplexComposition complex) {
		super(complex);
	}
	
	public ComplexComposition getDomainObj() {
		ComplexComposition doComp = new ComplexComposition();
		super.updateDomainObj(doComp);
		return doComp;
	}
}
