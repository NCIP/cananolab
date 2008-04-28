package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.PropertyReader;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
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
		setupParticle(theForm, request);
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

	protected abstract CharacterizationBean setCharacterizationBean(
			DynaValidatorForm theForm, Characterization chara, UserBean user)
			throws Exception;

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		Characterization chara = prepareCharacterization(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		setCharacterizationBean(theForm, chara, user);
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

	/**
	 * Download action to handle characterization file download and viewing
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		FileService service = new FileService();
		LabFileBean fileBean = service.findFile(fileId, user);
		String fileRoot = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File dFile = new File(fileRoot + File.separator
				+ fileBean.getDomainFile().getUri());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileBean.getDomainFile().getName());
			response.setHeader("cache-control", "Private");

			java.io.InputStream in = new FileInputStream(dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];

			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"error.noCharacterizationFile");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			return mapping.findForward("particleMessage");
		}
		return null;
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		Characterization chara = prepareCharacterization(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		setCharacterizationBean(theForm, chara, user);
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
		setCharacterizationBean(theForm, chara, user);
		return mapping.findForward("detailPrintView");
	}

	public ActionForward exportDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request);
		Characterization chara = prepareCharacterization(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean charBean = setCharacterizationBean(theForm, chara,
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
