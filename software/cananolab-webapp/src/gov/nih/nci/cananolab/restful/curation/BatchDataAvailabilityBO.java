package gov.nih.nci.cananolab.restful.curation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.restful.core.AbstractDispatchBO;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.BatchDataAvailabilityProcess;
import gov.nih.nci.cananolab.ui.form.GenerateBatchDataAvailabilityForm;

@Component("batchDataAvailabilityBO")
public class BatchDataAvailabilityBO extends AbstractDispatchBO
{
	@Autowired
	private DataAvailabilityService dataAvailabilityServiceDAO;
	
	@Autowired
	private SampleService sampleService;
	
	private static final int CUT_OFF_NUM_SAMPLES = 30;

	public void setupNew(GenerateBatchDataAvailabilityForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		form.setOption(BatchDataAvailabilityProcess.BATCH_OPTION1);
	}

	// option1 - generate all: update existing one and generate new ones.
	// option2 - re-generate for samples with existing data availability
	// option3 - delete existing data availability
	public List<String> generate(GenerateBatchDataAvailabilityForm form, HttpServletRequest request) throws Exception
	{
		List<String> messages = new ArrayList<String>();
		String option = form.getOption();
		HttpSession session = request.getSession();

		List<String> sampleIdsToRun = new ArrayList<String>();
		if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION1)) {
			// find all sampleIds
			sampleIdsToRun = sampleService.findSampleIdsBy("", "", null, null,
					null, null, null, null, null, null, null);
		} else {
			// find samplesIds with existing data availability
			sampleIdsToRun = dataAvailabilityServiceDAO.findSampleIdsWithDataAvailability();
		}
		// empty sampleIds
		if (sampleIdsToRun.isEmpty()) {
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION2)
					|| option.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
				messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.empty.metrics"));
			} else {
				
				messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.nosamples"));
			}
			//return mapping.findForward("input");
			return messages;
		}
		// check how many samples to run, if less than the CUT_OFF_NUM_SAMPLES,
		// we don't have to run batch in a separate thread
		if (sampleIdsToRun.size() < CUT_OFF_NUM_SAMPLES) {
			int failures = 0;
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION1)
					|| option.equals(BatchDataAvailabilityProcess.BATCH_OPTION2)) {
				failures = dataAvailabilityServiceDAO.saveBatchDataAvailability(sampleIdsToRun);

				messages.add("You've successfully generated data availability metrics for "+sampleIdsToRun.size()+" sample(s).");
			}
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
				failures = dataAvailabilityServiceDAO.deleteBatchDataAvailability(sampleIdsToRun);

				messages.add("You've successfully deleted data availability metrics for "+sampleIdsToRun.size() +"-"+failures +" sample(s).");
			}
			if (failures > 0) {
				messages.add(failures +" samples failed to be processed due to an internal error.");
			}

			//	return mapping.findForward("input");
			return messages;

		}

		// run in a separate thread
		//
		// We only want one BatchDataAvailabilityProcess per session.
		//
		BatchDataAvailabilityProcess batchProcess = (BatchDataAvailabilityProcess) session
				.getAttribute("BatchDataAvailabilityProcess");
		if (batchProcess == null) {
			this.startThreadForBatchProcess(batchProcess, session, sampleIdsToRun, dataAvailabilityServiceDAO, option);
		} else {
			if (!batchProcess.isComplete()) {

				messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.duplicateRequest"));
			//	saveMessages(request, messages);
			//	return mapping.findForward("input");
				return messages;
			} else {
			//	ActionMessage msg = new ActionMessage(
			//			"message.batchDataAvailability.previousRequest.completed");
				messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.previousRequest.completed"));
			//	saveMessages(request, messages);
			//	return mapping.findForward("input");
				return messages;

			}
		}
		String optionMessage = "generate";
		if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
			optionMessage = "delete";
		}
	//	ActionMessage msg = new ActionMessage(
	//			"message.batchDataAvailability.longRunning", sampleIdsToRun
	//					.size(), optionMessage);
		messages.add("It will take a while to "+ optionMessage +" data availability metrics for "+ sampleIdsToRun
				.size() +" samples.  Please click on the RESULTS tab later to check the progress.");
	//	saveMessages(request, messages);
	//	return mapping.findForward("input");
		return messages;

	}

	private void startThreadForBatchProcess(
			BatchDataAvailabilityProcess batchProcess, HttpSession session,
			List<String> sampleIdsToRun,
			DataAvailabilityService dataAvailabilityService, String option)
			throws Exception {
		session.setAttribute("hasResultsWaiting", true);
		batchProcess = new BatchDataAvailabilityProcess(
				dataAvailabilityService, sampleIdsToRun,
				option);
		batchProcess.process();
		session.setAttribute("BatchDataAvailabilityProcess", batchProcess);
		// obtain the list of long running processes
		List<LongRunningProcess> longRunningProcesses = new ArrayList<LongRunningProcess>();
		if (session.getAttribute("longRunningProcesses") != null) {
			longRunningProcesses = (List<LongRunningProcess>) session
					.getAttribute("longRunningProcesses");
		}
		longRunningProcesses.add(batchProcess);
		session.setAttribute("longRunningProcesses", longRunningProcesses);
	}
}
