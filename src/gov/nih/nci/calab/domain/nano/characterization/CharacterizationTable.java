/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zengje
 *
 */
public class CharacterizationTable {

	private Long id;
	private String type;
	private String file;
	
	private Collection<TableData> tableDataCollection = new ArrayList<TableData>();
	/**
	 * 
	 */
	public CharacterizationTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Collection<TableData> getTableDataCollection() {
		return tableDataCollection;
	}

	public void setTableDataCollection(Collection<TableData> tableDataCollection) {
		this.tableDataCollection = tableDataCollection;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
