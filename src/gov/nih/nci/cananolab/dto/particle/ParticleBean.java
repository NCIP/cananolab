package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents shared properties of nanoparticle samples to be shown
 * in the view pages.
 * 
 * @author pansu
 * 
 */
public class ParticleBean {
	private String keywordsStr;

	private Collection<Keyword> keywords;

	private String[] visibilityGroups = new String[0];

	private String gridNode;

	private NanoparticleSample particleSample;

	public ParticleBean() {
		particleSample = new NanoparticleSample();
		particleSample.setSource(new Source());
		keywords = new HashSet<Keyword>();
	}

	public ParticleBean(NanoparticleSample particleSample) {
		this.particleSample = particleSample;
		keywords = particleSample.getKeywordCollection();
		SortedSet<String> keywordStrs = new TreeSet<String>();
		for (Keyword keyword : keywords) {
			keywordStrs.add(keyword.getName());
		}
		keywordsStr = StringUtils.join(keywordStrs, "\r\n");
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
		return this.keywordsStr;
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
		if (keywordsStr.length() > 0) {
			String[] strs = keywordsStr.split("\r\n");
			for (String str : strs) {
				// change to upper case
				Keyword keyword = new Keyword();
				keyword.setName(str.toUpperCase());
				keywords.add(keyword);				
			}
		}
		particleSample.setKeywordCollection(keywords);
	}

	public Collection<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<Keyword> keywords) {
		this.keywords = keywords;
	}

	public NanoparticleSample getParticleSample() {
		return particleSample;
	}

	public void setParticleSample(NanoparticleSample particleSample) {
		this.particleSample = particleSample;
	}
}
