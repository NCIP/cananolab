package gov.nih.nci.calab.domain.nano.function;

public interface Linkage {

	public void setDescription(String description);
	public String getDescription();

	public void setId(Long id);
	public Long getId();
	
	public void setAgent(Agent agent);
	public Agent getAgent();

}
