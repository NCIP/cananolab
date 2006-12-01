package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.ImageContrastAgent;

/**
 * This class represents properties of ImageContrastAgent to be shown the function view pages.
 * 
 * @author pansu
 * 
 */
public class ImageContrastAgentBean extends AgentBean {
	private String name;
	private String type;
	public ImageContrastAgentBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImageContrastAgentBean(ImageContrastAgent imageContrastAgent) {
		super(imageContrastAgent);
		this.name=imageContrastAgent.getName();
		this.type=imageContrastAgent.getType();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
