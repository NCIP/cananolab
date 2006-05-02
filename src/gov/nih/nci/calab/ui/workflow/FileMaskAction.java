package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * This class handle workflow file mask process.
 * 
 * @author zhoujim, pansu
 *
 */

public class FileMaskAction extends AbstractBaseAction
{
    /**
     * This method is setting up the parameters for the workflow mask files.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return mapping forward
     */
    public ActionForward executeTask(ActionMapping mapping, 
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        DynaActionForm fileForm = (DynaActionForm)form;
        ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
        String runId=(String)fileForm.get("runId");
        RunBean runBean = workflowService.getAssayInfoByRun((ExecuteWorkflowBean)session.getAttribute("workflow"), runId);
    	
        fileForm.set("assayType", runBean.getAssayBean().getAssayType());
        fileForm.set("assayName", runBean.getAssayBean().getAssayName());
        fileForm.set("runName", runBean.getName());    
        
        String inout=(String)fileForm.get("inout");
        //getting inout from request attribute when parameter is not available
        if (request.getAttribute("inout")!=null) {
        	inout=(String)request.getAttribute("inout");
        	fileForm.set("inout",(String)request.getAttribute("inout"));
        }
         
        // Retrieve filename(not uri) from database
        List<FileBean> fileBeanList = new ArrayList<FileBean>();
        if (inout.equalsIgnoreCase(CalabConstants.INPUT)) {
            List<FileBean> allfiles = runBean.getInputFileBeans();
            for(FileBean fileBean: allfiles){
            	if (!fileBean.getFileMaskStatus().equals(CalabConstants.MASK_STATUS)){
            		fileBeanList.add(fileBean);
            	}
            }
        } 
        else if (inout.equalsIgnoreCase(CalabConstants.OUTPUT)) {
            List<FileBean> allfiles = runBean.getOutputFileBeans();
            for(FileBean fileBean: allfiles){
            	if (!fileBean.getFileMaskStatus().equals(CalabConstants.MASK_STATUS)){
            		fileBeanList.add(fileBean);
            	}
            }
        }
        request.setAttribute("filesToMask", fileBeanList);
        return mapping.findForward("success");
    }
    
    public boolean loginRequired() {        
         return true;
    }
}
