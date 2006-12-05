package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.UnclassifiedAgent;
/**
 * This class represents properties of an other agent to be shown the function view pages.
 * 
 * @author pansu
 * 
 */
public class UnclassifiedAgentBean extends AgentBean{
	public UnclassifiedAgent getDomainObj() {
		UnclassifiedAgent agent=new UnclassifiedAgent();
		super.updateDomainObj(agent);
		return agent;
	}

}
