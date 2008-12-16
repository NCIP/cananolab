package gov.nih.nci.cananolab.ui.common;

/**
 * This class submits PointOfContact and assigns visibility  
 *  
 * @author tanq
 */

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitPointOfContactAction extends BaseAnnotationAction {

	private static Logger logger = Logger
			.getLogger(SubmitPointOfContactAction.class);

	/**
	 * create new pointOfContact / update pointOfContact
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
				pointOfContactBean.getDomain()
						.setCreatedBy(user.getLoginName());
				otherPointOfContactCollection.add(pointOfContactBean
						.getDomain());
			}
		}
		// created_date set in service
		PointOfContactService service = new PointOfContactServiceLocalImpl();
		service.savePointOfContact(primaryPointOfContact.getDomain(),
				otherPointOfContactCollection);
		// assign primary pointOfContact visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.assignVisibility(primaryPointOfContact.getDomain().getId()
				.toString(), primaryPointOfContact.getPocVisibilityGroups());
		// TODO::: assign organization visibility
		// assignPOCVisibility(primaryPointOfContact, authService);
		if (otherPointOfContactCollection != null) {
			for (PointOfContactBean pointOfContactBean : otherPointOfContactBeanCollection) {
				// assign other pointOfContact visibility
				authService.assignVisibility(pointOfContactBean.getDomain()
						.getId().toString(), pointOfContactBean
						.getPocVisibilityGroups());
				// TODO::: assign organization visibility
				// assignPOCVisibility(pointOfContactBean, authService);
			}
		}

		/**
		 * Prepare for nanoparticle sample form
		 * 
		 */
		// add new added pointOfContact to drop down list
		SortedSet<PointOfContactBean> samplePointOfContacts = InitNanoparticleSetup
				.getInstance().getNanoparticleSamplePointOfContacts(request);
		samplePointOfContacts.add(primaryPointOfContact);
		request.getSession().setAttribute("allPointOfContacts",
				samplePointOfContacts);

		// set selected primary pointOfContact
		DynaValidatorForm particleSampleForm = (DynaValidatorForm) request
				.getSession().getAttribute("nanoparticleSampleForm");
		if (particleSampleForm != null) {
			ParticleBean particleSampleBean = (ParticleBean) particleSampleForm
					.get("particleSampleBean");
			if (particleSampleBean != null) {
				NanoparticleSample particle = particleSampleBean
						.getDomainParticleSample();

				// set selected pointOfContact				
				particle.setPrimaryPointOfContact(primaryPointOfContact
						.getDomain());
				//particleSampleBean.setPOCId(primaryPointOfContact.getDomain().getId().toString());
				// set pointOfContact to particle
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
				// TODO:need??
				particle.setPrimaryPointOfContact(primaryPointOfContact
						.getDomain());

				if (otherPointOfContactCollection != null
						&& otherPointOfContactCollection.size() > 0) {
					particleSampleBean.getDomainParticleSample()
							.setOtherPointOfContactCollection(
									otherPointOfContactCollection);
					
					for (PointOfContact otherPoc:otherPointOfContactCollection) {
						// set pointOfContact to particle
						if (otherPoc.getNanoparticleSampleCollection() != null) {
							//TODO::: test, use to fix lazy loading issue				
							PointOfContact dbOtherPoc = null;
							if (otherPoc.getId()!=null) {
								dbOtherPoc = service.loadPOCNanoparticleSample(otherPoc);
							}	
							otherPoc.setNanoparticleSampleCollection(dbOtherPoc.getNanoparticleSampleCollection());
							otherPoc.getNanoparticleSampleCollection() .add(
											particle);
						} else {
							Collection<NanoparticleSample> nanoparticleSampleCollection = new HashSet<NanoparticleSample>();
							nanoparticleSampleCollection.add(particle);
							otherPoc.setNanoparticleSampleCollection(
									nanoparticleSampleCollection);
						}
					}
					
				}
				// TODO:need??
				particle
						.setOtherPointOfContactCollection(otherPointOfContactCollection);
			}
		}

		request.getSession().setAttribute("primaryPointOfContact",
				primaryPointOfContact);
		request.getSession().setAttribute("otherPointOfContactCollection",
				otherPointOfContactCollection);

		request.getSession().removeAttribute("submitPointOfContactForm");
		forward = mapping.findForward("updateParticle");
		return forward;
	}

	/**
	 * create pointOfContact form
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
			forward = mapping.findForward("submitPointOfContact");
		}
		return forward;
	}

	// TODO::
	/**
	 * update pointOfContact form *
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String particleId = request.getParameter("particleId");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PointOfContactService pointOfContactService = new PointOfContactServiceLocalImpl();
		PointOfContactBean primaryPointOfContact = pointOfContactService
				.findPrimaryPointOfContact(particleId);
		List<PointOfContactBean> otherPointOfContactCollection = pointOfContactService
				.findOtherPointOfContactCollection(particleId);
		// TODO:: shuang need to check isHidden() before showing in jsp
		setVisibility(user, primaryPointOfContact, true);
		if (otherPointOfContactCollection != null) {
			for (PointOfContactBean pointOfContactBean : otherPointOfContactCollection) {
				setVisibility(user, pointOfContactBean, true);
			}
		}
		
		theForm.set("poc", primaryPointOfContact);
		OtherPointOfContactsBean otherPointOfContactsBean = new OtherPointOfContactsBean();
		otherPointOfContactsBean.setOtherPointOfContacts(otherPointOfContactCollection);
		theForm.set("otherPoc",otherPointOfContactsBean);
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
						.findForward("particleSubmitPubmedPointOfContact");
			} else {
				forward = mapping.findForward("particleSubmitPointOfContact");
			}
			// request.setAttribute("particleId", particleId);
		} else {
			if (pubMedId != null && pubMedId > 0) {
				forward = mapping
						.findForward("pointOfContactSubmitPubmedPointOfContact");
			} else {
				forward = mapping
						.findForward("pointOfContactSubmitPointOfContact");
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
		// String pointOfContactId = request.getParameter("fileId");
		// String location = request.getParameter("location");
		// PointOfContactService pointOfContactService = null;
		// if (location.equals("local")) {
		// pointOfContactService = new PointOfContactServiceLocalImpl();
		// } else {
		// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
		// request, location);
		// pointOfContactService = new
		// PointOfContactServiceRemoteImpl(serviceUrl);
		// }
		// PointOfContactBean pointOfContactBean =
		// pointOfContactService.findPointOfContactById(pointOfContactId);
		// this.checkVisibility(request, location, user, pointOfContactBean);
		// theForm.set("file", pointOfContactBean);
		// InitPOCSetup.getInstance().setPointOfContactDropdowns(request);
		// // if particleId is available direct to particle specific page
		// String particleId = request.getParameter("particleId");
		// ActionForward forward = mapping.findForward("view");
		// if (particleId != null) {
		// forward = mapping.findForward("particleViewPointOfContact");
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
			pointOfContactService = new PointOfContactServiceRemoteImpl(
					serviceUrl);
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
		theForm.set("poc", primaryPointOfContact);
		OtherPointOfContactsBean otherPointOfContactsBean = new OtherPointOfContactsBean();
		otherPointOfContactsBean.setOtherPointOfContacts(otherPointOfContactCollection);
		theForm.set("otherPoc",otherPointOfContactsBean);

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
			pointOfContactService = new PointOfContactServiceRemoteImpl(
					serviceUrl);
		}
		// String pointOfContactId = request.getParameter("pointOfContactId");
		// PointOfContactBean pubBean =
		// pointOfContactService.findPointOfContactById(pointOfContactId);
		// checkVisibility(request, location, user, pubBean);
		// DynaValidatorForm theForm = (DynaValidatorForm) form;
		// theForm.set("file", pubBean);
		return mapping.findForward("pointOfContactDetailPrintView");
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
		// PointOfContactBean pointOfContactBean = ((PointOfContactBean)
		// theForm.get("file"));
		// String selectedPointOfContactType =
		// ((PointOfContact)pointOfContactBean.getDomainFile()).getCategory();
		// if (selectedPointOfContactType!=null) {
		// SortedSet<String> types = (SortedSet<String>)
		// request.getSession().getAttribute("pointOfContactCategories");
		// for(String op: types) {
		// System.out.println("options:" + op);
		// }
		// if (types!=null) {
		// types.add(selectedPointOfContactType);
		// request.getSession().setAttribute("pointOfContactCategories", types);
		// }
		// }
		// String selectedPointOfContactStatus =
		// ((PointOfContact)pointOfContactBean.getDomainFile()).getStatus();
		// if (selectedPointOfContactStatus!=null) {
		// SortedSet<String> statuses = (SortedSet<String>)
		// request.getSession().getAttribute("pointOfContactStatuses");
		// if (statuses!=null) {
		// statuses.add(selectedPointOfContactStatus);
		// request.getSession().setAttribute("pointOfContactStatuses",
		// statuses);
		// }
		// }
		// PointOfContact pub = (PointOfContact)
		// pointOfContactBean.getDomainFile();
		//		
		// theForm.set("file", pointOfContactBean);
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
	// PointOfContactBean pbean = (PointOfContactBean) theForm
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
					+ pointOfContactBean.getDisplayName();
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
	}

	public ActionForward getPointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pointOfContactId = request.getParameter("pocId");
		String pocIndex = request.getParameter("pocIndex");
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		PointOfContactBean pocBean = pocService
				.findPointOfContactById(pointOfContactId);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OtherPointOfContactsBean otherPocsBean = (OtherPointOfContactsBean) theForm
				.get("otherPoc");
		otherPocsBean.setPointOfContact(pocIndex, pocBean);
		theForm.set("otherPoc", otherPocsBean);

		return mapping.getInputForward();
	}
	
	
	public ActionForward addPointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OtherPointOfContactsBean entity = (OtherPointOfContactsBean) theForm
				.get("otherPoc");
		entity.addPointOfContact();
		return mapping.getInputForward();
	}

	public ActionForward removePointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OtherPointOfContactsBean entity = (OtherPointOfContactsBean) theForm
				.get("otherPoc");
		entity.removePointOfContact(ind);

		return mapping.getInputForward();
	}

}
