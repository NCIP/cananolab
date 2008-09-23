package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.particle.InitCompositionSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.publication.InitPublicationSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.DataLinkBean;
import gov.nih.nci.cananolab.util.PropertyReader;

import java.io.File;
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
	public ParticleBean setupParticle(DynaValidatorForm theForm,
			HttpServletRequest request, String location) throws Exception {
		String particleId = request.getParameter("particleId");
		if (particleId == null) {
			particleId = theForm.getString("particleId");
		}
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		NanoparticleSampleService service = null;
		if (location.equals("local")) {
			service = new NanoparticleSampleServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new NanoparticleSampleServiceRemoteImpl(serviceUrl);
		}
		ParticleBean particleBean = service
				.findNanoparticleSampleById(particleId);
		particleBean.setLocation(location);
		request.setAttribute("theParticle", particleBean);
		if (location.equals("local")) {
			InitNanoparticleSetup.getInstance().getOtherParticleNames(
					request,
					particleBean.getDomainParticleSample().getName(),
					particleBean.getDomainParticleSample().getSource()
							.getOrganizationName(), user);
		}
		return particleBean;
	}

	protected void saveFilesToFileSystem(List<LabFileBean> files)
			throws Exception {
		// save file data to file system and set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);

		FileService fileService = new FileServiceLocalImpl();
		for (LabFileBean fileBean : files) {
			fileService.writeFile(fileBean.getDomainFile(), fileBean
					.getNewFileData());
			authService.assignVisibility(fileBean.getDomainFile().getId()
					.toString(), fileBean.getVisibilityGroups());
		}
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
	
	public Map<String, SortedSet<DataLinkBean>> setupDataTree(
			ParticleBean particleBean, HttpServletRequest request)
			throws Exception {
		request.setAttribute("updateDataTree", "true");
//		InitSetup.getInstance()
//				.getDefaultAndOtherLookupTypes(request, "reportCategories",
//						"Report", "category", "otherCategory", true);
//		InitSetup.getInstance()
//			.getDefaultAndOtherLookupTypes(request, "publicationCategories",
//				"Publication", "category", "otherCategory", true);
//		InitSetup.getInstance()
//			.getDefaultAndOtherLookupTypes(request, "publicationStatuses",
//			"Publication", "status", "otherStatus", true);
		return InitNanoparticleSetup.getInstance().getDataTree(particleBean,
				request);
	}

	public ActionForward setupDeleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String submitType = request.getParameter("submitType");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		Map<String, SortedSet<DataLinkBean>> dataTree = setupDataTree(
				particleBean, request);
		SortedSet<DataLinkBean> dataToDelete = dataTree.get(submitType);
		request.getSession().setAttribute("actionName",
				dataToDelete.first().getDataLink());
		request.getSession().setAttribute("dataToDelete", dataToDelete);
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
		String className = InitSetup.getInstance().getObjectName(submitType,
				request.getSession().getServletContext());
		String fullClassName = ClassUtils.getFullClass(className)
				.getCanonicalName();

		String[] dataIds = (String[]) theForm.get("idsToDelete");
		NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
		ActionMessages msgs = new ActionMessages();
		for (String id : dataIds) {
			if (!checkDelete(request, msgs, id)) {
				return mapping.findForward("annotationDeleteView");
			}
			sampleService.deleteAnnotationById(fullClassName, new Long(id));
		}
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		setupDataTree(particleBean, request);
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
		LabFileBean fileBean = null;
		String serviceUrl = null;
		if (location.equals("local")) {
			fileService = new FileServiceLocalImpl();
		}
		// CQL2HQL filters out subclasses, disabled the filter
		else {
			serviceUrl = InitSetup.getInstance().getGridServiceUrl(request,
					location);
			fileService = new FileServiceRemoteImpl(serviceUrl);
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
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File dFile = new File(fileRoot + File.separator
				+ fileBean.getDomainFile().getUri());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename=\""
					+ fileBean.getDomainFile().getName()+"\"");
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

	protected NanoparticleSample[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		if (otherParticles.length == 0) {
			return null;
		}
		NanoparticleSample[] particleSamples = new NanoparticleSample[otherParticles.length];
		NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
		int i = 0;
		for (String other : otherParticles) {
			NanoparticleSample particleSample = sampleService
					.findNanoparticleSampleByName(other);
			particleSamples[i] = particleSample;
			i++;
		}
		return particleSamples;
	}

	protected boolean validateFileBean(HttpServletRequest request,
			ActionMessages msgs, LabFileBean fileBean) {

		boolean noErrors = true;
		if (fileBean==null) {
			return noErrors;
		}
		LabFile labfile = fileBean.getDomainFile();
		if (labfile.getTitle().length() == 0) {
			ActionMessage msg = new ActionMessage("errors.required",
					"file title");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			noErrors = false;
		}

		if (labfile.getType().length() == 0) {
			ActionMessage msg = new ActionMessage("errors.required",
					"file type");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			noErrors = false;
		}

		if (labfile.getUriExternal()) {
			if (fileBean.getExternalUrl() == null
					|| fileBean.getExternalUrl().trim().length() == 0) {
				ActionMessage msg = new ActionMessage("errors.required",
						"external url");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
			}
		} else{ 
			//all empty				
			if ((fileBean.getUploadedFile()==null || fileBean.getUploadedFile().toString().trim().length()==0) && 
				 (fileBean.getExternalUrl()==null || fileBean.getExternalUrl().trim().length()==0) &&
				 (fileBean.getDomainFile()==null || fileBean.getDomainFile().getName()==null)){
				ActionMessage msg = new ActionMessage("errors.required",
						"uploaded file");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
			//the case that user switch from url to upload file, but no file is selected
			}else if ((fileBean.getUploadedFile() == null			
				|| fileBean.getUploadedFile().getFileName().length() == 0) &&
				fileBean.getExternalUrl()!=null && fileBean.getExternalUrl().trim().length()>0) {					
				ActionMessage msg = new ActionMessage("errors.required",
						"uploaded file");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				this.saveErrors(request, msgs);
				noErrors = false;
			}
		}		
		return noErrors;
	}
	
	public ActionForward unspecified (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws InvalidSessionException {
		HttpSession session = request.getSession();
		if (session.isNew()) {
			throw new InvalidSessionException(
					"Session timed out.  Please start again");
		}
		return mapping.getInputForward();
	}

}
