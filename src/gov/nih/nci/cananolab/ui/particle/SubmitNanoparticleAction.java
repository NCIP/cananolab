package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up the submit a new nanoparticle sample page and submits a new nanoparticle sample
 *  
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleSampleServiceHelper;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = (ParticleBean) theForm
				.get("particleSampleBean");
		Long particleId = particleSampleBean.getDomainParticleSample().getId();
		if (particleId != null && particleId > 0) {
			PointOfContactService pointOfContactService = new PointOfContactServiceLocalImpl();
			List<PointOfContactBean> otherPointOfContactBeanList = pointOfContactService
					.findOtherPointOfContactCollection(particleId.toString());
			if (otherPointOfContactBeanList!=null && otherPointOfContactBeanList.size()>0) {
				Collection<PointOfContact> otherPointOfContactCollection = new HashSet<PointOfContact>();
				for (PointOfContactBean pocBean: otherPointOfContactBeanList) {
					otherPointOfContactCollection.add(pocBean.getDomain());
				}
				particleSampleBean
					.getDomainParticleSample()
					.setOtherPointOfContactCollection(otherPointOfContactCollection);
			}			
		}
		
		ParticleBean pocParticleBean = (ParticleBean) request.getSession()
				.getAttribute("pocParticle");
		if (pocParticleBean!=null && pocParticleBean.getDomainParticleSample()!=null) {
			Collection<PointOfContact> otherPointOfContactCollection = pocParticleBean
					.getDomainParticleSample().getOtherPointOfContactCollection();
			particleSampleBean
				.getDomainParticleSample()
				.setOtherPointOfContactCollection(otherPointOfContactCollection);
		}
		
		particleSampleBean.setupDomainParticleSample();		
		// persist in the database
		NanoparticleSampleService service = new NanoparticleSampleServiceLocalImpl();
		service.saveNanoparticleSample(particleSampleBean
				.getDomainParticleSample());
		// assign CSM visibility and associated public visibility
		// requires fully loaded particle if particle Id is not null)		
		if (particleId != null) {
			String[] visibilityGroups = particleSampleBean.getVisibilityGroups();
			ParticleBean fullyLoadedParticleBean = service
					.findFullNanoparticleSampleById(particleSampleBean
							.getDomainParticleSample().getId().toString());
			fullyLoadedParticleBean.setVisibilityGroups(visibilityGroups);
			service.assignVisibility(fullyLoadedParticleBean);
		} else {
			service.assignVisibility(particleSampleBean);
		}
		request.setAttribute("particleId", particleSampleBean
				.getDomainParticleSample().getId().toString());
		return setupUpdate(mapping, form, request, response);
	}

	private void setupLookups(HttpServletRequest request, String sampleOrg)
			throws Exception {
		InitNanoparticleSetup.getInstance()
				.getNanoparticleSamplePointOfContacts(request);
		InitSecuritySetup.getInstance().getAllVisibilityGroupsWithoutOrg(
				request, sampleOrg);
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
				.getPrimaryPointOfContact().getOrganization().getName());
		setupDataTree(particleSampleBean, request);

		// for display "back" button on the publication detail view
		String particleId = request.getParameter("particleId");
		HttpSession session = request.getSession();
		if (particleId != null && !particleId.equals("null")
				&& particleId.trim().length() > 0) {
			session.setAttribute("docParticleId", particleId);
		} else {
			session.removeAttribute("docParticleId");
			ParticleBean sessionParticleSampleBean = (ParticleBean) request
				.getSession().getAttribute("pocParticle");
			//if update the particle, remove the previous particle data from session
			String sessionParticleId = null;
			if (sessionParticleSampleBean!=null &&
					sessionParticleSampleBean.getDomainParticleSample()!=null &&
					sessionParticleSampleBean.getDomainParticleSample().getId() != null) {
				sessionParticleId = sessionParticleSampleBean
						.getDomainParticleSample().getId().toString();
			}
			if (particleSampleBean != null
					&& particleSampleBean.getDomainParticleSample() != null
					&& particleSampleBean.getDomainParticleSample().getId() != null
					&& !particleSampleBean.getDomainParticleSample()
							.getId().toString().equals(sessionParticleId)) {
				session.removeAttribute("pocParticle");
			}
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
		request.getSession().removeAttribute("pocParticle");
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

	public ActionForward fromPOC(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getSession().getAttribute("pocParticle") != null) {
			ParticleBean particleSampleBean = (ParticleBean) request
					.getSession().getAttribute("pocParticle");
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			theForm.set("particleSampleBean", particleSampleBean);
		}
		setupLookups(request, null);
		return mapping.getInputForward();
	}

	public ActionForward newPointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = (ParticleBean) (theForm
				.get("particleSampleBean"));
		request.getSession().setAttribute("pocParticle", particleSampleBean);		
		return mapping.findForward("newPointOfContact");
	}
	
	public ActionForward pointOfContactDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = (ParticleBean) (theForm
				.get("particleSampleBean"));
		Long particleId = particleSampleBean.getDomainParticleSample().getId();
		if (particleId != null && particleId > 0) {
			PointOfContactService pointOfContactService = new PointOfContactServiceLocalImpl();
			List<PointOfContactBean> otherPointOfContactBeanList = pointOfContactService
					.findOtherPointOfContactCollection(particleId.toString());
			if (otherPointOfContactBeanList!=null && otherPointOfContactBeanList.size()>0) {
				Collection<PointOfContact> otherPointOfContactCollection = new HashSet<PointOfContact>();
				for (PointOfContactBean pocBean: otherPointOfContactBeanList) {
					otherPointOfContactCollection.add(pocBean.getDomain());
				}
				particleSampleBean
					.getDomainParticleSample()
					.setOtherPointOfContactCollection(otherPointOfContactCollection);
			}			
		}
		request.getSession().setAttribute("pocParticle", particleSampleBean);		
		return mapping.findForward("pointOfContactDetailView");
	}
}
