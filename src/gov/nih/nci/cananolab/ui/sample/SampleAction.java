package gov.nih.nci.cananolab.ui.sample;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SampleAction extends BaseAnnotationAction {
	SampleServiceHelper helper = new SampleServiceHelper();

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) theForm
				.get("sampleBean");
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
				sampleBean.getDomain()
						.setOtherPointOfContactCollection(
								otherPointOfContactCollection);
			}
		}

		SampleBean pocSampleBean = (SampleBean) request.getSession()
				.getAttribute("pocSample");
		if (pocSampleBean != null
				&& pocSampleBean.getDomain() != null) {
			Collection<PointOfContact> otherPointOfContactCollection = pocSampleBean
					.getDomain()
					.getOtherPointOfContactCollection();
			sampleBean.getDomain()
					.setOtherPointOfContactCollection(
							otherPointOfContactCollection);
		}

		sampleBean.setupDomain();
		// persist in the database
		SampleService service = new SampleServiceLocalImpl();
		service.saveSample(sampleBean
				.getDomain());
		// assign CSM visibility and associated public visibility
		// requires fully loaded particle if particle Id is not null)
		if (sampleId != null) {
			String[] visibilityGroups = sampleBean
					.getVisibilityGroups();
			SampleBean fullyLoadedSampleBean = service
					.findFullSampleById(sampleBean
							.getDomain().getId().toString());
			fullyLoadedSampleBean.setVisibilityGroups(visibilityGroups);
			service.assignVisibility(fullyLoadedSampleBean);
		} else {
			service.assignVisibility(sampleBean);
		}
		request.setAttribute("sampleId", sampleBean
				.getDomain().getId().toString());
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
		SampleBean sampleBean = setupSample(theForm, request,
				"local");
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		// set visibility
		SampleService service = new SampleServiceLocalImpl();
		service.retrieveVisibility(sampleBean, user);
		theForm.set("sampleBean", sampleBean);
		setupLookups(request, sampleBean.getDomain()
				.getPrimaryPointOfContact().getOrganization().getName());
		// setupDataTree(sampleBean, request);
		return mapping.findForward("summaryView");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = setupSample(theForm, request,
				"local");
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		// set visibility
		SampleService service = new SampleServiceLocalImpl();
		service.retrieveVisibility(sampleBean, user);
		theForm.set("sampleBean", sampleBean);
		setupLookups(request, sampleBean.getDomain()
				.getPrimaryPointOfContact().getOrganization().getName());
		// setupDataTree(sampleBean, request);
		return mapping.findForward("summaryEdit");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		SampleBean sampleBean = setupSample(theForm, request,
				location);
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

	public boolean canUserExecute(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PARTICLE);
	}

	public ActionForward fromPOC(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getSession().getAttribute("pocSample") != null) {
			SampleBean sampleBean = (SampleBean) request
					.getSession().getAttribute("pocSample");
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
		SampleBean sampleBean = (SampleBean) (theForm
				.get("sampleBean"));
		request.getSession().setAttribute("pocSample", sampleBean);
		return mapping.findForward("newPointOfContact");
	}

	public ActionForward pointOfContactDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) (theForm
				.get("sampleBean"));
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
				sampleBean.getDomain()
						.setOtherPointOfContactCollection(
								otherPointOfContactCollection);
			}
		}
		request.getSession().setAttribute("pocSample", sampleBean);
		return mapping.findForward("pointOfContactDetailView");
	}
}
