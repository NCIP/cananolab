package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.FavoriteBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleFavoriteBean {

	List<FavoriteBean> samples;
	List<FavoriteBean> protocols;
	List<FavoriteBean> publications;
	
	List<String> errors = new ArrayList<String>();

	public List<FavoriteBean> getSamples() {
		return samples;
	}

	public void setSamples(List<FavoriteBean> samples) {
		this.samples = samples;
	}

	public List<FavoriteBean> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<FavoriteBean> protocols) {
		this.protocols = protocols;
	}

	public List<FavoriteBean> getPublications() {
		return publications;
	}

	public void setPublications(List<FavoriteBean> publications) {
		this.publications = publications;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
}
