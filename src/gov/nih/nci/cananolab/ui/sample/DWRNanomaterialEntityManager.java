package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;

import java.util.List;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;

/**
 * Work with DWR to set up drop-downs required in the nanomaterial page
 *
 * @author pansu
 *
 */
public class DWRNanomaterialEntityManager {
	public DWRNanomaterialEntityManager() {
	}

	public ComposingElementBean addInherentFunction(FunctionBean function) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		entity.getTheComposingElement().addFunction(function);
		return entity.getTheComposingElement();
	}

	public ComposingElementBean deleteInherentFunction(FunctionBean function) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		entity.getTheComposingElement().removeFunction(function);
		return entity.getTheComposingElement();
	}

	public ComposingElementBean getComposingElementFromList(int index)
			throws Exception {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		List<ComposingElementBean> composingElements = entity
				.getComposingElements();
		ComposingElementBean composingElement = composingElements.get(index);
		entity.setTheComposingElement(composingElement);
		return composingElement;
	}

	public ComposingElementBean resetTheComposingElement() {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		ComposingElementBean elementBean = new ComposingElementBean();
		entity.setTheComposingElement(elementBean);
		return elementBean;
	}
}
