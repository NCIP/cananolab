package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleFunctionAction.java,v 1.1 2006-11-03 21:16:37 zengje Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.function.LinkageBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;


public class NanoparticleFunctionAction extends AbstractDispatchAction {
	private static Logger logger = Logger.getLogger(NanoparticleFunctionAction.class);

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
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		FunctionBean function =(FunctionBean) theForm.get("function");
		
		String viewTitle = (String) theForm.get("viewTitle");
		String description = (String) theForm.get("description");
//		String characterizationSource = (String) theForm.get("characterizationSource");
		//sizeChar.setCharacterizationSource(characterizationSource);
		//sizeChar.setDescription(description);
		//sizeChar.setViewTitle(viewTitle);

		if (function.getId() == null || function.getId() == "") {
			
			function.setId( (String) theForm.get("functionId") );
			
		}
		
		request.getSession().setAttribute("newFunction", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
//		service.addParticleSurface(particleType, particleName, function);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleSurface");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return forward;
	}

	/**
	 * Set up the input forms for adding data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		initSetup(request, theForm);
		return mapping.getInputForward();
	}

	private void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("function", new FunctionBean());
	}

	private void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		InitSessionSetup.getInstance().setAllAgentTypes(session);
		InitSessionSetup.getInstance().setAllAgentTargetTypes(session);	
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
	}

	/**
	 * Set up the input forms for updating data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
//	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		String particleType = (String) theForm.get("particleType");
//		String functionId = (String) theForm.get("functionId");
//		
////		SearchNanoparticleService service = new SearchNanoparticleService();
////		Function function = (Surface)service.getFunctionBy(functionId);
//		
//		HttpSession session = request.getSession();
//		// clear session data from the input forms
//		clearMap(session, theForm, mapping);
//
//		theForm.set("functionId", functionId);
//		theForm.set("viewTitle", aChar.getIdentificationName());
//		theForm.set("description", aChar.getDescription());
//
//		
//		FunctionBean sChar = new FunctionBean(aChar);
//		
//		theForm.set("achar", sChar);
//		
//		initSetup(request, theForm);
//
//		if (sChar.getInstrument() != null) {
//			InitSessionSetup.getInstance().setManufacturerPerType(session, sChar.getInstrument().getType());
//			session.setAttribute("selectedInstrumentType", sChar.getInstrument().getType());
//		}
//
//		return mapping.getInputForward();
//	}

	/**
	 * Set up the input fields for read only view data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
//	public ActionForward view(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		return setupUpdate(mapping, form, request, response);
//	}

	/**
	 * Update multiple children on the same form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		FunctionBean function = (FunctionBean) theForm.get("function");
		updateAgents(function);
		theForm.set("function", function);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.getInputForward();
	}

	/**
	 * Update surface chemistry for Surface
	 * 
	 * @param particle
	 */
	private void updateAgents(FunctionBean function) {
		int agentNum = function.getNumberOfLinkage();
		List<LinkageBean> origLinkages = function.getLinkages();
		int origNum = (origLinkages == null) ? 0 : origLinkages
				.size();
		List<LinkageBean> linkages = new ArrayList<LinkageBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < agentNum; i++) {
				LinkageBean linkage = new LinkageBean();
				linkages.add(linkage);
			}
		}
		// use keep original surface group info
		else if (agentNum <= origNum) {
			for (int i = 0; i < agentNum; i++) {
				linkages.add((LinkageBean) origLinkages.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				linkages.add((LinkageBean) origLinkages.get(i));
			}
			for (int i = origNum; i < agentNum; i++) {
				linkages.add(new LinkageBean());
			}
		}
		function.setLinkages(linkages);
	}
	
	public ActionForward updateAgentForm(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
		throws Exception {
		
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
	
		FunctionBean function=(FunctionBean) theForm.get("function");
		
		int index = Integer.parseInt(request.getParameter("index"));
		
		LinkageBean linkage = function.getLinkages().get(index);
		
//		String agentType = for
//		linkage.getAgent().setType()
		
		
//		if (function.getLinkages().getAgent() != null) {
//			String type = function.getInstrument().getType();
//			session.setAttribute("selectedInstrumentType", type);
//			//logger.info("***************Action: getting manufacturers for " + type);
//			InitSessionSetup.getInstance().setManufacturerPerType(session, sizeChar.getInstrument().getType());
//		}

		return mapping.getInputForward();
		
	}
	
	
	
	public boolean loginRequired() {
		return true;
	}
	

}
