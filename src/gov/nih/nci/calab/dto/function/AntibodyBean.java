package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Antibody;

public class AntibodyBean extends BaseAgentBean {
	private String name;

	private String species;

	public AntibodyBean() {
	}

	public AntibodyBean(Antibody antibody) {
		super(antibody);
		this.name = antibody.getName();
		this.species = antibody.getSpecies();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public void updateDomainObj(Antibody antibody) {
		// super has been called in AgentBean level, so no need to call again here.
//		super.updateDomainObj(antibody);
		antibody.setName(name);
		antibody.setSpecies(species);
	}
	
}
