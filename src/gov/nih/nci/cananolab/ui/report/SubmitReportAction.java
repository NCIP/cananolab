package gov.nih.nci.cananolab.ui.report;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */
/* CVS $Id: SubmitReportAction.java,v 1.31 2008-08-26 23:21:19 cais Exp $ */

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceLocalImpl;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.document.SubmitPublicationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.PropertyReader;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitReportAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ReportBean reportBean = (ReportBean) theForm.get("file");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		
		reportBean.setupDomainFile(CaNanoLabConstants.FOLDER_REPORT, user
				.getLoginName());
		if (!validateReportFile(request, reportBean)) {
			return mapping.getInputForward();
		}
		ReportService service = new ReportServiceLocalImpl();
		service.saveReport((Report) reportBean.getDomainFile(), reportBean
				.getParticleNames(), reportBean.getNewFileData());
		// set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(reportBean.getDomainFile().getId()
				.toString(), reportBean.getVisibilityGroups());

		InitReportSetup.getInstance().persistReportDropdowns(request,
				reportBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitReport.file",
				reportBean.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		
		HttpSession session = request.getSession();	
		String particleId = (String) session.getAttribute("docParticleId");	
//		if (particleId==null ||particleId.length()==0) {
//			Object particleIdObj = session.getAttribute("particleId");
//			if (particleIdObj!=null) {
//				particleId = particleIdObj.toString();
//				request.setAttribute("particleId", particleId);
//			}else {
//				request.removeAttribute("particleId");
//			}
//		}		
		if (particleId != null
				&& particleId.length() > 0) {
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			ParticleBean particleBean = sampleService
					.findNanoparticleSampleById(particleId);
			particleBean.setLocation("local");
			setupDataTree(particleBean, request);
			forward = mapping.findForward("particleSuccess");
		}
		session.removeAttribute("particleId");
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");		
		if (particleId != null) {
			session.setAttribute("particleId", particleId);
		}else {
			//if it is not calling from particle, remove previous set attribute if applicable
			session.removeAttribute("particleId");
		}
		InitReportSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		ActionForward forward = mapping.getInputForward();
		if (particleId != null) {
			forward = mapping.findForward("particleSubmitReport");
			session.setAttribute("docParticleId", particleId);
			request.setAttribute("particleId", particleId);
		}else {
			session.removeAttribute("docParticleId");
			request.removeAttribute("particleId");
		}		
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");	
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String reportId = request.getParameter("reportId");
		if (reportId==null) {
			reportId = request.getParameter("fileId");
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ReportService reportService = new ReportServiceLocalImpl();
		ReportBean reportBean = reportService.findReportById(reportId);
		FileService fileService = new FileServiceLocalImpl();
		fileService.retrieveVisibility(reportBean, user);
		theForm.set("file", reportBean);
		InitReportSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		ActionForward forward = mapping.getInputForward();
		if (particleId != null && particleId.length() > 0) {
			forward = mapping.findForward("particleSubmitReport");
			request.setAttribute("particleId", particleId);
			session.setAttribute("docParticleId", particleId);
		}else {
			request.removeAttribute("particleId");
			session.removeAttribute("docParticleId");
			forward = mapping.findForward("documentSubmitReport");
		}
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String reportId = request.getParameter("reportId");
		String location = request.getParameter("location");
		ReportService reportService = null;
		if (location.equals("local")) {
			reportService = new ReportServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			reportService = new ReportServiceRemoteImpl(serviceUrl);
		}
		ReportBean reportBean = reportService.findReportById(reportId);
		if (location.equals("local")) {
			// retrieve visibility
			FileService fileService = new FileServiceLocalImpl();
			LabFileBean labFileBean = new LabFileBean(reportBean.getDomainFile());
			fileService.retrieveVisibility(labFileBean, user);
			if (labFileBean.isHidden()){
				reportBean.setHidden(true);
			}else{
				reportBean.setHidden(false);
			}
		}		
		theForm.set("file", reportBean);
		InitReportSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.findForward("view");
		if (particleId != null) {
			forward = mapping.findForward("particleViewReport");
		}
		return forward;
	}
	
	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		String location = request.getParameter("location");
		ReportService reportService = null;
		if (location.equals("local")) {
			reportService = new ReportServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			reportService = new ReportServiceRemoteImpl(serviceUrl);
		}		
		String reportId = request.getParameter("reportId");
		
		ReportBean reportBean = reportService.findReportById(reportId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("file", reportBean);
		
		
		String particleId = request.getParameter("particleId");
		ActionForward forward = null;
		if(particleId == null || particleId.length() == 0) {
			forward = mapping.findForward("documentDetailView");
		} else {
			forward = mapping.findForward("particleDetailView");
		}
		
		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
				+ "&reportId=" + reportId +
				"&submitType=" + submitType + "&location="
				+ location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);
		
		return forward;
	}


	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_DOCUMENT);
	}
	
	private boolean validateReportFile(HttpServletRequest request,
			ReportBean reportBean) throws Exception {
		ActionMessages msgs = new ActionMessages();
		if (!validateFileBean(request, msgs, reportBean)) {
			return false;
		}		
		return true;
	}
	
	
	protected boolean validateFileBean(HttpServletRequest request,
			ActionMessages msgs, LabFileBean fileBean) {
		boolean noErrors = true;
		LabFile labfile = fileBean.getDomainFile();		
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
				 (fileBean.getExternalUrl()==null || fileBean.getExternalUrl().trim().length()==0)  &&
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
	
	//if report is the first record of the delete list, SubmitReportAction is called
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SubmitPublicationAction pubAction = new SubmitPublicationAction();
		return pubAction.deleteAll(mapping, form, request, response);
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		HttpSession session = request.getSession();	
		String particleId = (String) session.getAttribute("particleId");
		
		ActionForward forward = null;	
		if (particleId != null && particleId.trim().length() > 0) {
			forward = mapping.findForward("particleSubmitReport");
		}else {
			forward = mapping.findForward("documentSubmitReport");
		}
		return forward;
	}
	
	public ActionForward exportDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String location = request.getParameter("location");
		ReportService reportService = null;
		if (location.equals("local")) {
			reportService = new ReportServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			reportService = new ReportServiceRemoteImpl(serviceUrl);
		}		
		String reportId = request.getParameter("reportId");
		ReportBean reportBean = reportService.findReportById(reportId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("file", reportBean);		
		String title = reportBean.getDomainFile().getTitle();
		if (title!=null && title.length()>10) {
			title = title.substring(0,10);
		}
		String fileName = this.getExportFileName(title, 
				"detailView");
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename=\""
				+ fileName + ".xls\"");
		setReportFileFullPath(request, reportBean, location);
		ReportService service = null;
		if (location.equals("local")) {
			service = new ReportServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new ReportServiceRemoteImpl(
					serviceUrl);
		}
		service.exportDetail(reportBean, response.getOutputStream());
		return null;
	}
	
	private String getExportFileName(String titleName, String viewType) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(titleName);
		nameParts.add("Report");
		nameParts.add(viewType);
		nameParts.add(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}
	
	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		String location = request.getParameter("location");
		ReportService reportService = null;
		if (location.equals("local")) {
			reportService = new ReportServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			reportService = new ReportServiceRemoteImpl(serviceUrl);
		}		
		String reportId = request.getParameter("reportId");
		ReportBean reportBean = reportService.findReportById(reportId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("file", reportBean);
		return mapping.findForward("reportDetailPrintView");
	}
	
	private void setReportFileFullPath(HttpServletRequest request,
			ReportBean reportBean, String location) throws Exception {
		if (location.equals("local")) {
			// set file full path
			if (!reportBean.getDomainFile().getUriExternal()) {
				String fileRoot = PropertyReader.getProperty(
						CaNanoLabConstants.FILEUPLOAD_PROPERTY,
						"fileRepositoryDir");
				reportBean.setFullPath(fileRoot + File.separator
						+ reportBean.getDomainFile().getUri());
			} else {
				reportBean.setFullPath(reportBean.getDomainFile().getUri());
			}

		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);

			URL localURL = new URL(request.getRequestURL().toString());
			String actionPath = localURL.getPath();
			URL remoteUrl = new URL(serviceUrl);
			String remoteServerHostUrl = remoteUrl.getProtocol() + "://"
					+ remoteUrl.getHost() + ":" + remoteUrl.getPort();
			String remoteDownloadUrlBase = remoteServerHostUrl + actionPath
					+ "?dispatch=download&location=local&fileId=";

			String remoteDownloadUrl = remoteDownloadUrlBase
					+ reportBean.getDomainFile().getId().toString();
			reportBean.setFullPath(remoteDownloadUrl);
		}
	}
}
