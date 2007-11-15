/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.MetalParticleComposition;

/**
 * This class represents properties of a Metal Particle composition to be shown
 * in the view page.
 * 
 * @author pansu
 */
public class MetalParticleBean extends BaseCoreShellCoatingBean {

	public MetalParticleBean() {

	}

	public MetalParticleBean(MetalParticleComposition doComp) {
		super(doComp);
	}
}
