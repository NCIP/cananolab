package gov.nih.nci.cananolab.restful.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.BaseCompositionEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("compositionManager")
public class CompositionManager
{
	@Autowired
	private CompositionService compositionService;

	public String getEntityIncludePage(String entityType, String parent)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			String includePage = InitCompositionSetup.getInstance().getDetailPage(entityType, parent);
			String content = wctx.forwardToString(includePage);
			return content;
		} catch (Exception e) {
			return "";
		}
	}

	public FileBean getFileById(String type, String id) throws Exception {
//		WebContext wctx = WebContextFactory.get();
//		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
//		if (user == null) {
//			return null;
//		}
//		FileBean fileBean = getService().findFileById(id);
//		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("compositionForm"));
//		if (type.equals("nanomaterialEntity")) {
//			NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
//					.get("nanomaterialEntity");
//			entity.setTheFile(fileBean);
//		} else if (type.equals("functionalizingEntity")) {
//			FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
//					.get("functionalizingEntity");
//			entity.setTheFile(fileBean);
//		} else if (type.equals("comp")) {
//			CompositionBean comp = (CompositionBean) compositionForm
//					.get("comp");
//			comp.setTheFile(fileBean);
//		} else {
//			ChemicalAssociationBean assoc = (ChemicalAssociationBean) compositionForm
//					.get("assoc");
//			assoc.setTheFile(fileBean);
//		}
//		return fileBean;
		
		return null;
	}

	public FileBean resetTheFile(String type) throws Exception {
//		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("compositionForm"));
//		if (compositionForm == null) {
//			return null;
//		}
//		FileBean fileBean = new FileBean();
//
//		if (type.equals("nanomaterialEntity")) {
//			NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
//					.get("nanomaterialEntity");
//			entity.setTheFile(fileBean);
//		} else if (type.equals("functionalizingEntity")) {
//			FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
//					.get("functionalizingEntity");
//			entity.setTheFile(fileBean);
//		} else if (type.equals("comp")) {
//			CompositionBean comp = (CompositionBean) compositionForm
//					.get("comp");
//			comp.setTheFile(fileBean);
//		} else {
//			ChemicalAssociationBean assoc = (ChemicalAssociationBean) compositionForm
//					.get("assoc");
//			assoc.setTheFile(fileBean);
//		}
//		return fileBean;
		return null;
	}

	public List<BaseCompositionEntityBean> getAssociatedElementOptions(
			String compositionType, HttpServletRequest request) {
		if (StringUtils.isEmpty(compositionType)) {
			return new ArrayList<BaseCompositionEntityBean>();
		}
		List<BaseCompositionEntityBean> entities = null;
		if (SpringSecurityUtil.getPrincipal() == null) {
			return null;
		}
		if (compositionType.equals("nanomaterial entity")) {
			entities = (List<BaseCompositionEntityBean>) request.getSession()
					.getAttribute("sampleMaterialEntities");

		} else if (compositionType.equals("functionalizing entity")) {
			entities = (List<BaseCompositionEntityBean>) request.getSession()
					.getAttribute("sampleFunctionalizingEntities");
		}

		return entities;
	}

	public List<ComposingElementBean> getComposingElementsByNanomaterialEntityId(String id, HttpServletRequest request) throws Exception
	{
		if (SpringSecurityUtil.getPrincipal() == null || StringUtils.isEmpty(id))
			return null;
		
		NanomaterialEntityBean entityBean = compositionService.findNanomaterialEntityById(id);
		List<ComposingElementBean> composingElements = entityBean.getComposingElements();
		return composingElements;
	}
	
}