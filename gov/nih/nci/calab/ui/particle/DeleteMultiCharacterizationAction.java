/**
 * 
 */
package gov.nih.nci.calab.ui.particle;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.particle.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author zengje
 * 
 */
public class DeleteMultiCharacterizationAction extends AbstractDispatchAction {

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// setCharacterizationTypeCharacterizations
		String deleteType = request.getParameter("charCategory");
		
		
		Map<String, List<CharacterizationBean>> charsMap = (Map<String, List<CharacterizationBean>>) (request
				.getSession().getAttribute("charaLeafToCharacterizations"));
		List<CharacterizationBean> charBeans = charsMap.get(deleteType);
		
		if (charBeans != null) {
			request.getSession().setAttribute("charBeans", charBeans);
			return mapping.getInputForward();

		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.delete.no.characterizations");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			return mapping.findForward("message");
		}

	}

	public ActionForward deleteConfirmed(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String[] charIds = (String[]) theForm.get("charIds");
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// setCharacterizationTypeCharacterizations
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.deleteCharacterizations(particleName, particleType, charIds);

		// signal the session that characterization has been changed
		request.getSession().setAttribute("newCharacterizationCreated", "true");

		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.delete.characterization");
		msgs.add("message", msg);
		saveMessages(request, msgs);

		forward = mapping.findForward("message");

		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasDeletePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}
