package gov.nih.nci.calab.dto.search;

import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class includes all properties of a workflow to be displayed in the
 * search workflow result page.
 * 
 * @author pansu
 * 
 */
public class WorkflowResultBean {
	private String fileName="";

	private String assayType="";

	private String assayName="";

	private String assayRunName="";
	
	private String assayRunDate="";

	private String aliquotName="";

	private String fileSubmissionDate="";

	private String fileSubmitter="";

	private String fileMaskStatus="";

	public WorkflowResultBean(String fileName, String assayType, String assayName,
			String assayRunName, String assayRunDate, String aliquotName, String fileSubmissionDate,
			String fileSubmitter, String fileMaskStatus) {
		super();
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.assayType = assayType;
		this.assayName = assayName;
		this.assayRunName=assayRunName;
		this.assayRunDate = assayRunDate;
		this.aliquotName = aliquotName;
		this.fileSubmissionDate = fileSubmissionDate;
		this.fileSubmitter = fileSubmitter;
		this.fileMaskStatus = fileMaskStatus;
	}

	public String getAliquotName() {
		return aliquotName;
	}

	public void setAliquotName(String aliquotName) {
		this.aliquotName = aliquotName;
	}

	public String getAssayName() {
		return assayName;
	}

	public void setAssayName(String assayName) {
		this.assayName = assayName;
	}

	public String getAssayRunDate() {
		return assayRunDate;
	}

	public void setAssayRunDate(String assayRunDate) {
		this.assayRunDate = assayRunDate;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getFileMaskStatus() {
		return fileMaskStatus;
	}

	public void setFileMaskStatus(String fileMaskStatus) {
		this.fileMaskStatus = fileMaskStatus;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSubmissionDate() {
		return fileSubmissionDate;
	}

	public void setFileSubmissionDate(String fileSubmissionDate) {
		this.fileSubmissionDate = fileSubmissionDate;
	}

	public String getFileSubmitter() {
		return fileSubmitter;
	}

	public void setFileSubmitter(String fileSubmitter) {
		this.fileSubmitter = fileSubmitter;
	}

	public int hashCode() {
		return toString().hashCode();
	}
	
	public String toString() {
		String[] thisAttrs=new String[] {fileName, assayType, assayName,
				assayRunDate, aliquotName, fileSubmissionDate,
				fileSubmitter, fileMaskStatus};
		return StringUtils.join(thisAttrs,":");		
	}
	
	public int compareTo(Object b) {
		if (b instanceof WorkflowResultBean) {
			WorkflowResultBean bWorkflow=(WorkflowResultBean)b;			
			return toString().compareTo(bWorkflow.toString());
		}
		else {
			return -1;
		}
	}
	
	public boolean equals(Object b) {
		if (this.compareTo(b)==0) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getAssayRunName() {
		return assayRunName;
	}

	public void setAssayRunName(String assayRunName) {
		this.assayRunName = assayRunName;
	}
}
