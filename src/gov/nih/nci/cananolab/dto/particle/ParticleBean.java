package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
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

	private NanoparticleSample domainParticleSample = new NanoparticleSample();

	private String createdBy;

	private boolean hidden;

	private SortedSet<String> keywordSet = new TreeSet<String>();

	private String location; // e.g. local, caNanoLab-WashU, etc

	private String[] nanoparticleEntityClassNames=new String[0];

	private String[] functionalizingEntityClassNames=new String[0];

	private String[] functionClassNames=new String[0];

	private String[] characterizationClassNames=new String[0];
	private String POCId = "";
	private String POCName = "";
	private String POCOrganizationName = "";

	public ParticleBean() {
		domainParticleSample.setPrimaryPointOfContact(new PointOfContact());
		POCName = "";
		POCId = "";
	}

	public ParticleBean(NanoparticleSample particleSample) {
		this.domainParticleSample = particleSample;

		if (particleSample.getKeywordCollection() != null) {
			for (Keyword keyword : particleSample.getKeywordCollection()) {
				keywordSet.add(keyword.getName());
			}
		}
		keywordsStr = StringUtils.join(keywordSet, "\r\n");
		if (domainParticleSample!=null) {
			POCId = domainParticleSample.getPrimaryPointOfContact().getId().toString();
			String firstName = domainParticleSample.getPrimaryPointOfContact().getFirstName();
			POCName = "";
			if (firstName!=null) {
				POCName = firstName +" ";
			}
			String lastName = domainParticleSample.getPrimaryPointOfContact().getLastName();
			if (lastName!=null) {
				POCName+=lastName;
			}
			if (domainParticleSample.getPrimaryPointOfContact().getOrganization()!=null) {
				POCOrganizationName = domainParticleSample.getPrimaryPointOfContact().getOrganization().getName();
				if (POCOrganizationName!=null) {
					POCName+="("+POCOrganizationName+")";
				}
			}
		}
	}

	public String[] getVisibilityGroups() {
		return this.visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
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

	public SortedSet<String> getKeywordSet() {
		return keywordSet;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDomainParticleSample(NanoparticleSample domainParticleSample) {
		this.domainParticleSample = domainParticleSample;
	}

	public String[] getNanoparticleEntityClassNames() {
		return nanoparticleEntityClassNames;
	}

	public void setNanoparticleEntityClassNames(
			String[] nanoparticleEntityClassNames) {
		this.nanoparticleEntityClassNames = nanoparticleEntityClassNames;
	}

	public String[] getFunctionalizingEntityClassNames() {
		return functionalizingEntityClassNames;
	}

	public void setFunctionalizingEntityClassNames(
			String[] functionalizingEntityClassNames) {
		this.functionalizingEntityClassNames = functionalizingEntityClassNames;
	}

	public String[] getFunctionClassNames() {
		return functionClassNames;
	}

	public void setFunctionClassNames(String[] functionClassNames) {
		this.functionClassNames = functionClassNames;
	}

	public String[] getCharacterizationClassNames() {
		return characterizationClassNames;
	}

	public void setCharacterizationClassNames(String[] characterizationClassNames) {
		this.characterizationClassNames = characterizationClassNames;
	}

	/**
	 * @return the pOCName
	 */
	public String getPOCName() {
		return POCName;
	}

	/**
	 * @param name the pOCName to set
	 */
	public void setPOCName(String name) {
		POCName = name;
	}

	/**
	 * @return the pOCOrganizationName
	 */
	public String getPOCOrganizationName() {
		return POCOrganizationName;
	}

	/**
	 * @return the pOCId
	 */
	public String getPOCId() {
		return POCId;
	}

	/**
	 * @param id the pOCId to set
	 */
	public void setPOCId(String id) {
		POCId = id;
	}
}
