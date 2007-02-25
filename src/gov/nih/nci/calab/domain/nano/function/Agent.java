package gov.nih.nci.calab.domain.nano.function;

import java.util.ArrayList;
import java.util.Collection;

public class Agent implements java.io.Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String description;

	private Collection<AgentTarget> agentTargetCollection = new ArrayList<AgentTarget>();

	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<AgentTarget> getAgentTargetCollection() {
		return agentTargetCollection;
	}

	public void setAgentTargetCollection(
			Collection<AgentTarget> agentTargetCollection) {
		this.agentTargetCollection = agentTargetCollection;
	}

}
