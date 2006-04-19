/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;
import java.util.Date;

/**
 * @author zengje
 * 
 */
public class FileBean {

	private String id = "";

	private String path = "";

	private String filename = "";

	private String createDateStr = "";

	private String fileSubmitter = "";

	private String fileMaskStatus = "";

	private Date createdDate;

	/**
	 * 
	 */
	public FileBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	// used in WorkflowResultBean
	public FileBean(String path, String fileSubmissionDate,
			String fileSubmitter, String fileMaskStatus) {
		this.path = path;
		this.createDateStr = fileSubmissionDate;
		this.fileSubmitter = fileSubmitter;
		this.fileMaskStatus = fileMaskStatus;
	}

	public FileBean(String id, String path) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.path = path;
		this.filename = parsePath(path);
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
		this.filename = parsePath(path);
	}

	public String getFilename() {
		return filename;
	}

	// public void setFilename(String filename) {
	// this.filename = filename;
	// }
	//	

	private String parsePath(String path) {
		String rootpath = PropertyReader.getProperty(
				CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File file = new File(rootpath + path);
		return file.getName();
	}

	public String getFileMaskStatus() {
		return fileMaskStatus;
	}

	public void setFileMaskStatus(String fileMaskStatus) {
		this.fileMaskStatus = fileMaskStatus;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String fileSubmissionDate) {
		this.createDateStr = fileSubmissionDate;
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

}
