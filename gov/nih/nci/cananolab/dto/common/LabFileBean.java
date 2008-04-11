package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.LabFile;

public class LabFileBean {
	private Integer fileNumber;
	private LabFile labFile;
	
	public LabFileBean(LabFile labFile) {
		this.labFile=labFile;
	}

	public Integer getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(Integer fileNumber) {
		this.fileNumber = fileNumber;
	}

	public LabFile getLabFile() {
		return labFile;
	}

	public void setLabFile(LabFile labFile) {
		this.labFile = labFile;
	}
}
