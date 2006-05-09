package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class FileMaskAction extends AbstractBaseAction {
	/**
	 * This method is setting up the parameters for the workflow mask files.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return mapping forward
	 */
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DynaActionForm fileForm = (DynaActionForm) form;
		String runId = (String) fileForm.get("runId");
		String inout = (String) fileForm.get("inout");

		// Retrieve filename(not uri) from database
		ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
		List<FileBean> fileBeanList = new ArrayList<FileBean>();
		List<FileBean> files = workflowService.getLastestFileListByRun(runId,
				inout);
		for (FileBean fileBean : files) {
			if (!fileBean.getFileMaskStatus()
					.equals(CalabConstants.MASK_STATUS)) {
				fileBeanList.add(fileBean);
			}
		}

		if (!fileBeanList.isEmpty()) {
			request.setAttribute("filesToMask", fileBeanList);
		}
		return mapping.findForward("success");
	}

	public boolean loginRequired() {
		return true;
	}
}
