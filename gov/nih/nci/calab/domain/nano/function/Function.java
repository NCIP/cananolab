/**
 * 
 */
package gov.nih.nci.calab.domain.nano.function;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zengje
 *
 */
public class Function {
	private Long id;
	private String type;
	private String description;
	private String activationMethod;
	private String identificationName; // view title
	
	private Collection<Linkage> linkageCollection = new ArrayList<Linkage>();
	
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

	public Collection<Linkage> getLinkageCollection() {
		return linkageCollection;
	}

	public void setLinkageCollection(Collection<Linkage> linkageCollection) {
		this.linkageCollection = linkageCollection;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}
	
}
