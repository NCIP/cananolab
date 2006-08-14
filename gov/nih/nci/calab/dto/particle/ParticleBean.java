package gov.nih.nci.calab.dto.particle;

import gov.nih.nci.calab.dto.inventory.SampleBean;

public class ParticleBean extends SampleBean {

	private String name;

	private String particleType;
	
	private String functionTypes;
	
	private String characterizationTypes;

	private String keywords;

	public ParticleBean(String id, String name) {
		super(id, name);
	}

	public ParticleBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParticleType() {
		return particleType;
	}

	public void setParticleType(String particleType) {
		this.particleType = particleType;
	}

	public String getCharacterizationTypes() {
		return characterizationTypes;
	}

	public void setCharacterizationTypes(String characterizationTypes) {
		this.characterizationTypes = characterizationTypes;
	}

	public String getFunctionTypes() {
		return functionTypes;
	}

	public void setFunctionTypes(String functionTypes) {
		this.functionTypes = functionTypes;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
