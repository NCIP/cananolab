package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Probe;
/**
 * This class represents properties of Probe to be shown the function view pages.
 * 
 * @author pansu
 * 
 */
public class ProbeBean extends AgentBean{
	private String type;
	private String name;
	
	
	public ProbeBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProbeBean(Probe probe) {
		super(probe);
		this.type=probe.getType();
		this.name=probe.getName();
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
	
}
