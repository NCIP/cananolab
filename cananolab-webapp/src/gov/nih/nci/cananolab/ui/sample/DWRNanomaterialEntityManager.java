package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.security.UserBean;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Work with DWR to set up drop-downs required in the nanomaterial entity page
 *
 * @author pansu
 *
 */
public class DWRNanomaterialEntityManager {
	private CompositionServiceHelper helper;

	private CompositionServiceHelper getHelper() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		helper = new CompositionServiceHelper(user);
		return helper;
	}

	public ComposingElementBean addInherentFunction(FunctionBean function) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		entity.getTheComposingElement().addFunction(function);
		return entity.getTheComposingElement();
	}

	public ComposingElementBean deleteInherentFunction(FunctionBean function) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		entity.getTheComposingElement().removeFunction(function);
		return entity.getTheComposingElement();
	}

	public ComposingElementBean getComposingElementById(String id)
			throws Exception {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		ComposingElement composingElement = getHelper().findComposingElementById(id);
		ComposingElementBean composingElementBean = new ComposingElementBean(
				composingElement);
		entity.setTheComposingElement(composingElementBean);
		return composingElementBean;
	}

	public ComposingElementBean resetTheComposingElement() {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		ComposingElementBean elementBean = new ComposingElementBean();
		entity.setTheComposingElement(elementBean);
		return elementBean;
	}
}
