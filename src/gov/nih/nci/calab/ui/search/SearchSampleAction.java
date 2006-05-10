package gov.nih.nci.calab.ui.search;

/**
 * This class searches samples based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.10 2006-05-10 14:37:46 zengje Exp $ */

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.search.SearchSampleService;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

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

		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		// search base on aliquotName if aliquotName is present
		// otherwise search base sampleName
		boolean showAliquot = ((String) theForm.get("showAliquot"))
				.equals("true") ? true : false;
		String searchName = (String) theForm.get("searchName");
		String sampleName = "";
		String aliquotName = "";
		if (showAliquot) {
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
		
		if (!isValidDate(dateAccessionedBeginStr) || !isValidDate(dateAccessionedEndStr)) {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage msg = new ActionMessage("errors.date", "Sample accessioned date");
//			msgs.add("error", msg);
//			saveMessages(request, msgs);
//			
//			forward = mapping.findForward("input");
//
//			return forward;
			throw new CalabException("Year of sample accessionied date must be in a four digit format.");

		}

		request.setAttribute("showAliquot", showAliquot);

		Date dateAccessionedBegin = dateAccessionedBeginStr.length() == 0 ? null
				: StringUtils.convertToDate(dateAccessionedBeginStr,
						"MM/dd/yyyy");
		Date dateAccessionedEnd = dateAccessionedEndStr.length() == 0 ? null
				: StringUtils
						.convertToDate(dateAccessionedEndStr, "MM/dd/yyyy");
		String sampleSubmitter = (String) theForm.get("sampleSubmitter");
		StorageLocation storageLocation = (StorageLocation) theForm
				.get("storageLocation");

		// pass the parameters to the searchSampleService
		SearchSampleService searchSampleService = new SearchSampleService();

		List<SampleBean> samples = null;
		List<AliquotBean> aliquots = null;

		if (showAliquot) {
			if (aliquotName.equals("all")) {
				aliquotName = "";
			}
			aliquots = searchSampleService.searchAliquotsByAliquotName(
					aliquotName, sampleType, sampleSource, sourceSampleId,
					dateAccessionedBegin, dateAccessionedEnd, sampleSubmitter,
					storageLocation);
		} else if (sampleName.length() >= 0) {
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

		if (!showAliquot && (samples == null || samples.isEmpty())
				|| showAliquot && (aliquots == null || aliquots.isEmpty())) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchSample.noResult");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward = mapping.getInputForward();
		} else {
			if (!showAliquot) {
				session.setAttribute("samples", samples);
			} else {
				session.setAttribute("aliquots", aliquots);
			}
			forward = mapping.findForward("success");
		}
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
	
	private boolean isValidDate(String date) {
		if ((date == null) || (date.length()== 0)) { 
			return true;
		}
		String year = date.substring(date.lastIndexOf("/")+1);
		if (year.length()<4){
			return false;
		}
		return true;
	}

}
