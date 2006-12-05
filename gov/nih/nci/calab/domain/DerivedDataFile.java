/**
 * 
 */
package gov.nih.nci.calab.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author zengje
 *
 */
public class DerivedDataFile extends LabFile {
	
	private Collection<Keyword> keywordCollection = new HashSet<Keyword>();
	/**
	 * 
	 */
	public DerivedDataFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Collection<Keyword> getKeywordCollection() {
		return keywordCollection;
	}
	public void setKeywordCollection(Collection<Keyword> keywordCollection) {
		this.keywordCollection = keywordCollection;
	}

}
