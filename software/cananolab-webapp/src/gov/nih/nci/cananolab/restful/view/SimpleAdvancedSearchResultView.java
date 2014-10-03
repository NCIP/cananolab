package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.restful.bean.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdvancedSearchResultView {
	
	List<SimpleAdvancedSearchSampleBean> samples = new ArrayList<SimpleAdvancedSearchSampleBean>();
	
	List<LabelValueBean> columnTitles = new ArrayList<LabelValueBean>();

	
	boolean showPOC;
	boolean showNanomaterialEntity;
	boolean showFunctionalizingEntity;
	boolean showFunction;
	
	List<String> errors = new ArrayList<String>();
	
	public void createColumnTitles(List<String> columnNamesFromQueries) {
		columnTitles.clear();
		LabelValueBean title = new LabelValueBean("Sample Name", "Sample");
		columnTitles.add(title);
		
		for (String colName : columnNamesFromQueries) {
			String type = SimpleAdvancedSearchSampleBean.getHtmlFieldType(colName);
			title = new LabelValueBean(colName, type);
			columnTitles.add(title);
		}
	}
	
	public List<SimpleAdvancedSearchSampleBean> getSamples() {
		return samples;
	}
	public void setSamples(List<SimpleAdvancedSearchSampleBean> samples) {
		this.samples = samples;
	}
	
	public List<LabelValueBean> getColumnTitles() {
		return columnTitles;
	}

	public void setColumnTitles(List<LabelValueBean> columnTitles) {
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
