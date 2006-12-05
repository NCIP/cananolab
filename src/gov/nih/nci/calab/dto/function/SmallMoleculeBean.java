package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.SmallMolecule;
/**
 * This class represents properties of SmallMolecule to be shown the function view pages.
 * 
 * @author pansu
 * 
 */
public class SmallMoleculeBean extends AgentBean{
	private String name;
	private String compoundName;
	
	public SmallMoleculeBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SmallMoleculeBean(SmallMolecule smallMolecule) {
		super(smallMolecule);
		this.name=smallMolecule.getName();
		this.compoundName=smallMolecule.getCompoundName();
	}
	public String getCompoundName() {
		return compoundName;
	}
	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public SmallMolecule getDomainObj() {
		SmallMolecule smallMolecule = new SmallMolecule();
		super.updateDomainObj(smallMolecule);
		smallMolecule.setName(name);
		smallMolecule.setCompoundName(compoundName);
		return smallMolecule;
	}
}
