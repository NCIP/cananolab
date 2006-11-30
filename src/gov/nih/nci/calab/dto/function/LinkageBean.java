/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Attachment;
import gov.nih.nci.calab.domain.nano.function.Encapsulation;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.service.util.CananoConstants;

/**
 * @author zengje
 * 
 */
public class LinkageBean {

	private String id;

	private String description;

	private String type = CananoConstants.ATTACHMENT;

	private String value;

	private AgentBean agent = new AgentBean();

	/**
	 * 
	 */
	public LinkageBean() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getValue() {
		return value;
	}

	public void setValue(String linkageProperty) {
		this.value = linkageProperty;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AgentBean getAgent() {
		return agent;
	}

	public void setAgent(AgentBean agent) {
		this.agent = agent;
	}

	public Linkage getDomainObj() {
		if (type.equals(CananoConstants.ATTACHMENT)) {
			Attachment doAttach = new Attachment();
			if (getId() != null && getId().length() > 0) {
				doAttach.setId(new Long(getId()));
			}
			doAttach.setBondType(value);
			doAttach.setDescription(description);
			doAttach.setAgent(agent.getDomainObj());
			return doAttach;
		} else {
			Encapsulation doEncap = new Encapsulation();
			if (getId() != null && getId().length() > 0) {
				doEncap.setId(new Long(getId()));
			}
			doEncap.setLocalization(value);
			doEncap.setDescription(description);
			doEncap.setAgent(agent.getDomainObj());
			return doEncap;
		}
	}
}
