package gov.nih.nci.cananolab.restful.view;

import java.util.ArrayList;
import java.util.List;

public class SimpleTableBean {
	
	List<String> headers = new ArrayList<String>();
	List<SimpleRowBean> rows = new ArrayList<SimpleRowBean>();
	
	public void addHeader(String name) {
		headers.add(name);
	}
	
	public void addRow(List<String> rowVals) {
		SimpleRowBean row = new SimpleRowBean();
		row.setCells(rowVals);
		rows.add(row);
	}
	
	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	
	public List<SimpleRowBean> getRows() {
		return rows;
	}

	public void setRows(List<SimpleRowBean> rows) {
		this.rows = rows;
	}




	private class SimpleRowBean {
		List<String> cells;

		public List<String> getCells() {
			return cells;
		}

		public void setCells(List<String> cells) {
			this.cells = cells;
		}
	}
}
