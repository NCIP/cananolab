/**
 * 
 */
package gov.nih.nci.calab.domain.nano.function;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

/**
 * @author zengje
 *
 */
public class Function {
	private Long id;
	private String type;
	private String activationMethod;
	
	private Nanoparticle nanoparticle;
	
	/**
	 * 
	 */
	public Function() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getActivationMethod() {
		return activationMethod;
	}

	public void setActivationMethod(String activationMethod) {
		this.activationMethod = activationMethod;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Nanoparticle getNanoparticle() {
		return nanoparticle;
	}

	public void setNanoparticle(Nanoparticle nanoparticle) {
		this.nanoparticle = nanoparticle;
	}

	
}
