package gov.nih.nci.calab.dto.common;

/**
 * This class represents a search criteria which includes a type of search data
 * of a certain classifcation with a low value and a high value, e.g.
 * classification=size, type=z-average, lowValue=20 nm, highValue=100 nm.
 * 
 * @author pansu
 * 
 */
public class SearchableBean {
	private String classification="";

	private String type="";

	private String lowValue="";

	private String highValue="";

	public SearchableBean() {
		
	}
	public SearchableBean(String classification, String type, String lowValue,
			String highValue) {
		super();
		// TODO Auto-generated constructor stub
		this.classification = classification;
		this.type = type;
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	public String getHighValue() {
		return highValue;
	}

	public void setHighValue(String highValue) {
		this.highValue = highValue;
	}

	public String getLowValue() {
		return lowValue;
	}

	public void setLowValue(String lowValue) {
		this.lowValue = lowValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
}
