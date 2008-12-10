package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up the submit a new nanoparticle sample page and submits a new nanoparticle sample
 *  
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleSampleServiceHelper;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitNanoparticleAction extends BaseAnnotationAction {
	NanoparticleSampleServiceHelper helper = new NanoparticleSampleServiceHelper();

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = (ParticleBean) theForm
				.get("particleSampleBean");
		particleSampleBean.setupDomainParticleSample();

//		OrganizationBean primaryOrganization = (OrganizationBean) request
//				.getSession().getAttribute("primaryOrganization");
//		HashSet<Organization> otherOrganizationCollection = (HashSet<Organization>) request
//			.getSession().getAttribute("otherOrganizationCollection");
//		
		/**
		 * relationship with Organization
		 */
		// set selected organization
//		NanoparticleSample particle = particleSampleBean.getDomainParticleSample();
//		particle
//				.setPrimaryOrganization(primaryOrganization.getDomain());
//		// set organization to particle
//		if (primaryOrganization.getDomain()
//				.getPrimaryNanoparticleSampleCollection() != null) {
//			primaryOrganization.getDomain()
//					.getPrimaryNanoparticleSampleCollection().add(
//							particle);					
//		} else {
//			Collection<NanoparticleSample> primaryNanoparticleSampleCollection = new HashSet<NanoparticleSample>();
//			primaryNanoparticleSampleCollection.add(particle);
//			primaryOrganization.getDomain()
//					.setPrimaryNanoparticleSampleCollection(
//							primaryNanoparticleSampleCollection);				
//		}
//		//TODO:need??
//		particle.setPrimaryOrganization(primaryOrganization.getDomain());

//		if (otherOrganizationCollection != null
//				&& otherOrganizationCollection.size() > 0) {
//			particleSampleBean.getDomainParticleSample()
//					.setOtherOrganizationCollection(
//							otherOrganizationCollection);					
//		}
//		particle
//				.setOtherOrganizationCollection(otherOrganizationCollection);
		
		//end of relationship with organization
		
		
		// persist in the database
		String newPrimaryPointOfContactName = particleSampleBean.getPOCName();
		NanoparticleSampleService service = new NanoparticleSampleServiceLocalImpl();
		service.saveNanoparticleSample(particleSampleBean
				.getDomainParticleSample());
		

		// set CSM visibility
		// add sample source as a new CSM_GROUP and assign the sample to
		// the new group
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		String[] visibleGroups = new String[particleSampleBean
				.getVisibilityGroups().length + 1];
		for (int i = 0; i < visibleGroups.length - 1; i++) {
			visibleGroups[i] = particleSampleBean.getVisibilityGroups()[i];
		}
		//TODO: to be verified
		visibleGroups[visibleGroups.length - 1] = newPrimaryPointOfContactName;

		particleSampleBean = service
				.findFullNanoparticleSampleById(particleSampleBean
						.getDomainParticleSample().getId().toString());
		particleSampleBean.setVisibilityGroups(visibleGroups);

		authService.assignVisibility(particleSampleBean
				.getDomainParticleSample().getName(), visibleGroups);
		// includes remove & assign public visibility
		service.assignAssociatedPublicVisibility(authService,
				particleSampleBean, visibleGroups);
		
		particleSampleBean.setLocation("local");
		theForm.set("particleSampleBean", particleSampleBean);
		forward = mapping.findForward("update");
		request.setAttribute("theParticle", particleSampleBean);
		setupLookups(request, particleSampleBean.getDomainParticleSample()
			.getPrimaryPointOfContact().getLastName());
		setupDataTree(particleSampleBean, request);
		setupLookups(request, particleSampleBean.getDomainParticleSample()
				.getPrimaryPointOfContact().getLastName());
		return forward;
	}

	private void setupLookups(HttpServletRequest request, String sampleSource)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitNanoparticleSetup.getInstance().getNanoparticleSamplePointOfContacts(
				request, user);
		InitSecuritySetup.getInstance().getAllVisibilityGroupsWithoutSource(
				request, sampleSource);
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = setupParticle(theForm, request,
				"local");
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		// set visibility
		NanoparticleSampleService service = new NanoparticleSampleServiceLocalImpl();
		service.retrieveVisibility(particleSampleBean, user);
		theForm.set("particleSampleBean", particleSampleBean);
		setupLookups(request, particleSampleBean.getDomainParticleSample()
				.getPrimaryPointOfContact().getLastName());
		setupDataTree(particleSampleBean, request);

		// for display "back" button on the publication detail view
		String particleId = request.getParameter("particleId");
		HttpSession session = request.getSession();
		if (particleId != null && !particleId.equals("null")
				&& particleId.trim().length() > 0) {
			session.setAttribute("docParticleId", particleId);
		} else {
			session.removeAttribute("docParticleId");
		}

		return mapping.findForward("update");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		ParticleBean particleSampleBean = setupParticle(theForm, request,
				location);
		theForm.set("particleSampleBean", particleSampleBean);
		setupDataTree(particleSampleBean, request);

		// for display "back" button on the publication detail view
		String particleId = request.getParameter("particleId");
		HttpSession session = request.getSession();
		if (particleId != null && !particleId.equals("null")
				&& particleId.trim().length() > 0) {
			session.setAttribute("docParticleId", particleId);
		} else {
			session.removeAttribute("docParticleId");
		}

		return mapping.findForward("view");
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("nanoparticleSampleForm");
		setupLookups(request, null);

		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}

	public ActionForward setupPointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// setupLookups(request, null);

		return mapping.findForward("pointOfContact");
	}
	
	public ActionForward pointOfContactDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// setupLookups(request, null);

		return mapping.findForward("pointOfContactDetailView");
	}
}
