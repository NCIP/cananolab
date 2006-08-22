package gov.nih.nci.calab.domain.nano.particle;

import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Function;

import java.util.Collection;

public class Nanoparticle extends Sample {

	private String classification; 
	
	private Collection keywordCollection;
	
	private Collection<Function> functionCollection;
	
	private Collection<Characterization> characterizationCollection;
	
	public Nanoparticle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
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

	public Collection<Characterization> getCharacterizationCollection() {
		return characterizationCollection;
	}

	public void setCharacterizationCollection(
			Collection<Characterization> characterizationCollection) {
		this.characterizationCollection = characterizationCollection;
	}
	
	
}
