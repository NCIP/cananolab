package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.sample.InitSampleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DataLinkBean;
import gov.nih.nci.cananolab.util.PropertyReader;

import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Base action for all annotation actions
 *
 * @author pansu
 *
 */
public abstract class BaseAnnotationAction extends AbstractDispatchAction {

	/**
	 * setupSample() will retrieve a SampleBean based on the sampleId which is in request/form.
	 * And then check user's access privilege, throws Exception if user doesn't have privilege.
	 * Otherwise, set visibility of Primary POC of sample based on user's privilege.
	 * Lastly, set the SampleBean in request object.
	 *
	 * @param theForm
	 * @param request
	 * @param location
	 * @return SampleBean
	 * @throws Exception if user in session is not allowed to access this sample particle.
	 */
	public SampleBean setupSample(DynaValidatorForm theForm,
			HttpServletRequest request, String location) throws Exception {
		String sampleId = request.getParameter("sampleId");
		if (sampleId!=null) {
			theForm.set("sampleId", sampleId);
		}
		else {
			sampleId = (String) request.getAttribute("sampleId");
			if (sampleId == null) {
				sampleId = theForm.getString("sampleId");
			}
		}
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		SampleService service = null;
		if (location.equals("local")) {
			service = new SampleServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			//TODO model change
			//service = new SampleServiceRemoteImpl(serviceUrl);
		}
		SampleBean sampleBean = service.findSampleById(sampleId);
		if (location.equals("local")) {
			// check access privilege
			AuthorizationService auth = new AuthorizationService(Constants.CSM_APP_NAME);
			boolean access = auth.isUserAllowed(sampleBean
					.getDomain().getName(), user);
			if (!access) {
				if (user != null) {
					request.getSession().removeAttribute("user");
				}
				throw new NoAccessException(
						"You don't have the required privileges to access this sample");
			}
		}
		sampleBean.setLocation(location);
		request.setAttribute("theSample", sampleBean);
		return sampleBean;
	}

	protected void saveFilesToFileSystem(List<FileBean> files) throws Exception {
		// save file data to file system and set visibility
		FileService fileService = new FileServiceLocalImpl();
		for (FileBean fileBean : files) {
			fileService.writeFile(fileBean.getDomainFile(), fileBean
					.getNewFileData());
			fileService.assignVisibility(fileBean);
		}
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PARTICLE);
	}

	public Map<String, SortedSet<DataLinkBean>> setupDataTree(
			SampleBean sampleBean, HttpServletRequest request) throws Exception {
		request.setAttribute("updateDataTree", "true");
		return InitSampleSetup.getInstance().getDataTree(sampleBean, request);
	}

	public ActionForward setupDeleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String submitType = request.getParameter("submitType");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = setupSample(theForm, request, "local");
		//TODO add implementation detail
		return mapping.findForward("annotationDeleteView");
	}

	// check for cases where delete can't happen
	protected boolean checkDelete(HttpServletRequest request,
			ActionMessages msgs, String id) throws Exception {
		return true;
	}

	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String submitType = request.getParameter("submitType");
		String className = InitSetup.getInstance().getClassName(submitType,
				request.getSession().getServletContext());
		String fullClassName = ClassUtils.getFullClass(className)
				.getCanonicalName();

		String[] dataIds = (String[]) theForm.get("idsToDelete");
		SampleService sampleService = new SampleServiceLocalImpl();
		ActionMessages msgs = new ActionMessages();
		for (String id : dataIds) {
			if (!checkDelete(request, msgs, id)) {
				return mapping.findForward("annotationDeleteView");
			}
			sampleService.deleteAnnotationById(fullClassName, new Long(id));
		}
		SampleBean sampleBean = setupSample(theForm, request, "local");
		setupDataTree(sampleBean, request);
		ActionMessage msg = new ActionMessage("message.deleteAnnotations",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return mapping.findForward("success");
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

		String fileId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String location = request.getParameter("location");
		FileService fileService = null;
		String remoteServerHostUrl = "";
		FileBean fileBean = null;
		String serviceUrl = null;
		if (location.equals("local")) {
			fileService = new FileServiceLocalImpl();
		}
		// CQL2HQL filters out subclasses, disabled the filter
		else {
			serviceUrl = InitSetup.getInstance().getGridServiceUrl(request,
					location);
			// TODO model change
			// fileService = new FileServiceRemoteImpl(serviceUrl);
		}
		fileBean = fileService.findFileById(fileId, user);
		if (fileBean != null) {
			if (fileBean.getDomainFile().getUriExternal()) {
				response.sendRedirect(fileBean.getDomainFile().getUri());
				return null;
			}
		}
		if (!location.equals("local")) {
			// assume grid service is located on the same server and port as
			// webapp
			URL localURL = new URL(request.getRequestURL().toString());
			String actionPath = localURL.getPath();

			URL remoteUrl = new URL(serviceUrl);
			remoteServerHostUrl = remoteUrl.getProtocol() + "://"
					+ remoteUrl.getHost() + ":" + remoteUrl.getPort();
			String remoteDownloadUrl = remoteServerHostUrl + actionPath
					+ "?dispatch=download" + "&fileId=" + fileId
					+ "&location=local";
			// remote URL
			response.sendRedirect(remoteDownloadUrl);
			return null;
		}

		String fileRoot = PropertyReader.getProperty(
				Constants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		java.io.File dFile = new java.io.File(fileRoot + java.io.File.separator
				+ fileBean.getDomainFile().getUri());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename=\""
					+ fileBean.getDomainFile().getName() + "\"");
			response.setHeader("cache-control", "Private");

			java.io.InputStream in = new FileInputStream(dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];

			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
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

	protected Sample[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		String[] otherSamples = (String[]) theForm.get("otherSamples");
		if (otherSamples.length == 0) {
			return null;
		}
		Sample[] samples = new Sample[otherSamples.length];
		SampleService sampleService = new SampleServiceLocalImpl();
		int i = 0;
		for (String other : otherSamples) {
			Sample sample = sampleService.findSampleByName(other);
			samples[i] = sample;
			i++;
		}
		return samples;
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
							.getDomainFile().getName() == null)) {
				ActionMessage msg = new ActionMessage("errors.required",
						"uploaded file");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
				// the case that user switch from url to upload file, but no
				// file is selected
			} else if ((fileBean.getUploadedFile() == null || fileBean
					.getUploadedFile().getFileName().length() == 0)
					&& fileBean.getExternalUrl() != null
					&& fileBean.getExternalUrl().trim().length() > 0) {
				ActionMessage msg = new ActionMessage("errors.required",
						"uploaded file");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
			}
		}
		return noErrors;
	}

	public void checkVisibility(HttpServletRequest request, String location,
			UserBean user, FileBean fileBean) throws Exception {
		if (location.equals("local")) {
			FileService fileService = new FileServiceLocalImpl();
			fileService.retrieveVisibility(fileBean, user);
			if (fileBean.isHidden()) {
				if (user != null) {
					request.getSession().removeAttribute("user");
					throw new NoAccessException();
				} else {
					throw new InvalidSessionException();
				}
			}
		}
	}
}
