package gov.nih.nci.calab.dto.particle;

import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.Composition;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.Collection;

/**
 * This class represents shared properties of nanoparticles to be shown in the
 * view page.
 * 
 * @author pansu
 * 
 */
public class ParticleBean extends SampleBean {

	private String particleClassification;

	private String[] functionTypes;

	private String[] characterizationTypes;

	private String[] keywords;

	private String[] visibilityGroups;

	public ParticleBean(String id, String name) {
		super(id, name);
	}

	public ParticleBean() {
	}

	public ParticleBean(String id, String name, String particleSource,
			String particleType, String particleClassification,
			String[] functionTypes, String[] characterizationTypes,
			String[] keywords) {
		this(id, name);
		setSampleType(particleType);
		setSampleSource(particleSource);
		this.particleClassification = particleClassification;
		this.functionTypes = functionTypes;
		this.characterizationTypes = characterizationTypes;
		this.keywords = keywords;
	}

	public ParticleBean(Nanoparticle particle) {
		this(particle.getId().toString(), particle.getName());
		this.setSampleType(particle.getType());
		this.setSampleSource(particle.getSource().getOrganizationName());
		this.particleClassification = particle.getClassification();
		Collection<Keyword> keywordCol = particle.getKeywordCollection();
		int i = 0;
		keywords = new String[keywordCol.size()];
		for (Keyword keywordObj : keywordCol) {
			keywords[i] = keywordObj.getName();
			i++;
		}
		Collection<Characterization> characterizationCol = particle
				.getCharacterizationCollection();
		int j = 0;
		characterizationTypes = new String[characterizationCol.size()];
		for (Characterization charObj : characterizationCol) {
			String charType="";
			if (charObj instanceof Composition) {
				charType=CalabConstants.COMPOSITION_CHARACTERIZATION;
			}
			//TODO add other types here
			characterizationTypes[j] = charObj.getClassification()+":"+charType;
			j++;
		}
	}

	public String[] getCharacterizationTypes() {
		return characterizationTypes;
	}

	public void setCharacterizationTypes(String[] characterizationTypes) {
		this.characterizationTypes = characterizationTypes;
	}

	public String[] getFunctionTypes() {
		return functionTypes;
	}

	public void setFunctionTypes(String[] functionTypes) {
		this.functionTypes = functionTypes;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getParticleClassification() {
		return particleClassification;
	}

	public void setParticleClassification(String particleClassification) {
		this.particleClassification = particleClassification;
	}

	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

}
