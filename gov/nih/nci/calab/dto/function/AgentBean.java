package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.AgentTarget;
import gov.nih.nci.calab.domain.nano.function.Antibody;
import gov.nih.nci.calab.domain.nano.function.DNA;
import gov.nih.nci.calab.domain.nano.function.ImageContrastAgent;
import gov.nih.nci.calab.domain.nano.function.Peptide;
import gov.nih.nci.calab.domain.nano.function.Probe;
import gov.nih.nci.calab.domain.nano.function.SmallMolecule;
import gov.nih.nci.calab.domain.nano.function.UnclassifiedAgent;
import gov.nih.nci.calab.service.util.CananoConstants;

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

	private String type = CananoConstants.PEPTIDE;

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

	public Agent getDomainObj() {
		if (type.equals(CananoConstants.DNA)) {
			DNA doAgent = new DNA();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setSequence(((DNABean) this).getSequence());
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		} else if (type.equals(CananoConstants.PEPTIDE)) {
			Peptide doAgent = new Peptide();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setSequence(((PeptideBean) this).getSequence());
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		} else if (type.equals(CananoConstants.SMALL_MOLECULE)) {
			SmallMolecule doAgent = new SmallMolecule();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(((SmallMoleculeBean) this).getName());
			doAgent.setCompoundName(((SmallMoleculeBean) this)
					.getCompoundName());
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		} else if (type.equals(CananoConstants.PROBE)) {
			Probe doAgent = new Probe();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(((ProbeBean) this).getName());
			doAgent.setType(((ProbeBean) this).getType());
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		} else if (type.equals(CananoConstants.ANTIBODY)) {
			Antibody doAgent = new Antibody();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(((AntibodyBean) this).getName());
			doAgent.setSpecies(((AntibodyBean) this).getSpecies());
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		} else if (type.equals(CananoConstants.ANTIBODY)) {
			Antibody doAgent = new Antibody();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(((AntibodyBean) this).getName());
			doAgent.setSpecies(((AntibodyBean) this).getSpecies());
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		} else if (type.equals(CananoConstants.IMAGE_CONTRAST_AGENT)) {
			ImageContrastAgent doAgent = new ImageContrastAgent();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(((ImageContrastAgentBean) this).getName());
			doAgent.setType(((ImageContrastAgentBean) this).getType());
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		} else {
			UnclassifiedAgent doAgent = new UnclassifiedAgent();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			for (AgentTargetBean agentTarget : getAgentTargets())
				doAgent.getAgentTargetCollection().add(
						agentTarget.getDomainObj());
			return doAgent;
		}
	}
}
