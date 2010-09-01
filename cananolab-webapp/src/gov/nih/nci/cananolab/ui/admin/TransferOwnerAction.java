/**
 * The caNanoLab Software License, Version 1.5
 *
 * Copyright 2006 SAIC. This software was developed in conjunction with the National
 * Cancer Institute, and so to the extent government employees are co-authors, any
 * rights in such works shall be subject to Title 17 of the United States Code,
 * section 105.
 *
 */
package gov.nih.nci.cananolab.ui.admin;

/**
 * Action class for Transfer Ownership section.
 *
 * @author lethai
 */
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.community.impl.CommunityServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class TransferOwnerAction extends Action {
	//private static Logger logger = Logger.getLogger(TransferOwnerAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		System.out.println("........ TransferOwnerAction..............");
		String currentOwner = (String) theForm.get("currentOwner");
		String newOwner = (String) theForm.get("newOwner");
		System.out.println("current owner: " + currentOwner + " new owner: " + newOwner);
		
		String[] dataTypes = new String[0];
		if (theForm != null) {
			dataTypes = (String[]) theForm
					.get("dataType");
			
		}
		Map<String,String> sampleResult=null;
		Map<String,String> protocolResult = null;
		Map<String,String> publicationResult = null;
		Map<String,String> collaborationGroupResult = null;
		
		for(int i=0; i<dataTypes.length; i++){
			if(dataTypes[i].equalsIgnoreCase("Sample")){
				sampleResult = searchSample(currentOwner,  request);
				transferOwnerForSample(currentOwner, newOwner, sampleResult.keySet(), request);
			}else if(dataTypes[i].equalsIgnoreCase("Protocol")){
				protocolResult = searchProtocol(currentOwner, request);
				transferOwnerForProtocol(currentOwner, newOwner, protocolResult.keySet(), request);
			}else if(dataTypes[i].equalsIgnoreCase("Publication")){
				publicationResult = searchPublication(currentOwner, request);
				transferOwnerForPublication(currentOwner, newOwner, publicationResult.keySet(), request);
			}else if(dataTypes[i].equalsIgnoreCase("Collaboration Group")){
				collaborationGroupResult = searchCollaborationGroup(currentOwner, request);
				transferOwnerForCollaborationGroup(currentOwner, newOwner, collaborationGroupResult.keySet(), request);
			}
		}	
		
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage("message.transferOwner");
		messages.add("message", message);
		saveMessages(request, messages);
		
		return mapping.findForward("success");
	}
	
	private Map<String, String> searchSample(String currentOwner, HttpServletRequest request) throws Exception{
		SampleService service = null;
		SecurityService securityService = getSecurityService(request);
		if (request.getSession().getAttribute("sampleService") != null) {
			service = (SampleService) request.getSession().getAttribute(
					"sampleService");
		} else {
			service = new SampleServiceLocalImpl(securityService);
			request.getSession().setAttribute("sampleService",service);
		}
		
		Map<String, String> sampleIds = service.findSampleIdsByOwner(currentOwner);
		
		return sampleIds;
	}
	
	private Map<String,String> searchCollaborationGroup(String currentOwner, HttpServletRequest request) throws Exception{
		CommunityService service = null;
		SecurityService securityService = getSecurityService(request);
		
		if (request.getSession().getAttribute("communityService") != null) {
			service = (CommunityService) request.getSession().getAttribute(
					"communityService");
		} else {
			service = new CommunityServiceLocalImpl(securityService);
			request.getSession().setAttribute("communityService",service);
		}
		return service.findCollaborationGroupByOwner(currentOwner);
		
	}
	/**
	 * map contain publication id and name
	 * @param currentOwner
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> searchPublication(String currentOwner, HttpServletRequest request) throws Exception{
		SecurityService securityService = this.getSecurityService(request);
		PublicationService service = (PublicationService)request.getSession().getAttribute("publicationService");
		if(service == null){
			service = new PublicationServiceLocalImpl(securityService);
			request.getSession().setAttribute("publicationService",service);
		}
		Map<String, String> publications = service.findPublicationsByOwner(currentOwner);
		return publications;
	}
	
	private Map<String, String> searchProtocol(String currentOwner, HttpServletRequest request) throws Exception{
		SecurityService securityService  = getSecurityService(request);
		ProtocolService service = (ProtocolService)request.getSession().getAttribute("protocolService");
		if(service == null){
			service = new ProtocolServiceLocalImpl(securityService);
			request.getSession().setAttribute("protocolService",service);
		}
		Map<String, String> protocols = service.findProtocolsByOwner(currentOwner);
		return protocols;
	}	
	
	private void transferOwnerForSample(String currentOwner, String newOwner, 
			Set<String> sampleIds, HttpServletRequest request) throws Exception{
		SecurityService securityService = getSecurityService(request);
		if(!securityService.getUserBean().isAdmin()){
			//you do not have permission to change sample owner
			throw new NoAccessException();
		}
		
		SampleService service = (SampleService) request.getSession()
		.getAttribute("sampleService");
		
		service.transferOwner(sampleIds, currentOwner, newOwner);
	}
	
	private void transferOwnerForPublication(String currentOwner, String newOwner, 
			Set<String> publicationIds, HttpServletRequest request) throws Exception{
		SecurityService securityService = getSecurityService(request);
		if(!securityService.getUserBean().isAdmin()){
			//you do not have permission to change publication owner
			throw new NoAccessException();
		}
		
		PublicationService service = (PublicationService) request.getSession()
		.getAttribute("publicationService");
		
		service.transferOwner(publicationIds, currentOwner, newOwner);
	}
	
	private void transferOwnerForProtocol(String currentOwner, String newOwner, 
			Set<String> protocolIds, HttpServletRequest request) throws Exception{
		SecurityService securityService = getSecurityService(request);
		if(!securityService.getUserBean().isAdmin()){
			//you do not have permission to change protocol owner
			throw new NoAccessException();
		}
		
		ProtocolService service = (ProtocolService) request.getSession()
		.getAttribute("protocolService");
		
		service.transferOwner(protocolIds, currentOwner, newOwner);
	}
	
	private void transferOwnerForCollaborationGroup(String currentOwner, String newOwner, 
			Set<String> collaborationGroupIds, HttpServletRequest request) throws Exception{
		SecurityService securityService = getSecurityService(request);
		if(!securityService.getUserBean().isAdmin()){
			//you do not have permission to change collaboration group owner
			throw new NoAccessException();
		}
		
		CommunityService service = (CommunityService) request.getSession()
		.getAttribute("communityService");
		
		service.transferOwner(collaborationGroupIds,  newOwner, currentOwner);
	}

	private SecurityService getSecurityService(HttpServletRequest request) throws Exception{
		SecurityService securityService = (SecurityService) request
		.getSession().getAttribute("securityService");

		if(securityService == null){
			securityService = new SecurityService(AccessibilityBean.CSM_APP_NAME);
		}
		return securityService;
	}
}
