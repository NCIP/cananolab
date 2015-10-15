package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.restful.bean.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class SimpleAdvancedSearchResultView {
	
	@JsonIgnore
	List<SimpleAdvancedSearchSampleBean> samples = new ArrayList<SimpleAdvancedSearchSampleBean>();
	
	@JsonIgnore
	List<LabelValueBean> columnTitles = new ArrayList<LabelValueBean>();
	
	SimpleTableBean resultTable = new SimpleTableBean();

//	
//	boolean showPOC;
//	boolean showNanomaterialEntity;
//	boolean showFunctionalizingEntity;
//	boolean showFunction;
//	
	List<String> errors = new ArrayList<String>();
	
	public void transformToTableView() {
		this.resultTable.getColumnTitles().clear();
		this.resultTable.getColumnTitles().addAll(this.columnTitles);
		
//		int idx = 0;
//		for (LabelValueBean lableval : this.columnTitles) {
//			String colName = lableval.getLabel();
//			this.resultTable.getHeaders().add(colName);
//		}
		
		for (SimpleAdvancedSearchSampleBean sample : this.samples) {
			//List<Map<String, Object>> rowCells = new ArrayList<Map<String, Object>>();
			//List<Map<String, Object>> rowCels = sample.getRowCells();
			//this.resultTable.getDiffRows().add(rowCels);
			
			this.resultTable.getDataRows().add(sample.getDataRow());
		}
		
	}
	
	public void createColumnTitles(List<String> columnNamesFromQueries) {
		columnTitles.clear();
		LabelValueBean title = new LabelValueBean("Sample Name", "sampleName");
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

//	public boolean isShowPOC() {
//		return showPOC;
//	}
//	public void setShowPOC(boolean showPOC) {
//		this.showPOC = showPOC;
//	}
//	public boolean isShowNanomaterialEntity() {
//		return showNanomaterialEntity;
//	}
//	public void setShowNanomaterialEntity(boolean showNanomaterialEntity) {
//		this.showNanomaterialEntity = showNanomaterialEntity;
//	}
//	public boolean isShowFunctionalizingEntity() {
//		return showFunctionalizingEntity;
//	}
//	public void setShowFunctionalizingEntity(boolean showFunctionalizingEntity) {
//		this.showFunctionalizingEntity = showFunctionalizingEntity;
//	}
//	public boolean isShowFunction() {
//		return showFunction;
//	}
//	public void setShowFunction(boolean showFunction) {
//		this.showFunction = showFunction;
//	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public SimpleTableBean getResultTable() {
		return resultTable;
	}

	public void setResultTable(SimpleTableBean resultTable) {
		this.resultTable = resultTable;
	}
	
	

}
