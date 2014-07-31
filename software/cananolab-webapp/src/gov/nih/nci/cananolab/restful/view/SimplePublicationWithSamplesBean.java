package gov.nih.nci.cananolab.restful.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimplePublicationWithSamplesBean {

	String id;
	String type;
	
	String authors;
	
	String title;
	String journal;
	
	Map<String, String> samples;
	
	public Map<String, String> getSamples() {
		return samples;
	}
	public void setSamples(Map<String, String> samples) {
		this.samples = samples;
	}
	long year;
	String volumn;
	
	List<String> errors = new ArrayList<String>();
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public long getYear() {
		return year;
	}
	public void setYear(long year) {
		this.year = year;
	}
	public String getVolumn() {
		return volumn;
	}
	public void setVolumn(String volumn) {
		this.volumn = volumn;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	
}
