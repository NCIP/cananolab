package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the data files associated with characterizations to be
 * shown in the view pages.
 * 
 * @author chand
 * 
 */
public class DerivedBioAssayDataBean {
	private String id;

	private String type;

	private CharacterizationFileBean file;
	
	private String fileId;

	private List<DatumBean> datumList = new ArrayList<DatumBean>();
	
	private String numberOfDataPoints;

	public DerivedBioAssayDataBean() {
	}

	public DerivedBioAssayDataBean(String type, CharacterizationFileBean file) {
		super();
		// TODO Auto-generated constructor stub
		this.type = type;
		this.file = file;
	}
	
	public DerivedBioAssayDataBean(DerivedBioAssayData table) {
		super();
		this.id = table.getId().toString();
		this.type = table.getType();
		
		this.file = new CharacterizationFileBean();
//		this.file.setName(table.getFile());
		LabFile doFile = table.getFile();
		if (doFile != null) {
			this.file.setCreatedBy(doFile.getCreatedBy());
			this.file.setCreatedDate(doFile.getCreatedDate());
			this.file.setDescription(doFile.getDescription());
			this.file.setId(doFile.getId().toString());
			this.file.setName(doFile.getFilename());
			this.file.setPath(doFile.getPath());
			this.file.setTitle(doFile.getTitle());
			
			// TODO: visibility group
		}

		/*
		for (CharacterizationTableDataBean tableData : this.getTableDataList()) {
			table.getTableDataCollection().add(tableData.getDomainObj());
		}
		*/
		for (Datum tableData : table.getDatumCollection()) {
			DatumBean ctDataBean = new DatumBean(tableData);
			datumList.add(ctDataBean);
		}
		this.setNumberOfDataPoints(datumList.size() + "");
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

	public List<DatumBean> getDatumList() {
		return datumList;
	}

	public void setDatumList(
			List<DatumBean> datumList) {
		this.datumList = datumList;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public DerivedBioAssayData getDomainObj() {
		DerivedBioAssayData table = new DerivedBioAssayData();
		if (getId() != null && getId().length() > 0) {
			table.setId(new Long(getId()));
		}
		table.setType(type);
		//TODO need to decide whether use fileId and file object
		if (file != null)
//		    table.setFile(file.getPath() + file.getName());
			table.setFile(file.getDomainObject());
		for (DatumBean datum : this.getDatumList()) {
			table.getDatumCollection().add(datum.getDomainObj());
		}
		return table;
	}

	public String getNumberOfDataPoints() {
		return numberOfDataPoints;
	}

	public void setNumberOfDataPoints(String numberOfDataPoints) {
		this.numberOfDataPoints = numberOfDataPoints;
	}

}
