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
 * This class handle workflow file mask process.
 * 
 * @author zhoujim
 *
 */

public class FileMaskAction extends AbstractDispatchAction
{
    private static org.apache.log4j.Logger logger_ =
        org.apache.log4j.Logger.getLogger(FileMaskAction.class);
    
    public String fullPathName = null; 
    /**
     * This method is setting up the parameters for the workflow mask files.
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
        fileForm.set("inout", (request.getParameter("type")).equalsIgnoreCase("in")?"Input" : "Output");
 
        String contentPath = request.getContextPath();
        
        // Retrieve filename(not uri) from database
        List<FileBean> fileBeanList = new ArrayList<FileBean>();
        if ((request.getParameter("type")).equalsIgnoreCase("in")) {
            List<FileBean> allfiles = runBean.getInputFileBeans();
            for(FileBean fileBean: allfiles){
            	if (fileBean.getStatus().equals(CalabConstants.OTHER_STATUS)){
            		fileBeanList.add(fileBean);
            	}
            }
        } else if ((request.getParameter("type")).equalsIgnoreCase("out")) {
            List<FileBean> allfiles = runBean.getOutputFileBeans();
            for(FileBean fileBean: allfiles){
            	if (fileBean.getStatus().equals(CalabConstants.OTHER_STATUS)){
            		fileBeanList.add(fileBean);
            	}
            }
        }
         

        List<FileDownloadInfo>  fileNameHolder = new ArrayList<FileDownloadInfo>();
        for(FileBean fileBean: fileBeanList)
        {
            FileDownloadInfo fileDownloadInfo = new FileDownloadInfo();
            fileDownloadInfo.setFileName(fileBean.getFilename());
            fileDownloadInfo.setUploadDate(fileBean.getCreatedDate());
            fileDownloadInfo.setAction(contentPath + "/fileMask.do?method=maskFile&fileId=" + fileBean.getId() +"&fileName=" +  fileBean.getFilename());
            fileNameHolder.add(fileDownloadInfo);
        }
        fileForm.set("fileInfoList", fileNameHolder);

        return mapping.findForward("success");
    }
    
    public ActionForward maskFile(ActionMapping mapping, 
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        DynaValidatorActionForm fileForm = (DynaValidatorActionForm)form;
        
        String fileId = (String)fileForm.get("fileId");
        String fileName = (String)fileForm.get("fileName");
       
        return mapping.findForward("maskpage");
    }
    
    public boolean loginRequired() {        
         return true;
    }
}
