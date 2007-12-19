package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for Shape characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleShapeAction.java,v 1.5 2007-12-13 16:30:36 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleShapeAction extends BaseCharacterizationAction {
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
		ShapeBean propBean = (ShapeBean) theForm.get("shape");
		ShapeBean shapeBean = new ShapeBean(propBean, charBean);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticleShape(shapeBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			ShapeBean aShapeBean = new ShapeBean(propBean, acharBean);
			service.addParticleShape(aShapeBean);
		}
		return postCreate(request, theForm, mapping);	}
}
