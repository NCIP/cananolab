package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.util.ActionUtil;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.SpecialCharReplacer;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.io.File;
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
 * This class prepares the list of files within a run for different actions.
 * 
 * @author pansu
 * 
 */

public class RunFileAction extends AbstractDispatchAction {
	/**
	 * This method is setting up the parameters for the workflow mask files.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return mapping forward
	 */

	public ActionForward setupMask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		List<FileBean> files = getFilesForRun(request);
		List<FileBean> unmaskedFiles = new ArrayList<FileBean>();
		InitSessionSetup.getInstance().setCurrentRun(request);
		for (FileBean fileBean : files) {
			if (!fileBean.getFileMaskStatus()
					.equals(CaNanoLabConstants.MASK_STATUS)) {
				unmaskedFiles.add(fileBean);
			}
		}
		if (!unmaskedFiles.isEmpty()) {
			request.setAttribute("filesToMask", unmaskedFiles);
		}
		return mapping.findForward("mask");
	}

	public ActionForward setupDownload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<FileBean> files = getFilesForRun(request);
		if (!files.isEmpty()) {
			request.setAttribute("filesToDownload", files);
		}
		return mapping.findForward("download");
	}
	
	private List<FileBean> getFilesForRun(HttpServletRequest request)
			throws Exception {

		HttpSession session = request.getSession();
		RunBean currentRun = (RunBean) session.getAttribute("currentRun");
		String inout = (String) session.getAttribute("inout");
		ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
		List<FileBean> files = workflowService.getLastestFileListByRun(
				currentRun.getId(), inout);
		return files;
	}

	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm fileForm = (DynaActionForm) form;

		response.setHeader("Cache-Control", "Private");
		// set currentRun based on request parameter runId.
		String inout = (String) request.getParameter("inout");

		InitSessionSetup.getInstance().setCurrentRun(request);
		HttpSession session = request.getSession();
		RunBean currentRun = (RunBean) session.getAttribute("currentRun");
		if (inout == null) {
			inout = (String) session.getAttribute("inout");
		}

		SpecialCharReplacer specialCharReplacer = new SpecialCharReplacer();
		String assayType = specialCharReplacer.getReplacedString(currentRun
				.getAssayBean().getAssayType());
		String assayName = specialCharReplacer.getReplacedString(currentRun
				.getAssayBean().getAssayName());
		String runName = specialCharReplacer.getReplacedString(currentRun
				.getName());

		String fileName = (String) fileForm.get("fileName");
		String path = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		String fullPathName = path + assayType + File.separator + assayName
				+ File.separator + runName + File.separator + inout
				+ File.separator + CaNanoLabConstants.UNCOMPRESSED_FILE_DIRECTORY;
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
