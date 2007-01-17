/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.DerivedDataFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zengje
 * 
 */
public class DerivedBioAssayData implements Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String type;

	private DerivedDataFile file;

	private Collection<Datum> datumCollection = new ArrayList<Datum>();

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Collection<Datum> getDatumCollection() {
		return datumCollection;
	}

	public void setDatumCollection(Collection<Datum> datumCollection) {
		this.datumCollection = datumCollection;
	}

	public DerivedDataFile getFile() {
		return file;
	}

	public void setFile(DerivedDataFile file) {
		this.file = file;
	}

}
