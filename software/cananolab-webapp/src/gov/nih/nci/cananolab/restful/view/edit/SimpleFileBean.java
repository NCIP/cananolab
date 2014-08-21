package gov.nih.nci.cananolab.restful.view.edit;

public class SimpleFileBean {

	boolean uriExternal =false;
	String uri = "";
	String type = "";
	String title = "";
	String description = "";
	String keywordsStr = "";
	Long id;
	
	public boolean getUriExternal() {
		return uriExternal;
	}
	public void setUriExternal(boolean uriExternal) {
		this.uriExternal = uriExternal;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywordsStr() {
		return keywordsStr;
	}
	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}

