package gov.nih.nci.calab.dto.workflow;

import java.util.Date;

public class FileDownloadInfo
{
    private String fileName;
    private Date uploadDate;
    private String action;
    
    public String getAction()
    {
        return action;
    }
    public void setAction(String action)
    {
        this.action = action;
    }
    public String getFileName()
    {
        return fileName;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    public Date getUploadDate()
    {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate)
    {
        this.uploadDate = uploadDate;
    }
    

}
