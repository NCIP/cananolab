package gov.nih.nci.cananolab.dto.particle.characterization;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a row in the summary view capturing data from a single
 * DerivedBioAssayData
 * 
 * @author pansu
 * 
 */
public class CharacterizationSummaryRowBean {
	private CharacterizationBean charBean;

	private Map<String, String> datumMap = new HashMap<String, String>();

	//private DerivedBioAssayDataBean derivedBioAssayDataBean;
	private DatumBean datumBean;

	public CharacterizationSummaryRowBean() {
	}

	public Map<String, String> getDatumMap() {
		return datumMap;
	}

	public void setDatumMap(Map<String, String> datumMap) {
		this.datumMap = datumMap;
	}

	public CharacterizationBean getCharBean() {
		return charBean;
	}

	public void setCharBean(CharacterizationBean charBean) {
		this.charBean = charBean;
	}

	/**
	 * @return the datumBean
	 */
	public DatumBean getDatumBean() {
		return datumBean;
	}

	/**
	 * @param datumBean the datumBean to set
	 */
	public void setDatumBean(DatumBean datumBean) {
		this.datumBean = datumBean;
	}

	
}
