package gov.nih.nci.calab.ui.search;

/**
 * This class searches samples based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.1 2006-03-28 23:03:44 pansu Exp $ */

import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.service.search.SearchSampleService;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchSampleAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(SearchSampleAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		HttpSession session = request.getSession();
		try {
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			String sampleId = (String) theForm.get("sampleId");
			String aliquotId = (String) theForm.get("aliquotId");
			String sampleType = (String) theForm.get("sampleType");
			String sampleSource = (String) theForm.get("sampleSource");
			String sourceSampleId = (String) theForm.get("sourceSampleId");
			String dateAccessionedBeginStr = (String) theForm
					.get("dateAccessionedBegin");
			String dateAccessionedEndStr = (String) theForm
					.get("dateAccessionedEnd");

			Date dateAccessionedBegin = dateAccessionedBeginStr.length() == 0 ? null
					: StringUtils.convertToDate(dateAccessionedBeginStr,
							"MM/dd/yyyy");
			Date dateAccessionedEnd = dateAccessionedEndStr.length() == 0 ? null
					: StringUtils.convertToDate(dateAccessionedEndStr,
							"MM/dd/yyyy");
			String sampleSubmitter = (String) theForm.get("sampleSubmitter");
			StorageLocation storageLocation = (StorageLocation) theForm
					.get("storageLocation");

			// pass the parameters to the searchSampleService
			SearchSampleService searchSampleService = new SearchSampleService();
			// search base on aliquotId if aliquotId is present
			// otherwise search base sampleId
			List<SampleBean> samples = null;
			if (sampleId.length() == 0 && aliquotId.length() == 0) {
				samples = searchSampleService.searchSamples(sampleType,
						sampleSource, sourceSampleId, dateAccessionedBegin,
						dateAccessionedEnd, sampleSubmitter, storageLocation);
			}
			else if (aliquotId.length() > 0) {
				samples = searchSampleService.searchSamplesByAliquotId(
						aliquotId, sampleType, sampleSource, sourceSampleId,
						dateAccessionedBegin, dateAccessionedEnd,
						sampleSubmitter, storageLocation);
			} else if (sampleId.length() >= 0) {
				samples = searchSampleService.searchSamplesBySampleId(sampleId,
						sampleType, sampleSource, sourceSampleId,
						dateAccessionedBegin, dateAccessionedEnd,
						sampleSubmitter, storageLocation);
			}

			if (samples == null || samples.isEmpty()) {
				ActionMessage msg = new ActionMessage(
						"message.searchSample.noResult");
				msgs.add("message", msg);
				saveMessages(request, msgs);
				forward = mapping.getInputForward();
			} else {
				session.setAttribute("samples", samples);
				forward = mapping.findForward("success");
			}
		} catch (Exception e) {
			ActionMessage error = new ActionMessage("error.searchSample");
			msgs.add("error", error);
			saveMessages(request, msgs);
			logger.error("Caught exception searching sample container data", e);
			forward = mapping.getInputForward();
		}
		return forward;
	}

	public boolean loginRequired() {
		// temporarily set to false until login module is working
		return false;
		// return true;
	}

}
