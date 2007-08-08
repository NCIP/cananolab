/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.Attachment;
import gov.nih.nci.calab.domain.nano.function.Encapsulation;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.domain.nano.function.UnclassifiedLinkage;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * @author zengje
 * 
 */
public class LinkageBean {

	private String id;

	private String description;

	private String type;

	private AgentBean agent = new AgentBean();

	private String bondType;// for atttachments

	private String localization; // for encapsulations

	/**
	 * 
	 */
	public LinkageBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LinkageBean(Linkage linkage) {
		this.description = linkage.getDescription();
		this.id = linkage.getId().toString();
		if (linkage instanceof Attachment) {
			bondType = ((Attachment) linkage).getBondType();
			this.type = CaNanoLabConstants.ATTACHMENT;
		} else if (linkage instanceof Encapsulation) {
			localization = ((Encapsulation) linkage).getLocalization();
			this.type = CaNanoLabConstants.ENCAPSULATION;
		} else if (linkage instanceof UnclassifiedLinkage) {
			this.type = CaNanoLabConstants.OTHER;
		}
		this.agent = new AgentBean(linkage.getAgent());
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

	public void updateDomainObj(Linkage doLinkage) {
		doLinkage.setDescription(description);
		if (type.equals(CaNanoLabConstants.ATTACHMENT)) {
			((Attachment) doLinkage).setBondType(bondType);
		} else if (type.equals(CaNanoLabConstants.ENCAPSULATION)) {
			((Encapsulation) doLinkage).setLocalization(localization);
		}
		updateAgent(doLinkage);
	}

	public Linkage getDomainObj() {
		Linkage doLinkage = null;
		// create new instances of different types if not persisted already
		if (type.equals(CaNanoLabConstants.ATTACHMENT)) {
			doLinkage = new Attachment();
		} else if (type.equals(CaNanoLabConstants.ENCAPSULATION)) {
			doLinkage = new Encapsulation();
		} else {
			doLinkage = new UnclassifiedLinkage();
		}

		doLinkage.setDescription(description);
		if (type.equals(CaNanoLabConstants.ATTACHMENT)) {
			((Attachment) doLinkage).setBondType(bondType);
		} else if (type.equals(CaNanoLabConstants.ENCAPSULATION)) {
			((Encapsulation) doLinkage).setLocalization(localization);
		}
		updateAgent(doLinkage);
		return doLinkage;
	}

	private void updateAgent(Linkage doLinkage) {
		if (agent.getId() != null) {
			Agent doAgent = doLinkage.getAgent();
			if (doAgent != null
					&& agent.getType().equals(
							doAgent.getClass().getSimpleName())) {
				agent.updateDomainObj(doAgent);
			}
			// if the agent type is updated create new instance of new agent
			// type
			else {
				Agent newDoAgent = agent.getDomainObj();
				doLinkage.setAgent(newDoAgent);
			}
		} else {
			Agent doAgent = agent.getDomainObj();
			doLinkage.setAgent(doAgent);
		}
	}

	public String getBondType() {
		return bondType;
	}

	public void setBondType(String bondType) {
		this.bondType = bondType;
	}

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}
}
