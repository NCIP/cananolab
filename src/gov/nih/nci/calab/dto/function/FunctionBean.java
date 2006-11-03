/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.dto.characterization.composition.SurfaceGroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengje
 *
 */
public class FunctionBean {

	private String id;
//	private String type;
	private String activationMethod;
	private String description;
	private String viewTitle;
	
	private List<LinkageBean> linkages = new ArrayList<LinkageBean>();;
	private int numberOfLinkage;
	
	/**
	 * 
	 */
	public FunctionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getActivationMethod() {
		return activationMethod;
	}

	public void setActivationMethod(String activationMethod) {
		this.activationMethod = activationMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<LinkageBean> getLinkages() {
		return linkages;
	}

	public void setLinkages(List<LinkageBean> linkages) {
		this.linkages = linkages;
	}

	public int getNumberOfLinkage() {
		return numberOfLinkage;
	}

	public void setNumberOfLinkage(int numberOfLinkage) {
		this.numberOfLinkage = numberOfLinkage;
	}

	public String getViewTitle() {
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

}
