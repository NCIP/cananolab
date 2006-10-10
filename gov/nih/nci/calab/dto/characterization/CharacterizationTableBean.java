package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.nano.characterization.CharacterizationTable;

import gov.nih.nci.calab.domain.nano.characterization.TableData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the data files associated with characterizations to be
 * shown in the view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationTableBean {
	private String id;

	private String type;

	private CharacterizationFileBean file;
	
	private String fileId;

	private List<CharacterizationTableDataBean> tableDataList = new ArrayList<CharacterizationTableDataBean>();

	public CharacterizationTableBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CharacterizationTableBean(String type, CharacterizationFileBean file) {
		super();
		// TODO Auto-generated constructor stub
		this.type = type;
		this.file = file;
	}
	
	public CharacterizationTableBean(CharacterizationTable table) {
		super();
		this.id = table.getId().toString();
		this.type = table.getType();
		
		this.file = new CharacterizationFileBean();
		this.file.setName(table.getFile());

		/*
		for (CharacterizationTableDataBean tableData : this.getTableDataList()) {
			table.getTableDataCollection().add(tableData.getDomainObj());
		}
		*/
		for (TableData tableData : table.getTableDataCollection()) {
			CharacterizationTableDataBean ctDataBean = new CharacterizationTableDataBean(tableData);
			tableDataList.add(ctDataBean);
		}
	}

	public CharacterizationFileBean getFile() {
		return file;
	}

	public void setFile(CharacterizationFileBean file) {
		this.file = file;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<CharacterizationTableDataBean> getTableDataList() {
		return tableDataList;
	}

	public void setTableDataList(
			List<CharacterizationTableDataBean> tableDataList) {
		this.tableDataList = tableDataList;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public CharacterizationTable getDomainObj() {
		CharacterizationTable table = new CharacterizationTable();
		if (getId() != null && getId().length() > 0) {
			table.setId(new Long(getId()));
		}
		table.setType(type);
		//TODO need to decide whether use fileId and file object
		if (file != null)
		    table.setFile(file.getPath() + file.getName());
		for (CharacterizationTableDataBean tableData : this.getTableDataList()) {
			table.getTableDataCollection().add(tableData.getDomainObj());
		}
		return table;
	}

}
