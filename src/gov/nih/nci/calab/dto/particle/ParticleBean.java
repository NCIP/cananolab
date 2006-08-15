package gov.nih.nci.calab.dto.particle;

import gov.nih.nci.calab.dto.inventory.SampleBean;

public class ParticleBean extends SampleBean {

	private String particleCategory;

	private String functionTypes;

	private String characterizationTypes;

	private String keywords;

	public ParticleBean(String id, String name) {
		super(id, name);
	}

	public ParticleBean() {
	}

	public ParticleBean(String id, String name, String particleSource,
			String particleType, String particleCategory, String functionTypes,
			String characterizationTypes, String keywords) {
		this(id, name);
		setSampleType(particleType);
		setSampleSource(particleSource);
		this.particleCategory = particleCategory;
		this.functionTypes = functionTypes;
		this.characterizationTypes = characterizationTypes;
		this.keywords = keywords;
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

	public String getParticleCategory() {
		return particleCategory;
	}

	public void setParticleCategory(String particleCategory) {
		this.particleCategory = particleCategory;
	}
}
