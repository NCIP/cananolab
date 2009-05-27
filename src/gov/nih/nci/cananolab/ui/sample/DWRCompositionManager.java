package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

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
	private FileServiceHelper helper = new FileServiceHelper();

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
		File file = helper.findFileById(id);
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
}
