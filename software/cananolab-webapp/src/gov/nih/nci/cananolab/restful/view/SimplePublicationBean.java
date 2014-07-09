package gov.nih.nci.cananolab.restful.view;

public class SimplePublicationBean {

	
	private String displayName = "";
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String[] getResearchAreas() {
		return researchAreas;
	}
	public void setResearchAreas(String[] researchAreas) {
		this.researchAreas = researchAreas;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywordsDisplayName() {
		return keywordsDisplayName;
	}
	public void setKeywordsDisplayName(String keywordsDisplayName) {
		this.keywordsDisplayName = keywordsDisplayName;
	}
	public String[] getKeywordsStr() {
		return keywordsStr;
	}
	public void setKeywordsStr(String[] keywordsStr) {
		this.keywordsStr = keywordsStr;
	}
	private String[] researchAreas;
	private String description;
	private String keywordsDisplayName;
	private String[] keywordsStr;
}
