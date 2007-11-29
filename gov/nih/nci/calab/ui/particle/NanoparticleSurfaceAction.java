package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSurfaceAction.java,v 1.5 2007-11-29 19:20:06 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceChemistryBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleSurfaceAction extends BaseCharacterizationAction {
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
		CharacterizationBean charBean = super.prepareCreate(request, theForm);
		SurfaceBean propBean = (SurfaceBean) theForm.get("surface");
		SurfaceBean surfaceBean = new SurfaceBean(propBean, charBean);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticleSurface(surfaceBean);
		CharacterizationBean[] otherChars = super.prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			SurfaceBean aSurfaceBean = new SurfaceBean(propBean, acharBean);
			service.addParticleSurface(aSurfaceBean);
		}
		super.postCreate(request, theForm);
		request.getSession().setAttribute("newSurfaceCreated", "true");
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleSurface");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}

	public ActionForward addSurfaceChemistry(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SurfaceBean surface = (SurfaceBean) theForm.get("surface");
		List<SurfaceChemistryBean> origChemistries = surface
				.getSurfaceChemistries();

		int origNum = (origChemistries == null) ? 0 : origChemistries.size();
		List<SurfaceChemistryBean> chems = new ArrayList<SurfaceChemistryBean>();
		for (int i = 0; i < origNum; i++) {
			chems.add(origChemistries.get(i));
		}
		// add a new one
		chems.add(new SurfaceChemistryBean());
		surface.setSurfaceChemistries(chems);
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		return input(mapping, form, request, response);
	}

	public ActionForward removeSurfaceChemistry(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
		int compInd = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SurfaceBean surface = (SurfaceBean) theForm.get("surface");
		List<SurfaceChemistryBean> origChemistries = surface
				.getSurfaceChemistries();

		int origNum = (origChemistries == null) ? 0 : origChemistries.size();
		List<SurfaceChemistryBean> chems = new ArrayList<SurfaceChemistryBean>();
		for (int i = 0; i < origNum; i++) {
			chems.add(origChemistries.get(i));
		}
		// remove the one at the index
		if (origNum > 0) {
			chems.remove(compInd);
		}
		surface.setSurfaceChemistries(chems);
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		return input(mapping, form, request, response);
	}
}
