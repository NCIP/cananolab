package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.security.UserBean;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Work with DWR to set up drop-downs required in the functionalizing entity
 * page
 *
 * @author pansu
 *
 */
public class DWRFunctionalizingEntityManager {
	private CompositionServiceHelper helper;

	private CompositionServiceHelper getHelper() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		helper = new CompositionServiceHelper(user);
		return helper;
	}

	public FunctionBean addTarget(TargetBean target) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		entity.getTheFunction().addTarget(target);
		return entity.getTheFunction();
	}

	public FunctionBean deleteTarget(TargetBean target) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		entity.getTheFunction().removeTarget(target);
		return entity.getTheFunction();
	}

	public FunctionBean getFunctionById(String id) throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		DynaValidatorForm compositionForm = (DynaValidatorForm) (wctx
				.getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		Function function = getHelper().findFunctionById(id);
		FunctionBean functionBean = new FunctionBean(function);
		entity.setTheFunction(functionBean);
		return functionBean;
	}

	public FunctionBean resetTheFunction() {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		FunctionBean function = new FunctionBean();
		entity.setTheFunction(function);
		return function;
	}
}
