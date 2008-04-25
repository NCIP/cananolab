package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class BaseCharacterizationAction extends BaseAnnotationAction {

	public ActionForward addDerivedBioAssayData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.addDerivedBioAssayData();
		return mapping.getInputForward();
	}

	public ActionForward removeDerivedBioAssayData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		String fileIndexStr = (String) request.getParameter("compInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.removeDerivedBioAssayData(fileInd);
		return mapping.getInputForward();
	}

	public ActionForward addDerivedDatum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("compInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		DerivedBioAssayDataBean derivedBioAssayData=achar.getDerivedBioAssayDataList().get(fileInd);
		derivedBioAssayData.addDerivedDatum();
		return mapping.getInputForward();
	}

	public ActionForward removeDerivedDatum(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// if user pressed cancel in load characterization file
		String cancel = request.getParameter("cancel");
		if (cancel != null) {
			return mapping.getInputForward();
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("compInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		String dataIndexStr = (String) request.getParameter("childCompInd");
		int dataInd = Integer.parseInt(dataIndexStr);
		DerivedBioAssayDataBean derivedBioAssayData=achar.getDerivedBioAssayDataList().get(fileInd);
		derivedBioAssayData.removeDerivedDatum(dataInd);
		return mapping.getInputForward();
		// return mapping.getInputForward(); this gives an
		// IndexOutOfBoundException in the jsp page
	}
}
