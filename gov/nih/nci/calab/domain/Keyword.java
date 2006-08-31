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
public class Keyword {
	
	private static final long serialVersionUID = 1234567890L;
	
	private Long id;
	private String name;
	
	private Collection<Nanoparticle> nanoparticleCollection = new HashSet<Nanoparticle>();
	/**
	 * 
	 */
	public Keyword() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<Nanoparticle> getNanoparticleCollection() {
		return nanoparticleCollection;
	}
	public void setNanoparticleCollection(Collection<Nanoparticle> nanoparticleCollection) {
		this.nanoparticleCollection = nanoparticleCollection;
	}

}
