package gov.nih.nci.calab.domain.nano.function;

import java.util.ArrayList;
import java.util.Collection;

public class DNA implements Agent {

	private Long id;
	private String description;
	private String sequence;
	private Collection<AgentTarget> agentTargetCollection = new ArrayList<AgentTarget>();	
	public DNA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
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
