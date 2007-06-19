/**
 * 
 */
package gov.nih.nci.calab.ui.submit;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

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
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;

		// setCharacterizationTypeCharacterizations
		String deleteType = request.getParameter("charCategory");
		Map<String, List<CharacterizationBean>> charsMap = (Map<String, List<CharacterizationBean>>) (request
				.getSession().getAttribute("allCharacterizations"));
		List<CharacterizationBean> charBeans = charsMap.get(deleteType);

		// convert charBeans to array due to the struts-config
		if (charBeans != null) {
			theForm.set("charBeans", charBeans
					.toArray(new CharacterizationBean[charBeans.size()]));
			request.setAttribute("charBeansValue", "true");
		}

		forward = mapping.findForward("success");

		return forward;
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

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.delete.characterization");
		msgs.add("message", msg);
		saveMessages(request, msgs);

		forward = mapping.findForward("success");

		return forward;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.calab.ui.core.AbstractDispatchAction#loginRequired()
	 */
	@Override
	public boolean loginRequired() {
		// TODO Auto-generated method stub
		return true;
	}

}
