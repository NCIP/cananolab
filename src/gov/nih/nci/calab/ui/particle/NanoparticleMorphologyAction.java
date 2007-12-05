package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for Morphology characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleMorphologyAction.java,v 1.4 2007-12-05 20:01:09 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleMorphologyAction extends BaseCharacterizationAction {

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
		MorphologyBean propBean = (MorphologyBean) theForm.get("morphology");
		MorphologyBean morphologyBean = new MorphologyBean(propBean, charBean);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticleMorphology(morphologyBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			MorphologyBean aMorphologyBean = new MorphologyBean(propBean,
					acharBean);
			service.addParticleMorphology(aMorphologyBean);
		}
		request.getSession().setAttribute("newMorphologyCreated", "true");
		return postCreate(request, theForm, mapping);	}
}
