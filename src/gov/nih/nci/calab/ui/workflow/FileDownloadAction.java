package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.FileDownloadInfo;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.ActionUtil;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorActionForm;

/**
 * This class handle workflow upload process.
 * 
 * @author zhoujim
 *
 */

public class FileDownloadAction extends AbstractDispatchAction
{
    private static org.apache.log4j.Logger logger_ =
        org.apache.log4j.Logger.getLogger(FileDownloadAction.class);
    
     
    /**
     * This method is setting up the parameters for the workflow input upload files
     * or output upload files.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return mapping forward
     */
    public ActionForward setup(ActionMapping mapping, 
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        
        ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
        String runId = request.getParameter("runId");
        RunBean runBean = workflowService.getAssayInfoByRun((ExecuteWorkflowBean)session.getAttribute("workflow"), runId);
        
    	DynaValidatorActionForm fileForm = (DynaValidatorActionForm)form;
        fileForm.set("assayType", runBean.getAssayBean().getAssayType());
        fileForm.set("assay", runBean.getAssayBean().getAssayName());
        fileForm.set("run", runBean.getName());
//        fileForm.set("inout", request.getParameter("type"));
        String inout=(String)fileForm.get("inout");
        String contentPath = request.getContextPath();
        
        String path = PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
        String fullPathName = path + fileForm.get("assayType") + File.separator 
                                   + fileForm.get("assay") + File.separator
                                   + fileForm.get("run")   + File.separator
                                   + fileForm.get("inout") + File.separator
                                   + CalabConstants.UNCOMPRESSED_FILE_DIRECTORY;
        
        // Retrieve filename(not uri) from database
        List<FileBean> fileBeanList = new ArrayList<FileBean>();
//        if ((request.getParameter("type")).equalsIgnoreCase(CalabConstants.INPUT)) {
        if (inout.equalsIgnoreCase(CalabConstants.INPUT)) {
            fileBeanList = runBean.getInputFileBeans();
//        } else if ((request.getParameter("type")).equalsIgnoreCase(CalabConstants.OUTPUT)) {
        } else if (inout.equalsIgnoreCase(CalabConstants.OUTPUT)) {
            fileBeanList = runBean.getOutputFileBeans();
        }
         

        List<FileDownloadInfo>  fileNameHolder = new ArrayList<FileDownloadInfo>();
        for(FileBean fileBean: fileBeanList)
        {
            FileDownloadInfo fileDownloadInfo = new FileDownloadInfo();
            fileDownloadInfo.setFileName(fileBean.getFilename());
            fileDownloadInfo.setUploadDate(fileBean.getCreatedDate());
            fileDownloadInfo.setAction(contentPath+"/fileDownload.do?method=downloadFile&fileName="+fileBean.getFilename()
                                                  +"&runId="+runId+"&inout="+fileForm.get("inout"));
            fileNameHolder.add(fileDownloadInfo);
        }

        
        fileForm.set("fileInfoList", fileNameHolder);
        fileForm.set("downloadAll", contentPath+"/fileDownload.do?method=downloadFile&fileName="+CalabConstants.ALL_FILES+".zip");
        return mapping.findForward("success");
    }
    
    public ActionForward downloadFile(ActionMapping mapping, 
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        HttpSession session = request.getSession();
        DynaValidatorActionForm fileForm = (DynaValidatorActionForm)form;
        ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
        String runId = request.getParameter("runId");
        RunBean runBean = workflowService.getAssayInfoByRun((ExecuteWorkflowBean)session.getAttribute("workflow"), runId);

        fileForm.set("assayType", runBean.getAssayBean().getAssayType());
        fileForm.set("assay", runBean.getAssayBean().getAssayName());
        fileForm.set("run", runBean.getName());
        fileForm.set("inout", request.getParameter("type"));

        
        String fileName = (String)fileForm.get("fileName");
        String path = PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
        String fullPathName = path + fileForm.get("assayType") + File.separator 
                                   + fileForm.get("assay") + File.separator
                                   + fileForm.get("run")   + File.separator
                                   + fileForm.get("inout") + File.separator
                                   + CalabConstants.UNCOMPRESSED_FILE_DIRECTORY;
        File f = new File(fullPathName+File.separator+fileName);
        if (!f.exists())
        {
            logger_.error("File has been remove, please contact system administrator.");
            throw new Exception ("File has been remove, please contact system administrator.");
        }
        ActionUtil actionUtil = new ActionUtil();
        actionUtil.writeBinaryStream(f, response);
        
        return null;
    }
    
    public boolean loginRequired() {        
         return true;
    }
}
