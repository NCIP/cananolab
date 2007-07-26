package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents attributes of a characerization file to be viewed in a
 * view page.
 * 
 * @author pansu
 * 
 */
public class DerivedBioAssayDataBean extends LabFileBean {

	private String[] keywords = new String[0];

	private String keywordsStr;

	private String particleName;

	private String characterizationName;

	private String[] categories=new String[0];

	private List<DatumBean> datumList = new ArrayList<DatumBean>();

	private String numberOfDataPoints;

	public String getCharacterizationName() {
		return characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}

	public DerivedBioAssayDataBean() {
	}

	public DerivedBioAssayDataBean(DerivedBioAssayData charFile) {
		super(charFile);
		List<String> allkeywords = new ArrayList<String>();
		for (Keyword keyword : ((DerivedBioAssayData) charFile)
				.getKeywordCollection()) {
			allkeywords.add(keyword.getName());
		}
		keywords = allkeywords.toArray(new String[0]);
		for (Datum tableData : charFile.getDatumCollection()) {
			if (tableData != null) {
				DatumBean ctDataBean = new DatumBean(tableData);
				datumList.add(ctDataBean);
			}
		}
		numberOfDataPoints = datumList.size() + "";
		this.categories=charFile.getCategories();
	}

	public String getNumberOfDataPoints() {
		return numberOfDataPoints;
	}

	public void setNumberOfDataPoints(String numberOfDataPoints) {
		this.numberOfDataPoints = numberOfDataPoints;
	}

	public DerivedBioAssayDataBean(DerivedBioAssayData charFile,
			String gridNodeHost) {
		this(charFile);
		setGridNode(gridNodeHost);
	}

	public DerivedBioAssayData getDomainObject() {
		DerivedBioAssayData charFile = new DerivedBioAssayData();
		if (getId() != null && getId().length() > 0) {
			charFile.setId(new Long(getId()));
		}
		charFile.setCreatedBy(getCreatedBy());
		charFile.setCreatedDate(getCreatedDate());
		charFile.setDescription(getDescription());
		charFile.setComments(getComments());
		charFile.setFilename(getName());
		charFile.setUri(getUri());
		charFile.setTitle(getTitle());
		charFile.setVersion(getVersion());
		charFile.setType(getType());
		charFile.setCategories(categories);
		charFile.setContent(getFileContent());
		for (String keywordValue : keywords) {
			Keyword keyword = new Keyword();
			keyword.setName(keywordValue);
			charFile.getKeywordCollection().add(keyword);
		}
		for (DatumBean datum : datumList) {
			charFile.getDatumCollection().add(datum.getDomainObj());
		}
		charFile.setCategories(categories);
		return charFile;
	}

	public String getKeywordsStr() {
		keywordsStr = StringUtils.join(keywords, "\r\n");
		return keywordsStr;
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
		if (keywordsStr.length() > 0)
			this.keywords = keywordsStr.split("\r\n");
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	/**
	 * Create a new instance that has the same metadata except id and path
	 * 
	 * @return
	 */
	public DerivedBioAssayDataBean copy(boolean copyData) {
		DerivedBioAssayDataBean newCharFileBean = new DerivedBioAssayDataBean();
		// do not copy file id
		newCharFileBean.setKeywords(keywords);
		newCharFileBean.setCategories(categories);
		newCharFileBean.setDescription(getDescription());
		newCharFileBean.setVisibilityGroups(getVisibilityGroups());
		newCharFileBean.setTitle(getTitle());
		newCharFileBean.setVersion(getVersion());
		newCharFileBean.setName(getName());
		newCharFileBean.setCreatedBy(getCreatedBy());
		newCharFileBean.setCreatedDate(getCreatedDate());
		newCharFileBean.setFileContent(getFileContent());
		//copy uri but will be modified		
		newCharFileBean.setUri(getUri());
		newCharFileBean.setCharacterizationName(characterizationName);
		newCharFileBean.setType(getType());
		if (copyData) {
			List<DatumBean> newDatumList = new ArrayList<DatumBean>();
			for (DatumBean datum : datumList) {
				DatumBean newDatum = new DatumBean();
				newDatum.setCategory(datum.getCategory());
				newDatum.setName(datum.getName());
				newDatum.setValue(datum.getValue());
				newDatum.setStatisticsType(datum.getStatisticsType());
				newDatum.setCategory(datum.getCategory());
				newDatum.setUnit(datum.getUnit());
				newDatumList.add(newDatum);
			}
			newCharFileBean.setDatumList(newDatumList);
		}
		return newCharFileBean;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public List<DatumBean> getDatumList() {
		return datumList;
	}

	public void setDatumList(List<DatumBean> datumList) {
		this.datumList = datumList;
	}
}
