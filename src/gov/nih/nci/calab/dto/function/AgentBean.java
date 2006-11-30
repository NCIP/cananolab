package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.Antibody;
import gov.nih.nci.calab.domain.nano.function.DNA;
import gov.nih.nci.calab.domain.nano.function.ImageContrastAgent;
import gov.nih.nci.calab.domain.nano.function.Peptide;
import gov.nih.nci.calab.domain.nano.function.Probe;
import gov.nih.nci.calab.domain.nano.function.SmallMolecule;
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
	private String type=CananoConstants.PEPTIDE;
	private String name;
	private String description;
	// otherValue can be "compoundName" for SamllMolecule; "type" for Probe and ImageContrastAgent;
	//"speicies" for Antibody; or "sequence" for DNA and Peptide
	
	private String otherValue;
	private String numberOfAgentTargets;
	
	private List<AgentTargetBean>agentTargets=new ArrayList<AgentTargetBean>();
	
	/**
	 * 
	 */
	public AgentBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getOtherValue() {
		return otherValue;
	}
	public void setOtherValue(String otherValue) {
		this.otherValue = otherValue;
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
	
	public Agent getDomainObj(){
		if (type.equals(CananoConstants.DNA)){
			DNA doAgent = new DNA();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setSequence(otherValue);
			for (AgentTargetBean agentTarget: getAgentTargets())
			doAgent.getAgentTargetCollection().add(agentTarget.getDomainObj());
			return doAgent;
		} else if (type.equals(CananoConstants.PEPTIDE)) {
			Peptide doAgent = new Peptide();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setSequence(otherValue);
			for (AgentTargetBean agentTarget: getAgentTargets())
			doAgent.getAgentTargetCollection().add(agentTarget.getDomainObj());	
			return doAgent;
		} else if (type.equals(CananoConstants.SMALL_MOLECULE)) {
			SmallMolecule doAgent = new SmallMolecule();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(name);
			doAgent.setCompoundName(otherValue);
			for (AgentTargetBean agentTarget: getAgentTargets())
			doAgent.getAgentTargetCollection().add(agentTarget.getDomainObj());	
			return doAgent;
		} else if (type.equals(CananoConstants.PROBE)) {
			Probe doAgent = new Probe();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(name);
			doAgent.setType(otherValue);
			for (AgentTargetBean agentTarget: getAgentTargets())
			doAgent.getAgentTargetCollection().add(agentTarget.getDomainObj());	
			return doAgent;
		}else if (type.equals(CananoConstants.ANTIBODY)) {
			Antibody doAgent = new Antibody();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(name);
			doAgent.setSpecies(otherValue);
			for (AgentTargetBean agentTarget: getAgentTargets())
			doAgent.getAgentTargetCollection().add(agentTarget.getDomainObj());	
			return doAgent;
		} else {
			ImageContrastAgent doAgent = new ImageContrastAgent();
			if (getId() != null && getId().length() > 0) {
				doAgent.setId(new Long(getId()));
			}
			doAgent.setDescription(description);
			doAgent.setName(name);
			doAgent.setType(otherValue);
			for (AgentTargetBean agentTarget: getAgentTargets())
			doAgent.getAgentTargetCollection().add(agentTarget.getDomainObj());	
			return doAgent;
		}	
	}
}
