package gov.nih.nci.cananolab.ui.form;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

public class CharacterizationForm {
	
	CharacterizationBean achar;
	String sampleId;
	String[] otherSamples;
	String charId;
	boolean copyData;
	int page;
	String dispatch;
	public CharacterizationBean getAchar() {
		return achar;
	}
	public void setAchar(CharacterizationBean achar) {
		this.achar = achar;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String[] getOtherSamples() {
		return otherSamples;
	}
	public void setOtherSamples(String[] otherSamples) {
		this.otherSamples = otherSamples;
	}
	public String getCharId() {
		return charId;
	}
	public void setCharId(String charId) {
		this.charId = charId;
	}
	public boolean isCopyData() {
		return copyData;
	}
	public void setCopyData(boolean copyData) {
		this.copyData = copyData;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	
}
