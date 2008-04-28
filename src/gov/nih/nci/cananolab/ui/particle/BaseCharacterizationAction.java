package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public abstract class BaseCharacterizationAction extends BaseAnnotationAction {
	/**
	 * Set up the input form for adding new characterization
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
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		request.getSession().removeAttribute("characterizationForm");
		ParticleBean particleBean = setupParticle(theForm, request);
		ServletContext appContext = request.getSession().getServletContext();
		String submitType = request.getParameter("submitType");
		String charClass = InitSetup.getInstance().getObjectName(submitType,
				appContext);
		setLookups(request, charClass);
		return mapping.getInputForward();
	}

	protected void setLookups(HttpServletRequest request, String charClass)
			throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		request.getSession().setAttribute("charClass", charClass);
		InitSetup.getInstance().setSharedDropdowns(appContext);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, charClass);
		InitNanoparticleSetup.getInstance().getFileTypes(request);
	}

	protected abstract void setCharacterizationBean(DynaValidatorForm theForm,
			Characterization chara) throws Exception;

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		Characterization chara = prepareCharacterization(theForm, request);
		setCharacterizationBean(theForm, chara);
		String className = ClassUtils.getShortClassName(chara.getClass()
				.getCanonicalName());
		setLookups(request, className);
		return mapping.getInputForward();
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	protected Characterization prepareCharacterization(
			DynaValidatorForm theForm, HttpServletRequest request)
			throws Exception {
		ParticleBean particleBean = setupParticle(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String charId = request.getParameter("dataId");
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		Characterization chara = charService.findCharacterizationById(charId);
		return chara;
	}

	public ActionForward addDerivedBioAssayData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.addDerivedBioAssayData();
		return mapping.getInputForward();
	}

	public ActionForward removeDerivedBioAssayData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		String fileIndexStr = (String) request.getParameter("compInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.removeDerivedBioAssayData(fileInd);
		return mapping.getInputForward();
	}

	public ActionForward addDerivedDatum(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("compInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		DerivedBioAssayDataBean derivedBioAssayData = achar
				.getDerivedBioAssayDataList().get(fileInd);
		derivedBioAssayData.addDerivedDatum();
		return mapping.getInputForward();
	}

	public ActionForward removeDerivedDatum(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("compInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		String dataIndexStr = (String) request.getParameter("childCompInd");
		int dataInd = Integer.parseInt(dataIndexStr);
		DerivedBioAssayDataBean derivedBioAssayData = achar
				.getDerivedBioAssayDataList().get(fileInd);
		derivedBioAssayData.removeDerivedDatum(dataInd);
		return mapping.getInputForward();
		// return mapping.getInputForward(); this gives an
		// IndexOutOfBoundException in the jsp page
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		Characterization chara = prepareCharacterization(theForm, request);
		setCharacterizationBean(theForm, chara);
		String particleId = request.getParameter("particleId");
		String characterizationId = request.getParameter("dataId");
		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
				+ "&dataId=" + characterizationId + "&submitType=" + submitType;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);
		return mapping.findForward("detailView");
	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		String submitType = request.getParameter("submitType");
		String className = InitSetup.getInstance().getObjectName(submitType,
				request.getSession().getServletContext());
		String fullClassName = ClassUtils.getFullClass(className)
				.getCanonicalName();
		String requestUrl = request.getRequestURL().toString();
		ParticleBean particleBean = setupParticle(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		CharacterizationSummaryBean charSummary = service
				.getParticleCharacterizationSummaryByClass(particleBean
						.getDomainParticleSample().getName(), fullClassName,
						user);

		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printSummaryView&particleId=" + particleId
				+ "&submitType=" + submitType;
		String printAllLinkURL = requestUrl
				+ ".do?page=0&dispatch=printFullSummaryView&particleId="
				+ particleId + "&submitType=" + submitType;
		request.getSession().setAttribute("printSummaryViewLinkURL",
				printLinkURL);
		request.getSession().setAttribute("printFullSummaryViewLinkURL",
				printAllLinkURL);
		request.getSession().setAttribute("charSummary", charSummary);
		return mapping.findForward("summaryView");
	}
}
