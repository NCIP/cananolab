package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        fileForm.set("assayType", "Prescreening Assay");
        fileForm.set("assay", "STE-1");
        fileForm.set("run", "run 1");
        fileForm.set("runby", "Jim Zhou");
        fileForm.set("rundate", "04/11/2006");
        fileForm.set("inout", "input");
        
        fileForm.set("archiveValue", PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY,"archiveValue"));
        fileForm.set("servletURL", PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY,"servletURL"));
        fileForm.set("notifyURL", PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY,"notifyURL"));
        fileForm.set("module", "calab");
        fileForm.set("permissibleFileExtension", null);
        
        
        return mapping.findForward("success");
    }
    
}
