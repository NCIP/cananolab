package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for InVitro CellViability characterization. 
 *  
 * @author beasleyj
 */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class InvitroCellViabilityAction extends BaseCharacterizationAction {

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
		CharacterizationBean charBean = super.prepareCreate(request, theForm);
		CytotoxicityBean propBean = (CytotoxicityBean) theForm
				.get("cytotoxicity");
		CytotoxicityBean cytoBean = new CytotoxicityBean(propBean, charBean);
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addCellViability(particleType, particleName, cytoBean);
		CharacterizationBean[] otherChars=super.prepareCopy(request, theForm, service);
		for (CharacterizationBean acharBean: otherChars) {
			service.addCellViability(particleType, particleName, acharBean);
		}
		super.postCreate(request, theForm);
		request.getSession().setAttribute("newCytoCreated", "true");
		
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addInvitroCellViability");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}
}
