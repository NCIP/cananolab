package gov.nih.nci.cananolab.restful.protocol;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitProtocolBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.ProtocolForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class ProtocolBO extends BaseAnnotationBO{
	public List<String> create(SimpleSubmitProtocolBean bean,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		ProtocolBean protocolBean = transferSimpleSubmitProtocolBean(bean);//(ProtocolBean) form.getProtocol();
		Boolean newProtocol = true;
		if (protocolBean.getDomain().getId() != null
				&& protocolBean.getDomain().getId() > 0) {
			newProtocol = false;
		}
		msgs = this.saveProtocol(request, protocolBean);
		if(msgs.size()>0){
			return msgs;
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// retract from public if updating an existing public record and not
		// curator
		if (!newProtocol && !user.isCurator() && protocolBean.getPublicStatus()) {
		//	retractFromPublic(form, request, protocolBean.getDomain()
		//			.getId().toString(), protocolBean.getDomain().getName(),
		//			"protocol");
		//	msg = new ActionMessage("message.updateProtocol.retractFromPublic");
		
		} else {
		
			msgs.add("success");
			request.getSession().removeAttribute("newFileData");
		}
	//	forward = mapping.findForward("success");
		return msgs;
	}

	private ProtocolBean transferSimpleSubmitProtocolBean(
			SimpleSubmitProtocolBean bean) {
		ProtocolBean proBean = new ProtocolBean();
		Protocol protocol = new Protocol();
		FileBean fileBean = new FileBean();
		fileBean.setNewFileData(bean.getNewFileData());
		File file = new File();
			file.setTitle(bean.getFileTitle());
			file.setDescription(bean.getFileDescription());
			file.setId(bean.getFileId());
			file.setUri(bean.getUri());
			file.setUriExternal(bean.getUriExternal());
			file.setName(bean.getFileName());
		fileBean.setDomainFile(file);
		protocol.setType(bean.getType());
		protocol.setName(bean.getName());
		protocol.setVersion(bean.getVersion());
		protocol.setCreatedDate(bean.getCreatedDate());
		protocol.setId(bean.getId());
		protocol.setCreatedBy(bean.getCreatedBy());
		protocol.setAbbreviation(bean.getAbbreviation());
		proBean.setFileBean(fileBean);
		proBean.setGroupAccesses(bean.getGroupAccesses());
		proBean.setUserAccesses(bean.getUserAccesses());
		proBean.setTheAccess(bean.getTheAccess());
		proBean.setUserDeletable(bean.getUserDeletable());
		proBean.setUserUpdatable(bean.getUserUpdatable());
		proBean.setDomain(protocol);
		return proBean;
		
	}

	private List<String> saveProtocol(HttpServletRequest request,
			ProtocolBean protocolBean) throws Exception {
		List<String> msgs = new ArrayList<String>();
		msgs = validateProtocolBean(protocolBean);
		if(msgs.size()>0){
			return msgs;
		}
		ProtocolService service = this.setServiceInSession(request);
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		protocolBean
				.setupDomain(Constants.FOLDER_PROTOCOL, user.getLoginName());
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		String timestamp = DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute("newFileData");
		if(newFileData!=null){
			protocolBean.getFileBean().setNewFileData((byte[]) request.getSession().getAttribute("newFileData"));
			protocolBean.getFileBean().getDomainFile().setUri(Constants.FOLDER_PROTOCOL+ "/" + timestamp + "_"
					+ protocolBean.getFileBean().getDomainFile().getName());
		}
		service.saveProtocol(protocolBean);
		return msgs;
	}

	private List<String> validateProtocolBean(ProtocolBean protocolBean) {
		List<String> errors = new ArrayList<String>();
		
		Protocol protocol = (Protocol) protocolBean.getDomain();
		String name = protocol.getName();
		if(name == null||name == ""){
			errors.add("Protocol Name is required.");
		}
		if(!InputValidationUtil.isTextFieldWhiteList(name)){
			errors.add(PropertyUtil.getProperty("protocol", "protocol.name.invalid"));
		}
		String type = protocol.getType();
		if(type == null||type == ""){
			errors.add("Protocol Type is required.");
		}
		if(!InputValidationUtil.isTextFieldWhiteList(type)){
			errors.add(PropertyUtil.getProperty("protocol", "protocol.type.invalid"));
		}
		String version = protocol.getVersion();
		
		if(!InputValidationUtil.isTextFieldWhiteList(version)){
			errors.add(PropertyUtil.getProperty("protocol", "protocol.version.invalid"));
		}
		String abbreviation = protocol.getAbbreviation();
		
		if(!InputValidationUtil.isTextFieldWhiteList(abbreviation)){
			errors.add(PropertyUtil.getProperty("protocol", "protocol.abbreviation.invalid"));
		}
		String title = protocolBean.getFileBean().getDomainFile().getTitle();
		
		if(!InputValidationUtil.isTextFieldWhiteList(title)){
			errors.add(PropertyUtil.getProperty("protocol", "protocol.title.invalid"));
		}
		String fileName = protocolBean.getFileBean().getDomainFile().getName();
		
		if(!InputValidationUtil.isTextFieldWhiteList(fileName)){
			errors.add(PropertyUtil.getProperty("protocol", "protocol.file.name.invalid"));
		}
		
		
		String externalUrl = protocolBean.getFileBean().getDomainFile().getUri();
		if(!InputValidationUtil.isTextFieldWhiteList(externalUrl)){
			errors.add(PropertyUtil.getProperty("protocol", "file.uri.invalid"));
		}
		
		return errors;
	}
	

	// for retaining user selected values during validation
	public void input(ProtocolForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	super.checkOpenAccessForm(theForm, request);
		ProtocolBean protocolBean = ((ProtocolBean) form.getProtocol());
		escapeXmlForFileUri(protocolBean.getFileBean());
		this.setServiceInSession(request);
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		setupDynamicDropdowns(request, protocolBean);
	//	return mapping.findForward("inputPage");
	}

	private void setupDynamicDropdowns(HttpServletRequest request,
			ProtocolBean protocolBean) throws Exception {
		String selectedProtocolType = protocolBean.getDomain().getType();
		String selectedProtocolName = protocolBean.getDomain().getName();
		ProtocolServiceLocalImpl protocolService = (ProtocolServiceLocalImpl) (ProtocolService) request
				.getSession().getAttribute("protocolService");
		ProtocolServiceHelper helper = protocolService.getHelper();
		// retrieve user entered protocol names that haven't been saved as
		// protocols
		SortedSet<String> otherNames = LookupService.findLookupValues(
				selectedProtocolType + " protocol type", "otherName");
		if (!StringUtils.isEmpty(selectedProtocolType)) {
			SortedSet<String> protocolNames = helper
					.getProtocolNamesBy(selectedProtocolType);
			protocolNames.addAll(otherNames);
			request.getSession().setAttribute("protocolNamesByType",
					protocolNames);
		} else {
			request.getSession()
					.setAttribute("protocolNamesByType", otherNames);
		}

		// retrieve user entered protocol versions that haven't been saved
		// as protocols
		SortedSet<String> otherVersions = LookupService.findLookupValues(
				selectedProtocolType + " protocol type", "otherVersion");
		if (!StringUtils.isEmpty(selectedProtocolName)) {
			SortedSet<String> protocolVersions = helper.getProtocolVersionsBy(
					selectedProtocolType, selectedProtocolName);
			protocolVersions.addAll(otherVersions);
			request.getSession().setAttribute("protocolVersionsByTypeName",
					protocolVersions);
		} else {
			request.getSession().setAttribute("protocolVersionsByTypeName",
					otherVersions);
		}
	}

	public void setupNew(ProtocolForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("protocolForm");
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	super.checkOpenAccessForm(theForm, request);
		setServiceInSession(request);
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		request.getSession().removeAttribute("protocolNamesByType");
		request.getSession().removeAttribute("protocolVersionsByTypeName");
		request.getSession().removeAttribute("updateProtocol");
	//	saveToken(request);
	//	return mapping.findForward("inputPage");
	}

	public SimpleSubmitProtocolBean setupUpdate(String protocolId,
			HttpServletRequest request)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	super.checkOpenAccessForm(theForm, request);
		ProtocolForm form = new ProtocolForm();
		SimpleSubmitProtocolBean bean = new SimpleSubmitProtocolBean();
		protocolId = super.validateId(request, "protocolId");
		ProtocolService service = this.setServiceInSession(request);
		ProtocolBean protocolBean = service.findProtocolById(protocolId);
		if (protocolBean == null) {
			throw new NotExistException("No such protocol in the database");
		}
		form.setProtocol(protocolBean);
		setupDynamicDropdowns(request, protocolBean);
		request.getSession().setAttribute("updateProtocol", "true");
		setUpSubmitForReviewButton(request, protocolBean.getDomain().getId()
				.toString(), protocolBean.getPublicStatus());
	//	saveToken(request);
	//	return mapping.findForward("inputPage");
		bean.transferProtocolBeanForEdit(protocolBean, request);
		return bean;
	}

	private void setAccesses(HttpServletRequest request,
			ProtocolBean protocolBean) throws Exception {
		ProtocolService service = this.setServiceInSession(request);
		List<AccessibilityBean> groupAccesses = service
				.findGroupAccessibilities(protocolBean.getDomain().getId()
						.toString());
		List<AccessibilityBean> userAccesses = service
				.findUserAccessibilities(protocolBean.getDomain().getId()
						.toString());
		protocolBean.setUserAccesses(userAccesses);
		protocolBean.setGroupAccesses(groupAccesses);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		protocolBean.setUser(user);
	}

	/**
	 * Delete a protocol from Protocol update form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public List<String> delete(SimpleSubmitProtocolBean form,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocolBean = transferSimpleSubmitProtocolBean(form);//form.getProtocol();
		ProtocolService service = this.setServiceInSession(request);
		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				protocolBean.getDomain().getId().toString(), protocolBean
						.getDomain().getName(), "protocol");
		service.deleteProtocol(protocolBean.getDomain());
		
		msgs.add("success");
	
		return msgs;
	}

	private ProtocolService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		ProtocolService protocolService = new ProtocolServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("protocolService", protocolService);
		return protocolService;
	}

	public SimpleSubmitProtocolBean saveAccess(SimpleSubmitProtocolBean form,
			HttpServletRequest request)
			throws Exception {
		
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocol = transferSimpleSubmitProtocolBean(form); //form.getProtocol();
		AccessibilityBean theAccess = protocol.getTheAccess();
		//if (!super.validateAccess(request, theAccess)) {
		//	return input(mapping, form, request, response);
		//}
		ProtocolService service = this.setServiceInSession(request);
		// if protocol is new, save protocol first
		if (protocol.getDomain().getId() == 0) {
			this.saveProtocol(request, protocol);
		}
		// if protocol is public, the access is not public, retract public
		// privilege would be handled in the service method
		
		service.assignAccessibility(theAccess, protocol.getDomain());
		// update status to retracted if the access is not public and protocol
		// is public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)
				&& protocol.getPublicStatus()) {
			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS,
					request, protocol.getDomain().getId().toString(), protocol
							.getDomain().getName(), "protocol");
		}
		// if access is public, pending review status, update review
		// status to public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
			this.switchPendingReviewToPublic(request, protocol.getDomain()
					.getId().toString());
		}

		if (protocol.getDomain().getId() == null) {
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			protocol.setupDomain(Constants.FOLDER_PROTOCOL, user.getLoginName());
			service.saveProtocol(protocol);
		}
		this.setAccesses(request, protocol);
		request.setAttribute("protocolId", protocol.getDomain().getId()
				.toString());
	//	resetToken(request);
		return setupUpdate(protocol.getDomain().getId()
				.toString(), request);
	}

	public SimpleSubmitProtocolBean deleteAccess(SimpleSubmitProtocolBean form,
			HttpServletRequest request)
			throws Exception {
		
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocol = transferSimpleSubmitProtocolBean(form);  //form.getProtocol();
		AccessibilityBean theAccess = protocol.getTheAccess();
		ProtocolService service = this.setServiceInSession(request);
		
		service.removeAccessibility(theAccess, protocol.getDomain());
		this.setAccesses(request, protocol);
		request.setAttribute("protocolId", protocol.getDomain().getId()
				.toString());
		return setupUpdate(protocol.getDomain().getId()
				.toString(), request);
	}

	protected void removePublicAccess(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		ProtocolBean protocol = (ProtocolBean) theForm.get("protocol");
		ProtocolService service = this.setServiceInSession(request);
		service.removeAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS,
				protocol.getDomain());
	}

	public String download(String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProtocolService service = this.setServiceInSession(request);
		return downloadFile(service, fileId, request, response);
	}

	public void saveFile(InputStream fileInputStream, String fileName, HttpServletRequest request) {
		// TODO Auto-generated method stub
	
		try {
			byte[] fileData = IOUtils.toByteArray(fileInputStream);
			request.getSession().setAttribute("newFileData", fileData);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
