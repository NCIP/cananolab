package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.function.ImagingFunction;
import gov.nih.nci.cananolab.domain.nanomaterial.Biopolymer;
import gov.nih.nci.cananolab.domain.nanomaterial.CarbonNanotube;
import gov.nih.nci.cananolab.domain.nanomaterial.Dendrimer;
import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.domain.nanomaterial.Fullerene;
import gov.nih.nci.cananolab.domain.nanomaterial.Liposome;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.nanomaterial.Polymer;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.util.CompositionUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleComposingElementBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleNanomaterialEntityBean;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class NanomaterialEntityBO extends BaseAnnotationBO{
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
	public List<String> create(SimpleNanomaterialEntityBean nanoBean,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		String sampleId = nanoBean.getSampleId();
		NanomaterialEntityBean entityBean = transferNanoMateriaEntityBean(nanoBean, request);  
		this.setServicesInSession(request);
		SampleBean sampleBean = setupSampleById(sampleId, request);
		List<String> otherSampleNames = nanoBean.getOtherSampleNames();
		msgs = validateInputs(request, entityBean);
		if (msgs.size()>0) {
			return msgs;
		}
		this.saveEntity(request, sampleId, entityBean);
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entityBean);
		SampleBean[] otherSampleBeans = null;
		if(otherSampleNames!=null){
		otherSampleBeans = prepareCopy(request, otherSampleNames,
				sampleBean);
		}
		if (otherSampleBeans != null) {
			CompositionService compService = (CompositionService) request
					.getSession().getAttribute("compositionService");
		
			compService.copyAndSaveNanomaterialEntity(entityBean,
					sampleBean, otherSampleBeans);
		}
		// save action messages in the session so composition.do know about them
		// to preselect nanomaterial entity after returning to the summary page
		msgs.add("success");
		request.getSession().setAttribute("tab", "1");
		return msgs;
	}

	private List<String> validateInputs(HttpServletRequest request,
			NanomaterialEntityBean entityBean) {
		List<String> msgs = new ArrayList<String>();
				
		msgs = validateEntity(request, msgs, entityBean);

		msgs = validateInherentFunctionType(request, msgs, entityBean);

		msgs = validateEntityFile(request,msgs,entityBean);

		return msgs;
	}

	public void input(CompositionForm form,
			HttpServletRequest request)
			throws Exception {

		NanomaterialEntityBean entityBean = form.getNanomaterialEntity();
		escapeXmlForFileUri(entityBean.getTheFile());
		entityBean.updateEmptyFieldsToNull();
		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = entityBean.getTheFile();
		//preserveUploadedFile(request, theFile, "nanomaterialEntity");

		this.checkOpenForms(entityBean, request);
	}

	private List<String> saveEntity(HttpServletRequest request, String sampleId, 
			NanomaterialEntityBean entityBean)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		this.setServicesInSession(request);

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
				msgs.add(entityBean.getType()+" is an invalid nanomaterial entity type. It is a pre-defined composition type.");
			} else {
				msgs.add(entityBean.getType()+" is an invalid nanomaterial entity type. It is a pre-defined composition type.");

				entityBean.setType(null);
			}
			entityBean.setType(null);
		}
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.saveNanomaterialEntity(sampleBean, entityBean);
		
		// retract from public if updating an existing public record and not
		// curator
		if (!newEntity && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(sampleId, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			msgs.add(PropertyUtil.getProperty("sample", "message.updateSample.retractFromPublic"));
			return msgs;
		}
		return msgs;
	}

	private List<String> validateInherentFunctionType(HttpServletRequest request, List<String> msgs,
			NanomaterialEntityBean entityBean) {

		for (ComposingElementBean composingElementBean : entityBean
				.getComposingElements()) {
			if(composingElementBean.getDomain().getType()==null||composingElementBean.getDomain().getType().equals("")){
				msgs.add("Composing Element Type is required.");
			}
			if(composingElementBean.getDomain().getName()==null||composingElementBean.getDomain().getName().equals("")){
				msgs.add("Composing Element Chemical Name is required.");
			}
			for (FunctionBean functionBean : composingElementBean
					.getInherentFunctions()) {
				if (StringUtils.isEmpty(functionBean.getType())) {
					msgs.add("Inherent function type is required.");
				} else if (!StringUtils.xssValidate(functionBean.getType())) {
					msgs.add(PropertyUtil.getProperty("sample", "function.type.invalid"));
				}
			}
		}
		return msgs;
	}

	// per app scan, can not easily validate in the validation.xml
	private List<String> validateEntity(HttpServletRequest request, List<String> msgs,
			NanomaterialEntityBean entityBean) {
		if(entityBean.getType()==null||entityBean.getType().equals("")){
			msgs.add("Nanomaterial Entity Type is required.");
		}
		if (entityBean.getType().equalsIgnoreCase("biopolymer")) {
			if(entityBean.getBiopolymer().getName()==null){
				msgs.add("Biopolymer Name is required.");
			}
			if (entityBean.getBiopolymer().getName() != null
					&& !StringUtils.xssValidate(entityBean.getBiopolymer()
							.getName())) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.biopolymer.name.invalid"));
			}
			if(entityBean.getBiopolymer().getType()==null){
				msgs.add("Biopolymer Type is required.");
			}
			if (entityBean.getBiopolymer().getType() != null
					&& !StringUtils.xssValidate(entityBean.getBiopolymer()
							.getType())) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.biopolymer.type.invalid"));
			}
		} else if (entityBean.getType().equalsIgnoreCase("liposome")) {
			if (entityBean.getLiposome().getPolymerName() != null
					&& !StringUtils.xssValidate(entityBean.getLiposome()
							.getPolymerName())) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.liposome.polymerName.invalid"));
			}
		} else if (entityBean.getType().equalsIgnoreCase("emulsion")) {
			if (entityBean.getEmulsion().getPolymerName() != null
					&& !StringUtils.xssValidate(entityBean.getEmulsion()
							.getPolymerName())) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.emulsion.polymerName.invalid"));
			}
		} else if (entityBean.getType().equalsIgnoreCase("polymer")) {
			if (entityBean.getPolymer().getInitiator() != null
					&& !StringUtils.xssValidate(entityBean.getPolymer()
							.getInitiator())) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.polymer.initiator.invalid"));
			}
		} else if (entityBean.getType().equalsIgnoreCase("dendrimer")) {
			if (entityBean.getDendrimer().getBranch() != null
					&& !StringUtils.xssValidate(entityBean.getDendrimer()
							.getBranch())) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.dendrimer.branch.invalid"));
			}
		} else if (entityBean.getType().equalsIgnoreCase("carbon nanotube")) {
			if (entityBean.getCarbonNanotube().getAverageLengthUnit() != null
					&& !entityBean.getCarbonNanotube().getAverageLengthUnit()
							.matches(Constants.UNIT_PATTERN)) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.carbonNanotube.averageLengthUnit.invalid"));
			}
			if (entityBean.getCarbonNanotube().getChirality() != null
					&& !StringUtils.xssValidate(entityBean.getCarbonNanotube()
							.getChirality())) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.carbonNanotube.chirality.invalid"));
			}
			if (entityBean.getCarbonNanotube().getDiameterUnit() != null
					&& !entityBean.getCarbonNanotube().getDiameterUnit()
							.matches(Constants.UNIT_PATTERN)) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.carbonNanotube.diameterUnit.invalid"));
			}
		} else if (entityBean.getType().equalsIgnoreCase("fullerene")) {
			if (entityBean.getFullerene().getAverageDiameterUnit() != null
					&& !entityBean.getFullerene().getAverageDiameterUnit()
							.matches(Constants.UNIT_PATTERN)) {
				msgs.add(PropertyUtil.getProperty("sample", "nanomaterialEntityForm.fullerene.averageDiameterUnit.invalid"));
			}
		}
		return msgs;
	}

	private List<String> validateEntityFile(HttpServletRequest request, List<String> msgs,
			NanomaterialEntityBean entityBean) {
		//ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
		msgs = validateFileBean(request, msgs, filebean);
			if (msgs.size()>0) {
				return msgs;
			}
		}
		return msgs;
	}

	public void setLookups(HttpServletRequest request) throws Exception {
		ServletContext appContext = request.getSession().getServletContext();
		InitCompositionSetup.getInstance().setNanomaterialEntityDropdowns(
				request);
		InitSetup.getInstance().getDefaultTypesByLookup(appContext,
				"wallTypes", "carbon nanotube", "wallType");

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
			HttpServletRequest request)
			throws Exception {
		NanomaterialEntityBean entityBean = new NanomaterialEntityBean();
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		this.checkOpenForms(entityBean, request);
		// clear copy to otherSamples
		return CompositionUtil.reformatLocalSearchDropdownsInSessionForNanoEntity(request.getSession());

	}

	public SimpleNanomaterialEntityBean setupUpdate(String sampleId, String entityId,
			HttpServletRequest request)
			throws Exception {
	//	entityId = super.validateId(request, "dataId");
		CompositionForm form = new CompositionForm();
		// set up other particles with the same primary point of contact
		CompositionService compService = this.setServicesInSession(request);
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId);
		form.setNanomaterialEntity(entityBean);
		form.setOtherSamples(new String[0]);
		this.checkOpenForms(entityBean, request);
		request.getSession().setAttribute("sampleId", sampleId);
		SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
		nano.transferNanoMaterialEntityBeanToSimple(entityBean, request);
		return nano;
	}

	public void setupView(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
		String entityId = super.validateId(request, "dataId");
		CompositionService compService = this.setServicesInSession(request);

		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId);
		request.setAttribute("nanomaterialEntity", entityBean);
		String detailPage = null;
		if (entityBean.isWithProperties()) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getClassName(), "nanomaterialEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
	}

	public SimpleNanomaterialEntityBean saveComposingElement(SimpleNanomaterialEntityBean nanoBean, HttpServletRequest request) throws Exception {
		NanomaterialEntityBean entity = transferNanoMateriaEntityBean(nanoBean, request);
		String sampleId = nanoBean.getSampleId();
		List<String> msgs =new ArrayList<String>();
		ComposingElementBean composingElement = entity.getTheComposingElement();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		composingElement.setupDomain(user.getLoginName());
		entity.addComposingElement(composingElement);
		
		// save nanomaterial entity
		msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
			SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
			nano.setErrors(msgs);
			return nano;
		}
		msgs = this.saveEntity(request, sampleId, entity);
		
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(composingElement.getDomain());

		// return to setupUpdate to retrieve the correct entity from database
		// after saving to database.
		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		return setupUpdate(sampleId, entity.getDomainEntity().getId()
				.toString(), request);
	}

	private NanomaterialEntityBean transferNanoMateriaEntityBean(
			SimpleNanomaterialEntityBean nanoBean, HttpServletRequest request) {
		NanomaterialEntityBean bean = new NanomaterialEntityBean();
		NanomaterialEntity nanoEntity = null;
		
		Collection<ComposingElement> coll = new HashSet<ComposingElement>();
		Collection<File> filecoll = new HashSet<File>();
		//setting up sampleComposition 
		//Managed to get the sampleComposition in the backend to avoid lazy loading things
		
		SampleComposition sampleComp = null;
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
			CompositionServiceHelper helper = new CompositionServiceHelper(securityService);
			try {
				sampleComp = helper.findCompositionBySampleId(nanoBean.getSampleId()
						.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		//Setting up the ComposingElement
		ComposingElementBean compBean = new ComposingElementBean();
		
		SimpleComposingElementBean sBean = nanoBean.getSimpleCompBean();
		
		ComposingElement comp = new ComposingElement();
		
		FunctionBean func = new FunctionBean();
		ImagingFunction img = new ImagingFunction();
		Collection<Function> hash = new HashSet<Function>();
		List<Map<String, Object>> funclist;
		List<FunctionBean> inherentFunctions = new ArrayList<FunctionBean>();
		if(sBean!=null){
			comp.setDescription(sBean.getDescription());
			comp.setType(sBean.getType());
			comp.setName(sBean.getName());
			comp.setPubChemDataSourceName(sBean.getPubChemDataSourceName());
			comp.setPubChemId(sBean.getPubChemId());
			if((sBean.getId()!=null)&&(sBean.getId()>0)){
				comp.setId(sBean.getId());
				comp.setCreatedBy(sBean.getCreatedBy());
				comp.setCreatedDate(sBean.getCreatedDate());
			}
			comp.setMolecularFormula(sBean.getMolecularFormula());
			comp.setMolecularFormulaType(sBean.getMolecularFormulaType());
			comp.setValue(sBean.getValue());
			comp.setValueUnit(sBean.getValueUnit());
			funclist = sBean.getInherentFunction();
			if(funclist!= null){
			for(int j=0;j<funclist.size();j++){
				func = new FunctionBean();
				img.setModality((String) funclist.get(j).get("modality"));
				func.setType((String) funclist.get(j).get("type"));
				func.setDescription((String) funclist.get(j).get("description"));
				func.setImagingFunction(img);
				Function function = new Function();
				function.setDescription((String) funclist.get(j).get("description"));
				if(new Long((String) funclist.get(j).get("id"))>0){
					function.setCreatedBy((String) funclist.get(j).get("createdBy"));
					function.setCreatedDate(new Date((Long) funclist.get(j).get("createdDate")));
					function.setId(new Long((String) funclist.get(j).get("id")));
				}
				func.setDomainFunction(function);
				hash.add(function);
				inherentFunctions.add(func);

		}
		}
		compBean.setTheFunction(func);
		compBean.setDomain(comp);
		compBean.setInherentFunctions(inherentFunctions);
		
		bean.setTheComposingElement(compBean);
		}
		//setting up composing elements if there exists composingElements
		List<SimpleComposingElementBean> list = nanoBean.getComposingElements();

		func = new FunctionBean();
		img = new ImagingFunction();
		hash = new HashSet<Function>();

		List<ComposingElementBean> compList = new ArrayList<ComposingElementBean>();
		if(list!=null){
		for(SimpleComposingElementBean simpleComp : list){
			compBean = new ComposingElementBean();
			comp = new ComposingElement();

			comp.setDescription(simpleComp.getDescription());
			comp.setType(simpleComp.getType());
			comp.setName(simpleComp.getName());
			comp.setPubChemDataSourceName(simpleComp.getPubChemDataSourceName());
			comp.setPubChemId(simpleComp.getPubChemId());
			comp.setId(simpleComp.getId());
			comp.setCreatedBy(simpleComp.getCreatedBy());
			comp.setCreatedDate(simpleComp.getCreatedDate());
			comp.setMolecularFormula(simpleComp.getMolecularFormula());
			comp.setMolecularFormulaType(simpleComp.getMolecularFormulaType());
			comp.setValue(simpleComp.getValue());
			comp.setValueUnit(simpleComp.getValueUnit());
			funclist = simpleComp.getInherentFunction();
			if(funclist!= null){
				for(int j=0;j<funclist.size();j++){
					img.setModality((String) funclist.get(j).get("modality"));
					func.setType((String) funclist.get(j).get("type"));
					func.setDescription((String) funclist.get(j).get("description"));
					func.setImagingFunction(img);
					Function function = new Function();
					function.setDescription((String) funclist.get(j).get("description"));
					
					if(new Long((String) funclist.get(j).get("id"))>0){
						function.setId(new Long((String) funclist.get(j).get("id")));
						function.setCreatedBy((String) funclist.get(j).get("createdBy"));
						function.setCreatedDate(new Date((Long) funclist.get(j).get("createdDate")));
					}
					hash.add(function);
					func.setDomainFunction(function);
				}
			}
			comp.setInherentFunctionCollection(hash);
			coll.add(comp);
			compBean.setTheFunction(func);
			compBean.setDomain(comp);
			compList.add(compBean);
		}
	}
		//setting up theFile
		SimpleFileBean fBean = nanoBean.getFileBean();
		FileBean fileBean = new FileBean();
		File file = new File();
		if(fBean!=null){
			
			file.setType(fBean.getType());
			file.setTitle(fBean.getTitle());
			file.setDescription(fBean.getDescription());
			file.setUri(fBean.getUri());
			if(fBean.getId()!=null){
				file.setId(fBean.getId());
				file.setCreatedBy(fBean.getCreatedBy());
				file.setCreatedDate(fBean.getCreatedDate());
			}
			file.setUriExternal(fBean.getUriExternal());
			fileBean.setKeywordsStr(fBean.getKeywordsStr());
			fileBean.setExternalUrl(fBean.getExternalUrl());
			fileBean.setTheAccess(fBean.getTheAccess());

			fileBean.setDomainFile(file);
		}
		bean.setTheFile(fileBean);
		
		//setting up files
		List<SimpleFileBean> filelist =  nanoBean.getFiles();
		
		List<FileBean> fileBeanList = new ArrayList<FileBean>();
		if(filelist!=null){
		for(SimpleFileBean sFBean : filelist){
			fileBean = new FileBean();
			file = new File();
			file.setType(sFBean.getType());
			file.setTitle(sFBean.getTitle());
			file.setDescription(sFBean.getDescription());
			file.setUri(sFBean.getUri());
			file.setId(sFBean.getId());
			file.setCreatedBy(sFBean.getCreatedBy());
			file.setCreatedDate(sFBean.getCreatedDate());
			file.setUriExternal(sFBean.getUriExternal());
			fileBean.setKeywordsStr(sFBean.getKeywordsStr());
			file.setKeywordCollection(new HashSet<Keyword>());
			if(!StringUtils.isEmpty(sFBean.getKeywordsStr())){
				String[] strs = sFBean.getKeywordsStr().split("\r\n");
				for (String str : strs) {
					// change to upper case
					Keyword keyword = new Keyword();
					keyword.setName(str.toUpperCase());
					file.getKeywordCollection().add(keyword);
				}
			}
			fileBean.setTheAccess(sFBean.getTheAccess());
			fileBean.setExternalUrl(sFBean.getExternalUrl());
			fileBean.setDomainFile(file);
			filecoll.add(file);
			fileBeanList.add(fileBean);
		}
		}
		
		if(nanoBean.getType().equalsIgnoreCase("fullerene")){
			Fullerene fullerene = new Fullerene();
			if(nanoBean.getDomainEntity()!=null){
				if(nanoBean.getDomainEntity().get("averageDiameter") != null)
					fullerene.setAverageDiameter(new Float((String) nanoBean.getDomainEntity().get("averageDiameter")));
				if(nanoBean.getDomainEntity().get("averageDiameterUnit") != null)
					fullerene.setAverageDiameterUnit((String) nanoBean.getDomainEntity().get("averageDiameterUnit"));
				if(nanoBean.getDomainEntity().get("numberOfCarbon") != null)
					fullerene.setNumberOfCarbon(new Integer((String) nanoBean.getDomainEntity().get("numberOfCarbon")));
				if(nanoBean.getDomainEntity().get("id")!=null){
					fullerene.setId(new Long((Integer)nanoBean.getDomainEntity().get("id")));
					fullerene.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
					fullerene.setCreatedDate(new Date((Long) nanoBean.getDomainEntity().get("createdDate")));
					fullerene.setSampleComposition(sampleComp);
				}else{
					fullerene.setSampleComposition(null);
				}	
			}
			fullerene.setComposingElementCollection(coll);
			fullerene.setFileCollection(filecoll);
			bean.setFullerene(fullerene);
			nanoEntity = fullerene;
		}
			
		else if(nanoBean.getType().equalsIgnoreCase("dendrimer")){
			Dendrimer den = new Dendrimer();
			if(nanoBean.getDomainEntity()!=null){
				if(nanoBean.getDomainEntity().get("branch")!=null)
					den.setBranch((String) nanoBean.getDomainEntity().get("branch"));
				if(nanoBean.getDomainEntity().get("generation")!=null)
					den.setGeneration(new Float((String) nanoBean.getDomainEntity().get("generation")));
				if(nanoBean.getDomainEntity().get("id")!=null){
					den.setId(new Long((Integer) nanoBean.getDomainEntity().get("id")));
					den.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
					den.setCreatedDate(new Date((Long) nanoBean.getDomainEntity().get("createdDate")));
					den.setSampleComposition(sampleComp);
				}else{
					den.setSampleComposition(null);
	    		}
			}
			den.setComposingElementCollection(coll);
			den.setFileCollection(filecoll);
			nanoEntity = den;
			bean.setDendrimer(den);
		}
			
		else if(nanoBean.getType().equalsIgnoreCase("biopolymer")){
			Biopolymer bio = new Biopolymer();
			if(nanoBean.getDomainEntity()!=null){
				if(nanoBean.getDomainEntity().get("type")!=null)
					bio.setType((String) nanoBean.getDomainEntity().get("type"));
				if(nanoBean.getDomainEntity().get("name")!=null)
					bio.setName((String) nanoBean.getDomainEntity().get("name"));
				if(nanoBean.getDomainEntity().get("sequence")!=null)
					bio.setSequence((String) nanoBean.getDomainEntity().get("sequence"));
				if(nanoBean.getDomainEntity().get("id")!=null){
					bio.setId(new Long((Integer) nanoBean.getDomainEntity().get("id")));
					bio.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
					bio.setCreatedDate(new Date((Long) nanoBean.getDomainEntity().get("createdDate")));
					bio.setSampleComposition(sampleComp);
				}else{
					bio.setSampleComposition(null);
				}
			}
			bio.setComposingElementCollection(coll);
			bio.setFileCollection(filecoll);
			bean.setBiopolymer(bio);
			nanoEntity = bio;
			
		}
		else if(nanoBean.getType().equalsIgnoreCase("Liposome")){
			Liposome lipo = new Liposome();
			if(nanoBean.getDomainEntity()!=null){
				if(nanoBean.getDomainEntity().get("isPolymerized")!=null){
					lipo.setPolymerized(Boolean.valueOf((String) nanoBean.getDomainEntity().get("isPolymerized")));
					bean.setIsPolymerized((String) nanoBean.getDomainEntity().get("isPolymerized"));
				}
				if(nanoBean.getDomainEntity().get("polymerName")!=null)
					lipo.setPolymerName((String) nanoBean.getDomainEntity().get("polymerName"));
				if(nanoBean.getDomainEntity().get("id")!=null){
					lipo.setId(new Long((Integer) nanoBean.getDomainEntity().get("id")));
					lipo.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
					lipo.setCreatedDate(new Date((Long) nanoBean.getDomainEntity().get("createdDate")));
					lipo.setSampleComposition(sampleComp);
				}else{
					lipo.setSampleComposition(null);
				}
			}
			lipo.setComposingElementCollection(coll);
			lipo.setFileCollection(filecoll);
			nanoEntity = lipo;
			bean.setLiposome(lipo);
		}
		else if(nanoBean.getType().equalsIgnoreCase("Emulsion")){
			Emulsion em = new Emulsion();
			if(nanoBean.getDomainEntity()!=null){
				if(nanoBean.getDomainEntity().get("isPolymerized")!=null){
					em.setPolymerized(Boolean.valueOf((String) nanoBean.getDomainEntity().get("isPolymerized")));
					bean.setIsPolymerized((String) nanoBean.getDomainEntity().get("isPolymerized"));
				}
				if(nanoBean.getDomainEntity().get("polymerName")!=null)
					em.setPolymerName((String) nanoBean.getDomainEntity().get("polymerName"));
				if(nanoBean.getDomainEntity().get("id")!=null){
					em.setId(new Long((Integer) nanoBean.getDomainEntity().get("id")));
					em.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
					em.setCreatedDate(new Date((Long) nanoBean.getDomainEntity().get("createdDate")));
					em.setSampleComposition(sampleComp);
				}else{
					em.setSampleComposition(null);
				}
			}
			em.setComposingElementCollection(coll);
			em.setFileCollection(filecoll);
			bean.setEmulsion(em);
			nanoEntity = em;
		}
		else if(nanoBean.getType().equalsIgnoreCase("Polymer")){
			Polymer poly = new Polymer();
			if(nanoBean.getDomainEntity()!=null){
				if(nanoBean.getDomainEntity().get("isCrossLinked")!=null){
					poly.setCrossLinked(Boolean.valueOf((String) nanoBean.getDomainEntity().get("isCrossLinked")));
					bean.setIsCrossLinked((String) nanoBean.getDomainEntity().get("isCrossLinked"));
				}
				if(nanoBean.getDomainEntity().get("crossLinkDegree")!=null)
					poly.setCrossLinkDegree(new Float((String) nanoBean.getDomainEntity().get("crossLinkDegree")));
				if(nanoBean.getDomainEntity().get("initiator")!=null)
					poly.setInitiator((String) nanoBean.getDomainEntity().get("initiator"));
				if(nanoBean.getDomainEntity().get("id")!=null){
					poly.setId(new Long((Integer) nanoBean.getDomainEntity().get("id")));
					poly.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
					poly.setCreatedDate(new Date((Long) nanoBean.getDomainEntity().get("createdDate")));
					poly.setSampleComposition(sampleComp);
				}else{
					poly.setSampleComposition(null);
				}
			}
			poly.setComposingElementCollection(coll);
			poly.setFileCollection(filecoll);
			bean.setPolymer(poly);
			nanoEntity = poly;
		}
			
		else if(nanoBean.getType().equalsIgnoreCase("carbon nanotube")){
			CarbonNanotube ctube = new CarbonNanotube();
			if(nanoBean.getDomainEntity()!=null){
				if(nanoBean.getDomainEntity().get("averageLength")!=null)
				ctube.setAverageLength(new Float((String) nanoBean.getDomainEntity().get("averageLength")));
				if(nanoBean.getDomainEntity().get("averageLengthUnit")!=null)
				ctube.setAverageLengthUnit((String) nanoBean.getDomainEntity().get("averageLengthUnit"));
				if(nanoBean.getDomainEntity().get("chirality")!=null)
				ctube.setChirality((String) nanoBean.getDomainEntity().get("chirality"));
				if(nanoBean.getDomainEntity().get("diameter")!=null)
				ctube.setDiameter(new Float((String) nanoBean.getDomainEntity().get("diameter")));
				if(nanoBean.getDomainEntity().get("diameterUnit")!=null)
				ctube.setDiameterUnit((String) nanoBean.getDomainEntity().get("diameterUnit"));
				if(nanoBean.getDomainEntity().get("wallType")!=null)
				ctube.setWallType((String) nanoBean.getDomainEntity().get("wallType"));
				if(nanoBean.getDomainEntity().get("id")!=null){
					ctube.setId(new Long((Integer) nanoBean.getDomainEntity().get("id")));
					ctube.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
					ctube.setCreatedDate(new Date((Long) nanoBean.getDomainEntity().get("createdDate")));
					ctube.setSampleComposition(sampleComp);
				}else{
					ctube.setSampleComposition(null);
				}
			}
			ctube.setComposingElementCollection(coll);
			ctube.setFileCollection(filecoll);
			bean.setCarbonNanotube(ctube);
			nanoEntity = ctube;
			
		}else{
			nanoEntity = new OtherNanomaterialEntity();
			if(nanoBean.getDomainEntity()!=null){
			if(nanoBean.getDomainEntity().get("id")!=null){
				nanoEntity.setId(new Long((Integer) nanoBean.getDomainEntity().get("id")));
				nanoEntity.setCreatedBy((String) nanoBean.getDomainEntity().get("createdBy"));
				nanoEntity.setCreatedDate((Date) nanoBean.getDomainEntity().get("createdDate"));
				nanoEntity.setSampleComposition(sampleComp);

			}else
				nanoEntity.setSampleComposition(null);
			}
			nanoEntity.setComposingElementCollection(coll);
			
			nanoEntity.setFileCollection(filecoll);
		}
			
		bean.setComposingElements(compList);
		bean.setFiles(fileBeanList);
		bean.setType(nanoBean.getType());
		bean.setDescription(nanoBean.getDescription());
		bean.setDomainEntity(nanoEntity);
		
		return bean;
	}
	

	public SimpleNanomaterialEntityBean removeComposingElement(SimpleNanomaterialEntityBean nanoBean, HttpServletRequest request) throws Exception {
		List<String> msgs = new ArrayList<String>();
		NanomaterialEntityBean entity = transferNanoMateriaEntityBean(nanoBean, request);
		ComposingElementBean composingElement = entity.getTheComposingElement();
		// check if composing element is associated with an association
		CompositionServiceLocalImpl compService = (CompositionServiceLocalImpl) (this
				.setServicesInSession(request));
		SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
		if (!compService.checkChemicalAssociationBeforeDelete(entity
				.getDomainEntity().getSampleComposition(), composingElement
				.getDomain())) {
			throw new ChemicalAssociationViolationException(
					"The composing element is used in a chemical association.  Please delete the chemcial association first before deleting the nanomaterial entity.");
		}
		entity.removeComposingElement(composingElement);
		msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
			nano.setErrors(msgs);
			return nano;
		}
		this.saveEntity(request, nanoBean.getSampleId(), entity);
		compService.removeAccesses(entity.getDomainEntity(),
				composingElement.getDomain());
		this.checkOpenForms(entity, request);
		return setupUpdate(nanoBean.getSampleId(), entity.getDomainEntity().getId()
				.toString(), request);
	}

	public SimpleNanomaterialEntityBean saveFile(SimpleNanomaterialEntityBean nanoBean,
			HttpServletRequest request)
			throws Exception {
		NanomaterialEntityBean entity = transferNanoMateriaEntityBean(nanoBean, request);
		FileBean theFile = entity.getTheFile();
		this.setServicesInSession(request);
		SampleBean sampleBean = setupSampleById(nanoBean.getSampleId(), request); 
		// setup domainFile uri for fileBean
		String internalUriPath = Constants.FOLDER_PARTICLE + '/'
				+ sampleBean.getDomain().getName() + '/' + "nanomaterialEntity";
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		
		String timestamp = DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute("newFileData");
		if(!theFile.getDomainFile().getUriExternal()){
			if(newFileData!=null){
				theFile.setNewFileData((byte[]) request.getSession().getAttribute("newFileData"));
				theFile.getDomainFile().setUri(Constants.FOLDER_PARTICLE + '/'
						+ sampleBean.getDomain().getName() + '/' + "nanomaterialEntity"+ "/" + timestamp + "_"
						+ theFile.getDomainFile().getName());
			}else if(theFile.getDomainFile().getId()!=null){
				theFile.getDomainFile().setUri(theFile.getDomainFile().getName());
			}else{
				theFile.getDomainFile().setUri(null);
			}
		}
		entity.addFile(theFile);
		
		// restore previously uploaded file from session.
		//restoreUploadedFile(request, theFile);

		// save nanomaterial entity to save file because inverse="false"
		List<String> msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
			SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
			nano.setErrors(msgs);
			return nano;
		}
		this.saveEntity(request,nanoBean.getSampleId(), entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		 request.getSession().removeAttribute("newFileData");

		return setupUpdate(nanoBean.getSampleId(), entity.getDomainEntity().getId()
				.toString(), request);
	}

	public SimpleNanomaterialEntityBean removeFile(SimpleNanomaterialEntityBean nanoBean,
			HttpServletRequest request)
			throws Exception {
		NanomaterialEntityBean entity = transferNanoMateriaEntityBean(nanoBean, request); 

		FileBean theFile = entity.getTheFile();
		entity.removeFile(theFile);
		entity.setTheFile(new FileBean());
		// save nanomaterial entity
		List<String> msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
			SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
			nano.setErrors(msgs);
			return nano;
		}
		this.saveEntity(request, nanoBean.getSampleId(), entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());
		request.setAttribute("anchor", "file");
		this.checkOpenForms(entity, request);
		return setupUpdate(nanoBean.getSampleId(), entity.getDomainEntity().getId()
				.toString(), request);
	}

	public List<String> delete(SimpleNanomaterialEntityBean nanoBean,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		CompositionService compositionService = this
				.setServicesInSession(request);
		NanomaterialEntityBean entityBean = transferNanoMateriaEntityBean(nanoBean, request);
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		entityBean.setupDomainEntity(user.getLoginName());
		compositionService.deleteNanomaterialEntity(entityBean
				.getDomainEntity());
		compositionService.removeAccesses(entityBean.getDomainEntity());

		msgs.add("success");
		return msgs;
	}

	private void checkOpenForms(NanomaterialEntityBean entity,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
//		Boolean openFile = false, openComposingElement = false;
//		if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
//			openFile = true;
//		}
//		session.setAttribute("openFile", openFile);
//		if (dispatch.equals("input")
//				&& browserDispatch.equals("saveComposingElement")
//				|| ((dispatch.equals("setupNew") || dispatch
//						.equals("setupUpdate")) && entity
//						.getComposingElements().isEmpty())) {
//			openComposingElement = true;
//		}
//		session.setAttribute("openComposingElement", openComposingElement);

		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entity);
		/**
		 * other nanomaterial entity types are not stored in the lookup are
		 * retrieved through reflection only after saving to the database. Need
		 * to update session variable before saving to the database
		 */
		// Nanomaterial Entity Type
		String entityType = entity.getType();
		setOtherValueOption(request, entityType, "nanomaterialEntityTypes");
		// function type
		String functionType = entity.getTheComposingElement().getTheFunction()
				.getType();
		setOtherValueOption(request, functionType, "functionTypes");

		String detailPage = null;
		if (entity.isWithProperties()) {
			if (!StringUtils.isEmpty(entity.getType())) {
				detailPage = InitCompositionSetup.getInstance().getDetailPage(
						entity.getType(), "nanomaterialEntity");
			}
			request.setAttribute("entityDetailPage", detailPage);
		}
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

	public String download(String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CompositionService service = (CompositionService) (request.getSession()
				.getAttribute("compositionService"));
		return downloadFile(service, fileId, request, response);
	}
	public NanomaterialEntityBean setupNanoEntityForAdvSearch(String sampleId, String entityId,
			HttpServletRequest request)
			throws Exception {
	//	entityId = super.validateId(request, "dataId");
		CompositionForm form = new CompositionForm();
		// set up other particles with the same primary point of contact
		CompositionService compService = this.setServicesInSession(request);
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId);
		form.setNanomaterialEntity(entityBean);
		form.setOtherSamples(new String[0]);
		this.checkOpenForms(entityBean, request);
		request.getSession().setAttribute("sampleId", sampleId);
//		SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
//		nano.transferNanoMaterialEntityBeanToSimple(entityBean, request);
		return entityBean;
	}
}
