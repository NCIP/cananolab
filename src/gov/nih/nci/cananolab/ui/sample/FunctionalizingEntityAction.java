package gov.nih.nci.cananolab.ui.sample;

/**
 * This class allows users to submit functionalizing entity data under sample composition.
 *
 * @author pansu
 */

/* CVS $Id: FunctionalizingEntityAction.java,v 1.45 2008-09-12 20:09:52 tanq Exp $ */

import java.util.List;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

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
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		if (!validateTargets(request, entityBean)) {
			return mapping.getInputForward();
		}

		if (!validateEntityFile(request, entityBean)) {
			return mapping.getInputForward();
		}

		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entityBean);

		this.saveEntity(request, theForm, entityBean);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addFunctionalizingEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect functionalizing entity after returning to the summary
		// page
		request.getSession().setAttribute("tab", "2");
		return mapping.findForward("success");
	}

	private boolean validateTargets(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) throws Exception {
		for (FunctionBean funcBean : entityBean.getFunctions()) {
			if ("TargetingFunction".equals(funcBean.getClassName())) {
				for (TargetBean targetBean : funcBean.getTargets()) {
					if (targetBean.getType() == null
							|| targetBean.getType().trim().length() == 0) {

						ActionMessages msgs = new ActionMessages();
						ActionMessage msg = new ActionMessage(
								"errors.required", "Target type");
						msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
						this.saveErrors(request, msgs);

						return false;
					}
					if (targetBean.getName() == null
							|| targetBean.getName().trim().length() == 0) {

						ActionMessages msgs = new ActionMessages();
						ActionMessage msg = new ActionMessage(
								"errors.required", "Target name");
						msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
						this.saveErrors(request, msgs);

						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean validateEntityFile(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) throws Exception {
		ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
			if (!validateFileBean(request, msgs, filebean)) {
				return false;
			}
		}
		return true;
	}

	private void saveEntity(HttpServletRequest request,
			DynaValidatorForm theForm, FunctionalizingEntityBean entityBean)
			throws Exception {
		CompositionService compositionService = new CompositionServiceLocalImpl();
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		try {
			entityBean.setupDomainEntity(user.getLoginName());
		} catch (ClassCastException ex) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = null;
			if (!StringUtils.isEmpty(ex.getMessage())
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msg = new ActionMessage("errors.invalidOtherType", ex
						.getMessage(), "Function");
			} else {
				msg = new ActionMessage("errors.invalidOtherType", entityBean
						.getType(), "functionalizing entity");
				entityBean.setType(null);
			}
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
		}

		compositionService.saveFunctionalizingEntity(sampleBean, entityBean,
				user);
		// save to other samples (only when user click [Submit] button.)
		String dispatch = (String) theForm.get("dispatch");
		if ("create".equals(dispatch)) {
			SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
					sampleBean);
			if (otherSampleBeans != null) {
				compositionService.copyAndSaveFunctionalizingEntity(entityBean,
						sampleBean, otherSampleBeans, user);
			}
		}
	}

	/**
	 * Set up the input form for adding new nanomaterial entity
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		request.getSession().removeAttribute("compositionForm");
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		request.getSession().setAttribute("onloadJavascript",
				"setEntityInclude('feType', 'functionalizingEntity');");
		this.checkOpenForms(entityBean, request);
		// clear copy to otherSamples
		theForm.set("otherSamples", new String[0]);

		return mapping.findForward("inputForm");
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitCompositionSetup.getInstance().setFunctionalizingEntityDropdowns(
				request);
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String entityId = request.getParameter("dataId");
		String location = theForm.getString(Constants.LOCATION);
		if (entityId == null) {
			entityId = (String) request.getAttribute("dataId");
		}
		CompositionService compService = new CompositionServiceLocalImpl();
		if (Constants.LOCAL_SITE.equals(location)) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			compService = new CompositionServiceRemoteImpl(serviceUrl);
		}
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId, user);
		request.setAttribute("functionalizingEntity", entityBean);
		String detailPage = null;
		if (entityBean.isWithProperties()) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getClassName(), "FunctionalizingEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
		return mapping.findForward("singleSummaryView");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		String sampleId = theForm.getString("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		String entityId = request.getParameter("dataId");
		if (entityId == null) {
			entityId = (String) request.getAttribute("dataId");
		}

		UserBean user = (UserBean) session.getAttribute("user");
		CompositionService compService = new CompositionServiceLocalImpl();
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId, user);
		theForm.set("functionalizingEntity", entityBean);
		this.setLookups(request);
		// clear copy to otherSamples
		theForm.set("otherSamples", new String[0]);
		this.checkOpenForms(entityBean, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward saveFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FunctionBean function = entity.getTheFunction();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		function.setupDomainFunction(user.getLoginName(), 0);
		entity.addFunction(function);
		this.saveEntity(request, theForm, entity);
		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		// return to setupUpdate to get the correct entity from database
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward removeFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FunctionBean function = entity.getTheFunction();
		entity.removeFunction(function);
		this.saveEntity(request, theForm, entity);
		checkOpenForms(entity, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward saveFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FileBean theFile = entity.getTheFile();
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/"
				+ "functionalizingEntity";
		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		entity.addFile(theFile);

		// restore previously uploaded file from session.
		restoreUploadedFile(request, theFile);

		// save the functionalizing entity
		this.saveEntity(request, theForm, entity);

		request.setAttribute("anchor", "file");
		this.checkOpenForms(entity, request);

		return mapping.findForward("inputForm");
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FileBean theFile = entity.getTheFile();
		entity.removeFile(theFile);
		request.setAttribute("anchor", "file");
		// save the functionalizing entity
		this.saveEntity(request, theForm, entity);
		checkOpenForms(entity, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = entity.getTheFile();
		preserveUploadedFile(request, theFile, "functionalizingEntity");

		this.checkOpenForms(entity, request);

		return mapping.findForward("inputForm");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionService compositionService = new CompositionServiceLocalImpl();
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		entityBean.setupDomainEntity(user.getLoginName());
		ActionMessages msgs = new ActionMessages();
		compositionService.deleteFunctionalizingEntity(entityBean
				.getDomainEntity(), user);
		ActionMessage msg = new ActionMessage(
				"message.deleteFunctionalizingEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about
		// them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		return mapping.findForward("success");
	}

	private void checkOpenForms(FunctionalizingEntityBean entity,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean openFile = false, openFunction = false;
		if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
			openFile = true;
		}
		session.setAttribute("openFile", openFile);
		if (dispatch.equals("input")
				&& browserDispatch.equals("saveFunction")
				|| ((dispatch.equals("setupNew") || dispatch
						.equals("setupUpdate")) && entity.getFunctions()
						.isEmpty())) {
			openFunction = true;
		}
		session.setAttribute("openFunction", openFunction);

		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entity);

		/**
		 * If user entered customized value selecting [other] on previous page,
		 * we should show and highlight the entered value on the edit page.
		 */
		// Functional Entity Type
		String entityType = entity.getType();
		setOtherValueOption(request, entityType, "functionalizingEntityTypes");

		// Functional Entity Function Type
		String functionType = entity.getTheFunction().getType();
		setOtherValueOption(request, functionType, "functionTypes");
		String detailPage = null;
		if (!StringUtils.isEmpty(entity.getType())) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entity.getType(), "functionalizingEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
		
		// Feature request [26487] Deeper Edit Links.
		if ("setupUpdate".equals(dispatch)) {
			StringBuilder sb = new StringBuilder();
			List<FunctionBean> functions = entity.getFunctions();
			if (functions.size() == 1) {
				FunctionBean theFunction = functions.get(0);
				sb.append("setTheFunction(");
				sb.append(theFunction.getDomainFunction().getId());
				sb.append(");");
			}
			List<FileBean> files = entity.getFiles();
			if (files.size() == 1) {
				FileBean fileBean = files.get(0);
				sb.append("setTheFile('functionalizingEntity', ");
				sb.append(fileBean.getDomainFile().getId());
				sb.append(')');
			}
			if (sb.length() > 0) {
				// Have to use JavaScript to populate Function table.
				request.setAttribute("onloadJavascript", sb.toString());
			}
		}
	}
}
