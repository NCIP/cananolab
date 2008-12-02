package gov.nih.nci.cananolab.ui.organization;

/**
 * This class submits organization and assigns visibility  
 *  
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.organization.OrganizationService;
import gov.nih.nci.cananolab.service.organization.impl.OrganizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.organization.impl.OrganizationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

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

public class SubmitOrganizationAction extends BaseAnnotationAction {

	/**
	 * create new organization
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;	
		
		OrganizationBean primaryOrganization = (OrganizationBean) theForm.get("primaryOrganization");
		List<OrganizationBean> otherOrganizationCollection = 
			(List<OrganizationBean>)theForm.get("otherOrganizationCollection");
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
//		organizationBean.setupDomainFile(CaNanoLabConstants.FOLDER_PUBLICATION, user
//				.getLoginName(), 0);
		primaryOrganization.getDomain().setCreatedBy(user.getLoginName());
		OrganizationService service = new OrganizationServiceLocalImpl();
		service.saveOrganization(primaryOrganization,  
				otherOrganizationCollection);
		// set visibility
//		AuthorizationService authService = new AuthorizationService(
//				CaNanoLabConstants.CSM_APP_NAME);
//		authService.assignVisibility(organizationBean.getDomain().getId()
//				.toString(), organizationBean.getVisibilityGroups());

		
		//TODO:  set poc visibility
//		if (organizationBean.getVisibilityGroups()!=null && 
//				Arrays.asList(organizationBean.getVisibilityGroups())
//					.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)){
//			if (organization.getAuthorCollection()!=null) {
//				for (Author author: organization.getAuthorCollection()) {
//					if (author!=null) {
//						if (author.getId()!=null && 
//								author.getId().toString().trim().length()>0) {
//							authService.assignPublicVisibility(author.getId().toString());
//						}
//					
//					}
//				}
//			}
//		}
//		InitPOCSetup.getInstance().persistPOCDropdowns(request,
//				organizationBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.submitOrganization.organization",
				primaryOrganization.getDomain().getName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		
		//TODO: to verify forward page (Shuang)
		HttpSession session = request.getSession();
		String particleId = (String) session.getAttribute("docParticleId");
		
		if (particleId != null
				&& particleId.length() > 0) {
			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
			ParticleBean particleBean = sampleService
					.findNanoparticleSampleById(particleId);
			particleBean.setLocation("local");
			setupDataTree(particleBean, request);
			forward = mapping.findForward("particleSuccess");
		}
		//session.removeAttribute("particleId");
		return forward;
	}

	/**
	 * create organization form
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");	
		InitPOCSetup.getInstance().setPOCDropdowns(request);			
		ActionForward forward = mapping.getInputForward();		
		if (particleId != null && !particleId.equals("null")
				&& particleId.trim().length() > 0) {
			forward = mapping.findForward("submitOrganization");
		}
		return forward;
	}
	
	//TODO::
	/**
	 * update organization form	 * 
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();	
		String particleId = request.getParameter("particleId");		
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		OrganizationService organizationService = new OrganizationServiceLocalImpl();
		OrganizationBean primaryOrganization = 
			organizationService.findPrimaryOrganization(particleId);
		List<OrganizationBean> otherOrganizationCollection = 
			organizationService.findOtherOrganizationCollection(particleId);	
		//TODO:: 
		//checkVisibility(request,  location, user,  pubBean);	
		theForm.set("primaryOrganization", primaryOrganization);	
		theForm.set("otherOrganizationCollection", otherOrganizationCollection);
		InitPOCSetup.getInstance().setPOCDropdowns(request);
		
		//TODO: forward to be verified by Shuang
		ActionForward forward = mapping.findForward("submitOrganization");
		return forward;
	}
		
	private ActionForward getReturnForward(ActionMapping mapping, 
			String particleId, Long pubMedId) {
		ActionForward forward = null;
		if (particleId != null && particleId.trim().length() > 0) {
			if (pubMedId != null && pubMedId > 0) {
				forward = mapping
						.findForward("particleSubmitPubmedOrganization");
			} else {
				forward = mapping.findForward("particleSubmitOrganization");
			}
//			request.setAttribute("particleId", particleId);
		}else {
			if (pubMedId != null && pubMedId > 0) {
				forward = mapping
						.findForward("organizationSubmitPubmedOrganization");
			} else {
				forward = mapping.findForward("organizationSubmitOrganization");
			}
//			request.removeAttribute("particleId");
		}
		return forward;
	}

	//TODO: to be removed
	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		HttpSession session = request.getSession();
//		UserBean user = (UserBean) session.getAttribute("user");
//		String organizationId = request.getParameter("fileId");
//		String location = request.getParameter("location");
//		OrganizationService organizationService = null;
//		if (location.equals("local")) {
//			organizationService = new OrganizationServiceLocalImpl();
//		} else {
//			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
//					request, location);
//			organizationService = new OrganizationServiceRemoteImpl(serviceUrl);
//		}
//		OrganizationBean organizationBean = organizationService.findOrganizationById(organizationId);
//		this.checkVisibility(request, location, user, organizationBean);		
//		theForm.set("file", organizationBean);
//		InitPOCSetup.getInstance().setOrganizationDropdowns(request);
//		// if particleId is available direct to particle specific page
//		String particleId = request.getParameter("particleId");
//		ActionForward forward = mapping.findForward("view");
//		if (particleId != null) {
//			forward = mapping.findForward("particleViewOrganization");
//		}
//		return forward;
		return null;
	}
	
	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		String location = request.getParameter("location");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		OrganizationService organizationService = null;
		if (location.equals("local")) {
			organizationService = new OrganizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			organizationService = new OrganizationServiceRemoteImpl(serviceUrl);
		}		
		String particleId = request.getParameter("particleId");
		//TODO:: 
		OrganizationBean primaryOrganization = 
			organizationService.findPrimaryOrganization(particleId);
		List<OrganizationBean> otherOrganizationCollection = 
			organizationService.findOtherOrganizationCollection(particleId);	
		//TODO:: 
		//checkVisibility(request,  location, user,  pubBean);	
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("primaryOrganization", primaryOrganization);	
		theForm.set("otherOrganizationCollection", otherOrganizationCollection);
	
		ActionForward forward = null;
		forward = mapping.findForward("organizationDetailView");
		
		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
				+ "&submitType=" + submitType + "&location="
				+ location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);
		
		return forward;
	}
	
	//TODO::
	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		String location = request.getParameter("location");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		OrganizationService organizationService = null;
		if (location.equals("local")) {
			organizationService = new OrganizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			organizationService = new OrganizationServiceRemoteImpl(serviceUrl);
		}		
//		String organizationId = request.getParameter("organizationId");
//		OrganizationBean pubBean = organizationService.findOrganizationById(organizationId);
//		checkVisibility(request, location, user, pubBean);
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		theForm.set("file", pubBean);
		return mapping.findForward("organizationDetailPrintView");
	}

	//TODO::
	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();	
		String particleId = (String) session.getAttribute("docParticleId");
		
//		//save new entered other types
//		InitPOCSetup.getInstance().setPOCDropdowns(request);
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		
//		OrganizationBean organizationBean = ((OrganizationBean) theForm.get("file"));
//		String selectedOrganizationType = ((Organization)organizationBean.getDomainFile()).getCategory();
//		if (selectedOrganizationType!=null) {
//			SortedSet<String> types = (SortedSet<String>)
//				request.getSession().getAttribute("organizationCategories");
//			for(String op: types) {
//		    	System.out.println("options:" + op);
//		    }
//			if (types!=null) {
//				types.add(selectedOrganizationType);
//				request.getSession().setAttribute("organizationCategories", types);
//			}
//		}
//		String selectedOrganizationStatus = ((Organization)organizationBean.getDomainFile()).getStatus();
//		if (selectedOrganizationStatus!=null) {
//			SortedSet<String> statuses = (SortedSet<String>)
//				request.getSession().getAttribute("organizationStatuses");
//			if (statuses!=null) {
//				statuses.add(selectedOrganizationStatus);
//				request.getSession().setAttribute("organizationStatuses", statuses);
//			}
//		}
//		Organization pub = (Organization) organizationBean.getDomainFile();
//		
//		theForm.set("file", organizationBean);	
//
//		// if pubMedId is available, the related fields should be set to read only.			
//		
//		Long pubMedId = pub.getPubMedId();
//		ActionForward forward = getReturnForward(mapping, particleId, pubMedId);
//		
//		return forward;
		return null;
	}

//	public ActionForward addAuthor(ActionMapping mapping,
//			ActionForm form, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		OrganizationBean pbean = (OrganizationBean) theForm
//				.get("file");
//		pbean.addAuthor();
//
//		return mapping.getInputForward();
//	}
//	
	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PUBLICATION);
	}
	
	public void checkVisibility(HttpServletRequest request, String location,
			UserBean user, OrganizationBean organization) throws Exception {
		//TODO::
//		if (location.equals("local")) {
//			FileService fileService = new FileServiceLocalImpl();
//			fileService.retrieveVisibility(fileBean, user);
//			if (fileBean.isHidden()) {
//				if (user != null) {
//					request.getSession().removeAttribute("user");
//					throw new NoAccessException();
//				} else {
//					throw new InvalidSessionException();
//				}
//			}
//		}
	}

}
