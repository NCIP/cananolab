package gov.nih.nci.calab.dto.characterization;

import java.util.HashMap;
import java.util.Map;

public class CharacterizationSummaryRowBean {
	private CharacterizationBean charBean;

	private Map<String, String> datumMap = new HashMap<String, String>();

	private DerivedBioAssayDataBean charFile;

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

	public DerivedBioAssayDataBean getCharFile() {
		return charFile;
	}

	public void setCharFile(DerivedBioAssayDataBean charFile) {
		this.charFile = charFile;
	}
}
