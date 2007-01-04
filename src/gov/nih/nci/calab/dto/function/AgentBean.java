package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.AgentTarget;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author zengje
 * 
 */
public class AgentBean {

	private String id;

	private String type = CaNanoLabConstants.PEPTIDE;

	private String description;

	// otherValue can be "compoundName" for SamllMolecule; "type" for Probe and
	// ImageContrastAgent;
	// "speicies" for Antibody; or "sequence" for DNA and Peptide

	private String numberOfAgentTargets;

	private List<AgentTargetBean> agentTargets = new ArrayList<AgentTargetBean>();

	/**
	 * 
	 */
	public AgentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AgentBean(Agent agent) {
		this.id = agent.getId().toString();
		this.description = agent.getDescription();
		for (AgentTarget agentTarget : agent.getAgentTargetCollection()) {
			agentTargets.add(new AgentTargetBean(agentTarget));
		}
		this.numberOfAgentTargets = agentTargets.size() + "";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumberOfAgentTargets() {
		return numberOfAgentTargets;
	}

	public void setNumberOfAgentTargets(String numberOfAgentTargets) {
		this.numberOfAgentTargets = numberOfAgentTargets;
	}

	public List<AgentTargetBean> getAgentTargets() {
		return agentTargets;
	}

	public void setAgentTargets(List<AgentTargetBean> agentTargets) {
		this.agentTargets = agentTargets;
	}

	public void updateDomainObj(Agent agent) {
		for (AgentTargetBean agentTarget : getAgentTargets()) {
			agent.getAgentTargetCollection().add(agentTarget.getDomainObj());
		}
		agent.setDescription(description);
		if (getId() != null && getId().length() > 0) {
			agent.setId(new Long(getId()));
		}		
	}
}
