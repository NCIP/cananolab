package gov.nih.nci.calab.ui.sample;

/**
 * This class searches samples based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.4 2007-11-16 22:30:15 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.dto.sample.StorageLocation;
import gov.nih.nci.calab.service.sample.SearchSampleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchSampleAction extends AbstractDispatchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();

		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleName = (String) theForm.get("sampleName");
		String sampleType = (String) theForm.get("sampleType");
		String sampleSource = (String) theForm.get("sampleSource");
		String sourceSampleId = (String) theForm.get("sourceSampleId");
		String dateAccessionedBeginStr = (String) theForm
				.get("dateAccessionedBegin");
		String dateAccessionedEndStr = (String) theForm
				.get("dateAccessionedEnd");

		Date dateAccessionedBegin = dateAccessionedBeginStr.length() == 0 ? null
				: StringUtils.convertToDate(dateAccessionedBeginStr,
						CaNanoLabConstants.ACCEPT_DATE_FORMAT);
		Date dateAccessionedEnd = dateAccessionedEndStr.length() == 0 ? null
				: StringUtils.convertToDate(dateAccessionedEndStr,
						CaNanoLabConstants.ACCEPT_DATE_FORMAT);
		String sampleSubmitter = (String) theForm.get("sampleSubmitter");
		StorageLocation storageLocation = (StorageLocation) theForm
				.get("storageLocation");

		// pass the parameters to the searchSampleService
		SearchSampleService searchSampleService = new SearchSampleService();

		List<SampleBean> samples = null;
		if (sampleName.length() >= 0) {
			if (sampleName.equals("all")) {
				sampleName = "";
			}
			samples = searchSampleService.searchSamplesBySampleName(sampleName,
					sampleType, sampleSource, sourceSampleId,
					dateAccessionedBegin, dateAccessionedEnd, sampleSubmitter,
					storageLocation);
		} else {
			samples = searchSampleService.searchSamples(sampleType,
					sampleSource, sourceSampleId, dateAccessionedBegin,
					dateAccessionedEnd, sampleSubmitter, storageLocation);
		}
		if (samples == null || samples.isEmpty()) {
			ActionMessage msg = new ActionMessage(
					"message.searchSample.noResult");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		} else {
			// create a list of ContainerBeans for use in display tag
			List<ContainerBean> containers = new ArrayList<ContainerBean>();
			for (SampleBean sample : samples) {
				containers.addAll(sample.getContainers());
			}
			session.setAttribute("sampleContainers", containers);
			forward = mapping.findForward("success");
		}

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSecuritySetup.getInstance().setAllUsers(session);
		InitSampleSetup.getInstance().setAllSampleContainerInfo(session);
		InitSampleSetup.getInstance().setAllSampleSources(session);
		InitSampleSetup.getInstance().setAllSourceSampleIds(session);

		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_SAMPLE);
	}
}
