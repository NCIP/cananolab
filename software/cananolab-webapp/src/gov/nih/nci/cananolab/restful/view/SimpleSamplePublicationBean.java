package gov.nih.nci.cananolab.restful.view;

import java.util.List;

public class SimpleSamplePublicationBean {

	long sampleId;
	String reference;
	
	List<String> authors;
	
	String title;
	String journal;
	
	List<String> compositions;
	List<String> characterizations;
	
	long year;
	String volumn;
	
	
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	public long getSampleId() {
		return sampleId;
	}
	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getCompositions() {
		return compositions;
	}
	public void setCompositions(List<String> compositions) {
		this.compositions = compositions;
	}
	public List<String> getCharacterizations() {
		return characterizations;
	}
	public void setCharacterizations(List<String> characterizations) {
		this.characterizations = characterizations;
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
	
	
}
