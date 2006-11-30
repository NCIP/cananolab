package gov.nih.nci.calab.ui.submit;

/**
 * This class uploads a report file and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: SubmitReportAction.java,v 1.4 2006-11-30 20:26:54 chand Exp $ */

import java.io.File;
import java.io.FileInputStream;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.MolecularWeight;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.physical.MolecularWeightBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitReportService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitReportAction extends BaseCharacterizationAction {
	private static Logger logger = Logger.getLogger(SubmitReportAction.class);

	public ActionForward submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String[] particleNames = (String[]) theForm.get("particleNames");
		String[] visibilities = (String[]) theForm.get("visibilities");
		FormFile reportFile = (FormFile) theForm.get("reportFile");
		String title=(String)theForm.get("title");
		String description=(String)theForm.get("description");
		String reportType = (String) theForm.get("reportType");
		String comment = (String) theForm.get("comment");
		
		SubmitReportService service=new SubmitReportService();
		
		service.submit(particleNames, reportType, reportFile, title, description, comment, visibilities);
				
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("message.submitReport.secure",
				StringUtils.join(visibilities, ", "));
		ActionMessage msg2 = new ActionMessage("message.submitReport.file",
				reportFile.getFileName());
		msgs.add("message", msg1);
		msgs.add("message", msg2);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setAllSampleContainers(session);
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
	
	protected void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
	}

	protected void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
		throws Exception {
	}

	protected void setLoadFileRequest(HttpServletRequest request) {
	}

	protected void setFormCharacterizationBean(DynaValidatorForm theForm, Characterization aChar) throws Exception {
	}

}
