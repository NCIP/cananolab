package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for InVitro Coagulation characterization. 
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

public class InvitroCharacterizationAction extends BaseCharacterizationAction {
	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		super.initSetup(request, theForm);
		InitParticleSetup.getInstance().setAllInvitroDropdowns(
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
	public ActionForward coagulation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addCoagulation(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addCoagulation(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward caspase3Activation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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

	public ActionForward cellViability(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		CytotoxicityBean propBean = (CytotoxicityBean) theForm
				.get("cytotoxicity");
		CytotoxicityBean cytoBean = new CytotoxicityBean(propBean, charBean);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addCellViability(cytoBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			CytotoxicityBean aCytoBean = new CytotoxicityBean(propBean,
					acharBean);
			service.addCellViability(aCytoBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward cFU_GM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addCFU_GM(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addCFU_GM(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward chemotaxis(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addChemotaxis(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addChemotaxis(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward complementActivation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addComplementActivation(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addComplementActivation(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward cytokineInduction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addCytokineInduction(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addCytokineInduction(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward enzymeInduction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addEnzymeInduction(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addEnzymeInduction(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addEnzymeInduction(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addEnzymeInduction(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward hemolysis(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addHemolysis(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addHemolysis(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward leukocyteProliferation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addLeukocyteProliferation(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);

		for (CharacterizationBean acharBean : otherChars) {
			service.addLeukocyteProliferation(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward nKCellCytotoxicActivity(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addNKCellCytotoxicActivity(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addNKCellCytotoxicActivity(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward oxidativeBurst(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addOxidativeBurst(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addOxidativeBurst(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward oxidativeStress(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addOxidativeStress(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addOxidativeStress(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward phagocytosis(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addPhagocytosis(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addPhagocytosis(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}

	public ActionForward plateletAggregation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addPlateletAggregation(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addPlateletAggregation(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}
	
	public ActionForward proteinBinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = prepareCreate(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addProteinBinding(charBean);
		CharacterizationBean[] otherChars = prepareCopy(request, theForm);
		for (CharacterizationBean acharBean : otherChars) {
			service.addProteinBinding(acharBean);
		}
		return postCreate(request, theForm, mapping);
	}
}
