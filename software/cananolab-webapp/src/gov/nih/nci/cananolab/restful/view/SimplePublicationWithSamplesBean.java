package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.util.StringUtils;

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
	String externalUrl;
	Map<String, String> samples;
	
	
	public String getExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
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
	
	public void transferDataFromPublication(PublicationBean pubBean) {
		
		if (pubBean == null) {
			logger.error("Input publication object is null!!");
			return;
		}
		
		Publication pub = (Publication)pubBean.getDomainFile();
		
		this.setId(String.valueOf(pub.getId()));
		
		this.setTitle(pub.getTitle());
		if (pub.getYear() != null)
			this.setYear(pub.getYear().longValue());
		this.setJournal(pub.getJournalName());
		this.setVolumn(pub.getVolume() + ":" + pub.getStartPage() + "-" + pub.getEndPage());
		this.setDescription(pub.getDescription());
		this.setExternalUrl(pub.getUri());
		this.setAuthors(pubBean.getAuthorsDisplayName());
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
	
//	protected String getAuthorDisplayNames(Publication pub) {
//		// standard PubMed journal citation format
//		// e.g. Freedman SB, Adler M, Seshadri R, Powell EC. Oral ondansetron
//		// for gastroenteritis in a pediatric emergency department. N Engl J
//		// Med. 2006 Apr 20;354(16):1698-705. PubMed PMID: 12140307.
//		List<String> strs = new ArrayList<String>();
//		
//		Collection<Author> authors = pub.getAuthorCollection();
//		
//		strs.add(getAuthorsDisplayName());
//		// remove last . in the title
//		if (pub.getTitle().endsWith(".")) {
//			strs.add(pub.getTitle().substring(0, pub.getTitle().length() - 1));
//		} else {
//			strs.add(pub.getTitle());
//		}
//		strs.add(pub.getJournalName());
//		strs.add(getPublishInfoDisplayName());
//		strs.add(getPubMedDisplayName());
//
//		if (pub.getPubMedId() == null) {
//			strs.add(getDOIDisplayName());
//		}
//		if (pub.getPubMedId() == null
//				&& StringUtils.isEmpty(pub.getDigitalObjectId())) {
//			strs.add(getUriDisplayName());
//		}
//
//		displayName = StringUtils.join(strs, ". ") + ".";
//
//		return displayName;
//	}
	
	protected String getAuthorsDisplayName(Publication pub) {
		Collection<Author> authors = pub.getAuthorCollection();
		List<String> strs = new ArrayList<String>();
		for (Author author : authors) {
			List<String> authorStrs = new ArrayList<String>();
			authorStrs.add(author.getLastName());
			authorStrs.add(author.getInitial());
			strs.add(StringUtils.join(authorStrs, ", "));
		}
		return StringUtils.join(strs, ", ");
	}
}
