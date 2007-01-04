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
		super();
		// TODO Auto-generated constructor stub
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
		}
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

	public AgentTarget getDomainObj() {
		if (type.equals(CaNanoLabConstants.RECEPTOR)) {
			Receptor doReceptor = new Receptor();
			if (getId() != null && getId().length() > 0) {
				doReceptor.setId(new Long(getId()));
			}
			doReceptor.setDescription(description);
			doReceptor.setName(name);
			return doReceptor;
		} else if (type.equals(CaNanoLabConstants.ANTIGEN)) {
			Antigen doAntigen = new Antigen();
			if (getId() != null && getId().length() > 0) {
				doAntigen.setId(new Long(getId()));
			}
			doAntigen.setDescription(description);
			doAntigen.setName(name);
			return doAntigen;
		} else {			
			UnclassifiedAgentTarget doAntigen=new UnclassifiedAgentTarget();
			if (getId() != null && getId().length() > 0) {
				doAntigen.setId(new Long(getId()));
			}
			doAntigen.setDescription(description);			
			return doAntigen;
		}

	}
}
