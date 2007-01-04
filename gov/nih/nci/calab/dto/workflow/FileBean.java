/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

import gov.nih.nci.calab.domain.InputFile;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.Date;

/**
 * @author zengje
 * 
 */
public class FileBean {

	private String id = "";

	private String path = "";

	private String filename = "";

	private String fileSubmitter = "";

	private String fileMaskStatus = "";

	private Date createdDate;

	private String shortFilename = "";

	private String inoutType = "";

	private String timePrefix = "";

	/**
	 * 
	 */
	public FileBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	// used in WorkflowResultBean
	public FileBean(String id, String path, String shortFileName,
			Date fileSubmissionDate, String fileSubmitter,
			String fileMaskStatus, String inoutType) {
		this.id = id;
		this.path = path;
		this.createdDate = fileSubmissionDate;
		this.fileSubmitter = fileSubmitter;
		this.filename = getFileName(path);
		this.shortFilename = shortFileName;
		this.fileMaskStatus = (fileMaskStatus.length() == 0 && filename
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : fileMaskStatus;
		this.inoutType = inoutType;
	}

	public FileBean(String id, String path) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.path = path;
		this.filename = getFileName(path);
	}

	public String getTimePrefix() {
		if (filename.length() > 0) {
			String[] toks = filename.split("_");
			timePrefix = toks[0] + "-" + toks[1];
		}
		return timePrefix;
	}

	public FileBean(InputFile infile) {
		this.id = StringUtils.convertToString(infile.getId());
		this.path = infile.getPath();
		this.fileSubmitter = infile.getCreatedBy();
		this.filename = getFileName(path);
		this.shortFilename = infile.getFilename();
		this.fileMaskStatus = (fileMaskStatus.length() == 0 && filename
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : fileMaskStatus;
		this.inoutType = CaNanoLabConstants.INPUT;
		this.createdDate=infile.getCreatedDate();
	}

	public FileBean(OutputFile outfile) {
		this.id = StringUtils.convertToString(outfile.getId());
		this.path = outfile.getPath();
		this.fileSubmitter = outfile.getCreatedBy();
		this.fileMaskStatus = (fileMaskStatus.length() == 0 && filename
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : fileMaskStatus;
		this.filename = getFileName(path);
		this.shortFilename = outfile.getFilename();
		this.inoutType = CaNanoLabConstants.OUTPUT;
		this.createdDate=outfile.getCreatedDate();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		this.filename = getFileName(path);
	}

	public String getFilename() {
		return filename;
	}

	// public void setFilename(String filename) {
	// this.filename = filename;
	// }
	//	

	private String getFileName(String path) {
		String[] tokens = path.split("/");
		return tokens[tokens.length - 1];
	}

	public String getFileMaskStatus() {
		return fileMaskStatus;
	}

	public void setFileMaskStatus(String fileMaskStatus) {
		this.fileMaskStatus = (fileMaskStatus.length() == 0 && getFilename()
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : fileMaskStatus;
	}

	public String getFileSubmitter() {
		return fileSubmitter;
	}

	public void setFileSubmitter(String fileSubmitter) {
		this.fileSubmitter = fileSubmitter;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getShortFilename() {
		return shortFilename;
	}

	public void setShortFilename(String shortFileName) {
		this.shortFilename = shortFileName;
	}

	public String getInoutType() {
		return inoutType;
	}

	public void setInoutType(String inoutType) {
		this.inoutType = inoutType;
	}

}
