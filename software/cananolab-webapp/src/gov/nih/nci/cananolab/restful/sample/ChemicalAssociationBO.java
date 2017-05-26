package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.linkage.Attachment;
import gov.nih.nci.cananolab.domain.particle.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.AssociatedElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.BaseCompositionEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.CompositionUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleChemicalAssociationBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.ui.form.CompositionForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.domain.linkage.Attachment;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("chemicalAssociationBO")
public class ChemicalAssociationBO extends BaseAnnotationBO
{
	@Autowired
	private CurationService curationServiceDAO;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private CompositionService compositionService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	public List<String> create(SimpleChemicalAssociationBean bean,
			HttpServletRequest request)
			throws Exception {

		List<String> msgs = new ArrayList<String>();
		ChemicalAssociationBean assocBean = transferChemicalAssociationBean(bean, request);
		 msgs = validateAssociatedElements(assocBean);
		 msgs = validateComposingElements(assocBean, msgs);
		 msgs = validateAssociationFile(request, msgs, assocBean);
		if (msgs.size()>0) {
			return msgs;
		}
		
		saveAssociation(request, bean.getSampleId(), assocBean);
		msgs.add("success");
		// save action messages in the session so composition.do know about them
		// to preselect chemical association after returning to the summary page
		request.getSession().setAttribute("tab", "3");
		return msgs;
	}
	/**
	 *  Added if the user misses to pick the composingElement for the nanomaterial entity
	 * @param assocBean
	 * @return
	 */
	private List<String> validateComposingElements(
			ChemicalAssociationBean assocBean, List<String> msgs) {
		 String compTypeA = assocBean.getAssociatedElementA().getCompositionType();
		 String compTypeB = assocBean.getAssociatedElementB().getCompositionType();
			
		 if((compTypeA!=null)&&(compTypeA.equalsIgnoreCase("nanomaterial entity"))&&(assocBean.getAssociatedElementA().getEntityDisplayName()!=null)){
				if(assocBean.getAssociatedElementA().getComposingElement().getId()==null)
						msgs.add("Choosing an element on the left is required.");
			}
			if((compTypeB!=null)&&(compTypeB.equalsIgnoreCase("nanomaterial entity"))&&(assocBean.getAssociatedElementB().getEntityDisplayName()!=null)){
				if(assocBean.getAssociatedElementB().getComposingElement().getId()==null)
						msgs.add("Choosing an element on the right is required.");
			}
			return msgs;
	}

	private ChemicalAssociationBean transferChemicalAssociationBean(
			SimpleChemicalAssociationBean bean, HttpServletRequest request) {
		
		ChemicalAssociationBean chemBean = new ChemicalAssociationBean();
		ChemicalAssociation chemAssociation = null;
		AssociatedElementBean associatedElementA = new AssociatedElementBean();
		AssociatedElementBean associatedElementB = new AssociatedElementBean();
		ComposingElement comp = new ComposingElement();
		Collection<File> filecoll = new HashSet<File>();		
		
		//setting up associatedElementA
		if(bean.getAssociatedElementA()!=null){
			if(bean.getAssociatedElementA().getComposingElement()!=null){
				if(bean.getAssociatedElementA().getComposingElement().getId()!=null)
				comp.setId(bean.getAssociatedElementA().getComposingElement().getId());
			}
			chemBean.setType(bean.getType());
			chemBean.setDescription(bean.getDescription());
			associatedElementA.setCompositionType(bean.getAssociatedElementA().getCompositionType());
			associatedElementA.setEntityDisplayName(bean.getAssociatedElementA().getEntityDisplayName());
			if(bean.getAssociatedElementA().getEntityId()!=null)
			associatedElementA.setEntityId(bean.getAssociatedElementA().getEntityId());
			
			associatedElementA.setComposingElement(comp);
			chemBean.setAssociatedElementA(associatedElementA);
		}
		//setting up AssociatedElementB
		if(bean.getAssociatedElementB()!=null){
			if(bean.getAssociatedElementB().getComposingElement()!=null){
				comp = new ComposingElement();
				if(bean.getAssociatedElementB().getComposingElement().getId()!=null)
					comp.setId(bean.getAssociatedElementB().getComposingElement().getId());
			}
			associatedElementB.setCompositionType(bean.getAssociatedElementB().getCompositionType());
			associatedElementB.setEntityDisplayName(bean.getAssociatedElementB().getEntityDisplayName());
			if(bean.getAssociatedElementB().getEntityId()!=null)
			associatedElementB.setEntityId(bean.getAssociatedElementB().getEntityId());
			associatedElementB.setComposingElement(comp);
			chemBean.setAssociatedElementB(associatedElementB);
		}
		//Setting up theFile
		//setting up theFile
				SimpleFileBean fBean = bean.getSimpleFile();
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
					fileBean.setExternalUrl(fBean.getExternalUrl());
					fileBean.setKeywordsStr(fBean.getKeywordsStr());
					fileBean.setDomainFile(file);
				}
				chemBean.setTheFile(fileBean);
				
				//setting up files
				List<SimpleFileBean> filelist =  bean.getFiles();
				
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
					file.setKeywordCollection(new HashSet<Keyword>());
					file.setCreatedDate(sFBean.getCreatedDate());
					file.setUriExternal(sFBean.getUriExternal());
					fileBean.setExternalUrl(sFBean.getExternalUrl());
					fileBean.setKeywordsStr(sFBean.getKeywordsStr());
					if(!StringUtils.isEmpty(sFBean.getKeywordsStr())){
						String[] strs = sFBean.getKeywordsStr().split("\r\n");
						for (String str : strs) {
							// change to upper case
							Keyword keyword = new Keyword();
							keyword.setName(str.toUpperCase());
							file.getKeywordCollection().add(keyword);
						}
					}
					fileBean.setDomainFile(file);
					filecoll.add(file);
					fileBeanList.add(fileBean);
				}
				}
				chemBean.setFiles(fileBeanList);
				
				//setting up chemical association
				
				AssociatedElement assoA = new AssociatedElement();
				if(bean.getAssociatedElementA()!=null){
				if(bean.getAssociatedElementA().getComposingElement()!=null){
					if(bean.getAssociationId()>0){
						assoA.setCreatedBy(bean.getAssociatedElementA().getComposingElement().getCreatedBy());
						assoA.setCreatedDate(bean.getAssociatedElementA().getComposingElement().getCreatedDate());
						assoA.setDescription(bean.getAssociatedElementA().getComposingElement().getDescription());
						if(bean.getAssociatedElementA().getComposingElement().getId()!=null)
							assoA.setId(bean.getAssociatedElementA().getComposingElement().getId());
						assoA.setMolecularFormula(bean.getAssociatedElementA().getComposingElement().getMolecularFormula());
						assoA.setMolecularFormulaType(bean.getAssociatedElementA().getComposingElement().getMolecularFormulaType());
						assoA.setName(bean.getAssociatedElementA().getComposingElement().getName());
						assoA.setPubChemDataSourceName(bean.getAssociatedElementA().getComposingElement().getPubChemDataSourceName());
						assoA.setPubChemId(bean.getAssociatedElementA().getComposingElement().getPubChemId());
						assoA.setValue(bean.getAssociatedElementA().getComposingElement().getValue());
						assoA.setValueUnit(bean.getAssociatedElementA().getComposingElement().getValueUnit());
					}else{
						if(bean.getAssociatedElementA().getComposingElement().getId()!=null)
							assoA.setId(bean.getAssociatedElementA().getComposingElement().getId());
					}
				}
				}
				AssociatedElement assoB = new AssociatedElement();

				if(bean.getAssociatedElementB()!=null){
					if(bean.getAssociatedElementB().getComposingElement()!=null){
						if(bean.getAssociationId()>0){
							assoB.setCreatedBy(bean.getAssociatedElementB().getComposingElement().getCreatedBy());
							assoB.setCreatedDate(bean.getAssociatedElementB().getComposingElement().getCreatedDate());
							assoB.setDescription(bean.getAssociatedElementB().getComposingElement().getDescription());
							if(bean.getAssociatedElementB().getComposingElement().getId()!=null)
								assoB.setId(bean.getAssociatedElementB().getComposingElement().getId());
							assoB.setMolecularFormula(bean.getAssociatedElementB().getComposingElement().getMolecularFormula());
							assoB.setMolecularFormulaType(bean.getAssociatedElementB().getComposingElement().getMolecularFormulaType());
							assoB.setName(bean.getAssociatedElementB().getComposingElement().getName());
							assoB.setPubChemDataSourceName(bean.getAssociatedElementB().getComposingElement().getPubChemDataSourceName());
							assoB.setPubChemId(bean.getAssociatedElementB().getComposingElement().getPubChemId());
							assoB.setValue(bean.getAssociatedElementB().getComposingElement().getValue());
							assoB.setValueUnit(bean.getAssociatedElementB().getComposingElement().getValueUnit());
						}else{
							if(bean.getAssociatedElementB().getComposingElement().getId()!=null)
								assoB.setId(bean.getAssociatedElementB().getComposingElement().getId());
						}
					}
					}
				
				//setting up sampleComposition 
				//Managed to get the sampleComposition in the backend to avoid lazy loading things
				SampleComposition sampleComp = null;
				if(bean.getAssociationId()>0){
				try {
							sampleComp = compositionService.getHelper().findCompositionBySampleId(bean.getSampleId()
									.toString());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
				}
				if(bean.getType().equalsIgnoreCase("attachment")){
					Attachment att = new Attachment();
					att.setAssociatedElementA(assoA);
					att.setAssociatedElementB(assoB);
					att.setFileCollection(filecoll);
					att.setBondType(bean.getBondType());
					if(bean.getAssociationId()>0){
						att.setId(bean.getAssociationId());
						att.setCreatedBy(bean.getCreatedBy());
						att.setCreatedDate(bean.getCreatedDate());
						att.setSampleComposition(sampleComp);
					}else{
						att.setSampleComposition(null);
					}
					chemBean.setAttachment(att);
					chemAssociation = att;
					
				}else{
					chemAssociation = new OtherChemicalAssociation();
					chemAssociation.setAssociatedElementA(assoA);
					chemAssociation.setAssociatedElementA(assoB);
					chemAssociation.setFileCollection(filecoll);
					if(bean.getAssociationId()>0){
						chemAssociation.setId(bean.getAssociationId());
						chemAssociation.setCreatedBy(bean.getCreatedBy());
						chemAssociation.setCreatedDate(bean.getCreatedDate());
						chemAssociation.setSampleComposition(sampleComp);
					}else{
						chemAssociation.setSampleComposition(null);
					}
					chemBean.setDomainAssociation(chemAssociation);
				}
		
				return chemBean;
	}

	private List<String> validateAssociationFile(HttpServletRequest request, List<String> msgs,
			ChemicalAssociationBean entityBean) throws Exception {
		for (FileBean filebean : entityBean.getFiles()) {
			msgs = validateFileBean(request, msgs, filebean);
			if (msgs.size()>0) {
				return msgs;
			}
		}
		return msgs;
	}

	private List<String> validateAssociatedElements(ChemicalAssociationBean assocBean)
			throws Exception {
		// validate if composing element is null
		// if (assocBean.getAssociatedElementA().getComposingElement().getId()
		// == null) {
		// ActionMessage msg = new ActionMessage(
		// "error.nullComposingElementAInAssociation");
		// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// saveErrors(request, msgs);
		// noErrors = false;
		// }
		// if (assocBean.getAssociatedElementB().getDomainElement() instanceof
		// ComposingElement
		// && assocBean.getAssociatedElementB().getDomainElement().getId() ==
		// null) {
		// ActionMessage msg = new ActionMessage(
		// "error.nullComposingElementBInAssociation");
		// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// saveErrors(request, msgs);
		// noErrors = false;
		// }
		// if (!noErrors) {
		// return mapping.getInputForward();
		// }
		// validate if the same associated elements are chosen on both sides
		//boolean noErrors = true;
		List<String> msgs = new ArrayList<String>();

		if(assocBean.getType()==null||assocBean.getType().equals("")){
			msgs.add("Chemical Association Type is required.");
		}
		String entityTypeA = assocBean.getAssociatedElementA()
				.getEntityDisplayName();
		String entityIdA = assocBean.getAssociatedElementA().getEntityId();
		String entityTypeB = assocBean.getAssociatedElementB()
				.getEntityDisplayName();
		String entityIdB = assocBean.getAssociatedElementB().getEntityId();
		
		if((entityTypeA!=null)&&(entityIdA!=null)&&(entityTypeB!=null)&&(entityIdB!=null)){
		if (entityTypeA.equals(entityTypeB) && entityIdA.equals(entityIdB)) {
			//noErrors = false;
			msgs.add(PropertyUtil.getProperty("sample", "error.duplicateAssociatedElementsInAssociation"));
		}
		}else {
			if((entityTypeA==null||entityTypeA.equals(""))&&(entityIdA==null)){
				msgs.add("Choosing an element on the left is required.");
			}
			if((entityTypeB==null||entityTypeB.equals(""))&&(entityIdB==null)){
				msgs.add("Choosing an element on the right is required.");
			}
		}
		return msgs;
	}

	public List<String> saveAssociation(HttpServletRequest request, String sampleId, ChemicalAssociationBean assocBean)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(sampleId, request);
		Boolean newAssoc = true;
		try {
			assocBean.setupDomainAssociation(SpringSecurityUtil.getLoggedInUserName());
			if (assocBean.getDomainAssociation().getId() != null) {
				newAssoc = false;
			}
		} catch (ClassCastException ex) {
			if (!StringUtils.isEmpty(ex.getMessage())
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msgs.add(ex.getMessage()+ "is an invalid Chemical Association type. It is a pre-defined composition type.");
			} else {
				msgs.add(assocBean.getType()+ "is an invalid Chemical Association type. It is a pre-defined composition type.");
				assocBean.setType(null);
			}
		}
		compositionService.saveChemicalAssociation(sampleBean, assocBean);
		// retract from public if updating an existing public record and not
		// curator
		if (!newAssoc && !SpringSecurityUtil.getPrincipal().isCurator() && 
			springSecurityAclService.checkObjectPublic(Long.valueOf(sampleId), SecureClassesEnum.SAMPLE.getClazz()))
		{
			retractFromPublic(request, sampleBean.getDomain().getId(), 
							  sampleBean.getDomain().getName(), "sample", SecureClassesEnum.SAMPLE.getClazz());
			msgs.add(PropertyUtil.getProperty("sample", "message.updateSample.retractFromPublic"));
		}
		Boolean hasFunctionalizingEntity = (Boolean) request.getSession().getAttribute("hasFunctionalizingEntity");
		InitCompositionSetup.getInstance().persistChemicalAssociationDropdowns(
				request, assocBean, hasFunctionalizingEntity);
		return msgs;
	}

	/**
	 * Set up the input form for adding new chemical association
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
		ChemicalAssociationBean assocBean = new ChemicalAssociationBean();
		CompositionBean compositionBean = compositionService.findCompositionBySampleId(sampleId);
		List<String> errors= new ArrayList<String>();
		errors = validateComposition(compositionBean, request);
		if (errors.size()>0) {
			Map<String, Object> errorMap = new HashMap<String, Object>();
			errorMap.put("errors", errors);
			return errorMap;
			}
		request.getSession().removeAttribute("compositionForm");
		setLookups(request, compositionBean);
		this.checkOpenForms(assocBean, request);
		return CompositionUtil.reformatLocalSearchDropdownsInSessionForChemicalAssociation(request.getSession());
	}

	public List<String> validateComposition(CompositionBean compositionBean, HttpServletRequest request) throws Exception
	{
		List<String> msgs = new ArrayList<String>();
		// if no composition return to summary view page
		if (compositionBean == null) {
			msgs.add(PropertyUtil.getProperty("sample", "message.nullComposition"));
			return msgs;
		}
		// check if sample has the required nanomaterial entities
		if (compositionBean.getNanomaterialEntities().isEmpty()) {
			msgs.add(PropertyUtil.getProperty("sample", "message.emptyMaterialsEntitiesInAssociation"));
			return msgs;
		}
		// check whether nanomaterial entities has composing elements
		int numberOfCE = 0;
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			if (!entityBean.getComposingElements().isEmpty()) {
				numberOfCE += entityBean.getComposingElements().size();
			}
		}
		if (numberOfCE == 0) {
			msgs.add(PropertyUtil.getProperty("sample", "message.emptyComposingElementsInAssociation"));
			return msgs;
		}
		// check whether it has a functionalizing entity
		boolean hasFunctionalizingEntity = false;
		if (!compositionBean.getFunctionalizingEntities().isEmpty()) {
			hasFunctionalizingEntity = true;
		}
		if (!hasFunctionalizingEntity && numberOfCE == 1) {
			msgs.add(PropertyUtil.getProperty("sample", "message.oneComposingElementsInAssociation"));
			return msgs;
		}
		if (!hasFunctionalizingEntity) {
			msgs.add(PropertyUtil.getProperty("sample", "message.emptyFunctionalizingEntityInAssociation"));
			return msgs;
		}
		return msgs;
	}
	
	//unused code
	/*public void input(CompositionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ChemicalAssociationBean assocBean = form.getAssoc();
		escapeXmlForFileUri(assocBean.getTheFile());
		prepareEntityLists(assocBean, request);
		HttpSession session = request.getSession();
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			throw new InvalidSessionException();
		}
		Boolean hasFunctionalizingEntities = (Boolean) session.getAttribute("hasFunctionalizingEntity");
		InitCompositionSetup.getInstance().persistChemicalAssociationDropdowns(
				request, assocBean, hasFunctionalizingEntities);

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = assocBean.getTheFile();
		//preserveUploadedFile(request, theFile, "Chemical Association");

		this.checkOpenForms(assocBean, request);
		//return mapping.findForward("inputForm");
	}*/

	private void setLookups(HttpServletRequest request, CompositionBean compositionBean) throws Exception
	{
		// check whether it has a functionalizing entity
		boolean hasFunctionalizingEntity = false;
		if (!compositionBean.getFunctionalizingEntities().isEmpty()) {
			hasFunctionalizingEntity = true;
		}
		InitCompositionSetup.getInstance().setChemicalAssociationDropdowns(
				request, hasFunctionalizingEntity);
		// use BaseCompositionEntityBean for DWR ajax
		List<BaseCompositionEntityBean> materialEntities = new ArrayList<BaseCompositionEntityBean>();
		for (NanomaterialEntityBean entityBean : compositionBean.getNanomaterialEntities()) {
			if (!entityBean.getComposingElements().isEmpty()) {
				materialEntities.add(entityBean);
			}
		}
		request.getSession().setAttribute("sampleMaterialEntities", materialEntities);
		request.getSession().setAttribute("sampleFunctionalizingEntities", compositionBean.getFunctionalizingEntities());
		request.getSession().setAttribute("hasFunctionalizingEntity", hasFunctionalizingEntity);
	}

	public void prepareEntityLists(String sampleId, ChemicalAssociationBean assocBean, HttpServletRequest request) throws Exception
	{
		HttpSession session = request.getSession();
		// associated element A
		List<BaseCompositionEntityBean> entityListA = null;
		List<ComposingElementBean> ceListA = new ArrayList<ComposingElementBean>();
		if (assocBean.getAssociatedElementA().getCompositionType().equals("nanomaterial entity")) {
			entityListA = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session.getAttribute("sampleMaterialEntities"));
			// get composing elements
			NanomaterialEntityBean entityBean = compositionService.findNanomaterialEntityById(sampleId, assocBean
							.getAssociatedElementA().getEntityId());
			ceListA = entityBean.getComposingElements();
		} else {
			entityListA = new ArrayList<BaseCompositionEntityBean>((List<BaseCompositionEntityBean>) session
							.getAttribute("sampleFunctionalizingEntities"));
		}
		session.setAttribute("ceListA", ceListA);
		session.setAttribute("entityListA", entityListA);
		// associated element B
		List<BaseCompositionEntityBean> entityListB = null;
		List<ComposingElementBean> ceListB = new ArrayList<ComposingElementBean>();
		if (assocBean.getAssociatedElementB().getCompositionType().equals("nanomaterial entity")) {
			entityListB = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session.getAttribute("sampleMaterialEntities"));
			// get composing elements
			NanomaterialEntityBean entityBean = compositionService.findNanomaterialEntityById(sampleId, assocBean.getAssociatedElementB().getEntityId());
			ceListB = entityBean.getComposingElements();
		} else {
			entityListB = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session.getAttribute("sampleFunctionalizingEntities"));
		}
		session.setAttribute("ceListB", ceListB);
		session.setAttribute("entityListB", entityListB);
	}

	public SimpleChemicalAssociationBean setupUpdate(String sampleId, String assocId, HttpServletRequest request)
			throws Exception {
		// set up compositionBean required to set up drop-down
		CompositionBean compositionBean = compositionService.findCompositionBySampleId(sampleId);
		setLookups(request, compositionBean);
		//String assocId = super.validateId(request, "dataId");
		ChemicalAssociationBean assocBean = compositionService.findChemicalAssociationById(sampleId, assocId);
		prepareEntityLists(sampleId, assocBean, request);
		//theForm.set("assoc", assocBean);
		this.checkOpenForms(assocBean, request);
	//	return mapping.findForward("inputForm");
		request.getSession().setAttribute("sampleId", sampleId);
		SimpleChemicalAssociationBean chemBean = new SimpleChemicalAssociationBean();
		chemBean.trasferToSimpleChemicalAssociation(assocBean, request, springSecurityAclService);
		return chemBean;
	}

	public SimpleChemicalAssociationBean saveFile(SimpleChemicalAssociationBean bean, HttpServletRequest request)
			throws Exception {
		ChemicalAssociationBean assoc = transferChemicalAssociationBean(bean, request);
		FileBean theFile = assoc.getTheFile();
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(bean.getSampleId(), request);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/"
				+ "chemicalAssociation";
		theFile.setupDomainFile(internalUriPath, SpringSecurityUtil.getLoggedInUserName());
		
		String timestamp = DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute("newFileData");
		if(!theFile.getDomainFile().getUriExternal()){
			if(newFileData!=null){
				theFile.setNewFileData((byte[]) request.getSession().getAttribute("newFileData"));
				theFile.getDomainFile().setUri(Constants.FOLDER_PARTICLE + '/'
						+ sampleBean.getDomain().getName() + '/' + "chemicalAssociation"+ "/" + timestamp + "_"
						+ theFile.getDomainFile().getName());
			}else if(theFile.getDomainFile().getId()!=null){
				theFile.getDomainFile().setUri(theFile.getDomainFile().getName());
			}else{
				theFile.getDomainFile().setUri(null);
			}
		}
		assoc.addFile(theFile);

		// restore previously uploaded file from session.
		//this.restoreUploadedFile(request, theFile);
		msgs = validateAssociatedElements(assoc);
		msgs = validateComposingElements(assoc, msgs);
		msgs = validateAssociationFile(request, msgs, assoc);
		
		SimpleChemicalAssociationBean simpleAsso = new SimpleChemicalAssociationBean();
			if (msgs.size()>0) {
				simpleAsso.setErrors(msgs);
				return simpleAsso;
			}
		// save the association
		saveAssociation(request, bean.getSampleId(), assoc);
		// comp service has already been created
		compositionService.assignAccesses(assoc.getDomainAssociation().getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		this.checkOpenForms(assoc, request);
		request.getSession().removeAttribute("newFileData");
		request.setAttribute("dataId", assoc.getDomainAssociation().getId().toString());
		return setupUpdate(bean.getSampleId(), assoc.getDomainAssociation().getId().toString(), request);
	}

	public SimpleChemicalAssociationBean removeFile(SimpleChemicalAssociationBean bean,
			HttpServletRequest request)
			throws Exception {
		ChemicalAssociationBean assoc = transferChemicalAssociationBean(bean, request);
		FileBean theFile = assoc.getTheFile();
		assoc.removeFile(theFile);
		assoc.setTheFile(new FileBean());
		request.setAttribute("anchor", "file");
		// save the association
		saveAssociation(request, bean.getSampleId(), assoc);
		// comp service has already been created
		compositionService.removeAccesses(assoc.getDomainAssociation().getSampleComposition(), theFile.getDomainFile());

		this.checkOpenForms(assoc, request);
	//	return mapping.findForward("inputForm");
		return setupUpdate(bean.getSampleId(), assoc.getDomainAssociation().getId()
				.toString(), request);
	}

	public List<String> delete(SimpleChemicalAssociationBean bean, HttpServletRequest request)
			throws Exception {
		
		List<String> msgs = new ArrayList<String>();
		ChemicalAssociationBean assocBean = transferChemicalAssociationBean(bean, request);

		assocBean.setupDomainAssociation(SpringSecurityUtil.getLoggedInUserName());

		compositionService.deleteChemicalAssociation(assocBean.getDomainAssociation());
		// TODO remove accessibility

		msgs.add("success");
		return msgs;
	}

	private void checkOpenForms(ChemicalAssociationBean assoc, HttpServletRequest request) {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean openFile = false;
//		if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
//			openFile = true;
//		}
		session.setAttribute("openFile", openFile);
		/**
		 * If user entered customized value selecting [other] on previous page,
		 * we should show and highlight the entered value on the edit page.
		 */
		// Association Type
		String assocType = assoc.getType();
		setOtherValueOption(request, assocType, "chemicalAssociationTypes");
		// Bond Type
		String bondType = assoc.getAttachment().getBondType();
		setOtherValueOption(request, bondType, "bondTypes");
		// File Type
		String fileType = assoc.getTheFile().getDomainFile().getType();
		setOtherValueOption(request, fileType, "fileTypes");

		// Feature request [26487] Deeper Edit Links.
		if ("setupUpdate".equals(dispatch)) {
			List<FileBean> files = assoc.getFiles();
			if (files.size() == 1) {
				FileBean fileBean = files.get(0);
				StringBuilder sb = new StringBuilder();
				sb.append("setTheFile('assoc', ");
				sb.append(fileBean.getDomainFile().getId());
				sb.append(')');
				request.setAttribute("onloadJavascript", sb.toString());
			}
		}
	}

	@Override
	public CurationService getCurationServiceDAO() {
		return this.curationServiceDAO;
	}

	@Override
	public SampleService getSampleService() {
		return this.sampleService;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

	@Override
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	
}


