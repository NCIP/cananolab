package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;

import java.util.List;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;

/**
 * Work with DWR to set up drop-downs required in the functionalizing entity page
 *
 * @author pansu
 *
 */
public class DWRFunctionalizingEntityManager {
	public DWRFunctionalizingEntityManager() {
	}

	public FunctionBean addTarget(TargetBean target) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		entity.getTheFunction().addTarget(target);
		return entity.getTheFunction();
	}

	public FunctionBean deleteTarget(TargetBean target) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		entity.getTheFunction().removeTarget(target);
		return entity.getTheFunction();
	}

	public FunctionBean getFunctionFromList(int index)
			throws Exception {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		List<FunctionBean> functions = entity
				.getFunctions();
		FunctionBean function = functions.get(index);
		entity.setTheFunction(function);
		return function;
	}

	public FunctionBean resetTheFunction() {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		FunctionBean function = new FunctionBean();
		entity.setTheFunction(function);
		return function;
	}
}
