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
import gov.nih.nci.cananolab.dto.admin.TransferOwnerBean;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.community.CommunityService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
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
import org.apache.struts.action.DynaActionForm;

public class TransferOwnerAction extends Action {
	private static Logger logger = Logger.getLogger(TransferOwnerAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DynaActionForm theForm = (DynaActionForm) form;
		TransferOwnerBean bean = (TransferOwnerBean)request.getAttribute("transferOwnerBean");
		

		Map<String,String> sampleResult = (Map<String,String>)request.getSession().getAttribute("samples");
		
		//Map<String,String> samples = (Map<String,String>)theForm.get("samples");
		/*String sampleId = request.getParameter("sampleId");
		if (!StringUtils.isEmpty(sampleId)) {
			theForm.set("sampleId", sampleId);
		} else {
			sampleId = (String) request.getAttribute("sampleId");
			if (sampleId == null) {
				sampleId = theForm.getString("sampleId");
			}
		}*/
		System.out.println("........ TransferOwnerAction..............");
		//System.out.println("current owner: " + bean.getCurrentOwner() + " new owner: " + bean.getNewOwner());
		
		//System.out.println("dataType: " + bean.getDataType().length + " samples: " + bean.getSamples().size());
		
		//String[] selectedSampleIds = bean.getSelectedSampleIds();
		if(sampleResult != null){
			Set<String> sampleIds = sampleResult.keySet();
			for(String id: sampleIds){
				String selectedId =(String)theForm.get(id);
				System.out.println("selecedId " + id + " " + selectedId);
			}
		}
		//TransferOwnerBean bean = ((TransferOwnerBean)form).getCurrentOwner();
		String[] dataTypes = new String[0];
		/*if (theForm != null) {
			dataTypes = (String[]) theForm
					.get("dataType");
			
		}*/
		
		Map<String,String> protocolResult = null;
		Map<String,String> publicationResult = null;
		Map<String,String> collaborationGroupResult = null;
		
		
		publicationResult = (Map<String,String>)request.getSession().getAttribute("publications");
		protocolResult = (Map<String,String>)request.getSession().getAttribute("protocols");
		collaborationGroupResult = (Map<String,String>)request.getSession().getAttribute("collaborationGroups");
		//String currentOwner = (String)request.getSession().getAttribute("currentOwner");
		//String newOwner = (String) request.getSession().getAttribute("newOnwer");
		dataTypes = (String[])request.getSession().getAttribute("dataType");
		
		if(sampleResult != null){
			System.out.println("sampleResult " + sampleResult.size() );			
		}
		if(publicationResult != null){
			System.out.println("publication result " + publicationResult.size());
		}
		if(protocolResult != null){
			System.out.println("protocol result " + protocolResult.size());
		}
		if(collaborationGroupResult != null){
			System.out.println("collaboration group result " + collaborationGroupResult.size());
		}
		
		//	System.out.println("currentOwner " +currentOwner + " newOnwer: " + newOwner);
		
		if(publicationResult != null){
			System.out.println("publication result " + publicationResult.size());
		}
		
		String currentOwner = bean.getCurrentOwner();
		String newOwner = bean.getNewOwner();
		for(int i=0; i<dataTypes.length; i++){
			if(dataTypes[i].equalsIgnoreCase("Sample")){
				Map<String,String>samples = (Map<String,String>)request.getSession().getAttribute("samples");
				transferOwnerForSample(currentOwner, newOwner, samples.keySet(), request);
				
			}else if(dataTypes[i].equalsIgnoreCase("Protocol")){
				Map<String,String> protocols = (Map<String,String>)request.getSession().getAttribute("protocols");
				transferOwnerForProtocol(currentOwner, newOwner, protocols.keySet(), request);
			}else if(dataTypes[i].equalsIgnoreCase("Publication")){
				Map<String,String>publications = (Map<String,String>)request.getSession().getAttribute("publications");
				transferOwnerForPublication(currentOwner, newOwner, publications.keySet(), request);
			}else if(dataTypes[i].equalsIgnoreCase("Collaboration Group")){
				Map<String,String>collaborationGroups = (Map<String,String>)request.getSession().getAttribute("collaborationGroups");
				transferOwnerForCollaborationGroup(currentOwner, newOwner, collaborationGroups.keySet(), request);
			}
		}
		
		return null;
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
		
		service.transferOwner(publicationIds,  newOwner);
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
		
		service.transferOwner(protocolIds,  newOwner);
	}
	
	private void transferOwnerForCollaborationGroup(String currentGroupName, String newGroupName, 
			Set<String> collaborationGroupIds, HttpServletRequest request) throws Exception{
		SecurityService securityService = getSecurityService(request);
		if(!securityService.getUserBean().isAdmin()){
			//you do not have permission to change collaboration group owner
			throw new NoAccessException();
		}
		
		CommunityService service = (CommunityService) request.getSession()
		.getAttribute("communityService");
		
		service.transferOwner(collaborationGroupIds,  newGroupName);
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
