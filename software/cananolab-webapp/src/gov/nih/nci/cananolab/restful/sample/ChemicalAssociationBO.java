package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.AssociatedElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.BaseCompositionEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.CompositionUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleChemicalAssociationBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.CompositionForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class ChemicalAssociationBO extends BaseAnnotationBO{
	public List<String> create(SimpleChemicalAssociationBean bean,
			HttpServletRequest request)
			throws Exception {

		List<String> msgs = new ArrayList<String>();
		ChemicalAssociationBean assocBean = transferChemicalAssociationBean(bean);

		 msgs = validateAssociationFile(request, assocBean);
		if (msgs.size()>0) {
			return msgs;
		}
		 msgs = validateAssociatedElements(assocBean);
		if (msgs.size()>0) {
			msgs.add(PropertyUtil.getProperty("sample", "error.duplicateAssociatedElementsInAssociation"));
			return msgs;
		}
		this.setServicesInSession(request);
		saveAssociation(request, bean.getSampleId(), assocBean);
		msgs.add("success");
		// save action messages in the session so composition.do know about them
		// to preselect chemical association after returning to the summary page
		request.getSession().setAttribute("tab", "3");
		return msgs;
	}

	private ChemicalAssociationBean transferChemicalAssociationBean(
			SimpleChemicalAssociationBean bean) {
		
		ChemicalAssociationBean chemBean = new ChemicalAssociationBean();
		AssociatedElementBean associatedElementA = new AssociatedElementBean();
		AssociatedElementBean associatedElementB = new AssociatedElementBean();
		ComposingElement comp = new ComposingElement();
		
		comp.setCreatedBy(bean.getAssociatedElementA().getComposingElement().getCreatedBy());
		comp.setCreatedDate(bean.getAssociatedElementA().getComposingElement().getCreatedDate());
		comp.setDescription(bean.getAssociatedElementA().getComposingElement().getDescription());
		if(bean.getAssociatedElementA().getComposingElement().getId()!=null)
		comp.setId(bean.getAssociatedElementA().getComposingElement().getId());
		comp.setMolecularFormula(bean.getAssociatedElementA().getComposingElement().getMolecularFormula());
		comp.setMolecularFormulaType(bean.getAssociatedElementA().getComposingElement().getMolecularFormulaType());
		comp.setName(bean.getAssociatedElementA().getComposingElement().getName());
		comp.setPubChemDataSourceName(bean.getAssociatedElementA().getComposingElement().getPubChemDataSourceName());
		comp.setPubChemId(bean.getAssociatedElementA().getComposingElement().getPubChemId());
		comp.setType(bean.getAssociatedElementA().getComposingElement().getType());
		comp.setValue(bean.getAssociatedElementA().getComposingElement().getValue());
		comp.setValueUnit(bean.getAssociatedElementA().getComposingElement().getValueUnit());
		
		chemBean.setType(bean.getType());
		chemBean.setDescription(bean.getDescription());
		associatedElementA.setCompositionType(bean.getAssociatedElementA().getCompositionType());
		associatedElementA.setEntityDisplayName(bean.getAssociatedElementA().getEntityDisplayName());
		if(bean.getAssociatedElementA().getEntityId()!=null)
		associatedElementA.setEntityId(bean.getAssociatedElementA().getEntityId());
		
		
		associatedElementA.setComposingElement(comp);
		chemBean.setAssociatedElementA(associatedElementA);
		comp = new ComposingElement();
		
		comp.setCreatedBy(bean.getAssociatedElementB().getComposingElement().getCreatedBy());
		comp.setCreatedDate(bean.getAssociatedElementB().getComposingElement().getCreatedDate());
		comp.setDescription(bean.getAssociatedElementB().getComposingElement().getDescription());
		if(bean.getAssociatedElementB().getComposingElement().getId()!=null)
		comp.setId(bean.getAssociatedElementB().getComposingElement().getId());
		comp.setMolecularFormula(bean.getAssociatedElementB().getComposingElement().getMolecularFormula());
		comp.setMolecularFormulaType(bean.getAssociatedElementB().getComposingElement().getMolecularFormulaType());
		comp.setName(bean.getAssociatedElementB().getComposingElement().getName());
		comp.setPubChemDataSourceName(bean.getAssociatedElementB().getComposingElement().getPubChemDataSourceName());
		comp.setPubChemId(bean.getAssociatedElementB().getComposingElement().getPubChemId());
		comp.setType(bean.getAssociatedElementB().getComposingElement().getType());
		comp.setValue(bean.getAssociatedElementB().getComposingElement().getValue());
		comp.setValueUnit(bean.getAssociatedElementB().getComposingElement().getValueUnit());
		
		associatedElementB.setCompositionType(bean.getAssociatedElementB().getCompositionType());
		associatedElementB.setEntityDisplayName(bean.getAssociatedElementB().getEntityDisplayName());
		if(bean.getAssociatedElementB().getEntityId()!=null)
		associatedElementB.setEntityId(bean.getAssociatedElementB().getEntityId());
		associatedElementB.setComposingElement(comp);
		chemBean.setAssociatedElementB(associatedElementB);
		
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
					file.setCreatedBy(fBean.getCreatedBy());
					file.setCreatedDate(fBean.getCreatedDate());
					file.setUriExternal(fBean.getUriExternal());
					fileBean.setKeywordsStr(fBean.getKeywordsStr());
					fileBean.setDomainFile(file);
				}
				chemBean.setTheFile(fileBean);
				
				//setting up files
				List<SimpleFileBean> filelist =  bean.getFiles();
				fileBean = new FileBean();
				file = new File();
				List<FileBean> fileBeanList = new ArrayList<FileBean>();
				if(filelist!=null){
				for(SimpleFileBean sFBean : filelist){
					file.setType(sFBean.getType());
					file.setTitle(sFBean.getTitle());
					file.setDescription(sFBean.getDescription());
					file.setUri(sFBean.getUri());
					file.setCreatedBy(sFBean.getCreatedBy());
					file.setCreatedDate(sFBean.getCreatedDate());
					file.setUriExternal(sFBean.getUriExternal());
					fileBean.setKeywordsStr(sFBean.getKeywordsStr());
					fileBean.setDomainFile(file);
				//	fileBean.setDomainFile(file);
				//	filecoll.add(file);
					fileBeanList.add(fileBean);
				}
				}
				chemBean.setFiles(fileBeanList);
				
		return chemBean;
	}

	private List<String> validateAssociationFile(HttpServletRequest request,
			ChemicalAssociationBean entityBean) throws Exception {
		List<String> msgs = new ArrayList<String>();
		for (FileBean filebean : entityBean.getFiles()) {
			msgs = validateFileBean(request, filebean);
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
		String entityTypeA = assocBean.getAssociatedElementA()
				.getEntityDisplayName();
		String entityIdA = assocBean.getAssociatedElementA().getEntityId();
		String entityTypeB = assocBean.getAssociatedElementB()
				.getEntityDisplayName();
		String entityIdB = assocBean.getAssociatedElementB().getEntityId();
		if (entityTypeA.equals(entityTypeB) && entityIdA.equals(entityIdB)) {
			//noErrors = false;
			msgs = new ArrayList<String>();
		}
		return msgs;
	}

	public List<String> saveAssociation(HttpServletRequest request,
			String sampleId, ChemicalAssociationBean assocBean)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(sampleId, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Boolean newAssoc = true;
		try {
			assocBean.setupDomainAssociation(user.getLoginName());
			if (assocBean.getDomainAssociation().getId() != null) {
				newAssoc = false;
			}
		} catch (ClassCastException ex) {
			ActionMessage msg = null;
			if (!StringUtils.isEmpty(ex.getMessage())
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
//				msg = new ActionMessage("errors.invalidOtherType", ex
//						.getMessage(), "Chemical Association");
			} else {
//				msg = new ActionMessage("errors.invalidOtherType", assocBean
//						.getType(), "Chemical Association");
				assocBean.setType(null);
			}
		}
		// comp service already created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.saveChemicalAssociation(sampleBean, assocBean);
		// retract from public if updating an existing public record and not
		// curator
		if (!newAssoc && !user.isCurator() && sampleBean.getPublicStatus()) {
//			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
//					.toString(), sampleBean.getDomain().getName(), "sample");
//			ActionMessages messages = new ActionMessages();
//			ActionMessage msg = null;
//			msg = new ActionMessage("message.updateSample.retractFromPublic");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, messages);
		}
		Boolean hasFunctionalizingEntity = (Boolean) request.getSession()
				.getAttribute("hasFunctionalizingEntity");
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
		//theForm.set("assoc", assocBean);
		//String sampleId = theForm.getString("sampleId");
		CompositionService compService = this.setServicesInSession(request);
		CompositionBean compositionBean = compService
				.findCompositionBySampleId(sampleId);
		// if composition doesn't have required information, return to summary
		// view page
//		if (!validateComposition(compositionBean, request)) {
//			return mapping.findForward("success");
//		}
		request.getSession().removeAttribute("compositionForm");
		setLookups(request, compositionBean);
		this.checkOpenForms(assocBean, request);
		return CompositionUtil.reformatLocalSearchDropdownsInSessionForChemicalAssociation(request.getSession());
	}

	public boolean validateComposition(CompositionBean compositionBean,
			HttpServletRequest request) throws Exception {
		ActionMessages msgs = new ActionMessages();
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);

		// if no composition return to summary view page
		if (compositionBean == null) {
			ActionMessage msg = new ActionMessage("message.nullComposition");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			return false;
		}
		// check if sample has the required nanomaterial entities
		if (compositionBean.getNanomaterialEntities().isEmpty()) {
			ActionMessage msg = new ActionMessage(
					"message.emptyMaterialsEntitiesInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			return false;
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
			ActionMessage msg = new ActionMessage(
					"message.emptyComposingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			return false;
		}
		// check whether it has a functionalizing entity
		boolean hasFunctionalizingEntity = false;
		if (!compositionBean.getFunctionalizingEntities().isEmpty()) {
			hasFunctionalizingEntity = true;
		}
		if (!hasFunctionalizingEntity && numberOfCE == 1) {
			ActionMessage msg = new ActionMessage(
					"message.oneComposingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			return false;
		}
		if (!hasFunctionalizingEntity) {
			ActionMessage msg = new ActionMessage(
					"message.emptyFunctionalizingEntityInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			return false;
		}
		return true;
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		escapeXmlForFileUri(assocBean.getTheFile());
		prepareEntityLists(assocBean, request);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if (user == null) {
			throw new InvalidSessionException();
		}
		Boolean hasFunctionalizingEntities = (Boolean) session
				.getAttribute("hasFunctionalizingEntity");
		InitCompositionSetup.getInstance().persistChemicalAssociationDropdowns(
				request, assocBean, hasFunctionalizingEntities);

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = assocBean.getTheFile();
		preserveUploadedFile(request, theFile, "Chemical Association");

		this.checkOpenForms(assocBean, request);
		return mapping.findForward("inputForm");
	}

	private void setLookups(HttpServletRequest request,
			CompositionBean compositionBean) throws Exception {
		// check whether it has a functionalizing entity
		boolean hasFunctionalizingEntity = false;
		if (!compositionBean.getFunctionalizingEntities().isEmpty()) {
			hasFunctionalizingEntity = true;
		}
		InitCompositionSetup.getInstance().setChemicalAssociationDropdowns(
				request, hasFunctionalizingEntity);
		// use BaseCompositionEntityBean for DWR ajax
		List<BaseCompositionEntityBean> materialEntities = new ArrayList<BaseCompositionEntityBean>();
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			if (!entityBean.getComposingElements().isEmpty()) {
				materialEntities.add(entityBean);
			}
		}
		request.getSession().setAttribute("sampleMaterialEntities",
				materialEntities);
		request.getSession().setAttribute("sampleFunctionalizingEntities",
				compositionBean.getFunctionalizingEntities());
		request.getSession().setAttribute("hasFunctionalizingEntity",
				hasFunctionalizingEntity);
	}

	public void prepareEntityLists(ChemicalAssociationBean assocBean,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		CompositionService service = new CompositionServiceLocalImpl();
		// associated element A
		List<BaseCompositionEntityBean> entityListA = null;
		List<ComposingElementBean> ceListA = new ArrayList<ComposingElementBean>();
		if (assocBean.getAssociatedElementA().getCompositionType().equals(
				"nanomaterial entity")) {
			entityListA = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleMaterialEntities"));
			// get composing elements
			NanomaterialEntityBean entityBean = service
					.findNanomaterialEntityById(assocBean
							.getAssociatedElementA().getEntityId());
			ceListA = entityBean.getComposingElements();
		} else {
			entityListA = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleFunctionalizingEntities"));
		}
		session.setAttribute("ceListA", ceListA);
		session.setAttribute("entityListA", entityListA);
		// associated element B
		List<BaseCompositionEntityBean> entityListB = null;
		List<ComposingElementBean> ceListB = new ArrayList<ComposingElementBean>();
		if (assocBean.getAssociatedElementB().getCompositionType().equals(
				"nanomaterial entity")) {
			entityListB = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleMaterialEntities"));
			// get composing elements
			NanomaterialEntityBean entityBean = service
					.findNanomaterialEntityById(assocBean
							.getAssociatedElementB().getEntityId());
			ceListB = entityBean.getComposingElements();
		} else {
			entityListB = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleFunctionalizingEntities"));
		}
		session.setAttribute("ceListB", ceListB);
		session.setAttribute("entityListB", entityListB);
	}

	public SimpleChemicalAssociationBean setupUpdate(String sampleId, String dataId,
			HttpServletRequest request)
			throws Exception {
		// set up compositionBean required to set up drop-down
		CompositionService compService = this.setServicesInSession(request);
		CompositionBean compositionBean = compService
				.findCompositionBySampleId(sampleId);
		setLookups(request, compositionBean);
		String assocId = super.validateId(request, "dataId");
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssociationById(assocId);
		prepareEntityLists(assocBean, request);
		//theForm.set("assoc", assocBean);
		this.checkOpenForms(assocBean, request);
	//	return mapping.findForward("inputForm");
		SimpleChemicalAssociationBean chemBean = new SimpleChemicalAssociationBean();
		chemBean.trasferToSimpleChemicalAssociation(assocBean, request);
		return chemBean;
	}

	public SimpleChemicalAssociationBean saveFile(SimpleChemicalAssociationBean bean,
			HttpServletRequest request)
			throws Exception {
		ChemicalAssociationBean assoc = transferChemicalAssociationBean(bean);
		FileBean theFile = assoc.getTheFile();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		this.setServicesInSession(request);
		SampleBean sampleBean = setupSampleById(bean.getSampleId(), request);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/"
				+ "chemicalAssociation";
		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		assoc.addFile(theFile);

		// restore previously uploaded file from session.
		this.restoreUploadedFile(request, theFile);

		// save the association
		saveAssociation(request, bean.getSampleId(), assoc);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(assoc.getDomainAssociation()
				.getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		this.checkOpenForms(assoc, request);

		request.setAttribute("dataId", assoc.getDomainAssociation().getId()
				.toString());
		return setupUpdate(bean.getSampleId(), assoc.getDomainAssociation().getId()
				.toString(), request);
	}

	public SimpleChemicalAssociationBean removeFile(SimpleChemicalAssociationBean bean,
			HttpServletRequest request)
			throws Exception {
		ChemicalAssociationBean assoc = transferChemicalAssociationBean(bean);
		FileBean theFile = assoc.getTheFile();
		assoc.removeFile(theFile);
		assoc.setTheFile(new FileBean());
		request.setAttribute("anchor", "file");
		// save the association
		saveAssociation(request, bean.getSampleId(), assoc);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(assoc.getDomainAssociation()
				.getSampleComposition(), theFile.getDomainFile());

		this.checkOpenForms(assoc, request);
	//	return mapping.findForward("inputForm");
		return setupUpdate(bean.getSampleId(), assoc.getDomainAssociation().getId()
				.toString(), request);
	}

	public List<String> delete(SimpleChemicalAssociationBean bean,
			HttpServletRequest request)
			throws Exception {
		
		List<String> msgs = new ArrayList<String>();
		ChemicalAssociationBean assocBean = transferChemicalAssociationBean(bean);

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		assocBean.setupDomainAssociation(user.getLoginName());

		CompositionService compService = this.setServicesInSession(request);
		compService.deleteChemicalAssociation(assocBean.getDomainAssociation());
		// TODO remove accessibility
//		ActionMessage msg = new ActionMessage(
//				"message.deleteChemicalAssociation");
		msgs.add(PropertyUtil.getProperty("sample", "message.deleteChemicalAssociation"));
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
	//	return mapping.findForward("success");
		return msgs;
	}

	private void checkOpenForms(ChemicalAssociationBean assoc,
			HttpServletRequest request) {
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
}


