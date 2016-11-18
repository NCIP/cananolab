package gov.nih.nci.cananolab.restful.protocol;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitProtocolBean;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.ui.form.ProtocolForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("protocolBO")
public class ProtocolBO extends BaseAnnotationBO
{
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private CurationService curationServiceDAO;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private ProtocolService protocolService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	public List<String> create(SimpleSubmitProtocolBean bean, HttpServletRequest request) throws Exception
	{
		List<String> msgs = new ArrayList<String>();
		ProtocolBean protocolBean = transferSimpleSubmitProtocolBean(bean);
		Boolean newProtocol = true;
		if (protocolBean.getDomain().getId() != null && protocolBean.getDomain().getId() > 0)
		{
			newProtocol = false;
		}
		msgs = this.saveProtocol(request, protocolBean);
		bean.setId(protocolBean.getDomain().getId());
		request.getSession().removeAttribute("newFileData");

		if(msgs.size()>0){
			return msgs;
		}
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		// retract from public if updating an existing public record and not curator
		if (!newProtocol && !userDetails.isCurator() && 
			springSecurityAclService.checkObjectPublic(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz())) {
			retractFromPublic(request, protocolBean.getDomain().getId(), protocolBean.getDomain().getName(),
							  "protocol", SecureClassesEnum.PROTOCOL.getClazz());
			
//			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS, request,
//					protocolBean.getDomain().getId().toString(), protocolBean.getDomain().getName(), "protocol");
			msgs.add("retract success");
			return msgs;
		} else
		{
			msgs.add("success");
		}
		return msgs;
	}

	private ProtocolBean transferSimpleSubmitProtocolBean(SimpleSubmitProtocolBean bean)
	{
		ProtocolBean proBean = new ProtocolBean();
		Protocol protocol = new Protocol();
		FileBean fileBean = new FileBean();
		fileBean.setNewFileData(bean.getNewFileData());
		fileBean.setExternalUrl(bean.getExternalUrl());
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
		
		boolean isWriteable = springSecurityAclService.currentUserHasWritePermission(bean.getId(), SecureClassesEnum.PROTOCOL.getClazz());
		boolean isDeleteable = springSecurityAclService.currentUserHasDeletePermission(bean.getId(), SecureClassesEnum.PROTOCOL.getClazz());
		proBean.setUserDeletable(isDeleteable);
		proBean.setUserUpdatable(isWriteable);
		proBean.setDomain(protocol);
		return proBean;
		
	}

	private List<String> saveProtocol(HttpServletRequest request, ProtocolBean protocolBean) throws Exception
	{
		List<String> msgs = validateProtocolBean(protocolBean);
		if(msgs != null && msgs.size()>0){
			return msgs;
		}
		protocolBean.setupDomain(Constants.FOLDER_PROTOCOL, SpringSecurityUtil.getLoggedInUserName());
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request, protocolBean);
		String timestamp = DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute("newFileData");
		if(newFileData!=null){
			protocolBean.getFileBean().setNewFileData((byte[]) request.getSession().getAttribute("newFileData"));
			protocolBean.getFileBean().getDomainFile().setUri(Constants.FOLDER_PROTOCOL+ "/" + timestamp + "_"
					+ protocolBean.getFileBean().getDomainFile().getName());
		}
		protocolService.saveProtocol(protocolBean);
		return msgs;
	}

	private List<String> validateProtocolBean(ProtocolBean protocolBean)
	{
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
		
		
		String uri = protocolBean.getFileBean().getDomainFile().getUri();
		if(!InputValidationUtil.isTextFieldWhiteList(uri)){
			errors.add(PropertyUtil.getProperty("protocol", "file.uri.invalid"));
		}
		
		String externalUrl = protocolBean.getFileBean().getExternalUrl();
		if(!InputValidationUtil.isUrlValid(externalUrl)){
			errors.add("External URL is invalid");
		}
		
		return errors;
	}
	

	// for retaining user selected values during validation
	public void input(ProtocolForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	super.checkOpenAccessForm(theForm, request);
		ProtocolBean protocolBean = ((ProtocolBean) form.getProtocol());
		escapeXmlForFileUri(protocolBean.getFileBean());
		InitProtocolSetup.getInstance().persistProtocolDropdowns(request,
				protocolBean);
		setupDynamicDropdowns(request, protocolBean);
	//	return mapping.findForward("inputPage");
	}

	private void setupDynamicDropdowns(HttpServletRequest request, ProtocolBean protocolBean) throws Exception
	{
		String selectedProtocolType = protocolBean.getDomain().getType();
		String selectedProtocolName = protocolBean.getDomain().getName();
		ProtocolServiceHelper helper = protocolService.getHelper();
		// retrieve user entered protocol names that haven't been saved as
		// protocols
		SortedSet<String> otherNames = LookupService.findLookupValues(selectedProtocolType + " protocol type", "otherName");
		if (!StringUtils.isEmpty(selectedProtocolType)) {
			SortedSet<String> protocolNames = helper.getProtocolNamesBy(selectedProtocolType);
			protocolNames.addAll(otherNames);
			request.getSession().setAttribute("protocolNamesByType", protocolNames);
		} else {
			request.getSession().setAttribute("protocolNamesByType", otherNames);
		}

		// retrieve user entered protocol versions that haven't been saved
		// as protocols
		SortedSet<String> otherVersions = LookupService.findLookupValues(selectedProtocolType + " protocol type", "otherVersion");
		if (!StringUtils.isEmpty(selectedProtocolName)) {
			SortedSet<String> protocolVersions = helper.getProtocolVersionsBy(selectedProtocolType, selectedProtocolName);
			protocolVersions.addAll(otherVersions);
			request.getSession().setAttribute("protocolVersionsByTypeName", protocolVersions);
		} else {
			request.getSession().setAttribute("protocolVersionsByTypeName", otherVersions);
		}
	}

	public void setupNew(ProtocolForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		request.getSession().removeAttribute("protocolForm");
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	super.checkOpenAccessForm(theForm, request);
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
		request.getSession().removeAttribute("protocolNamesByType");
		request.getSession().removeAttribute("protocolVersionsByTypeName");
		request.getSession().removeAttribute("updateProtocol");
	//	saveToken(request);
	//	return mapping.findForward("inputPage");
	}

	public SimpleSubmitProtocolBean setupUpdate(String protocolId, HttpServletRequest request) throws Exception
	{
		InitProtocolSetup.getInstance().setProtocolDropdowns(request);
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
	//	super.checkOpenAccessForm(theForm, request);
		ProtocolForm form = new ProtocolForm();
		SimpleSubmitProtocolBean bean = new SimpleSubmitProtocolBean();
		protocolId = super.validateId(request, "protocolId");
		ProtocolBean protocolBean = protocolService.findProtocolById(protocolId);
		if (protocolBean == null) {
			throw new NotExistException("No such protocol in the database");
		}
		form.setProtocol(protocolBean);
		setupDynamicDropdowns(request, protocolBean);
		request.getSession().setAttribute("updateProtocol", "true");
		setUpSubmitForReviewButton(request, protocolBean.getDomain().getId().toString(), 
								   springSecurityAclService.checkObjectPublic(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz()));
	//	saveToken(request);
	//	return mapping.findForward("inputPage");
		transferProtocolBeanForEdit(protocolBean, bean, request);
		return bean;
	}
	
	public void transferProtocolBeanForEdit(ProtocolBean protocolBean, SimpleSubmitProtocolBean bean, HttpServletRequest request)
	{
		bean.setAbbreviation(protocolBean.getDomain().getAbbreviation());
		bean.setCreatedBy(protocolBean.getDomain().getCreatedBy());
		bean.setCreatedDate(protocolBean.getDomain().getCreatedDate());
		
		if(protocolBean.getDomain().getFile()!= null){
		//	setFileDescription(bean.getDomain().getFile().getDescription());
			bean.setFileId(protocolBean.getDomain().getFile().getId());
		//	setFileTitle(bean.getDomain().getFile().getTitle());
			bean.setUri(protocolBean.getDomain().getFile().getUri());
			bean.setUriExternal(protocolBean.getDomain().getFile().getUriExternal());
			bean.setFileName(protocolBean.getDomain().getFile().getName());
			bean.setFileDescription(protocolBean.getDomain().getFile().getDescription());
			bean.setFileTitle(protocolBean.getDomain().getFile().getTitle());
		}
		bean.setGroupAccesses(protocolBean.getGroupAccesses());
		bean.setUserAccesses(protocolBean.getUserAccesses());
		bean.setTheAccess(protocolBean.getTheAccess());
		bean.setId(protocolBean.getDomain().getId());
		bean.setIsOwner(springSecurityAclService.isOwnerOfObject(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz()));
		bean.setIsPublic(springSecurityAclService.checkObjectPublic(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz()));
		bean.setName(protocolBean.getDomain().getName());
		bean.setType(protocolBean.getDomain().getType());
		bean.setVersion(protocolBean.getDomain().getVersion());
		bean.setReview((Boolean) request.getAttribute("review"));
		bean.setExternalUrl(protocolBean.getFileBean().getExternalUrl());
		
		boolean isWriteable = springSecurityAclService.currentUserHasWritePermission(bean.getId(), SecureClassesEnum.PROTOCOL.getClazz());
		boolean isDeleteable = springSecurityAclService.currentUserHasDeletePermission(bean.getId(), SecureClassesEnum.PROTOCOL.getClazz());
		
		bean.setUserDeletable(isDeleteable);
		bean.setUserUpdatable(isWriteable);
	}

	private void setAccesses(HttpServletRequest request, ProtocolBean protocolBean) throws Exception
	{
		springSecurityAclService.loadAccessControlInfoForObject(protocolBean.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz(), protocolBean);
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
	public List<String> delete(SimpleSubmitProtocolBean form, HttpServletRequest request) throws Exception
	{
		List<String> msgs = new ArrayList<String>();
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocolBean = transferSimpleSubmitProtocolBean(form);//form.getProtocol();
		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				protocolBean.getDomain().getId().toString(), protocolBean.getDomain().getName(), "protocol");
		protocolService.deleteProtocol(protocolBean.getDomain());
		msgs.add("success");
	
		return msgs;
	}

	public SimpleSubmitProtocolBean saveAccess(SimpleSubmitProtocolBean form, HttpServletRequest request) throws Exception
	{
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocol = transferSimpleSubmitProtocolBean(form); //form.getProtocol();
		AccessControlInfo theAccess = protocol.getTheAccess();
		//if (!super.validateAccess(request, theAccess)) {
		//	return input(mapping, form, request, response);
		//}
		// if protocol is new, save protocol first
		if (protocol.getDomain().getId() == 0) {
			this.saveProtocol(request, protocol);
		}
		// if protocol is public, the access is not public, retract public
		// privilege would be handled in the service method
		
		protocolService.assignAccessibility(theAccess, protocol.getDomain());
		// update status to retracted if the access is not public and protocol is public
		if (!CaNanoRoleEnum.ROLE_ANONYMOUS.toString().equalsIgnoreCase(theAccess.getRecipient()) &&
			springSecurityAclService.checkObjectPublic(protocol.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz()))
		{
			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS, request, protocol.getDomain().getId().toString(), protocol
							.getDomain().getName(), "protocol");
			springSecurityAclService.retractObjectFromPublic(protocol.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz());
		}
		// if access is public, pending review status, update review
		// status to public
		if (CaNanoRoleEnum.ROLE_ANONYMOUS.toString().equalsIgnoreCase(theAccess.getRecipient())) {
			this.switchPendingReviewToPublic(request, protocol.getDomain().getId().toString());
		}

		if (protocol.getDomain().getId() == null) {
			protocol.setupDomain(Constants.FOLDER_PROTOCOL, SpringSecurityUtil.getLoggedInUserName());
			protocolService.saveProtocol(protocol);
		}
		this.setAccesses(request, protocol);
		request.setAttribute("protocolId", protocol.getDomain().getId().toString());
	//	resetToken(request);
		return setupUpdate(protocol.getDomain().getId().toString(), request);
	}

	public SimpleSubmitProtocolBean deleteAccess(SimpleSubmitProtocolBean form, HttpServletRequest request) throws Exception
	{		
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		ProtocolBean protocol = transferSimpleSubmitProtocolBean(form);  //form.getProtocol();
		AccessControlInfo theAccess = protocol.getTheAccess();
		protocolService.removeAccessibility(theAccess, protocol.getDomain());
		this.setAccesses(request, protocol);
		request.setAttribute("protocolId", protocol.getDomain().getId().toString());
		return setupUpdate(protocol.getDomain().getId().toString(), request);
	}

	protected void removePublicAccess(ProtocolBean protocol, HttpServletRequest request) throws Exception {
	//	ProtocolBean protocol = theForm.getProtocol();
		springSecurityAclService.retractObjectFromPublic(protocol.getDomain().getId(), SecureClassesEnum.PROTOCOL.getClazz());
	}

	public String download(String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		return downloadFile(protocolService, fileId, request, response);
	}

	public void saveFile(InputStream fileInputStream, String fileName, HttpServletRequest request)
	{
		try {
			byte[] fileData = IOUtils.toByteArray(fileInputStream);
			request.getSession().setAttribute("newFileData", fileData);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * Delete a protocol from MyWorkSpace
	 * 
	 * @param mapping
	 * @param protocolId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<String> deleteProtocolById(String protocolId, HttpServletRequest request) throws Exception
	{
		List<String> msgs = new ArrayList<String>();
		ProtocolBean protocolBean = protocolService.findProtocolById(protocolId);
		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				protocolBean.getDomain().getId().toString(), protocolBean
						.getDomain().getName(), "protocol");
		protocolService.deleteProtocol(protocolBean.getDomain());
		
		msgs.add("success");
	
		return msgs;
	}

	@Override
	public CurationService getCurationServiceDAO() {
		return curationServiceDAO;
	}

	@Override
	public SampleService getSampleService() {
		return sampleService;
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
