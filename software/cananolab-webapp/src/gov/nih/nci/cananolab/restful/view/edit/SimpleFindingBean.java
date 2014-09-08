package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.Row;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleFindingBean {
	
	long findingId;
	
	private int numberOfColumns;
	private int numberOfRows;
	
	private List<SimpleRowBean> rows = new ArrayList<SimpleRowBean>();
	
	
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
		
		
		transferRowsFromFindingBean(findingBean);
		
		Finding domain = findingBean.getDomain();
		if (domain == null)
			return;
		
		if (domain.getId() != null)
			findingId = domain.getId();
		
		transferFilesFromFindingBean(findingBean.getFiles());
	}
	
	public void transferToFindingBean(FindingBean findingBean, UserBean user) 
	throws Exception {
		findingBean.setColumnHeaders(columnHeaders);
		findingBean.setNumberOfColumns(numberOfColumns);
		findingBean.setNumberOfRows(numberOfRows);
		findingBean.setupColumnOrder();
		
		//TODO: set files
		List<FileBean> fileBeans = findingBean.getFiles();
		if (fileBeans == null) return;
		
	}
	
	protected void transferFilesFromFindingBean(List<FileBean> files) {
		if (files == null) return;
		
		for (FileBean file : files) {
			SimpleFileBean simpleFile = new SimpleFileBean();
			simpleFile.setDescription(file.getDescription());
			File domainFile = file.getDomainFile();
			if (domainFile != null) {
				if (domainFile.getId() != null)
					simpleFile.setId(domainFile.getId());
				simpleFile.setTitle(domainFile.getTitle());
				simpleFile.setType(domainFile.getType());
				simpleFile.setUri(domainFile.getUri());
				simpleFile.setUriExternal(domainFile.getUriExternal());
				
			}
			
			this.files.add(simpleFile);
		}
	}
	
	public void transferTableNumbersToFindingBean(FindingBean findingBean) {
		if (findingBean == null) return;
		
		findingBean.setNumberOfColumns(this.numberOfColumns);
		findingBean.setNumberOfRows(this.numberOfRows);
	}
	
	public void transferColumnOrderToFindingBean(FindingBean findingBean) {
		if (findingBean == null) return;
		
		findingBean.setColumnHeaders(this.columnHeaders);
	}
	
	public void transferRowsFromFindingBean(FindingBean findingBean) {
		if (findingBean == null) return;
		
		List<Row> beanRows = findingBean.getRows();
		this.rows.clear();
	
		if (beanRows != null) {
			for (Row beanRow : beanRows) {
				SimpleRowBean aRow = new SimpleRowBean();
				aRow.transferFromRow(beanRow);
				
				this.rows.add(aRow);
			}
		}
	}
	
	protected void transferRowsToFindingBean(FindingBean findingBean) {
		if (this.rows == null) return;
		if (findingBean == null) return;
		
		findingBean.getRows().clear();
		for (SimpleRowBean simpleRow : this.rows) {
			Row rowBean = new Row();
			simpleRow.transferToRow(rowBean);
			findingBean.getRows().add(rowBean);
		}
	}
	
	public void setDefaultColumnNameForNullHeaders() {
		int headerIdx = 1;
		for (ColumnHeader header : this.columnHeaders) {
			if (header.getColumnName() == null || header.getColumnName().length() == 0)
				header.setColumnName("Column "  + headerIdx);
			
			headerIdx++;
		}
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

	public List<SimpleRowBean> getRows() {
		return rows;
	}
	public void setRows(List<SimpleRowBean> rows) {
		this.rows = rows;
	}
	
	
	public int getNumberOfRows() {
		return numberOfRows;
	}


	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
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
