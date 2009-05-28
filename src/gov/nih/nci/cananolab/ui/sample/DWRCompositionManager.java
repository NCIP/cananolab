package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
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
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.util.DataLinkBean;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

/**
 * Work with DWR to set up drop-downs required in the composition pages
 *
 * @author pansu, cais
 *
 */
public class DWRCompositionManager {
	private FileServiceHelper fileHelper = new FileServiceHelper();
	private CompositionServiceHelper compHelper = new CompositionServiceHelper();

	public DWRCompositionManager() {
	}

	public String getEntityIncludePage(String entityType, String parent)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			ServletContext appContext = wctx.getServletContext();
			String includePage = InitCompositionSetup.getInstance()
					.getDetailPage(appContext, entityType, parent);
			String content = wctx.forwardToString(includePage);
			return content;
		} catch (Exception e) {
			return "";
		}
	}

	public FileBean getFileById(String type, String id) throws Exception {
		File file = fileHelper.findFileById(id);
		FileBean fileBean = new FileBean(file);
		FileService service = new FileServiceLocalImpl();
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		service.retrieveVisibility(fileBean, user);

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

	public FileBean resetTheFile(String type) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		FileBean fileBean = new FileBean();
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
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		List<BaseCompositionEntityBean> entities = null;

		if (compositionType.equals("Nanomaterial Entity")) {
			entities = (List<BaseCompositionEntityBean>) request.getSession()
					.getAttribute("sampleMaterialEntities");

		} else if (compositionType.equals("Functionalizing Entity")) {
			entities = (List<BaseCompositionEntityBean>) request.getSession()
					.getAttribute("sampleFunctionalizingEntities");
		}

		return entities;
	}

	public List<ComposingElementBean> getComposingElementsByNanomaterialEntityId(
			String id) throws Exception {
		if (id == null || id.length() == 0) {
			return null;
		}
		NanomaterialEntity entity = compHelper.findNanomaterialEntityById(id);
		NanomaterialEntityBean entityBean = new NanomaterialEntityBean(entity);
		List<ComposingElementBean> composingElements = entityBean
				.getComposingElements();
		return composingElements;
	}
}
