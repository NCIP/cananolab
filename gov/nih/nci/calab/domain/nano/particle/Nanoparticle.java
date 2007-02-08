package gov.nih.nci.calab.domain.nano.particle;

import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Function;

import java.util.Collection;
import java.util.HashSet;

public class Nanoparticle extends Sample {

	private static final long serialVersionUID = 1234567890L;
	
	private String classification; 
	
	private Collection<Keyword> keywordCollection = new HashSet<Keyword>();
	
	private Collection<Function> functionCollection = new HashSet<Function>();
	
	private Collection<Characterization> characterizationCollection = new HashSet<Characterization>();
	
	private Collection<Report> reportCollection = new HashSet<Report>();
	
	private Collection<AssociatedFile> associatedFileCollection = new HashSet<AssociatedFile>();
	
	public Nanoparticle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Collection<Function> getFunctionCollection() {
		return functionCollection;
	}

	public void setFunctionCollection(Collection<Function> functionCollection) {
		this.functionCollection = functionCollection;
	}

	public Collection<Keyword> getKeywordCollection() {
		return keywordCollection;
	}

	public void setKeywordCollection(Collection<Keyword> keywordCollection) {
		this.keywordCollection = keywordCollection;
	}

	public Collection<Characterization> getCharacterizationCollection() {
		return characterizationCollection;
	}

	public void setCharacterizationCollection(
			Collection<Characterization> characterizationCollection) {
		this.characterizationCollection = characterizationCollection;
	}

	public Collection<AssociatedFile> getAssociatedFileCollection() {
		return associatedFileCollection;
	}

	public void setAssociatedFileCollection(
			Collection<AssociatedFile> associatedFileCollection) {
		this.associatedFileCollection = associatedFileCollection;
	}

	public Collection<Report> getReportCollection() {
		return reportCollection;
	}

	public void setReportCollection(Collection<Report> reportCollection) {
		this.reportCollection = reportCollection;
	}
	
	
}
