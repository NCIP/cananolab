package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.util.ActionUtil;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.SpecialCharReplacer;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * This class handle workflow upload process.
 * 
 * @author zhoujim, pansu
 * 
 */

public class FileDownloadAction extends AbstractDispatchAction {
	/**
	 * This method is setting up the parameters for the workflow input upload
	 * files or output upload files.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return mapping forward
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaActionForm fileForm = (DynaActionForm) form;
		String runId = (String) fileForm.get("runId");
		String inout = (String) fileForm.get("inout");

		// Retrieve filename(not uri) from database
		ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
		List<FileBean> fileBeanList = workflowService.getLastestFileListByRun(
				runId, inout);

		if (!fileBeanList.isEmpty()) {
			request.setAttribute("filesToDownload", fileBeanList);
		}

		return mapping.findForward("success");
	}

	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm fileForm = (DynaActionForm) form;
		
		String assayType = (String) fileForm.get("assayType");
		String assayName = (String) fileForm.get("assayName");
		String runName = (String) fileForm.get("runName");		
		String inout = (String) fileForm.get("inout");
		
		SpecialCharReplacer specialCharReplacer = new SpecialCharReplacer();
		assayType = specialCharReplacer.getReplacedString(assayType);
		assayName = specialCharReplacer.getReplacedString(assayName);
		runName = specialCharReplacer.getReplacedString(runName);	

		String fileName = (String) fileForm.get("fileName");
		String path = PropertyReader.getProperty(
				CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		String fullPathName = path + assayType + File.separator
				+ assayName + File.separator + runName
				+ File.separator + inout + File.separator
				+ CalabConstants.UNCOMPRESSED_FILE_DIRECTORY;
		File f = new File(fullPathName + File.separator + fileName);
		if (!f.exists()) {
			throw new CalabException(
					"File to download doesn't exist on the server");
		}
		ActionUtil actionUtil = new ActionUtil();
		actionUtil.writeBinaryStream(f, response);

		return null;
	}

	public boolean loginRequired() {
		return true;
	}
}
