package gov.nih.nci.cananolab.ui.report;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */
/* CVS $Id: SubmitReportAction.java,v 1.20 2008-07-31 19:20:05 cais Exp $ */

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.document.DocumentService;
import gov.nih.nci.cananolab.service.document.impl.DocumentServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceLocalImpl;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.system.client.ApplicationServiceProvider;

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
		if (request.getParameter("particleId") != null
				&& request.getParameter("particleId").length() > 0) {
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			ParticleBean particleBean = sampleService
					.findNanoparticleSampleById(request
							.getParameter("particleId"));
			setupDataTree(particleBean, request);
			forward = mapping.findForward("particleSuccess");
		}
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitReportSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.getInputForward();
		if (particleId != null) {
			forward = mapping.findForward("particleSubmitReport");
			request.setAttribute("particleId", particleId);
		}
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String reportId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ReportService reportService = new ReportServiceLocalImpl();
		ReportBean reportBean = reportService.findReportById(reportId);
		FileService fileService = new FileServiceLocalImpl();
		fileService.retrieveVisibility(reportBean, user);
		theForm.set("file", reportBean);
		InitReportSetup.getInstance().setReportDropdowns(request);
		// if particleId is available direct to particle specific page
		String particleId = request.getParameter("particleId");
		ActionForward forward = mapping.getInputForward();
		if (particleId != null) {
			forward = mapping.findForward("particleSubmitReport");
			request.setAttribute("particleId", particleId);
		}
		return forward;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String reportId = request.getParameter("fileId");
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
		
//		setupParticle(theForm, request, location);
//		Characterization chara = prepareCharacterization(theForm, request,
//				location);
//		UserBean user = (UserBean) request.getSession().getAttribute("user");
//		getCharacterizationBean(theForm, chara, user, location);
//		String particleId = request.getParameter("particleId");
//		String publicationId = request.getParameter("dataId");
//		String className = request.getParameter("dataClassName");
//		String submitType = request.getParameter("submitType");
//		String requestUrl = request.getRequestURL().toString();
//		String printLinkURL = requestUrl
//				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
//				+ "&dataId=" + publicationId + "&dataClassName="
//				+ className + "&submitType=" + submitType + "&location="
//				+ location;
//		request.getSession().setAttribute("printDetailViewLinkURL",
//				printLinkURL);
		return mapping.findForward("detailView");
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
	
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		String submitType = request.getParameter("submitType");
		String[] dataIds = (String[]) theForm.get("idsToDelete");
		DocumentService documentService = new DocumentServiceLocalImpl();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
			.getApplicationService();
		NanoparticleSample particle = (NanoparticleSample)appService.getObject(
				NanoparticleSample.class, "id", new Long(particleId));
		
		ActionMessages msgs = new ActionMessages();
		for (String id : dataIds) {
			if (!checkDelete(request, msgs, id)) {
				return mapping.findForward("annotationDeleteView");
			}
			documentService.removeDocumentFromParticle(particle, new Long(id));
		}
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		setupDataTree(particleBean, request);
		ActionMessage msg = new ActionMessage("message.deleteDocuments",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return mapping.findForward("success");
	}


}
