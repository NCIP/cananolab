/**
 * 
 */
package gov.nih.nci.calab.domain;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

/**
 * @author zengje
 *
 */
public class Keyword {
	private Long id;
	private String name;
	
	private Nanoparticle nanoparticle;
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
	public Nanoparticle getNanoparticle() {
		return nanoparticle;
	}
	public void setNanoparticle(Nanoparticle nanoparticle) {
		this.nanoparticle = nanoparticle;
	}

}
