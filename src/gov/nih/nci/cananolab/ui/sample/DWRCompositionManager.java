package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.MaterialBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Work with DWR to set up drop-downs required in the composition pages
 * 
 * @author pansu, cais
 * 
 */
public class DWRCompositionManager {
	public DWRCompositionManager() {
	}

	public String getEntityIncludePage(String entityType, String parent)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			String includePage = InitCompositionSetup.getInstance()
					.getDetailPage(entityType, parent);
			String content = wctx.forwardToString(includePage);
			return content;
		} catch (Exception e) {
			return "";
		}
	}

//	public FileBean getFileById(String type, String id) throws Exception {
//		WebContext wctx = WebContextFactory.get();
//		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
//		if (user == null) {
//			return null;
//		}
//		FileService service = new FileServiceLocalImpl();
//		FileBean fileBean = service.findFileById(id, user);
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
//	}

//	public FileBean resetTheFile(String type) {
//		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("compositionForm"));
//		if (compositionForm == null) {
//			return null;
//		}
//		FileBean fileBean = new FileBean();
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
//	}

	public List<MaterialBean> getAssociatedElementOptions(
			String compositionType) {
		if (StringUtils.isEmpty(compositionType)) {
			return new ArrayList<MaterialBean>();
		}
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		List<MaterialBean> entities = null;
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		if (compositionType.equals("nanomaterial entity")) {
			entities = (List<MaterialBean>) request.getSession()
					.getAttribute("sampleMaterialEntities");

		} else if (compositionType.equals("functionalizing entity")) {
			entities = (List<MaterialBean>) request.getSession()
					.getAttribute("sampleFunctionalizingEntities");
		}

		return entities;
	}

//	public List<ComposingElementBean> getComposingElementsByNanomaterialEntityId(
//			String id) throws Exception {	
//		WebContext wctx = WebContextFactory.get();
//		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
//		if (user == null) {
//			return null;
//		}
//		if (StringUtils.isEmpty(id)) {
//			return null;
//		}
//		CompositionService compService = new CompositionServiceLocalImpl();
//		NanomaterialEntityBean entityBean = compService
//				.findNanomaterialEntityById(id, user);
//		List<ComposingElementBean> composingElements = entityBean
//				.getComposingElements();
//		return composingElements;
//	}
}
