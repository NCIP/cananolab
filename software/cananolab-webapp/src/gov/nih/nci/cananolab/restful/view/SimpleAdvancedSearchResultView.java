package gov.nih.nci.cananolab.restful.view;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdvancedSearchResultView {
	
	List<SimpleAdvancedSearchSampleBean> samples = new ArrayList<SimpleAdvancedSearchSampleBean>();
	
	List<String> columnTitles = new ArrayList<String>();
	
	
	boolean showPOC;
	boolean showNanomaterialEntity;
	boolean showFunctionalizingEntity;
	boolean showFunction;
	
	List<String> errors = new ArrayList<String>();
	
	
	public List<SimpleAdvancedSearchSampleBean> getSamples() {
		return samples;
	}
	public void setSamples(List<SimpleAdvancedSearchSampleBean> samples) {
		this.samples = samples;
	}
	public List<String> getColumnTitles() {
		return columnTitles;
	}
	public void setColumnTitles(List<String> columnTitles) {
		this.columnTitles = columnTitles;
	}
	public boolean isShowPOC() {
		return showPOC;
	}
	public void setShowPOC(boolean showPOC) {
		this.showPOC = showPOC;
	}
	public boolean isShowNanomaterialEntity() {
		return showNanomaterialEntity;
	}
	public void setShowNanomaterialEntity(boolean showNanomaterialEntity) {
		this.showNanomaterialEntity = showNanomaterialEntity;
	}
	public boolean isShowFunctionalizingEntity() {
		return showFunctionalizingEntity;
	}
	public void setShowFunctionalizingEntity(boolean showFunctionalizingEntity) {
		this.showFunctionalizingEntity = showFunctionalizingEntity;
	}
	public boolean isShowFunction() {
		return showFunction;
	}
	public void setShowFunction(boolean showFunction) {
		this.showFunction = showFunction;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	

}
