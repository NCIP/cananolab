package gov.nih.nci.calab.ui.search;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: RemoteNanoparticleCompositionAction.java,v 1.1 2007-03-19 17:32:06 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComplexComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.MetalParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.QuantumDotComposition;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.ComplexParticleBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.MetalParticleBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.characterization.composition.QuantumDotBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.search.GridSearchService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteNanoparticleCompositionAction extends AbstractDispatchAction{

	private void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();
		theForm.set("dendrimer", new DendrimerBean());
		theForm.set("polymer", new PolymerBean());
		theForm.set("fullerene", new FullereneBean());
		theForm.set("carbonNanotube", new CarbonNanotubeBean());
		theForm.set("emulsion", new EmulsionBean());
		theForm.set("complexParticle", new ComplexParticleBean());
		theForm.set("liposome", new LiposomeBean());
		theForm.set("quantumDot", new QuantumDotBean());
		theForm.set("metalParticle", new MetalParticleBean());
		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("particlePage", mapping.findForward(
				particleType.toLowerCase()).getPath());
	}

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String compositionId = theForm.getString("characterizationId");
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		String gridNodeHost=request.getParameter("gridNodeHost");
		GridNodeBean gridNode = gridNodeMap.get(gridNodeHost);

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setRemoteSideParticleMenu(request,
				particleName, gridNode);

		GridSearchService service = new GridSearchService();
		ParticleComposition comp = service.getRemoteComposition(compositionId,
				particleName, gridNode);

		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		if (particleType.equalsIgnoreCase(CaNanoLabConstants.DENDRIMER_TYPE)) {
			DendrimerBean dendrimer = new DendrimerBean(
					(DendrimerComposition) comp);
			theForm.set("dendrimer", dendrimer);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.POLYMER_TYPE)) {
			PolymerBean polymer = new PolymerBean((PolymerComposition) comp);
			theForm.set("polymer", polymer);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.LIPOSOME_TYPE)) {
			LiposomeBean liposome = new LiposomeBean((LiposomeComposition) comp);
			theForm.set("liposome", liposome);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.FULLERENE_TYPE)) {
			FullereneBean fullerene = new FullereneBean(
					(FullereneComposition) comp);
			theForm.set("fullerene", fullerene);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.CARBON_NANOTUBE_TYPE)) {
			CarbonNanotubeBean carbonNanotube = new CarbonNanotubeBean(
					(CarbonNanotubeComposition) comp);
			theForm.set("carbonNanotube", carbonNanotube);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.EMULSION_TYPE)) {
			EmulsionBean emulsion = new EmulsionBean((EmulsionComposition) comp);
			theForm.set("emulsion", emulsion);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPLEX_PARTICLE_TYPE)) {
			ComplexParticleBean complexParticle = new ComplexParticleBean(
					(ComplexComposition) comp);
			theForm.set("complexParticle", complexParticle);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.QUANTUM_DOT_TYPE)) {
			QuantumDotBean quantumDot = new QuantumDotBean(
					(QuantumDotComposition) comp);
			theForm.set("quantumDot", quantumDot);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.METAL_PARTICLE_TYPE)) {
			MetalParticleBean metalParticle = new MetalParticleBean(
					(MetalParticleComposition) comp);
			theForm.set("metalParticle", metalParticle);
		}
		theForm.set("characterizationSource", comp.getSource());
		theForm.set("viewTitle", comp.getIdentificationName());
		theForm.set("description", comp.getDescription());
		ActionForward forward = mapping.findForward("success");
		return forward;
	}


	@Override
	public boolean loginRequired() {
		// TODO Auto-generated method stub
		return true;
	}
}
