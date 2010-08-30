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
 * Action class for transfer ownership section.
 *
 * @author lethai
 */
import gov.nih.nci.cananolab.dto.admin.TransferOwnerBean;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class TransferOwnerSearchAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		System.out.println("........ TransferOwnerSearchAction..............");
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
		TransferOwnerBean bean = new TransferOwnerBean();
		for(int i=0; i<dataTypes.length; i++){
			if(dataTypes[i].equalsIgnoreCase("Sample")){
				sampleResult = searchSample(currentOwner,  request);
				bean.setSamples(sampleResult);
				String[] sampleIds = sampleResult.keySet().toArray(new String[0]);
				bean.setSampleIds(sampleIds);
				bean.setSelectedSampleIds(sampleIds);
			}else if(dataTypes[i].equalsIgnoreCase("Protocol")){
				protocolResult = searchProtocol(currentOwner, request);
			}else if(dataTypes[i].equalsIgnoreCase("Publication")){
				publicationResult = searchPublication(currentOwner, request);
			}else if(dataTypes[i].equalsIgnoreCase("Collaboration Group")){
				collaborationGroupResult = searchCollaborationGroup(currentOwner, request);
			}
		}
		
	
		bean.setDataType(dataTypes);
		bean.setCurrentOwner(currentOwner);
		bean.setNewOwner(newOwner);
		bean.setSamples(sampleResult);
		String[] sampleIds = sampleResult.keySet().toArray(new String[0]);
		bean.setSampleIds(sampleIds);
		bean.setSelectedSampleIds(sampleIds);
		request.setAttribute("transferOwnerBean", bean);
		request.getSession().setAttribute("samples", sampleResult);
		/*request.getSession().setAttribute("publications", publicationResult);
		request.getSession().setAttribute("protocols", protocolResult);
		request.getSession().setAttribute("collaborationGroups", collaborationGroupResult);
		request.getSession().setAttribute("currentOwner", currentOwner);
		request.getSession().setAttribute("newOnwer", newOwner);
		request.getSession().setAttribute("dataType", dataTypes);
		*/
		return mapping.findForward("success");
	}
	
	private Map<String, String> searchSample(String currentOwner, HttpServletRequest request) throws Exception{
		SampleService service = null;
		if (request.getSession().getAttribute("sampleService") != null) {
			service = (SampleService) request.getSession().getAttribute(
					"sampleService");
		} else {
			service = this.setServiceInSession(request);
		}
		SecurityService securityService = getSecurityService(request);
		
		Map<String, String> sampleIds = service.findSampleIdsBy(currentOwner);
		
		return sampleIds;
	}
	
	private Map<String,String> searchCollaborationGroup(String currentGroupName, HttpServletRequest request) throws Exception{
		CommunityService service = null;
		SecurityService securityService = getSecurityService(request);
		
		if (request.getSession().getAttribute("communityService") != null) {
			service = (CommunityService) request.getSession().getAttribute(
					"communityService");
		} else {
			service = new CommunityServiceLocalImpl(securityService);
		}
		return service.findCollaborationGroupByGroupName(currentGroupName);
		
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
		}
		Map<String, String> publications = service.findPublicationsByOwner(currentOwner);
		return publications;
	}
	
	private Map<String, String> searchProtocol(String currentOwner, HttpServletRequest request) throws Exception{
		SecurityService securityService  = getSecurityService(request);
		ProtocolService service = (ProtocolService)request.getSession().getAttribute("protocolService");
		if(service == null){
			service = new ProtocolServiceLocalImpl(securityService);
		}
		Map<String, String> protocols = service.findProtocolsByOwner(currentOwner);
		return protocols;
	}
	
	private SampleService setServiceInSession(HttpServletRequest request)
	throws Exception {
		SecurityService securityService = (SecurityService) request
			.getSession().getAttribute("securityService");
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return sampleService;
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
