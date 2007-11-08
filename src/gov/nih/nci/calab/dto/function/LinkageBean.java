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

	}

	public LinkageBean(Linkage linkage) {
		this.description = linkage.getDescription();
		this.id = linkage.getId().toString();
		if (linkage instanceof Attachment) {
			this.bondType = ((Attachment) linkage).getBondType();
			this.type = CaNanoLabConstants.ATTACHMENT;
		} else if (linkage instanceof Encapsulation) {
			this.localization = ((Encapsulation) linkage).getLocalization();
			this.type = CaNanoLabConstants.ENCAPSULATION;
		} else if (linkage instanceof UnclassifiedLinkage) {
			this.type = CaNanoLabConstants.OTHER;
		}
		this.agent = new AgentBean(linkage.getAgent());
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AgentBean getAgent() {
		return this.agent;
	}

	public void setAgent(AgentBean agent) {
		this.agent = agent;
	}

	public void updateDomainObj(Linkage doLinkage) {
		doLinkage.setDescription(this.description);
		if (this.type.equals(CaNanoLabConstants.ATTACHMENT)) {
			((Attachment) doLinkage).setBondType(this.bondType);
		} else if (this.type.equals(CaNanoLabConstants.ENCAPSULATION)) {
			((Encapsulation) doLinkage).setLocalization(this.localization);
		}
		updateAgent(doLinkage);
	}

	public Linkage getDomainObj() {
		Linkage doLinkage = null;
		// create new instances of different types if not persisted already
		if (this.type.equals(CaNanoLabConstants.ATTACHMENT)) {
			doLinkage = new Attachment();
		} else if (this.type.equals(CaNanoLabConstants.ENCAPSULATION)) {
			doLinkage = new Encapsulation();
		} else {
			doLinkage = new UnclassifiedLinkage();
		}

		doLinkage.setDescription(this.description);
		if (this.type.equals(CaNanoLabConstants.ATTACHMENT)) {
			((Attachment) doLinkage).setBondType(this.bondType);
		} else if (this.type.equals(CaNanoLabConstants.ENCAPSULATION)) {
			((Encapsulation) doLinkage).setLocalization(this.localization);
		}
		updateAgent(doLinkage);
		return doLinkage;
	}

	private void updateAgent(Linkage doLinkage) {
		Agent doAgent = null;
		if (this.agent.getId() != null) {
			doAgent = doLinkage.getAgent();
			if (doAgent != null
					&& this.agent.getType().equals(
							doAgent.getClass().getSimpleName())) {
				this.agent.updateDomainObj(doAgent);
			}
			// if the agent type is updated create new instance of new agent
			// type
			else {
				doAgent = this.agent.getDomainObj();
				doLinkage.setAgent(doAgent);
			}
		} else {
			doAgent = this.agent.getDomainObj();
			doLinkage.setAgent(doAgent);
		}
	}

	public String getBondType() {
		return this.bondType;
	}

	public void setBondType(String bondType) {
		this.bondType = bondType;
	}

	public String getLocalization() {
		return this.localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}
}
