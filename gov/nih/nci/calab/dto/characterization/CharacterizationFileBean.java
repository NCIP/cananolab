package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.DerivedDataFile;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
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
public class CharacterizationFileBean extends LabFileBean {

	private String[] keywords = new String[0];

	private String keywordsStr;

	private String particleName;
	
	private String characterizationName;
	
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

	public CharacterizationFileBean() {
	}

	public CharacterizationFileBean(DerivedDataFile charFile) {
		super(charFile, CaNanoLabConstants.CHARACTERIZATION_FILE);
		List<String> allkeywords = new ArrayList<String>();
		for (Keyword keyword : ((DerivedDataFile) charFile)
				.getKeywordCollection()) {
			allkeywords.add(keyword.getName());
		}
		keywords = allkeywords.toArray(new String[0]);
	}

	public CharacterizationFileBean(DerivedDataFile charFile, 
			String gridNodeHost) {
		this(charFile);
		setGridNode(gridNodeHost);
	}
	
	public DerivedDataFile getDomainObject() {
		DerivedDataFile dataFile = new DerivedDataFile();
		if (getId() != null && getId().length() > 0) {
			dataFile.setId(new Long(getId()));
		}
		dataFile.setCreatedBy(getCreatedBy());
		dataFile.setCreatedDate(getCreatedDate());
		dataFile.setDescription(getDescription());
		dataFile.setComments(getComments());
		dataFile.setFilename(getName());
		dataFile.setPath(getPath());
		dataFile.setTitle(getTitle());
		dataFile.setVersion(getVersion());
		for (String keywordValue : keywords) {
			Keyword keyword = new Keyword();
			keyword.setName(keywordValue);
			dataFile.getKeywordCollection().add(keyword);
		}
		return dataFile;
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
	 * @return
	 */
	public CharacterizationFileBean copy() {
		CharacterizationFileBean newCharFileBean=new CharacterizationFileBean();
		//do not copy file id and path
		newCharFileBean.setKeywords(keywords);
		newCharFileBean.setDescription(getDescription());
		newCharFileBean.setVisibilityGroups(getVisibilityGroups());
		newCharFileBean.setTitle(getTitle());
		newCharFileBean.setVersion(getVersion());
		newCharFileBean.setName(getName());
		newCharFileBean.setCreatedBy(getCreatedBy());
		newCharFileBean.setCreatedDate(getCreatedDate());
		newCharFileBean.setUploadedFile(getUploadedFile());
		newCharFileBean.setCharacterizationName(characterizationName);
		
		return newCharFileBean;
	}
}
