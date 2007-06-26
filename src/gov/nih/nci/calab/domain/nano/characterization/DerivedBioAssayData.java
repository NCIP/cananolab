/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.LabFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author zengje
 * 
 */
public class DerivedBioAssayData extends LabFile implements Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String[] categories;

	private Collection<Datum> datumCollection = new ArrayList<Datum>();

	private Collection<Keyword> keywordCollection = new HashSet<Keyword>();

	/**
	 * 
	 */
	public DerivedBioAssayData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories=categories;
	}

	public Collection<Datum> getDatumCollection() {
		return datumCollection;
	}

	public void setDatumCollection(Collection<Datum> datumCollection) {
		this.datumCollection = datumCollection;
	}

	public Collection<Keyword> getKeywordCollection() {
		return keywordCollection;
	}

	public void setKeywordCollection(Collection<Keyword> keywordCollection) {
		this.keywordCollection = keywordCollection;
	}
}
