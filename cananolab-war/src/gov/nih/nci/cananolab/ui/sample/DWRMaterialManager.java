package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FunctionBean;
import gov.nih.nci.cananolab.dto.common.MaterialBean;
import gov.nih.nci.cananolab.dto.common.TargetBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;

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
public class DWRMaterialManager {
	private CompositionServiceHelper helper = new CompositionServiceHelper();

	public DWRMaterialManager() {
	}

//	public FunctionBean addTarget(TargetBean target) {
//		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("compositionForm"));
//		if (compositionForm==null) {
//			return null;
//		}
//		MaterialBean entity = (MaterialBean) compositionForm
//				.get("functionalizingEntity");
//		entity.getTheFunction().addTarget(target);
//		return entity.getTheFunction();
//	}
//
//	public FunctionBean deleteTarget(TargetBean target) {
//		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("compositionForm"));
//		if (compositionForm==null) {
//			return null;
//		}
//		MaterialBean entity = (MaterialBean) compositionForm
//				.get("functionalizingEntity");
//		entity.getTheFunction().removeTarget(target);
//		return entity.getTheFunction();
//	}

//	public FunctionBean getFunctionById(String id) throws Exception {
//		WebContext wctx = WebContextFactory.get();
//		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
//		DynaValidatorForm compositionForm = (DynaValidatorForm) (wctx
//				.getSession().getAttribute("compositionForm"));
//		if (compositionForm==null) {
//			return null;
//		}
//		MaterialBean entity = (MaterialBean) compositionForm
//				.get("functionalizingEntity");
//		Function function = helper.findFunctionById(id, user);
//		FunctionBean functionBean = new FunctionBean(function);
//		entity.setTheFunction(functionBean);
//		return functionBean;
//	}

//	public FunctionBean resetTheFunction() {
//		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("compositionForm"));
//		if (compositionForm==null) {
//			return null;
//		}
//		MaterialBean entity = (MaterialBean) compositionForm
//				.get("functionalizingEntity");
//		FunctionBean function = new FunctionBean();
//		entity.setTheFunction(function);
//		return function;
//	}
}
