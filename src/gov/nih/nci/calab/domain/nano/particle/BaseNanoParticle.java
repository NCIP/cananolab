package gov.nih.nci.calab.domain.nano.particle;

import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.nano.characterization.BaseCharacterization;
import gov.nih.nci.calab.domain.nano.function.Function;

import java.util.Collection;

public class BaseNanoParticle extends Sample {

	private String particleCategory; 
	
	private Collection keywordCollection;
	
	private Collection<Function> functionCollection;
	
	private Collection<BaseCharacterization> characterizationCollection;
	
	public BaseNanoParticle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getParticleCategory() {
		return particleCategory;
	}

	public void setParticleCategory(String particleCategory) {
		this.particleCategory = particleCategory;
	}

	public Collection<Function> getFunctionCollection() {
		return functionCollection;
	}

	public void setFunctionCollection(Collection<Function> functionCollection) {
		this.functionCollection = functionCollection;
	}

	public Collection getKeywordCollection() {
		return keywordCollection;
	}

	public void setKeywordCollection(Collection keywordCollection) {
		this.keywordCollection = keywordCollection;
	}

	public Collection<BaseCharacterization> getCharacterizationCollection() {
		return characterizationCollection;
	}

	public void setCharacterizationCollection(
			Collection<BaseCharacterization> characterizationCollection) {
		this.characterizationCollection = characterizationCollection;
	}
	
	
}
