package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for InVitro Caspase3Activation characterization. 
 *  
 * @author beasleyj
 */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class InvitroCaspase3ActivationAction extends BaseCharacterizationAction {

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
		CytotoxicityBean propBean = (CytotoxicityBean) theForm
				.get("cytotoxicity");
		CytotoxicityBean cytoBean = new CytotoxicityBean(propBean, charBean);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addCaspase3Activation(cytoBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			CytotoxicityBean aCytoBean = new CytotoxicityBean(propBean,
					acharBean);
			service.addCaspase3Activation(aCytoBean);
		}
		return postCreate(request, theForm, mapping);
	}

}
