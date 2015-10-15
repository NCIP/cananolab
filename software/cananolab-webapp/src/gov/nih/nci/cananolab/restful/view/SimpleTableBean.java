package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.restful.bean.LabelValueBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleTableBean {
	
	//List<String> headers = new ArrayList<String>();
	List<LabelValueBean> columnTitles = new ArrayList<LabelValueBean>();
	List<SimpleRowBean> rows = new ArrayList<SimpleRowBean>();
	
	List<SimpleAdvancedResultRow> dataRows = new ArrayList<SimpleAdvancedResultRow>();
	
	List<List<Map<String, Object>>> diffRows = new ArrayList<List<Map<String, Object>>>();
	
//	public void addHeader(String name) {
//		headers.add(name);
//	}
	
	public void addRow(List<Object> rowVals) {
		SimpleRowBean row = new SimpleRowBean();
		row.setCells(rowVals);
		rows.add(row);
	}
	
	public List<LabelValueBean> getColumnTitles() {
		return columnTitles;
	}



	public void setColumnTitles(List<LabelValueBean> columnTitles) {
		this.columnTitles = columnTitles;
	}



	public List<SimpleRowBean> getRows() {
		return rows;
	}

	public void setRows(List<SimpleRowBean> rows) {
		this.rows = rows;
	}

	public List<List<Map<String, Object>>> getDiffRows() {
		return diffRows;
	}

	public void setDiffRows(List<List<Map<String, Object>>> diffRows) {
		this.diffRows = diffRows;
	}

	public List<SimpleAdvancedResultRow> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<SimpleAdvancedResultRow> dataRows) {
		this.dataRows = dataRows;
	}

	
}
