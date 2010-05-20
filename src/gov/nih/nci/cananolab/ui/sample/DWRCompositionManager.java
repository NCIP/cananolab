package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.BaseCompositionEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Work with DWR to set up drop-downs required in the composition pages
 *
 * @author pansu, cais
 *
 */
public class DWRCompositionManager {
	private CompositionServiceLocalImpl compService;

	private CompositionServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		compService = new CompositionServiceLocalImpl(user);
		return compService;
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

	public FileBean getFileById(String type, String id) throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		FileService fileService = ((CompositionServiceLocalImpl) getService())
				.getFileService();
		FileBean fileBean = fileService.findFileById(id);
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (type.equals("nanomaterialEntity")) {
			NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
					.get("nanomaterialEntity");
			entity.setTheFile(fileBean);
		} else if (type.equals("functionalizingEntity")) {
			FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
					.get("functionalizingEntity");
			entity.setTheFile(fileBean);
		} else if (type.equals("comp")) {
			CompositionBean comp = (CompositionBean) compositionForm
					.get("comp");
			comp.setTheFile(fileBean);
		} else {
			ChemicalAssociationBean assoc = (ChemicalAssociationBean) compositionForm
					.get("assoc");
			assoc.setTheFile(fileBean);
		}
		return fileBean;
	}

	public FileBean resetTheFile(String type) throws Exception {
		WebContext wctx = WebContextFactory.get();

		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		if (compositionForm == null) {
			return null;
		}
		FileBean fileBean = new FileBean();
		// set file default visibilities
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		// get assigned visible groups for samples
		String sampleName = (String) wctx.getSession().getAttribute(
				"sampleName");
		if (sampleName == null) {
			return null;
		}
		String[] visibilityGroups = authService.getAccessibleGroups(sampleName,
				Constants.CSM_READ_PRIVILEGE);
		fileBean.setVisibilityGroups(visibilityGroups);

		if (type.equals("nanomaterialEntity")) {
			NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
					.get("nanomaterialEntity");
			entity.setTheFile(fileBean);
		} else if (type.equals("functionalizingEntity")) {
			FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
					.get("functionalizingEntity");
			entity.setTheFile(fileBean);
		} else if (type.equals("comp")) {
			CompositionBean comp = (CompositionBean) compositionForm
					.get("comp");
			comp.setTheFile(fileBean);
		} else {
			ChemicalAssociationBean assoc = (ChemicalAssociationBean) compositionForm
					.get("assoc");
			assoc.setTheFile(fileBean);
		}
		return fileBean;
	}

	public List<BaseCompositionEntityBean> getAssociatedElementOptions(
			String compositionType) {
		if (StringUtils.isEmpty(compositionType)) {
			return new ArrayList<BaseCompositionEntityBean>();
		}
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		List<BaseCompositionEntityBean> entities = null;
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
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

	public List<ComposingElementBean> getComposingElementsByNanomaterialEntityId(
			String id) throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		NanomaterialEntityBean entityBean = getService()
				.findNanomaterialEntityById(id);
		List<ComposingElementBean> composingElements = entityBean
				.getComposingElements();
		return composingElements;
	}
}
