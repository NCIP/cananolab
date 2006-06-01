package gov.nih.nci.calab.ui.search;

/**
 * This class searches samples based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.14 2006-06-01 15:37:53 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.service.search.SearchSampleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.ArrayList;
import java.util.Arrays;
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

public class SearchSampleAction extends AbstractBaseAction {
	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();

		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		// search base on aliquotName if aliquotName is present
		// otherwise search base sampleName
		boolean isAliquot = ((String) theForm.get("isAliquot")).equals("true") ? true
				: false;
		String searchName = (String) theForm.get("searchName");
		String sampleName = "";
		String aliquotName = "";
		if (isAliquot) {
			aliquotName = searchName;
		} else {
			sampleName = searchName;
		}
		String sampleType = (String) theForm.get("sampleType");
		String sampleSource = (String) theForm.get("sampleSource");
		String sourceSampleId = (String) theForm.get("sourceSampleId");
		String dateAccessionedBeginStr = (String) theForm
				.get("dateAccessionedBegin");
		String dateAccessionedEndStr = (String) theForm
				.get("dateAccessionedEnd");

		Date dateAccessionedBegin = dateAccessionedBeginStr.length() == 0 ? null
				: StringUtils.convertToDate(dateAccessionedBeginStr,
						CalabConstants.ACCEPT_DATE_FORMAT);
		Date dateAccessionedEnd = dateAccessionedEndStr.length() == 0 ? null
				: StringUtils.convertToDate(dateAccessionedEndStr,
						CalabConstants.ACCEPT_DATE_FORMAT);
		String sampleSubmitter = (String) theForm.get("sampleSubmitter");
		StorageLocation storageLocation = (StorageLocation) theForm
				.get("storageLocation");

		// pass the parameters to the searchSampleService
		SearchSampleService searchSampleService = new SearchSampleService();

		List<SampleBean> samples = null;
		List<AliquotBean> aliquots = null;

		// search aliquot
		if (isAliquot) {
			if (aliquotName.equals("all")) {
				aliquotName = "";
			}
			aliquots = searchSampleService.searchAliquotsByAliquotName(
					aliquotName, sampleType, sampleSource, sourceSampleId,
					dateAccessionedBegin, dateAccessionedEnd, sampleSubmitter,
					storageLocation);
			if (aliquots == null || aliquots.isEmpty()) {
				ActionMessage msg = new ActionMessage(
						"message.searchSample.noResult");
				msgs.add("message", msg);
				saveMessages(request, msgs);
			} 
			session.setAttribute("aliquots", aliquots);
			forward = mapping.findForward("successAliquot");
		}
		// search sample
		else {
			if (sampleName.length() >= 0) {
				if (sampleName.equals("all")) {
					sampleName = "";
				}
				samples = searchSampleService.searchSamplesBySampleName(
						sampleName, sampleType, sampleSource, sourceSampleId,
						dateAccessionedBegin, dateAccessionedEnd,
						sampleSubmitter, storageLocation);
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
					containers.addAll(Arrays.asList(sample.getContainers()));
				}
				session.setAttribute("sampleContainers", containers);
				forward = mapping.findForward("success");
			}
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
