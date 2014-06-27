package gov.nih.nci.cananolab.ui.form;

import gov.nih.nci.cananolab.dto.particle.SampleBean;

public class SampleForm {
	String dispatch;
	int page;
	SampleBean sampleBean;
	String sampleId;
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public SampleBean getSampleBean() {
		return sampleBean;
	}
	public void setSampleBean(SampleBean sampleBean) {
		this.sampleBean = sampleBean;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	
	
}
