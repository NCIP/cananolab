package gov.nih.nci.calab.ui.workflow;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.file.FileLocatorUtils;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;
import gov.nih.nci.calab.service.util.file.HttpUploadedFileData;

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

public class FileUploadAction extends DispatchAction
{
    private static org.apache.log4j.Logger logger_ =
        org.apache.log4j.Logger.getLogger(FileUploadAction.class);
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
        DynaValidatorActionForm fileForm = (DynaValidatorActionForm)form;
        //TODO: get data from database or GUI, currently, all parameters are hardcoded. 
        fileForm.set("assayType", "Prescreening_Assay");
        fileForm.set("assay", "STE_1");
        fileForm.set("run", "run1");
        fileForm.set("runby", "Jim Zhou");
        fileForm.set("rundate", "04/11/2006");
        fileForm.set("inout", "input");
        
        HttpSession session = request.getSession();
        
        fileForm.set("archiveValue", PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY,"archiveValue"));
        fileForm.set("servletURL", PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY,"servletURL"));
        fileForm.set("notifyURL", PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY,"notifyURL"));
        fileForm.set("defaultURL", PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY, "defaultURL"));
        fileForm.set("sid", session.getId());
        fileForm.set("module", "calab");
        fileForm.set("permissibleFileExtension", "*");
        
        HttpFileUploadSessionData hFileUploadData = (HttpFileUploadSessionData)session.getAttribute("httpFileUploadSessionData");
        if (hFileUploadData != null)
        {
            session.removeAttribute("httpFileUploadSessionData");
        }
        hFileUploadData = new HttpFileUploadSessionData();
        hFileUploadData.setAssayType("Prescreening_Assay");
        hFileUploadData.setAssay("STE_1");
        hFileUploadData.setRun("run1");
        hFileUploadData.setInout("input");
        
        session.setAttribute("httpFileUploadSessionData", hFileUploadData);
        
        return mapping.findForward("success");
    }
    
    public ActionForward persistFileUploadData(ActionMapping mapping, 
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        ActionForward forward = null;
        HttpFileUploadSessionData mySessionData = (HttpFileUploadSessionData)request.getSession().getAttribute("httpFileUploadSessionData");
        String mode = (String)request.getParameter("mode");
        
        if ("stop".equals(mode))
        {
            if (mySessionData != null)
            {
                mySessionData.setIsStopped(true);
                List list = mySessionData.getFileList();
                
                String path = PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY, "inputFileDirectory");
                String fullPathName = path + mySessionData.getAssayType() + File.separator 
                + mySessionData.getAssay() + File.separator
                + mySessionData.getRun()   + File.separator
                + mySessionData.getInout();
                
                for (Iterator it = list.iterator(); it.hasNext();)
                {
                    HttpUploadedFileData data = (HttpUploadedFileData)it.next();
                    
                    try
                    {
                        //File file = new File("C:\\temp_zips\\upload_files", data.getFileName());
                        File file = new File(fullPathName, data.getFileName());
                    
                        if (file.exists())
                        {
                            file.delete();
                        }
                    }
                    catch (Exception e)
                    {
                        logger_.info("Mode " + mode  + ": File " + data.getFileName() + " can't be deleted due to io error");
                    }
                }
                //clear the cache
                mySessionData.clearList();
            }
            forward = null;
        } // end of if "stop".equals(mode)
        
        //Right after uploaded all the files, parsing service will be called
        //to start process these files.  This allows multiple uploads from
        //the same applet.  After each upload, the file parsing process is
        //put into a queue for one by one processing.
        else if ("success".equals(mode))
        {
            //Should not occur, but in case.
            if (mySessionData == null)
            {
                logger_.debug("Session data not found");
                return mapping.findForward("expiredSession");
            }
            
            
            List fileList = mySessionData.getFileList();
            String assayType = mySessionData.getAssayType();
            String assay = mySessionData.getAssay();
            String run = mySessionData.getRun();
            String inout = mySessionData.getInout();
            
            //Persist data here 
            logger_.info("Persist file upload data to database ");

            mySessionData.clearList();

            forward = null;
        }
        //If user says it's done, then move to our defaut page for this action.
        else if ("done".equals(mode))
        {
            request.getSession().removeAttribute("httpFileUploadSessionData");
            forward = mapping.findForward("done");
        }
                
        return forward;    

    }
}
