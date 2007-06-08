package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSurfaceAction.java,v 1.17 2007-06-08 22:15:48 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceChemistryBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

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
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		CharacterizationBean charBean = super.prepareCreate(request, theForm);
		SurfaceBean propBean = (SurfaceBean) theForm.get("surface");
		SurfaceBean surfaceBean = new SurfaceBean(propBean, charBean);
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleSurface(particleType, particleName,
				surfaceBean);
		ActionMessages msgs = new ActionMessages();
		// ActionMessage msg = new ActionMessage("message.addParticleSize");
		ActionMessage msg = new ActionMessage("message.addParticleSurface");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}

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
		SurfaceBean achar = (SurfaceBean) theForm.get("achar");
		String type = (String) request.getParameter("type");
		if (type != null && !type.equals("") && type.equals("charTables")) {
			updateCharacterizationTables(achar);
		}
		if (type != null && !type.equals("") && type.equals("surfaceChemistries")) {
			updateSurfaceChemistries(achar);;
		}				
		theForm.set("achar", achar);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.getInputForward();
	}

	/**
	 * Update surface chemistry for Surface
	 * 
	 * @param particle
	 */
	private void updateSurfaceChemistries(SurfaceBean surface) {
		int surfaceChemistryNum = Integer.parseInt(surface.getNumberOfSurfaceChemistries());
		List<SurfaceChemistryBean> origSurfaceChemistries = surface
				.getSurfaceChemistries();
		int origNum = (origSurfaceChemistries == null) ? 0
				: origSurfaceChemistries.size();
		List<SurfaceChemistryBean> surfaceChemistries = new ArrayList<SurfaceChemistryBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < surfaceChemistryNum; i++) {
				SurfaceChemistryBean surfaceChemistry = new SurfaceChemistryBean();
				surfaceChemistries.add(surfaceChemistry);
			}
		}
		// use keep original surface group info
		else if (surfaceChemistryNum <= origNum) {
			for (int i = 0; i < surfaceChemistryNum; i++) {
				surfaceChemistries
						.add((SurfaceChemistryBean) origSurfaceChemistries
								.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				surfaceChemistries
						.add((SurfaceChemistryBean) origSurfaceChemistries
								.get(i));
			}
			for (int i = origNum; i < surfaceChemistryNum; i++) {
				surfaceChemistries.add(new SurfaceChemistryBean());
			}
		}
		surface.setSurfaceChemistries(surfaceChemistries);
	}

	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "surface");
		request.setAttribute("loadFileForward", "surfaceInputForm");

	}
}
