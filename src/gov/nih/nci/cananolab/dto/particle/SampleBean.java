package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents shared properties of samples to be shown
 * in the view pages.
 *
 * @author pansu
 *
 */
public class SampleBean {
	private String keywordsStr;

	private String[] visibilityGroups = new String[0];

	private Sample domain = new Sample();

	private String createdBy;

	private SortedSet<String> keywordSet = new TreeSet<String>();

	private String location; // e.g. local, caNanoLab-WashU, etc

	private String[] nanomaterialEntityClassNames = new String[0];

	private String[] functionalizingEntityClassNames = new String[0];

	private String[] functionClassNames = new String[0];

	private String[] characterizationClassNames = new String[0];

	private PointOfContactBean pocBean;

	public SampleBean() {
		pocBean = new PointOfContactBean();
	}

	public SampleBean(Sample sample) {
		this.domain = sample;

		if (sample.getKeywordCollection() != null) {
			for (Keyword keyword : sample.getKeywordCollection()) {
				keywordSet.add(keyword.getName());
			}
		}
		keywordsStr = StringUtils.join(keywordSet, "\r\n");
		if (domain != null) {
			PointOfContact primaryPOC = domain
					.getPrimaryPointOfContact();
			pocBean = new PointOfContactBean(primaryPOC);
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

	public Sample getDomain() {
		return domain;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setupDomain() {
		// always update createdBy and createdDate
		domain.setCreatedBy(createdBy);
		domain.setCreatedDate(new Date());
		if (domain.getKeywordCollection() != null) {
			domain.getKeywordCollection().clear();
		} else {
			domain.setKeywordCollection(new HashSet<Keyword>());
		}
		if (keywordsStr.length() > 0) {
			String[] strs = keywordsStr.split("\r\n");
			for (String str : strs) {
				// change to upper case
				Keyword keyword = new Keyword();
				keyword.setName(str.toUpperCase());
				domain.getKeywordCollection().add(keyword);
			}
		}
		if (pocBean != null) {
			domain.setPrimaryPointOfContact(pocBean.getDomain());
		}
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
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

	public void setDomain(Sample domainSample) {
		this.domain = domainSample;
	}

	public String[] getNanomaterialEntityClassNames() {
		return nanomaterialEntityClassNames;
	}

	public void setNanomaterialEntityClassNames(
			String[] nanomaterialEntityClassNames) {
		this.nanomaterialEntityClassNames = nanomaterialEntityClassNames;
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

	public void setCharacterizationClassNames(
			String[] characterizationClassNames) {
		this.characterizationClassNames = characterizationClassNames;
	}

	public PointOfContactBean getPocBean() {
		return pocBean;
	}

	public void setPocBean(PointOfContactBean pocBean) {
		this.pocBean = pocBean;
		this.domain.setPrimaryPointOfContact(pocBean.getDomain());
	}
}
