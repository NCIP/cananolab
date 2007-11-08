/**
 * 
 */
package gov.nih.nci.calab.domain;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;
import java.util.HashSet;

public class AssociatedFile extends LabFile {

	private static final long serialVersionUID = 1234567890L;

	private Collection<Nanoparticle> particleCollection = new HashSet<Nanoparticle>();

	/**
	 * 
	 */
	public AssociatedFile() {
	}

	public Collection<Nanoparticle> getParticleCollection() {
		return this.particleCollection;
	}

	public void setParticleCollection(
			Collection<Nanoparticle> particleCollection) {
		this.particleCollection = particleCollection;
	}

}
