package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

/**
 * Work with DWR to set up drop-downs required in the functionalizing entity
 * page
 *
 * @author pansu
 *
 */
public class DWRFunctionalizingEntityManager {
	private CompositionServiceHelper helper = new CompositionServiceHelper();

	public DWRFunctionalizingEntityManager() {
	}

	public FunctionBean addTarget(TargetBean target) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm==null) {
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
		if (compositionForm==null) {
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
		if (compositionForm==null) {
			return null;
		}
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		Function function = helper.findFunctionById(id, user);
		FunctionBean functionBean = new FunctionBean(function);
		// update function type based mapping stored in session
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		functionBean
				.updateType(InitSetup.getInstance()
						.getClassNameToDisplayNameLookup(
								webContext.getServletContext()));
		entity.setTheFunction(functionBean);
		return functionBean;
	}

	public FunctionBean resetTheFunction() {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm==null) {
			return null;
		}
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
				.get("functionalizingEntity");
		FunctionBean function = new FunctionBean();
		entity.setTheFunction(function);
		return function;
	}
}
