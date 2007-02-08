/**
 * 
 */
package gov.nih.nci.calab.domain;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author zengje
 * 
 */
public class Report extends LabFile {
	private static final long serialVersionUID = 1234567890L;
	private Collection<Nanoparticle> particleCollection = new HashSet<Nanoparticle>();

	/**
	 * 
	 */
	public Report() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Collection<Nanoparticle> getParticleCollection() {
		return particleCollection;
	}

	public void setParticleCollection(Collection<Nanoparticle> particleCollection) {
		this.particleCollection = particleCollection;
	}

}
