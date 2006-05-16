package gov.nih.nci.calab.dto.search;

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;

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

	public WorkflowResultBean(String filePath, String assayType,
			String assayName, String assayRunId, String assayRunName, String assayRunDate,
			String aliquotName, String aliquotStatus,
			String fileSubmissionDate, String fileSubmitter,
			String fileMaskStatus, String inoutType) {
		super();
		// TODO Auto-generated constructor stub
		this.file = new FileBean("", filePath, fileSubmissionDate, fileSubmitter,
				fileMaskStatus, inoutType);
		this.assay = new AssayBean(assayName, assayType);
		this.run = new RunBean(assayRunId, assayRunName, assayRunDate);
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
}
