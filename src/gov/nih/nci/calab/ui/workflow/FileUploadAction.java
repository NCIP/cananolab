package gov.nih.nci.calab.ui.workflow;

import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.SpecialCharReplacer;
import gov.nih.nci.calab.service.util.file.FileNameConvertor;
import gov.nih.nci.calab.service.util.file.FilePacker;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;
import gov.nih.nci.calab.service.util.file.HttpUploadedFileData;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

public class FileUploadAction extends AbstractDispatchAction {
	private static org.apache.log4j.Logger logger_ = org.apache.log4j.Logger
			.getLogger(FileUploadAction.class);

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

		String inout = (String) request.getParameter("inout");
		String menuType = (String) request.getParameter("menuType");
		InitSessionSetup.getInstance().setCurrentRun(request);
		HttpSession session = request.getSession();
		RunBean currentRun = (RunBean) session.getAttribute("currentRun");
		if (inout == null) {
			inout = (String) session.getAttribute("inout");
		}
		if (menuType == null) {
			menuType = (String) session.getAttribute("menuType");
		}

		SpecialCharReplacer specialCharReplacer = new SpecialCharReplacer();
		String assayType = specialCharReplacer.getReplacedString(currentRun
				.getAssayBean().getAssayType());
		String assayName = specialCharReplacer.getReplacedString(currentRun
				.getAssayBean().getAssayName());
		String runName = specialCharReplacer.getReplacedString(currentRun
				.getName());


		DynaActionForm fileForm = (DynaActionForm) form;
		
		fileForm.set("archiveValue", PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "archiveValue"));
		fileForm.set("servletURL", PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "servletURL"));
		fileForm.set("notifyURL", PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "notifyURL"));
		fileForm.set("defaultURL", PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "defaultURL"));
		fileForm.set("sid", session.getId());
		fileForm.set("module", "calab");
		fileForm.set("permissibleFileExtension", "*");

		HttpFileUploadSessionData hFileUploadData = new HttpFileUploadSessionData();
		hFileUploadData.setInout(inout);
		hFileUploadData.setRunId(currentRun.getId());
		hFileUploadData.setAssay(assayName);
		hFileUploadData.setAssayType(assayType);
		hFileUploadData.setRun(runName);
		// set where this upload from
		hFileUploadData.setFromType(menuType);

		session.setAttribute("httpFileUploadSessionData", hFileUploadData);
		
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);
	
		return mapping.findForward("success");
	}

	public ActionForward persistFileUploadData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		HttpFileUploadSessionData mySessionData = (HttpFileUploadSessionData) request
				.getSession().getAttribute("httpFileUploadSessionData");
		String path = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		String relativePathName = mySessionData.getAssayType() + File.separator
				+ mySessionData.getAssay() + File.separator
				+ mySessionData.getRun() + File.separator
				+ mySessionData.getInout();
		String fullPathName = path + relativePathName;

		String mode = (String) request.getParameter("mode");
		HttpSession session = request.getSession();

		if ("stop".equals(mode)) {
			if (mySessionData != null) {
				mySessionData.setIsStopped(true);
				List list = mySessionData.getFileList();

				for (Iterator it = list.iterator(); it.hasNext();) {
					HttpUploadedFileData data = (HttpUploadedFileData) it
							.next();

					try {
						File file = new File(fullPathName, data.getFileName());

						if (file.exists()) {
							file.delete();
						}

						// delete uncompressed files
						String fileName = null;
						FileNameConvertor fnConvertor = new FileNameConvertor();
						fileName = fnConvertor.getConvertedFileName(data
								.getFileName());

						File uncompressedFile = new File(fullPathName, fileName);
						if (uncompressedFile.exists()) {
							uncompressedFile.delete();
						}
					} catch (Exception e) {
						logger_.info("Mode " + mode + ": File "
								+ data.getFileName()
								+ " can't be deleted due to io error");
					}
				}
				// clear the cache
				mySessionData.clearList();
			}
			forward = null;
		} // end of if "stop".equals(mode)

		// Right after uploaded all the files, parsing service will be called
		// to start process these files. This allows multiple uploads from
		// the same applet. After each upload, the file parsing process is
		// put into a queue for one by one processing.
		else if ("success".equals(mode)) {
			// Should not occur, but in case.
			if (mySessionData == null) {
				logger_.debug("Session data not found");
				return mapping.findForward("expiredSession");
			}

			List fileList = mySessionData.getFileList();
			String inout = mySessionData.getInout();
			String runId = mySessionData.getRunId();

			logger_.info("Persist file upload data to database ");
			// use CaNanoLabConstants.URI_SEPERATOR to save in the database, which
			// might be different from physical file structure/seperator
			String uriPathName = mySessionData.getAssayType()
					+ CaNanoLabConstants.URI_SEPERATOR + mySessionData.getAssay()
					+ CaNanoLabConstants.URI_SEPERATOR + mySessionData.getRun()
					+ CaNanoLabConstants.URI_SEPERATOR + mySessionData.getInout();
			String unzipFilePath = CaNanoLabConstants.URI_SEPERATOR + uriPathName
					+ CaNanoLabConstants.URI_SEPERATOR
					+ CaNanoLabConstants.UNCOMPRESSED_FILE_DIRECTORY;
			ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
			workflowService.saveFile(fileList, unzipFilePath, runId, inout,
					(String) session.getAttribute("creator"));

			session.setAttribute("newFileLoaded", "true");
			session.setAttribute("newRunCreated", "true");
			// After data persistence, we need to create all.zip for all upload
			// files,
			// which includes previous uploaded file and uploaded files
			// currently.

			List<FileBean> fileBeans = workflowService.getLastestFileListByRun(
					runId, inout);

			FilePacker fPacker = new FilePacker();
			String uncompressedFileDirecory = fullPathName + File.separator
					+ CaNanoLabConstants.UNCOMPRESSED_FILE_DIRECTORY;
			fPacker.removeOldZipFile(uncompressedFileDirecory);

			List<String> fileNameHolder = new ArrayList<String>();
			for (FileBean fileBean : fileBeans) {
				fileNameHolder.add(fileBean.getFilename());
			}
			String[] needToPackFiles = new String[fileNameHolder.size()];
			for (int i = 0; i < needToPackFiles.length; i++) {
				needToPackFiles[i] = (String) fileNameHolder.get(i);
			}
			boolean isCreated = fPacker.packFiles(needToPackFiles,
					uncompressedFileDirecory);
			logger_.info("Creating ALL_FILES.zip is " + isCreated);
			mySessionData.clearList();

			forward = null;
		}
		// If user says it's done, then move to our defaut page for this action.
		else if ("done".equals(mode)) {
			// The page will be forwarded to the DefaultURl defined in
			// fileupload.properties
			forward = null;
		}

		return forward;

	}

	public boolean loginRequired() {
		return true;
	}
}
