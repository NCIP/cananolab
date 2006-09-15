package gov.nih.nci.calab.dto.characterization;

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

	private String fileName;

	private List<CharacterizationTableDataBean> tableDataList = new ArrayList<CharacterizationTableDataBean>();

	public CharacterizationTableBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CharacterizationTableBean(String type, String fileName) {
		super();
		// TODO Auto-generated constructor stub
		this.type = type;
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public void setTableDataList(List<CharacterizationTableDataBean> tableDataList) {
		this.tableDataList = tableDataList;
	}
}
