package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleEntityAction.java,v 1.17 2008-04-22 17:38:34 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleEntityAction extends BaseAnnotationAction {

	/**
	 * Add or update the data to database
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NanoparticleCompositionService compositionService = new NanoparticleCompositionService();
		ParticleBean particleBean = initSetup(theForm, request);
		NanoparticleEntityBean entityBean = (NanoparticleEntityBean) theForm
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
		for (ComposingElementBean compElementBean : entityBean
				.getComposingElements()) {
			if (compElementBean.getDomainComposingElement().getId() == null) {
				compElementBean.getDomainComposingElement().setCreatedBy(
						user.getLoginName());
				compElementBean.getDomainComposingElement().setCreatedDate(now);
			}
		}

		compositionService.saveNanoparticleEntity(particleBean, entityBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addNanoparticleEntity");
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
		request.getSession().removeAttribute("nanoparticleEntityForm");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = initSetup(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		InitCompositionSetup.getInstance().setNanoparticleEntityTypes(request);
		InitCompositionSetup.getInstance().getEmulsionComposingElementTypes(
				request);
		InitCompositionSetup.getInstance().getComposingElementTypes(request);
		InitCompositionSetup.getInstance().setFunctionTypes(request);
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
		NanoparticleEntityBean entityBean = compService
				.findNanoparticleEntityBy(entityId, user);
		String entityType = InitSetup.getInstance().getDisplayName(
				entityBean.getClassName(), session.getServletContext());
		entityBean.setType(entityType);
		// set composing element function type
		for (ComposingElementBean compElementBean : entityBean
				.getComposingElements()) {
			for (FunctionBean functionBean : compElementBean
					.getInherentFunctions()) {
				String functionType = InitSetup.getInstance().getDisplayName(
						functionBean.getClassName(),
						session.getServletContext());
				functionBean.setType(functionType);
			}
		}
		theForm.set("entity", entityBean);
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		InitCompositionSetup.getInstance().setNanoparticleEntityTypes(request);
		InitCompositionSetup.getInstance().getEmulsionComposingElementTypes(
				request);
		InitCompositionSetup.getInstance().getComposingElementTypes(request);
		InitCompositionSetup.getInstance().setFunctionTypes(request);
		return mapping.getInputForward();
	}

	public ActionForward addComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanoparticleEntityBean entity = (NanoparticleEntityBean) theForm
				.get("entity");
		entity.addComposingElement();
		return mapping.getInputForward();
	}

	public ActionForward removeComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanoparticleEntityBean entity = (NanoparticleEntityBean) theForm
				.get("entity");
		entity.removeComposingElement(ind);
		return mapping.getInputForward();
	}

	public ActionForward addInherentFunction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanoparticleEntityBean entity = (NanoparticleEntityBean) theForm
				.get("entity");

		String compEleIndexStr = (String) request.getParameter("compInd");
		int compEleIndex = Integer.parseInt(compEleIndexStr);
		ComposingElementBean compElement = (ComposingElementBean) entity
				.getComposingElements().get(compEleIndex);

		compElement.addFunction();
		return mapping.getInputForward();
	}

	public ActionForward removeInherentFunction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String compEleIndexStr = (String) request.getParameter("compInd");
		int compEleIndex = Integer.parseInt(compEleIndexStr);

		String functionIndexStr = (String) request.getParameter("childCompInd");
		int functionIndex = Integer.parseInt(functionIndexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanoparticleEntityBean entity = (NanoparticleEntityBean) theForm
				.get("entity");
		ComposingElementBean compElement = (ComposingElementBean) entity
				.getComposingElements().get(compEleIndex);
		compElement.removeFunction(functionIndex);
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

	protected NanoparticleEntityBean[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		NanoparticleEntityBean entityBean = (NanoparticleEntityBean) theForm
				.get("entity");

		ParticleBean particle = (ParticleBean) theForm.get("particle");
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		NanoparticleEntityBean[] entityBeans = new NanoparticleEntityBean[otherParticles.length];
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
