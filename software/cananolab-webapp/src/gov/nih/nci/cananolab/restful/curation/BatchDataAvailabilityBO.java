package gov.nih.nci.cananolab.restful.curation;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.restful.core.AbstractDispatchBO;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.BatchDataAvailabilityProcess;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.form.GenerateBatchDataAvailabilityForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class BatchDataAvailabilityBO extends AbstractDispatchBO {
	private DataAvailabilityService dataAvailabilityService;
	private static final int CUT_OFF_NUM_SAMPLES = 30;

	public DataAvailabilityService getDataAvailabilityService() {
		return dataAvailabilityService;
	}

	public void setDataAvailabilityService(
			DataAvailabilityService dataAvailabilityService) {
		this.dataAvailabilityService = dataAvailabilityService;
	}

	public void setupNew(GenerateBatchDataAvailabilityForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	//	DynaActionForm theForm = (DynaActionForm) form;
		form.setOption(BatchDataAvailabilityProcess.BATCH_OPTION1);
	//	return mapping.findForward("input");
	}

	// option1 - generate all: update existing one and generate new ones.
	// option2 - re-generate for samples with existing data availability
	// option3 - delete existing data availability
	public List<String> generate(GenerateBatchDataAvailabilityForm form,
			HttpServletRequest request)
			throws Exception {
	//	DynaActionForm theForm = (DynaActionForm) form;
		List<String> messages = new ArrayList<String>();
		String option = form.getOption();
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		SecurityService securityService = new SecurityService(
				AccessibilityBean.CSM_APP_NAME, user);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);

		List<String> sampleIdsToRun = new ArrayList<String>();
		if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION1)) {
			// find all sampleIds
			sampleIdsToRun = sampleService.findSampleIdsBy("", "", null, null,
					null, null, null, null, null, null, null);
		} else {
			// find samplesIds with existing data availability
			sampleIdsToRun = dataAvailabilityService
					.findSampleIdsWithDataAvailability(securityService);
		}
		// empty sampleIds
		if (sampleIdsToRun.isEmpty()) {
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION2)
					|| option
							.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
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
					|| option
							.equals(BatchDataAvailabilityProcess.BATCH_OPTION2)) {
				failures = dataAvailabilityService.saveBatchDataAvailability(
						sampleIdsToRun, securityService);
//				ActionMessage msg = new ActionMessage(
//						"message.batchDataAvailability.generate.success",
//						sampleIdsToRun.size());
			//	messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.generate.success")+":"+sampleIdsToRun.size());
				messages.add("You've successfully generated data availability metrics for "+sampleIdsToRun.size()+" sample(s).");
			}
			if (option.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
				failures = dataAvailabilityService.deleteBatchDataAvailability(
						sampleIdsToRun, securityService);
//				ActionMessage msg = new ActionMessage(
//						"message.batchDataAvailability.delete.success",
//						sampleIdsToRun.size() - failures);
				//messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.delete.success"));
				messages.add("You've successfully deleted data availability metrics for "+sampleIdsToRun.size() +"-"+failures +" sample(s).");
			}
			if (failures > 0) {
//				ActionMessage msg = new ActionMessage(
//						"message.batchDataAvailability.failed", failures);
			//	messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.failed"));
				messages.add(failures +"samples failed to be processed due to an internal error.");
			}

		//	saveMessages(request, messages);
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
			this.startThreadForBatchProcess(batchProcess, session,
					sampleIdsToRun, dataAvailabilityService, securityService,
					option, user);
		} else {
			if (!batchProcess.isComplete()) {
//				ActionMessage msg = new ActionMessage(
//						"message.batchDataAvailability.duplicateRequest");
				messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.duplicateRequest"));
			//	saveMessages(request, messages);
			//	return mapping.findForward("input");
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
	//	messages.add(PropertyUtil.getProperty("application", "message.batchDataAvailability.longRunning"));
		messages.add("It will take a while to "+ optionMessage +" data availability metrics for "+ sampleIdsToRun
				.size() +" samples.  Please click on the RESULTS tab later to check the progress.");
	//	saveMessages(request, messages);
	//	return mapping.findForward("input");
		return messages;

	}

	private void startThreadForBatchProcess(
			BatchDataAvailabilityProcess batchProcess, HttpSession session,
			List<String> sampleIdsToRun,
			DataAvailabilityService dataAvailabilityService,
			SecurityService securityService, String option, UserBean user)
			throws Exception {
		session.setAttribute("hasResultsWaiting", true);
		batchProcess = new BatchDataAvailabilityProcess(
				dataAvailabilityService, securityService, sampleIdsToRun,
				option, user);
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
