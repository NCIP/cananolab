package gov.nih.nci.cananolab.ui.form;

import gov.nih.nci.cananolab.dto.common.PublicationBean;

public class PublicationForm {

	String dispatch;
	PublicationBean publicationBean;
	public PublicationBean getPublicationBean() {
		return publicationBean;
	}
	public void setPublicationBean(PublicationBean publicationBean) {
		this.publicationBean = publicationBean;
	}
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
	int page;
	String sampleId;
	String[] otherSamples;
}
