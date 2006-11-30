package gov.nih.nci.calab.domain.nano.function;

import java.util.Collection;

public interface Agent {

	public void setDescription(String description);
	public String getDescription();

	public void setId(Long id);
	public Long getId();

	public void setAgentTargetCollection(Collection<AgentTarget> agentTargetCollection);
	public Collection<AgentTarget> getAgentTargetCollection();
}
