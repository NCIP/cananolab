package gov.nih.nci.calab.dto.search;

/**
 * This class includes all properties of a workflow to be displayed in the
 * search workflow result page.
 * 
 * @author pansu
 * 
 */
public class WorkflowResultBean {
	private String fileName;

	private String assayType;

	private String assayName;

	private String assayRunDate;

	private String aliquotId;

	private String fileSubmissionDate;

	private String fileSubmitter;

	private String fileMaskStatus;

	public WorkflowResultBean(String fileName, String assayType, String assayName,
			String assayRunDate, String aliquotId, String fileSubmissionDate,
			String fileSubmitter, String fileMaskStatus) {
		super();
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.assayType = assayType;
		this.assayName = assayName;
		this.assayRunDate = assayRunDate;
		this.aliquotId = aliquotId;
		this.fileSubmissionDate = fileSubmissionDate;
		this.fileSubmitter = fileSubmitter;
		this.fileMaskStatus = fileMaskStatus;
	}

	public String getAliquotId() {
		return aliquotId;
	}

	public void setAliquotId(String aliquotId) {
		this.aliquotId = aliquotId;
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

}
