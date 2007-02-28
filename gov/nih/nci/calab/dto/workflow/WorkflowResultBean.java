package gov.nih.nci.calab.dto.workflow;

import java.util.Date;

import gov.nih.nci.calab.dto.inventory.AliquotBean;

/**
 * This class includes all properties of a workflow to be displayed in the
 * search workflow result page.
 * 
 * @author pansu
 * 
 */
public class WorkflowResultBean {
	private FileBean file;

	private AssayBean assay;

	private AliquotBean aliquot;

	private RunBean run;	
	
	public WorkflowResultBean(String fileId, String filePath,
			String shortFileName, String assayType, String assayName,
			String assayRunId, String assayRunName, Date assayRunDate,
			String aliquotName, String aliquotStatus, Date fileSubmissionDate,
			String fileSubmitter, String fileMaskStatus, String inoutType) {
		super();
		this.file = new FileBean(fileId, filePath, shortFileName,
				fileSubmissionDate, fileSubmitter, fileMaskStatus, inoutType);
		this.assay = new AssayBean(assayName, assayType);
		this.run = new RunBean(assayRunId, assayRunName, assayRunDate);
		this.run.setAssayBean(this.assay);
		this.aliquot = new AliquotBean(aliquotName, aliquotStatus);
	}

	public AliquotBean getAliquot() {
		return aliquot;
	}

	public void setAliquot(AliquotBean aliquot) {
		this.aliquot = aliquot;
	}

	public AssayBean getAssay() {
		return assay;
	}

	public void setAssay(AssayBean assay) {
		this.assay = assay;
	}

	public FileBean getFile() {
		return file;
	}

	public void setFile(FileBean file) {
		this.file = file;
	}

	public RunBean getRun() {
		return run;
	}

	public void setRun(RunBean run) {
		this.run = run;
	}
	
	public WorkflowResultBean getSelf() {
		return this;
	}
}
