package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.AgentTarget;
import gov.nih.nci.calab.domain.nano.function.Antigen;
import gov.nih.nci.calab.domain.nano.function.Receptor;
import gov.nih.nci.calab.domain.nano.function.UnclassifiedAgentTarget;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * This class represents the data associated with an agent target be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class AgentTargetBean {

	private String id;

	private String description;

	private String name;

	private String type;

	public AgentTargetBean() {
	}

	public AgentTargetBean(AgentTarget agentTarget) {
		this.id = agentTarget.getId().toString();
		this.description = agentTarget.getDescription();
		if (agentTarget instanceof Antigen) {
			this.type = CaNanoLabConstants.ANTIGEN;
			this.name = ((Antigen) agentTarget).getName();
		}
		if (agentTarget instanceof Receptor) {
			this.type = CaNanoLabConstants.RECEPTOR;
			this.name = ((Receptor) agentTarget).getName();
		} else if (agentTarget instanceof UnclassifiedAgentTarget) {
			this.type = CaNanoLabConstants.OTHER;
		}
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void updateDomainObj(AgentTarget agentTarget) {
		agentTarget.setDescription(this.description);
		if (this.type.equals(CaNanoLabConstants.RECEPTOR)) {
			((Receptor) agentTarget).setName(this.name);

		} else if (this.type.equals(CaNanoLabConstants.ANTIGEN)) {
			((Antigen) agentTarget).setName(this.name);
		}
	}

	public AgentTarget getDomainObj() {
		AgentTarget agentTarget = null;
		if (this.type.equals(CaNanoLabConstants.RECEPTOR)) {
			agentTarget = new Receptor();
			((Receptor) agentTarget).setName(this.name);
		} else if (this.type.equals(CaNanoLabConstants.ANTIGEN)) {
			agentTarget = new Antigen();
			((Antigen) agentTarget).setName(this.name);
		} else {
			agentTarget = new UnclassifiedAgentTarget();
		}
		agentTarget.setDescription(this.description);
		return agentTarget;
	}
}
