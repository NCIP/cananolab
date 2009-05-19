package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;

import java.io.IOException;
import java.util.List;

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

	public FileBean getFileFromList(String type, int index) {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		List<FileBean> files = null;
		if (type.equals("nanomaterial entity")) {
			NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
					.get("nanomaterialEntity");
			files = entity.getFiles();
		} else if (type.equals("functionalizing entity")) {
			FunctionalizingEntityBean entity = (FunctionalizingEntityBean) compositionForm
					.get("functionalizingEntity");
			files = entity.getFiles();
		} else {
			ChemicalAssociationBean assoc = (ChemicalAssociationBean) compositionForm
					.get("assoc");
			files = assoc.getFiles();
		}
		FileBean theFile = files.get(index);
		return theFile;
	}

	public ComposingElementBean addInherentFunction(
			ComposingElementBean composingElementBean, FunctionBean function) {
		composingElementBean.addFunction(function);
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		entity.setTheComposingElement(composingElementBean);
		return composingElementBean;
	}

	public ComposingElementBean deleteInherentFunction(
			ComposingElementBean composingElementBean, FunctionBean function) {
		composingElementBean.removeFunction(function);
		return composingElementBean;
	}

	public ComposingElementBean getComposingElementFromList(int index)
			throws Exception {
		DynaValidatorForm compositionForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("compositionForm"));
		NanomaterialEntityBean entity = (NanomaterialEntityBean) compositionForm
				.get("nanomaterialEntity");
		List<ComposingElementBean> composingElements = entity
				.getComposingElements();
		return composingElements.get(index);
	}
}
