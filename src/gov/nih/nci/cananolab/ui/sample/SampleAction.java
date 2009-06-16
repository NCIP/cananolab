package gov.nih.nci.cananolab.ui.sample;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.OtherPointOfContactsBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.PointOfContactService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SampleAction extends BaseAnnotationAction {
	// logger
	private static Logger logger = Logger.getLogger(SampleAction.class);

	// SampleServiceHelper
	SampleServiceHelper helper = new SampleServiceHelper();

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		Long sampleId = sampleBean.getDomain().getId();
		if (sampleId != null && sampleId > 0) {
			PointOfContactService pointOfContactService = new PointOfContactServiceLocalImpl();
			List<PointOfContactBean> otherPointOfContactBeanList = pointOfContactService
					.findOtherPointOfContactCollection(sampleId.toString());
			if (otherPointOfContactBeanList != null
					&& otherPointOfContactBeanList.size() > 0) {
				Collection<PointOfContact> otherPointOfContactCollection = new HashSet<PointOfContact>();
				for (PointOfContactBean pocBean : otherPointOfContactBeanList) {
					otherPointOfContactCollection.add(pocBean.getDomain());
				}
				sampleBean.getDomain().setOtherPointOfContactCollection(
						otherPointOfContactCollection);
			}
		}

		SampleBean pocSampleBean = (SampleBean) request.getSession()
				.getAttribute("pocSample");
		if (pocSampleBean != null && pocSampleBean.getDomain() != null) {
			Collection<PointOfContact> otherPointOfContactCollection = pocSampleBean
					.getDomain().getOtherPointOfContactCollection();
			sampleBean.getDomain().setOtherPointOfContactCollection(
					otherPointOfContactCollection);
		}

		sampleBean.setupDomain();
		// persist in the database
		SampleService service = new SampleServiceLocalImpl();
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		service.saveSample(sampleBean, user);
		request.setAttribute("sampleId", sampleBean.getDomain().getId()
				.toString());
		request.getSession().removeAttribute("submitPOCProcessing");
		return summaryEdit(mapping, form, request, response);
	}

	private void setupLookups(HttpServletRequest request, String sampleOrg)
			throws Exception {
		InitSampleSetup.getInstance().getAllPointOfContacts(request);
		InitSecuritySetup.getInstance().getAllVisibilityGroupsWithoutOrg(
				request, sampleOrg);
	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = theForm.getString("location");
		// "setupSample()" will get the SampleBean
		SampleBean sampleBean = setupSample(theForm, request, location, false);
		theForm.set("sampleBean", sampleBean);
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		// set visibility of the POCBean
		// If Primary POC is not hidden set it in request object.
		PointOfContactBean primaryPoc = sampleBean.getPocBean();
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		pocService.retrieveVisibility(primaryPoc, user);
		if (!primaryPoc.isHidden()) {
			request.setAttribute("primaryPoc", primaryPoc);
		}

		// Set visibility of Other POC, set them in request only when as least
		// one is visible.
		String sampleId = sampleBean.getDomain().getId().toString();
		PointOfContactService pointOfContactService = new PointOfContactServiceLocalImpl();
		List<PointOfContactBean> otherPointOfContactCollection = pointOfContactService
				.findOtherPointOfContactCollection(sampleId);
		if (otherPointOfContactCollection != null
				&& !otherPointOfContactCollection.isEmpty()) {
			try {
				AuthorizationService auth = new AuthorizationService(
						Constants.CSM_APP_NAME);
				for (PointOfContactBean pocBean : otherPointOfContactCollection) {
					if (auth.checkReadPermission(user, pocBean.getDomain()
							.getId().toString())) {
						pocBean.setHidden(false);
					} else {
						otherPointOfContactCollection.remove(pocBean);
						pocBean.setHidden(true);
					}
				}
			} catch (Exception e) {
				String err = "Error in setting visibility for OtherPointOfContact.";
				logger.error(err, e);
				throw new PointOfContactException(err, e);
			}
			if (!otherPointOfContactCollection.isEmpty()) {
				OtherPointOfContactsBean otherPointOfContactsBean = new OtherPointOfContactsBean();
				otherPointOfContactsBean
						.setOtherPointOfContacts(otherPointOfContactCollection);
				request.setAttribute("otherPoc", otherPointOfContactsBean);
			}
		}

		return mapping.findForward("summaryView");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = setupSample(theForm, request, Constants.LOCAL_SITE, false);
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		PointOfContactService pocService = new PointOfContactServiceLocalImpl();
		pocService.retrieveVisibility(sampleBean.getPocBean(), user);
		theForm.set("sampleBean", sampleBean);
		setupLookups(request, sampleBean.getDomain().getPrimaryPointOfContact()
				.getOrganization().getName());
		// setupDataTree(sampleBean, request);
		return mapping.findForward("summaryEdit");
	}

	// TODO validate whether this method is needed
	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		SampleBean sampleBean = setupSample(theForm, request, location, false);
		theForm.set("sampleBean", sampleBean);
		request.getSession().setAttribute("theSample", sampleBean);
		return mapping.findForward("view");
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("pocSample");
		setupLookups(request, null);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PARTICLE);
	}

	public ActionForward fromPOC(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getSession().getAttribute("pocSample") != null) {
			SampleBean sampleBean = (SampleBean) request.getSession()
					.getAttribute("pocSample");
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			theForm.set("sampleBean", sampleBean);
		}
		setupLookups(request, null);
		return mapping.getInputForward();
	}

	public ActionForward newPointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) (theForm.get("sampleBean"));
		request.getSession().setAttribute("pocSample", sampleBean);
		return mapping.findForward("newPointOfContact");
	}

	public ActionForward pointOfContactDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) (theForm.get("sampleBean"));
		Long sampleId = sampleBean.getDomain().getId();
		if (sampleId != null && sampleId > 0) {
			PointOfContactService pointOfContactService = new PointOfContactServiceLocalImpl();
			List<PointOfContactBean> otherPointOfContactBeanList = pointOfContactService
					.findOtherPointOfContactCollection(sampleId.toString());
			if (otherPointOfContactBeanList != null
					&& otherPointOfContactBeanList.size() > 0) {
				Collection<PointOfContact> otherPointOfContactCollection = new HashSet<PointOfContact>();
				for (PointOfContactBean pocBean : otherPointOfContactBeanList) {
					otherPointOfContactCollection.add(pocBean.getDomain());
				}
				sampleBean.getDomain().setOtherPointOfContactCollection(
						otherPointOfContactCollection);
			}
		}
		request.getSession().setAttribute("pocSample", sampleBean);
		return mapping.findForward("pointOfContactDetailView");
	}
}
