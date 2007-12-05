package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for Solubility characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSolubilityAction.java,v 1.4 2007-12-05 20:01:09 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleSolubilityAction extends BaseCharacterizationAction {
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
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		SolubilityBean propBean = (SolubilityBean) theForm.get("solubility");
		SolubilityBean solubilityBean = new SolubilityBean(propBean, charBean);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticleSolubility(solubilityBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			SolubilityBean aSolubilityBean = new SolubilityBean(propBean,
					acharBean);
			service.addParticleSolubility(aSolubilityBean);
		}
		request.getSession().setAttribute("newSolubilityCreated", "true");
		return postCreate(request, theForm, mapping);
	}
}
