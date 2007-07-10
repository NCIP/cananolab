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

	private String uri = "";

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
	public FileBean(String id, String uri, String shortFileName,
			Date fileSubmissionDate, String fileSubmitter,
			String fileMaskStatus, String inoutType) {
		this.id = id;
		this.uri = uri;
		this.createdDate = fileSubmissionDate;
		this.fileSubmitter = fileSubmitter;
		this.filename = getFileName(uri);
		this.shortFilename = shortFileName;
		this.fileMaskStatus = (fileMaskStatus.length() == 0 && filename
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : fileMaskStatus;
		this.inoutType = inoutType;
	}

	public FileBean(String id, String uri) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.uri = uri;
		this.filename = getFileName(uri);
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
		this.uri = infile.getUri();
		this.fileSubmitter = infile.getCreatedBy();
		this.filename = getFileName(uri);
		this.shortFilename = infile.getFilename();
		this.fileMaskStatus = (fileMaskStatus.length() == 0 && filename
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : fileMaskStatus;
		this.inoutType = CaNanoLabConstants.INPUT;
		this.createdDate=infile.getCreatedDate();
	}

	public FileBean(OutputFile outfile) {
		this.id = StringUtils.convertToString(outfile.getId());
		this.uri = outfile.getUri();
		this.fileSubmitter = outfile.getCreatedBy();
		this.fileMaskStatus = (fileMaskStatus.length() == 0 && filename
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : fileMaskStatus;
		this.filename = getFileName(uri);
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
		this.filename = getFileName(uri);
	}

	public String getFilename() {
		return filename;
	}

	// public void setFilename(String filename) {
	// this.filename = filename;
	// }
	//	

	private String getFileName(String uri) {
		String[] tokens = uri.split("/");
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
