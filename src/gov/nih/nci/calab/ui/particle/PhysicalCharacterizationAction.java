package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: PhysicalCharacterizationAction.java,v 1.1 2007-12-18 16:35:41 pansu Exp $ */

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceChemistryBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class PhysicalCharacterizationAction extends BaseCharacterizationAction {

	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		super.initSetup(request, theForm);
		InitParticleSetup.getInstance().setAllPhysicalDropdowns(
				request.getSession());
	}

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
	public ActionForward size(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticleSize(charBean);
		theForm.set("achar", charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addParticleSize(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

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
	public ActionForward surface(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		SurfaceBean propBean = (SurfaceBean) theForm.get("surface");
		SurfaceBean surfaceBean = new SurfaceBean(propBean, charBean);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticleSurface(surfaceBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			SurfaceBean aSurfaceBean = new SurfaceBean(propBean, acharBean);
			service.addParticleSurface(aSurfaceBean);
		}
		return postCreate(request, theForm, mapping);
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
	public ActionForward solubility(ActionMapping mapping, ActionForm form,
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
		return postCreate(request, theForm, mapping);
	}

	public ActionForward shape(ActionMapping mapping, ActionForm form,
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
		return postCreate(request, theForm, mapping);
	}

	public ActionForward purity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticlePurity(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addParticlePurity(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward morphology(ActionMapping mapping, ActionForm form,
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
		return postCreate(request, theForm, mapping);
	}

	public ActionForward molecularWeight(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addParticleMolecularWeight(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addParticleMolecularWeight(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}
}
