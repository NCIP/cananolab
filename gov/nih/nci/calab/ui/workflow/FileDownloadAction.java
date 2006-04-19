package gov.nih.nci.calab.ui.workflow;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.dto.workflow.FileDownloadInfo;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.ActionUtil;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.file.FileLocatorUtils;
import gov.nih.nci.calab.service.util.file.FileNameConvertor;
import gov.nih.nci.calab.service.util.file.FilePacker;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;
import gov.nih.nci.calab.service.util.file.HttpUploadedFileData;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
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
    
    public String fullPathName = null; 
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
        //TODO: get data from database or GUI, currently, all parameters are hardcoded. 
        fileForm.set("assayType", runBean.getAssayBean().getAssayType());
        fileForm.set("assay", runBean.getAssayBean().getAssayName());
        fileForm.set("run", runBean.getName());
        fileForm.set("inout", (request.getParameter("type")).equalsIgnoreCase("in")?"Input" : "Output");
 
//    	fileForm.set("assayType", "Prescreening_Assay");
//        fileForm.set("assay", "STE_1");
//        fileForm.set("run", "run1");
//        fileForm.set("inout", "Input");
        String contentPath = request.getContextPath();
        
        String path = PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY, "inputFileDirectory");
        fullPathName = path + fileForm.get("assayType") + File.separator 
                                   + fileForm.get("assay") + File.separator
                                   + fileForm.get("run")   + File.separator
                                   + fileForm.get("inout") + File.separator
                                   + CalabConstants.UNCOMPRESSED_FILE_DIRECTORY;

        ArrayList  fileNameHolder = new ArrayList();
        String[] listFiles = new File(fullPathName).list();
        String allFileName = CalabConstants.ALL_FILES + ".zip";

        for(int i = 0; i < listFiles.length; i++ )
        {
            if (!listFiles[i].equalsIgnoreCase(allFileName))
            {    
                FileDownloadInfo fileDownloadInfo = new FileDownloadInfo();
                fileDownloadInfo.setFileName(listFiles[i]);
                fileDownloadInfo.setUploadDate(new Date());
                fileDownloadInfo.setAction(contentPath+"/fileDownload.do?method=downloadFile&fileName="+listFiles[i]);
                fileNameHolder.add(fileDownloadInfo);
            }
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
        DynaValidatorActionForm fileForm = (DynaValidatorActionForm)form;
        
        String fileName = (String)fileForm.get("fileName");
        File f = new File(fullPathName+File.separator+fileName);
        ActionUtil actionUtil = new ActionUtil();
        actionUtil.writeBinaryStream(f, response);
        
        return null;
    }
    
    public boolean loginRequired() {        
         return true;
    }
}
