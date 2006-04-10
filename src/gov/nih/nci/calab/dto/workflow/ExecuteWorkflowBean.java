/**
 * This is a Data Transfer Object that sends and recieves data between
 * CreateRunAction and ExecuteWorkflowService objects 
 *
 */
package gov.nih.nci.calab.dto.workflow;
import java.util.List;
/**
 * @author caLAB Team
 *
 */
public class ExecuteWorkflowBean {

	private String inFileName;

	private String outFileName;

	private String assayType;

	private String assayName;

	private String runName;

	private String runDate;

	private String runBy;

	private List<String> aliquotIds;

	private String fileSubmissionDate;

	private String fileSubmitter;

	private String fileMaskStatus;

	public ExecuteWorkflowBean(
			String inFileName,
			String outFileName,
			String assayType,
			String assayName,
			String runName,
			String runDate,
			String runBy,
			List <String> aliquotIds,
			String fileSubmissionDate,
			String fileSubmitter,
			String fileMaskStatus) 
	{
		super();
		this.inFileName=inFileName;
		this.outFileName=outFileName;
		this.assayType=assayType;
		this.assayName=assayName;
		this.runName=runName;
		this.runDate=runDate;
		this.runBy=runBy;
		this.aliquotIds=aliquotIds;
		this.fileSubmissionDate=fileSubmissionDate;
		this.fileSubmitter=fileSubmitter;
		this.fileMaskStatus=fileMaskStatus;
	}

	public String getInFileName()
	{
		return inFileName;
	}
	public void setInFileName(String inFileName)
	{
		this.inFileName=inFileName;
	}
	public String getOutFileName()
	{
		return outFileName;
	}
	public void setOutFileName(String outFileName)
	{
		this.outFileName=outFileName;
	}
	public String getAssayType()
	{
		return assayType;
	}
	public void setAssayType(String assayType)
	{
		this.assayType=assayType;
	}
	public String getAssayName()
	{
		return assayName;
	}
	public void setAssayName(String assayName)
	{
		this.assayName=assayName;	
	}
	public String getRunName()
	{
		return runName;
	}
	public void setRunName(String runName)
	{
		this.runName=runName;
	}
	public String getRunDate()
	{
		return runDate;
	}
	public void setRunDate(String runDate)
	{
		this.runDate=runDate;	
	}
	public String getRunBy()
	{
		return runBy;
	}
	public void setRunBy(String runBy)
	{
		this.runBy=runBy;	
	}
	public List<String> getAliquotIds()
	{
		return aliquotIds;
	}
	public void setAliquotIds(List<String> aliquotIds)	
	{
		this.aliquotIds=aliquotIds;	
	}
	public String getFileSubmissionDate()
	{
		return fileSubmissionDate;
	}
	public void setFileSubmissionDate(String fileSubmissionDate)
	{
		this.fileSubmissionDate=fileSubmissionDate;		
	}
	public String getFileSubmitter()
	{
		return fileSubmitter;
	}
	public void setFileSubmitter(String fileSubmitter)
	{
		this.fileSubmitter=fileSubmitter;
	}
	public String getFileMaskStatus()
	{
		return fileMaskStatus;
	}
	public void setFileMaskStatus(String fileMaskStatus)
	{
		this.fileMaskStatus=fileMaskStatus;		
	}

}
