package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

	private String[] visibilityGroups = new String[0];

	private String gridNode;

	private NanoparticleSample domainParticleSample = new NanoparticleSample();

	private String createdBy;

	private boolean hidden;

	private List<ReportBean> reports = new ArrayList<ReportBean>();

	private SortedSet<String>keywordSet=new TreeSet<String>();
	
	public ParticleBean() {
		domainParticleSample.setSource(new Source());
	}

	public ParticleBean(NanoparticleSample particleSample) {
		this.domainParticleSample = particleSample;
		
		if (particleSample.getKeywordCollection() != null) {
			for (Keyword keyword : particleSample.getKeywordCollection()) {
				keywordSet.add(keyword.getName());
			}
		}
		keywordsStr = StringUtils.join(keywordSet, "\r\n");
		if (particleSample.getReportCollection() != null) {
			for (Report report : particleSample.getReportCollection()) {
				reports.add(new ReportBean(report, false));
			}
		}
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

	public NanoparticleSample getDomainParticleSample() {
		return domainParticleSample;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setupDomainParticleSample() {
		// always update createdBy and createdDate
		domainParticleSample.setCreatedBy(createdBy);
		domainParticleSample.setCreatedDate(new Date());
		if (domainParticleSample.getKeywordCollection() != null) {
			domainParticleSample.getKeywordCollection().clear();
		} else {
			domainParticleSample.setKeywordCollection(new HashSet<Keyword>());
		}
		if (keywordsStr.length() > 0) {
			String[] strs = keywordsStr.split("\r\n");
			for (String str : strs) {
				// change to upper case
				Keyword keyword = new Keyword();
				keyword.setName(str.toUpperCase());
				domainParticleSample.getKeywordCollection().add(keyword);
			}
		}
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public List<ReportBean> getReports() {
		return reports;
	}

	public SortedSet<String> getKeywordSet() {
		return keywordSet;
	}
}
