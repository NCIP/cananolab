package gov.nih.nci.cananolab.restful.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.domain.agentmaterial.Antibody;
import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.agentmaterial.SmallMolecule;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.function.ImagingFunction;
import gov.nih.nci.cananolab.domain.function.OtherFunction;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer;
import gov.nih.nci.cananolab.domain.particle.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.CompositionUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFunctionBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFunctionalizingEntityBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.CompositionForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jvnet.hk2.config.Changed.TYPE;

public class FunctionalizingEntityBO extends BaseAnnotationBO {
	public List<String> create(SimpleFunctionalizingEntityBean bean,
			HttpServletRequest request) throws Exception {
		FunctionalizingEntityBean entityBean = transferSimpleFunctionalizingEntity(
				bean, request);
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(bean.getSampleId(), request);
		List<String> otherSampleNames = bean.getOtherSampleNames();
		this.setServicesInSession(request);
		msgs = validateInputs(request, msgs, entityBean);
		if (msgs.size() > 0) {
			return msgs;
		}
		this.saveEntity(request, bean.getSampleId(), entityBean);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entityBean);

		// save to other samples (only when user click [Submit] button.)
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		SampleBean[] otherSampleBeans = null;
		if (otherSampleNames != null) {
			otherSampleBeans = prepareCopy(request, otherSampleNames,
					sampleBean);
		}
		if (otherSampleBeans != null) {
			compService.copyAndSaveFunctionalizingEntity(entityBean,
					sampleBean, otherSampleBeans);
		}

		// to preselect functionalizing entity after returning to the summary
		// page
		request.getSession().setAttribute("tab", "2");
		msgs.add("success");
		return msgs;
	}

	private List<String> validateTargets(HttpServletRequest request,
			List<String> msgs, FunctionalizingEntityBean entityBean) {
		for (FunctionBean funcBean : entityBean.getFunctions()) {
			if (funcBean.getType() == null || funcBean.getType().equals("")) {
				msgs.add("Function Type is required.");
			}
			if ("TargetingFunction".equals(funcBean.getClassName())) {
				for (TargetBean targetBean : funcBean.getTargets()) {
					if (StringUtils.isEmpty(targetBean.getType())) {
						msgs.add("Target Type is required.");
					} else if (!StringUtils.xssValidate(targetBean.getType())) {
						msgs.add("Target Type is invalid.");
					}
				}
			}
		}
		return msgs;
	}

	private List<String> validateEntityFile(HttpServletRequest request,
			List<String> msgs, FunctionalizingEntityBean entityBean) {
		for (FileBean filebean : entityBean.getFiles()) {
			msgs = validateFileBean(request, msgs, filebean);
			if (msgs.size() > 0) {
				return msgs;
			}
		}
		return msgs;
	}

	private List<String> saveEntity(HttpServletRequest request,
			String sampleId, FunctionalizingEntityBean entityBean)
			throws Exception {
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(sampleId, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Boolean newEntity = true;
		try {
			entityBean.setupDomainEntity(user.getLoginName());
			if (entityBean.getDomainEntity().getId() != null) {
				newEntity = false;
			}
		} catch (ClassCastException ex) {

			if (!StringUtils.isEmpty(ex.getMessage())
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msgs.add(ex.getMessage()
						+ "is an invalid Function type. It is a pre-defined composition type.");
			} else {
				msgs.add(ex.getMessage()
						+ "is an invalid functionalizing entity type. It is a pre-defined composition type.");

				entityBean.setType(null);
			}
			return msgs;
		}

		compService.saveFunctionalizingEntity(sampleBean, entityBean);
		// retract from public if updating an existing public record and not
		// curator
		if (!newEntity && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(sampleId, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			msgs.add(PropertyUtil.getProperty("sample",
					"message.updateSample.retractFromPublic"));
		}

		return msgs;
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
	public Map<String, Object> setupNew(String sampleId,
			HttpServletRequest request) throws Exception {
		FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean();
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		request.getSession().setAttribute("onloadJavascript",
				"setEntityInclude('feType', 'functionalizingEntity')");
		this.checkOpenForms(entityBean, request);
		// clear copy to otherSamples
		// form.setOtherSamples(new String[0]);

		return CompositionUtil
				.reformatLocalSearchDropdownsInSessionForFunctionalizingEntity(request
						.getSession());
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitCompositionSetup.getInstance().setFunctionalizingEntityDropdowns(
				request);
	}

	public void setupView(CompositionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String entityId = super.validateId(request, "dataId");
		CompositionService compService = this.setServicesInSession(request);

		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId);
		request.setAttribute("functionalizingEntity", entityBean);
		String detailPage = null;
		if (entityBean.isWithProperties()) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getClassName(), "functionalizingEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
	}

	public SimpleFunctionalizingEntityBean setupUpdate(String sampleId,
			String dataId, HttpServletRequest request) throws Exception {
		CompositionService compService = this.setServicesInSession(request);
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		// dataId = super.validateId(request, "dataId");
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(dataId);
		this.setLookups(request);
		// clear copy to otherSamples
		// form.setOtherSamples(new String[0]);
		checkOpenForms(entityBean, request);
		request.getSession().setAttribute("sampleId", sampleId);
		SimpleFunctionalizingEntityBean bean = new SimpleFunctionalizingEntityBean();
		bean.tranferSimpleFunctionalizingBean(entityBean, request);
		return bean;
	}

	public SimpleFunctionalizingEntityBean saveFunction(
			SimpleFunctionalizingEntityBean bean, HttpServletRequest request)
			throws Exception {
		FunctionalizingEntityBean entity = transferSimpleFunctionalizingEntity(
				bean, request);
		List<String> msgs = new ArrayList<String>();
		this.setServicesInSession(request);
		FunctionBean function = entity.getTheFunction();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		function.setupDomainFunction(user.getLoginName(), 0);
		entity.addFunction(function);
		// this.addFunction(function, entity.getFunctions());
		msgs = validateInputs(request, msgs, entity);
		if (msgs.size() > 0) {
			SimpleFunctionalizingEntityBean funcBean = new SimpleFunctionalizingEntityBean();
			funcBean.setErrors(msgs);
			return funcBean;
		}
		this.saveEntity(request, bean.getSampleId(), entity);

		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(function.getDomainFunction());

		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		// return to setupUpdate to get the correct entity from database
		return setupUpdate(bean.getSampleId(), entity.getDomainEntity().getId()
				.toString(), request);
	}

	private void addFunction(FunctionBean function, List<FunctionBean> functions) {
		int index = -1;

		for (int i = 0; i < functions.size(); i++) {
			if (function.getDomainFunction().getId() != null) {
				if (function.getDomainFunction().getId()
						.equals(functions.get(i).getDomainFunction().getId()))
					index = i;
			}
		}
		if (index != -1) {
			functions.remove(function);
			// retain the original order
			functions.add(index, function);
		} else {
			functions.add(function);
		}
	}

	public SimpleFunctionalizingEntityBean removeFunction(
			SimpleFunctionalizingEntityBean bean, HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		FunctionalizingEntityBean entity = transferSimpleFunctionalizingEntity(
				bean, request);
		FunctionBean function = entity.getTheFunction();
		entity.removeFunction(function);
		msgs = validateInputs(request, msgs, entity);
		if (msgs.size() > 0) {
			SimpleFunctionalizingEntityBean funcBean = new SimpleFunctionalizingEntityBean();
			funcBean.setErrors(msgs);
			return funcBean;
		}
		this.saveEntity(request, bean.getSampleId(), entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity(),
				function.getDomainFunction());
		checkOpenForms(entity, request);
		return setupUpdate(bean.getSampleId(), entity.getDomainEntity().getId()
				.toString(), request);
	}

	public SimpleFunctionalizingEntityBean saveFile(
			SimpleFunctionalizingEntityBean bean, HttpServletRequest request)
			throws Exception {
		FunctionalizingEntityBean entity = transferSimpleFunctionalizingEntity(
				bean, request);
		this.setServicesInSession(request);
		FileBean theFile = entity.getTheFile();
		String sampleId = bean.getSampleId();
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(sampleId, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/"
				+ "functionalizingEntity";
		theFile.setupDomainFile(internalUriPath, user.getLoginName());

		String timestamp = DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute(
				"newFileData");
		if(!theFile.getDomainFile().getUriExternal()){
			if (newFileData != null) {
				theFile.setNewFileData((byte[]) request.getSession().getAttribute(
						"newFileData"));
				theFile.getDomainFile().setUri(
						Constants.FOLDER_PARTICLE + '/'
								+ sampleBean.getDomain().getName() + '/'
								+ "functionalizingEntity" + "/" + timestamp + "_"
								+ theFile.getDomainFile().getName());
			}else{
				theFile.getDomainFile().setUri(null);
			}
		}
		entity.addFile(theFile);

		// save the functionalizing entity
		msgs = validateInputs(request, msgs, entity);
		if (msgs.size() > 0) {
			SimpleFunctionalizingEntityBean funcBean = new SimpleFunctionalizingEntityBean();
			funcBean.setErrors(msgs);
			return funcBean;
		}
		this.saveEntity(request, sampleId, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		request.getSession().removeAttribute("newFileData");
		this.checkOpenForms(entity, request);

		return setupUpdate(sampleId, entity.getDomainEntity().getId()
				.toString(), request);
	}

	public SimpleFunctionalizingEntityBean removeFile(
			SimpleFunctionalizingEntityBean bean, HttpServletRequest request)
			throws Exception {
		FunctionalizingEntityBean entity = transferSimpleFunctionalizingEntity(
				bean, request);
		FileBean theFile = entity.getTheFile();
		List<String> msgs = new ArrayList<String>();
		entity.removeFile(theFile);
		request.setAttribute("anchor", "file");
		// save the functionalizing entity;
		msgs = validateInputs(request, msgs, entity);
		if (msgs.size() > 0) {
			SimpleFunctionalizingEntityBean funcBean = new SimpleFunctionalizingEntityBean();
			funcBean.setErrors(msgs);
			return funcBean;
		}
		this.saveEntity(request, bean.getSampleId(), entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		checkOpenForms(entity, request);
		 
		return setupUpdate(bean.getSampleId(), entity.getDomainEntity().getId()
				.toString(), request);
	}

	// per app scan, can not easily validate in the validation.xml
	private List<String> validateEntity(HttpServletRequest request,
			List<String> msgs, FunctionalizingEntityBean entityBean) {
		boolean status = true;
		if (entityBean.getType() == null || entityBean.getType().equals("")) {
			msgs.add("Functionalizing Entity Type is required.");
		}
		if (entityBean.getName() == null || entityBean.getName().equals("")) {
			msgs.add("Functionalizing Entity Chemical Name is required.");
		}
		if (entityBean.getType().equalsIgnoreCase("biopolymer")) {
			if (entityBean.getBiopolymer().getType() != null
					&& !StringUtils.xssValidate(entityBean.getBiopolymer()
							.getType())) {
				msgs.add(PropertyUtil.getProperty("sample",
						"functionalizingEntity.biopolymer.type.invalid"));
			}
		}
		if (entityBean.getType().equalsIgnoreCase("antibody")) {
			if (entityBean.getAntibody().getType() != null
					&& !StringUtils.xssValidate(entityBean.getAntibody()
							.getType())) {
				msgs.add(PropertyUtil.getProperty("sample",
						"functionalizingEntity.antibody.type.invalid"));

			}
			if (entityBean.getAntibody().getIsotype() != null
					&& !StringUtils.xssValidate(entityBean.getAntibody()
							.getIsotype())) {
				msgs.add(PropertyUtil.getProperty("sample",
						"functionalizingEntity.antibody.isotype.invalid"));
			}
		}
		if (entityBean.getType().equalsIgnoreCase("small molecule")) {
			if (entityBean.getSmallMolecule().getAlternateName() != null
					&& !StringUtils.xssValidate(entityBean.getSmallMolecule()
							.getAlternateName())) {
				msgs.add(PropertyUtil
						.getProperty("sample",
								"functionalizingEntity.smallMolecule.alternateName.invalid"));

			}
		}
		return msgs;
	}

	private List<String> validateInputs(HttpServletRequest request,
			List<String> msgs, FunctionalizingEntityBean entityBean) {
		msgs = validateEntity(request, msgs, entityBean);
		msgs = validateTargets(request, msgs, entityBean);
		msgs = validateEntityFile(request, msgs, entityBean);
		return msgs;
	}

	public void input(CompositionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		FunctionalizingEntityBean entity = form.getFunctionalizingEntity();
		escapeXmlForFileUri(entity.getTheFile());
		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = entity.getTheFile();
		// preserveUploadedFile(request, theFile, "functionalizingEntity");

		this.checkOpenForms(entity, request);

		// return mapping.findForward("inputForm");
	}

	public List<String> delete(SimpleFunctionalizingEntityBean bean,
			HttpServletRequest request) throws Exception {
		List<String> msgs = new ArrayList<String>();
		CompositionService compositionService = this
				.setServicesInSession(request);
		FunctionalizingEntityBean entityBean = transferSimpleFunctionalizingEntity(
				bean, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		entityBean.setupDomainEntity(user.getLoginName());
		compositionService.deleteFunctionalizingEntity(entityBean
				.getDomainEntity());
		compositionService.removeAccesses(entityBean.getDomainEntity());

		msgs.add("success");
		return msgs;
	}

	private void checkOpenForms(FunctionalizingEntityBean entity,
			HttpServletRequest request) throws Exception {
		// String dispatch = request.getParameter("dispatch");
		// String browserDispatch = getBrowserDispatch(request);
		// HttpSession session = request.getSession();
		// Boolean openFile = false, openFunction = false;
		// if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
		// openFile = true;
		// }
		// session.setAttribute("openFile", openFile);
		// if (dispatch.equals("input")
		// && browserDispatch.equals("saveFunction")
		// || ((dispatch.equals("setupNew") || dispatch
		// .equals("setupUpdate")) && entity.getFunctions()
		// .isEmpty())) {
		// openFunction = true;
		// }
		// session.setAttribute("openFunction", openFunction);

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
		if (entity.isWithProperties()) {
			if (!StringUtils.isEmpty(entity.getType())) {
				detailPage = InitCompositionSetup.getInstance().getDetailPage(
						entity.getType(), "functionalizingEntity");
			}
		}
		request.setAttribute("entityDetailPage", detailPage);
	}

	private CompositionService setServicesInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);

		CompositionService compService = new CompositionServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("compositionService", compService);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return compService;
	}

	public FunctionalizingEntityBean transferSimpleFunctionalizingEntity(
			SimpleFunctionalizingEntityBean bean, HttpServletRequest request) {

		// setting up theFunction
		FunctionalizingEntityBean funcBean = new FunctionalizingEntityBean();
		List<FunctionBean> funcList = new ArrayList<FunctionBean>();
		List<FileBean> fileList = new ArrayList<FileBean>();

		FunctionalizingEntity domainEntity = null;

		SimpleFunctionBean sFunction = bean.getSimpleFunctionBean();
		TargetBean target = new TargetBean();
		FunctionBean functionBean = new FunctionBean();
		Function theFunction = new Function();
		List<TargetBean> targets = new ArrayList<TargetBean>();
		if (sFunction != null) {
			functionBean.setType(sFunction.getType());
			functionBean.setDescription(sFunction.getDescription());
			if(sFunction.getId()>0){
				functionBean.setId(sFunction.getId().toString());
			}
			theFunction.setDescription(sFunction.getDescription());
			if (sFunction.getId() != null) {
				theFunction.setId(sFunction.getId());
				theFunction.setCreatedBy(sFunction.getCreatedBy());
				theFunction.setCreatedDate(sFunction.getCreatedDate());
			}
			ImagingFunction img = new ImagingFunction();
			img.setModality(sFunction.getModality());
			
			if (sFunction.getTargets() != null) {
				for (int i = 0; i < sFunction.getTargets().size(); i++) {
					target = new TargetBean();
					if (sFunction.getTargets().get(i).get("id") != null)
						target.setId(sFunction.getTargets().get(i).get("id"));
					target.setDescription(sFunction.getTargets().get(i)
							.get("description"));
					target.setName(sFunction.getTargets().get(i).get("name"));
					target.setType(sFunction.getTargets().get(i).get("type"));
					targets.add(target);
				}
			}
			functionBean.setImagingFunction(img);
			functionBean.setTargets(targets);
			functionBean.setDomainFunction(theFunction);
		}
		funcBean.setTheFunction(functionBean);

		// setting up functions if ther exists
		Collection<Function> funCollection = new HashSet<Function>();
		if (bean.getFunctionList() != null) {
			for (SimpleFunctionBean fuBean : bean.getFunctionList()) {
				functionBean = new FunctionBean();
				theFunction = new Function();
				ImagingFunction img = new ImagingFunction();
				img.setModality(fuBean.getModality());
				targets = new ArrayList<TargetBean>();
				theFunction.setDescription(fuBean.getDescription());
				theFunction.setCreatedBy(fuBean.getCreatedBy());
				theFunction.setCreatedDate(fuBean.getCreatedDate());
				theFunction.setId(fuBean.getId());
				functionBean.setType(fuBean.getType());
				functionBean.setDescription(fuBean.getDescription());
				functionBean.setId(fuBean.getId().toString());
				functionBean.setImagingFunction(img);
				for (int i = 0; i < fuBean.getTargets().size(); i++) {
					target = new TargetBean();
					if (fuBean.getTargets().get(i).get("id") != null)
						target.setId(fuBean.getTargets().get(i).get("id"));
					target.setDescription(fuBean.getTargets().get(i)
							.get("description"));
					target.setName(fuBean.getTargets().get(i).get("name"));
					target.setType(fuBean.getTargets().get(i).get("type"));
					targets.add(target);
				}
				functionBean.setDomainFunction(theFunction);
				functionBean.setTargets(targets);
				funCollection.add(theFunction);
				funcList.add(functionBean);
			}

		}
		// setting up theFile

		SimpleFileBean sFBean = bean.getFileBean();

		FileBean fileBean = new FileBean();
		File file = new File();
		if (sFBean != null) {
			file.setType(sFBean.getType());
			file.setTitle(sFBean.getTitle());
			file.setDescription(sFBean.getDescription());
			file.setUri(sFBean.getUri());
			if (sFBean.getId() != null) {
				file.setId(sFBean.getId());
				file.setCreatedBy(sFBean.getCreatedBy());
				file.setCreatedDate(sFBean.getCreatedDate());
			}
			file.setUriExternal(sFBean.getUriExternal());
			fileBean.setExternalUrl(sFBean.getExternalUrl());
			fileBean.setKeywordsStr(sFBean.getKeywordsStr());
			fileBean.setTheAccess(sFBean.getTheAccess());

			fileBean.setDomainFile(file);
		}
		funcBean.setTheFile(fileBean);

		// setting up files if there exists
		Collection<File> filecoll = new HashSet<File>();
		List<SimpleFileBean> filelist = bean.getFileList();

		if (filelist != null) {
			for (SimpleFileBean sBean : filelist) {
				fileBean = new FileBean();
				file = new File();
				file.setType(sBean.getType());
				file.setTitle(sBean.getTitle());
				file.setDescription(sBean.getDescription());
				file.setUri(sBean.getUri());
				file.setId(sBean.getId());
				file.setCreatedBy(sBean.getCreatedBy());
				file.setCreatedDate(sBean.getCreatedDate());
				file.setUriExternal(sBean.getUriExternal());
				fileBean.setExternalUrl(sBean.getExternalUrl());
				fileBean.setKeywordsStr(sBean.getKeywordsStr());
				file.setKeywordCollection(new HashSet<Keyword>());
				if(!StringUtils.isEmpty(sBean.getKeywordsStr())){
					String[] strs = sBean.getKeywordsStr().split("\r\n");
					for (String str : strs) {
						// change to upper case
						Keyword keyword = new Keyword();
						keyword.setName(str.toUpperCase());
						file.getKeywordCollection().add(keyword);
					}
				}
				fileBean.setTheAccess(sBean.getTheAccess());

				fileBean.setDomainFile(file);
				filecoll.add(file);
				fileList.add(fileBean);
			}
		}

		// setting up sampleComposition
		// Managed to get the sampleComposition in the backend to avoid lazy
		// loading things
		SampleComposition sampleComp = null;
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		CompositionServiceHelper helper = new CompositionServiceHelper(
				securityService);
		try {
			sampleComp = helper.findCompositionBySampleId(bean.getSampleId()
					.toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (bean.getType().equalsIgnoreCase("small molecule")) {
			SmallMolecule mol = new SmallMolecule();
			mol.setAlternateName((String) bean.getDomainEntity().get(
					"alternateName"));
			if (bean.getDomainEntity().get("id") != null) {
				mol.setId(new Long((Integer) bean.getDomainEntity().get("id")));
				mol.setCreatedBy((String) bean.getDomainEntity().get(
						"createdBy"));
				mol.setCreatedDate(new Date((Long) bean.getDomainEntity().get(
						"createdDate")));
				mol.setSampleComposition(sampleComp);

			} else {
				mol.setSampleComposition(null);

			}
			mol.setFunctionCollection(funCollection);
			mol.setFileCollection(filecoll);
			funcBean.setSmallMolecule(mol);
			domainEntity = mol;
		} else if (bean.getType().equalsIgnoreCase("Biopolymer")) {

			Biopolymer bio = new Biopolymer();
			if (bean.getDomainEntity().get("id") != null) {
				bio.setId(new Long((Integer) bean.getDomainEntity().get("id")));
				bio.setCreatedBy((String) bean.getDomainEntity().get(
						"createdBy"));
				bio.setCreatedDate(new Date((Long) bean.getDomainEntity().get(
						"createdDate")));
				bio.setSampleComposition(sampleComp);

			} else {
				bio.setSampleComposition(null);

			}
			bio.setType((String) bean.getDomainEntity().get("type"));
			bio.setSequence((String) bean.getDomainEntity().get("sequence"));
			bio.setFunctionCollection(funCollection);

			bio.setFileCollection(filecoll);
			funcBean.setBiopolymer(bio);
			domainEntity = bio;
		} else if (bean.getType().equalsIgnoreCase("Antibody")) {
			Antibody body = new Antibody();

			if (bean.getDomainEntity().get("id") != null) {
				body.setId(new Long((Integer) bean.getDomainEntity().get("id")));
				body.setCreatedBy((String) bean.getDomainEntity().get(
						"createdBy"));
				body.setCreatedDate(new Date((Long) bean.getDomainEntity().get(
						"createdDate")));
				body.setSampleComposition(sampleComp);

			} else
				body.setSampleComposition(null);

			body.setType((String) bean.getDomainEntity().get("type"));
			body.setIsotype((String) bean.getDomainEntity().get("isoType"));
			body.setSpecies((String) bean.getDomainEntity().get("species"));
			body.setFunctionCollection(funCollection);
			body.setFileCollection(filecoll);
			funcBean.setAntibody(body);
			domainEntity = body;
		} else {
			domainEntity = new OtherFunctionalizingEntity();
			if (bean.getDomainEntity() != null) {
				if (bean.getDomainEntity().get("id") != null) {
					domainEntity.setId(new Long((Integer) bean
							.getDomainEntity().get("id")));
					domainEntity.setCreatedBy((String) bean.getDomainEntity()
							.get("createdBy"));
					domainEntity.setCreatedDate(new Date((Long) bean
							.getDomainEntity().get("createdDate")));
					domainEntity.setSampleComposition(sampleComp);

				} else {
					domainEntity.setSampleComposition(null);
				}
				domainEntity.setFunctionCollection(funCollection);
				domainEntity.setFileCollection(filecoll);
			}
		}
		ActivationMethod act = new ActivationMethod();
		if(bean.getActivationId()>0)
			act.setId(bean.getActivationId());
		act.setType(bean.getActivationMethodType());
		act.setActivationEffect(bean.getActivationEffect());
		funcBean.setActivationMethod(act);
		funcBean.setType(bean.getType());
		funcBean.setName(bean.getName());
		funcBean.setPubChemDataSourceName(bean.getPubChemDataSourceName());
		funcBean.setPubChemId(bean.getPubChemId());
		funcBean.setValue(bean.getValue());
		funcBean.setValueUnit(bean.getValueUnit());
		funcBean.setMolecularFormula(bean.getMolecularFormula());
		funcBean.setMolecularFormulaType(bean.getMolecularFormulaType());
		funcBean.setDescription(bean.getDescription());
		funcBean.setFiles(fileList);
		funcBean.setFunctions(funcList);
		funcBean.setDomainEntity(domainEntity);
		return funcBean;
	}

}
