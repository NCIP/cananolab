package gov.nih.nci.cananolab.ui.common;

import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.service.common.impl.ExperimentConfigServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Submits and updates technique and associated instruments of an
 * ExperimentConfig
 *
 * @author pansu, tanq
 *
 */

public class SubmitExperimentConfigAction extends BaseAnnotationAction {

	private static Logger logger = Logger
			.getLogger(SubmitExperimentConfigAction.class);

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		if (request.getSession().getAttribute("experimentConfigToSave") != null) {
			ExperimentConfigBean configBean = (ExperimentConfigBean) (request
					.getSession().getAttribute("experimentConfigToSave"));
			ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();
			service.saveExperimentConfig(configBean.getDomain());
		}
		return forward;
	}

	public ActionForward addInstrument(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean aCharbean = (CharacterizationBean) theForm
				.get("aChar");
		aCharbean.getTheExperimentConfig().addInstrument();
		return mapping.getInputForward();
	}
}
