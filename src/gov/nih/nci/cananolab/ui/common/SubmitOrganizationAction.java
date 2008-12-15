package gov.nih.nci.cananolab.ui.common;

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
import gov.nih.nci.cananolab.dto.common.OtherPointOfContactsBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitOrganizationAction extends BaseAnnotationAction {

//	private static Logger logger = Logger
//			.getLogger(SubmitPointOfContactAction.class);

	/**
	 * create new organization / update organization
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PointOfContactBean primaryPointOfContact = (PointOfContactBean) theForm
				.get("poc");
		OtherPointOfContactsBean otherPointOfContactsBean = (OtherPointOfContactsBean) theForm
				.get("otherPoc");
		List<PointOfContactBean> otherPointOfContactBeanCollection = null;
		if (otherPointOfContactsBean != null) {
			otherPointOfContactBeanCollection = otherPointOfContactsBean
				.getOtherPointOfContacts();
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		primaryPointOfContact.getDomain().setCreatedBy(user.getLoginName());
		Collection<PointOfContact> otherPointOfContactCollection = null;
		if (otherPointOfContactBeanCollection != null) {
			otherPointOfContactCollection = new HashSet<PointOfContact>();
			for (PointOfContactBean pointOfContactBean : otherPointOfContactBeanCollection) {
				pointOfContactBean.getDomain().setCreatedBy(user.getLoginName());
				otherPointOfContactCollection.add(pointOfContactBean.getDomain());
			}
		}
		
		//TODO::: 111
//		// created_date set in service
//		OrganizationService service = new OrganizationServiceLocalImpl();
//		service.saveOrganization(primaryOrganization.getDomain(),
//				otherOrganizationCollection);
//		// assign primary organization visibility
//		AuthorizationService authService = new AuthorizationService(
//				CaNanoLabConstants.CSM_APP_NAME);
//		authService.assignVisibility(primaryOrganization.getDomain().getId()
//				.toString(), primaryOrganization.getVisibilityGroups());
//		// assign primary organization's poc visibility
//		assignPOCVisibility(primaryOrganization, authService);
//		if (otherOrganizationCollection != null) {
//			for (OrganizationBean organizationBean : otherOrganizationBeanCollection) {
//				// assign other organization visibility
//				authService.assignVisibility(organizationBean.getDomain()
//						.getId().toString(), organizationBean
//						.getVisibilityGroups());
//				// assign other organization's poc visibility
//				assignPOCVisibility(organizationBean, authService);
//			}
//		}

		/**
		 * Prepare for nanoparticle sample form
		 * 
		 */
		// add new added organization to drop down list
		SortedSet<PointOfContactBean> samplePointOfContacts = InitNanoparticleSetup
				.getInstance().getNanoparticleSamplePointOfContacts(request);
		samplePointOfContacts.add(primaryPointOfContact);
		request.getSession().setAttribute("allPointOfContacts",
				samplePointOfContacts);

		// set selected primary organization
		DynaValidatorForm particleSampleForm = (DynaValidatorForm) request
				.getSession().getAttribute("nanoparticleSampleForm");
		if (particleSampleForm != null) {
			ParticleBean particleSampleBean = (ParticleBean) particleSampleForm
					.get("particleSampleBean");
			if (particleSampleBean != null) {
				NanoparticleSample particle = particleSampleBean
						.getDomainParticleSample();

				// set selected organization
				particle
						.setPrimaryPointOfContact(primaryPointOfContact.getDomain());
				// set organization to particle
				if (primaryPointOfContact.getDomain()
						.getPrimaryNanoparticleSampleCollection() != null) {
					primaryPointOfContact.getDomain()
							.getPrimaryNanoparticleSampleCollection().add(
									particle);					
				} else {
					Collection<NanoparticleSample> primaryNanoparticleSampleCollection = new HashSet<NanoparticleSample>();
					primaryNanoparticleSampleCollection.add(particle);
					primaryPointOfContact.getDomain()
							.setPrimaryNanoparticleSampleCollection(
									primaryNanoparticleSampleCollection);				
				}
				//TODO:need??
				particle.setPrimaryPointOfContact(primaryPointOfContact.getDomain());

				if (otherPointOfContactCollection != null
						&& otherPointOfContactCollection.size() > 0) {
					particleSampleBean.getDomainParticleSample()
							.setOtherPointOfContactCollection(
									otherPointOfContactCollection);					
				}
				particle
						.setOtherPointOfContactCollection(otherPointOfContactCollection);
			}
		}

		request.getSession().setAttribute("primaryPointOfContact",
				primaryPointOfContact);
		request.getSession().setAttribute("otherPointOfContactCollection",
				otherPointOfContactCollection);

		request.getSession().removeAttribute("submitOrganizationForm");
		forward = mapping.findForward("updateParticle");
		return forward;
	}

	public ActionForward create2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		PointOfContactBean primaryPointOfContact = (PointOfContactBean) theForm
				.get("orga");
		// TODO::
		// List<OrganizationBean> otherOrganizationCollection =
		// (List<OrganizationBean>) theForm
		// .get("otherOrganizationCollection");
		List<OrganizationBean> otherOrganizationCollection = null;

		request.getSession().setAttribute("primaryPointOfContact",
				primaryPointOfContact);
		request.getSession().setAttribute("otherPointOfContactCollection",
				otherOrganizationCollection);

		// add new added organization to drop down list		
		SortedSet<PointOfContactBean> samplePointOfContacts = InitNanoparticleSetup
				.getInstance()
				.getNanoparticleSamplePointOfContacts(request);
		samplePointOfContacts.add(primaryPointOfContact);
		request.getSession().setAttribute("allPointOfContacts",
				samplePointOfContacts);
		// set selected primary organization
		ParticleBean particleSampleBean = (ParticleBean) request.getSession()
				.getAttribute("theParticle");
		NanoparticleSample particle = null;
		if (particleSampleBean != null) {
			particle = particleSampleBean.getDomainParticleSample();
			particleSampleBean.getDomainParticleSample()
					.setPrimaryPointOfContact(primaryPointOfContact.getDomain());
		}
		// if (particle!=null) {
		// //set organization to particle
		// primaryOrganization.getDomain().setCreatedBy(user.getLoginName());
		// particle.setPrimaryOrganization(primaryOrganization.getDomain());
		// particle.setOtherOrganizationCollection(otherOrganizationCollection);
		// }
		forward = mapping.findForward("updateParticle");

		return forward;
	}

	// private void assignOrganizationToParticle(NanoparticleSample particle,
	// OrganizationBean primaryOrganization, List<OrganizationBean>
	// otherOrganizationCollection,
	// UserBean user) {
	// primaryOrganization.getDomain().setCreatedBy(user.getLoginName());
	//
	// }

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
		PointOfContactService pointOfContactService = new PointOfContactServiceLocalImpl();
		PointOfContactBean primaryPointOfContactBean = pointOfContactService
				.findPrimaryPointOfContact(particleId);
		List<PointOfContactBean> otherPointOfContactBeanCollection = pointOfContactService
				.findOtherPointOfContactCollection(particleId);
		// TODO:: shuang need to check isHidden() before showing in jsp
		setVisibility(user, primaryPointOfContactBean, true);
		if (otherPointOfContactBeanCollection != null) {
			for (PointOfContactBean pointOfContactBean : otherPointOfContactBeanCollection) {
				setVisibility(user, pointOfContactBean, true);
			}
		}
		theForm.set("primaryPointOfContact", primaryPointOfContactBean);
		theForm.set("otherPointOfContactCollection", otherPointOfContactBeanCollection);
		InitPOCSetup.getInstance().setPOCDropdowns(request);

		// TODO: forward to be verified by Shuang
		ActionForward forward = mapping.findForward("submitPointOfContact");
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
		PointOfContactService pointOfContactService = null;
		if (location.equals("local")) {
			pointOfContactService = new PointOfContactServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			pointOfContactService = new PointOfContactServiceRemoteImpl(serviceUrl);
		}
		String particleId = request.getParameter("particleId");
		PointOfContactBean primaryPointOfContact = pointOfContactService
				.findPrimaryPointOfContact(particleId);
		List<PointOfContactBean> otherPointOfContactCollection = pointOfContactService
				.findOtherPointOfContactCollection(particleId);
		setVisibility(user, primaryPointOfContact, false);
		if (otherPointOfContactCollection != null) {
			for (PointOfContactBean pointOfContactBean : otherPointOfContactCollection) {
				setVisibility(user, pointOfContactBean, false);
			}
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("primaryPointOfContact", primaryPointOfContact);
		theForm.set("otherPointOfContactCollection", otherPointOfContactCollection);

		ActionForward forward = null;
		forward = mapping.findForward("pointOfContactDetailView");

		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
				+ "&submitType=" + submitType + "&location=" + location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);

		return forward;
	}

	// TODO::
	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String location = request.getParameter("location");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PointOfContactService pointOfContactService = null;
		if (location.equals("local")) {
			pointOfContactService = new PointOfContactServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			pointOfContactService = new PointOfContactServiceRemoteImpl(serviceUrl);
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

	private void setVisibility(UserBean user,
			PointOfContactBean pointOfContactBean, boolean setVisibilityGroups)
			throws Exception {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (auth.isUserAllowed(pointOfContactBean.getDomain().getId()
					.toString(), user)) {
				pointOfContactBean.setHidden(false);
				if (setVisibilityGroups) {
					// get assigned visible groups
					List<String> accessibleGroups = auth.getAccessibleGroups(
							pointOfContactBean.getDomain().getId().toString(),
							CaNanoLabConstants.CSM_READ_PRIVILEGE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					pointOfContactBean.setPocVisibilityGroups(visibilityGroups);
				}
			} else {
				pointOfContactBean.setHidden(true);
			}
		} catch (Exception e) {
			String err = "Error in setting visibility groups for pointOfContact "
					+ pointOfContactBean.getDomain().getLastName();
//			logger.error(err, e);
			throw new PointOfContactException(err, e);
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
		OtherOrganizationsBean entity = (OtherOrganizationsBean) theForm
				.get("otherOrga");
		entity.addOrganization();

		return mapping.getInputForward();
	}

	public ActionForward removeOrganization(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OtherOrganizationsBean entity = (OtherOrganizationsBean) theForm
				.get("otherOrga");
		entity.removeOrganization(ind);

		return mapping.getInputForward();
	}

}
