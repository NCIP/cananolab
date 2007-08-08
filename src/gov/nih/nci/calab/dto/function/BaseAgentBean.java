package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.AgentTarget;

import java.util.ArrayList;
import java.util.List;

public class BaseAgentBean {
	private String id;

	private String type;

	private String description;

	private List<AgentTargetBean> agentTargets = new ArrayList<AgentTargetBean>();

	public BaseAgentBean() {
		
	}
	public BaseAgentBean(Agent agent) {
		this.id = agent.getId().toString();
		this.description = agent.getDescription();
		for (AgentTarget agentTarget : agent.getAgentTargetCollection()) {
			getAgentTargets().add(new AgentTargetBean(agentTarget));
		}
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

	public List<AgentTargetBean> getAgentTargets() {
		return agentTargets;
	}

	public void setAgentTargets(List<AgentTargetBean> agentTargets) {
		this.agentTargets = agentTargets;
	}

	public void updateDomainObj(Agent doAgent) {
		doAgent.setDescription(description);
	}
}
