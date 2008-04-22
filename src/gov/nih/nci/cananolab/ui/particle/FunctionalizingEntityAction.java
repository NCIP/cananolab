package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: FunctionalizingEntityAction.java,v 1.5 2008-04-22 06:59:30 pansu Exp $ */

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Antigen;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.Antibody;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Date;
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

public class FunctionalizingEntityAction extends AbstractDispatchAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NanoparticleCompositionService compositionService = new NanoparticleCompositionService();
		ParticleBean particleBean = initSetup(theForm, request);
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("entity");
		Date now = new Date();
		if (entityBean.getDomainEntity().getId() != null
				&& entityBean.getDomainEntity().getId() == 0) {
			entityBean.getDomainEntity().setId(null);
		}
		if (entityBean.getDomainEntity().getId() == null) {
			entityBean.getDomainEntity().setCreatedBy(user.getLoginName());
			entityBean.getDomainEntity().setCreatedDate(now);
		}
		compositionService.saveFunctionalizingEntity(particleBean, entityBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addFunctionalizingEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		return forward;
	}

	public ParticleBean initSetup(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		String particleId = request.getParameter("particleId");
		if (particleId == null) {
			particleId = theForm.getString("particleId");
		}
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		NanoparticleSampleService service = new NanoparticleSampleService();
		ParticleBean particleBean = service.findNanoparticleSampleById(
				particleId, user);
		request.setAttribute("theParticle", particleBean);
		theForm.set("particleId", particleId);
		return particleBean;
	}

	/**
	 * Set up the input form for adding new nanoparticle entity
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("functionalizingEntityForm");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = initSetup(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		InitNanoparticleSetup.getInstance().setFunctionalizingEntityTypes(
				request);
		InitNanoparticleSetup.getInstance().setOtherParticleNames(
				request,
				particleBean.getParticleSample().getName(),
				particleBean.getParticleSample().getSource()
						.getOrganizationName(), user);

		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = initSetup(theForm, request);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String entityId = request.getParameter("dataId");
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityBy(entityId, user);
		String entityType = InitSetup.getInstance().getDisplayName(
				entityBean.getClassName(), session.getServletContext());
		entityBean.setType(entityType);
		// set function type
		for (FunctionBean functionBean : entityBean.getFunctions()) {
			String functionType = InitSetup.getInstance().getDisplayName(
					functionBean.getClassName(), session.getServletContext());
			functionBean.setType(functionType);
		}
		theForm.set("entity", entityBean);
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		InitNanoparticleSetup.getInstance().setFunctionalizingEntityTypes(
				request);
		InitNanoparticleSetup.getInstance().setFunctionTypes(request);
		return mapping.getInputForward();
	}

	public ActionForward addFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("entity");
		entity.addFunction();
		return mapping.getInputForward();
	}

	public ActionForward removeFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("entity");
		entity.removeFunction(ind);
		return mapping.getInputForward();
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * DynaValidatorForm theForm = (DynaValidatorForm) form;
		 * NanoparticleEntityBean entity = (NanoparticleEntityBean) theForm
		 * .get("entity"); // update editable dropdowns HttpSession session =
		 * request.getSession();
		 * InitNanoparticleSetup.getInstance().updateEditableDropdown(session,
		 * composition.getCharacterizationSource(), "characterizationSources");
		 * 
		 * PolymerBean polymer = (PolymerBean) theForm.get("polymer");
		 * updatePolymerEditable(session, polymer);
		 * 
		 * DendrimerBean dendrimer = (DendrimerBean) theForm.get("dendrimer");
		 * updateDendrimerEditable(session, dendrimer);
		 */
		return mapping.findForward("setup");
	}

	protected FunctionalizingEntityBean[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("entity");

		ParticleBean particle = (ParticleBean) theForm.get("particle");
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		FunctionalizingEntityBean[] entityBeans = new FunctionalizingEntityBean[otherParticles.length];
		if (otherParticles.length == 0) {
			return entityBeans;
		}
		// retrieve file contents

		// FileService fileService = new FileService();
		// for (DerivedBioAssayDataBean file : entityBean.getFiles()) {
		// byte[] content = fileService.getFileContent(new Long(file.getId()));
		// file.setFileContent(content);
		// }
		//
		// NanoparticleSampleService service = new NanoparticleSampleService();
		// UserBean user = (UserBean) request.getSession().getAttribute("user");
		// int i = 0;
		// for (String particleName : otherParticles) {
		// NanoparticleEntityBean newEntityBean = entityBean.copy();
		// // overwrite particle
		// ParticleBean otherParticle = service.findNanoparticleSampleByName(
		// particleName, user);
		// newrBean.setParticle(otherParticle);
		// // reset view title
		// String timeStamp = StringUtils.convertDateToString(new Date(),
		// "MMddyyHHmmssSSS");
		// String autoTitle =
		// CaNanoLabConstants.AUTO_COPY_CHARACTERIZATION_VIEW_TITLE_PREFIX
		// + timeStamp;
		//
		// newCharBean.setViewTitle(autoTitle);
		// List<DerivedBioAssayDataBean> dataList = newCharBean
		// .getDerivedBioAssayDataList();
		// // replace particleName in path and uri with new particleName
		// for (DerivedBioAssayDataBean derivedBioAssayData : dataList) {
		// String origUri = derivedBioAssayData.getUri();
		// if (origUri != null)
		// derivedBioAssayData.setUri(origUri.replace(particle
		// .getSampleName(), particleName));
		// }
		// charBeans[i] = newCharBean;
		// i++;
		// }
		return entityBeans;
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}
