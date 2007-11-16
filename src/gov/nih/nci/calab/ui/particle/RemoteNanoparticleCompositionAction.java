package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: RemoteNanoparticleCompositionAction.java,v 1.3 2007-11-16 19:16:24 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteNanoparticleCompositionAction extends AbstractDispatchAction {

	private void clearMap(HttpSession session, DynaValidatorForm theForm)
			throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();
		theForm.set("composition", new CompositionBean());
		theForm.set("dendrimer", new DendrimerBean());
		theForm.set("polymer", new PolymerBean());
		theForm.set("fullerene", new FullereneBean());
		theForm.set("carbonNanotube", new CarbonNanotubeBean());
		theForm.set("emulsion", new EmulsionBean());
		theForm.set("liposome", new LiposomeBean());
		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
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
		String gridNodeHost = request.getParameter("gridNodeHost");
		GridNodeBean gridNode = gridNodeMap.get(gridNodeHost);

		HttpSession session = request.getSession();
		InitParticleSetup.getInstance().setRemoteSideParticleMenu(request,
				particleName, gridNode);

		GridSearchService service = new GridSearchService();
		ParticleComposition comp = service.getRemoteComposition(compositionId,
				particleName, gridNode);
		CompositionBean compositionBean=new CompositionBean(comp);		
		// clear session data from the input forms
		clearMap(session, theForm);
		if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_DENDRIMER_TYPE)) {
			DendrimerBean dendrimer = new DendrimerBean(
					(DendrimerComposition) comp);
			theForm.set("dendrimer", dendrimer);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_POLYMER_TYPE)) {
			PolymerBean polymer = new PolymerBean((PolymerComposition) comp);
			theForm.set("polymer", polymer);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_LIPOSOME_TYPE)) {
			LiposomeBean liposome = new LiposomeBean((LiposomeComposition) comp);
			theForm.set("liposome", liposome);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_FULLERENE_TYPE)) {
			FullereneBean fullerene = new FullereneBean(
					(FullereneComposition) comp);
			theForm.set("fullerene", fullerene);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_CARBON_NANOTUBE_TYPE)) {
			CarbonNanotubeBean carbonNanotube = new CarbonNanotubeBean(
					(CarbonNanotubeComposition) comp);
			theForm.set("carbonNanotube", carbonNanotube);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_EMULSION_TYPE)) {
			EmulsionBean emulsion = new EmulsionBean((EmulsionComposition) comp);
			theForm.set("emulsion", emulsion);
		}
		theForm.set("composition", compositionBean );
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return true;
	}
}
