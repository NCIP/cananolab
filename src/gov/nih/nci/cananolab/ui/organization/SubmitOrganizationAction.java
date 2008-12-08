package gov.nih.nci.cananolab.ui.organization;

/**
 * This class submits organization and assigns visibility  
 *  
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.dto.common.OtherOrganizationsBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.OrganizationException;
import gov.nih.nci.cananolab.service.organization.OrganizationService;
import gov.nih.nci.cananolab.service.organization.impl.OrganizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.organization.impl.OrganizationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitOrganizationAction extends BaseAnnotationAction {

	private static Logger logger = Logger
			.getLogger(SubmitOrganizationAction.class);

	/**
	 * create new organization / update organization
	 */
//	public ActionForward create(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		ActionForward forward = null;
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		String particleId = request.getParameter("particleId");
//		OrganizationBean primaryOrganization = (OrganizationBean) theForm
//				.get("orga");
////		List<OrganizationBean> otherOrganizationCollection = (List<OrganizationBean>) theForm
////				.get("otherOrganizationCollection");
//		List<OrganizationBean> otherOrganizationCollection = null;
//
//		UserBean user = (UserBean) request.getSession().getAttribute("user");
//		
//		primaryOrganization.getDomain().setCreatedBy(user.getLoginName());
//		OrganizationService service = new OrganizationServiceLocalImpl();
//		service.saveOrganization(particleId, primaryOrganization,
//				otherOrganizationCollection);
//		// assign organization visibility
//		AuthorizationService authService = new AuthorizationService(
//				CaNanoLabConstants.CSM_APP_NAME);
//		authService.assignVisibility(primaryOrganization.getDomain().getId()
//				.toString(), primaryOrganization.getVisibilityGroups());
//		// assign poc visibility
//		assignPOCVisibility(primaryOrganization, authService);
//		if (otherOrganizationCollection != null) {
//			for (OrganizationBean organizationBean : otherOrganizationCollection) {
//				// assign or ganization visibility
//				authService.assignVisibility(organizationBean.getDomain()
//						.getId().toString(), organizationBean
//						.getVisibilityGroups());
//				// assign poc visibility
//				assignPOCVisibility(organizationBean, authService);
//			}
//		}
//		// TODO: assign attribute level visibility (work with Sue)
//
//		ActionMessages msgs = new ActionMessages();
//		ActionMessage msg = new ActionMessage(
//				"message.submitOrganization.organization", primaryOrganization
//						.getDomain().getName());
//		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//		saveMessages(request, msgs);
//		forward = mapping.findForward("success");
//
//		// TODO: to verify forward page (Shuang)
//		HttpSession session = request.getSession();
//		particleId = (String) session.getAttribute("docParticleId");
//
//		if (particleId != null && particleId.length() > 0) {
//			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
//			ParticleBean particleBean = sampleService
//					.findNanoparticleSampleById(particleId);
//			particleBean.setLocation("local");
//			setupDataTree(particleBean, request);
//			forward = mapping.findForward("particleSuccess");
//		}
//		// session.removeAttribute("particleId");
//		return forward;
//	}

	
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		
		OrganizationBean primaryOrganization = 
			(OrganizationBean) theForm.get("orga");
		
		OtherOrganizationsBean otherOrganization = 
			(OtherOrganizationsBean) theForm.get("otherOrga");
		
		List<OrganizationBean> otherOrganizationCollection = 
			otherOrganization.getOrtherOrganizations();
	
		HttpSession session = request.getSession();
		session.setAttribute("primaryOrganization", primaryOrganization);
		session.setAttribute("otherOrganizationCollection", otherOrganizationCollection);
				
		//add new added organization to drop down list
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SortedSet<Organization> sampleOrganizations = 
			InitNanoparticleSetup.getInstance().getNanoparticleSampleOrganizations(
				request, user);
		sampleOrganizations.add(primaryOrganization.getDomain());
		session.setAttribute("allUserParticleOrganizations", sampleOrganizations);
		
		//set selected primary organization
		ParticleBean particleSampleBean = 
			(ParticleBean) session.getAttribute("theParticle");
		NanoparticleSample particle = null;
		if (particleSampleBean!=null) {
			particle = particleSampleBean.getDomainParticleSample();
			particleSampleBean.getDomainParticleSample().
				setPrimaryOrganization(primaryOrganization.getDomain());			
		}
//		if (particle!=null) {
//			//set organization to particle
//			primaryOrganization.getDomain().setCreatedBy(user.getLoginName());
//			particle.setPrimaryOrganization(primaryOrganization.getDomain());		
//			particle.setOtherOrganizationCollection(otherOrganizationCollection);
//		}
		
		primaryOrganization.getDomain().setCreatedBy(user.getLoginName());
		OrganizationService service = new OrganizationServiceLocalImpl();
//		service.saveOrganization(particleId, primaryOrganization,
//				otherOrganizationCollection);
		
		// assign or organization visibility
//		AuthorizationService authService = new AuthorizationService(
//				CaNanoLabConstants.CSM_APP_NAME);
//		authService.assignVisibility(primaryOrganization.getDomain().getId()
//				.toString(), primaryOrganization.getVisibilityGroups());
		
		// assign poc visibility
//		assignPOCVisibility(primaryOrganization, authService);
//		if (otherOrganizationCollection != null) {
//			for (OrganizationBean organizationBean : otherOrganizationCollection) {
//				// assign or ganization visibility
//				authService.assignVisibility(organizationBean.getDomain()
//						.getId().toString(), organizationBean
//						.getVisibilityGroups());
//				// assign poc visibility
//				assignPOCVisibility(organizationBean, authService);
//			}
//		}
		// TODO: assign attribute level visibility (work with Sue)

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.submitOrganization.organization", primaryOrganization
						.getDomain().getName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		
		return mapping.findForward("updateParticle");

	}

	
//	private void assignOrganizationToParticle(NanoparticleSample particle,
//			OrganizationBean primaryOrganization, List<OrganizationBean> otherOrganizationCollection,
//			UserBean user) {
//		primaryOrganization.getDomain().setCreatedBy(user.getLoginName());
//
//	}
	
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

	// TODO::
	/**
	 * update organization form *
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String particleId = request.getParameter("particleId");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		OrganizationService organizationService = new OrganizationServiceLocalImpl();
		OrganizationBean primaryOrganization = organizationService
				.findPrimaryOrganization(particleId);
		List<OrganizationBean> otherOrganizationCollection = organizationService
				.findOtherOrganizationCollection(particleId);
		// TODO:: shuang need to check isHidden() before showing in jsp
		setVisibility(user, primaryOrganization, true);
		if (otherOrganizationCollection!=null) {
			for (OrganizationBean organizationBean : otherOrganizationCollection) {
				setVisibility(user, organizationBean, true);
			}
		}
		theForm.set("primaryOrganization", primaryOrganization);
		theForm.set("otherOrganizationCollection", otherOrganizationCollection);
		InitPOCSetup.getInstance().setPOCDropdowns(request);

		// TODO: forward to be verified by Shuang
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
			// request.setAttribute("particleId", particleId);
		} else {
			if (pubMedId != null && pubMedId > 0) {
				forward = mapping
						.findForward("organizationSubmitPubmedOrganization");
			} else {
				forward = mapping.findForward("organizationSubmitOrganization");
			}
			// request.removeAttribute("particleId");
		}
		return forward;
	}

	// TODO: to be removed
	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// DynaValidatorForm theForm = (DynaValidatorForm) form;
		// HttpSession session = request.getSession();
		// UserBean user = (UserBean) session.getAttribute("user");
		// String organizationId = request.getParameter("fileId");
		// String location = request.getParameter("location");
		// OrganizationService organizationService = null;
		// if (location.equals("local")) {
		// organizationService = new OrganizationServiceLocalImpl();
		// } else {
		// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
		// request, location);
		// organizationService = new OrganizationServiceRemoteImpl(serviceUrl);
		// }
		// OrganizationBean organizationBean =
		// organizationService.findOrganizationById(organizationId);
		// this.checkVisibility(request, location, user, organizationBean);
		// theForm.set("file", organizationBean);
		// InitPOCSetup.getInstance().setOrganizationDropdowns(request);
		// // if particleId is available direct to particle specific page
		// String particleId = request.getParameter("particleId");
		// ActionForward forward = mapping.findForward("view");
		// if (particleId != null) {
		// forward = mapping.findForward("particleViewOrganization");
		// }
		// return forward;
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
		OrganizationBean primaryOrganization = organizationService
				.findPrimaryOrganization(particleId);
		List<OrganizationBean> otherOrganizationCollection = organizationService
				.findOtherOrganizationCollection(particleId);
		setVisibility(user, primaryOrganization, false);
		if (otherOrganizationCollection!=null) {
			for (OrganizationBean organizationBean : otherOrganizationCollection) {
				setVisibility(user, organizationBean, false);
			}
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("primaryOrganization", primaryOrganization);
		theForm.set("otherOrganizationCollection", otherOrganizationCollection);

		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
				+ "&submitType=" + submitType + "&location=" + location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);

		return mapping.findForward("organizationDetailView");
	}

	// TODO::
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
		// String organizationId = request.getParameter("organizationId");
		// OrganizationBean pubBean =
		// organizationService.findOrganizationById(organizationId);
		// checkVisibility(request, location, user, pubBean);
		// DynaValidatorForm theForm = (DynaValidatorForm) form;
		// theForm.set("file", pubBean);
		return mapping.findForward("organizationDetailPrintView");
	}

	// TODO::
	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		String particleId = (String) session.getAttribute("docParticleId");

		// //save new entered other types
		// InitPOCSetup.getInstance().setPOCDropdowns(request);
		// DynaValidatorForm theForm = (DynaValidatorForm) form;
		//		
		// OrganizationBean organizationBean = ((OrganizationBean)
		// theForm.get("file"));
		// String selectedOrganizationType =
		// ((Organization)organizationBean.getDomainFile()).getCategory();
		// if (selectedOrganizationType!=null) {
		// SortedSet<String> types = (SortedSet<String>)
		// request.getSession().getAttribute("organizationCategories");
		// for(String op: types) {
		// System.out.println("options:" + op);
		// }
		// if (types!=null) {
		// types.add(selectedOrganizationType);
		// request.getSession().setAttribute("organizationCategories", types);
		// }
		// }
		// String selectedOrganizationStatus =
		// ((Organization)organizationBean.getDomainFile()).getStatus();
		// if (selectedOrganizationStatus!=null) {
		// SortedSet<String> statuses = (SortedSet<String>)
		// request.getSession().getAttribute("organizationStatuses");
		// if (statuses!=null) {
		// statuses.add(selectedOrganizationStatus);
		// request.getSession().setAttribute("organizationStatuses", statuses);
		// }
		// }
		// Organization pub = (Organization) organizationBean.getDomainFile();
		//		
		// theForm.set("file", organizationBean);
		//
		// // if pubMedId is available, the related fields should be set to read
		// only.
		//		
		// Long pubMedId = pub.getPubMedId();
		// ActionForward forward = getReturnForward(mapping, particleId,
		// pubMedId);
		//		
		// return forward;
		return mapping.getInputForward();
	}

	// public ActionForward addAuthor(ActionMapping mapping,
	// ActionForm form, HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// DynaValidatorForm theForm = (DynaValidatorForm) form;
	// OrganizationBean pbean = (OrganizationBean) theForm
	// .get("file");
	// pbean.addAuthor();
	//
	// return mapping.getInputForward();
	// }
	//	
	public boolean loginRequired() {
		return true;
	}

	private void setVisibility(UserBean user, OrganizationBean organizationBean,
			boolean setVisibilityGroups) throws Exception {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (auth.isUserAllowed(organizationBean.getDomain().getId()
					.toString(), user)) {
				organizationBean.setHidden(false);
				if (setVisibilityGroups) {
					// get assigned visible groups
					List<String> accessibleGroups = auth.getAccessibleGroups(
							organizationBean.getDomain().getId().toString(),
							CaNanoLabConstants.CSM_READ_PRIVILEGE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					organizationBean.setVisibilityGroups(visibilityGroups);
				}
			} else {
				organizationBean.setHidden(true);
			}
		} catch (Exception e) {
			String err = "Error in setting visibility groups for organization "
					+ organizationBean.getDomain().getName();
			logger.error(err, e);
			throw new OrganizationException(err, e);
		}
	}

	private void assignPOCVisibility(OrganizationBean organizationBean,
			AuthorizationService authService) throws Exception {
		if (organizationBean.getVisibilityGroups() != null
				&& Arrays.asList(organizationBean.getVisibilityGroups())
						.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
			Organization organization = organizationBean.getDomain();
			if (organization.getPointOfContactCollection() != null) {
				for (PointOfContact poc : organization
						.getPointOfContactCollection()) {
					if (poc != null) {
						if (poc.getId() != null
								&& poc.getId().toString().trim().length() > 0) {
							authService.assignPublicVisibility(poc.getId()
									.toString());
						}
					}
				}
			}
		}
	}

	public ActionForward addPointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OrganizationBean entity = (OrganizationBean) theForm.get("orga");
		entity.addPointOfContact();

		return mapping.getInputForward();
	}
	
	public ActionForward removePointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OrganizationBean entity = (OrganizationBean) theForm.get("orga");
		entity.removePointOfContact(ind);

		return mapping.getInputForward();
	}
	
	public ActionForward addOrganization(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OtherOrganizationsBean entity = (OtherOrganizationsBean) theForm.get("otherOrga");
		entity.addOrganization();

		return mapping.getInputForward();
	}
	
	public ActionForward removeOrganization(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OtherOrganizationsBean entity = (OtherOrganizationsBean) theForm.get("otherOrga");
		entity.removeOrganization(ind);

		return mapping.getInputForward();
	}

}
