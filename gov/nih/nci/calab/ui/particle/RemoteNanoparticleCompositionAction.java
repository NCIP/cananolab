package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: RemoteNanoparticleCompositionAction.java,v 1.9 2007-12-18 16:36:18 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.BaseRemoteSearchAction;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteNanoparticleCompositionAction extends BaseRemoteSearchAction {

	private void clearMap(HttpSession session, DynaValidatorForm theForm)
			throws Exception {
		// clear session data from the input forms
		theForm.set("dendrimer", new DendrimerBean());
		theForm.set("polymer", new PolymerBean());
		theForm.set("fullerene", new FullereneBean());
		theForm.set("carbonNanotube", new CarbonNanotubeBean());
		theForm.set("emulsion", new EmulsionBean());
		theForm.set("liposome", new LiposomeBean());
		theForm.set("composition", new CompositionBean());
		theForm.set("particle", new ParticleBean());
	}

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// clear session data from the input forms
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		clearMap(session, theForm);

		Map<String, GridNodeBean> gridNodes = prepareSearch(request);
		String compositionId = request.getParameter("characterizationId");
		String particleName = request.getParameter("particleName");
		String particleType = request.getParameter("particleType");
		String gridNodeHost = request.getParameter("gridNodeHost");
		GridNodeBean gridNode = gridNodes.get(gridNodeHost);

		InitParticleSetup.getInstance().setRemoteSideParticleMenu(request,
				particleName, gridNode);

		GridSearchService service = new GridSearchService();
		Nanoparticle particle = service.getRemoteNanoparticle(particleName,
				gridNode);
		theForm.set("particle", new ParticleBean(particle));
		ParticleComposition comp = service.getRemoteComposition(compositionId,
				particleName, gridNode);

		CompositionBean compositionBean = new CompositionBean(comp);
		if (particleType
				.equalsIgnoreCase(Characterization.DENDRIMER_TYPE)) {
			DendrimerBean dendrimer = new DendrimerBean(
					(DendrimerComposition) comp);
			theForm.set("dendrimer", dendrimer);
		} else if (particleType
				.equalsIgnoreCase(Characterization.POLYMER_TYPE)) {
			PolymerBean polymer = new PolymerBean((PolymerComposition) comp);
			theForm.set("polymer", polymer);
		} else if (particleType
				.equalsIgnoreCase(Characterization.LIPOSOME_TYPE)) {
			LiposomeBean liposome = new LiposomeBean((LiposomeComposition) comp);
			theForm.set("liposome", liposome);
		} else if (particleType
				.equalsIgnoreCase(Characterization.FULLERENE_TYPE)) {
			FullereneBean fullerene = new FullereneBean(
					(FullereneComposition) comp);
			theForm.set("fullerene", fullerene);
		} else if (particleType
				.equalsIgnoreCase(Characterization.CARBON_NANOTUBE_TYPE)) {
			CarbonNanotubeBean carbonNanotube = new CarbonNanotubeBean(
					(CarbonNanotubeComposition) comp);
			theForm.set("carbonNanotube", carbonNanotube);
		} else if (particleType
				.equalsIgnoreCase(Characterization.EMULSION_TYPE)) {
			EmulsionBean emulsion = new EmulsionBean((EmulsionComposition) comp);
			theForm.set("emulsion", emulsion);
		}
		theForm.set("composition", compositionBean);

		ActionForward forward = mapping.findForward("success");
		return forward;
	}
}
