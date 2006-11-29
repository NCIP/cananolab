/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengje
 *
 */
public class FunctionBean {

	private String id;
	private String type;
	private String activationMethod;
	private String description;
	private String viewTitle;
	
	private List<LinkageBean> linkages = new ArrayList<LinkageBean>();;
	private int numberOfLinkages;
	
	/**
	 * 
	 */
	public FunctionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FunctionBean(String id, String type, String viewTitle) {
		this.id=id;		
		this.type=type;
		this.viewTitle=viewTitle;
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

	public int getNumberOfLinkages() {
		return numberOfLinkages;
	}

	public void setNumberOfLinkages(int numberOfLinkages) {
		this.numberOfLinkages = numberOfLinkages;
	}

	public String getViewTitle() {
			// get only the first number of characters of the title
		if (viewTitle!=null &&viewTitle.length() > CalabConstants.MAX_VIEW_TITLE_LENGTH) {
			return viewTitle.substring(0, CalabConstants.MAX_VIEW_TITLE_LENGTH);
		}
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
