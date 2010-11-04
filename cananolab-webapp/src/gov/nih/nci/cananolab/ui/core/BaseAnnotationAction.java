package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Base action for all annotation actions
 *
 * @author pansu
 *
 */
public abstract class BaseAnnotationAction extends AbstractDispatchAction {
	protected CurationService curationService;

	/**
	 * setupSample() will retrieve a SampleBean based on the sampleId which is
	 * in request/form. And then check user's access privilege, throws Exception
	 * if user doesn't have privilege. Otherwise, set visibility of Primary POC
	 * of sample based on user's privilege. Finally, set the SampleBean in
	 * request object.
	 *
	 * @param theForm
	 * @param request
	 * @return SampleBean
	 * @throws Exception
	 *             if user is not allowed to access the sample
	 */
	public SampleBean setupSample(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		String sampleId = request.getParameter("sampleId");
		if (!StringUtils.isEmpty(sampleId)) {
			theForm.set("sampleId", sampleId);
		} else {
			sampleId = (String) request.getAttribute("sampleId");
			if (sampleId == null) {
				sampleId = theForm.getString("sampleId");
			}
		}
		// sample service has been created earlier
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");

		SampleBean sampleBean = service.findSampleById(sampleId, true);
		if (sampleBean == null) {
			throw new NotExistException("No such sample in the system");
		}
		request.setAttribute("theSample", sampleBean);

		return sampleBean;
	}

	// check for cases where delete can't happen
	protected boolean checkDelete(HttpServletRequest request,
			ActionMessages msgs, String id) throws Exception {
		return true;
	}

	/**
	 * Download action to handle file downloading and viewing
	 *
	 * @param
	 * @return
	 */
	protected ActionForward downloadFile(BaseService service,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileId = request.getParameter("fileId");
		FileBean fileBean = service.findFileById(fileId);
		if (fileBean != null) {
			if (fileBean.getDomainFile().getUriExternal()) {
				response.sendRedirect(fileBean.getDomainFile().getUri());
				return null;
			}
		}
		String fileRoot = PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "fileRepositoryDir");
		java.io.File dFile = new java.io.File(fileRoot + java.io.File.separator
				+ fileBean.getDomainFile().getUri());
		if (dFile.exists()) {
			ExportUtils.prepareReponseForImage(response, fileBean
					.getDomainFile().getUri());

			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(dFile));
				out = response.getOutputStream();
				byte[] bytes = new byte[32768];
				int numRead = 0;
				while ((numRead = in.read(bytes)) > 0) {
					out.write(bytes, 0, numRead);
				}
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("error.noFile");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			throw new FileException("File " + fileBean.getDomainFile().getUri()
					+ " doesn't exist on the server");
		}
		return null;
	}

	/**
	 * Download action to handle file downloading and viewing
	 *
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	protected SampleBean[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm, SampleBean oldSampleBean)
			throws Exception {
		String[] otherSamples = (String[]) theForm.get("otherSamples");
		if (otherSamples.length == 0) {
			return null;
		}
		SampleBean[] sampleBeans = new SampleBean[otherSamples.length];
		SampleService sampleService = (SampleService) request.getSession()
				.getAttribute("sampleService");
		int i = 0;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		for (String other : otherSamples) {
			SampleBean sampleBean = sampleService.findSampleByName(other);
			sampleBean.setGroupAccesses(oldSampleBean.getGroupAccesses());
			sampleBean.setUserAccesses(oldSampleBean.getUserAccesses());
			sampleBean.setUser(user);
			sampleBeans[i] = sampleBean;
			i++;
		}
		return sampleBeans;
	}

	protected boolean validateFileBean(HttpServletRequest request,
			ActionMessages msgs, FileBean fileBean) {

		boolean noErrors = true;
		if (fileBean == null) {
			return noErrors;
		}
		File File = fileBean.getDomainFile();
		if (File.getTitle().length() == 0) {
			ActionMessage msg = new ActionMessage("errors.required",
					"file title");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			noErrors = false;
		}

		if (File.getType().length() == 0) {
			ActionMessage msg = new ActionMessage("errors.required",
					"file type");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			noErrors = false;
		}

		if (File.getUriExternal()) {
			if (fileBean.getExternalUrl() == null
					|| fileBean.getExternalUrl().trim().length() == 0) {
				ActionMessage msg = new ActionMessage("errors.required",
						"external url");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
			}
		} else {
			// all empty
			if ((fileBean.getUploadedFile() == null || fileBean
					.getUploadedFile().toString().trim().length() == 0)
					&& (fileBean.getExternalUrl() == null || fileBean
							.getExternalUrl().trim().length() == 0)
					&& (fileBean.getDomainFile() == null || fileBean
							.getDomainFile().getTitle() == null)) {
				ActionMessage msg = new ActionMessage("errors.required",
						"uploaded file");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
				// the case that user switch from url to upload file, but no
				// file is selected
			} else if ((fileBean.getUploadedFile() == null || StringUtils
					.isEmpty(fileBean.getUploadedFile().getFileName()))
					&& !StringUtils.isEmpty(fileBean.getExternalUrl())) {
				ActionMessage msg = new ActionMessage("errors.required",
						"uploaded file");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
			}
		}
		return noErrors;
	}

	/**
	 * If user entered customized value by selecting [other] option previously,
	 * then add the value in collection, so user can see it again.
	 *
	 * @param request
	 * @param value
	 * @param sessionName
	 * @param attributeName
	 */
	public void setOtherValueOption(HttpServletRequest request, String value,
			String sessionName) {
		if (!StringUtils.isEmpty(value)) {
			Collection<String> otherTypes = (Collection<String>) request
					.getSession().getAttribute(sessionName);
			if (otherTypes != null && !otherTypes.contains(value)) {
				otherTypes.add(value);
			}
		}
	}

	/**
	 * Returns a partial URL for downloading a file from local/remote host.
	 *
	 * @param request
	 * @param serviceUrl
	 * @return
	 * @throws Exception
	 */
	protected StringBuilder getDownloadUrl(HttpServletRequest request)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getRequestURL().toString());

		sb.append(ExportUtils.DOWNLOAD_URL).append("&fileId=");

		return sb;
	}

	/**
	 * Save uploaded form file in session for later use, avoid upload again.
	 *
	 * @param request
	 * @param theFile
	 */
	protected void preserveUploadedFile(HttpServletRequest request,
			FileBean theFile, String folder) {
		FormFile uploadedFile = theFile.getUploadedFile();
		if (uploadedFile != null && uploadedFile.getFileSize() > 0) {
			// 1.save uploaded file in session for later use.
			HttpSession session = request.getSession();
			session.removeAttribute("uploadedFormFile");
			session.setAttribute("uploadedFormFile", uploadedFile);

			// 2.construct URI for File to show it on file upload page.
			SampleBean sampleBean = (SampleBean) request.getSession()
					.getAttribute("theSample");
			String internalUriPath = Constants.FOLDER_PARTICLE + '/'
					+ sampleBean.getDomain().getName() + '/' + folder;
			String timestamp = DateUtils.convertDateToString(Calendar
					.getInstance().getTime(), "yyyyMMdd_HH-mm-ss-SSS");
			theFile.getDomainFile().setUri(
					internalUriPath + '/' + timestamp + '_'
							+ uploadedFile.getFileName());
		}
	}

	/**
	 * If FileBean specified using a uploaded file but the file is empty, we
	 * know we should get the uploaded file from session.
	 *
	 * @param request
	 * @param theFile
	 */
	protected void restoreUploadedFile(HttpServletRequest request,
			FileBean theFile) {
		if (!theFile.getDomainFile().getUriExternal()
				&& StringUtils.isEmpty(theFile.getExternalUrl())
				&& (theFile.getUploadedFile() == null || theFile
						.getUploadedFile().getFileSize() <= 0)) {
			HttpSession session = request.getSession();
			theFile.setUploadedFile((FormFile) session
					.getAttribute("uploadedFormFile"));
		}
	}

	public Boolean canUserExecutePrivateDispatch(HttpServletRequest request,
			String protectedData) throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null) {
			return false;
		}
		if (protectedData == null) {
			return true;
		} else {
			// per app scan check id format
			ActionMessages msgs = new ActionMessages();
			if (protectedData != null
					&& !protectedData.matches(Constants.NUMERIC_PATTERN)) {
				ActionMessage msg = new ActionMessage("invalid.id");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				throw new NotExistException("Invalid ID format");
			}
			try {
				SecurityService securityService = getSecurityServiceFromSession(request);
				return securityService.checkCreatePermission(protectedData);
			} catch (Exception e) {
				throw new SecurityException();
			}
		}
	}

	public ActionForward submitForReview(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		String dataId = request.getParameter("reviewDataId");
		String dataType = request.getParameter("reviewDataType");
		String dataName = request.getParameter("reviewDataName");
		String forwardName = request.getParameter("forwardName");
		// set default forward name to be summaryEdit
		if (forwardName == null) {
			forwardName = "summaryEdit";
		}
		DataReviewStatusBean dataReviewStatusBean = new DataReviewStatusBean();
		dataReviewStatusBean.setDataId(dataId);
		dataReviewStatusBean.setDataName(dataName);
		dataReviewStatusBean.setDataType(dataType);
		dataReviewStatusBean
				.setReviewStatus(DataReviewStatusBean.PENDING_STATUS);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		dataReviewStatusBean.setSubmittedBy(user.getLoginName());
		dataReviewStatusBean.setSubmittedDate(new Date());
		curationService.submitDataForReview(dataReviewStatusBean,
				securityService);

		ActionMessages messages = new ActionMessages();
		ActionMessage msg = null;
		msg = new ActionMessage("message.submitReview");
		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, messages);
		return mapping.findForward(forwardName);
	}

	protected void setUpSubmitForReviewButton(HttpServletRequest request,
			String dataId, Boolean publicData) throws Exception {
		// show 'submit for review' button if data is not public and doesn't
		// have tracted status and user is
		// not curator
		if (!publicData) {
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			SecurityService securityService = getSecurityServiceFromSession(request);
			DataReviewStatusBean reviewStatus = curationService
					.findDataReviewStatusBeanByDataId(dataId, securityService);

			if (!user.isCurator()
					&& (reviewStatus == null || reviewStatus != null
							&& reviewStatus.getReviewStatus().equals(
									DataReviewStatusBean.RETRACTED_STATUS))) {
				request.setAttribute("review", true);
			} else {
				request.setAttribute("review", false);
			}
		} else {
			request.setAttribute("review", false);
		}
	}

	protected void switchPendingReviewToPublic(HttpServletRequest request,
			String dataId) throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SecurityService securityService = getSecurityServiceFromSession(request);
		DataReviewStatusBean reviewStatus = curationService
				.findDataReviewStatusBeanByDataId(dataId, securityService);
		if (user.isCurator()
				&& reviewStatus != null
				&& reviewStatus.getReviewStatus().equals(
						DataReviewStatusBean.PENDING_STATUS)) {
			reviewStatus.setReviewStatus(DataReviewStatusBean.PUBLIC_STATUS);
			curationService.submitDataForReview(reviewStatus, securityService);
		}
	}

	/**
	 * Update the review status of the given data to the give status
	 *
	 * @param status
	 * @param request
	 * @param dataId
	 * @param dataName
	 * @param dataType
	 * @throws Exception
	 */
	protected void updateReviewStatusTo(String status,
			HttpServletRequest request, String dataId, String dataName,
			String dataType) throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SecurityService securityService = getSecurityServiceFromSession(request);
		DataReviewStatusBean reviewStatus = curationService
				.findDataReviewStatusBeanByDataId(dataId, securityService);
		if (reviewStatus == null) {
			reviewStatus = new DataReviewStatusBean();
			reviewStatus.setDataId(dataId);
			reviewStatus.setDataName(dataName);
			reviewStatus.setDataType(dataType);
			reviewStatus.setSubmittedBy(user.getLoginName());
			reviewStatus.setSubmittedDate(new Date());
		} else {
			if (reviewStatus.getReviewStatus().equals(status)) {
				return;
			}
		}
		reviewStatus.setReviewStatus(status);
		curationService.submitDataForReview(reviewStatus, securityService);
	}

	protected void retractFromPublic(DynaValidatorForm theForm,
			HttpServletRequest request, String dataId, String dataName,
			String dataType) throws Exception {
		updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS, request,
				dataId, dataName, dataType);
		removePublicAccess(theForm, request);
	}

	// to be overwritten by child class if necessary
	protected void removePublicAccess(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		SampleBean sample = this.setupSample(theForm, request);
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		service.removeAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS, sample
				.getDomain());
	}

	public CurationService getCurationService() {
		return curationService;
	}

	public void setCurationService(CurationService curationService) {
		this.curationService = curationService;
	}

	// check if the entered user name is valid
	private Boolean validateGroupAccess(HttpServletRequest request,
			AccessibilityBean access) throws Exception {
		SecurityService securityService = getSecurityServiceFromSession(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user.isCurator()
				&& access.getGroupName().equalsIgnoreCase(
						AccessibilityBean.CSM_PUBLIC_GROUP)) {
			return true;
		}
		if (access.getAccessBy().equals(AccessibilityBean.ACCESS_BY_GROUP)) {
			String groupName = access.getGroupName();
			return securityService.isGroupAccessibleByUser(groupName);
		}
		return true;
	}

	// check if the entered group name is valid
	private Boolean validateUserAccess(HttpServletRequest request,
			AccessibilityBean access) throws Exception {
		SecurityService securityService = getSecurityServiceFromSession(request);
		if (access.getAccessBy().equals(AccessibilityBean.ACCESS_BY_USER)) {
			UserBean user = access.getUserBean();
			return securityService.isUserValid(user.getLoginName());
		}
		return true;
	}

	protected Boolean validateAccess(HttpServletRequest request,
			AccessibilityBean theAccess) throws Exception {
		Boolean accessValid = true;
		ActionMessages msgs = new ActionMessages();
		// per app scan, in case user submits parameters through URL instead of
		// form
		if (!theAccess.getAccessBy().equalsIgnoreCase(
				AccessibilityBean.ACCESS_BY_GROUP)
				&& !theAccess.getAccessBy().equalsIgnoreCase(
						AccessibilityBean.ACCESS_BY_PUBLIC)
				&& !theAccess.getAccessBy().equalsIgnoreCase(
						AccessibilityBean.ACCESS_BY_USER)) {
			ActionMessage msg = new ActionMessage("error.invalidAccessBy",
					theAccess.getAccessBy());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			accessValid = false;
		}
		if (!theAccess.getRoleName().equalsIgnoreCase(
				AccessibilityBean.CSM_CURD_ROLE)
				&& !theAccess.getRoleName().equalsIgnoreCase(
						AccessibilityBean.CSM_READ_ROLE)) {
			ActionMessage msg = new ActionMessage("error.invalidRoleName",
					theAccess.getRoleName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			accessValid = false;
		}
		if (!validateGroupAccess(request, theAccess)) {
			ActionMessage msg = new ActionMessage("error.invalidGroup",
					theAccess.getGroupName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			accessValid = false;
		}
		if (!validateUserAccess(request, theAccess)) {
			ActionMessage msg = new ActionMessage("error.invalidUser",
					theAccess.getUserBean().getLoginName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			accessValid = false;
		}
		request.getSession().setAttribute("accessValid", accessValid);
		return accessValid;
	}

	protected String validateId(HttpServletRequest request, String idParameter)
			throws Exception {
		String id = request.getParameter(idParameter);
		if (id == null) {
			id = (String) request.getAttribute(idParameter);
		}
		if (id == null) {
			throw new NotExistException("No such ID in the database");
		}
		// per app scan
		if (!id.matches(Constants.NUMERIC_PATTERN)) {
			throw new NotExistException("Invalid value for ID");
		}
		return id;
	}

	protected void checkOpenAccessForm(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean accessValid = true;
		if (session.getAttribute("accessValid") != null) {
			accessValid = (Boolean) session.getAttribute("accessValid");
		}
		Boolean openAccess = false;
		if (dispatch.equals("input") && browserDispatch.equals("saveAccess")
				|| !accessValid) {
			openAccess = true;
		}
		session.setAttribute("openAccess", openAccess);
		session.removeAttribute("accessValid");
		// in case of validation error still show access label correctly.
		request.setAttribute("onloadJavascript", "toggleAccessNameLabel();");
	}
}
