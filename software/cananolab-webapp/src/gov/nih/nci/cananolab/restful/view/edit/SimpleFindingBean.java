package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.Row;

import java.util.ArrayList;
import java.util.List;

public class SimpleFindingBean {
	
	long findingId;
	
	private int numberOfColumns;
	private int numberOfRows;
	
	private List<Row> rows = new ArrayList<Row>();
	private List<ColumnHeader> columnHeaders = new ArrayList<ColumnHeader>();
	
	private List<SimpleFileBean> files = new ArrayList<SimpleFileBean>();

	private int theFileIndex;
	
	//private Finding domain = new Finding();
	//private FileBean theFile = new FileBean();
	
	
	
	List<String> errors = new ArrayList<String>();
	
	public void transferFromFindingBean(FindingBean findingBean) {
		columnHeaders =  findingBean.getColumnHeaders();
		//findingBean.getDomain();
		//findingBean.getFiles();
		numberOfColumns = findingBean.getNumberOfColumns();
		numberOfRows = findingBean.getNumberOfRows();
		
		
		//List<Row> beanRows = findingBean.getRows();
//		if (beanRows != null) {
//			for (Row beanRow : beanRows) {
//				beanRow.
//			}
//		}
		
		
		Finding domain = findingBean.getDomain();
		if (domain == null)
			return;
		
		if (domain.getId() != null)
			findingId = domain.getId();
		
	}
	public void transferToFindingBean(FindingBean findingBean) {
		
		findingBean.setNumberOfColumns(this.numberOfColumns);
		findingBean.setNumberOfRows(this.numberOfRows);
		
	}
	
	

	public long getFindingId() {
		return findingId;
	}
	public void setFindingId(long findingId) {
		this.findingId = findingId;
	}
	public int getNumberOfColumns() {
		return numberOfColumns;
	}



	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}



	public int getNumberOfRows() {
		return numberOfRows;
	}



	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}



	public List<Row> getRows() {
		return rows;
	}



	public void setRows(List<Row> rows) {
		this.rows = rows;
	}



	public List<ColumnHeader> getColumnHeaders() {
		return columnHeaders;
	}



	public void setColumnHeaders(List<ColumnHeader> columnHeaders) {
		this.columnHeaders = columnHeaders;
	}



	public List<SimpleFileBean> getFiles() {
		return files;
	}



	public void setFiles(List<SimpleFileBean> files) {
		this.files = files;
	}



	public int getTheFileIndex() {
		return theFileIndex;
	}



	public void setTheFileIndex(int theFileIndex) {
		this.theFileIndex = theFileIndex;
	}



	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	
	
}
