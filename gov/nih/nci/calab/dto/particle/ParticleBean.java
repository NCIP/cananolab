package gov.nih.nci.calab.dto.particle;

import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.inventory.SampleBean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

	private String[] characterizations;

	private String[] keywords;

	private String[] visibilityGroups;

	private String gridNode;

	public ParticleBean(String id, String name) {
		super(id, name);
	}

	public ParticleBean() {
	}

	public ParticleBean(String id, String name, String particleSource,
			String particleType, String particleClassification,
			String[] functionTypes, String[] characterizations,
			String[] keywords) {
		this(id, name);
		setSampleType(particleType);
		setSampleSource(particleSource);
		this.particleClassification = particleClassification;
		this.functionTypes = functionTypes;
		this.characterizations = characterizations;
		this.keywords = keywords;
	}

	public ParticleBean(Nanoparticle particle) {
		this(particle.getId().toString(), particle.getName());
		this.setSampleType(particle.getType());
		this.setSampleSource(particle.getSource().getOrganizationName());
		if (particle.getSource() != null) {
			this.setSampleSource(particle.getSource().getOrganizationName());
		} else {
			this.setSampleSource("");
		}
		this.particleClassification = particle.getClassification();
		Collection<Keyword> keywordCol = particle.getKeywordCollection();
		// get a unique set of keywords
		SortedSet<String> keywordSet = new TreeSet<String>();
		for (Keyword keywordObj : keywordCol) {
			keywordSet.add(keywordObj.getName());
		}
		keywords = keywordSet.toArray(new String[0]);

		Collection<Characterization> characterizationCol = particle
				.getCharacterizationCollection();
		// get a unique list of characterization
		Set<String> charcterizationSet = new HashSet<String>();
		for (Characterization charObj : characterizationCol) {
			charcterizationSet.add(charObj.getClassification() + ":"
					+ charObj.getName());
		}
		characterizations = charcterizationSet.toArray(new String[0]);

		Collection<Function> functionCol = particle.getFunctionCollection();
		// get a unique list of function
		Set<String> functionTypeSet = new HashSet<String>();
		for (Function funcObj : functionCol) {
			functionTypeSet.add(funcObj.getType());
		}
		functionTypes = functionTypeSet.toArray(new String[0]);
	}

	public ParticleBean(Nanoparticle particle, String gridNode) {
		this(particle);
		this.gridNode = gridNode;
	}

	public String[] getCharacterizations() {
		return characterizations;
	}

	public void setCharacterizations(String[] characterizations) {
		this.characterizations = characterizations;
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

	public String getGridNode() {
		return gridNode;
	}

	public void setGridNode(String gridNode) {
		this.gridNode = gridNode;
	}

}
