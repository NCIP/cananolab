package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleFunctionAction.java,v 1.13 2007-08-08 18:56:01 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.dto.function.AgentBean;
import gov.nih.nci.calab.dto.function.AgentTargetBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.function.LinkageBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleFunctionAction extends AbstractDispatchAction {
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
		FunctionBean function = (FunctionBean) theForm.get("function");

		if (function.getId() == null || function.getId() == "") {
			function.setId((String) theForm.get("functionId"));
		}

		// validate agent target
		for (LinkageBean linkageBean : function.getLinkages()) {
			for (AgentTargetBean agentTargetBean : linkageBean.getAgent()
					.getAgentTargets()) {
				if (agentTargetBean.getName().length() == 0
						&& agentTargetBean.getDescription().length() == 0
						&& agentTargetBean.getType().length() == 0) {
					throw new RuntimeException("Agent target type can not be empty.");
				}
			}
		}
		request.getSession().setAttribute("newFunctionCreated", "true");
		request.getSession().setAttribute("newBondTypeCreated", "true");
		request.getSession().setAttribute("newContrastAgentTypeCreated", "true");
		// TODO save in database
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleFunction(particleType, particleName, function);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addparticle.function");
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
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		InitSessionSetup.getInstance().setAllFunctionDropdowns(session);
	}

	/**
	 * Set up the form for updating existing characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		initSetup(request, theForm);
		String functionId = (String) theForm.get("functionId");

		SearchNanoparticleService service = new SearchNanoparticleService();
		Function aFunc = service.getFunctionBy(functionId);

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		FunctionBean function = new FunctionBean(aFunc);
		theForm.set("function", function);
		return mapping.findForward("setup");
	}

	/**
	 * Prepare the form for viewing existing characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward addLinkage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionBean function = (FunctionBean) theForm.get("function");
		List<LinkageBean> origLinkages = function.getLinkages();
		int origNum = (origLinkages == null) ? 0 : origLinkages.size();

		List<LinkageBean> linkages = new ArrayList<LinkageBean>();
		for (int i = 0; i < origNum; i++) {
			linkages.add((LinkageBean) origLinkages.get(i));
		}
		// add a new one
		linkages.add(new LinkageBean());
		function.setLinkages(linkages);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward removeLinkage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String linkageIndex = (String) request.getParameter("linkageInd");
		int ind = Integer.parseInt(linkageIndex);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionBean function = (FunctionBean) theForm.get("function");
		List<LinkageBean> origLinkages = function.getLinkages();
		int origNum = (origLinkages == null) ? 0 : origLinkages.size();
		List<LinkageBean> linkages = new ArrayList<LinkageBean>();
		for (int i = 0; i < origNum; i++) {
			linkages.add(origLinkages.get(i));
		}
		// remove the one at the index
		if (origNum > 0) {
			linkages.remove(ind);
		}
		function.setLinkages(linkages);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward addTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String linkageIndex = (String) request.getParameter("linkageInd");
		int ind = Integer.parseInt(linkageIndex);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionBean function = (FunctionBean) theForm.get("function");
		AgentBean agent = function.getLinkages().get(ind).getAgent();
		List<AgentTargetBean> origTargets = agent.getAgentTargets();
		int origNum = (origTargets == null) ? 0 : origTargets.size();
		List<AgentTargetBean> targets = new ArrayList<AgentTargetBean>();
		for (int i = 0; i < origNum; i++) {
			targets.add(origTargets.get(i));
		}
		// add a new one
		targets.add(new AgentTargetBean());
		agent.setAgentTargets(targets);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward removeTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		String linkageIndex = (String) request.getParameter("linkageInd");
		int ind = Integer.parseInt(linkageIndex);
		String targetIndex = (String) request.getParameter("targetInd");
		int tInd = Integer.parseInt(targetIndex);

		FunctionBean function = (FunctionBean) theForm.get("function");
		AgentBean agent = function.getLinkages().get(ind).getAgent();
		List<AgentTargetBean> origTargets = agent.getAgentTargets();
		int origNum = (origTargets == null) ? 0 : origTargets.size();

		List<AgentTargetBean> targets = new ArrayList<AgentTargetBean>();
		for (int i = 0; i < origNum; i++) {
			targets.add(origTargets.get(i));
		}
		// remove the one at the index
		if (origNum > 0) {
			targets.remove(tInd);
		}
		agent.setAgentTargets(targets);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		// update editable dropdowns
		FunctionBean function = (FunctionBean) theForm.get("function");
		HttpSession session = request.getSession();
		updateEditables(session, function);
		return mapping.findForward("setup");
	}

	private void updateEditables(HttpSession session, FunctionBean function)
			throws Exception {
		// update bondType editable dropdowns
		for (LinkageBean linkage : function.getLinkages()) {
			if (linkage.getType() != null
					&& linkage.getType().equals(CaNanoLabConstants.ATTACHMENT)) {
				InitSessionSetup.getInstance().updateEditableDropdown(session,
						linkage.getBondType(), "allBondTypes");
			}
			if (linkage.getAgent() != null
					&& linkage.getAgent().getType() != null
					&& linkage.getAgent().getType().equals(
							CaNanoLabConstants.IMAGE_CONTRAST_AGENT)) {
				InitSessionSetup.getInstance().updateEditableDropdown(session,
						linkage.getAgent().getImageContrastAgent().getType(),
						"allImageContrastAgentTypes");
			}
		}
	}

	public boolean loginRequired() {
		return true;
	}
}
