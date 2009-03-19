package gov.nih.nci.cananolab.ui.common;

/**
 * This class submits PointOfContact and assigns visibility  
 *  
 * @author tanq, pansu
 */

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.OtherPointOfContactsBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
		String[] primaryVisibilityGroups = primaryPointOfContact.getVisibilityGroups();
		Collection<PointOfContact> otherPointOfContactCollection = null;
		if (otherPointOfContactBeanCollection != null) {
			otherPointOfContactCollection = new HashSet<PointOfContact>();
			for (PointOfContactBean otherPocBean : otherPointOfContactBeanCollection) {
				otherPocBean.getDomain()
						.setCreatedBy(user.getLoginName());
				otherPointOfContactCollection.add(otherPocBean
						.getDomain());
			}
		}
		// created_date set in service
		PointOfContactService service = new PointOfContactServiceLocalImpl();
		service.savePointOfContact(primaryPointOfContact.getDomain(),
				otherPointOfContactCollection);
		// assign primary pointOfContact visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		authService.assignVisibility(primaryPointOfContact.getDomain().getId()
				.toString(), primaryVisibilityGroups,
				primaryPointOfContact.getOrganization().getName());

		if (otherPointOfContactCollection != null) {
			for (PointOfContactBean pointOfContactBean : otherPointOfContactBeanCollection) {
				// assign other pointOfContact visibility
				authService.assignVisibility(pointOfContactBean.getDomain()
						.getId().toString(), pointOfContactBean
						.getVisibilityGroups(), pointOfContactBean.getDomain()
						.getOrganization().getName());
			}
		}
		InitPOCSetup.getInstance().persistPOCDropdowns(request,
				primaryPointOfContact.getDomain(), otherPointOfContactCollection);

		/**
		 * Prepare for sample form
		 * 
		 */
		SampleBean sampleBean = (SampleBean) request.getSession()
				.getAttribute("pocSample");
		sampleBean.setPocBean(primaryPointOfContact);
		sampleBean.getDomain().setOtherPointOfContactCollection(otherPointOfContactCollection);
		
		//remove blue warning message
//		ActionMessages msgs = new ActionMessages();
//		ActionMessage msg = new ActionMessage(
//				"pointOfContact.updateOtherPOC");
//		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//		saveMessages(request, msgs);
		
		String sampleId = getSampleId(request);
		if (sampleId != null) {
			SampleServiceLocalImpl particleService = 
				new SampleServiceLocalImpl();
			particleService.saveOtherPOCs(sampleBean.getDomain());
			request.setAttribute("sampleId", sampleId);
			return mapping.findForward("updateSample");
		} else {
			request.getSession().setAttribute("submitPOCProcessing", "true");
			return mapping.findForward("submitSample");
		}
	}

	/**
	 * create pointOfContact form
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String sampleId = getSampleId(request);
		session.removeAttribute("submitPointOfContactForm");
		InitPOCSetup.getInstance().setPOCDropdowns(request);
		ActionForward forward = mapping.getInputForward();
		if (sampleId != null && !sampleId.equals("null")
				&& sampleId.trim().length() > 0) {
			forward = mapping.findForward("submitPointOfContact");
		}
		return forward;
	}

		
	/**
	 * update pointOfContact form *
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		PointOfContactService pointOfContactService = 
			new PointOfContactServiceLocalImpl();		
		String sampleId = getSampleId(request);
		String pocId = getPOCId(request);
		PointOfContactBean primaryPointOfContact = pointOfContactService
				.findPointOfContactById(pocId);
		List<PointOfContactBean> otherPointOfContactCollection = null;
		if (sampleId != null && sampleId.trim().length() > 0) {
			otherPointOfContactCollection = pointOfContactService
					.findOtherPointOfContactCollection(sampleId);
		}
		setVisibility(user, primaryPointOfContact, true);
		if (otherPointOfContactCollection != null) {
			for (PointOfContactBean pointOfContactBean : otherPointOfContactCollection) {
				setVisibility(user, pointOfContactBean, true);
			}
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("poc", primaryPointOfContact);
		OtherPointOfContactsBean otherPointOfContactsBean = new OtherPointOfContactsBean();
		otherPointOfContactsBean
				.setOtherPointOfContacts(otherPointOfContactCollection);
		theForm.set("otherPoc", otherPointOfContactsBean);
		InitPOCSetup.getInstance().setPOCDropdowns(request);
		
		SampleBean sessionSampleSampleBean = (SampleBean) request
		.getSession().getAttribute("pocSample");
		String sessionSampleId = null;
		if (sessionSampleSampleBean.getDomain().getId() != null) {
			sessionSampleId = sessionSampleSampleBean
					.getDomain().getId().toString();
		}
		if (sampleId != null
				&& !sampleId.equals(sessionSampleId)) {
			request.getSession().removeAttribute("pocSample");
		}
		ActionForward forward = null;
		if(sampleId == null || sampleId.trim().length() == 0) {
			forward = mapping.findForward("submitPointOfContact");
		} else {
			forward = mapping.findForward("sampleSubmitPointOfContact");
		}
		return forward;		
	}

	private String getSampleId(HttpServletRequest request) {
		String sampleId = null;
		if (request.getSession().getAttribute("pocSample") != null) {
			SampleBean sampleBean = (SampleBean) request
					.getSession().getAttribute("pocSample");
			sampleId = (sampleBean.getDomain().getId() == null) ? null
					: sampleBean.getDomain().getId()
							.toString();
		} else {
			sampleId = request.getParameter("sampleId");
		}
		return sampleId;
	}

	private String getPOCId(HttpServletRequest request) {
		String pocId = null;
		if (request.getSession().getAttribute("pocSample") != null) {
			SampleBean sampleBean = (SampleBean) request
					.getSession().getAttribute("pocSample");
			pocId = sampleBean.getPocBean().getDomain().getId()
					.toString();
		} else {
			pocId = request.getParameter("pocId");
		}
		return pocId;
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
		String sampleId = getSampleId(request);
		String pocId = getPOCId(request);
		PointOfContactBean primaryPointOfContact = pointOfContactService
				.findPointOfContactById(pocId);
		List<PointOfContactBean> otherPointOfContactCollection = null;
		if (sampleId != null && sampleId.trim().length() > 0) {
			otherPointOfContactCollection = pointOfContactService
					.findOtherPointOfContactCollection(sampleId);
		}
		setVisibility(user, primaryPointOfContact, false);
		if (otherPointOfContactCollection != null) {
			for (PointOfContactBean pointOfContactBean : otherPointOfContactCollection) {
				setVisibility(user, pointOfContactBean, false);
			}
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("poc", primaryPointOfContact);
		OtherPointOfContactsBean otherPointOfContactsBean = new OtherPointOfContactsBean();
		otherPointOfContactsBean
				.setOtherPointOfContacts(otherPointOfContactCollection);
		theForm.set("otherPoc", otherPointOfContactsBean);
		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&sampleId=" + sampleId
				+ "&submitType=" + submitType + "&location=" + location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);		
		ActionForward forward = null;
		if(sampleId == null || sampleId.length() == 0) {
			forward = mapping.findForward("detailView");
		} else {
			forward = mapping.findForward("samplePOCDetailView");
		}
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

	
	public boolean loginRequired() {
		return true;
	}

	private void setVisibility(UserBean user,
			PointOfContactBean pointOfContactBean, boolean setVisibilityGroups)
			throws Exception {
		try {
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);
			if (auth.isUserAllowed(pointOfContactBean.getDomain().getId()
					.toString(), user)) {
				pointOfContactBean.setHidden(false);
				if (setVisibilityGroups) {
					// get assigned visible groups
					List<String> accessibleGroups = auth.getAccessibleGroups(
							pointOfContactBean.getDomain().getId().toString(),
							Constants.CSM_READ_PRIVILEGE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					pointOfContactBean.setVisibilityGroups(visibilityGroups);
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
		ActionForward forward = mapping.getInputForward();
		String sampleId = getSampleId(request);
		if (sampleId != null && !sampleId.equals("null")
				&& sampleId.trim().length() > 0) {
			forward = mapping.findForward("submitPointOfContact");
		}
		persisOtherTypes(theForm, request);
		return forward;
	}

	public ActionForward addPointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		OtherPointOfContactsBean entity = (OtherPointOfContactsBean) theForm
				.get("otherPoc");
		entity.addPointOfContact();
		persisOtherTypes(theForm, request);		
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
				
		ActionForward forward = mapping.getInputForward();
		String sampleId = getSampleId(request);
		if (sampleId != null && !sampleId.equals("null")
				&& sampleId.trim().length() > 0) {
			forward = mapping.findForward("submitPointOfContact");
		}	
		persisOtherTypes(theForm, request);
		return forward;
		//return mapping.getInputForward();
	}

	public ActionForward getOrganization(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String orgName = request.getParameter("orgName");
		String orgIndex = request.getParameter("orgIndex");
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		Organization org = pocService.findOrganizationByName(orgName);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (orgIndex == null || orgIndex.trim().length() == 0) {
			PointOfContactBean pocBean = (PointOfContactBean) theForm
					.get("poc");
			pocBean.setOrganization(org);
			theForm.set("poc", pocBean);
		} else {
			OtherPointOfContactsBean otherPocsBean = (OtherPointOfContactsBean) theForm
					.get("otherPoc");
			int index = Integer.parseInt(orgIndex);
			otherPocsBean.getOtherPointOfContacts().get(index).setOrganization(
					org);
			theForm.set("otherPoc", otherPocsBean);
		}
		ActionForward forward = mapping.getInputForward();
		String sampleId = getSampleId(request);
		if (sampleId != null && !sampleId.equals("null")
				&& sampleId.trim().length() > 0) {
			forward = mapping.findForward("submitPointOfContact");
		}
		persisOtherTypes(theForm, request);
		return forward;
		//return mapping.getInputForward();
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String sampleId = getSampleId(request);
		request.getSession().removeAttribute("pocSample");
		if (sampleId != null) {
			request.setAttribute("sampleId", sampleId);
			return mapping.findForward("updateSample");
		} else {
			return mapping.findForward("submitSample");
		}
	}
	
	private void persisOtherTypes(DynaValidatorForm theForm, HttpServletRequest request) 
		throws Exception{
		PointOfContactBean primaryPointOfContactBean = (PointOfContactBean) theForm
			.get("poc");
		OtherPointOfContactsBean entity = (OtherPointOfContactsBean) theForm
				.get("otherPoc");
		PointOfContact primaryPointOfContact = null;
		if (primaryPointOfContactBean!=null) {
			primaryPointOfContact = primaryPointOfContactBean.getDomain();
		}
		Collection<PointOfContact> otherPointOfContactCollection = new ArrayList<PointOfContact>();
		if (entity!=null) {
			List<PointOfContactBean> otherPOCList = entity.getOtherPointOfContacts();
			if (otherPOCList!=null) {
				for (PointOfContactBean otherPOCBean: otherPOCList) {
					otherPointOfContactCollection.add(otherPOCBean.getDomain());
				}
			}
		}
		InitPOCSetup.getInstance().persistPOCDropdowns(request, 
				primaryPointOfContact, otherPointOfContactCollection);
	}	
}


