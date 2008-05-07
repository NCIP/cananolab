package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Base action for characterization actions
 * 
 * @author pansu
 * 
 */
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
		clearForm(theForm);
		request.getSession().setAttribute("characterizationForm", theForm);
		setupParticle(theForm, request);
		// set charclass
		ServletContext appContext = request.getSession().getServletContext();
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		String submitType = request.getParameter("submitType");
		String charClass = InitSetup.getInstance().getObjectName(submitType,
				appContext);
		charBean.setClassName(charClass);
		setLookups(request, charBean);
		return mapping.getInputForward();
	}

	abstract protected void clearForm(DynaValidatorForm theForm);

	protected void setLookups(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		InitNanoparticleSetup.getInstance().setSharedDropdowns(request);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, charBean.getClassName());
		InitProtocolSetup.getInstance().getProtocolFilesByChar(request, charBean);
	}

	protected abstract CharacterizationBean getCharacterizationBean(
			DynaValidatorForm theForm, Characterization chara, UserBean user)
			throws Exception;

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		Characterization chara = prepareCharacterization(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean charBean = getCharacterizationBean(theForm, chara,
				user);
		setLookups(request, charBean);
		return mapping.getInputForward();
	}

	// used in many dispatch methods
	private Characterization prepareCharacterization(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		String charId = request.getParameter("dataId");
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		Characterization chara = charService.findCharacterizationById(charId);
		request.getSession().setAttribute("characterizationForm", theForm);
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
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
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
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);

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
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);

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
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);

		return mapping.getInputForward();
		// return mapping.getInputForward(); this gives an
		// IndexOutOfBoundException in the jsp page
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		Characterization chara = prepareCharacterization(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		getCharacterizationBean(theForm, chara, user);
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

	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		Characterization chara = prepareCharacterization(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		getCharacterizationBean(theForm, chara, user);
		return mapping.findForward("detailPrintView");
	}

	public ActionForward exportDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request);
		Characterization chara = prepareCharacterization(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean charBean = getCharacterizationBean(theForm, chara,
				user);
		String fileName = this.getExportFileName(particleBean
				.getDomainParticleSample().getName(), "detailView", charBean
				.getClassName());
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName + ".xls");
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.exportDetail(charBean, response.getOutputStream());

		return null;
	}

	private CharacterizationSummaryBean setupCharSummary(
			DynaValidatorForm theForm, HttpServletRequest request)
			throws Exception {

		String submitType = request.getParameter("submitType");
		String className = InitSetup.getInstance().getObjectName(submitType,
				request.getSession().getServletContext());
		String fullClassName = ClassUtils.getFullClass(className)
				.getCanonicalName();
		ParticleBean particleBean = setupParticle(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		CharacterizationSummaryBean charSummary = service
				.getParticleCharacterizationSummaryByClass(particleBean
						.getDomainParticleSample().getName(), fullClassName,
						user);
		request.setAttribute("charSummary", charSummary);
		return charSummary;

	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		String submitType = request.getParameter("submitType");
		setupCharSummary(theForm, request);
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl + "?page=0&particleId=" + particleId
				+ "&submitType=" + submitType + "&dispatch=printSummaryView";
		String printAllLinkURL = requestUrl + "?page=0&particleId="
				+ particleId + "&submitType=" + submitType
				+ "&dispatch=printFullSummaryView";
		request.getSession().setAttribute("printSummaryViewLinkURL",
				printLinkURL);
		request.getSession().setAttribute("printFullSummaryViewLinkURL",
				printAllLinkURL);
		return mapping.findForward("summaryView");
	}

	public ActionForward printSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupCharSummary(theForm, request);
		return mapping.findForward("summaryPrintView");
	}

	public ActionForward printFullSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupCharSummary(theForm, request);
		return mapping.findForward("summaryPrintAllView");
	}

	public ActionForward exportSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request);
		CharacterizationSummaryBean charSummaryBean = setupCharSummary(theForm,
				request);
		String fileName = getExportFileName(particleBean
				.getDomainParticleSample().getName(), "summaryView",
				charSummaryBean.getCharBeans().get(0).getClassName());
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName + ".xls");
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.exportSummary(charSummaryBean, response.getOutputStream());
		return null;
	}

	public ActionForward exportFullSummary(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request);
		CharacterizationSummaryBean charSummaryBean = setupCharSummary(theForm,
				request);
		String fileName = getExportFileName(particleBean
				.getDomainParticleSample().getName(), "summaryView",
				charSummaryBean.getCharBeans().get(0).getClassName());
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName + ".xls");

		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.exportFullSummary(charSummaryBean, response.getOutputStream());
		return null;
	}

	private String getExportFileName(String particleName, String viewType,
			String charClass) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(particleName);
		nameParts.add(charClass);
		nameParts.add(viewType);
		nameParts.add(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}
}
