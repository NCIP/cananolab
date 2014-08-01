package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Class to hold publication data and it's associated samples' basic info (sampleId and sampleName currently)
 * 
 * @author yangs8
 *
 */
public class SimplePublicationWithSamplesBean {
	private static Logger logger = Logger.getLogger(SimplePublicationWithSamplesBean.class);

	String id;
	String type;
	
	String authors;
	
	String title;
	String journal;
	
	String description;
	
	Map<String, String> samples;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
	
	public void transferDataFromPublication(Publication pubBean) {
		
		if (pubBean == null) {
			logger.error("Input publication object is null!!");
			return;
		}
		this.setId(String.valueOf(pubBean.getId()));
		
		this.setTitle(pubBean.getTitle());
		this.setYear(pubBean.getYear().longValue());
		this.setJournal(pubBean.getJournalName());
		this.setVolumn(pubBean.getVolume() + ":" + pubBean.getStartPage() + "-" + pubBean.getEndPage());
		this.setDescription(pubBean.getDescription());
		
		Collection<Author> authors = pubBean.getAuthorCollection();
		if (authors != null) {
			StringBuilder sb = new StringBuilder();
			for (Author author : authors) {
				if (sb.length() > 0) 
					sb.append("; ");
				
				sb.append(author.getLastName()).append(", ").append(author.getFirstName()).append(" ").append(author.getInitial());
			}
			
			this.setAuthors(sb.toString());
		}
	}
	
	public void transferSampleDataFromSampleList(List<Sample> sampleBeans) {
		if (sampleBeans == null) {
			logger.info("There is no associated samples for publication");
			return;
		}
		
		this.samples = new HashMap<String, String>();
		for (Sample sample : sampleBeans) {
			samples.put(String.valueOf(sample.getId()), sample.getName());
		}
	}
}
