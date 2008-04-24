package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: FunctionalizingEntityAction.java,v 1.11 2008-04-24 22:36:51 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class FunctionalizingEntityAction extends BaseAnnotationAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanoparticleCompositionService compositionService = new NanoparticleCompositionService();
		ParticleBean particleBean = initSetup(theForm, request);
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("entity");
		entityBean.setDomainEntity();
		compositionService.saveFunctionalizingEntity(particleBean
				.getDomainParticleSample(), entityBean.getDomainEntity());
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
		InitCompositionSetup.getInstance().setFunctionalizingEntityTypes(
				request);
		InitNanoparticleSetup.getInstance().setOtherParticleNames(
				request,
				particleBean.getDomainParticleSample().getName(),
				particleBean.getDomainParticleSample().getSource()
						.getOrganizationName(), user);

		setLookups(request);
		
		return mapping.getInputForward();
	}
	
	private void setLookups(HttpServletRequest request) throws Exception {
		InitCompositionSetup.getInstance().getTargetTypes(request);
		InitCompositionSetup.getInstance().getAntibodyTypes(request);
		InitCompositionSetup.getInstance().getAntibodyIsotypes(request);
		InitCompositionSetup.getInstance().getActivationMethods(request);
		InitCompositionSetup.getInstance().setFunctionTypes(request);
		InitCompositionSetup.getInstance().getFunctionalizingEntityUnits(request);
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
		InitCompositionSetup.getInstance().setFunctionalizingEntityTypes(
				request);
		
		setLookups(request);
		
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

	public ActionForward addFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("entity");
		entity.addFile();
		return mapping.getInputForward();
	}

	public ActionForward removeFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("entity");
		entity.removeFile(ind);
		return mapping.getInputForward();
	}
	
	public ActionForward addTarget(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("entity");

		String funcIndexStr = (String) request.getParameter("compInd");
		int funcIndex = Integer.parseInt(funcIndexStr);
		FunctionBean function = (FunctionBean) entity
				.getFunctions().get(funcIndex);

		function.addTarget();
		return mapping.getInputForward();
	}

	public ActionForward removeTarget(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String funcIndexStr = (String) request.getParameter("compInd");
		int funcIndex = Integer.parseInt(funcIndexStr);

		String targetIndexStr = (String) request.getParameter("childCompInd");
		int targetIndex = Integer.parseInt(targetIndexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("entity");
		FunctionBean function = (FunctionBean) entity
						.getFunctions().get(funcIndex);
		function.removeTarget(targetIndex);
		return mapping.getInputForward();
	}
	
	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * DynaValidatorForm theForm = (DynaValidatorForm) form;
		 * FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
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

	protected FunctionalizingEntityBean[] prepareCopy(
			HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
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
}
