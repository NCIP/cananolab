package gov.nih.nci.calab.dto.function;

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
	private String type="Peptide";
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
}
