package gov.nih.nci.calab.dto.particle;

import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.service.util.StringUtils;

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

	private String keywordsStr;

	private String[] keywords = new String[0];

	private String[] visibilityGroups = new String[0];

	private String gridNode;

	public ParticleBean(String id, String name, String type) {
		super(id, name);
		this.setSampleType(type);
	}

	public ParticleBean(String id, String name, String type, String source) {
		super(id, name);
		this.setSampleType(type);
		this.setSampleSource(source);
	}

	public ParticleBean() {
		super();
	}
	
	public ParticleBean(Nanoparticle particle) {
		this(particle.getId().toString(), particle.getName(), particle
				.getType());
		String source = (particle.getSource() == null) ? "" : particle
				.getSource().getOrganizationName();
		this.setSampleSource(source);

		this.particleClassification = particle.getClassification();
		setParticleFunctions(particle);
		setParticleCharacterizations(particle);
		setParticleKeywords(particle);
	}

	private void setParticleFunctions(Nanoparticle particle) {
		// get a unique list of function
		Collection<Function> functionCol = particle.getFunctionCollection();

		Set<String> functionTypeSet = new HashSet<String>();
		for (Function funcObj : functionCol) {
			functionTypeSet.add(funcObj.getType());
		}
		this.setFunctionTypes(functionTypeSet.toArray(new String[0]));
	}

	private void setParticleCharacterizations(Nanoparticle particle) {
		// get a unique list of characterization
		Collection<Characterization> characterizationCol = particle
				.getCharacterizationCollection();

		Set<String> charcterizationSet = new HashSet<String>();
		for (Characterization charObj : characterizationCol) {
			charcterizationSet.add(charObj.getClassification() + ":"
					+ charObj.getName());
		}
		this.setCharacterizations(charcterizationSet.toArray(new String[0]));
	}

	private void setParticleKeywords(Nanoparticle particle) {
		// get a unique set of keywords
		Collection<Keyword> keywordCol = particle.getKeywordCollection();
		SortedSet<String> keywordSet = new TreeSet<String>();
		for (Keyword keywordObj : keywordCol) {
			keywordSet.add(keywordObj.getName());
		}
		this.setKeywords(keywordSet.toArray(new String[0]));
	}

	public ParticleBean(Nanoparticle particle, String gridNode) {
		this(particle);
		this.gridNode = gridNode;
	}

	public String[] getCharacterizations() {
		return this.characterizations;
	}

	public void setCharacterizations(String[] characterizations) {
		this.characterizations = characterizations;
	}

	public String[] getFunctionTypes() {
		return this.functionTypes;
	}

	public void setFunctionTypes(String[] functionTypes) {
		this.functionTypes = functionTypes;
	}

	public String[] getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getParticleClassification() {
		return this.particleClassification;
	}

	public void setParticleClassification(String particleClassification) {
		this.particleClassification = particleClassification;
	}

	public String[] getVisibilityGroups() {
		return this.visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	public String getGridNode() {
		return this.gridNode;
	}

	public void setGridNode(String gridNode) {
		this.gridNode = gridNode;
	}

	public String getKeywordsStr() {
		this.keywordsStr = StringUtils.join(this.keywords, "\r\n");
		return this.keywordsStr;
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
		if (keywordsStr.length() > 0)
			this.keywords = this.keywordsStr.split("\r\n");
	}
}
